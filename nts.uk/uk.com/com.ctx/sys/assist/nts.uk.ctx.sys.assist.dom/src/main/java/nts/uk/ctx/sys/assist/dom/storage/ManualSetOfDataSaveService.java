/**
 * 
 */
package nts.uk.ctx.sys.assist.dom.storage;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;

import com.google.common.base.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.layer.infra.file.temp.ApplicationTemporaryFileFactory;
import nts.arc.layer.infra.file.temp.ApplicationTemporaryFilesContainer;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.security.crypt.commonkey.CommonKeyCrypt;
import nts.uk.ctx.sys.assist.dom.category.Category;
import nts.uk.ctx.sys.assist.dom.category.CategoryRepository;
import nts.uk.ctx.sys.assist.dom.category.TimeStore;
import nts.uk.ctx.sys.assist.dom.categoryfieldmt.CategoryFieldMt;
import nts.uk.ctx.sys.assist.dom.categoryfieldmt.CategoryFieldMtRepository;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;
import nts.uk.ctx.sys.assist.dom.tablelist.TableListRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.csv.CSVFileData;
import nts.uk.shr.infra.file.csv.CSVReportGenerator;

/**
 * @author nam.lh
 *
 */
@Stateless
public class ManualSetOfDataSaveService extends ExportService<Object> {

	private static final List<String> LST_NAME_ID_HEADER_TABLE = Arrays.asList("CMF003_500", "CMF003_501", "CMF003_502",
			"CMF003_503", "CMF003_504", "CMF003_505", "CMF003_506", "CMF003_507", "CMF003_508", "CMF003_509",
			"CMF003_510", "CMF003_511", "CMF003_512", "CMF003_513", "CMF003_514", "CMF003_515", "CMF003_516",
			"CMF003_517", "CMF003_585", "CMF003_586", "CMF003_587", "CMF003_588", "CMF003_589", "CMF003_590",
			"CMF003_591", "CMF003_592", "CMF003_593", "CMF003_594", "CMF003_595", "CMF003_596", "CMF003_597",
			"CMF003_598", "CMF003_599", "CMF003_600", "CMF003_601", "CMF003_602", "CMF003_603", "CMF003_604",
			"CMF003_605", "CMF003_606", "CMF003_607", "CMF003_608", "CMF003_609", "CMF003_610", "CMF003_611",
			"CMF003_612", "CMF003_518", "CMF003_519", "CMF003_520", "CMF003_521", "CMF003_522", "CMF003_523",
			"CMF003_524", "CMF003_525", "CMF003_526", "CMF003_527", "CMF003_528", "CMF003_529", "CMF003_530",
			"CMF003_531", "CMF003_532", "CMF003_533", "CMF003_534", "CMF003_535", "CMF003_536", "CMF003_537",
			"CMF003_538", "CMF003_539", "CMF003_540", "CMF003_541", "CMF003_542", "CMF003_543", "CMF003_544",
			"CMF003_545", "CMF003_546", "CMF003_547", "CMF003_548", "CMF003_549", "CMF003_550", "CMF003_551",
			"CMF003_552", "CMF003_553", "CMF003_554", "CMF003_555", "CMF003_556", "CMF003_557", "CMF003_558",
			"CMF003_559", "CMF003_560", "CMF003_561", "CMF003_562", "CMF003_563", "CMF003_564", "CMF003_565",
			"CMF003_566", "CMF003_567", "CMF003_568", "CMF003_569", "CMF003_570", "CMF003_571", "CMF003_572",
			"CMF003_573", "CMF003_574", "CMF003_575", "CMF003_576", "CMF003_577", "CMF003_578", "CMF003_579",
			"CMF003_580", "CMF003_581", "CMF003_582", "CMF003_613", "CMF003_583", "CMF003_584");

	private static final List<String> LST_NAME_ID_HEADER_TABLE_CSV2 = Arrays.asList("SID", "SCD", "BUSINESS_NAME");

	private static final List<String> LST_NAME_ID_HEADER_TABLE_CSV3 = Arrays.asList("H_CID", "H_SID", "H_DATE",
			"H_DATE_START", "H_DATE_END");

	private static final String CSV_EXTENSION = ".csv";
	private static final String ZIP_EXTENSION = ".zip";
	private static final String FILE_NAME_CSV1 = "保存対象テーブル一覧";
	private static final String FILE_NAME_CSV2 = "対象社員";
	private static final int NUM_OF_TABLE_EACH_PROCESS = 100;

	@Inject
	private ResultOfSavingRepository repoResultSaving;
	@Inject
	private DataStorageMngRepository repoDataSto;
	@Inject
	private CategoryRepository repoCategory;
	@Inject
	private CategoryFieldMtRepository repoCateField;
	@Inject
	private TargetCategoryRepository repoTargetCat;
	@Inject
	private TargetEmployeesRepository repoTargetEmp;
	@Inject
	private CSVReportGenerator generator;
	@Inject
	private TableListRepository repoTableList;
	@Inject
	private ApplicationTemporaryFileFactory applicationTemporaryFileFactory;

