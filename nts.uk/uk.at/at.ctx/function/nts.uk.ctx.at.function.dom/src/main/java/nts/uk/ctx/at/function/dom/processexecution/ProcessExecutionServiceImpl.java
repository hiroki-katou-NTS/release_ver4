package nts.uk.ctx.at.function.dom.processexecution;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.task.schedule.cron.CronSchedule;
import nts.arc.task.schedule.job.jobdata.ScheduledJobUserData;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.CurrentExecutionStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogHistory;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskSettingRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogHistRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogManageRepository;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.ExecutionTaskSetting;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.TaskEndDate;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.TaskEndTime;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.CronType;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.EndDateClassification;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.EndTimeClassification;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.OneDayRepeatClassification;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.RepeatContentItem;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.primitivevalue.EndTime;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.primitivevalue.StartTime;
import nts.uk.shr.com.task.schedule.UkJobScheduleOptions;
import nts.uk.shr.com.task.schedule.UkJobScheduler;
import nts.uk.shr.com.task.schedule.UkScheduledJob;

@Stateless
public class ProcessExecutionServiceImpl implements ProcessExecutionService {

	private static final BigDecimal BIG_DECIMAL_100 = new BigDecimal(100);

	private static final BigDecimal BIG_DECIMAL_120 = new BigDecimal(120);

	@Inject
	private ProcessExecutionLogHistRepository processExecLogHistoryRepo;
	
	@Inject
	private ProcessExecutionLogManageRepository processExecutionLogManageRepository;
	
	@Inject
	private ExecutionTaskSettingRepository executionTaskSettingRepository;

	@Inject
	private UkJobScheduler scheduler;

	@Override
	public GeneralDateTime processNextExecDateTimeCreation(ExecutionTaskSetting execTaskSet) {
		GeneralDateTime nextExecDateTime = null;
		// アルゴリズム「スケジュールされたバッチ処理の次回実行日時を取得する」を実行する
		Optional<GeneralDateTime> oNextExecScheduleDateTime = execTaskSet.getScheduleId() != null
				// ・次回実行日時（スケジュールID）
				? this.scheduler.getNextFireTime(execTaskSet.getScheduleId())
				: Optional.empty();
		Optional<GeneralDateTime> oNextExecEndScheduleDateTime = execTaskSet.getEndScheduleId()
				// 次回実行日時（終了処理スケジュールID）
				.map(endScheduleId -> this.scheduler.getNextFireTime(endScheduleId)).orElse(Optional.empty());
		Optional<GeneralDateTime> oNextExecRepScheduleDateTime = execTaskSet.getRepeatScheduleId()
				// ・次回実行日時（1日の繰り返しスケジュールID）
				.map(repScheduleId -> this.scheduler.getNextFireTime(repScheduleId)).orElse(Optional.empty());

		// 「次回実行日時（スケジュールID）」をチェックする
		if (!oNextExecScheduleDateTime.isPresent()) {
			// 次回実行日時をNULLとする
			return nextExecDateTime;
		}

		// 「次回実行日時（1日の繰り返しスケジュールID）」または「次回実行日時（終了処理スケジュールID）」が取得できたか確認する
		GeneralDateTime nextExecScheduleDateTime = oNextExecScheduleDateTime.get();
		GeneralDateTime now = GeneralDateTime.now();
//		long epochSeconds = now.localDateTime().atZone(ZoneId.systemDefault()).toEpochSecond();
//		now = GeneralDateTime.ofEpochSecond(epochSeconds, ZoneOffset.ofHours(9));
		// 取得できなかった場合
		if (!oNextExecRepScheduleDateTime.isPresent()) {
			// 次回実行日時（スケジュールID）を次回実行日時とする
			nextExecDateTime = nextExecScheduleDateTime;
		}
		// 取得できた場合
		else {
			// 「次回実行日時（スケジュールID）」が現在のシステム日時を過ぎているか判定する
			// 次回実行日時（スケジュールID） ＞ システム日時
			if (nextExecScheduleDateTime.after(now) && ((oNextExecRepScheduleDateTime.isPresent()
					// Check if scheduleId has passed the limit datetime and move on to the next ID
					&& oNextExecRepScheduleDateTime.get().after(nextExecScheduleDateTime))
					|| (oNextExecEndScheduleDateTime.isPresent()
							&& oNextExecEndScheduleDateTime.get().after(nextExecScheduleDateTime)))) {
				// 次回実行日時（スケジュールID）を次回実行日時とする
				nextExecDateTime = nextExecScheduleDateTime;
			}
			// 次回実行日時（スケジュールID）<= システム日時 < 次回実行日時（1日の繰り返しスケジュールID）
			else if (oNextExecRepScheduleDateTime.isPresent() && oNextExecRepScheduleDateTime.get().after(now)
					// Check if repeatScheduleId has passed the limit datetime and move on to endScheduleId
					&& oNextExecEndScheduleDateTime.isPresent()
					&& oNextExecEndScheduleDateTime.get().after(oNextExecRepScheduleDateTime.get())) {
				// 次回実行日時（1日の繰り返しスケジュールID）を次回実行日時とする
				nextExecDateTime = oNextExecRepScheduleDateTime.get();
			}
			// 次回実行日時（1日の繰り返しスケジュールID）<= システム日時
			else if (oNextExecEndScheduleDateTime.isPresent() && oNextExecEndScheduleDateTime.get().after(now)) {
				// 次回実行日時（終了処理スケジュールID）を次回実行日時とする
				nextExecDateTime = oNextExecEndScheduleDateTime.get();
			}
			
		}

		// 「次回実行日時（暫定）」が「終了日＋終了時刻」を過ぎているか判定する
		GeneralDateTime endDateTime = null;
		TaskEndDate endDate = execTaskSet.getEndDate();
		if (endDate.getEndDateCls().equals(EndDateClassification.DATE) && endDate.getEndDate().isPresent()) {
			TaskEndTime endTime = execTaskSet.getEndTime();
			if (endTime != null && endTime.getEndTimeCls().equals(EndTimeClassification.YES)
					&& endTime.getEndTime().isPresent()) {
				// →「実行タスク設定.終了日.終了日」＋「実行タスク設定.終了時刻設定.終了時刻」＝終了日時
				endDateTime = GeneralDateTime
						.fromString(
								endDate.getEndDate().get().toString("yyyy/MM/dd") + " "
										+ endTime.getEndTime().get().hour() + ":" + StringUtil.padLeft(
												String.valueOf(endTime.getEndTime().get().minute()), 2, '0')
										+ ":00",
								"yyyy/MM/dd HH:mm:ss");
			} else {
				// →「実行タスク設定.終了日.終了日」＋0:00＝終了日時
				endDateTime = GeneralDateTime.fromString(
						endDate.getEndDate().get().toString("yyyy/MM/dd") + " 00:00:00", "yyyy/MM/dd HH:mm:ss");
			}
		}

		// 終了日時を過ぎている
		if (endDateTime != null && endDateTime.before(nextExecDateTime)) {
			// 次回実行日時をNULLとする
			return null;
		}
		return nextExecDateTime;
	}

