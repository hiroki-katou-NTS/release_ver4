package nts.uk.ctx.sys.assist.dom.datarestoration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nts.arc.system.ServerSystemProperties;
import nts.arc.time.GeneralDateTime;
import nts.gul.csv.CSVBufferReader;
import nts.gul.text.StringUtil;
import nts.uk.ctx.sys.assist.dom.category.Category;
import nts.uk.ctx.sys.assist.dom.category.CategoryRepository;
import nts.uk.ctx.sys.assist.dom.category.StorageRangeSaved;
import nts.uk.ctx.sys.assist.dom.datarestoration.common.CsvFileUtil;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;
import nts.uk.shr.com.enumcommon.NotUseAtr;

@Stateless
public class RecoveryStorageService {
	@Resource
	private SessionContext scContext;

	@Inject
	private PerformDataRecoveryRepository performDataRecoveryRepository;

	@Inject
	private CategoryRepository categoryRepository;

	@Inject
	private DataRecoveryMngRepository dataRecoveryMngRepository;

	@Inject
	private DataReEmployeeAdapter empDataMngRepo;

	@Inject
	private DataRecoveryResultRepository dataRecoveryResultRepository;

	private RecoveryStorageService self;
	
	@Inject
	private SaveLogDataRecoverServices saveLogDataRecoverServices; 
	
	@Inject
	private ProcessRecoverOneEmpHandle processRecoverOneEmpHandle;
	
	@Inject
	private ProcessRecoverListTblByCompanyHandle processRecoverListTblByCompanyHandle;

	@PostConstruct
	public void init() {
		this.self = scContext.getBusinessObject(RecoveryStorageService.class);
	}

	public static final String DATE_FORMAT = "yyyyMMdd";

	public static final int SELECTION_TARGET_FOR_RES = 1;

	public static final String SQL_EXCEPTION = "113";

	public static final String SETTING_EXCEPTION = "5";

	public static final String INDEX_CID_CSV = "0";

	public static final String INDEX_SID_CSV = "5";

	public static final String NONE_DATE = "9";

	public static final String YEAR = "6";

	public static final String YEAR_MONTH = "7";

	public static final String YEAR_MONTH_DAY = "8";

	public static final String GET_CLS_KEY_QUERY = "getClsKeyQuery";

	public static final String GET_FILED_KEY_UPDATE = "getFiledKeyUpdate";

	// fix bug #125405
	public static final String GET_FILED_KEY_QUERY = "getFieldKeyQuery";

	public static final String INDEX_HEADER = "indexUpdate";

	public static final Integer HEADER_CSV = 0;

	public static final Integer INDEX_SID = 1;

	public static final Integer INDEX_H_DATE = 2;

	public static final Integer INDEX_H_START_DATE = 3;

	public static Integer NUMBER_ERROR = 0;

	private static final String DATA_STORE_PATH = ServerSystemProperties.fileStoragePath();

	private static final Logger LOGGER = LoggerFactory.getLogger(RecoveryStorageService.class);

	private static final Map<String, Integer> datetimeRange = new HashMap<String, Integer>();
	static {
		datetimeRange.put(YEAR_MONTH_DAY, 10);
		datetimeRange.put(YEAR_MONTH, 7);
		datetimeRange.put(YEAR, 4);
	}

