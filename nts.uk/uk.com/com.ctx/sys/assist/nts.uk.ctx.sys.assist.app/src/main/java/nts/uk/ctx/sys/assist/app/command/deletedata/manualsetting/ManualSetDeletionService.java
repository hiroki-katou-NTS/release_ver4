/**
 * 
 */
package nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.layer.infra.file.temp.ApplicationTemporaryFileFactory;
import nts.arc.layer.infra.file.temp.ApplicationTemporaryFilesContainer;
import nts.arc.time.GeneralDateTime;
import nts.gul.security.crypt.commonkey.CommonKeyCrypt;
import nts.uk.ctx.sys.assist.dom.categoryfieldmtfordelete.CategoryFieldMtForDelRepository;
import nts.uk.ctx.sys.assist.dom.categoryfieldmtfordelete.CategoryFieldMtForDelete;
import nts.uk.ctx.sys.assist.dom.categoryfordelete.CategoryForDelete;
import nts.uk.ctx.sys.assist.dom.categoryfordelete.CategoryForDeleteRepository;
import nts.uk.ctx.sys.assist.dom.categoryfordelete.TimeStore;
import nts.uk.ctx.sys.assist.dom.deletedata.CategoryDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.CategoryDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionCsvRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.DelType;
import nts.uk.ctx.sys.assist.dom.deletedata.EmployeeDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.EmployeesDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.FileName;
import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ManagementDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.ManualSetDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.OperatingCondition;
import nts.uk.ctx.sys.assist.dom.deletedata.Result;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultLogDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultLogDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultState;
import nts.uk.ctx.sys.assist.dom.deletedata.SaveStatus;
import nts.uk.ctx.sys.assist.dom.deletedata.TableDeletionDataCsv;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.csv.CSVReportGenerator;
import nts.uk.shr.infra.file.csv.CsvReportWriter;

/**
 * @author hiep.th
 *
 */
@Stateless
public class ManualSetDeletionService extends ExportService<Object>{
	private static final String MSG_START_DEL_LOG = "CMF005_213";
	private static final String MSG_END_NORMAL_DEL_LOG = "CMF005_214";
	private static final String MSG_END_ABNORMAL_DEL_LOG = "CMF005_215";
	private static final String MSG_END_TERMINATE_DEL_LOG = "CMF005_216";

	private static final List<String> LST_NAME_ID_HEADER_TABLE_FILE = Arrays.asList("CMF003_500", "CMF003_501",
			"CMF003_502", "CMF003_503", "CMF003_504", "CMF003_505", "CMF003_506", "CMF003_507", "CMF003_508",
			"CMF003_509", "CMF003_510", "CMF003_511", "CMF003_512", "CMF003_513", "CMF003_514", "CMF003_515",
			"CMF003_516", "CMF003_517", "CMF003_585", "CMF003_586", "CMF003_587", "CMF003_588", "CMF003_589",
			"CMF003_590", "CMF003_591", "CMF003_592", "CMF003_593", "CMF003_594", "CMF003_595", "CMF003_596",
			"CMF003_597", "CMF003_598", "CMF003_599", "CMF003_600", "CMF003_601", "CMF003_602", "CMF003_603",
			"CMF003_604", "CMF003_605", "CMF003_606", "CMF003_607", "CMF003_608", "CMF003_609", "CMF003_610",
			"CMF003_611", "CMF003_612", "CMF003_518", "CMF003_519", "CMF003_520", "CMF003_521", "CMF003_522",
			"CMF003_523", "CMF003_524", "CMF003_525", "CMF003_526", "CMF003_527", "CMF003_528", "CMF003_529",
			"CMF003_530", "CMF003_531", "CMF003_532", "CMF003_533", "CMF003_534", "CMF003_535", "CMF003_536",
			"CMF003_537", "CMF003_538", "CMF003_539", "CMF003_540", "CMF003_541", "CMF003_542", "CMF003_543",
			"CMF003_544", "CMF003_545", "CMF003_546", "CMF003_547", "CMF003_548", "CMF003_549", "CMF003_550",
			"CMF003_551", "CMF003_552", "CMF003_553", "CMF003_554", "CMF003_555", "CMF003_556", "CMF003_557",
			"CMF003_558", "CMF003_559", "CMF003_560", "CMF003_561", "CMF003_562", "CMF003_563", "CMF003_564",
			"CMF003_565", "CMF003_566", "CMF003_567", "CMF003_568", "CMF003_569", "CMF003_570", "CMF003_571",
			"CMF003_572", "CMF003_573", "CMF003_574", "CMF003_575", "CMF003_576", "CMF003_577", "CMF003_578",
			"CMF003_579", "CMF003_580", "CMF003_581", "CMF003_582", "CMF003_613", "CMF003_583", "CMF003_584");

	private static final List<String> LST_NAME_ID_HEADER_EMPLOYEE_FILE = Arrays.asList("SID", "SCD", "BUSINESS_NAME");
	private static final List<String> LST_NAME_HEADER_FIX_DATA_FILE = Arrays.asList("CMF003_620", "CMF003_621", "CMF003_622",
			"CMF003_623", "CMF003_624");

	private static final String TABLE_NAME_FILE = "保存対象テーブル一覧";
	private static final String EMPLOYEE_NAME_FILE = "対象社員";
	private static final String FILE_EXTENSION = ".csv";
	private static final String ZIP_FILE_EXTENSION = ".zip";

