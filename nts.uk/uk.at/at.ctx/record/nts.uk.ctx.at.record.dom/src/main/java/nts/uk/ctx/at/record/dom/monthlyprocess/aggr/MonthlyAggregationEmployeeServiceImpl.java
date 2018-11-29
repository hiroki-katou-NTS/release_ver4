package nts.uk.ctx.at.record.dom.monthlyprocess.aggr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.diagnose.stopwatch.concurrent.ConcurrentStopwatches;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.monthly.performance.EditStateOfMonthlyPerRepository;
import nts.uk.ctx.at.record.dom.monthly.updatedomain.UpdateAllDomainMonthService;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.GetClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.AggregateMonthlyRecordService;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnAndRsvLeave;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.LockStatus;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ErrorPresent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExeStateOfCalAndSum;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス：月別集計　（社員の月別実績を集計する）
 * @author shuichi_ishida
 */
@Stateless
public class MonthlyAggregationEmployeeServiceImpl implements MonthlyAggregationEmployeeService {

	/** リポジトリ：就業計算と集計実行ログ */
	@Inject
	private EmpCalAndSumExeLogRepository empCalAndSumExeLogRepository;
	/** ドメインサービス：月別実績を集計する */
	@Inject
	private AggregateMonthlyRecordService aggregateMonthlyRecordService;
	// （2018.3.1 shuichi_ishida）　単純入出力テスト用クラス
//	private MonthlyRelatedDataInOutTest aggregateMonthlyRecordService;
	/** 集計期間を取得する */
	@Inject
	private GetClosurePeriod getClosurePeriod;
	/** 月別実績の編集状態 */
	@Inject
	private EditStateOfMonthlyPerRepository editStateRepo;
	/** 月別集計が必要とするリポジトリ */
	@Inject
	private RepositoriesRequiredByMonthlyAggr repositories;
	
	/** リポジトリ：月別実績の勤怠時間 */
//	@Inject
//	private TimeOfMonthlyRepository timeOfMonthlyRepo;
//	private AttendanceTimeOfMonthlyRepository attendanceTimeRepository;		// 旧版
	/** アダプタ：承認状態の作成（月次） */
//	@Inject
//	private CreateMonthlyApproverAdapter createMonthlyApproverAd;
	/** リポジトリ：週別実績の勤怠時間 */
//	@Inject
//	private AttendanceTimeOfWeeklyRepository attendanceTimeWeekRepo;
	/** リポジトリ：月別実績の任意項目 */
//	@Inject
//	private AnyItemOfMonthlyRepository anyItemRepository;
	/** リポジトリ：管理時間の36協定時間 */
//	@Inject
//	private AgreementTimeOfManagePeriodRepository agreementTimeRepository;
	/** 残数系データ */
//	@Inject
//	private RemainMergeRepository remainMergeRepo;
	/** エラーメッセージ情報 */
	@Inject
	private ErrMessageInfoRepository errMessageInfoRepository;
	/** 月別実績データストアドプロシージャ */
//	@Inject
//	private ProcMonthlyData procMonthlyData;
//	@Inject
//	private StoredProcdureProcess storedProcedureProcess;
	
	/** 月別実績(WORK)を登録する */
	@Inject
	private UpdateAllDomainMonthService monthService;
	
	/** 社員の月別実績を集計する */
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public ProcessState aggregate(AsyncCommandHandlerContext asyncContext, String companyId, String employeeId,
			GeneralDate criteriaDate, String empCalAndSumExecLogID, ExecutionType executionType) {

		ProcessState status = ProcessState.SUCCESS;
		val dataSetter = asyncContext.getDataSetter();
		
		// 月別集計で必要な会社別設定を取得する
		val companySets = MonAggrCompanySettings.loadSettings(companyId, this.repositories);
		if (companySets.getErrorInfos().size() > 0){
			
			// エラー処理
			List<MonthlyAggregationErrorInfo> errorInfoList = new ArrayList<>();
			for (val errorInfo : companySets.getErrorInfos().entrySet()){
				errorInfoList.add(new MonthlyAggregationErrorInfo(errorInfo.getKey(), errorInfo.getValue()));
			}
			this.errorProc(dataSetter, employeeId, empCalAndSumExecLogID, criteriaDate, errorInfoList);
			return status;
		}
		
		val aggrStatus = this.aggregate(asyncContext, companyId, employeeId, criteriaDate,
				empCalAndSumExecLogID, executionType, companySets);
		
		return aggrStatus.getState();
	}
	
	/** 社員の月別実績を集計する */
	@SuppressWarnings("rawtypes")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public MonthlyAggrEmpServiceValue aggregate(AsyncCommandHandlerContext asyncContext, String companyId, String employeeId,
			GeneralDate criteriaDate, String empCalAndSumExecLogID, ExecutionType executionType,
			MonAggrCompanySettings companySets) {
		
		MonthlyAggrEmpServiceValue status = new MonthlyAggrEmpServiceValue();
		val dataSetter = asyncContext.getDataSetter();
		
		// 前回集計結果　（年休積立年休の集計結果）
		AggrResultOfAnnAndRsvLeave prevAggrResult = new AggrResultOfAnnAndRsvLeave();

		ConcurrentStopwatches.start("11000:集計期間の判断：");
		
		// 集計期間の判断　（実締め毎集計期間だけをすべて取り出す）
		List<AggrPeriodEachActualClosure> aggrPeriods = new ArrayList<>();
		val closurePeriods = this.getClosurePeriod.get(companyId, employeeId, criteriaDate,
				Optional.empty(), Optional.empty(), Optional.empty());
		for (val closurePeriod : closurePeriods) aggrPeriods.addAll(closurePeriod.getAggrPeriods());
		
		// 全体の期間を求める
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
		}
		