	@Override
	protected void handle(ExportServiceContext<Object> context) {
		ManualSetOfDataSave domain = (ManualSetOfDataSave) context.getQuery();
		serverManualSaveProcessing(domain, context.getGeneratorContext());
	}

	public void serverManualSaveProcessing(ManualSetOfDataSave manualSetting, FileGeneratorContext generatorContext) {
		// ドメインモデル「データ保存の保存結果」へ書き出す
		String storeProcessingId = manualSetting.getStoreProcessingId();
		String cid = manualSetting.getCid();
		int systemType = manualSetting.getSystemType().value;
		String practitioner = manualSetting.getPractitioner();
		int saveForm = 0;
		String saveSetCode = null;
		String saveName = manualSetting.getSaveSetName().v();
		int saveForInvest = manualSetting.getIdentOfSurveyPre().value;
		GeneralDateTime saveStartDatetime = GeneralDateTime.now();

		int fileSize = 0;
		String saveFileName = null;
		GeneralDateTime saveEndDatetime = null;
		int deletedFiles = 0;
		String compressedPassword = null;
		int targetNumberPeople = 0;
		int saveStatus = 0;
		String fileId = null;

		ResultOfSaving data = new ResultOfSaving(storeProcessingId, cid, systemType, fileSize, saveSetCode,
				saveFileName, saveName, saveForm, saveEndDatetime, saveStartDatetime, deletedFiles, compressedPassword,
				practitioner, targetNumberPeople, saveStatus, saveForInvest, fileId);
		repoResultSaving.add(data);

		// ドメインモデル「データ保存動作管理」を登録する
		repoDataSto.update(storeProcessingId, OperatingCondition.INPREPARATION);

		// アルゴリズム「対象テーブルの選定と条件設定」を実行
		ResultState resultState = selectTargetTable(storeProcessingId, manualSetting);

		if (resultState != ResultState.NORMAL_END) {
			evaluateAbnormalEnd(storeProcessingId, 0);
			return;
		}

		// 対象社員のカウント件数を取り保持する
		List<TargetEmployees> targetEmployees = repoTargetEmp.getTargetEmployeesListById(storeProcessingId);

		// アルゴリズム「対象データの保存」を実行
		resultState = saveTargetData(storeProcessingId, generatorContext, manualSetting, targetEmployees);

		// 処理結果を判定
		switch (resultState) {
		case NORMAL_END:
			evaluateNormalEnd(storeProcessingId, generatorContext, manualSetting, targetEmployees.size());
			break;

		case ABNORMAL_END:
			evaluateAbnormalEnd(storeProcessingId, targetEmployees.size());
			break;

		case INTERRUPTION:
			evaluateInterruption(storeProcessingId, targetEmployees.size());
			break;
		}

	}