	public void recoveryStorage(String dataRecoveryProcessId) throws Exception {
		NUMBER_ERROR = 0;
		Optional<PerformDataRecovery> performRecoveries = performDataRecoveryRepository
				.getPerformDatRecoverById(dataRecoveryProcessId);
		String uploadId = performRecoveries.get().getUploadfileId();
		List<Category> listCategory = categoryRepository.findById(dataRecoveryProcessId, SELECTION_TARGET_FOR_RES);

		Optional<DataRecoveryMng> dataRecoveryMng = dataRecoveryMngRepository
				.getDataRecoveryMngById(dataRecoveryProcessId);
		if (dataRecoveryMng.isPresent() && dataRecoveryMng.get().getSuspendedState() == NotUseAtr.USE) {
			dataRecoveryResultRepository.updateEndDateTimeExecutionResult(dataRecoveryProcessId,
					DataRecoveryOperatingCondition.INTERRUPTION_END);
			performDataRecoveryRepository.remove(dataRecoveryProcessId);
			performDataRecoveryRepository.deleteTableListByDataStorageProcessingId(dataRecoveryProcessId);
			return;
		}

		// update OperatingCondition
		dataRecoveryMngRepository.updateByOperatingCondition(dataRecoveryProcessId,
				DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS);

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		int numberCateSucess = 0;
		
		// get list table by dataRecoveryProcessId (khg co trong eap)
		List<TableList> listTbl = performDataRecoveryRepository.getByDataRecoveryId(dataRecoveryProcessId);

		// ログ連番をZeroクリア
		saveLogDataRecoverServices.saveStartDataRecoverLog(dataRecoveryProcessId);

		// 処理対象のカテゴリを処理する
		for (Category category : listCategory) {

			List<TableList> tableUse = performDataRecoveryRepository.getByStorageRangeSaved(
					category.getCategoryId().v(), dataRecoveryProcessId, StorageRangeSaved.EARCH_EMP);
			List<TableList> tableNotUse = performDataRecoveryRepository.getByStorageRangeSaved(
					category.getCategoryId().v(), dataRecoveryProcessId, StorageRangeSaved.ALL_EMP);

			TableListByCategory tableListByCategory = new TableListByCategory(category.getCategoryId().v(), tableUse);
			TableListByCategory tableNotUseCategory = new TableListByCategory(category.getCategoryId().v(), tableNotUse);

			// カテゴリ単位の復旧
			condition = exCurrentCategory(tableListByCategory, tableNotUseCategory, uploadId, dataRecoveryProcessId, listTbl);

			// のカテゴリカウントをカウントアップ

			if (condition != DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS) {
				break;
			}
			numberCateSucess++;
			dataRecoveryMngRepository.updateCategoryCnt(dataRecoveryProcessId, numberCateSucess);
		}

		if (condition == DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS) {
			dataRecoveryMngRepository.updateByOperatingCondition(dataRecoveryProcessId,
					DataRecoveryOperatingCondition.DONE);
			dataRecoveryResultRepository.updateEndDateTimeExecutionResult(dataRecoveryProcessId,
					DataRecoveryOperatingCondition.DONE);
		} else {
			dataRecoveryMngRepository.updateByOperatingCondition(dataRecoveryProcessId, condition);
			dataRecoveryResultRepository.updateEndDateTimeExecutionResult(dataRecoveryProcessId, condition);
		}

		performDataRecoveryRepository.deleteTableListByDataStorageProcessingId(dataRecoveryProcessId);
		performDataRecoveryRepository.remove(dataRecoveryProcessId);

	}

	public DataRecoveryOperatingCondition exCurrentCategory(TableListByCategory tableListByCategory,
			TableListByCategory tableNotUseByCategory, String uploadId, String dataRecoveryProcessId, List<TableList> listTbl) throws Exception {

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		// カテゴリの中の社員単位の処理

		condition = processEmpInCategory(tableListByCategory, dataRecoveryProcessId, uploadId, listTbl);

		if (condition == DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS) {
			// の処理対象社員コードをクリアする
			dataRecoveryMngRepository.updateProcessTargetEmpCode(dataRecoveryProcessId, null);

			// カテゴリの中の日付単位の処理
			condition = processByDateInCategory(tableNotUseByCategory, dataRecoveryProcessId, uploadId, listTbl);
		}

		dataRecoveryMngRepository.updateByOperatingCondition(dataRecoveryProcessId, condition);
		return condition;
	}

