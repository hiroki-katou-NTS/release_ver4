package nts.uk.pr.file.infra.paymentdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.uk.ctx.pr.proto.dom.allot.CompanyAllotSettingRepository;
import nts.uk.ctx.pr.proto.dom.allot.PersonalAllotSettingRepository;
import nts.uk.ctx.pr.proto.dom.itemmaster.ItemMaster;
import nts.uk.ctx.pr.proto.dom.itemmaster.ItemMasterRepository;
import nts.uk.ctx.pr.proto.dom.layout.LayoutMaster;
import nts.uk.ctx.pr.proto.dom.layout.LayoutMasterRepository;
import nts.uk.ctx.pr.proto.dom.layout.category.LayoutMasterCategory;
import nts.uk.ctx.pr.proto.dom.layout.category.LayoutMasterCategoryRepository;
import nts.uk.ctx.pr.proto.dom.layout.detail.LayoutMasterDetail;
import nts.uk.ctx.pr.proto.dom.layout.detail.LayoutMasterDetailRepository;
import nts.uk.ctx.pr.proto.dom.layout.line.LayoutMasterLine;
import nts.uk.ctx.pr.proto.dom.layout.line.LayoutMasterLineRepository;
import nts.uk.ctx.pr.proto.dom.layout.line.LineDispAtr;
import nts.uk.ctx.pr.proto.dom.paymentdata.Payment;
import nts.uk.ctx.pr.proto.dom.paymentdata.paymentdatemaster.PaymentDateProcessingMaster;
import nts.uk.ctx.pr.proto.dom.paymentdata.repository.PaymentDataRepository;
import nts.uk.ctx.pr.proto.dom.paymentdata.repository.PaymentDateProcessingMasterRepository;
import nts.uk.pr.file.infra.paymentdata.PaymentDataQuery;
import nts.uk.pr.file.infra.paymentdata.PaymentDataQueryRepository;
import nts.uk.pr.file.infra.paymentdata.result.DetailItemDto;
import nts.uk.pr.file.infra.paymentdata.result.LayoutMasterCategoryDto;
import nts.uk.pr.file.infra.paymentdata.result.LineDto;
import nts.uk.pr.file.infra.paymentdata.result.PaymentDataHeaderDto;
import nts.uk.pr.file.infra.paymentdata.result.PaymentDataResult;
import nts.uk.pr.file.infra.paymentdata.result.PrintPositionCategoryDto;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
public class GetPaymentDataQueryProcessor {

	private static final int PAY_BONUS_ATR = 0;

	/** 雇用区分マスタ]の処理日番号 */
	private static final int PROCESSING_NO = 1;

	/** PersonalPaymentSettingRepository */
	@Inject
	private PersonalAllotSettingRepository personalPSRepository;

	@Inject
	private CompanyAllotSettingRepository companyAllotSettingRepository;

	/** LayoutMasterRepository */
	@Inject
	private LayoutMasterRepository layoutMasterRepository;

	@Inject
	private LayoutMasterCategoryRepository layoutMasterCategoryRepository;

	@Inject
	private LayoutMasterLineRepository layoutMasterLineRepository;

	@Inject
	private LayoutMasterDetailRepository layoutMasterDetailRepository;

	@Inject
	private PaymentDateProcessingMasterRepository payDateMasterRepository;

	@Inject
	private PaymentDataRepository paymentDataRepository;

	@Inject
	private PaymentDataQueryRepository queryRepository;

	@Inject
	private ItemMasterRepository itemMasterRepository;

