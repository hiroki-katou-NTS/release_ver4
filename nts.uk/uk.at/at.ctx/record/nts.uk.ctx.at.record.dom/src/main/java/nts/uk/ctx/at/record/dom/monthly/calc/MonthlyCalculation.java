package nts.uk.ctx.at.record.dom.monthly.calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.diagnose.stopwatch.concurrent.ConcurrentStopwatches;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.util.Time;
import nts.uk.ctx.at.record.dom.monthly.AttendanceItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.actualworkingtime.RegularAndIrregularTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.Flex;
import nts.uk.ctx.at.record.dom.monthly.flex.CalcFlexChangeDto;
import nts.uk.ctx.at.record.dom.monthly.flex.ConditionCalcResult;
import nts.uk.ctx.at.record.dom.monthly.roundingset.RoundingSetOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.MonthlyAggregationErrorInfo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyOldDatas;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByDefo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByFlex;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByReg;
import nts.uk.ctx.at.record.dom.weekly.AttendanceTimeOfWeekly;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.statutory.worktime.shared.WeekStart;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleofovertimework.RoleOvertimeWorkEnum;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleopenperiod.RoleOfOpenPeriodEnum;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績の月の計算
 * @author shuichi_ishida
 */
@Getter
public class MonthlyCalculation {

	/** 実働時間 */
	private RegularAndIrregularTimeOfMonthly actualWorkingTime;
	/** フレックス時間 */
	private FlexTimeOfMonthly flexTime;
	/** 法定労働時間 */
	private AttendanceTimeMonth statutoryWorkingTime;
	/** 集計時間 */
	private AggregateTotalWorkingTime aggregateTime;
	/** 総労働時間 */
	private AttendanceTimeMonth totalWorkingTime;
	/** 総拘束時間 */
	private AggregateTotalTimeSpentAtWork totalTimeSpentAtWork;
	/** 36協定時間 */
	@Setter
	private AgreementTimeOfMonthly agreementTime;

	/** 会社ID */
	private String companyId;
	/** 社員ID */
	private String employeeId;
	/** 年月 */
	private YearMonth yearMonth;
	/** 締めID */
	private ClosureId closureId;
	/** 締め日付 */
	private ClosureDate closureDate;
	/** 期間 */
	private DatePeriod procPeriod;
	/** 労働条件項目 */
	private WorkingConditionItem workingConditionItem;
	/** 労働制 */
	private WorkingSystem workingSystem;
	/** 社員 */
	private EmployeeImport employee;
	/** 職場ID */
	private String workplaceId;
	/** 雇用コード */
	private String employmentCd;
	/** 退職月度がどうか */
	private boolean isRetireMonth;
	/** 締め */
	private Optional<Closure> closureOpt;

	/** 通常勤務が必要とする設定 */
	private SettingRequiredByReg settingsByReg;
	/** 変形労働勤務が必要とする設定 */
	private SettingRequiredByDefo settingsByDefo;
	/** フレックス勤務が必要とする設定 */
	private SettingRequiredByFlex settingsByFlex;
	/** 月別集計で必要な会社別設定 */
	private MonAggrCompanySettings companySets;
	/** 月別集計で必要な社員別設定 */
	private MonAggrEmployeeSettings employeeSets;
	
	/** 月の計算中の日別実績データ */
	private MonthlyCalculatingDailys monthlyCalculatingDailys;
	/** 実績の勤務情報リスト */
	private Map<GeneralDate, WorkInformation> workInfoOfRecordMap;
	/** 月別実績の勤怠時間　（集計前） */
	private Optional<AttendanceTimeOfMonthly> originalData;
	/** 週別実績の勤怠時間 */
	private List<AttendanceTimeOfWeekly> attendanceTimeWeeks;

	/** 開始週NO */
	private int startWeekNo;
	/** 年度 */
	private Year year;
	/** 管理期間の36協定時間 */
	private AgreementTimeOfManagePeriod agreementTimeOfManagePeriod;
	/** エラー情報 */
	private List<MonthlyAggregationErrorInfo> errorInfos;
	
	/** 労働条件項目 */
	private List<WorkingConditionItem> workingConditionItems;
	/** 労働条件 */
	private Map<String, DatePeriod> workingConditions;
	
	/**
	 * コンストラクタ
	 */
	public MonthlyCalculation(){

		this.actualWorkingTime = new RegularAndIrregularTimeOfMonthly();
		this.flexTime = new FlexTimeOfMonthly();
		this.statutoryWorkingTime = new AttendanceTimeMonth(0);
		this.aggregateTime = new AggregateTotalWorkingTime();
		this.totalWorkingTime = new AttendanceTimeMonth(0);
		this.totalTimeSpentAtWork = new AggregateTotalTimeSpentAtWork();
		this.agreementTime = new AgreementTimeOfMonthly();
		
		this.companyId = "empty";
		this.employeeId = "empty";
		this.yearMonth = new YearMonth(0);
		this.closureId = ClosureId.RegularEmployee;
		this.closureDate = new ClosureDate(1, true);
		this.procPeriod = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		this.workingConditionItem = null;
		this.workingSystem = WorkingSystem.REGULAR_WORK;
		this.employee = null;
		this.workplaceId = "empty";
		this.employmentCd = "empty";
		this.isRetireMonth = false;
		this.closureOpt = Optional.empty();
		this.settingsByReg = new SettingRequiredByReg(this.companyId);
		this.settingsByDefo = new SettingRequiredByDefo(this.companyId);
		this.settingsByFlex = new SettingRequiredByFlex();
		this.companySets = null;
		this.employeeSets = null;
		
		this.monthlyCalculatingDailys = new MonthlyCalculatingDailys();
		this.workInfoOfRecordMap = new HashMap<>();
		this.originalData = null;
		this.attendanceTimeWeeks = new ArrayList<>();

		this.startWeekNo = 0;
		this.year = new Year(0);
		this.agreementTimeOfManagePeriod = new AgreementTimeOfManagePeriod(this.employeeId, this.yearMonth);
		this.errorInfos = new ArrayList<>();
	}

	/**
	 * ファクトリー
	 * @param actualWorkingTime 実働時間
	 * @param flexTime フレックス時間
	 * @param statutoryWorkingTime 法定労働時間
	 * @param aggregateTime 集計時間
	 * @param totalWorkingTime 総労働時間
	 * @param totalTimeSpentAtWork 総拘束時間
	 * @param agreementTime 36協定時間
	 * @return 月別実績の月の計算
	 */
	public static MonthlyCalculation of(
			RegularAndIrregularTimeOfMonthly actualWorkingTime,
			FlexTimeOfMonthly flexTime,
			AttendanceTimeMonth statutoryWorkingTime,
			AggregateTotalWorkingTime aggregateTime,
			AttendanceTimeMonth totalWorkingTime,
			AggregateTotalTimeSpentAtWork totalTimeSpentAtWork,
			AgreementTimeOfMonthly agreementTime){
		
		val domain = new MonthlyCalculation();
		domain.actualWorkingTime = actualWorkingTime;
		domain.flexTime = flexTime;
		domain.statutoryWorkingTime = statutoryWorkingTime;
		domain.aggregateTime = aggregateTime;
		domain.totalWorkingTime = totalWorkingTime;
		domain.totalTimeSpentAtWork = totalTimeSpentAtWork;
		domain.agreementTime = agreementTime;
		return domain;
	}
	