	/**
	 * Xử lý những table không phải dạng lịch sử 
	 */
	public DataRecoveryOperatingCondition processEmpInCategory(TableListByCategory tableListByCategory,
			String dataRecoveryProcessId, String uploadId, List<TableList> listTbl) throws Exception {

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		List<DataRecoveryTable> tableList = new ArrayList<>();

		// カテゴリ単位の復旧
		if (tableListByCategory.getTables().size() > 0) {
			// -- Get List data from CSV file

			// Create [対象データ] TargetData
			Set<String> hashId = new HashSet<>();
			for (int j = 0; j < tableListByCategory.getTables().size(); j++) {

				Set<String> listSid = CsvFileUtil.getListSid(uploadId,
						tableListByCategory.getTables().get(j).getInternalFileName());

				// -- Tổng hợp ID Nhân viên duy nhất từ List Data
				hashId.addAll(listSid);

				DataRecoveryTable targetData = new DataRecoveryTable(uploadId,
						tableListByCategory.getTables().get(j).getInternalFileName(), listSid.isEmpty() ? false : true,
						tableListByCategory.getTables().get(j).getTableEnglishName(), 
						tableListByCategory.getTables().get(j).getTableJapaneseName(),
						tableListByCategory.getTables().get(j).getTableNo(),
						tableListByCategory.getTables().get(j).getHasParentTblFlg()
						);
					tableList.add(targetData);
			}
			
			// 対象社員コード＿ID
			List<EmployeeDataReInfoImport> employeeInfos = empDataMngRepo
					.findByIdsEmployee(new ArrayList<String>(hashId));

			// fix bug cho trường hợp retore từ màn hình cmf005 - start
			// Trường hợp này list employeeInfos lấy dữ liệu từ table
			// BSYMT_EMP_DTA_MNG_INFO bị rỗng, do đã bị xóa từ màn cmf005.
			// Nên list nhân viên tôi sẽ lấy ra từ file excel.
			if (employeeInfos.size() != hashId.size()) {
				List<String> listSidHasData = employeeInfos.stream().map(i -> i.getEmployeeId())
						.collect(Collectors.toList());
				List<String> listSid = new ArrayList<String>(hashId);
				for (int i = 0; i < listSid.size(); i++) {
					if (!listSidHasData.contains(listSid.get(i))) {
						employeeInfos.add(new EmployeeDataReInfoImport("", "", listSid.get(i), "",
								GeneralDateTime.now(), "", ""));
					}
				}
			}
			// - end

			// check employeeId in Target of PreformDataRecovery
			List<Target> listTarget = performDataRecoveryRepository.findByDataRecoveryId(dataRecoveryProcessId);

			// trường hợp list nhân viên tổng hợp từ các file excel băng null.
			// thì lấy list nhân viên từ màn hình truyền lên.
			if (hashId.isEmpty()) {
				employeeInfos = listTarget.stream().map(i -> {
					return new EmployeeDataReInfoImport(null, null, i.getSid(), i.getScd().orElse(""), null, null,
							null);
				}).collect(Collectors.toList());
			}
			
			// order list employee by empCode
			List<EmployeeDataReInfoImport> empsOrderByScd = employeeInfos.stream()
					.sorted(Comparator.comparing(EmployeeDataReInfoImport::getEmployeeCode))
					.collect(Collectors.toList());

			Optional<PerformDataRecovery> performDataRecovery = performDataRecoveryRepository
					.getPerformDatRecoverById(dataRecoveryProcessId);
			
			// Recover childTables , parentTables
			// Check recovery method [復旧方法]
			if (performDataRecovery.isPresent() && performDataRecovery.get().getRecoveryMethod() == RecoveryMethod.RESTORE_SELECTED_RANGE) {
				List<EmployeeDataReInfoImport> empSelectRange = listTarget.stream().map(i -> {
					return new EmployeeDataReInfoImport(null, null, i.getSid(), i.getScd().orElse(""), null, null,
							null);
				}).collect(Collectors.toList());
				
				if (empSelectRange.isEmpty())
					return DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
				
				this.processRecoverOneEmpHandle.recoverDataOneEmp(dataRecoveryProcessId, empSelectRange, tableList, listTarget, listTbl);
				
			} else {
				this.processRecoverOneEmpHandle.recoverDataOneEmp(dataRecoveryProcessId, empsOrderByScd, tableList, listTarget, listTbl);
			}
		 }
		return condition;
	}


