package nts.uk.ctx.at.record.dom.monthlyprocess.aggr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.OptimisticLockException;

import lombok.Getter;
import lombok.val;
import nts.arc.diagnose.stopwatch.concurrent.ConcurrentStopwatches;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.task.tran.AtomTask;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.error.ThrowableAnalyzer;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.creationprocess.getperiodcanprocesse.AchievementAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.creationprocess.getperiodcanprocesse.GetPeriodCanProcesse;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.creationprocess.getperiodcanprocesse.IgnoreFlagDuringLock;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.createdailyresults.ProcessState;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.createdailyresults.closegetunlockedperiod.ClosingGetUnlockedPeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.GetClosurePeriod;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLock;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.LockStatus;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ErrorPresent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExeStateOfCalAndSum;
import nts.uk.ctx.at.shared.dom.dailyperformanceprocessing.ErrMessageResource;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsRecRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.AggregateMonthlyRecordService;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.AggregateMonthlyRecordValue;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.MonthlyAggregationErrorInfo;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.IntegrationOfMonthly;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * ????????????????????????????????????????????????????????????????????????????????????
 * @author shuichi_ishida
 */
public class MonthlyAggregationEmployeeService {
	/**
	 * ????????????????????????????????????
	 * @param asyncContext ????????????????????????????????????
	 * @param companyId ??????ID
	 * @param employeeId ??????ID
	 * @param criteriaDate ?????????
	 * @param empCalAndSumExecLogID ?????????????????????????????????ID
	 * @param executionType ???????????????????????????????????????
	 * @return ????????????
	 */
	@SuppressWarnings("rawtypes")
	public static AggregationResult aggregate(RequireM1 require, CacheCarrier cacheCarrier,
			Optional<AsyncCommandHandlerContext> asyncContext, String companyId, String employeeId,
			GeneralDate criteriaDate, String empCalAndSumExecLogID, ExecutionType executionType,
			Optional<Boolean> canAggrWhenLock) {

		ProcessState status = ProcessState.SUCCESS;

		// ??????????????????????????????????????????????????????
		val companySets = MonAggrCompanySettings.loadSettings(require, companyId);
		if (companySets.getErrorInfos().size() > 0){

			// ???????????????
			List<MonthlyAggregationErrorInfo> errorInfoList = new ArrayList<>();
			for (val errorInfo : companySets.getErrorInfos().entrySet()){
				errorInfoList.add(new MonthlyAggregationErrorInfo(errorInfo.getKey(), errorInfo.getValue()));
			}

			return AggregationResult.build(status).newAtomTask(
					errorProc(require, asyncContext.map(c -> c.getDataSetter()), 
							employeeId, empCalAndSumExecLogID, criteriaDate, errorInfoList));
		}

		val aggrStatus = aggregate(require, cacheCarrier, asyncContext, companyId, employeeId, criteriaDate,
				empCalAndSumExecLogID, executionType, companySets, canAggrWhenLock);

		return aggrStatus;
	}