	private ResultState selectTargetTable(String storeProcessingId, ManualSetOfDataSave optManualSetting) {
		// Get list category from
		List<TargetCategory> targetCategories = repoTargetCat.getTargetCategoryListById(storeProcessingId);
		List<String> categoryIds = targetCategories.stream().map(x -> {
			return x.getCategoryId();
		}).collect(Collectors.toList());

		// update domain 「データ保存動作管理」 Data storage operation management
		repoDataSto.update(storeProcessingId, categoryIds.size(), 0, OperatingCondition.SAVING);

		List<Category> categorys = repoCategory.getCategoryByListId(categoryIds);
		List<CategoryFieldMt> categoryFieldMts = repoCateField.getCategoryFieldMtByListId(categoryIds);
		int countCategoryFieldMts = categoryFieldMts.stream()
				.collect(Collectors.groupingBy(CategoryFieldMt::getCategoryId)).keySet().size();
		if (categoryIds.size() != countCategoryFieldMts) {
			return ResultState.ABNORMAL_END;
		}

		String cId = optManualSetting.getCid();
		for (CategoryFieldMt categoryFieldMt : categoryFieldMts) {

			String categoryName = "";
			int storageRangeSaved = 0;
			TimeStore retentionPeriodCls = null;
			int anotherComCls = 0;
			String screenRetentionPeriod = "";
			GeneralDate saveDateFrom = null;
			GeneralDate saveDateTo = null;
			int surveyPreservation = optManualSetting.getIdentOfSurveyPre().value;
			String compressedFileName = "";
			Optional<Category> category = categorys.stream()
					.filter(c -> c.getCategoryId().v().equals(categoryFieldMt.getCategoryId())).findFirst();
			if (category.isPresent()) {
				categoryName = category.get().getCategoryName().v();
				storageRangeSaved = category.get().getStorageRangeSaved().value;
				retentionPeriodCls = category.get().getTimeStore();
				anotherComCls = category.get().getOtherCompanyCls() != null ? category.get().getOtherCompanyCls().value
						: null;

				switch (retentionPeriodCls) {
				case DAILY:
					saveDateFrom = optManualSetting.getDaySaveStartDate();
					saveDateTo = optManualSetting.getDaySaveEndDate();
					screenRetentionPeriod = saveDateFrom.toString("yyyy/MM/dd") + "～"
							+ saveDateTo.toString("yyyy/MM/dd");
					break;
				case MONTHLY:
					saveDateFrom = optManualSetting.getMonthSaveStartDate();
					saveDateTo = optManualSetting.getMonthSaveEndDate();
					screenRetentionPeriod = saveDateFrom.toString("yyyy/MM") + "～" + saveDateTo.toString("yyyy/MM");
					break;
				case ANNUAL:
					saveDateFrom = GeneralDate.ymd(optManualSetting.getStartYear().v(), 1, 1);
					saveDateTo = GeneralDate.ymd(optManualSetting.getEndYear().v(), 12, 31);
					screenRetentionPeriod = saveDateFrom.toString("yyyy") + "～" + saveDateTo.toString("yyyy");
					break;
				default:
					break;
				}

			}
			String internalFileName = cId + categoryName + categoryFieldMt.getTableJapanName();

			// B42
			String datetimenow = LocalDateTime.now().toString();
			SaveSetName savename = optManualSetting.getSaveSetName();
			compressedFileName = cId + savename.toString() + datetimenow;

			TableList listtable = new TableList(categoryFieldMt.getCategoryId(), categoryName, storeProcessingId, "",
					categoryFieldMt.getTableNo(), categoryFieldMt.getTableJapanName(),
					categoryFieldMt.getTableEnglishName(), categoryFieldMt.getFieldAcqCid(),
					categoryFieldMt.getFieldAcqDateTime(), categoryFieldMt.getFieldAcqEmployeeId(),
					categoryFieldMt.getFieldAcqEndDate(), categoryFieldMt.getFieldAcqStartDate(), "",
					optManualSetting.getSaveSetName().toString(), "0", saveDateFrom, saveDateTo, storageRangeSaved,
					retentionPeriodCls != null ? retentionPeriodCls.value : null, internalFileName, anotherComCls, "",
					"", compressedFileName, categoryFieldMt.getFieldChild1(), categoryFieldMt.getFieldChild2(),
					categoryFieldMt.getFieldChild3(), categoryFieldMt.getFieldChild4(),
					categoryFieldMt.getFieldChild5(), categoryFieldMt.getFieldChild6(),
					categoryFieldMt.getFieldChild7(), categoryFieldMt.getFieldChild8(),
					categoryFieldMt.getFieldChild9(), categoryFieldMt.getFieldChild10(),
					categoryFieldMt.getHistoryCls() != null ? categoryFieldMt.getHistoryCls().value : null, 0, 0,
					categoryFieldMt.getClsKeyQuery1(), categoryFieldMt.getClsKeyQuery2(),
					categoryFieldMt.getClsKeyQuery3(), categoryFieldMt.getClsKeyQuery4(),
					categoryFieldMt.getClsKeyQuery5(), categoryFieldMt.getClsKeyQuery6(),
					categoryFieldMt.getClsKeyQuery7(), categoryFieldMt.getClsKeyQuery8(),
					categoryFieldMt.getClsKeyQuery9(), categoryFieldMt.getClsKeyQuery10(),
					categoryFieldMt.getFieldKeyQuery1(), categoryFieldMt.getFieldKeyQuery2(),
					categoryFieldMt.getFieldKeyQuery3(), categoryFieldMt.getFieldKeyQuery4(),
					categoryFieldMt.getFieldKeyQuery5(), categoryFieldMt.getFieldKeyQuery6(),
					categoryFieldMt.getFieldKeyQuery7(), categoryFieldMt.getFieldKeyQuery8(),
					categoryFieldMt.getFieldKeyQuery9(), categoryFieldMt.getFieldKeyQuery10(),
					categoryFieldMt.getDefaultCondKeyQuery(), categoryFieldMt.getFieldDate1(),
					categoryFieldMt.getFieldDate2(), categoryFieldMt.getFieldDate3(), categoryFieldMt.getFieldDate4(),
					categoryFieldMt.getFieldDate5(), categoryFieldMt.getFieldDate6(), categoryFieldMt.getFieldDate7(),
					categoryFieldMt.getFieldDate8(), categoryFieldMt.getFieldDate9(), categoryFieldMt.getFieldDate10(),
					categoryFieldMt.getFieldDate11(), categoryFieldMt.getFieldDate12(),
					categoryFieldMt.getFieldDate13(), categoryFieldMt.getFieldDate14(),
					categoryFieldMt.getFieldDate15(), categoryFieldMt.getFieldDate16(),
					categoryFieldMt.getFieldDate17(), categoryFieldMt.getFieldDate18(),
					categoryFieldMt.getFieldDate19(), categoryFieldMt.getFieldDate20(),
					categoryFieldMt.getFiledKeyUpdate1(), categoryFieldMt.getFiledKeyUpdate2(),
					categoryFieldMt.getFiledKeyUpdate3(), categoryFieldMt.getFiledKeyUpdate4(),
					categoryFieldMt.getFiledKeyUpdate5(), categoryFieldMt.getFiledKeyUpdate6(),
					categoryFieldMt.getFiledKeyUpdate7(), categoryFieldMt.getFiledKeyUpdate8(),
					categoryFieldMt.getFiledKeyUpdate9(), categoryFieldMt.getFiledKeyUpdate10(),
					categoryFieldMt.getFiledKeyUpdate11(), categoryFieldMt.getFiledKeyUpdate12(),
					categoryFieldMt.getFiledKeyUpdate13(), categoryFieldMt.getFiledKeyUpdate14(),
					categoryFieldMt.getFiledKeyUpdate15(), categoryFieldMt.getFiledKeyUpdate16(),
					categoryFieldMt.getFiledKeyUpdate17(), categoryFieldMt.getFiledKeyUpdate18(),
					categoryFieldMt.getFiledKeyUpdate19(), categoryFieldMt.getFiledKeyUpdate20(), screenRetentionPeriod,
					optManualSetting.getSuppleExplanation(), categoryFieldMt.getParentTblJpName(),
					categoryFieldMt.getHasParentTblFlg() != null ? categoryFieldMt.getHasParentTblFlg().value : 0,
					categoryFieldMt.getParentTblName(), categoryFieldMt.getFieldParent1(),
					categoryFieldMt.getFieldParent2(), categoryFieldMt.getFieldParent3(),
					categoryFieldMt.getFieldParent4(), categoryFieldMt.getFieldParent5(),
					categoryFieldMt.getFieldParent6(), categoryFieldMt.getFieldParent7(),
					categoryFieldMt.getFieldParent8(), categoryFieldMt.getFieldParent9(),
					categoryFieldMt.getFieldParent10(), surveyPreservation);

			repoTableList.add(listtable);
		}

		return ResultState.NORMAL_END;
	}