	@Inject
	private ManagementDeletionRepository repoManagementDel;
	@Inject
	private EmployeesDeletionRepository repoEmployeesDel;
	@Inject
	private CategoryDeletionRepository repoCategoryDel;
	@Inject
	private CategoryForDeleteRepository repoCategoryForDel;
	@Inject
	private CategoryFieldMtForDelRepository repoCtgFieldMtForDelRep;
	@Inject
	private ResultDeletionRepository repoResultDel;
	@Inject
	private ResultLogDeletionRepository repoResultLogDel;
	@Inject
	private DataDeletionCsvRepository repoCsv;
	@Inject
	private ApplicationTemporaryFileFactory applicationTemporaryFileFactory;
	@Inject
	private CSVReportGenerator generator;
	@Inject 
	private SaveErrorLogDeleteResult saveErrLogDel;
	@Inject
	private DeleteDataForCategory deleteDataForCategory;
	
	@Override
	protected void handle(ExportServiceContext<Object> context) {
		ManualSetDeletion domain = (ManualSetDeletion) context.getQuery();
		serverManualDelProcessing(domain, context.getGeneratorContext());
	}

	/**
	 * 
	 * @param delId
	 * @param generatorContext
	 */
	public void serverManualDelProcessing(ManualSetDeletion domain, FileGeneratorContext generatorContext) {
		// ドメインモデル「データ削除の手動設定」を読み込む
//		Optional<ManualSetDeletion> manualSetDeletion = repoManualSetDel.getManualSetDeletionById(delId);
		if (domain != null) {
			String delId = domain.getDelId();
//			ManualSetDeletion domain = manualSetDeletion.get();

			// ドメインモデル「データ削除の保存結果」へ追加する
			saveStartResultDel(domain);

			// ドメインモデル「データ削除の結果ログ」へ追加
			saveStartResultLogDel(domain);

			// ドメインモデル「データ削除動作管理」を更新する
			saveStartManagementDel(domain);

			// アルゴリズム「サーバデータ削除テーブル取得」を実行する
			List<CategoryForDelete> categories = getDataDelAgth(delId);
			List<EmployeeDeletion> employeeDeletions = repoEmployeesDel.getEmployeesDeletionListById(delId);
			List<TableDeletionDataCsv> tableDeletionDataCsvs = getTableDeletionData(delId);

			Result resultSave = null;
			if (domain.isSaveBeforeDeleteFlg()) {
				// アルゴリズム「サーバデータ削除保存」を実行する
				resultSave = saveDataDelAgth(generatorContext, delId, domain, tableDeletionDataCsvs, employeeDeletions,
						categories);
				ResultState state = resultSave.getState();
				if (state != ResultState.NORMAL_END) {
					//ドメインモデル「データ削除の結果ログ」へ追加する
					saveEndLogResult(domain, state);
					//ドメインモデル「データ削除の保存結果」を更新する
					saveEndResultDel(domain, resultSave,domain.isSaveBeforeDeleteFlg(), employeeDeletions.size());
					//ドメインモデル「データ削除動作管理」を更新する
					saveEndManagementDel(delId, state);
					
					return;
				}
			}

			// アルゴリズム「サーバデータ削除実行」を実行する
			ResultState resultDelState = deleteDataAgth(delId, domain, tableDeletionDataCsvs, employeeDeletions, categories);
			Result resultDel = new Result(resultDelState, null, null);
			if (resultDelState != ResultState.NORMAL_END) {
				//圧縮ファイルが作成されている場合、圧縮ファイルを削除する
				if (resultSave != null && resultSave.getFile() != null) {
					resultSave.getFile().delete();
				}
			}
			else {
				if (resultSave != null) {
					resultDel.setFile(resultSave.getFile());
					resultDel.setFileId(resultSave.getFileId());
				}
			}
			
			//ドメインモデル「データ削除の結果ログ」へ追加する
			saveEndLogResult(domain, resultDel.getState());
			//ドメインモデル「データ削除の保存結果」を更新する
			saveEndResultDel(domain, resultDel,domain.isSaveBeforeDeleteFlg(),employeeDeletions.size() );
			//ドメインモデル「データ削除動作管理」を更新する
			saveEndManagementDel(delId, resultDelState);
		} else {
    		throw new RuntimeException("Don't get setting of deletion.");
		}
	}
	
	

	/**
	 * ドメインモデル「データ削除の保存結果」へ追加する save the starting result of deletion
	 * 
	 * @param domain
	 *            ManualSetDeletion
	 */
	private void saveStartResultDel(ManualSetDeletion domain) {
		GeneralDateTime startDateTimeDel = GeneralDateTime.now();
		int delType = DelType.MANUAL.value;
		int fileSize = 0;
		int numberEmployees = 0;
		ResultDeletion resultDomain = ResultDeletion.createFromJavatype(domain.getDelId(), domain.getCompanyId(),
				domain.getDelName().v(), delType, domain.isSaveBeforeDeleteFlg(), null, numberEmployees,
				domain.getSystemType(), domain.getSId(), SaveStatus.SUCCESS.value, startDateTimeDel, null, null, null,
				fileSize, null);
		repoResultDel.add(resultDomain);
	}