	/**
	 * 集計準備
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param procPeriod 期間
	 * @param workingConditionItem 労働条件項目
	 * @param startWeekNo 開始週NO
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param monthlyCalcDailys 月の計算中の日別実績データ
	 * @param monthlyOldDatas 集計前の月別実績データ
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void prepareAggregation(
			String companyId, String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate,
			DatePeriod procPeriod, WorkingConditionItem workingConditionItem,
			int startWeekNo,
			MonAggrCompanySettings companySets,
			MonAggrEmployeeSettings employeeSets,
			MonthlyCalculatingDailys monthlyCalcDailys,
			MonthlyOldDatas monthlyOldDatas,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		this.companyId = companyId;
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
		this.procPeriod = procPeriod;
		this.workingConditionItem = workingConditionItem;
		this.workingSystem = workingConditionItem.getLaborSystem();
		
		this.settingsByReg = new SettingRequiredByReg(companyId);
		this.settingsByDefo = new SettingRequiredByDefo(companyId);
		this.settingsByFlex = new SettingRequiredByFlex();
		this.companySets = companySets;
		this.employeeSets = employeeSets;
		
		this.monthlyCalculatingDailys = monthlyCalcDailys;
		
		// 社員を取得する
		this.employee = employeeSets.getEmployee();
		
		// 退職月か確認する　（変形労働勤務の月単位集計：精算月判定に利用）
		this.isRetireMonth = false;
		if (procPeriod.contains(this.employee.getRetiredDate())) this.isRetireMonth = true;
		
		// 期間終了日時点の雇用コードを取得する
		val employmentOpt = employeeSets.getEmployment(procPeriod.end());
		if (employmentOpt.isPresent()){
			this.employmentCd = employmentOpt.get().getEmploymentCode();
		}
		
		// 期間終了日時点の職場IDを取得する
		val workplaceOpt = employeeSets.getWorkplace(procPeriod.end());
		if (workplaceOpt.isPresent()){
			this.workplaceId = workplaceOpt.get().getWorkplaceId();
		}
		
		// 「締め」　取得
		this.closureOpt = Optional.ofNullable(companySets.getClosureMap().get(closureId.value));
		
		// 通常勤務月別実績集計設定　（基準：期間終了日）
		if (this.workingSystem == WorkingSystem.REGULAR_WORK){
			val regularAggrSetOpt = repositories.getRegularAggrSet().get(
					companyId, this.employmentCd, employeeId, procPeriod.end(), companySets, employeeSets);
			if (!regularAggrSetOpt.isPresent()){
				this.errorInfos.add(new MonthlyAggregationErrorInfo(
						"007", new ErrMessageContent(TextResource.localize("Msg_1234"))));
				return;
			}
			this.settingsByReg.setRegularAggrSet(regularAggrSetOpt.get());
		}

		// 変形労働月別実績集計設定　（基準：期間終了日）
		if (this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			val deforAggrSetOpt = repositories.getDeforAggrSet().get(
					companyId, this.employmentCd, employeeId, procPeriod.end(), companySets, employeeSets);
			if (!deforAggrSetOpt.isPresent()){
				this.errorInfos.add(new MonthlyAggregationErrorInfo(
						"007", new ErrMessageContent(TextResource.localize("Msg_1234"))));
				return;
			}
			this.settingsByDefo.setDeforAggrSet(deforAggrSetOpt.get());
		}

		// フレックス月別実績集計設定　（基準：期間終了日）
		if (this.workingSystem == WorkingSystem.FLEX_TIME_WORK){
			val flexAggrSetOpt = repositories.getFlexAggrSet().get(
					companyId, this.employmentCd, employeeId, procPeriod.end(), companySets, employeeSets);
			if (!flexAggrSetOpt.isPresent()){
				this.errorInfos.add(new MonthlyAggregationErrorInfo(
						"007", new ErrMessageContent(TextResource.localize("Msg_1234"))));
				return;
			}
			this.settingsByFlex.setFlexAggrSet(flexAggrSetOpt.get());
			
			// フレックス勤務の月別集計設定
			this.settingsByFlex.setMonthlyAggrSetOfFlexOpt(Optional.of(companySets.getAggrSetOfFlex()));
			
			// フレックス勤務所定労働時間
			this.settingsByFlex.setGetFlexPredWorkTimeOpt(Optional.of(companySets.getFlexPredWorkTime()));
			
			// フレックス不足の年休補填管理
			this.settingsByFlex.setInsufficientFlexOpt(companySets.getInsufficientFlexOpt());
			
			// フレックス不足の繰越上限管理
			this.settingsByFlex.setFlexShortageLimitOpt(companySets.getFlexShortageLimitOpt());
		}
		
		// 法定内振替順設定
		this.settingsByReg.setLegalTransferOrderSet(companySets.getLegalTransferOrderSet());
		this.settingsByDefo.setLegalTransferOrderSet(companySets.getLegalTransferOrderSet());
		
		// 残業枠の役割
		for (val roleOverTimeFrame : companySets.getRoleOverTimeFrameList()){
			this.settingsByReg.getRoleOverTimeFrameMap().putIfAbsent(
					roleOverTimeFrame.getOvertimeFrNo().v(), roleOverTimeFrame);
			this.settingsByDefo.getRoleOverTimeFrameMap().putIfAbsent(
					roleOverTimeFrame.getOvertimeFrNo().v(), roleOverTimeFrame);
			
			// 自動的に除く残業枠
			if (roleOverTimeFrame.getRoleOTWorkEnum() != RoleOvertimeWorkEnum.MIX_IN_OUT_STATUTORY) continue;
			this.settingsByReg.getAutoExceptOverTimeFrames().add(roleOverTimeFrame);
			this.settingsByDefo.getAutoExceptOverTimeFrames().add(roleOverTimeFrame);
		}
		
		// 休出枠の役割
		for (val roleHolidayWorkFrame : companySets.getRoleHolidayWorkFrameList()){
			this.settingsByReg.getRoleHolidayWorkFrameMap().putIfAbsent(
					roleHolidayWorkFrame.getBreakoutFrNo().v(), roleHolidayWorkFrame);
			this.settingsByDefo.getRoleHolidayWorkFrameMap().putIfAbsent(
					roleHolidayWorkFrame.getBreakoutFrNo().v(), roleHolidayWorkFrame);
			this.settingsByFlex.getRoleHolidayWorkFrameMap().putIfAbsent(
					roleHolidayWorkFrame.getBreakoutFrNo().v(), roleHolidayWorkFrame);
			
			// 自動的に除く休出枠
			if (roleHolidayWorkFrame.getRoleOfOpenPeriodEnum() != RoleOfOpenPeriodEnum.MIX_WITHIN_OUTSIDE_STATUTORY) continue;
			this.settingsByReg.getAutoExceptHolidayWorkFrames().add(roleHolidayWorkFrame);
			this.settingsByDefo.getAutoExceptHolidayWorkFrames().add(roleHolidayWorkFrame);
		}
		
		// 休暇加算時間設定
		this.settingsByReg.getHolidayAdditionMap().putAll(companySets.getHolidayAdditionMap());
		this.settingsByDefo.getHolidayAdditionMap().putAll(companySets.getHolidayAdditionMap());
		this.settingsByFlex.getHolidayAdditionMap().putAll(companySets.getHolidayAdditionMap());
		
		// 法定労働時間を取得する年月（年度＋月）を取得する　（Redmine#106201）
		// 暦上の年月を渡して、年度に沿った年月を取得する
		YearMonth statYearMonth = repositories.getCompany().getYearMonthFromCalenderYM(companyId, yearMonth);
		
		// 週間、月間法定・所定労働時間　取得
		switch (this.workingSystem){
		case REGULAR_WORK:
		case VARIABLE_WORKING_TIME_WORK:
			val monAndWeekStatTimeOpt = repositories.getMonthlyStatutoryWorkingHours().getMonAndWeekStatutoryTime(
					companyId, this.employmentCd, employeeId, procPeriod.end(), statYearMonth, this.workingSystem);
			if (!monAndWeekStatTimeOpt.isPresent()){
				this.errorInfos.add(new MonthlyAggregationErrorInfo(
						"008", new ErrMessageContent(TextResource.localize("Msg_1235"))));
				break;
			}
			val monAndWeekStatTime = monAndWeekStatTimeOpt.get();
			int weekMinutes = monAndWeekStatTime.getWeeklyEstimateTime().v();
			int monthMinutes = monAndWeekStatTime.getMonthlyEstimateTime().v();
			this.statutoryWorkingTime = new AttendanceTimeMonth(monthMinutes);
			this.settingsByReg.setStatutoryWorkingTimeWeek(new AttendanceTimeMonth(weekMinutes));
			this.settingsByReg.setStatutoryWorkingTimeMonth(new AttendanceTimeMonth(monthMinutes));
			this.settingsByDefo.setStatutoryWorkingTimeWeek(new AttendanceTimeMonth(weekMinutes));
			this.settingsByDefo.setStatutoryWorkingTimeMonth(new AttendanceTimeMonth(monthMinutes));
			break;
		case FLEX_TIME_WORK:
			val flexMonAndWeekStatTime = repositories.getMonthlyStatutoryWorkingHours().getFlexMonAndWeekStatutoryTime(
					companyId, this.employmentCd, employeeId, procPeriod.end(), statYearMonth);
			int statMinutes = flexMonAndWeekStatTime.getStatutorySetting().v();
			int predMinutes = flexMonAndWeekStatTime.getSpecifiedSetting().v();
			this.statutoryWorkingTime = new AttendanceTimeMonth(statMinutes);
			this.settingsByFlex.setStatutoryWorkingTimeMonth(new AttendanceTimeMonth(statMinutes));
			this.settingsByFlex.setPrescribedWorkingTimeMonth(new AttendanceTimeMonth(predMinutes));
			
			// 退職日が当月の期間内の時、翌月繰越可能時間 = 0
			if (this.isRetireMonth) break;
			
			// 翌月までの労働条件を確認する
			GeneralDate nextEndDate = procPeriod.end().addDays(31);		// 翌月終了日を含む期間（月末対策のため、31日加算）
			DatePeriod checkPeriod = new DatePeriod(procPeriod.start(), nextEndDate);
			List<WorkingConditionItem> workingConditionItems = repositories.getWorkingConditionItem()
					.getBySidAndPeriodOrderByStrD(employeeId, checkPeriod);
			List<WorkingConditionItem> workConditions = workingConditionItems.stream()
					.filter(x -> x.getLaborSystem().equals(WorkingSystem.FLEX_TIME_WORK)).collect(Collectors.toList());
			
			// フレックス期間がなければ、翌月繰越可能時間 = 0
			if (workConditions.isEmpty()) break;
			
			// 社員のフレックス繰越上限時間（翌月繰越可能時間）を求める
			CalcFlexChangeDto calcFlex = CalcFlexChangeDto.createCalcFlexDto(employeeId, procPeriod.end());
			calcFlex.createWCItem(workConditions);
			ConditionCalcResult conditionResult = repositories.getCheckBeforeCalcFlex().getConditionCalcFlex(
					companyId, calcFlex);
			long canNextCarryforwardSeconds = 0L;
			try {
				canNextCarryforwardSeconds = Time.parse(conditionResult.getValueResult()).totalSeconds();
			}
			catch (Exception ex) {
				canNextCarryforwardSeconds = 0L;
			}
			long canNextCarryforwardMinute = canNextCarryforwardSeconds / Time.STEP;
			this.settingsByFlex.setCanNextCarryforwardTimeMonth(new AttendanceTimeMonth((int)canNextCarryforwardMinute));
			break;
		default:
			this.statutoryWorkingTime = new AttendanceTimeMonth(0);
			break;
		}
		
		// 実績の勤務情報リスト
		for (val workInfoOfDaily : monthlyCalcDailys.getWorkInfoOfDailyMap().values()){
			val ymd = workInfoOfDaily.getYmd();
			this.workInfoOfRecordMap.put(ymd, workInfoOfDaily.getRecordInfo());
		}
		
		// 月別実績の勤怠時間　既存データ
		this.originalData = monthlyOldDatas.getAttendanceTime();
		
		// 週NO　確認
		this.startWeekNo = startWeekNo;

		// 36協定運用設定を取得
		val agreementOperationSetOpt = companySets.getAgreementOperationSet();
		if (!agreementOperationSetOpt.isPresent()) {
			this.errorInfos.add(new MonthlyAggregationErrorInfo(
					"017", new ErrMessageContent(TextResource.localize("Msg_1246"))));
		}
		else {
			val agreementOperationSet = agreementOperationSetOpt.get();
			
			// 年度　設定　（36協定用）
			int calcedYear = this.yearMonth.year();
			int startingMonth = agreementOperationSet.getStartingMonth().value + 1;		// 起算月
			if (this.yearMonth.month() < startingMonth) calcedYear--;
			this.year = new Year(calcedYear);
		}
	}
	
	/**
	 * 集計関連設定のコピー
	 * @param source コピー元：月別実績の月の計算
	 */
	public void copySettings(MonthlyCalculation source){
		this.companyId = source.companyId;
		this.employeeId = source.employeeId;
		this.yearMonth = source.yearMonth;
		this.closureId = source.closureId;
		this.closureDate = source.closureDate;
		this.procPeriod = source.procPeriod;
		this.workingConditionItem = source.workingConditionItem;
		this.workingSystem = source.workingSystem;
		this.employee = source.employee;
		this.workplaceId = source.workplaceId;
		this.employmentCd = source.employmentCd;
		this.isRetireMonth = source.isRetireMonth;
		this.closureOpt = source.closureOpt;
		this.settingsByReg = source.settingsByReg;
		this.settingsByDefo = source.settingsByDefo;
		this.settingsByFlex = source.settingsByFlex;
		this.companySets = source.companySets;
		this.employeeSets = source.employeeSets;
		
		this.monthlyCalculatingDailys = source.monthlyCalculatingDailys;
		this.workInfoOfRecordMap = source.workInfoOfRecordMap;
		this.originalData = source.originalData;
		this.attendanceTimeWeeks = source.attendanceTimeWeeks;

		this.startWeekNo = source.startWeekNo;
		this.year = source.year;
		this.agreementTimeOfManagePeriod = source.agreementTimeOfManagePeriod;
		this.errorInfos = source.errorInfos;
	}
	