		// 月別集計で必要な社員別設定を取得
		val employeeSets = MonAggrEmployeeSettings.loadSettings(
				companyId, employeeId, allPeriod, this.repositories);
		if (employeeSets.getErrorInfos().size() > 0){
			
			// エラー処理
			List<MonthlyAggregationErrorInfo> errorInfoList = new ArrayList<>();
			for (val errorInfo : employeeSets.getErrorInfos().entrySet()){
				errorInfoList.add(new MonthlyAggregationErrorInfo(errorInfo.getKey(), errorInfo.getValue()));
			}
			this.errorProc(dataSetter, employeeId, empCalAndSumExecLogID, criteriaDate, errorInfoList);
			return status;
		}
		
		ConcurrentStopwatches.stop("11000:集計期間の判断：");
		
		for (val aggrPeriod : aggrPeriods){
			val yearMonth = aggrPeriod.getYearMonth();
			val closureId = aggrPeriod.getClosureId();
			val closureDate = aggrPeriod.getClosureDate();
			val datePeriod = aggrPeriod.getPeriod();
			
			//ConcurrentStopwatches.start("12000:集計期間ごと：" + aggrPeriod.getYearMonth().toString());
			
			// 中断依頼が出されているかチェックする
//			if (asyncContext.hasBeenRequestedToCancel()) {
//				status.setState(ProcessState.INTERRUPTION);
//				return status;
//			}
			
			// 「就業計算と集計実行ログ」を取得し、実行状況を確認する
			val exeLogOpt = this.empCalAndSumExeLogRepository.getByEmpCalAndSumExecLogID(empCalAndSumExecLogID);
			if (!exeLogOpt.isPresent()){
				status.setState(ProcessState.INTERRUPTION);
				return status;
			}
			if (exeLogOpt.get().getExecutionStatus().isPresent()){
				val executionStatus = exeLogOpt.get().getExecutionStatus().get();
				if (executionStatus == ExeStateOfCalAndSum.START_INTERRUPTION){
					status.setState(ProcessState.INTERRUPTION);
					return status;
				}
			}
			
			// 処理する期間が締められているかチェックする
			if (employeeSets.checkClosedMonth(datePeriod.end())){
				continue;
			}
			
			// アルゴリズム「実績ロックされているか判定する」を実行する
			if (companySets.getDetermineActualLocked(datePeriod.end(), closureId.value) == LockStatus.LOCK){
				continue;
			}
			
			// 再実行の場合
			if (executionType == ExecutionType.RERUN){
				
				// 編集状態を削除
				this.editStateRepo.remove(employeeId, yearMonth, closureId, closureDate);
			}
			
			// 月別実績を集計する　（アルゴリズム）
			val value = this.aggregateMonthlyRecordService.aggregate(companyId, employeeId,
					yearMonth, closureId, closureDate, datePeriod, prevAggrResult, companySets, employeeSets,
					Optional.empty(), Optional.empty());
			
			// 状態を確認する
			if (value.getErrorInfos().size() > 0) {

				// エラー処理
				List<MonthlyAggregationErrorInfo> errorInfoList = new ArrayList<>();
				errorInfoList.addAll(value.getErrorInfos().values());
				this.errorProc(dataSetter, employeeId, empCalAndSumExecLogID, datePeriod.end(), errorInfoList);
				
				// 中断するエラーがある時、中断処理をする
				if (value.isInterruption()){
					//asyncContext.finishedAsCancelled();
					status.setState(ProcessState.INTERRUPTION);
					return status;
				}
				
				break;
			}
			
			// 前回集計結果の退避
			prevAggrResult = value.getAggrResultOfAnnAndRsvLeave();
			
			// 月別実績(WORK)を登録する
			this.monthService.merge(Arrays.asList(value.getIntegration()), datePeriod.end());
			
			status.getOutAggrPeriod().add(aggrPeriod);
			
			//ConcurrentStopwatches.stop("12000:集計期間ごと：" + aggrPeriod.getYearMonth().toString());
		}
		return status;
	}
	
	/**
	 * エラー処理
	 * @param dataSetter データセッター
	 * @param employeeId 社員ID
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param outYmd 出力年月日
	 * @param errorInfoList エラー情報リスト
	 */
	private void errorProc(
			TaskDataSetter dataSetter,
			String employeeId,
			String empCalAndSumExecLogID,
			GeneralDate outYmd,
			List<MonthlyAggregationErrorInfo> errorInfoList){
		
		// 「エラーあり」に更新
		dataSetter.updateData("monthlyAggregateHasError", ErrorPresent.HAS_ERROR.nameId);
		
		// エラー出力
		errorInfoList.sort((a, b) -> a.getResourceId().compareTo(b.getResourceId()));
		for (val errorInfo : errorInfoList){
			this.errMessageInfoRepository.add(new ErrMessageInfo(
					employeeId,
					empCalAndSumExecLogID,
					new ErrMessageResource(errorInfo.getResourceId()),
					ExecutionContent.MONTHLY_AGGREGATION,
					outYmd,
					errorInfo.getMessage()));
		}
	}
	
	/**
	 * 期間重複があるか
	 * @param period1 期間1
	 * @param period2 期間2
	 * @return true：重複あり、false：重複なし
	 */
	private boolean periodCompareEx(DatePeriod period1, DatePeriod period2){
		
		if (period1.start().after(period2.end())) return false;
		if (period1.end().before(period2.start())) return false;
		return true;
	}
}
