package nts.uk.ctx.at.record.infra.entity.monthly.mergetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.affiliationinformation.primitivevalue.ClassificationCode;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimesMonth;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculation;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculationAndMinus;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AggregateAffiliationInfo;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfMonthly;
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
import nts.uk.ctx.at.record.dom.monthly.mergetable.AggregateAbsenceDaysMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.AggregateBonusPayTimeMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.AggregateDivergenceTimeMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.AggregateGoOutMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.AggregateHolidayWorkTimeMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.AggregateOverTimeMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.AggregatePremiumTimeMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.AggregateSpecificDaysMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.ExcessOutsideWorkMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.ExcessOutsideWorkOfMonthlyMerge;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.VerticalTotalOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.WorkClockOfMonthly;
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
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.hdwkandcompleave.KrcdtMonAggrHdwkTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.hdwkandcompleave.KrcdtMonHdwkTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.overtime.KrcdtMonAggrOverTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.overtime.KrcdtMonOverTime;
import nts.uk.ctx.at.record.infra.entity.monthly.calc.totalworkingtime.vacationusetime.KrcdtMonVactUseTime;
import nts.uk.ctx.at.record.infra.entity.monthly.excessoutside.KrcdtMonExcoutTime;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.workclock.KrcdtMonWorkClock;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.workdays.KrcdtMonAggrAbsnDays;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.workdays.KrcdtMonAggrSpecDays;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.workdays.KrcdtMonLeave;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrBnspyTime;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrDivgTime;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrGoout;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonAggrPremTime;
import nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime.KrcdtMonMedicalTime;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.JobTitleId;
import nts.uk.ctx.at.shared.dom.shortworktime.ChildCareAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.predset.WorkTimeNightShift;
import nts.uk.ctx.at.shared.dom.worktype.CloseAtr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 残数系以外
 * 
 * @author lanlt
 *
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_MON_MERGE")
public class KrcdtMonMerge extends UkJpaEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtMonMergePk krcdtMonMergePk;

	/** KRCDT_MON_AGGR_ABSN_DAYS 30 **/

	/** 欠勤時間 */

	@Column(name = "ABSENCE_TIME_NO_1")
	public double absenceTimeNo1;

	@Column(name = "ABSENCE_TIME_NO_2")
	public double absenceTimeNo2;

	@Column(name = "ABSENCE_TIME_NO_3")
	public double absenceTimeNo3;

	@Column(name = "ABSENCE_TIME_NO_4")
	public double absenceTimeNo4;

	@Column(name = "ABSENCE_TIME_NO_5")
	public double absenceTimeNo5;

	@Column(name = "ABSENCE_TIME_NO_6")
	public double absenceTimeNo6;

	@Column(name = "ABSENCE_TIME_NO_7")
	public double absenceTimeNo7;

	@Column(name = "ABSENCE_TIME_NO_8")
	public double absenceTimeNo8;

	@Column(name = "ABSENCE_TIME_NO_9")
	public double absenceTimeNo9;

	@Column(name = "ABSENCE_TIME_NO_10")
	public double absenceTimeNo10;

	@Column(name = "ABSENCE_TIME_NO_11")
	public double absenceTimeNo11;

	@Column(name = "ABSENCE_TIME_NO_12")
	public double absenceTimeNo12;

	@Column(name = "ABSENCE_TIME_NO_13")
	public double absenceTimeNo13;

	@Column(name = "ABSENCE_TIME_NO_14")
	public double absenceTimeNo14;

	@Column(name = "ABSENCE_TIME_NO_15")
	public double absenceTimeNo15;

	@Column(name = "ABSENCE_TIME_NO_16")
	public double absenceTimeNo16;

	@Column(name = "ABSENCE_TIME_NO_17")
	public double absenceTimeNo17;

	@Column(name = "ABSENCE_TIME_NO_18")
	public double absenceTimeNo18;

	@Column(name = "ABSENCE_TIME_NO_19")
	public double absenceTimeNo19;

	@Column(name = "ABSENCE_TIME_NO_20")
	public double absenceTimeNo20;

	@Column(name = "ABSENCE_TIME_NO_21")
	public double absenceTimeNo21;

	@Column(name = "ABSENCE_TIME_NO_22")
	public double absenceTimeNo22;

	@Column(name = "ABSENCE_TIME_NO_23")
	public double absenceTimeNo23;

	@Column(name = "ABSENCE_TIME_NO_24")
	public double absenceTimeNo24;

	@Column(name = "ABSENCE_TIME_NO_25")
	public double absenceTimeNo25;

	@Column(name = "ABSENCE_TIME_NO_26")
	public double absenceTimeNo26;

	@Column(name = "ABSENCE_TIME_NO_27")
	public double absenceTimeNo27;

	@Column(name = "ABSENCE_TIME_NO_28")
	public double absenceTimeNo28;

	@Column(name = "ABSENCE_TIME_NO_29")
	public double absenceTimeNo29;

	@Column(name = "ABSENCE_TIME_NO_30")
	public double absenceTimeNo30;

	/** 欠勤日数 */

	@Column(name = "ABSENCE_DAYS_NO_1")
	public int absenceDayNo1;

	@Column(name = "ABSENCE_DAYS_NO_2")
	public int absenceDayNo2;

	@Column(name = "ABSENCE_DAYS_NO_3")
	public int absenceDayNo3;

	@Column(name = "ABSENCE_DAYS_NO_4")
	public int absenceDayNo4;

	@Column(name = "ABSENCE_DAYS_NO_5")
	public int absenceDayNo5;

	@Column(name = "ABSENCE_DAYS_NO_6")
	public int absenceDayNo6;

	@Column(name = "ABSENCE_DAYS_NO_7")
	public int absenceDayNo7;

	@Column(name = "ABSENCE_DAYS_NO_8")
	public int absenceDayNo8;

	@Column(name = "ABSENCE_DAYS_NO_9")
	public int absenceDayNo9;

	@Column(name = "ABSENCE_DAYS_NO_10")
	public int absenceDayNo10;

	@Column(name = "ABSENCE_DAYS_NO_11")
	public int absenceDayNo11;

	@Column(name = "ABSENCE_DAYS_NO_12")
	public int absenceDayNo12;

	@Column(name = "ABSENCE_DAYS_NO_13")
	public int absenceDayNo13;

	@Column(name = "ABSENCE_DAYS_NO_14")
	public int absenceDayNo14;

	@Column(name = "ABSENCE_DAYS_NO_15")
	public int absenceDayNo15;

	@Column(name = "ABSENCE_DAYS_NO_16")
	public int absenceDayNo16;

	@Column(name = "ABSENCE_DAYS_NO_17")
	public int absenceDayNo17;

	@Column(name = "ABSENCE_DAYS_NO_18")
	public int absenceDayNo18;

	@Column(name = "ABSENCE_DAYS_NO_19")
	public int absenceDayNo19;

	@Column(name = "ABSENCE_DAYS_NO_20")
	public int absenceDayNo20;

	@Column(name = "ABSENCE_DAYS_NO_21")
	public int absenceDayNo21;

	@Column(name = "ABSENCE_DAYS_NO_22")
	public int absenceDayNo22;

	@Column(name = "ABSENCE_DAYS_NO_23")
	public int absenceDayNo23;

	@Column(name = "ABSENCE_DAYS_NO_24")
	public int absenceDayNo24;

	@Column(name = "ABSENCE_DAYS_NO_25")
	public int absenceDayNo25;

	@Column(name = "ABSENCE_DAYS_NO_26")
	public int absenceDayNo26;

	@Column(name = "ABSENCE_DAYS_NO_27")
	public int absenceDayNo27;

	@Column(name = "ABSENCE_DAYS_NO_28")
	public int absenceDayNo28;

	@Column(name = "ABSENCE_DAYS_NO_29")
	public int absenceDayNo29;

	@Column(name = "ABSENCE_DAYS_NO_30")
	public int absenceDayNo30;

	/** KRCDT_MON_AGGR_BNSPY_TIME 10 **/

	/** 加給時間 */
	@Column(name = "BONUS_PAY_TIME_1")
	public int bonusPayTime1;

	@Column(name = "BONUS_PAY_TIME_2")
	public int bonusPayTime2;

	@Column(name = "BONUS_PAY_TIME_3")
	public int bonusPayTime3;

	@Column(name = "BONUS_PAY_TIME_4")
	public int bonusPayTime4;

	@Column(name = "BONUS_PAY_TIME_5")
	public int bonusPayTime5;

	@Column(name = "BONUS_PAY_TIME_6")
	public int bonusPayTime6;

	@Column(name = "BONUS_PAY_TIME_7")
	public int bonusPayTime7;

	@Column(name = "BONUS_PAY_TIME_8")
	public int bonusPayTime8;

	@Column(name = "BONUS_PAY_TIME_9")
	public int bonusPayTime9;

	@Column(name = "BONUS_PAY_TIME_10")
	public int bonusPayTime10;

	/** 特定加給時間 */
	@Column(name = "SPEC_BONUS_PAY_TIME_1")
	public int specificBonusPayTime1;

	@Column(name = "SPEC_BONUS_PAY_TIME_2")
	public int specificBonusPayTime2;

	@Column(name = "SPEC_BONUS_PAY_TIME_3")
	public int specificBonusPayTime3;

	@Column(name = "SPEC_BONUS_PAY_TIME_4")
	public int specificBonusPayTime4;

	@Column(name = "SPEC_BONUS_PAY_TIME_5")
	public int specificBonusPayTime5;

	@Column(name = "SPEC_BONUS_PAY_TIME_6")
	public int specificBonusPayTime6;

	@Column(name = "SPEC_BONUS_PAY_TIME_7")
	public int specificBonusPayTime7;

	@Column(name = "SPEC_BONUS_PAY_TIME_8")
	public int specificBonusPayTime8;

	@Column(name = "SPEC_BONUS_PAY_TIME_9")
	public int specificBonusPayTime9;

	@Column(name = "SPEC_BONUS_PAY_TIME_10")
	public int specificBonusPayTime10;

	/** 休出加給時間 */
	@Column(name = "HDWK_BONUS_PAY_TIME_1")
	public int holidayWorkBonusPayTime1;

	@Column(name = "HDWK_BONUS_PAY_TIME_2")
	public int holidayWorkBonusPayTime2;

	@Column(name = "HDWK_BONUS_PAY_TIME_3")
	public int holidayWorkBonusPayTime3;

	@Column(name = "HDWK_BONUS_PAY_TIME_4")
	public int holidayWorkBonusPayTime4;

	@Column(name = "HDWK_BONUS_PAY_TIME_5")
	public int holidayWorkBonusPayTime5;

	@Column(name = "HDWK_BONUS_PAY_TIME_6")
	public int holidayWorkBonusPayTime6;

	@Column(name = "HDWK_BONUS_PAY_TIME_7")
	public int holidayWorkBonusPayTime7;

	@Column(name = "HDWK_BONUS_PAY_TIME_8")
	public int holidayWorkBonusPayTime8;

	@Column(name = "HDWK_BONUS_PAY_TIME_9")
	public int holidayWorkBonusPayTime9;

	@Column(name = "HDWK_BONUS_PAY_TIME_10")
	public int holidayWorkBonusPayTime10;

	/** 休出特定加給時間 **/
	@Column(name = "HDWK_SPEC_BNSPAY_TIME_1")
	public int holidayWorkSpecificBonusPayTime1;

	@Column(name = "HDWK_SPEC_BNSPAY_TIME_2")
	public int holidayWorkSpecificBonusPayTime2;

	@Column(name = "HDWK_SPEC_BNSPAY_TIME_3")
	public int holidayWorkSpecificBonusPayTime3;

	@Column(name = "HDWK_SPEC_BNSPAY_TIME_4")
	public int holidayWorkSpecificBonusPayTime4;

	@Column(name = "HDWK_SPEC_BNSPAY_TIME_5")
	public int holidayWorkSpecificBonusPayTime5;

	@Column(name = "HDWK_SPEC_BNSPAY_TIME_6")
	public int holidayWorkSpecificBonusPayTime6;

	@Column(name = "HDWK_SPEC_BNSPAY_TIME_7")
	public int holidayWorkSpecificBonusPayTime7;

	@Column(name = "HDWK_SPEC_BNSPAY_TIME_8")
	public int holidayWorkSpecificBonusPayTime8;

	@Column(name = "HDWK_SPEC_BNSPAY_TIME_9")
	public int holidayWorkSpecificBonusPayTime9;

	@Column(name = "HDWK_SPEC_BNSPAY_TIME_10")
	public int holidayWorkSpecificBonusPayTime10;

	/** KRCDT_MON_AGGR_DIVG_TIME 10 **/

	/** 乖離フラグ - DIVERGENCE_ATR */
	@Column(name = "DIVERGENCE_ATR_1")
	public int divergenceAtr1;

	@Column(name = "DIVERGENCE_ATR_2")
	public int divergenceAtr2;

	@Column(name = "DIVERGENCE_ATR_3")
	public int divergenceAtr3;

	@Column(name = "DIVERGENCE_ATR_4")
	public int divergenceAtr4;

	@Column(name = "DIVERGENCE_ATR_5")
	public int divergenceAtr5;

	@Column(name = "DIVERGENCE_ATR_6")
	public int divergenceAtr6;

	@Column(name = "BONUS_PAY_TIME_7")
	public int divergenceAtr7;

	@Column(name = "DIVERGENCE_ATR_8")
	public int divergenceAtr8;

	@Column(name = "DIVERGENCE_ATR_9")
	public int divergenceAtr9;

	@Column(name = "DIVERGENCE_ATR_10")
	public int divergenceAtr10;

	/** 乖離時間 - DIVERGENCE_TIME */
	@Column(name = "DIVERGENCE_TIME_1")
	public int divergenceTime1;

	@Column(name = "DIVERGENCE_TIME_2")
	public int divergenceTime2;

	@Column(name = "DIVERGENCE_TIME_3")
	public int divergenceTime3;

	@Column(name = "DIVERGENCE_TIME_4")
	public int divergenceTime4;

	@Column(name = "DIVERGENCE_TIME_5")
	public int divergenceTime5;

	@Column(name = "DIVERGENCE_TIME_6")
	public int divergenceTime6;

	@Column(name = "DIVERGENCE_TIME_7")
	public int divergenceTime7;

	@Column(name = "DIVERGENCE_TIME_8")
	public int divergenceTime8;

	@Column(name = "DIVERGENCE_TIME_9")
	public int divergenceTime9;

	@Column(name = "DIVERGENCE_TIME_10")
	public int divergenceTime10;

	/** 控除時間 - DEDUCTION_TIME */
	@Column(name = "DEDUCTION_TIME_1")
	public int deductionTime1;

	@Column(name = "DEDUCTION_TIME_2")
	public int deductionTime2;

	@Column(name = "DEDUCTION_TIME_3")
	public int deductionTime3;

	@Column(name = "DEDUCTION_TIME_4")
	public int deductionTime4;

	@Column(name = "DEDUCTION_TIME_5")
	public int deductionTime5;

	@Column(name = "DEDUCTION_TIME_6")
	public int deductionTime6;

	@Column(name = "DEDUCTION_TIME_7")
	public int deductionTime7;

	@Column(name = "DEDUCTION_TIME_8")
	public int deductionTime8;

	@Column(name = "DEDUCTION_TIME_9")
	public int deductionTime9;

	@Column(name = "DEDUCTION_TIME_10")
	public int deductionTime10;

	/** 控除後乖離時間 - DVRGEN_TIME_AFT_DEDU_1 **/
	@Column(name = "DVRGEN_TIME_AFT_DEDU_1")
	public int divergenceTimeAfterDeduction1;

	@Column(name = "DVRGEN_TIME_AFT_DEDU_2")
	public int divergenceTimeAfterDeduction2;

	@Column(name = "DVRGEN_TIME_AFT_DEDU_3")
	public int divergenceTimeAfterDeduction3;

	@Column(name = "DVRGEN_TIME_AFT_DEDU_4")
	public int divergenceTimeAfterDeduction4;

	@Column(name = "DVRGEN_TIME_AFT_DEDU_5")
	public int divergenceTimeAfterDeduction5;

	@Column(name = "DVRGEN_TIME_AFT_DEDU_6")
	public int divergenceTimeAfterDeduction6;

	@Column(name = "DVRGEN_TIME_AFT_DEDU_7")
	public int divergenceTimeAfterDeduction7;

	@Column(name = "DVRGEN_TIME_AFT_DEDU_8")
	public int divergenceTimeAfterDeduction8;

	@Column(name = "DVRGEN_TIME_AFT_DEDU_9")
	public int divergenceTimeAfterDeduction9;

	@Column(name = "DVRGEN_TIME_AFT_DEDU_10")
	public int divergenceTimeAfterDeduction10;

	/* KRCDT_MON_AGGR_GOOUT 4 */

	/** 外出回数 - GOOUT_TIMES */
	@Column(name = "GOOUT_TIMES_1")
	public int goOutTimes1;

	@Column(name = "GOOUT_TIMES_2")
	public int goOutTimes2;

	@Column(name = "GOOUT_TIMES_3")
	public int goOutTimes3;

	@Column(name = "GOOUT_REASON_4")
	public int goOutTimes4;

	/** 法定内時間 - LEGAL_TIME */
	@Column(name = "LEGAL_TIME_1")
	public int legalTime1;

	@Column(name = "LEGAL_TIME_2")
	public int legalTime2;

	@Column(name = "LEGAL_TIME_3")
	public int legalTime3;

	@Column(name = "LEGAL_TIME_4")
	public int legalTime4;

	/** 計算法定内時間 - CALC_LEGAL_TIME */
	@Column(name = "CALC_LEGAL_TIME_1")
	public int calcLegalTime1;

	@Column(name = "CALC_LEGAL_TIME_2")
	public int calcLegalTime2;

	@Column(name = "CALC_LEGAL_TIME_3")
	public int calcLegalTime3;

	@Column(name = "CALC_LEGAL_TIME_4")
	public int calcLegalTime4;

	/** 法定外時間 - ILLEGAL_TIME */
	@Column(name = "ILLEGAL_TIME_1")
	public int illegalTime1;

	@Column(name = "ILLEGAL_TIME_2")
	public int illegalTime2;

	@Column(name = "ILLEGAL_TIME_3")
	public int illegalTime3;

	@Column(name = "ILLEGAL_TIME_4")
	public int illegalTime4;

	/** 計算法定外時間 - CALC_ILLEGAL_TIME */
	@Column(name = "CALC_ILLEGAL_TIME_1")
	public int calcIllegalTime1;

	@Column(name = "CALC_ILLEGAL_TIME_2")
	public int calcIllegalTime2;

	@Column(name = "CALC_ILLEGAL_TIME_3")
	public int calcIllegalTime3;

	@Column(name = "CALC_ILLEGAL_TIME_4")
	public int calcIllegalTime4;

	/** 合計時間 - TOTAL_TIME */
	@Column(name = "TOTAL_TIME_1")
	public int totalTime1;

	@Column(name = "TOTAL_TIME_2")
	public int totalTime2;

	@Column(name = "TOTAL_TIME_3")
	public int totalTime3;

	@Column(name = "TOTAL_TIME_4")
	public int totalTime4;

	/** 計算合計時間 - CALC_TOTAL_TIME */
	@Column(name = "CALC_TOTAL_TIME_1")
	public int calcTotalTime1;

	@Column(name = "CALC_TOTAL_TIME_2")
	public int calcTotalTime2;

	@Column(name = "CALC_TOTAL_TIME_3")
	public int calcTotalTime3;

	@Column(name = "CALC_TOTAL_TIME_4")
	public int calcTotalTime4;

	/* KRCDT_MON_AGGR_HDWK_TIME 10 */

	/** 休出時間 - HDWK_TIME_1 */
	@Column(name = "HDWK_TIME_1")
	public int holidayWorkTime1;

	@Column(name = "HDWK_TIME_2")
	public int holidayWorkTime2;

	@Column(name = "HDWK_TIME_3")
	public int holidayWorkTime3;

	@Column(name = "HDWK_TIME_4")
	public int holidayWorkTime4;

	@Column(name = "HDWK_TIME_5")
	public int holidayWorkTime5;

	@Column(name = "HDWK_TIME_6")
	public int holidayWorkTime6;

	@Column(name = "HDWK_TIME_7")
	public int holidayWorkTime7;

	@Column(name = "HDWK_TIME_8")
	public int holidayWorkTime8;

	@Column(name = "HDWK_TIME_9")
	public int holidayWorkTime9;

	@Column(name = "HDWK_TIME_10")
	public int holidayWorkTime10;

	/** 計算休出時間 - CALC_HDWK_TIME_1 */
	@Column(name = "CALC_HDWK_TIME_1")
	public int calcHolidayWorkTime1;

	@Column(name = "CALC_HDWK_TIME_2")
	public int calcHolidayWorkTime2;

	@Column(name = "CALC_HDWK_TIME_3")
	public int calcHolidayWorkTime3;

	@Column(name = "CALC_HDWK_TIME_4")
	public int calcHolidayWorkTime4;

	@Column(name = "CALC_HDWK_TIME_5")
	public int calcHolidayWorkTime5;

	@Column(name = "CALC_HDWK_TIME_6")
	public int calcHolidayWorkTime6;

	@Column(name = "CALC_HDWK_TIME_7")
	public int calcHolidayWorkTime7;

	@Column(name = "CALC_HDWK_TIME_8")
	public int calcHolidayWorkTime8;

	@Column(name = "CALC_HDWK_TIME_9")
	public int calcHolidayWorkTime9;

	@Column(name = "CALC_HDWK_TIME_10")
	public int calcHolidayWorkTime10;

	/** 事前休出時間 - BEFORE_HDWK_TIME_1 */
	@Column(name = "BEFORE_HDWK_TIME_1")
	public int beforeHolidayWorkTime1;

	@Column(name = "BEFORE_HDWK_TIME_2")
	public int beforeHolidayWorkTime2;

	@Column(name = "BEFORE_HDWK_TIME_3")
	public int beforeHolidayWorkTime3;

	@Column(name = "BEFORE_HDWK_TIME_4")
	public int beforeHolidayWorkTime4;

	@Column(name = "BEFORE_HDWK_TIME_5")
	public int beforeHolidayWorkTime5;

	@Column(name = "BEFORE_HDWK_TIME_6")
	public int beforeHolidayWorkTime6;

	@Column(name = "BEFORE_HDWK_TIME_7")
	public int beforeHolidayWorkTime7;

	@Column(name = "BEFORE_HDWK_TIME_8")
	public int beforeHolidayWorkTime8;

	@Column(name = "BEFORE_HDWK_TIME_9")
	public int beforeHolidayWorkTime9;

	@Column(name = "BEFORE_HDWK_TIME_10")
	public int beforeHolidayWorkTime10;

	/** 振替時間 - TRN_TIME_1 */
	@Column(name = "TRN_TIME_1")
	public int transferTime1;

	@Column(name = "TRN_TIME_2")
	public int transferTime2;

	@Column(name = "TRN_TIME_3")
	public int transferTime3;

	@Column(name = "TRN_TIME_4")
	public int transferTime4;

	@Column(name = "TRN_TIME_5")
	public int transferTime5;

	@Column(name = "TRN_TIME_6")
	public int transferTime6;

	@Column(name = "TRN_TIME_7")
	public int transferTime7;

	@Column(name = "TRN_TIME_8")
	public int transferTime8;

	@Column(name = "TRN_TIME_9")
	public int transferTime9;

	@Column(name = "TRN_TIME_10")
	public int transferTime10;

	/** 計算振替時間 - CALC_TRN_TIME_1 */
	@Column(name = "CALC_TRN_TIME_1")
	public int calcTransferTime1;

	@Column(name = "CALC_TRN_TIME_2")
	public int calcTransferTime2;

	@Column(name = "CALC_TRN_TIME_3")
	public int calcTransferTime3;

	@Column(name = "CALC_TRN_TIME_4")
	public int calcTransferTime4;

	@Column(name = "CALC_TRN_TIME_5")
	public int calcTransferTime5;

	@Column(name = "CALC_TRN_TIME_6")
	public int calcTransferTime6;

	@Column(name = "CALC_TRN_TIME_7")
	public int calcTransferTime7;

	@Column(name = "CALC_TRN_TIME_8")
	public int calcTransferTime8;

	@Column(name = "CALC_TRN_TIME_9")
	public int calcTransferTime9;

	@Column(name = "CALC_TRN_TIME_10")
	public int calcTransferTime10;

	/** 振替時間 - LEGAL_HDWK_TIME_1 */
	@Column(name = "LEGAL_HDWK_TIME_1")
	public int legalHolidayWorkTime1;

	@Column(name = "LEGAL_HDWK_TIME_2")
	public int legalHolidayWorkTime2;

	@Column(name = "LEGAL_HDWK_TIME_3")
	public int legalHolidayWorkTime3;

	@Column(name = "LEGAL_HDWK_TIME_4")
	public int legalHolidayWorkTime4;

	@Column(name = "LEGAL_HDWK_TIME_5")
	public int legalHolidayWorkTime5;

	@Column(name = "LEGAL_HDWK_TIME_6")
	public int legalHolidayWorkTime6;

	@Column(name = "LEGAL_HDWK_TIME_7")
	public int legalHolidayWorkTime7;

	@Column(name = "LEGAL_HDWK_TIME_8")
	public int legalHolidayWorkTime8;

	@Column(name = "LEGAL_HDWK_TIME_9")
	public int legalHolidayWorkTime9;

	@Column(name = "LEGAL_HDWK_TIME_10")
	public int legalHolidayWorkTime10;

	/** 法定内振替休出時間 - LEGAL_TRN_HDWK_TIME_1 */
	@Column(name = "LEGAL_TRN_HDWK_TIME_1")
	public int legalTransferHolidayWorkTime1;

	@Column(name = "LEGAL_TRN_HDWK_TIME_2")
	public int legalTransferHolidayWorkTime2;

	@Column(name = "LEGAL_TRN_HDWK_TIME_3")
	public int legalTransferHolidayWorkTime3;

	@Column(name = "LEGAL_TRN_HDWK_TIME_4")
	public int legalTransferHolidayWorkTime4;

	@Column(name = "LEGAL_TRN_HDWK_TIME_5")
	public int legalTransferHolidayWorkTime5;

	@Column(name = "LEGAL_TRN_HDWK_TIME_6")
	public int legalTransferHolidayWorkTime6;

	@Column(name = "LEGAL_TRN_HDWK_TIME_7")
	public int legalTransferHolidayWorkTime7;

	@Column(name = "LEGAL_TRN_HDWK_TIME_8")
	public int legalTransferHolidayWorkTime8;

	@Column(name = "LEGAL_TRN_HDWK_TIME_9")
	public int legalTransferHolidayWorkTime9;

	@Column(name = "LEGAL_TRN_HDWK_TIME_10")
	public int legalTransferHolidayWorkTime10;

	/** KRCDT_MON_AGGR_OVER_TIME 10 **/

	/** 残業時間 - OVER_TIME_1 */
	@Column(name = "OVER_TIME_1")
	public int overTime1;

	@Column(name = "OVER_TIME_2")
	public int overTime2;

	@Column(name = "OVER_TIME_3")
	public int overTime3;

	@Column(name = "OVER_TIME_4")
	public int overTime4;

	@Column(name = "OVER_TIME_5")
	public int overTime5;

	@Column(name = "OVER_TIME_6")
	public int overTime6;

	@Column(name = "OVER_TIME_7")
	public int overTime7;

	@Column(name = "OVER_TIME_8")
	public int overTime8;

	@Column(name = "OVER_TIME_9")
	public int overTime9;

	@Column(name = "OVER_TIME_10")
	public int overTime10;

	/** 計算残業時間 - CALC_OVER_TIME_1 */
	@Column(name = "CALC_OVER_TIME_1")
	public int calcOverTime1;

	@Column(name = "CALC_OVER_TIME_2")
	public int calcOverTime2;

	@Column(name = "CALC_OVER_TIME_3")
	public int calcOverTime3;

	@Column(name = "CALC_OVER_TIME_4")
	public int calcOverTime4;

	@Column(name = "CALC_OVER_TIME_5")
	public int calcOverTime5;

	@Column(name = "CALC_OVER_TIME_6")
	public int calcOverTime6;

	@Column(name = "CALC_OVER_TIME_7")
	public int calcOverTime7;

	@Column(name = "CALC_OVER_TIME_8")
	public int calcOverTime8;

	@Column(name = "CALC_OVER_TIME_9")
	public int calcOverTime9;

	@Column(name = "CALC_OVER_TIME_10")
	public int calcOverTime10;

	/** 事前残業時間 - BEFORE_OVER_TIME_1 */
	@Column(name = "BEFORE_OVER_TIME_1")
	public int beforeOverTime1;

	@Column(name = "BEFORE_OVER_TIME_2")
	public int beforeOverTime2;

	@Column(name = "BEFORE_OVER_TIME_3")
	public int beforeOverTime3;

	@Column(name = "BEFORE_OVER_TIME_4")
	public int beforeOverTime4;

	@Column(name = "BEFORE_OVER_TIME_5")
	public int beforeOverTime5;

	@Column(name = "BEFORE_OVER_TIME_6")
	public int beforeOverTime6;

	@Column(name = "BEFORE_OVER_TIME_7")
	public int beforeOverTime7;

	@Column(name = "BEFORE_OVER_TIME_8")
	public int beforeOverTime8;

	@Column(name = "BEFORE_OVER_TIME_9")
	public int beforeOverTime9;

	@Column(name = "BEFORE_OVER_TIME_10")
	public int beforeOverTime10;

	/** 振替残業時間 - TRNOVR_TIME_1 */
	@Column(name = "TRNOVR_TIME_1")
	public int transferOverTime1;

	@Column(name = "TRNOVR_TIME_2")
	public int transferOverTime2;

	@Column(name = "TRNOVR_TIME_3")
	public int transferOverTime3;

	@Column(name = "TRNOVR_TIME_4")
	public int transferOverTime4;

	@Column(name = "TRNOVR_TIME_5")
	public int transferOverTime5;

	@Column(name = "TRNOVR_TIME_6")
	public int transferOverTime6;

	@Column(name = "TRNOVR_TIME_7")
	public int transferOverTime7;

	@Column(name = "TRNOVR_TIME_8")
	public int transferOverTime8;

	@Column(name = "TRNOVR_TIME_9")
	public int transferOverTime9;

	@Column(name = "TRNOVR_TIME_10")
	public int transferOverTime10;

	/** 計算振替残業時間 - CALC_TRNOVR_TIME_1 */
	@Column(name = "CALC_TRNOVR_TIME_1")
	public int calcTransferOverTime1;

	@Column(name = "CALC_TRNOVR_TIME_2")
	public int calcTransferOverTime2;

	@Column(name = "CALC_TRNOVR_TIME_3")
	public int calcTransferOverTime3;

	@Column(name = "CALC_TRNOVR_TIME_4")
	public int calcTransferOverTime4;

	@Column(name = "CALC_TRNOVR_TIME_5")
	public int calcTransferOverTime5;

	@Column(name = "CALC_TRNOVR_TIME_6")
	public int calcTransferOverTime6;

	@Column(name = "CALC_TRNOVR_TIME_7")
	public int calcTransferOverTime7;

	@Column(name = "CALC_TRNOVR_TIME_8")
	public int calcTransferOverTime8;

	@Column(name = "CALC_TRNOVR_TIME_9")
	public int calcTransferOverTime9;

	@Column(name = "CALC_TRNOVR_TIME_10")
	public int calcTransferOverTime10;

	/** 法定内残業時間 -LEGAL_OVER_TIME_1 */
	@Column(name = "LEGAL_OVER_TIME_1")
	public int legalOverTime1;

	@Column(name = "LEGAL_OVER_TIME_2")
	public int legalOverTime2;

	@Column(name = "LEGAL_OVER_TIME_3")
	public int legalOverTime3;

	@Column(name = "LEGAL_OVER_TIME_4")
	public int legalOverTime4;

	@Column(name = "LEGAL_OVER_TIME_5")
	public int legalOverTime5;

	@Column(name = "LEGAL_OVER_TIME_6")
	public int legalOverTime6;

	@Column(name = "LEGAL_OVER_TIME_7")
	public int legalOverTime7;

	@Column(name = "LEGAL_OVER_TIME_8")
	public int legalOverTime8;

	@Column(name = "LEGAL_OVER_TIME_9")
	public int legalOverTime9;

	@Column(name = "LEGAL_OVER_TIME_10")
	public int legalOverTime10;

	/** 法定内振替残業時間 - LEGAL_TRNOVR_TIME_1 */
	@Column(name = "LEGAL_TRNOVR_TIME_1")
	public int legalTransferOverTime1;

	@Column(name = "LEGAL_TRNOVR_TIME_2")
	public int legalTransferOverTime2;

	@Column(name = "LEGAL_TRNOVR_TIME_3")
	public int legalTransferOverTime3;

	@Column(name = "LEGAL_TRNOVR_TIME_4")
	public int legalTransferOverTime4;

	@Column(name = "LEGAL_TRNOVR_TIME_5")
	public int legalTransferOverTime5;

	@Column(name = "LEGAL_TRNOVR_TIME_6")
	public int legalTransferOverTime6;

	@Column(name = "LEGAL_TRNOVR_TIME_7")
	public int legalTransferOverTime7;

	@Column(name = "LEGAL_TRNOVR_TIME_8")
	public int legalTransferOverTime8;

	@Column(name = "LEGAL_TRNOVR_TIME_9")
	public int legalTransferOverTime9;

	@Column(name = "LEGAL_TRNOVR_TIME_10")
	public int legalTransferOverTime10;

	/** KRCDT_MON_AGGR_PREM_TIME 10 **/

	/** 振替時間 - PREMIUM_TIME_1 */
	@Column(name = "PREMIUM_TIME_1")
	public int premiumTime1;

	@Column(name = "PREMIUM_TIME_2")
	public int premiumTime2;

	@Column(name = "PREMIUM_TIME_3")
	public int premiumTime3;

	@Column(name = "PREMIUM_TIME_4")
	public int premiumTime4;

	@Column(name = "PREMIUM_TIME_5")
	public int premiumTime5;

	@Column(name = "PREMIUM_TIME_6")
	public int premiumTime6;

	@Column(name = "PREMIUM_TIME_7")
	public int premiumTime7;

	@Column(name = "PREMIUM_TIME_8")
	public int premiumTime8;

	@Column(name = "PREMIUM_TIME_9")
	public int premiumTime9;

	@Column(name = "PREMIUM_TIME_10")
	public int premiumTime10;

	/** KRCDT_MON_AGGR_SPEC_DAYS 10 **/

	/** 特定日数 - SPECIFIC_DAYS_1 */
	@Column(name = "SPECIFIC_DAYS_1")
	public double specificDays1;

	@Column(name = "SPECIFIC_DAYS_2")
	public double specificDays2;

	@Column(name = "SPECIFIC_DAYS_3")
	public double specificDays3;

	@Column(name = "SPECIFIC_DAYS_4")
	public double specificDays4;

	@Column(name = "SPECIFIC_DAYS_5")
	public double specificDays5;

	@Column(name = "SPECIFIC_DAYS_6")
	public double specificDays6;

	@Column(name = "SPECIFIC_DAYS_7")
	public double specificDays7;

	@Column(name = "SPECIFIC_DAYS_8")
	public double specificDays8;

	@Column(name = "SPECIFIC_DAYS_9")
	public double specificDays9;

	@Column(name = "SPECIFIC_DAYS_10")
	public double specificDays10;

	/** 休出特定日数 - HDWK_SPECIFIC_DAYS_1 */
	@Column(name = "HDWK_SPECIFIC_DAYS_1")
	public double holidayWorkSpecificDays1;

	@Column(name = "HDWK_SPECIFIC_DAYS_2")
	public double holidayWorkSpecificDays2;

	@Column(name = "HDWK_SPECIFIC_DAYS_3")
	public double holidayWorkSpecificDays3;

	@Column(name = "HDWK_SPECIFIC_DAYS_4")
	public double holidayWorkSpecificDays4;

	@Column(name = "HDWK_SPECIFIC_DAYS_5")
	public double holidayWorkSpecificDays5;

	@Column(name = "HDWK_SPECIFIC_DAYS_6")
	public double holidayWorkSpecificDays6;

	@Column(name = "HDWK_SPECIFIC_DAYS_7")
	public double holidayWorkSpecificDays7;

	@Column(name = "HDWK_SPECIFIC_DAYS_8")
	public double holidayWorkSpecificDays8;

	@Column(name = "HDWK_SPECIFIC_DAYS_9")
	public double holidayWorkSpecificDays9;

	@Column(name = "HDWK_SPECIFIC_DAYS_10")
	public double holidayWorkSpecificDays10;

	/** KRCDT_MON_AGGR_TOTAL_SPT **/

	/** 拘束残業時間 */
	@Column(name = "SPENT_OVER_TIME")
	public int overTimeSpentAtWork;

	/** 拘束深夜時間 */
	@Column(name = "SPENT_MIDNIGHT_TIME")
	public int midnightTimeSpentAtWork;

	/** 拘束休出時間 */
	@Column(name = "SPENT_HOLIDAY_TIME")
	public int holidayTimeSpentAtWork;

	/** 拘束差異時間 */
	@Column(name = "SPENT_VARIENCE_TIME")
	public int varienceTimeSpentAtWork;

	/** 総拘束時間 */
	@Column(name = "TOTAL_SPENT_TIME")
	public int totalTimeSpentAtWork;

	/* KRCDT_MON_AGGR_TOTAL_WRK */

	/** 就業時間 */
	@Column(name = "WORK_TIME")
	public int workTime;

	/** 実働就業時間 */
	@Column(name = "ACTWORK_TIME")
	public int actualWorkTime;

	/** 所定内割増時間 */
	@Column(name = "WITPRS_PREMIUM_TIME")
	public int withinPrescribedPremiumTime;

	/** 計画所定労働時間 */
	@Column(name = "SCHE_PRS_WORK_TIME")
	public int schedulePrescribedWorkingTime;

	/** 実績所定労働時間 */
	@Column(name = "RECD_PRS_WORK_TIME")
	public int recordPrescribedWorkingTime;

	/* KRCDT_MON_ATTENDANCE_TIME */

	/** 開始年月日 */
	@Column(name = "START_YMD")
	public GeneralDate startYmd;

	/** 終了年月日 */
	@Column(name = "END_YMD")
	public GeneralDate endYmd;

	/** 集計日数 */
	@Column(name = "AGGREGATE_DAYS")
	public double aggregateDays;

	/** 法定労働時間 */
	@Column(name = "STAT_WORKING_TIME")
	public int statutoryWorkingTime;

	/** 総労働時間 */
	@Column(name = "TOTAL_WORKING_TIME")
	public int totalWorkingTime;

	/* KRCDT_MON_FLEX_TIME */

	/** フレックス時間 */
	@Column(name = "FLEX_TIME")
	public int flexTime;

	/** 計算フレックス時間 */
	@Column(name = "CALC_FLEX_TIME")
	public int calcFlexTime;

	/** 事前フレックス時間 */
	@Column(name = "BEFORE_FLEX_TIME")
	public int beforeFlexTime;

	/** 法定内フレックス時間 */
	@Column(name = "LEGAL_FLEX_TIME")
	public int legalFlexTime;

	/** 法定外フレックス時間 */
	@Column(name = "ILLEGAL_FLEX_TIME")
	public int illegalFlexTime;

	/** フレックス超過時間 */
	@Column(name = "FLEX_EXCESS_TIME")
	public int flexExcessTime;

	/** フレックス不足時間 */
	@Column(name = "FLEX_SHORTAGE_TIME")
	public int flexShortageTime;

	/** フレックス繰越時間 */
	@Column(name = "FLEX_CRYFWD_TIME")
	public int flexCarryforwardTime;

	/** フレックス繰越勤務時間 */
	@Column(name = "FLEX_CRYFWD_WORK_TIME")
	public int flexCarryforwardWorkTime;

	/** フレックス繰越不足時間 */
	@Column(name = "FLEX_CRYFWD_SHT_TIME")
	public int flexCarryforwardShortageTime;

	/** 超過フレ区分 */
	@Column(name = "EXCESS_FLEX_ATR")
	public int excessFlexAtr;

	/** 原則時間 */
	@Column(name = "PRINCIPLE_TIME")
	public int principleTime;

	/** 便宜上時間 */
	@Column(name = "CONVENIENCE_TIME")
	public int forConvenienceTime;

	/** 年休控除日数 */
	@Column(name = "ANNUAL_DEDUCT_DAYS")
	public double annualLeaveDeductDays;

	/** 欠勤控除時間 */
	@Column(name = "ABSENCE_DEDUCT_TIME")
	public int absenceDeductTime;

	/** 控除前のフレックス不足時間 */
	@Column(name = "SHORT_TIME_BFR_DEDUCT")
	public int shotTimeBeforeDeduct;

	/* KRCDT_MON_HDWK_TIME */

	/** 休出合計時間 */
	@Column(name = "TOTAL_HDWK_TIME")
	public int totalHolidayWorkTime;

	/** 計算休出合計時間 */
	@Column(name = "CALC_TOTAL_HDWK_TIME")
	public int calcTotalHolidayWorkTime;

	/** 事前休出時間 */
	@Column(name = "BEFORE_HDWK_TIME")
	public int beforeHolidayWorkTime;

	/** 振替合計時間 */
	@Column(name = "TOTAL_TRN_TIME")
	public int totalTransferTime;

	/** 計算振替合計時間 */
	@Column(name = "CALC_TOTAL_TRN_TIME")
	public int calcTotalTransferTime;

	/* KRCDT_MON_LEAVE */

	/** 産前休業日数 */
	@Column(name = "PRENATAL_LEAVE_DAYS")
	public double prenatalLeaveDays;

	/** 産後休業日数 */
	@Column(name = "POSTPARTUM_LEAVE_DAYS")
	public double postpartumLeaveDays;

	/** 育児休業日数 */
	@Column(name = "CHILDCARE_LEAVE_DAYS")
	public double childcareLeaveDays;

	/** 介護休業日数 */
	@Column(name = "CARE_LEAVE_DAYS")
	public double careLeaveDays;

	/** 傷病休業日数 */
	@Column(name = "INJILN_LEAVE_DAYS")
	public double injuryOrIllnessLeaveDays;

	/** 任意休業日数01 */
	@Column(name = "ANY_LEAVE_DAYS_01")
	public double anyLeaveDays01;

	/** 任意休業日数02 */
	@Column(name = "ANY_LEAVE_DAYS_02")
	public double anyLeaveDays02;

	/** 任意休業日数03 */
	@Column(name = "ANY_LEAVE_DAYS_03")
	public double anyLeaveDays03;

	/** 任意休業日数04 */
	@Column(name = "ANY_LEAVE_DAYS_04")
	public double anyLeaveDays04;

	/* KRCDT_MON_MEDICAL_TIME */

	/** 日勤夜勤区分 */
	@Column(name = "DAY_NIGHT_ATR")
	public int dayNightAtr;

	/** 勤務時間 */
	@Column(name = "WORK__MEDICAL_TIME")
	public int workMedicalTime;

	/** 控除時間 */
	@Column(name = "DEDUCTION_TIME")
	public int deductionTime;

	/** 申送時間 */
	@Column(name = "TAKE_OVER_TIME")
	public int takeOverTime;

	/* KRCDT_MON_OVER_TIME */

	/** 残業合計時間 */
	@Column(name = "TOTAL_OVER_TIME")
	public int totalOverTime;

	/** 計算残業合計時間 */
	@Column(name = "CALC_TOTAL_OVER_TIME")
	public int calcTotalOverTime;

	/** 事前残業時間 */
	@Column(name = "BEFORE_OVER_TIME")
	public int beforeOverTime;

	/** 振替残業合計時間 */
	@Column(name = "TOTAL_TRNOVR_TIME")
	public int totalTransferOverTime;

	/** 計算振替残業合計時間 */
	@Column(name = "CALC_TOTAL_TRNOVR_TIME")
	public int calcTotalTransferOverTime;

	/* KRCDT_MON_REG_IRREG_TIME */

	/** 週割増合計時間 */
	@Column(name = "WEEK_TOTAL_PREM_TIME")
	public int weeklyTotalPremiumTime;

	/** 月割増合計時間 */
	@Column(name = "MON_TOTAL_PREM_TIME")
	public int monthlyTotalPremiumTime;

	/** 複数月変形途中時間 */
	@Column(name = "MULTI_MON_IRGMDL_TIME")
	public int multiMonthIrregularMiddleTime;

	/** 変形期間繰越時間 */
	@Column(name = "IRGPERIOD_CRYFWD_TIME")
	public int irregularPeriodCarryforwardTime;

	/** 変形労働不足時間 */
	@Column(name = "IRG_SHORTAGE_TIME")
	public int irregularWorkingShortageTime;

	/** 変形法定内残業時間 */
	@Column(name = "IRG_LEGAL_OVER_TIME")
	public int irregularLegalOverTime;

	/** 計算変形法定内残業時間 */
	@Column(name = "CALC_IRG_LGL_OVER_TIME")
	public int calcIrregularLegalOverTime;

	/* KRCDT_MON_VACT_USE_TIME */

	/** 年休使用時間 */
	@Column(name = "ANLLEV_USE_TIME")
	public int annualLeaveUseTime;

	/** 積立年休使用時間 */
	@Column(name = "RETYEA_USE_TIME")
	public int retentionYearlyUseTime;

	/** 特別休暇使用時間 */
	@Column(name = "SPHD_USE_TIME")
	public int specialHolidayUseTime;

	/** 代休使用時間 */
	@Column(name = "CMPLEV_USE_TIME")
	public int compensatoryLeaveUseTime;

	/* KRCDT_MON_VERTICAL_TOTAL */

	/** 勤務日数 */
	@Column(name = "WORK_DAYS")
	public double workDays;

	/** 勤務回数 */
	@Column(name = "WORK_TIMES")
	public int workTimes;

	/** 二回勤務回数 */
	@Column(name = "TWOTIMES_WORK_TIMES")
	public int twoTimesWorkTimes;

	/** 臨時勤務回数 */
	@Column(name = "TEMPORARY_WORK_TIMES")
	public int temporaryWorkTimes;

	/** 所定日数 */
	@Column(name = "PREDET_DAYS")
	public double predetermineDays;

	/** 休日日数 */
	@Column(name = "HOLIDAY_DAYS")
	public double holidayDays;

	/** 出勤日数 */
	@Column(name = "ATTENDANCE_DAYS")
	public double attendanceDays;

	/** 休出日数 */
	@Column(name = "HOLIDAY_WORK_DAYS")
	public double holidayWorkDays;

	/** 欠勤合計日数 */
	@Column(name = "TOTAL_ABSENCE_DAYS")
	public double totalAbsenceDays;

	/** 欠勤合計時間 */
	@Column(name = "TOTAL_ABSENCE_TIME")
	public int totalAbsenceTime;

	/** 給与出勤日数 */
	@Column(name = "PAY_ATTENDANCE_DAYS")
	public double payAttendanceDays;

	/** 給与欠勤日数 */
	@Column(name = "PAY_ABSENCE_DAYS")
	public double payAbsenceDays;

	/** 育児外出回数 */
	@Column(name = "CLDCAR_GOOUT_TIMES")
	public int childcareGoOutTimes;

	/** 育児外出時間 */
	@Column(name = "CLDCAR_GOOUT_TIME")
	public int childcareGoOutTime;

	/** 介護外出回数 */
	@Column(name = "CARE_GOOUT_TIMES")
	public int careGoOutTimes;

	/** 介護外出時間 */
	@Column(name = "CARE_GOOUT_TIME")
	public int careGoOutTime;

	/** 割増深夜時間 */
	@Column(name = "PREM_MIDNIGHT_TIME")
	public int premiumMidnightTime;

	/** 割増法定内時間外時間 */
	@Column(name = "PREM_LGL_OUTWRK_TIME")
	public int premiumLegalOutsideWorkTime;

	/** 割増法定外時間外時間 */
	@Column(name = "PREM_ILG_OUTWRK_TIME")
	public int premiumIllegalOutsideWorkTime;

	/** 割増法定内休出時間 */
	@Column(name = "PREM_LGL_HDWK_TIME")
	public int premiumLegalHolidayWorkTime;

	/** 割増法定外休出時間 */
	@Column(name = "PREM_ILG_HDWK_TIME")
	public int premiumIllegalHolidayWorkTime;

	/** 休憩時間 */
	@Column(name = "BREAK_TIME")
	public int breakTime;

	/** 法定内休日時間 */
	@Column(name = "LEGAL_HOLIDAY_TIME")
	public int legalHolidayTime;

	/** 法定外休日時間 */
	@Column(name = "ILLEGAL_HOLIDAY_TIME")
	public int illegalHolidayTime;

	/** 法定外祝日休日時間 */
	@Column(name = "ILLEGAL_SPCHLD_TIME")
	public int illegalSpecialHolidayTime;

	/** 残業深夜時間 */
	@Column(name = "OVWK_MDNT_TIME")
	public int overWorkMidnightTime;

	/** 計算残業深夜時間 */
	@Column(name = "CALC_OVWK_MDNT_TIME")
	public int calcOverWorkMidnightTime;

	/** 法定内深夜時間 */
	@Column(name = "LGL_MDNT_TIME")
	public int legalMidnightTime;

	/** 計算法定内深夜時間 */
	@Column(name = "CALC_LGL_MDNT_TIME")
	public int calcLegalMidnightTime;

	/** 法定外深夜時間 */
	@Column(name = "ILG_MDNT_TIME")
	public int illegalMidnightTime;

	/** 計算法定外深夜時間 */
	@Column(name = "CALC_ILG_MDNT_TIME")
	public int calcIllegalMidnightTime;

	/** 法定外事前深夜時間 */
	@Column(name = "ILG_BFR_MDNT_TIME")
	public int illegalBeforeMidnightTime;

	/** 法定内休出深夜時間 */
	@Column(name = "LGL_HDWK_MDNT_TIME")
	public int legalHolidayWorkMidnightTime;

	/** 計算法定内休出深夜時間 */
	@Column(name = "CALC_LGL_HDWK_MDNT_TIME")
	public int calcLegalHolidayWorkMidnightTime;

	/** 法定外休出深夜時間 */
	@Column(name = "ILG_HDWK_MDNT_TIME")
	public int illegalHolidayWorkMidnightTime;

	/** 計算法定外休出深夜時間 */
	@Column(name = "CALC_ILG_HDWK_MDNT_TIME")
	public int calcIllegalHolidayWorkMidnightTime;

	/** 祝日休出深夜時間 */
	@Column(name = "SPHD_HDWK_MDNT_TIME")
	public int specialHolidayWorkMidnightTime;

	/** 計算祝日休出深夜時間 */
	@Column(name = "CALC_SPHD_HDWK_MDNT_TIME")
	public int calcSpecialHolidayWorkMidnightTime;

	/** 遅刻回数 */
	@Column(name = "LATE_TIMES")
	public int lateTimes;

	/** 遅刻時間 */
	@Column(name = "LATE_TIME")
	public int lateTime;

	/** 計算遅刻時間 */
	@Column(name = "CALC_LATE_TIME")
	public int calcLateTime;

	/** 早退回数 */
	@Column(name = "LEAVEEARLY_TIMES")
	public int leaveEarlyTimes;

	/** 早退時間 */
	@Column(name = "LEAVEEARLY_TIME")
	public int leaveEarlyTime;

	/** 計算早退時間 */
	@Column(name = "CALC_LEAVEEARLY_TIME")
	public int calcLeaveEarlyTime;

	/** 入退門出勤前時間 */
	@Column(name = "ALGT_BFR_ATND_TIME")
	public int attendanceLeaveGateBeforeAttendanceTime;

	/** 入退門退勤後時間 */
	@Column(name = "ALGT_AFT_LVWK_TIME")
	public int attendanceLeaveGateAfterLeaveWorkTime;

	/** 入退門滞在時間 */
	@Column(name = "ALGT_STAYING_TIME")
	public int attendanceLeaveGateStayingTime;

	/** 入退門不就労時間 */
	@Column(name = "ALGT_UNEMPLOYED_TIME")
	public int attendanceLeaveGateUnemployedTime;

	/** 予実差異時間 */
	@Column(name = "BUDGET_VARIENCE_TIME")
	public int budgetVarienceTime;

	/* KRCDT_MON_EXCESS_OUTSIDE 50 */

	/** 週割増合計時間 */
	@Column(name = "TOTAL_WEEK_PRM_TIME_1")
	public int totalWeeklyPremiumTime1;

	@Column(name = "TOTAL_WEEK_PRM_TIME_2")
	public int totalWeeklyPremiumTime2;

	@Column(name = "TOTAL_WEEK_PRM_TIME_3")
	public int totalWeeklyPremiumTime3;

	@Column(name = "TOTAL_WEEK_PRM_TIME_4")
	public int totalWeeklyPremiumTime4;

	@Column(name = "TOTAL_WEEK_PRM_TIME_5")
	public int totalWeeklyPremiumTime5;

	@Column(name = "TOTAL_WEEK_PRM_TIME_6")
	public int totalWeeklyPremiumTime6;

	@Column(name = "TOTAL_WEEK_PRM_TIME_7")
	public int totalWeeklyPremiumTime7;

	@Column(name = "TOTAL_WEEK_PRM_TIME_8")
	public int totalWeeklyPremiumTime8;

	@Column(name = "TOTAL_WEEK_PRM_TIME_9")
	public int totalWeeklyPremiumTime9;

	@Column(name = "TOTAL_WEEK_PRM_TIME_10")
	public int totalWeeklyPremiumTime10;

	@Column(name = "TOTAL_WEEK_PRM_TIME_11")
	public int totalWeeklyPremiumTime11;

	@Column(name = "TOTAL_WEEK_PRM_TIME_12")
	public int totalWeeklyPremiumTime12;

	@Column(name = "TOTAL_WEEK_PRM_TIME_13")
	public int totalWeeklyPremiumTime13;

	@Column(name = "TOTAL_WEEK_PRM_TIME_14")
	public int totalWeeklyPremiumTime14;

	@Column(name = "TOTAL_WEEK_PRM_TIME_15")
	public int totalWeeklyPremiumTime15;

	@Column(name = "TOTAL_WEEK_PRM_TIME_16")
	public int totalWeeklyPremiumTime16;

	@Column(name = "TOTAL_WEEK_PRM_TIME_17")
	public int totalWeeklyPremiumTime17;

	@Column(name = "TOTAL_WEEK_PRM_TIME_18")
	public int totalWeeklyPremiumTime18;

	@Column(name = "TOTAL_WEEK_PRM_TIME_19")
	public int totalWeeklyPremiumTime19;

	@Column(name = "TOTAL_WEEK_PRM_TIME_20")
	public int totalWeeklyPremiumTime20;

	@Column(name = "TOTAL_WEEK_PRM_TIME_21")
	public int totalWeeklyPremiumTime21;

	@Column(name = "TOTAL_WEEK_PRM_TIME_22")
	public int totalWeeklyPremiumTime22;

	@Column(name = "TOTAL_WEEK_PRM_TIME_23")
	public int totalWeeklyPremiumTime23;

	@Column(name = "TOTAL_WEEK_PRM_TIME_24")
	public int totalWeeklyPremiumTime24;

	@Column(name = "TOTAL_WEEK_PRM_TIME_25")
	public int totalWeeklyPremiumTime25;

	@Column(name = "TOTAL_WEEK_PRM_TIME_26")
	public int totalWeeklyPremiumTime26;

	@Column(name = "TOTAL_WEEK_PRM_TIME_27")
	public int totalWeeklyPremiumTime27;

	@Column(name = "TOTAL_WEEK_PRM_TIME_28")
	public int totalWeeklyPremiumTime28;

	@Column(name = "TOTAL_WEEK_PRM_TIME_29")
	public int totalWeeklyPremiumTime29;

	@Column(name = "TOTAL_WEEK_PRM_TIME_30")
	public int totalWeeklyPremiumTime30;

	@Column(name = "TOTAL_WEEK_PRM_TIME_31")
	public int totalWeeklyPremiumTime31;

	@Column(name = "TOTAL_WEEK_PRM_TIME_32")
	public int totalWeeklyPremiumTime32;

	@Column(name = "TOTAL_WEEK_PRM_TIME_33")
	public int totalWeeklyPremiumTime33;

	@Column(name = "TOTAL_WEEK_PRM_TIME_34")
	public int totalWeeklyPremiumTime34;

	@Column(name = "TOTAL_WEEK_PRM_TIME_35")
	public int totalWeeklyPremiumTime35;

	@Column(name = "TOTAL_WEEK_PRM_TIME_36")
	public int totalWeeklyPremiumTime36;

	@Column(name = "TOTAL_WEEK_PRM_TIME_37")
	public int totalWeeklyPremiumTime37;

	@Column(name = "TOTAL_WEEK_PRM_TIME_38")
	public int totalWeeklyPremiumTime38;

	@Column(name = "TOTAL_WEEK_PRM_TIME_39")
	public int totalWeeklyPremiumTime39;

	@Column(name = "TOTAL_WEEK_PRM_TIME_40")
	public int totalWeeklyPremiumTime40;

	@Column(name = "TOTAL_WEEK_PRM_TIME_41")
	public int totalWeeklyPremiumTime41;

	@Column(name = "TOTAL_WEEK_PRM_TIME_4")
	public int totalWeeklyPremiumTime42;

	@Column(name = "TOTAL_WEEK_PRM_TIME_43")
	public int totalWeeklyPremiumTime43;

	@Column(name = "TOTAL_WEEK_PRM_TIME_44")
	public int totalWeeklyPremiumTime44;

	@Column(name = "TOTAL_WEEK_PRM_TIME_45")
	public int totalWeeklyPremiumTime45;

	@Column(name = "TOTAL_WEEK_PRM_TIME_46")
	public int totalWeeklyPremiumTime46;

	@Column(name = "TOTAL_WEEK_PRM_TIME_47")
	public int totalWeeklyPremiumTime47;

	@Column(name = "TOTAL_WEEK_PRM_TIME_48")
	public int totalWeeklyPremiumTime48;

	@Column(name = "TOTAL_WEEK_PRM_TIME_49")
	public int totalWeeklyPremiumTime49;

	@Column(name = "TOTAL_WEEK_PRM_TIME_50")
	public int totalWeeklyPremiumTime50;

	/** 月割増合計時間 */
	@Column(name = "TOTAL_MONTH_PRM_TIME_1")
	public int totalMonthlyPremiumTime1;

	@Column(name = "TOTAL_MONTH_PRM_TIME_2")
	public int totalMonthlyPremiumTime2;

	@Column(name = "TOTAL_MONTH_PRM_TIME_3")
	public int totalMonthlyPremiumTime3;

	@Column(name = "TOTAL_MONTH_PRM_TIME_4")
	public int totalMonthlyPremiumTime4;

	@Column(name = "TOTAL_MONTH_PRM_TIME_5")
	public int totalMonthlyPremiumTime5;

	@Column(name = "TOTAL_MONTH_PRM_TIME_6")
	public int totalMonthlyPremiumTime6;

	@Column(name = "TOTAL_MONTH_PRM_TIME_7")
	public int totalMonthlyPremiumTime7;

	@Column(name = "TOTAL_MONTH_PRM_TIME_8")
	public int totalMonthlyPremiumTime8;

	@Column(name = "TOTAL_MONTH_PRM_TIME_9")
	public int totalMonthlyPremiumTime9;

	@Column(name = "TOTAL_MONTH_PRM_TIME_10")
	public int totalMonthlyPremiumTime10;

	@Column(name = "TOTAL_MONTH_PRM_TIME_11")
	public int totalMonthlyPremiumTime11;

	@Column(name = "TOTAL_MONTH_PRM_TIME_12")
	public int totalMonthlyPremiumTime12;

	@Column(name = "TOTAL_MONTH_PRM_TIME_13")
	public int totalMonthlyPremiumTime13;

	@Column(name = "TOTAL_MONTH_PRM_TIME_14")
	public int totalMonthlyPremiumTime14;

	@Column(name = "TOTAL_MONTH_PRM_TIME_15")
	public int totalMonthlyPremiumTime15;

	@Column(name = "TOTAL_MONTH_PRM_TIME_16")
	public int totalMonthlyPremiumTime16;

	@Column(name = "TOTAL_MONTH_PRM_TIME_17")
	public int totalMonthlyPremiumTime17;

	@Column(name = "TOTAL_MONTH_PRM_TIME_18")
	public int totalMonthlyPremiumTime18;

	@Column(name = "TOTAL_MONTH_PRM_TIME_19")
	public int totalMonthlyPremiumTime19;

	@Column(name = "TOTAL_MONTH_PRM_TIME_20")
	public int totalMonthlyPremiumTime20;

	@Column(name = "TOTAL_MONTH_PRM_TIME_21")
	public int totalMonthlyPremiumTime21;

	@Column(name = "TOTAL_MONTH_PRM_TIME_22")
	public int totalMonthlyPremiumTime22;

	@Column(name = "TOTAL_MONTH_PRM_TIME_23")
	public int totalMonthlyPremiumTime23;

	@Column(name = "TOTAL_MONTH_PRM_TIME_24")
	public int totalMonthlyPremiumTime24;

	@Column(name = "TOTAL_MONTH_PRM_TIME_25")
	public int totalMonthlyPremiumTime25;

	@Column(name = "TOTAL_MONTH_PRM_TIME_26")
	public int totalMonthlyPremiumTime26;

	@Column(name = "TOTAL_MONTH_PRM_TIME_27")
	public int totalMonthlyPremiumTime27;

	@Column(name = "TOTAL_MONTH_PRM_TIME_28")
	public int totalMonthlyPremiumTime28;

	@Column(name = "TOTAL_MONTH_PRM_TIME_29")
	public int totalMonthlyPremiumTime29;

	@Column(name = "TOTAL_MONTH_PRM_TIME_30")
	public int totalMonthlyPremiumTime30;

	@Column(name = "TOTAL_MONTH_PRM_TIME_31")
	public int totalMonthlyPremiumTime31;

	@Column(name = "TOTAL_MONTH_PRM_TIME_32")
	public int totalMonthlyPremiumTime32;

	@Column(name = "TOTAL_MONTH_PRM_TIME_33")
	public int totalMonthlyPremiumTime33;

	@Column(name = "TOTAL_MONTH_PRM_TIME_34")
	public int totalMonthlyPremiumTime34;

	@Column(name = "TOTAL_MONTH_PRM_TIME_35")
	public int totalMonthlyPremiumTime35;

	@Column(name = "TOTAL_MONTH_PRM_TIME_36")
	public int totalMonthlyPremiumTime36;

	@Column(name = "TOTAL_MONTH_PRM_TIME_37")
	public int totalMonthlyPremiumTime37;

	@Column(name = "TOTAL_MONTH_PRM_TIME_38")
	public int totalMonthlyPremiumTime38;

	@Column(name = "TOTAL_MONTH_PRM_TIME_39")
	public int totalMonthlyPremiumTime39;

	@Column(name = "TOTAL_MONTH_PRM_TIME_40")
	public int totalMonthlyPremiumTime40;

	@Column(name = "TOTAL_MONTH_PRM_TIME_41")
	public int totalMonthlyPremiumTime41;

	@Column(name = "TOTAL_MONTH_PRM_TIME_4")
	public int totalMonthlyPremiumTime42;

	@Column(name = "TOTAL_MONTH_PRM_TIME_43")
	public int totalMonthlyPremiumTime43;

	@Column(name = "TOTAL_MONTH_PRM_TIME_44")
	public int totalMonthlyPremiumTime44;

	@Column(name = "TOTAL_MONTH_PRM_TIME_45")
	public int totalMonthlyPremiumTime45;

	@Column(name = "TOTAL_MONTH_PRM_TIME_46")
	public int totalMonthlyPremiumTime46;

	@Column(name = "TOTAL_MONTH_PRM_TIME_47")
	public int totalMonthlyPremiumTime47;

	@Column(name = "TOTAL_MONTH_PRM_TIME_48")
	public int totalMonthlyPremiumTime48;

	@Column(name = "TOTAL_MONTH_PRM_TIME_49")
	public int totalMonthlyPremiumTime49;

	@Column(name = "TOTAL_MONTH_PRM_TIME_50")
	public int totalMonthlyPremiumTime50;

	/** 変形繰越時間 */
	@Column(name = "DEFORM_CARRYFWD_TIME_1")
	public int deformationCarryforwardTime1;

	@Column(name = "DEFORM_CARRYFWD_TIME_2")
	public int deformationCarryforwardTime2;

	@Column(name = "DEFORM_CARRYFWD_TIME_3")
	public int deformationCarryforwardTime3;

	@Column(name = "DEFORM_CARRYFWD_TIME_4")
	public int deformationCarryforwardTime4;

	@Column(name = "DEFORM_CARRYFWD_TIME_5")
	public int deformationCarryforwardTime5;

	@Column(name = "DEFORM_CARRYFWD_TIME_6")
	public int deformationCarryforwardTime6;

	@Column(name = "DEFORM_CARRYFWD_TIME_7")
	public int deformationCarryforwardTime7;

	@Column(name = "DEFORM_CARRYFWD_TIME_8")
	public int deformationCarryforwardTime8;

	@Column(name = "DEFORM_CARRYFWD_TIME_9")
	public int deformationCarryforwardTime9;

	@Column(name = "DEFORM_CARRYFWD_TIME_10")
	public int deformationCarryforwardTime10;

	@Column(name = "DEFORM_CARRYFWD_TIME_11")
	public int deformationCarryforwardTime11;

	@Column(name = "DEFORM_CARRYFWD_TIME_12")
	public int deformationCarryforwardTime12;

	@Column(name = "DEFORM_CARRYFWD_TIME_13")
	public int deformationCarryforwardTime13;

	@Column(name = "DEFORM_CARRYFWD_TIME_14")
	public int deformationCarryforwardTime14;

	@Column(name = "DEFORM_CARRYFWD_TIME_15")
	public int deformationCarryforwardTime15;

	@Column(name = "DEFORM_CARRYFWD_TIME_16")
	public int deformationCarryforwardTime16;

	@Column(name = "DEFORM_CARRYFWD_TIME_17")
	public int deformationCarryforwardTime17;

	@Column(name = "DEFORM_CARRYFWD_TIME_18")
	public int deformationCarryforwardTime18;

	@Column(name = "DEFORM_CARRYFWD_TIME_19")
	public int deformationCarryforwardTime19;

	@Column(name = "DEFORM_CARRYFWD_TIME_20")
	public int deformationCarryforwardTime20;

	@Column(name = "DEFORM_CARRYFWD_TIME_21")
	public int deformationCarryforwardTime21;

	@Column(name = "DEFORM_CARRYFWD_TIME_22")
	public int deformationCarryforwardTime22;

	@Column(name = "DEFORM_CARRYFWD_TIME_23")
	public int deformationCarryforwardTime23;

	@Column(name = "DEFORM_CARRYFWD_TIME_24")
	public int deformationCarryforwardTime24;

	@Column(name = "DEFORM_CARRYFWD_TIME_25")
	public int deformationCarryforwardTime25;

	@Column(name = "DEFORM_CARRYFWD_TIME_26")
	public int deformationCarryforwardTime26;

	@Column(name = "DEFORM_CARRYFWD_TIME_27")
	public int deformationCarryforwardTime27;

	@Column(name = "DEFORM_CARRYFWD_TIME_28")
	public int deformationCarryforwardTime28;

	@Column(name = "DEFORM_CARRYFWD_TIME_29")
	public int deformationCarryforwardTime29;

	@Column(name = "DEFORM_CARRYFWD_TIME_30")
	public int deformationCarryforwardTime30;

	@Column(name = "DEFORM_CARRYFWD_TIME_31")
	public int deformationCarryforwardTime31;

	@Column(name = "DEFORM_CARRYFWD_TIME_32")
	public int deformationCarryforwardTime32;

	@Column(name = "DEFORM_CARRYFWD_TIME_33")
	public int deformationCarryforwardTime33;

	@Column(name = "DEFORM_CARRYFWD_TIME_34")
	public int deformationCarryforwardTime34;

	@Column(name = "DEFORM_CARRYFWD_TIME_35")
	public int deformationCarryforwardTime35;

	@Column(name = "DEFORM_CARRYFWD_TIME_36")
	public int deformationCarryforwardTime36;

	@Column(name = "DEFORM_CARRYFWD_TIME_37")
	public int deformationCarryforwardTime37;

	@Column(name = "DEFORM_CARRYFWD_TIME_38")
	public int deformationCarryforwardTime38;

	@Column(name = "DEFORM_CARRYFWD_TIME_39")
	public int deformationCarryforwardTime39;

	@Column(name = "DEFORM_CARRYFWD_TIME_40")
	public int deformationCarryforwardTime40;

	@Column(name = "DEFORM_CARRYFWD_TIME_41")
	public int deformationCarryforwardTime41;

	@Column(name = "DEFORM_CARRYFWD_TIME_4")
	public int deformationCarryforwardTime42;

	@Column(name = "DEFORM_CARRYFWD_TIME_43")
	public int deformationCarryforwardTime43;

	@Column(name = "DEFORM_CARRYFWD_TIME_44")
	public int deformationCarryforwardTime44;

	@Column(name = "DEFORM_CARRYFWD_TIME_45")
	public int deformationCarryforwardTime45;

	@Column(name = "DEFORM_CARRYFWD_TIME_46")
	public int deformationCarryforwardTime46;

	@Column(name = "DEFORM_CARRYFWD_TIME_47")
	public int deformationCarryforwardTime47;

	@Column(name = "DEFORM_CARRYFWD_TIME_48")
	public int deformationCarryforwardTime48;

	@Column(name = "DEFORM_CARRYFWD_TIME_49")
	public int deformationCarryforwardTime49;

	@Column(name = "DEFORM_CARRYFWD_TIME_50")
	public int deformationCarryforwardTime50;

	/* KRCDT_MON_EXCOUT_TIME */

	/** 振替時間 - EXCESS_TIME_1 */
	@Column(name = "EXCESS_TIME_1")
	public int excessTime1;

	@Column(name = "EXCESS_TIME_2")
	public int excessTime2;

	@Column(name = "EXCESS_TIME_3")
	public int excessTime3;

	@Column(name = "EXCESS_TIME_4")
	public int excessTime4;

	@Column(name = "EXCESS_TIME_5")
	public int excessTime5;

	@Column(name = "EXCESS_TIME_6")
	public int excessTime6;

	@Column(name = "EXCESS_TIME_7")
	public int excessTime7;

	@Column(name = "EXCESS_TIME_8")
	public int excessTime8;

	@Column(name = "EXCESS_TIME_9")
	public int excessTime9;

	@Column(name = "EXCESS_TIME_10")
	public int excessTime10;

	@Column(name = "EXCESS_TIME_11")
	public int excessTime11;

	@Column(name = "EXCESS_TIME_12")
	public int excessTime12;

	@Column(name = "EXCESS_TIME_13")
	public int excessTime13;

	@Column(name = "EXCESS_TIME_14")
	public int excessTime14;

	@Column(name = "EXCESS_TIME_15")
	public int excessTime15;

	@Column(name = "EXCESS_TIME_16")
	public int excessTime16;

	@Column(name = "EXCESS_TIME_17")
	public int excessTime17;

	@Column(name = "EXCESS_TIME_18")
	public int excessTime18;

	@Column(name = "EXCESS_TIME_19")
	public int excessTime19;

	@Column(name = "EXCESS_TIME_20")
	public int excessTime20;

	@Column(name = "EXCESS_TIME_21")
	public int excessTime21;

	@Column(name = "EXCESS_TIME_22")
	public int excessTime22;

	@Column(name = "EXCESS_TIME_23")
	public int excessTime23;

	@Column(name = "EXCESS_TIME_24")
	public int excessTime24;

	@Column(name = "EXCESS_TIME_25")
	public int excessTime25;

	@Column(name = "EXCESS_TIME_26")
	public int excessTime26;

	@Column(name = "EXCESS_TIME_27")
	public int excessTime27;

	@Column(name = "EXCESS_TIME_28")
	public int excessTime28;

	@Column(name = "EXCESS_TIME_29")
	public int excessTime29;

	@Column(name = "EXCESS_TIME_30")
	public int excessTime30;

	@Column(name = "EXCESS_TIME_31")
	public int excessTime31;

	@Column(name = "EXCESS_TIME_32")
	public int excessTime32;

	@Column(name = "EXCESS_TIME_33")
	public int excessTime33;

	@Column(name = "EXCESS_TIME_34")
	public int excessTime34;

	@Column(name = "EXCESS_TIME_35")
	public int excessTime35;

	@Column(name = "EXCESS_TIME_36")
	public int excessTime36;

	@Column(name = "EXCESS_TIME_37")
	public int excessTime37;

	@Column(name = "EXCESS_TIME_38")
	public int excessTime38;

	@Column(name = "EXCESS_TIME_39")
	public int excessTime39;

	@Column(name = "EXCESS_TIME_40")
	public int excessTime40;

	@Column(name = "EXCESS_TIME_41")
	public int excessTime41;

	@Column(name = "EXCESS_TIME_4")
	public int excessTime42;

	@Column(name = "EXCESS_TIME_43")
	public int excessTime43;

	@Column(name = "EXCESS_TIME_44")
	public int excessTime44;

	@Column(name = "EXCESS_TIME_45")
	public int excessTime45;

	@Column(name = "EXCESS_TIME_46")
	public int excessTime46;

	@Column(name = "EXCESS_TIME_47")
	public int excessTime47;

	@Column(name = "EXCESS_TIME_48")
	public int excessTime48;

	@Column(name = "EXCESS_TIME_49")
	public int excessTime49;

	@Column(name = "EXCESS_TIME_50")
	public int excessTime50;

	/* KRCDT_MON_AGREEMENT_TIME */

	/** 36協定時間 */
	@Column(name = "AGREEMENT_TIME")
	public int agreementTime;

	/** 限度エラー時間 */
	@Column(name = "LIMIT_ERROR_TIME")
	public int limitErrorTime;

	/** 限度アラーム時間 */
	@Column(name = "LIMIT_ALARM_TIME")
	public int limitAlarmTime;

	/** 特例限度エラー時間 */
	@Column(name = "EXCEPT_LIMIT_ERR_TIME")
	public Integer exceptionLimitErrorTime;

	/** 特例限度アラーム時間 */
	@Column(name = "EXCEPT_LIMIT_ALM_TIME")
	public Integer exceptionLimitAlarmTime;

	/** 状態 */
	@Column(name = "STATUS")
	public int status;

	/* KRCDT_MON_AFFILIATION */

	/** 月初雇用コード */
	@Column(name = "FIRST_EMP_CD")
	public String firstEmploymentCd;

	/** 月初職場ID */
	@Column(name = "FIRST_WKP_ID")
	public String firstWorkplaceId;

	/** 月初職位ID */
	@Column(name = "FIRST_JOB_ID")
	public String firstJobTitleId;

	/** 月初分類コード */
	@Column(name = "FIRST_CLS_CD")
	public String firstClassCd;

	/** 月初勤務種別コード */
	@Column(name = "FIRST_BUS_CD")
	public String firstBusinessTypeCd;

	/** 月末雇用コード */
	@Column(name = "LAST_EMP_CD")
	public String lastEmploymentCd;

	/** 月末職場ID */
	@Column(name = "LAST_WKP_ID")
	public String lastWorkplaceId;

	/** 月末職位ID */
	@Column(name = "LAST_JOB_ID")
	public String lastJobTitleId;

	/** 月末分類コード */
	@Column(name = "LAST_CLS_CD")
	public String lastClassCd;

	/** 月末勤務種別コード */
	@Column(name = "LAST_BUS_CD")
	public String lastBusinessTypeCd;

	@Override
	protected Object getKey() {
		return this.krcdtMonMergePk;
	}

	/** KRCDT_MON_AGGR_ABSN_DAYS 30 **/
	public void toEntityAbsenceDays(AggregateAbsenceDaysMerge absenceDaysMerge) {
		this.toEntityAbsenceDays1(absenceDaysMerge.getAbsenceDays1());
		this.toEntityAbsenceDays2(absenceDaysMerge.getAbsenceDays2());
		this.toEntityAbsenceDays2(absenceDaysMerge.getAbsenceDays2());
		this.toEntityAbsenceDays3(absenceDaysMerge.getAbsenceDays3());
		this.toEntityAbsenceDays4(absenceDaysMerge.getAbsenceDays4());
		this.toEntityAbsenceDays5(absenceDaysMerge.getAbsenceDays5());
		this.toEntityAbsenceDays6(absenceDaysMerge.getAbsenceDays6());
		this.toEntityAbsenceDays7(absenceDaysMerge.getAbsenceDays7());
		this.toEntityAbsenceDays8(absenceDaysMerge.getAbsenceDays8());
		this.toEntityAbsenceDays9(absenceDaysMerge.getAbsenceDays9());
		this.toEntityAbsenceDays10(absenceDaysMerge.getAbsenceDays10());
		this.toEntityAbsenceDays11(absenceDaysMerge.getAbsenceDays11());
		this.toEntityAbsenceDays12(absenceDaysMerge.getAbsenceDays12());
		this.toEntityAbsenceDays13(absenceDaysMerge.getAbsenceDays13());
		this.toEntityAbsenceDays14(absenceDaysMerge.getAbsenceDays14());
		this.toEntityAbsenceDays15(absenceDaysMerge.getAbsenceDays15());
		this.toEntityAbsenceDays16(absenceDaysMerge.getAbsenceDays16());
		this.toEntityAbsenceDays17(absenceDaysMerge.getAbsenceDays17());
		this.toEntityAbsenceDays18(absenceDaysMerge.getAbsenceDays18());
		this.toEntityAbsenceDays19(absenceDaysMerge.getAbsenceDays19());
		this.toEntityAbsenceDays20(absenceDaysMerge.getAbsenceDays20());
		this.toEntityAbsenceDays21(absenceDaysMerge.getAbsenceDays21());
		this.toEntityAbsenceDays22(absenceDaysMerge.getAbsenceDays22());
		this.toEntityAbsenceDays23(absenceDaysMerge.getAbsenceDays23());
		this.toEntityAbsenceDays24(absenceDaysMerge.getAbsenceDays24());
		this.toEntityAbsenceDays25(absenceDaysMerge.getAbsenceDays25());
		this.toEntityAbsenceDays26(absenceDaysMerge.getAbsenceDays26());
		this.toEntityAbsenceDays27(absenceDaysMerge.getAbsenceDays27());
		this.toEntityAbsenceDays28(absenceDaysMerge.getAbsenceDays28());
		this.toEntityAbsenceDays29(absenceDaysMerge.getAbsenceDays29());
		this.toEntityAbsenceDays30(absenceDaysMerge.getAbsenceDays30());
	}

	/** KRCDT_MON_AGGR_BNSPY_TIME 10 **/
	public void toEntityBonusPayTime(AggregateBonusPayTimeMerge bonusPayTimeMerge) {
		this.toEntityBonusPayTime1(bonusPayTimeMerge.getPaytime1());
		this.toEntityBonusPayTime2(bonusPayTimeMerge.getPaytime2());
		this.toEntityBonusPayTime3(bonusPayTimeMerge.getPaytime3());
		this.toEntityBonusPayTime4(bonusPayTimeMerge.getPaytime4());
		this.toEntityBonusPayTime5(bonusPayTimeMerge.getPaytime5());
		this.toEntityBonusPayTime6(bonusPayTimeMerge.getPaytime6());
		this.toEntityBonusPayTime7(bonusPayTimeMerge.getPaytime7());
		this.toEntityBonusPayTime8(bonusPayTimeMerge.getPaytime8());
		this.toEntityBonusPayTime9(bonusPayTimeMerge.getPaytime9());
		this.toEntityBonusPayTime10(bonusPayTimeMerge.getPaytime10());
	}

	/** KRCDT_MON_AGGR_DIVG_TIME 10 **/
	public void toEntityDivergenceTime(AggregateDivergenceTimeMerge divergenceTime) {
		this.toEntityDivergenceTime1(divergenceTime.getDivergenceTime1());
		this.toEntityDivergenceTime2(divergenceTime.getDivergenceTime2());
		this.toEntityDivergenceTime3(divergenceTime.getDivergenceTime3());
		this.toEntityDivergenceTime4(divergenceTime.getDivergenceTime4());
		this.toEntityDivergenceTime5(divergenceTime.getDivergenceTime5());
		this.toEntityDivergenceTime6(divergenceTime.getDivergenceTime6());
		this.toEntityDivergenceTime7(divergenceTime.getDivergenceTime7());
		this.toEntityDivergenceTime8(divergenceTime.getDivergenceTime8());
		this.toEntityDivergenceTime9(divergenceTime.getDivergenceTime9());
		this.toEntityDivergenceTime10(divergenceTime.getDivergenceTime10());
	}

	/** KRCDT_MON_AGGR_GOOUT 4 **/
	public void toEntityGoOut(AggregateGoOutMerge goOutMeger) {
		this.toEntityGoOut1(goOutMeger.getGoOut1());
		this.toEntityGoOut2(goOutMeger.getGoOut2());
		this.toEntityGoOut3(goOutMeger.getGoOut3());
		this.toEntityGoOut4(goOutMeger.getGoOut4());
	}

	/** KRCDT_MON_AGGR_HDWK_TIME 10 **/
	public void toEntityHolidayWorkTime(AggregateHolidayWorkTimeMerge domain) {
		this.toEntityHolidayWorkTime1(domain.getHolidayWorkTime1());
		this.toEntityHolidayWorkTime2(domain.getHolidayWorkTime2());
		this.toEntityHolidayWorkTime3(domain.getHolidayWorkTime3());
		this.toEntityHolidayWorkTime4(domain.getHolidayWorkTime4());
		this.toEntityHolidayWorkTime5(domain.getHolidayWorkTime5());
		this.toEntityHolidayWorkTime6(domain.getHolidayWorkTime6());
		this.toEntityHolidayWorkTime7(domain.getHolidayWorkTime7());
		this.toEntityHolidayWorkTime8(domain.getHolidayWorkTime8());
		this.toEntityHolidayWorkTime9(domain.getHolidayWorkTime9());
		this.toEntityHolidayWorkTime10(domain.getHolidayWorkTime10());
	}

	/** KRCDT_MON_AGGR_OVER_TIME 10 **/
	public void toEntityOverTime(AggregateOverTimeMerge domain) {
		this.toEntityOverTime1(domain.getOverTime1());
		this.toEntityOverTime2(domain.getOverTime2());
		this.toEntityOverTime3(domain.getOverTime3());
		this.toEntityOverTime4(domain.getOverTime4());
		this.toEntityOverTime5(domain.getOverTime5());
		this.toEntityOverTime6(domain.getOverTime6());
		this.toEntityOverTime7(domain.getOverTime7());
		this.toEntityOverTime8(domain.getOverTime8());
		this.toEntityOverTime9(domain.getOverTime9());
		this.toEntityOverTime10(domain.getOverTime10());
	}

	/** KRCDT_MON_AGGR_PREM_TIME 10 **/
	public void toEntityPremiumTime(AggregatePremiumTimeMerge domain) {
		this.toEntityPremiumTime1(domain.getPremiumTime1());
		this.toEntityPremiumTime2(domain.getPremiumTime2());
		this.toEntityPremiumTime3(domain.getPremiumTime3());
		this.toEntityPremiumTime4(domain.getPremiumTime4());
		this.toEntityPremiumTime5(domain.getPremiumTime5());
		this.toEntityPremiumTime6(domain.getPremiumTime6());
		this.toEntityPremiumTime7(domain.getPremiumTime7());
		this.toEntityPremiumTime8(domain.getPremiumTime8());
		this.toEntityPremiumTime9(domain.getPremiumTime9());
		this.toEntityPremiumTime10(domain.getPremiumTime10());
	}

	/** KRCDT_MON_AGGR_SPEC_DAYS 10 **/
	public void toEntitySpecificDays(AggregateSpecificDaysMerge domain) {
		this.toEntitySpecificDays1(domain.getSpecificDays1());
		this.toEntitySpecificDays2(domain.getSpecificDays2());
		this.toEntitySpecificDays3(domain.getSpecificDays3());
		this.toEntitySpecificDays4(domain.getSpecificDays4());
		this.toEntitySpecificDays5(domain.getSpecificDays5());
		this.toEntitySpecificDays6(domain.getSpecificDays6());
		this.toEntitySpecificDays7(domain.getSpecificDays7());
		this.toEntitySpecificDays8(domain.getSpecificDays8());
		this.toEntitySpecificDays9(domain.getSpecificDays9());
		this.toEntitySpecificDays10(domain.getSpecificDays10());
	}

	/** KRCDT_MON_EXCESS_OUTSIDE 50 **/
	public void toEntityExcessOutsideWorkOfMonthly(ExcessOutsideWorkOfMonthlyMerge domain) {
		this.toEntityExcessOutsideWorkOfMonthly1(domain.getExcessOutsideWorkOfMonthly1());
		this.toEntityExcessOutsideWorkOfMonthly2(domain.getExcessOutsideWorkOfMonthly2());
		this.toEntityExcessOutsideWorkOfMonthly3(domain.getExcessOutsideWorkOfMonthly3());
		this.toEntityExcessOutsideWorkOfMonthly4(domain.getExcessOutsideWorkOfMonthly4());
		this.toEntityExcessOutsideWorkOfMonthly5(domain.getExcessOutsideWorkOfMonthly5());
		this.toEntityExcessOutsideWorkOfMonthly6(domain.getExcessOutsideWorkOfMonthly6());
		this.toEntityExcessOutsideWorkOfMonthly7(domain.getExcessOutsideWorkOfMonthly7());
		this.toEntityExcessOutsideWorkOfMonthly8(domain.getExcessOutsideWorkOfMonthly8());
		this.toEntityExcessOutsideWorkOfMonthly9(domain.getExcessOutsideWorkOfMonthly9());
		this.toEntityExcessOutsideWorkOfMonthly10(domain.getExcessOutsideWorkOfMonthly10());
		this.toEntityExcessOutsideWorkOfMonthly11(domain.getExcessOutsideWorkOfMonthly11());
		this.toEntityExcessOutsideWorkOfMonthly12(domain.getExcessOutsideWorkOfMonthly12());
		this.toEntityExcessOutsideWorkOfMonthly13(domain.getExcessOutsideWorkOfMonthly13());
		this.toEntityExcessOutsideWorkOfMonthly14(domain.getExcessOutsideWorkOfMonthly14());
		this.toEntityExcessOutsideWorkOfMonthly15(domain.getExcessOutsideWorkOfMonthly15());
		this.toEntityExcessOutsideWorkOfMonthly16(domain.getExcessOutsideWorkOfMonthly16());
		this.toEntityExcessOutsideWorkOfMonthly17(domain.getExcessOutsideWorkOfMonthly17());
		this.toEntityExcessOutsideWorkOfMonthly18(domain.getExcessOutsideWorkOfMonthly18());
		this.toEntityExcessOutsideWorkOfMonthly19(domain.getExcessOutsideWorkOfMonthly19());
		this.toEntityExcessOutsideWorkOfMonthly20(domain.getExcessOutsideWorkOfMonthly20());
		this.toEntityExcessOutsideWorkOfMonthly21(domain.getExcessOutsideWorkOfMonthly21());
		this.toEntityExcessOutsideWorkOfMonthly22(domain.getExcessOutsideWorkOfMonthly22());
		this.toEntityExcessOutsideWorkOfMonthly23(domain.getExcessOutsideWorkOfMonthly23());
		this.toEntityExcessOutsideWorkOfMonthly24(domain.getExcessOutsideWorkOfMonthly24());
		this.toEntityExcessOutsideWorkOfMonthly25(domain.getExcessOutsideWorkOfMonthly25());
		this.toEntityExcessOutsideWorkOfMonthly26(domain.getExcessOutsideWorkOfMonthly26());
		this.toEntityExcessOutsideWorkOfMonthly27(domain.getExcessOutsideWorkOfMonthly27());
		this.toEntityExcessOutsideWorkOfMonthly28(domain.getExcessOutsideWorkOfMonthly28());
		this.toEntityExcessOutsideWorkOfMonthly29(domain.getExcessOutsideWorkOfMonthly29());
		this.toEntityExcessOutsideWorkOfMonthly30(domain.getExcessOutsideWorkOfMonthly30());
		this.toEntityExcessOutsideWorkOfMonthly31(domain.getExcessOutsideWorkOfMonthly31());
		this.toEntityExcessOutsideWorkOfMonthly32(domain.getExcessOutsideWorkOfMonthly32());
		this.toEntityExcessOutsideWorkOfMonthly33(domain.getExcessOutsideWorkOfMonthly33());
		this.toEntityExcessOutsideWorkOfMonthly34(domain.getExcessOutsideWorkOfMonthly34());
		this.toEntityExcessOutsideWorkOfMonthly35(domain.getExcessOutsideWorkOfMonthly35());
		this.toEntityExcessOutsideWorkOfMonthly36(domain.getExcessOutsideWorkOfMonthly36());
		this.toEntityExcessOutsideWorkOfMonthly37(domain.getExcessOutsideWorkOfMonthly37());
		this.toEntityExcessOutsideWorkOfMonthly38(domain.getExcessOutsideWorkOfMonthly38());
		this.toEntityExcessOutsideWorkOfMonthly39(domain.getExcessOutsideWorkOfMonthly39());
		this.toEntityExcessOutsideWorkOfMonthly40(domain.getExcessOutsideWorkOfMonthly40());
		this.toEntityExcessOutsideWorkOfMonthly41(domain.getExcessOutsideWorkOfMonthly41());
		this.toEntityExcessOutsideWorkOfMonthly42(domain.getExcessOutsideWorkOfMonthly42());
		this.toEntityExcessOutsideWorkOfMonthly43(domain.getExcessOutsideWorkOfMonthly43());
		this.toEntityExcessOutsideWorkOfMonthly44(domain.getExcessOutsideWorkOfMonthly44());
		this.toEntityExcessOutsideWorkOfMonthly45(domain.getExcessOutsideWorkOfMonthly45());
		this.toEntityExcessOutsideWorkOfMonthly46(domain.getExcessOutsideWorkOfMonthly46());
		this.toEntityExcessOutsideWorkOfMonthly47(domain.getExcessOutsideWorkOfMonthly47());
		this.toEntityExcessOutsideWorkOfMonthly48(domain.getExcessOutsideWorkOfMonthly48());
		this.toEntityExcessOutsideWorkOfMonthly49(domain.getExcessOutsideWorkOfMonthly49());
		this.toEntityExcessOutsideWorkOfMonthly50(domain.getExcessOutsideWorkOfMonthly50());

	}

	/** KRCDT_MON_EXCOUT_TIME **/
	public void toEntityExcessOutsideWorkMerge(ExcessOutsideWorkMerge domain) {
		this.toEntityExcessOutsideWork1(domain.getExcessOutsideWork1());
		this.toEntityExcessOutsideWork2(domain.getExcessOutsideWork2());
		this.toEntityExcessOutsideWork3(domain.getExcessOutsideWork3());
		this.toEntityExcessOutsideWork4(domain.getExcessOutsideWork4());
		this.toEntityExcessOutsideWork5(domain.getExcessOutsideWork5());
		this.toEntityExcessOutsideWork6(domain.getExcessOutsideWork6());
		this.toEntityExcessOutsideWork7(domain.getExcessOutsideWork7());
		this.toEntityExcessOutsideWork8(domain.getExcessOutsideWork8());
		this.toEntityExcessOutsideWork9(domain.getExcessOutsideWork9());
		this.toEntityExcessOutsideWork10(domain.getExcessOutsideWork10());
		this.toEntityExcessOutsideWork11(domain.getExcessOutsideWork11());
		this.toEntityExcessOutsideWork12(domain.getExcessOutsideWork12());
		this.toEntityExcessOutsideWork13(domain.getExcessOutsideWork13());
		this.toEntityExcessOutsideWork14(domain.getExcessOutsideWork14());
		this.toEntityExcessOutsideWork15(domain.getExcessOutsideWork15());
		this.toEntityExcessOutsideWork16(domain.getExcessOutsideWork16());
		this.toEntityExcessOutsideWork17(domain.getExcessOutsideWork17());
		this.toEntityExcessOutsideWork18(domain.getExcessOutsideWork18());
		this.toEntityExcessOutsideWork19(domain.getExcessOutsideWork19());
		this.toEntityExcessOutsideWork20(domain.getExcessOutsideWork20());
		this.toEntityExcessOutsideWork21(domain.getExcessOutsideWork21());
		this.toEntityExcessOutsideWork22(domain.getExcessOutsideWork22());
		this.toEntityExcessOutsideWork23(domain.getExcessOutsideWork23());
		this.toEntityExcessOutsideWork24(domain.getExcessOutsideWork24());
		this.toEntityExcessOutsideWork25(domain.getExcessOutsideWork25());
		this.toEntityExcessOutsideWork26(domain.getExcessOutsideWork26());
		this.toEntityExcessOutsideWork27(domain.getExcessOutsideWork27());
		this.toEntityExcessOutsideWork28(domain.getExcessOutsideWork28());
		this.toEntityExcessOutsideWork29(domain.getExcessOutsideWork29());
		this.toEntityExcessOutsideWork30(domain.getExcessOutsideWork30());
		this.toEntityExcessOutsideWork31(domain.getExcessOutsideWork31());
		this.toEntityExcessOutsideWork32(domain.getExcessOutsideWork32());
		this.toEntityExcessOutsideWork33(domain.getExcessOutsideWork33());
		this.toEntityExcessOutsideWork34(domain.getExcessOutsideWork34());
		this.toEntityExcessOutsideWork35(domain.getExcessOutsideWork35());
		this.toEntityExcessOutsideWork36(domain.getExcessOutsideWork36());
		this.toEntityExcessOutsideWork37(domain.getExcessOutsideWork37());
		this.toEntityExcessOutsideWork38(domain.getExcessOutsideWork38());
		this.toEntityExcessOutsideWork39(domain.getExcessOutsideWork39());
		this.toEntityExcessOutsideWork40(domain.getExcessOutsideWork40());
		this.toEntityExcessOutsideWork41(domain.getExcessOutsideWork41());
		this.toEntityExcessOutsideWork42(domain.getExcessOutsideWork42());
		this.toEntityExcessOutsideWork43(domain.getExcessOutsideWork43());
		this.toEntityExcessOutsideWork44(domain.getExcessOutsideWork44());
		this.toEntityExcessOutsideWork45(domain.getExcessOutsideWork45());
		this.toEntityExcessOutsideWork46(domain.getExcessOutsideWork46());
		this.toEntityExcessOutsideWork47(domain.getExcessOutsideWork47());
		this.toEntityExcessOutsideWork48(domain.getExcessOutsideWork48());
		this.toEntityExcessOutsideWork49(domain.getExcessOutsideWork49());
		this.toEntityExcessOutsideWork50(domain.getExcessOutsideWork50());

	}

	public void toEntityAbsenceDays1(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo1 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo1 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays2(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo2 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo2 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays3(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo3 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo3 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays4(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo4 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo4 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays5(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo5 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo5 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays6(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo6 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo6 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays7(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo7 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo7 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays8(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo8 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo8 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays9(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo9 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo9 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays10(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo10 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo10 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays11(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo11 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo11 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays12(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo12 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo12 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays13(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo13 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo13 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays14(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo14 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo14 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays15(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo15 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo15 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays16(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo16 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo16 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays17(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo17 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo17 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays18(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo18 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo18 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays19(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo19 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo19 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays20(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo20 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo20 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays21(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo21 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo21 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays22(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo22 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo22 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays23(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo23 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo23 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays24(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo24 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo24 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays25(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo25 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo25 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays26(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo26 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo26 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays27(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo27 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo27 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays28(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo28 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo28 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays29(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo29 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo29 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityAbsenceDays30(AggregateAbsenceDays absenceDays) {
		this.absenceDayNo30 = absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTimeNo30 = absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	public void toEntityBonusPayTime1(AggregateBonusPayTime paytime) {
		this.bonusPayTime1 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime1 = paytime.getSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime1 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime1 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	public void toEntityBonusPayTime2(AggregateBonusPayTime paytime) {
		this.bonusPayTime2 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime2 = paytime.getSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime2 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime2 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	public void toEntityBonusPayTime3(AggregateBonusPayTime paytime) {
		this.bonusPayTime3 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime3 = paytime.getSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime3 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime3 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	public void toEntityBonusPayTime4(AggregateBonusPayTime paytime) {
		this.bonusPayTime4 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime4 = paytime.getSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime4 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime4 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	public void toEntityBonusPayTime5(AggregateBonusPayTime paytime) {
		this.bonusPayTime5 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime5 = paytime.getSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime5 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime5 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	public void toEntityBonusPayTime6(AggregateBonusPayTime paytime) {
		this.bonusPayTime6 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime6 = paytime.getSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime6 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime6 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	public void toEntityBonusPayTime7(AggregateBonusPayTime paytime) {
		this.bonusPayTime7 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime7 = paytime.getSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime7 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime7 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	public void toEntityBonusPayTime8(AggregateBonusPayTime paytime) {
		this.bonusPayTime8 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime8 = paytime.getSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime8 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime8 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	public void toEntityBonusPayTime9(AggregateBonusPayTime paytime) {
		this.bonusPayTime9 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime9 = paytime.getSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime9 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime9 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	public void toEntityBonusPayTime10(AggregateBonusPayTime paytime) {
		this.bonusPayTime10 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime10 = paytime.getSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime10 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime10 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	public void toEntityDivergenceTime1(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr1 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime1 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime1 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction1 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	public void toEntityDivergenceTime2(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr2 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime2 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime2 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction2 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	public void toEntityDivergenceTime3(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr3 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime3 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime3 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction3 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	public void toEntityDivergenceTime4(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr4 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime4 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime4 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction4 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	public void toEntityDivergenceTime5(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr5 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime5 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime5 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction5 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	public void toEntityDivergenceTime6(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr6 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime6 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime6 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction6 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	public void toEntityDivergenceTime7(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr7 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime7 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime7 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction7 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	public void toEntityDivergenceTime8(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr8 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime8 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime8 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction8 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	public void toEntityDivergenceTime9(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr9 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime9 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime9 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction9 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	public void toEntityDivergenceTime10(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr10 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime10 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime10 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction10 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	public void toEntityGoOut1(AggregateGoOut goOut) {
		this.goOutTimes1 = goOut.getTimes() == null ? 0
				: (goOut.getTimes().v() == null ? 0
						: (goOut.getTimes().v() == null ? 0 : goOut.getTimes().v().intValue()));
		this.legalTime1 = goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getTime() == null ? 0 : goOut.getLegalTime().getTime().v());
		this.calcLegalTime1 = goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getCalcTime() == null ? 0 : goOut.getLegalTime().getCalcTime().v());
		this.illegalTime1 = goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getTime() == null ? 0 : goOut.getIllegalTime().getTime().v());
		this.calcIllegalTime1 = goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getCalcTime() == null ? 0 : goOut.getIllegalTime().getCalcTime().v());
		this.totalTime1 = goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getTime() == null ? 0 : goOut.getTotalTime().getTime().v());
		this.calcTotalTime1 = goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getCalcTime() == null ? 0 : goOut.getTotalTime().getCalcTime().v());
	}

	public void toEntityGoOut2(AggregateGoOut goOut) {
		this.goOutTimes2 = goOut.getTimes() == null ? 0
				: (goOut.getTimes().v() == null ? 0
						: (goOut.getTimes().v() == null ? 0 : goOut.getTimes().v().intValue()));
		this.legalTime2 = goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getTime() == null ? 0 : goOut.getLegalTime().getTime().v());
		this.calcLegalTime2 = goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getCalcTime() == null ? 0 : goOut.getLegalTime().getCalcTime().v());
		this.illegalTime2 = goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getTime() == null ? 0 : goOut.getIllegalTime().getTime().v());
		this.calcIllegalTime2 = goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getCalcTime() == null ? 0 : goOut.getIllegalTime().getCalcTime().v());
		this.totalTime2 = goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getTime() == null ? 0 : goOut.getTotalTime().getTime().v());
		this.calcTotalTime2 = goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getCalcTime() == null ? 0 : goOut.getTotalTime().getCalcTime().v());
	}

	public void toEntityGoOut3(AggregateGoOut goOut) {
		this.goOutTimes3 = goOut.getTimes() == null ? 0
				: (goOut.getTimes().v() == null ? 0
						: (goOut.getTimes().v() == null ? 0 : goOut.getTimes().v().intValue()));
		this.legalTime3 = goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getTime() == null ? 0 : goOut.getLegalTime().getTime().v());
		this.calcLegalTime3 = goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getCalcTime() == null ? 0 : goOut.getLegalTime().getCalcTime().v());
		this.illegalTime3 = goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getTime() == null ? 0 : goOut.getIllegalTime().getTime().v());
		this.calcIllegalTime3 = goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getCalcTime() == null ? 0 : goOut.getIllegalTime().getCalcTime().v());
		this.totalTime3 = goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getTime() == null ? 0 : goOut.getTotalTime().getTime().v());
		this.calcTotalTime3 = goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getCalcTime() == null ? 0 : goOut.getTotalTime().getCalcTime().v());
	}

	public void toEntityGoOut4(AggregateGoOut goOut) {
		this.goOutTimes4 = goOut.getTimes() == null ? 0
				: (goOut.getTimes().v() == null ? 0 : goOut.getTimes().v().intValue());
		this.legalTime4 = goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getTime() == null ? 0 : goOut.getLegalTime().getTime().v());
		this.calcLegalTime4 = goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getCalcTime() == null ? 0 : goOut.getLegalTime().getCalcTime().v());
		this.illegalTime4 = goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getTime() == null ? 0 : goOut.getIllegalTime().getTime().v());
		this.calcIllegalTime4 = goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getCalcTime() == null ? 0 : goOut.getIllegalTime().getCalcTime().v());
		this.totalTime4 = goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getTime() == null ? 0 : goOut.getTotalTime().getTime().v());
		this.calcTotalTime4 = goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getCalcTime() == null ? 0 : goOut.getTotalTime().getCalcTime().v());
	}

	/* KRCDT_MON_AGGR_HDWK_TIME 10 */
	public void toEntityHolidayWorkTime1(AggregateHolidayWorkTime domain) {
		this.holidayWorkTime1 = domain.getHolidayWorkTime() == null ? 0 : domain.getHolidayWorkTime().getTime().v();
		this.calcHolidayWorkTime1 = domain.getHolidayWorkTime() == null ? 0
				: domain.getHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime1 = domain.getBeforeHolidayWorkTime().v();
		this.transferTime1 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getTime().v();
		this.calcTransferTime1 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getCalcTime().v();
		this.legalHolidayWorkTime1 = domain.getLegalHolidayWorkTime() == null ? 0
				: domain.getLegalHolidayWorkTime().v();
		this.legalTransferHolidayWorkTime1 = domain.getLegalTransferHolidayWorkTime() == null ? 0
				: domain.getLegalTransferHolidayWorkTime().v();
	}

	public void toEntityHolidayWorkTime2(AggregateHolidayWorkTime domain) {
		this.holidayWorkTime2 = domain.getHolidayWorkTime() == null ? 0 : domain.getHolidayWorkTime().getTime().v();
		this.calcHolidayWorkTime2 = domain.getHolidayWorkTime() == null ? 0
				: domain.getHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime2 = domain.getBeforeHolidayWorkTime().v();
		this.transferTime2 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getTime().v();
		this.calcTransferTime2 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getCalcTime().v();
		this.legalHolidayWorkTime2 = domain.getLegalHolidayWorkTime() == null ? 0
				: domain.getLegalHolidayWorkTime().v();
		this.legalTransferHolidayWorkTime2 = domain.getLegalTransferHolidayWorkTime() == null ? 0
				: domain.getLegalTransferHolidayWorkTime().v();
	}

	public void toEntityHolidayWorkTime3(AggregateHolidayWorkTime domain) {
		this.holidayWorkTime3 = domain.getHolidayWorkTime() == null ? 0 : domain.getHolidayWorkTime().getTime().v();
		this.calcHolidayWorkTime3 = domain.getHolidayWorkTime() == null ? 0
				: domain.getHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime3 = domain.getBeforeHolidayWorkTime().v();
		this.transferTime3 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getTime().v();
		this.calcTransferTime3 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getCalcTime().v();
		this.legalHolidayWorkTime3 = domain.getLegalHolidayWorkTime() == null ? 0
				: domain.getLegalHolidayWorkTime().v();
		this.legalTransferHolidayWorkTime3 = domain.getLegalTransferHolidayWorkTime() == null ? 0
				: domain.getLegalTransferHolidayWorkTime().v();
	}

	public void toEntityHolidayWorkTime4(AggregateHolidayWorkTime domain) {
		this.holidayWorkTime4 = domain.getHolidayWorkTime() == null ? 0 : domain.getHolidayWorkTime().getTime().v();
		this.calcHolidayWorkTime4 = domain.getHolidayWorkTime() == null ? 0
				: domain.getHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime4 = domain.getBeforeHolidayWorkTime().v();
		this.transferTime4 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getTime().v();
		this.calcTransferTime4 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getCalcTime().v();
		this.legalHolidayWorkTime4 = domain.getLegalHolidayWorkTime() == null ? 0
				: domain.getLegalHolidayWorkTime().v();
		this.legalTransferHolidayWorkTime4 = domain.getLegalTransferHolidayWorkTime() == null ? 0
				: domain.getLegalTransferHolidayWorkTime().v();
	}

	public void toEntityHolidayWorkTime5(AggregateHolidayWorkTime domain) {
		this.holidayWorkTime5 = domain.getHolidayWorkTime() == null ? 0 : domain.getHolidayWorkTime().getTime().v();
		this.calcHolidayWorkTime5 = domain.getHolidayWorkTime() == null ? 0
				: domain.getHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime5 = domain.getBeforeHolidayWorkTime().v();
		this.transferTime5 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getTime().v();
		this.calcTransferTime5 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getCalcTime().v();
		this.legalHolidayWorkTime5 = domain.getLegalHolidayWorkTime() == null ? 0
				: domain.getLegalHolidayWorkTime().v();
		this.legalTransferHolidayWorkTime5 = domain.getLegalTransferHolidayWorkTime() == null ? 0
				: domain.getLegalTransferHolidayWorkTime().v();
	}

	public void toEntityHolidayWorkTime6(AggregateHolidayWorkTime domain) {
		this.holidayWorkTime6 = domain.getHolidayWorkTime() == null ? 0 : domain.getHolidayWorkTime().getTime().v();
		this.calcHolidayWorkTime6 = domain.getHolidayWorkTime() == null ? 0
				: domain.getHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime6 = domain.getBeforeHolidayWorkTime().v();
		this.transferTime6 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getTime().v();
		this.calcTransferTime6 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getCalcTime().v();
		this.legalHolidayWorkTime6 = domain.getLegalHolidayWorkTime() == null ? 0
				: domain.getLegalHolidayWorkTime().v();
		this.legalTransferHolidayWorkTime6 = domain.getLegalTransferHolidayWorkTime() == null ? 0
				: domain.getLegalTransferHolidayWorkTime().v();
	}

	public void toEntityHolidayWorkTime7(AggregateHolidayWorkTime domain) {
		this.holidayWorkTime7 = domain.getHolidayWorkTime() == null ? 0 : domain.getHolidayWorkTime().getTime().v();
		this.calcHolidayWorkTime7 = domain.getHolidayWorkTime() == null ? 0
				: domain.getHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime7 = domain.getBeforeHolidayWorkTime().v();
		this.transferTime7 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getTime().v();
		this.calcTransferTime7 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getCalcTime().v();
		this.legalHolidayWorkTime7 = domain.getLegalHolidayWorkTime() == null ? 0
				: domain.getLegalHolidayWorkTime().v();
		this.legalTransferHolidayWorkTime7 = domain.getLegalTransferHolidayWorkTime() == null ? 0
				: domain.getLegalTransferHolidayWorkTime().v();
	}

	public void toEntityHolidayWorkTime8(AggregateHolidayWorkTime domain) {
		this.holidayWorkTime8 = domain.getHolidayWorkTime() == null ? 0 : domain.getHolidayWorkTime().getTime().v();
		this.calcHolidayWorkTime8 = domain.getHolidayWorkTime() == null ? 0
				: domain.getHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime8 = domain.getBeforeHolidayWorkTime().v();
		this.transferTime8 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getTime().v();
		this.calcTransferTime8 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getCalcTime().v();
		this.legalHolidayWorkTime8 = domain.getLegalHolidayWorkTime() == null ? 0
				: domain.getLegalHolidayWorkTime().v();
		this.legalTransferHolidayWorkTime8 = domain.getLegalTransferHolidayWorkTime() == null ? 0
				: domain.getLegalTransferHolidayWorkTime().v();
	}

	public void toEntityHolidayWorkTime9(AggregateHolidayWorkTime domain) {
		this.holidayWorkTime9 = domain.getHolidayWorkTime() == null ? 0 : domain.getHolidayWorkTime().getTime().v();
		this.calcHolidayWorkTime9 = domain.getHolidayWorkTime() == null ? 0
				: domain.getHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime9 = domain.getBeforeHolidayWorkTime().v();
		this.transferTime9 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getTime().v();
		this.calcTransferTime9 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getCalcTime().v();
		this.legalHolidayWorkTime9 = domain.getLegalHolidayWorkTime() == null ? 0
				: domain.getLegalHolidayWorkTime().v();
		this.legalTransferHolidayWorkTime9 = domain.getLegalTransferHolidayWorkTime() == null ? 0
				: domain.getLegalTransferHolidayWorkTime().v();
	}

	public void toEntityHolidayWorkTime10(AggregateHolidayWorkTime domain) {
		this.holidayWorkTime10 = domain.getHolidayWorkTime() == null ? 0 : domain.getHolidayWorkTime().getTime().v();
		this.calcHolidayWorkTime10 = domain.getHolidayWorkTime() == null ? 0
				: domain.getHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime10 = domain.getBeforeHolidayWorkTime().v();
		this.transferTime10 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getTime().v();
		this.calcTransferTime10 = domain.getTransferTime() == null ? 0 : domain.getTransferTime().getCalcTime().v();
		this.legalHolidayWorkTime10 = domain.getLegalHolidayWorkTime() == null ? 0
				: domain.getLegalHolidayWorkTime().v();
		this.legalTransferHolidayWorkTime10 = domain.getLegalTransferHolidayWorkTime() == null ? 0
				: domain.getLegalTransferHolidayWorkTime().v();
	}

	/* KRCDT_MON_AGGR_OVER_TIME 10 */
	public void toEntityOverTime1(AggregateOverTime domain) {
		this.overTime1 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime1 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime1 = domain.getBeforeOverTime().v();
		this.transferOverTime1 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime1 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime1 = domain.getLegalOverTime().v();
		this.legalTransferOverTime1 = domain.getLegalTransferOverTime().v();
	}

	public void toEntityOverTime2(AggregateOverTime domain) {
		this.overTime2 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime2 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime2 = domain.getBeforeOverTime().v();
		this.transferOverTime2 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime2 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime2 = domain.getLegalOverTime().v();
		this.legalTransferOverTime2 = domain.getLegalTransferOverTime().v();
	}

	public void toEntityOverTime3(AggregateOverTime domain) {
		this.overTime3 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime3 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime3 = domain.getBeforeOverTime().v();
		this.transferOverTime3 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime3 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime3 = domain.getLegalOverTime().v();
		this.legalTransferOverTime3 = domain.getLegalTransferOverTime().v();
	}

	public void toEntityOverTime4(AggregateOverTime domain) {
		this.overTime4 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime4 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime4 = domain.getBeforeOverTime().v();
		this.transferOverTime4 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime4 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime4 = domain.getLegalOverTime().v();
		this.legalTransferOverTime4 = domain.getLegalTransferOverTime().v();
	}

	public void toEntityOverTime5(AggregateOverTime domain) {
		this.overTime5 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime5 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime5 = domain.getBeforeOverTime().v();
		this.transferOverTime5 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime5 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime5 = domain.getLegalOverTime().v();
		this.legalTransferOverTime5 = domain.getLegalTransferOverTime().v();
	}

	public void toEntityOverTime6(AggregateOverTime domain) {
		this.overTime6 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime6 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime6 = domain.getBeforeOverTime().v();
		this.transferOverTime6 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime6 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime6 = domain.getLegalOverTime().v();
		this.legalTransferOverTime6 = domain.getLegalTransferOverTime().v();
	}

	public void toEntityOverTime7(AggregateOverTime domain) {
		this.overTime7 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime7 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime7 = domain.getBeforeOverTime().v();
		this.transferOverTime7 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime7 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime7 = domain.getLegalOverTime().v();
		this.legalTransferOverTime7 = domain.getLegalTransferOverTime().v();
	}

	public void toEntityOverTime8(AggregateOverTime domain) {
		this.overTime8 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime8 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime8 = domain.getBeforeOverTime().v();
		this.transferOverTime8 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime8 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime8 = domain.getLegalOverTime().v();
		this.legalTransferOverTime8 = domain.getLegalTransferOverTime().v();
	}

	public void toEntityOverTime9(AggregateOverTime domain) {
		this.overTime9 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime9 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime9 = domain.getBeforeOverTime().v();
		this.transferOverTime9 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime9 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime9 = domain.getLegalOverTime().v();
		this.legalTransferOverTime9 = domain.getLegalTransferOverTime().v();
	}

	public void toEntityOverTime10(AggregateOverTime domain) {
		this.overTime10 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime10 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime10 = domain.getBeforeOverTime().v();
		this.transferOverTime10 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime10 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime10 = domain.getLegalOverTime().v();
		this.legalTransferOverTime10 = domain.getLegalTransferOverTime().v();
	}

	/** KRCDT_MON_AGGR_PREM_TIME 10 **/

	public void toEntityPremiumTime1(AggregatePremiumTime domain) {
		this.premiumTime1 = domain.getTime().v();

	}

	public void toEntityPremiumTime2(AggregatePremiumTime domain) {
		this.premiumTime2 = domain.getTime().v();

	}

	public void toEntityPremiumTime3(AggregatePremiumTime domain) {
		this.premiumTime3 = domain.getTime().v();

	}

	public void toEntityPremiumTime4(AggregatePremiumTime domain) {
		this.premiumTime4 = domain.getTime().v();

	}

	public void toEntityPremiumTime5(AggregatePremiumTime domain) {
		this.premiumTime5 = domain.getTime().v();

	}

	public void toEntityPremiumTime6(AggregatePremiumTime domain) {
		this.premiumTime6 = domain.getTime().v();

	}

	public void toEntityPremiumTime7(AggregatePremiumTime domain) {
		this.premiumTime7 = domain.getTime().v();

	}

	public void toEntityPremiumTime8(AggregatePremiumTime domain) {
		this.premiumTime8 = domain.getTime().v();

	}

	public void toEntityPremiumTime9(AggregatePremiumTime domain) {
		this.premiumTime9 = domain.getTime().v();

	}

	public void toEntityPremiumTime10(AggregatePremiumTime domain) {
		this.premiumTime10 = domain.getTime().v();

	}

	/** KRCDT_MON_AGGR_SPEC_DAYS 10 **/

	public void toEntitySpecificDays1(AggregateSpecificDays domain) {
		this.specificDays1 = domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays1 = domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	public void toEntitySpecificDays2(AggregateSpecificDays domain) {
		this.specificDays2 = domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays2 = domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	public void toEntitySpecificDays3(AggregateSpecificDays domain) {
		this.specificDays3 = domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays3 = domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	public void toEntitySpecificDays4(AggregateSpecificDays domain) {
		this.specificDays4 = domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays4 = domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	public void toEntitySpecificDays5(AggregateSpecificDays domain) {
		this.specificDays5 = domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays5 = domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	public void toEntitySpecificDays6(AggregateSpecificDays domain) {
		this.specificDays6 = domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays6 = domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	public void toEntitySpecificDays7(AggregateSpecificDays domain) {
		this.specificDays7 = domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays7 = domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	public void toEntitySpecificDays8(AggregateSpecificDays domain) {
		this.specificDays8 = domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays8 = domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	public void toEntitySpecificDays9(AggregateSpecificDays domain) {
		this.specificDays9 = domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays9 = domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	public void toEntitySpecificDays10(AggregateSpecificDays domain) {
		this.specificDays10 = domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays10 = domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	/* KRCDT_MON_AGGR_TOTAL_SPT */
	public void toEntityTotalTimeSpentAtWork(AggregateTotalTimeSpentAtWork domain) {
		this.overTimeSpentAtWork = domain.getOverTimeSpentAtWork().v();
		this.midnightTimeSpentAtWork = domain.getMidnightTimeSpentAtWork().v();
		this.holidayTimeSpentAtWork = domain.getHolidayTimeSpentAtWork().v();
		this.varienceTimeSpentAtWork = domain.getVarienceTimeSpentAtWork().v();
		this.totalTimeSpentAtWork = domain.getTotalTimeSpentAtWork().v();
	}

	/* KRCDT_MON_AGGR_TOTAL_WRK */

	public void toEntityTotalWorkingTime(AggregateTotalWorkingTime domain) {
		this.workTime = domain.getWorkTime().getWorkTime().v();
		this.actualWorkTime = domain.getWorkTime().getActualWorkTime().v();
		this.withinPrescribedPremiumTime = domain.getWorkTime().getWithinPrescribedPremiumTime().v();
		this.schedulePrescribedWorkingTime = domain.getPrescribedWorkingTime().getSchedulePrescribedWorkingTime().v();
		this.recordPrescribedWorkingTime = domain.getPrescribedWorkingTime().getRecordPrescribedWorkingTime().v();
	}

	/* KRCDT_MON_ATTENDANCE_TIME */

	public void toEntityAttendanceTimeOfMonthly(AttendanceTimeOfMonthly domain) {
		val monthlyCalculation = domain.getMonthlyCalculation();
		this.startYmd = domain.getDatePeriod().start();
		this.endYmd = domain.getDatePeriod().end();
		this.aggregateDays = domain.getAggregateDays().v();
		this.statutoryWorkingTime = monthlyCalculation.getStatutoryWorkingTime().v();
		this.totalWorkingTime = monthlyCalculation.getTotalWorkingTime().v();
	}

	/* KRCDT_MON_FLEX_TIME */

	public void toEntityFlexTimeOfMonthly(FlexTimeOfMonthly domain) {

		val flexTime = domain.getFlexTime();
		val flexCarryForwardTime = domain.getFlexCarryforwardTime();
		val flexTimeOfExcessOutsideTime = domain.getFlexTimeOfExcessOutsideTime();
		val flexShortDeductTime = domain.getFlexShortDeductTime();

		this.flexTime = flexTime.getFlexTime().getTime().v();
		this.calcFlexTime = flexTime.getFlexTime().getCalcTime().v();
		this.beforeFlexTime = flexTime.getBeforeFlexTime().v();
		this.legalFlexTime = flexTime.getLegalFlexTime().v();
		this.illegalFlexTime = flexTime.getIllegalFlexTime().v();
		this.flexExcessTime = domain.getFlexExcessTime().v();
		this.flexShortageTime = domain.getFlexShortageTime().v();
		this.flexCarryforwardWorkTime = flexCarryForwardTime.getFlexCarryforwardWorkTime().v();
		this.flexCarryforwardTime = flexCarryForwardTime.getFlexCarryforwardTime().v();
		this.flexCarryforwardShortageTime = flexCarryForwardTime.getFlexCarryforwardShortageTime().v();
		this.excessFlexAtr = flexTimeOfExcessOutsideTime.getExcessFlexAtr().value;
		this.principleTime = flexTimeOfExcessOutsideTime.getPrincipleTime().v();
		this.forConvenienceTime = flexTimeOfExcessOutsideTime.getForConvenienceTime().v();
		this.annualLeaveDeductDays = flexShortDeductTime.getAnnualLeaveDeductDays().v();
		this.absenceDeductTime = flexShortDeductTime.getAbsenceDeductTime().v();
		this.shotTimeBeforeDeduct = flexShortDeductTime.getFlexShortTimeBeforeDeduct().v();
	}

	/* KRCDT_MON_HDWK_TIME */
	public void toEntityHolidayWorkTimeOfMonthly(HolidayWorkTimeOfMonthly domain) {

		this.totalHolidayWorkTime = domain.getTotalHolidayWorkTime().getTime().v();
		this.calcTotalHolidayWorkTime = domain.getTotalHolidayWorkTime().getCalcTime().v();
		this.beforeHolidayWorkTime = domain.getBeforeHolidayWorkTime().v();
		this.totalTransferTime = domain.getTotalTransferTime().getTime().v();
		this.calcTotalTransferTime = domain.getTotalTransferTime().getCalcTime().v();
	}

	/* KRCDT_MON_LEAVE - リポジトリ：月別実績の休業 only update */

	public void toEntityLeaveOfMonthly(LeaveOfMonthly domain) {
		this.prenatalLeaveDays = 0.0;
		this.postpartumLeaveDays = 0.0;
		this.childcareLeaveDays = 0.0;
		this.careLeaveDays = 0.0;
		this.injuryOrIllnessLeaveDays = 0.0;
		this.anyLeaveDays01 = 0.0;
		this.anyLeaveDays02 = 0.0;
		this.anyLeaveDays03 = 0.0;
		this.anyLeaveDays04 = 0.0;
		val fixLeaveDaysMap = domain.getFixLeaveDays();
		if (fixLeaveDaysMap.containsKey(CloseAtr.PRENATAL)) {
			this.prenatalLeaveDays = fixLeaveDaysMap.get(CloseAtr.PRENATAL).getDays().v();
		}
		if (fixLeaveDaysMap.containsKey(CloseAtr.POSTPARTUM)) {
			this.postpartumLeaveDays = fixLeaveDaysMap.get(CloseAtr.POSTPARTUM).getDays().v();
		}
		if (fixLeaveDaysMap.containsKey(CloseAtr.CHILD_CARE)) {
			this.childcareLeaveDays = fixLeaveDaysMap.get(CloseAtr.CHILD_CARE).getDays().v();
		}
		if (fixLeaveDaysMap.containsKey(CloseAtr.CARE)) {
			this.careLeaveDays = fixLeaveDaysMap.get(CloseAtr.CARE).getDays().v();
		}
		if (fixLeaveDaysMap.containsKey(CloseAtr.INJURY_OR_ILLNESS)) {
			this.injuryOrIllnessLeaveDays = fixLeaveDaysMap.get(CloseAtr.INJURY_OR_ILLNESS).getDays().v();
		}
		val anyLeaveDaysMap = domain.getAnyLeaveDays();
		if (anyLeaveDaysMap.containsKey(1)) {
			this.anyLeaveDays01 = anyLeaveDaysMap.get(1).getDays().v();
		}
		if (anyLeaveDaysMap.containsKey(2)) {
			this.anyLeaveDays02 = anyLeaveDaysMap.get(2).getDays().v();
		}
		if (anyLeaveDaysMap.containsKey(3)) {
			this.anyLeaveDays03 = anyLeaveDaysMap.get(3).getDays().v();
		}
		if (anyLeaveDaysMap.containsKey(4)) {
			this.anyLeaveDays04 = anyLeaveDaysMap.get(4).getDays().v();
		}
	}

	/* KRCDT_MON_MEDICAL_TIME */
	public void toEntityMedicalTimeOfMonthly(MedicalTimeOfMonthly domain) {
		this.dayNightAtr = domain.getDayNightAtr().value;
		this.workTime = domain.getWorkTime().v();
		this.deductionTime = domain.getDeducationTime().v();
		this.takeOverTime = domain.getTakeOverTime().v();
	}

	/* KRCDT_MON_OVER_TIME */
	public void toEntityOverTimeOfMonthly(OverTimeOfMonthly domain) {

		this.totalOverTime = domain.getTotalOverTime().getTime().v();
		this.calcTotalOverTime = domain.getTotalOverTime().getCalcTime().v();
		this.beforeOverTime = domain.getBeforeOverTime().v();
		this.totalTransferOverTime = domain.getTotalTransferOverTime().getTime().v();
		this.calcTotalTransferOverTime = domain.getTotalTransferOverTime().getCalcTime().v();
	}

	/* KRCDT_MON_REG_IRREG_TIME */
	public void toEntityRegAndIrreTimeOfMonth(RegularAndIrregularTimeOfMonthly domain) {

		val irregularWorkingTime = domain.getIrregularWorkingTime();

		this.weeklyTotalPremiumTime = domain.getWeeklyTotalPremiumTime().v();
		this.monthlyTotalPremiumTime = domain.getMonthlyTotalPremiumTime().v();
		this.multiMonthIrregularMiddleTime = irregularWorkingTime.getMultiMonthIrregularMiddleTime().v();
		this.irregularPeriodCarryforwardTime = irregularWorkingTime.getIrregularPeriodCarryforwardTime().v();
		this.irregularWorkingShortageTime = irregularWorkingTime.getIrregularWorkingShortageTime().v();
		this.irregularLegalOverTime = irregularWorkingTime.getIrregularLegalOverTime().getTime().v();
		this.calcIrregularLegalOverTime = irregularWorkingTime.getIrregularLegalOverTime().getCalcTime().v();
	}

	/* KRCDT_MON_VACT_USE_TIME */

	public void toEntityVacationUseTimeOfMonth(VacationUseTimeOfMonthly domain) {
		this.annualLeaveUseTime = domain.getAnnualLeave().getUseTime().v();
		this.retentionYearlyUseTime = domain.getRetentionYearly().getUseTime().v();
		this.specialHolidayUseTime = domain.getSpecialHoliday().getUseTime().v();
		this.compensatoryLeaveUseTime = domain.getCompensatoryLeave().getUseTime().v();

	}

	/* KRCDT_MON_VERTICAL_TOTAL */

	public void toEntityVerticalTotalOfMonthly(VerticalTotalOfMonthly domain) {

		val vtWorkDays = domain.getWorkDays();
		val vtWorkTime = domain.getWorkTime();

		this.workDays = vtWorkDays.getWorkDays().getDays().v();
		this.workTimes = vtWorkDays.getWorkTimes().getTimes().v();
		this.twoTimesWorkTimes = vtWorkDays.getTwoTimesWorkTimes().getTimes().v();
		this.temporaryWorkTimes = vtWorkDays.getTemporaryWorkTimes().getTimes().v();
		this.predetermineDays = vtWorkDays.getPredetermineDays().getPredeterminedDays().v();
		this.holidayDays = vtWorkDays.getHolidayDays().getDays().v();
		this.attendanceDays = vtWorkDays.getAttendanceDays().getDays().v();
		this.holidayWorkDays = vtWorkDays.getHolidayWorkDays().getDays().v();
		this.totalAbsenceDays = vtWorkDays.getAbsenceDays().getTotalAbsenceDays().v();
		this.totalAbsenceTime = vtWorkDays.getAbsenceDays().getTotalAbsenceTime().v();
		this.payAttendanceDays = vtWorkDays.getPayDays().getPayAttendanceDays().v();
		this.payAbsenceDays = vtWorkDays.getPayDays().getPayAbsenceDays().v();

		this.childcareGoOutTimes = 0;
		this.childcareGoOutTime = 0;
		this.careGoOutTimes = 0;
		this.careGoOutTime = 0;
		val goOutForChildCares = vtWorkTime.getGoOut().getGoOutForChildCares();
		if (goOutForChildCares.containsKey(ChildCareAtr.CHILD_CARE)) {
			val goOutForChildCare = goOutForChildCares.get(ChildCareAtr.CHILD_CARE);
			this.childcareGoOutTimes = goOutForChildCare.getTimes().v();
			this.childcareGoOutTime = goOutForChildCare.getTime().v();
		}
		if (goOutForChildCares.containsKey(ChildCareAtr.CARE)) {
			val goOutForCare = goOutForChildCares.get(ChildCareAtr.CARE);
			this.careGoOutTimes = goOutForCare.getTimes().v();
			this.careGoOutTime = goOutForCare.getTime().v();
		}

		this.premiumMidnightTime = vtWorkTime.getPremiumTime().getMidnightTime().v();
		this.premiumLegalOutsideWorkTime = vtWorkTime.getPremiumTime().getLegalOutsideWorkTime().v();
		this.premiumIllegalOutsideWorkTime = vtWorkTime.getPremiumTime().getIllegalOutsideWorkTime().v();
		this.premiumLegalHolidayWorkTime = vtWorkTime.getPremiumTime().getLegalHolidayWorkTime().v();
		this.premiumIllegalHolidayWorkTime = vtWorkTime.getPremiumTime().getIllegalHolidayWorkTime().v();
		this.breakTime = vtWorkTime.getBreakTime().getBreakTime().v();
		this.legalHolidayTime = vtWorkTime.getHolidayTime().getLegalHolidayTime().v();
		this.illegalHolidayTime = vtWorkTime.getHolidayTime().getIllegalHolidayTime().v();
		this.illegalSpecialHolidayTime = vtWorkTime.getHolidayTime().getIllegalSpecialHolidayTime().v();
		this.overWorkMidnightTime = vtWorkTime.getMidnightTime().getOverWorkMidnightTime().getTime().v();
		this.calcOverWorkMidnightTime = vtWorkTime.getMidnightTime().getOverWorkMidnightTime().getCalcTime().v();
		this.legalMidnightTime = vtWorkTime.getMidnightTime().getLegalMidnightTime().getTime().v();
		this.calcLegalMidnightTime = vtWorkTime.getMidnightTime().getLegalMidnightTime().getCalcTime().v();
		this.illegalMidnightTime = vtWorkTime.getMidnightTime().getIllegalMidnightTime().getTime().getTime().v();
		this.calcIllegalMidnightTime = vtWorkTime.getMidnightTime().getIllegalMidnightTime().getTime().getCalcTime()
				.v();
		this.illegalBeforeMidnightTime = vtWorkTime.getMidnightTime().getIllegalMidnightTime().getBeforeTime().v();
		this.legalHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getLegalHolidayWorkMidnightTime().getTime()
				.v();
		this.calcLegalHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getLegalHolidayWorkMidnightTime()
				.getCalcTime().v();
		this.illegalHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getIllegalHolidayWorkMidnightTime().getTime()
				.v();
		this.calcIllegalHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getIllegalHolidayWorkMidnightTime()
				.getCalcTime().v();
		this.specialHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getSpecialHolidayWorkMidnightTime().getTime()
				.v();
		this.calcSpecialHolidayWorkMidnightTime = vtWorkTime.getMidnightTime().getSpecialHolidayWorkMidnightTime()
				.getCalcTime().v();
		this.lateTimes = vtWorkTime.getLateLeaveEarly().getLate().getTimes().v();
		this.lateTime = vtWorkTime.getLateLeaveEarly().getLate().getTime().getTime().v();
		this.calcLateTime = vtWorkTime.getLateLeaveEarly().getLate().getTime().getCalcTime().v();
		this.leaveEarlyTimes = vtWorkTime.getLateLeaveEarly().getLeaveEarly().getTimes().v();
		this.leaveEarlyTime = vtWorkTime.getLateLeaveEarly().getLeaveEarly().getTime().getTime().v();
		this.calcLeaveEarlyTime = vtWorkTime.getLateLeaveEarly().getLeaveEarly().getTime().getCalcTime().v();
		this.attendanceLeaveGateBeforeAttendanceTime = vtWorkTime.getAttendanceLeaveGateTime().getTimeBeforeAttendance()
				.v();
		this.attendanceLeaveGateAfterLeaveWorkTime = vtWorkTime.getAttendanceLeaveGateTime().getTimeAfterLeaveWork()
				.v();
		this.attendanceLeaveGateStayingTime = vtWorkTime.getAttendanceLeaveGateTime().getStayingTime().v();
		this.attendanceLeaveGateUnemployedTime = vtWorkTime.getAttendanceLeaveGateTime().getUnemployedTime().v();
		this.budgetVarienceTime = vtWorkTime.getBudgetTimeVarience().getTime().v();
	}

	/* KRCDT_MON_EXCESS_OUTSIDE 50 */

	public void toEntityExcessOutsideWorkOfMonthly1(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime1 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime1 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime1 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly2(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime2 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime2 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime2 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly3(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime3 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime3 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime3 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly4(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime4 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime4 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime4 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly5(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime5 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime5 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime5 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly6(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime6 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime6 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime6 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly7(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime7 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime7 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime7 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly8(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime8 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime8 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime8 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly9(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime9 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime9 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime9 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly10(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime10 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime10 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime10 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly11(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime11 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime11 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime11 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly12(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime12 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime12 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime12 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly13(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime13 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime13 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime13 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly14(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime14 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime14 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime14 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly15(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime15 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime15 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime15 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly16(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime16 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime16 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime16 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly17(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime17 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime17 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime17 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly18(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime18 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime18 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime18 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly19(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime19 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime19 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime19 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly20(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime20 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime20 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime20 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly21(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime21 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime21 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime21 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly22(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime22 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime22 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime22 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly23(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime23 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime23 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime23 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly24(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime24 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime24 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime24 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly25(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime25 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime25 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime25 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly26(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime26 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime26 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime26 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly27(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime27 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime27 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime27 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly28(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime28 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime28 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime28 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly29(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime29 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime29 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime29 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly30(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime30 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime30 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime30 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly31(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime31 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime31 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime31 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly32(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime32 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime32 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime32 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly33(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime33 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime33 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime33 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly34(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime34 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime34 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime34 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly35(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime35 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime35 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime35 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly36(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime36 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime36 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime36 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly37(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime37 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime37 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime37 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly38(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime38 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime38 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime38 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly39(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime39 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime39 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime39 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly40(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime40 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime40 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime40 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly41(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime41 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime41 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime41 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly42(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime42 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime42 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime42 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly43(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime43 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime43 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime43 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly44(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime44 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime44 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime44 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly45(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime45 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime45 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime45 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly46(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime46 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime46 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime46 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly47(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime47 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime47 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime47 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly48(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime48 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime48 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime48 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly49(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime49 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime49 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime49 = domain.getDeformationCarryforwardTime().v();
	}

	public void toEntityExcessOutsideWorkOfMonthly50(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime50 = domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime50 = domain.getMonthlyTotalPremiumTime().v();
		this.deformationCarryforwardTime50 = domain.getDeformationCarryforwardTime().v();
	}

	/* KRCDT_MON_EXCOUT_TIME */
	public void toEntityExcessOutsideWork1(ExcessOutsideWork domain) {
		this.excessTime1 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork2(ExcessOutsideWork domain) {
		this.excessTime2 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork3(ExcessOutsideWork domain) {
		this.excessTime3 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork4(ExcessOutsideWork domain) {
		this.excessTime4 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork5(ExcessOutsideWork domain) {
		this.excessTime5 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork6(ExcessOutsideWork domain) {
		this.excessTime6 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork7(ExcessOutsideWork domain) {
		this.excessTime7 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork8(ExcessOutsideWork domain) {
		this.excessTime8 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork9(ExcessOutsideWork domain) {
		this.excessTime9 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork10(ExcessOutsideWork domain) {
		this.excessTime10 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork11(ExcessOutsideWork domain) {
		this.excessTime11 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork12(ExcessOutsideWork domain) {
		this.excessTime12 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork13(ExcessOutsideWork domain) {
		this.excessTime13 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork14(ExcessOutsideWork domain) {
		this.excessTime14 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork15(ExcessOutsideWork domain) {
		this.excessTime15 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork16(ExcessOutsideWork domain) {
		this.excessTime16 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork17(ExcessOutsideWork domain) {
		this.excessTime17 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork18(ExcessOutsideWork domain) {
		this.excessTime18 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork19(ExcessOutsideWork domain) {
		this.excessTime19 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork20(ExcessOutsideWork domain) {
		this.excessTime20 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork21(ExcessOutsideWork domain) {
		this.excessTime21 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork22(ExcessOutsideWork domain) {
		this.excessTime22 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork23(ExcessOutsideWork domain) {
		this.excessTime23 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork24(ExcessOutsideWork domain) {
		this.excessTime24 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork25(ExcessOutsideWork domain) {
		this.excessTime25 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork26(ExcessOutsideWork domain) {
		this.excessTime26 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork27(ExcessOutsideWork domain) {
		this.excessTime27 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork28(ExcessOutsideWork domain) {
		this.excessTime28 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork29(ExcessOutsideWork domain) {
		this.excessTime29 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork30(ExcessOutsideWork domain) {
		this.excessTime30 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork31(ExcessOutsideWork domain) {
		this.excessTime31 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork32(ExcessOutsideWork domain) {
		this.excessTime32 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork33(ExcessOutsideWork domain) {
		this.excessTime33 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork34(ExcessOutsideWork domain) {
		this.excessTime34 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork35(ExcessOutsideWork domain) {
		this.excessTime35 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork36(ExcessOutsideWork domain) {
		this.excessTime36 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork37(ExcessOutsideWork domain) {
		this.excessTime37 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork38(ExcessOutsideWork domain) {
		this.excessTime38 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork39(ExcessOutsideWork domain) {
		this.excessTime39 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork40(ExcessOutsideWork domain) {
		this.excessTime40 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork41(ExcessOutsideWork domain) {
		this.excessTime41 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork42(ExcessOutsideWork domain) {
		this.excessTime42 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork43(ExcessOutsideWork domain) {
		this.excessTime43 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork44(ExcessOutsideWork domain) {
		this.excessTime44 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork45(ExcessOutsideWork domain) {
		this.excessTime45 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork46(ExcessOutsideWork domain) {
		this.excessTime46 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork47(ExcessOutsideWork domain) {
		this.excessTime47 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork48(ExcessOutsideWork domain) {
		this.excessTime48 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork49(ExcessOutsideWork domain) {
		this.excessTime49 = domain.getExcessTime().v();
	}

	public void toEntityExcessOutsideWork50(ExcessOutsideWork domain) {
		this.excessTime50 = domain.getExcessTime().v();
	}

	/* KRCDT_MON_AGREEMENT_TIME */
	public void toEntityAgreementTimeOfMonthly(AgreementTimeOfMonthly domain) {
		this.agreementTime = domain.getAgreementTime().v();
		this.limitErrorTime = domain.getLimitErrorTime().v();
		this.limitAlarmTime = domain.getLimitAlarmTime().v();
		this.exceptionLimitErrorTime = (domain.getExceptionLimitErrorTime().isPresent()
				? domain.getExceptionLimitErrorTime().get().v()
				: null);
		this.exceptionLimitAlarmTime = (domain.getExceptionLimitAlarmTime().isPresent()
				? domain.getExceptionLimitAlarmTime().get().v()
				: null);
		this.status = domain.getStatus().value;
	}

	/* KRCDT_MON_AFFILIATION */

	public void toEntityAffiliationInfoOfMonthly(AffiliationInfoOfMonthly domain) {

		this.firstEmploymentCd = domain.getFirstInfo().getEmploymentCd().v();
		this.firstWorkplaceId = domain.getFirstInfo().getWorkplaceId().v();
		this.firstJobTitleId = domain.getFirstInfo().getJobTitleId().v();
		this.firstClassCd = domain.getFirstInfo().getClassCd().v();
		this.firstBusinessTypeCd = domain.getFirstInfo().getBusinessTypeCd().v();
		this.lastEmploymentCd = domain.getLastInfo().getEmploymentCd().v();
		this.lastWorkplaceId = domain.getLastInfo().getWorkplaceId().v();
		this.lastJobTitleId = domain.getLastInfo().getJobTitleId().v();
		this.lastClassCd = domain.getLastInfo().getClassCd().v();
		this.lastBusinessTypeCd = domain.getLastInfo().getBusinessTypeCd().v();

	}

	// public AggregateAbsenceDays toDomainAbsenceDays1() {
	// //TODO wrong type
	// return AggregateAbsenceDays.of(
	// new AttendanceDaysMonth(this.absenceDayNo1),
	// new AttendanceTimeMonth(this.absenceTimeNo1));
	// }

	public AggregateBonusPayTimeMerge toDomainBonusPayTimeMerge() {
		AggregateBonusPayTimeMerge merge = new AggregateBonusPayTimeMerge();
		merge.setPaytime1(toDomainBonusPayTime1());
		merge.setPaytime2(toDomainBonusPayTime2());
		merge.setPaytime3(toDomainBonusPayTime3());
		merge.setPaytime4(toDomainBonusPayTime4());
		merge.setPaytime5(toDomainBonusPayTime5());
		merge.setPaytime6(toDomainBonusPayTime6());
		merge.setPaytime7(toDomainBonusPayTime7());
		merge.setPaytime8(toDomainBonusPayTime8());
		merge.setPaytime9(toDomainBonusPayTime9());
		merge.setPaytime10(toDomainBonusPayTime10());
		return merge;
	}

	public AggregateDivergenceTimeMerge toDomainDivergenceTimeMerge() {
		AggregateDivergenceTimeMerge merge = new AggregateDivergenceTimeMerge();
		merge.setDivergenceTime1(toDomainDivergenceTime1());
		merge.setDivergenceTime2(toDomainDivergenceTime2());
		merge.setDivergenceTime3(toDomainDivergenceTime3());
		merge.setDivergenceTime4(toDomainDivergenceTime4());
		merge.setDivergenceTime5(toDomainDivergenceTime5());
		merge.setDivergenceTime6(toDomainDivergenceTime6());
		merge.setDivergenceTime7(toDomainDivergenceTime7());
		merge.setDivergenceTime8(toDomainDivergenceTime8());
		merge.setDivergenceTime9(toDomainDivergenceTime9());
		merge.setDivergenceTime10(toDomainDivergenceTime10());
		return merge;
	}

	public AggregateGoOutMerge toDomainGoOut() {
		AggregateGoOutMerge merge = new AggregateGoOutMerge();
		merge.setGoOut1(toDomainGoOut1());
		merge.setGoOut2(toDomainGoOut2());
		merge.setGoOut3(toDomainGoOut3());
		merge.setGoOut4(toDomainGoOut4());
		return merge;
	}
	
	public AggregateHolidayWorkTimeMerge toDomainHolidayWorkTimeMerge() {
		AggregateHolidayWorkTimeMerge merge = new AggregateHolidayWorkTimeMerge();
		merge.setHolidayWorkTime1(toDomainHolidayWorkTime1());
		merge.setHolidayWorkTime2(toDomainHolidayWorkTime2());
		merge.setHolidayWorkTime3(toDomainHolidayWorkTime3());
		merge.setHolidayWorkTime4(toDomainHolidayWorkTime4());
		merge.setHolidayWorkTime5(toDomainHolidayWorkTime5());
		merge.setHolidayWorkTime6(toDomainHolidayWorkTime6());
		merge.setHolidayWorkTime7(toDomainHolidayWorkTime7());
		merge.setHolidayWorkTime8(toDomainHolidayWorkTime8());
		merge.setHolidayWorkTime9(toDomainHolidayWorkTime9());
		merge.setHolidayWorkTime10(toDomainHolidayWorkTime10());

		return merge;
	}
	
	public AggregateOverTimeMerge toDomainOverTimeMerge() {
		AggregateOverTimeMerge merge = new AggregateOverTimeMerge();
		merge.setOverTime1(toDomainOverTime1());
		merge.setOverTime2(toDomainOverTime2());
		merge.setOverTime3(toDomainOverTime3());
		merge.setOverTime4(toDomainOverTime4());
		merge.setOverTime5(toDomainOverTime5());
		merge.setOverTime6(toDomainOverTime6());
		merge.setOverTime7(toDomainOverTime7());
		merge.setOverTime8(toDomainOverTime8());
		merge.setOverTime9(toDomainOverTime9());
		merge.setOverTime10(toDomainOverTime10());
		return merge;
	}
	
	public AggregatePremiumTimeMerge toDomainPremiumTimeMerge() {
		AggregatePremiumTimeMerge merge = new AggregatePremiumTimeMerge();
		merge.setPremiumTime1(toDomainPremiumTime1());
		merge.setPremiumTime2(toDomainPremiumTime2());
		merge.setPremiumTime3(toDomainPremiumTime3());
		merge.setPremiumTime4(toDomainPremiumTime4());
		merge.setPremiumTime5(toDomainPremiumTime5());
		merge.setPremiumTime6(toDomainPremiumTime6());
		merge.setPremiumTime7(toDomainPremiumTime7());
		merge.setPremiumTime8(toDomainPremiumTime8());
		merge.setPremiumTime9(toDomainPremiumTime9());
		merge.setPremiumTime10(toDomainPremiumTime10());
		return merge;
	}
	
	public AggregateSpecificDaysMerge toDomainSpecificDaysMerge() {
		AggregateSpecificDaysMerge merge = new AggregateSpecificDaysMerge();
		merge.setSpecificDays1(toDomainSpecificDays1());
		merge.setSpecificDays2(toDomainSpecificDays2());
		merge.setSpecificDays3(toDomainSpecificDays3());
		merge.setSpecificDays4(toDomainSpecificDays4());
		merge.setSpecificDays5(toDomainSpecificDays5());
		merge.setSpecificDays6(toDomainSpecificDays6());
		merge.setSpecificDays7(toDomainSpecificDays7());
		merge.setSpecificDays8(toDomainSpecificDays8());
		merge.setSpecificDays9(toDomainSpecificDays9());
		merge.setSpecificDays10(toDomainSpecificDays10());
		return merge;
	}


	/**
	 * ドメインに変換
	 * 
	 * @return 集計加給時間
	 */
	public AggregateBonusPayTime toDomainBonusPayTime1() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime1),
				new AttendanceTimeMonth(this.specificBonusPayTime1),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime1),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime1));
	}

	public AggregateBonusPayTime toDomainBonusPayTime2() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime2),
				new AttendanceTimeMonth(this.specificBonusPayTime2),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime2),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime2));
	}

	public AggregateBonusPayTime toDomainBonusPayTime3() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime3),
				new AttendanceTimeMonth(this.specificBonusPayTime3),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime3),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime3));
	}

	public AggregateBonusPayTime toDomainBonusPayTime4() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime4),
				new AttendanceTimeMonth(this.specificBonusPayTime4),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime4),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime4));
	}

	public AggregateBonusPayTime toDomainBonusPayTime5() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime5),
				new AttendanceTimeMonth(this.specificBonusPayTime5),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime5),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime5));
	}

	public AggregateBonusPayTime toDomainBonusPayTime6() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime6),
				new AttendanceTimeMonth(this.specificBonusPayTime6),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime6),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime6));
	}

	public AggregateBonusPayTime toDomainBonusPayTime7() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime7),
				new AttendanceTimeMonth(this.specificBonusPayTime7),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime7),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime7));
	}

	public AggregateBonusPayTime toDomainBonusPayTime8() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime8),
				new AttendanceTimeMonth(this.specificBonusPayTime8),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime8),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime8));
	}

	public AggregateBonusPayTime toDomainBonusPayTime9() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime9),
				new AttendanceTimeMonth(this.specificBonusPayTime9),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime9),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime9));
	}

	public AggregateBonusPayTime toDomainBonusPayTime10() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime10),
				new AttendanceTimeMonth(this.specificBonusPayTime10),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime10),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime10));
	}

	/**
	 * ドメインに変換
	 * 
	 * @return 集計乖離時間
	 */
	public AggregateDivergenceTime toDomainDivergenceTime1() {
		return AggregateDivergenceTime.of(
				new AttendanceTimeMonth(this.divergenceTime1),
				new AttendanceTimeMonth(this.deductionTime1),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction1),
				EnumAdaptor.valueOf(this.divergenceAtr1, DivergenceAtrOfMonthly.class));
	}

	public AggregateDivergenceTime toDomainDivergenceTime2() {
		return AggregateDivergenceTime.of(
				new AttendanceTimeMonth(this.divergenceTime2),
				new AttendanceTimeMonth(this.deductionTime2),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction2),
				EnumAdaptor.valueOf(this.divergenceAtr2, DivergenceAtrOfMonthly.class));
	}

	public AggregateDivergenceTime toDomainDivergenceTime3() {
		return AggregateDivergenceTime.of(
				new AttendanceTimeMonth(this.divergenceTime3),
				new AttendanceTimeMonth(this.deductionTime3),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction3),
				EnumAdaptor.valueOf(this.divergenceAtr3, DivergenceAtrOfMonthly.class));
	}

	public AggregateDivergenceTime toDomainDivergenceTime4() {
		return AggregateDivergenceTime.of(
				new AttendanceTimeMonth(this.divergenceTime4),
				new AttendanceTimeMonth(this.deductionTime4),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction4),
				EnumAdaptor.valueOf(this.divergenceAtr4, DivergenceAtrOfMonthly.class));
	}

	public AggregateDivergenceTime toDomainDivergenceTime5() {
		return AggregateDivergenceTime.of(
				new AttendanceTimeMonth(this.divergenceTime5),
				new AttendanceTimeMonth(this.deductionTime5),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction5),
				EnumAdaptor.valueOf(this.divergenceAtr5, DivergenceAtrOfMonthly.class));
	}

	public AggregateDivergenceTime toDomainDivergenceTime6() {
		return AggregateDivergenceTime.of(
				new AttendanceTimeMonth(this.divergenceTime6),
				new AttendanceTimeMonth(this.deductionTime6),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction6),
				EnumAdaptor.valueOf(this.divergenceAtr6, DivergenceAtrOfMonthly.class));
	}

	public AggregateDivergenceTime toDomainDivergenceTime7() {
		return AggregateDivergenceTime.of(
				new AttendanceTimeMonth(this.divergenceTime7),
				new AttendanceTimeMonth(this.deductionTime7),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction7),
				EnumAdaptor.valueOf(this.divergenceAtr7, DivergenceAtrOfMonthly.class));
	}

	public AggregateDivergenceTime toDomainDivergenceTime8() {
		return AggregateDivergenceTime.of(
				new AttendanceTimeMonth(this.divergenceTime8),
				new AttendanceTimeMonth(this.deductionTime8),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction8),
				EnumAdaptor.valueOf(this.divergenceAtr8, DivergenceAtrOfMonthly.class));
	}

	public AggregateDivergenceTime toDomainDivergenceTime9() {
		return AggregateDivergenceTime.of(
				new AttendanceTimeMonth(this.divergenceTime9),
				new AttendanceTimeMonth(this.deductionTime9),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction9),
				EnumAdaptor.valueOf(this.divergenceAtr9, DivergenceAtrOfMonthly.class));
	}

	public AggregateDivergenceTime toDomainDivergenceTime10() {
		return AggregateDivergenceTime.of(
				new AttendanceTimeMonth(this.divergenceTime10),
				new AttendanceTimeMonth(this.deductionTime10),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction10),
				EnumAdaptor.valueOf(this.divergenceAtr10, DivergenceAtrOfMonthly.class));
	}

	public AggregateGoOut toDomainGoOut1() {
		return AggregateGoOut.of(
				new AttendanceTimesMonth(this.goOutTimes1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.legalTime1),
						new AttendanceTimeMonth(this.calcLegalTime1)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.illegalTime1),
						new AttendanceTimeMonth(this.calcIllegalTime1)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTime1),
						new AttendanceTimeMonth(this.calcTotalTime1)));
	}

	public AggregateGoOut toDomainGoOut2() {
		return AggregateGoOut.of(new AttendanceTimesMonth(this.goOutTimes2),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.legalTime2),
						new AttendanceTimeMonth(this.calcLegalTime2)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.illegalTime2),
						new AttendanceTimeMonth(this.calcIllegalTime2)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTime2),
						new AttendanceTimeMonth(this.calcTotalTime2)));
	}

	public AggregateGoOut toDomainGoOut3() {
		return AggregateGoOut.of(
				new AttendanceTimesMonth(this.goOutTimes3),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.legalTime3),
						new AttendanceTimeMonth(this.calcLegalTime3)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.illegalTime3),
						new AttendanceTimeMonth(this.calcIllegalTime3)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTime3),
						new AttendanceTimeMonth(this.calcTotalTime3)));
	}

	public AggregateGoOut toDomainGoOut4() {
		return AggregateGoOut.of(
				new AttendanceTimesMonth(this.goOutTimes4),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.legalTime4),
						new AttendanceTimeMonth(this.calcLegalTime4)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.illegalTime4),
						new AttendanceTimeMonth(this.calcIllegalTime4)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTime4),
						new AttendanceTimeMonth(this.calcTotalTime4)));
	}

	/**
	 * ドメインに変換
	 * @return 集計休出時間
	 */
	
	public AggregateHolidayWorkTime toDomainHolidayWorkTime1() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(1),
		new TimeMonthWithCalculation(
		new AttendanceTimeMonth(this.holidayWorkTime1),
		new AttendanceTimeMonth(this.calcHolidayWorkTime1)),
		new AttendanceTimeMonth(this.beforeHolidayWorkTime1),
		new TimeMonthWithCalculation(
		new AttendanceTimeMonth(this.transferTime1),
		new AttendanceTimeMonth(this.calcTransferTime1)),
		new AttendanceTimeMonth(this.legalHolidayWorkTime1),
		new AttendanceTimeMonth(this.legalTransferHolidayWorkTime1));
		}

	public AggregateHolidayWorkTime toDomainHolidayWorkTime2() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(1),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.holidayWorkTime2),
			new AttendanceTimeMonth(this.calcHolidayWorkTime2)),
		new AttendanceTimeMonth(this.beforeHolidayWorkTime2),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.transferTime2),
			new AttendanceTimeMonth(this.calcTransferTime2)),
		new AttendanceTimeMonth(this.legalHolidayWorkTime2),
		new AttendanceTimeMonth(this.legalTransferHolidayWorkTime2));
		}

	public AggregateHolidayWorkTime toDomainHolidayWorkTime3() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(1),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.holidayWorkTime3),
			new AttendanceTimeMonth(this.calcHolidayWorkTime3)),
		new AttendanceTimeMonth(this.beforeHolidayWorkTime3),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.transferTime3),
			new AttendanceTimeMonth(this.calcTransferTime3)),
		new AttendanceTimeMonth(this.legalHolidayWorkTime3),
		new AttendanceTimeMonth(this.legalTransferHolidayWorkTime3));
		}

	public AggregateHolidayWorkTime toDomainHolidayWorkTime4() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(1),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.holidayWorkTime4),
			new AttendanceTimeMonth(this.calcHolidayWorkTime4)),
		new AttendanceTimeMonth(this.beforeHolidayWorkTime4),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.transferTime4),
			new AttendanceTimeMonth(this.calcTransferTime4)),
		new AttendanceTimeMonth(this.legalHolidayWorkTime4),
		new AttendanceTimeMonth(this.legalTransferHolidayWorkTime4));
		}

	public AggregateHolidayWorkTime toDomainHolidayWorkTime5() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(1),
		new TimeMonthWithCalculation(
		new AttendanceTimeMonth(this.holidayWorkTime5),
			new AttendanceTimeMonth(this.calcHolidayWorkTime5)),
			new AttendanceTimeMonth(this.beforeHolidayWorkTime5),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.transferTime5),
			new AttendanceTimeMonth(this.calcTransferTime5)),
		new AttendanceTimeMonth(this.legalHolidayWorkTime5),
		new AttendanceTimeMonth(this.legalTransferHolidayWorkTime5));
		}

	public AggregateHolidayWorkTime toDomainHolidayWorkTime6() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(1),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.holidayWorkTime6),
			new AttendanceTimeMonth(this.calcHolidayWorkTime6)),
		new AttendanceTimeMonth(this.beforeHolidayWorkTime6),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.transferTime6),
			new AttendanceTimeMonth(this.calcTransferTime6)),
		new AttendanceTimeMonth(this.legalHolidayWorkTime6),
		new AttendanceTimeMonth(this.legalTransferHolidayWorkTime6));
		}

	public AggregateHolidayWorkTime toDomainHolidayWorkTime7() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(1),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.holidayWorkTime7),
			new AttendanceTimeMonth(this.calcHolidayWorkTime7)),
		new AttendanceTimeMonth(this.beforeHolidayWorkTime7),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.transferTime7),
			new AttendanceTimeMonth(this.calcTransferTime7)),
		new AttendanceTimeMonth(this.legalHolidayWorkTime7),
		new AttendanceTimeMonth(this.legalTransferHolidayWorkTime7));
		}

	public AggregateHolidayWorkTime toDomainHolidayWorkTime8() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(1),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.holidayWorkTime8),
			new AttendanceTimeMonth(this.calcHolidayWorkTime8)),
		new AttendanceTimeMonth(this.beforeHolidayWorkTime8),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.transferTime8),
			new AttendanceTimeMonth(this.calcTransferTime8)),
		new AttendanceTimeMonth(this.legalHolidayWorkTime8),
		new AttendanceTimeMonth(this.legalTransferHolidayWorkTime8));
		}

	public AggregateHolidayWorkTime toDomainHolidayWorkTime9() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(1),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.holidayWorkTime9),
			new AttendanceTimeMonth(this.calcHolidayWorkTime9)),
		new AttendanceTimeMonth(this.beforeHolidayWorkTime9),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.transferTime9),
			new AttendanceTimeMonth(this.calcTransferTime9)),
		new AttendanceTimeMonth(this.legalHolidayWorkTime9),
		new AttendanceTimeMonth(this.legalTransferHolidayWorkTime9));
	}

	public AggregateHolidayWorkTime toDomainHolidayWorkTime10() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(1),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.holidayWorkTime10),
			new AttendanceTimeMonth(this.calcHolidayWorkTime10)),
		new AttendanceTimeMonth(this.beforeHolidayWorkTime10),
		new TimeMonthWithCalculation(
			new AttendanceTimeMonth(this.transferTime10),
			new AttendanceTimeMonth(this.calcTransferTime10)),
		new AttendanceTimeMonth(this.legalHolidayWorkTime10),
		new AttendanceTimeMonth(this.legalTransferHolidayWorkTime10));
	}
	
	/** KRCDT_MON_AGGR_OVER_TIME 10 **/
	/**
	 * ドメインに変換
	 * @return 集計残業時間
	 */
//	public AggregateOverTime toDomain(){
//		
//		return AggregateOverTime.of(
//				new OverTimeFrameNo(1),
//				new TimeMonthWithCalculation(
//						new AttendanceTimeMonth(this.overTime1),
//						new AttendanceTimeMonth(this.calcOverTime1)),
//				new AttendanceTimeMonth(this.beforeOverTime1),
//				new TimeMonthWithCalculation(
//						new AttendanceTimeMonth(this.transferOverTime1),
//						new AttendanceTimeMonth(this.calcTransferOverTime1)),
//				new AttendanceTimeMonth(this.legalOverTime1),
//				new AttendanceTimeMonth(this.legalTransferOverTime1));
//	}

	public AggregateOverTime toDomainOverTime1() {
		return AggregateOverTime.of(new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime1), 
						new AttendanceTimeMonth(this.calcOverTime1)),
				new AttendanceTimeMonth(this.beforeOverTime1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.transferOverTime1),
						new AttendanceTimeMonth(this.calcTransferOverTime1)),
				new AttendanceTimeMonth(this.legalOverTime1), 
				new AttendanceTimeMonth(this.legalTransferOverTime1));
	}

	public AggregateOverTime toDomainOverTime2() {
		return AggregateOverTime.of(new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime2), 
						new AttendanceTimeMonth(this.calcOverTime2)),
				new AttendanceTimeMonth(this.beforeOverTime2),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.transferOverTime2),
						new AttendanceTimeMonth(this.calcTransferOverTime2)),
				new AttendanceTimeMonth(this.legalOverTime2), 
				new AttendanceTimeMonth(this.legalTransferOverTime2));
	}

	public AggregateOverTime toDomainOverTime3() {
		return AggregateOverTime.of(new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime3), 
						new AttendanceTimeMonth(this.calcOverTime3)),
				new AttendanceTimeMonth(this.beforeOverTime3),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.transferOverTime3),
						new AttendanceTimeMonth(this.calcTransferOverTime3)),
				new AttendanceTimeMonth(this.legalOverTime3), 
				new AttendanceTimeMonth(this.legalTransferOverTime3));
	}

	public AggregateOverTime toDomainOverTime4() {
		return AggregateOverTime.of(new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime4), 
						new AttendanceTimeMonth(this.calcOverTime4)),
				new AttendanceTimeMonth(this.beforeOverTime4),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.transferOverTime4),
						new AttendanceTimeMonth(this.calcTransferOverTime4)),
				new AttendanceTimeMonth(this.legalOverTime4), 
				new AttendanceTimeMonth(this.legalTransferOverTime4));
	}

	public AggregateOverTime toDomainOverTime5() {
		return AggregateOverTime.of(new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime5), 
						new AttendanceTimeMonth(this.calcOverTime5)),
				new AttendanceTimeMonth(this.beforeOverTime5),
				new TimeMonthWithCalculation(new AttendanceTimeMonth(this.transferOverTime5),
						new AttendanceTimeMonth(this.calcTransferOverTime5)),
				new AttendanceTimeMonth(this.legalOverTime5),
				new AttendanceTimeMonth(this.legalTransferOverTime5));
	}

	public AggregateOverTime toDomainOverTime6() {
		return AggregateOverTime.of(new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime6), 
						new AttendanceTimeMonth(this.calcOverTime6)),
				new AttendanceTimeMonth(this.beforeOverTime6),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.transferOverTime6),
						new AttendanceTimeMonth(this.calcTransferOverTime6)),
				new AttendanceTimeMonth(this.legalOverTime6), 
				new AttendanceTimeMonth(this.legalTransferOverTime6));
	}

	public AggregateOverTime toDomainOverTime7() {
		return AggregateOverTime.of(new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime7), 
						new AttendanceTimeMonth(this.calcOverTime7)),
				new AttendanceTimeMonth(this.beforeOverTime7),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.transferOverTime7),
						new AttendanceTimeMonth(this.calcTransferOverTime7)),
				new AttendanceTimeMonth(this.legalOverTime7), 
				new AttendanceTimeMonth(this.legalTransferOverTime7));
	}

	public AggregateOverTime toDomainOverTime8() {
		return AggregateOverTime.of(new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime8), 
						new AttendanceTimeMonth(this.calcOverTime8)),
				new AttendanceTimeMonth(this.beforeOverTime8),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.transferOverTime8),
						new AttendanceTimeMonth(this.calcTransferOverTime8)),
				new AttendanceTimeMonth(this.legalOverTime8), 
				new AttendanceTimeMonth(this.legalTransferOverTime8));
	}

	public AggregateOverTime toDomainOverTime9() {
		return AggregateOverTime.of(new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime9), 
						new AttendanceTimeMonth(this.calcOverTime9)),
				new AttendanceTimeMonth(this.beforeOverTime9),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.transferOverTime9),
						new AttendanceTimeMonth(this.calcTransferOverTime9)),
				new AttendanceTimeMonth(this.legalOverTime9), 
				new AttendanceTimeMonth(this.legalTransferOverTime9));
	}

	public AggregateOverTime toDomainOverTime10() {
		return AggregateOverTime.of(new OverTimeFrameNo(1),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime10), 
						new AttendanceTimeMonth(this.calcOverTime10)),
				new AttendanceTimeMonth(this.beforeOverTime10),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.transferOverTime10),
						new AttendanceTimeMonth(this.calcTransferOverTime10)),
				new AttendanceTimeMonth(this.legalOverTime10),
				new AttendanceTimeMonth(this.legalTransferOverTime10));
	}

	/** KRCDT_MON_AGGR_PREM_TIME 10 **/
	/**
	 * ドメインに変換
	 * @return 集計割増時間
	 */
	public AggregatePremiumTime toDomainPremiumTime1() {
		return AggregatePremiumTime.of(0, new AttendanceTimeMonth(this.premiumTime1));
	}

	public AggregatePremiumTime toDomainPremiumTime2() {
		return AggregatePremiumTime.of(0, new AttendanceTimeMonth(this.premiumTime2));
	}

	public AggregatePremiumTime toDomainPremiumTime3() {
		return AggregatePremiumTime.of(0, new AttendanceTimeMonth(this.premiumTime3));
	}

	public AggregatePremiumTime toDomainPremiumTime4() {
		return AggregatePremiumTime.of(0, new AttendanceTimeMonth(this.premiumTime4));
	}

	public AggregatePremiumTime toDomainPremiumTime5() {
		return AggregatePremiumTime.of(0, new AttendanceTimeMonth(this.premiumTime5));
	}

	public AggregatePremiumTime toDomainPremiumTime6() {
		return AggregatePremiumTime.of(0, new AttendanceTimeMonth(this.premiumTime6));
	}

	public AggregatePremiumTime toDomainPremiumTime7() {
		return AggregatePremiumTime.of(0, new AttendanceTimeMonth(this.premiumTime7));
	}

	public AggregatePremiumTime toDomainPremiumTime8() {
		return AggregatePremiumTime.of(0, new AttendanceTimeMonth(this.premiumTime8));
	}

	public AggregatePremiumTime toDomainPremiumTime9() {
		return AggregatePremiumTime.of(0, new AttendanceTimeMonth(this.premiumTime9));
	}

	public AggregatePremiumTime toDomainPremiumTime10() {
		return AggregatePremiumTime.of(0, new AttendanceTimeMonth(this.premiumTime10));
	}

	/** KRCDT_MON_AGGR_SPEC_DAYS 10 **/
	
	/**
	 * ドメインに変換
	 * @return 集計特定日数
	 */
	public AggregateSpecificDays toDomainSpecificDays1(){
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1),
				new AttendanceDaysMonth(this.specificDays1),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays1));
	}
	
	public AggregateSpecificDays toDomainSpecificDays2() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1),
				new AttendanceDaysMonth(this.specificDays2),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays2));
	}

	public AggregateSpecificDays toDomainSpecificDays3() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1), 
				new AttendanceDaysMonth(this.specificDays3),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays3));
	}

	public AggregateSpecificDays toDomainSpecificDays4() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1), 
				new AttendanceDaysMonth(this.specificDays4),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays4));
	}

	public AggregateSpecificDays toDomainSpecificDays5() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1), 
				new AttendanceDaysMonth(this.specificDays5),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays5));
	}

	public AggregateSpecificDays toDomainSpecificDays6() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1), 
				new AttendanceDaysMonth(this.specificDays6),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays6));
	}

	public AggregateSpecificDays toDomainSpecificDays7() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1), 
				new AttendanceDaysMonth(this.specificDays7),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays7));
	}

	public AggregateSpecificDays toDomainSpecificDays8() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1), 
				new AttendanceDaysMonth(this.specificDays8),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays8));
	}

	public AggregateSpecificDays toDomainSpecificDays9() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1), 
				new AttendanceDaysMonth(this.specificDays9),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays9));
	}

	public AggregateSpecificDays toDomainSpecificDays10() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1), 
				new AttendanceDaysMonth(this.specificDays10),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays10));
	}
	
	/** KRCDT_MON_AGGR_TOTAL_SPT **/
	
	/**
	 * ドメインに変換
	 * @return 集計総拘束時間
	 */
	public AggregateTotalTimeSpentAtWork toDomainTotalTimeSpentAtWork(){
		
		return AggregateTotalTimeSpentAtWork.of(
				new AttendanceTimeMonth(this.overTimeSpentAtWork),
				new AttendanceTimeMonth(this.midnightTimeSpentAtWork),
				new AttendanceTimeMonth(this.holidayTimeSpentAtWork),
				new AttendanceTimeMonth(this.varienceTimeSpentAtWork),
				new AttendanceTimeMonth(this.totalTimeSpentAtWork));
	}
	
	/** KRCDT_MON_AGGR_TOTAL_WRK **/
	
	/**
	 * ドメインに変換
	 * @param krcdtMonOverTime 月別実績の残業時間
	 * @param krcdtMonAggrOverTimes 集計残業時間
	 * @param krcdtMonHdwkTime 月別実績の休出時間
	 * @param krcdtMonAggrHdwkTimes 集計休出時間
	 * @param krcdtMonVactUseTime 月別実績の休暇使用時間
	 * @return 集計総労働時間
	 */
	public AggregateTotalWorkingTime toDomainTotalWorkingTime(
			KrcdtMonOverTime krcdtMonOverTime,
			List<KrcdtMonAggrOverTime> krcdtMonAggrOverTimes,
			KrcdtMonHdwkTime krcdtMonHdwkTime,
			List<KrcdtMonAggrHdwkTime> krcdtMonAggrHdwkTimes,
			KrcdtMonVactUseTime krcdtMonVactUseTime){
		
		// 月別実績の就業時間
		val workTime = WorkTimeOfMonthly.of(
				new AttendanceTimeMonth(this.workTime),
				new AttendanceTimeMonth(this.withinPrescribedPremiumTime),
				new AttendanceTimeMonth(this.actualWorkTime));

		// 月別実績の残業時間
		OverTimeOfMonthly overTime = new OverTimeOfMonthly();
		if (krcdtMonOverTime != null){
			overTime = krcdtMonOverTime.toDomain(krcdtMonAggrOverTimes);
		}
		
		// 月別実績の休出時間
		HolidayWorkTimeOfMonthly holidayWorkTime = new HolidayWorkTimeOfMonthly();
		if (krcdtMonHdwkTime != null){
			holidayWorkTime = krcdtMonHdwkTime.toDomain(krcdtMonAggrHdwkTimes);
		}
		
		// 月別実績の休暇使用時間
		VacationUseTimeOfMonthly vacationUseTime = new VacationUseTimeOfMonthly();
		if (krcdtMonVactUseTime != null){
			vacationUseTime = krcdtMonVactUseTime.toDomain();
		}
		
		// 月別実績の所定労働時間
		val prescribedWorkingTime = PrescribedWorkingTimeOfMonthly.of(
				new AttendanceTimeMonth(this.schedulePrescribedWorkingTime),
				new AttendanceTimeMonth(this.recordPrescribedWorkingTime));
		
		// 集計総労働時間
		return AggregateTotalWorkingTime.of(
				workTime,
				overTime,
				holidayWorkTime,
				vacationUseTime,
				prescribedWorkingTime);
	}
	
	/** KRCDT_MON_ATTENDANCE_TIME **/
	/**
	 * ドメインに変換
	 * @return 月別実績の勤怠時間
	 */
	public AttendanceTimeOfMonthly toDomainAttendanceTimeOfMonthly() {
		// 月別実績の36協定時間
		MonthlyCalculation monthlyCalculation =  MonthlyCalculation.of(null, null, new AttendanceTimeMonth(this.statutoryWorkingTime), 
				null, new AttendanceTimeMonth(this.totalWorkingTime), null, null);
		
		return AttendanceTimeOfMonthly.of(
				this.krcdtMonMergePk.getEmployeeId(),
				new YearMonth(this.krcdtMonMergePk.getYearMonth()),
				ClosureId.valueOf(this.krcdtMonMergePk.getClosureId()),
				new ClosureDate(this.krcdtMonMergePk.getClosureDay(), (this.krcdtMonMergePk.getIsLastDay() != 0)),
				new DatePeriod(this.startYmd, this.endYmd),
				monthlyCalculation,
				null,
				null,
				null,
				new AttendanceDaysMonth(this.aggregateDays));
	}
	
	/** KRCDT_MON_FLEX_TIME **/
	/**
	 * ドメインに変換
	 * @return 月別実績のフレックス時間
	 */
	public FlexTimeOfMonthly toDomainFlexTimeOfMonthly(){
		
		return FlexTimeOfMonthly.of(
				FlexTime.of(
						new TimeMonthWithCalculationAndMinus(
								new AttendanceTimeMonthWithMinus(this.flexTime),
								new AttendanceTimeMonthWithMinus(this.calcFlexTime)),
						new AttendanceTimeMonth(this.beforeFlexTime),
						new AttendanceTimeMonthWithMinus(this.legalFlexTime),
						new AttendanceTimeMonthWithMinus(this.illegalFlexTime)),
				new AttendanceTimeMonth(this.flexExcessTime),
				new AttendanceTimeMonth(this.flexShortageTime),
				FlexCarryforwardTime.of(
						new AttendanceTimeMonth(this.flexCarryforwardWorkTime),
						new AttendanceTimeMonth(this.flexCarryforwardTime),
						new AttendanceTimeMonth(this.flexCarryforwardShortageTime)),
				FlexTimeOfExcessOutsideTime.of(
						EnumAdaptor.valueOf(this.excessFlexAtr, ExcessFlexAtr.class),
						new AttendanceTimeMonth(this.principleTime),
						new AttendanceTimeMonth(this.forConvenienceTime)),
				FlexShortDeductTime.of(
						new AttendanceDaysMonth(this.annualLeaveDeductDays),
						new AttendanceTimeMonth(this.absenceDeductTime),
						new AttendanceTimeMonth(this.shotTimeBeforeDeduct)));
	}
	
	/** KRCDT_MON_HDWK_TIME **/
	/**
	 * ドメインに変換
	 * @param krcdtMonAggrHdwkTimes 集計休出時間
	 * @return 月別実績の休出時間
	 */
	public HolidayWorkTimeOfMonthly toDomainHolidayWorkTimeOfMonthly(){
		//TODO
		
		return HolidayWorkTimeOfMonthly.of(
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalHolidayWorkTime),
						new AttendanceTimeMonth(this.calcTotalHolidayWorkTime)),
				new AttendanceTimeMonth(this.beforeHolidayWorkTime),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTransferTime),
						new AttendanceTimeMonth(this.calcTotalTransferTime)),
				new ArrayList<>());
	}
	
	/** KRCDT_MON_LEAVE - リポジトリ：月別実績の休業 only update **/
	/**
	 * ドメインに変換
	 * @return 月別実績の休業
	 */
	public LeaveOfMonthly toDomainLeaveOfMonthly(){
		
		List<AggregateLeaveDays> fixLeaveDaysList = new ArrayList<>();
		List<AnyLeave> anyLeaveDaysList = new ArrayList<>();
		
		if (this.prenatalLeaveDays != 0.0){
			fixLeaveDaysList.add(AggregateLeaveDays.of(
					CloseAtr.PRENATAL, new AttendanceDaysMonth(this.prenatalLeaveDays)));
		}
		if (this.postpartumLeaveDays != 0.0){
			fixLeaveDaysList.add(AggregateLeaveDays.of(
					CloseAtr.POSTPARTUM, new AttendanceDaysMonth(this.postpartumLeaveDays)));
		}
		if (this.childcareLeaveDays != 0.0){
			fixLeaveDaysList.add(AggregateLeaveDays.of(
					CloseAtr.CHILD_CARE, new AttendanceDaysMonth(this.childcareLeaveDays)));
		}
		if (this.careLeaveDays != 0.0){
			fixLeaveDaysList.add(AggregateLeaveDays.of(
					CloseAtr.CARE, new AttendanceDaysMonth(this.careLeaveDays)));
		}
		if (this.injuryOrIllnessLeaveDays != 0.0){
			fixLeaveDaysList.add(AggregateLeaveDays.of(
					CloseAtr.INJURY_OR_ILLNESS, new AttendanceDaysMonth(this.injuryOrIllnessLeaveDays)));
		}
		if (this.anyLeaveDays01 != 0.0){
			anyLeaveDaysList.add(AnyLeave.of(1, new AttendanceDaysMonth(this.anyLeaveDays01)));
		}
		if (this.anyLeaveDays02 != 0.0){
			anyLeaveDaysList.add(AnyLeave.of(2, new AttendanceDaysMonth(this.anyLeaveDays02)));
		}
		if (this.anyLeaveDays03 != 0.0){
			anyLeaveDaysList.add(AnyLeave.of(3, new AttendanceDaysMonth(this.anyLeaveDays03)));
		}
		if (this.anyLeaveDays04 != 0.0){
			anyLeaveDaysList.add(AnyLeave.of(4, new AttendanceDaysMonth(this.anyLeaveDays04)));
		}
		
		val domain = LeaveOfMonthly.of(fixLeaveDaysList, anyLeaveDaysList);
		return domain;
	}
	
	/** KRCDT_MON_MEDICAL_TIME **/
	/**
	 * ドメインに変換
	 * @return 月別実績の医療時間
	 */
	public MedicalTimeOfMonthly toDomainMedicalTimeOfMonthly(){
		return MedicalTimeOfMonthly.of(
				EnumAdaptor.valueOf(this.dayNightAtr, WorkTimeNightShift.class),
				new AttendanceTimeMonth(this.workTime),
				new AttendanceTimeMonth(this.deductionTime),
				new AttendanceTimeMonth(this.takeOverTime));
	}
	
	/** KRCDT_MON_OVER_TIME **/
	/**
	 * ドメインに変換
	 * @param krcdtMonAggrOverTimes 集計残業時間
	 * @return 月別実績の残業時間
	 */
	public OverTimeOfMonthly toDomain(){
		List<AggregateOverTime> overTimeList = this.getOverTimeLst();	
		return OverTimeOfMonthly.of(
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalOverTime),
						new AttendanceTimeMonth(this.calcTotalOverTime)),
				new AttendanceTimeMonth(this.beforeOverTime),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTransferOverTime),
						new AttendanceTimeMonth(this.calcTotalTransferOverTime)),
				overTimeList);
	}
	
	private  List<AggregateOverTime> getOverTimeLst(){
		List<AggregateOverTime> overTimeList = new ArrayList<>();
		overTimeList.add(this.toDomainOverTime1());
		overTimeList.add(this.toDomainOverTime2());
		overTimeList.add(this.toDomainOverTime3());
		overTimeList.add(this.toDomainOverTime4());
		overTimeList.add(this.toDomainOverTime5());
		overTimeList.add(this.toDomainOverTime6());
		overTimeList.add(this.toDomainOverTime7());
		overTimeList.add(this.toDomainOverTime8());
		overTimeList.add(this.toDomainOverTime9());
		overTimeList.add(this.toDomainOverTime10());
		return overTimeList;
	}
	
	/** KRCDT_MON_REG_IRREG_TIME **/
	/**
	 * ドメインに変換
	 * @return 月別実績の通常変形時間
	 */
	public RegularAndIrregularTimeOfMonthly toDomainRegularAndIrregularTimeOfMonthly(){
		
		// 月別実績の変形労働時間
		val irregularWorkingTime = IrregularWorkingTimeOfMonthly.of(
				new AttendanceTimeMonthWithMinus(this.multiMonthIrregularMiddleTime),
				new AttendanceTimeMonthWithMinus(this.irregularPeriodCarryforwardTime),
				new AttendanceTimeMonth(this.irregularWorkingShortageTime),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.irregularLegalOverTime),
						new AttendanceTimeMonth(this.calcIrregularLegalOverTime)));
		
		return RegularAndIrregularTimeOfMonthly.of(
				new AttendanceTimeMonth(this.monthlyTotalPremiumTime),
				new AttendanceTimeMonth(this.weeklyTotalPremiumTime),
				irregularWorkingTime);
	}
	
	/** KRCDT_MON_VACT_USE_TIME **/
	/**
	 * ドメインに変換
	 * @return 月別実績の休暇使用時間
	 */
	public VacationUseTimeOfMonthly toDomainVacationUseTimeOfMonthly(){
		
		return VacationUseTimeOfMonthly.of(
				AnnualLeaveUseTimeOfMonthly.of(
						new AttendanceTimeMonth(this.annualLeaveUseTime)),
				RetentionYearlyUseTimeOfMonthly.of(
						new AttendanceTimeMonth(this.retentionYearlyUseTime)),
				SpecialHolidayUseTimeOfMonthly.of(
						new AttendanceTimeMonth(this.specialHolidayUseTime)),
				CompensatoryLeaveUseTimeOfMonthly.of(
						new AttendanceTimeMonth(this.compensatoryLeaveUseTime)));
	}
	
	/** KRCDT_MON_VERTICAL_TOTAL **/
	
	/**
	 * ドメインに変換
	 * @param krcdtMonLeave 月別実績の休業
	 * @param krcdtMonAggrAbsnDays 集計欠勤日数
	 * @param krcdtMonAggrSpecDays 集計特定日数
	 * @param krcdtMonAggrBnspyTime 集計加給時間
	 * @param krcdtMonAggrGoout 集計外出
	 * @param krcdtMonAggrPremTime 集計割増時間
	 * @param krcdtMonAggrDivgTime 集計乖離時間
	 * @param krcdtMonMedicalTime 月別実績の医療時間
	 * @param krcdtMonWorkClock 月別実績の勤務時刻
	 * @return 月別実績の縦計
	 */
	public VerticalTotalOfMonthly toDomainVerticalTotalOfMonthly(
			KrcdtMonLeave krcdtMonLeave,
			List<KrcdtMonAggrAbsnDays> krcdtMonAggrAbsnDays,
			List<KrcdtMonAggrSpecDays> krcdtMonAggrSpecDays,
			List<KrcdtMonAggrBnspyTime> krcdtMonAggrBnspyTime,
			List<KrcdtMonAggrGoout> krcdtMonAggrGoout,
			List<KrcdtMonAggrPremTime> krcdtMonAggrPremTime,
			List<KrcdtMonAggrDivgTime> krcdtMonAggrDivgTime,
			List<KrcdtMonMedicalTime> krcdtMonMedicalTime,
			KrcdtMonWorkClock krcdtMonWorkClock){
		
		if (krcdtMonAggrAbsnDays == null) krcdtMonAggrAbsnDays = new ArrayList<>();
		if (krcdtMonAggrSpecDays == null) krcdtMonAggrSpecDays = new ArrayList<>();
		if (krcdtMonAggrBnspyTime == null) krcdtMonAggrBnspyTime = new ArrayList<>();
		if (krcdtMonAggrGoout == null) krcdtMonAggrGoout = new ArrayList<>();
		if (krcdtMonAggrPremTime == null) krcdtMonAggrPremTime = new ArrayList<>();
		if (krcdtMonAggrDivgTime == null) krcdtMonAggrDivgTime = new ArrayList<>();
		if (krcdtMonMedicalTime == null) krcdtMonMedicalTime = new ArrayList<>();
		
		// 育児外出
		List<GoOutForChildCare> goOutForChildCares = new ArrayList<>();
		if (this.childcareGoOutTimes != 0 || this.childcareGoOutTime != 0){
			goOutForChildCares.add(GoOutForChildCare.of(
					ChildCareAtr.CHILD_CARE,
					new AttendanceTimesMonth(this.childcareGoOutTimes),
					new AttendanceTimeMonth(this.childcareGoOutTime)));
		}
		if (this.careGoOutTimes != 0 || this.careGoOutTime != 0){
			goOutForChildCares.add(GoOutForChildCare.of(
					ChildCareAtr.CARE,
					new AttendanceTimesMonth(this.careGoOutTimes),
					new AttendanceTimeMonth(this.careGoOutTime)));
		}
		
		// 月別実績の休業
		LeaveOfMonthly leave = new LeaveOfMonthly();
		if (krcdtMonLeave != null) leave = krcdtMonLeave.toDomain();
		
		// 月別実績の勤務日数
		val workDays = WorkDaysOfMonthly.of(
				AttendanceDaysOfMonthly.of(new AttendanceDaysMonth(this.attendanceDays)),
				AbsenceDaysOfMonthly.of(
						new AttendanceDaysMonth(this.totalAbsenceDays),
						new AttendanceTimeMonth(this.totalAbsenceTime),
						krcdtMonAggrAbsnDays.stream().map(c -> c.toDomain()).collect(Collectors.toList())),
				PredeterminedDaysOfMonthly.of(
						new AttendanceDaysMonth(this.predetermineDays)),
				WorkDaysDetailOfMonthly.of(new AttendanceDaysMonth(this.workDays)),
				HolidayDaysOfMonthly.of(new AttendanceDaysMonth(this.holidayDays)),
				SpecificDaysOfMonthly.of(
						krcdtMonAggrSpecDays.stream().map(c -> c.toDomain()).collect(Collectors.toList())),
				HolidayWorkDaysOfMonthly.of(new AttendanceDaysMonth(this.holidayWorkDays)),
				PayDaysOfMonthly.of(
						new AttendanceDaysMonth(this.payAttendanceDays),
						new AttendanceDaysMonth(this.payAbsenceDays)),
				WorkTimesOfMonthly.of(new AttendanceTimesMonth(this.workTimes)),
				TwoTimesWorkTimesOfMonthly.of(new AttendanceTimesMonth(this.twoTimesWorkTimes)),
				TemporaryWorkTimesOfMonthly.of(new AttendanceTimesMonth(this.temporaryWorkTimes)),
				leave);
		
		// 月別実績の勤務時間
		val workTime = nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.WorkTimeOfMonthly.of(
				BonusPayTimeOfMonthly.of(
						krcdtMonAggrBnspyTime.stream().map(c -> c.toDomain()).collect(Collectors.toList())),
				GoOutOfMonthly.of(
						krcdtMonAggrGoout.stream().map(c -> c.toDomain()).collect(Collectors.toList()),
						goOutForChildCares),
				PremiumTimeOfMonthly.of(
						krcdtMonAggrPremTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()),
						new AttendanceTimeMonth(this.premiumMidnightTime),
						new AttendanceTimeMonth(this.premiumLegalOutsideWorkTime),
						new AttendanceTimeMonth(this.premiumLegalHolidayWorkTime),
						new AttendanceTimeMonth(this.premiumIllegalOutsideWorkTime),
						new AttendanceTimeMonth(this.premiumIllegalHolidayWorkTime)),
				BreakTimeOfMonthly.of(new AttendanceTimeMonth(this.breakTime)),
				HolidayTimeOfMonthly.of(
						new AttendanceTimeMonth(this.legalHolidayTime),
						new AttendanceTimeMonth(this.illegalHolidayTime),
						new AttendanceTimeMonth(this.illegalSpecialHolidayTime)),
				MidnightTimeOfMonthly.of(
						new TimeMonthWithCalculation(
								new AttendanceTimeMonth(this.overWorkMidnightTime),
								new AttendanceTimeMonth(this.calcOverWorkMidnightTime)),
						new TimeMonthWithCalculation(
								new AttendanceTimeMonth(this.legalMidnightTime),
								new AttendanceTimeMonth(this.calcLegalMidnightTime)),
						IllegalMidnightTime.of(
								new TimeMonthWithCalculation(
										new AttendanceTimeMonth(this.illegalMidnightTime),
										new AttendanceTimeMonth(this.calcIllegalMidnightTime)),
								new AttendanceTimeMonth(this.illegalBeforeMidnightTime)),
						new TimeMonthWithCalculation(
								new AttendanceTimeMonth(this.legalHolidayWorkMidnightTime),
								new AttendanceTimeMonth(this.calcLegalHolidayWorkMidnightTime)),
						new TimeMonthWithCalculation(
								new AttendanceTimeMonth(this.illegalHolidayWorkMidnightTime),
								new AttendanceTimeMonth(this.calcIllegalHolidayWorkMidnightTime)),
						new TimeMonthWithCalculation(
								new AttendanceTimeMonth(this.specialHolidayWorkMidnightTime),
								new AttendanceTimeMonth(this.calcSpecialHolidayWorkMidnightTime))),
				LateLeaveEarlyOfMonthly.of(
						LeaveEarly.of(
								new AttendanceTimesMonth(this.leaveEarlyTimes),
								new TimeMonthWithCalculation(
										new AttendanceTimeMonth(this.leaveEarlyTime),
										new AttendanceTimeMonth(this.calcLeaveEarlyTime))),
						Late.of(
								new AttendanceTimesMonth(this.lateTimes),
								new TimeMonthWithCalculation(
										new AttendanceTimeMonth(this.lateTime),
										new AttendanceTimeMonth(this.calcLateTime)))),
				AttendanceLeaveGateTimeOfMonthly.of(
						new AttendanceTimeMonth(this.attendanceLeaveGateBeforeAttendanceTime),
						new AttendanceTimeMonth(this.attendanceLeaveGateAfterLeaveWorkTime),
						new AttendanceTimeMonth(this.attendanceLeaveGateStayingTime),
						new AttendanceTimeMonth(this.attendanceLeaveGateUnemployedTime)),
				BudgetTimeVarienceOfMonthly.of(new AttendanceTimeMonth(this.budgetVarienceTime)),
				DivergenceTimeOfMonthly.of(
						krcdtMonAggrDivgTime.stream().map(c -> c.toDomain()).collect(Collectors.toList())),
				krcdtMonMedicalTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
		
		// 月別実績の勤務時刻
		WorkClockOfMonthly workClock = new WorkClockOfMonthly();
		if (krcdtMonWorkClock != null) workClock = krcdtMonWorkClock.toDomain();
		
		return VerticalTotalOfMonthly.of(workDays, workTime, workClock);
	}
	
	/** KRCDT_MON_EXCESS_OUTSIDE 50 **/
	/**
	 * ドメインに変換
	 * @param krcdtMonExcoutTime 時間外超過
	 * @return 月別実績の時間外超過
	 */
	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly1(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime){

		if (krcdtMonExcoutTime == null) krcdtMonExcoutTime = new ArrayList<>();
		
		return ExcessOutsideWorkOfMonthly.of(
				new AttendanceTimeMonth(this.totalWeeklyPremiumTime1),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime1),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime1),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly2(List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime2),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime2),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime2),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly3(List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime3),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime3),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime3),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly4(List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime4),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime4),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime4),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly5(List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime5),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime5),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime5),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly6(List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime6),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime6),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime6),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly7(List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime7),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime7),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime7),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly8(List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime8),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime8),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime8),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly9(List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime9),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime9),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime9),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly10(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime10),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime10),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime10),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly11(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime11),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime11),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime11),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly12(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime12),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime12),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime12),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly13(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime13),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime13),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime13),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly14(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime14),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime14),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime14),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly15(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime15),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime15),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime15),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly16(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime16),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime16),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime16),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly17(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime17),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime17),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime17),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly18(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime18),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime18),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime18),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly19(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime19),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime19),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime19),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly20(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime20),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime20),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime20),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly21(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime21),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime21),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime21),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly22(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime22),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime22),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime22),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly23(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime23),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime23),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime23),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly24(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime24),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime24),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime24),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly25(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime25),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime25),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime25),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly26(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime26),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime26),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime26),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly27(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime27),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime27),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime27),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly28(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime28),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime28),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime28),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly29(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime29),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime29),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime29),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly30(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime30),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime30),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime30),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly31(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime31),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime31),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime31),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly32(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime32),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime32),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime32),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly33(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime33),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime33),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime33),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly34(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime34),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime34),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime34),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly35(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime35),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime35),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime35),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly36(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime36),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime36),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime36),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly37(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime37),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime37),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime37),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly38(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime38),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime38),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime38),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly39(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime39),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime39),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime39),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly40(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime40),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime40),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime40),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly41(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime41),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime41),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime41),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly42(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime42),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime42),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime42),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly43(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime43),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime43),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime43),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly44(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime44),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime44),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime44),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly45(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime45),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime45),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime45),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly46(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime46),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime46),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime46),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly47(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime47),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime47),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime47),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly48(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime48),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime48),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime48),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly49(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime49),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime49),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime49),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	public ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly50(
			List<KrcdtMonExcoutTime> krcdtMonExcoutTime) {
		if (krcdtMonExcoutTime == null)
			krcdtMonExcoutTime = new ArrayList<>();
		return ExcessOutsideWorkOfMonthly.of(new AttendanceTimeMonth(this.totalWeeklyPremiumTime50),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime50),
				new AttendanceTimeMonthWithMinus(this.deformationCarryforwardTime50),
				krcdtMonExcoutTime.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
	}

	/** KRCDT_MON_AGREEMENT_TIME **/
	/**
	 * ドメインに変換
	 * @return 月別実績の36協定時間
	 */
	public AgreementTimeOfMonthly toDomainAgreementTimeOfMonthly(){
		
		return AgreementTimeOfMonthly.of(
				new AttendanceTimeMonth(this.agreementTime),
				new LimitOneMonth(this.limitErrorTime),
				new LimitOneMonth(this.limitAlarmTime),
				(this.exceptionLimitErrorTime == null ?
						Optional.empty() : Optional.of(new LimitOneMonth(this.exceptionLimitErrorTime))),
				(this.exceptionLimitAlarmTime == null ?
						Optional.empty() : Optional.of(new LimitOneMonth(this.exceptionLimitAlarmTime))),
				EnumAdaptor.valueOf(this.status, AgreementTimeStatusOfMonthly.class));
	}
	
	/** KRCDT_MON_AFFILIATION **/
	/**
	 * ドメインに変換
	 * @return 月別実績の所属情報
	 */
	public AffiliationInfoOfMonthly toDomainAffiliationInfoOfMonthly(){
		
		return AffiliationInfoOfMonthly.of(
				this.krcdtMonMergePk.getEmployeeId(),
				new YearMonth(this.krcdtMonMergePk.getYearMonth()),
				EnumAdaptor.valueOf(this.krcdtMonMergePk.getClosureId(), ClosureId.class),
				new ClosureDate(this.krcdtMonMergePk.getClosureDay(), (this.krcdtMonMergePk.getIsLastDay() == 1)),
				AggregateAffiliationInfo.of(
						new EmploymentCode(this.firstEmploymentCd),
						new WorkplaceId(this.firstWorkplaceId),
						new JobTitleId(this.firstJobTitleId),
						new ClassificationCode(this.firstClassCd),
						new BusinessTypeCode(this.firstBusinessTypeCd)),
				AggregateAffiliationInfo.of(
						new EmploymentCode(this.lastEmploymentCd),
						new WorkplaceId(this.lastWorkplaceId),
						new JobTitleId(this.lastJobTitleId),
						new ClassificationCode(this.lastClassCd),
						new BusinessTypeCode(this.lastBusinessTypeCd)));
	}
}