	/**
	 * 履歴ごとに月別実績を集計する
	 * @param aggrPeriod 集計期間
	 * @param aggrAtr 集計区分
	 * @param annualLeaveDeductDays 年休控除日数
	 * @param absenceDeductTime 欠勤控除時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregate(DatePeriod aggrPeriod, MonthlyAggregateAtr aggrAtr,
			Optional<AttendanceDaysMonth> annualLeaveDeductDays,
			Optional<AttendanceTimeMonth> absenceDeductTime,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 集計結果　初期化
		this.actualWorkingTime = new RegularAndIrregularTimeOfMonthly();
		this.flexTime = new FlexTimeOfMonthly();
		this.aggregateTime = new AggregateTotalWorkingTime();
		this.totalTimeSpentAtWork = new AggregateTotalTimeSpentAtWork();

		// 既存データの復元
		this.restoreOriginalData(annualLeaveDeductDays, absenceDeductTime);

		// 不正呼び出しの時、集計しない
		if (this.workingConditionItem == null) return;
		
		ConcurrentStopwatches.start("12221:共有項目：");
		
		// 共有項目を集計する
		this.aggregateTime.aggregateSharedItem(aggrPeriod,
				this.monthlyCalculatingDailys.getAttendanceTimeOfDailyMap(),
				this.monthlyCalculatingDailys.getWorkInfoOfDailyMap(),
				this.companySets, repositories);

		ConcurrentStopwatches.stop("12221:共有項目：");
		
		// 通常勤務　or　変形労働　の時
		if (this.workingSystem == WorkingSystem.REGULAR_WORK ||
				this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){

			ConcurrentStopwatches.start("12222:通常変形の月別実績：");
			
			// 通常・変形労働勤務の月別実績を集計する
			val aggrValue = this.actualWorkingTime.aggregateMonthly(
					this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					aggrPeriod, this.workingSystem, this.closureOpt, aggrAtr,
					this.employmentCd, this.settingsByReg, this.settingsByDefo,
					this.aggregateTime, null, this.startWeekNo,
					this.companySets, this.employeeSets, this.monthlyCalculatingDailys, repositories);
			this.aggregateTime = aggrValue.getAggregateTotalWorkingTime();
			this.attendanceTimeWeeks.addAll(aggrValue.getAttendanceTimeWeeks());

			ConcurrentStopwatches.stop("12222:通常変形の月別実績：");
			ConcurrentStopwatches.start("12223:通常変形の月単位：");
			
			// 通常・変形労働勤務の月単位の時間を集計する
			this.actualWorkingTime.aggregateMonthlyHours(this.companyId, this.employeeId,
					this.yearMonth, this.closureId, this.closureDate, aggrPeriod, this.workingSystem,
					aggrAtr, this.isRetireMonth, this.workplaceId, this.employmentCd,
					this.settingsByReg, this.settingsByDefo, this.aggregateTime, repositories);

			ConcurrentStopwatches.stop("12223:通常変形の月単位：");
		}
		// フレックス時間勤務　の時
		else if (this.workingSystem == WorkingSystem.FLEX_TIME_WORK){
			
			// フレックス集計方法を取得する
			val flexAggrMethod = this.settingsByFlex.getFlexAggrSet().getAggrMethod();

			ConcurrentStopwatches.start("12222:フレックスの月別実績：");
			
			// フレックス勤務の月別実績を集計する
			val aggrValue = this.flexTime.aggregateMonthly(this.companyId, this.employeeId,
					this.yearMonth, this.closureId, this.closureDate, aggrPeriod, this.workingSystem,
					aggrAtr, this.closureOpt, flexAggrMethod, this.settingsByFlex,
					this.aggregateTime, null, this.startWeekNo,
					this.companySets, this.employeeSets, this.monthlyCalculatingDailys, repositories);
			this.aggregateTime = aggrValue.getAggregateTotalWorkingTime();
			this.attendanceTimeWeeks.addAll(aggrValue.getAttendanceTimeWeeks());

			ConcurrentStopwatches.stop("12222:フレックスの月別実績：");
			ConcurrentStopwatches.start("12223:フレックスの月単位：");
			
			// フレックス勤務の月単位の時間を集計する
			this.flexTime.aggregateMonthlyHours(this.companyId, this.employeeId,
					this.yearMonth, aggrPeriod, flexAggrMethod, this.workingConditionItem,
					this.workplaceId, this.employmentCd,
					this.settingsByFlex, this.aggregateTime,
					repositories);

			ConcurrentStopwatches.stop("12223:フレックスの月単位：");
		}

		ConcurrentStopwatches.start("12224:実働時間：");
		
		// 実働時間の集計
		this.aggregateTime.aggregateActualWorkingTime(aggrPeriod, this.workingSystem,
				this.actualWorkingTime, this.flexTime);

		ConcurrentStopwatches.stop("12224:実働時間：");
		ConcurrentStopwatches.start("12225:フレックス補填：");
		
		// フレックス時間勤務の時
		if (this.workingSystem == WorkingSystem.FLEX_TIME_WORK){

			// 年休使用時間に加算する
			this.addAnnualLeaveUseTime();
			
			// フレックス勤務の就業時間を求める　（Redmine#106235）
			val workTimeOpt = this.flexTime.askWorkTimeOfFlex(
					 this.companyId, this.employeeId, this.yearMonth, aggrPeriod,
					this.settingsByFlex.getFlexAggrSet().getAggrMethod(),
					settingsByFlex, this.aggregateTime);
			if (workTimeOpt.isPresent()) {
				this.aggregateTime.getWorkTime().setWorkTime(workTimeOpt.get());
			}
			
			// 控除時間が余分に入れられていないか確認する
			this.checkDeductTime();
		}

		ConcurrentStopwatches.stop("12225:フレックス補填：");
		ConcurrentStopwatches.start("12226:総労働時間：");
		
		// 総労働時間を計算
		this.calcTotalWorkingTime();

		ConcurrentStopwatches.stop("12226:総労働時間：");
		ConcurrentStopwatches.start("12227:管理期間の36協定：");
		
		// 管理期間の36協定時間の作成
		this.agreementTimeOfManagePeriod = new AgreementTimeOfManagePeriod(this.employeeId, this.yearMonth);
		this.agreementTimeOfManagePeriod.aggregate(aggrPeriod.end(), aggrAtr, this, repositories);

		ConcurrentStopwatches.stop("12227:管理期間の36協定：");
		
		// 月別実績の36協定へ値を移送
		this.agreementTime = this.agreementTimeOfManagePeriod.getAgreementTime();
	}
	
	/**
	 * 総労働時間と36協定時間の再計算
	 * @param aggrPeriod 集計期間
	 * @param aggrAtr 集計区分
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void recalcTotalAndAgreement(
			DatePeriod aggrPeriod,
			MonthlyAggregateAtr aggrAtr,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 総労働時間を計算
		this.calcTotalWorkingTime();
		
		// 管理期間の36協定時間の作成
		this.agreementTimeOfManagePeriod = new AgreementTimeOfManagePeriod(this.employeeId, this.yearMonth);
		this.agreementTimeOfManagePeriod.aggregate(aggrPeriod.end(), aggrAtr, this, repositories);
		
		// 月別実績の36協定へ値を移送
		this.agreementTime = this.agreementTimeOfManagePeriod.getAgreementTime();
	}
	
	/**
	 * 既存データの復元
	 * @param annualDeductDays 年休控除日数
	 * @param absenceDeductTime 欠勤控除時間
	 */
	private void restoreOriginalData(
			Optional<AttendanceDaysMonth> annualDeductDays,
			Optional<AttendanceTimeMonth> absenceDeductTime){
		
		// 年休控除日数・欠勤控除時間
		AttendanceDaysMonth applyAnnualDeductDays = new AttendanceDaysMonth(0.0);
		AttendanceTimeMonth applyAbsenceDeductTime = new AttendanceTimeMonth(0);
		if (annualDeductDays.isPresent() || absenceDeductTime.isPresent()){
			if (annualDeductDays.isPresent()) applyAnnualDeductDays = annualDeductDays.get();
			if (absenceDeductTime.isPresent()) applyAbsenceDeductTime = absenceDeductTime.get();
		}
		else if (this.originalData.isPresent()){
			val monthlyCalculation = this.originalData.get().getMonthlyCalculation();
			val flexShortDeductTime = monthlyCalculation.getFlexTime().getFlexShortDeductTime();
			applyAnnualDeductDays = flexShortDeductTime.getAnnualLeaveDeductDays();
			applyAbsenceDeductTime = flexShortDeductTime.getAbsenceDeductTime();
		}
		this.flexTime.getFlexShortDeductTime().setAnnualLeaveDeductDays(applyAnnualDeductDays);
		this.flexTime.getFlexShortDeductTime().setAbsenceDeductTime(applyAbsenceDeductTime);
	}
	
