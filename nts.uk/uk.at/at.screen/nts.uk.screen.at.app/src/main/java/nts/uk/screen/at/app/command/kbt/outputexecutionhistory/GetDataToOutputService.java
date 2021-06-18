package nts.uk.screen.at.app.command.kbt.outputexecutionhistory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.layer.infra.file.temp.ApplicationTemporaryFileFactory;
import nts.arc.layer.infra.file.temp.ApplicationTemporaryFilesContainer;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.app.find.employee.dto.EmployeeBasicInfoExportDto;
import nts.uk.ctx.at.function.app.find.employee.dto.EmployeeInfoExport;
import nts.uk.ctx.at.function.dom.adapter.employeebasic.EmployeeBasicInfoFnImport;
import nts.uk.ctx.at.function.dom.adapter.employeebasic.EmployeeInfoImport;
import nts.uk.ctx.at.function.dom.adapter.employeebasic.SyEmployeeFnAdapter;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.OverallErrorDetail;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogHistory;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogHistRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionRepository;
import nts.uk.ctx.at.function.dom.processexecution.storage.ResultState;
import nts.uk.ctx.at.record.dom.executionstatusmanage.optionalperiodprocess.AggrPeriodInforRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppDataInfoDailyRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.AppDataInfoMonthlyRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogRepository;
import nts.uk.ctx.at.shared.dom.person.PersonAdaptor;
import nts.uk.ctx.at.shared.dom.person.PersonImport;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.query.app.exi.ExacErrorLogQueryDto;
import nts.uk.query.app.exi.ExacErrorLogQueryFinder;
import nts.uk.query.app.exo.ExternalOutLogQueryDto;
import nts.uk.query.app.exo.ExternalOutLogQueryFinder;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.csv.CSVReportGenerator;
import nts.uk.shr.infra.file.csv.CsvReportWriter;

@Stateless
public class GetDataToOutputService extends ExportService<Object> {

	private static final List<String> LST_NAME_ID_HEADER_TABLE_CSV1 = Arrays.asList("KBT002_289", "KBT002_7",
			"KBT002_8", "KBT002_202", "KBT002_203", "KBT002_204", "KBT002_205", "KBT002_206", "KBT002_207",
			"KBT002_220");

	private static final List<String> LST_NAME_ID_HEADER_TABLE_CSV2 = Arrays.asList("KBT002_289", "KBT002_7",
			"KBT002_8", "KBT002_201", "KBT002_202", "KBT002_203", "KBT002_204", "KBT002_205", "KBT002_206",
			"KBT002_334", "KBT002_207");

	private static final List<String> LST_NAME_ID_HEADER_TABLE_CSV3 = Arrays.asList("KBT002_289", "KBT002_7",
			"KBT002_8", "KBT002_201", "KBT002_184", "KBT002_292", "KBT002_186", "KBT002_187");

	private static final List<String> LST_NAME_ID_HEADER_TABLE_CSV4 = Arrays.asList("KBT002_289", "KBT002_7",
			"KBT002_8", "KBT002_316", "KBT002_317", "KBT002_318", "KBT002_319", "KBT002_320");

	private static final List<String> LST_NAME_ID_HEADER_TABLE_CSV5 = Arrays.asList("KBT002_289", "KBT002_7",
			"KBT002_8", "KBT002_321", "KBT002_184", "KBT002_292", "KBT002_186", "KBT002_322", "KBT002_323",
			"KBT002_187");

	private static final String CSV_EXTENSION = ".csv";
	private static final String ZIP_EXTENSION = ".zip";
	private static final String FILE_NAME_ZIP = "KBT002_更新処理自動実行の実行履歴_";
	private static final String FILE_NAME_CSV1 = "KBT002_更新処理自動実行の実行状況";
	private static final String FILE_NAME_CSV2 = "KBT002_実行項目の詳細状況";
	private static final String FILE_NAME_CSV3 = "KBT002_実行項目のエラー詳細";
	private static final String FILE_NAME_CSV4 = "KBT002_実行項目のエラー詳細（外部受入）";
	private static final String FILE_NAME_CSV5 = "KBT002_実行項目のエラー詳細（外部出力）";

	@Inject
	private ProcessExecutionLogHistRepository updateProAutoExeRepo;

	@Inject
	private ProcessExecutionRepository procExecRepo;

	@Inject
	private ScheduleErrorLogRepository scheduleErrorLogRepo;

	@Inject
	private ErrMessageInfoRepository errMessageInfoRepo;

	@Inject
	private AggrPeriodInforRepository errorInfoRepo;

	@Inject
	private AppDataInfoDailyRepository appDataInfoDailyRepo;

	@Inject
	private AppDataInfoMonthlyRepository appDataInfoMonthlyRepo;

	@Inject
	private SyEmployeeFnAdapter syEmployeeFnAdapter;

	@Inject
	private PersonAdaptor personAdaptor;

	@Inject
	private CSVReportGenerator generator;

	@Inject
	private ApplicationTemporaryFileFactory applicationTemporaryFileFactory;

	@Inject
	private ExternalOutLogQueryFinder externalOutLogQueryFinder;

	@Inject
	private ExacErrorLogQueryFinder exacErrorLogQueryFinder;