	/**
	 * ドメインモデル「データ削除の結果ログ」へ追加 save the starting result log of deletion
	 * 
	 * @param domain
	 */
	private void saveStartResultLogDel(ManualSetDeletion domain) {
		GeneralDateTime logTime = GeneralDateTime.now();
		ResultLogDeletion resultLogDomain = ResultLogDeletion.createFromJavatype(0, domain.getDelId(),
				domain.getCompanyId(), logTime, TextResource.localize(MSG_START_DEL_LOG), null, null, null);
		repoResultLogDel.add(resultLogDomain);
	}

	/**
	 * ドメインモデル「データ削除動作管理」を更新する
	 * 
	 * @param domain
	 */
	private void saveStartManagementDel(ManualSetDeletion domain) {
		int totalCategoryCount = 0;
		int categoryCount = 0;
		int errorCount = 0;
		OperatingCondition operatingCondition = OperatingCondition.INPREPARATION;
		boolean isInterruptedFlg = false;
		ManagementDeletion managementDomain = ManagementDeletion.createFromJavatype(domain.getDelId(), isInterruptedFlg,
				totalCategoryCount, categoryCount, errorCount, operatingCondition.value);
		repoManagementDel.add(managementDomain);
	}

	
	
	/**
	 * ドメインモデル「データ削除動作管理」を更新する
	 * @param domain
	 * @param state
	 */
	private void saveEndManagementDel(String delId, ResultState state) {
		OperatingCondition operatingCondition = OperatingCondition.DONE;
		
		if (state == ResultState.ABNORMAL_END) {
			operatingCondition = OperatingCondition.ABNORMAL_TERMINATION;
		}
		else if (state == ResultState.TERMINATE) {
			operatingCondition = OperatingCondition.INTERRUPTION_END;
		}
		
		repoManagementDel.updateOperationCond(delId, operatingCondition);
	}
	/**
	 * ドメインモデル「データ削除の結果ログ」へ追加する
	 */
	private void saveEndLogResult(ManualSetDeletion domain, ResultState state) {
		String msgId = MSG_END_NORMAL_DEL_LOG;
		if (state == ResultState.ABNORMAL_END) {
			msgId = MSG_END_ABNORMAL_DEL_LOG;
		} else if (state == ResultState.TERMINATE) {
			msgId = MSG_END_TERMINATE_DEL_LOG;
		}
		GeneralDateTime logTime = GeneralDateTime.now();
		int seqId = repoResultLogDel.getMaxSeqId(domain.getDelId()) + 1;
		ResultLogDeletion resultLogDomain = ResultLogDeletion.createFromJavatype(seqId, domain.getDelId(),
				domain.getCompanyId(), logTime, TextResource.localize(msgId), null, null, null);
		repoResultLogDel.add(resultLogDomain);
	}
	
	/**
	 * ドメインモデル「データ削除の保存結果」を更新する
	 * Update domain model 「データ削除の保存結果」
	 */
	private void saveEndResultDel(ManualSetDeletion domain, Result result,Boolean isSaveBeforeDeleteFlg, int numberEmployees) {
		Optional<ResultDeletion> optResultDel = repoResultDel.getResultDeletionById(domain.getDelId());
		if (optResultDel.isPresent()) {
			int fileSize = 0;
			String fileId = null;
			
			ResultState state = result.getState();
			SaveStatus status = SaveStatus.SUCCESS;
			if (state == ResultState.ABNORMAL_END) {
				status = SaveStatus.FAILURE;
			}
			else if (state == ResultState.TERMINATE) {
				status = SaveStatus.INTERRUPTION;
			}
			GeneralDateTime endDateTimeDel = GeneralDateTime.now();
			File file = result.getFile();
			if (file != null) {
				fileSize = (int)file.length();
				fileId = result.getFileId();
			}
			String datetimenow = GeneralDateTime.now().toString("yyyyMMddHHmmss");
			String nameFile = domain.getCompanyId() + domain.getDelName()  + datetimenow;	
			ResultDeletion resultDel = optResultDel.get();
			resultDel.setStatus(status);
			resultDel.setEndDateTimeDel(endDateTimeDel);
			resultDel.setFileSize(fileSize);
			resultDel.setFileId(fileId);
			if (fileId == null || fileId == "") {
				resultDel.setDeletedFilesFlg(true);
				resultDel.setFileName(new FileName(""));
			} else {
				resultDel.setDeletedFilesFlg(false);
				resultDel.setFileName(new FileName(nameFile));
			}
		
			resultDel.setNumberEmployees(numberEmployees);
			repoResultDel.update(resultDel, domain);
		}
	}

	/**
	 * アルゴリズム「サーバデータ削除テーブル取得」を実行する
	 */
	private List<CategoryForDelete> getDataDelAgth(String delId) {
		// Get list category from
		List<CategoryDeletion> categoryDeletions = repoCategoryDel.getCategoryDeletionListById(delId);
		List<String> categoryIds = categoryDeletions.stream().map(x -> {
			return x.getCategoryId();
		}).collect(Collectors.toList());
		List<CategoryForDelete> categorys = repoCategoryForDel.getCategoryByListId(categoryIds);
		// update domain 「データ保存動作管理」 Data operation management
		int totalCount = categorys.size();
		repoManagementDel.updateTotalCatCount(delId, totalCount);

		return categorys;
	}
	
	
	/**
	 * アルゴリズム「サーバデータ削除テーブル取得」を実行する
	 */
	private List<CategoryFieldMtForDelete> getCtgFildMtForDel(List<String> categoryIds) {
		// Get list category from
		List<CategoryFieldMtForDelete> categoryFieldMts = repoCtgFieldMtForDelRep.getCategoryFieldMtByListId(categoryIds);

		return categoryFieldMts;
	}