	public DataRecoveryOperatingCondition processByDateInCategory(TableListByCategory tableNotUseByCategory,
			String dataRecoveryProcessId, String uploadId, List<TableList> listTbl) throws Exception {

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		if (tableNotUseByCategory.getTables().size() == 0) {
			return condition;
		}

		// Create [対象データ] TargetData
		Set<String> hashId = new HashSet<>();
		
		List<DataRecoveryTable> listTablesOrder = new ArrayList<>();
		List<DataRecoveryTable> parentTables = new ArrayList<>();
		List<DataRecoveryTable> childTables= new ArrayList<>();
		
		HashMap<String, CSVBufferReader> csvByteReadMaper_TableNotUse = new HashMap<>();
		for (int j = 0; j < tableNotUseByCategory.getTables().size(); j++) {

			Set<String> listSid = CsvFileUtil.getListSid(uploadId,
					tableNotUseByCategory.getTables().get(j).getInternalFileName());

			// -- Tổng hợp ID Nhân viên duy nhất từ List Data
			hashId.addAll(listSid);

			String filePath = getExtractDataStoragePath(uploadId) + "//"
					+ tableNotUseByCategory.getTables().get(j).getInternalFileName() + ".csv";

			CSVBufferReader reader = new CSVBufferReader(new File(filePath));
			reader.setCharset("UTF-8");
			csvByteReadMaper_TableNotUse.put(tableNotUseByCategory.getTables().get(j).getInternalFileName(), reader);
			
			DataRecoveryTable targetData = new DataRecoveryTable(uploadId,
					tableNotUseByCategory.getTables().get(j).getInternalFileName(), listSid.isEmpty() ? false : true,
							tableNotUseByCategory.getTables().get(j).getTableEnglishName(),
							tableNotUseByCategory.getTables().get(j).getTableJapaneseName(),
							tableNotUseByCategory.getTables().get(j).getTableNo(),
							tableNotUseByCategory.getTables().get(j).getHasParentTblFlg());
			
			if (tableNotUseByCategory.getTables().get(j).getHasParentTblFlg() == NotUseAtr.USE) {
				childTables.add(targetData);
			} else {
				parentTables.add(targetData);
			}
		}
		
		parentTables.sort((o2, o1) -> {
			return o1.getTableNo().compareTo(o2.getTableNo());
		});

		childTables.sort((o2, o1) -> {
			return o1.getTableNo().compareTo(o2.getTableNo());
		});
		
		listTablesOrder.addAll(childTables);
		listTablesOrder.addAll(parentTables);
		
		// テーブル一覧のカレントの1行分の項目を取得する
		for (DataRecoveryTable tableC : listTablesOrder) {
			
			//Optional<TableList> table = performDataRecoveryRepository.getByInternal(tableC.getFileNameCsv(), dataRecoveryProcessId);
			
			Optional<TableList> table = listTbl.stream().filter(tbl -> tbl.getTableEnglishName().equals(tableC.getTableEnglishName())).findFirst();
			
			if (!table.isPresent()) {
				return DataRecoveryOperatingCondition.ABNORMAL_TERMINATION;
			}
			
			// Get trạng thái domain データ復旧動作管理
			Optional<DataRecoveryMng> dataRecoveryMng = dataRecoveryMngRepository
					.getDataRecoveryMngById(dataRecoveryProcessId);
			if (dataRecoveryMng.isPresent() && dataRecoveryMng.get().getSuspendedState() == NotUseAtr.USE) {
				return DataRecoveryOperatingCondition.INTERRUPTION_END;
			}

			Set<String> hasSidInCsv = CsvFileUtil.getListSid(uploadId, table.get().getInternalFileName().toString());

			DataRecoveryTable dataRecoveryTable = new DataRecoveryTable(uploadId, table.get().getInternalFileName(),
																		hasSidInCsv.isEmpty() ? false : true, table.get().getTableEnglishName(), table.get().getTableJapaneseName(),
																		table.get().getTableNo(), table.get().getHasParentTblFlg());

			condition = exDataTabeRangeDate(dataRecoveryTable, table , dataRecoveryProcessId,
					csvByteReadMaper_TableNotUse);

			// Xác định trạng thái error
			if (condition == DataRecoveryOperatingCondition.ABNORMAL_TERMINATION) {
				return condition;
			}
		}
		
		return condition;
	}
	