	@Override
	protected void handle(ExportServiceContext<Object> context) {
		GetDataToOutputCommand command = (GetDataToOutputCommand) context.getQuery();
		// 出力するデータを取得する
		UpdateProcessAutoRunDataDto updateProAutoRunData = this.getDataOutput(command);
		// Step 取得した「更新処理自動実行データ」からcsvファイルを作成する
		FileGeneratorContext generatorContext = context.getGeneratorContext();
		// create file csv
		ResultState resultState = this.saveAllFileCSV(generatorContext, updateProAutoRunData);
		// Step 作成したcsvファイルをまとめて圧縮する
		// Step 圧縮したファイルをダウンロードする
		if (resultState.equals(ResultState.NORMAL_END)) {
			this.zipAllFileCSV(generatorContext);
		}
	}

	private ResultState saveAllFileCSV(FileGeneratorContext generatorContext,
			UpdateProcessAutoRunDataDto updateProAutoRunData) {
		try {
			this.generalFileCSV1(generatorContext, updateProAutoRunData);
			this.generalFileCSV2(generatorContext, updateProAutoRunData);
			this.generalFileCSV3(generatorContext, updateProAutoRunData);
			this.generalFileCSV4(generatorContext, updateProAutoRunData);
			this.generalFileCSV5(generatorContext, updateProAutoRunData);
			return ResultState.NORMAL_END;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultState.ABNORMAL_END;
		}
	}