	/**
	 * 年休使用時間に加算する
	 */
	private void addAnnualLeaveUseTime(){
		
		// 控除後の結果にエラーがある時、加算しない
		val afterDeduct = this.flexTime.getDeductDaysAndTime();
		if (afterDeduct.getErrorInfos().size() > 0) return;
		if (!afterDeduct.getPredetermineTimeSetOfWeekDay().isPresent()) return;

		// 控除前の年休控除時間を取得する
		val beforeDeductTime = this.flexTime.getAnnualLeaveTimeBeforeDeduct();
		
		// 控除前の年休控除時間を年休使用時間に加算する
		val annualLeave = this.aggregateTime.getVacationUseTime().getAnnualLeave();
		annualLeave.addMinuteToUseTime(beforeDeductTime.v());
	}
	
	/**
	 * 控除時間が余分に入れられていないか確認する
	 */
	public void checkDeductTime(){
		
		// 控除後の結果にエラーがある時、確認しない
		val afterDeduct = this.flexTime.getDeductDaysAndTime();
		if (afterDeduct.getErrorInfos().size() > 0) return;
		if (!afterDeduct.getPredetermineTimeSetOfWeekDay().isPresent()) return;
		
		// 控除時間が余分に入力されていないか確認する
		val predetermineTimeSet = afterDeduct.getPredetermineTimeSetOfWeekDay().get();
		val predAddTimeAM = predetermineTimeSet.getPredTime().getAddTime().getMorning();
		boolean isExtraTime = false;
		if (afterDeduct.getAnnualLeaveDeductTime().greaterThanOrEqualTo(predAddTimeAM.v())){
			isExtraTime = true;
		}
		else if (afterDeduct.getAbsenceDeductTime().greaterThan(0)){
			isExtraTime = true;
		}
		if (isExtraTime){
			
			// 「余分な控除時間のエラーフラグ」をtrueにする
			this.flexTime.getFlexShortDeductTime().setErrorAtrOfExtraDeductTime(true);
			
			// 社員の月別実績のエラーを作成する
			val perError = this.flexTime.getPerErrors();
			if (!perError.contains(Flex.FLEX_SHORTAGE_TIME_EXCESS_DEDUCTION)){
				perError.add(Flex.FLEX_SHORTAGE_TIME_EXCESS_DEDUCTION);
			}
		}
	}
	
