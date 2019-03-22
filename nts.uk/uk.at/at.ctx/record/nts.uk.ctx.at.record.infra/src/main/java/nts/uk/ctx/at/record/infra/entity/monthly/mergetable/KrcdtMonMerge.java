package nts.uk.ctx.at.record.infra.entity.monthly.mergetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
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
import nts.uk.ctx.at.record.dom.monthly.excessoutside.ExcessOutSideWorkEachBreakdown;
import nts.uk.ctx.at.record.dom.monthly.excessoutside.ExcessOutsideWork;
import nts.uk.ctx.at.record.dom.monthly.excessoutside.ExcessOutsideWorkOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.mergetable.AggregateAbsenceDaysMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.MonthMergeKey;
import nts.uk.ctx.at.record.dom.monthly.totalcount.TotalCount;
import nts.uk.ctx.at.record.dom.monthly.totalcount.TotalCountByPeriod;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.VerticalTotalOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.EndClockOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.WorkClockOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon.AggrPCLogonClock;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon.AggrPCLogonDivergence;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon.PCLogonClockOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon.PCLogonDivergenceOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon.PCLogonOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.WorkDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.leave.AggregateLeaveDays;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.leave.AnyLeave;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.leave.LeaveOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.paydays.PayDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.specificdays.AggregateSpecificDays;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.specificdays.SpecificDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AbsenceDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AggregateAbsenceDays;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AggregateSpcVacationDays;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AttendanceDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.HolidayDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.HolidayWorkDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.PredeterminedDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.RecruitmentDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.SpcVacationDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.TemporaryWorkTimesOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.TwoTimesWorkTimesOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.WorkDaysDetailOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.WorkTimesOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.WorkTimeOfMonthlyVT;
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
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.common.times.AttendanceTimesMonth;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.JobTitleId;
import nts.uk.ctx.at.shared.dom.shortworktime.ChildCareAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.predset.WorkTimeNightShift;
import nts.uk.ctx.at.shared.dom.worktype.CloseAtr;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 残数系以外
 * @author lanlt
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

	@Column(name = "ABSENCE_TIME_1")
	public double absenceTime1;

	@Column(name = "ABSENCE_TIME_2")
	public double absenceTime2;

	@Column(name = "ABSENCE_TIME_3")
	public double absenceTime3;

	@Column(name = "ABSENCE_TIME_4")
	public double absenceTime4;

	@Column(name = "ABSENCE_TIME_5")
	public double absenceTime5;

	@Column(name = "ABSENCE_TIME_6")
	public double absenceTime6;

	@Column(name = "ABSENCE_TIME_7")
	public double absenceTime7;

	@Column(name = "ABSENCE_TIME_8")
	public double absenceTime8;

	@Column(name = "ABSENCE_TIME_9")
	public double absenceTime9;

	@Column(name = "ABSENCE_TIME_10")
	public double absenceTime10;

	@Column(name = "ABSENCE_TIME_11")
	public double absenceTime11;

	@Column(name = "ABSENCE_TIME_12")
	public double absenceTime12;

	@Column(name = "ABSENCE_TIME_13")
	public double absenceTime13;

	@Column(name = "ABSENCE_TIME_14")
	public double absenceTime14;

	@Column(name = "ABSENCE_TIME_15")
	public double absenceTime15;

	@Column(name = "ABSENCE_TIME_16")
	public double absenceTime16;

	@Column(name = "ABSENCE_TIME_17")
	public double absenceTime17;

	@Column(name = "ABSENCE_TIME_18")
	public double absenceTime18;

	@Column(name = "ABSENCE_TIME_19")
	public double absenceTime19;

	@Column(name = "ABSENCE_TIME_20")
	public double absenceTime20;

	@Column(name = "ABSENCE_TIME_21")
	public double absenceTime21;

	@Column(name = "ABSENCE_TIME_22")
	public double absenceTime22;

	@Column(name = "ABSENCE_TIME_23")
	public double absenceTime23;

	@Column(name = "ABSENCE_TIME_24")
	public double absenceTime24;

	@Column(name = "ABSENCE_TIME_25")
	public double absenceTime25;

	@Column(name = "ABSENCE_TIME_26")
	public double absenceTime26;

	@Column(name = "ABSENCE_TIME_27")
	public double absenceTime27;

	@Column(name = "ABSENCE_TIME_28")
	public double absenceTime28;

	@Column(name = "ABSENCE_TIME_29")
	public double absenceTime29;

	@Column(name = "ABSENCE_TIME_30")
	public double absenceTime30;

	/** 欠勤日数 */

	@Column(name = "ABSENCE_DAYS_1")
	public int absenceDay1;

	@Column(name = "ABSENCE_DAYS_2")
	public int absenceDay2;

	@Column(name = "ABSENCE_DAYS_3")
	public int absenceDay3;

	@Column(name = "ABSENCE_DAYS_4")
	public int absenceDay4;

	@Column(name = "ABSENCE_DAYS_5")
	public int absenceDay5;

	@Column(name = "ABSENCE_DAYS_6")
	public int absenceDay6;

	@Column(name = "ABSENCE_DAYS_7")
	public int absenceDay7;

	@Column(name = "ABSENCE_DAYS_8")
	public int absenceDay8;

	@Column(name = "ABSENCE_DAYS_9")
	public int absenceDay9;

	@Column(name = "ABSENCE_DAYS_10")
	public int absenceDay10;

	@Column(name = "ABSENCE_DAYS_11")
	public int absenceDay11;

	@Column(name = "ABSENCE_DAYS_12")
	public int absenceDay12;

	@Column(name = "ABSENCE_DAYS_13")
	public int absenceDay13;

	@Column(name = "ABSENCE_DAYS_14")
	public int absenceDay14;

	@Column(name = "ABSENCE_DAYS_15")
	public int absenceDay15;

	@Column(name = "ABSENCE_DAYS_16")
	public int absenceDay16;

	@Column(name = "ABSENCE_DAYS_17")
	public int absenceDay17;

	@Column(name = "ABSENCE_DAYS_18")
	public int absenceDay18;

	@Column(name = "ABSENCE_DAYS_19")
	public int absenceDay19;

	@Column(name = "ABSENCE_DAYS_20")
	public int absenceDay20;

	@Column(name = "ABSENCE_DAYS_21")
	public int absenceDay21;

	@Column(name = "ABSENCE_DAYS_22")
	public int absenceDay22;

	@Column(name = "ABSENCE_DAYS_23")
	public int absenceDay23;

	@Column(name = "ABSENCE_DAYS_24")
	public int absenceDay24;

	@Column(name = "ABSENCE_DAYS_25")
	public int absenceDay25;

	@Column(name = "ABSENCE_DAYS_26")
	public int absenceDay26;

	@Column(name = "ABSENCE_DAYS_27")
	public int absenceDay27;

	@Column(name = "ABSENCE_DAYS_28")
	public int absenceDay28;

	@Column(name = "ABSENCE_DAYS_29")
	public int absenceDay29;

	@Column(name = "ABSENCE_DAYS_30")
	public int absenceDay30;

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

	@Column(name = "DIVERGENCE_ATR_7")
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

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出.回数- 私用外出回数 - 回数- GOOUT_TIMES_PRIVATE*/
	@Column(name = "GOOUT_TIMES_PRIVATE")
	public int goOutTimesPrivate;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出.回数  - 公用外出回数 - 回数- GOOUT_TIMES_PUBLIC*/
	@Column(name = "GOOUT_TIMES_PUBLIC")
	public int goOutTimesPublic;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出.回数  - 有償外出回数- 回数- GOOUT_TIMES_COMPENSATION*/
	@Column(name = "GOOUT_TIMES_COMPENSATION")
	public int goOutTimesCompensation;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出.回数  - 組合外出回数- 回数- GOOUT_TIMES_UNION*/
	@Column(name = "GOOUT_TIMES_UNION")
	public int goOutTimesUnion;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定内時間 - 時間 - 私用法定内時間 - LEGAL_TIME_PRIVATE*/
	@Column(name = "LEGAL_TIME_PRIVATE")
	public int legalTimePrivate;
	
	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定内時間 - 時間 - 公用法定内時間- 時間 - LEGAL_TIME_PUBLIC*/
	@Column(name = "LEGAL_TIME_PUBLIC")
	public int legalTimePublic;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定内時間 - 時間 - 有償計算法定内時間 - LEGAL_TIME_COMPENSATION*/
	@Column(name = "LEGAL_TIME_COMPENSATION")
	public int legalTimeCompensation;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定内時間 - 時間 - 組合法定内時間  - LEGAL_TIME_UNION*/
	@Column(name = "LEGAL_TIME_UNION")
	public int legalTimeUnion;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定内時間 - 計算時間 - 私用計算法定外時間 - CALC_LEGAL_TIME_PRIVATE*/
	@Column(name = "CALC_LEGAL_TIME_PRIVATE")
	public int calcLegalTimePrivate;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定内時間 - 計算時間 - 公用計算法定内時間 - CALC_LEGAL_TIME_PUBLIC*/
	@Column(name = "CALC_LEGAL_TIME_PUBLIC")
	public int calcLegalTimePublic;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定内時間 - 計算時間 - 有償計算法定内時間 - CALC_LEGAL_TIME_COMPENSATION*/
	@Column(name = "CALC_LEGAL_TIME_COMPENSATION")
	public int calcLegalTimeCompensation;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定内時間 - 計算時間 - 組合計算法定内時間 - CALC_LEGAL_TIME_UNION*/
	@Column(name = "CALC_LEGAL_TIME_UNION")
	public int calcLegalTimeUnion;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定外時間 法定外時間  - 時間 - 私用法定外時間 - ILLEGAL_TIME_PRIVATE */
	@Column(name = "ILLEGAL_TIME_PRIVATE")
	public int illegalTimePrivate;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定外時間 法定外時間  - 時間 - 公用法定外時間 - ILLEGAL_TIME_PUBLIC */
	@Column(name = "ILLEGAL_TIME_PUBLIC")
	public int illegalTimePublic;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定外時間 法定外時間  - 時間 - 有償法定外時間 - ILLEGAL_TIME_COMPENSATION */
	@Column(name = "ILLEGAL_TIME_COMPENSATION")
	public int illegalTimeCompensation;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定外時間 法定外時間  - 時間 - 組合法定外時間 - ILLEGAL_TIME_UNION */
	@Column(name = "ILLEGAL_TIME_UNION")
	public int illegalTimeUnion;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定外時間 - 計算時間 - 私用計算法定外時間 - CALC_ILLEGAL_TIME_PRIVATE */
	@Column(name = "CALC_ILLEGAL_TIME_PRIVATE")
	public int calcIllegalTimePrivate;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定外時間 - 計算時間 - 公用計算法定外時間 - CALC_ILLEGAL_TIME_PUBLIC */
	@Column(name = "CALC_ILLEGAL_TIME_PUBLIC")
	public int calcIllegalTimePublic;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定外時間 - 計算時間 - 有償計算法定外時間 - CALC_ILLEGAL_TIME_COMPENSATION */
	@Column(name = "CALC_ILLEGAL_TIME_COMPENSATION")
	public int calcIllegalTimeCompensation;

	/** 月別実績の勤怠時間．縦計．勤務時間．外出．外出．法定外時間 - 計算時間 - 組合計算法定外時間 - CALC_ILLEGAL_TIME_UNION */
	@Column(name = "CALC_ILLEGAL_TIME_UNION")
	public int calcIllegalTimeUnion;

	/**月別実績の勤怠時間．縦計．勤務時間．外出．外出．合計時間 - 時間 - 私用合計時間 - TOTAL_TIME_PRIVATE */
	@Column(name = "TOTAL_TIME_PRIVATE")
	public int totalTimePrivate;

	/**月別実績の勤怠時間．縦計．勤務時間．外出．外出．合計時間 - 時間 - 公用合計時間 - TOTAL_TIME_PUBLIC */
	@Column(name = "TOTAL_TIME_PUBLIC")
	public int totalTimePublic;

	/**月別実績の勤怠時間．縦計．勤務時間．外出．外出．合計時間 - 時間 - 有償合計時間 - TOTAL_TIME_COMPENSATION */
	@Column(name = "TOTAL_TIME_COMPENSATION")
	public int totalTimeCompensation;

	/**月別実績の勤怠時間．縦計．勤務時間．外出．外出．合計時間 - 時間 - 組合合計時間 - TOTAL_TIME_UNION */
	@Column(name = "TOTAL_TIME_UNION")
	public int totalTimeUnion;

	/**月別実績の勤怠時間．縦計．勤務時間．外出．外出．合計時間 - 計算時間 - 私用計算合計時間  - CALC_TOTAL_TIME_PRIVATE */
	@Column(name = "CALC_TOTAL_TIME_PRIVATE")
	public int calcTotalTimePrivate;

	/**月別実績の勤怠時間．縦計．勤務時間．外出．外出．合計時間 - 計算時間 - 公用計算合計時間  - CALC_TOTAL_TIME_PUBLIC */
	@Column(name = "CALC_TOTAL_TIME_PUBLIC")
	public int calcTotalTimePublic;

	/**月別実績の勤怠時間．縦計．勤務時間．外出．外出．合計時間 - 計算時間 - 有償計算合計時間  -  CALC_TOTAL_TIME_COMPENSATION*/
	@Column(name = "CALC_TOTAL_TIME_COMPENSATION")
	public int calcTotalTimeCompensation;

	/**月別実績の勤怠時間．縦計．勤務時間．外出．外出．合計時間 - 計算時間 - 組合計算合計時間  - CALC_TOTAL_TIME_UNION */
	@Column(name = "CALC_TOTAL_TIME_UNION")
	public int calcTotalTimeUnion;

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
	
	//月別実績の勤怠時間．縦計．勤務日数．特別休暇日数．特別休暇日数
	/** 特別休暇日数1 */
	@Column(name = "SPCVACT_DAYS_1")
	public double spcVactDays1;

	@Column(name = "SPCVACT_DAYS_2")
	public double spcVactDays2;

	@Column(name = "SPCVACT_DAYS_3")
	public double spcVactDays3;

	@Column(name = "SPCVACT_DAYS_4")
	public double spcVactDays4;

	@Column(name = "SPCVACT_DAYS_5")
	public double spcVactDays5;

	@Column(name = "SPCVACT_DAYS_6")
	public double spcVactDays6;

	@Column(name = "SPCVACT_DAYS_7")
	public double spcVactDays7;

	@Column(name = "SPCVACT_DAYS_8")
	public double spcVactDays8;

	@Column(name = "SPCVACT_DAYS_9")
	public double spcVactDays9;

	@Column(name = "SPCVACT_DAYS_10")
	public double spcVactDays10;

	@Column(name = "SPCVACT_DAYS_11")
	public double spcVactDays11;

	@Column(name = "SPCVACT_DAYS_12")
	public double spcVactDays12;

	@Column(name = "SPCVACT_DAYS_13")
	public double spcVactDays13;

	@Column(name = "SPCVACT_DAYS_14")
	public double spcVactDays14;

	@Column(name = "SPCVACT_DAYS_15")
	public double spcVactDays15;

	@Column(name = "SPCVACT_DAYS_16")
	public double spcVactDays16;

	@Column(name = "SPCVACT_DAYS_17")
	public double spcVactDays17;

	@Column(name = "SPCVACT_DAYS_18")
	public double spcVactDays18;

	@Column(name = "SPCVACT_DAYS_19")
	public double spcVactDays19;

	@Column(name = "SPCVACT_DAYS_20")
	public double spcVactDays20;

	@Column(name = "SPCVACT_DAYS_21")
	public double spcVactDays21;

	@Column(name = "SPCVACT_DAYS_22")
	public double spcVactDays22;

	@Column(name = "SPCVACT_DAYS_23")
	public double spcVactDays23;

	@Column(name = "SPCVACT_DAYS_24")
	public double spcVactDays24;

	@Column(name = "SPCVACT_DAYS_25")
	public double spcVactDays25;

	@Column(name = "SPCVACT_DAYS_26")
	public double spcVactDays26;

	@Column(name = "SPCVACT_DAYS_27")
	public double spcVactDays27;

	@Column(name = "SPCVACT_DAYS_28")
	public double spcVactDays28;

	@Column(name = "SPCVACT_DAYS_29")
	public double spcVactDays29;

	@Column(name = "SPCVACT_DAYS_30")
	public double spcVactDays30;
	
	//月別実績の勤怠時間．縦計．勤務日数．特別休暇日数．特別休暇日数
	/** 特別休暇時間1 */
	@Column(name = "SPCVACT_TIME_1")
	public double spcVactTime1;

	@Column(name = "SPCVACT_TIME_2")
	public double spcVactTime2;

	@Column(name = "SPCVACT_TIME_3")
	public double spcVactTime3;

	@Column(name = "SPCVACT_TIME_4")
	public double spcVactTime4;

	@Column(name = "SPCVACT_TIME_5")
	public double spcVactTime5;

	@Column(name = "SPCVACT_TIME_6")
	public double spcVactTime6;

	@Column(name = "SPCVACT_TIME_7")
	public double spcVactTime7;

	@Column(name = "SPCVACT_TIME_8")
	public double spcVactTime8;

	@Column(name = "SPCVACT_TIME_9")
	public double spcVactTime9;

	@Column(name = "SPCVACT_TIME_10")
	public double spcVactTime10;

	@Column(name = "SPCVACT_TIME_11")
	public double spcVactTime11;

	@Column(name = "SPCVACT_TIME_12")
	public double spcVactTime12;

	@Column(name = "SPCVACT_TIME_13")
	public double spcVactTime13;

	@Column(name = "SPCVACT_TIME_14")
	public double spcVactTime14;

	@Column(name = "SPCVACT_TIME_15")
	public double spcVactTime15;

	@Column(name = "SPCVACT_TIME_16")
	public double spcVactTime16;

	@Column(name = "SPCVACT_TIME_17")
	public double spcVactTime17;

	@Column(name = "SPCVACT_TIME_18")
	public double spcVactTime18;

	@Column(name = "SPCVACT_TIME_19")
	public double spcVactTime19;

	@Column(name = "SPCVACT_TIME_20")
	public double spcVactTime20;

	@Column(name = "SPCVACT_TIME_21")
	public double spcVactTime21;

	@Column(name = "SPCVACT_TIME_22")
	public double spcVactTime22;

	@Column(name = "SPCVACT_TIME_23")
	public double spcVactTime23;

	@Column(name = "SPCVACT_TIME_24")
	public double spcVactTime24;

	@Column(name = "SPCVACT_TIME_25")
	public double spcVactTime25;

	@Column(name = "SPCVACT_TIME_26")
	public double spcVactTime26;

	@Column(name = "SPCVACT_TIME_27")
	public double spcVactTime27;

	@Column(name = "SPCVACT_TIME_28")
	public double spcVactTime28;

	@Column(name = "SPCVACT_TIME_29")
	public double spcVactTime29;

	@Column(name = "SPCVACT_TIME_30")
	public double spcVactTime30;
	
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
	/** 月別実績の勤怠時間．縦計．勤務時間．医療時間 勤務時間 - 勤務時間 - 日勤勤務時間 - DAY_SHIFT_MEDICAL_TIME*/
	@Column(name = "DAY_SHIFT_MEDICAL_TIME")
	public int dayWorkMedicalTime;

	/** 月別実績の勤怠時間．縦計．勤務時間．医療時間 勤務時間 - 控除時間 - 日勤控除時間 - DAY_SHIFT_DEDUCTION_TIME*/
	/** 控除時間 */
	@Column(name = "DAY_SHIFT_DEDUCTION_TIME")
	public int dayDeductionTime;

	/** 月別実績の勤怠時間．縦計．勤務時間．医療時間 勤務時間 - 申送時間- 日勤申送時間 - DAY_SHIFT_TAKEOVER_TIME*/
	@Column(name = "DAY_SHIFT_TAKEOVER_TIME")
	public int dayTakeOverTime;

	//月別実績の勤怠時間．縦計．勤務時間．医療時間
	
	/** 勤務時間 - 夜勤控除時間 - NIGHT_SHIFT_MEDICAL_TIME*/
	@Column(name = "NIGHT_SHIFT_MEDICAL_TIME")
	public int nightMedicalTime;
	
	/** 控除時間 - 夜勤控除時間 - NIGHT_SHIFT_DEDUCTION_TIME  */
	@Column(name = "NIGHT_SHIFT_DEDUCTION_TIME")
	public int nightDeductionTime;

	/** 申送時間 - 夜勤申送時間 - NIGHT_SHIFT_TAKEOVER_TIME*/
	@Column(name = "NIGHT_SHIFT_TAKEOVER_TIME")
	public int nightTakeOverTime;
	
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

	/** 月別実績の勤怠時間．時間外超過- 週割増合計時間 - 時間外超過用*/
	@Column(name = "WEEK_TOTAL_PREM_TIME")
	public int weeklyTotalPremiumTime;

	/** 月別実績の勤怠時間．時間外超過 - 月割増合計時間 - 時間外超過用*/
	@Column(name = "MON_TOTAL_PREM_TIME")
	public int monthlyTotalPremiumTime;

	/**月別実績の勤怠時間．時間外超過 - 変形繰越時間 - 時間外超過用変形繰越時間*/
	@Column(name = "MULTI_MON_IRGMDL_TIME")
	public int multiMonthIrregularMiddleTime;

	/** 月別実績の勤怠時間．時間外超過  - 変形繰越時間 - 時間外超過用変形繰越時間 */
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
	
	/** 月別実績の勤怠時間．縦計．勤務日数．振出日数- 日数 - 振出日数 - RECRUIT_DAYS*/
	@Column(name = "RECRUIT_DAYS")
	public double recruitDays;
	
	/** 月別実績の勤怠時間．縦計．勤務日数．特別休暇日数- 特別休暇合計時間  - 特別休暇合計日数- TOTAL_SPCVACT_DAYS */
	@Column(name = "TOTAL_SPCVACT_DAYS")
	public double totalSpcvactDays;
	
	/** 月別実績の勤怠時間．縦計．勤務日数．特別休暇日数- 特別休暇合計時間 - 特別休暇合計時間 - TOTAL_SPCVACT_TIME*/
	@Column(name = "TOTAL_SPCVACT_TIME")
	public int totalSpcvactTime;
	
	
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

	/* KRCDT_MON_EXCESS_OUTSIDE  */
	@Column(name = "EXCESS_WEEK_TOTAL_PREM_TIME")
	public int totalWeeklyPremiumTime1;

	@Column(name = "EXCESS_MON_TOTAL_PREM_TIME")
	public int totalMonthlyPremiumTime1;

	@Column(name = "EXCESS_MULTI_MON_IRGMDL_TIME")
	public int multiMonIrgmdlTime;

	/* KRCDT_MON_EXCOUT_TIME 50*/
	/** 月別実績の勤怠時間．時間外超過．時間 - 超過時間 - 超過時間 - EXCESS_TIME_1_1*/
	@Column(name = "EXCESS_TIME_1_1")
	public int excessTime_1_1;

	@Column(name = "EXCESS_TIME_1_2")
	public int excessTime_1_2;

	@Column(name = "EXCESS_TIME_1_3")
	public int excessTime_1_3;

	@Column(name = "EXCESS_TIME_1_4")
	public int excessTime_1_4;

	@Column(name = "EXCESS_TIME_1_5")
	public int excessTime_1_5;

	@Column(name = "EXCESS_TIME_1_6")
	public int excessTime_1_6;

	@Column(name = "EXCESS_TIME_1_7")
	public int excessTime_1_7;

	@Column(name = "EXCESS_TIME_1_8")
	public int excessTime_1_8;

	@Column(name = "EXCESS_TIME_1_9")
	public int excessTime_1_9;

	@Column(name = "EXCESS_TIME_1_10")
	public int excessTime_1_10;

	@Column(name = "EXCESS_TIME_2_1")
	public int excessTime_2_1;

	@Column(name = "EXCESS_TIME_2_2")
	public int excessTime_2_2;

	@Column(name = "EXCESS_TIME_2_3")
	public int excessTime_2_3;

	@Column(name = "EXCESS_TIME_2_4")
	public int excessTime_2_4;

	@Column(name = "EXCESS_TIME_2_5")
	public int excessTime_2_5;

	@Column(name = "EXCESS_TIME_2_6")
	public int excessTime_2_6;

	@Column(name = "EXCESS_TIME_2_7")
	public int excessTime_2_7;

	@Column(name = "EXCESS_TIME_2_8")
	public int excessTime_2_8;

	@Column(name = "EXCESS_TIME_2_9")
	public int excessTime_2_9;

	@Column(name = "EXCESS_TIME_2_10")
	public int excessTime_2_10;

	@Column(name = "EXCESS_TIME_3_1")
	public int excessTime_3_1;

	@Column(name = "EXCESS_TIME_3_2")
	public int excessTime_3_2;

	@Column(name = "EXCESS_TIME_3_3")
	public int excessTime_3_3;

	@Column(name = "EXCESS_TIME_3_4")
	public int excessTime_3_4;

	@Column(name = "EXCESS_TIME_3_5")
	public int excessTime_3_5;

	@Column(name = "EXCESS_TIME_3_6")
	public int excessTime_3_6;

	@Column(name = "EXCESS_TIME_3_7")
	public int excessTime_3_7;
	
	@Column(name = "EXCESS_TIME_3_8")
	public int excessTime_3_8;

	@Column(name = "EXCESS_TIME_3_9")
	public int excessTime_3_9;

	@Column(name = "EXCESS_TIME_3_10")
	public int excessTime_3_10;

	@Column(name = "EXCESS_TIME_4_1")
	public int excessTime_4_1;

	@Column(name = "EXCESS_TIME_4_2")
	public int excessTime_4_2;

	@Column(name = "EXCESS_TIME_4_3")
	public int excessTime_4_3;

	@Column(name = "EXCESS_TIME_4_4")
	public int excessTime_4_4;

	@Column(name = "EXCESS_TIME_4_5")
	public int excessTime_4_5;

	@Column(name = "EXCESS_TIME_4_6")
	public int excessTime_4_6;

	@Column(name = "EXCESS_TIME_4_7")
	public int excessTime_4_7;

	@Column(name = "EXCESS_TIME_4_8")
	public int excessTime_4_8;

	@Column(name = "EXCESS_TIME_4_9")
	public int excessTime_4_9;

	@Column(name = "EXCESS_TIME_4_10")
	public int excessTime_4_10;

	@Column(name = "EXCESS_TIME_5_1")
	public int excessTime_5_1;

	@Column(name = "EXCESS_TIME_5_2")
	public int excessTime_5_2;

	@Column(name = "EXCESS_TIME_5_3")
	public int excessTime_5_3;

	@Column(name = "EXCESS_TIME_5_4")
	public int excessTime_5_4;

	@Column(name = "EXCESS_TIME_5_5")
	public int excessTime_5_5;

	@Column(name = "EXCESS_TIME_5_6")
	public int excessTime_5_6;

	@Column(name = "EXCESS_TIME_5_7")
	public int excessTime_5_7;

	@Column(name = "EXCESS_TIME_5_8")
	public int excessTime_5_8;

	@Column(name = "EXCESS_TIME_5_9")
	public int excessTime_5_9;

	@Column(name = "EXCESS_TIME_5_10")
	public int excessTime_5_10;


	/* KRCDT_MON_AGREEMENT_TIME */

	/** 月別実績の勤怠時間．月の計算．36協定時間 - 36協定時間 -36協定時間  -AGREEMENT_TIME*/
	@Column(name = "AGREEMENT_TIME")
	public int agreementTime;

	/**月別実績の勤怠時間．月の計算．36協定時間 - 限度アラーム時間 - 限度エラー時間 - LIMIT_ERROR_TIME*/
	@Column(name = "LIMIT_ERROR_TIME")
	public int limitErrorTime;

	/**月別実績の勤怠時間．月の計算．36協定時間 - 特例限度エラー時間 - 限度アラーム時間 - LIMIT_ALARM_TIME*/
	@Column(name = "LIMIT_ALARM_TIME")
	public int limitAlarmTime;

	/** 月別実績の勤怠時間．月の計算．36協定時間 - 特例限度エラー時間 - 特例限度エラー時間 - EXCEPT_LIMIT_ERR_TIME*/
	@Column(name = "EXCEPT_LIMIT_ERR_TIME")
	public Integer exceptionLimitErrorTime;

	/** 月別実績の勤怠時間．月の計算．36協定時間 - 特例限度アラーム時間 - 特例限度アラーム時間 - EXCEPT_LIMIT_ALM_TIME */
	@Column(name = "EXCEPT_LIMIT_ALM_TIME")
	public Integer exceptionLimitAlarmTime;

	/**月別実績の勤怠時間．月の計算．36協定時間 - 状態- 状態  - AGREEMENT_STATUS*/
	@Column(name = "AGREEMENT_STATUS")
	public int status;

	/* KRCDT_MON_AFFILIATION - 月別実績の所属情報．月末の情報 */
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
	
	//月別実績の勤怠時間．縦計．勤務時刻．終業時刻 
	/** 終業回数 -終業回数*/
	@Column(name = "ENDWORK_TIMES")
	public int endWorkTimes;

	/** 終業合計時刻 - 終業合計時刻 */
	@Column(name = "ENDWORK_TOTAL_CLOCK")
	public int endWorkTotalClock;

	/** 終業平均時刻 - 終業平均時刻 */
	@Column(name = "ENDWORK_AVE_CLOCK")
	public int endWorkAveClock;

	//月別実績の勤怠時間．縦計．勤務時刻．PCログオン情報．PCログオン時刻．PCログオン時刻
	/** ログオン平均時刻 - ログオン平均時刻*/
	@Column(name = "LOGON_TOTAL_DAYS")
	public double logOnTotalDays;

	/** ログオン合計時刻 - ログオン合計時刻 */
	@Column(name = "LOGON_TOTAL_CLOCK")
	public int logOnTotalClock;

	/** ログオン平均時刻 -ログオン平均時刻 */
	@Column(name = "LOGON_AVE_CLOCK")
	public int logOnAveClock;

	//月別実績の勤怠時間．縦計．勤務時刻．PCログオン情報．PCログオン時刻．PCログオフ時刻
	/** ログオフ合計日数 -ログオフ合計日数*/
	@Column(name = "LOGOFF_TOTAL_DAYS")
	public double logOffTotalDays;

	/**ログオフ合計時刻 - ログオフ合計時刻 */
	@Column(name = "LOGOFF_TOTAL_CLOCK")
	public int logOffTotalClock;

	/**ログオフ平均時刻 - ログオフ平均時刻 */
	@Column(name = "LOGOFF_AVE_CLOCK")
	public int logOffAveClock;

	//月別実績の勤怠時間．縦計．勤務時刻．PCログオン情報．PCログオン乖離．PCログオン乖離
	/** ログオン乖離日数 - ログオン乖離日数*/
	@Column(name = "LOGON_DIV_DAYS")
	public double logOnDivDays;
	
	/** ログオン乖離合計時間 - ログオン乖離合計時間*/
	@Column(name = "LOGON_DIV_TOTAL_TIME")
	public int logOnDivTotalTime;
	
	/** ログオン乖離平均時間- ログオン乖離平均時間*/
	@Column(name = "LOGON_DIV_AVE_TIME")
	public int logOnDivAveTime;
	
	//月別実績の勤怠時間．縦計．勤務時刻．PCログオン情報．PCログオン乖離．PCログオフ乖離
	/** ログオフ乖離日数*/
	@Column(name = "LOGOFF_DIV_DAYS")
	public double logOffDivDays;
	
	/** ログオフ乖離合計時間*/
	@Column(name = "LOGOFF_DIV_TOTAL_TIME")
	public int logOffDivTotalTime;
	
	/**ログオフ乖離平均時間*/
	@Column(name = "LOGOFF_DIV_AVE_TIME")
	public int logOffDivAveTime;

	//月別実績の勤怠時間．回数集計．回数集計
	/** 回数集計時間1 */
	@Column(name = "TOTAL_COUNT_TIME_1")
	public int totalCountTime1;

	@Column(name = "TOTAL_COUNT_TIME_2")
	public int totalCountTime2;

	@Column(name = "TOTAL_COUNT_TIME_3")
	public int totalCountTime3;

	@Column(name = "TOTAL_COUNT_TIME_4")
	public int totalCountTime4;

	@Column(name = "TOTAL_COUNT_TIME_5")
	public int totalCountTime5;

	@Column(name = "TOTAL_COUNT_TIME_6")
	public int totalCountTime6;

	@Column(name = "TOTAL_COUNT_TIME_7")
	public int totalCountTime7;

	@Column(name = "TOTAL_COUNT_TIME_8")
	public int totalCountTime8;

	@Column(name = "TOTAL_COUNT_TIME_9")
	public int totalCountTime9;

	@Column(name = "TOTAL_COUNT_TIME_10")
	public int totalCountTime10;

	@Column(name = "TOTAL_COUNT_TIME_11")
	public int totalCountTime11;

	@Column(name = "TOTAL_COUNT_TIME_12")
	public int totalCountTime12;

	@Column(name = "TOTAL_COUNT_TIME_13")
	public int totalCountTime13;

	@Column(name = "TOTAL_COUNT_TIME_14")
	public int totalCountTime14;

	@Column(name = "TOTAL_COUNT_TIME_15")
	public int totalCountTime15;

	@Column(name = "TOTAL_COUNT_TIME_16")
	public int totalCountTime16;

	@Column(name = "TOTAL_COUNT_TIME_17")
	public int totalCountTime17;

	@Column(name = "TOTAL_COUNT_TIME_18")
	public int totalCountTime18;

	@Column(name = "TOTAL_COUNT_TIME_19")
	public int totalCountTime19;

	@Column(name = "TOTAL_COUNT_TIME_20")
	public int totalCountTime20;

	@Column(name = "TOTAL_COUNT_TIME_21")
	public int totalCountTime21;

	@Column(name = "TOTAL_COUNT_TIME_22")
	public int totalCountTime22;

	@Column(name = "TOTAL_COUNT_TIME_23")
	public int totalCountTime23;

	@Column(name = "TOTAL_COUNT_TIME_24")
	public int totalCountTime24;

	@Column(name = "TOTAL_COUNT_TIME_25")
	public int totalCountTime25;

	@Column(name = "TOTAL_COUNT_TIME_26")
	public int totalCountTime26;

	@Column(name = "TOTAL_COUNT_TIME_27")
	public int totalCountTime27;

	@Column(name = "TOTAL_COUNT_TIME_28")
	public int totalCountTime28;

	@Column(name = "TOTAL_COUNT_TIME_29")
	public int totalCountTime29;

	@Column(name = "TOTAL_COUNT_TIME_30")
	public int totalCountTime30;
	
	//月別実績の勤怠時間．回数集計．回数集計
	/** 回数 - 回数集計回数1*/
	@Column(name = "TOTAL_COUNT_DAYS1")
	public double totalCountDays1;

	@Column(name = "TOTAL_COUNT_DAYS2")
	public double totalCountDays2;

	@Column(name = "TOTAL_COUNT_DAYS3")
	public double totalCountDays3;

	@Column(name = "TOTAL_COUNT_DAYS4")
	public double totalCountDays4;

	@Column(name = "TOTAL_COUNT_DAYS5")
	public double totalCountDays5;

	@Column(name = "TOTAL_COUNT_DAYS6")
	public double totalCountDays6;

	@Column(name = "TOTAL_COUNT_DAYS7")
	public double totalCountDays7;

	@Column(name = "TOTAL_COUNT_DAYS8")
	public double totalCountDays8;

	@Column(name = "TOTAL_COUNT_DAYS9")
	public double totalCountDays9;

	@Column(name = "TOTAL_COUNT_DAYS10")
	public double totalCountDays10;

	@Column(name = "TOTAL_COUNT_DAYS11")
	public double totalCountDays11;

	@Column(name = "TOTAL_COUNT_DAYS12")
	public double totalCountDays12;

	@Column(name = "TOTAL_COUNT_DAYS13")
	public double totalCountDays13;

	@Column(name = "TOTAL_COUNT_DAYS14")
	public double totalCountDays14;

	@Column(name = "TOTAL_COUNT_DAYS15")
	public double totalCountDays15;

	@Column(name = "TOTAL_COUNT_DAYS16")
	public double totalCountDays16;

	@Column(name = "TOTAL_COUNT_DAYS17")
	public double totalCountDays17;

	@Column(name = "TOTAL_COUNT_DAYS18")
	public double totalCountDays18;

	@Column(name = "TOTAL_COUNT_DAYS19")
	public double totalCountDays19;

	@Column(name = "TOTAL_COUNT_DAYS20")
	public double totalCountDays20;

	@Column(name = "TOTAL_COUNT_DAYS21")
	public double totalCountDays21;

	@Column(name = "TOTAL_COUNT_DAYS22")
	public double totalCountDays22;

	@Column(name = "TOTAL_COUNT_DAYS23")
	public double totalCountDays23;

	@Column(name = "TOTAL_COUNT_DAYS24")
	public double totalCountDays24;

	@Column(name = "TOTAL_COUNT_DAYS25")
	public double totalCountDays25;

	@Column(name = "TOTAL_COUNT_DAYS26")
	public double totalCountDays26;

	@Column(name = "TOTAL_COUNT_DAYS27")
	public double totalCountDays27;

	@Column(name = "TOTAL_COUNT_DAYS28")
	public double totalCountDays28;

	@Column(name = "TOTAL_COUNT_DAYS29")
	public double totalCountDays29;

	@Column(name = "TOTAL_COUNT_DAYS30")
	public double totalCountDays30;
	
	@Override
	protected Object getKey() {
		return this.krcdtMonMergePk;
	}
	
	public 	MonthMergeKey toDomainKey() {
		MonthMergeKey key = new MonthMergeKey();
		key.setEmployeeId(this.krcdtMonMergePk.getEmployeeId());
		key.setYearMonth(new YearMonth(this.krcdtMonMergePk.getYearMonth()));
		key.setClosureId(EnumAdaptor.valueOf(this.krcdtMonMergePk.getClosureId(), ClosureId.class));
		key.setClosureDate(new ClosureDate(this.krcdtMonMergePk.getClosureDay(),
			(this.krcdtMonMergePk.getIsLastDay() == 1)));
		return key;
	}

	/** KRCDT_MON_AGGR_OVER_TIME 10 **/
	private void toEntityOverTime(Map<OverTimeFrameNo, AggregateOverTime> aggregateOverTimeMap) {
		for (int i = 1; i <= 10; i++){
			OverTimeFrameNo frameNo = new OverTimeFrameNo(i);
			AggregateOverTime aggrOverTime = new AggregateOverTime(frameNo);
			if (aggregateOverTimeMap.containsKey(frameNo)){
				aggrOverTime = aggregateOverTimeMap.get(frameNo);
			}
			switch (i) {
			case 1:
				toEntityOverTime1(aggrOverTime);
				break;
			case 2:
				toEntityOverTime2(aggrOverTime);
				break;
			case 3:
				toEntityOverTime3(aggrOverTime);
				break;
			case 4:
				toEntityOverTime4(aggrOverTime);
				break;
			case 5:
				toEntityOverTime5(aggrOverTime);
				break;
			case 6:
				toEntityOverTime6(aggrOverTime);
				break;
			case 7:
				toEntityOverTime7(aggrOverTime);
				break;
			case 8:
				toEntityOverTime8(aggrOverTime);
				break;
			case 9:
				toEntityOverTime9(aggrOverTime);
				break;
			case 10:
				toEntityOverTime10(aggrOverTime);
				break;
			}
		}
	}

	/** KRCDT_MON_EXCOUT_TIME 50 **/
	private void toEntityExcessOutsideWorkMerge(ExcessOutsideWorkOfMonthly excessOutsideWork) {
		
		// 時間外超過
		toEntityExcessOutsideWorkOfMonthly(excessOutsideWork);
		
		// 時間外超過：時間
		Map<Integer, ExcessOutSideWorkEachBreakdown> excessOutsideTimeMap = excessOutsideWork == null ? new HashMap<>() : excessOutsideWork.getTime();
		
		for (int i = 0; i < 50; i++){
			int breakdownNo = i / 5 + 1;
			int excessNo = i % 5 + 1;
			
			ExcessOutsideWork excessOutsideTime = new ExcessOutsideWork(breakdownNo, excessNo);
			if (excessOutsideTimeMap.containsKey(breakdownNo)){
				Map<Integer, ExcessOutsideWork> breakdown = excessOutsideTimeMap.get(breakdownNo).getBreakdown();
				if (breakdown.containsKey(excessNo)){
					excessOutsideTime = breakdown.get(excessNo);
				}
			}
			switch (i + 1){
			case 1:
				toEntityExcessOutsideWork1(excessOutsideTime);
				break;
			case 2:
				toEntityExcessOutsideWork2(excessOutsideTime);
				break;
			case 3:
				toEntityExcessOutsideWork3(excessOutsideTime);
				break;
			case 4:
				toEntityExcessOutsideWork4(excessOutsideTime);
				break;
			case 5:
				toEntityExcessOutsideWork5(excessOutsideTime);
				break;
			case 6:
				toEntityExcessOutsideWork6(excessOutsideTime);
				break;
			case 7:
				toEntityExcessOutsideWork7(excessOutsideTime);
				break;
			case 8:
				toEntityExcessOutsideWork8(excessOutsideTime);
				break;
			case 9:
				toEntityExcessOutsideWork9(excessOutsideTime);
				break;
			case 10:
				toEntityExcessOutsideWork10(excessOutsideTime);
				break;
			case 11:
				toEntityExcessOutsideWork11(excessOutsideTime);
				break;
			case 12:
				toEntityExcessOutsideWork12(excessOutsideTime);
				break;
			case 13:
				toEntityExcessOutsideWork13(excessOutsideTime);
				break;
			case 14:
				toEntityExcessOutsideWork14(excessOutsideTime);
				break;
			case 15:
				toEntityExcessOutsideWork15(excessOutsideTime);
				break;
			case 16:
				toEntityExcessOutsideWork16(excessOutsideTime);
				break;
			case 17:
				toEntityExcessOutsideWork17(excessOutsideTime);
				break;
			case 18:
				toEntityExcessOutsideWork18(excessOutsideTime);
				break;
			case 19:
				toEntityExcessOutsideWork19(excessOutsideTime);
				break;
			case 20:
				toEntityExcessOutsideWork20(excessOutsideTime);
				break;
			case 21:
				toEntityExcessOutsideWork21(excessOutsideTime);
				break;
			case 22:
				toEntityExcessOutsideWork22(excessOutsideTime);
				break;
			case 23:
				toEntityExcessOutsideWork23(excessOutsideTime);
				break;
			case 24:
				toEntityExcessOutsideWork24(excessOutsideTime);
				break;
			case 25:
				toEntityExcessOutsideWork25(excessOutsideTime);
				break;
			case 26:
				toEntityExcessOutsideWork26(excessOutsideTime);
				break;
			case 27:
				toEntityExcessOutsideWork27(excessOutsideTime);
				break;
			case 28:
				toEntityExcessOutsideWork28(excessOutsideTime);
				break;
			case 29:
				toEntityExcessOutsideWork29(excessOutsideTime);
				break;
			case 30:
				toEntityExcessOutsideWork30(excessOutsideTime);
				break;
			case 31:
				toEntityExcessOutsideWork31(excessOutsideTime);
				break;
			case 32:
				toEntityExcessOutsideWork32(excessOutsideTime);
				break;
			case 33:
				toEntityExcessOutsideWork33(excessOutsideTime);
				break;
			case 34:
				toEntityExcessOutsideWork34(excessOutsideTime);
				break;
			case 35:
				toEntityExcessOutsideWork35(excessOutsideTime);
				break;
			case 36:
				toEntityExcessOutsideWork36(excessOutsideTime);
				break;
			case 37:
				toEntityExcessOutsideWork37(excessOutsideTime);
				break;
			case 38:
				toEntityExcessOutsideWork38(excessOutsideTime);
				break;
			case 39:
				toEntityExcessOutsideWork39(excessOutsideTime);
				break;
			case 40:
				toEntityExcessOutsideWork40(excessOutsideTime);
				break;				
			case 41:
				toEntityExcessOutsideWork41(excessOutsideTime);
				break;
			case 42:
				toEntityExcessOutsideWork42(excessOutsideTime);
				break;
			case 43:
				toEntityExcessOutsideWork43(excessOutsideTime);
				break;
			case 44:
				toEntityExcessOutsideWork44(excessOutsideTime);
				break;
			case 45:
				toEntityExcessOutsideWork45(excessOutsideTime);
				break;			
			case 46:
				toEntityExcessOutsideWork46(excessOutsideTime);
				break;
			case 47:
				toEntityExcessOutsideWork47(excessOutsideTime);
				break;
			case 48:
				toEntityExcessOutsideWork48(excessOutsideTime);
				break;
			case 49:
				toEntityExcessOutsideWork49(excessOutsideTime);
				break;
			case 50:
				toEntityExcessOutsideWork50(excessOutsideTime);
				break;				
			} 
		}
	}

	private void toEntityAbsenceDays1(AggregateAbsenceDays absenceDays) {
		this.absenceDay1 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime1 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays2(AggregateAbsenceDays absenceDays) {
		this.absenceDay2 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime2 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays3(AggregateAbsenceDays absenceDays) {
		this.absenceDay3 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime3 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays4(AggregateAbsenceDays absenceDays) {
		this.absenceDay4 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime4 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays5(AggregateAbsenceDays absenceDays) {
		this.absenceDay5 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime5 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays6(AggregateAbsenceDays absenceDays) {
		this.absenceDay6 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime6 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays7(AggregateAbsenceDays absenceDays) {
		this.absenceDay7 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime7 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays8(AggregateAbsenceDays absenceDays) {
		this.absenceDay8 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime8 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays9(AggregateAbsenceDays absenceDays) {
		this.absenceDay9 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime9 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays10(AggregateAbsenceDays absenceDays) {
		this.absenceDay10 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime10 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays11(AggregateAbsenceDays absenceDays) {
		this.absenceDay11 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime11 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays12(AggregateAbsenceDays absenceDays) {
		this.absenceDay12 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime12 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays13(AggregateAbsenceDays absenceDays) {
		this.absenceDay13 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime13 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays14(AggregateAbsenceDays absenceDays) {
		this.absenceDay14 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime14 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays15(AggregateAbsenceDays absenceDays) {
		this.absenceDay15 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime15 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays16(AggregateAbsenceDays absenceDays) {
		this.absenceDay16 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime16 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays17(AggregateAbsenceDays absenceDays) {
		this.absenceDay17 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime17 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays18(AggregateAbsenceDays absenceDays) {
		this.absenceDay18 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime18 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays19(AggregateAbsenceDays absenceDays) {
		this.absenceDay19 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime19 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays20(AggregateAbsenceDays absenceDays) {
		this.absenceDay20 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime20 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays21(AggregateAbsenceDays absenceDays) {
		this.absenceDay21 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime21 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays22(AggregateAbsenceDays absenceDays) {
		this.absenceDay22 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime22 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays23(AggregateAbsenceDays absenceDays) {
		this.absenceDay23 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime23 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays24(AggregateAbsenceDays absenceDays) {
		this.absenceDay24 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime24 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays25(AggregateAbsenceDays absenceDays) {
		this.absenceDay25 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime25 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays26(AggregateAbsenceDays absenceDays) {
		this.absenceDay26 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime26 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays27(AggregateAbsenceDays absenceDays) {
		this.absenceDay27 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime27 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays28(AggregateAbsenceDays absenceDays) {
		this.absenceDay28 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime28 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays29(AggregateAbsenceDays absenceDays) {
		this.absenceDay29 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime29 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityAbsenceDays30(AggregateAbsenceDays absenceDays) {
		this.absenceDay30 = absenceDays == null || absenceDays.getDays() == null ? 0
				: (absenceDays.getDays().v() == null ? 0 : absenceDays.getDays().v().intValue());
		this.absenceTime30 = absenceDays == null || absenceDays.getTime() == null ? 0
				: (absenceDays.getTime().v() == null ? 0 : absenceDays.getTime().v());
	}

	private void toEntityBonusPayTime1(AggregateBonusPayTime paytime) {
		this.bonusPayTime1 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime1 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime1 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime1 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	private void toEntityBonusPayTime2(AggregateBonusPayTime paytime) {
		this.bonusPayTime2 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime2 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime2 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime2 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	private void toEntityBonusPayTime3(AggregateBonusPayTime paytime) {
		this.bonusPayTime3 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime3 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime3 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime3 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	private void toEntityBonusPayTime4(AggregateBonusPayTime paytime) {
		this.bonusPayTime4 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime4 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime4 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime4 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	private void toEntityBonusPayTime5(AggregateBonusPayTime paytime) {
		this.bonusPayTime5 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime5 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime5 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime5 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	private void toEntityBonusPayTime6(AggregateBonusPayTime paytime) {
		this.bonusPayTime6 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime6 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime6 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime6 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	private void toEntityBonusPayTime7(AggregateBonusPayTime paytime) {
		this.bonusPayTime7 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime7 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime7 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime7 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	private void toEntityBonusPayTime8(AggregateBonusPayTime paytime) {
		this.bonusPayTime8 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime8 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime8 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime8 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	private void toEntityBonusPayTime9(AggregateBonusPayTime paytime) {
		this.bonusPayTime9 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime9 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime9 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime9 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	private void toEntityBonusPayTime10(AggregateBonusPayTime paytime) {
		this.bonusPayTime10 = paytime.getBonusPayTime() == null ? 0 : paytime.getBonusPayTime().v();
		this.specificBonusPayTime10 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getSpecificBonusPayTime().v();
		this.holidayWorkBonusPayTime10 = paytime.getHolidayWorkBonusPayTime() == null ? 0
				: paytime.getHolidayWorkBonusPayTime().v();
		this.holidayWorkSpecificBonusPayTime10 = paytime.getHolidayWorkSpecificBonusPayTime() == null ? 0
				: paytime.getHolidayWorkSpecificBonusPayTime().v();
	}

	private void toEntityDivergenceTime1(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr1 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime1 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime1 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction1 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	private void toEntityDivergenceTime2(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr2 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime2 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime2 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction2 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	private void toEntityDivergenceTime3(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr3 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime3 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime3 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction3 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	private void toEntityDivergenceTime4(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr4 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime4 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime4 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction4 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	private void toEntityDivergenceTime5(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr5 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime5 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime5 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction5 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	private void toEntityDivergenceTime6(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr6 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime6 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime6 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction6 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	private void toEntityDivergenceTime7(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr7 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime7 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime7 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction7 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	private void toEntityDivergenceTime8(AggregateDivergenceTime divergenceTime) {
		this.divergenceAtr8 = divergenceTime.getDivergenceAtr() == null ? 0 : divergenceTime.getDivergenceAtr().value;
		this.divergenceTime8 = divergenceTime.getDivergenceTime() == null ? 0 : divergenceTime.getDivergenceTime().v();
		this.deductionTime8 = divergenceTime.getDeductionTime() == null ? 0 : divergenceTime.getDeductionTime().v();
		this.divergenceTimeAfterDeduction8 = divergenceTime.getDivergenceTimeAfterDeduction() == null ? 0
				: divergenceTime.getDivergenceTimeAfterDeduction().v();
	}

	private void toEntityDivergenceTime9(AggregateDivergenceTime divergenceTime) {
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

	private void toEntityGoOut1(AggregateGoOut goOut) {
		this.goOutTimesPrivate = goOut == null || goOut.getTimes() == null ? 0
				: (goOut.getTimes().v() == null ? 0
						: (goOut.getTimes().v() == null ? 0 : goOut.getTimes().v().intValue()));
		this.legalTimePrivate = goOut == null || goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getTime() == null ? 0 : goOut.getLegalTime().getTime().v());
		this.calcLegalTimePrivate = goOut == null || goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getCalcTime() == null ? 0 : goOut.getLegalTime().getCalcTime().v());
		this.illegalTimePrivate = goOut == null || goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getTime() == null ? 0 : goOut.getIllegalTime().getTime().v());
		this.calcIllegalTimePrivate = goOut == null || goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getCalcTime() == null ? 0 : goOut.getIllegalTime().getCalcTime().v());
		this.totalTimePrivate = goOut == null || goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getTime() == null ? 0 : goOut.getTotalTime().getTime().v());
		this.calcTotalTimePrivate = goOut == null || goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getCalcTime() == null ? 0 : goOut.getTotalTime().getCalcTime().v());
	}

	private void toEntityGoOut2(AggregateGoOut goOut) {
		this.goOutTimesPublic = goOut == null || goOut.getTimes() == null ? 0
				: (goOut.getTimes().v() == null ? 0
						: (goOut.getTimes().v() == null ? 0 : goOut.getTimes().v().intValue()));
		this.legalTimePublic = goOut == null || goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getTime() == null ? 0 : goOut.getLegalTime().getTime().v());
		this.calcLegalTimePublic = goOut == null || goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getCalcTime() == null ? 0 : goOut.getLegalTime().getCalcTime().v());
		this.illegalTimePublic = goOut == null || goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getTime() == null ? 0 : goOut.getIllegalTime().getTime().v());
		this.calcIllegalTimePublic = goOut == null || goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getCalcTime() == null ? 0 : goOut.getIllegalTime().getCalcTime().v());
		this.totalTimePublic = goOut == null || goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getTime() == null ? 0 : goOut.getTotalTime().getTime().v());
		this.calcTotalTimePublic = goOut == null || goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getCalcTime() == null ? 0 : goOut.getTotalTime().getCalcTime().v());
	}

	private void toEntityGoOut3(AggregateGoOut goOut) {
		this.goOutTimesCompensation = goOut == null || goOut.getTimes() == null ? 0
				: (goOut.getTimes().v() == null ? 0
						: (goOut.getTimes().v() == null ? 0 : goOut.getTimes().v().intValue()));
		this.legalTimeCompensation = goOut == null || goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getTime() == null ? 0 : goOut.getLegalTime().getTime().v());
		this.calcLegalTimeCompensation = goOut == null || goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getCalcTime() == null ? 0 : goOut.getLegalTime().getCalcTime().v());
		this.illegalTimeCompensation = goOut == null || goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getTime() == null ? 0 : goOut.getIllegalTime().getTime().v());
		this.calcIllegalTimeCompensation = goOut == null || goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getCalcTime() == null ? 0 : goOut.getIllegalTime().getCalcTime().v());
		this.totalTimeCompensation = goOut == null || goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getTime() == null ? 0 : goOut.getTotalTime().getTime().v());
		this.calcTotalTimeCompensation = goOut == null || goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getCalcTime() == null ? 0 : goOut.getTotalTime().getCalcTime().v());
	}

	private void toEntityGoOut4(AggregateGoOut goOut) {
		this.goOutTimesUnion = goOut == null || goOut.getTimes() == null ? 0
				: (goOut.getTimes().v() == null ? 0 : goOut.getTimes().v().intValue());
		this.legalTimeUnion = goOut == null || goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getTime() == null ? 0 : goOut.getLegalTime().getTime().v());
		this.calcLegalTimeUnion = goOut == null || goOut.getLegalTime() == null ? 0
				: (goOut.getLegalTime().getCalcTime() == null ? 0 : goOut.getLegalTime().getCalcTime().v());
		this.illegalTimeUnion = goOut == null || goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getTime() == null ? 0 : goOut.getIllegalTime().getTime().v());
		this.calcIllegalTimeUnion = goOut == null || goOut.getIllegalTime() == null ? 0
				: (goOut.getIllegalTime().getCalcTime() == null ? 0 : goOut.getIllegalTime().getCalcTime().v());
		this.totalTimeUnion = goOut == null || goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getTime() == null ? 0 : goOut.getTotalTime().getTime().v());
		this.calcTotalTimeUnion = goOut == null || goOut.getTotalTime() == null ? 0
				: (goOut.getTotalTime().getCalcTime() == null ? 0 : goOut.getTotalTime().getCalcTime().v());
	}

	/* KRCDT_MON_AGGR_HDWK_TIME 10 */
	private void toEntityHolidayWorkTime1(AggregateHolidayWorkTime domain) {
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

	private void toEntityHolidayWorkTime2(AggregateHolidayWorkTime domain) {
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

	private void toEntityHolidayWorkTime3(AggregateHolidayWorkTime domain) {
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

	private void toEntityHolidayWorkTime4(AggregateHolidayWorkTime domain) {
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

	private void toEntityHolidayWorkTime5(AggregateHolidayWorkTime domain) {
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

	private void toEntityHolidayWorkTime6(AggregateHolidayWorkTime domain) {
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

	private void toEntityHolidayWorkTime7(AggregateHolidayWorkTime domain) {
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

	private void toEntityHolidayWorkTime8(AggregateHolidayWorkTime domain) {
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

	private void toEntityHolidayWorkTime9(AggregateHolidayWorkTime domain) {
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

	private void toEntityHolidayWorkTime10(AggregateHolidayWorkTime domain) {
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
	private void toEntityOverTime1(AggregateOverTime domain) {
		this.overTime1 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime1 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime1 = domain.getBeforeOverTime().v();
		this.transferOverTime1 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime1 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime1 = domain.getLegalOverTime().v();
		this.legalTransferOverTime1 = domain.getLegalTransferOverTime().v();
	}

	private void toEntityOverTime2(AggregateOverTime domain) {
		this.overTime2 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime2 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime2 = domain.getBeforeOverTime().v();
		this.transferOverTime2 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime2 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime2 = domain.getLegalOverTime().v();
		this.legalTransferOverTime2 = domain.getLegalTransferOverTime().v();
	}

	private void toEntityOverTime3(AggregateOverTime domain) {
		this.overTime3 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime3 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime3 = domain.getBeforeOverTime().v();
		this.transferOverTime3 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime3 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime3 = domain.getLegalOverTime().v();
		this.legalTransferOverTime3 = domain.getLegalTransferOverTime().v();
	}

	private void toEntityOverTime4(AggregateOverTime domain) {
		this.overTime4 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime4 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime4 = domain.getBeforeOverTime().v();
		this.transferOverTime4 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime4 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime4 = domain.getLegalOverTime().v();
		this.legalTransferOverTime4 = domain.getLegalTransferOverTime().v();
	}

	private void toEntityOverTime5(AggregateOverTime domain) {
		this.overTime5 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime5 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime5 = domain.getBeforeOverTime().v();
		this.transferOverTime5 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime5 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime5 = domain.getLegalOverTime().v();
		this.legalTransferOverTime5 = domain.getLegalTransferOverTime().v();
	}

	private void toEntityOverTime6(AggregateOverTime domain) {
		this.overTime6 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime6 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime6 = domain.getBeforeOverTime().v();
		this.transferOverTime6 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime6 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime6 = domain.getLegalOverTime().v();
		this.legalTransferOverTime6 = domain.getLegalTransferOverTime().v();
	}

	private void toEntityOverTime7(AggregateOverTime domain) {
		this.overTime7 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime7 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime7 = domain.getBeforeOverTime().v();
		this.transferOverTime7 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime7 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime7 = domain.getLegalOverTime().v();
		this.legalTransferOverTime7 = domain.getLegalTransferOverTime().v();
	}

	private void toEntityOverTime8(AggregateOverTime domain) {
		this.overTime8 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime8 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime8 = domain.getBeforeOverTime().v();
		this.transferOverTime8 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime8 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime8 = domain.getLegalOverTime().v();
		this.legalTransferOverTime8 = domain.getLegalTransferOverTime().v();
	}

	private void toEntityOverTime9(AggregateOverTime domain) {
		this.overTime9 = domain.getOverTime() == null ? 0 : domain.getOverTime().getTime().v();
		this.calcOverTime9 = domain.getOverTime() == null ? 0 : domain.getOverTime().getCalcTime().v();
		this.beforeOverTime9 = domain.getBeforeOverTime().v();
		this.transferOverTime9 = domain.getTransferOverTime() == null ? 0 : domain.getTransferOverTime().getTime().v();
		this.calcTransferOverTime9 = domain.getTransferOverTime() == null ? 0
				: domain.getTransferOverTime().getCalcTime().v();
		this.legalOverTime9 = domain.getLegalOverTime().v();
		this.legalTransferOverTime9 = domain.getLegalTransferOverTime().v();
	}

	private void toEntityOverTime10(AggregateOverTime domain) {
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
	private void toEntityPremiumTime1(AggregatePremiumTime domain) {
		this.premiumTime1 = domain.getTime().v();

	}

	private void toEntityPremiumTime2(AggregatePremiumTime domain) {
		this.premiumTime2 = domain.getTime().v();

	}

	private void toEntityPremiumTime3(AggregatePremiumTime domain) {
		this.premiumTime3 = domain.getTime().v();

	}

	private void toEntityPremiumTime4(AggregatePremiumTime domain) {
		this.premiumTime4 = domain.getTime().v();

	}

	private void toEntityPremiumTime5(AggregatePremiumTime domain) {
		this.premiumTime5 = domain.getTime().v();
	}

	private void toEntityPremiumTime6(AggregatePremiumTime domain) {
		this.premiumTime6 = domain.getTime().v();

	}

	private void toEntityPremiumTime7(AggregatePremiumTime domain) {
		this.premiumTime7 = domain.getTime().v();

	}

	private void toEntityPremiumTime8(AggregatePremiumTime domain) {
		this.premiumTime8 = domain.getTime().v();

	}

	private void toEntityPremiumTime9(AggregatePremiumTime domain) {
		this.premiumTime9 = domain.getTime().v();

	}

	private void toEntityPremiumTime10(AggregatePremiumTime domain) {
		this.premiumTime10 = domain.getTime().v();

	}

	/** KRCDT_MON_AGGR_SPEC_DAYS 10 **/
	private void toEntitySpecificDays1(AggregateSpecificDays domain) {
		this.specificDays1 = domain == null || domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays1 = domain == null || domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	private void toEntitySpecificDays2(AggregateSpecificDays domain) {
		this.specificDays2 = domain == null || domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays2 = domain == null || domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	private void toEntitySpecificDays3(AggregateSpecificDays domain) {
		this.specificDays3 = domain == null || domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays3 = domain == null || domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	private void toEntitySpecificDays4(AggregateSpecificDays domain) {
		this.specificDays4 = domain == null || domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays4 = domain == null || domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	private void toEntitySpecificDays5(AggregateSpecificDays domain) {
		this.specificDays5 = domain == null || domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays5 = domain == null || domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	private void toEntitySpecificDays6(AggregateSpecificDays domain) {
		this.specificDays6 = domain == null || domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays6 = domain == null || domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	private void toEntitySpecificDays7(AggregateSpecificDays domain) {
		this.specificDays7 = domain == null || domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays7 = domain == null || domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	private void toEntitySpecificDays8(AggregateSpecificDays domain) {
		this.specificDays8 = domain == null || domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays8 = domain == null || domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	private void toEntitySpecificDays9(AggregateSpecificDays domain) {
		this.specificDays9 = domain == null || domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays9 = domain == null || domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	private void toEntitySpecificDays10(AggregateSpecificDays domain) {
		this.specificDays10 = domain == null || domain.getSpecificDays() == null ? 0 : domain.getSpecificDays().v();
		this.holidayWorkSpecificDays10 = domain == null || domain.getHolidayWorkSpecificDays() == null ? 0
				: domain.getHolidayWorkSpecificDays().v();
	}

	/* KRCDT_MON_AGGR_TOTAL_SPT */
	private void toEntityTotalTimeSpentAtWork(AggregateTotalTimeSpentAtWork domain) {
		this.overTimeSpentAtWork = domain == null ? 0 : domain.getOverTimeSpentAtWork().v();
		this.midnightTimeSpentAtWork = domain == null ? 0 : domain.getMidnightTimeSpentAtWork().v();
		this.holidayTimeSpentAtWork = domain == null ? 0 : domain.getHolidayTimeSpentAtWork().v();
		this.varienceTimeSpentAtWork = domain == null ? 0 : domain.getVarienceTimeSpentAtWork().v();
		this.totalTimeSpentAtWork = domain == null ? 0 : domain.getTotalTimeSpentAtWork().v();
	}

	/* KRCDT_MON_AGGR_TOTAL_WRK */
	private void toEntityTotalWorkingTime(AggregateTotalWorkingTime domain) {
		/** 就業時間 */
		WorkTimeOfMonthly workTime = domain.getWorkTime();
		/** 所定労働時間 */
		PrescribedWorkingTimeOfMonthly prescribedWorkingTime = domain.getPrescribedWorkingTime();
		/** 残業時間 */
		OverTimeOfMonthly overTime = domain.getOverTime();
		/** 休出時間 */
		HolidayWorkTimeOfMonthly holidayWorkTime = domain.getHolidayWorkTime();
		/** 休暇使用時間 */
		VacationUseTimeOfMonthly vacationUseTime = domain.getVacationUseTime();
		toEntityWorkTimeOfMonthly(workTime);
		toEntityPrescribedWorkingTimeOfMonthly(prescribedWorkingTime);
		toEntityOverTimeOfMonthly(overTime);
		toEntityHolidayWorkTimeOfMonthly(holidayWorkTime);
		toEntityVacationUseTimeOfMonth(vacationUseTime);
	}
	
	/** 就業時間 */
	private void toEntityWorkTimeOfMonthly(WorkTimeOfMonthly workTime) {
		this.workTime = workTime.getWorkTime().v();
		this.actualWorkTime = workTime.getActualWorkTime().v();
		this.withinPrescribedPremiumTime = workTime.getWithinPrescribedPremiumTime().v();
	}
	
	/** 所定労働時間 */
	private void toEntityPrescribedWorkingTimeOfMonthly(PrescribedWorkingTimeOfMonthly prescribedWorkingTime) {
		this.schedulePrescribedWorkingTime = prescribedWorkingTime.getSchedulePrescribedWorkingTime().v();
		this.recordPrescribedWorkingTime = prescribedWorkingTime.getRecordPrescribedWorkingTime().v();
	}

	/* KRCDT_MON_ATTENDANCE_TIME */
	public void toEntityAttendanceTimeOfMonthly(AttendanceTimeOfMonthly domain) {
		
		this.startYmd = domain.getDatePeriod().start();
		this.endYmd = domain.getDatePeriod().end();
		this.aggregateDays = domain.getAggregateDays().v();
		
		/** 月の計算 */
		val monthlyCalculation = domain.getMonthlyCalculation();
		toEntityMonthlyCalculation(monthlyCalculation);
		
		/** 時間外超過 */
		val excessOutsideWork = domain.getExcessOutsideWork();
		toEntityExcessOutsideWorkMerge(excessOutsideWork);
		
		/** 縦計 */
		val verticalTotal = domain.getVerticalTotal();
		toEntityVerticalTotal(verticalTotal);
		
		/** 回数集計 */
		val totalCount = domain.getTotalCount();
		toEntityTotalCount(totalCount.getTotalCountList());
	}
	
	public void resetAttendanceTime() {
		
		this.aggregateDays = 0;
		
		this.weeklyTotalPremiumTime = 0;
		this.monthlyTotalPremiumTime = 0;
		this.multiMonthIrregularMiddleTime = 0;
		this.irregularPeriodCarryforwardTime = 0;
		this.irregularWorkingShortageTime = 0;
		this.irregularLegalOverTime = 0;
		this.calcIrregularLegalOverTime = 0;
		
		this.flexTime = 0;
		this.calcFlexTime = 0;
		this.beforeFlexTime = 0;
		this.legalFlexTime = 0;
		this.illegalFlexTime = 0;
		this.flexExcessTime = 0;
		this.flexShortageTime = 0;
		this.flexCarryforwardWorkTime = 0;
		this.flexCarryforwardTime = 0;
		this.flexCarryforwardShortageTime = 0;
		this.excessFlexAtr = 0;
		this.principleTime = 0;
		this.forConvenienceTime = 0;
		this.annualLeaveDeductDays = 0;
		this.absenceDeductTime = 0;
		this.shotTimeBeforeDeduct = 0;
		
		/** 法定労働時間 */
		this.statutoryWorkingTime = 0;
		
		/** 集計時間 */
		this.workTime = 0;
		this.actualWorkTime = 0;
		this.withinPrescribedPremiumTime = 0;
		this.schedulePrescribedWorkingTime = 0;
		this.recordPrescribedWorkingTime = 0;
		this.totalOverTime = 0;
		this.calcTotalOverTime = 0;
		this.beforeOverTime = 0;
		this.totalTransferOverTime = 0;
		this.calcTotalTransferOverTime = 0;
		
		/** 集計残業時間 */
		toEntityOverTime(new HashMap<>());
		
		this.totalHolidayWorkTime = 0;
		this.calcTotalHolidayWorkTime = 0;
		this.beforeHolidayWorkTime = 0;
		this.totalTransferTime = 0;
		this.calcTotalTransferTime = 0;
		toEntityAggregateHolidayWorkTime(new HashMap<>());
		
		this.dayWorkMedicalTime = 0;
		this.dayDeductionTime = 0;
		this.dayTakeOverTime = 0;
		this.nightMedicalTime = 0;
		this.nightDeductionTime = 0;
		this.nightTakeOverTime = 0;
		
		/** 総労働時間 */
		this.totalWorkingTime = 0;
		
		/** 総拘束時間 */
		toEntityTotalTimeSpentAtWork(null);
		
		/** 36協定時間 */
		toEntityAgreementTimeOfMonthly(null);
		
		/** 時間外超過 */
		toEntityExcessOutsideWorkMerge(null);
		
		/** 縦計 */
		this.attendanceDays = 0;
		
		/** 欠勤日数 */
		this.totalAbsenceDays = 0;
		this.totalAbsenceTime = 0;
		toEntityAbsenceDays(new HashMap<>());
		
		/** 所定日数  */
		this.predetermineDays = 0;
		/** 勤務日数  */
		this.workDays = 0;
		/** 休日日数 */
		this.holidayDays = 0;
		
		/** 特定日数 SpecificDaysOfMonthly */
		toEntitySpecificDaysOfMonthly(new HashMap<>());
		
		/** 休出日数  */
		this.holidayWorkDays = 0;
		/** 給与用日数 */
		this.payAttendanceDays = 0;
		this.payAbsenceDays = 0;
		/** 勤務回数  */
		this.workTimes = 0;
		/** 二回勤務回数 */
		this.twoTimesWorkTimes = 0;
		/** 臨時勤務回数 */
		this.temporaryWorkTimes = 0;
		
		/** 休業 */
		this.toEntityLeaveOfMonthly(null);
		
		/** 振出日数 */
		this.recruitDays = 0;
		
		/** 特別休暇日数 */
		this.totalSpcvactDays = 0;
		this.totalSpcvactTime = 0;
		this.toEntitySpcVacationDaysList(new HashMap<>());
		
		/** 加給時間 */
		toEntityBonusPayTime(new HashMap<>());
		
		/** 外出 */
		toEntityGoOut1(null);
		toEntityGoOut2(null);
		toEntityGoOut3(null);
		toEntityGoOut4(null);
		
		/** 育児外出 */
		this.childcareGoOutTimes = 0;
		this.childcareGoOutTime = 0;
		this.careGoOutTimes = 0;
		this.careGoOutTime = 0;
		
		/** 割増時間 */
		toEntityPremiumTimeOfMonthly(null);
		
		/** 休憩時間 */
		toEntityBreakTime(null);
		
		/** 休日時間 */
		toEntityHolidayTime(null);
		
		/** 深夜時間 */
		this.overWorkMidnightTime = 0;
		this.calcOverWorkMidnightTime = 0;
		
		/** 法定内深夜時間 */
		this.legalMidnightTime = 0;
		this.calcLegalMidnightTime = 0;
		
		/** 法定外深夜時間 */
		this.illegalMidnightTime = 0;
		this.calcIllegalMidnightTime = 0;
		this.illegalBeforeMidnightTime = 0;
		
		/** 法定内休出深夜時間 */
		this.legalHolidayWorkMidnightTime = 0;
		this.calcLegalHolidayWorkMidnightTime = 0;
		
		/** 法定外休出深夜時間 */
		this.illegalHolidayWorkMidnightTime = 0;
		this.calcIllegalHolidayWorkMidnightTime = 0;
		
		/** 祝日休出深夜時間 */
		this.specialHolidayWorkMidnightTime = 0;
		this.calcSpecialHolidayWorkMidnightTime = 0;
		
		/** 遅刻早退 */
		this.lateTimes = 0;
		this.lateTime = 0;
		this.calcLateTime = 0;
		
		/** 早退 */
		this.leaveEarlyTimes = 0;
		this.leaveEarlyTime = 0;
		this.calcLeaveEarlyTime = 0;
		
		/** 出勤前時間 */
		this.attendanceLeaveGateBeforeAttendanceTime = 0;
		/** 退勤後時間 */
		this.attendanceLeaveGateAfterLeaveWorkTime = 0;
		/** 滞在時間 */
		this.attendanceLeaveGateStayingTime = 0;
		/** 不就労時間 */
		this.attendanceLeaveGateUnemployedTime = 0;
		
		/** 予実差異時間 */
		this.budgetVarienceTime  = 0;
		
		/** 乖離時間 */
		toEntityDivergenceTimeOfMonthly(null);
		
		/** 医療時間 */
		this.dayWorkMedicalTime = 0;
		this.dayDeductionTime = 0;
		this.dayTakeOverTime = 0;
		this.nightMedicalTime = 0;
		this.nightDeductionTime = 0;
		this.nightTakeOverTime = 0;
		
		/** 勤務時刻 */
		this.endWorkTimes = 0;
		this.endWorkTotalClock = 0;
		this.endWorkAveClock = 0;
		
		/** 合計日数 */
		this.logOnTotalDays = 0;
		/** 合計時刻 */
		this.logOnTotalClock = 0;
		/** 平均時刻 */
		this.logOnAveClock = 0;
		/** PCログオフ時刻 */
		this.logOffTotalDays = 0;
		this.logOffTotalClock = 0;
		this.logOffAveClock = 0;
		
		/** PCログオン乖離 */
		/** 日数 */
		this.logOnDivDays = 0;
		/** 合計時間 */
		this.logOnDivTotalTime = 0;
		/** 平均時間 */
		this.logOnDivAveTime = 0;
		
		/** PCログオフ乖離 */		
	    /** 日数 */
	    this.logOffDivDays = 0;
	    /** 合計時間 */
	    this.logOffDivTotalTime = 0;
	    /** 平均時間 */
	    this.logOffDivAveTime = 0;
		
		/** 回数集計 */
		toEntityTotalCount(new HashMap<>());
	}
	
	/**
	 * 月別実績の月の計算
	 * MonthlyCalculation
	 */
	private void toEntityMonthlyCalculation(MonthlyCalculation monthlyCalculation) {

		/** 実働時間 */
		val actualWorkingTime = monthlyCalculation.getActualWorkingTime();
		/** フレックス時間 */
		val flexTime = monthlyCalculation.getFlexTime();
		
		toEntityRegAndIrreTimeOfMonth(actualWorkingTime);
		
		toEntityFlexTimeOfMonthly(flexTime);
		
		/** 法定労働時間 */
		this.statutoryWorkingTime = monthlyCalculation.getStatutoryWorkingTime().v();
		
		/** 集計時間 */
		val aggregateTime = monthlyCalculation.getAggregateTime();
		toEntityTotalWorkingTime(aggregateTime);
		
		/** 総労働時間 */
		this.totalWorkingTime = monthlyCalculation.getTotalWorkingTime().v();
		
		/** 総拘束時間 */
		val totalTimeSpentAtWork = monthlyCalculation.getTotalTimeSpentAtWork();
		toEntityTotalTimeSpentAtWork(totalTimeSpentAtWork);
		
		/** 36協定時間 */
		val agreementTime = monthlyCalculation.getAgreementTime();
		toEntityAgreementTimeOfMonthly(agreementTime);
	}

	/* KRCDT_MON_FLEX_TIME */
	private void toEntityFlexTimeOfMonthly(FlexTimeOfMonthly domain) {

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

	/* KRCDT_MON_HDWK_TIME 集計時間：休出・代休：集計休出時間*/
	private void toEntityHolidayWorkTimeOfMonthly(HolidayWorkTimeOfMonthly domain) {
		/** 休出合計時間 */
		TimeMonthWithCalculation totalHolidayWorkTime = domain.getTotalHolidayWorkTime();
		/** 振替合計時間 */
		TimeMonthWithCalculation totalTransferTime = domain.getTotalTransferTime();
		/** 集計休出時間 */
		Map<HolidayWorkFrameNo, AggregateHolidayWorkTime> aggregateHolidayWorkTimeMap = domain.getAggregateHolidayWorkTimeMap();
		
		this.totalHolidayWorkTime = totalHolidayWorkTime.getTime().v();
		this.calcTotalHolidayWorkTime = totalHolidayWorkTime.getCalcTime().v();
		this.beforeHolidayWorkTime = domain.getBeforeHolidayWorkTime().v();
		this.totalTransferTime = totalTransferTime.getTime().v();
		this.calcTotalTransferTime = totalTransferTime.getCalcTime().v();
		toEntityAggregateHolidayWorkTime(aggregateHolidayWorkTimeMap);
	}
	
	/** KRCDT_MON_AGGR_HDWK_TIME 10 **/
	private void toEntityAggregateHolidayWorkTime(Map<HolidayWorkFrameNo, AggregateHolidayWorkTime> aggregateHolidayWorkTimeMap) {
		for (int i = 1; i <= 10; i++){
			HolidayWorkFrameNo frameNo = new HolidayWorkFrameNo(i);
			AggregateHolidayWorkTime aggrHolidayWorkTime = new AggregateHolidayWorkTime(frameNo);
			if (aggregateHolidayWorkTimeMap.containsKey(frameNo)){
				aggrHolidayWorkTime = aggregateHolidayWorkTimeMap.get(frameNo);
			}
			switch (i){
			case 1:
				toEntityHolidayWorkTime1(aggrHolidayWorkTime);
				break;
			case 2:
				toEntityHolidayWorkTime2(aggrHolidayWorkTime);
				break;
			case 3:
				toEntityHolidayWorkTime3(aggrHolidayWorkTime);
				break;
			case 4:
				toEntityHolidayWorkTime4(aggrHolidayWorkTime);
				break;
			case 5:
				toEntityHolidayWorkTime5(aggrHolidayWorkTime);
				break;
			case 6:
				toEntityHolidayWorkTime6(aggrHolidayWorkTime);
				break;
			case 7:
				toEntityHolidayWorkTime7(aggrHolidayWorkTime);
				break;
			case 8:
				toEntityHolidayWorkTime8(aggrHolidayWorkTime);
				break;
			case 9:
				toEntityHolidayWorkTime9(aggrHolidayWorkTime);
				break;
			case 10:
				toEntityHolidayWorkTime10(aggrHolidayWorkTime);
				break;
			}
		}
	}

	/* KRCDT_MON_LEAVE - リポジトリ：月別実績の休業 only update */
	private void toEntityLeaveOfMonthly(LeaveOfMonthly domain) {
		this.prenatalLeaveDays = 0.0;
		this.postpartumLeaveDays = 0.0;
		this.childcareLeaveDays = 0.0;
		this.careLeaveDays = 0.0;
		this.injuryOrIllnessLeaveDays = 0.0;
		this.anyLeaveDays01 = 0.0;
		this.anyLeaveDays02 = 0.0;
		this.anyLeaveDays03 = 0.0;
		this.anyLeaveDays04 = 0.0;
		if(domain == null){
			return;
		}
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
	private void toEntityMedicalTimeOfMonthly(Map<WorkTimeNightShift, MedicalTimeOfMonthly>  medicalTime) {
		for (WorkTimeNightShift i : medicalTime.keySet()) {
			MedicalTimeOfMonthly domain = (MedicalTimeOfMonthly) medicalTime.get(i);
			switch (i) {
			case DAY_SHIFT:
				this.dayWorkMedicalTime = domain.getWorkTime().v();
				this.dayDeductionTime = domain.getDeducationTime().v();
				this.dayTakeOverTime = domain.getTakeOverTime().v();
				break;
			case NIGHT_SHIFT:
				this.nightMedicalTime = domain.getWorkTime().v();
				this.nightDeductionTime = domain.getDeducationTime().v();
				this.nightTakeOverTime = domain.getTakeOverTime().v();
				break;
			default:
				break;
			}
		}
	}

	/* KRCDT_MON_OVER_TIME */
	private void toEntityOverTimeOfMonthly(OverTimeOfMonthly domain) {
		/** 残業合計時間 */
		TimeMonthWithCalculation totalOverTime = domain.getTotalOverTime();
		/** 振替残業合計時間 */
		TimeMonthWithCalculation totalTransferOverTime = domain.getTotalTransferOverTime();
		
		this.totalOverTime = totalOverTime.getTime().v();
		this.calcTotalOverTime = totalOverTime.getCalcTime().v();
		this.beforeOverTime = domain.getBeforeOverTime().v();
		this.totalTransferOverTime = totalTransferOverTime.getTime().v();
		this.calcTotalTransferOverTime = totalTransferOverTime.getCalcTime().v();
		
		/** 集計残業時間 */
		toEntityOverTime(domain.getAggregateOverTimeMap());
	}

	/* KRCDT_MON_REG_IRREG_TIME */
	private void toEntityRegAndIrreTimeOfMonth(RegularAndIrregularTimeOfMonthly domain) {

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
	private void toEntityVacationUseTimeOfMonth(VacationUseTimeOfMonthly domain) {
		this.annualLeaveUseTime = domain.getAnnualLeave().getUseTime().v();
		this.retentionYearlyUseTime = domain.getRetentionYearly().getUseTime().v();
		this.specialHolidayUseTime = domain.getSpecialHoliday().getUseTime().v();
		this.compensatoryLeaveUseTime = domain.getCompensatoryLeave().getUseTime().v();
	}
	
	/** KRCDT_MON_AGGR_SPEC_DAYS 10 **/
	private void toEntitySpecificDaysOfMonthly(Map<SpecificDateItemNo, AggregateSpecificDays> specificDaysList) {
		for (int i = 1; i <= 10; i++){
			SpecificDateItemNo itemNo = new SpecificDateItemNo(i);
			AggregateSpecificDays specificDay = new AggregateSpecificDays(itemNo);
			if (specificDaysList.containsKey(itemNo)){
				specificDay = specificDaysList.get(itemNo);
			}
			switch (i){
			case 1:
				this.toEntitySpecificDays1(specificDay); break;
			case 2:
				this.toEntitySpecificDays2(specificDay); break;
			case 3:
				this.toEntitySpecificDays3(specificDay); break;
			case 4:
				this.toEntitySpecificDays4(specificDay); break;
			case 5:
				this.toEntitySpecificDays5(specificDay); break;
			case 6:
				this.toEntitySpecificDays6(specificDay); break;
			case 7:
				this.toEntitySpecificDays7(specificDay); break;
			case 8:
				this.toEntitySpecificDays8(specificDay); break;
			case 9:
				this.toEntitySpecificDays9(specificDay); break;
			case 10:
				this.toEntitySpecificDays10(specificDay); break;
			}
		}
	}
	
	private void toEntitySpcVacationDaysList(Map<Integer, AggregateSpcVacationDays> spcVacationDaysList) {
		for (int i = 1; i <= 30; i++){
			AggregateSpcVacationDays specificDay = new AggregateSpcVacationDays(i);
			if (spcVacationDaysList.containsKey(i)){
				specificDay = spcVacationDaysList.get(i);
			}
			switch (i){
			case 1:
				toEntitySpcVacationDays1(specificDay); break;
			case 2:
				toEntitySpcVacationDays2(specificDay); break;
			case 3:
				toEntitySpcVacationDays3(specificDay); break;
			case 4:
				toEntitySpcVacationDays4(specificDay); break;
			case 5:
				toEntitySpcVacationDays5(specificDay); break;
			case 6:
				toEntitySpcVacationDays6(specificDay); break;
			case 7:
				toEntitySpcVacationDays7(specificDay); break;
			case 8:
				toEntitySpcVacationDays8(specificDay); break;
			case 9:
				toEntitySpcVacationDays9(specificDay); break;
			case 10:
				toEntitySpcVacationDays10(specificDay); break;
			case 11:
				toEntitySpcVacationDays11(specificDay); break;
			case 12:
				toEntitySpcVacationDays12(specificDay); break;
			case 13:
				toEntitySpcVacationDays13(specificDay); break;
			case 14:
				toEntitySpcVacationDays14(specificDay); break;
			case 15:
				toEntitySpcVacationDays15(specificDay); break;
			case 16:
				toEntitySpcVacationDays16(specificDay); break;
			case 17:
				toEntitySpcVacationDays17(specificDay); break;
			case 18:
				toEntitySpcVacationDays18(specificDay); break;
			case 19:
				toEntitySpcVacationDays19(specificDay); break;
			case 20:
				toEntitySpcVacationDays20(specificDay); break;
			case 21:
				toEntitySpcVacationDays21(specificDay); break;
			case 22:
				toEntitySpcVacationDays22(specificDay); break;
			case 23:
				toEntitySpcVacationDays23(specificDay); break;
			case 24:
				toEntitySpcVacationDays24(specificDay); break;
			case 25:
				toEntitySpcVacationDays25(specificDay); break;
			case 26:
				toEntitySpcVacationDays26(specificDay); break;
			case 27:
				toEntitySpcVacationDays27(specificDay); break;
			case 28:
				toEntitySpcVacationDays28(specificDay); break;
			case 29:
				toEntitySpcVacationDays29(specificDay); break;
			case 30:
				toEntitySpcVacationDays30(specificDay); break;				
			}
		}
	}
	
	/** 勤務日数 */
	private void toEntityWorkDays(WorkDaysOfMonthly vtWorkDays) {
		
		/** 出勤日数 */
		val attendanceDays =  vtWorkDays.getAttendanceDays();
		this.attendanceDays = attendanceDays.getDays().v();
		
		/** 欠勤日数 */
		val absenceDays =  vtWorkDays.getAbsenceDays();
		this.totalAbsenceDays = absenceDays.getTotalAbsenceDays().v();
		this.totalAbsenceTime = absenceDays.getTotalAbsenceTime().v();
		toEntityAbsenceDays(absenceDays.getAbsenceDaysList());
		
		/** 所定日数  */
		val predetermineDays = vtWorkDays.getPredetermineDays();
		this.predetermineDays = predetermineDays.getPredeterminedDays().v();
		
		/** 勤務日数  */
		val workDays = vtWorkDays.getWorkDays();
		this.workDays = workDays.getDays().v();
		
		/** 休日日数 */
		val holidayDays = vtWorkDays.getHolidayDays();
		this.holidayDays = holidayDays.getDays().v();
		
		/** 特定日数 SpecificDaysOfMonthly */
		val specificDays = vtWorkDays.getSpecificDays();
		this.toEntitySpecificDaysOfMonthly(specificDays.getSpecificDays());
		
		/** 休出日数  */
		val holidayWorkDays = vtWorkDays.getHolidayWorkDays();
		this.holidayWorkDays = holidayWorkDays.getDays().v();
		
		/** 給与用日数 */
		val payDays = vtWorkDays.getPayDays();
		this.payAttendanceDays = payDays.getPayAttendanceDays().v();
		this.payAbsenceDays = payDays.getPayAbsenceDays().v();
		
		/** 勤務回数  */
		val workTimes = vtWorkDays.getWorkTimes();
		this.workTimes = workTimes.getTimes().v();
		
		/** 二回勤務回数 */
		val twoTimesWorkTimes = vtWorkDays.getTwoTimesWorkTimes();
		this.twoTimesWorkTimes = twoTimesWorkTimes.getTimes().v();
		
		/** 臨時勤務回数 */
		val temporaryWorkTimes = vtWorkDays.getTemporaryWorkTimes();
		this.temporaryWorkTimes = temporaryWorkTimes.getTimes().v();
		
		/** 休業 */
		val leave = vtWorkDays.getLeave();
		this.toEntityLeaveOfMonthly(leave);
		
		/** 振出日数 */
		val recruitmentDays = vtWorkDays.getRecruitmentDays();
		this.recruitDays = recruitmentDays.getDays().v();
		
		/** 特別休暇日数 */
		val specialVacationDays = vtWorkDays.getSpecialVacationDays();
		this.totalSpcvactDays = specialVacationDays.getTotalSpcVacationDays().v();
		this.totalSpcvactTime = specialVacationDays.getTotalSpcVacationTime().v();
		this.toEntitySpcVacationDaysList(specialVacationDays.getSpcVacationDaysList());
	}
	
	/** 勤務時間 */
	private void toEntityWorkTime(WorkTimeOfMonthlyVT vtWorkTime) {
		/** 加給時間 */
		val bonusPayTime = vtWorkTime.getBonusPayTime();
		toEntityBonusPayTime(bonusPayTime.getBonusPayTime());
		
		/** 外出 */
		val goOuts = vtWorkTime.getGoOut();
		
		/** 外出 */
		val  goOut = goOuts.getGoOuts();
		toEntityGout(goOut);
		
		/** 育児外出 */
		val childCare = goOuts.getGoOutForChildCares();
		toEntityGoOutChildCare(childCare);
		
		/** 割増時間 */
		val premiumTimes = vtWorkTime.getPremiumTime();
		toEntityPremiumTimeOfMonthly(premiumTimes);
		
		/** 休憩時間 */
		toEntityBreakTime(vtWorkTime.getBreakTime());
		
		/** 休日時間 */
		toEntityHolidayTime(vtWorkTime.getHolidayTime());
		
		/** 深夜時間 */
		toEntityMidNightTime(vtWorkTime.getMidnightTime());
		
		/** 遅刻早退 */
		toEntityLateLeaveEarlyOfMonthly(vtWorkTime.getLateLeaveEarly());
		
		/** 入退門時間 */
		toEntityAttendanceLeaveGateTimeOfMonthly(vtWorkTime.getAttendanceLeaveGateTime());
		
		/** 予実差異時間 */
		this.budgetVarienceTime  = vtWorkTime.getBudgetTimeVarience().getTime().v();
		
		/** 乖離時間 */
		toEntityDivergenceTimeOfMonthly(vtWorkTime.getDivergenceTime());
		
		/** 医療時間 */
		Map<WorkTimeNightShift, MedicalTimeOfMonthly> medicalTime = vtWorkTime.getMedicalTime();
		toEntityMedicalTimeOfMonthly(medicalTime);
	}
	
	/** 乖離時間 */
	private void toEntityDivergenceTimeOfMonthly(DivergenceTimeOfMonthly divergenceTime) {
		/** 乖離時間 */
		Map<Integer, AggregateDivergenceTime> divergenceTimeList = divergenceTime == null ? new HashMap<>() : divergenceTime.getDivergenceTimeList();
		
		for (int i = 1; i <= 10; i++){
			AggregateDivergenceTime bonus = new AggregateDivergenceTime(i);
			if (divergenceTimeList.containsKey(i)){
				bonus = divergenceTimeList.get(i);
			}
			switch (i){
			case 1:
				this.toEntityDivergenceTime1(bonus); break;
			case 2:
				this.toEntityDivergenceTime2(bonus); break;
			case 3:
				this.toEntityDivergenceTime3(bonus); break;
			case 4:
				this.toEntityDivergenceTime4(bonus); break;
			case 5:
				this.toEntityDivergenceTime5(bonus); break;
			case 6:
				this.toEntityDivergenceTime6(bonus); break;
			case 7:
				this.toEntityDivergenceTime7(bonus); break;
			case 8:
				this.toEntityDivergenceTime8(bonus); break;
			case 9:
				this.toEntityDivergenceTime9(bonus); break;
			case 10:
				this.toEntityDivergenceTime10(bonus); break;
			}
		}
	}
	
	/** KRCDT_MON_AGGR_ABSN_DAYS 30 **/
	/** 欠勤日数 */
	private void toEntityAbsenceDays(Map<Integer, AggregateAbsenceDays> absenceDaysList) {
		for (int i = 1; i <= 30; i++){
			AggregateAbsenceDays absenceDays = new AggregateAbsenceDays(i);
			if (absenceDaysList.containsKey(i)){
				absenceDays = absenceDaysList.get(i);
			}
			switch (i){
			case 1:
				toEntityAbsenceDays1(absenceDays);
				break;
			case 2:
				toEntityAbsenceDays2(absenceDays);
				break;
			case 3:
				toEntityAbsenceDays3(absenceDays);
				break;
			case 4:
				toEntityAbsenceDays4(absenceDays);
				break;
			case 5:
				toEntityAbsenceDays5(absenceDays);
				break;
			case 6:
				toEntityAbsenceDays6(absenceDays);
				break;
			case 7:
				toEntityAbsenceDays7(absenceDays);
				break;
			case 8:
				toEntityAbsenceDays8(absenceDays);
				break;
			case 9:
				toEntityAbsenceDays9(absenceDays);
				break;
			case 10:
				toEntityAbsenceDays10(absenceDays);
				break;
			case 11:
				toEntityAbsenceDays11(absenceDays);
				break;
			case 12:
				toEntityAbsenceDays12(absenceDays);
				break;
			case 13:
				toEntityAbsenceDays13(absenceDays);
				break;
			case 14:
				toEntityAbsenceDays14(absenceDays);
				break;
			case 15:
				toEntityAbsenceDays15(absenceDays);
				break;
			case 16:
				toEntityAbsenceDays16(absenceDays);
				break;
			case 17:
				toEntityAbsenceDays17(absenceDays);
				break;
			case 18:
				toEntityAbsenceDays18(absenceDays);
				break;
			case 19:
				toEntityAbsenceDays19(absenceDays);
				break;
			case 20:
				toEntityAbsenceDays20(absenceDays);
				break;
			case 21:
				toEntityAbsenceDays21(absenceDays);
				break;
			case 22:
				toEntityAbsenceDays22(absenceDays);
				break;
			case 23:
				toEntityAbsenceDays23(absenceDays);
				break;
			case 24:
				toEntityAbsenceDays24(absenceDays);
				break;
			case 25:
				toEntityAbsenceDays25(absenceDays);
				break;
			case 26:
				toEntityAbsenceDays26(absenceDays);
				break;
			case 27:
				toEntityAbsenceDays27(absenceDays);
				break;
			case 28:
				toEntityAbsenceDays28(absenceDays);
				break;
			case 29:
				toEntityAbsenceDays29(absenceDays);
				break;
			case 30:
				toEntityAbsenceDays30(absenceDays);
				break;
			}
		}
	}
	
	/** 入退門時間 */
	private void toEntityAttendanceLeaveGateTimeOfMonthly(AttendanceLeaveGateTimeOfMonthly attendanceLeaveGateTime) {
		
		/** 出勤前時間 */
		this.attendanceLeaveGateBeforeAttendanceTime = attendanceLeaveGateTime.getTimeBeforeAttendance().v();
		/** 退勤後時間 */
		this.attendanceLeaveGateAfterLeaveWorkTime = attendanceLeaveGateTime.getTimeAfterLeaveWork().v();
		/** 滞在時間 */
		this.attendanceLeaveGateStayingTime = attendanceLeaveGateTime.getStayingTime().v();
		/** 不就労時間 */
		this.attendanceLeaveGateUnemployedTime = attendanceLeaveGateTime.getUnemployedTime().v();
	}
	
	/** 遅刻早退 */
	private void toEntityLateLeaveEarlyOfMonthly(LateLeaveEarlyOfMonthly lateLeaveEarly) {
		
		/** 遅刻 */
		Late late = lateLeaveEarly.getLate();
		this.lateTimes = late.getTimes().v();
		this.lateTime = late.getTime().getTime().v();
		this.calcLateTime = late.getTime().getCalcTime().v();
		
		/** 早退 */
		LeaveEarly leaveEarly = lateLeaveEarly.getLeaveEarly();
		this.leaveEarlyTimes = leaveEarly.getTimes().v();
		this.leaveEarlyTime = leaveEarly.getTime().getTime().v();
		this.calcLeaveEarlyTime = leaveEarly.getTime().getCalcTime().v();
	}
	
	/** 深夜時間 */
	private void toEntityMidNightTime(MidnightTimeOfMonthly midnightTime) {
		/** 残業深夜時間 */
		TimeMonthWithCalculation overWorkMidnightTime =  midnightTime.getOverWorkMidnightTime();
		this.overWorkMidnightTime = overWorkMidnightTime.getTime().v();
		this.calcOverWorkMidnightTime = overWorkMidnightTime.getCalcTime().v();
		
		/** 法定内深夜時間 */
		TimeMonthWithCalculation legalMidnightTime = midnightTime.getLegalMidnightTime();
		this.legalMidnightTime = legalMidnightTime.getTime().v();
		this.calcLegalMidnightTime = legalMidnightTime.getCalcTime().v();
		
		/** 法定外深夜時間 */
		IllegalMidnightTime illegalMidnightTime = midnightTime.getIllegalMidnightTime();
		this.illegalMidnightTime = illegalMidnightTime.getTime().getTime().v();
		this.calcIllegalMidnightTime = illegalMidnightTime.getTime().getCalcTime().v();
		this.illegalBeforeMidnightTime = illegalMidnightTime.getBeforeTime().v();
		
		/** 法定内休出深夜時間 */
		TimeMonthWithCalculation legalHolidayWorkMidnightTime = midnightTime.getLegalHolidayWorkMidnightTime();
		this.legalHolidayWorkMidnightTime = legalHolidayWorkMidnightTime.getTime().v();
		this.calcLegalHolidayWorkMidnightTime = legalHolidayWorkMidnightTime.getCalcTime().v();
		
		/** 法定外休出深夜時間 */
		TimeMonthWithCalculation illegalHolidayWorkMidnightTime = midnightTime.getIllegalHolidayWorkMidnightTime();
		this.illegalHolidayWorkMidnightTime = illegalHolidayWorkMidnightTime.getTime().v();
		this.calcIllegalHolidayWorkMidnightTime = illegalHolidayWorkMidnightTime.getCalcTime().v();
		
		/** 祝日休出深夜時間 */
		TimeMonthWithCalculation specialHolidayWorkMidnightTime = midnightTime.getSpecialHolidayWorkMidnightTime();
		this.specialHolidayWorkMidnightTime = specialHolidayWorkMidnightTime.getTime().v();
		this.calcSpecialHolidayWorkMidnightTime =specialHolidayWorkMidnightTime.getCalcTime().v();
	}
	
	/** 休日時間 */
	private void toEntityHolidayTime(HolidayTimeOfMonthly holidayTime) {
		
		this.legalHolidayTime = holidayTime == null ? 0 : holidayTime.getLegalHolidayTime().v();
		this.illegalHolidayTime = holidayTime == null ? 0 : holidayTime.getIllegalHolidayTime().v();
		this.illegalSpecialHolidayTime = holidayTime == null ? 0 : holidayTime.getIllegalSpecialHolidayTime().v();
	}
	
	/** 休憩時間 */
	private void toEntityBreakTime(BreakTimeOfMonthly breakTime) {
		
		this.breakTime = breakTime == null ? 0 : breakTime.getBreakTime().v();
	}
	
	/** 割増時間 */
	private void toEntityPremiumTimeOfMonthly(PremiumTimeOfMonthly premiumTimes) {
		/** 割増時間 */
		this.toEntityPremiumTime(premiumTimes == null ? new HashMap<>() : premiumTimes.getPremiumTime());
		/** 深夜時間 */
		this.premiumMidnightTime = premiumTimes == null ? 0 : premiumTimes.getMidnightTime().v();
		/** 法定内時間外時間 */
		this.premiumLegalOutsideWorkTime = premiumTimes == null ? 0 : premiumTimes.getLegalOutsideWorkTime().v();
		/** 法定内休出時間 */
		this.premiumIllegalOutsideWorkTime = premiumTimes == null ? 0 : premiumTimes.getIllegalOutsideWorkTime().v();
		/** 法定外時間外時間 */
		this.premiumLegalHolidayWorkTime = premiumTimes == null ? 0 : premiumTimes.getLegalHolidayWorkTime().v();
		/** 法定外休出時間 */
		this.premiumIllegalHolidayWorkTime = premiumTimes == null ? 0 : premiumTimes.getIllegalHolidayWorkTime().v();
	}
	
	/** KRCDT_MON_AGGR_BNSPY_TIME 10 **/
	private void toEntityBonusPayTime(Map<Integer, AggregateBonusPayTime> bonusPayTime) {
		for (int i = 1; i <= 10; i++){
			AggregateBonusPayTime bonus = new AggregateBonusPayTime(i);
			if (bonusPayTime.containsKey(i)){
				bonus = bonusPayTime.get(i);
			}
			switch (i){
			case 1:
				this.toEntityBonusPayTime1(bonus); break;
			case 2:
				this.toEntityBonusPayTime2(bonus); break;
			case 3:
				this.toEntityBonusPayTime3(bonus); break;
			case 4:
				this.toEntityBonusPayTime4(bonus); break;
			case 5:
				this.toEntityBonusPayTime5(bonus); break;
			case 6:
				this.toEntityBonusPayTime6(bonus); break;
			case 7:
				this.toEntityBonusPayTime7(bonus); break;
			case 8:
				this.toEntityBonusPayTime8(bonus); break;
			case 9:
				this.toEntityBonusPayTime9(bonus); break;
			case 10:
				this.toEntityBonusPayTime10(bonus); break;
			}
		}
	}
	
	/** 外出 */
	/** KRCDT_MON_AGGR_GOOUT 4 **/
	private void toEntityGout(Map<GoingOutReason, AggregateGoOut> goOuts) {
		
		GoingOutReason reason1 = GoingOutReason.PRIVATE;
		AggregateGoOut goOut1 = new AggregateGoOut(reason1);
		if (goOuts.containsKey(reason1)) goOut1 = goOuts.get(reason1);
		this.toEntityGoOut1(goOut1);
		
		GoingOutReason reason2 = GoingOutReason.PUBLIC;
		AggregateGoOut goOut2 = new AggregateGoOut(reason2);
		if (goOuts.containsKey(reason2)) goOut2 = goOuts.get(reason2);
		this.toEntityGoOut2(goOut2);
		
		GoingOutReason reason3 = GoingOutReason.COMPENSATION;
		AggregateGoOut goOut3 = new AggregateGoOut(reason3);
		if (goOuts.containsKey(reason3)) goOut3 = goOuts.get(reason3);
		this.toEntityGoOut3(goOut3);
		
		GoingOutReason reason4 = GoingOutReason.UNION;
		AggregateGoOut goOut4 = new AggregateGoOut(reason4);
		if (goOuts.containsKey(reason4)) goOut4 = goOuts.get(reason4);
		this.toEntityGoOut4(goOut4);
	}
	
	/** 育児外出 */
	private void toEntityGoOutChildCare(Map<ChildCareAtr, GoOutForChildCare> goOutForChildCares) {
		
		ChildCareAtr atr1 = ChildCareAtr.CHILD_CARE;
		GoOutForChildCare goOutForChildCare1 = new GoOutForChildCare(atr1);
		if (goOutForChildCares.containsKey(atr1)) goOutForChildCare1 = goOutForChildCares.get(atr1);
		this.childcareGoOutTimes = goOutForChildCare1.getTimes().v();
		this.childcareGoOutTime = goOutForChildCare1.getTime().v();
		
		ChildCareAtr atr2 = ChildCareAtr.CARE;
		GoOutForChildCare goOutForChildCare2 = new GoOutForChildCare(atr2);
		if (goOutForChildCares.containsKey(atr2)) goOutForChildCare2 = goOutForChildCares.get(atr2);
		this.careGoOutTimes = goOutForChildCare2.getTimes().v();
		this.careGoOutTime = goOutForChildCare2.getTime().v();
	}
	
	/** KRCDT_MON_AGGR_PREM_TIME 10 **/
	private void toEntityPremiumTime(Map<Integer, AggregatePremiumTime> premiumTimes) {
		for (int i = 1; i <= 10; i++){
			AggregatePremiumTime premiumTime = new AggregatePremiumTime(i);
			if (premiumTimes.containsKey(i)){
				premiumTime = premiumTimes.get(i);
			}
			switch (i){
			case 1:
				this.toEntityPremiumTime1(premiumTime);
				break;
			case 2:
				this.toEntityPremiumTime2(premiumTime);
				break;
			case 3:
				this.toEntityPremiumTime3(premiumTime);
				break;
			case 4:
				this.toEntityPremiumTime4(premiumTime);
				break;
			case 5:
				this.toEntityPremiumTime5(premiumTime);
				break;
			case 6:
				this.toEntityPremiumTime6(premiumTime);
				break;
			case 7:
				this.toEntityPremiumTime7(premiumTime);
				break;
			case 8:
				this.toEntityPremiumTime8(premiumTime);
				break;
			case 9:
				this.toEntityPremiumTime9(premiumTime);
				break;
			case 10:
				this.toEntityPremiumTime10(premiumTime);
				break;
			default:
				break;
			}
		}
	}
	
	/** 勤務時刻 */
	private void toEntityWorkClock(WorkClockOfMonthly workClock) {
		/** 終業時刻 */
		val endClock = workClock.getEndClock();
		this.endWorkTimes = endClock.getTimes().v();
		this.endWorkTotalClock = endClock.getTotalClock().v();
		this.endWorkAveClock = endClock.getAverageClock().v();
		
		/** PCログオン情報 */
		val logonInfo = workClock.getLogonInfo();
		
		/** PCログオン時刻 */
		val logonClock = logonInfo.getLogonClock();
		
		/** 合計日数 */
		this.logOnTotalDays = logonClock.getLogonClock().getTotalDays().v();
		/** 合計時刻 */
		this.logOnTotalClock = logonClock.getLogonClock().getTotalClock().v();
		/** 平均時刻 */
		this.logOnAveClock = logonClock.getLogonClock().getAverageClock().v();
		
		/** PCログオフ時刻 */
		val logoffClock = logonClock.getLogoffClock();
		this.logOffTotalDays = logoffClock.getTotalDays().v();
		this.logOffTotalClock = logoffClock.getTotalClock().v();
		this.logOffAveClock = logoffClock.getAverageClock().v();
		
		/** PCログオン乖離 */
		val logonDivergence = logonInfo.getLogonDivergence().getLogonDivergence();
		
		/** 日数 */
		this.logOnDivDays = logonDivergence.getDays().v();
		/** 合計時間 */
		this.logOnDivTotalTime = logonDivergence.getTotalTime().v();
		/** 平均時間 */
		this.logOnDivAveTime = logonDivergence.getAverageTime().v();
		
		/** PCログオフ乖離 */		
	    val logoffDivergence = logonInfo.getLogonDivergence().getLogoffDivergence();
	    /** 日数 */
	    this.logOffDivDays = logoffDivergence.getDays().v();
	    /** 合計時間 */
	    this.logOffDivTotalTime = logoffDivergence.getTotalTime().v();
	    /** 平均時間 */
	    this.logOffDivAveTime = logoffDivergence.getAverageTime().v();
	}

	/*  期間別の縦計 - KRCDT_MON_VERTICAL_TOTAL */
	private void toEntityVerticalTotal(VerticalTotalOfMonthly domain) {
		/** 勤務日数 */
		val vtWorkDays = domain.getWorkDays();
		/** 勤務時間 */
		val vtWorkTime = domain.getWorkTime();
		
		toEntityWorkDays(vtWorkDays);
		
		toEntityWorkTime(vtWorkTime);
		
		/** 勤務時刻 */
		toEntityWorkClock(domain.getWorkClock());
	}
	
	private void toEntityTotalCount(Map<Integer, TotalCount> totalCountList) {
		for (int i = 1; i <= 30; i++){
			TotalCount totalCount = new TotalCount(i);
			if (totalCountList.containsKey(i)){
				totalCount = totalCountList.get(i);
			}
			switch (i){
			case 1:
				this.toEntityTotalCount1(totalCount);
				break;
			case 2:
				this.toEntityTotalCount2(totalCount);
				break;
			case 3:
				this.toEntityTotalCount3(totalCount);
				break;
			case 4:
				this.toEntityTotalCount4(totalCount);
				break;
			case 5:
				this.toEntityTotalCount5(totalCount);
				break;
			case 6:
				this.toEntityTotalCount6(totalCount);
				break;
			case 7:
				this.toEntityTotalCount7(totalCount);
				break;
			case 8:
				this.toEntityTotalCount8(totalCount);
				break;
			case 9:
				this.toEntityTotalCount9(totalCount);
				break;
			case 10:
				this.toEntityTotalCount10(totalCount);
				break;
			case 11:
				this.toEntityTotalCount11(totalCount);
				break;
			case 12:
				this.toEntityTotalCount12(totalCount);
				break;
			case 13:
				this.toEntityTotalCount13(totalCount);
				break;
			case 14:
				this.toEntityTotalCount14(totalCount);
				break;
			case 15:
				this.toEntityTotalCount15(totalCount);
				break;
			case 16:
				this.toEntityTotalCount16(totalCount);
				break;
			case 17:
				this.toEntityTotalCount17(totalCount);
				break;
			case 18:
				this.toEntityTotalCount18(totalCount);
				break;
			case 19:
				this.toEntityTotalCount19(totalCount);
				break;
			case 20:
				this.toEntityTotalCount20(totalCount);
				break;
			case 21:
				this.toEntityTotalCount21(totalCount);
				break;
			case 22:
				this.toEntityTotalCount22(totalCount);
				break;
			case 23:
				this.toEntityTotalCount23(totalCount);
				break;
			case 24:
				this.toEntityTotalCount24(totalCount);
				break;
			case 25:
				this.toEntityTotalCount25(totalCount);
				break;
			case 26:
				this.toEntityTotalCount26(totalCount);
				break;
			case 27:
				this.toEntityTotalCount27(totalCount);
				break;
			case 28:
				this.toEntityTotalCount28(totalCount);
				break;
			case 29:
				this.toEntityTotalCount29(totalCount);
				break;
			case 30:
				this.toEntityTotalCount30(totalCount);
				break;
			}
		}
	}

	/* KRCDT_MON_EXCESS_OUTSIDE*/
	private void toEntityExcessOutsideWorkOfMonthly(ExcessOutsideWorkOfMonthly domain) {
		this.totalWeeklyPremiumTime1 = domain == null ? 0 : domain.getWeeklyTotalPremiumTime().v();
		this.totalMonthlyPremiumTime1 = domain == null ? 0 : domain.getMonthlyTotalPremiumTime().v();
		this.multiMonIrgmdlTime = domain == null ? 0 : domain.getDeformationCarryforwardTime().v();
	}	
	
	/* KRCDT_MON_EXCOUT_TIME 50 */
	private void toEntityExcessOutsideWork1(ExcessOutsideWork domain) {
		this.excessTime_1_1 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork2(ExcessOutsideWork domain) {
		this.excessTime_2_1 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork3(ExcessOutsideWork domain) {
		this.excessTime_3_1 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork4(ExcessOutsideWork domain) {
		this.excessTime_1_4 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork5(ExcessOutsideWork domain) {
		this.excessTime_1_5 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork6(ExcessOutsideWork domain) {
		this.excessTime_1_2 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork7(ExcessOutsideWork domain) {
		this.excessTime_2_2 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork8(ExcessOutsideWork domain) {
		this.excessTime_2_3 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork9(ExcessOutsideWork domain) {
		this.excessTime_2_4 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork10(ExcessOutsideWork domain) {
		this.excessTime_2_5 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork11(ExcessOutsideWork domain) {
		this.excessTime_1_3 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork12(ExcessOutsideWork domain) {
		this.excessTime_3_2 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork13(ExcessOutsideWork domain) {
		this.excessTime_3_3 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork14(ExcessOutsideWork domain) {
		this.excessTime_3_4 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork15(ExcessOutsideWork domain) {
		this.excessTime_3_5 = domain == null ? 0 : domain.getExcessTime().v();
	}
	private void toEntityExcessOutsideWork16(ExcessOutsideWork domain) {
		this.excessTime_4_1 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork17(ExcessOutsideWork domain) {
		this.excessTime_4_2 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork18(ExcessOutsideWork domain) {
		this.excessTime_4_3 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork19(ExcessOutsideWork domain) {
		this.excessTime_4_4 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork20(ExcessOutsideWork domain) {
		this.excessTime_4_5 = domain == null ? 0 : domain.getExcessTime().v();
	}	

	private void toEntityExcessOutsideWork21(ExcessOutsideWork domain) {
		this.excessTime_5_1 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork22(ExcessOutsideWork domain) {
		this.excessTime_5_2 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork23(ExcessOutsideWork domain) {
		this.excessTime_5_3 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork24(ExcessOutsideWork domain) {
		this.excessTime_5_4 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork25(ExcessOutsideWork domain) {
		this.excessTime_5_5 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork26(ExcessOutsideWork domain) {
		this.excessTime_1_6 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork27(ExcessOutsideWork domain) {
		this.excessTime_2_6 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork28(ExcessOutsideWork domain) {
		this.excessTime_3_6 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork29(ExcessOutsideWork domain) {
		this.excessTime_4_6 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork30(ExcessOutsideWork domain) {
		this.excessTime_5_6 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork31(ExcessOutsideWork domain) {
		this.excessTime_1_7 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork32(ExcessOutsideWork domain) {
		this.excessTime_2_7 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork33(ExcessOutsideWork domain) {
		this.excessTime_3_7 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork34(ExcessOutsideWork domain) {
		this.excessTime_4_7 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork35(ExcessOutsideWork domain) {
		this.excessTime_5_7 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork36(ExcessOutsideWork domain) {
		this.excessTime_1_8 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork37(ExcessOutsideWork domain) {
		this.excessTime_2_8 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork38(ExcessOutsideWork domain) {
		this.excessTime_3_8 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork39(ExcessOutsideWork domain) {
		this.excessTime_4_8 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork40(ExcessOutsideWork domain) {
		this.excessTime_5_8 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork41(ExcessOutsideWork domain) {
		this.excessTime_1_9 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork42(ExcessOutsideWork domain) {
		this.excessTime_2_9 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork43(ExcessOutsideWork domain) {
		this.excessTime_3_9 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork44(ExcessOutsideWork domain) {
		this.excessTime_4_9 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork45(ExcessOutsideWork domain) {
		this.excessTime_5_9 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork46(ExcessOutsideWork domain) {
		this.excessTime_1_10 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork47(ExcessOutsideWork domain) {
		this.excessTime_2_10 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork48(ExcessOutsideWork domain) {
		this.excessTime_3_10 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork49(ExcessOutsideWork domain) {
		this.excessTime_4_10 = domain == null ? 0 : domain.getExcessTime().v();
	}

	private void toEntityExcessOutsideWork50(ExcessOutsideWork domain) {
		this.excessTime_5_10 = domain == null ? 0 : domain.getExcessTime().v();
	}

	/* KRCDT_MON_AGREEMENT_TIME */
	private void toEntityAgreementTimeOfMonthly(AgreementTimeOfMonthly domain) {
		this.agreementTime = domain == null ? 0 : domain.getAgreementTime().v();
		this.limitErrorTime = domain == null ? 0 : domain.getLimitErrorTime().v();
		this.limitAlarmTime = domain == null ? 0 : domain.getLimitAlarmTime().v();
		this.exceptionLimitErrorTime = (domain != null && domain.getExceptionLimitErrorTime().isPresent()
				? domain.getExceptionLimitErrorTime().get().v()
				: null);
		this.exceptionLimitAlarmTime = (domain != null && domain.getExceptionLimitAlarmTime().isPresent()
				? domain.getExceptionLimitAlarmTime().get().v()
				: null);
		this.status = domain == null ? 0 : domain.getStatus().value;
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
	
	public void resetAffiliationInfo() {

		this.firstEmploymentCd = "";
		this.firstWorkplaceId = "";
		this.firstJobTitleId = "";
		this.firstClassCd = "";
		this.firstBusinessTypeCd = "";
		this.lastEmploymentCd = "";
		this.lastWorkplaceId = "";
		this.lastJobTitleId = "";
		this.lastClassCd = "";
		this.lastBusinessTypeCd = "";
	}

	/**
	 * ドメインに変換
	 * @return 集計加給時間
	 */
	private AggregateBonusPayTime toDomainBonusPayTime1() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime1),
				new AttendanceTimeMonth(this.specificBonusPayTime1),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime1),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime1));
	}

	private AggregateBonusPayTime toDomainBonusPayTime2() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime2),
				new AttendanceTimeMonth(this.specificBonusPayTime2),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime2),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime2));
	}

	private AggregateBonusPayTime toDomainBonusPayTime3() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime3),
				new AttendanceTimeMonth(this.specificBonusPayTime3),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime3),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime3));
	}

	private AggregateBonusPayTime toDomainBonusPayTime4() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime4),
				new AttendanceTimeMonth(this.specificBonusPayTime4),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime4),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime4));
	}

	private AggregateBonusPayTime toDomainBonusPayTime5() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime5),
				new AttendanceTimeMonth(this.specificBonusPayTime5),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime5),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime5));
	}

	private AggregateBonusPayTime toDomainBonusPayTime6() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime6),
				new AttendanceTimeMonth(this.specificBonusPayTime6),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime6),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime6));
	}

	private AggregateBonusPayTime toDomainBonusPayTime7() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime7),
				new AttendanceTimeMonth(this.specificBonusPayTime7),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime7),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime7));
	}

	private AggregateBonusPayTime toDomainBonusPayTime8() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime8),
				new AttendanceTimeMonth(this.specificBonusPayTime8),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime8),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime8));
	}

	private AggregateBonusPayTime toDomainBonusPayTime9() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime9),
				new AttendanceTimeMonth(this.specificBonusPayTime9),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime9),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime9));
	}

	private AggregateBonusPayTime toDomainBonusPayTime10() {
		return AggregateBonusPayTime.of(new AttendanceTimeMonth(this.bonusPayTime10),
				new AttendanceTimeMonth(this.specificBonusPayTime10),
				new AttendanceTimeMonth(this.holidayWorkBonusPayTime10),
				new AttendanceTimeMonth(this.holidayWorkSpecificBonusPayTime10));
	}

	/**
	 * ドメインに変換
	 * @return 集計乖離時間
	 */
	private AggregateDivergenceTime toDomainDivergenceTime1() {
		return AggregateDivergenceTime.of(1,
				new AttendanceTimeMonth(this.divergenceTime1),
				new AttendanceTimeMonth(this.deductionTime1),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction1),
				EnumAdaptor.valueOf(this.divergenceAtr1, DivergenceAtrOfMonthly.class));
	}

	private AggregateDivergenceTime toDomainDivergenceTime2() {
		return AggregateDivergenceTime.of(2,
				new AttendanceTimeMonth(this.divergenceTime2),
				new AttendanceTimeMonth(this.deductionTime2),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction2),
				EnumAdaptor.valueOf(this.divergenceAtr2, DivergenceAtrOfMonthly.class));
	}

	private AggregateDivergenceTime toDomainDivergenceTime3() {
		return AggregateDivergenceTime.of(3,
				new AttendanceTimeMonth(this.divergenceTime3),
				new AttendanceTimeMonth(this.deductionTime3),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction3),
				EnumAdaptor.valueOf(this.divergenceAtr3, DivergenceAtrOfMonthly.class));
	}

	private AggregateDivergenceTime toDomainDivergenceTime4() {
		return AggregateDivergenceTime.of(4,
				new AttendanceTimeMonth(this.divergenceTime4),
				new AttendanceTimeMonth(this.deductionTime4),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction4),
				EnumAdaptor.valueOf(this.divergenceAtr4, DivergenceAtrOfMonthly.class));
	}

	private AggregateDivergenceTime toDomainDivergenceTime5() {
		return AggregateDivergenceTime.of(5,
				new AttendanceTimeMonth(this.divergenceTime5),
				new AttendanceTimeMonth(this.deductionTime5),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction5),
				EnumAdaptor.valueOf(this.divergenceAtr5, DivergenceAtrOfMonthly.class));
	}

	private AggregateDivergenceTime toDomainDivergenceTime6() {
		return AggregateDivergenceTime.of(6,
				new AttendanceTimeMonth(this.divergenceTime6),
				new AttendanceTimeMonth(this.deductionTime6),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction6),
				EnumAdaptor.valueOf(this.divergenceAtr6, DivergenceAtrOfMonthly.class));
	}

	private AggregateDivergenceTime toDomainDivergenceTime7() {
		return AggregateDivergenceTime.of(7,
				new AttendanceTimeMonth(this.divergenceTime7),
				new AttendanceTimeMonth(this.deductionTime7),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction7),
				EnumAdaptor.valueOf(this.divergenceAtr7, DivergenceAtrOfMonthly.class));
	}

	private AggregateDivergenceTime toDomainDivergenceTime8() {
		return AggregateDivergenceTime.of(8,
				new AttendanceTimeMonth(this.divergenceTime8),
				new AttendanceTimeMonth(this.deductionTime8),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction8),
				EnumAdaptor.valueOf(this.divergenceAtr8, DivergenceAtrOfMonthly.class));
	}

	private AggregateDivergenceTime toDomainDivergenceTime9() {
		return AggregateDivergenceTime.of(9,
				new AttendanceTimeMonth(this.divergenceTime9),
				new AttendanceTimeMonth(this.deductionTime9),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction9),
				EnumAdaptor.valueOf(this.divergenceAtr9, DivergenceAtrOfMonthly.class));
	}

	private AggregateDivergenceTime toDomainDivergenceTime10() {
		return AggregateDivergenceTime.of(10,
				new AttendanceTimeMonth(this.divergenceTime10),
				new AttendanceTimeMonth(this.deductionTime10),
				new AttendanceTimeMonth(this.divergenceTimeAfterDeduction10),
				EnumAdaptor.valueOf(this.divergenceAtr10, DivergenceAtrOfMonthly.class));
	}

	private AggregateGoOut toDomainGoOut1() {
		return AggregateGoOut.of(
				new AttendanceTimesMonth(this.goOutTimesPrivate),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.legalTimePrivate),
						new AttendanceTimeMonth(this.calcLegalTimePrivate)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.illegalTimePrivate),
						new AttendanceTimeMonth(this.calcIllegalTimePrivate)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTimePrivate),
						new AttendanceTimeMonth(this.calcTotalTimePrivate)));
	}

	private AggregateGoOut toDomainGoOut2() {
		return AggregateGoOut.of(new AttendanceTimesMonth(this.goOutTimesPublic),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.legalTimePublic),
						new AttendanceTimeMonth(this.calcLegalTimePublic)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.illegalTimePublic),
						new AttendanceTimeMonth(this.calcIllegalTimePublic)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTimePublic),
						new AttendanceTimeMonth(this.calcTotalTimePublic)));
	}

	private AggregateGoOut toDomainGoOut3() {
		return AggregateGoOut.of(
				new AttendanceTimesMonth(this.goOutTimesCompensation),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.legalTimeCompensation),
						new AttendanceTimeMonth(this.calcLegalTimeCompensation)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.illegalTimeCompensation),
						new AttendanceTimeMonth(this.calcIllegalTimeCompensation)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTimeCompensation),
						new AttendanceTimeMonth(this.calcTotalTimeCompensation)));
	}

	private AggregateGoOut toDomainGoOut4() {
		return AggregateGoOut.of(
				new AttendanceTimesMonth(this.goOutTimesUnion),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.legalTimeUnion),
						new AttendanceTimeMonth(this.calcLegalTimeUnion)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.illegalTimeUnion),
						new AttendanceTimeMonth(this.calcIllegalTimeUnion)),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTimeUnion),
						new AttendanceTimeMonth(this.calcTotalTimeUnion)));
	}

	/**
	 * ドメインに変換
	 * @return 集計休出時間
	 */
	private AggregateHolidayWorkTime toDomainHolidayWorkTime1() {
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

	private AggregateHolidayWorkTime toDomainHolidayWorkTime2() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(2),
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

	private AggregateHolidayWorkTime toDomainHolidayWorkTime3() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(3),
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

	private AggregateHolidayWorkTime toDomainHolidayWorkTime4() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(4),
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

	private AggregateHolidayWorkTime toDomainHolidayWorkTime5() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(5),
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

	private AggregateHolidayWorkTime toDomainHolidayWorkTime6() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(6),
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

	private AggregateHolidayWorkTime toDomainHolidayWorkTime7() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(7),
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

	private AggregateHolidayWorkTime toDomainHolidayWorkTime8() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(8),
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

	private AggregateHolidayWorkTime toDomainHolidayWorkTime9() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(9),
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

	private AggregateHolidayWorkTime toDomainHolidayWorkTime10() {
		return AggregateHolidayWorkTime.of(
		new HolidayWorkFrameNo(10),
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
	private AggregateOverTime toDomainOverTime1() {
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

	private AggregateOverTime toDomainOverTime2() {
		return AggregateOverTime.of(new OverTimeFrameNo(2),
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

	private AggregateOverTime toDomainOverTime3() {
		return AggregateOverTime.of(new OverTimeFrameNo(3),
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

	private AggregateOverTime toDomainOverTime4() {
		return AggregateOverTime.of(new OverTimeFrameNo(4),
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

	private AggregateOverTime toDomainOverTime5() {
		return AggregateOverTime.of(new OverTimeFrameNo(5),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.overTime5), 
						new AttendanceTimeMonth(this.calcOverTime5)),
				new AttendanceTimeMonth(this.beforeOverTime5),
				new TimeMonthWithCalculation(new AttendanceTimeMonth(this.transferOverTime5),
						new AttendanceTimeMonth(this.calcTransferOverTime5)),
				new AttendanceTimeMonth(this.legalOverTime5),
				new AttendanceTimeMonth(this.legalTransferOverTime5));
	}

	private AggregateOverTime toDomainOverTime6() {
		return AggregateOverTime.of(new OverTimeFrameNo(6),
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

	private AggregateOverTime toDomainOverTime7() {
		return AggregateOverTime.of(new OverTimeFrameNo(7),
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

	private AggregateOverTime toDomainOverTime8() {
		return AggregateOverTime.of(new OverTimeFrameNo(8),
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

	private AggregateOverTime toDomainOverTime9() {
		return AggregateOverTime.of(new OverTimeFrameNo(9),
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

	private AggregateOverTime toDomainOverTime10() {
		return AggregateOverTime.of(new OverTimeFrameNo(10),
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
	private AggregatePremiumTime toDomainPremiumTime1() {
		return AggregatePremiumTime.of(1, new AttendanceTimeMonth(this.premiumTime1));
	}

	private AggregatePremiumTime toDomainPremiumTime2() {
		return AggregatePremiumTime.of(2, new AttendanceTimeMonth(this.premiumTime2));
	}

	private AggregatePremiumTime toDomainPremiumTime3() {
		return AggregatePremiumTime.of(3, new AttendanceTimeMonth(this.premiumTime3));
	}

	private AggregatePremiumTime toDomainPremiumTime4() {
		return AggregatePremiumTime.of(4, new AttendanceTimeMonth(this.premiumTime4));
	}

	private AggregatePremiumTime toDomainPremiumTime5() {
		return AggregatePremiumTime.of(5, new AttendanceTimeMonth(this.premiumTime5));
	}

	private AggregatePremiumTime toDomainPremiumTime6() {
		return AggregatePremiumTime.of(6, new AttendanceTimeMonth(this.premiumTime6));
	}

	private AggregatePremiumTime toDomainPremiumTime7() {
		return AggregatePremiumTime.of(7, new AttendanceTimeMonth(this.premiumTime7));
	}

	private AggregatePremiumTime toDomainPremiumTime8() {
		return AggregatePremiumTime.of(8, new AttendanceTimeMonth(this.premiumTime8));
	}

	private AggregatePremiumTime toDomainPremiumTime9() {
		return AggregatePremiumTime.of(9, new AttendanceTimeMonth(this.premiumTime9));
	}

	private AggregatePremiumTime toDomainPremiumTime10() {
		return AggregatePremiumTime.of(10, new AttendanceTimeMonth(this.premiumTime10));
	}

	/** KRCDT_MON_AGGR_SPEC_DAYS 10 **/
	private List<AggregateSpecificDays> getSpecificDaysLst(){
		List<AggregateSpecificDays> lst= new ArrayList<>();
		lst.add(this.toDomainSpecificDays1());
		lst.add(this.toDomainSpecificDays2());
		lst.add(this.toDomainSpecificDays3());
		lst.add(this.toDomainSpecificDays4());
		lst.add(this.toDomainSpecificDays5());
		lst.add(this.toDomainSpecificDays6());
		lst.add(this.toDomainSpecificDays7());
		lst.add(this.toDomainSpecificDays8());
		lst.add(this.toDomainSpecificDays9());
		lst.add(this.toDomainSpecificDays10());
		return lst;
	}
	
	/**
	 * ドメインに変換
	 * @return 集計特定日数
	 */
	private AggregateSpecificDays toDomainSpecificDays1(){
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(1),
				new AttendanceDaysMonth(this.specificDays1),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays1));
	}
	
	private AggregateSpecificDays toDomainSpecificDays2() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(2),
				new AttendanceDaysMonth(this.specificDays2),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays2));
	}

	private AggregateSpecificDays toDomainSpecificDays3() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(3), 
				new AttendanceDaysMonth(this.specificDays3),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays3));
	}

	private AggregateSpecificDays toDomainSpecificDays4() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(4), 
				new AttendanceDaysMonth(this.specificDays4),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays4));
	}

	private AggregateSpecificDays toDomainSpecificDays5() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(5), 
				new AttendanceDaysMonth(this.specificDays5),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays5));
	}

	private AggregateSpecificDays toDomainSpecificDays6() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(6), 
				new AttendanceDaysMonth(this.specificDays6),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays6));
	}

	private AggregateSpecificDays toDomainSpecificDays7() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(7), 
				new AttendanceDaysMonth(this.specificDays7),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays7));
	}

	private AggregateSpecificDays toDomainSpecificDays8() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(8), 
				new AttendanceDaysMonth(this.specificDays8),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays8));
	}

	private AggregateSpecificDays toDomainSpecificDays9() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(9), 
				new AttendanceDaysMonth(this.specificDays9),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays9));
	}

	private AggregateSpecificDays toDomainSpecificDays10() {
		return AggregateSpecificDays.of(
				new SpecificDateItemNo(10), 
				new AttendanceDaysMonth(this.specificDays10),
				new AttendanceDaysMonth(this.holidayWorkSpecificDays10));
	}
	
	/** KRCDT_MON_AGGR_TOTAL_SPT **/
	
	/**
	 * ドメインに変換
	 * @return 集計総拘束時間
	 */
	private AggregateTotalTimeSpentAtWork toDomainTotalTimeSpentAtWork(){
		
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
	private AggregateTotalWorkingTime toDomainTotalWorkingTime(){
		
		// 月別実績の就業時間
		val workTime = WorkTimeOfMonthly.of(
				new AttendanceTimeMonth(this.workTime),
				new AttendanceTimeMonth(this.withinPrescribedPremiumTime),
				new AttendanceTimeMonth(this.actualWorkTime));

		// 月別実績の残業時間
		OverTimeOfMonthly overTime = toDomainOverTimeOfMonthly();
		
		// 月別実績の休出時間
		HolidayWorkTimeOfMonthly holidayWorkTime = toDomainHolidayWorkTimeOfMonthly();
		
		// 月別実績の休暇使用時間
		VacationUseTimeOfMonthly vacationUseTime = toDomainVacationUseTimeOfMonthly();
		
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
	
	
	private MonthlyCalculation toDomainMonthlyCalculation() {
		// 月別実績の通常変形時間
		RegularAndIrregularTimeOfMonthly regAndIrgTime = toDomainRegularAndIrregularTimeOfMonthly();
		
		// 月別実績のフレックス時間
		FlexTimeOfMonthly flexTime = toDomainFlexTimeOfMonthly();
		
		// 集計総労働時間
		AggregateTotalWorkingTime aggregateTotalWorkingTime = toDomainTotalWorkingTime();
		
		// 集計総拘束時間
		AggregateTotalTimeSpentAtWork aggregateTotalTimeSpent = toDomainTotalTimeSpentAtWork();
		
		// 月別実績の36協定時間
		AgreementTimeOfMonthly agreementTime =  toDomainAgreementTimeOfMonthly();
		
		// 月別実績の月の計算
		return  MonthlyCalculation.of(
				regAndIrgTime, 
				flexTime, 
				new AttendanceTimeMonth(this.statutoryWorkingTime),
				aggregateTotalWorkingTime,
				new AttendanceTimeMonth(this.totalWorkingTime), 
				aggregateTotalTimeSpent, 
				agreementTime);
	}
	
	private List<TotalCount> getTotalCounts(){
		List<TotalCount> lst = new ArrayList<>();
		lst.add(toDomainTotalCount(1, this.totalCountDays1, this.totalCountTime1));
		lst.add(toDomainTotalCount(2, this.totalCountDays2, this.totalCountTime2));
		lst.add(toDomainTotalCount(3, this.totalCountDays3, this.totalCountTime3));
		lst.add(toDomainTotalCount(4, this.totalCountDays4, this.totalCountTime4));
		lst.add(toDomainTotalCount(5, this.totalCountDays5, this.totalCountTime5));
		lst.add(toDomainTotalCount(6, this.totalCountDays6, this.totalCountTime6));
		lst.add(toDomainTotalCount(7, this.totalCountDays7, this.totalCountTime7));
		lst.add(toDomainTotalCount(8, this.totalCountDays8, this.totalCountTime8));
		lst.add(toDomainTotalCount(9, this.totalCountDays9, this.totalCountTime9));
		lst.add(toDomainTotalCount(10, this.totalCountDays10, this.totalCountTime10	));
		lst.add(toDomainTotalCount(11, this.totalCountDays11, this.totalCountTime11	));
		lst.add(toDomainTotalCount(12, this.totalCountDays12, this.totalCountTime12	));
		lst.add(toDomainTotalCount(13, this.totalCountDays13, this.totalCountTime13	));
		lst.add(toDomainTotalCount(14, this.totalCountDays14, this.totalCountTime14	));
		lst.add(toDomainTotalCount(15, this.totalCountDays15, this.totalCountTime15	));
		lst.add(toDomainTotalCount(16, this.totalCountDays16, this.totalCountTime16	));
		lst.add(toDomainTotalCount(17, this.totalCountDays17, this.totalCountTime17	));
		lst.add(toDomainTotalCount(18, this.totalCountDays18, this.totalCountTime18	));
		lst.add(toDomainTotalCount(19, this.totalCountDays19, this.totalCountTime19	));
		lst.add(toDomainTotalCount(20, this.totalCountDays20, this.totalCountTime20	));
		lst.add(toDomainTotalCount(21, this.totalCountDays21, this.totalCountTime21	));
		lst.add(toDomainTotalCount(22, this.totalCountDays22, this.totalCountTime22	));
		lst.add(toDomainTotalCount(23, this.totalCountDays23, this.totalCountTime23	));
		lst.add(toDomainTotalCount(24, this.totalCountDays24, this.totalCountTime24	));
		lst.add(toDomainTotalCount(25, this.totalCountDays25, this.totalCountTime25	));
		lst.add(toDomainTotalCount(26, this.totalCountDays26, this.totalCountTime26	));
		lst.add(toDomainTotalCount(27, this.totalCountDays27, this.totalCountTime27	));
		lst.add(toDomainTotalCount(28, this.totalCountDays28, this.totalCountTime28	));
		lst.add(toDomainTotalCount(29, this.totalCountDays29, this.totalCountTime29	));
		lst.add(toDomainTotalCount(30, this.totalCountDays30, this.totalCountTime30	));
		return lst;
	}
	
	private List<AggregateSpcVacationDays> getSpcVacationDays(){
		List<AggregateSpcVacationDays> lst = new ArrayList<>();
		lst.add(toDomainSpcVacationDays(1, this.spcVactDays1, this.spcVactTime1));
		lst.add(toDomainSpcVacationDays(2, this.spcVactDays2, this.spcVactTime2));
		lst.add(toDomainSpcVacationDays(3, this.spcVactDays3, this.spcVactTime3));
		lst.add(toDomainSpcVacationDays(4, this.spcVactDays4, this.spcVactTime4));
		lst.add(toDomainSpcVacationDays(5, this.spcVactDays5, this.spcVactTime5));
		lst.add(toDomainSpcVacationDays(6, this.spcVactDays6, this.spcVactTime6));
		lst.add(toDomainSpcVacationDays(7, this.spcVactDays7, this.spcVactTime7));
		lst.add(toDomainSpcVacationDays(8, this.spcVactDays8, this.spcVactTime8));
		lst.add(toDomainSpcVacationDays(9, this.spcVactDays9, this.spcVactTime9));
		lst.add(toDomainSpcVacationDays(10, this.spcVactDays10, this.spcVactTime10));
		lst.add(toDomainSpcVacationDays(11, this.spcVactDays11, this.spcVactTime11));
		lst.add(toDomainSpcVacationDays(12, this.spcVactDays12, this.spcVactTime12));
		lst.add(toDomainSpcVacationDays(13, this.spcVactDays13, this.spcVactTime13));
		lst.add(toDomainSpcVacationDays(14, this.spcVactDays14, this.spcVactTime14));
		lst.add(toDomainSpcVacationDays(15, this.spcVactDays15, this.spcVactTime15));
		lst.add(toDomainSpcVacationDays(16, this.spcVactDays16, this.spcVactTime16));
		lst.add(toDomainSpcVacationDays(17, this.spcVactDays17, this.spcVactTime17));
		lst.add(toDomainSpcVacationDays(18, this.spcVactDays18, this.spcVactTime18));
		lst.add(toDomainSpcVacationDays(19, this.spcVactDays19, this.spcVactTime19));
		lst.add(toDomainSpcVacationDays(20, this.spcVactDays20, this.spcVactTime20));
		lst.add(toDomainSpcVacationDays(21, this.spcVactDays21, this.spcVactTime21));
		lst.add(toDomainSpcVacationDays(22, this.spcVactDays22, this.spcVactTime22));
		lst.add(toDomainSpcVacationDays(23, this.spcVactDays23, this.spcVactTime23));
		lst.add(toDomainSpcVacationDays(24, this.spcVactDays24, this.spcVactTime24));
		lst.add(toDomainSpcVacationDays(25, this.spcVactDays25, this.spcVactTime25));
		lst.add(toDomainSpcVacationDays(26, this.spcVactDays26, this.spcVactTime26));
		lst.add(toDomainSpcVacationDays(27, this.spcVactDays27, this.spcVactTime27));
		lst.add(toDomainSpcVacationDays(28, this.spcVactDays28, this.spcVactTime28));
		lst.add(toDomainSpcVacationDays(29, this.spcVactDays29, this.spcVactTime29));
		lst.add(toDomainSpcVacationDays(30, this.spcVactDays30, this.spcVactTime30));
		return lst;
	}
	
	/** KRCDT_MON_ATTENDANCE_TIME **/
	/**
	 * ドメインに変換
	 * @return 月別実績の勤怠時間
	 */
	public AttendanceTimeOfMonthly toDomainAttendanceTimeOfMonthly() {
		
		// 月別実績の月の計算
		MonthlyCalculation monthlyCalculation = toDomainMonthlyCalculation();
		
		// 月別実績の時間外超過
		ExcessOutsideWorkOfMonthly excessOutsideWork = toDomainExcessOutsideWorkOfMonthly();
		
		// TODO:LamVT-HERE ----------------------------()()()()()()()()()()-----------
		// 月別実績の縦計
		VerticalTotalOfMonthly verticalTotal = toDomainVerticalTotalOfMonthly();
		
		// 期間別の回数集計
		TotalCountByPeriod totalCount = toDomainTotalCountByPeriod(this.getTotalCounts());
		
		return AttendanceTimeOfMonthly.of(
				this.krcdtMonMergePk.getEmployeeId(),
				new YearMonth(this.krcdtMonMergePk.getYearMonth()),
				ClosureId.valueOf(this.krcdtMonMergePk.getClosureId()),
				new ClosureDate(this.krcdtMonMergePk.getClosureDay(), (this.krcdtMonMergePk.getIsLastDay() != 0)),
				new DatePeriod(this.startYmd, this.endYmd),
				monthlyCalculation,
				excessOutsideWork,
				verticalTotal,
				totalCount,
				new AttendanceDaysMonth(this.aggregateDays));
	}
	
	private TotalCountByPeriod toDomainTotalCountByPeriod(List<TotalCount> totalCounts) {
		TotalCountByPeriod totalCountPeriod = new TotalCountByPeriod();
		
		if (totalCounts != null){
			totalCountPeriod = TotalCountByPeriod.of(totalCounts);
		}
		
		return totalCountPeriod;
	}
	
	/** KRCDT_MON_FLEX_TIME **/
	/**
	 * ドメインに変換
	 * @return 月別実績のフレックス時間
	 */
	private FlexTimeOfMonthly toDomainFlexTimeOfMonthly(){
		
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
						new AttendanceTimeMonth(this.flexCarryforwardTime),
						new AttendanceTimeMonth(this.flexCarryforwardWorkTime),
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
	private HolidayWorkTimeOfMonthly toDomainHolidayWorkTimeOfMonthly(){
		List<AggregateHolidayWorkTime> aggrHolidayWorkTimeLst = this.getAggrHolidayWorkTimeLst();
		
		return HolidayWorkTimeOfMonthly.of(
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalHolidayWorkTime),
						new AttendanceTimeMonth(this.calcTotalHolidayWorkTime)),
				new AttendanceTimeMonth(this.beforeHolidayWorkTime),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.totalTransferTime),
						new AttendanceTimeMonth(this.calcTotalTransferTime)),
				aggrHolidayWorkTimeLst);
	}
	
	private List<AggregateHolidayWorkTime> getAggrHolidayWorkTimeLst(){
		List<AggregateHolidayWorkTime> aggrHolidayWorkTimeLst = new ArrayList<>();
		aggrHolidayWorkTimeLst.add(this.toDomainHolidayWorkTime1());
		aggrHolidayWorkTimeLst.add(this.toDomainHolidayWorkTime2());
		aggrHolidayWorkTimeLst.add(this.toDomainHolidayWorkTime3());
		aggrHolidayWorkTimeLst.add(this.toDomainHolidayWorkTime4());
		aggrHolidayWorkTimeLst.add(this.toDomainHolidayWorkTime5());
		aggrHolidayWorkTimeLst.add(this.toDomainHolidayWorkTime6());
		aggrHolidayWorkTimeLst.add(this.toDomainHolidayWorkTime7());
		aggrHolidayWorkTimeLst.add(this.toDomainHolidayWorkTime8());
		aggrHolidayWorkTimeLst.add(this.toDomainHolidayWorkTime9());
		aggrHolidayWorkTimeLst.add(this.toDomainHolidayWorkTime10());

		return aggrHolidayWorkTimeLst;
	}	
	/** KRCDT_MON_LEAVE - リポジトリ：月別実績の休業 only update **/
	/**
	 * ドメインに変換
	 * @return 月別実績の休業
	 */
	private LeaveOfMonthly toDomainLeaveOfMonthly(){
		
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
	private List<MedicalTimeOfMonthly> toDomainMedicalTimeOfMonthly(){
		List<MedicalTimeOfMonthly> medicalTimeLst = new ArrayList<>();
		medicalTimeLst.add(MedicalTimeOfMonthly.of(
				WorkTimeNightShift.DAY_SHIFT,
				new AttendanceTimeMonth(this.dayWorkMedicalTime),
				new AttendanceTimeMonth(this.dayDeductionTime),
				new AttendanceTimeMonth(this.dayTakeOverTime)));
		medicalTimeLst.add(MedicalTimeOfMonthly.of(
				WorkTimeNightShift.NIGHT_SHIFT,
				new AttendanceTimeMonth(this.nightMedicalTime),
				new AttendanceTimeMonth(this.nightDeductionTime),
				new AttendanceTimeMonth(this.nightTakeOverTime)));
		return medicalTimeLst;
	}
	
	/** KRCDT_MON_OVER_TIME **/
	/**
	 * ドメインに変換
	 * @param krcdtMonAggrOverTimes 集計残業時間
	 * @return 月別実績の残業時間
	 */
	private OverTimeOfMonthly toDomainOverTimeOfMonthly(){
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
	private RegularAndIrregularTimeOfMonthly toDomainRegularAndIrregularTimeOfMonthly(){
		
		// 月別実績の変形労働時間
		val irregularWorkingTime = IrregularWorkingTimeOfMonthly.of(
				new AttendanceTimeMonthWithMinus(this.multiMonthIrregularMiddleTime),
				new AttendanceTimeMonthWithMinus(this.irregularPeriodCarryforwardTime),
				new AttendanceTimeMonth(this.irregularWorkingShortageTime),
				new TimeMonthWithCalculation(
						new AttendanceTimeMonth(this.irregularLegalOverTime),
						new AttendanceTimeMonth(this.calcIrregularLegalOverTime)));
		
		return RegularAndIrregularTimeOfMonthly.of(
				new AttendanceTimeMonth(this.weeklyTotalPremiumTime),
				new AttendanceTimeMonth(this.monthlyTotalPremiumTime),
				irregularWorkingTime);
	}
	
	/** KRCDT_MON_VACT_USE_TIME **/
	/**
	 * ドメインに変換
	 * @return 月別実績の休暇使用時間
	 */
	private VacationUseTimeOfMonthly toDomainVacationUseTimeOfMonthly(){
		
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
	private VerticalTotalOfMonthly toDomainVerticalTotalOfMonthly(){

		LeaveOfMonthly  krcdtMonLeave = this.toDomainLeaveOfMonthly();
		List<AggregateAbsenceDays> krcdtMonAggrAbsnDays  = this.getAbsenceDaysLst();
		List<AggregateSpecificDays> krcdtMonAggrSpecDays  = this.getSpecificDaysLst();
		List<AggregateBonusPayTime> krcdtMonAggrBnspyTime = this.getBonusPayTimeLst();
		List<AggregateGoOut> krcdtMonAggrGoout = this.getGoOutLst();
		List<AggregatePremiumTime> krcdtMonAggrPremTime = this.getPremiumTimeLst();
		List<AggregateDivergenceTime> krcdtMonAggrDivgTime = this.getDivergenceTimeLst();
		List<MedicalTimeOfMonthly> krcdtMonMedicalTime = new ArrayList<>();
		krcdtMonMedicalTime.addAll(this.toDomainMedicalTimeOfMonthly());
		
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
		if (krcdtMonLeave != null) leave = krcdtMonLeave;
		
		// 月別実績の勤務日数
		val workDays = WorkDaysOfMonthly.of(
				AttendanceDaysOfMonthly.of(new AttendanceDaysMonth(this.attendanceDays)),
				AbsenceDaysOfMonthly.of(
						new AttendanceDaysMonth(this.totalAbsenceDays),
						new AttendanceTimeMonth(this.totalAbsenceTime),
						krcdtMonAggrAbsnDays),
				PredeterminedDaysOfMonthly.of(
						new AttendanceDaysMonth(this.predetermineDays)),
				WorkDaysDetailOfMonthly.of(new AttendanceDaysMonth(this.workDays)),
				HolidayDaysOfMonthly.of(new AttendanceDaysMonth(this.holidayDays)),
				SpecificDaysOfMonthly.of(
						krcdtMonAggrSpecDays),
				HolidayWorkDaysOfMonthly.of(new AttendanceDaysMonth(this.holidayWorkDays)),
				PayDaysOfMonthly.of(
						new AttendanceDaysMonth(this.payAttendanceDays),
						new AttendanceDaysMonth(this.payAbsenceDays)),
				WorkTimesOfMonthly.of(new AttendanceTimesMonth(this.workTimes)),
				TwoTimesWorkTimesOfMonthly.of(new AttendanceTimesMonth(this.twoTimesWorkTimes)),
				TemporaryWorkTimesOfMonthly.of(new AttendanceTimesMonth(this.temporaryWorkTimes)),
				leave,
				RecruitmentDaysOfMonthly.of(new AttendanceDaysMonth(new Double(this.recruitDays))),  // avoid compile error
				SpcVacationDaysOfMonthly.of(new AttendanceDaysMonth(new Double(this.totalSpcvactDays)), new AttendanceTimeMonth(this.totalSpcvactTime), this.getSpcVacationDays())); // avoid compile error
		
		// 月別実績の勤務時間
		val workTime = WorkTimeOfMonthlyVT.of(
				BonusPayTimeOfMonthly.of(
						krcdtMonAggrBnspyTime),
				GoOutOfMonthly.of(
						krcdtMonAggrGoout,
						goOutForChildCares),
				PremiumTimeOfMonthly.of(
						krcdtMonAggrPremTime,
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
				BudgetTimeVarienceOfMonthly.of(new AttendanceTimeMonthWithMinus(this.budgetVarienceTime)),
				DivergenceTimeOfMonthly.of(krcdtMonAggrDivgTime), krcdtMonMedicalTime);
		
		
		/** 終業時刻 */
		EndClockOfMonthly endClock = EndClockOfMonthly.of(new AttendanceTimesMonth(this.endWorkTimes), new AttendanceTimeMonth(this.endWorkTotalClock), new AttendanceTimeMonth(this.endWorkAveClock));
		/** PCログオン時刻 */
		AggrPCLogonClock logonClock = AggrPCLogonClock.of(new AttendanceDaysMonth(this.logOnTotalDays), new AttendanceTimeMonth(this.logOnTotalClock), new AttendanceTimeMonth(this.logOnAveClock));
		/** PCログオフ時刻 */
		AggrPCLogonClock logoffClock = AggrPCLogonClock.of(new AttendanceDaysMonth(this.logOffTotalDays), new AttendanceTimeMonth(this.logOffTotalClock), new AttendanceTimeMonth(this.logOffAveClock));
		/** PCログオン時刻 */
		PCLogonClockOfMonthly logOnClock = PCLogonClockOfMonthly.of(logonClock, logoffClock);
		/** PCログオフ乖離 */
		AggrPCLogonDivergence logonDivergence = AggrPCLogonDivergence.of(new AttendanceDaysMonth(this.logOnDivDays), new AttendanceTimeMonth(this.logOnDivTotalTime), new AttendanceTimeMonth(this.logOnDivAveTime));
		/** PCログオフ乖離 */
		AggrPCLogonDivergence logoffDivergence = AggrPCLogonDivergence.of(new AttendanceDaysMonth(this.logOffDivDays), new AttendanceTimeMonth(this.logOffDivTotalTime), new AttendanceTimeMonth(this.logOffDivAveTime));
		val workClock = WorkClockOfMonthly.of(endClock,
				PCLogonOfMonthly.of(logOnClock, PCLogonDivergenceOfMonthly.of(logonDivergence, logoffDivergence)));
		
		return VerticalTotalOfMonthly.of(workDays, workTime, workClock);
	}
	
	/** KRCDT_MON_EXCESS_OUTSIDE 50 **/
	/**
	 * ドメインに変換
	 * @param krcdtMonExcoutTime 時間外超過
	 * @return 月別実績の時間外超過
	 */
	
	private List<ExcessOutsideWork> getExcessOutsideWorkLst(){
		List<ExcessOutsideWork> excessOutsideWork = new ArrayList<>();
		
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(1, 1, this.excessTime_1_1));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(1, 2, this.excessTime_1_2));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(1, 3, this.excessTime_1_3));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(1, 4, this.excessTime_1_4));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(1, 5, this.excessTime_1_5));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(1, 6, this.excessTime_1_6));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(1, 7, this.excessTime_1_7));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(1, 8, this.excessTime_1_8));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(1, 9, this.excessTime_1_9));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(1, 10, this.excessTime_1_10));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(2, 1, this.excessTime_2_1));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(2, 2, this.excessTime_2_2));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(2, 3, this.excessTime_2_3));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(2, 4, this.excessTime_2_4));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(2, 5, this.excessTime_2_5));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(2, 6, this.excessTime_2_6));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(2, 7, this.excessTime_2_7));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(2, 8, this.excessTime_2_8));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(2, 9, this.excessTime_2_9));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(2, 10, this.excessTime_2_10));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(3, 1, this.excessTime_3_1));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(3, 2, this.excessTime_3_2));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(3, 3, this.excessTime_3_3));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(3, 4, this.excessTime_3_4));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(3, 5, this.excessTime_3_5));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(3, 6, this.excessTime_3_6));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(3, 7, this.excessTime_3_7));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(3, 8, this.excessTime_3_8));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(3, 9, this.excessTime_3_9));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(3, 10, this.excessTime_3_10));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(4, 1, this.excessTime_4_1));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(4, 2, this.excessTime_4_2));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(4, 3, this.excessTime_4_3));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(4, 4, this.excessTime_4_4));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(4, 5, this.excessTime_4_5));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(4, 6, this.excessTime_4_6));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(4, 7, this.excessTime_4_7));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(4, 8, this.excessTime_4_8));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(4, 9, this.excessTime_4_9));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(4, 10, this.excessTime_4_10));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(5, 1, this.excessTime_5_1));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(5, 2, this.excessTime_5_2));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(5, 3, this.excessTime_5_3));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(5, 4, this.excessTime_5_4));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(5, 5, this.excessTime_5_5));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(5, 6, this.excessTime_5_6));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(5, 7, this.excessTime_5_7));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(5, 8, this.excessTime_5_8));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(5, 9, this.excessTime_5_9));
		excessOutsideWork.add(this.toDomainExcessOutsideWorkXX(5, 10, this.excessTime_5_10));
		
		return excessOutsideWork;
	}
	
	private ExcessOutsideWorkOfMonthly toDomainExcessOutsideWorkOfMonthly(){
		List<ExcessOutsideWork> excessOutsideWork = this.getExcessOutsideWorkLst();		
		return ExcessOutsideWorkOfMonthly.of(
				new AttendanceTimeMonth(this.totalWeeklyPremiumTime1),
				new AttendanceTimeMonth(this.totalMonthlyPremiumTime1),
				new AttendanceTimeMonthWithMinus(this.multiMonIrgmdlTime),
				excessOutsideWork);
	}

	/** KRCDT_MON_EXCOUT_TIME **/
	/**
	 * ドメインに変換
	 * @return 時間外超過
	 */
	private ExcessOutsideWork toDomainExcessOutsideWorkXX(int excessNo, int breakdownNo, int excessTime) {
		return ExcessOutsideWork.of(breakdownNo, excessNo, new AttendanceTimeMonth(excessTime));
	}
	
	/** KRCDT_MON_AGREEMENT_TIME **/
	/**
	 * ドメインに変換
	 * @return 月別実績の36協定時間
	 */
	private AgreementTimeOfMonthly toDomainAgreementTimeOfMonthly(){
		
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
	
	/** KRCDT_MON_AGGR_ABSN_DAYS
	 * 
	 */
	public AggregateAbsenceDaysMerge toDomainAbsenceDays() {
		
		AggregateAbsenceDaysMerge merge = new AggregateAbsenceDaysMerge();
		merge.setAbsenceDays1(this.toDomainAbsenceDays1());
		merge.setAbsenceDays2(this.toDomainAbsenceDays2());
		merge.setAbsenceDays3(this.toDomainAbsenceDays3());
		merge.setAbsenceDays4(this.toDomainAbsenceDays4());
		merge.setAbsenceDays5(this.toDomainAbsenceDays5());
		merge.setAbsenceDays6(this.toDomainAbsenceDays6());
		merge.setAbsenceDays7(this.toDomainAbsenceDays7());
		merge.setAbsenceDays8(this.toDomainAbsenceDays8());
		merge.setAbsenceDays9(this.toDomainAbsenceDays9());
		merge.setAbsenceDays10(this.toDomainAbsenceDays10());
		merge.setAbsenceDays11(this.toDomainAbsenceDays11());
		merge.setAbsenceDays12(this.toDomainAbsenceDays12());
		merge.setAbsenceDays13(this.toDomainAbsenceDays13());
		merge.setAbsenceDays14(this.toDomainAbsenceDays14());
		merge.setAbsenceDays15(this.toDomainAbsenceDays15());
		merge.setAbsenceDays16(this.toDomainAbsenceDays16());
		merge.setAbsenceDays17(this.toDomainAbsenceDays17());
		merge.setAbsenceDays18(this.toDomainAbsenceDays18());
		merge.setAbsenceDays19(this.toDomainAbsenceDays19());
		merge.setAbsenceDays20(this.toDomainAbsenceDays20());
		merge.setAbsenceDays21(this.toDomainAbsenceDays21());
		merge.setAbsenceDays22(this.toDomainAbsenceDays22());
		merge.setAbsenceDays23(this.toDomainAbsenceDays23());
		merge.setAbsenceDays24(this.toDomainAbsenceDays24());
		merge.setAbsenceDays25(this.toDomainAbsenceDays25());
		merge.setAbsenceDays26(this.toDomainAbsenceDays26());
		merge.setAbsenceDays27(this.toDomainAbsenceDays27());
		merge.setAbsenceDays28(this.toDomainAbsenceDays28());
		merge.setAbsenceDays29(this.toDomainAbsenceDays29());
		merge.setAbsenceDays30(this.toDomainAbsenceDays30());
		return merge;
		
	}
	private  List<AggregateBonusPayTime> getBonusPayTimeLst() {
		List<AggregateBonusPayTime> merge = new ArrayList<>();
		merge.add(toDomainBonusPayTime1());
		merge.add(toDomainBonusPayTime2());
		merge.add(toDomainBonusPayTime3());
		merge.add(toDomainBonusPayTime4());
		merge.add(toDomainBonusPayTime5());
		merge.add(toDomainBonusPayTime6());
		merge.add(toDomainBonusPayTime7());
		merge.add(toDomainBonusPayTime8());
		merge.add(toDomainBonusPayTime9());
		merge.add(toDomainBonusPayTime10());
		return merge;
	}
	private List<AggregateAbsenceDays> getAbsenceDaysLst(){
		List<AggregateAbsenceDays> lst = new ArrayList<>();
		lst.add(this.toDomainAbsenceDays1());
		lst.add(this.toDomainAbsenceDays2());
		lst.add(this.toDomainAbsenceDays3());
		lst.add(this.toDomainAbsenceDays4());
		lst.add(this.toDomainAbsenceDays5());
		lst.add(this.toDomainAbsenceDays6());
		lst.add(this.toDomainAbsenceDays7());
		lst.add(this.toDomainAbsenceDays8());
		lst.add(this.toDomainAbsenceDays9());
		lst.add(this.toDomainAbsenceDays10());
		lst.add(this.toDomainAbsenceDays11());
		lst.add(this.toDomainAbsenceDays12());
		lst.add(this.toDomainAbsenceDays13());
		lst.add(this.toDomainAbsenceDays14());
		lst.add(this.toDomainAbsenceDays15());
		lst.add(this.toDomainAbsenceDays16());
		lst.add(this.toDomainAbsenceDays17());
		lst.add(this.toDomainAbsenceDays18());
		lst.add(this.toDomainAbsenceDays19());
		lst.add(this.toDomainAbsenceDays20());
		lst.add(this.toDomainAbsenceDays21());
		lst.add(this.toDomainAbsenceDays22());
		lst.add(this.toDomainAbsenceDays23());
		lst.add(this.toDomainAbsenceDays24());
		lst.add(this.toDomainAbsenceDays25());
		lst.add(this.toDomainAbsenceDays26());
		lst.add(this.toDomainAbsenceDays27());
		lst.add(this.toDomainAbsenceDays28());
		lst.add(this.toDomainAbsenceDays29());
		lst.add(this.toDomainAbsenceDays30());
		return lst;
	}
	
	private List<AggregateGoOut> getGoOutLst() {
		List<AggregateGoOut> merge = new ArrayList<>();
		merge.add(toDomainGoOut1());
		merge.add(toDomainGoOut2());
		merge.add(toDomainGoOut3());
		merge.add(toDomainGoOut4());
		return merge;
	}
	
	private List<AggregatePremiumTime> getPremiumTimeLst() {
		List<AggregatePremiumTime> merge = new ArrayList<>();
		merge.add(toDomainPremiumTime1());
		merge.add(toDomainPremiumTime2());
		merge.add(toDomainPremiumTime3());
		merge.add(toDomainPremiumTime4());
		merge.add(toDomainPremiumTime5());
		merge.add(toDomainPremiumTime6());
		merge.add(toDomainPremiumTime7());
		merge.add(toDomainPremiumTime8());
		merge.add(toDomainPremiumTime9());
		merge.add(toDomainPremiumTime10());
		return merge;
	}
	
	
	private List<AggregateDivergenceTime> getDivergenceTimeLst() {
		List<AggregateDivergenceTime> merge = new ArrayList<>();
		merge.add(toDomainDivergenceTime1());
		merge.add(toDomainDivergenceTime2());
		merge.add(toDomainDivergenceTime3());
		merge.add(toDomainDivergenceTime4());
		merge.add(toDomainDivergenceTime5());
		merge.add(toDomainDivergenceTime6());
		merge.add(toDomainDivergenceTime7());
		merge.add(toDomainDivergenceTime8());
		merge.add(toDomainDivergenceTime9());
		merge.add(toDomainDivergenceTime10());
		return merge;
	}
	
	private AggregateAbsenceDays toDomainAbsenceDays1() {
		return AggregateAbsenceDays.of(1, new AttendanceDaysMonth(new Double(this.absenceDay1)),
				new AttendanceTimeMonth((int) this.absenceTime1));
	}

	private AggregateAbsenceDays toDomainAbsenceDays2() {
		return AggregateAbsenceDays.of(2, new AttendanceDaysMonth(new Double(this.absenceDay2)),
				new AttendanceTimeMonth((int) this.absenceTime2));
	}

	private AggregateAbsenceDays toDomainAbsenceDays3() {
		return AggregateAbsenceDays.of(3, new AttendanceDaysMonth(new Double(this.absenceDay3)),
				new AttendanceTimeMonth((int) this.absenceTime3));
	}

	private AggregateAbsenceDays toDomainAbsenceDays4() {
		return AggregateAbsenceDays.of(4, new AttendanceDaysMonth(new Double(this.absenceDay4)),
				new AttendanceTimeMonth((int) this.absenceTime4));
	}

	private AggregateAbsenceDays toDomainAbsenceDays5() {
		return AggregateAbsenceDays.of(5, new AttendanceDaysMonth(new Double(this.absenceDay5)),
				new AttendanceTimeMonth((int) this.absenceTime5));
	}

	private AggregateAbsenceDays toDomainAbsenceDays6() {
		return AggregateAbsenceDays.of(6, new AttendanceDaysMonth(new Double(this.absenceDay6)),
				new AttendanceTimeMonth((int) this.absenceTime6));
	}

	private AggregateAbsenceDays toDomainAbsenceDays7() {
		return AggregateAbsenceDays.of(7, new AttendanceDaysMonth(new Double(this.absenceDay7)),
				new AttendanceTimeMonth((int) this.absenceTime7));
	}

	private AggregateAbsenceDays toDomainAbsenceDays8() {
		return AggregateAbsenceDays.of(8, new AttendanceDaysMonth(new Double(this.absenceDay8)),
				new AttendanceTimeMonth((int) this.absenceTime8));
	}

	private AggregateAbsenceDays toDomainAbsenceDays9() {
		return AggregateAbsenceDays.of(9, new AttendanceDaysMonth(new Double(this.absenceDay9)),
				new AttendanceTimeMonth((int) this.absenceTime9));
	}

	private AggregateAbsenceDays toDomainAbsenceDays10() {
		return AggregateAbsenceDays.of(10, new AttendanceDaysMonth(new Double(this.absenceDay10)),
				new AttendanceTimeMonth((int) this.absenceTime10));
	}

	private AggregateAbsenceDays toDomainAbsenceDays11() {
		return AggregateAbsenceDays.of(11, new AttendanceDaysMonth(new Double(this.absenceDay11)),
				new AttendanceTimeMonth((int) this.absenceTime11));
	}

	private AggregateAbsenceDays toDomainAbsenceDays12() {
		return AggregateAbsenceDays.of(12,new AttendanceDaysMonth(new Double(this.absenceDay12)),
				new AttendanceTimeMonth((int) this.absenceTime12));
	}

	private AggregateAbsenceDays toDomainAbsenceDays13() {
		return AggregateAbsenceDays.of(13, new AttendanceDaysMonth(new Double(this.absenceDay13)),
				new AttendanceTimeMonth((int) this.absenceTime13));
	}

	private AggregateAbsenceDays toDomainAbsenceDays14() {
		return AggregateAbsenceDays.of(14, new AttendanceDaysMonth(new Double(this.absenceDay14)),
				new AttendanceTimeMonth((int) this.absenceTime14));
	}

	private AggregateAbsenceDays toDomainAbsenceDays15() {
		return AggregateAbsenceDays.of(15, new AttendanceDaysMonth(new Double(this.absenceDay15)),
				new AttendanceTimeMonth((int) this.absenceTime15));
	}

	private AggregateAbsenceDays toDomainAbsenceDays16() {
		return AggregateAbsenceDays.of(16, new AttendanceDaysMonth(new Double(this.absenceDay16)),
				new AttendanceTimeMonth((int) this.absenceTime16));
	}

	private AggregateAbsenceDays toDomainAbsenceDays17() {
		return AggregateAbsenceDays.of(17, new AttendanceDaysMonth(new Double(this.absenceDay17)),
				new AttendanceTimeMonth((int) this.absenceTime17));
	}

	private AggregateAbsenceDays toDomainAbsenceDays18() {
		return AggregateAbsenceDays.of(18 ,new AttendanceDaysMonth(new Double(this.absenceDay18)),
				new AttendanceTimeMonth((int) this.absenceTime18));
	}

	private AggregateAbsenceDays toDomainAbsenceDays19() {
		return AggregateAbsenceDays.of(19, new AttendanceDaysMonth(new Double(this.absenceDay19)),
				new AttendanceTimeMonth((int) this.absenceTime19));
	}

	private AggregateAbsenceDays toDomainAbsenceDays20() {
		return AggregateAbsenceDays.of(20, new AttendanceDaysMonth(new Double(this.absenceDay20)),
				new AttendanceTimeMonth((int) this.absenceTime20));
	}

	private AggregateAbsenceDays toDomainAbsenceDays21() {
		return AggregateAbsenceDays.of(21, new AttendanceDaysMonth(new Double(this.absenceDay21)),
				new AttendanceTimeMonth((int) this.absenceTime21));
	}

	private AggregateAbsenceDays toDomainAbsenceDays22() {
		return AggregateAbsenceDays.of(22, new AttendanceDaysMonth(new Double(this.absenceDay22)),
				new AttendanceTimeMonth((int) this.absenceTime22));
	}

	private AggregateAbsenceDays toDomainAbsenceDays23() {
		return AggregateAbsenceDays.of(23, new AttendanceDaysMonth(new Double(this.absenceDay23)),
				new AttendanceTimeMonth((int) this.absenceTime23));
	}

	private AggregateAbsenceDays toDomainAbsenceDays24() {
		return AggregateAbsenceDays.of(24, new AttendanceDaysMonth(new Double(this.absenceDay24)),
				new AttendanceTimeMonth((int) this.absenceTime24));
	}

	private AggregateAbsenceDays toDomainAbsenceDays25() {
		return AggregateAbsenceDays.of(25,new AttendanceDaysMonth(new Double(this.absenceDay25)),
				new AttendanceTimeMonth((int) this.absenceTime25));
	}

	private AggregateAbsenceDays toDomainAbsenceDays26() {
		return AggregateAbsenceDays.of(26, new AttendanceDaysMonth(new Double(this.absenceDay26)),
				new AttendanceTimeMonth((int) this.absenceTime26));
	}

	private AggregateAbsenceDays toDomainAbsenceDays27() {
		return AggregateAbsenceDays.of(27, new AttendanceDaysMonth(new Double(this.absenceDay27)),
				new AttendanceTimeMonth((int) this.absenceTime27));
	}

	private AggregateAbsenceDays toDomainAbsenceDays28() {
		return AggregateAbsenceDays.of(28, new AttendanceDaysMonth(new Double(this.absenceDay28)),
				new AttendanceTimeMonth((int) this.absenceTime28));
	}

	private AggregateAbsenceDays toDomainAbsenceDays29() {
		return AggregateAbsenceDays.of(29, new AttendanceDaysMonth(new Double(this.absenceDay29)),
				new AttendanceTimeMonth((int) this.absenceTime29));
	}

	private AggregateAbsenceDays toDomainAbsenceDays30() {
		return AggregateAbsenceDays.of(30, new AttendanceDaysMonth(new Double(this.absenceDay30)),
				new AttendanceTimeMonth((int) this.absenceTime30));
	}
	
	private void toEntitySpcVacationDays1(AggregateSpcVacationDays domain) {
		this.spcVactDays1 = domain.getDays().v();
		this.spcVactTime1 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays2(AggregateSpcVacationDays domain) {
		this.spcVactDays2 = domain.getDays().v();
		this.spcVactTime2 = domain.getTime().v();
	}
	
		private void toEntitySpcVacationDays3(AggregateSpcVacationDays domain) {
		this.spcVactDays3 = domain.getDays().v();
		this.spcVactTime3 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays4(AggregateSpcVacationDays domain) {
		this.spcVactDays4 = domain.getDays().v();
		this.spcVactTime4 = domain.getTime().v();
	}
		
	private void toEntitySpcVacationDays5(AggregateSpcVacationDays domain) {
		this.spcVactDays5 = domain.getDays().v();
		this.spcVactTime5 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays6(AggregateSpcVacationDays domain) {
		this.spcVactDays6 = domain.getDays().v();
		this.spcVactTime6 = domain.getTime().v();
	}
		private void toEntitySpcVacationDays7(AggregateSpcVacationDays domain) {
		this.spcVactDays7 = domain.getDays().v();
		this.spcVactTime7 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays8(AggregateSpcVacationDays domain) {
		this.spcVactDays8 = domain.getDays().v();
		this.spcVactTime8 = domain.getTime().v();
	}	
	
	private void toEntitySpcVacationDays9(AggregateSpcVacationDays domain) {
		this.spcVactDays9 = domain.getDays().v();
		this.spcVactTime9 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays10(AggregateSpcVacationDays domain) {
		this.spcVactDays10 = domain.getDays().v();
		this.spcVactTime10 = domain.getTime().v();
	}	
	
	
	private void toEntitySpcVacationDays11(AggregateSpcVacationDays domain) {
		this.spcVactDays11 = domain.getDays().v();
		this.spcVactTime11 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays12(AggregateSpcVacationDays domain) {
		this.spcVactDays12 = domain.getDays().v();
		this.spcVactTime12 = domain.getTime().v();
	}
	
		private void toEntitySpcVacationDays13(AggregateSpcVacationDays domain) {
		this.spcVactDays13 = domain.getDays().v();
		this.spcVactTime13 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays14(AggregateSpcVacationDays domain) {
		this.spcVactDays14 = domain.getDays().v();
		this.spcVactTime14 = domain.getTime().v();
	}
		
	private void toEntitySpcVacationDays15(AggregateSpcVacationDays domain) {
		this.spcVactDays15 = domain.getDays().v();
		this.spcVactTime15 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays16(AggregateSpcVacationDays domain) {
		this.spcVactDays16 = domain.getDays().v();
		this.spcVactTime16 = domain.getTime().v();
	}
		private void toEntitySpcVacationDays17(AggregateSpcVacationDays domain) {
		this.spcVactDays17 = domain.getDays().v();
		this.spcVactTime17 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays18(AggregateSpcVacationDays domain) {
		this.spcVactDays18 = domain.getDays().v();
		this.spcVactTime18 = domain.getTime().v();
	}	
	
	private void toEntitySpcVacationDays19(AggregateSpcVacationDays domain) {
		this.spcVactDays19 = domain.getDays().v();
		this.spcVactTime19 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays20(AggregateSpcVacationDays domain) {
		this.spcVactDays20 = domain.getDays().v();
		this.spcVactTime20 = domain.getTime().v();
	}	
	
	private void toEntitySpcVacationDays21(AggregateSpcVacationDays domain) {
		this.spcVactDays21 = domain.getDays().v();
		this.spcVactTime21 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays22(AggregateSpcVacationDays domain) {
		this.spcVactDays22 = domain.getDays().v();
		this.spcVactTime22 = domain.getTime().v();
	}
	
		private void toEntitySpcVacationDays23(AggregateSpcVacationDays domain) {
		this.spcVactDays23 = domain.getDays().v();
		this.spcVactTime23 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays24(AggregateSpcVacationDays domain) {
		this.spcVactDays24 = domain.getDays().v();
		this.spcVactTime24 = domain.getTime().v();
	}
		
	private void toEntitySpcVacationDays25(AggregateSpcVacationDays domain) {
		this.spcVactDays25 = domain.getDays().v();
		this.spcVactTime25 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays26(AggregateSpcVacationDays domain) {
		this.spcVactDays26 = domain.getDays().v();
		this.spcVactTime26 = domain.getTime().v();
	}
		private void toEntitySpcVacationDays27(AggregateSpcVacationDays domain) {
		this.spcVactDays27 = domain.getDays().v();
		this.spcVactTime27 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays28(AggregateSpcVacationDays domain) {
		this.spcVactDays28 = domain.getDays().v();
		this.spcVactTime28 = domain.getTime().v();
	}	
	
	private void toEntitySpcVacationDays29(AggregateSpcVacationDays domain) {
		this.spcVactDays29 = domain.getDays().v();
		this.spcVactTime29 = domain.getTime().v();
	}
	
	private void toEntitySpcVacationDays30(AggregateSpcVacationDays domain) {
		this.spcVactDays30 = domain.getDays().v();
		this.spcVactTime30 = domain.getTime().v();
	}	

	private void toEntityTotalCount1(TotalCount domain) {
		this.totalCountDays1 = domain.getCount().v();
		this.totalCountTime1 = domain.getTime().v();
	}
	
	private void toEntityTotalCount2(TotalCount domain) {
		this.totalCountDays2 = domain.getCount().v();
		this.totalCountTime2 = domain.getTime().v();
	}
	
		private void toEntityTotalCount3(TotalCount domain) {
		this.totalCountDays3 = domain.getCount().v();
		this.totalCountTime3 = domain.getTime().v();
	}
	
	private void toEntityTotalCount4(TotalCount domain) {
		this.totalCountDays4 = domain.getCount().v();
		this.totalCountTime4 = domain.getTime().v();
	}
		
	private void toEntityTotalCount5(TotalCount domain) {
		this.totalCountDays5 = domain.getCount().v();
		this.totalCountTime5 = domain.getTime().v();
	}
	
	private void toEntityTotalCount6(TotalCount domain) {
		this.totalCountDays6 = domain.getCount().v();
		this.totalCountTime6 = domain.getTime().v();
	}
		private void toEntityTotalCount7(TotalCount domain) {
		this.totalCountDays7 = domain.getCount().v();
		this.totalCountTime7 = domain.getTime().v();
	}
	
	private void toEntityTotalCount8(TotalCount domain) {
		this.totalCountDays8 = domain.getCount().v();
		this.totalCountTime8 = domain.getTime().v();
	}	
	
	private void toEntityTotalCount9(TotalCount domain) {
		this.totalCountDays9 = domain.getCount().v();
		this.totalCountTime9 = domain.getTime().v();
	}
	
	private void toEntityTotalCount10(TotalCount domain) {
		this.totalCountDays10 = domain.getCount().v();
		this.totalCountTime10 = domain.getTime().v();
	}	
	
	
	private void toEntityTotalCount11(TotalCount domain) {
		this.totalCountDays11 = domain.getCount().v();
		this.totalCountTime11 = domain.getTime().v();
	}
	
	private void toEntityTotalCount12(TotalCount domain) {
		this.totalCountDays12 = domain.getCount().v();
		this.totalCountTime12 = domain.getTime().v();
	}
	
		private void toEntityTotalCount13(TotalCount domain) {
		this.totalCountDays13 = domain.getCount().v();
		this.totalCountTime13 = domain.getTime().v();
	}
	
	private void toEntityTotalCount14(TotalCount domain) {
		this.totalCountDays14 = domain.getCount().v();
		this.totalCountTime14 = domain.getTime().v();
	}
		
	private void toEntityTotalCount15(TotalCount domain) {
		this.totalCountDays15 = domain.getCount().v();
		this.totalCountTime15 = domain.getTime().v();
	}
	
	private void toEntityTotalCount16(TotalCount domain) {
		this.totalCountDays16 = domain.getCount().v();
		this.totalCountTime16 = domain.getTime().v();
	}
		private void toEntityTotalCount17(TotalCount domain) {
		this.totalCountDays17 = domain.getCount().v();
		this.totalCountTime17 = domain.getTime().v();
	}
	
	private void toEntityTotalCount18(TotalCount domain) {
		this.totalCountDays18 = domain.getCount().v();
		this.totalCountTime18 = domain.getTime().v();
	}	
	
	private void toEntityTotalCount19(TotalCount domain) {
		this.totalCountDays19 = domain.getCount().v();
		this.totalCountTime19 = domain.getTime().v();
	}
	
	private void toEntityTotalCount20(TotalCount domain) {
		this.totalCountDays20 = domain.getCount().v();
		this.totalCountTime20 = domain.getTime().v();
	}	
	
	private void toEntityTotalCount21(TotalCount domain) {
		this.totalCountDays21 = domain.getCount().v();
		this.totalCountTime21 = domain.getTime().v();
	}
	
	private void toEntityTotalCount22(TotalCount domain) {
		this.totalCountDays22 = domain.getCount().v();
		this.totalCountTime22 = domain.getTime().v();
	}
	
		private void toEntityTotalCount23(TotalCount domain) {
		this.totalCountDays23 = domain.getCount().v();
		this.totalCountTime23 = domain.getTime().v();
	}
	
	private void toEntityTotalCount24(TotalCount domain) {
		this.totalCountDays24 = domain.getCount().v();
		this.totalCountTime24 = domain.getTime().v();
	}
		
	private void toEntityTotalCount25(TotalCount domain) {
		this.totalCountDays25 = domain.getCount().v();
		this.totalCountTime25 = domain.getTime().v();
	}
	
	private void toEntityTotalCount26(TotalCount domain) {
		this.totalCountDays26 = domain.getCount().v();
		this.totalCountTime26 = domain.getTime().v();
	}
		private void toEntityTotalCount27(TotalCount domain) {
		this.totalCountDays27 = domain.getCount().v();
		this.totalCountTime27 = domain.getTime().v();
	}
	
	private void toEntityTotalCount28(TotalCount domain) {
		this.totalCountDays28 = domain.getCount().v();
		this.totalCountTime28 = domain.getTime().v();
	}	
	
	private void toEntityTotalCount29(TotalCount domain) {
		this.totalCountDays29 = domain.getCount().v();
		this.totalCountTime29 = domain.getTime().v();
	}
	
	private void toEntityTotalCount30(TotalCount domain) {
		this.totalCountDays30  = domain.getCount().v();
		this.totalCountTime30 = domain.getTime().v();
	}	
	
	private TotalCount toDomainTotalCount(int totalCountNo, double totalCountDays, int totalCountTime) {
	    return TotalCount.of(totalCountNo, new AttendanceDaysMonth(totalCountDays), new AttendanceTimeMonth(totalCountTime));
	}
	
	private AggregateSpcVacationDays toDomainSpcVacationDays(int spcVacationFrameNo, double days, double time) {
	    return AggregateSpcVacationDays.of(spcVacationFrameNo, new AttendanceDaysMonth(days), new AttendanceTimeMonth((int)time));
	}
}