	/**
	 * Gets the data output. ドメインモデル「更新処理自動実行ログ履歴」を取得する
	 *
	 * @param command the command
	 * @return the data output
	 */
	private UpdateProcessAutoRunDataDto getDataOutput(GetDataToOutputCommand command) {
		// Step ドメインモデル「更新処理自動実行ログ履歴」を取得する
		List<ProcessExecutionLogHistory> listProcessExeHistory = this
				.updateProcessAutomaticExecutionLogHistory(command);

		// 「更新処理自動実行ログ履歴」取得できたか
		// 取得できない
		if (listProcessExeHistory.isEmpty()) {
			// Step エラーメッセージ（Msg_37）を表示する
			throw new BusinessException("Msg_37");
		}

		// 取得できた
		// Step ドメインモデル「更新処理自動実行」を取得する
		String companyId = AppContexts.user().companyId();
		List<UpdateProcessAutoExecutionDto> lisProcessExecution = this.acquireTheDomainModel(companyId,
				listProcessExeHistory);

		// Step OUTPUT「更新処理自動実行データ」を作成する
		UpdateProcessAutoRunDataDto updateProAutoRunData = new UpdateProcessAutoRunDataDto(lisProcessExecution,
				Collections.emptyList(), Collections.emptyList(), command.getIsExportEmployeeName());

		List<ExecutionLogDetailDto> lstLogDetail = new ArrayList<>();
		List<String> empIds = new ArrayList<>();
		// 取得した「更新処理自動実行ログ履歴」をループする - Loop the acquired 「更新処理自動実行ログ履歴」
		for (ProcessExecutionLogHistory logHistory : listProcessExeHistory) {
			// create empty ExecutionLogDetailDto
			ExecutionLogDetailDto execLogDetail = ExecutionLogDetailDto.builder().build();

			// set ProcessExecutionLogHistoryDto to ExecutionLogDetailDto
			ProcessExecutionLogHistoryDto processExecutionLogHistoryDto = ProcessExecutionLogHistoryDto.builder()
					.build();
			logHistory.setMemento(processExecutionLogHistoryDto);
			execLogDetail.setProcessExecLogHistory(processExecutionLogHistoryDto);
			// 処理中の「更新処理自動実行ログ履歴．全体の業務エラー状態」を確認する
			// 全体の業務エラー状態 = true
			if (logHistory.getErrorBusiness().isPresent() && logHistory.getErrorBusiness().get()) {
				// Step 処理中の「更新処理自動実行ログ履歴．各処理の終了状態．業務エラー状態」 = true の項目を確認する
				for (ExecutionTaskLog taskLog : logHistory.getTaskLogList()) {
					if (taskLog.getErrorBusiness().isPresent() && taskLog.getErrorBusiness().get()) {
						String execIdByLogHistory = logHistory.getExecId();

						switch (taskLog.getProcExecTask()) {
						// Case 1 ドメインモデル「スケジューScheduleErrorLogル作成エラーログ」を取得する
						case SCH_CREATION:
							List<ScheduleErrorLogDto> scheduleErrorLogDtos = this.scheduleErrorLogRepo
									.findByExecutionId(execIdByLogHistory).stream().map(item -> {
										ScheduleErrorLogDto dto = ScheduleErrorLogDto.builder().build();
										item.saveToMemento(dto);
										return dto;
									}).collect(Collectors.toList());
							// 更新処理自動実行データ．実行ログ詳細．スケジュール作成エラー．社員ID
							empIds.addAll(scheduleErrorLogDtos.stream().map(ScheduleErrorLogDto::getEmployeeId)
									.collect(Collectors.toList()));
							execLogDetail.setScheduleErrorLog(scheduleErrorLogDtos);
							break;
						// Case 2 ドメインモデル「エラーメッセージ情報」を取得する
						case DAILY_CREATION:
						case DAILY_CALCULATION:
						case MONTHLY_AGGR:
						case RFL_APR_RESULT:
							if (execLogDetail.getErrMessageInfo().isEmpty()) {
								List<ErrMessageInfoDto> errMessageInfoDtos = this.errMessageInfoRepo
										.getAllErrMessageInfoByEmpID(execIdByLogHistory).stream()
										.map(item -> ErrMessageInfoDto.builder().employeeID(item.getEmployeeID())
												.empCalAndSumExecLogID(item.getEmpCalAndSumExecLogID())
												.resourceID(item.getResourceID().v())
												.executionContent(item.getExecutionContent().value)
												.disposalDay(item.getDisposalDay()).messageError(item.getMessageError().v())
												.build())
										.collect(Collectors.toList());
								// 更新処理自動実行データ．実行ログ詳細．日次・月次処理エラー．社員ID
								empIds.addAll(errMessageInfoDtos.stream().map(ErrMessageInfoDto::getEmployeeID)
										.collect(Collectors.toList()));
								execLogDetail.setErrMessageInfo(errMessageInfoDtos);
							}
							break;
						// Case 3 ドメインモデル「任意期間集計エラーメッセージ情報」を取得する
						case AGGREGATION_OF_ARBITRARY_PERIOD:
							List<AggrPeriodInforDto> aggrPeriodInforDtos = this.errorInfoRepo
									.findAll(execIdByLogHistory).stream()
									.map(item -> AggrPeriodInforDto.builder().memberId(item.getMemberId())
											.periodArrgLogId(item.getPeriodArrgLogId()).resourceId(item.getResourceId())
											.processDay(item.getProcessDay()).errorMess(item.getErrorMess().v())
											.build())
									.collect(Collectors.toList());
							// 更新処理自動実行データ．実行ログ詳細．任意期間集計エラー．社員ID
							empIds.addAll(aggrPeriodInforDtos.stream().map(AggrPeriodInforDto::getMemberId)
									.collect(Collectors.toList()));
							execLogDetail.setAggrPeriodInfor(aggrPeriodInforDtos);
							break;
						// Case 4 ドメインモデル「承認中間データエラーメッセージ情報（日別実績）」を取得する
						case APP_ROUTE_U_DAI:
							List<AppDataInfoDailyDto> appDataInfoDailyDtos = this.appDataInfoDailyRepo
									.getAppDataInfoDailyByExeID(execIdByLogHistory).stream()
									.map(item -> AppDataInfoDailyDto.builder().employeeId(item.getEmployeeId())
											.executionId(item.getExecutionId()).errorMessage(item.getErrorMessage().v())
											.build())
									.collect(Collectors.toList());
							// 更新処理自動実行データ．実行ログ詳細．承認ルート更新（日次）エラー．社員ID
							empIds.addAll(appDataInfoDailyDtos.stream().map(AppDataInfoDailyDto::getEmployeeId)
									.collect(Collectors.toList()));
							execLogDetail.setAppDataInfoDailies(appDataInfoDailyDtos);
							break;
						// Case 5 ドメインモデル「承認中間データエラーメッセージ情報（月別実績）」を取得する
						case APP_ROUTE_U_MON:
							List<AppDataInfoMonthlyDto> appDataInfoMonthlyDtos = this.appDataInfoMonthlyRepo
									.getAppDataInfoMonthlyByExeID(execIdByLogHistory).stream()
									.map(item -> AppDataInfoMonthlyDto.builder().employeeId(item.getEmployeeId())
											.executionId(item.getExecutionId()).errorMessage(item.getErrorMessage().v())
											.build())
									.collect(Collectors.toList());
							// 更新処理自動実行データ．実行ログ詳細．承認ルート更新（月次）エラー．社員ID
							empIds.addAll(appDataInfoMonthlyDtos.stream().map(AppDataInfoMonthlyDto::getEmployeeId)
									.collect(Collectors.toList()));
							execLogDetail.setAppDataInfoMonthlies(appDataInfoMonthlyDtos);
							break;
						// Case 6 ドメインモデル「外部受入エラーログ」のデータを取得する
						case EXTERNAL_ACCEPTANCE:
							execLogDetail.setExacErrorLogImports(
									this.exacErrorLogQueryFinder.getExacErrorLogByProcessId(execIdByLogHistory));
							break;
						// Case 7 ドメインモデル「外部出力結果ログ」のデータを取得する
						case EXTERNAL_OUTPUT:
							execLogDetail.setExternalOutLogImports(this.externalOutLogQueryFinder
									.getExternalOutLogById(companyId, execIdByLogHistory, 0)); // ProcessingClassification.ERROR
																								// = 0
							break;
						default:
							break;
						}
					}
				}
			}
			lstLogDetail.add(execLogDetail);
		}
		// remove duplicate employeeId by empIds
		List<String> deDupEmpIds = empIds.stream().distinct().collect(Collectors.toList());
		// Step 社員ID(List)から個人社員基本情報を取得
		List<EmployeeBasicInfoExportDto> empBasicIf = this.obtainBasicPersonalEmployeeInformation(deDupEmpIds);

		// Convert lst EmployeeBasicInfoExportDto -> lst EmployeeSearchDto
		List<EmployeeInfoImport> empInfoResult = empBasicIf.stream()
				.map(empBs -> new EmployeeInfoImport(empBs.getEmployeeId(), empBs.getEmployeeCode(),
						empBs.getBusinessName()))
				.collect(Collectors.toList());
		updateProAutoRunData.setLstEmployeeSearch(empInfoResult);

		// 「更新処理自動実行データ」を更新後「実行ログ詳細」をソートする
		// ::::「更新処理自動実行データ．実行ログ詳細．更新処理自動実行ログ履歴．前回実行日時」の昇順
		lstLogDetail.sort(Comparator.comparing(item -> item.getProcessExecLogHistory().getLastExecDateTime()));
		// 「更新処理自動実行データ」を更新後「実行ログ詳細」をソートする
		// ::::「更新処理自動実行データ．実行ログ詳細．更新処理自動実行ログ履歴．各処理の終了状態．更新処理」の昇順
		lstLogDetail.forEach(item -> {
			item.getProcessExecLogHistory().getTaskLogList()
					.sort(Comparator.comparing(logTask -> logTask.getProcExecTask().value));
		});
		// Step 「実行ログ詳細」を作成してOUTPUT「更新処理自動実行データ」に追加する
		updateProAutoRunData.setLstExecLogDetail(lstLogDetail);
		// Step OUTPUT「更新処理自動実行データ」を更新して返す
		return updateProAutoRunData;
	}

