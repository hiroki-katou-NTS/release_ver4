package nts.uk.ctx.pr.file.app.core.wageprovision.statementlayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.wageprovision.formula.FormulaRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.salaryindividualamountname.SalIndAmountNameRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.CategoryAtr;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.StatementItemRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.DeductionItemDetailSet;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.LineByLineSetting;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.PaymentItemDetailSet;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.SettingByCtg;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.SettingByItem;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.SettingByItemCustom;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.StatementLayout;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.StatementLayoutHist;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.StatementLayoutHistRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.StatementLayoutRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.StatementLayoutSet;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.StatementLayoutSetRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.wagetable.WageTableRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class StatementLayoutExportService extends ExportService<StatementLayoutExportQuery> {

	@Inject
	private StatementLayoutFileGenerator dtatementLayoutFileGenerator;

	@Inject
	private WageTableRepository wageTableRepo;

	@Inject
	private SalIndAmountNameRepository salIndAmountNameRepo;

	@Inject
	private FormulaRepository formulaRepo;

	@Inject
	private StatementItemRepository statementItemRepo;

	@Inject
	private StatementLayoutRepository statementLayoutRepo;

	@Inject
	private StatementLayoutHistRepository statementLayoutHistRepo;

	@Inject
	private StatementLayoutSetRepository statementLayoutSetRepo;

	@Override
	protected void handle(ExportServiceContext<StatementLayoutExportQuery> exportServiceContext) {
		String cid = AppContexts.user().companyId();
		StatementLayoutExportQuery cmd = exportServiceContext.getQuery();
		List<String> sttCodes = exportServiceContext.getQuery().getStatementCodes();
		int processingDate = cmd.getProcessingDate();
		List<StatementLayoutSetExportData> exportData = new ArrayList<StatementLayoutSetExportData>();

		// ドメインモデル「賃金テーブル」を取得する
		Map<String, String> wageTableMap = wageTableRepo.getAllWageTable(cid).stream()
				.collect(Collectors.toMap(x -> x.getWageTableCode().v(), x -> x.getWageTableName().v()));
		// ドメインモデル「給与個人別金額名称」を取得する
		Map<MapKey, String> salIndAmountNameMap = salIndAmountNameRepo.getSalIndAmountName(cid).stream()
				.collect(Collectors.toMap(x -> new MapKey(x.getIndividualPriceCode().v(), x.getCateIndicator().value),
						x -> x.getIndividualPriceName().v()));
		// ドメインモデル「計算式」を取得する
		Map<String, String> formulaMap = formulaRepo.getAllFormula().stream()
				.collect(Collectors.toMap(x -> x.getFormulaCode().v(), x -> x.getFormulaName().v()));
		Map<MapKey, String> statementItemMap = statementItemRepo.getItemCustomByDeprecated(cid, false).stream()
				.collect(Collectors.toMap(x -> new MapKey(x.getItemNameCd(), x.getCategoryAtr()), x -> x.getName()));

		// ドメインモデル「明細書レイアウト」を取得する
		Map<String, StatementLayout> statementLayoutMap = statementLayoutRepo
				.getAllStatementLayoutByCidAndCodes(cid, sttCodes).stream()
				.collect(Collectors.toMap(x -> x.getStatementCode().v(), x -> x));

		// ドメインモデル「明細書レイアウト履歴」を取得する
		// 取得した明細書レイアウト履歴から基準年月に準ずる履歴IDを取得する
		Map<String, StatementLayoutHist> statementLayoutHistMap = statementLayoutHistRepo
				.getLayoutHistByCidAndCodesAndYM(cid, sttCodes, processingDate).stream()
				.collect(Collectors.toMap(x -> x.getStatementCode().v(), x -> x));

		// 選択された明細書ごとに処理を行う
		for (String sttCode : sttCodes) {
			StatementLayout sttLayout;
			StatementLayoutHist sttLayoutHist;
			String histId = null;
			List<SettingByCtgExportData> listSettingByCtgEx = new ArrayList<SettingByCtgExportData>();

			// ドメインモデル「明細書レイアウト」を取得する
			if (!statementLayoutMap.containsKey(sttCode)) {
				continue;
			}
			sttLayout = statementLayoutMap.get(sttCode);
			// ドメインモデル「明細書レイアウト履歴」を取得する
			if (statementLayoutHistMap.containsKey(sttCode)) {
				sttLayoutHist = statementLayoutHistMap.get(sttCode);
				if (!sttLayoutHist.items().isEmpty()) {
					// 取得した明細書レイアウト履歴から基準年月に準ずる履歴IDを取得する
					histId = sttLayoutHist.items().get(0).identifier();
				}
			}
			if (histId == null)
				continue;
			// ドメインモデル「明細書レイアウト設定」を取得する
			Optional<StatementLayoutSet> sttLayoutSetOtp = statementLayoutSetRepo.getStatementLayoutSetById(histId);
			// 取得した給与項目IDごとに処理を実施する
			if (!sttLayoutSetOtp.isPresent())
				continue;
			StatementLayoutSet sttLayoutSet = sttLayoutSetOtp.get();
			for (SettingByCtg set : sttLayoutSet.getListSettingByCtg()) {
				SettingByCtgExportData setEx = new SettingByCtgExportData();
				// ドメインモデル「明細書項目名称」を取得する
				// ドメインモデル「明細書項目」を取得する
				switch (set.getCtgAtr()) {
				case PAYMENT_ITEM:
					// ドメインモデル「支給項目明細設定」を取得する
					// paymentItemDetailSetRepo.getPaymentItemDetailSetById(histId);
					break;
				case DEDUCTION_ITEM:
					// ドメインモデル「控除項目明細設定」を取得する
					// deductionItemDetailSetRepo.getDeductionItemDetailSetById(histId);
					break;
				default:
					break;
				}
				// ドメインモデル「明細書項目範囲設定」を取得する

				List<LineByLineSettingExportData> listLineByLineSetEx = this.mapLineSetting(set, wageTableMap,
						salIndAmountNameMap, formulaMap, statementItemMap);
				setEx.setCtgAtr(set.getCtgAtr());
				setEx.setListLineByLineSet(listLineByLineSetEx);
				listSettingByCtgEx.add(setEx);
			}

			StatementLayoutSetExportData data = new StatementLayoutSetExportData();
			data.setStatementCode(sttLayout.getStatementCode().v());
			data.setStatementName(sttLayout.getStatementName().v());
			data.setProcessingDate(new YearMonth(processingDate));
			data.setLayoutPattern(sttLayoutSet.getLayoutPattern());
			data.setListSettingByCtg(listSettingByCtgEx);
			exportData.add(data);
		}
		dtatementLayoutFileGenerator.generate(exportServiceContext.getGeneratorContext(), exportData);
	}

	private List<LineByLineSettingExportData> mapLineSetting(SettingByCtg set, Map<String, String> wageTableMap,
			Map<MapKey, String> salIndAmountNameMap, Map<String, String> formulaMap,
			Map<MapKey, String> statementItemMap) {
		List<LineByLineSettingExportData> listLineByLineSetEx = new ArrayList<LineByLineSettingExportData>();
		for (LineByLineSetting line : set.getListLineByLineSet()) {
			LineByLineSettingExportData lineEx = new LineByLineSettingExportData();
			List<SettingByItemExportData> listSetByItemEx = this.mapItemSetting(line, set.getCtgAtr(), wageTableMap,
					salIndAmountNameMap, formulaMap, statementItemMap);
			lineEx.setPrintSet(line.getPrintSet());
			lineEx.setLineNumber(line.getLineNumber());
			lineEx.setListSetByItem(listSetByItemEx);
			listLineByLineSetEx.add(lineEx);
		}

		return listLineByLineSetEx;
	}

	private List<SettingByItemExportData> mapItemSetting(LineByLineSetting line, CategoryAtr ctgAtr,
			Map<String, String> wageTableMap, Map<MapKey, String> salIndAmountNameMap, Map<String, String> formulaMap,
			Map<MapKey, String> statementItemMap) {
		List<SettingByItemExportData> listSetByItemEx = new ArrayList<SettingByItemExportData>();
		for (SettingByItem item : line.getListSetByItem()) {
			SettingByItemExportData itemEx = new SettingByItemExportData();
			itemEx.setItemPosition(item.getItemPosition());
			itemEx.setItemId(item.getItemId());
			if (item instanceof SettingByItemCustom) {
				SettingByItemCustom itemCustom = (SettingByItemCustom) item;
				itemEx.setItemName(itemCustom.getShortName());
				switch (ctgAtr) {
				case PAYMENT_ITEM:
					this.mapPayment(itemEx, itemCustom, wageTableMap, salIndAmountNameMap, formulaMap);
					break;
				case DEDUCTION_ITEM:
					this.mapDeduction(itemEx, itemCustom, wageTableMap, salIndAmountNameMap, formulaMap,
							statementItemMap);
					break;
				case ATTEND_ITEM:
					this.mapAttend(itemEx, itemCustom);
					break;
				default:
					break;
				}
			}
			listSetByItemEx.add(itemEx);
		}
		return listSetByItemEx;
	}

	private void mapPayment(SettingByItemExportData itemEx, SettingByItemCustom itemCustom,
			Map<String, String> wageTableMap, Map<MapKey, String> salIndAmountNameMap, Map<String, String> formulaMap) {
		PaymentExportData paymentEx = new PaymentExportData();
		Optional<PaymentItemDetailSet> paymentItemDetailOtp = itemCustom.getPaymentItemDetailSet();
		if (paymentItemDetailOtp.isPresent()) {
			PaymentItemDetailSet paymentItemDetail = paymentItemDetailOtp.get();
			paymentEx.setTotalObj(paymentItemDetail.getTotalObj());
			paymentEx.setCalcMethod(paymentItemDetail.getCalcMethod());
			paymentEx.setProportionalAtr(paymentItemDetail.getProportionalAtr());
			ItemRangeSetExportData itemRangeSet = new ItemRangeSetExportData();
			paymentEx.setItemRangeSet(itemRangeSet);

			switch (paymentItemDetail.getCalcMethod()) {
			case CACL_FOMULA:
				if (paymentItemDetail.getCalcFomulaCd().isPresent()) {
					String formulaCd = paymentItemDetail.getCalcFomulaCd().get().v();
					paymentEx.setCalcFomulaCd(formulaCd);
					if (formulaMap.containsKey(formulaCd)) {
						paymentEx.setCalcFomulaName(formulaMap.get(formulaCd));
					}
				}
				break;
			case PERSON_INFO_REF:
				if (paymentItemDetail.getPersonAmountCd().isPresent()) {
					String personAmountCd = paymentItemDetail.getPersonAmountCd().get().v();
					paymentEx.setPersonAmountCd(personAmountCd);
					MapKey key = new MapKey(personAmountCd, CategoryAtr.PAYMENT_ITEM.value);
					if (salIndAmountNameMap.containsKey(key)) {
						paymentEx.setPersonAmountName(salIndAmountNameMap.get(key));
					}
				}
				break;
			case WAGE_TABLE:
				if (paymentItemDetail.getWageTblCode().isPresent()) {
					String wageTblCode = paymentItemDetail.getWageTblCode().get().v();
					paymentEx.setWageTblCode(wageTblCode);
					if (wageTableMap.containsKey(wageTblCode)) {
						paymentEx.setWageTblName(wageTableMap.get(wageTblCode));
					}
				}
				break;
			case COMMON_AMOUNT:
				if (paymentItemDetail.getCommonAmount().isPresent()) {
					Long commonAmount = paymentItemDetail.getCommonAmount().get().v();
					paymentEx.setCommonAmount(formatCurrency(commonAmount));
				}
			default:
				break;
			}
			itemEx.setPayment(paymentEx);
		}
	}

	private void mapDeduction(SettingByItemExportData itemEx, SettingByItemCustom itemCustom,
			Map<String, String> wageTableMap, Map<MapKey, String> salIndAmountNameMap, Map<String, String> formulaMap,
			Map<MapKey, String> statementItemMap) {
		DeductionExportData deductionEx = new DeductionExportData();
		Optional<DeductionItemDetailSet> deductionitemDetailOtp = itemCustom.getDeductionItemDetailSet();
		if (deductionitemDetailOtp.isPresent()) {
			DeductionItemDetailSet deductionItemDetail = deductionitemDetailOtp.get();
			deductionEx.setTotalObj(deductionItemDetail.getTotalObj());
			deductionEx.setCalcMethod(deductionItemDetail.getCalcMethod());
			deductionEx.setProportionalAtr(deductionItemDetail.getProportionalAtr());
			ItemRangeSetExportData itemRangeSet = new ItemRangeSetExportData();
			deductionEx.setItemRangeSet(itemRangeSet);

			switch (deductionItemDetail.getCalcMethod()) {
			case CACL_FOMULA:
				if (deductionItemDetail.getCalcFormulaCd().isPresent()) {
					String formulaCd = deductionItemDetail.getCalcFormulaCd().get().v();
					deductionEx.setCalcFomulaCd(formulaCd);
					if (formulaMap.containsKey(formulaCd)) {
						deductionEx.setCalcFomulaName(formulaMap.get(formulaCd));
					}
				}
				break;
			case PERSON_INFO_REF:
				if (deductionItemDetail.getPersonAmountCd().isPresent()) {
					String personAmountCd = deductionItemDetail.getPersonAmountCd().get().v();
					deductionEx.setPersonAmountCd(personAmountCd);
					MapKey key = new MapKey(personAmountCd, CategoryAtr.DEDUCTION_ITEM.value);
					if (salIndAmountNameMap.containsKey(key)) {
						deductionEx.setPersonAmountName(salIndAmountNameMap.get(key));
					}
				}
				break;
			case WAGE_TABLE:
				if (deductionItemDetail.getWageTblCd().isPresent()) {
					String wageTblCode = deductionItemDetail.getWageTblCd().get().v();
					deductionEx.setWageTblCode(wageTblCode);
					if (wageTableMap.containsKey(wageTblCode)) {
						deductionEx.setWageTblName(wageTableMap.get(wageTblCode));
					}
				}
				break;
			case SUPPLY_OFFSET:
				if (deductionItemDetail.getSupplyOffset().isPresent()) {
					String sttCode = deductionItemDetail.getSupplyOffset().get();
					deductionEx.setSupplyOffset(sttCode);
					MapKey key = new MapKey(sttCode, CategoryAtr.DEDUCTION_ITEM.value);
					if (statementItemMap.containsKey(key)) {
						deductionEx.setSupplyOffsetName(statementItemMap.get(key));
					}
				}
				break;
			default:
				break;
			}
			itemEx.setDeduction(deductionEx);
		}
	}

	private void mapAttend(SettingByItemExportData itemEx, SettingByItemCustom itemCustom) {
		AttendExportData attendEx = new AttendExportData();
		ItemRangeSetExportData itemRangeSet = new ItemRangeSetExportData();
		attendEx.setItemRangeSet(itemRangeSet);
		itemEx.setAttend(attendEx);
	}

	List<String> getItemIds(SettingByCtg settingByCtg) {
		List<String> itemIds = new ArrayList<String>();
		for (LineByLineSetting line : settingByCtg.getListLineByLineSet()) {
			for (SettingByItem item : line.getListSetByItem()) {
				itemIds.add(item.getItemId());
			}
		}
		return itemIds;
	}

	private static String formatCurrency(Long number) {
		if (number == null)
			return "";
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.JAPAN);
		return formatter.format(number);
	}
}