	@Override
	public boolean isPassAverageExecTimeExceeded(String companyId, ExecutionCode execItemCode, //#115526
			GeneralDateTime execStartDateTime) {
		// ドメインモデル「更新処理自動実行管理」を取得する
		Optional<ProcessExecutionLogManage> optProcessExecutionLogManage = this.processExecutionLogManageRepository
				.getLogByCIdAndExecCd(companyId, execItemCode.v());
		// 「更新処理自動実行管理」をチェックする
		// 取得できた　AND　現在の実行状態 == 実行中
		if (optProcessExecutionLogManage.isPresent() &&
				optProcessExecutionLogManage.get().getCurrentStatus().isPresent() &&
				optProcessExecutionLogManage.get().getCurrentStatus().get().equals(CurrentExecutionStatus.RUNNING)) {
			// 過去の実行平均時間を取得する
			BigDecimal averageRunTime = this.getAverageRunTime(companyId, execItemCode);
			
			// 「実行平均時間」をチェックする
			// 実行平均時間 > 0
			if (averageRunTime.compareTo(BigDecimal.ZERO) > 0) {
				// 現在の経過時間と取得した「実行平均時間」を比較する
				BigDecimal currentRunTime = BigDecimal.valueOf(GeneralDateTime.now().seconds() - execStartDateTime.seconds());
				// 【比較方法】 システム日時 - INPUT．「実行開始日時」> 取得した「実行平均時間」*120%
				// 【OUTPUT】 boolean（true：超過している/false：超過してない）
				return currentRunTime.compareTo(averageRunTime.multiply(BIG_DECIMAL_120).divide(BIG_DECIMAL_100)) > 0;
			}
		}
		return false;
	}

	@Override
	public BigDecimal getAverageRunTime(String companyId, ExecutionCode execItemCd) {
		// ドメインモデル「更新処理自動実行ログ履歴」を取得する
		List<ProcessExecutionLogHistory> listHistory = this.processExecLogHistoryRepo
				.getByCompanyIdAndExecItemCd(companyId, execItemCd.v());
		if (listHistory.isEmpty()) {
			return BigDecimal.ZERO;
		}

		// 実行平均時間を計算する
		Integer sumExecutionTime = listHistory.stream()
				.filter(history -> history.getLastEndExecDateTime().isPresent()
						&& history.getLastExecDateTime().isPresent())
				.sorted(Comparator.comparing(item -> item.getLastExecDateTime().get())).limit(10)
				.mapToInt(history -> history.getLastEndExecDateTime().get().seconds()
						- history.getLastExecDateTime().get().seconds())
				.sum();

		// 計算した「実行平均時間」を返す
		return BigDecimal.valueOf(sumExecutionTime).divide(BigDecimal.valueOf(listHistory.size()),
				RoundingMode.CEILING);
	}