	private ResultState saveTargetData(String storeProcessingId, FileGeneratorContext generatorContext,
			ManualSetOfDataSave optManualSetting, List<TargetEmployees> targetEmployees) {
		// アルゴリズム「対象データの保存」を実行
		ResultState resultState;

		// テーブル一覧の内容をテンポラリーフォルダにcsvファイルで書き出す
		resultState = generalCsv(generatorContext, storeProcessingId);

		if (resultState != ResultState.NORMAL_END) {
			return resultState;
		}

		// 「テーブル一覧」の調査保存の識別が「する」の場合、ドメインモデル「対象社員」のビジネスネームを全てNULLクリアする
		if (optManualSetting.getIdentOfSurveyPre() == NotUseAtr.USE) {
			repoTargetEmp.removeBusinessName(storeProcessingId);
		}

		// 対象社員の内容をcsvファイルに暗号化して書き出す
		resultState = generalCsv2(generatorContext, targetEmployees);

		if (resultState != ResultState.NORMAL_END) {
			return resultState;
		}

		return resultState;
	}

	private ResultState generalCsv(FileGeneratorContext generatorContext, String storeProcessingId) {
		try {
			ResultState resultState = ResultState.NORMAL_END;
			List<String> headerCsv = this.getTextHeaderCsv1();
			// Get data from Manual Setting table
			List<Map<String, Object>> dataSourceCsv = new ArrayList<>();
			int offset = 0;
			String categoryId = "";
			while (true) {
				// テーブル一覧の１行分を処理する
				List<TableList> tableLists = repoTableList.getByOffsetAndNumber(storeProcessingId, offset,
						NUM_OF_TABLE_EACH_PROCESS);

				for (TableList tableList : tableLists) {
					dataSourceCsv = getDataSourceCsv1(dataSourceCsv, headerCsv, tableList);

					// Add Table to CSV Auto
					resultState = generalCsvAuto(generatorContext, storeProcessingId, tableList);
					if (resultState != ResultState.NORMAL_END) {
						return resultState;
					}
					// テーブル一覧で次の処理行のカテゴリが異なる場合
					// ドメインモデル「データ保存動作管理」を更新する
					if (!tableList.getCategoryId().equals(categoryId)) {
						categoryId = tableList.getCategoryId();
						repoDataSto.increaseCategoryCount(storeProcessingId);
					}
				}

				offset += NUM_OF_TABLE_EACH_PROCESS;
				// テーブルをすべて書き出したか判定
				if (tableLists.size() < NUM_OF_TABLE_EACH_PROCESS)
					break;
			}

			CSVFileData fileData = new CSVFileData(FILE_NAME_CSV1 + CSV_EXTENSION, headerCsv, dataSourceCsv);
			generator.generate(generatorContext, fileData);
			return ResultState.NORMAL_END;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultState.ABNORMAL_END;
		}

	}