	/**
	 * get data detail
	 * 
	 * @param companyCode
	 *            code
	 * @return PaymentDataResult
	 */
	public PaymentDataResult find(PaymentDataQuery query) {
		PaymentDataResult result = new PaymentDataResult();
		String companyCode = AppContexts.user().companyCode();

		// get 処理年月マスタ
		PaymentDateProcessingMaster payDateMaster = this.payDateMasterRepository
				.find(companyCode, PAY_BONUS_ATR, PROCESSING_NO).get();
		int processingYM = payDateMaster.getCurrentProcessingYm().v();

		// get stmtCode
		String stmtCode = this.personalPSRepository.find(companyCode, query.getPersonId(), processingYM)
				.map(o -> o.getPaymentDetailCode().v()).orElseGet(() -> {
					return this.companyAllotSettingRepository.find(companyCode).get()
							.getPaymentDetailCode().v();
				});

		// get 明細書マスタ
		List<LayoutMaster> mLayouts = this.layoutMasterRepository.findAll(companyCode, stmtCode, processingYM);
		if (mLayouts.isEmpty()) {
			result.setPaymentHeader(new PaymentDataHeaderDto(query.getPersonId(), "", "", "", "", processingYM, null, "", "", query.getEmployeeCode(), "", false, null));
			return result;
		}

		LayoutMaster mLayout = mLayouts.get(0);
		int startYM = mLayout.getStartYM().v();

		// get 明細書マスタカテゴリ
		List<LayoutMasterCategory> mCates = this.layoutMasterCategoryRepository.getCategories(companyCode,
				mLayout.getStmtCode().v(), startYM);

		// get 明細書マスタ行
		List<LayoutMasterLine> mLines = this.layoutMasterLineRepository.getLines(companyCode, stmtCode, startYM);

		// get 明細書マスタ明細
		List<LayoutMasterDetail> mDetails = this.layoutMasterDetailRepository.findAll(companyCode, stmtCode, startYM);

		// get 項目マスタ
		List<ItemMaster> mItems = this.itemMasterRepository.findAll(companyCode);

		// get header of payment
		Optional<Payment> optPHeader = this.paymentDataRepository.find(companyCode, query.getPersonId(), PROCESSING_NO,
				PAY_BONUS_ATR, processingYM, 0);

		List<DetailItemDto> detailItems = getDetailItems(query, result, companyCode, processingYM, mLayout, optPHeader,
				mCates, mLines);

		result.setCategories(mergeDataToLayout(mCates, mLines, mDetails, detailItems, mItems));
		return result;
	}

	/**
	 * 項目明細を取得
	 * 
	 * @param query
	 * @param result
	 * @param companyCode
	 * @param payDateMaster
	 * @param layout
	 * @param optPHeader
	 * @return
	 */
	private List<DetailItemDto> getDetailItems(PaymentDataQuery query, PaymentDataResult result, String companyCode,
			int processingYM, LayoutMaster layout, Optional<Payment> optPHeader, List<LayoutMasterCategory> mCates,
			List<LayoutMasterLine> mLines) {

		List<PrintPositionCategoryDto> printPosCates = this.getPrintPosCategories(mCates, mLines);

		if (optPHeader.isPresent()) {
			Payment payment = optPHeader.get();
			result.setPaymentHeader(new PaymentDataHeaderDto(query.getPersonId(), payment.getCompanyName(), payment.getDepartmentCode(), payment.getDepartmentName(), payment.getPersonName().v() , processingYM,
					payment.getDependentNumber().v(), payment.getSpecificationCode().v(), layout.getStmtName().v(),
					query.getEmployeeCode(), payment.getComment().v(), true,
					printPosCates));

			return this.queryRepository.findAll(companyCode, query.getPersonId(), PAY_BONUS_ATR, processingYM);

		} else {
			result.setPaymentHeader(new PaymentDataHeaderDto(query.getPersonId(), "" , "", "", "", processingYM, 0,
					layout.getStmtCode().v(), layout.getStmtName().v(), query.getEmployeeCode(), "", false,
					printPosCates));

			return new ArrayList<>();
		}

	}

	/**
	 * merge data to layout master
	 * 
	 * @param mCates
	 * @param lines
	 * @param details
	 * @param datas
	 * @return
	 */
	private static List<LayoutMasterCategoryDto> mergeDataToLayout(List<LayoutMasterCategory> mCates,
			List<LayoutMasterLine> lines, List<LayoutMasterDetail> details, List<DetailItemDto> datas,
			List<ItemMaster> mItems) {

		List<LayoutMasterCategoryDto> categories = new ArrayList<>();

		mCates.sort((x, y) -> x.getCtgPos().compareTo(y.getCtgPos()));
		for (LayoutMasterCategory category : mCates) {
			int ctAtr = category.getCtAtr().value;
			String ctName = category.getCtAtr().toName();

			Long lineCounts = lines.stream().filter(x -> x.getCategoryAtr().value == ctAtr).count();
			if (lineCounts == 0) {
				continue;
			}
			List<LayoutMasterLine> fLines = lines.stream().filter(x -> x.getCategoryAtr().value == ctAtr)
					.collect(Collectors.toList());
			List<LineDto> lineItems = getDetailItems(fLines, details, datas, ctAtr, mItems);

			categories.add(LayoutMasterCategoryDto.fromDomain(ctAtr, ctName, category.getCtgPos().v(),
					lineCounts.intValue(), lineItems));
		}

		return categories;
	}