	/**
	 * ????????????????????????????????????
	 * @param asyncContext ????????????????????????????????????
	 * @param companyId ??????ID
	 * @param employeeId ??????ID
	 * @param criteriaDate ?????????
	 * @param empCalAndSumExecLogID ?????????????????????????????????ID
	 * @param executionType ???????????????????????????????????????
	 * @param companySets ???????????????????????????????????????
	 * @return ????????????
	 */
	@SuppressWarnings("rawtypes")
	public static AggregationResult aggregate(RequireM1 require, CacheCarrier cacheCarrier,
			Optional<AsyncCommandHandlerContext> asyncContext, String companyId, String employeeId,
			GeneralDate criteriaDate, String empCalAndSumExecLogID, ExecutionType executionType,
			MonAggrCompanySettings companySets, Optional<Boolean> canAggrWhenLock) {

		MonthlyAggrEmpServiceValue status = new MonthlyAggrEmpServiceValue();
		val dataSetter = asyncContext.map(c -> c.getDataSetter());

		// ????????????????????????????????????
		Optional<AbsRecRemainMngOfInPeriod> prevAbsRecResultOpt = Optional.empty();
		Optional<BreakDayOffRemainMngOfInPeriod> prevBreakDayOffresultOpt = Optional.empty();

		ConcurrentStopwatches.start("11000:????????????????????????");

		// ????????????????????????????????????????????????????????????????????????????????????
		List<AggrPeriodEachActualClosure> aggrPeriods = new ArrayList<>();
		val closurePeriods = GetClosurePeriod.get(require, companyId, employeeId, criteriaDate,
				Optional.empty(), Optional.empty(), Optional.empty());
		for (val closurePeriod : closurePeriods) aggrPeriods.addAll(closurePeriod.getAggrPeriods());

		// ??????????????????????????????????????????Redmine#107271???EA#3434???
		DatePeriod remainPeriod = calcPeriodForRemainingProc(companySets, aggrPeriods);

		// ???????????????????????????
		DatePeriod allPeriod = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		if (aggrPeriods.size() > 0){
			val headPeriod = aggrPeriods.get(0).getPeriod();
			allPeriod = new DatePeriod(headPeriod.start(), headPeriod.end());
			for (val aggrPeriod : aggrPeriods){
				GeneralDate startYmd = allPeriod.start();
				GeneralDate endYmd = allPeriod.end();
				if (startYmd.after(aggrPeriod.getPeriod().start())) startYmd = aggrPeriod.getPeriod().start();
				if (endYmd.before(aggrPeriod.getPeriod().end())) endYmd = aggrPeriod.getPeriod().end();
				allPeriod = new DatePeriod(startYmd, endYmd);
			}

			// ?????????36???????????????????????????????????????1?????????????????????????????????Redmine#107701???
			allPeriod = new DatePeriod(allPeriod.start().addMonths(-1), allPeriod.end());
		}

		// ????????????????????????????????????????????????
		val employeeSets = MonAggrEmployeeSettings.loadSettings(require, cacheCarrier,
				companyId, employeeId, allPeriod);
		if (employeeSets.getErrorInfos().size() > 0){

			// ???????????????
			List<MonthlyAggregationErrorInfo> errorInfoList = new ArrayList<>();
			for (val errorInfo : employeeSets.getErrorInfos().entrySet()){
				errorInfoList.add(new MonthlyAggregationErrorInfo(errorInfo.getKey(), errorInfo.getValue()));
			}
			return AggregationResult.build(status)
					.newAtomTask(errorProc(require, dataSetter, employeeId, empCalAndSumExecLogID,
											criteriaDate, errorInfoList));
		}

		ConcurrentStopwatches.stop("11000:????????????????????????");

		List<AtomTask> atomTasks = new ArrayList<>();
		
		//get ?????????????????????/??????????????????
		Optional<ExecutionLog> executionLog = require.getByExecutionContent(empCalAndSumExecLogID, ExecutionContent.MONTHLY_AGGREGATION.value);
		
		IgnoreFlagDuringLock ignoreFlagDuringLock = executionLog.flatMap(c -> c.getIsCalWhenLock())
				.map(c -> c ? IgnoreFlagDuringLock.CAN_CAL_LOCK : IgnoreFlagDuringLock.CANNOT_CAL_LOCK)
				.orElse(IgnoreFlagDuringLock.CANNOT_CAL_LOCK);
		
		List<IntegrationOfMonthly> cacheMonthly = new ArrayList<>();
		MonthlyAggregationEmployeeServiceRequireImpl requireCache = new MonthlyAggregationEmployeeServiceRequireImpl(cacheMonthly, require);
		
		for (val aggrPeriod : aggrPeriods) {
			val yearMonth = aggrPeriod.getYearMonth();
			val closureId = aggrPeriod.getClosureId();
			val closureDate = aggrPeriod.getClosureDate();
			val datePeriod = aggrPeriod.getPeriod();

			// ?????????????????????????????????????????????????????????????????????????????????
			val exeLogOpt = require.calAndSumExeLog(empCalAndSumExecLogID);
			if (exeLogOpt.isPresent() && exeLogOpt.get().getExecutionStatus().isPresent()){
				val executionStatus = exeLogOpt.get().getExecutionStatus().get();
				if (executionStatus == ExeStateOfCalAndSum.START_INTERRUPTION){
					status.setState(ProcessState.INTERRUPTION);
					return AggregationResult.build(status);
				}
			}
			
			//??????????????????????????????????????????????????????
			List<DatePeriod> listPeriod = ClosingGetUnlockedPeriod.get(require, datePeriod, closureId.value, ignoreFlagDuringLock, AchievementAtr.MONTHLY);
			if(listPeriod.isEmpty()) {
				continue;
			}
			for(DatePeriod periodNew : listPeriod) {
				// ??????????????????
				if (executionType == ExecutionType.RERUN){
	
					// ?????????????????????
					require.transaction(AtomTask.of(() -> require.removeMonthEditState(employeeId, yearMonth, closureId, closureDate)));
				}
	
				// ????????????????????????????????????????????????????????????Redmine#107271???EA#3434???
				Boolean isRemainProc = false;
				if (remainPeriod.contains(periodNew.start())) isRemainProc = true;
	
				AggregateMonthlyRecordValue value = new AggregateMonthlyRecordValue();
				try {
					// ??????????????????????????????????????????????????????
					value = AggregateMonthlyRecordService.aggregate(requireCache, cacheCarrier, companyId, employeeId,
							yearMonth, closureId, closureDate, periodNew,
							prevAbsRecResultOpt, prevBreakDayOffresultOpt,
							companySets, employeeSets, Optional.empty(), Optional.empty(), isRemainProc);
				}
				catch (Exception ex) {
					boolean isOptimisticLock = new ThrowableAnalyzer(ex).findByClass(OptimisticLockException.class).isPresent();
					if (!isOptimisticLock) {
						throw ex;
					}
					atomTasks.add(MonthlyAggregationErrorService.errorProcForOptimisticLock(require,
							dataSetter, employeeId, empCalAndSumExecLogID, periodNew.end()));
					aggrPeriod.setHappendOptimistLockError(true);
					status.getOutAggrPeriod().add(aggrPeriod);
					return AggregationResult.build(status, atomTasks);
				}
	
				// ?????????????????????
				if (value.getErrorInfos().size() > 0) {
	
					// ???????????????
					List<MonthlyAggregationErrorInfo> errorInfoList = new ArrayList<>();
					errorInfoList.addAll(value.getErrorInfos().values());
					atomTasks.add(errorProc(require, dataSetter, employeeId, empCalAndSumExecLogID, periodNew.end(), errorInfoList));
	
					// ?????????????????????????????????????????????????????????
					if (value.isInterruption()){
						//asyncContext.finishedAsCancelled();
						status.setState(ProcessState.INTERRUPTION);
						return AggregationResult.build(status, atomTasks);
					}
	
					break;
				}
	
				try {
					// ????????????(WORK)???????????????
					val monthlyData  = value.getIntegration();
					cacheMonthly.add(monthlyData);
					atomTasks.add(mergeMonth(require, Arrays.asList(monthlyData), periodNew.end()));
				}
				catch (Exception ex) {
					boolean isOptimisticLock = new ThrowableAnalyzer(ex).findByClass(OptimisticLockException.class).isPresent();
					if (!isOptimisticLock) {
						throw ex;
					}
					atomTasks.add(MonthlyAggregationErrorService.errorProcForOptimisticLock(
							require, dataSetter, employeeId, empCalAndSumExecLogID, periodNew.end()));
					aggrPeriod.setHappendOptimistLockError(true);
					return AggregationResult.build(status, atomTasks);
				}
				finally {
					status.getOutAggrPeriod().add(aggrPeriod);
				}
			}
		}
		return AggregationResult.build(status, atomTasks);
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 * @param baseDate ?????????
	 * @param closureId ??????ID
	 * @return ???????????????????????????????????????or??????????????????
	 */
	public static LockStatus getDetermineActualLocked(RequireM4 require,
			MonAggrCompanySettings comSettings,
			GeneralDate baseDate, Integer closureId){
		// ???????????????
		val actualLock = require.actualLock(AppContexts.user().companyId(), closureId);

		LockStatus currentLockStatus = LockStatus.UNLOCK;

		// ????????????????????????????????????
		if (!actualLock.isPresent()) return currentLockStatus;

		// ????????????????????????????????????
		currentLockStatus = actualLock.get().getMonthlyLockState();

		// ????????????????????????????????????
		if (currentLockStatus == LockStatus.UNLOCK) return LockStatus.UNLOCK;

		// ????????????????????????????????????????????????????????????
		if (!comSettings.getCurrentMonthPeriodMap().containsKey(closureId)) return LockStatus.UNLOCK;
		DatePeriod currentPeriod = comSettings.getCurrentMonthPeriodMap().get(closureId);
		if (currentPeriod == null) return LockStatus.UNLOCK;
		if (currentPeriod.contains(baseDate)) {
			// ?????????????????????????????????????????????
			return LockStatus.LOCK;
		}
		//????????????????????????????????????????????????
		return LockStatus.UNLOCK;
	}

	private static AtomTask mergeMonth(RequireM3 require, List<IntegrationOfMonthly> domains, GeneralDate targetDate) {

		return AtomTask.of(() -> require.merge(domains, targetDate) );
	}

	/**
	 * ????????????????????????????????????
	 * @param companySets ???????????????????????????????????????
	 * @param aggrPeriods ?????????????????????
	 * @return ??????
	 */
	private static DatePeriod calcPeriodForRemainingProc(
			MonAggrCompanySettings companySets,
			List<AggrPeriodEachActualClosure> aggrPeriods){

		DatePeriod result = new DatePeriod(GeneralDate.min(), GeneralDate.min());

		// ?????????????????????
		aggrPeriods.sort((a, b) -> a.getPeriod().start().compareTo(b.getPeriod().start()));
		for (AggrPeriodEachActualClosure aggrPeriod : aggrPeriods) {

			// ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
			int closureId = aggrPeriod.getClosureId().value;
			if (companySets.getCurrentMonthPeriodMap().containsKey(closureId)) {

				// ?????????????????????????????????????????????
				DatePeriod currentPeriod = companySets.getCurrentMonthPeriodMap().get(closureId);
				if (periodCompareEx(currentPeriod, aggrPeriod.getPeriod()) == true) {

					// ????????????????????????????????????
					if (result.start() == GeneralDate.min()) {
						// 1?????????????????????????????????????????????
						result = new DatePeriod(aggrPeriod.getPeriod().start(), aggrPeriod.getPeriod().end());
					}
					else {
						// 2??????????????????????????????????????????
						result = new DatePeriod(result.start(), aggrPeriod.getPeriod().end());
					}
				}
			}
		}

		// ????????????????????????????????????
		return result;
	}

	/**
	 * ???????????????
	 * @param dataSetter ?????????????????????
	 * @param employeeId ??????ID
	 * @param empCalAndSumExecLogID ?????????????????????????????????ID
	 * @param outYmd ???????????????
	 * @param errorInfoList ????????????????????????
	 */
	private static AtomTask errorProc(RequireM2 require,
			Optional<TaskDataSetter> dataSetter,
			String employeeId,
			String empCalAndSumExecLogID,
			GeneralDate outYmd,
			List<MonthlyAggregationErrorInfo> errorInfoList){

		// ??????????????????????????????
		dataSetter.ifPresent(ds -> ds.updateData("monthlyAggregateHasError", ErrorPresent.HAS_ERROR.nameId));

		// ???????????????
		errorInfoList.sort((a, b) -> a.getResourceId().compareTo(b.getResourceId()));
		return AtomTask.of(() -> {
			for (val errorInfo : errorInfoList){
				require.add(new ErrMessageInfo(
						employeeId,
						empCalAndSumExecLogID,
						new ErrMessageResource(errorInfo.getResourceId()),
						ExecutionContent.MONTHLY_AGGREGATION,
						outYmd,
						errorInfo.getMessage()));
			}
		});
	}

	/**
	 * ????????????????????????
	 * @param period1 ??????1
	 * @param period2 ??????2
	 * @return true??????????????????false???????????????
	 */
	private static boolean periodCompareEx(DatePeriod period1, DatePeriod period2){

		if (period1.start().after(period2.end())) return false;
		if (period1.end().before(period2.start())) return false;
		return true;
	}

	@Getter
	public static class AggregationResult {

		MonthlyAggrEmpServiceValue status;

		List<AtomTask> atomTasks;

		private AggregationResult (MonthlyAggrEmpServiceValue status, List<AtomTask> atomTasks) {
			this.atomTasks = atomTasks;
			this.status = status;
		}

		static AggregationResult build(MonthlyAggrEmpServiceValue status) {
			return new AggregationResult(status, new ArrayList<>());
		}

		static AggregationResult build(ProcessState status) {
			MonthlyAggrEmpServiceValue value = new MonthlyAggrEmpServiceValue();
			value.setState(status);
			return new AggregationResult(value, new ArrayList<>());
		}

		static AggregationResult build(MonthlyAggrEmpServiceValue status, List<AtomTask> atomTasks) {
			return new AggregationResult(status, atomTasks);
		}

		public AggregationResult newAtomTask(AtomTask task) {
			this.atomTasks.add(task);
			return this;
		}
	}

	public static interface RequireM4 {
		Optional<ActualLock> actualLock(String comId, int closureId);
		
		Optional<ExecutionLog> getByExecutionContent(String empCalAndSumExecLogID, int executionContent);

	}

	public static interface RequireM2 extends MonthlyAggregationErrorService.RequireM1 {}

	public static interface RequireM1 extends MonAggrEmployeeSettings.RequireM2,
		GetClosurePeriod.RequireM1, AggregateMonthlyRecordService.RequireM2,
		RequireM2, RequireM3, RequireM4, MonAggrCompanySettings.RequireM6,
		GetPeriodCanProcesse.Require {

		Optional<EmpCalAndSumExeLog> calAndSumExeLog (String empCalAndSumExecLogID);

		void removeMonthEditState(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);

		void transaction(AtomTask task);
	}

	public static interface RequireM3 {

		void merge(List<IntegrationOfMonthly> domains, GeneralDate targetDate);
	}

}