	private List<Map<String, Object>> getDataSourceCsv1(List<Map<String, Object>> dataSourceCsv, List<String> headerCsv,
			TableList tableList) {
		Map<String, Object> rowCsv = new HashMap<>();
		rowCsv.put(headerCsv.get(0), tableList.getDataStorageProcessingId());
		rowCsv.put(headerCsv.get(1), tableList.getSaveForm());
		rowCsv.put(headerCsv.get(2), tableList.getSaveSetCode());
		rowCsv.put(headerCsv.get(3), tableList.getSaveSetName());
		rowCsv.put(headerCsv.get(4), tableList.getSupplementaryExplanation());
		rowCsv.put(headerCsv.get(5), tableList.getCategoryId());
		rowCsv.put(headerCsv.get(6), tableList.getCategoryName());
		rowCsv.put(headerCsv.get(7), tableList.getRetentionPeriodCls().value);
		rowCsv.put(headerCsv.get(8), tableList.getStorageRangeSaved().value);
		rowCsv.put(headerCsv.get(9), tableList.getScreenRetentionPeriod());
		rowCsv.put(headerCsv.get(10), tableList.getReferenceYear());
		rowCsv.put(headerCsv.get(11), tableList.getReferenceMonth());
		rowCsv.put(headerCsv.get(12), tableList.getSurveyPreservation().value);
		rowCsv.put(headerCsv.get(13), tableList.getAnotherComCls().value);
		rowCsv.put(headerCsv.get(14), tableList.getTableNo());
		rowCsv.put(headerCsv.get(15), tableList.getTableJapaneseName());
		rowCsv.put(headerCsv.get(16), tableList.getTableEnglishName());
		rowCsv.put(headerCsv.get(17), tableList.getHistoryCls().value);
		rowCsv.put(headerCsv.get(18), tableList.getHasParentTblFlg().value);
		rowCsv.put(headerCsv.get(19), tableList.getParentTblJpName());
		rowCsv.put(headerCsv.get(20), tableList.getParentTblName());
		rowCsv.put(headerCsv.get(21), tableList.getFieldParent1());
		rowCsv.put(headerCsv.get(22), tableList.getFieldParent2());
		rowCsv.put(headerCsv.get(23), tableList.getFieldParent3());
		rowCsv.put(headerCsv.get(24), tableList.getFieldParent4());
		rowCsv.put(headerCsv.get(25), tableList.getFieldParent5());
		rowCsv.put(headerCsv.get(26), tableList.getFieldParent6());
		rowCsv.put(headerCsv.get(27), tableList.getFieldParent7());
		rowCsv.put(headerCsv.get(28), tableList.getFieldParent8());
		rowCsv.put(headerCsv.get(29), tableList.getFieldParent9());
		rowCsv.put(headerCsv.get(30), tableList.getFieldParent10());
		rowCsv.put(headerCsv.get(31), tableList.getFieldChild1());
		rowCsv.put(headerCsv.get(32), tableList.getFieldChild2());
		rowCsv.put(headerCsv.get(33), tableList.getFieldChild3());
		rowCsv.put(headerCsv.get(34), tableList.getFieldChild4());
		rowCsv.put(headerCsv.get(35), tableList.getFieldChild5());
		rowCsv.put(headerCsv.get(36), tableList.getFieldChild6());
		rowCsv.put(headerCsv.get(37), tableList.getFieldChild7());
		rowCsv.put(headerCsv.get(38), tableList.getFieldChild8());
		rowCsv.put(headerCsv.get(39), tableList.getFieldChild9());
		rowCsv.put(headerCsv.get(40), tableList.getFieldChild10());
		rowCsv.put(headerCsv.get(41), tableList.getFieldAcqCid());
		rowCsv.put(headerCsv.get(42), tableList.getFieldAcqEmployeeId());
		rowCsv.put(headerCsv.get(43), tableList.getFieldAcqDateTime());
		rowCsv.put(headerCsv.get(44), tableList.getFieldAcqStartDate());
		rowCsv.put(headerCsv.get(45), tableList.getFieldAcqEndDate());
		rowCsv.put(headerCsv.get(46), tableList.getDefaultCondKeyQuery());
		rowCsv.put(headerCsv.get(47), tableList.getFieldKeyQuery1());
		rowCsv.put(headerCsv.get(48), tableList.getFieldKeyQuery2());
		rowCsv.put(headerCsv.get(49), tableList.getFieldKeyQuery3());
		rowCsv.put(headerCsv.get(50), tableList.getFieldKeyQuery4());
		rowCsv.put(headerCsv.get(51), tableList.getFieldKeyQuery5());
		rowCsv.put(headerCsv.get(52), tableList.getFieldKeyQuery6());
		rowCsv.put(headerCsv.get(53), tableList.getFieldKeyQuery7());
		rowCsv.put(headerCsv.get(54), tableList.getFieldKeyQuery8());
		rowCsv.put(headerCsv.get(55), tableList.getFieldKeyQuery9());
		rowCsv.put(headerCsv.get(56), tableList.getFieldKeyQuery10());
		rowCsv.put(headerCsv.get(57), tableList.getClsKeyQuery1());
		rowCsv.put(headerCsv.get(58), tableList.getClsKeyQuery2());
		rowCsv.put(headerCsv.get(59), tableList.getClsKeyQuery3());
		rowCsv.put(headerCsv.get(60), tableList.getClsKeyQuery4());
		rowCsv.put(headerCsv.get(61), tableList.getClsKeyQuery5());
		rowCsv.put(headerCsv.get(62), tableList.getClsKeyQuery6());
		rowCsv.put(headerCsv.get(63), tableList.getClsKeyQuery7());
		rowCsv.put(headerCsv.get(64), tableList.getClsKeyQuery8());
		rowCsv.put(headerCsv.get(65), tableList.getClsKeyQuery9());
		rowCsv.put(headerCsv.get(66), tableList.getClsKeyQuery10());
		rowCsv.put(headerCsv.get(67), tableList.getFiledKeyUpdate1());
		rowCsv.put(headerCsv.get(68), tableList.getFiledKeyUpdate2());
		rowCsv.put(headerCsv.get(69), tableList.getFiledKeyUpdate3());
		rowCsv.put(headerCsv.get(70), tableList.getFiledKeyUpdate4());
		rowCsv.put(headerCsv.get(71), tableList.getFiledKeyUpdate5());
		rowCsv.put(headerCsv.get(72), tableList.getFiledKeyUpdate6());
		rowCsv.put(headerCsv.get(73), tableList.getFiledKeyUpdate7());
		rowCsv.put(headerCsv.get(74), tableList.getFiledKeyUpdate8());
		rowCsv.put(headerCsv.get(75), tableList.getFiledKeyUpdate9());
		rowCsv.put(headerCsv.get(76), tableList.getFiledKeyUpdate10());
		rowCsv.put(headerCsv.get(77), tableList.getFiledKeyUpdate11());
		rowCsv.put(headerCsv.get(78), tableList.getFiledKeyUpdate12());
		rowCsv.put(headerCsv.get(79), tableList.getFiledKeyUpdate13());
		rowCsv.put(headerCsv.get(80), tableList.getFiledKeyUpdate14());
		rowCsv.put(headerCsv.get(81), tableList.getFiledKeyUpdate15());
		rowCsv.put(headerCsv.get(82), tableList.getFiledKeyUpdate16());
		rowCsv.put(headerCsv.get(83), tableList.getFiledKeyUpdate17());
		rowCsv.put(headerCsv.get(84), tableList.getFiledKeyUpdate18());
		rowCsv.put(headerCsv.get(85), tableList.getFiledKeyUpdate19());
		rowCsv.put(headerCsv.get(86), tableList.getFiledKeyUpdate20());
		rowCsv.put(headerCsv.get(87), tableList.getFieldDate1());
		rowCsv.put(headerCsv.get(88), tableList.getFieldDate2());
		rowCsv.put(headerCsv.get(89), tableList.getFieldDate3());
		rowCsv.put(headerCsv.get(90), tableList.getFieldDate4());
		rowCsv.put(headerCsv.get(91), tableList.getFieldDate5());
		rowCsv.put(headerCsv.get(92), tableList.getFieldDate6());
		rowCsv.put(headerCsv.get(93), tableList.getFieldDate7());
		rowCsv.put(headerCsv.get(94), tableList.getFieldDate8());
		rowCsv.put(headerCsv.get(95), tableList.getFieldDate9());
		rowCsv.put(headerCsv.get(96), tableList.getFieldDate10());
		rowCsv.put(headerCsv.get(97), tableList.getFieldDate11());
		rowCsv.put(headerCsv.get(98), tableList.getFieldDate12());
		rowCsv.put(headerCsv.get(99), tableList.getFieldDate13());
		rowCsv.put(headerCsv.get(100), tableList.getFieldDate14());
		rowCsv.put(headerCsv.get(101), tableList.getFieldDate15());
		rowCsv.put(headerCsv.get(102), tableList.getFieldDate16());
		rowCsv.put(headerCsv.get(103), tableList.getFieldDate17());
		rowCsv.put(headerCsv.get(104), tableList.getFieldDate18());
		rowCsv.put(headerCsv.get(105), tableList.getFieldDate19());
		rowCsv.put(headerCsv.get(106), tableList.getFieldDate20());
		rowCsv.put(headerCsv.get(107), tableList.getSaveDateFrom());
		rowCsv.put(headerCsv.get(108), tableList.getSaveDateTo());
		rowCsv.put(headerCsv.get(109), tableList.getCompressedFileName());
		rowCsv.put(headerCsv.get(110), tableList.getInternalFileName());
		rowCsv.put(headerCsv.get(111), tableList.getDataRecoveryProcessId());
		rowCsv.put(headerCsv.get(112), tableList.getCanNotBeOld());
		rowCsv.put(headerCsv.get(113), tableList.getSelectionTargetForRes());

		dataSourceCsv.add(rowCsv);
		return dataSourceCsv;
	}