	@Override
	public UkJobScheduleOptions buildScheduleOptions(Class<? extends UkScheduledJob> job, CronType cronType,
			CronSchedule cron, ScheduledJobUserData userData, ExecutionTaskSetting execTaskSetting) {
		GeneralDateTime now = GeneralDateTime.now();
		Integer timeSystem = now.minutes() + now.hours() * 60;
		GeneralDate startDate = execTaskSetting.getStartDate();
		Optional<GeneralDate> endDate = execTaskSetting.getEndDate().getEndDateCls().equals(EndDateClassification.DATE)
				? execTaskSetting.getEndDate().getEndDate()
				: Optional.empty();
		StartTime startTime = execTaskSetting.getStartTime();
		Optional<EndTime> endTime = execTaskSetting.getOneDayRepInr().getOneDayRepCls()
				.equals(OneDayRepeatClassification.YES)
				&& execTaskSetting.getEndTime().getEndTimeCls().equals(EndTimeClassification.YES)
						? execTaskSetting.getEndTime().getEndTime()
						: Optional.empty();
		String scheduleIdTemplate = "KBT002_" + execTaskSetting.getExecItemCd().v();
		String scheduleId = null;

		// sheet 補足資料⑤
		// compare system date and system time
		if (execTaskSetting.getContent().equals(RepeatContentItem.ONCE_TIME)
				|| execTaskSetting.getContent().equals(RepeatContentItem.WEEKLY_DAYS)) {
			GeneralDate today = now.toDate();
			// →次回実行日時の日付がシステム日付で、システム日時が次回実行日時の開始時刻を過ぎていたら
			if (startDate.equals(today)) {
				if (startTime.v() < timeSystem) {
					// 開始日をシステム日付の 次の日付にする。
					startDate = today.addDays(1);
				}
			}
			// →次回実行日時がシステム日時より前であった場合
			else if (startDate.before(today)) {
				// 開始日をシステム日付にする。
				startDate = today;
			}
		}

		switch (cronType) {
		case START:
			scheduleId = scheduleIdTemplate;
			break;
		case REPEAT:
			scheduleId = scheduleIdTemplate + "_rep";
			break;
		case END:
			scheduleId = scheduleIdTemplate + "_end";
			break;
		}
		UkJobScheduleOptions.Builder builder = UkJobScheduleOptions.builder(job, scheduleId, cron).userData(userData)
				.startDate(startDate).startClock(startTime);
		endDate.ifPresent(builder::endDate);
		endTime.ifPresent(builder::endClock);
		return builder.build();
	}

	@Override
	public NextExecutionDateTimeDto isPassNextExecutionDateTime(String cid, String execItemCd) {
		// ドメインモデル「更新処理自動実行管理」を取得する
		Optional<ProcessExecutionLogManage> optProcessExecutionLogManage = this.processExecutionLogManageRepository
				.getLogByCIdAndExecCd(cid, execItemCd);
		// ドメインモデル「更新処理自動実行管理」をチェックする
		// 取得できた　AND　現在の実行状態 == 待機中
		if (optProcessExecutionLogManage.isPresent() && 
				optProcessExecutionLogManage.get().getCurrentStatus().isPresent() &&
				optProcessExecutionLogManage.get().getCurrentStatus().get().equals(CurrentExecutionStatus.WAITING)) {
			// ドメインモデル「実行タスク設定」を取得する
			Optional<ExecutionTaskSetting> optExecTaskSetting = this.executionTaskSettingRepository
					.getByCidAndExecCd(cid, execItemCd);
			// ドメインモデル「実行タスク設定」をチェックする
			// 取得できた
			if (optExecTaskSetting.isPresent()) {
				// 次回実行日時作成処理
				Optional<GeneralDateTime> nextExecDateTime = Optional.ofNullable(
						this.processNextExecDateTimeCreation(optExecTaskSetting.get()));
				// 取得した「次回実行日時」 >= システム日時
				if (nextExecDateTime.isPresent()) {
					boolean isPassNextExecDateTime = nextExecDateTime.get().before(GeneralDateTime.now());
					return new NextExecutionDateTimeDto(nextExecDateTime, isPassNextExecDateTime);
				}
			}
		}
		// 取得した「次回実行日時」と「判定結果 = false」を返す
		return new NextExecutionDateTimeDto();
	}

}
