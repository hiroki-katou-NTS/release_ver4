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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nts.arc.system.ServerSystemProperties;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.csv.CSVBufferReader;
import nts.gul.csv.CSVParsedResult;
import nts.gul.csv.NtsCsvRecord;
import nts.gul.text.StringUtil;
import nts.uk.ctx.sys.assist.dom.category.Category;
import nts.uk.ctx.sys.assist.dom.category.CategoryRepository;
import nts.uk.ctx.sys.assist.dom.category.StorageRangeSaved;
import nts.uk.ctx.sys.assist.dom.category.TimeStore;
import nts.uk.ctx.sys.assist.dom.categoryfieldmt.HistoryDiviSion;
import nts.uk.ctx.sys.assist.dom.datarestoration.common.CsvFileUtil;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.i18n.TextResource;

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

	@Inject
	private DataRecoveryLogRepository repoDataRecoveryLog;

	private RecoveryStorageService self;

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

		// ログ連番をZeroクリア
		saveStartDataRecoverLog(dataRecoveryProcessId);

		// 処理対象のカテゴリを処理する
		for (Category category : listCategory) {

			List<TableList> tableUse = performDataRecoveryRepository.getByStorageRangeSaved(
					category.getCategoryId().v(), dataRecoveryProcessId, StorageRangeSaved.EARCH_EMP);
			List<TableList> tableNotUse = performDataRecoveryRepository.getByStorageRangeSaved(
					category.getCategoryId().v(), dataRecoveryProcessId, StorageRangeSaved.ALL_EMP);

			TableListByCategory tableListByCategory = new TableListByCategory(category.getCategoryId().v(), tableUse);
			TableListByCategory tableNotUseCategory = new TableListByCategory(category.getCategoryId().v(),
					tableNotUse);

			// カテゴリ単位の復旧
			condition = exCurrentCategory(tableListByCategory, tableNotUseCategory, uploadId, dataRecoveryProcessId);

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

	private void saveStartDataRecoverLog(String dataRecoveryProcessId) {
		GeneralDate logTime = GeneralDate.today();
		DataRecoveryLog resultLogDomain = DataRecoveryLog.createFromJavatype(dataRecoveryProcessId, null, null, logTime,
				0, null, null);
		repoDataRecoveryLog.add(resultLogDomain);
	}

	public DataRecoveryOperatingCondition exCurrentCategory(TableListByCategory tableListByCategory,
			TableListByCategory tableNotUseByCategory, String uploadId, String dataRecoveryProcessId) throws Exception {

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		// カテゴリの中の社員単位の処理

		condition = exTableUse(tableListByCategory, dataRecoveryProcessId, uploadId);

		if (condition == DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS) {
			// の処理対象社員コードをクリアする
			dataRecoveryMngRepository.updateProcessTargetEmpCode(dataRecoveryProcessId, null);

			// カテゴリの中の日付単位の処理
			condition = exTableNotUse(tableNotUseByCategory, dataRecoveryProcessId, uploadId);

		}

		dataRecoveryMngRepository.updateByOperatingCondition(dataRecoveryProcessId, condition);
		return condition;
	}

	/**
	 * Xử lý những table không phải dạng lịch sử 
	 */
	public DataRecoveryOperatingCondition exTableUse(TableListByCategory tableListByCategory,
			String dataRecoveryProcessId, String uploadId) throws Exception {

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		List<DataRecoveryTable> targetDataByCate = new ArrayList<>();

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
						tableListByCategory.getTables().get(j).getInternalFileName(), listSid.isEmpty() ? false : true);
				targetDataByCate.add(targetData);
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

			Optional<PerformDataRecovery> performDataRecovery = performDataRecoveryRepository
					.getPerformDatRecoverById(dataRecoveryProcessId);

			// Check recovery method [復旧方法]
			if (performDataRecovery.isPresent() && performDataRecovery.get().getRecoveryMethod() == RecoveryMethod.RESTORE_SELECTED_RANGE) {
				List<EmployeeDataReInfoImport> empSelectRange = listTarget.stream().map(i -> {
					return new EmployeeDataReInfoImport(null, null, i.getSid(), i.getScd().orElse(""), null, null,
							null);
				}).collect(Collectors.toList());

				if (empSelectRange.isEmpty())
					return DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
				this.forEmployee(dataRecoveryProcessId, empSelectRange, targetDataByCate, listTarget);

			} else {
				this.forEmployee(dataRecoveryProcessId, employeeInfos, targetDataByCate, listTarget);
			}
		}
		return condition;
	}

	@Transactional(value = TxType.REQUIRES_NEW, rollbackOn = Exception.class)
	public DataRecoveryOperatingCondition forEmployee(String dataRecoveryProcessId,
			List<EmployeeDataReInfoImport> employeeInfos, List<DataRecoveryTable> targetDataByCate,
			List<Target> listTarget) throws Exception {

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		String errorCode = "";

		HashMap<String, CSVBufferReader> csvByteReadMaper_TableUse = new HashMap<>();

		// khởi tạo csv Reader
		for (int i = 0; i < targetDataByCate.size(); i++) {
			String filePath = getExtractDataStoragePath(targetDataByCate.get(i).getUploadId()) + "//"
					+ targetDataByCate.get(i).getFileNameCsv() + ".csv";
			CSVBufferReader reader = new CSVBufferReader(new File(filePath));
			reader.setCharset("UTF-8");
			csvByteReadMaper_TableUse.put(targetDataByCate.get(i).getFileNameCsv(), reader);
		}

		int i = 0;
		// Foreach 対象社員コード＿ID
		for (EmployeeDataReInfoImport employeeDataMngInfoImport : employeeInfos) {

			long startTime = System.nanoTime();

			// Update current employeeCode
			dataRecoveryMngRepository.updateProcessTargetEmpCode(dataRecoveryProcessId,
					employeeDataMngInfoImport.getEmployeeCode());

			// 対象社員データ処理
			try {
				condition = self.recoveryDataByEmployee(dataRecoveryProcessId,
						employeeDataMngInfoImport.getEmployeeId(), targetDataByCate, listTarget,
						csvByteReadMaper_TableUse,employeeDataMngInfoImport.getEmployeeCode());
				long endTime = System.nanoTime();
				long duration = (endTime - startTime) / 1000000; // ms;
				System.out.println("== Employee :" + i++ + " == Time Restore => " + duration);
			} catch (Exception e) {
				errorCode = e.getMessage();
				NUMBER_ERROR++;
				dataRecoveryMngRepository.updateErrorCount(dataRecoveryProcessId, NUMBER_ERROR);
			}
			if (errorCode.equals(SETTING_EXCEPTION)) {
				return DataRecoveryOperatingCondition.ABNORMAL_TERMINATION;
			}
			// check interruption [中断]
			Optional<DataRecoveryMng> dataRecovery = dataRecoveryMngRepository
					.getDataRecoveryMngById(dataRecoveryProcessId);
			if (dataRecovery.isPresent() && dataRecovery.get().getSuspendedState() == NotUseAtr.USE) {
				return DataRecoveryOperatingCondition.INTERRUPTION_END;
			}
		}
		return condition;
	}

	public DataRecoveryOperatingCondition recoveryDataByEmployee(String dataRecoveryProcessId, String employeeId,
			List<DataRecoveryTable> targetDataByCate, List<Target> listTarget,
			HashMap<String, CSVBufferReader> csvByteReadMaper, String employeeCode) throws Exception {

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		Optional<PerformDataRecovery> performDataRecovery = performDataRecoveryRepository
				.getPerformDatRecoverById(dataRecoveryProcessId);

		// current target Data [カレント対象データ]
		int i = 1;
		for (DataRecoveryTable dataRecoveryTable : targetDataByCate) {
			Optional<TableList> tableList = performDataRecoveryRepository
					.getByInternal(dataRecoveryTable.getFileNameCsv(), dataRecoveryProcessId);
			// check date [日付処理の設定]

			List<String> resultsSetting = new ArrayList<>();
			resultsSetting = this.settingDate(tableList);
			if (resultsSetting.isEmpty()) {
				// GHI LOG
				String target            = employeeCode;
				String errorContent      = null;
				GeneralDate targetDate   = GeneralDate.today();
				String contentSql        = null;
				String processingContent = "日付処理の設定  "+TextResource.localize("CMF004_463") + " "+tableList.get().getTableJapaneseName();
				saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
				// #9005_2
				LOGGER.error("Setting error rollBack transaction");
				throw new Exception(SETTING_EXCEPTION);
			}

			// 履歴区分の判別する - check history division
			if (tableList.isPresent() && tableList.get().getHistoryCls() == HistoryDiviSion.HAVE_HISTORY) {
				try {
					deleteDataEmpTableHistory(tableList, true, employeeId, dataRecoveryProcessId, employeeCode);
				} catch (Exception err) {
					// GHI LOG
					String target			 = employeeCode;
					String errorContent		 = err.getMessage();
					GeneralDate targetDate   = GeneralDate.today();
					String contentSql        = err.getLocalizedMessage();
					String processingContent = "履歴データ削除";
					saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
					// #9005_1
					LOGGER.error("SQL error rollBack transaction");
					throw new Exception(SQL_EXCEPTION);
				}
			}

			try {
				// 対象社員の日付順の処理
				condition = crudDataByTable(dataRecoveryTable, employeeId, dataRecoveryProcessId, tableList,performDataRecovery, 
											resultsSetting, true, csvByteReadMaper, employeeCode);
			} catch (Exception e) {
				// GHI LOG
				String target            = employeeCode;
				String errorContent      = e.getMessage();
				GeneralDate targetDate   = GeneralDate.today();
				String contentSql        = e.getLocalizedMessage();
				String processingContent = "データベース復旧処理  " + tableList.get().getTableJapaneseName();
				saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
				// #9005_3
				// DELETE/INSERT error
				LOGGER.error("SQL error rollBack transaction " + employeeId);
				throw new Exception(SQL_EXCEPTION);
			}

			// Setting error
			if (condition == DataRecoveryOperatingCondition.ABNORMAL_TERMINATION) {
				LOGGER.error("Setting error rollBack transaction");
				throw new Exception(SETTING_EXCEPTION);
			}

		}
		return condition;
	}
	
	public DataRecoveryOperatingCondition crudDataByTable(DataRecoveryTable dataRecoveryTable, String employeeId,
			String dataRecoveryProcessId, Optional<TableList> tableList,
			Optional<PerformDataRecovery> performDataRecovery, List<String> dateSetting, Boolean tableUse,
			HashMap<String, CSVBufferReader> csvByteReadMaper, String employeeCode) throws ParseException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException , Exception{

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;

		// dieu chinh
		List<String> targetDataHeader = CsvFileUtil.getCsvHeader(dataRecoveryTable.getFileNameCsv(),
				dataRecoveryTable.getUploadId());

		List<DataRecoveryOperatingCondition> listCondition = new ArrayList<>();
		try {
			System.out.println("============= employeeId " + employeeId);
			if (employeeId != null && dataRecoveryTable.isHasSidInCsv()) {
				CSVBufferReader reader = csvByteReadMaper.get(dataRecoveryTable.getFileNameCsv());
				reader.setCharset("UTF-8");
				reader.readFilter(1000, dataRow -> {

					List<NtsCsvRecord> records = dataRow.getRecords();

					System.out.println("============= list record size " + records.size());
					if (!records.isEmpty()) {

						StringBuilder DELETE_INSERT_TO_TABLE = new StringBuilder("");
						StringBuilder INSERT_TO_TABLE = new StringBuilder("");

						List<Integer> listCount = new ArrayList<>();
						String namePhysicalCid = null, TABLE_NAME = null;
						List<Map<String, String>> listFiledWhere = new ArrayList<>();
						String cidCurrent = AppContexts.user().companyId();

						HashMap<Integer, String> indexAndFiled = new HashMap<>();
						// search data by employee

						if (tableList.isPresent()) {
							TABLE_NAME = tableList.get().getTableEnglishName();
							try {
								indexAndFiled = indexMapFiledCsv(targetDataHeader, tableList);
							} catch (NoSuchMethodException | SecurityException | IllegalAccessException
									| IllegalArgumentException | InvocationTargetException e) {
								e.printStackTrace();
							}
						}

						// 対象データの会社IDをパラメータの会社IDに入れ替える - swap CID
						// 既存データの検索
						try {
							namePhysicalCid = findNamePhysicalCid(tableList);
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException
								| IllegalArgumentException | InvocationTargetException e1) {
							e1.printStackTrace();
						}

						List<String> columnNotNull = checkTypeColumn(TABLE_NAME);
						int check = 0;
						for (int k = 0; k < records.size(); k++) {

							NtsCsvRecord row = records.get(k);

							int indexCidOfCsv = 0;
							int count = 0;

							// データベース復旧処理
							if ((tableUse && employeeId != null && !row.getColumn(INDEX_SID).equals(employeeId))) {
								continue;
							}

							// set câu insert
							StringBuilder INSERT_BY_TABLE = new StringBuilder("INSERT INTO ");

							INSERT_BY_TABLE.append(TABLE_NAME);
							INSERT_BY_TABLE.append(" (");

							StringBuilder values = new StringBuilder(" VALUES (");
							//

							String V_FILED_KEY_UPDATE = null, h_Date_Csv = null, dateSub = "";

							if (dateSetting.size() == 1) {
								h_Date_Csv = row.getColumn(INDEX_H_DATE).toString();
							} else if (dateSetting.size() == 2) {
								h_Date_Csv = row.getColumn(INDEX_H_START_DATE).toString();
							}

							// 履歴区分を判別する - check history division
							try {
								if (((tableUse && tableList.get().getHistoryCls() == HistoryDiviSion.NO_HISTORY)
									|| !tableUse) && (performDataRecovery.isPresent()
									&& performDataRecovery.get().getRecoveryMethod() == RecoveryMethod.RESTORE_SELECTED_RANGE
									&& !checkSettingDate(dateSetting, tableList, h_Date_Csv))) {
									continue;
								}
							} catch (Exception e) {
								String target            = employeeCode;
								String errorContent      = null;
								GeneralDate targetDate   = null;
								String contentSql        = null;
								String processingContent = "データの日付判別  " + TextResource.localize("CMF004_464"); 
								saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
								// #9007_1
								LOGGER.error("SQL error rollBack transaction " + employeeId);
								throw new RuntimeException(e);
							}

							// update recovery date for have history, save
							// range
							// none, year,
							// year/month, year/month/day
							if (!h_Date_Csv.isEmpty()) {
								if (tableList.get().getHistoryCls() == HistoryDiviSion.HAVE_HISTORY && tableUse)
									dateSub = dateTimeCutter(YEAR_MONTH_DAY, h_Date_Csv).orElse("");
								if (tableList.get().getRetentionPeriodCls() == TimeStore.FULL_TIME) {
									dateSub = "";
								}

								dateSub = dateTimeCutter(dateSetting.get(0), h_Date_Csv).orElse("");
							} else {
								dateSub = "";
							}

							dataRecoveryMngRepository.updateRecoveryDate(dataRecoveryProcessId, dateSub);

							// create filed where for query
							Map<String, String> filedWhere = new HashMap<>();
							for (Map.Entry<Integer, String> entry : indexAndFiled.entrySet()) {
								V_FILED_KEY_UPDATE = row.getColumn(entry.getKey()).toString();
								filedWhere.put(entry.getValue(), V_FILED_KEY_UPDATE);
							}
							listFiledWhere.add(filedWhere);

							if (tableUse) {
								count = performDataRecoveryRepository.countDataTransactionExitTableByVKeyUp(filedWhere,
										TABLE_NAME, namePhysicalCid, cidCurrent, dataRecoveryProcessId, employeeCode);
							} else {
								count = performDataRecoveryRepository.countDataExitTableByVKeyUp(filedWhere, TABLE_NAME,
										namePhysicalCid, cidCurrent, dataRecoveryProcessId, employeeCode);
							}

							if (count > 1 && tableUse) {
								listCondition.add(DataRecoveryOperatingCondition.ABNORMAL_TERMINATION);
							} else if (count > 1 && !tableUse) {
								continue;
							}

							indexCidOfCsv = targetDataHeader.indexOf(namePhysicalCid);

							for (int j = 5; j < row.columnLength(); j++) {
								String header_column_name = targetDataHeader.get(j);
								// add columns name
								INSERT_BY_TABLE.append(targetDataHeader.get(j) + ", ");
								boolean anyNonEmpty = columnNotNull.stream()
										.anyMatch(x -> x.equals(header_column_name));
								String value = j == indexCidOfCsv ? cidCurrent : row.getColumn(j).toString();
								// add values
								if (StringUtils.isEmpty(value)) {
									if (anyNonEmpty) {
										values.append("'',");
									} else {
										values.append("null,");
									}
								} else {
									values.append("N'" + value.replaceAll("\u00A0", "\"") + "' collate Japanese_XJIS_100_CI_AS_SC,");
								}
							}

							INSERT_BY_TABLE.append(")");
							INSERT_BY_TABLE.append(values.toString().replaceAll(",$", ")"));

							// query.executeUpdate();
							if (count == 1) {
								DELETE_INSERT_TO_TABLE.append(INSERT_BY_TABLE.toString() + " ");
							} else if (count == 0) {
								INSERT_TO_TABLE.append(INSERT_BY_TABLE.toString() + " ");
							}

							listCount.add(count);
							check++;
						}

						// insert delete data
						if (check > 0) {
							try {
								if (tableUse) {
									crudRowTransaction(listCount, listFiledWhere, TABLE_NAME, namePhysicalCid,cidCurrent, DELETE_INSERT_TO_TABLE, INSERT_TO_TABLE, employeeCode,
											dataRecoveryProcessId, tableList.get());
								} else {
									crudRow(listCount, listFiledWhere, TABLE_NAME, namePhysicalCid, cidCurrent,DELETE_INSERT_TO_TABLE, INSERT_TO_TABLE, employeeCode,
											dataRecoveryProcessId, tableList.get());
								}
							} catch (Exception e) {
								String target = employeeCode;
								String errorContent = e.getMessage();
								GeneralDate targetDate = GeneralDate.today();
								String contentSql = e.getLocalizedMessage();
								String processingContent = "データベース復旧処理 " + TextResource.localize("CMF004_465") + " "+ tableList.get().getTableJapaneseName();
								saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent,contentSql);
								LOGGER.info("Error delete or insert data for table " + TABLE_NAME);
								throw new RuntimeException(e);
							}
						}
					}
				}, 1, employeeId);

			} else {

				CSVBufferReader reader = csvByteReadMaper.get(dataRecoveryTable.getFileNameCsv());
				reader.setCharset("UTF-8");

				Consumer<CSVParsedResult> csvResult = (dataRow) -> {

					List<NtsCsvRecord> records = dataRow.getRecords();

					System.out.println("============= list record size " + records.size());
					if (!records.isEmpty()) {

						StringBuilder DELETE_INSERT_TO_TABLE = new StringBuilder("");
						StringBuilder INSERT_TO_TABLE = new StringBuilder("");

						List<Integer> listCount = new ArrayList<>();
						String namePhysicalCid = null, TABLE_NAME = null;
						List<Map<String, String>> listFiledWhere = new ArrayList<>();
						String cidCurrent = AppContexts.user().companyId();

						HashMap<Integer, String> indexAndFiled = new HashMap<>();
						// search data by employee

						if (tableList.isPresent()) {
							TABLE_NAME = tableList.get().getTableEnglishName();
							try {
								indexAndFiled = indexMapFiledCsv(targetDataHeader, tableList);
							} catch (NoSuchMethodException | SecurityException | IllegalAccessException
									| IllegalArgumentException | InvocationTargetException e) {
								e.printStackTrace();
							}
						}

						// 対象データの会社IDをパラメータの会社IDに入れ替える - swap CID
						// 既存データの検索
						try {
							namePhysicalCid = findNamePhysicalCid(tableList);
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException
								| IllegalArgumentException | InvocationTargetException e1) {
							e1.printStackTrace();
						}

						List<String> columnNotNull = checkTypeColumn(TABLE_NAME);
						int check = 0;
						for (int k = 0; k < records.size(); k++) {

							NtsCsvRecord row = records.get(k);

							if (row.getRowNumber() != 0) {
								int indexCidOfCsv = 0;
								int count = 0;

								// データベース復旧処理
								if ((tableUse && employeeId != null && !row.getColumn(INDEX_SID).equals(employeeId))) {
									continue;
								}

								// set câu insert
								StringBuilder INSERT_BY_TABLE = new StringBuilder("INSERT INTO ");

								INSERT_BY_TABLE.append(TABLE_NAME);
								INSERT_BY_TABLE.append(" (");

								StringBuilder values = new StringBuilder(" VALUES (");
								//

								String V_FILED_KEY_UPDATE = null, h_Date_Csv = null, dateSub = "";

								if (dateSetting.size() == 1) {
									h_Date_Csv = row.getColumn(INDEX_H_DATE).toString();
								} else if (dateSetting.size() == 2) {
									h_Date_Csv = row.getColumn(INDEX_H_START_DATE).toString();
								}

								// 履歴区分を判別する - check history division
								try {
									if (((tableUse && tableList.get().getHistoryCls() == HistoryDiviSion.NO_HISTORY)
										|| !tableUse)&& (performDataRecovery.isPresent() && performDataRecovery.get().getRecoveryMethod() == RecoveryMethod.RESTORE_SELECTED_RANGE
										&& !checkSettingDate(dateSetting, tableList, h_Date_Csv))) {
											continue;
									}
								} catch (Exception e) {
									String target            = null;
									String errorContent      = null;
									GeneralDate targetDate   = null;
									String contentSql        = null;
									String processingContent = "データの日付判別  " + TextResource.localize("CMF004_464"); 
									saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
									// #9007_1
									throw new RuntimeException(e);
								}

								// update recovery date for have history, save
								// range
								// none, year,
								// year/month, year/month/day
								if (!h_Date_Csv.isEmpty()) {
									if (tableList.get().getHistoryCls() == HistoryDiviSion.HAVE_HISTORY && tableUse)
										dateSub = dateTimeCutter(YEAR_MONTH_DAY, h_Date_Csv).orElse("");
									if (tableList.get().getRetentionPeriodCls() == TimeStore.FULL_TIME) {
										dateSub = "";
									}

									dateSub = dateTimeCutter(dateSetting.get(0), h_Date_Csv).orElse("");
								} else {
									dateSub = "";
								}

								dataRecoveryMngRepository.updateRecoveryDate(dataRecoveryProcessId, dateSub);

								// create filed where for query
								Map<String, String> filedWhere = new HashMap<>();
								for (Map.Entry<Integer, String> entry : indexAndFiled.entrySet()) {
									V_FILED_KEY_UPDATE = row.getColumn(entry.getKey()).toString();
									filedWhere.put(entry.getValue(), V_FILED_KEY_UPDATE);
								}
								listFiledWhere.add(filedWhere);

								if (tableUse) {
									count = performDataRecoveryRepository.countDataTransactionExitTableByVKeyUp(
											filedWhere, TABLE_NAME, namePhysicalCid, cidCurrent,dataRecoveryProcessId, employeeCode);
								} else {
									count = performDataRecoveryRepository.countDataExitTableByVKeyUp(filedWhere,
											TABLE_NAME, namePhysicalCid, cidCurrent,dataRecoveryProcessId, employeeCode);
								}

								if (count > 1 && tableUse) {
									listCondition.add(DataRecoveryOperatingCondition.ABNORMAL_TERMINATION);
								} else if (count > 1 && !tableUse) {
									continue;
								}

								indexCidOfCsv = targetDataHeader.indexOf(namePhysicalCid);

								for (int j = 5; j < row.columnLength(); j++) {

									String header_column_name = targetDataHeader.get(j);

									// add columns name
									INSERT_BY_TABLE.append(targetDataHeader.get(j) + ", ");
									boolean anyNonEmpty = columnNotNull.stream()
											.anyMatch(x -> x.equals(header_column_name));
									String value = j == indexCidOfCsv ? cidCurrent : row.getColumn(j).toString();
									// add values
									if (StringUtils.isEmpty(value)) {
										if (anyNonEmpty) {
											values.append("'',");
										} else {
											values.append("null,");
										}
									} else {
										values.append("N'" + value.replaceAll("\u00A0", "\"") + "' collate Japanese_XJIS_100_CI_AS_SC,");
									}
								}

								INSERT_BY_TABLE.append(")");
								INSERT_BY_TABLE.append(values.toString().replaceAll(",$", ")"));

								// query.executeUpdate();
								if (count == 1) {
									DELETE_INSERT_TO_TABLE.append(INSERT_BY_TABLE.toString() + " ");
								} else if (count == 0) {
									INSERT_TO_TABLE.append(INSERT_BY_TABLE.toString() + " ");
								}

								listCount.add(count);
								check++;
							}
						}

						// insert delete data
						if (check > 0) {
							try {
								if (tableUse) {
									crudRowTransaction(listCount, listFiledWhere, TABLE_NAME, namePhysicalCid,cidCurrent, DELETE_INSERT_TO_TABLE, INSERT_TO_TABLE, null,
													   dataRecoveryProcessId, tableList.get());
								} else {
									crudRow(listCount, listFiledWhere, TABLE_NAME, namePhysicalCid, cidCurrent,DELETE_INSERT_TO_TABLE, INSERT_TO_TABLE, null, dataRecoveryProcessId,
											tableList.get());
								}
							} catch (Exception e) {
								String target = employeeCode;
								String errorContent = e.getMessage();
								GeneralDate targetDate = GeneralDate.today();
								String contentSql = e.getLocalizedMessage();
								String processingContent = "データベース復旧処理 " + TextResource.localize("CMF004_465") + " " + tableList.get().getTableJapaneseName();
								saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate,processingContent, contentSql);
								LOGGER.info("Error delete or insert data for table " + TABLE_NAME);
								throw new RuntimeException(e);
							}
						}
					}
				};
				reader.readChunk(csvResult, null, null);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return listCondition.isEmpty() ? condition : listCondition.get(0);
	}

	public DataRecoveryOperatingCondition exTableNotUse(TableListByCategory tableNotUseByCategory,
			String dataRecoveryProcessId, String uploadId) throws Exception {

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		if (tableNotUseByCategory.getTables().size() == 0) {
			return condition;
		}

		// Create [対象データ] TargetData
		Set<String> hashId = new HashSet<>();
		List<DataRecoveryTable> targetDataByCate = new ArrayList<>();
		HashMap<String, CSVBufferReader> csvByteReadMaper_TableNotUse = new HashMap<>();
		for (int j = 0; j < tableNotUseByCategory.getTables().size(); j++) {

			Set<String> listSid = CsvFileUtil.getListSid(uploadId,
					tableNotUseByCategory.getTables().get(j).getInternalFileName());

			// -- Tổng hợp ID Nhân viên duy nhất từ List Data
			hashId.addAll(listSid);

			DataRecoveryTable targetData = new DataRecoveryTable(uploadId,
					tableNotUseByCategory.getTables().get(j).getInternalFileName(), listSid.isEmpty() ? false : true);

			targetDataByCate.add(targetData);

			String filePath = getExtractDataStoragePath(uploadId) + "//"
					+ tableNotUseByCategory.getTables().get(j).getInternalFileName() + ".csv";

			CSVBufferReader reader = new CSVBufferReader(new File(filePath));
			reader.setCharset("UTF-8");
			csvByteReadMaper_TableNotUse.put(tableNotUseByCategory.getTables().get(j).getInternalFileName(), reader);
		}

		// テーブル一覧のカレントの1行分の項目を取得する
		for (TableList tableList : tableNotUseByCategory.getTables()) {

			// Get trạng thái domain データ復旧動作管理
			Optional<DataRecoveryMng> dataRecoveryMng = dataRecoveryMngRepository
					.getDataRecoveryMngById(dataRecoveryProcessId);
			if (dataRecoveryMng.isPresent() && dataRecoveryMng.get().getSuspendedState() == NotUseAtr.USE) {
				return DataRecoveryOperatingCondition.INTERRUPTION_END;
			}

			Set<String> hasSidInCsv = CsvFileUtil.getListSid(uploadId, tableList.getInternalFileName().toString());

			DataRecoveryTable dataRecoveryTable = new DataRecoveryTable(uploadId, tableList.getInternalFileName(),
					hasSidInCsv.isEmpty() ? false : true);

			condition = exDataTabeRangeDate(dataRecoveryTable, Optional.of(tableList), dataRecoveryProcessId,
					csvByteReadMaper_TableNotUse);
			

			// Xác định trạng thái error
			if (condition == DataRecoveryOperatingCondition.ABNORMAL_TERMINATION) {
				return condition;
			}
		}

		return condition;
	}
	
	@Transactional(value = TxType.REQUIRES_NEW, rollbackOn = Exception.class)
	public DataRecoveryOperatingCondition exDataTabeRangeDate(DataRecoveryTable dataRecoveryTable,
			Optional<TableList> tableList, String dataRecoveryProcessId,
			HashMap<String, CSVBufferReader> csvByteReadMaper) throws ParseException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {

		// アルゴリズム「日付処理の設定」を実行し日付設定を取得する
		List<String> dateSetting = new ArrayList<>();
		dateSetting = this.settingDate(tableList);

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;
		if (dateSetting.isEmpty()) {
			return DataRecoveryOperatingCondition.ABNORMAL_TERMINATION;
		}

		// khởi tạo csv Reader
		String filePath = getExtractDataStoragePath(dataRecoveryTable.getUploadId()) + "//"
				+ dataRecoveryTable.getFileNameCsv() + ".csv";
		CSVBufferReader reader = new CSVBufferReader(new File(filePath));
		reader.setCharset("UTF-8");
		csvByteReadMaper.put(dataRecoveryTable.getFileNameCsv(), reader);

		if (tableList.isPresent()) {

			// 履歴区分の判別する - check phân loại lịch sử
			if (tableList.get().getHistoryCls() == HistoryDiviSion.HAVE_HISTORY) {
				try {
					deleteDataTableHistory(tableList, false, null, dataRecoveryProcessId);
				} catch (Exception e) {
					String target            = null;
					String errorContent      = e.getMessage();
					GeneralDate targetDate   = null;
					String contentSql        = e.getLocalizedMessage();
					String processingContent = "履歴データ削除 " +TextResource.localize("CMF004_462") + " " + tableList.get().getTableJapaneseName(); 
					saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
					// #9010_1
					
					LOGGER.info("Delete data of employee have history error");
					throw new Exception(SQL_EXCEPTION);
				}
			}

			Optional<PerformDataRecovery> performDataRecovery = performDataRecoveryRepository
					.getPerformDatRecoverById(dataRecoveryProcessId);
			try {
				condition = this.crudDataByTable(dataRecoveryTable, null, dataRecoveryProcessId, tableList,
						performDataRecovery, dateSetting, false, csvByteReadMaper, null);
			} catch (Exception e) {
				// GHI LOG
				String target            = null;
				String errorContent      = e.getMessage();
				GeneralDate targetDate   = GeneralDate.today();
				String contentSql        = e.getLocalizedMessage();
				String processingContent = "データベース復旧処理  "+ TextResource.localize("CMF004_465") + " " + tableList.get().getTableJapaneseName();
				saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
				// #9010_2
				// DELETE/INSERT error
				throw new Exception(SQL_EXCEPTION);

			}
		}
		return condition;
	}

	public List<String> checkTypeColumn(String TABLE_NAME) {
		List<String> data = performDataRecoveryRepository.getTypeColumnNotNull(TABLE_NAME);
		return data;
	}
	
	public void crudRow(List<Integer> listCount, List<Map<String, String>> lsiFiledWhere, String TABLE_NAME,
			String namePhysicalCid, String cidCurrent, StringBuilder deleteInsertToTable, StringBuilder insertToTable,
			String employeeCode, String dataRecoveryProcessId, TableList tableList) {
		try {
			List<Map<String, String>> listFiledWhereToDelAndInsert = new ArrayList<>();
			List<Map<String, String>> listFiledWhereToInsert = new ArrayList<>();
			for (int i = 0; i < listCount.size(); i++) {
				if (listCount.get(i) == 1) {
					listFiledWhereToDelAndInsert.add(lsiFiledWhere.get(i));
				} else if (listCount.get(i) == 0) {
					listFiledWhereToInsert.add(lsiFiledWhere.get(i));
				}
			}
			if (listFiledWhereToDelAndInsert.size() > 0) {
				// truong hop ban ghi do van con ton tai trong database : thi
				// xoa di va insert lai
				performDataRecoveryRepository.deleteDataExitTableByVkey(listFiledWhereToDelAndInsert, TABLE_NAME,
						namePhysicalCid, cidCurrent, employeeCode, dataRecoveryProcessId);

				performDataRecoveryRepository.insertDataTable(deleteInsertToTable, employeeCode, dataRecoveryProcessId);
			}
			if (listFiledWhereToInsert.size() > 0) {
				// truong hop ban ghi do bi xoa di roi : thi chỉ cần insert vào thôi.
				performDataRecoveryRepository.insertDataTable(insertToTable, employeeCode, dataRecoveryProcessId);
			}
		} catch (Exception e) {
			String target = employeeCode;
			String errorContent = e.getMessage();
			GeneralDate targetDate = GeneralDate.today();
			String contentSql = e.getLocalizedMessage();
			String processingContent = "データベース復旧処理 " + TextResource.localize("CMF004_465");
			saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent,contentSql);
			LOGGER.info("Error delete or insert data for table " + TABLE_NAME);
			throw e;
		}
	}

	public void crudRowTransaction(List<Integer> listCount, List<Map<String, String>> lsiFiledWhere, String TABLE_NAME,
			String namePhysicalCid, String cidCurrent, StringBuilder deleteInsertToTable, StringBuilder insertToTable,
			String employeeCode, String dataRecoveryProcessId, TableList tableList) {
		try {
			List<Map<String, String>> listFiledWhereToDelAndInsert = new ArrayList<>();
			List<Map<String, String>> listFiledWhereToInsert = new ArrayList<>();
			for (int i = 0; i < listCount.size(); i++) {
				if (listCount.get(i) == 1) {
					listFiledWhereToDelAndInsert.add(lsiFiledWhere.get(i));
				} else if (listCount.get(i) == 0) {
					listFiledWhereToInsert.add(lsiFiledWhere.get(i));
				}
			}
			if (listFiledWhereToDelAndInsert.size() > 0) {
				// truong hop ban ghi do van con ton tai trong database : thi
				// xoa di va insert lai
				performDataRecoveryRepository.deleteTransactionDataExitTableByVkey(listFiledWhereToDelAndInsert,
						TABLE_NAME, namePhysicalCid, cidCurrent, employeeCode, dataRecoveryProcessId);

				performDataRecoveryRepository.insertTransactionDataTable(deleteInsertToTable, employeeCode, dataRecoveryProcessId);
			}
			if (listFiledWhereToInsert.size() > 0) {

				performDataRecoveryRepository.insertTransactionDataTable(insertToTable, employeeCode, dataRecoveryProcessId);
			}

		} catch (Exception e) {
			String target = employeeCode;
			String errorContent = e.getMessage();
			GeneralDate targetDate = GeneralDate.today();
			String contentSql = e.getLocalizedMessage();
			String processingContent = "データベース復旧処理 " + TextResource.localize("CMF004_465");
			saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent,contentSql);
			LOGGER.info("Error delete  or insert data for table " + TABLE_NAME);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public String findNamePhysicalCid(Optional<TableList> tableList) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		String namePhysical = null;
		if (tableList.isPresent()) {

			Optional<Object> keyQuery = Optional.empty();
			Optional<Object> filedKey = Optional.empty();
			for (int i = 1; i < 11; i++) {
				Method m1 = TableList.class.getMethod(GET_CLS_KEY_QUERY + i);
				keyQuery = (Optional<Object>) m1.invoke(tableList.get());
				Method m2 = TableList.class.getMethod(GET_FILED_KEY_QUERY + i);
				filedKey = (Optional<Object>) m2.invoke(tableList.get());
				if (keyQuery.isPresent()) {
					if (keyQuery.get().equals(INDEX_CID_CSV)) {
						namePhysical = (String) filedKey.get();
					}
				}
			}

		}
		return namePhysical;
	}

	@SuppressWarnings("unchecked")
	public void deleteDataEmpTableHistory(Optional<TableList> tableList, Boolean tableNotUse, String employeeId,
			String dataRecoveryProcessId, String employeeCode) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, Exception {
		if (!tableList.isPresent()) {
			return;
		}
		// Delete history

		String[] whereCid = { "" };
		String[] whereSid = { "" };

		//
		Optional<Object> keyQuery;
		Optional<Object> filedKey;
		for (int i = 1; i < 11; i++) {
			Method m1 = TableList.class.getMethod(GET_CLS_KEY_QUERY + i);
			keyQuery = (Optional<Object>) m1.invoke(tableList.get());
			Method m2 = TableList.class.getMethod(GET_FILED_KEY_QUERY + i);
			filedKey = (Optional<Object>) m2.invoke(tableList.get());
			if (keyQuery.isPresent()) {
				if (keyQuery.get().equals(INDEX_CID_CSV)) {
					whereCid[0] = (String) filedKey.get();
				} else if (keyQuery.get().equals(INDEX_SID_CSV) && tableNotUse) {
					whereSid[0] = (String) filedKey.get();
				}
			}
		}

		String cidCurrent = AppContexts.user().companyId();
		try {
			if (tableNotUse) {
				performDataRecoveryRepository.deleteTransactionEmployeeHis(tableList.get(), whereCid[0], whereSid[0],
						cidCurrent, employeeId);
			} else {
				performDataRecoveryRepository.deleteEmployeeHis(tableList.get(), whereCid[0], whereSid[0], cidCurrent,
						employeeId);
			}
		} catch (Exception err) {
			String target            = employeeCode;
			String errorContent      = err.getMessage();
			GeneralDate targetDate   = null;
			String contentSql        = err.getLocalizedMessage();
			String processingContent = "履歴データ削除  " +TextResource.localize("CMF004_462"); 
			saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
			// #9006
			LOGGER.error("SQL error rollBack transaction");
			throw new Exception(SQL_EXCEPTION);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void deleteDataTableHistory(Optional<TableList> tableList, Boolean tableNotUse, String employeeId,
			String dataRecoveryProcessId) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, Exception {
		if (!tableList.isPresent()) {
			return;
		}
		// Delete history

		String[] whereCid = { "" };
		String[] whereSid = { "" };

		//
		Optional<Object> keyQuery;
		Optional<Object> filedKey;
		for (int i = 1; i < 11; i++) {
			Method m1 = TableList.class.getMethod(GET_CLS_KEY_QUERY + i);
			keyQuery = (Optional<Object>) m1.invoke(tableList.get());
			Method m2 = TableList.class.getMethod(GET_FILED_KEY_QUERY + i);
			filedKey = (Optional<Object>) m2.invoke(tableList.get());
			if (keyQuery.isPresent()) {
				if (keyQuery.get().equals(INDEX_CID_CSV)) {
					whereCid[0] = (String) filedKey.get();
				} else if (keyQuery.get().equals(INDEX_SID_CSV) && tableNotUse) {
					whereSid[0] = (String) filedKey.get();
				}
			}
		}

		String cidCurrent = AppContexts.user().companyId();
		try {
			if (tableNotUse) {
				performDataRecoveryRepository.deleteTransactionEmployeeHis(tableList.get(), whereCid[0], whereSid[0],
						cidCurrent, employeeId);
			} else {
				performDataRecoveryRepository.deleteEmployeeHis(tableList.get(), whereCid[0], whereSid[0], cidCurrent,
						employeeId);
			}
		} catch (Exception err) {
			String target            = null;
			String errorContent      = err.getMessage();
			GeneralDate targetDate   = null;
			String contentSql        = err.getLocalizedMessage();
			String processingContent = "履歴データ削除  " +TextResource.localize("CMF004_462") + " " + tableList.get().getTableJapaneseName(); 
			saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
			// #9011
			LOGGER.error("SQL error rollBack transaction");
			throw new Exception(SQL_EXCEPTION);
		}
	}

	private void saveErrorLogDataRecover(String recoveryProcessId, String target, String errorContent,
			GeneralDate targetDate, String processingContent, String contentSql) {
		int logSequenceNumber = repoDataRecoveryLog.getMaxSeqId(recoveryProcessId) + 1;
		DataRecoveryLog dataRecoveryLog = new DataRecoveryLog(recoveryProcessId, target, errorContent, targetDate,
				logSequenceNumber, processingContent, contentSql);
		repoDataRecoveryLog.add(dataRecoveryLog);
	}

	@SuppressWarnings("unchecked")
	public List<String> settingDate(Optional<TableList> tableList) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// 「テーブル一覧」の抽出キー区から日付項目を設定する
		List<String> checkKeyQuery = new ArrayList<>();
		List<String> resultsSetting = new ArrayList<>();
		TimeStore timeStore = null;
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

			timeStore = tableList.get().getRetentionPeriodCls();
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

	public Boolean checkSettingDate(List<String> resultsSetting, Optional<TableList> tableList, String h_Date_Csv)
			throws ParseException {

		if (StringUtil.isNullOrEmpty(h_Date_Csv, true)) {
			return false;
		}

		GeneralDate hDateCsv = stringToGenaralDate(h_Date_Csv);
		GeneralDate dateFrom = stringToGenaralDate(tableList.get().getSaveDateFrom().get());
		GeneralDate dateTo = stringToGenaralDate(tableList.get().getSaveDateTo().get());

		if (YEAR.equals(resultsSetting.get(0))
				&& (hDateCsv.year() < dateFrom.year() || hDateCsv.year() > dateTo.year())) {
			return false;
		} else if (YEAR_MONTH.equals(resultsSetting.get(0))) {
			if ((dateFrom.year() > hDateCsv.year()
					|| (dateFrom.year() == hDateCsv.year() && hDateCsv.month() < dateFrom.month()))
					|| (dateTo.year() < hDateCsv.year()
							|| (dateTo.year() == hDateCsv.year() && hDateCsv.month() > dateTo.month()))) {
				return false;
			}
		} else if (YEAR_MONTH_DAY.equals(resultsSetting.get(0))
				&& (dateFrom.after(hDateCsv) || dateTo.before(hDateCsv))) {
			return false;
		}
		return true;
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

	@SuppressWarnings("unchecked")
	private HashMap<Integer, String> indexMapFiledCsv(List<String> targetDataHeader, Optional<TableList> tableList)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		HashMap<Integer, String> indexfiledUpdate = new HashMap<>();
		String[] whereCid = { "" };
		Integer index = null;
		Optional<Object> filedKey = Optional.empty();
		for (int i = 1; i < 21; i++) {
			Method m2 = TableList.class.getMethod(GET_FILED_KEY_UPDATE + i);
			filedKey = (Optional<Object>) m2.invoke(tableList.get());
			if (filedKey.isPresent()) {
				whereCid[0] = (String) filedKey.get();
				if (!whereCid[0].isEmpty()) {
					index = targetDataHeader.indexOf((String) filedKey.get());
					indexfiledUpdate.put(index, whereCid[0]);
				}
			}
		}

		return indexfiledUpdate;
	}

	/**
	 * Cut String by type
	 * 
	 * @param type
	 *            RangeType
	 * @param datetime
	 * @return Optional<String>
	 */
	private Optional<String> dateTimeCutter(String type, String datetime) {
		return datetimeRange.containsKey(type)
				? Optional.of(stringToGenaralDate(datetime).toString().substring(0, datetimeRange.get(type)))
				: Optional.empty();
	}

	/**
	 * Convert String to GeneralDate
	 * 
	 * @param datetime
	 * @return GeneralDate
	 */
	private GeneralDate stringToGenaralDate(String datetime) {
		if (StringUtil.isNullOrEmpty(datetime, true)) {
			return null;
		}
		if (datetime.replaceAll("[^\\d.]", "").length() == 6) {
			datetime = datetime + "/01";
		} else if (datetime.replaceAll("[^\\d.]", "").length() == 4) {
			datetime = datetime + "/01/01";
		}
		return GeneralDate.fromString(datetime.replaceAll("[^\\d.]", "").substring(0, 8), DATE_FORMAT);
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