	private ResultState generalCsv2(FileGeneratorContext generatorContext, List<TargetEmployees> targetEmployees) {
		try {
			// Add Table to CSV2
			List<String> headerCsv2 = this.getTextHeaderCsv2();
			List<Map<String, Object>> dataSourceCsv2 = new ArrayList<>();
			for (TargetEmployees targetEmp : targetEmployees) {
				Map<String, Object> rowCsv2 = new HashMap<>();
				rowCsv2.put(headerCsv2.get(0), targetEmp.getSid());
				rowCsv2.put(headerCsv2.get(1), targetEmp.getScd());
				rowCsv2.put(headerCsv2.get(2), CommonKeyCrypt.encrypt(targetEmp.getBusinessname().v()));
				dataSourceCsv2.add(rowCsv2);
			}

			CSVFileData fileData = new CSVFileData(FILE_NAME_CSV2 + CSV_EXTENSION, headerCsv2, dataSourceCsv2);

			generator.generate(generatorContext, fileData);
			return ResultState.NORMAL_END;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultState.ABNORMAL_END;
		}

	}

	private ResultState generalCsvAuto(FileGeneratorContext generatorContext, String storeProcessingId,
			TableList tableList) {
		try {
			// ドメインモデル「データ保存動作管理」を取得し「中断終了」を判別
			Optional<DataStorageMng> dataStorageMng = repoDataSto.getDataStorageMngById(storeProcessingId);

			// TrungBV: Fix check interrupt
			if (dataStorageMng.isPresent() && dataStorageMng.get().getDoNotInterrupt() == NotUseAtr.USE) {
				dataStorageMng.get().operatingCondition = EnumAdaptor.valueOf(OperatingCondition.INTERRUPTION_END.value, OperatingCondition.class);
				repoDataSto.update(dataStorageMng.get());
				return ResultState.INTERRUPTION;
			}
			
//			if (dataStorageMng.isPresent()
//					&& dataStorageMng.get().operatingCondition == OperatingCondition.INTERRUPTION_END) {
//				return ResultState.INTERRUPTION;
//			}

			Class<?> tableExport = repoTableList.getTypeForTableName(tableList.getTableEnglishName());
			List<?> listObject = repoTableList.getDataDynamic(tableList, tableExport);

			// Add Table to CSV Auto
			List<String> headerCsv3 = this.getTextHeaderCsv3(tableExport);
			List<Map<String, Object>> dataSourceCsv3 = new ArrayList<>();
			for (Object object : listObject) {
				Map<String, Object> rowCsv = new HashMap<>();

				for (String key : headerCsv3) {
					String header = key;
					if (key.equals(LST_NAME_ID_HEADER_TABLE_CSV3.get(0))
							&& !Strings.isNullOrEmpty(tableList.getFieldAcqCid())) {
						header = tableList.getFieldAcqCid();
					}
					if (key.equals(LST_NAME_ID_HEADER_TABLE_CSV3.get(1))
							&& !Strings.isNullOrEmpty(tableList.getFieldAcqEmployeeId())) {
						header = tableList.getFieldAcqEmployeeId();
					}
					if (key.equals(LST_NAME_ID_HEADER_TABLE_CSV3.get(2))
							&& !Strings.isNullOrEmpty(tableList.getFieldAcqDateTime())) {
						header = tableList.getFieldAcqDateTime();
					}
					if (key.equals(LST_NAME_ID_HEADER_TABLE_CSV3.get(3))
							&& !Strings.isNullOrEmpty(tableList.getFieldAcqStartDate())) {
						header = tableList.getFieldAcqStartDate();
					}
					if (key.equals(LST_NAME_ID_HEADER_TABLE_CSV3.get(4))
							&& !Strings.isNullOrEmpty(tableList.getFieldAcqEndDate())) {
						header = tableList.getFieldAcqEndDate();
					}
					String fieldName = repoTableList.getFieldForColumnName(object.getClass(), header);
					Object resultObj = null;
					if (!Strings.isNullOrEmpty(fieldName)) {
						resultObj = object;
						for (String name : fieldName.split("\\.")) {
							resultObj = getValueByPropertyName(resultObj, name);
						}
					}
					rowCsv.put(key, resultObj);
				}

				dataSourceCsv3.add(rowCsv);
			}

			CSVFileData fileData = new CSVFileData(AppContexts.user().companyId() + tableList.getCategoryName()
					+ tableList.getTableJapaneseName() + CSV_EXTENSION, headerCsv3, dataSourceCsv3);

			generator.generate(generatorContext, fileData);
			return ResultState.NORMAL_END;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultState.ABNORMAL_END;
		}
	}

