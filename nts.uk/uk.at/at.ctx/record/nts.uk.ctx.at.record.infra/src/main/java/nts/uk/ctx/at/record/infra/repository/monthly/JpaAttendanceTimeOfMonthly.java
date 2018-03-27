package nts.uk.ctx.at.record.infra.repository.monthly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimesMonth;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculation;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculationAndMinus;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.AggregateTotalTimeSpentAtWork;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.monthly.calc.actualworkingtime.IrregularWorkingTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.actualworkingtime.RegularAndIrregularTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.ExcessFlexAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexCarryforwardTime;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexShortDeductTime;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTime;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTimeOfExcessOutsideTime;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.PrescribedWorkingTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.WorkTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.hdwkandcompleave.AggregateHolidayWorkTime;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.hdwkandcompleave.HolidayWorkTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.overtime.AggregateOverTime;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.overtime.OverTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.AnnualLeaveUseTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.CompensatoryLeaveUseTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.RetentionYearlyUseTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.SpecialHolidayUseTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.VacationUseTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.excessoutside.ExcessOutsideWork;
import nts.uk.ctx.at.record.dom.monthly.excessoutside.ExcessOutsideWorkOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.VerticalTotalOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.WorkDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.leave.AggregateLeaveDays;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.leave.AnyLeave;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.leave.LeaveOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.paydays.PayDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.specificdays.AggregateSpecificDays;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.specificdays.SpecificDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AbsenceDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AggregateAbsenceDays;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AttendanceDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.HolidayDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.HolidayWorkDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.PredeterminedDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.TemporaryWorkTimesOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.TwoTimesWorkTimesOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.WorkDaysDetailOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.WorkTimesOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.attdleavegatetime.AttendanceLeaveGateTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.bonuspaytime.AggregateBonusPayTime;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.bonuspaytime.BonusPayTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.breaktime.BreakTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.divergencetime.AggregateDivergenceTime;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.divergencetime.DivergenceAtrOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.divergencetime.DivergenceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.goout.AggregateGoOut;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.goout.GoOutForChildCare;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.goout.GoOutOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.holidaytime.HolidayTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.lateleaveearly.Late;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.lateleaveearly.LateLeaveEarlyOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.lateleaveearly.LeaveEarly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.medicaltime.MedicalTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.midnighttime.IllegalMidnightTime;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.midnighttime.MidnightTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.premiumtime.AggregatePremiumTime;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.premiumtime.PremiumTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.timevarience.BudgetTimeVarienceOfMonthly;
import nts.uk.ctx.at.record.dom.raisesalarytime.primitivevalue.SpecificDateItemNo;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitOneMonth;
import nts.uk.ctx.at.record.infra.entity.monthly.KrcdtMonAttendanceTime;
import nts.uk.ctx.at.record.infra.entity.monthly.KrcdtMonAttendanceTimePK;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.KrcdtMonAggrTotalSpt;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.KrcdtMonAgreementTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.actualworkingtime.KrcdtMonRegIrregTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.flex.KrcdtMonFlexTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.KrcdtMonAggrTotalWrk;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.hdwkandcompleave.KrcdtMonAggrHdwkTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.hdwkandcompleave.KrcdtMonAggrHdwkTimePK;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.hdwkandcompleave.KrcdtMonHdwkTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.overtime.KrcdtMonAggrOverTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.overtime.KrcdtMonAggrOverTimePK;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.overtime.KrcdtMonOverTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.vacationusetime.KrcdtMonVactUseTime;
import nts.uk.ctx.at.record.infra.entity.monthly.excessoutside.KrcdtMonExcessOutside;
import nts.uk.ctx.at.record.infra.entity.monthly.excessoutside.KrcdtMonExcoutTime;
import nts.uk.ctx.at.record.infra.entity.monthly.excessoutside.KrcdtMonExcoutTimePK;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.KrcdtMonVerticalTotal;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.workdays.KrcdtMonAggrAbsnDays;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.workdays.KrcdtMonAggrAbsnDaysPK;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.workdays.KrcdtMonAggrSpecDays;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.workdays.KrcdtMonAggrSpecDaysPK;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.workdays.KrcdtMonLeave;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrBnspyTime;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrBnspyTimePK;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrDivgTime;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrDivgTimePK;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrGoout;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrGooutPK;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrPremTime;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrPremTimePK;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonMedicalTime;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonMedicalTimePK;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.shortworktime.ChildCareAtr;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.predset.WorkTimeNightShift;
import nts.uk.ctx.at.shared.dom.worktype.CloseAtr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * リポジトリ実装：月別実績の勤怠時間
 * @author shuichi_ishida
 */
@Stateless
public class JpaAttendanceTimeOfMonthly extends JpaRepository implements AttendanceTimeOfMonthlyRepository {

	private static final String FIND_BY_YEAR_MONTH = "SELECT a FROM KrcdtMonAttendanceTime a "
			+ "WHERE a.PK.employeeId = :employeeId "
			+ "AND a.PK.yearMonth = :yearMonth "
			+ "ORDER BY a.startYmd ";

	private static final String FIND_BY_YM_AND_CLOSURE_ID = "SELECT a FROM KrcdtMonAttendanceTime a "
			+ "WHERE a.PK.employeeId = :employeeId "
			+ "AND a.PK.yearMonth = :yearMonth "
			+ "AND a.PK.closureId = :closureId "
			+ "ORDER BY a.startYmd ";

	private static final String DELETE_BY_YEAR_MONTH = "DELETE FROM KrcdtMonAttendanceTime a "
			+ "WHERE a.PK.employeeId = :employeeId "
			+ "AND a.PK.yearMonth = :yearMonth ";

