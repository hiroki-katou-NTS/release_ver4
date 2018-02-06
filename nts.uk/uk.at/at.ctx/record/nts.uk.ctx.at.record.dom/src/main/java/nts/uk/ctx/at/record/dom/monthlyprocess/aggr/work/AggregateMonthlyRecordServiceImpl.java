package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

/**
 * ドメインサービス：月別実績を集計する
 * @author shuichi_ishida
 */
@Stateless
public class AggregateMonthlyRecordServiceImpl implements AggregateMonthlyRecordService {

	/** 労働条件項目 */
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	/** 労働条件 */
	@Inject
	private WorkingConditionRepository workingConditionRepository;
	/** 社員 */
	@Inject
	private EmpEmployeeAdapter empEmployeeAdapter;

	/** 月別集計が必要とするリポジトリ */
	@Inject
	private RepositoriesRequiredByMonthlyAggr repositories;
	
	/**
	 * 集計処理　（アルゴリズム）
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param datePeriod 期間
	 * @return 集計結果
	 */
	@Override
	public AggregateMonthlyRecordValue aggregate(String companyId, String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate, DatePeriod datePeriod) {
		
		val returnValue = new AggregateMonthlyRecordValue();
		
		// 「労働条件項目」を取得
		val workingConditionItems = this.workingConditionItemRepository.getBySidAndPeriodOrderByStrD(
				employeeId, datePeriod);
		
		// 社員を取得する
		EmployeeImport employee = null;
		employee = this.empEmployeeAdapter.findByEmpId(employeeId);
		if (employee == null){
			String errMsg = "社員データが見つかりません。　社員ID：" + employeeId;
			throw new BusinessException(new RawErrorMessage(errMsg));
		}
		
		// 項目の数だけループ
		for (val workingConditionItem : workingConditionItems){
			
			// 「労働条件」の該当履歴から期間を取得
			val historyId = workingConditionItem.getHistoryId();
			val workingConditionOpt = this.workingConditionRepository.getByHistoryId(historyId);
			if (!workingConditionOpt.isPresent()) continue;
			val workingCondition = workingConditionOpt.get();

			// 処理期間を計算　（処理期間と労働条件履歴期間の重複を確認する）
			val dateHistoryItems = workingCondition.getDateHistoryItem();
			if (dateHistoryItems.isEmpty()) continue;
			val term = dateHistoryItems.get(0).span();
			DatePeriod procPeriod = this.confirmProcPeriod(datePeriod, term);
			if (procPeriod == null) {
				// 履歴の期間と重複がない時
				continue;
			}
			
			// 退職月か確認する　（変形労働勤務の月単位集計：精算月判定に利用）
			boolean isRetireMonth = false;
			if (procPeriod.contains(employee.getRetiredDate())) isRetireMonth = true;
			
			// 入社前、退職後を期間から除く
			val termInOffice = new DatePeriod(employee.getEntryDate(), employee.getRetiredDate());
			procPeriod = this.confirmProcPeriod(procPeriod, termInOffice);
			if (procPeriod == null) {
				// 処理期間全体が、入社前または退職後の時
				continue;
			}
			
			// 月別実績の勤怠時間　初期データ作成
			val attendanceTime = new AttendanceTimeOfMonthly(employeeId, yearMonth,
					closureId, closureDate, procPeriod);
			
			// 労働制を確認する
			val workingSystem = workingConditionItem.getLaborSystem();
			
			// 月の計算
			attendanceTime.aggregate(companyId, workingSystem, isRetireMonth, this.repositories);
			
			// 縦計
			attendanceTime.verticalTotal(companyId, workingSystem, this.repositories);
			
			// 時間外超過

			// 計算結果を戻り値に蓄積
			returnValue.getAttendanceTimes().add(attendanceTime);
		}
		
		//*****（テスト）　2017/12検収テスト用。仮データ設定。
		/*
		Random random = new Random();
		val randomVal = random.nextInt(9) + 1;		// 1～9の乱数発生
		val attendanceTime = new AttendanceTimeOfMonthly(employeeId, yearMonth,
				closureId, closureDate, datePeriod);
		val monthlyCalculation = attendanceTime.getMonthlyCalculation();
		val totalWorkingTime = monthlyCalculation.getTotalWorkingTime();
		totalWorkingTime.getWorkTime().setWorkTime(new AttendanceTimeMonth(450 + randomVal));
		val aggrOvertime1 =	AggregateOverTime.of(
				new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(118),
						new AttendanceTimeMonth(120 + randomVal)),
				new AttendanceTimeMonth(0),
				TimeMonthWithCalculation.ofSameTime(0),
				new AttendanceTimeMonth(0),
				new AttendanceTimeMonth(0));
		val aggrOvertime2 =	AggregateOverTime.of(
				new OverTimeFrameNo(2),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(236),
						new AttendanceTimeMonth(240 + randomVal)),
				new AttendanceTimeMonth(0),
				TimeMonthWithCalculation.ofSameTime(0),
				new AttendanceTimeMonth(0),
				new AttendanceTimeMonth(0));
		totalWorkingTime.getOverTime().getAggregateOverTimeMap().put(
				aggrOvertime1.getOverTimeFrameNo(), aggrOvertime1);
		if (randomVal >= 5){
			totalWorkingTime.getOverTime().getAggregateOverTimeMap().put(
					aggrOvertime2.getOverTimeFrameNo(), aggrOvertime2);
		}
		val aggrHdwktime1 = AggregateHolidayWorkTime.of(
				new HolidayWorkFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(178),
						new AttendanceTimeMonth(180 + randomVal)),
				new AttendanceTimeMonth(0),
				TimeMonthWithCalculation.ofSameTime(0),
				new AttendanceTimeMonth(0),
				new AttendanceTimeMonth(0));
		val aggrHdwktime2 = AggregateHolidayWorkTime.of(
				new HolidayWorkFrameNo(2),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(296),
						new AttendanceTimeMonth(300 + randomVal)),
				new AttendanceTimeMonth(0),
				TimeMonthWithCalculation.ofSameTime(0),
				new AttendanceTimeMonth(0),
				new AttendanceTimeMonth(0));
		totalWorkingTime.getHolidayWorkTime().getAggregateHolidayWorkTimeMap().put(
				aggrHdwktime1.getHolidayWorkFrameNo(), aggrHdwktime1);
		if (randomVal >= 6){
			totalWorkingTime.getHolidayWorkTime().getAggregateHolidayWorkTimeMap().put(
					aggrHdwktime2.getHolidayWorkFrameNo(), aggrHdwktime2);
		}
		val actualWorkingTime = monthlyCalculation.getActualWorkingTime();
		actualWorkingTime.setWeeklyTotalPremiumTime(new AttendanceTimeMonth(540 + randomVal));
		actualWorkingTime.setMonthlyTotalPremiumTime(new AttendanceTimeMonth(2460 + randomVal));
		returnValue.getAttendanceTimes().add(attendanceTime);
		*/
		
		//*****（テスト）　2017/12集計設定読み込み
		/*
		val aggrSet = this.repositories.getAggrSettingMonthly().get("TESTCMP", "TESTWKP", "XM", "XESTSYA");
		val otlist = aggrSet.getRegularWork().getAggregateTimeSet().getTreatOverTimeOfLessThanCriteriaPerDay().getAutoExcludeOverTimeFrames();
		*/
		
		return returnValue;
	}	
	
	/**
	 * 処理期間との重複を確認する　（重複期間を取り出す）
	 * @param target 処理期間
	 * @param comparison 比較対象期間
	 * @return 重複期間　（null = 重複なし）
	 */
	private DatePeriod confirmProcPeriod(DatePeriod target, DatePeriod comparison){

		DatePeriod overlap = null;		// 重複期間
		
		// 開始前
		if (target.isBefore(comparison)) return overlap;
		
		// 終了後
		if (target.isAfter(comparison)) return overlap;
		
		// 重複あり
		overlap = target;
		
		// 開始日より前を除外
		if (overlap.contains(comparison.start())){
			overlap = overlap.cutOffWithNewStart(comparison.start());
		}
		
		// 終了日より後を除外
		if (overlap.contains(comparison.end())){
			overlap = overlap.cutOffWithNewEnd(comparison.end());
		}

		return overlap;
	}
}
