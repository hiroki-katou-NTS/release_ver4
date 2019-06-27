package nts.uk.ctx.sys.assist.dom.datarestoration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nts.arc.system.ServerSystemProperties;
import nts.arc.time.GeneralDate;
import nts.gul.csv.CSVBufferReader;
import nts.gul.csv.CSVParsedResult;
import nts.gul.csv.NtsCsvRecord;
import nts.gul.text.StringUtil;
import nts.uk.ctx.sys.assist.dom.category.TimeStore;
import nts.uk.ctx.sys.assist.dom.categoryfieldmt.HistoryDiviSion;
import nts.uk.ctx.sys.assist.dom.datarestoration.common.CsvFileUtil;
import nts.uk.ctx.sys.assist.dom.tablelist.TableList;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;


@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ProcessRecoverListTblByCompanyHandle {
	
	@Inject
	private PerformDataRecoveryRepository performDataRecoveryRepository;

	@Inject
	private DataRecoveryMngRepository dataRecoveryMngRepository;

	@Inject
	private SaveLogDataRecoverServices saveLogDataRecoverServices; 
	

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

	
	public DataRecoveryOperatingCondition recoverDataOneTable(TableList table, String dataRecoveryProcessId,DataRecoveryOperatingCondition condition,
			DataRecoveryTable dataRecoveryTable,List<String> dateSetting,HashMap<String, CSVBufferReader> csvByteReadMaper) throws Exception {

		// 履歴区分の判別する - check phân loại lịch sử
		if (table.getHistoryCls() == HistoryDiviSion.HAVE_HISTORY) {
			try {
				deleteDataTableHistory(table, false, null, dataRecoveryProcessId);
				System.out.println("DELETE TABLE BY COMPANY : " + table.getTableEnglishName());
			} catch (Exception e) {
				String target            = null;
				String errorContent      = e.getMessage();
				GeneralDate targetDate   = null;
				String contentSql        = e.getMessage();
				String processingContent = "履歴データ削除 " +TextResource.localize("CMF004_462") + " " + table.getTableJapaneseName(); 
				saveLogDataRecoverServices.saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
				// #9010_1
				LOGGER.info("Delete data of employee have history error");
				throw new Exception(SQL_EXCEPTION);
			}
		}

		Optional<PerformDataRecovery> performDataRecovery = performDataRecoveryRepository
				.getPerformDatRecoverById(dataRecoveryProcessId);
		try {
			condition = this.crudDataByTable(dataRecoveryTable, null, dataRecoveryProcessId, table,
					performDataRecovery, dateSetting, false, csvByteReadMaper, null);
		} catch (Exception e) {
			// GHI LOG
			String target            = null;
			String errorContent      = e.getMessage();
			GeneralDate targetDate   = GeneralDate.today();
			String contentSql        = e.getMessage();
			String processingContent = "データベース復旧処理  " +TextResource.localize("CMF004_465") + " " + table.getTableJapaneseName();
			saveLogDataRecoverServices.saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
			// #9010_2
			// DELETE/INSERT error
			throw new Exception(SQL_EXCEPTION);

		}
		
		return condition;	
	}
	
	@SuppressWarnings("unchecked")
	public void deleteDataTableHistory(TableList tableList, Boolean tableNotUse, String employeeId,
			String dataRecoveryProcessId) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, Exception {

		String[] whereCid = { "" };
		String[] whereSid = { "" };

		//
		Optional<Object> keyQuery;
		Optional<Object> filedKey;
		for (int i = 1; i < 11; i++) {
			Method m1 = TableList.class.getMethod(GET_CLS_KEY_QUERY + i);
			keyQuery = (Optional<Object>) m1.invoke(tableList);
			Method m2 = TableList.class.getMethod(GET_FILED_KEY_QUERY + i);
			filedKey = (Optional<Object>) m2.invoke(tableList);
			if (keyQuery.isPresent()) {
				if (keyQuery.get().equals(INDEX_CID_CSV)) {
					whereCid[0] = (String) filedKey.get();
				} else if (keyQuery.get().equals(INDEX_SID_CSV) && tableNotUse) {
					whereSid[0] = (String) filedKey.get();
				}
			}
		}

		String cidCurrent  = AppContexts.user().companyId();
		try {
			if (tableNotUse) {
				performDataRecoveryRepository.deleteTransactionEmployeeHis(tableList, whereCid[0], whereSid[0],cidCurrent, employeeId);
			} else {
				performDataRecoveryRepository.deleteEmployeeHis(tableList, whereCid[0], whereSid[0], cidCurrent,employeeId);
			}
		} catch (Exception err) {
			String target            = null;
			String errorContent      = err.getMessage();
			GeneralDate targetDate   = null;
			String contentSql        = err.getMessage();
			String processingContent = "履歴データ削除  " +TextResource.localize("CMF004_462") + " " + tableList.getTableJapaneseName(); 
			saveLogDataRecoverServices.saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
			// #9011
			throw err;
		}
	}
	
	public DataRecoveryOperatingCondition crudDataByTable(DataRecoveryTable dataRecoveryTable, String employeeId,
			String dataRecoveryProcessId, TableList table,
			Optional<PerformDataRecovery> performDataRecovery, List<String> dateSetting, Boolean tableUse,
			HashMap<String, CSVBufferReader> csvByteReadMaper, String employeeCode) throws ParseException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException , Exception{

		DataRecoveryOperatingCondition condition = DataRecoveryOperatingCondition.FILE_READING_IN_PROGRESS;

		// dieu chinh
		List<String> targetDataHeader = CsvFileUtil.getCsvHeader(dataRecoveryTable.getFileNameCsv(),
				dataRecoveryTable.getUploadId());

		List<DataRecoveryOperatingCondition> listCondition = new ArrayList<>();
		try {
			System.out.println("=== empCode " + employeeCode);
			System.out.println("=== RECOVER: table No "+ table.getTableNo() +" table Name: "+ table.getTableEnglishName());
			if (employeeId != null && dataRecoveryTable.isHasSidInCsv()) {
				CSVBufferReader reader = csvByteReadMaper.get(dataRecoveryTable.getFileNameCsv());
				reader.setCharset("UTF-8");
				reader.readFilter(1000, dataRow -> {

					List<NtsCsvRecord> records = dataRow.getRecords();
					if (!records.isEmpty()) {

						StringBuilder DELETE_INSERT_TO_TABLE = new StringBuilder("");
						StringBuilder INSERT_TO_TABLE = new StringBuilder("");

						List<Integer> listCount = new ArrayList<>();
						String namePhysicalCid = null, TABLE_NAME = null;
						List<Map<String, String>> listFiledWhere = new ArrayList<>();
						String cidCurrent = AppContexts.user().companyId();

						HashMap<Integer, String> indexAndFiled = new HashMap<>();
						// search data by employee

						
						TABLE_NAME = table.getTableEnglishName();
						try {
							indexAndFiled = indexMapFiledCsv(targetDataHeader, table);
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException
								| IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
						

						// 対象データの会社IDをパラメータの会社IDに入れ替える - swap CID
						// 既存データの検索
						try {
							namePhysicalCid = findNamePhysicalCid(table);
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
								if (((tableUse && table.getHistoryCls() == HistoryDiviSion.NO_HISTORY)
									|| !tableUse) && (performDataRecovery.isPresent()
									&& performDataRecovery.get().getRecoveryMethod() == RecoveryMethod.RESTORE_SELECTED_RANGE
									&& !checkSettingDate(dateSetting, table, h_Date_Csv))) {
									continue;
								}
							} catch (Exception e) {
								String target            = employeeCode;
								String errorContent      = null;
								GeneralDate targetDate   = null;
								String contentSql        = null;
								String processingContent = "データの日付判別  " + TextResource.localize("CMF004_464") + table.getTableJapaneseName(); 
								saveLogDataRecoverServices.saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
								// #9007_1
								LOGGER.error("SQL error rollBack transaction " + employeeId);
								throw new RuntimeException(e);
							}

							// update recovery date for have history, save
							// range
							// none, year,
							// year/month, year/month/day
							if (!h_Date_Csv.isEmpty()) {
								if (table.getHistoryCls() == HistoryDiviSion.HAVE_HISTORY && tableUse)
									dateSub = dateTimeCutter(YEAR_MONTH_DAY, h_Date_Csv).orElse("");
								if (table.getRetentionPeriodCls() == TimeStore.FULL_TIME) {
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
											dataRecoveryProcessId, table);
								} else {
									crudRow(listCount, listFiledWhere, TABLE_NAME, namePhysicalCid, cidCurrent,DELETE_INSERT_TO_TABLE, INSERT_TO_TABLE, employeeCode,
											dataRecoveryProcessId, table);
								}
							} catch (Exception e) {
								String target = employeeCode;
								String errorContent = e.getMessage();
								GeneralDate targetDate = GeneralDate.today();
								String contentSql = e.getMessage();
								String processingContent = "データベース復旧処理 " + TextResource.localize("CMF004_465") + " "+ table.getTableJapaneseName();
								saveLogDataRecoverServices.saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent,contentSql);
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
					if (!records.isEmpty()) {

						StringBuilder DELETE_INSERT_TO_TABLE = new StringBuilder("");
						StringBuilder INSERT_TO_TABLE = new StringBuilder("");

						List<Integer> listCount = new ArrayList<>();
						String namePhysicalCid = null, TABLE_NAME = null;
						List<Map<String, String>> listFiledWhere = new ArrayList<>();
						String cidCurrent = AppContexts.user().companyId();

						HashMap<Integer, String> indexAndFiled = new HashMap<>();
						// search data by employee

						
						TABLE_NAME = table.getTableEnglishName();
						try {
							indexAndFiled = indexMapFiledCsv(targetDataHeader, table);
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException
								| IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}

						// 対象データの会社IDをパラメータの会社IDに入れ替える - swap CID
						// 既存データの検索
						try {
							namePhysicalCid = findNamePhysicalCid(table);
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
									if (((tableUse && table.getHistoryCls() == HistoryDiviSion.NO_HISTORY)
										|| !tableUse)&& (performDataRecovery.isPresent() && performDataRecovery.get().getRecoveryMethod() == RecoveryMethod.RESTORE_SELECTED_RANGE
										&& !checkSettingDate(dateSetting, table, h_Date_Csv))) {
											continue;
									}
								} catch (Exception e) {
									String target            = null;
									String errorContent      = null;
									GeneralDate targetDate   = null;
									String contentSql        = null;
									String processingContent = "データの日付判別  " + TextResource.localize("CMF004_464") + table.getTableJapaneseName(); 
									saveLogDataRecoverServices.saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent, contentSql);
									// #9007_1
									throw new RuntimeException(e);
								}

								// update recovery date for have history, save
								// range
								// none, year,
								// year/month, year/month/day
								if (!h_Date_Csv.isEmpty()) {
									if (table.getHistoryCls() == HistoryDiviSion.HAVE_HISTORY && tableUse)
										dateSub = dateTimeCutter(YEAR_MONTH_DAY, h_Date_Csv).orElse("");
									if (table.getRetentionPeriodCls() == TimeStore.FULL_TIME) {
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
													   dataRecoveryProcessId, table);
								} else {
									crudRow(listCount, listFiledWhere, TABLE_NAME, namePhysicalCid, cidCurrent,DELETE_INSERT_TO_TABLE, INSERT_TO_TABLE, null, dataRecoveryProcessId,
											table);
								}
							} catch (Exception e) {
								String target = employeeCode;
								String errorContent = e.getMessage();
								GeneralDate targetDate = GeneralDate.today();
								String contentSql = e.getMessage();
								String processingContent = "データベース復旧処理 " + TextResource.localize("CMF004_465") + " " + table.getTableJapaneseName();
								saveLogDataRecoverServices.saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate,processingContent, contentSql);
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
			String contentSql = e.getMessage();
			String processingContent = "データベース復旧処理 " + TextResource.localize("CMF004_465");
			saveLogDataRecoverServices.saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent,contentSql);
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
			String contentSql = e.getMessage();
			String processingContent = "データベース復旧処理 " + TextResource.localize("CMF004_465");
			saveLogDataRecoverServices.saveErrorLogDataRecover(dataRecoveryProcessId, target, errorContent, targetDate, processingContent,contentSql);
			LOGGER.info("Error delete  or insert data for table " + TABLE_NAME);
			throw e;
		}
	}
	
	
	private Optional<String> dateTimeCutter(String type, String datetime) {
		return datetimeRange.containsKey(type)
				? Optional.of(stringToGenaralDate(datetime).toString().substring(0, datetimeRange.get(type)))
				: Optional.empty();
	}
	
	public Boolean checkSettingDate(List<String> resultsSetting, TableList tableList, String h_Date_Csv)
			throws ParseException {

		if (StringUtil.isNullOrEmpty(h_Date_Csv, true)) {
			return false;
		}

		GeneralDate hDateCsv = stringToGenaralDate(h_Date_Csv);
		GeneralDate dateFrom = stringToGenaralDate(tableList.getSaveDateFrom().get());
		GeneralDate dateTo = stringToGenaralDate(tableList.getSaveDateTo().get());

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
	
	@SuppressWarnings("unchecked")
	public List<String> settingDate(TableList tableList) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// 「テーブル一覧」の抽出キー区から日付項目を設定する
		List<String> checkKeyQuery = new ArrayList<>();
		List<String> resultsSetting = new ArrayList<>();
		int countY = 0, countYm = 0, countYmd = 0;

		Optional<Object> keyQuery = Optional.empty();

		for (int i = 1; i < 11; i++) {
			Method m1 = TableList.class.getMethod(GET_CLS_KEY_QUERY + i);
			keyQuery = (Optional<Object>) m1.invoke(tableList);
			if (keyQuery.isPresent() && !((String) keyQuery.get()).isEmpty()) {
				checkKeyQuery.add((String) keyQuery.get());
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
	
	public List<String> checkTypeColumn(String TABLE_NAME) {
		List<String> data = performDataRecoveryRepository.getTypeColumnNotNull(TABLE_NAME);
		return data;
	}
	
	@SuppressWarnings("unchecked")
	public String findNamePhysicalCid(TableList tableList) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		String namePhysical = null;

		Optional<Object> keyQuery = Optional.empty();
		Optional<Object> filedKey = Optional.empty();
		for (int i = 1; i < 11; i++) {
			Method m1 = TableList.class.getMethod(GET_CLS_KEY_QUERY + i);
			keyQuery = (Optional<Object>) m1.invoke(tableList);
			Method m2 = TableList.class.getMethod(GET_FILED_KEY_QUERY + i);
			filedKey = (Optional<Object>) m2.invoke(tableList);
			if (keyQuery.isPresent()) {
				if (keyQuery.get().equals(INDEX_CID_CSV)) {
					namePhysical = (String) filedKey.get();
				}
			}
		}

		return namePhysical;
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<Integer, String> indexMapFiledCsv(List<String> targetDataHeader, TableList tableList)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		HashMap<Integer, String> indexfiledUpdate = new HashMap<>();
		String[] whereCid = { "" };
		Integer index = null;
		Optional<Object> filedKey = Optional.empty();
		for (int i = 1; i < 21; i++) {
			Method m2 = TableList.class.getMethod(GET_FILED_KEY_UPDATE + i);
			filedKey = (Optional<Object>) m2.invoke(tableList);
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
	
	
	public static String getExtractDataStoragePath(String fileId) {
		return DATA_STORE_PATH + "//packs//" + fileId;
	}

}