	/**
	 * アルゴリズム「サーバデータ削除保存」を実行する save data deletion to file
	 * 
	 * @param generatorContext
	 * @param delId
	 * @param domain
	 * @param employeeDeletions
	 * @param categories
	 */
	private Result saveDataDelAgth(FileGeneratorContext generatorContext, String delId, ManualSetDeletion domain,
			List<TableDeletionDataCsv> tableDeletionDataCsvs, List<EmployeeDeletion> employeeDeletions,
			List<CategoryForDelete> categories) {
	
		File file = null;
		// アルゴリズム「サーバデータ削除保存ファイル」を実行する
		ResultState resultSaving = saveDataDelToCsvAgth(generatorContext, tableDeletionDataCsvs, delId, domain,
				employeeDeletions, categories);
		if (resultSaving == ResultState.NORMAL_END) {
			// アルゴリズム「サーバデータ削除保存圧縮」を実行する
			file = zipFolderDataAgth(generatorContext, domain);
			if (file == null) {
				return new Result(ResultState.ABNORMAL_END, null, null);
			}else {
				String fileId = generatorContext.getTaskId();
				return new Result(ResultState.NORMAL_END, file, fileId);
			}
		} else {
			return new Result(resultSaving, null, null);
		}
	}

	/**
	 * アルゴリズム「サーバデータ削除保存ファイル」を実行する
	 */
	private ResultState saveDataDelToCsvAgth(FileGeneratorContext generatorContext,
			List<TableDeletionDataCsv> tableDeletionDataCsvs, String delId, ManualSetDeletion domain,
			List<EmployeeDeletion> employeeDeletions, List<CategoryForDelete> categories) {
		try {
			// Update domain model 「データ削除動作管理」
			repoManagementDel.updateCatCountAnCond(delId, 0, OperatingCondition.SAVING);
			
			// テーブル一覧の内容をCSVファイルに書き出す Add Table to CSV
			generalTableDeletionToCsv(generatorContext, tableDeletionDataCsvs, domain);

			// 対象社員の内容をCSVファイルに暗号化して書き出す
			generalEmployeesToCsv(generatorContext, delId, domain);

			Map<String, List<TableDeletionDataCsv>> mapCatWithDatas = mapCatWithDataDel(tableDeletionDataCsvs);
			if (mapCatWithDatas != null) {
				int categoryCount = 0;
				for (CategoryForDelete category : categories) {
					categoryCount++;
					
					// ドメインモデル「データ削除動作管理」を更新する
					repoManagementDel.updateCatCount(delId, categoryCount);

					// ドメインモデル「データ削除動作管理.中断するしない」を確認
					Optional<ManagementDeletion> maOptional = repoManagementDel.getManagementDeletionById(delId);
					if (maOptional.isPresent()) {
						if (maOptional.get().isInterruptedFlg()) {
							return ResultState.TERMINATE;
						}
					} else {
						return ResultState.ABNORMAL_END;
					}
					
					String categoryId = category.getCategoryId().v();
					List<TableDeletionDataCsv> listTableForDel = mapCatWithDatas.get(categoryId);
					if (listTableForDel != null) {
						for (TableDeletionDataCsv tableDataDel : listTableForDel) {
							// アルゴリズム「サーバデータ削除テーブルデータ書出」を実行する
							ResultState generalResult = generalDataForCategoryToCsv(generatorContext, domain,
									employeeDeletions, tableDataDel);
							if (generalResult == ResultState.ABNORMAL_END) {
								ManagementDeletion managementDeletion = maOptional.get();
								int errorCount = managementDeletion.getErrorCount();
								managementDeletion.setErrorCount(errorCount + 1);
								repoManagementDel.update(managementDeletion);
								return ResultState.ABNORMAL_END;
							}
						}
					}
				}
			}
			else {
				return ResultState.ABNORMAL_END;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultState.ABNORMAL_END;
		}

		return ResultState.NORMAL_END;
	}
	
	/**
	 * 
	 * @param categories
	 * @param tableDeletionDataCsvs
	 * @return
	 */
	private Map<String, List<TableDeletionDataCsv>> mapCatWithDataDel(List<TableDeletionDataCsv> tableDeletionDataCsvs) {
		Map<String, List<TableDeletionDataCsv>> mapCatWithDatas = new HashMap<>();
		
		for (TableDeletionDataCsv tableDataDel : tableDeletionDataCsvs) {
			String categoryId = tableDataDel.getCategoryId();
			
			List<TableDeletionDataCsv> catDatas = mapCatWithDatas.get(categoryId);
			if (catDatas == null) {
				catDatas = new ArrayList<>();
				catDatas.add(tableDataDel);
				mapCatWithDatas.put(categoryId, catDatas);
			}
			else {
				catDatas.add(tableDataDel);
			}
		}
		
		return mapCatWithDatas;
	}

	/**
	 * アルゴリズム「サーバデータ削除テーブルデータ書出」を実行する
	 */
	private ResultState generalDataForCategoryToCsv(FileGeneratorContext generatorContext, ManualSetDeletion domain,
			List<EmployeeDeletion> employeeDeletions, TableDeletionDataCsv tableDataDel) {
		try {
			List<String> columNames = repoCsv.getColumnName(tableDataDel.getTableEnglishName());
			List<String> header = getHeaderForDataFile(columNames);
			
			repoCsv.backupCsvFile(tableDataDel, employeeDeletions, header , generatorContext);
			
		} catch (Exception e) {
			e.printStackTrace();
			saveErrLogDel.saveErrorWhenCreFileCsv(domain, e.getMessage());		
			return ResultState.ABNORMAL_END;
		}
		return ResultState.NORMAL_END;
	}

	/**
	 * アルゴリズム「サーバデータ削除保存圧縮」を実行する
	 */
	private File zipFolderDataAgth(FileGeneratorContext generatorContext, ManualSetDeletion domain) {
		// Update domain model 「データ削除動作管理」
		repoManagementDel.updateOperationCond(domain.getDelId(), OperatingCondition.COMPRESSING);

		ApplicationTemporaryFilesContainer applicationTemporaryFilesContainer = applicationTemporaryFileFactory
				.createContainer();
		
		// ファイル圧縮実行
		boolean isExistCompressPassFlg = domain.isExistCompressPassFlg();
		String datetimenow = GeneralDateTime.now().toString("yyyyMMddHHmmss"); 
		String nameFile = domain.getCompanyId() + domain.getDelName()  + datetimenow + ZIP_FILE_EXTENSION;
		
		Path compressedFile = null;
		if (!isExistCompressPassFlg) {
			compressedFile = applicationTemporaryFilesContainer.zipWithName(generatorContext, nameFile, false);
		} else {
			String password = domain.getPasswordCompressFileEncrypt().get().v();
			compressedFile = applicationTemporaryFilesContainer.zipWithName(generatorContext, nameFile, password, false);
		}
		
		applicationTemporaryFilesContainer.removeContainer();
		
		if (compressedFile != null) {
			return compressedFile.toFile();
		}
		return null;
	}
	
	/**
	 * 
	 * @param delId
	 * @return
	 */
	private List<TableDeletionDataCsv> getTableDeletionData(String delId) {
		List<TableDeletionDataCsv> dataDeletionCsvs = repoCsv.getTableDelDataCsvById(delId);
		return dataDeletionCsvs;
	}

	/**
	 * 
	 * @param generatorContext
	 * @param dataDeletionCsvs
	 * @return
	 */
	private ResultState generalTableDeletionToCsv(FileGeneratorContext generatorContext,
			List<TableDeletionDataCsv> dataDeletionCsvs, ManualSetDeletion domain) {
		try {
			List<String> headerCsv = this.getHeaderForTableDelFile();
			
			CsvReportWriter csvFile = generator.generate(generatorContext,TABLE_NAME_FILE + FILE_EXTENSION, headerCsv , "UTF-8");
			
			for (TableDeletionDataCsv dataTarget : dataDeletionCsvs) {
				Map<String, Object> rowCsv = new HashMap<>();
				rowCsv.put(headerCsv.get(0), dataTarget.getDelId());
				rowCsv.put(headerCsv.get(1), dataTarget.getDelType());
				rowCsv.put(headerCsv.get(2), dataTarget.getDelCode());
				rowCsv.put(headerCsv.get(3), dataTarget.getDelName());
				rowCsv.put(headerCsv.get(4), dataTarget.getSupplementExplanation());
				rowCsv.put(headerCsv.get(5), dataTarget.getCategoryId());
				rowCsv.put(headerCsv.get(6), dataTarget.getCategoryName());
				rowCsv.put(headerCsv.get(7), dataTarget.getTimeStore());
				rowCsv.put(headerCsv.get(8), dataTarget.getRecoveryStorageRange());
				
				// CMF003_509,CMF003_510, CMF003_511 missing
				if(dataTarget.getTimeStore() == TimeStore.DAILY.value){
					rowCsv.put(headerCsv.get(9), dataTarget.getStartDateOfDaily() + "~" + dataTarget.getEndDateOfDaily());
					rowCsv.put(headerCsv.get(107), dataTarget.getStartDateOfDaily());
					rowCsv.put(headerCsv.get(108), dataTarget.getEndDateOfDaily());
				}else if(dataTarget.getTimeStore() == TimeStore.MONTHLY.value){
					rowCsv.put(headerCsv.get(9), dataTarget.getStartMonthOfMonthly() + "~" + dataTarget.getEndMonthOfMonthly());
					rowCsv.put(headerCsv.get(107), dataTarget.getStartMonthOfMonthly());
					rowCsv.put(headerCsv.get(108), dataTarget.getEndMonthOfMonthly());
				}else if(dataTarget.getTimeStore() == TimeStore.ANNUAL.value){
					rowCsv.put(headerCsv.get(9), dataTarget.getStartYearOfMonthly() + "~" + dataTarget.getEndYearOfMonthly());
					rowCsv.put(headerCsv.get(107), dataTarget.getStartYearOfMonthly());
					rowCsv.put(headerCsv.get(108), dataTarget.getEndYearOfMonthly());
				}else {
					rowCsv.put(headerCsv.get(9), "");
					rowCsv.put(headerCsv.get(107), "");
					rowCsv.put(headerCsv.get(108), "");
				}
				rowCsv.put(headerCsv.get(10), "");
				rowCsv.put(headerCsv.get(11), "");
				
				rowCsv.put(headerCsv.get(12), dataTarget.getSaveForInvest());
				rowCsv.put(headerCsv.get(13), dataTarget.getOtherCompanyCls());
				rowCsv.put(headerCsv.get(14), dataTarget.getTableNo());
				rowCsv.put(headerCsv.get(15), dataTarget.getTableJapanName());
				rowCsv.put(headerCsv.get(16), dataTarget.getTableEnglishName());
				rowCsv.put(headerCsv.get(17), dataTarget.getHistoryCls());
				
				rowCsv.put(headerCsv.get(18), dataTarget.getHasParentTblFlg());
				rowCsv.put(headerCsv.get(19), dataTarget.getParentTblJapanName().orElse(""));
				rowCsv.put(headerCsv.get(20), dataTarget.getParentTblName().orElse(""));
				
				rowCsv.put(headerCsv.get(21), dataTarget.getFieldParent1().orElse(""));
				rowCsv.put(headerCsv.get(22), dataTarget.getFieldParent2().orElse(""));
				rowCsv.put(headerCsv.get(23), dataTarget.getFieldParent3().orElse(""));
				rowCsv.put(headerCsv.get(24), dataTarget.getFieldParent4().orElse(""));
				rowCsv.put(headerCsv.get(25), dataTarget.getFieldParent5().orElse(""));
				rowCsv.put(headerCsv.get(26), dataTarget.getFieldParent6().orElse(""));
				rowCsv.put(headerCsv.get(27), dataTarget.getFieldParent7().orElse(""));
				rowCsv.put(headerCsv.get(28), dataTarget.getFieldParent8().orElse(""));
				rowCsv.put(headerCsv.get(29), dataTarget.getFieldParent9().orElse(""));
				rowCsv.put(headerCsv.get(30), dataTarget.getFieldParent10().orElse(""));
				rowCsv.put(headerCsv.get(31), dataTarget.getFieldChild1().orElse(""));
				rowCsv.put(headerCsv.get(32), dataTarget.getFieldChild2().orElse(""));
				rowCsv.put(headerCsv.get(33), dataTarget.getFieldChild3().orElse(""));
				rowCsv.put(headerCsv.get(34), dataTarget.getFieldChild4().orElse(""));
				rowCsv.put(headerCsv.get(35), dataTarget.getFieldChild5().orElse(""));
				rowCsv.put(headerCsv.get(36), dataTarget.getFieldChild6().orElse(""));
				rowCsv.put(headerCsv.get(37), dataTarget.getFieldChild7().orElse(""));
				rowCsv.put(headerCsv.get(38), dataTarget.getFieldChild8().orElse(""));
				rowCsv.put(headerCsv.get(39), dataTarget.getFieldChild9().orElse(""));
				rowCsv.put(headerCsv.get(40), dataTarget.getFieldChild10().orElse(""));
				
				rowCsv.put(headerCsv.get(41), dataTarget.getFieldAcqCid().orElse(""));
				rowCsv.put(headerCsv.get(42), dataTarget.getFieldAcqEmployeeId().orElse(""));
				rowCsv.put(headerCsv.get(43), dataTarget.getFieldAcqDateTime().orElse(""));
				rowCsv.put(headerCsv.get(44), dataTarget.getFieldAcqStartDate().orElse(""));
				rowCsv.put(headerCsv.get(45), dataTarget.getFieldAcqEndDate().orElse(""));
				rowCsv.put(headerCsv.get(46), dataTarget.getDefaultCondKeyQuery().orElse(""));
				
				rowCsv.put(headerCsv.get(47), dataTarget.getFieldKeyQuery1().orElse(""));
				rowCsv.put(headerCsv.get(48), dataTarget.getFieldKeyQuery2().orElse(""));
				rowCsv.put(headerCsv.get(49), dataTarget.getFieldKeyQuery3().orElse(""));
				rowCsv.put(headerCsv.get(50), dataTarget.getFieldKeyQuery4().orElse(""));
				rowCsv.put(headerCsv.get(51), dataTarget.getFieldKeyQuery5().orElse(""));
				rowCsv.put(headerCsv.get(52), dataTarget.getFieldKeyQuery6().orElse(""));
				rowCsv.put(headerCsv.get(53), dataTarget.getFieldKeyQuery7().orElse(""));
				rowCsv.put(headerCsv.get(54), dataTarget.getFieldKeyQuery8().orElse(""));
				rowCsv.put(headerCsv.get(55), dataTarget.getFieldKeyQuery9().orElse(""));
				rowCsv.put(headerCsv.get(56), dataTarget.getFieldKeyQuery10().orElse(""));

				rowCsv.put(headerCsv.get(57), dataTarget.getClsKeyQuery1().orElse(""));
				rowCsv.put(headerCsv.get(58), dataTarget.getClsKeyQuery2().orElse(""));
				rowCsv.put(headerCsv.get(59), dataTarget.getClsKeyQuery3().orElse(""));
				rowCsv.put(headerCsv.get(60), dataTarget.getClsKeyQuery4().orElse(""));
				rowCsv.put(headerCsv.get(61), dataTarget.getClsKeyQuery5().orElse(""));
				rowCsv.put(headerCsv.get(62), dataTarget.getClsKeyQuery6().orElse(""));
				rowCsv.put(headerCsv.get(63), dataTarget.getClsKeyQuery7().orElse(""));
				rowCsv.put(headerCsv.get(64), dataTarget.getClsKeyQuery8().orElse(""));
				rowCsv.put(headerCsv.get(65), dataTarget.getClsKeyQuery9().orElse(""));
				rowCsv.put(headerCsv.get(66), dataTarget.getClsKeyQuery10().orElse(""));

				rowCsv.put(headerCsv.get(67), dataTarget.getFiledKeyUpdate1().orElse(""));
				rowCsv.put(headerCsv.get(68), dataTarget.getFiledKeyUpdate2().orElse(""));
				rowCsv.put(headerCsv.get(69), dataTarget.getFiledKeyUpdate3().orElse(""));
				rowCsv.put(headerCsv.get(70), dataTarget.getFiledKeyUpdate4().orElse(""));
				rowCsv.put(headerCsv.get(71), dataTarget.getFiledKeyUpdate5().orElse(""));
				rowCsv.put(headerCsv.get(72), dataTarget.getFiledKeyUpdate6().orElse(""));
				rowCsv.put(headerCsv.get(73), dataTarget.getFiledKeyUpdate7().orElse(""));
				rowCsv.put(headerCsv.get(74), dataTarget.getFiledKeyUpdate8().orElse(""));
				rowCsv.put(headerCsv.get(75), dataTarget.getFiledKeyUpdate9().orElse(""));
				rowCsv.put(headerCsv.get(76), dataTarget.getFiledKeyUpdate10().orElse(""));
				rowCsv.put(headerCsv.get(77), dataTarget.getFiledKeyUpdate11().orElse(""));
				rowCsv.put(headerCsv.get(78), dataTarget.getFiledKeyUpdate12().orElse(""));
				rowCsv.put(headerCsv.get(79), dataTarget.getFiledKeyUpdate13().orElse(""));
				rowCsv.put(headerCsv.get(80), dataTarget.getFiledKeyUpdate14().orElse(""));
				rowCsv.put(headerCsv.get(81), dataTarget.getFiledKeyUpdate15().orElse(""));
				rowCsv.put(headerCsv.get(82), dataTarget.getFiledKeyUpdate16().orElse(""));
				rowCsv.put(headerCsv.get(83), dataTarget.getFiledKeyUpdate17().orElse(""));
				rowCsv.put(headerCsv.get(84), dataTarget.getFiledKeyUpdate18().orElse(""));
				rowCsv.put(headerCsv.get(85), dataTarget.getFiledKeyUpdate19().orElse(""));
				rowCsv.put(headerCsv.get(86), dataTarget.getFiledKeyUpdate20().orElse(""));

				rowCsv.put(headerCsv.get(87), dataTarget.getFieldDate1().orElse(""));
				rowCsv.put(headerCsv.get(88), dataTarget.getFieldDate2().orElse(""));
				rowCsv.put(headerCsv.get(89), dataTarget.getFieldDate3().orElse(""));
				rowCsv.put(headerCsv.get(90), dataTarget.getFieldDate4().orElse(""));
				rowCsv.put(headerCsv.get(91), dataTarget.getFieldDate5().orElse(""));
				rowCsv.put(headerCsv.get(92), dataTarget.getFieldDate6().orElse(""));
				rowCsv.put(headerCsv.get(93), dataTarget.getFieldDate7().orElse(""));
				rowCsv.put(headerCsv.get(94), dataTarget.getFieldDate8().orElse(""));
				rowCsv.put(headerCsv.get(95), dataTarget.getFieldDate9().orElse(""));
				rowCsv.put(headerCsv.get(96), dataTarget.getFieldDate10().orElse(""));
				rowCsv.put(headerCsv.get(97), dataTarget.getFieldDate11().orElse(""));
				rowCsv.put(headerCsv.get(98), dataTarget.getFieldDate12().orElse(""));
				rowCsv.put(headerCsv.get(99), dataTarget.getFieldDate13().orElse(""));
				rowCsv.put(headerCsv.get(100), dataTarget.getFieldDate14().orElse(""));
				rowCsv.put(headerCsv.get(101), dataTarget.getFieldDate15().orElse(""));
				rowCsv.put(headerCsv.get(102), dataTarget.getFieldDate16().orElse(""));
				rowCsv.put(headerCsv.get(103), dataTarget.getFieldDate17().orElse(""));
				rowCsv.put(headerCsv.get(104), dataTarget.getFieldDate18().orElse(""));
				rowCsv.put(headerCsv.get(105), dataTarget.getFieldDate19().orElse(""));
				rowCsv.put(headerCsv.get(106), dataTarget.getFieldDate20().orElse(""));
				
				rowCsv.put(headerCsv.get(109), dataTarget.getCompressedFileName());
				rowCsv.put(headerCsv.get(110), dataTarget.getInternalFileName());
				rowCsv.put(headerCsv.get(111), "");
				rowCsv.put(headerCsv.get(112), 1);
				rowCsv.put(headerCsv.get(113), 1);
				
				csvFile.writeALine(rowCsv);
			}
			csvFile.destroy();

			return ResultState.NORMAL_END;
		} catch (Exception e) {
			e.printStackTrace();
			saveErrLogDel.saveErrorWhenCreFileCsv(domain, e.getMessage());	
			return ResultState.ABNORMAL_END;
		}
	}

	/**
	 * 
	 * @param generatorContext
	 * @param delId
	 * @return
	 */
	private ResultState generalEmployeesToCsv(FileGeneratorContext generatorContext, String delId, ManualSetDeletion domain) {
		try {
			List<EmployeeDeletion> employeeDeletions = repoEmployeesDel.getEmployeesDeletionListById(delId);
			List<String> headerCsvListEmp = this.getHeaderForEmployeeFile();
			CsvReportWriter csvFile = generator.generate(generatorContext,EMPLOYEE_NAME_FILE + FILE_EXTENSION, headerCsvListEmp, "UTF-8");
			for (EmployeeDeletion employeeDeletion : employeeDeletions) {
				Map<String, Object> rowCsv = new HashMap<>();
				rowCsv.put(headerCsvListEmp.get(0), employeeDeletion.getEmployeeId());
				rowCsv.put(headerCsvListEmp.get(1), employeeDeletion.getEmployeeCode());
				rowCsv.put(headerCsvListEmp.get(2), employeeDeletion.getBusinessName() != null
						? CommonKeyCrypt.encrypt(employeeDeletion.getBusinessName().v()) : "");
				
				csvFile.writeALine(rowCsv);
			}
			csvFile.destroy();

			return ResultState.NORMAL_END;
		} catch (Exception e) {
			e.printStackTrace();
			saveErrLogDel.saveErrorWhenCreFileCsv(domain, e.getMessage());	
			return ResultState.ABNORMAL_END;
		}
		
	}

	/**
	 * 
	 * @return
	 */
	private List<String> getHeaderForTableDelFile() {
		List<String> lstHeader = new ArrayList<>();
		for (String nameId : LST_NAME_ID_HEADER_TABLE_FILE) {
			lstHeader.add(TextResource.localize(nameId));
		}
		return lstHeader;
	}

	/**
	 * get header for employee file
	 * 
	 * @return
	 */
	private List<String> getHeaderForEmployeeFile() {
		List<String> lstHeader = new ArrayList<>();
		for (String nameId : LST_NAME_ID_HEADER_EMPLOYEE_FILE) {
			lstHeader.add(TextResource.localize(nameId));
		}
		return lstHeader;
	}

	/**
	 * get header for data file
	 * 
	 * @return
	 */
	private List<String> getHeaderForDataFile(List<String> columNames) {
		List<String> lstHeader = new ArrayList<>();
		for (String nameId : LST_NAME_HEADER_FIX_DATA_FILE) {
			lstHeader.add(TextResource.localize(nameId));
		}

		for (String nameColumn : columNames) {
			lstHeader.add(nameColumn);
		}
		return lstHeader;
	}

	/**
	 * アルゴリズム「サーバデータ削除実行」を実行する execute the deletion data
	 */
	private ResultState deleteDataAgth(String delId, ManualSetDeletion domain,
			List<TableDeletionDataCsv> tableDeletionDataCsvs, List<EmployeeDeletion> employeeDeletions,
			List<CategoryForDelete> categories) {

			// Update domain model 「データ削除動作管理」
			repoManagementDel.updateCatCountAnCond(delId, 0, OperatingCondition.DELETING);

			Map<String, List<TableDeletionDataCsv>> mapCatWithDatas = mapCatWithDataDel(tableDeletionDataCsvs);
			if (mapCatWithDatas != null) {
				int categoryCount = 0;
				boolean hasError = false;
				
				for (CategoryForDelete category : categories) {
					categoryCount++;

					// ドメインモデル「データ削除動作管理」を更新する
					repoManagementDel.updateCatCount(delId, categoryCount);

					// ドメインモデル「データ削除動作管理.中断するしない」を確認
					Optional<ManagementDeletion> maOptional = repoManagementDel.getManagementDeletionById(delId);
					if (maOptional.isPresent()) {
						if (maOptional.get().isInterruptedFlg()) {
							return ResultState.TERMINATE;
						}
					} else {
						return ResultState.ABNORMAL_END;
					}

					String categoryId = category.getCategoryId().v();
					List<TableDeletionDataCsv> catDatas = mapCatWithDatas.get(categoryId);
					if (catDatas != null) {
						List<TableDeletionDataCsv> parentTables = new ArrayList<>();
						List<TableDeletionDataCsv> childTables = new ArrayList<>();
						
						for (TableDeletionDataCsv tableDataDel : catDatas) {
							if (tableDataDel.hasParentTblFlg()) {
								childTables.add(tableDataDel);
							} else {
								parentTables.add(tableDataDel);
							}
						}
						try {
							deleteDataForCategory.deleteProcess(childTables, parentTables, employeeDeletions, 
									maOptional.get(), domain);
						} catch (Exception e) {
							e.printStackTrace();
							hasError = true;
						}
					}
				}
				if (hasError) {
					return ResultState.ABNORMAL_END;
				}
			}
		return ResultState.NORMAL_END;
	}
}