	/**
	 * 総労働時間の計算
	 * @param datePeriod 期間
	 */
	public void calcTotalWorkingTime(){

		this.totalWorkingTime = new AttendanceTimeMonth(this.aggregateTime.getTotalWorkingTargetTime().v() +
				this.actualWorkingTime.getTotalWorkingTargetTime().v() +
				this.flexTime.getTotalWorkingTargetTime().v());
	}
	
	/**
	 * 36協定時間の集計
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param procPeriod 期間
	 * @param isRetireMonth 退職月度かどうか
	 * @param annualLeaveDeductDays 年休控除日数
	 * @param absenceDeductTime 欠勤控除時間
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param monthlyCalcDailys 月の計算中の日別実績データ
	 * @param monthlyOldDatas 集計前の月別実績データ
	 * @param basicCalced 月の計算結果（基本計算）
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public Optional<AgreementTimeOfManagePeriod> aggregateAgreementTime(
			String companyId, String employeeId,YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate,
			DatePeriod procPeriod,
			Optional<AttendanceDaysMonth> annualLeaveDeductDays,
			Optional<AttendanceTimeMonth> absenceDeductTime,
			MonAggrCompanySettings companySets,
			MonAggrEmployeeSettings employeeSets,
			MonthlyCalculatingDailys monthlyCalcDailys,
			MonthlyOldDatas monthlyOldDatas,
			Optional<MonthlyCalculation> basicCalced,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 36協定運用設定を取得
		val agreementOperationSetOpt = companySets.getAgreementOperationSet();
		if (!agreementOperationSetOpt.isPresent()) {
			this.errorInfos.add(new MonthlyAggregationErrorInfo(
					"017", new ErrMessageContent(TextResource.localize("Msg_1246"))));
			return Optional.of(AgreementTimeOfManagePeriod.of(employeeId, yearMonth, this.errorInfos));
		}
		val agreementOperationSet = agreementOperationSetOpt.get();
		
		// 集計期間を取得
		val aggrPeriod = agreementOperationSet.getAggregatePeriod(procPeriod);
		
		// 「労働条件項目」を取得
		List<WorkingConditionItem> workingConditionItems = repositories.getWorkingConditionItem()
				.getBySidAndPeriodOrderByStrD(employeeId, aggrPeriod.getPeriod());
		if (workingConditionItems.isEmpty()){
			this.errorInfos.add(new MonthlyAggregationErrorInfo(
					"006", new ErrMessageContent(TextResource.localize("Msg_430"))));
			return Optional.of(AgreementTimeOfManagePeriod.of(employeeId, yearMonth, this.errorInfos));
		}
		
		// 同じ労働制の履歴を統合
		this.IntegrateHistoryOfSameWorkSys(workingConditionItems, repositories);

		// 項目の数だけループ
		MonthlyCalculation agreementCalc = null;
		int weekNo = 1;
		for (val workingConditionItem : this.workingConditionItems){

			// 「労働条件」の該当履歴から期間を取得
			val historyId = workingConditionItem.getHistoryId();
			if (!this.workingConditions.containsKey(historyId)) continue;

			// 処理期間を計算　（36協定の集計期間と労働条件履歴期間の重複を確認する）
			val term = this.workingConditions.get(historyId);
			DatePeriod period = MonthlyCalculation.confirmProcPeriod(aggrPeriod.getPeriod(), term);
			if (period == null) {
				// 履歴の期間と重複がない時
				continue;
			}

			// 基本計算結果と計算期間が異なる場合に、集計する
			boolean isSameBasic = false;
			if (basicCalced.isPresent()){
				if (period.start().compareTo(basicCalced.get().getProcPeriod().start()) == 0 &&
					period.end().compareTo(basicCalced.get().getProcPeriod().end()) == 0){
					isSameBasic = true;
				}
			}
			MonthlyCalculation calcWork = new MonthlyCalculation();
			if (isSameBasic){
				calcWork = basicCalced.get();
			}
			else {
				
				// 集計準備
				calcWork.prepareAggregation(
						companyId, employeeId, aggrPeriod.getYearMonth(), closureId, closureDate,
						period, workingConditionItem, weekNo,
						companySets, employeeSets, monthlyCalcDailys, monthlyOldDatas, repositories);
				if (calcWork.errorInfos.size() > 0){
					return Optional.of(AgreementTimeOfManagePeriod.of(employeeId, yearMonth, calcWork.errorInfos));
				}
				calcWork.year = aggrPeriod.getYear();
				
				// 集計中の労働制を確認する
				if (calcWork.workingSystem == WorkingSystem.FLEX_TIME_WORK){
					
					// 年休控除日数と欠勤控除時間があるか確認する
					if (annualLeaveDeductDays.isPresent() || absenceDeductTime.isPresent()){
						if (!annualLeaveDeductDays.isPresent()){
							annualLeaveDeductDays = Optional.of(new AttendanceDaysMonth(0.0));
						}
						if (!absenceDeductTime.isPresent()){
							absenceDeductTime = Optional.of(new AttendanceTimeMonth(0));
						}
					}
				}
				
				// 履歴ごとに月別実績を集計する
				calcWork.aggregate(period, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
						annualLeaveDeductDays, absenceDeductTime, repositories);
			}
			
			// データを合算する
			if (agreementCalc == null){
				agreementCalc = calcWork;
			}
			else {
				calcWork.sum(agreementCalc);
				agreementCalc = calcWork;
			}
		}

		// 管理時間の36協定時間を返す
		if (agreementCalc == null) return Optional.empty();
		return Optional.of(agreementCalc.agreementTimeOfManagePeriod);
	}
	
	/**
	 * 同じ労働制の履歴を統合
	 * @param target 労働条件項目リスト　（統合前）
	 * @param attendanceTimeOfDailysOpt 日別実績の勤怠時間リスト
	 * @return 労働条件項目リスト　（統合後）
	 */
	private void IntegrateHistoryOfSameWorkSys(
			List<WorkingConditionItem> target,
			RepositoriesRequiredByMonthlyAggr repositories){

		this.workingConditionItems = new ArrayList<>();
		this.workingConditions = new HashMap<>();
		
		val itrTarget = target.listIterator();
		while (itrTarget.hasNext()){
			
			// 要素[n]を取得
			WorkingConditionItem startItem = itrTarget.next();
			val startHistoryId = startItem.getHistoryId();
			val startConditionOpt = repositories.getWorkingCondition().getByHistoryId(startHistoryId);
			if (!startConditionOpt.isPresent()) continue;
			val startCondition = startConditionOpt.get();
			if (startCondition.getDateHistoryItem().isEmpty()) continue;
			DatePeriod startPeriod = startCondition.getDateHistoryItem().get(0).span();
			
			// 要素[n]と要素[n+1]以降を順次比較
			WorkingConditionItem endItem = null;
			while (itrTarget.hasNext()){
				WorkingConditionItem nextItem = target.get(itrTarget.nextIndex());
				if (startItem.getLaborSystem() != nextItem.getLaborSystem() ||
					startItem.getHourlyPaymentAtr() != nextItem.getHourlyPaymentAtr()){
					
					// 労働制または時給者区分が異なる履歴が見つかった時点で、労働条件の統合をやめる
					break;
				}
			
				// 労働制と時給者区分が同じ履歴の要素を順次取得
				endItem = itrTarget.next();
			}
			
			// 次の要素がなくなった、または、異なる履歴が見つかれば、集計要素を確定する
			if (endItem == null){
				this.workingConditionItems.add(startItem);
				this.workingConditions.putIfAbsent(startHistoryId, startPeriod);
				continue;
			}
			val endHistoryId = endItem.getHistoryId();
			val endConditionOpt = repositories.getWorkingCondition().getByHistoryId(endHistoryId);
			if (!endConditionOpt.isPresent()) continue;;
			val endCondition = endConditionOpt.get();
			if (endCondition.getDateHistoryItem().isEmpty()) continue;
			this.workingConditionItems.add(endItem);
			this.workingConditions.putIfAbsent(endHistoryId,
					new DatePeriod(startPeriod.start(), endCondition.getDateHistoryItem().get(0).end()));
		}
	}
	