	private boolean checkExecutionContent(ProcessExecutionTask procExecTask, ErrMessageInfoDto errMessage) {
		switch (procExecTask) {
		case DAILY_CALCULATION: 
			return errMessage.getExecutionContent().equals(ExecutionContent.DAILY_CALCULATION.value);
		case DAILY_CREATION:
			return errMessage.getExecutionContent().equals(ExecutionContent.DAILY_CREATION.value);
		case MONTHLY_AGGR:
			return errMessage.getExecutionContent().equals(ExecutionContent.MONTHLY_AGGREGATION.value);
		case RFL_APR_RESULT:
			return errMessage.getExecutionContent().equals(ExecutionContent.REFLRCT_APPROVAL_RESULT.value);
		default:
			return false;
		}
	}

	private List<ProcessExecutionLogHistory> updateProcessAutomaticExecutionLogHistory(
			GetDataToOutputCommand commandHandlerContext) {
		String companyId = AppContexts.user().companyId();
		GeneralDateTime startDate = commandHandlerContext.getStartDate();
		GeneralDateTime endDate = commandHandlerContext.getEndDate();
		return this.updateProAutoExeRepo.getByCompanyIdAndDateAndEmployeeName(companyId, startDate, endDate);
	}

	private List<UpdateProcessAutoExecutionDto> acquireTheDomainModel(String companyId,
			List<ProcessExecutionLogHistory> processExeHistory) {
		List<String> execItemCd = processExeHistory.stream().map(item -> item.getExecItemCd().v()).distinct()
				.collect(Collectors.toList());
		return this.procExecRepo.findByCidAndExecItemCds(companyId, execItemCd).stream()
				.map(UpdateProcessAutoExecutionDto::createFromDomain).collect(Collectors.toList());
	}

	private List<EmployeeBasicInfoExportDto> obtainBasicPersonalEmployeeInformation(List<String> empIds) {
		List<EmployeeBasicInfoExportDto> result = new ArrayList<>();
		// Step ドメインモデル「社員データ管理情報」を取得する
		List<EmployeeBasicInfoFnImport> employeeInfoImports = this.syEmployeeFnAdapter.findBySIds(empIds);

		// Step ドメインモデル「社員データ管理情報」が取得できたかどうかチェックする
		// 取得できなかった場合（データ件数＝０件）
		if (employeeInfoImports.isEmpty()) {
			return result;
		}
		// 取得できた場合（データ件数≠０件）
		else {
			// Step ドメインモデル「所属会社履歴（社員別）」を取得する
			// get list personId
			List<String> lstPersonId = employeeInfoImports.stream().map(EmployeeBasicInfoFnImport::getPId)
					.collect(Collectors.toList());
			// Step アルゴリズム「個人IDから個人情報を取得」実行する
			List<EmployeeInfoExport> lstEmpSearch = this.getPersonalInformation(lstPersonId);
			// 取得できなかった場合（データ件数＝０件）
			if (lstEmpSearch.isEmpty()) {
				return result;
			}
			// 取得できた場合（データ件数≠０件)
			lstEmpSearch.forEach(person -> {
				Optional<EmployeeBasicInfoFnImport> fnImport = employeeInfoImports.stream()
						.filter(em -> person.getPersonId().equals(em.getPId())).findAny();

				EmployeeBasicInfoExportDto empBasicInfo = new EmployeeBasicInfoExportDto();
				empBasicInfo.setPersonId(person.getPersonId());
				empBasicInfo.setGender(person.getGender());
				empBasicInfo.setBirthDay(person.getBirthDay());
				empBasicInfo.setBusinessName(person.getBusinessName());
				empBasicInfo.setEmployeeCode(fnImport.map(EmployeeBasicInfoFnImport::getEmployeeCode).orElse(null));
				empBasicInfo.setEmployeeId(fnImport.map(EmployeeBasicInfoFnImport::getEmployeeId).orElse(null));
				empBasicInfo.setEntryDate(fnImport.map(EmployeeBasicInfoFnImport::getEntryDate).orElse(null));
				empBasicInfo.setRetiredDate(fnImport.map(EmployeeBasicInfoFnImport::getRetiredDate).orElse(null));
				result.add(empBasicInfo);
			});
		}
		return result;
	}

