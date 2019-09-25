package nts.uk.ctx.at.function.dom.processexecution.updatelogafterprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.adapter.employeemanage.EmployeeManageAdapter;
import nts.uk.ctx.at.function.dom.adapter.executionlog.ScheduleErrorLogAdapter;
import nts.uk.ctx.at.function.dom.adapter.executionlog.ScheduleErrorLogImport;
import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.AlarmCategoryFn;
import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.ExecutionLogAdapterFn;
import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.ExecutionLogErrorDetailFn;
import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.ExecutionLogImportFn;
import nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovaldaily.AppDataInfoDailyImport;
import nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovaldaily.CreateperApprovalDailyAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovalmonthly.AppDataInfoMonthlyImport;
import nts.uk.ctx.at.function.dom.adapter.workrecord.actualsituation.createperapprovalmonthly.CreateperApprovalMonthlyAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoImport;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogRepository;
import nts.uk.shr.com.i18n.TextResource;

/**
 * 各処理の後のログ更新処理
 * 
 * @author tutk
 *
 */
@Stateless
public class UpdateLogAfterProcess {
	@Inject
	private EmployeeManageAdapter employeeManageAdapter;

	@Inject
	private ErrMessageInfoAdapter errMessageInfoAdapter;

	@Inject
	private ScheduleErrorLogAdapter scheduleErrorLogAdapter;

	@Inject
	private CreateperApprovalDailyAdapter createperApprovalDailyAdapter;

	@Inject
	private CreateperApprovalMonthlyAdapter createperApprovalMonthlyAdapter;

	@Inject
	private ProcessExecutionLogRepository procExecLogRepo;

	@Inject
	private ExecutionLogAdapterFn executionLogAdapterFn;