	/**
	 * 処理期間との重複を確認する　（重複期間を取り出す）
	 * @param target 処理期間
	 * @param comparison 比較対象期間
	 * @return 重複期間　（null = 重複なし）
	 */
	public static DatePeriod confirmProcPeriod(DatePeriod target, DatePeriod comparison){

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
	
	/**
	 * 勤怠項目IDに対応する時間を取得する　（丸め処理付き）
	 * @param attendanceItemId 勤怠項目ID
	 * @param roundingSet 月別実績の丸め設定
	 * @param isExcessOutside 時間外超過設定で丸めるかどうか
	 * @return 勤怠月間時間
	 */
	public AttendanceTimeMonth getTimeOfAttendanceItemId(
			int attendanceItemId,
			RoundingSetOfMonthly roundingSet,
			boolean isExcessOutside){

		AttendanceTimeMonth notExistTime = new AttendanceTimeMonth(0);

		val overTimeMap = this.aggregateTime.getOverTime().getAggregateOverTimeMap();
		val hdwkTimeMap = this.aggregateTime.getHolidayWorkTime().getAggregateHolidayWorkTimeMap();
		
		// 就業時間
		if (attendanceItemId == AttendanceItemOfMonthly.WORK_TIME.value){
			val workTime = this.aggregateTime.getWorkTime().getWorkTime();
			if (isExcessOutside) return roundingSet.excessOutsideRound(attendanceItemId, workTime);
			return roundingSet.itemRound(attendanceItemId, workTime);
		}
		
		// 残業時間
		if (attendanceItemId >= AttendanceItemOfMonthly.OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.OVER_TIME_10.value){
			val overTimeFrameNo = new OverTimeFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.OVER_TIME_01.value + 1);
			if (!overTimeMap.containsKey(overTimeFrameNo)) return notExistTime;
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId,
						overTimeMap.get(overTimeFrameNo).getOverTime().getTime());
			}
			return roundingSet.itemRound(attendanceItemId,
					overTimeMap.get(overTimeFrameNo).getOverTime().getTime());
		}
		
		// 計算残業時間
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_OVER_TIME_10.value){
			val overTimeFrameNo = new OverTimeFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.CALC_OVER_TIME_01.value + 1);
			if (!overTimeMap.containsKey(overTimeFrameNo)) return notExistTime;
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId,
						overTimeMap.get(overTimeFrameNo).getOverTime().getCalcTime());
			}
			return roundingSet.itemRound(attendanceItemId,
					overTimeMap.get(overTimeFrameNo).getOverTime().getCalcTime());
		}
		
		// 振替残業時間
		if (attendanceItemId >= AttendanceItemOfMonthly.TRANSFER_OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.TRANSFER_OVER_TIME_10.value){
			val overTimeFrameNo = new OverTimeFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.TRANSFER_OVER_TIME_01.value + 1);
			if (!overTimeMap.containsKey(overTimeFrameNo)) return notExistTime;
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId,
						overTimeMap.get(overTimeFrameNo).getTransferOverTime().getTime());
			}
			return roundingSet.itemRound(attendanceItemId,
					overTimeMap.get(overTimeFrameNo).getTransferOverTime().getTime());
		}
		
		// 計算振替残業時間
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_TRANSFER_OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_TRANSFER_OVER_TIME_10.value){
			val overTimeFrameNo = new OverTimeFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.CALC_TRANSFER_OVER_TIME_01.value + 1);
			if (!overTimeMap.containsKey(overTimeFrameNo)) return notExistTime;
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId,
						overTimeMap.get(overTimeFrameNo).getTransferOverTime().getCalcTime());
			}
			return roundingSet.itemRound(attendanceItemId,
					overTimeMap.get(overTimeFrameNo).getTransferOverTime().getCalcTime());
		}
		
		// 休出時間
		if (attendanceItemId >= AttendanceItemOfMonthly.HOLIDAY_WORK_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.HOLIDAY_WORK_TIME_10.value){
			val holidayWorkTimeFrameNo = new HolidayWorkFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.HOLIDAY_WORK_TIME_01.value + 1);
			if (!hdwkTimeMap.containsKey(holidayWorkTimeFrameNo)) return notExistTime;
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId,
						hdwkTimeMap.get(holidayWorkTimeFrameNo).getHolidayWorkTime().getTime());
			}
			return roundingSet.itemRound(attendanceItemId,
					hdwkTimeMap.get(holidayWorkTimeFrameNo).getHolidayWorkTime().getTime());
		}
		
		// 計算休出時間
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_HOLIDAY_WORK_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_HOLIDAY_WORK_TIME_10.value){
			val holidayWorkTimeFrameNo = new HolidayWorkFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.CALC_HOLIDAY_WORK_TIME_01.value + 1);
			if (!hdwkTimeMap.containsKey(holidayWorkTimeFrameNo)) return notExistTime;
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId,
						hdwkTimeMap.get(holidayWorkTimeFrameNo).getHolidayWorkTime().getCalcTime());
			}
			return roundingSet.itemRound(attendanceItemId,
					hdwkTimeMap.get(holidayWorkTimeFrameNo).getHolidayWorkTime().getCalcTime());
		}
		
		// 振替時間
		if (attendanceItemId >= AttendanceItemOfMonthly.TRANSFER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.TRANSFER_TIME_10.value){
			val holidayWorkTimeFrameNo = new HolidayWorkFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.TRANSFER_TIME_01.value + 1);
			if (!hdwkTimeMap.containsKey(holidayWorkTimeFrameNo)) return notExistTime;
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId,
						hdwkTimeMap.get(holidayWorkTimeFrameNo).getTransferTime().getTime());
			}
			return roundingSet.itemRound(attendanceItemId,
					hdwkTimeMap.get(holidayWorkTimeFrameNo).getTransferTime().getTime());
		}
		
		// 計算振替時間
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_TRANSFER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_TRANSFER_TIME_10.value){
			val holidayWorkTimeFrameNo = new HolidayWorkFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.CALC_TRANSFER_TIME_01.value + 1);
			if (!hdwkTimeMap.containsKey(holidayWorkTimeFrameNo)) return notExistTime;
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId,
						hdwkTimeMap.get(holidayWorkTimeFrameNo).getTransferTime().getCalcTime());
			}
			return roundingSet.itemRound(attendanceItemId,
					hdwkTimeMap.get(holidayWorkTimeFrameNo).getTransferTime().getCalcTime());
		}
		
		// フレックス法定内時間
		if (attendanceItemId == AttendanceItemOfMonthly.FLEX_LEGAL_TIME.value){
			val flexLegalMinutes = this.flexTime.getFlexTime().getLegalFlexTime().v();
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId, new AttendanceTimeMonth(flexLegalMinutes));
			}
			return roundingSet.itemRound(attendanceItemId, new AttendanceTimeMonth(flexLegalMinutes));
		}
		
		// フレックス法定外時間
		if (attendanceItemId == AttendanceItemOfMonthly.FLEX_ILLEGAL_TIME.value){
			val flexIllegalMinutes = this.flexTime.getFlexTime().getIllegalFlexTime().v();
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId, new AttendanceTimeMonth(flexIllegalMinutes));
			}
			return roundingSet.itemRound(attendanceItemId, new AttendanceTimeMonth(flexIllegalMinutes));
		}
		
		// フレックス超過時間　（フレックス時間のプラス分）
		if (attendanceItemId == AttendanceItemOfMonthly.FLEX_EXCESS_TIME.value){
			val flexExcessMinutes = this.flexTime.getFlexTime().getFlexTime().getTime().v();
			if (flexExcessMinutes <= 0) return notExistTime;
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId, new AttendanceTimeMonth(flexExcessMinutes));
			}
			return roundingSet.itemRound(attendanceItemId, new AttendanceTimeMonth(flexExcessMinutes));
		}
		
		// 所定内割増時間
		if (attendanceItemId == AttendanceItemOfMonthly.WITHIN_PRESCRIBED_PREMIUM_TIME.value){
			val withinPrescribedPremiumTime = this.aggregateTime.getWorkTime().getWithinPrescribedPremiumTime();
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId, withinPrescribedPremiumTime);
			}
			return roundingSet.itemRound(attendanceItemId, withinPrescribedPremiumTime);
		}
		
		// 週割増合計時間
		if (attendanceItemId == AttendanceItemOfMonthly.WEEKLY_TOTAL_PREMIUM_TIME.value){
			val weeklyTotalPremiumTime = this.actualWorkingTime.getWeeklyTotalPremiumTime();
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId, weeklyTotalPremiumTime);
			}
			return roundingSet.itemRound(attendanceItemId, weeklyTotalPremiumTime);
		}
		
		// 月割増合計時間
		if (attendanceItemId == AttendanceItemOfMonthly.MONTHLY_TOTAL_PREMIUM_TIME.value){
			val monthlyTotalPremiumTime = this.actualWorkingTime.getMonthlyTotalPremiumTime();
			if (isExcessOutside){
				return roundingSet.excessOutsideRound(attendanceItemId, monthlyTotalPremiumTime);
			}
			return roundingSet.itemRound(attendanceItemId, monthlyTotalPremiumTime);
		}
		
		return notExistTime;
	}
	
	/**
	 * 週の集計をする日か確認する
	 * @param procYmd 処理日
	 * @param weekStart 週開始
	 * @param datePeriod 期間（月別集計用）
	 * @param closureOpt 締め
	 * @return true：集計する、false：集計しない
	 */
	public static boolean isAggregateWeek(GeneralDate procYmd, WeekStart weekStart, DatePeriod datePeriod,
			Optional<Closure> closureOpt){
		
		// 週開始から集計する曜日を求める　（週開始の曜日の前日の曜日が「集計する曜日」）
		int aggregateWeek = 0;
		switch (weekStart){
		case Monday:
			aggregateWeek = 7;
			break;
		case Tuesday:
			aggregateWeek = 1;
			break;
		case Wednesday:
			aggregateWeek = 2;
			break;
		case Thursday:
			aggregateWeek = 3;
			break;
		case Friday:
			aggregateWeek = 4;
			break;
		case Saturday:
			aggregateWeek = 5;
			break;
		case Sunday:
			aggregateWeek = 6;
			break;
		case TighteningStartDate:
			
			// 締め開始日を取得する
			GeneralDate closureDate = datePeriod.start();
			if (closureOpt.isPresent()){
				val closure = closureOpt.get();
				val closurePeriodOpt = closure.getClosurePeriodByYmd(datePeriod.start());
				if (closurePeriodOpt.isPresent()){
					closureDate = closurePeriodOpt.get().getPeriod().start();
				}
			}
			
			// 締め開始日の曜日から集計する曜日を求める
			aggregateWeek = closureDate.dayOfWeek() - 1;
			if (aggregateWeek == 0) aggregateWeek = 7;
			break;
		}
		
		// 集計する曜日を処理日の曜日と比較する
		val procWeek = procYmd.dayOfWeek();
		if (procWeek != aggregateWeek){
			if (!procYmd.equals(datePeriod.end())) return false;
		}
		return true;
	}
	
	/**
	 * 週開始と同じ曜日かどうか
	 * @param procYmd 処理日
	 * @param weekStart 週開始
	 * @return true：同じ、false：同じでない
	 */
	public static boolean isWeekStart(GeneralDate procYmd, WeekStart weekStart){
		
		val procWeek = procYmd.dayOfWeek();
		switch (weekStart){
		case Monday:
			if (procWeek == 1) return true;
			break;
		case Tuesday:
			if (procWeek == 2) return true;
			break;
		case Wednesday:
			if (procWeek == 3) return true;
			break;
		case Thursday:
			if (procWeek == 4) return true;
			break;
		case Friday:
			if (procWeek == 5) return true;
			break;
		case Saturday:
			if (procWeek == 6) return true;
			break;
		case Sunday:
			if (procWeek == 7) return true;
			break;
		default:
			break;
		}
		return false;
	}
	
	/**
	 * エラー情報の取得
	 * @return エラー情報リスト
	 */
	public List<MonthlyAggregationErrorInfo> getErrorInfos(){
		
		List<MonthlyAggregationErrorInfo> results = new ArrayList<>();
		results.addAll(this.errorInfos);
		results.addAll(this.actualWorkingTime.getErrorInfos());
		results.addAll(this.actualWorkingTime.getIrregularPeriodCarryforwardsTime().getErrorInfos());
		results.addAll(this.flexTime.getErrorInfos());
		return results;
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(MonthlyCalculation target){
		
		GeneralDate startDate = this.procPeriod.start();
		GeneralDate endDate = this.procPeriod.end();
		if (startDate.after(target.procPeriod.start())) startDate = target.procPeriod.start();
		if (endDate.before(target.procPeriod.end())) endDate = target.procPeriod.end();
		this.procPeriod = new DatePeriod(startDate, endDate);
		
		this.actualWorkingTime.sum(target.actualWorkingTime);
		this.flexTime.sum(target.flexTime);
		this.aggregateTime.sum(target.aggregateTime);
		this.totalWorkingTime = this.totalWorkingTime.addMinutes(target.totalWorkingTime.v());
		this.totalTimeSpentAtWork.sum(target.totalTimeSpentAtWork);
		// this.agreementTime.sum() は、下の this.agreementTimeOfManagePeriod.sum() に含まれる。参照関係に注意。
		
		this.agreementTimeOfManagePeriod.sum(target.agreementTimeOfManagePeriod);
	}
}