	private List<EmployeeInfoExport> getPersonalInformation(List<String> personId) {
		// Step ドメンモデル「個人」を取得
		List<EmployeeInfoExport> result = new ArrayList<>();

		List<PersonImport> lstPersonImport = this.personAdaptor.findByPids(personId);
		if (lstPersonImport.isEmpty()) {
			// Step 終了状態：個人情報取得失敗
			return result;
		} else {
			// Step 終了状態：成功
			lstPersonImport.forEach(ps -> {
				EmployeeInfoExport empSearchDto = new EmployeeInfoExport();
				empSearchDto.setPersonId(ps.getPersonId());
				empSearchDto.setGender(ps.getGender());
				empSearchDto.setBirthDay(ps.getBirthDate());
				empSearchDto.setBusinessName(ps.getPersonNameGroup().getBusinessName());
				result.add(empSearchDto);
			});
		}
		return result;
	}

	private String getTextResource(boolean check) {
		return check ? TextResource.localize("KBT002_290") : TextResource.localize("KBT002_291");
	}

	private <T> Optional<T> getEnumResource(Integer value, Class<T> c) {
		return Optional.ofNullable(value).map(data -> EnumAdaptor.valueOf(data, c));
	}

	private void generalFileCSV1(FileGeneratorContext generatorContext, UpdateProcessAutoRunDataDto updateProAutoRuns) {
		List<String> headerCSV1 = this.getTextHeaderCsv1();
		CsvReportWriter csv = this.generator.generate(generatorContext, FILE_NAME_CSV1 + CSV_EXTENSION, headerCSV1,
				"SHIFT-JIS");
		for (ExecutionLogDetailDto logDetail : updateProAutoRuns.getLstExecLogDetail()) {
			Optional<UpdateProcessAutoExecutionDto> procExec = updateProAutoRuns.getLstProcessExecution().stream()
					.filter(item -> item.getExecItemCode().equals(logDetail.getProcessExecLogHistory().getExecItemCd()))
					.findFirst();
			ProcessExecutionLogHistoryDto proHis = logDetail.getProcessExecLogHistory();

			Map<String, Object> rowCSV1 = new HashMap<>(this.saveHeaderCSV(proHis.getExecId(), proHis.getExecItemCd(),
					procExec.map(UpdateProcessAutoExecutionDto::getExecItemName)
							.orElse(TextResource.localize("KBT002_193"))));

			String lastExecDateTime = proHis.getLastExecDateTime() == null ? null
					: proHis.getLastExecDateTime().toString("yyyy-MM-dd HH:mm:ss");
			String lastEndExecDateTime = proHis.getLastEndExecDateTime() == null ? null
					: proHis.getLastEndExecDateTime().toString("yyyy-MM-dd HH:mm:ss");
			rowCSV1.put(headerCSV1.get(3), lastExecDateTime);
			rowCSV1.put(headerCSV1.get(4), lastEndExecDateTime);

			rowCSV1.put(headerCSV1.get(5), this.getHourByGetLastExecDateTimeAndGetLastEndExecDateTime(
					proHis.getLastExecDateTime(), proHis.getLastEndExecDateTime()));
			rowCSV1.put(headerCSV1.get(6), this.getEnumResource(proHis.getOverallStatus(), EndStatus.class)
					.map(data -> data.name).orElse(null));
			rowCSV1.put(headerCSV1.get(7),
					proHis.getErrorSystem() == null ? null : getTextResource(proHis.getErrorSystem()));
			rowCSV1.put(headerCSV1.get(8),
					proHis.getErrorBusiness() == null ? null : getTextResource(proHis.getErrorBusiness()));
			rowCSV1.put(headerCSV1.get(9),
					proHis.getOverallError() == null ? null
							: this.getEnumResource(proHis.getOverallError(), OverallErrorDetail.class)
									.map(data -> data.name).orElse(null));
			csv.writeALine(rowCSV1);
		}
		csv.destroy();
	}

	private void generalFileCSV2(FileGeneratorContext generatorContext, UpdateProcessAutoRunDataDto updateProAutoRuns) {
		List<String> headerCSV2 = this.getTextHeaderCsv2();
		CsvReportWriter csv = this.generator.generate(generatorContext, FILE_NAME_CSV2 + CSV_EXTENSION, headerCSV2,
				"SHIFT-JIS");
		for (ExecutionLogDetailDto logDetail : updateProAutoRuns.getLstExecLogDetail()) {
			Optional<UpdateProcessAutoExecutionDto> procExec = updateProAutoRuns.getLstProcessExecution().stream()
					.filter(item -> item.getExecItemCode().equals(logDetail.getProcessExecLogHistory().getExecItemCd()))
					.findFirst();
			ProcessExecutionLogHistoryDto proHis = logDetail.getProcessExecLogHistory();

			for (ExecutionTaskLog taskLog : proHis.getTaskLogList()) {
				Map<String, Object> rowCSV2 = new HashMap<>(this.saveHeaderCSV(proHis.getExecId(),
						proHis.getExecItemCd(), procExec.map(UpdateProcessAutoExecutionDto::getExecItemName)
								.orElse(TextResource.localize("KBT002_193"))));
				rowCSV2.put(headerCSV2.get(3), taskLog.getProcExecTask().name);
				String lastExecDateTime = taskLog.getLastExecDateTime()
						.map(item -> item.toString("yyyy-MM-dd HH:mm:ss")).orElse(null);
				String lastEndExecDateTime = taskLog.getLastEndExecDateTime()
						.map(item -> item.toString("yyyy-MM-dd HH:mm:ss")).orElse(null);
				rowCSV2.put(headerCSV2.get(4), lastExecDateTime);
				rowCSV2.put(headerCSV2.get(5), lastEndExecDateTime);
				rowCSV2.put(headerCSV2.get(6), this.getHourByGetLastExecDateTimeAndGetLastEndExecDateTime(
						taskLog.getLastExecDateTime().orElse(null), taskLog.getLastEndExecDateTime().orElse(null)));
				rowCSV2.put(headerCSV2.get(7),
						this.getEnumResource(taskLog.getStatus().map(data -> data.value).orElse(null), EndStatus.class)
								.map(data -> data.name).orElse(null));
				rowCSV2.put(headerCSV2.get(8), taskLog.getErrorSystem() == null ? null
						: taskLog.getErrorSystem().map(this::getTextResource).orElse(null));
				rowCSV2.put(headerCSV2.get(9), taskLog.getSystemErrorDetails().orElse(null));
				rowCSV2.put(headerCSV2.get(10), taskLog.getErrorBusiness() == null ? null
						: taskLog.getErrorBusiness().map(this::getTextResource).orElse(null));
				csv.writeALine(rowCSV2);
			}
		}
		csv.destroy();
	}