	/** 検索 */
	@Override
	public Optional<AttendanceTimeOfMonthly> find(String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate) {
		
		return this.queryProxy()
				.find(new KrcdtMonAttendanceTimePK(
						employeeId,
						yearMonth.v(),
						closureId.value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0)),
						KrcdtMonAttendanceTime.class)
				.map(c -> toDomain(c));
	}

	/** 検索　（年月） */
	@Override
	public List<AttendanceTimeOfMonthly> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		
		return this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonAttendanceTime.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList(c -> toDomain(c));
	}

	/** 検索　（年月と締めID） */
	@Override
	public List<AttendanceTimeOfMonthly> findByYMAndClosureIdOrderByStartYmd(String employeeId, YearMonth yearMonth,
			ClosureId closureId) {
		
		return this.queryProxy().query(FIND_BY_YM_AND_CLOSURE_ID, KrcdtMonAttendanceTime.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.setParameter("closureId", closureId.value)
				.getList(c -> toDomain(c));
	}
	
	/**
	 * エンティティ→ドメイン
	 * @param entity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：月別実績の勤怠時間
	 */
	private static AttendanceTimeOfMonthly toDomain(KrcdtMonAttendanceTime entity){

		// 月別実績の月の計算
		val monthlyCalculation = MonthlyCalculation.of(
				toDomainRegularAndIrregularTimeOfMonthly(entity),
				toDomainFlexTimeOfMonthly(entity),
				new AttendanceTimeMonth(entity.statutoryWorkingTime),
				toDomainAggregateTotalWorkingTime(entity),
				toDomainAggregateTotalTimeSpentAtWork(entity),
				toDomainAgreementTimeOfMonthly(entity));
		
		// 月別実績の勤怠時間
		val domain = AttendanceTimeOfMonthly.of(
				entity.PK.employeeId,
				new YearMonth(entity.PK.yearMonth),
				ClosureId.valueOf(entity.PK.closureId),
				new ClosureDate(entity.PK.closureDay, (entity.PK.isLastDay != 0)),
				new DatePeriod(entity.startYmd, entity.endYmd),
				monthlyCalculation,
				toDomainExcessOutsideWorkOfMonthly(entity),
				toDomainVerticalTotalOfMonthly(entity),
				new AttendanceDaysMonth(entity.aggregateDays));
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（月別実績の通常変形時間）
	 * @param parentEntity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：月別実績の通常変形時間
	 */
	private static RegularAndIrregularTimeOfMonthly toDomainRegularAndIrregularTimeOfMonthly(KrcdtMonAttendanceTime parentEntity){

		val entity = parentEntity.krcdtMonRegIrregTime;
		if (entity == null) return new RegularAndIrregularTimeOfMonthly();

		// 月別実績の変形労働時間
		val irregularWorkingTime = IrregularWorkingTimeOfMonthly.of(
				new AttendanceTimeMonthWithMinus(entity.multiMonthIrregularMiddleTime),
				new AttendanceTimeMonthWithMinus(entity.irregularPeriodCarryforwardTime),
				new AttendanceTimeMonth(entity.irregularWorkingShortageTime),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.irregularLegalOverTime),
						new AttendanceTimeMonth(entity.calcIrregularLegalOverTime))
				);
		
		// 月別実績の通常変形時間
		val domain = RegularAndIrregularTimeOfMonthly.of(
				new AttendanceTimeMonth(entity.monthlyTotalPremiumTime),
				new AttendanceTimeMonth(entity.weeklyTotalPremiumTime),
				irregularWorkingTime);
		return domain;
	}

	/**
	 * エンティティ→ドメイン　（月別実績のフレックス時間）
	 * @param parentEntity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：月別実績のフレックス時間
	 */
	private static FlexTimeOfMonthly toDomainFlexTimeOfMonthly(KrcdtMonAttendanceTime parentEntity){
		
		val entity = parentEntity.krcdtMonFlexTime;
		if (entity == null) return new FlexTimeOfMonthly();
		
		val domain = FlexTimeOfMonthly.of(
				FlexTime.of(
						new TimeMonthWithCalculationAndMinus(
								new AttendanceTimeMonthWithMinus(entity.flexTime),
								new AttendanceTimeMonthWithMinus(entity.calcFlexTime)),
						new AttendanceTimeMonth(entity.beforeFlexTime),
						new AttendanceTimeMonthWithMinus(entity.legalFlexTime),
						new AttendanceTimeMonthWithMinus(entity.illegalFlexTime)),
				new AttendanceTimeMonth(entity.flexExcessTime),
				new AttendanceTimeMonth(entity.flexShortageTime),
				FlexCarryforwardTime.of(
						new AttendanceTimeMonth(entity.flexCarryforwardWorkTime),
						new AttendanceTimeMonth(entity.flexCarryforwardTime),
						new AttendanceTimeMonth(entity.flexCarryforwardShortageTime)),
				FlexTimeOfExcessOutsideTime.of(
						EnumAdaptor.valueOf(entity.excessFlexAtr, ExcessFlexAtr.class),
						new AttendanceTimeMonth(entity.principleTime),
						new AttendanceTimeMonth(entity.forConvenienceTime)),
				FlexShortDeductTime.of(
						new AttendanceDaysMonth(entity.annualLeaveDeductDays),
						new AttendanceTimeMonth(entity.absenceDeductTime),
						new AttendanceTimeMonth(entity.shotTimeBeforeDeduct))
			);
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（集計総労働時間）
	 * @param parentEntity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：集計総労働時間
	 */
	private static AggregateTotalWorkingTime toDomainAggregateTotalWorkingTime(KrcdtMonAttendanceTime parentEntity){

		val entity = parentEntity.krcdtMonAggrTotalWrk;
		if (entity == null) return new AggregateTotalWorkingTime();

		// 月別実績の就業時間
		val workTime = WorkTimeOfMonthly.of(
				new AttendanceTimeMonth(entity.workTime),
				new AttendanceTimeMonth(entity.withinPrescribedPremiumTime));

		// 月別実績の所定労働時間
		val prescribedWorkingTime = PrescribedWorkingTimeOfMonthly.of(
				new AttendanceTimeMonth(entity.schedulePrescribedWorkingTime),
				new AttendanceTimeMonth(entity.recordPrescribedWorkingTime));
		
		// 集計総労働時間
		val domain = AggregateTotalWorkingTime.of(
				workTime,
				toDomainOverTimeOfMonthly(parentEntity),
				toDomainHolidayWorkTimeOfMonthly(parentEntity),
				toDomainVacationUseTimeOfMonthly(parentEntity),
				new AttendanceTimeMonth(entity.totalWorkingTime),
				prescribedWorkingTime);
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（月別実績の残業時間）
	 * @param entity エンティティ：月別実績の勤怠時間
	 * @param entityAggrs エンティティ：集計残業時間
	 * @return ドメイン：月別実績の残業時間
	 */
	private static OverTimeOfMonthly toDomainOverTimeOfMonthly(KrcdtMonAttendanceTime parentEntity){
		
		val entity = parentEntity.krcdtMonOverTime;
		if (entity == null) return new OverTimeOfMonthly();
		
		val domain = OverTimeOfMonthly.of(
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.totalOverTime),
						new AttendanceTimeMonth(entity.calcTotalOverTime)),
				new AttendanceTimeMonth(entity.beforeOverTime),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.totalTransferOverTime),
						new AttendanceTimeMonth(entity.calcTotalTransferOverTime)),
				parentEntity.krcdtMonAggrOverTimes.stream()
					.map(c -> toDomainAggregateOverTime(c)).collect(Collectors.toList()));
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（集計残業時間）
	 * @param entity エンティティ：集計残業時間
	 * @return ドメイン：集計残業時間
	 */
	private static AggregateOverTime toDomainAggregateOverTime(KrcdtMonAggrOverTime entity){
		
		val domain = AggregateOverTime.of(
				new OverTimeFrameNo(entity.PK.overTimeFrameNo),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.overTime),
						new AttendanceTimeMonth(entity.calcOverTime)),
				new AttendanceTimeMonth(entity.beforeOverTime),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.transferOverTime),
						new AttendanceTimeMonth(entity.calcTransferOverTime)),
				new AttendanceTimeMonth(entity.legalOverTime),
				new AttendanceTimeMonth(entity.legalTransferOverTime));
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（月別実績の休出時間）
	 * @param parentEntity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：月別実績の休出時間
	 */
	private static HolidayWorkTimeOfMonthly toDomainHolidayWorkTimeOfMonthly(KrcdtMonAttendanceTime parentEntity){
		
		val entity = parentEntity.krcdtMonHdwkTime;
		if (entity == null) return new HolidayWorkTimeOfMonthly();
		
		val domain = HolidayWorkTimeOfMonthly.of(
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.totalHolidayWorkTime),
						new AttendanceTimeMonth(entity.calcTotalHolidayWorkTime)),
				new AttendanceTimeMonth(entity.beforeHolidayWorkTime),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.totalTransferTime),
						new AttendanceTimeMonth(entity.calcTotalTransferTime)),
				parentEntity.krcdtMonAggrHdwkTimes.stream()
					.map(c -> toDomainAggregateHolidayWorkTime(c)).collect(Collectors.toList()));
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（集計休出時間）
	 * @param entity エンティティ：集計休出時間
	 * @return ドメイン：集計休出時間
	 */
	private static AggregateHolidayWorkTime toDomainAggregateHolidayWorkTime(KrcdtMonAggrHdwkTime entity){
		
		val domain = AggregateHolidayWorkTime.of(
				new HolidayWorkFrameNo(entity.PK.holidayWorkFrameNo),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.holidayWorkTime),
						new AttendanceTimeMonth(entity.calcHolidayWorkTime)),
				new AttendanceTimeMonth(entity.beforeHolidayWorkTime),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.transferTime),
						new AttendanceTimeMonth(entity.calcTransferTime)),
				new AttendanceTimeMonth(entity.legalHolidayWorkTime),
				new AttendanceTimeMonth(entity.legalTransferHolidayWorkTime));
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（月別実績の休暇使用時間）
	 * @param parentEntity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：月別実績の休暇使用時間
	 */
	private static VacationUseTimeOfMonthly toDomainVacationUseTimeOfMonthly(KrcdtMonAttendanceTime parentEntity){
		
		val entity = parentEntity.krcdtMonVactUseTime;
		if (entity == null) return new VacationUseTimeOfMonthly();
		
		val domain = VacationUseTimeOfMonthly.of(
				AnnualLeaveUseTimeOfMonthly.of(
						new AttendanceTimeMonth(entity.annualLeaveUseTime)),
				RetentionYearlyUseTimeOfMonthly.of(
						new AttendanceTimeMonth(entity.retentionYearlyUseTime)),
				SpecialHolidayUseTimeOfMonthly.of(
						new AttendanceTimeMonth(entity.specialHolidayUseTime)),
				CompensatoryLeaveUseTimeOfMonthly.of(
						new AttendanceTimeMonth(entity.compensatoryLeaveUseTime)));
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（集計総拘束時間）
	 * @param parentEntity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：集計総拘束時間
	 */
	private static AggregateTotalTimeSpentAtWork toDomainAggregateTotalTimeSpentAtWork(KrcdtMonAttendanceTime parentEntity){
		
		val entity = parentEntity.krcdtMonAggrTotalSpt;
		if (entity == null) return new AggregateTotalTimeSpentAtWork();
		
		val domain = AggregateTotalTimeSpentAtWork.of(
				new AttendanceTimeMonth(entity.overTimeSpentAtWork),
				new AttendanceTimeMonth(entity.midnightTimeSpentAtWork),
				new AttendanceTimeMonth(entity.holidayTimeSpentAtWork),
				new AttendanceTimeMonth(entity.varienceTimeSpentAtWork),
				new AttendanceTimeMonth(entity.totalTimeSpentAtWork));
		return domain;
	}

	/**
	 * エンティティ→ドメイン　（月別実績の時間外超過）
	 * @param parentEntity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：月別実績の時間外超過
	 */
	private static ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly(KrcdtMonAttendanceTime parentEntity){
		
		val entity = parentEntity.krcdtMonExcessOutside;
		if (entity == null) return new ExcessOutsideWorkOfMonthly();
		
		val domain = ExcessOutsideWorkOfMonthly.of(
				new AttendanceTimeMonth(entity.totalWeeklyPremiumTime),
				new AttendanceTimeMonth(entity.totalMonthlyPremiumTime),
				new AttendanceTimeMonthWithMinus(entity.deformationCarryforwardTime),
				parentEntity.krcdtMonExcoutTime.stream()
					.map(c -> toDomainExcessOutsideWork(c)).collect(Collectors.toList()));
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（時間外超過）
	 * @param entity エンティティ：時間外超過
	 * @return ドメイン：時間外超過
	 */
	private static ExcessOutsideWork toDomainExcessOutsideWork(KrcdtMonExcoutTime entity){

		val domain = ExcessOutsideWork.of(
				entity.PK.breakdownNo,
				entity.PK.excessNo,
				new AttendanceTimeMonth(entity.excessTime));
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（月別実績の36協定時間）
	 * @param parentEntity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：月別実績の36協定時間
	 */
	private static AgreementTimeOfMonthly toDomainAgreementTimeOfMonthly(KrcdtMonAttendanceTime parentEntity){
		
		val entity = parentEntity.krcdtMonAgreementTime;
		if (entity == null) return new AgreementTimeOfMonthly();
		
		val domain = AgreementTimeOfMonthly.of(
				new AttendanceTimeMonth(entity.agreementTime),
				new LimitOneMonth(entity.limitErrorTime),
				new LimitOneMonth(entity.limitAlarmTime),
				(entity.exceptionLimitErrorTime == null ?
						Optional.empty() : Optional.of(new LimitOneMonth(entity.exceptionLimitErrorTime))),
				(entity.exceptionLimitAlarmTime == null ?
						Optional.empty() : Optional.of(new LimitOneMonth(entity.exceptionLimitAlarmTime))),
				EnumAdaptor.valueOf(entity.status, AgreementTimeStatusOfMonthly.class));
		return domain;
	}
	
	/**
	 * エンティティ→ドメイン　（月別実績の縦計）
	 * @param parentEntity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：月別実績の縦計
	 */
	private static VerticalTotalOfMonthly toDomainVerticalTotalOfMonthly(KrcdtMonAttendanceTime parentEntity){
		
		val entity = parentEntity.krcdtMonVerticalTotal;
		if (entity == null) return new VerticalTotalOfMonthly();
		
		// 育児外出
		List<GoOutForChildCare> goOutForChildCares = new ArrayList<>();
		if (entity.childcareGoOutTimes != 0 || entity.childcareGoOutTime != 0){
			goOutForChildCares.add(GoOutForChildCare.of(
					ChildCareAtr.CHILD_CARE,
					new AttendanceTimesMonth(entity.childcareGoOutTimes),
					new AttendanceTimeMonth(entity.childcareGoOutTime)));
		}
		if (entity.careGoOutTimes != 0 || entity.careGoOutTime != 0){
			goOutForChildCares.add(GoOutForChildCare.of(
					ChildCareAtr.CARE,
					new AttendanceTimesMonth(entity.careGoOutTimes),
					new AttendanceTimeMonth(entity.careGoOutTime)));
		}
		
		// 月別実績の勤務日数
		val workDays = WorkDaysOfMonthly.of(
				AttendanceDaysOfMonthly.of(new AttendanceDaysMonth(entity.attendanceDays)),
				AbsenceDaysOfMonthly.of(
						new AttendanceDaysMonth(entity.totalAbsenceDays),
						parentEntity.krcdtMonAggrAbsnDays.stream()
								.map(c -> toDomainAggregateAbsenceDays(c)).collect(Collectors.toList())),
				PredeterminedDaysOfMonthly.of(
						new AttendanceDaysMonth(entity.predetermineDays),
						new AttendanceDaysMonth(entity.predetermineDaysBeforeGrant),
						new AttendanceDaysMonth(entity.predetermineDaysAfterGrant)),
				WorkDaysDetailOfMonthly.of(new AttendanceDaysMonth(entity.workDays)),
				HolidayDaysOfMonthly.of(new AttendanceDaysMonth(entity.holidayDays)),
				SpecificDaysOfMonthly.of(
						parentEntity.krcdtMonAggrSpecDays.stream()
								.map(c -> toDomainAggregateSpecificDays(c)).collect(Collectors.toList())),
				HolidayWorkDaysOfMonthly.of(new AttendanceDaysMonth(entity.holidayWorkDays)),
				PayDaysOfMonthly.of(
						new AttendanceDaysMonth(entity.payAttendanceDays),
						new AttendanceDaysMonth(entity.payAbsenceDays)),
				WorkTimesOfMonthly.of(new AttendanceTimesMonth(entity.workTimes)),
				TwoTimesWorkTimesOfMonthly.of(new AttendanceTimesMonth(entity.twoTimesWorkTimes)),
				TemporaryWorkTimesOfMonthly.of(new AttendanceTimesMonth(entity.temporaryWorkTimes)),
				toDomainLeaveOfMonthly(parentEntity));
		
		// 月別実績の勤務時間
		val workTime = nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.WorkTimeOfMonthly.of(
				BonusPayTimeOfMonthly.of(
						parentEntity.krcdtMonAggrBnspyTime.stream()
								.map(c -> toDomainAggregateBonusPayTime(c)).collect(Collectors.toList())),
				GoOutOfMonthly.of(
						parentEntity.krcdtMonAggrGoout.stream()
								.map(c -> toDomainAggregateGoOut(c)).collect(Collectors.toList()),
						goOutForChildCares),
				PremiumTimeOfMonthly.of(
						parentEntity.krcdtMonAggrPremTime.stream()
								.map(c -> toDomainAggregatePremiumTime(c)).collect(Collectors.toList()),
						new AttendanceTimeMonth(entity.premiumMidnightTime),
						new AttendanceTimeMonth(entity.premiumLegalOutsideWorkTime),
						new AttendanceTimeMonth(entity.premiumLegalHolidayWorkTime),
						new AttendanceTimeMonth(entity.premiumIllegalOutsideWorkTime),
						new AttendanceTimeMonth(entity.premiumIllegalHolidayWorkTime)),
				BreakTimeOfMonthly.of(new AttendanceTimeMonth(entity.breakTime)),
				HolidayTimeOfMonthly.of(
						new AttendanceTimeMonth(entity.legalHolidayTime),
						new AttendanceTimeMonth(entity.illegalHolidayTime),
						new AttendanceTimeMonth(entity.illegalSpecialHolidayTime)),
				MidnightTimeOfMonthly.of(
						new TimeMonthWithCalculation(
								new AttendanceTimeMonth(entity.overWorkMidnightTime),
								new AttendanceTimeMonth(entity.calcOverWorkMidnightTime)),
						new TimeMonthWithCalculation(
								new AttendanceTimeMonth(entity.legalMidnightTime),
								new AttendanceTimeMonth(entity.calcLegalMidnightTime)),
						IllegalMidnightTime.of(
								new TimeMonthWithCalculation(
										new AttendanceTimeMonth(entity.illegalMidnightTime),
										new AttendanceTimeMonth(entity.calcIllegalMidnightTime)),
								new AttendanceTimeMonth(entity.illegalBeforeMidnightTime)),
						new TimeMonthWithCalculation(
								new AttendanceTimeMonth(entity.legalHolidayWorkMidnightTime),
								new AttendanceTimeMonth(entity.calcLegalHolidayWorkMidnightTime)),
						new TimeMonthWithCalculation(
								new AttendanceTimeMonth(entity.illegalHolidayWorkMidnightTime),
								new AttendanceTimeMonth(entity.calcIllegalHolidayWorkMidnightTime)),
						new TimeMonthWithCalculation(
								new AttendanceTimeMonth(entity.specialHolidayWorkMidnightTime),
								new AttendanceTimeMonth(entity.calcSpecialHolidayWorkMidnightTime))),
				LateLeaveEarlyOfMonthly.of(
						LeaveEarly.of(
								new AttendanceTimesMonth(entity.leaveEarlyTimes),
								new TimeMonthWithCalculation(
										new AttendanceTimeMonth(entity.leaveEarlyTime),
										new AttendanceTimeMonth(entity.calcLeaveEarlyTime))),
						Late.of(
								new AttendanceTimesMonth(entity.lateTimes),
								new TimeMonthWithCalculation(
										new AttendanceTimeMonth(entity.lateTime),
										new AttendanceTimeMonth(entity.calcLateTime)))),
				AttendanceLeaveGateTimeOfMonthly.of(
						new AttendanceTimeMonth(entity.attendanceLeaveGateBeforeAttendanceTime),
						new AttendanceTimeMonth(entity.attendanceLeaveGateAfterLeaveWorkTime),
						new AttendanceTimeMonth(entity.attendanceLeaveGateStayingTime),
						new AttendanceTimeMonth(entity.attendanceLeaveGateUnemployedTime)),
				BudgetTimeVarienceOfMonthly.of(new AttendanceTimeMonth(entity.budgetVarienceTime)),
				DivergenceTimeOfMonthly.of(
						parentEntity.krcdtMonAggrDivgTime.stream()
								.map(c -> toDomainAggregateDivergenceTime(c)).collect(Collectors.toList())),
				parentEntity.krcdtMonMedicalTime.stream()
						.map(c -> toDomainMedicalTimeOfMonthly(c)).collect(Collectors.toList()));
		
		val domain = VerticalTotalOfMonthly.of(workDays, workTime);
		return domain;
	}

	/**
	 * エンティティ→ドメイン　（集計欠勤日数）
	 * @param entity エンティティ：集計欠勤日数
	 * @return ドメイン：集計欠勤日数
	 */
	private static AggregateAbsenceDays toDomainAggregateAbsenceDays(KrcdtMonAggrAbsnDays entity){
		
		val domain = AggregateAbsenceDays.of(
				entity.PK.absenceFrameNo,
				new AttendanceDaysMonth(entity.absenceDays));
		return domain;
	}

	/**
	 * エンティティ→ドメイン　（集計特定日数）
	 * @param entity エンティティ：集計特定日数
	 * @return ドメイン：集計特定日数
	 */
	private static AggregateSpecificDays toDomainAggregateSpecificDays(KrcdtMonAggrSpecDays entity){
		
		val domain = AggregateSpecificDays.of(
				new SpecificDateItemNo(entity.PK.specificDayItemNo),
				new AttendanceDaysMonth(entity.specificDays),
				new AttendanceDaysMonth(entity.holidayWorkSpecificDays));
		return domain;
	}

	/**
	 * エンティティ→ドメイン　（月別実績の休業）
	 * @param parentEntity エンティティ：月別実績の勤怠時間
	 * @return ドメイン：月別実績の休業
	 */
	private static LeaveOfMonthly toDomainLeaveOfMonthly(KrcdtMonAttendanceTime parentEntity){
		
		val entity = parentEntity.krcdtMonLeave;
		if (entity == null) return new LeaveOfMonthly();
		
		List<AggregateLeaveDays> fixLeaveDaysList = new ArrayList<>();
		List<AnyLeave> anyLeaveDaysList = new ArrayList<>();
		
		if (entity.prenatalLeaveDays != 0.0){
			fixLeaveDaysList.add(AggregateLeaveDays.of(
					CloseAtr.PRENATAL, new AttendanceDaysMonth(entity.prenatalLeaveDays)));
		}
		if (entity.postpartumLeaveDays != 0.0){
			fixLeaveDaysList.add(AggregateLeaveDays.of(
					CloseAtr.POSTPARTUM, new AttendanceDaysMonth(entity.postpartumLeaveDays)));
		}
		if (entity.childcareLeaveDays != 0.0){
			fixLeaveDaysList.add(AggregateLeaveDays.of(
					CloseAtr.CHILD_CARE, new AttendanceDaysMonth(entity.childcareLeaveDays)));
		}
		if (entity.careLeaveDays != 0.0){
			fixLeaveDaysList.add(AggregateLeaveDays.of(
					CloseAtr.CARE, new AttendanceDaysMonth(entity.careLeaveDays)));
		}
		if (entity.injuryOrIllnessLeaveDays != 0.0){
			fixLeaveDaysList.add(AggregateLeaveDays.of(
					CloseAtr.INJURY_OR_ILLNESS, new AttendanceDaysMonth(entity.injuryOrIllnessLeaveDays)));
		}
		if (entity.anyLeaveDays01 != 0.0){
			anyLeaveDaysList.add(AnyLeave.of(1, new AttendanceDaysMonth(entity.anyLeaveDays01)));
		}
		if (entity.anyLeaveDays02 != 0.0){
			anyLeaveDaysList.add(AnyLeave.of(2, new AttendanceDaysMonth(entity.anyLeaveDays02)));
		}
		if (entity.anyLeaveDays03 != 0.0){
			anyLeaveDaysList.add(AnyLeave.of(3, new AttendanceDaysMonth(entity.anyLeaveDays03)));
		}
		if (entity.anyLeaveDays04 != 0.0){
			anyLeaveDaysList.add(AnyLeave.of(4, new AttendanceDaysMonth(entity.anyLeaveDays04)));
		}
		
		val domain = LeaveOfMonthly.of(fixLeaveDaysList, anyLeaveDaysList);
		return domain;
	}

	/**
	 * エンティティ→ドメイン　（集計加給時間）
	 * @param entity エンティティ：集計加給時間
	 * @return ドメイン：集計加給時間
	 */
	private static AggregateBonusPayTime toDomainAggregateBonusPayTime(KrcdtMonAggrBnspyTime entity){
		
		val domain = AggregateBonusPayTime.of(
				entity.PK.bonusPayFrameNo,
				new AttendanceTimeMonth(entity.bonusPayTime),
				new AttendanceTimeMonth(entity.specificBonusPayTime),
				new AttendanceTimeMonth(entity.holidayWorkBonusPayTime),
				new AttendanceTimeMonth(entity.holidayWorkSpecificBonusPayTime));
		return domain;
	}

	/**
	 * エンティティ→ドメイン　（集計乖離時間）
	 * @param entity エンティティ：集計乖離時間
	 * @return ドメイン：集計乖離時間
	 */
	private static AggregateDivergenceTime toDomainAggregateDivergenceTime(KrcdtMonAggrDivgTime entity){
		
		val domain = AggregateDivergenceTime.of(
				entity.PK.divergenceTimeNo,
				new AttendanceTimeMonth(entity.divergenceTime),
				new AttendanceTimeMonth(entity.deductionTime),
				new AttendanceTimeMonth(entity.divergenceTimeAfterDeduction),
				EnumAdaptor.valueOf(entity.divergenceAtr, DivergenceAtrOfMonthly.class));
		return domain;
	}

	/**
	 * エンティティ→ドメイン　（集計外出）
	 * @param entity エンティティ：集計外出
	 * @return ドメイン：集計外出
	 */
	private static AggregateGoOut toDomainAggregateGoOut(KrcdtMonAggrGoout entity){
		
		val domain = AggregateGoOut.of(
				EnumAdaptor.valueOf(entity.PK.goOutReason, GoingOutReason.class),
				new AttendanceTimesMonth(entity.goOutTimes),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.legalTime),
						new AttendanceTimeMonth(entity.calcLegalTime)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.illegalTime),
						new AttendanceTimeMonth(entity.calcIllegalTime)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(entity.totalTime),
						new AttendanceTimeMonth(entity.calcTotalTime)));
		return domain;
	}

	/**
	 * エンティティ→ドメイン　（集計割増時間）
	 * @param entity エンティティ：集計割増時間
	 * @return ドメイン：集計割増時間
	 */
	private static AggregatePremiumTime toDomainAggregatePremiumTime(KrcdtMonAggrPremTime entity){
		
		val domain = AggregatePremiumTime.of(
				entity.PK.premiumTimeItemNo,
				new AttendanceTimeMonth(entity.premiumTime));
		return domain;
	}

	/**
	 * エンティティ→ドメイン　（月別実績の医療時間）
	 * @param entity エンティティ：月別実績の医療時間
	 * @return ドメイン：月別実績の医療時間
	 */
	private static MedicalTimeOfMonthly toDomainMedicalTimeOfMonthly(KrcdtMonMedicalTime entity){
		
		val domain = MedicalTimeOfMonthly.of(
				EnumAdaptor.valueOf(entity.PK.dayNightAtr, WorkTimeNightShift.class),
				new AttendanceTimeMonth(entity.workTime),
				new AttendanceTimeMonth(entity.deductionTime),
				new AttendanceTimeMonth(entity.takeOverTime));
		return domain;
	}
	
	/** 登録および更新 */
	@Override
	public void persistAndUpdate(AttendanceTimeOfMonthly domain){

		// 締め日付
		val closureDate = domain.getClosureDate();
		
		// キー
		val key = new KrcdtMonAttendanceTimePK(
				domain.getEmployeeId(),
				domain.getYearMonth().v(),
				domain.getClosureId().value,
				closureDate.getClosureDay().v(),
				(closureDate.getLastDayOfMonth() ? 1 : 0));

		// 月別実績の月の計算
		val monthlyCalculation = domain.getMonthlyCalculation();
		
		// 登録・更新を判断　および　キー値設定
		boolean isNeedPersist = false;
		KrcdtMonAttendanceTime entity = this.getEntityManager().find(KrcdtMonAttendanceTime.class, key);
		if (entity == null){
			isNeedPersist = true;
			entity = new KrcdtMonAttendanceTime();
			entity.PK = key;
		}
		
		// 登録・更新値の設定
		entity.startYmd = domain.getDatePeriod().start();
		entity.endYmd = domain.getDatePeriod().end();
		entity.aggregateDays = domain.getAggregateDays().v();
		entity.statutoryWorkingTime = monthlyCalculation.getStatutoryWorkingTime().v();

		// 実働時間：月別実績の通常変形時間　値設定
		val actualWorkingTime = monthlyCalculation.getActualWorkingTime();
		val irregularWorkingTime = actualWorkingTime.getIrregularWorkingTime();
		if (entity.krcdtMonRegIrregTime == null){
			entity.krcdtMonRegIrregTime = new KrcdtMonRegIrregTime();
			entity.krcdtMonRegIrregTime.PK = key;
		}
		val entityRegIrregTime = entity.krcdtMonRegIrregTime;
		entityRegIrregTime.weeklyTotalPremiumTime = actualWorkingTime.getWeeklyTotalPremiumTime().v();
		entityRegIrregTime.monthlyTotalPremiumTime = actualWorkingTime.getMonthlyTotalPremiumTime().v();
		entityRegIrregTime.multiMonthIrregularMiddleTime = irregularWorkingTime.getMultiMonthIrregularMiddleTime().v();
		entityRegIrregTime.irregularPeriodCarryforwardTime = irregularWorkingTime.getIrregularPeriodCarryforwardTime().v();
		entityRegIrregTime.irregularWorkingShortageTime = irregularWorkingTime.getIrregularWorkingShortageTime().v();
		entityRegIrregTime.irregularLegalOverTime = irregularWorkingTime.getIrregularLegalOverTime().getTime().v();
		entityRegIrregTime.calcIrregularLegalOverTime = irregularWorkingTime.getIrregularLegalOverTime().getCalcTime().v();
		
		// 月別実績のフレックス時間
		val flexTimeOfMonthly = monthlyCalculation.getFlexTime();
		val flexTime = flexTimeOfMonthly.getFlexTime();
		val flexCarryForwardTime = flexTimeOfMonthly.getFlexCarryforwardTime();
		val flexTimeOfExcessOutsideTime = flexTimeOfMonthly.getFlexTimeOfExcessOutsideTime();
		val flexShortDeductTime = flexTimeOfMonthly.getFlexShortDeductTime();
		if (entity.krcdtMonFlexTime == null){
			entity.krcdtMonFlexTime = new KrcdtMonFlexTime();
			entity.krcdtMonFlexTime.PK = key;
		}
		val entityFlexTime = entity.krcdtMonFlexTime;
		entityFlexTime.flexTime = flexTime.getFlexTime().getTime().v();
		entityFlexTime.calcFlexTime = flexTime.getFlexTime().getCalcTime().v();
		entityFlexTime.beforeFlexTime = flexTime.getBeforeFlexTime().v();
		entityFlexTime.legalFlexTime = flexTime.getLegalFlexTime().v();
		entityFlexTime.illegalFlexTime = flexTime.getIllegalFlexTime().v();
		entityFlexTime.flexExcessTime = flexTimeOfMonthly.getFlexExcessTime().v();
		entityFlexTime.flexShortageTime = flexTimeOfMonthly.getFlexShortageTime().v();
		entityFlexTime.flexCarryforwardWorkTime = flexCarryForwardTime.getFlexCarryforwardWorkTime().v();
		entityFlexTime.flexCarryforwardTime = flexCarryForwardTime.getFlexCarryforwardTime().v();
		entityFlexTime.flexCarryforwardShortageTime = flexCarryForwardTime.getFlexCarryforwardShortageTime().v();
		entityFlexTime.excessFlexAtr = flexTimeOfExcessOutsideTime.getExcessFlexAtr().value;
		entityFlexTime.principleTime = flexTimeOfExcessOutsideTime.getPrincipleTime().v();
		entityFlexTime.forConvenienceTime = flexTimeOfExcessOutsideTime.getForConvenienceTime().v();
		entityFlexTime.annualLeaveDeductDays = flexShortDeductTime.getAnnualLeaveDeductDays().v();
		entityFlexTime.absenceDeductTime = flexShortDeductTime.getAbsenceDeductTime().v();
		entityFlexTime.shotTimeBeforeDeduct = flexShortDeductTime.getFlexShortTimeBeforeDeduct().v();
		
		// 総労働時間：月別実績の休暇使用時間
		val totalWorkingTime = monthlyCalculation.getTotalWorkingTime();
		val vacationUseTime = totalWorkingTime.getVacationUseTime();
		val annualLeaveUseTime = vacationUseTime.getAnnualLeave();
		val retentionYearlyUseTime = vacationUseTime.getRetentionYearly();
		val specialHolidayUseTime = vacationUseTime.getSpecialHoliday();
		val compensatoryLeaveUseTime = vacationUseTime.getCompensatoryLeave();
		if (entity.krcdtMonVactUseTime == null){
			entity.krcdtMonVactUseTime = new KrcdtMonVactUseTime();
			entity.krcdtMonVactUseTime.PK = key;
		}
		val entityVactUseTime = entity.krcdtMonVactUseTime;
		entityVactUseTime.annualLeaveUseTime = annualLeaveUseTime.getUseTime().v();
		entityVactUseTime.retentionYearlyUseTime = retentionYearlyUseTime.getUseTime().v();
		entityVactUseTime.specialHolidayUseTime = specialHolidayUseTime.getUseTime().v();
		entityVactUseTime.compensatoryLeaveUseTime = compensatoryLeaveUseTime.getUseTime().v();
		
		// 総労働時間：集計総労働時間
		val workTime = totalWorkingTime.getWorkTime();
		val prescribedWorkingTime = totalWorkingTime.getPrescribedWorkingTime();
		if (entity.krcdtMonAggrTotalWrk == null){
			entity.krcdtMonAggrTotalWrk = new KrcdtMonAggrTotalWrk();
			entity.krcdtMonAggrTotalWrk.PK = key;
		}
		val entityAggrTotalWrk = entity.krcdtMonAggrTotalWrk;
		entityAggrTotalWrk.totalWorkingTime = totalWorkingTime.getTotalWorkingTime().v();
		entityAggrTotalWrk.workTime = workTime.getWorkTime().v();
		entityAggrTotalWrk.withinPrescribedPremiumTime = workTime.getWithinPrescribedPremiumTime().v();
		entityAggrTotalWrk.schedulePrescribedWorkingTime = prescribedWorkingTime.getSchedulePrescribedWorkingTime().v();
		entityAggrTotalWrk.recordPrescribedWorkingTime = prescribedWorkingTime.getRecordPrescribedWorkingTime().v();
		
		// 総労働時間：残業時間：月別実績の残業時間
		val overTime = totalWorkingTime.getOverTime();
		if (entity.krcdtMonOverTime == null){
			entity.krcdtMonOverTime = new KrcdtMonOverTime();
			entity.krcdtMonOverTime.PK = key;
		}
		val entityOverTime = entity.krcdtMonOverTime;
		entityOverTime.totalOverTime = overTime.getTotalOverTime().getTime().v();
		entityOverTime.calcTotalOverTime = overTime.getTotalOverTime().getCalcTime().v();
		entityOverTime.beforeOverTime = overTime.getBeforeOverTime().v();
		entityOverTime.totalTransferOverTime = overTime.getTotalTransferOverTime().getTime().v();
		entityOverTime.calcTotalTransferOverTime = overTime.getTotalTransferOverTime().getCalcTime().v();

		// 総労働時間：残業時間：集計残業時間
		val aggrOverTimeMap = overTime.getAggregateOverTimeMap();
		if (entity.krcdtMonAggrOverTimes == null) entity.krcdtMonAggrOverTimes = new ArrayList<>();
		val entityAggrOverTimeList = entity.krcdtMonAggrOverTimes;
		entityAggrOverTimeList.removeIf(
				a -> {return !aggrOverTimeMap.containsKey(new OverTimeFrameNo(a.PK.overTimeFrameNo));} );
		for (val aggrOverTime : aggrOverTimeMap.values()){
			KrcdtMonAggrOverTime entityAggrOverTime = new KrcdtMonAggrOverTime();
			boolean isAddAggrOverTime = false;
			val entityAggrOverTimeOpt = entityAggrOverTimeList.stream()
					.filter(c -> c.PK.overTimeFrameNo == aggrOverTime.getOverTimeFrameNo().v()).findFirst();
			if (entityAggrOverTimeOpt.isPresent()){
				entityAggrOverTime = entityAggrOverTimeOpt.get();
			}
			else {
				isAddAggrOverTime = true;
				entityAggrOverTime.PK = new KrcdtMonAggrOverTimePK(
						domain.getEmployeeId(),
						domain.getYearMonth().v(),
						domain.getClosureId().value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0),
						aggrOverTime.getOverTimeFrameNo().v());
			}
			entityAggrOverTime.overTime = aggrOverTime.getOverTime().getTime().v();
			entityAggrOverTime.calcOverTime = aggrOverTime.getOverTime().getCalcTime().v();
			entityAggrOverTime.beforeOverTime = aggrOverTime.getBeforeOverTime().v();
			entityAggrOverTime.transferOverTime = aggrOverTime.getTransferOverTime().getTime().v();
			entityAggrOverTime.calcTransferOverTime = aggrOverTime.getTransferOverTime().getCalcTime().v();
			entityAggrOverTime.legalOverTime = aggrOverTime.getLegalOverTime().v();
			entityAggrOverTime.legalTransferOverTime = aggrOverTime.getLegalTransferOverTime().v();
			if (isAddAggrOverTime) entityAggrOverTimeList.add(entityAggrOverTime);
		}
		
		// 総労働時間：休出・代休：月別実績の休出時間
		val holidayWorkTime = totalWorkingTime.getHolidayWorkTime();
		if (entity.krcdtMonHdwkTime == null){
			entity.krcdtMonHdwkTime = new KrcdtMonHdwkTime();
			entity.krcdtMonHdwkTime.PK = key;
		}
		val entityHdwkTime = entity.krcdtMonHdwkTime;
		entityHdwkTime.totalHolidayWorkTime = holidayWorkTime.getTotalHolidayWorkTime().getTime().v();
		entityHdwkTime.calcTotalHolidayWorkTime = holidayWorkTime.getTotalHolidayWorkTime().getCalcTime().v();
		entityHdwkTime.beforeHolidayWorkTime = holidayWorkTime.getBeforeHolidayWorkTime().v();
		entityHdwkTime.totalTransferTime = holidayWorkTime.getTotalTransferTime().getTime().v();
		entityHdwkTime.calcTotalTransferTime = holidayWorkTime.getTotalTransferTime().getCalcTime().v();
		
		// 総労働時間：休出・代休：集計休出時間
		val aggrHolidayWorkTimeMap = holidayWorkTime.getAggregateHolidayWorkTimeMap();
		if (entity.krcdtMonAggrHdwkTimes == null) entity.krcdtMonAggrHdwkTimes = new ArrayList<>();
		val entityAggrHdwkTimeList = entity.krcdtMonAggrHdwkTimes;
		entityAggrHdwkTimeList.removeIf(
				a -> {return !aggrHolidayWorkTimeMap.containsKey(new HolidayWorkFrameNo(a.PK.holidayWorkFrameNo));} );
		for (val aggrHolidayWorkTime : aggrHolidayWorkTimeMap.values()){
			KrcdtMonAggrHdwkTime entityAggrHdwkTime = new KrcdtMonAggrHdwkTime();
			boolean isAddAggrHdwkTime = false;
			val entityAggrHdwkTimeOpt = entityAggrHdwkTimeList.stream()
					.filter(c -> c.PK.holidayWorkFrameNo == aggrHolidayWorkTime.getHolidayWorkFrameNo().v()).findFirst();
			if (entityAggrHdwkTimeOpt.isPresent()){
				entityAggrHdwkTime = entityAggrHdwkTimeOpt.get();
			}
			else {
				isAddAggrHdwkTime = true;
				entityAggrHdwkTime.PK = new KrcdtMonAggrHdwkTimePK(
						domain.getEmployeeId(),
						domain.getYearMonth().v(),
						domain.getClosureId().value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0),
						aggrHolidayWorkTime.getHolidayWorkFrameNo().v());
			}
			entityAggrHdwkTime.holidayWorkTime = aggrHolidayWorkTime.getHolidayWorkTime().getTime().v();
			entityAggrHdwkTime.calcHolidayWorkTime = aggrHolidayWorkTime.getHolidayWorkTime().getCalcTime().v();
			entityAggrHdwkTime.beforeHolidayWorkTime = aggrHolidayWorkTime.getBeforeHolidayWorkTime().v();
			entityAggrHdwkTime.transferTime = aggrHolidayWorkTime.getTransferTime().getTime().v();
			entityAggrHdwkTime.calcTransferTime = aggrHolidayWorkTime.getTransferTime().getCalcTime().v();
			entityAggrHdwkTime.legalHolidayWorkTime = aggrHolidayWorkTime.getLegalHolidayWorkTime().v();
			entityAggrHdwkTime.legalTransferHolidayWorkTime = aggrHolidayWorkTime.getLegalTransferHolidayWorkTime().v();
			if (isAddAggrHdwkTime) entityAggrHdwkTimeList.add(entityAggrHdwkTime);
		}
		
		// 集計総拘束時間
		val totalTimeSpentAtWork = monthlyCalculation.getTotalTimeSpentAtWork();
		if (entity.krcdtMonAggrTotalSpt == null){
			entity.krcdtMonAggrTotalSpt = new KrcdtMonAggrTotalSpt();
			entity.krcdtMonAggrTotalSpt.PK = key;
		}
		val entityAggrTotalSpt = entity.krcdtMonAggrTotalSpt;
		entityAggrTotalSpt.overTimeSpentAtWork = totalTimeSpentAtWork.getOverTimeSpentAtWork().v();
		entityAggrTotalSpt.midnightTimeSpentAtWork = totalTimeSpentAtWork.getMidnightTimeSpentAtWork().v();
		entityAggrTotalSpt.holidayTimeSpentAtWork = totalTimeSpentAtWork.getHolidayTimeSpentAtWork().v();
		entityAggrTotalSpt.varienceTimeSpentAtWork = totalTimeSpentAtWork.getVarienceTimeSpentAtWork().v();
		entityAggrTotalSpt.totalTimeSpentAtWork = totalTimeSpentAtWork.getTotalTimeSpentAtWork().v();
		
		// 時間外超過
		val excessOutsideWork = domain.getExcessOutsideWork();
		if (entity.krcdtMonExcessOutside == null){
			entity.krcdtMonExcessOutside = new KrcdtMonExcessOutside();
			entity.krcdtMonExcessOutside.PK = key;
		}
		val entityExcessOutside = entity.krcdtMonExcessOutside;
		entityExcessOutside.totalWeeklyPremiumTime = excessOutsideWork.getWeeklyTotalPremiumTime().v();
		entityExcessOutside.totalMonthlyPremiumTime = excessOutsideWork.getMonthlyTotalPremiumTime().v();
		entityExcessOutside.deformationCarryforwardTime = excessOutsideWork.getDeformationCarryforwardTime().v();
		
		// 時間外超過：時間
		val excessOutsideTimeMap = excessOutsideWork.getTime();
		if (entity.krcdtMonExcoutTime == null) entity.krcdtMonExcoutTime = new ArrayList<>();
		val entityExcoutTimeList = entity.krcdtMonExcoutTime;
		entityExcoutTimeList.removeIf(
				a -> {return !excessOutsideWork.containsTime(a.PK.breakdownNo, a.PK.excessNo);} );
		for (val breakdowns : excessOutsideTimeMap.values()){
			for (val excessOutsideTime : breakdowns.getBreakdown().values()) {
				KrcdtMonExcoutTime entityExcoutTime = new KrcdtMonExcoutTime();
				boolean isAddExcoutTime = false;
				val entityExcoutTimeOpt = entityExcoutTimeList.stream()
						.filter(c -> (c.PK.breakdownNo == excessOutsideTime.getBreakdownNo() &&
									c.PK.excessNo == excessOutsideTime.getExcessNo())).findFirst();
				if (entityExcoutTimeOpt.isPresent()){
					entityExcoutTime = entityExcoutTimeOpt.get();
				}
				else {
					isAddExcoutTime = true;
					entityExcoutTime.PK = new KrcdtMonExcoutTimePK(
							domain.getEmployeeId(),
							domain.getYearMonth().v(),
							domain.getClosureId().value,
							closureDate.getClosureDay().v(),
							(closureDate.getLastDayOfMonth() ? 1 : 0),
							excessOutsideTime.getBreakdownNo(),
							excessOutsideTime.getExcessNo());
				}
				entityExcoutTime.excessTime = excessOutsideTime.getExcessTime().v();
				if (isAddExcoutTime) entityExcoutTimeList.add(entityExcoutTime);
			}
		}
		
		// 36協定時間
		val agreementTime = monthlyCalculation.getAgreementTime();
		if (entity.krcdtMonAgreementTime == null){
			entity.krcdtMonAgreementTime = new KrcdtMonAgreementTime();
			entity.krcdtMonAgreementTime.PK = key;
		}
		val entityAgreementTime = entity.krcdtMonAgreementTime;
		entityAgreementTime.agreementTime = agreementTime.getAgreementTime().v();
		entityAgreementTime.limitErrorTime = agreementTime.getLimitErrorTime().v();
		entityAgreementTime.limitAlarmTime = agreementTime.getLimitAlarmTime().v();
		entityAgreementTime.exceptionLimitErrorTime = (agreementTime.getExceptionLimitErrorTime().isPresent() ?
				agreementTime.getExceptionLimitErrorTime().get().v() : null); 
		entityAgreementTime.exceptionLimitAlarmTime = (agreementTime.getExceptionLimitAlarmTime().isPresent() ?
				agreementTime.getExceptionLimitAlarmTime().get().v() : null);
		entityAgreementTime.status = agreementTime.getStatus().value;
		
		// 縦計
		val verticalTotal = domain.getVerticalTotal();
		val vtWorkDays = verticalTotal.getWorkDays();
		val vtWorkTime = verticalTotal.getWorkTime();
		if (entity.krcdtMonVerticalTotal == null){
			entity.krcdtMonVerticalTotal = new KrcdtMonVerticalTotal();
			entity.krcdtMonVerticalTotal.PK = key;
		}
		val entityVerticalTotal = entity.krcdtMonVerticalTotal;
		entityVerticalTotal.workDays = vtWorkDays.getWorkDays().getDays().v();
		entityVerticalTotal.workTimes = vtWorkDays.getWorkTimes().getTimes().v();
		entityVerticalTotal.twoTimesWorkTimes = vtWorkDays.getTwoTimesWorkTimes().getTimes().v();
		entityVerticalTotal.temporaryWorkTimes = vtWorkDays.getTemporaryWorkTimes().getTimes().v();
		entityVerticalTotal.predetermineDays = vtWorkDays.getPredetermineDays().getPredeterminedDays().v();
		entityVerticalTotal.predetermineDaysBeforeGrant = vtWorkDays.getPredetermineDays().getPredeterminedDaysBeforeGrant().v();
		entityVerticalTotal.predetermineDaysAfterGrant = vtWorkDays.getPredetermineDays().getPredeterminedDaysAfterGrant().v();
		entityVerticalTotal.holidayDays = vtWorkDays.getHolidayDays().getDays().v();
		entityVerticalTotal.attendanceDays = vtWorkDays.getAttendanceDays().getDays().v();
		entityVerticalTotal.holidayWorkDays = vtWorkDays.getHolidayWorkDays().getDays().v();
		entityVerticalTotal.totalAbsenceDays = vtWorkDays.getAbsenceDays().getTotalAbsenceDays().v();
		entityVerticalTotal.payAttendanceDays = vtWorkDays.getPayDays().getPayAttendanceDays().v();
		entityVerticalTotal.payAbsenceDays = vtWorkDays.getPayDays().getPayAbsenceDays().v();
		
		entityVerticalTotal.childcareGoOutTimes = 0;
		entityVerticalTotal.childcareGoOutTime = 0;
		entityVerticalTotal.careGoOutTimes = 0;
		entityVerticalTotal.careGoOutTime = 0;
		val goOutForChildCares = vtWorkTime.getGoOut().getGoOutForChildCares();
		if (goOutForChildCares.containsKey(ChildCareAtr.CHILD_CARE)){
			val goOutForChildCare = goOutForChildCares.get(ChildCareAtr.CHILD_CARE);
			entityVerticalTotal.childcareGoOutTimes = goOutForChildCare.getTimes().v();
			entityVerticalTotal.childcareGoOutTime = goOutForChildCare.getTime().v();
		}
		if (goOutForChildCares.containsKey(ChildCareAtr.CARE)){
			val goOutForCare = goOutForChildCares.get(ChildCareAtr.CARE);
			entityVerticalTotal.careGoOutTimes = goOutForCare.getTimes().v();
			entityVerticalTotal.careGoOutTime = goOutForCare.getTime().v();
		}
		
		entityVerticalTotal.premiumMidnightTime = vtWorkTime.getPremiumTime().getMidnightTime().v();
		entityVerticalTotal.premiumLegalOutsideWorkTime = vtWorkTime.getPremiumTime().getLegalOutsideWorkTime().v();
		entityVerticalTotal.premiumIllegalOutsideWorkTime = vtWorkTime.getPremiumTime().getIllegalOutsideWorkTime().v();
		entityVerticalTotal.premiumLegalHolidayWorkTime = vtWorkTime.getPremiumTime().getLegalHolidayWorkTime().v();
		entityVerticalTotal.premiumIllegalHolidayWorkTime = vtWorkTime.getPremiumTime().getIllegalHolidayWorkTime().v();
		entityVerticalTotal.breakTime = vtWorkTime.getBreakTime().getBreakTime().v();
		entityVerticalTotal.legalHolidayTime = vtWorkTime.getHolidayTime().getLegalHolidayTime().v();
		entityVerticalTotal.illegalHolidayTime = vtWorkTime.getHolidayTime().getIllegalHolidayTime().v();
		entityVerticalTotal.illegalSpecialHolidayTime = vtWorkTime.getHolidayTime().getIllegalSpecialHolidayTime().v();
		entityVerticalTotal.overWorkMidnightTime = vtWorkTime.getMidnightTime().getOverWorkMidnightTime().getTime().v();
		entityVerticalTotal.calcOverWorkMidnightTime = vtWorkTime.getMidnightTime().getOverWorkMidnightTime().getCalcTime().v();
		entityVerticalTotal.legalMidnightTime = vtWorkTime.getMidnightTime().getLegalMidnightTime().getTime().v();
		entityVerticalTotal.calcLegalMidnightTime = vtWorkTime.getMidnightTime().getLegalMidnightTime().getCalcTime().v();
		entityVerticalTotal.illegalMidnightTime = vtWorkTime.getMidnightTime().getIllegalMidnightTime().getTime().getTime().v();
		entityVerticalTotal.calcIllegalMidnightTime = vtWorkTime.getMidnightTime().getIllegalMidnightTime().getTime().getCalcTime().v();
		entityVerticalTotal.illegalBeforeMidnightTime = vtWorkTime.getMidnightTime().getIllegalMidnightTime().getBeforeTime().v();
		entityVerticalTotal.legalHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getLegalHolidayWorkMidnightTime().getTime().v();
		entityVerticalTotal.calcLegalHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getLegalHolidayWorkMidnightTime().getCalcTime().v();
		entityVerticalTotal.illegalHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getIllegalHolidayWorkMidnightTime().getTime().v();
		entityVerticalTotal.calcIllegalHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getIllegalHolidayWorkMidnightTime().getCalcTime().v();
		entityVerticalTotal.specialHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getSpecialHolidayWorkMidnightTime().getTime().v();
		entityVerticalTotal.calcSpecialHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getSpecialHolidayWorkMidnightTime().getCalcTime().v();
		entityVerticalTotal.lateTimes = vtWorkTime.getLateLeaveEarly().getLate().getTimes().v();
		entityVerticalTotal.lateTime = vtWorkTime.getLateLeaveEarly().getLate().getTime().getTime().v();
		entityVerticalTotal.calcLateTime = vtWorkTime.getLateLeaveEarly().getLate().getTime().getCalcTime().v();
		entityVerticalTotal.leaveEarlyTimes = vtWorkTime.getLateLeaveEarly().getLeaveEarly().getTimes().v();
		entityVerticalTotal.leaveEarlyTime = vtWorkTime.getLateLeaveEarly().getLeaveEarly().getTime().getTime().v();
		entityVerticalTotal.calcLeaveEarlyTime = vtWorkTime.getLateLeaveEarly().getLeaveEarly().getTime().getCalcTime().v();
		entityVerticalTotal.attendanceLeaveGateBeforeAttendanceTime = vtWorkTime.getAttendanceLeaveGateTime().getTimeBeforeAttendance().v();
		entityVerticalTotal.attendanceLeaveGateAfterLeaveWorkTime = vtWorkTime.getAttendanceLeaveGateTime().getTimeAfterLeaveWork().v();
		entityVerticalTotal.attendanceLeaveGateStayingTime = vtWorkTime.getAttendanceLeaveGateTime().getStayingTime().v();
		entityVerticalTotal.attendanceLeaveGateUnemployedTime = vtWorkTime.getAttendanceLeaveGateTime().getUnemployedTime().v();
		entityVerticalTotal.budgetVarienceTime = vtWorkTime.getBudgetTimeVarience().getTime().v();
		
		// 縦計：勤務日数：集計欠勤日数
		val absenceDaysMap = vtWorkDays.getAbsenceDays().getAbsenceDaysList();
		if (entity.krcdtMonAggrAbsnDays == null) entity.krcdtMonAggrAbsnDays = new ArrayList<>();
		val entityAggrAbsnDaysList = entity.krcdtMonAggrAbsnDays;
		entityAggrAbsnDaysList.removeIf(a -> {return !absenceDaysMap.containsKey(a.PK.absenceFrameNo);} );
		for (val absenceDays : absenceDaysMap.values()){
			KrcdtMonAggrAbsnDays entityAggrAbsnDays = new KrcdtMonAggrAbsnDays();
			boolean isAddAggrAbsnDays = false;
			val entityAggrAbsnDaysOpt = entityAggrAbsnDaysList.stream()
					.filter(c -> c.PK.absenceFrameNo == absenceDays.getAbsenceFrameNo()).findFirst();
			if (entityAggrAbsnDaysOpt.isPresent()){
				entityAggrAbsnDays = entityAggrAbsnDaysOpt.get();
			}
			else {
				isAddAggrAbsnDays = true;
				entityAggrAbsnDays.PK = new KrcdtMonAggrAbsnDaysPK(
						domain.getEmployeeId(),
						domain.getYearMonth().v(),
						domain.getClosureId().value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0),
						absenceDays.getAbsenceFrameNo());
			}
			entityAggrAbsnDays.absenceDays = absenceDays.getDays().v();
			if (isAddAggrAbsnDays) entityAggrAbsnDaysList.add(entityAggrAbsnDays);
		}
		
		// 縦計：勤務日数：集計特定日数
		val specificDaysMap = vtWorkDays.getSpecificDays().getSpecificDays();
		if (entity.krcdtMonAggrSpecDays == null) entity.krcdtMonAggrSpecDays = new ArrayList<>();
		val entityAggrSpecDaysList = entity.krcdtMonAggrSpecDays;
		entityAggrSpecDaysList.removeIf(
				a -> {return !specificDaysMap.containsKey(new SpecificDateItemNo(a.PK.specificDayItemNo));} );
		for (val specificDays : specificDaysMap.values()){
			KrcdtMonAggrSpecDays entityAggrSpecDays = new KrcdtMonAggrSpecDays();
			boolean isAddAggrSpecDays = false;
			val entityAggrSpecDaysOpt = entityAggrSpecDaysList.stream()
					.filter(c -> c.PK.specificDayItemNo == specificDays.getSpecificDayItemNo().v()).findFirst();
			if (entityAggrSpecDaysOpt.isPresent()){
				entityAggrSpecDays = entityAggrSpecDaysOpt.get();
			}
			else {
				isAddAggrSpecDays = true;
				entityAggrSpecDays.PK = new KrcdtMonAggrSpecDaysPK(
						domain.getEmployeeId(),
						domain.getYearMonth().v(),
						domain.getClosureId().value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0),
						specificDays.getSpecificDayItemNo().v());
			}
			entityAggrSpecDays.specificDays = specificDays.getSpecificDays().v();
			entityAggrSpecDays.holidayWorkSpecificDays = specificDays.getHolidayWorkSpecificDays().v();
			if (isAddAggrSpecDays) entityAggrSpecDaysList.add(entityAggrSpecDays);
		}
		
		// 縦計：勤務日数：月別実績の休業
		val vtLeave = vtWorkDays.getLeave();
		if (entity.krcdtMonLeave == null){
			entity.krcdtMonLeave = new KrcdtMonLeave();
			entity.krcdtMonLeave.PK = key;
		}
		val entityLeave = entity.krcdtMonLeave;
		entityLeave.prenatalLeaveDays = 0.0;
		entityLeave.postpartumLeaveDays = 0.0;
		entityLeave.childcareLeaveDays = 0.0;
		entityLeave.careLeaveDays = 0.0;
		entityLeave.injuryOrIllnessLeaveDays = 0.0;
		entityLeave.anyLeaveDays01 = 0.0;
		entityLeave.anyLeaveDays02 = 0.0;
		entityLeave.anyLeaveDays03 = 0.0;
		entityLeave.anyLeaveDays04 = 0.0;
		val fixLeaveDaysMap = vtLeave.getFixLeaveDays();
		if (fixLeaveDaysMap.containsKey(CloseAtr.PRENATAL)){
			entityLeave.prenatalLeaveDays = fixLeaveDaysMap.get(CloseAtr.PRENATAL).getDays().v();
		}
		if (fixLeaveDaysMap.containsKey(CloseAtr.POSTPARTUM)){
			entityLeave.postpartumLeaveDays = fixLeaveDaysMap.get(CloseAtr.POSTPARTUM).getDays().v();
		}
		if (fixLeaveDaysMap.containsKey(CloseAtr.CHILD_CARE)){
			entityLeave.childcareLeaveDays = fixLeaveDaysMap.get(CloseAtr.CHILD_CARE).getDays().v();
		}
		if (fixLeaveDaysMap.containsKey(CloseAtr.CARE)){
			entityLeave.careLeaveDays = fixLeaveDaysMap.get(CloseAtr.CARE).getDays().v();
		}
		if (fixLeaveDaysMap.containsKey(CloseAtr.INJURY_OR_ILLNESS)){
			entityLeave.injuryOrIllnessLeaveDays = fixLeaveDaysMap.get(CloseAtr.INJURY_OR_ILLNESS).getDays().v();
		}
		val anyLeaveDaysMap = vtLeave.getAnyLeaveDays();
		if (anyLeaveDaysMap.containsKey(1)){
			entityLeave.anyLeaveDays01 = anyLeaveDaysMap.get(1).getDays().v();
		}
		if (anyLeaveDaysMap.containsKey(2)){
			entityLeave.anyLeaveDays02 = anyLeaveDaysMap.get(2).getDays().v();
		}
		if (anyLeaveDaysMap.containsKey(3)){
			entityLeave.anyLeaveDays03 = anyLeaveDaysMap.get(3).getDays().v();
		}
		if (anyLeaveDaysMap.containsKey(4)){
			entityLeave.anyLeaveDays04 = anyLeaveDaysMap.get(4).getDays().v();
		}
		
		// 縦計：勤務時間：集計加給時間
		val bonusPayTimeMap = vtWorkTime.getBonusPayTime().getBonusPayTime();
		if (entity.krcdtMonAggrBnspyTime == null) entity.krcdtMonAggrBnspyTime = new ArrayList<>();
		val entityAggrBnspyTimeList = entity.krcdtMonAggrBnspyTime;
		entityAggrBnspyTimeList.removeIf(a -> {return !bonusPayTimeMap.containsKey(a.PK.bonusPayFrameNo);} );
		for (val bonusPayTime : bonusPayTimeMap.values()){
			KrcdtMonAggrBnspyTime entityAggrBnspyTime = new KrcdtMonAggrBnspyTime();
			boolean isAddAggrBnspyTime = false;
			val entityAggrBnspyTimeOpt = entityAggrBnspyTimeList.stream()
					.filter(c -> c.PK.bonusPayFrameNo == bonusPayTime.getBonusPayFrameNo()).findFirst();
			if (entityAggrBnspyTimeOpt.isPresent()){
				entityAggrBnspyTime = entityAggrBnspyTimeOpt.get();
			}
			else {
				isAddAggrBnspyTime = true;
				entityAggrBnspyTime.PK = new KrcdtMonAggrBnspyTimePK(
						domain.getEmployeeId(),
						domain.getYearMonth().v(),
						domain.getClosureId().value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0),
						bonusPayTime.getBonusPayFrameNo());
			}
			entityAggrBnspyTime.bonusPayTime = bonusPayTime.getBonusPayTime().v();
			entityAggrBnspyTime.specificBonusPayTime = bonusPayTime.getSpecificBonusPayTime().v();
			entityAggrBnspyTime.holidayWorkBonusPayTime = bonusPayTime.getHolidayWorkBonusPayTime().v();
			entityAggrBnspyTime.holidayWorkSpecificBonusPayTime = bonusPayTime.getHolidayWorkSpecificBonusPayTime().v();
			if (isAddAggrBnspyTime) entityAggrBnspyTimeList.add(entityAggrBnspyTime);
		}
		
		// 縦計：勤務時間：集計乖離時間
		val divergenceTimeMap = vtWorkTime.getDivergenceTime().getDivergenceTimeList();
		if (entity.krcdtMonAggrDivgTime == null) entity.krcdtMonAggrDivgTime = new ArrayList<>();
		val entityAggrDivgTimeList = entity.krcdtMonAggrDivgTime;
		entityAggrDivgTimeList.removeIf(a -> {return !divergenceTimeMap.containsKey(a.PK.divergenceTimeNo);} );
		for (val divergenceTime : divergenceTimeMap.values()){
			KrcdtMonAggrDivgTime entityAggrDivgTime = new KrcdtMonAggrDivgTime();
			boolean isAddAggrDivgTime = false;
			val entityAggrDivgTimeOpt = entityAggrDivgTimeList.stream()
					.filter(c -> c.PK.divergenceTimeNo == divergenceTime.getDivergenceTimeNo()).findFirst();
			if (entityAggrDivgTimeOpt.isPresent()){
				entityAggrDivgTime = entityAggrDivgTimeOpt.get();
			}
			else {
				isAddAggrDivgTime = true;
				entityAggrDivgTime.PK = new KrcdtMonAggrDivgTimePK(
						domain.getEmployeeId(),
						domain.getYearMonth().v(),
						domain.getClosureId().value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0),
						divergenceTime.getDivergenceTimeNo());
			}
			entityAggrDivgTime.divergenceAtr = divergenceTime.getDivergenceAtr().value;
			entityAggrDivgTime.divergenceTime = divergenceTime.getDivergenceTime().v();
			entityAggrDivgTime.deductionTime = divergenceTime.getDeductionTime().v();
			entityAggrDivgTime.divergenceTimeAfterDeduction = divergenceTime.getDivergenceTimeAfterDeduction().v();
			if (isAddAggrDivgTime) entityAggrDivgTimeList.add(entityAggrDivgTime);
		}
		
		// 縦計：勤務時間：集計外出
		val goOutMap = vtWorkTime.getGoOut().getGoOuts();
		if (entity.krcdtMonAggrGoout == null) entity.krcdtMonAggrGoout = new ArrayList<>();
		val entityAggrGooutList = entity.krcdtMonAggrGoout;
		entityAggrGooutList.removeIf(
				a -> {return !goOutMap.containsKey(EnumAdaptor.valueOf(a.PK.goOutReason, GoingOutReason.class));} );
		for (val goOut : goOutMap.values()){
			KrcdtMonAggrGoout entityAggrGoout = new KrcdtMonAggrGoout();
			boolean isAddAggrGoout = false;
			val entityAggrGooutOpt = entityAggrGooutList.stream()
					.filter(c -> c.PK.goOutReason == goOut.getGoOutReason().value).findFirst();
			if (entityAggrGooutOpt.isPresent()){
				entityAggrGoout = entityAggrGooutOpt.get();
			}
			else {
				isAddAggrGoout = true;
				entityAggrGoout.PK = new KrcdtMonAggrGooutPK(
						domain.getEmployeeId(),
						domain.getYearMonth().v(),
						domain.getClosureId().value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0),
						goOut.getGoOutReason().value);
			}
			entityAggrGoout.goOutTimes = goOut.getTimes().v();
			entityAggrGoout.legalTime = goOut.getLegalTime().getTime().v();
			entityAggrGoout.calcLegalTime = goOut.getLegalTime().getCalcTime().v();
			entityAggrGoout.illegalTime = goOut.getIllegalTime().getTime().v();
			entityAggrGoout.calcIllegalTime = goOut.getIllegalTime().getCalcTime().v();
			entityAggrGoout.totalTime = goOut.getTotalTime().getTime().v();
			entityAggrGoout.calcTotalTime = goOut.getTotalTime().getCalcTime().v();
			if (isAddAggrGoout) entityAggrGooutList.add(entityAggrGoout);
		}
		
		// 縦計：勤務時間：集計割増時間
		val premiumTimeMap = vtWorkTime.getPremiumTime().getPremiumTime();
		if (entity.krcdtMonAggrPremTime == null) entity.krcdtMonAggrPremTime = new ArrayList<>();
		val entityAggrPremTimeList = entity.krcdtMonAggrPremTime;
		entityAggrPremTimeList.removeIf(a -> {return !premiumTimeMap.containsKey(a.PK.premiumTimeItemNo);} );
		for (val premiumTime : premiumTimeMap.values()){
			KrcdtMonAggrPremTime entityAggrPremTime = new KrcdtMonAggrPremTime();
			boolean isAddAggrPremTime = false;
			val entityAggrPremTimeOpt = entityAggrPremTimeList.stream()
					.filter(c -> c.PK.premiumTimeItemNo == premiumTime.getPremiumTimeItemNo()).findFirst();
			if (entityAggrPremTimeOpt.isPresent()){
				entityAggrPremTime = entityAggrPremTimeOpt.get();
			}
			else {
				isAddAggrPremTime = true;
				entityAggrPremTime.PK = new KrcdtMonAggrPremTimePK(
						domain.getEmployeeId(),
						domain.getYearMonth().v(),
						domain.getClosureId().value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0),
						premiumTime.getPremiumTimeItemNo());
			}
			entityAggrPremTime.premiumTime = premiumTime.getTime().v();
			if (isAddAggrPremTime) entityAggrPremTimeList.add(entityAggrPremTime);
		}
		
		// 縦計：勤務時間：月別実績の医療時間
		val medicalTimeMap = vtWorkTime.getMedicalTime();
		if (entity.krcdtMonMedicalTime == null) entity.krcdtMonMedicalTime = new ArrayList<>();
		val entityMedicalTimeList = entity.krcdtMonMedicalTime;
		entityMedicalTimeList.removeIf(
				a -> {return !medicalTimeMap.containsKey(EnumAdaptor.valueOf(a.PK.dayNightAtr, WorkTimeNightShift.class));} );
		for (val medicalTime : medicalTimeMap.values()){
			KrcdtMonMedicalTime entityMedicalTime = new KrcdtMonMedicalTime();
			boolean isAddMedicalTime = false;
			val entityMedicalTimeOpt = entityMedicalTimeList.stream()
					.filter(c -> c.PK.dayNightAtr == medicalTime.getDayNightAtr().value).findFirst();
			if (entityMedicalTimeOpt.isPresent()){
				entityMedicalTime = entityMedicalTimeOpt.get();
			}
			else {
				isAddMedicalTime = true;
				entityMedicalTime.PK = new KrcdtMonMedicalTimePK(
						domain.getEmployeeId(),
						domain.getYearMonth().v(),
						domain.getClosureId().value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0),
						medicalTime.getDayNightAtr().value);
			}
			entityMedicalTime.workTime = medicalTime.getWorkTime().v();
			entityMedicalTime.deductionTime = medicalTime.getDeducationTime().v();
			entityMedicalTime.takeOverTime = medicalTime.getTakeOverTime().v();
			if (isAddMedicalTime) entityMedicalTimeList.add(entityMedicalTime);
		}
		
		// 登録が必要な時、登録を実行
		if (isNeedPersist) this.getEntityManager().persist(entity);
	}
	
	/** 削除 */
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate) {
		
		this.commandProxy().remove(KrcdtMonAttendanceTime.class,
				new KrcdtMonAttendanceTimePK(
						employeeId,
						yearMonth.v(),
						closureId.value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0)));
	}

	/** 削除　（年月） */
	@Override
	public void removeByYearMonth(String employeeId, YearMonth yearMonth) {
		
		this.getEntityManager().createQuery(DELETE_BY_YEAR_MONTH)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.executeUpdate();
	}
}