	public DataRecoveryOperatingCondition exDataTabeRangeDate(DataRecoveryTable dataRecoveryTable,
			Optional<TableList> table, String dataRecoveryProcessId,
			HashMap<String, CSVBufferReader> csvByteReadMaper) throws ParseException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {

		// アルゴリズム「日付処理の設定」を実行し日付設定を取得する
		List<String> dateSetting = new ArrayList<>();
		try {
			dateSetting = this.settingDate(table);
		} catch (Exception e) {
			return DataRecoveryOperatingCondition.ABNORMAL_TERMINATION;
		}
		
		if (dateSetting.isEmpty()) {
			return DataRecoveryOperatingCondition.ABNORMAL_TERMINATION;
		}

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;

		// khởi tạo csv Reader
		String filePath = getExtractDataStoragePath(dataRecoveryTable.getUploadId()) + "//" + dataRecoveryTable.getFileNameCsv() + ".csv";
		CSVBufferReader reader = new CSVBufferReader(new File(filePath));
		reader.setCharset("UTF-8");
		csvByteReadMaper.put(dataRecoveryTable.getFileNameCsv(), reader);

		condition = this.processRecoverListTblByCompanyHandle.recoverDataOneTable(table.get(),dataRecoveryProcessId,condition,dataRecoveryTable,dateSetting,csvByteReadMaper);
		
		return condition;
	}


	@SuppressWarnings("unchecked")
	public List<String> settingDate(Optional<TableList> tableList) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// 「テーブル一覧」の抽出キー区から日付項目を設定する
		List<String> checkKeyQuery = new ArrayList<>();
		List<String> resultsSetting = new ArrayList<>();
		int countY = 0, countYm = 0, countYmd = 0;

		Optional<Object> keyQuery = Optional.empty();

		if (tableList.isPresent()) {
			for (int i = 1; i < 11; i++) {
				Method m1 = TableList.class.getMethod(GET_CLS_KEY_QUERY + i);
				keyQuery = (Optional<Object>) m1.invoke(tableList.get());
				if (keyQuery.isPresent() && !((String) keyQuery.get()).isEmpty()) {
					checkKeyQuery.add((String) keyQuery.get());
				}
			}
		}
		for (String currentKeyQuery : checkKeyQuery) {
			switch (currentKeyQuery) {
			case YEAR:
				countY++;
				break;
			case YEAR_MONTH:
				countYm++;
				break;
			case YEAR_MONTH_DAY:
				countYmd++;
				break;
			}
		}

		// không date
		if (countY == 0 && countYm == 0 && countYmd == 0) {
			resultsSetting.add(NONE_DATE);
		} else if (countY != 0 && countYm == 0 && countYmd == 0) {
			// năm hoặc phạm vi năm
			resultsSetting.add(YEAR);
			if (countY == 2) {
				resultsSetting.add(YEAR);
			}
		} else if (countY == 0 && countYm != 0 && countYmd == 0) {
			// tháng năm hoặc là phạm vi tháng năm
			resultsSetting.add(YEAR_MONTH);
			if (countYm == 2) {
				resultsSetting.add(YEAR_MONTH);
			}
		} else if (countY == 0 && countYm == 0 && countYmd != 0) {
			// ngày tháng năm hoặc phạm vi ngày tháng năm
			resultsSetting.add(YEAR_MONTH_DAY);
			if (countYmd == 2) {
				resultsSetting.add(YEAR_MONTH_DAY);
			}
		}
		return resultsSetting;
	}
	
	private List<List<String>> sortByDate(List<List<String>> targetDataTable) {
		List<List<String>> dataTableCus = new ArrayList<>();
		for (List<String> list : targetDataTable) {
			dataTableCus.add(list);
		}
		dataTableCus.remove(0);
		Collections.sort(dataTableCus, (o1, o2) -> {
			if (StringUtil.isNullOrEmpty(o1.get(2), true)) {
				return StringUtil.isNullOrEmpty(o2.get(2), true) ? 0 : -1;
			}
			if (StringUtil.isNullOrEmpty(o2.get(2), true)) {
				return 1;
			}
			return o1.get(2).compareTo(o2.get(2));
		});
		return dataTableCus;
	}


	static InputStream createInputStreamFromFile(String fileId, String fileName) {
		String filePath = getExtractDataStoragePath(fileId) + "//" + fileName + ".csv";
		try {
			return new FileInputStream(new File(filePath));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public static String getExtractDataStoragePath(String fileId) {
		return DATA_STORE_PATH + "//packs//" + fileId;
	}
}