	private void generalFileCSV3(FileGeneratorContext generatorContext, UpdateProcessAutoRunDataDto updateProAutoRuns) {
		boolean checkEmptyTaskLogList = updateProAutoRuns.getLstExecLogDetail().stream()
				.filter(item -> !item.getProcessExecLogHistory().getTaskLogList().isEmpty()).findAny().isPresent();

		List<String> headerCSV3 = this.getTextHeaderCsv3(updateProAutoRuns.isExportEmpName());
		if (checkEmptyTaskLogList) {
			CsvReportWriter csv = this.generator.generate(generatorContext, FILE_NAME_CSV3 + CSV_EXTENSION, headerCSV3,
					"SHIFT-JIS");
			Map<String, List<Map<String, Object>>> dataMap = new HashMap<>();
			for (ExecutionLogDetailDto logDetail : updateProAutoRuns.getLstExecLogDetail()) {
				Optional<UpdateProcessAutoExecutionDto> procExec = updateProAutoRuns.getLstProcessExecution().stream()
						.filter(item -> item.getExecItemCode()
								.equals(logDetail.getProcessExecLogHistory().getExecItemCd()))
						.findFirst();
				ProcessExecutionLogHistoryDto proHis = logDetail.getProcessExecLogHistory();
				dataMap.put(proHis.execId, new ArrayList<>());
				for (ExecutionTaskLog taskLog : proHis.getTaskLogList()) {
					List<Map<String, Object>> datas = new ArrayList<>();
					switch (taskLog.getProcExecTask()) {
					case SCH_CREATION:
						for (ScheduleErrorLogDto log : logDetail.getScheduleErrorLog()) {
							Map<String, Object> rowCSV3 = this.saveHeaderCSV3(updateProAutoRuns, procExec, proHis,
									taskLog, headerCSV3, log.getEmployeeId());
							rowCSV3.put(headerCSV3.get(headerCSV3.size() - 2), log.getDate().toString("yyyy-MM-dd"));
							rowCSV3.put(headerCSV3.get(headerCSV3.size() - 1), log.getErrorContent());
							datas.add(rowCSV3);
						}
						dataMap.get(proHis.execId).addAll(datas);
						break;
					case DAILY_CREATION:
					case DAILY_CALCULATION:
					case RFL_APR_RESULT:
					case MONTHLY_AGGR:
						for (ErrMessageInfoDto log : logDetail.getErrMessageInfo()) {
							Map<String, Object> rowCSV3 = this.saveHeaderCSV3(updateProAutoRuns, procExec, proHis,
									taskLog, headerCSV3, log.getEmployeeID());
							if (this.checkExecutionContent(taskLog.getProcExecTask(), log)) {
								rowCSV3.put(headerCSV3.get(headerCSV3.size() - 2), log.getDisposalDay().toString("yyyy-MM-dd"));
								rowCSV3.put(headerCSV3.get(headerCSV3.size() - 1), log.getMessageError());
							}
							datas.add(rowCSV3);
						}
						dataMap.get(proHis.execId).addAll(datas);
						break;
					case AGGREGATION_OF_ARBITRARY_PERIOD:
						for (AggrPeriodInforDto log : logDetail.getAggrPeriodInfor()) {
							Map<String, Object> rowCSV3 = this.saveHeaderCSV3(updateProAutoRuns, procExec, proHis,
									taskLog, headerCSV3, log.getMemberId());
							rowCSV3.put(headerCSV3.get(headerCSV3.size() - 2),
									log.getProcessDay().toString("yyyy-MM-dd"));
							rowCSV3.put(headerCSV3.get(headerCSV3.size() - 1), log.getErrorMess());
							datas.add(rowCSV3);
						}
						dataMap.get(proHis.execId).addAll(datas);
						break;
					case APP_ROUTE_U_DAI:
						for (AppDataInfoDailyDto log : logDetail.getAppDataInfoDailies()) {
							Map<String, Object> rowCSV3 = this.saveHeaderCSV3(updateProAutoRuns, procExec, proHis,
									taskLog, headerCSV3, log.getEmployeeId());
							rowCSV3.put(headerCSV3.get(headerCSV3.size() - 2), null);
							rowCSV3.put(headerCSV3.get(headerCSV3.size() - 1), log.getErrorMessage());
							datas.add(rowCSV3);
						}
						dataMap.get(proHis.execId).addAll(datas);
						break;
					case APP_ROUTE_U_MON:
						for (AppDataInfoMonthlyDto log : logDetail.getAppDataInfoMonthlies()) {
							Map<String, Object> rowCSV3 = this.saveHeaderCSV3(updateProAutoRuns, procExec, proHis,
									taskLog, headerCSV3, log.getEmployeeId());
							rowCSV3.put(headerCSV3.get(headerCSV3.size() - 2), null);
							rowCSV3.put(headerCSV3.get(headerCSV3.size() - 1), log.getErrorMessage());
							datas.add(rowCSV3);
						}
						dataMap.get(proHis.execId).addAll(datas);
						break;
					default:
						break;
					}
				}
			}
			dataMap.values().forEach(list -> list.forEach(csv::writeALine));
			csv.destroy();
		}
	}