	public void updateLogAfterProcess(ProcessExecutionTask processExecutionTask, String companyId, String execItemCd,
			ProcessExecution procExec, ProcessExecutionLog procExecLog, boolean isException, boolean isStopExec,
			String errorMessage) {
		// 就業担当者の社員ID（List）を取得する - RQ526
		List<String> listManagementId = employeeManageAdapter.getListEmpID(companyId, GeneralDate.today());

		String execId = procExecLog.getExecId();
		boolean isHasErrorBusiness = false;
		List<ScheduleErrorLogImport> listScheduleErrorLog = new ArrayList<>();
		List<AppDataInfoDailyImport> listAppDataInfoDaily = new ArrayList<>();
		List<AppDataInfoMonthlyImport> listAppDataInfoMonthly = new ArrayList<>();
		List<ErrMessageInfoImport> listErr = new ArrayList<>();
		switch (processExecutionTask) {
		case SCH_CREATION:
			// ドメインモデル「スケジュール作成エラーログ」を取得する
			listScheduleErrorLog = scheduleErrorLogAdapter.findByExecutionId(execId);
			if (!listScheduleErrorLog.isEmpty()) {
				isHasErrorBusiness = true;
			}
			break;
		case APP_ROUTE_U_DAI:
			// ドメインモデル「承認中間データエラーメッセージ情報（日別実績）」を取得する
			listAppDataInfoDaily = createperApprovalDailyAdapter.getAppDataInfoDailyByExeID(execId);
			if (!listAppDataInfoDaily.isEmpty()) {
				isHasErrorBusiness = true;
			}
			break;
		case APP_ROUTE_U_MON:
			// ドメインモデル「承認中間データエラーメッセージ情報（月別実績）」を取得する
			listAppDataInfoMonthly = createperApprovalMonthlyAdapter.getAppDataInfoMonthlyByExeID(execId);
			if (!listAppDataInfoMonthly.isEmpty()) {
				isHasErrorBusiness = true;
			}
			break;
		case AL_EXTRACTION:
				isHasErrorBusiness = false;
			break;
		default: // DAILY_CREATION DAILY_CALCULATION RFL_APR_RESULT MONTHLY_AGGR
			// ドメインモデル「エラーメッセージ情報」を取得する
			listErr = errMessageInfoAdapter.getAllErrMessageInfoByID(execId, getContent(processExecutionTask));
			if (!listErr.isEmpty()) {
				isHasErrorBusiness = true;
			}
			break;
		}

		// 取得できた場合
		if (isHasErrorBusiness) {
			// パラメータ「Exceptionの有無」を判断
			if (isException) {
				// ドメインモデル「更新処理自動実行ログ」を更新する
				procExecLog.getTaskLogList().forEach(task -> {
					if (processExecutionTask.value == task.getProcExecTask().value) {
						task.setLastEndExecDateTime(GeneralDateTime.now());
						task.setErrorSystem(true);
						task.setErrorBusiness(true);
						task.setStatus(Optional.ofNullable(isStopExec ? EndStatus.FORCE_END : EndStatus.SUCCESS));
					}
				});
				this.procExecLogRepo.update(procExecLog);

				// アルゴリズム「実行ログ登録」を実行する - RQ477
				ExecutionLogImportFn param = new ExecutionLogImportFn();
				// ・会社ID ＝ パラメータ.更新処理自動実行.会社ID
				param.setCompanyId(companyId);
				// ・管理社員ID ＝ パラメータ「就業担当社員ID」
				param.setManagerId(listManagementId);
				// ・実行完了日時 ＝ システム日時
				param.setFinishDateTime(GeneralDateTime.now());
				// ・エラーの有無 ＝ エラーあり
				param.setExistenceError(1);
				// ・実行内容 ＝ パラメータ「実行項目」
				param.setExecutionContent(getCategory(processExecutionTask));
				// ・実行ログエラー詳細 ＝ 取得したエラーメッセージ情報
				param.setTargerEmployee(getExecutionLogErrorDetail(processExecutionTask, listScheduleErrorLog,
						listAppDataInfoDaily, listAppDataInfoMonthly, listErr, isException, isHasErrorBusiness,
						listManagementId, errorMessage));

				executionLogAdapterFn.updateExecuteLog(param);

			} else {
				// ドメインモデル「更新処理自動実行ログ」を更新する
				procExecLog.getTaskLogList().forEach(task -> {
					if (processExecutionTask.value == task.getProcExecTask().value) {
						task.setLastEndExecDateTime(GeneralDateTime.now());
						task.setErrorSystem(false);
						task.setErrorBusiness(true);
						task.setStatus(Optional.ofNullable(isStopExec ? EndStatus.FORCE_END : EndStatus.SUCCESS));
					}
				});
				this.procExecLogRepo.update(procExecLog);

				// アルゴリズム「実行ログ登録」を実行する - RQ477
				ExecutionLogImportFn param = new ExecutionLogImportFn();
				// ・会社ID ＝ パラメータ.更新処理自動実行.会社ID
				param.setCompanyId(companyId);
				// ・管理社員ID ＝ パラメータ「就業担当社員ID」
				param.setManagerId(listManagementId);
				// ・実行完了日時 ＝ システム日時
				param.setFinishDateTime(GeneralDateTime.now());
				// ・エラーの有無 ＝ エラーあり
				param.setExistenceError(1);
				// ・実行内容 ＝ パラメータ「実行項目」
				param.setExecutionContent(getCategory(processExecutionTask));
				// ・実行ログエラー詳細 ＝ 取得したエラーメッセージ情報
				param.setTargerEmployee(getExecutionLogErrorDetail(processExecutionTask, listScheduleErrorLog,
						listAppDataInfoDaily, listAppDataInfoMonthly, listErr, isException, isHasErrorBusiness,
						listManagementId, errorMessage));
				executionLogAdapterFn.updateExecuteLog(param);

			}
		} else {// 取得できない場合
			// パラメータ「Exceptionの有無」を判断
			if (isException) {
				// ドメインモデル「更新処理自動実行ログ」を更新する
				procExecLog.getTaskLogList().forEach(task -> {
					if (processExecutionTask.value == task.getProcExecTask().value) {
						task.setLastEndExecDateTime(GeneralDateTime.now());
						task.setErrorSystem(true);
						task.setErrorBusiness(false);
						task.setStatus(Optional.ofNullable(isStopExec ? EndStatus.FORCE_END : EndStatus.SUCCESS));
					}
				});
				this.procExecLogRepo.update(procExecLog);

				// アルゴリズム「実行ログ登録」を実行する - RQ477
				ExecutionLogImportFn param = new ExecutionLogImportFn();
				// ・会社ID ＝ パラメータ.更新処理自動実行.会社ID
				param.setCompanyId(companyId);
				// ・管理社員ID ＝ パラメータ「就業担当社員ID」
				param.setManagerId(listManagementId);
				// ・実行完了日時 ＝ システム日時
				param.setFinishDateTime(GeneralDateTime.now());
				// ・エラーの有無 ＝ エラーあり
				param.setExistenceError(1);
				// ・実行内容 ＝ パラメータ「実行項目」
				param.setExecutionContent(getCategory(processExecutionTask));
				// ・実行ログエラー詳細 ＝ 取得したエラーメッセージ情報
				param.setTargerEmployee(getExecutionLogErrorDetail(processExecutionTask, listScheduleErrorLog,
						listAppDataInfoDaily, listAppDataInfoMonthly, listErr, isException, isHasErrorBusiness,
						listManagementId, errorMessage));

				executionLogAdapterFn.updateExecuteLog(param);

			} else {
				// ドメインモデル「更新処理自動実行ログ」を更新する
				procExecLog.getTaskLogList().forEach(task -> {
					if (processExecutionTask.value == task.getProcExecTask().value) {
						task.setLastEndExecDateTime(GeneralDateTime.now());
						task.setErrorSystem(false);
						task.setErrorBusiness(false);
						task.setStatus(Optional.ofNullable(isStopExec ? EndStatus.FORCE_END : EndStatus.SUCCESS));
					}
				});
				this.procExecLogRepo.update(procExecLog);

				// アルゴリズム「実行ログ登録」を実行する - RQ477
				ExecutionLogImportFn param = new ExecutionLogImportFn();
				// ・会社ID ＝ パラメータ.更新処理自動実行.会社ID
				param.setCompanyId(companyId);
				// ・管理社員ID ＝ パラメータ「就業担当社員ID」
				param.setManagerId(listManagementId);
				// ・実行完了日時 ＝ システム日時
				param.setFinishDateTime(GeneralDateTime.now());
				// ・エラーの有無 ＝ エラーあり
				param.setExistenceError(0);
				// ・実行内容 ＝ パラメータ「実行項目」
				param.setExecutionContent(getCategory(processExecutionTask));
				// ・実行ログエラー詳細 ＝ 取得したエラーメッセージ情報
				param.setTargerEmployee(Collections.emptyList());
				executionLogAdapterFn.updateExecuteLog(param);

			}
		}
		// INPUT「中断フラグ」をチェック
		if (isStopExec) {
			if (processExecutionTask == ProcessExecutionTask.SCH_CREATION) {
				// 各処理の終了状態 ＝ [日別作成、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [日別計算、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認結果反映、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.RFL_APR_RESULT, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [月別集計、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.MONTHLY_AGGR, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [アラーム抽出、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);
			} else if (processExecutionTask == ProcessExecutionTask.DAILY_CREATION) {
				// 各処理の終了状態 ＝ [日別計算、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認結果反映、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.RFL_APR_RESULT, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [月別集計、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.MONTHLY_AGGR, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [アラーム抽出、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);

			} else if (processExecutionTask == ProcessExecutionTask.DAILY_CALCULATION) {
				// 各処理の終了状態 ＝ [承認結果反映、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.RFL_APR_RESULT, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [月別集計、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.MONTHLY_AGGR, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [アラーム抽出、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);

			} else if (processExecutionTask == ProcessExecutionTask.RFL_APR_RESULT) {
				// 各処理の終了状態 ＝ [月別集計、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.MONTHLY_AGGR, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [アラーム抽出、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);

			} else if (processExecutionTask == ProcessExecutionTask.RFL_APR_RESULT) {
				// 各処理の終了状態 ＝ [月別集計、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.MONTHLY_AGGR, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [アラーム抽出、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);
			} else if (processExecutionTask == ProcessExecutionTask.MONTHLY_AGGR) {
				// 各処理の終了状態 ＝ [アラーム抽出、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);
			} else if (processExecutionTask == ProcessExecutionTask.AL_EXTRACTION) {
				// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
				// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);
			} else if (processExecutionTask == ProcessExecutionTask.APP_ROUTE_U_DAI) {
				// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.FORCE_END);
				// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);
			}
			// ドメインモデル「更新処理自動実行ログ」を更新する
			this.procExecLogRepo.update(procExecLog);
		}

	}