	/**
	 * Get item info by line
	 * 
	 * @param mLines
	 * @param mDetails
	 * @param datas
	 * @param ctAtr
	 * @return
	 */
	private static List<LineDto> getDetailItems(List<LayoutMasterLine> mLines, List<LayoutMasterDetail> mDetails,
			List<DetailItemDto> datas, int ctAtr, List<ItemMaster> mItems) {
		List<LineDto> rLines = new ArrayList<>();

		for (LayoutMasterLine mLine : mLines) {
			LineDto lineDto;
			List<DetailItemDto> items = new ArrayList<>();
			for (int i = 1; i <= 9; i++) {
				int posCol = i;
				DetailItemDto detailItem = mDetails.stream()
						.filter(x -> x.getCategoryAtr().value == ctAtr && 
								x.getAutoLineId().v().equals(mLine.getAutoLineId().v()) && 
								x.getItemPosColumn().v() == posCol)
						.findFirst().map(d -> {
							ItemMaster mItem = mItems.stream().filter(m -> m.getCategoryAtr().value == ctAtr
									&& m.getItemCode().v().equals(d.getItemCode().v())).findFirst().get();
							boolean isTaxtAtr = mItem.getTaxAtr().value == 3 || mItem.getTaxAtr().value == 4;
							Optional<DetailItemDto> dValue = datas.stream().filter(
									x -> x.getCategoryAtr() == ctAtr && x.getItemCode().equals(d.getItemCode().v()))
									.findFirst();
							DetailItemDto result;
							if (dValue.isPresent()) {
								DetailItemDto iValue = dValue.get();
								result= DetailItemDto.fromData(
										ctAtr, 
										mItem.getItemAtr().value, 
										d.getItemCode().v(),
										mItem.getItemAbName().v(),
										iValue.getValue(),
										d.getCalculationMethod().value,
										iValue.getCorrectFlag(),
										mLine.getLinePosition().v(), 
										d.getItemPosColumn().v(),
										mItem.getDeductAttribute().value, 
										d.getDisplayAtr().value, 
										mItem.getTaxAtr().value,
										iValue.getLimitAmount(),
										iValue.getCommuteAllowTaxImpose(),
										iValue.getCommuteAllowMonth(),
										iValue.getCommuteAllowFraction(),
										d.getSumScopeAtr().value,
										iValue.getValue() != null);
							} else {
								result =  DetailItemDto.fromData(
										ctAtr, 
										mItem.getItemAtr().value, 
										d.getItemCode().v(),
										mItem.getItemAbName().v(),
										isTaxtAtr ? 0.0: null,
									    d.getCalculationMethod().value,
										0,
										mLine.getLinePosition().v(), 
										d.getItemPosColumn().v(),
										mItem.getDeductAttribute().value, 
										d.getDisplayAtr().value, 
										mItem.getTaxAtr().value,
										null,
										0.0,
										0.0,
										0.0,
										d.getSumScopeAtr().value,
										false);
							}
							result.setItemType();
							return result;
						}).orElse(DetailItemDto.fromData(
								ctAtr, null, "", "", null, -1, 0, mLine.getLinePosition().v(), i, null, null, null, null, null, null, null, null, false));
				items.add(detailItem);
			}

			lineDto = new LineDto(mLine.getLinePosition().v(), items, mLine.getLineDispayAttribute().value);
			rLines.add(lineDto);
		}

		return rLines;
	}

	/**
	 * get list print post category(1~5)
	 * 
	 * @param mCates
	 * @param lines
	 * @return
	 */
	private List<PrintPositionCategoryDto> getPrintPosCategories(List<LayoutMasterCategory> mCates,
			List<LayoutMasterLine> lines) {
		List<PrintPositionCategoryDto> result = new ArrayList<>();

		for (int i = 1; i <= 4; i++) {
			PrintPositionCategoryDto printCates = this.getPrintPosition(mCates, lines, i);
			if (printCates!= null) {
				result.add(printCates);	
			}
		}

		return result;
	}

	/**
	 * get print post category
	 * 
	 * @param mCates
	 * @param lines
	 * @param position
	 * @return
	 */
	private PrintPositionCategoryDto getPrintPosition(List<LayoutMasterCategory> mCates, List<LayoutMasterLine> lines,
			int position) {
		Integer printPosCtg = mCates.stream().filter(x -> x.getCtgPos().v() == position).findFirst().map(x-> x.getCtAtr().value).orElse(null);
		if (printPosCtg == null) {
			return null;
		}
		Long countLine = lines.stream().filter(x -> x.getCategoryAtr().value == printPosCtg
				&& x.getLineDispayAttribute().value == LineDispAtr.ENABLE.value).count();

		return PrintPositionCategoryDto.fromDomain(printPosCtg, countLine.intValue());
	}

}