	private void generalFileCSV4(FileGeneratorContext generatorContext, UpdateProcessAutoRunDataDto updateProAutoRuns) {
		boolean checkListExacErrorLogEmpty = updateProAutoRuns.getLstExecLogDetail().stream()
				.filter(item -> !item.getExacErrorLogImports().isEmpty()).findAny().isPresent();

		List<String> headerCSV4 = this.getTextHeaderCsv4();
		if (checkListExacErrorLogEmpty) {
			CsvReportWriter csv = this.generator.generate(generatorContext, FILE_NAME_CSV4 + CSV_EXTENSION, headerCSV4,
					"SHIFT-JIS");
			for (ExecutionLogDetailDto logDetail : updateProAutoRuns.getLstExecLogDetail()) {
				Optional<UpdateProcessAutoExecutionDto> procExec = updateProAutoRuns.getLstProcessExecution().stream()
						.filter(item -> item.getExecItemCode()
								.equals(logDetail.getProcessExecLogHistory().getExecItemCd()))
						.findFirst();
				ProcessExecutionLogHistoryDto proHis = logDetail.getProcessExecLogHistory();
				logDetail.getExacErrorLogImports().sort(Comparator.comparing(ExacErrorLogQueryDto::getRecordNumber));
				for (ExacErrorLogQueryDto exacErrLog : logDetail.getExacErrorLogImports()) {
					Map<String, Object> rowCSV4 = new HashMap<>(this.saveHeaderCSV(proHis.getExecId(),
							proHis.getExecItemCd(), procExec.map(UpdateProcessAutoExecutionDto::getExecItemName)
									.orElse(TextResource.localize("KBT002_193"))));
					rowCSV4.put(headerCSV4.get(3), exacErrLog.getRecordNumber());
					rowCSV4.put(headerCSV4.get(4), exacErrLog.getCsvErrorItemName().orElse(null));
					rowCSV4.put(headerCSV4.get(5), exacErrLog.getItemName().orElse(null));
					rowCSV4.put(headerCSV4.get(6), exacErrLog.getCsvAcceptedValue().orElse(null));
					rowCSV4.put(headerCSV4.get(7), exacErrLog.getErrorContents().orElse(null));
					csv.writeALine(rowCSV4);
				}
			}
			csv.destroy();
		}
	}

	private void generalFileCSV5(FileGeneratorContext generatorContext, UpdateProcessAutoRunDataDto updateProAutoRuns) {
		boolean checkExternalErrLogEmpty = updateProAutoRuns.getLstExecLogDetail().stream()
				.filter(item -> !item.getExternalOutLogImports().isEmpty()).findAny().isPresent();

		List<String> headerCSV5 = this.getTextHeaderCsv5(updateProAutoRuns.isExportEmpName());
		if (checkExternalErrLogEmpty) {
			CsvReportWriter csv = this.generator.generate(generatorContext, FILE_NAME_CSV5 + CSV_EXTENSION, headerCSV5,
					"SHIFT-JIS");
			for (ExecutionLogDetailDto logDetail : updateProAutoRuns.getLstExecLogDetail()) {
				Optional<UpdateProcessAutoExecutionDto> procExec = updateProAutoRuns.getLstProcessExecution().stream()
						.filter(item -> item.getExecItemCode()
								.equals(logDetail.getProcessExecLogHistory().getExecItemCd()))
						.findFirst();
				ProcessExecutionLogHistoryDto proHis = logDetail.getProcessExecLogHistory();
				logDetail.getExternalOutLogImports()
						.sort(Comparator.comparing(ExternalOutLogQueryDto::getProcessCount).thenComparing(
								Comparator.nullsFirst(Comparator.comparing(data -> data.getErrorDate().orElse(null)))));
				for (ExternalOutLogQueryDto externalOutLogImport : logDetail.getExternalOutLogImports()) {
					Map<String, Object> rowCSV5 = new HashMap<>(this.saveHeaderCSV(proHis.getExecId(),
							proHis.getExecItemCd(), procExec.map(UpdateProcessAutoExecutionDto::getExecItemName)
									.orElse(TextResource.localize("KBT002_193"))));
					Optional<EmployeeInfoImport> emp = updateProAutoRuns.getLstEmployeeSearch().stream()
							.filter(item -> item.getSid().equals(externalOutLogImport.getErrorEmployee().orElse(null)))
							.findFirst();
					rowCSV5.put(headerCSV5.get(3), externalOutLogImport.getProcessCount());
					rowCSV5.put(headerCSV5.get(4), emp.map(EmployeeInfoImport::getScd).orElse(null));
					if (updateProAutoRuns.isExportEmpName()) {
						rowCSV5.put(headerCSV5.get(5), emp.map(EmployeeInfoImport::getBussinessName).orElse(null));
					}
					rowCSV5.put(headerCSV5.get(headerCSV5.size() - 4),
							externalOutLogImport.getErrorDate().map(item -> item.toString()).orElse(null));
					rowCSV5.put(headerCSV5.get(headerCSV5.size() - 3),
							externalOutLogImport.getErrorItem().orElse(null));
					rowCSV5.put(headerCSV5.get(headerCSV5.size() - 2),
							externalOutLogImport.getErrorTargetValue().orElse(null));
					rowCSV5.put(headerCSV5.get(headerCSV5.size() - 1),
							externalOutLogImport.getErrorContent().orElse(null));
					csv.writeALine(rowCSV5);
				}
			}
			csv.destroy();
		}
	}