	private int getContent(ProcessExecutionTask processExecutionTask) {
		if (processExecutionTask == ProcessExecutionTask.DAILY_CREATION) {
			return 0;
		} else if (processExecutionTask == ProcessExecutionTask.DAILY_CALCULATION) {
			return 1;
		} else if (processExecutionTask == ProcessExecutionTask.RFL_APR_RESULT) {
			return 2;
		}
		return 3;
	}

	private AlarmCategoryFn getCategory(ProcessExecutionTask processExecutionTask) {
		return EnumAdaptor.valueOf(processExecutionTask.value + 1, AlarmCategoryFn.class);
	}

	private List<ExecutionLogErrorDetailFn> getExecutionLogErrorDetail(ProcessExecutionTask processExecutionTask,
			List<ScheduleErrorLogImport> listScheduleErrorLog, List<AppDataInfoDailyImport> listAppDataInfoDaily,
			List<AppDataInfoMonthlyImport> listAppDataInfoMonthly, List<ErrMessageInfoImport> listErr,
			boolean isException, boolean isHasErrorBusiness, List<String> listManagerId, String errorMessage) {
		List<ExecutionLogErrorDetailFn> listExecutionLogErrorDetail = new ArrayList<>();
		// 取得したエラーメッセージ情報（（※1）
		if (isHasErrorBusiness) {
			if (processExecutionTask == ProcessExecutionTask.SCH_CREATION) { // 実行項目 = 「スケジュール作成」
				for (ScheduleErrorLogImport scheduleErrorLogImport : listScheduleErrorLog) {
					listExecutionLogErrorDetail.add(new ExecutionLogErrorDetailFn(
							scheduleErrorLogImport.getErrorContent(), scheduleErrorLogImport.getEmployeeId()));
				}
			} else if (processExecutionTask == ProcessExecutionTask.AL_EXTRACTION) {
				for (String manager : listManagerId) {
					listExecutionLogErrorDetail.add(new ExecutionLogErrorDetailFn(errorMessage, manager));
				}
			} else if (processExecutionTask == ProcessExecutionTask.APP_ROUTE_U_DAI) { // 実行項目 = 「承認ルート更新（日次）」
				for (AppDataInfoDailyImport appDataInfoDailyImport : listAppDataInfoDaily) {
					listExecutionLogErrorDetail.add(new ExecutionLogErrorDetailFn(
							appDataInfoDailyImport.getErrorMessage(), appDataInfoDailyImport.getEmployeeId()));
				}
			} else if (processExecutionTask == ProcessExecutionTask.APP_ROUTE_U_DAI) { // 実行項目 = 「承認ルート更新（月次）」
				for (AppDataInfoMonthlyImport appDataInfoMonthlyImport : listAppDataInfoMonthly) {
					listExecutionLogErrorDetail.add(new ExecutionLogErrorDetailFn(
							appDataInfoMonthlyImport.getErrorMessage(), appDataInfoMonthlyImport.getEmployeeId()));
				}
			} else { // 実行項目 = 「日別作成・日別計算 or 承認結果の反映 or 月別実績の集計」
				for (ErrMessageInfoImport errMessageInfoImport : listErr) {
					listExecutionLogErrorDetail.add(new ExecutionLogErrorDetailFn(
							errMessageInfoImport.getMessageError(), errMessageInfoImport.getEmployeeID()));
				}
			}

		}
		// パラメータ「就業担当社員ID」, メッセージ「Msg_1339」or「Msg_1552」 ）（List）
		if (isException) {
			for (String manager : listManagerId) {
				listExecutionLogErrorDetail
						.add(new ExecutionLogErrorDetailFn(TextResource.localize(errorMessage), manager));
			}
		}

		return listExecutionLogErrorDetail;
	}

	private void updateEachTaskStatus(ProcessExecutionLog procExecLog, ProcessExecutionTask execTask,
			EndStatus status) {
		procExecLog.getTaskLogList().forEach(task -> {
			if (execTask.value == task.getProcExecTask().value) {
				task.setStatus(Optional.ofNullable(status));
			}
		});
	}
}