	private ResultState evaluateNormalEnd(String storeProcessingId, FileGeneratorContext generatorContext,
			ManualSetOfDataSave optManualSetting, int totalTargetEmployees) {
		// ドメインモデル「データ保存動作管理」を更新する
		repoDataSto.update(storeProcessingId, OperatingCondition.INPROGRESS);

		try {
			// アルゴリズム「結果ファイルの圧縮」を実行
			ApplicationTemporaryFilesContainer applicationTemporaryFilesContainer = applicationTemporaryFileFactory
					.createContainer();
			NotUseAtr passwordAvailability = optManualSetting.getPasswordAvailability();
			String fileName = AppContexts.user().companyId() + optManualSetting.getSaveSetName()
					+ LocalDateTime.now().toString() + ZIP_EXTENSION;
			if (passwordAvailability == NotUseAtr.NOT_USE) {
				applicationTemporaryFilesContainer.zipWithName(generatorContext, fileName);
			}
			if (passwordAvailability == NotUseAtr.USE) {
				String password = optManualSetting.getCompressedPassword().v();
				applicationTemporaryFilesContainer.zipWithName(generatorContext, fileName, password);
			}

			applicationTemporaryFilesContainer.removeContainer();
		} catch (Exception e) {
			e.printStackTrace();
			return evaluateAbnormalEnd(storeProcessingId, totalTargetEmployees);
		}

		// ドメインモデル「データ保存動作管理」を更新する
		repoDataSto.update(storeProcessingId, OperatingCondition.DONE);

		// ドメインモデル「データ保存の保存結果」を書き出し
		String fileId = generatorContext.getTaskId();
		repoResultSaving.update(storeProcessingId, totalTargetEmployees, SaveStatus.SUCCESS, fileId, NotUseAtr.NOT_USE);

		return ResultState.NORMAL_END;
	}