	// saves duplicate fields of CSVs
	private Map<String, Object> saveHeaderCSV(String execId, String execCode, String execName) {
		Map<String, Object> rowCsv = new HashMap<>();
		rowCsv.put(TextResource.localize("KBT002_289"), execId);
		rowCsv.put(TextResource.localize("KBT002_7"), execCode);
		rowCsv.put(TextResource.localize("KBT002_8"), execName);
		return rowCsv;
	}

	private Map<String, Object> saveHeaderCSV3(UpdateProcessAutoRunDataDto updateProAutoRuns,
			Optional<UpdateProcessAutoExecutionDto> procExec, ProcessExecutionLogHistoryDto proHis,
			ExecutionTaskLog taskLog, List<String> headerCSV3, String sid) {
		Map<String, Object> rowCSV3 = new HashMap<>(this.saveHeaderCSV(proHis.getExecId(), proHis.getExecItemCd(),
				procExec.map(UpdateProcessAutoExecutionDto::getExecItemName)
						.orElse(TextResource.localize("KBT002_193"))));
		rowCSV3.put(headerCSV3.get(3), taskLog.getProcExecTask().name);
		Optional<EmployeeInfoImport> emp = updateProAutoRuns.getLstEmployeeSearch().stream()
				.filter(item -> item.getSid().equals(sid)).findFirst();
		rowCSV3.put(headerCSV3.get(4), emp.map(EmployeeInfoImport::getScd).orElse(null));
		if (updateProAutoRuns.isExportEmpName()) {
			rowCSV3.put(headerCSV3.get(5), emp.map(EmployeeInfoImport::getBussinessName).orElse(null));
		}
		return rowCSV3;
	}

	// convert GeneralDateTime to LocalDateTime and get Hour lastExecTime -
	// lasExecEndTime to String
	private String getHourByGetLastExecDateTimeAndGetLastEndExecDateTime(GeneralDateTime lastExecTime,
			GeneralDateTime lastExecEndTime) {
		if (lastExecTime == null || lastExecEndTime == null) {
			return null;
		}
		LocalDateTime convertLastTime = lastExecTime.localDateTime();
		LocalDateTime convertLastEndTime = lastExecEndTime.localDateTime();
		// get duration between convert last time and end time
		Duration dur = Duration.between(convertLastTime, convertLastEndTime);
		long millis = dur.toMillis();
		return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	}

	private void zipAllFileCSV(FileGeneratorContext generatorContext) {
		// アルゴリズム「結果ファイルの圧縮」を実行
		try {
			ApplicationTemporaryFilesContainer applicationTemporaryFilesContainer = applicationTemporaryFileFactory
					.createContainer();
			String fileName = FILE_NAME_ZIP + GeneralDateTime.now().toString("yyyyMMddHHmmss") + ZIP_EXTENSION;
			applicationTemporaryFilesContainer.zipWithName(generatorContext, fileName, false);
			applicationTemporaryFilesContainer.removeContainer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> getTextHeaderCsv1() {
		return LST_NAME_ID_HEADER_TABLE_CSV1.stream().map(TextResource::localize).collect(Collectors.toList());
	}

	private List<String> getTextHeaderCsv2() {
		return LST_NAME_ID_HEADER_TABLE_CSV2.stream().map(TextResource::localize).collect(Collectors.toList());
	}

	private List<String> getTextHeaderCsv3(boolean isExportEmpName) {
		if (!isExportEmpName) {
			return LST_NAME_ID_HEADER_TABLE_CSV3.stream().filter(item -> !item.equals("KBT002_292"))
					.map(TextResource::localize).collect(Collectors.toList());
		}
		return LST_NAME_ID_HEADER_TABLE_CSV3.stream().map(TextResource::localize).collect(Collectors.toList());
	}

	private List<String> getTextHeaderCsv4() {
		return LST_NAME_ID_HEADER_TABLE_CSV4.stream().map(TextResource::localize).collect(Collectors.toList());
	}

	private List<String> getTextHeaderCsv5(boolean isExportEmpName) {
		if (!isExportEmpName) {
			return LST_NAME_ID_HEADER_TABLE_CSV5.stream().filter(item -> !item.equals("KBT002_292"))
					.map(TextResource::localize).collect(Collectors.toList());
		}
		return LST_NAME_ID_HEADER_TABLE_CSV5.stream().map(TextResource::localize).collect(Collectors.toList());
	}
}