	private ResultState evaluateAbnormalEnd(String storeProcessingId, int totalTargetEmployees) {
		// ドメインモデル「データ保存動作管理」を更新する
		repoDataSto.update(storeProcessingId, OperatingCondition.ABNORMAL_TERMINATION);

		// ドメインモデル「データ保存の保存結果」を書き出し
		repoResultSaving.update(storeProcessingId, totalTargetEmployees, SaveStatus.FAILURE);

		return ResultState.ABNORMAL_END;
	}

	private ResultState evaluateInterruption(String storeProcessingId, int totalTargetEmployees) {
		// ドメインモデル「データ保存動作管理」を更新する
		repoDataSto.update(storeProcessingId, OperatingCondition.INTERRUPTION_END);

		// ドメインモデル「データ保存の保存結果」を書き出し
		repoResultSaving.update(storeProcessingId, totalTargetEmployees, SaveStatus.INTERRUPTION);

		return ResultState.INTERRUPTION;
	}

	private Object getValueByPropertyName(Object object, String fieldName) {
		try {
			Field[] fieldSuperclasses = object.getClass().getSuperclass().getDeclaredFields();
			for (Field field : fieldSuperclasses) {
				if (field.getName().equals(fieldName)) {
					field.setAccessible(true);
					return field.get(object);
				}
			}
			Field[] fields = object.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals(fieldName)) {
					field.setAccessible(true);
					return field.get(object);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<String> getTextHeaderCsv1() {
		List<String> lstHeader = new ArrayList<>();
		for (String nameId : LST_NAME_ID_HEADER_TABLE) {
			lstHeader.add(TextResource.localize(nameId));
		}
		return lstHeader;
	}

	private List<String> getTextHeaderCsv2() {
		List<String> lstHeader = new ArrayList<>();
		for (String nameId : LST_NAME_ID_HEADER_TABLE_CSV2) {
			lstHeader.add(TextResource.localize(nameId));
		}
		return lstHeader;
	}

	private List<String> getTextHeaderCsv3(Class<?> type) {
		List<String> columnCommons = new ArrayList<>();
		for (Field field : type.getSuperclass().getDeclaredFields()) {
			Column column = field.getDeclaredAnnotation(Column.class);
			if (column != null)
				columnCommons.add(column.name());
		}

		List<String> columnNames = new ArrayList<>();
		for (Field field : type.getDeclaredFields()) {
			if (field.isAnnotationPresent(EmbeddedId.class)) {
				Class<?> pk = field.getType();
				for (Field fieldPk : pk.getDeclaredFields()) {
					Column columnPk = fieldPk.getDeclaredAnnotation(Column.class);
					if (columnPk != null)
						columnNames.add(columnPk.name());
				}
			}
			Column column = field.getDeclaredAnnotation(Column.class);
			if (column != null)
				columnNames.add(column.name());
		}

		List<String> columnFix = new ArrayList<>(LST_NAME_ID_HEADER_TABLE_CSV3);
		columnFix.addAll(columnCommons);
		columnFix.addAll(columnNames);
		return columnFix;
	}

}
