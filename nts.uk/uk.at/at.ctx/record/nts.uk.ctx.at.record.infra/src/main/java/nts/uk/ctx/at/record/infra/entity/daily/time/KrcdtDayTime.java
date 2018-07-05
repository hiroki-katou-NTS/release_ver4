package nts.uk.ctx.at.record.infra.entity.daily.time;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.val;
import nts.gul.reflection.FieldReflection;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.ConstraintTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.TotalWorkingTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.temporarytime.TemporaryTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workingtime.StayingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workschedule.WorkScheduleTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workschedule.WorkScheduleTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.ExcessOverTimeWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculationMinusExist;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeGoOutTimes;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayMidnightWork;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AbsenceOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AnnualOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.HolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.OverSalaryOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SpecialHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SubstituteHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.TimeDigestOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.YearlyReservedOfDaily;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTimeSheet;
import nts.uk.ctx.at.record.dom.divergencetime.DiverdenceReasonCode;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReasonContent;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTimeOfDaily;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaiseSalaryTimeOfDailyPerfor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.shorttimework.enums.ChildCareAttribute;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.infra.entity.daily.actualworktime.KrcdtDayAttendanceTime;
import nts.uk.ctx.at.record.infra.entity.daily.actualworktime.KrcdtDayAttendanceTimePK;
import nts.uk.ctx.at.record.infra.entity.daily.latetime.KrcdtDayLateTime;
import nts.uk.ctx.at.record.infra.entity.daily.leaveearlytime.KrcdtDayLeaveEarlyTime;
import nts.uk.ctx.at.record.infra.entity.daily.premiumtime.KrcdtDayPremiumTime;
import nts.uk.ctx.at.record.infra.entity.daily.shortwork.KrcdtDaiShortWorkTime;
import nts.uk.ctx.at.record.infra.entity.daily.shortwork.KrcdtDayShorttime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCDT_DAY_TIME")
public class KrcdtDayTime extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/* 主キー */
	@EmbeddedId
	public KrcdtDayTimePK krcdtDayTimePK;
	
	/*----------------------日別実績の勤怠時間------------------------------*/
	/* 総労働時間 */
	@Column(name = "TOTAL_ATT_TIME")
	public int totalAttTime;
	/* 総計算時間 */
	@Column(name = "TOTAL_CALC_TIME")
	public int totalCalcTime;
	/* 実働時間 */
	@Column(name = "ACTWORK_TIME")
	public int actWorkTime;
	/* 勤務回数 */
	@Column(name = "WORK_TIMES")
	public int workTimes;
	/* 総拘束時間 */
	@Column(name = "TOTAL_BIND_TIME")
	public int totalBindTime;
	/* 深夜拘束時間 */
	@Column(name = "MIDN_BIND_TIME")
	public int midnBindTime;
	/* 拘束差異時間 */
	@Column(name = "BIND_DIFF_TIME")
	public int bindDiffTime;
	/* 時差勤務時間 */
	@Column(name = "DIFF_TIME_WORK_TIME")
	public int diffTimeWorkTime;
	/* 所定外深夜時間 */
	@Column(name = "OUT_PRS_MIDN_TIME")
	public int outPrsMidnTime;
	/* 計算所定外深夜時間 */
	@Column(name = "CALC_OUT_PRS_MIDN_TIME")
	public int calcOutPrsMidnTime;
	/* 事前所定外深夜時間 */
	@Column(name = "PRE_OUT_PRS_MIDN_TIME")
	public int preOutPrsMidnTime;
	/* 予実差異時間 */
	@Column(name = "BUDGET_TIME_VARIANCE")
	public int budgetTimeVariance;
	/* 不就労時間 */
	@Column(name = "UNEMPLOYED_TIME")
	public int unemployedTime;
	/* 滞在時間 */
	@Column(name = "STAYING_TIME")
	public int stayingTime;
	/* 出勤前時間 */
	@Column(name = "BFR_WORK_TIME")
	public int bfrWorkTime;
	/* 退勤後時間 */
	@Column(name = "AFT_LEAVE_TIME")
	public int aftLeaveTime;
	/* PCログオン前時間 */
	@Column(name = "BFR_PC_LOGON_TIME")
	public int bfrPcLogonTime;
	/* PCログオフ後時間 */
	@Column(name = "AFT_PC_LOGOFF_TIME")
	public int aftPcLogoffTime;
	/*所定外深夜乖離時間*/
	@Column(name = "DIV_OUT_PRS_MIDN_TIME")
	public int divOutPrsMidnTime;
	/*----------------------日別実績の勤怠時間------------------------------*/
	
	/*----------------------日別実績の休憩時間帯------------------------------*/
	@Column(name = "TO_RECORD_TOTAL_TIME")
	public Integer toRecordTotalTime;
	@Column(name ="TO_RECORD_IN_TIME")
	public Integer toRecordInTime;
	@Column(name ="TO_RECORD_OUT_TIME")
	public Integer toRecordOutTime;
	
	@Column(name ="DEDUCTION_TOTAL_TIME")
	public Integer deductionTotalTime;
	@Column(name ="DEDUCTION_IN_TIME")
	public Integer deductionInTime;
	@Column(name ="DEDUCTION_OUT_TIME")
	public Integer deductionOutTime;
	
	@Column(name ="CAL_TO_RECORD_TOTAL_TIME")
	public Integer calToRecordTotalTime;
	@Column(name ="CAL_TO_RECORD_IN_TIME")
	public Integer calToRecordInTime;
	@Column(name ="CAL_TO_RECORD_OUT_TIME")
	public Integer calToRecordOutTime;
	
	@Column(name ="CAL_DEDUCTION_TOTAL_TIME")
	public Integer calDeductionTotalTime;
	@Column(name ="CAL_DEDUCTION_IN_TIME")
	public Integer calDeductionInTime;
	@Column(name ="CAL_DEDUCTION_OUT_TIME")
	public Integer calDeductionOutTime;
	
	@Column(name ="DURINGWORK_TIME")
	public int duringworkTime;
	
	@Column(name ="COUNT")
	public Integer count;
	/*----------------------日別実績の休憩時間帯------------------------------*/
	
	/*----------------------日別実績の休出枠時間------------------------------*/
	/*休日出勤時間1*/
	@Column(name = "HOLI_WORK_TIME_1")
	public int holiWorkTime1;
	/*休日出勤時間2*/
	@Column(name = "HOLI_WORK_TIME_2")
	public int holiWorkTime2;
	/*休日出勤時間3*/
	@Column(name = "HOLI_WORK_TIME_3")
	public int holiWorkTime3;
	/*休日出勤時間4*/
	@Column(name = "HOLI_WORK_TIME_4")
	public int holiWorkTime4;
	/*休日出勤時間5*/
	@Column(name = "HOLI_WORK_TIME_5")
	public int holiWorkTime5;
	/*休日出勤時間6*/
	@Column(name = "HOLI_WORK_TIME_6")
	public int holiWorkTime6;
	/*休日出勤時間7*/
	@Column(name = "HOLI_WORK_TIME_7")
	public int holiWorkTime7;
	/*休日出勤時間8*/
	@Column(name = "HOLI_WORK_TIME_8")
	public int holiWorkTime8;
	/*休日出勤時間9*/
	@Column(name = "HOLI_WORK_TIME_9")
	public int holiWorkTime9;
	/*休日出勤時間10*/
	@Column(name = "HOLI_WORK_TIME_10")
	public int holiWorkTime10;
	/*振替時間1*/
	@Column(name = "TRANS_TIME_1")
	public int transTime1;
	/*振替時間2*/
	@Column(name = "TRANS_TIME_2")
	public int transTime2;
	/*振替時間3*/
	@Column(name = "TRANS_TIME_3")
	public int transTime3;
	/*振替時間4*/
	@Column(name = "TRANS_TIME_4")
	public int transTime4;
	/*振替時間5*/
	@Column(name = "TRANS_TIME_5")
	public int transTime5;
	/*振替時間6*/
	@Column(name = "TRANS_TIME_6")
	public int transTime6;
	/*振替時間7*/
	@Column(name = "TRANS_TIME_7")
	public int transTime7;
	/*振替時間8*/
	@Column(name = "TRANS_TIME_8")
	public int transTime8;
	/*振替時間9*/
	@Column(name = "TRANS_TIME_9")
	public int transTime9;
	/*振替時間10*/
	@Column(name = "TRANS_TIME_10")
	public int transTime10;
	/*計算休日出勤時間1*/
	@Column(name = "CALC_HOLI_WORK_TIME_1")
	public int calcHoliWorkTime1;
	/*計算休日出勤時間2*/
	@Column(name = "CALC_HOLI_WORK_TIME_2")
	public int calcHoliWorkTime2;
	/*計算休日出勤時間3*/
	@Column(name = "CALC_HOLI_WORK_TIME_3")
	public int calcHoliWorkTime3;
	/*計算休日出勤時間4*/
	@Column(name = "CALC_HOLI_WORK_TIME_4")
	public int calcHoliWorkTime4;
	/*計算休日出勤時間5*/
	@Column(name = "CALC_HOLI_WORK_TIME_5")
	public int calcHoliWorkTime5;
	/*計算休日出勤時間6*/
	@Column(name = "CALC_HOLI_WORK_TIME_6")
	public int calcHoliWorkTime6;
	/*計算休日出勤時間7*/
	@Column(name = "CALC_HOLI_WORK_TIME_7")
	public int calcHoliWorkTime7;
	/*計算休日出勤時間8*/
	@Column(name = "CALC_HOLI_WORK_TIME_8")
	public int calcHoliWorkTime8;
	/*計算休日出勤時間9*/
	@Column(name = "CALC_HOLI_WORK_TIME_9")
	public int calcHoliWorkTime9;
	/*計算休日出勤時間10*/
	@Column(name = "CALC_HOLI_WORK_TIME_10")
	public int calcHoliWorkTime10;
	/*計算振替時間1*/
	@Column(name = "CALC_TRANS_TIME_1")
	public int calcTransTime1;
	/*計算振替時間2*/
	@Column(name = "CALC_TRANS_TIME_2")
	public int calcTransTime2;
	/*計算振替時間3*/
	@Column(name = "CALC_TRANS_TIME_3")
	public int calcTransTime3;
	/*計算振替時間4*/
	@Column(name = "CALC_TRANS_TIME_4")
	public int calcTransTime4;
	/*計算振替時間5*/
	@Column(name = "CALC_TRANS_TIME_5")
	public int calcTransTime5;
	/*計算振替時間6*/
	@Column(name = "CALC_TRANS_TIME_6")
	public int calcTransTime6;
	/*計算振替時間7*/
	@Column(name = "CALC_TRANS_TIME_7")
	public int calcTransTime7;
	/*計算振替時間8*/
	@Column(name = "CALC_TRANS_TIME_8")
	public int calcTransTime8;
	/*計算振替時間9*/
	@Column(name = "CALC_TRANS_TIME_9")
	public int calcTransTime9;
	/*計算振替時間10*/
	@Column(name = "CALC_TRANS_TIME_10")
	public int calcTransTime10;
	/*事前申請時間1*/
	@Column(name = "PRE_APP_TIME_1")
	public int preAppTime1;
	/*事前申請時間2*/
	@Column(name = "PRE_APP_TIME_2")
	public int preAppTime2;
	/*事前申請時間3*/
	@Column(name = "PRE_APP_TIME_3")
	public int preAppTime3;
	/*事前申請時間4*/
	@Column(name = "PRE_APP_TIME_4")
	public int preAppTime4;
	/*事前申請時間5*/
	@Column(name = "PRE_APP_TIME_5")
	public int preAppTime5;
	/*事前申請時間6*/
	@Column(name = "PRE_APP_TIME_6")
	public int preAppTime6;
	/*事前申請時間7*/
	@Column(name = "PRE_APP_TIME_7")
	public int preAppTime7;
	/*事前申請時間8*/
	@Column(name = "PRE_APP_TIME_8")
	public int preAppTime8;
	/*事前申請時間9*/
	@Column(name = "PRE_APP_TIME_9")
	public int preAppTime9;
	/*事前申請時間10*/
	@Column(name = "PRE_APP_TIME_10")
	public int preAppTime10;
	/*法定内休日出勤深夜*/
	@Column(name = "LEG_HOLI_WORK_MIDN")
	public int legHoliWorkMidn;
	/*法定外休日出勤深夜*/
	@Column(name = "ILLEG_HOLI_WORK_MIDN")
	public int illegHoliWorkMidn;
	/*祝日日出勤深夜*/
	@Column(name = "PB_HOLI_WORK_MIDN")
	public int pbHoliWorkMidn;
	/*計算法定内休日出勤深夜*/
	@Column(name = "CALC_LEG_HOLI_WORK_MIDN")
	public int calcLegHoliWorkMidn;
	/*計算法定外休日出勤深夜*/
	@Column(name = "CALC_ILLEG_HOLI_WORK_MIDN")
	public int calcIllegHoliWorkMidn;
	/*計算祝日日出勤深夜*/
	@Column(name = "CALC_PB_HOLI_WORK_MIDN")
	public int calcPbHoliWorkMidn;
	/*休日出勤拘束時間*/
	@Column(name = "HOLI_WORK_BIND_TIME")
	public int holiWorkBindTime;
	/*休日出勤乖離時間１*/
	@Column(name = "DIV_HOLI_TIME_1")
	public int divHoliTime1;
	/*休日出勤乖離時間2*/
	@Column(name = "DIV_HOLI_TIME_2")
	public int divHoliTime2;
	/*休日出勤乖離時間3*/
	@Column(name = "DIV_HOLI_TIME_3")
	public int divHoliTime3;
	/*休日出勤乖離時間4*/
	@Column(name = "DIV_HOLI_TIME_4")
	public int divHoliTime4;
	/*休日出勤乖離時間5*/
	@Column(name = "DIV_HOLI_TIME_5")
	public int divHoliTime5;
	/*休日出勤乖離時間6*/
	@Column(name = "DIV_HOLI_TIME_6")
	public int divHoliTime6;
	/*休日出勤乖離時間7*/
	@Column(name = "DIV_HOLI_TIME_7")
	public int divHoliTime7;
	/*休日出勤乖離時間8*/
	@Column(name = "DIV_HOLI_TIME_8")
	public int divHoliTime8;
	/*休日出勤乖離時間9*/
	@Column(name = "DIV_HOLI_TIME_9")
	public int divHoliTime9;
	/*休日出勤乖離時間１0*/
	@Column(name = "DIV_HOLI_TIME_10")
	public int divHoliTime10;
	/*振替乖離時間１*/
	@Column(name = "DIV_HOLI_TRANS_TIME_1")
	public int divHoliTransTime1;
	/*振替乖離時間2*/
	@Column(name = "DIV_HOLI_TRANS_TIME_2")
	public int divHoliTransTime2;
	/*振替乖離時間3*/
	@Column(name = "DIV_HOLI_TRANS_TIME_3")
	public int divHoliTransTime3;
	/*振替乖離時間4*/
	@Column(name = "DIV_HOLI_TRANS_TIME_4")
	public int divHoliTransTime4;
	/*振替乖離時間5*/
	@Column(name = "DIV_HOLI_TRANS_TIME_5")
	public int divHoliTransTime5;
	/*振替乖離時間6*/
	@Column(name = "DIV_HOLI_TRANS_TIME_6")
	public int divHoliTransTime6;
	/*振替乖離時間7*/
	@Column(name = "DIV_HOLI_TRANS_TIME_7")
	public int divHoliTransTime7;
	/*振替乖離時間8*/
	@Column(name = "DIV_HOLI_TRANS_TIME_8")
	public int divHoliTransTime8;
	/*振替乖離時間9*/
	@Column(name = "DIV_HOLI_TRANS_TIME_9")
	public int divHoliTransTime9;
	/*振替乖離時間１0*/
	@Column(name = "DIV_HOLI_TRANS_TIME_10")
	public int divHoliTransTime10;
	/*法定内休日出勤深夜乖離時間*/
	@Column(name = "DIV_LEG_HOLI_WORK_MIDN")
	public int divLegHoliWorkMidn;
	/*法定外休日出勤深夜乖離時間*/
	@Column(name = "DIV_ILLEG_HOLI_WORK_MIDN")
	public int divIllegHoliWorkMidn;
	/*祝日出勤深夜乖離時間*/
	@Column(name = "DIV_PB_HOLI_WORK_MIDN")
	public int divPbHoliWorkMidn;
	/*----------------------日別実績の休出枠時間------------------------------*/
	
	/*----------------------日別実績の休暇時間------------------------------*/
	/* 年休使用時間 */
	@Column(name = "ANNUALLEAVE_TIME")
	public int annualleaveTime;
	/* 年休時間消化休暇使用時間 */
	@Column(name = "ANNUALLEAVE_TDV_TIME")
	public int annualleaveTdvTime;
	/* 代休使用時間 */
	@Column(name = "COMPENSATORYLEAVE_TIME")
	public int compensatoryLeaveTime;
	/* 代休時間消化休暇使用時間 */
	@Column(name = "COMPENSATORYLEAVE_TDV_TIME")
	public int compensatoryLeaveTdvTime;
	/* 特別休暇使用時間 */
	@Column(name = "SPECIALHOLIDAY_TIME")
	public int specialHolidayTime;
	/* 特別休暇時間消化休暇使用時間 */
	@Column(name = "SPECIALHOLIDAY_TDV_TIME")
	public int specialHolidayTdvTime;
	/* 超過有休使用時間 */
	@Column(name = "EXCESSSALARIES_TIME")
	public int excessSalaryiesTime;
	/* 超過有休時間消化休暇使用時間*/
	@Column(name = "EXCESSSALARIES_TDV_TIME")
	public int excessSalaryiesTdvTime;
	/* 積立年休使用時間*/
	@Column(name = "RETENTIONYEARLY_TIME")
	public int retentionYearlyTime;
	/* 欠勤使用時間*/
	@Column(name = "ABSENCE_TIME")
	public int absenceTime;
	/* 時間消化休暇使用時間*/
	@Column(name = "TDV_TIME")
	public int tdvTime;
	/* 時間消化休暇不足時間*/
	@Column(name = "TDV_SHORTAGE_TIME")
	public int tdvShortageTime;
	/*----------------------日別実績の休暇時間------------------------------*/
	
	/*----------------------日別実績の予定時間------------------------------*/
	/*勤務予定時間*/
	@Column(name = "WORK_SCHEDULE_TIME")
	public int workScheduleTime;
	/*計画所定労働時間*/
	@Column(name = "SCHEDULE_PRE_LABOR_TIME")
	public int schedulePreLaborTime;
	/*実績所定労働時間*/
	@Column(name = "RECORE_PRE_LABOR_TIME")
	public int recorePreLaborTime;
	/*----------------------日別実績の予定時間------------------------------*/
	
	/*----------------------日別実績の所定内時間------------------------------*/
	/*就業時間*/
	@Column(name = "WORK_TIME")
	public int workTime;
	/*実働就業時間*/
	@Column(name = "PEFOM_WORK_TIME")
	public int pefomWorkTime;
	/*所定内割増時間*/
	@Column(name = "PRS_INCLD_PRMIM_TIME")
	public int prsIncldPrmimTime;
	/*所定内深夜時間*/
	@Column(name = "PRS_INCLD_MIDN_TIME")
	public int prsIncldMidnTime;
	/*休暇加算時間*/
	@Column(name = "VACTN_ADD_TIME")
	public int vactnAddTime;
	/*計算所定内深夜時間*/
	@Column(name = "CALC_PRS_INCLD_MIDN_TIME")
	public int calcPrsIncldMidnTime;
	/*所定内深夜乖離時間*/
	@Column(name = "DIV_PRS_INCLD_MIDN_TIME")
	public int divPrsIncldMidnTime;
	/*----------------------日別実績の所定内時間------------------------------*/
	
	/*----------------------日別実績の残業時間------------------------------*/
	/*残業時間1*/
	@Column(name = "OVER_TIME_1")
	public int overTime1;
	/*残業時間2*/
	@Column(name = "OVER_TIME_2")
	public int overTime2;
	/*残業時間3*/
	@Column(name = "OVER_TIME_3")
	public int overTime3;
	/*残業時間4*/
	@Column(name = "OVER_TIME_4")
	public int overTime4;
	/*残業時間5*/
	@Column(name = "OVER_TIME_5")
	public int overTime5;
	/*残業時間6*/
	@Column(name = "OVER_TIME_6")
	public int overTime6;
	/*残業時間7*/
	@Column(name = "OVER_TIME_7")
	public int overTime7;
	/*残業時間8*/
	@Column(name = "OVER_TIME_8")
	public int overTime8;
	/*残業時間9*/
	@Column(name = "OVER_TIME_9")
	public int overTime9;
	/*残業時間10*/
	@Column(name = "OVER_TIME_10")
	public int overTime10;
	/*振替時間1*/
	@Column(name = "TRANS_OVER_TIME_1")
	public int transOverTime1;
	/*振替時間2*/
	@Column(name = "TRANS_OVER_TIME_2")
	public int transOverTime2;
	/*振替時間3*/
	@Column(name = "TRANS_OVER_TIME_3")
	public int transOverTime3;
	/*振替時間4*/
	@Column(name = "TRANS_OVER_TIME_4")
	public int transOverTime4;
	/*振替時間5*/
	@Column(name = "TRANS_OVER_TIME_5")
	public int transOverTime5;
	/*振替時間6*/
	@Column(name = "TRANS_OVER_TIME_6")
	public int transOverTime6;
	/*振替時間7*/
	@Column(name = "TRANS_OVER_TIME_7")
	public int transOverTime7;
	/*振替時間8*/
	@Column(name = "TRANS_OVER_TIME_8")
	public int transOverTime8;
	/*振替時間9*/
	@Column(name = "TRANS_OVER_TIME_9")
	public int transOverTime9;
	/*振替時間10*/
	@Column(name = "TRANS_OVER_TIME_10")
	public int transOverTime10;
	/*計算残業時間1*/
	@Column(name = "CALC_OVER_TIME_1")
	public int calcOverTime1;
	/*計算残業時間2*/
	@Column(name = "CALC_OVER_TIME_2")
	public int calcOverTime2;
	/*計算残業時間3*/
	@Column(name = "CALC_OVER_TIME_3")
	public int calcOverTime3;
	/*計算残業時間4*/
	@Column(name = "CALC_OVER_TIME_4")
	public int calcOverTime4;
	/*計算残業時間5*/
	@Column(name = "CALC_OVER_TIME_5")
	public int calcOverTime5;
	/*計算残業時間6*/
	@Column(name = "CALC_OVER_TIME_6")
	public int calcOverTime6;
	/*計算残業時間7*/
	@Column(name = "CALC_OVER_TIME_7")
	public int calcOverTime7;
	/*計算残業時間8*/
	@Column(name = "CALC_OVER_TIME_8")
	public int calcOverTime8;
	/*計算残業時間9*/
	@Column(name = "CALC_OVER_TIME_9")
	public int calcOverTime9;
	/*計算残業時間10*/
	@Column(name = "CALC_OVER_TIME_10")
	public int calcOverTime10;
	/*計算振替時間1*/
	@Column(name = "CALC_TRANS_OVER_TIME_1")
	public int calcTransOverTime1;
	/*計算振替時間2*/
	@Column(name = "CALC_TRANS_OVER_TIME_2")
	public int calcTransOverTime2;
	/*計算振替時間3*/
	@Column(name = "CALC_TRANS_OVER_TIME_3")
	public int calcTransOverTime3;
	/*計算振替時間4*/
	@Column(name = "CALC_TRANS_OVER_TIME_4")
	public int calcTransOverTime4;
	/*計算振替時間5*/
	@Column(name = "CALC_TRANS_OVER_TIME_5")
	public int calcTransOverTime5;
	/*計算振替時間6*/
	@Column(name = "CALC_TRANS_OVER_TIME_6")
	public int calcTransOverTime6;
	/*計算振替時間7*/
	@Column(name = "CALC_TRANS_OVER_TIME_7")
	public int calcTransOverTime7;
	/*計算振替時間8*/
	@Column(name = "CALC_TRANS_OVER_TIME_8")
	public int calcTransOverTime8;
	/*計算振替時間9*/
	@Column(name = "CALC_TRANS_OVER_TIME_9")
	public int calcTransOverTime9;
	/*計算振替時間10*/
	@Column(name = "CALC_TRANS_OVER_TIME_10")
	public int calcTransOverTime10;
	/*事前残業申請時間1*/
	@Column(name = "PRE_OVER_TIME_APP_TIME_1")
	public int preOverTimeAppTime1;
	/*事前残業申請時間2*/
	@Column(name = "PRE_OVER_TIME_APP_TIME_2")
	public int preOverTimeAppTime2;
	/*事前残業申請時間3*/
	@Column(name = "PRE_OVER_TIME_APP_TIME_3")
	public int preOverTimeAppTime3;
	/*事前残業申請時間4*/
	@Column(name = "PRE_OVER_TIME_APP_TIME_4")
	public int preOverTimeAppTime4;
	/*事前残業申請時間5*/
	@Column(name = "PRE_OVER_TIME_APP_TIME_5")
	public int preOverTimeAppTime5;
	/*事前残業申請時間6*/
	@Column(name = "PRE_OVER_TIME_APP_TIME_6")
	public int preOverTimeAppTime6;
	/*事前残業申請時間7*/
	@Column(name = "PRE_OVER_TIME_APP_TIME_7")
	public int preOverTimeAppTime7;
	/*事前残業申請時間8*/
	@Column(name = "PRE_OVER_TIME_APP_TIME_8")
	public int preOverTimeAppTime8;
	/*事前残業申請時間9*/
	@Column(name = "PRE_OVER_TIME_APP_TIME_9")
	public int preOverTimeAppTime9;
	/*事前残業申請時間10*/
	@Column(name = "PRE_OVER_TIME_APP_TIME_10")
	public int preOverTimeAppTime10;
	/*法定外残業深夜時間*/
	@Column(name = "ILEGL_MIDN_OVER_TIME")
	public int ileglMidntOverTime;
	/*計算法定外残業深夜時間*/
	@Column(name = "CALC_ILEGL_MIDN_OVER_TIME")
	public int calcIleglMidNOverTime;
	/*残業拘束時間*/
	@Column(name = "OVER_TIME_BIND_TIME")
	public int overTimeBindTime;
	/*変形法定内残業*/
	@Column(name = "DEFORM_LEGL_OVER_TIME")
	public int deformLeglOverTime;
	/*フレックス時間*/
	@Column(name = "FLEX_TIME")
	public int flexTime;
	/*計算フレックス時間*/
	@Column(name = "CALC_FLEX_TIME")
	public int calcFlexTime;
	/*事前申請フレックス時間*/
	@Column(name = "PRE_APP_FLEX_TIME")
	public int preAppFlexTime;
	/*残業乖離時間1*/
	@Column(name = "DIV_OVER_TIME_1")
	public int divOverTime1;
	/*残業乖離時間2*/
	@Column(name = "DIV_OVER_TIME_2")
	public int divOverTime2;
	/*残業乖離時間3*/
	@Column(name = "DIV_OVER_TIME_3")
	public int divOverTime3;
	/*残業乖離時間4*/
	@Column(name = "DIV_OVER_TIME_4")
	public int divOverTime4;
	/*残業乖離時間5*/
	@Column(name = "DIV_OVER_TIME_5")
	public int divOverTime5;
	/*残業乖離時間6*/
	@Column(name = "DIV_OVER_TIME_6")
	public int divOverTime6;
	/*残業乖離時間7*/
	@Column(name = "DIV_OVER_TIME_7")
	public int divOverTime7;
	/*残業乖離時間8*/
	@Column(name = "DIV_OVER_TIME_8")
	public int divOverTime8;
	/*残業乖離時間9*/
	@Column(name = "DIV_OVER_TIME_9")
	public int divOverTime9;
	/*残業乖離時間10*/
	@Column(name = "DIV_OVER_TIME_10")
	public int divOverTime10;
	/*振替乖離時間１*/
	@Column(name = "DIV_TRANS_OVER_TIME_1")
	public int divTransOverTime1;
	/*振替乖離時間2*/
	@Column(name = "DIV_TRANS_OVER_TIME_2")
	public int divTransOverTime2;
	/*振替乖離時間3*/
	@Column(name = "DIV_TRANS_OVER_TIME_3")
	public int divTransOverTime3;
	/*振替乖離時間4*/
	@Column(name = "DIV_TRANS_OVER_TIME_4")
	public int divTransOverTime4;
	/*振替乖離時間5*/
	@Column(name = "DIV_TRANS_OVER_TIME_5")
	public int divTransOverTime5;
	/*振替乖離時間6*/
	@Column(name = "DIV_TRANS_OVER_TIME_6")
	public int divTransOverTime6;
	/*振替乖離時間7*/
	@Column(name = "DIV_TRANS_OVER_TIME_7")
	public int divTransOverTime7;
	/*振替乖離時間8*/
	@Column(name = "DIV_TRANS_OVER_TIME_8")
	public int divTransOverTime8;
	/*振替乖離時間9*/
	@Column(name = "DIV_TRANS_OVER_TIME_9")
	public int divTransOverTime9;
	/*振替乖離時間１0*/
	@Column(name = "DIV_TRANS_OVER_TIME_10")
	public int divTransOverTime10;
	/*フレックス乖離時間*/
	@Column(name = "DIVERGENCE_FLEX_TIME")
	public int divergenceFlexTime;
	/*法定外残業深夜乖離時間*/
	@Column(name = "DIV_ILEGL_MIDN_OVER_TIME")
	public int divIleglMidnOverTime;
	/*----------------------日別実績の残業時間------------------------------*/
	
	/*----------------------日別実績の乖離時間------------------------------*/
	/*乖離時間１*/
	@Column(name = "DIVERGENCE_TIME1")
	public int divergenceTime1;
	/*乖離時間2*/
	@Column(name = "DIVERGENCE_TIME2")
	public int divergenceTime2;
	/*乖離時間3*/
	@Column(name = "DIVERGENCE_TIME3")
	public int divergenceTime3;
	/*乖離時間4*/
	@Column(name = "DIVERGENCE_TIME4")
	public int divergenceTime4;
	/*乖離時間5*/
	@Column(name = "DIVERGENCE_TIME5")
	public int divergenceTime5;
	/*乖離時間6*/
	@Column(name = "DIVERGENCE_TIME6")
	public int divergenceTime6;
	/*乖離時間7*/
	@Column(name = "DIVERGENCE_TIME7")
	public int divergenceTime7;
	/*乖離時間8*/
	@Column(name = "DIVERGENCE_TIME8")
	public int divergenceTime8;
	/*乖離時間9*/
	@Column(name = "DIVERGENCE_TIME9")
	public int divergenceTime9;
	/*乖離時間１0*/
	@Column(name = "DIVERGENCE_TIME10")
	public int divergenceTime10;
	
	/*控除時間１*/
	@Column(name = "DEDUCTION_TIME1")
	public int deductionTime1;
	/*控除時間2*/
	@Column(name = "DEDUCTION_TIME2")
	public int deductionTime2;
	/*控除時間3*/
	@Column(name = "DEDUCTION_TIME3")
	public int deductionTime3;
	/*控除時間4*/
	@Column(name = "DEDUCTION_TIME4")
	public int deductionTime4;
	/*控除時間5*/
	@Column(name = "DEDUCTION_TIME5")
	public int deductionTime5;
	/*控除時間6*/
	@Column(name = "DEDUCTION_TIME6")
	public int deductionTime6;
	/*控除時間7*/
	@Column(name = "DEDUCTION_TIME7")
	public int deductionTime7;
	/*控除時間8*/
	@Column(name = "DEDUCTION_TIME8")
	public int deductionTime8;
	/*控除時間9*/
	@Column(name = "DEDUCTION_TIME9")
	public int deductionTime9;
	/*控除時間１0*/
	@Column(name = "DEDUCTION_TIME10")
	public int deductionTime10;
	
	/*控除後乖離時間１*/
	@Column(name = "AFTER_DEDUCTION_TIME1")
	public int afterDeductionTime1;
	/*控除後乖離時間2*/
	@Column(name = "AFTER_DEDUCTION_TIME2")
	public int afterDeductionTime2;
	/*控除後乖離時間3*/
	@Column(name = "AFTER_DEDUCTION_TIME3")
	public int afterDeductionTime3;
	/*控除後乖離時間4*/
	@Column(name = "AFTER_DEDUCTION_TIME4")
	public int afterDeductionTime4;
	/*控除後乖離時間5*/
	@Column(name = "AFTER_DEDUCTION_TIME5")
	public int afterDeductionTime5;
	/*控除後乖離時間6*/
	@Column(name = "AFTER_DEDUCTION_TIME6")
	public int afterDeductionTime6;
	/*控除後乖離時間7*/
	@Column(name = "AFTER_DEDUCTION_TIME7")
	public int afterDeductionTime7;
	/*控除後乖離時間8*/
	@Column(name = "AFTER_DEDUCTION_TIME8")
	public int afterDeductionTime8;
	/*控除後乖離時間9*/
	@Column(name = "AFTER_DEDUCTION_TIME9")
	public int afterDeductionTime9;
	/*控除後乖離時間１0*/
	@Column(name = "AFTER_DEDUCTION_TIME10")
	public int afterDeductionTime10;
	
	/*乖離理由コード1*/
	@Column(name = "REASON_CODE1")
	public String reasonCode1;
	/*乖離理由コード2*/
	@Column(name = "REASON_CODE2")
	public String reasonCode2;
	/*乖離理由コード3*/
	@Column(name = "REASON_CODE3")
	public String reasonCode3;
	/*乖離理由コード4*/
	@Column(name = "REASON_CODE4")
	public String reasonCode4;
	/*乖離理由コード5*/
	@Column(name = "REASON_CODE5")
	public String reasonCode5;
	/*乖離理由コード6*/
	@Column(name = "REASON_CODE6")
	public String reasonCode6;
	/*乖離理由コード7*/
	@Column(name = "REASON_CODE7")
	public String reasonCode7;
	/*乖離理由コード8*/
	@Column(name = "REASON_CODE8")
	public String reasonCode8;
	/*乖離理由コード9*/
	@Column(name = "REASON_CODE9")
	public String reasonCode9;
	/*乖離理由コード10*/
	@Column(name = "REASON_CODE10")
	public String reasonCode10;

	/*乖離理由1*/
	@Column(name = "REASON1")
	public String reason1;
	/*乖離理由2*/
	@Column(name = "REASON2")
	public String reason2;
	/*乖離理由3*/
	@Column(name = "REASON3")
	public String reason3;
	/*乖離理由4*/
	@Column(name = "REASON4")
	public String reason4;
	/*乖離理由5*/
	@Column(name = "REASON5")
	public String reason5;
	/*乖離理由6*/
	@Column(name = "REASON6")
	public String reason6;
	/*乖離理由7*/
	@Column(name = "REASON7")
	public String reason7;
	/*乖離理由8*/
	@Column(name = "REASON8")
	public String reason8;
	/*乖離理由9*/
	@Column(name = "REASON9")
	public String reason9;
	/*乖離理由10*/
	@Column(name = "REASON10")
	public String reason10;
	/*----------------------日別実績の乖離時間------------------------------*/
	
	/*----------------------日別実績の休出時間帯------------------------------*/
	/*休日出勤1開始時刻*/
	@Column(name = "HOLI_WORK_1_STR_CLC")
	public int holiWork1StrClc;
	/*休日出勤1終了時刻*/
	@Column(name = "HOLI_WORK_1_END_CLC")
	public int holiWork1EndClc;
	/*休日出勤2開始時刻*/
	@Column(name = "HOLI_WORK_2_STR_CLC")
	public int holiWork2StrClc;
	/*休日出勤2終了時刻*/
	@Column(name = "HOLI_WORK_2_END_CLC")
	public int holiWork2EndClc;
	/*休日出勤3開始時刻*/
	@Column(name = "HOLI_WORK_3_STR_CLC")
	public int holiWork3StrClc;
	/*休日出勤3終了時刻*/
	@Column(name = "HOLI_WORK_3_END_CLC")
	public int holiWork3EndClc;
	/*休日出勤4開始時刻*/
	@Column(name = "HOLI_WORK_4_STR_CLC")
	public int holiWork4StrClc;
	/*休日出勤4終了時刻*/
	@Column(name = "HOLI_WORK_4_END_CLC")
	public int holiWork4EndClc;
	/*休日出勤5開始時刻*/
	@Column(name = "HOLI_WORK_5_STR_CLC")
	public int holiWork5StrClc;
	/*休日出勤5終了時刻*/
	@Column(name = "HOLI_WORK_5_END_CLC")
	public int holiWork5EndClc;
	/*休日出勤6開始時刻*/
	@Column(name = "HOLI_WORK_6_STR_CLC")
	public int holiWork6StrClc;
	/*休日出勤6終了時刻*/
	@Column(name = "HOLI_WORK_6_END_CLC")
	public int holiWork6EndClc;
	/*休日出勤7開始時刻*/
	@Column(name = "HOLI_WORK_7_STR_CLC")
	public int holiWork7StrClc;
	/*休日出勤7終了時刻*/
	@Column(name = "HOLI_WORK_7_END_CLC")
	public int holiWork7EndClc;
	/*休日出勤8開始時刻*/
	@Column(name = "HOLI_WORK_8_STR_CLC")
	public int holiWork8StrClc;
	/*休日出勤8終了時刻*/
	@Column(name = "HOLI_WORK_8_END_CLC")
	public int holiWork8EndClc;
	/*休日出勤9開始時刻*/
	@Column(name = "HOLI_WORK_9_STR_CLC")
	public int holiWork9StrClc;
	/*休日出勤9終了時刻*/
	@Column(name = "HOLI_WORK_9_END_CLC")
	public int holiWork9EndClc;
	/*休日出勤10開始時刻*/
	@Column(name = "HOLI_WORK_10_STR_CLC")
	public int holiWork10StrClc;
	/*休日出勤10終了時刻*/
	@Column(name = "HOLI_WORK_10_END_CLC")
	public int holiWork10EndClc;
	/*----------------------日別実績の休出時間帯------------------------------*/
	
	/*----------------------日別実績の残業時間帯------------------------------*/
	/*残業1開始時刻*/
	@Column(name = "OVER_TIME_1_STR_CLC")
	public int overTime1StrClc;
	/*残業1終了時刻*/
	@Column(name = "OVER_TIME_1_END_CLC")
	public int overTime1EndClc;
	/*残業2開始時刻*/
	@Column(name = "OVER_TIME_2_STR_CLC")
	public int overTime2StrClc;
	/*残業2終了時刻*/
	@Column(name = "OVER_TIME_2_END_CLC")
	public int overTime2EndClc;
	/*残業3開始時刻*/
	@Column(name = "OVER_TIME_3_STR_CLC")
	public int overTime3StrClc;
	/*残業3終了時刻*/
	@Column(name = "OVER_TIME_3_END_CLC")
	public int overTime3EndClc;
	/*残業4開始時刻*/
	@Column(name = "OVER_TIME_4_STR_CLC")
	public int overTime4StrClc;
	/*残業4終了時刻*/
	@Column(name = "OVER_TIME_4_END_CLC")
	public int overTime4EndClc;
	/*残業5開始時刻*/
	@Column(name = "OVER_TIME_5_STR_CLC")
	public int overTime5StrClc;
	/*残業5終了時刻*/
	@Column(name = "OVER_TIME_5_END_CLC")
	public int overTime5EndClc;
	/*残業6開始時刻*/
	@Column(name = "OVER_TIME_6_STR_CLC")
	public int overTime6StrClc;
	/*残業6終了時刻*/
	@Column(name = "OVER_TIME_6_END_CLC")
	public int overTime6EndClc;
	/*残業7開始時刻*/
	@Column(name = "OVER_TIME_7_STR_CLC")
	public int overTime7StrClc;
	/*残業7終了時刻*/
	@Column(name = "OVER_TIME_7_END_CLC")
	public int overTime7EndClc;
	/*残業8開始時刻*/
	@Column(name = "OVER_TIME_8_STR_CLC")
	public int overTime8StrClc;
	/*残業8終了時刻*/
	@Column(name = "OVER_TIME_8_END_CLC")
	public int overTime8EndClc;
	/*残業9開始時刻*/
	@Column(name = "OVER_TIME_9_STR_CLC")
	public int overTime9StrClc;
	/*残業9終了時刻*/
	@Column(name = "OVER_TIME_9_END_CLC")
	public int overTime9EndClc;
	/*残業10開始時刻*/
	@Column(name = "OVER_TIME_10_STR_CLC")
	public int overTime10StrClc;
	/*残業10終了時刻*/
	@Column(name = "OVER_TIME_10_END_CLC")
	public int overTime10EndClc;
	/*----------------------日別実績の残業時間帯------------------------------*/
	
	@Override
	protected Object getKey() {
		return this.krcdtDayTimePK;
	}
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayPremiumTime krcdtDayPremiumTime;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcdtDayTime", orphanRemoval = true)
	public List<KrcdtDayLeaveEarlyTime> krcdtDayLeaveEarlyTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcdtDayTime", orphanRemoval = true)
	public List<KrcdtDayLateTime> krcdtDayLateTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcdtDayTime", orphanRemoval = true)
	public List<KrcdtDaiShortWorkTime> krcdtDaiShortWorkTime;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcdtDayTime", orphanRemoval = true)
	public List<KrcdtDayShorttime> KrcdtDayShorttime;
	
	public static KrcdtDayTime toEntity(AttendanceTimeOfDailyPerformance attendanceTime) {
		val entity = new KrcdtDayTime();
		entity.krcdtDayTimePK = new KrcdtDayTimePK(attendanceTime.getEmployeeId(),
				attendanceTime.getYmd());
		entity.setData(attendanceTime);
		return entity;
	}
	
	public void setData(AttendanceTimeOfDailyPerformance attendanceTime) {
		/*----------------------日別実績の勤怠時間------------------------------*/
		ActualWorkingTimeOfDaily actualWork = attendanceTime.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWork = actualWork == null ? null :actualWork.getTotalWorkingTime();
		ConstraintTime constraintTime = actualWork == null ? null : actualWork.getConstraintTime();
		ExcessOfStatutoryMidNightTime excessStt = totalWork == null ? null : totalWork.getExcessOfStatutoryTimeOfDaily() == null ? null 
				: totalWork.getExcessOfStatutoryTimeOfDaily().getExcessOfStatutoryMidNightTime();
		StayingTimeOfDaily staying = attendanceTime.getStayingTime();
		if(totalWork != null){
			/* 総労働時間 */
			this.totalAttTime = totalWork.getTotalTime() == null ? 0 : totalWork.getTotalTime().valueAsMinutes();
			/* 総計算時間 */
			this.totalCalcTime = totalWork.getTotalCalcTime() == null ? 0 : totalWork.getTotalCalcTime().valueAsMinutes();
			/* 実働時間 */
			this.actWorkTime = totalWork.getActualTime() == null ? 0 : totalWork.getActualTime().valueAsMinutes();
			/* 勤務回数 */
			this.workTimes = totalWork.getWorkTimes() == null ? 0 : totalWork.getWorkTimes().v();
				
		}
		if(constraintTime != null){
			/* 総拘束時間 */
			this.totalBindTime = constraintTime.getTotalConstraintTime() == null ? 0 : constraintTime.getTotalConstraintTime().valueAsMinutes();
			/* 深夜拘束時間 */
			this.midnBindTime = constraintTime.getLateNightConstraintTime() == null ? 0 : constraintTime.getLateNightConstraintTime().valueAsMinutes();
		}
		if(actualWork != null){
			/* 拘束差異時間 */
			this.bindDiffTime = actualWork.getConstraintDifferenceTime() == null ? 0 : actualWork.getConstraintDifferenceTime().valueAsMinutes();
			/* 時差勤務時間 */
			this.diffTimeWorkTime = actualWork.getTimeDifferenceWorkingHours() == null ? 0 : actualWork.getTimeDifferenceWorkingHours().valueAsMinutes();
		}
		if(excessStt != null){
			/* 所定外深夜時間 */
			this.outPrsMidnTime = excessStt.getTime() == null | excessStt.getTime().getTime() == null ? 0 : excessStt.getTime().getTime().valueAsMinutes();
			this.calcOutPrsMidnTime = excessStt.getTime() == null | excessStt.getTime().getCalcTime() == null ? 0 : excessStt.getTime().getCalcTime().valueAsMinutes();
			/* 事前所定外深夜時間 */
			this.preOutPrsMidnTime = excessStt.getBeforeApplicationTime() == null ? 0 : excessStt.getBeforeApplicationTime().valueAsMinutes();
			//所定外深夜乖離時間
			this.divOutPrsMidnTime = excessStt.getTime() == null | excessStt.getTime().getDivergenceTime() == null ? 0 : excessStt.getTime().getDivergenceTime().valueAsMinutes();  
		}
		
		/* 予実差異時間 */
		this.budgetTimeVariance = attendanceTime.getBudgetTimeVariance() == null ? 0 : attendanceTime.getBudgetTimeVariance().valueAsMinutes();
		/* 不就労時間 */
		this.unemployedTime = attendanceTime.getUnEmployedTime() == null ? 0 : attendanceTime.getUnEmployedTime().valueAsMinutes();
		
		if(staying != null){
			/* 滞在時間 */
			this.stayingTime = staying.getStayingTime() == null ? 0 : staying.getStayingTime().valueAsMinutes();
			/* 出勤前時間 */
			this.bfrWorkTime = staying.getBeforeWoringTime() == null ? 0 : staying.getBeforeWoringTime().valueAsMinutes();
			/* 退勤後時間 */
			this.aftLeaveTime = staying.getAfterLeaveTime() == null ? 0 : staying.getAfterLeaveTime().valueAsMinutes();
			/* PCログオン前時間 */
			this.bfrPcLogonTime = staying.getBeforePCLogOnTime() == null ? 0 : staying.getBeforePCLogOnTime().valueAsMinutes();
			/* PCログオフ後時間 */
			this.aftPcLogoffTime = staying.getAfterPCLogOffTime() == null ? 0 : staying.getAfterPCLogOffTime().valueAsMinutes();
		}
		/*----------------------日別実績の勤怠時間------------------------------*/
		
		/*----------------------日別実績の休憩時間------------------------------*/
		this.toRecordTotalTime = 0;
		this.calToRecordTotalTime = 0;
		
		this.toRecordInTime = 0;
		this.calToRecordInTime = 0;
		
		this.toRecordOutTime = 0;
		this.calToRecordOutTime = 0;
		
		this.deductionTotalTime = 0;
		this.calDeductionTotalTime = 0;
		
		this.deductionInTime = 0;
		this.calDeductionInTime = 0;
		
		this.deductionOutTime = 0;
		this.calDeductionOutTime = 0;
		
		this.duringworkTime = 0;
		
		this.count = 0;
		
		if(attendanceTime != null) {
			/*----------------------日別実績の予定時間------------------------------*/
			if(attendanceTime.getWorkScheduleTimeOfDaily() !=null) {
				val domain = attendanceTime.getWorkScheduleTimeOfDaily(); 
				/*勤務予定時間*/
				this.workScheduleTime = domain.getWorkScheduleTime() == null || domain.getWorkScheduleTime().getTotal() == null ? 0 
					: domain.getWorkScheduleTime().getTotal().valueAsMinutes();
				/*計画所定労働時間*/
				this.schedulePreLaborTime = domain.getSchedulePrescribedLaborTime() == null ? 0 : domain.getSchedulePrescribedLaborTime().valueAsMinutes();
				/*実績所定労働時間*/
				this.recorePreLaborTime = domain.getRecordPrescribedLaborTime() == null ? 0 : domain.getRecordPrescribedLaborTime().valueAsMinutes();
			}
			/*----------------------日別実績の予定時間------------------------------*/
			
			if(attendanceTime.getActualWorkingTimeOfDaily() != null) {
				/*----------------------日別実績の乖離時間------------------------------*/
				if(actualWork != null&& actualWork.getDivTime() != null) {
					val a = actualWork.getDivTime();
					for(int loopNumber = 1 ; loopNumber <= 10 ; loopNumber++) {
						val divergenceTime = getDivergenceTime(a , loopNumber);
						setValue(divergenceTime, loopNumber);
					}
				}
				/*----------------------日別実績の乖離時間------------------------------*/
				if(attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime() != null) {
					/*----------------------日別実績の休憩時間------------------------------*/
					if(attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getBreakTimeOfDaily() != null) {
						val recordTime = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getBreakTimeOfDaily().getToRecordTotalTime();
						val dedTime = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getBreakTimeOfDaily().getDeductionTotalTime();
						val duringTime = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getBreakTimeOfDaily().getWorkTime();
						val workTimes = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getBreakTimeOfDaily().getGooutTimes();
						if(recordTime.getTotalTime() != null) {
							this.toRecordTotalTime = recordTime.getTotalTime().getTime() == null ? 0 : recordTime.getTotalTime().getTime().valueAsMinutes();
							this.calToRecordTotalTime = recordTime.getTotalTime().getCalcTime() == null ? 0 : recordTime.getTotalTime().getCalcTime().valueAsMinutes();
						}
						
						if(recordTime.getWithinStatutoryTotalTime() != null) {
							this.toRecordInTime = recordTime.getWithinStatutoryTotalTime().getTime() == null ? 0 : recordTime.getWithinStatutoryTotalTime().getTime().valueAsMinutes();
							this.calToRecordInTime = recordTime.getWithinStatutoryTotalTime().getCalcTime() == null ? 0 : recordTime.getWithinStatutoryTotalTime().getCalcTime().valueAsMinutes();;
						}

						if(recordTime.getExcessOfStatutoryTotalTime() != null) {
							this.toRecordOutTime = recordTime.getExcessOfStatutoryTotalTime().getTime() == null ? 0 : recordTime.getExcessOfStatutoryTotalTime().getTime().valueAsMinutes();
							this.calToRecordOutTime = recordTime.getExcessOfStatutoryTotalTime().getCalcTime() == null ? 0 : recordTime.getExcessOfStatutoryTotalTime().getCalcTime().valueAsMinutes();
						}
						
						if(dedTime.getTotalTime() != null) {
							this.deductionTotalTime = dedTime.getTotalTime().getTime() == null ? 0 : dedTime.getTotalTime().getTime().valueAsMinutes();
							this.calDeductionTotalTime = dedTime.getTotalTime().getCalcTime() == null ? 0 : dedTime.getTotalTime().getCalcTime().valueAsMinutes();
						}

						if(dedTime.getWithinStatutoryTotalTime() != null) {
							this.deductionInTime = dedTime.getWithinStatutoryTotalTime().getTime() == null ? 0 : dedTime.getWithinStatutoryTotalTime().getTime().valueAsMinutes();
							this.calDeductionInTime = dedTime.getWithinStatutoryTotalTime().getCalcTime() == null ? 0 : dedTime.getWithinStatutoryTotalTime().getCalcTime().valueAsMinutes();
						}
						
						if(dedTime.getExcessOfStatutoryTotalTime() != null) {
							this.deductionOutTime = dedTime.getExcessOfStatutoryTotalTime().getTime() == null ? 0 : dedTime.getExcessOfStatutoryTotalTime().getTime().valueAsMinutes();;
							this.calDeductionOutTime = dedTime.getExcessOfStatutoryTotalTime().getCalcTime() == null ? 0 : dedTime.getExcessOfStatutoryTotalTime().getCalcTime().valueAsMinutes();;
						}
						
						if(duringTime != null)
							this.duringworkTime = duringTime.v() == null ? 0 : duringTime.v();
						
						if(workTimes != null)
							this.count = workTimes.v() == null ? 0 : workTimes.v();
					}
					/*----------------------日別実績の休憩時間------------------------------*/
					/*----------------------日別実績の休出枠時間------------------------------*/
					val test = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily();
					val domain = test == null ? null : test.getWorkHolidayTime().orElse(null);
					
					if(domain != null && domain.getHolidayWorkFrameTime() != null || !domain.getHolidayWorkFrameTime().isEmpty()){
						HolidayWorkFrameTime frame1 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 1);
						HolidayWorkFrameTime frame2 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 2);
						HolidayWorkFrameTime frame3 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 3);
						HolidayWorkFrameTime frame4 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 4);
						HolidayWorkFrameTime frame5 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 5);
						HolidayWorkFrameTime frame6 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 6);
						HolidayWorkFrameTime frame7 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 7);
						HolidayWorkFrameTime frame8 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 8);
						HolidayWorkFrameTime frame9 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 9);
						HolidayWorkFrameTime frame10 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 10);
						/*休日出勤時間*/
						this.holiWorkTime1 = !frame1.getHolidayWorkTime().isPresent() || frame1.getHolidayWorkTime().get().getTime() == null ? 0 : frame1.getHolidayWorkTime().get().getTime().valueAsMinutes();
						this.holiWorkTime2 = !frame2.getHolidayWorkTime().isPresent() || frame2.getHolidayWorkTime().get().getTime() == null ? 0 : frame2.getHolidayWorkTime().get().getTime().valueAsMinutes();
						this.holiWorkTime3 = !frame3.getHolidayWorkTime().isPresent() || frame3.getHolidayWorkTime().get().getTime() == null ? 0 : frame3.getHolidayWorkTime().get().getTime().valueAsMinutes();
						this.holiWorkTime4 = !frame4.getHolidayWorkTime().isPresent() || frame4.getHolidayWorkTime().get().getTime() == null ? 0 : frame4.getHolidayWorkTime().get().getTime().valueAsMinutes();
						this.holiWorkTime5 = !frame5.getHolidayWorkTime().isPresent() || frame5.getHolidayWorkTime().get().getTime() == null ? 0 : frame5.getHolidayWorkTime().get().getTime().valueAsMinutes();
						this.holiWorkTime6 = !frame6.getHolidayWorkTime().isPresent() || frame6.getHolidayWorkTime().get().getTime() == null ? 0 : frame6.getHolidayWorkTime().get().getTime().valueAsMinutes();
						this.holiWorkTime7 = !frame7.getHolidayWorkTime().isPresent() || frame7.getHolidayWorkTime().get().getTime() == null ? 0 : frame7.getHolidayWorkTime().get().getTime().valueAsMinutes();
						this.holiWorkTime8 = !frame8.getHolidayWorkTime().isPresent() || frame8.getHolidayWorkTime().get().getTime() == null ? 0 : frame8.getHolidayWorkTime().get().getTime().valueAsMinutes();
						this.holiWorkTime9 = !frame9.getHolidayWorkTime().isPresent() || frame9.getHolidayWorkTime().get().getTime() == null ? 0 : frame9.getHolidayWorkTime().get().getTime().valueAsMinutes();
						this.holiWorkTime10 = !frame10.getHolidayWorkTime().isPresent() || frame10.getHolidayWorkTime().get().getTime() == null ? 0 : frame10.getHolidayWorkTime().get().getTime().valueAsMinutes();
						/*振替時間*/
						this.transTime1 = !frame1.getTransferTime().isPresent() || frame1.getTransferTime().get().getTime() == null ? 0 : frame1.getTransferTime().get().getTime().valueAsMinutes();
						this.transTime2 = !frame2.getTransferTime().isPresent() || frame2.getTransferTime().get().getTime() == null ? 0 : frame2.getTransferTime().get().getTime().valueAsMinutes();
						this.transTime3 = !frame3.getTransferTime().isPresent() || frame3.getTransferTime().get().getTime() == null ? 0 : frame3.getTransferTime().get().getTime().valueAsMinutes();
						this.transTime4 = !frame4.getTransferTime().isPresent() || frame4.getTransferTime().get().getTime() == null ? 0 : frame4.getTransferTime().get().getTime().valueAsMinutes();
						this.transTime5 = !frame5.getTransferTime().isPresent() || frame5.getTransferTime().get().getTime() == null ? 0 : frame5.getTransferTime().get().getTime().valueAsMinutes();
						this.transTime6 = !frame6.getTransferTime().isPresent() || frame6.getTransferTime().get().getTime() == null ? 0 : frame6.getTransferTime().get().getTime().valueAsMinutes();
						this.transTime7 = !frame7.getTransferTime().isPresent() || frame7.getTransferTime().get().getTime() == null ? 0 : frame7.getTransferTime().get().getTime().valueAsMinutes();
						this.transTime8 = !frame8.getTransferTime().isPresent() || frame8.getTransferTime().get().getTime() == null ? 0 : frame8.getTransferTime().get().getTime().valueAsMinutes();
						this.transTime9 = !frame9.getTransferTime().isPresent() || frame9.getTransferTime().get().getTime() == null ? 0 : frame9.getTransferTime().get().getTime().valueAsMinutes();
						this.transTime10 = !frame10.getTransferTime().isPresent() || frame10.getTransferTime().get().getTime() == null ? 0 : frame10.getTransferTime().get().getTime().valueAsMinutes();
						/*計算休日出勤時間*/
						this.calcHoliWorkTime1 = !frame1.getHolidayWorkTime().isPresent() || frame1.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame1.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
						this.calcHoliWorkTime2 = !frame2.getHolidayWorkTime().isPresent() || frame2.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame2.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
						this.calcHoliWorkTime3 = !frame3.getHolidayWorkTime().isPresent() || frame3.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame3.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
						this.calcHoliWorkTime4 = !frame4.getHolidayWorkTime().isPresent() || frame4.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame4.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
						this.calcHoliWorkTime5 = !frame5.getHolidayWorkTime().isPresent() || frame5.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame5.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
						this.calcHoliWorkTime6 = !frame6.getHolidayWorkTime().isPresent() || frame6.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame6.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
						this.calcHoliWorkTime7 = !frame7.getHolidayWorkTime().isPresent() || frame7.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame7.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
						this.calcHoliWorkTime8 = !frame8.getHolidayWorkTime().isPresent() || frame8.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame8.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
						this.calcHoliWorkTime9 = !frame9.getHolidayWorkTime().isPresent() || frame9.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame9.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
						this.calcHoliWorkTime10 = !frame10.getHolidayWorkTime().isPresent() || frame10.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame10.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
						/*計算振替時間*/
						this.calcTransTime1 = !frame1.getTransferTime().isPresent() || frame1.getTransferTime().get().getCalcTime() == null ? 0 : frame1.getTransferTime().get().getCalcTime().valueAsMinutes();
						this.calcTransTime2 = !frame2.getTransferTime().isPresent() || frame2.getTransferTime().get().getCalcTime() == null ? 0 : frame2.getTransferTime().get().getCalcTime().valueAsMinutes();
						this.calcTransTime3 = !frame3.getTransferTime().isPresent() || frame3.getTransferTime().get().getCalcTime() == null ? 0 : frame3.getTransferTime().get().getCalcTime().valueAsMinutes();
						this.calcTransTime4 = !frame4.getTransferTime().isPresent() || frame4.getTransferTime().get().getCalcTime() == null ? 0 : frame4.getTransferTime().get().getCalcTime().valueAsMinutes();
						this.calcTransTime5 = !frame5.getTransferTime().isPresent() || frame5.getTransferTime().get().getCalcTime() == null ? 0 : frame5.getTransferTime().get().getCalcTime().valueAsMinutes();
						this.calcTransTime6 = !frame6.getTransferTime().isPresent() || frame6.getTransferTime().get().getCalcTime() == null ? 0 : frame6.getTransferTime().get().getCalcTime().valueAsMinutes();
						this.calcTransTime7 = !frame7.getTransferTime().isPresent() || frame7.getTransferTime().get().getCalcTime() == null ? 0 : frame7.getTransferTime().get().getCalcTime().valueAsMinutes();
						this.calcTransTime8 = !frame8.getTransferTime().isPresent() || frame8.getTransferTime().get().getCalcTime() == null ? 0 : frame8.getTransferTime().get().getCalcTime().valueAsMinutes();
						this.calcTransTime9 = !frame9.getTransferTime().isPresent() || frame9.getTransferTime().get().getCalcTime() == null ? 0 : frame9.getTransferTime().get().getCalcTime().valueAsMinutes();
						this.calcTransTime10 = !frame10.getTransferTime().isPresent() || frame10.getTransferTime().get().getCalcTime() == null ? 0 : frame10.getTransferTime().get().getCalcTime().valueAsMinutes();
						/*事前申請時間*/
						this.preAppTime1 = !frame1.getBeforeApplicationTime().isPresent() || frame1.getBeforeApplicationTime().get() == null ? 0 : frame1.getBeforeApplicationTime().get().valueAsMinutes();
						this.preAppTime2 = !frame2.getBeforeApplicationTime().isPresent() || frame2.getBeforeApplicationTime().get() == null ? 0 : frame2.getBeforeApplicationTime().get().valueAsMinutes();
						this.preAppTime3 = !frame3.getBeforeApplicationTime().isPresent() || frame3.getBeforeApplicationTime().get() == null ? 0 : frame3.getBeforeApplicationTime().get().valueAsMinutes();
						this.preAppTime4 = !frame4.getBeforeApplicationTime().isPresent() || frame4.getBeforeApplicationTime().get() == null ? 0 : frame4.getBeforeApplicationTime().get().valueAsMinutes();
						this.preAppTime5 = !frame5.getBeforeApplicationTime().isPresent() || frame5.getBeforeApplicationTime().get() == null ? 0 : frame5.getBeforeApplicationTime().get().valueAsMinutes();
						this.preAppTime6 = !frame6.getBeforeApplicationTime().isPresent() || frame6.getBeforeApplicationTime().get() == null ? 0 : frame6.getBeforeApplicationTime().get().valueAsMinutes();
						this.preAppTime7 = !frame7.getBeforeApplicationTime().isPresent() || frame7.getBeforeApplicationTime().get() == null ? 0 : frame7.getBeforeApplicationTime().get().valueAsMinutes();
						this.preAppTime8 = !frame8.getBeforeApplicationTime().isPresent() || frame8.getBeforeApplicationTime().get() == null ? 0 : frame8.getBeforeApplicationTime().get().valueAsMinutes();
						this.preAppTime9 = !frame9.getBeforeApplicationTime().isPresent() || frame9.getBeforeApplicationTime().get() == null ? 0 : frame9.getBeforeApplicationTime().get().valueAsMinutes();
						this.preAppTime10 = !frame10.getBeforeApplicationTime().isPresent() || frame10.getBeforeApplicationTime().get() == null ? 0 : frame10.getBeforeApplicationTime().get().valueAsMinutes();	
						/*休日出勤乖離時間*/
						this.divHoliTime1 = !frame1.getHolidayWorkTime().isPresent() || frame1.getHolidayWorkTime().get().getDivergenceTime() == null ? 0 : frame1.getHolidayWorkTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTime2 = !frame2.getHolidayWorkTime().isPresent() || frame2.getHolidayWorkTime().get().getDivergenceTime() == null ? 0 : frame2.getHolidayWorkTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTime3 = !frame3.getHolidayWorkTime().isPresent() || frame3.getHolidayWorkTime().get().getDivergenceTime() == null ? 0 : frame3.getHolidayWorkTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTime4 = !frame4.getHolidayWorkTime().isPresent() || frame4.getHolidayWorkTime().get().getDivergenceTime() == null ? 0 : frame4.getHolidayWorkTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTime5 = !frame5.getHolidayWorkTime().isPresent() || frame5.getHolidayWorkTime().get().getDivergenceTime() == null ? 0 : frame5.getHolidayWorkTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTime6 = !frame6.getHolidayWorkTime().isPresent() || frame6.getHolidayWorkTime().get().getDivergenceTime() == null ? 0 : frame6.getHolidayWorkTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTime7 = !frame7.getHolidayWorkTime().isPresent() || frame7.getHolidayWorkTime().get().getDivergenceTime() == null ? 0 : frame7.getHolidayWorkTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTime8 = !frame8.getHolidayWorkTime().isPresent() || frame8.getHolidayWorkTime().get().getDivergenceTime() == null ? 0 : frame8.getHolidayWorkTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTime9 = !frame9.getHolidayWorkTime().isPresent() || frame9.getHolidayWorkTime().get().getDivergenceTime() == null ? 0 : frame9.getHolidayWorkTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTime10 = !frame10.getHolidayWorkTime().isPresent() || frame10.getHolidayWorkTime().get().getDivergenceTime() == null ? 0 : frame10.getHolidayWorkTime().get().getDivergenceTime().valueAsMinutes();
						/*振替乖離時間*/
						this.divHoliTransTime1 = !frame1.getTransferTime().isPresent() || frame1.getTransferTime().get().getDivergenceTime() == null ? 0 : frame1.getTransferTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTransTime2 = !frame2.getTransferTime().isPresent() || frame2.getTransferTime().get().getDivergenceTime() == null ? 0 : frame2.getTransferTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTransTime3 = !frame3.getTransferTime().isPresent() || frame3.getTransferTime().get().getDivergenceTime() == null ? 0 : frame3.getTransferTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTransTime4 = !frame4.getTransferTime().isPresent() || frame4.getTransferTime().get().getDivergenceTime() == null ? 0 : frame4.getTransferTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTransTime5 = !frame5.getTransferTime().isPresent() || frame5.getTransferTime().get().getDivergenceTime() == null ? 0 : frame5.getTransferTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTransTime6 = !frame6.getTransferTime().isPresent() || frame6.getTransferTime().get().getDivergenceTime() == null ? 0 : frame6.getTransferTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTransTime7 = !frame7.getTransferTime().isPresent() || frame7.getTransferTime().get().getDivergenceTime() == null ? 0 : frame7.getTransferTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTransTime8 = !frame8.getTransferTime().isPresent() || frame8.getTransferTime().get().getDivergenceTime() == null ? 0 : frame8.getTransferTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTransTime9 = !frame9.getTransferTime().isPresent() || frame9.getTransferTime().get().getDivergenceTime() == null ? 0 : frame9.getTransferTime().get().getDivergenceTime().valueAsMinutes();
						this.divHoliTransTime10 = !frame10.getTransferTime().isPresent() || frame10.getTransferTime().get().getDivergenceTime() == null ? 0 : frame10.getTransferTime().get().getDivergenceTime().valueAsMinutes();
						
						if(domain.getHolidayMidNightWork().isPresent()) {
							getHolidayMidNightWork(domain.getHolidayMidNightWork().get(), StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork).ifPresent(within -> {
								if(within.getTime() != null){
									/*法定内休日出勤深夜*/
									this.legHoliWorkMidn = within.getTime().getTime() == null ? 0 : within.getTime().getTime().valueAsMinutes();
									/*計算法定内休日出勤深夜*/
									this.calcLegHoliWorkMidn = within.getTime().getCalcTime() == null ? 0 : within.getTime().getCalcTime().valueAsMinutes();
									/*法定内休日出勤深夜乖離時間*/
									this.divLegHoliWorkMidn = within.getTime().getDivergenceTime() == null ? 0 : within.getTime().getDivergenceTime().valueAsMinutes();
								}
							});
							getHolidayMidNightWork(domain.getHolidayMidNightWork().get(), StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork).ifPresent(excess -> {
								if(excess.getTime() != null){
									/*法定外休日出勤深夜*/
									this.illegHoliWorkMidn = excess.getTime().getTime() == null ? 0 : excess.getTime().getTime().valueAsMinutes();
									/*計算法定外休日出勤深夜*/
									this.calcIllegHoliWorkMidn = excess.getTime().getCalcTime() == null ? 0 : excess.getTime().getCalcTime().valueAsMinutes();
									/*法定外休日出勤深夜乖離時間*/
									this.divIllegHoliWorkMidn = excess.getTime().getDivergenceTime() == null ? 0 : excess.getTime().getDivergenceTime().valueAsMinutes();
								}
							});
							getHolidayMidNightWork(domain.getHolidayMidNightWork().get(), StaturoryAtrOfHolidayWork.PublicHolidayWork).ifPresent(publicWork -> {
								if(publicWork.getTime() != null){
									/*祝日日出勤深夜*/
									this.pbHoliWorkMidn = publicWork.getTime().getTime() == null ? 0 : publicWork.getTime().getTime().valueAsMinutes();
									/*計算祝日日出勤深夜*/
									this.calcPbHoliWorkMidn = publicWork.getTime().getCalcTime() == null ? 0 : publicWork.getTime().getCalcTime().valueAsMinutes();
									/*祝日出勤深夜乖離時間*/
									this.divPbHoliWorkMidn = publicWork.getTime().getDivergenceTime() == null ? 0 : publicWork.getTime().getDivergenceTime().valueAsMinutes();
								}
							});
						}
						/*休日出勤拘束時間*/
						this.holiWorkBindTime = domain.getHolidayTimeSpentAtWork() == null ? 0 : domain.getHolidayTimeSpentAtWork().valueAsMinutes();
					}

					/*----------------------日別実績の休出枠時間------------------------------*/
					/*----------------------日別実績の休出時間帯------------------------------*/
					if(totalWork != null 
						&& totalWork.getExcessOfStatutoryTimeOfDaily() != null
						&& totalWork.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().isPresent()
						&& !totalWork.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().getHolidayWorkFrameTimeSheet().isEmpty()) {
						val holTimeSheet = totalWork.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().getHolidayWorkFrameTimeSheet(); 
						Optional<HolidayWorkFrameTimeSheet> sheet = Optional.empty();
						
						allClearHolidayWorkTS();
						sheet = getTimeSheet(holTimeSheet, 1);
						sheet.ifPresent(tc -> {
							/*休日出勤1開始時刻*/
							this.holiWork1StrClc = tc.getTimeSheet().getStart().valueAsMinutes();
							/*休日出勤1終了時刻*/
							this.holiWork1EndClc = tc.getTimeSheet().getEnd().valueAsMinutes();
						});
						sheet = getTimeSheet(holTimeSheet, 2);
						sheet.ifPresent(tc -> {
							/*休日出勤2開始時刻*/
							this.holiWork2StrClc = tc.getTimeSheet().getStart().valueAsMinutes();
							/*休日出勤2終了時刻*/
							this.holiWork2EndClc = tc.getTimeSheet().getEnd().valueAsMinutes();
						});
						sheet = getTimeSheet(holTimeSheet, 3);
						sheet.ifPresent(tc -> {
							/*休日出勤3開始時刻*/
							this.holiWork3StrClc = tc.getTimeSheet().getStart().valueAsMinutes();
							/*休日出勤3終了時刻*/
							this.holiWork3EndClc = tc.getTimeSheet().getEnd().valueAsMinutes();
						});
						sheet = getTimeSheet(holTimeSheet, 4);
						sheet.ifPresent(tc -> {
							/*休日出勤4開始時刻*/
							this.holiWork4StrClc = tc.getTimeSheet().getStart().valueAsMinutes();
							/*休日出勤4終了時刻*/
							this.holiWork4EndClc = tc.getTimeSheet().getEnd().valueAsMinutes();
						});
						sheet = getTimeSheet(holTimeSheet, 5);
						sheet.ifPresent(tc -> {
							/*休日出勤5開始時刻*/
							this.holiWork5StrClc = tc.getTimeSheet().getStart().valueAsMinutes();
							/*休日出勤5終了時刻*/
							this.holiWork5EndClc = tc.getTimeSheet().getEnd().valueAsMinutes();
						});
						sheet = getTimeSheet(holTimeSheet, 6);
						sheet.ifPresent(tc -> {
							/*休日出勤6開始時刻*/
							this.holiWork6StrClc = tc.getTimeSheet().getStart().valueAsMinutes();
							/*休日出勤6終了時刻*/
							this.holiWork6EndClc = tc.getTimeSheet().getEnd().valueAsMinutes();
						});
						sheet = getTimeSheet(holTimeSheet, 7);
						sheet.ifPresent(tc -> {
							/*休日出勤7開始時刻*/
							this.holiWork7StrClc = tc.getTimeSheet().getStart().valueAsMinutes();
							/*休日出勤7終了時刻*/
							this.holiWork7EndClc = tc.getTimeSheet().getEnd().valueAsMinutes();
						});
						sheet = getTimeSheet(holTimeSheet, 8);
						sheet.ifPresent(tc -> {
							/*休日出勤8開始時刻*/
							this.holiWork8StrClc = tc.getTimeSheet().getStart().valueAsMinutes();
							/*休日出勤8終了時刻*/
							this.holiWork8EndClc = tc.getTimeSheet().getEnd().valueAsMinutes();
						});
						sheet = getTimeSheet(holTimeSheet, 9);
						sheet.ifPresent(tc -> {
							/*休日出勤9開始時刻*/
							this.holiWork9StrClc = tc.getTimeSheet().getStart().valueAsMinutes();
							/*休日出勤9終了時刻*/
							this.holiWork9EndClc = tc.getTimeSheet().getEnd().valueAsMinutes();
						});
						sheet = getTimeSheet(holTimeSheet, 10);
						sheet.ifPresent(tc -> {
							/*休日出勤10開始時刻*/
							this.holiWork10StrClc = tc.getTimeSheet().getStart().valueAsMinutes();
							/*休日出勤10終了時刻*/
							this.holiWork10EndClc = tc.getTimeSheet().getEnd().valueAsMinutes();
						});
					}
				/*----------------------日別実績の休出時間帯------------------------------*/
					
					
					/*----------------------日別実績の休暇------------------------------*/
					val vacationDomain = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getHolidayOfDaily();
					this.annualleaveTime = 0;
					this.annualleaveTdvTime = 0;
					this.compensatoryLeaveTime = 0;
					this.compensatoryLeaveTdvTime = 0;
					this.specialHolidayTime = 0;
					this.specialHolidayTdvTime = 0;
					this.excessSalaryiesTime = 0;
					this.excessSalaryiesTdvTime = 0;
					this.retentionYearlyTime = 0;
					this.absenceTime = 0;
					this.tdvTime = 0;
					this.tdvShortageTime = 0;
					if(vacationDomain != null) {
						//年休
						if(vacationDomain.getAnnual() != null) {
							this.annualleaveTime = vacationDomain.getAnnual() == null ? 0 : vacationDomain.getAnnual().getUseTime().valueAsMinutes();
							this.annualleaveTdvTime = vacationDomain.getAnnual() == null ? 0 : vacationDomain.getAnnual().getDigestionUseTime().valueAsMinutes();
						}

						//代休
						if(vacationDomain.getSubstitute() != null) {
							this.compensatoryLeaveTime = vacationDomain.getSubstitute() == null ? 0 : vacationDomain.getSubstitute().getUseTime().valueAsMinutes();
							this.compensatoryLeaveTdvTime = vacationDomain.getSubstitute() == null ? 0 : vacationDomain.getSubstitute().getDigestionUseTime().valueAsMinutes();
						}
						
						//特別休暇
						if(vacationDomain.getSpecialHoliday() != null) {
							this.specialHolidayTime = vacationDomain.getSpecialHoliday() == null ? 0 : vacationDomain.getSpecialHoliday().getUseTime().valueAsMinutes();
							this.specialHolidayTdvTime = vacationDomain.getSpecialHoliday() == null ? 0 : vacationDomain.getSpecialHoliday().getDigestionUseTime().valueAsMinutes();
						}
						//超過有休
						if(vacationDomain.getOverSalary() != null) {
							this.excessSalaryiesTime = vacationDomain.getOverSalary() == null ? 0 : vacationDomain.getOverSalary().getUseTime().valueAsMinutes();
							this.excessSalaryiesTdvTime = vacationDomain.getOverSalary() == null ? 0 : vacationDomain.getOverSalary().getDigestionUseTime().valueAsMinutes();
						}
						//積立
						if(vacationDomain.getYearlyReserved() != null) {
							this.retentionYearlyTime = vacationDomain.getYearlyReserved() == null ? 0 : vacationDomain.getYearlyReserved().getUseTime().valueAsMinutes(); 
						}
						//欠勤
						if(vacationDomain.getAbsence() != null) {
							this.absenceTime = vacationDomain.getAbsence() == null ? 0 : vacationDomain.getAbsence().getUseTime().valueAsMinutes(); 
						}
						//時間消化休暇
						if(vacationDomain.getTimeDigest() != null) {
							this.tdvTime = vacationDomain.getTimeDigest() == null ? 0 : vacationDomain.getTimeDigest().getUseTime().valueAsMinutes();
							this.tdvShortageTime = vacationDomain.getTimeDigest() == null ? 0 : vacationDomain.getTimeDigest().getLeakageTime().valueAsMinutes();;
						}
					}
					/*----------------------日別実績の休暇------------------------------*/
					
					
					
					/*----------------------日別実績の所定内時間------------------------------*/
					if(totalWork.getWithinStatutoryTimeOfDaily() != null){
						val withinDomain = totalWork.getWithinStatutoryTimeOfDaily();
						/*就業時間*/
						this.workTime = withinDomain.getWorkTime() == null ? 0 : withinDomain.getWorkTime().valueAsMinutes();
						/*実働就業時間*/
						this.pefomWorkTime = withinDomain.getActualWorkTime() == null ? 0 : withinDomain.getActualWorkTime().valueAsMinutes();
						/*所定内割増時間*/
						this.prsIncldPrmimTime = withinDomain.getWithinPrescribedPremiumTime() == null ? 0 : withinDomain.getWithinPrescribedPremiumTime().valueAsMinutes();
						if(withinDomain.getWithinStatutoryMidNightTime() != null){
							TimeDivergenceWithCalculation winthinTime = withinDomain.getWithinStatutoryMidNightTime().getTime();
							/*所定内深夜時間*/
						    this.prsIncldMidnTime = winthinTime == null || winthinTime.getTime() == null ? 0 
						         : withinDomain.getWithinStatutoryMidNightTime().getTime().getTime().valueAsMinutes();
						    /*計算所定内深夜時間*/
						    this.calcPrsIncldMidnTime = winthinTime == null || winthinTime.getCalcTime() == null ? 0 
						         : withinDomain.getWithinStatutoryMidNightTime().getTime().getCalcTime().valueAsMinutes();
							/*所定内深夜乖離時間*/
							this.divPrsIncldMidnTime = winthinTime == null || winthinTime.getDivergenceTime() == null ? 0
									: withinDomain.getWithinStatutoryMidNightTime().getTime().getDivergenceTime().valueAsMinutes();
						}
						/*休暇加算時間*/
						this.vactnAddTime = withinDomain.getVacationAddTime() == null ? 0 : withinDomain.getVacationAddTime().valueAsMinutes();
					}
					/*----------------------日別実績の所定内時間------------------------------*/
					
					/*----------------------日別実績の残業時間------------------------------*/
					this.overTime1  = 0;
					this.overTime2  = 0;
					this.overTime3  = 0;
					this.overTime4  = 0;
					this.overTime5  = 0;
					this.overTime6  = 0;
					this.overTime7  = 0;
					this.overTime8  = 0;
					this.overTime9  = 0;
					this.overTime10 = 0;
					//振替時間
					this.transOverTime1 = 0;
					this.transOverTime2 = 0;
					this.transOverTime3 = 0;
					this.transOverTime4 = 0;
					this.transOverTime5 = 0;
					this.transOverTime6 = 0;
					this.transOverTime7 = 0;
					this.transOverTime8 = 0;
					this.transOverTime9 = 0;
					this.transOverTime10 = 0;
					//計算残業時間
					this.calcOverTime1 = 0;
					this.calcOverTime2 = 0;
					this.calcOverTime3 = 0;
					this.calcOverTime4 = 0;
					this.calcOverTime5 = 0;
					this.calcOverTime6 = 0;
					this.calcOverTime7 = 0;
					this.calcOverTime8 = 0;
					this.calcOverTime9 = 0;
					this.calcOverTime10= 0;
					//計算振替時間
					this.calcTransOverTime1 = 0;
					this.calcTransOverTime2 = 0;
					this.calcTransOverTime3 = 0;
					this.calcTransOverTime4 = 0;
					this.calcTransOverTime5 = 0;
					this.calcTransOverTime6 = 0;
					this.calcTransOverTime7 = 0;
					this.calcTransOverTime8 = 0;
					this.calcTransOverTime9 = 0;
					this.calcTransOverTime10 = 0;
					//事前残業申請
					this.preOverTimeAppTime1 = 0;
					this.preOverTimeAppTime2 = 0;
					this.preOverTimeAppTime3 = 0;
					this.preOverTimeAppTime4 = 0;
					this.preOverTimeAppTime5 = 0;
					this.preOverTimeAppTime6 = 0;
					this.preOverTimeAppTime7 = 0;
					this.preOverTimeAppTime8 = 0;
					this.preOverTimeAppTime9 = 0;
					this.preOverTimeAppTime10 = 0;
					//残業乖離時間
					this.divOverTime1  = 0;
					this.divOverTime2  = 0;
					this.divOverTime3  = 0;
					this.divOverTime4  = 0;
					this.divOverTime5  = 0;
					this.divOverTime6  = 0;
					this.divOverTime7  = 0;
					this.divOverTime8  = 0;
					this.divOverTime9  = 0;
					this.divOverTime10 = 0;
					//振替乖離時間
					this.divTransOverTime1 = 0;
					this.divTransOverTime2 = 0;
					this.divTransOverTime3 = 0;
					this.divTransOverTime4 = 0;
					this.divTransOverTime5 = 0;
					this.divTransOverTime6 = 0;
					this.divTransOverTime7 = 0;
					this.divTransOverTime8 = 0;
					this.divTransOverTime9 = 0;
					this.divTransOverTime10 = 0;
					
					this.ileglMidntOverTime = 0;
					//計算法定外
					this.calcIleglMidNOverTime = 0;
					//法定外残業深夜乖離時間
					this.divIleglMidnOverTime = 0;
					
					//拘束時間
					this.overTimeBindTime = 0;
					//変形法定内残業
					this.deformLeglOverTime = 0;
					//フレックス時間
					this.flexTime = 0;
					//計算フレックス時間
					this.calcFlexTime = 0;
					//事前フレックス時間
					this.preAppFlexTime = 0;
					//フレックス乖離時間
					this.divergenceFlexTime = 0;
					
					if(totalWork != null
						&& totalWork.getExcessOfStatutoryTimeOfDaily() != null
						&& totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()) {
						//深夜時間
						if(totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getExcessOverTimeWorkMidNightTime().isPresent()) {
							
							Finally<ExcessOverTimeWorkMidNightTime> excessOver = totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getExcessOverTimeWorkMidNightTime();
							//法定外
							this.ileglMidntOverTime = excessOver.get().getTime().getTime() == null ? 0 : excessOver.get().getTime().getTime().valueAsMinutes();
							//計算法定外
							this.calcIleglMidNOverTime = excessOver.get().getTime().getCalcTime() == null ? 0 : excessOver.get().getTime().getCalcTime().valueAsMinutes();
							//法定外残業深夜乖離時間
							this.divIleglMidnOverTime = excessOver.get().getTime().getDivergenceTime() == null ? 0 : excessOver.get().getTime().getDivergenceTime().valueAsMinutes();
						}
							
						//拘束時間
						this.overTimeBindTime = totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getExcessOverTimeWorkMidNightTime() == null ? 0 : totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getOverTimeWorkSpentAtWork().valueAsMinutes();
						//変形法定内残業
						this.deformLeglOverTime = totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getIrregularWithinPrescribedOverTimeWork() == null ? 0 : totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getIrregularWithinPrescribedOverTimeWork().valueAsMinutes();
						
						if(totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime() != null) {
							//フレックス時間
							this.flexTime = totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime().getFlexTime().getTime().valueAsMinutes();
							//計算フレックス時間
							this.calcFlexTime = totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime().getFlexTime().getCalcTime().valueAsMinutes();
							//事前フレックス時間
							this.preAppFlexTime = totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime().getBeforeApplicationTime() == null ? 0 :totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime().getBeforeApplicationTime().valueAsMinutes();
							//フレックス乖離時間
							this.divergenceFlexTime = totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime().getFlexTime().getDivergenceTime().valueAsMinutes();
						}
						if(!totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getOverTimeWorkFrameTime().isEmpty()) {
							//残業枠時間
							val overTimeOfDaily = totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get();
							if(overTimeOfDaily.getOverTimeWorkFrameTime() != null
									&& !overTimeOfDaily.getOverTimeWorkFrameTime().isEmpty()) {
								OverTimeFrameTime frame1 = getOverTimeFrame(overTimeOfDaily.getOverTimeWorkFrameTime(), 1);
								OverTimeFrameTime frame2 = getOverTimeFrame(overTimeOfDaily.getOverTimeWorkFrameTime(), 2);
								OverTimeFrameTime frame3 = getOverTimeFrame(overTimeOfDaily.getOverTimeWorkFrameTime(), 3);
								OverTimeFrameTime frame4 = getOverTimeFrame(overTimeOfDaily.getOverTimeWorkFrameTime(), 4);
								OverTimeFrameTime frame5 = getOverTimeFrame(overTimeOfDaily.getOverTimeWorkFrameTime(), 5);
								OverTimeFrameTime frame6 = getOverTimeFrame(overTimeOfDaily.getOverTimeWorkFrameTime(), 6);
								OverTimeFrameTime frame7 = getOverTimeFrame(overTimeOfDaily.getOverTimeWorkFrameTime(), 7);
								OverTimeFrameTime frame8 = getOverTimeFrame(overTimeOfDaily.getOverTimeWorkFrameTime(), 8);
								OverTimeFrameTime frame9 = getOverTimeFrame(overTimeOfDaily.getOverTimeWorkFrameTime(), 9);
								OverTimeFrameTime frame10 = getOverTimeFrame(overTimeOfDaily.getOverTimeWorkFrameTime(), 10);
								
								//残業時間
								this.overTime1  = frame1.getOverTimeWork() == null || frame1.getOverTimeWork().getTime() == null ? 0 : frame1.getOverTimeWork().getTime().valueAsMinutes();
								this.overTime2  = frame2.getOverTimeWork() == null || frame2.getOverTimeWork().getTime() == null ? 0 : frame2.getOverTimeWork().getTime().valueAsMinutes();
								this.overTime3  = frame3.getOverTimeWork() == null || frame3.getOverTimeWork().getTime() == null ? 0 : frame3.getOverTimeWork().getTime().valueAsMinutes();
								this.overTime4  = frame4.getOverTimeWork() == null || frame4.getOverTimeWork().getTime() == null ? 0 : frame4.getOverTimeWork().getTime().valueAsMinutes();
								this.overTime5  = frame5.getOverTimeWork() == null || frame5.getOverTimeWork().getTime() == null ? 0 : frame5.getOverTimeWork().getTime().valueAsMinutes();
								this.overTime6  = frame6.getOverTimeWork() == null || frame6.getOverTimeWork().getTime() == null ? 0 : frame6.getOverTimeWork().getTime().valueAsMinutes();
								this.overTime7  = frame7.getOverTimeWork() == null || frame7.getOverTimeWork().getTime() == null ? 0 : frame7.getOverTimeWork().getTime().valueAsMinutes();
								this.overTime8  = frame8.getOverTimeWork() == null || frame8.getOverTimeWork().getTime() == null ? 0 : frame8.getOverTimeWork().getTime().valueAsMinutes();
								this.overTime9  = frame9.getOverTimeWork() == null || frame9.getOverTimeWork().getTime() == null ? 0 : frame9.getOverTimeWork().getTime().valueAsMinutes();
								this.overTime10 = frame10.getOverTimeWork() == null || frame10.getOverTimeWork().getTime() == null ? 0 : frame10.getOverTimeWork().getTime().valueAsMinutes();
								//振替時間
								this.transOverTime1 = frame1.getTransferTime() == null || frame1.getTransferTime().getTime() == null ? 0 : frame1.getTransferTime().getTime().valueAsMinutes();
								this.transOverTime2 = frame2.getTransferTime() == null || frame2.getTransferTime().getTime() == null ? 0 : frame2.getTransferTime().getTime().valueAsMinutes();
								this.transOverTime3 = frame3.getTransferTime() == null || frame3.getTransferTime().getTime() == null ? 0 : frame3.getTransferTime().getTime().valueAsMinutes();
								this.transOverTime4 = frame4.getTransferTime() == null || frame4.getTransferTime().getTime() == null ? 0 : frame4.getTransferTime().getTime().valueAsMinutes();
								this.transOverTime5 = frame5.getTransferTime() == null || frame5.getTransferTime().getTime() == null ? 0 : frame5.getTransferTime().getTime().valueAsMinutes();
								this.transOverTime6 = frame6.getTransferTime() == null || frame6.getTransferTime().getTime() == null ? 0 : frame6.getTransferTime().getTime().valueAsMinutes();
								this.transOverTime7 = frame7.getTransferTime() == null || frame7.getTransferTime().getTime() == null ? 0 : frame7.getTransferTime().getTime().valueAsMinutes();
								this.transOverTime8 = frame8.getTransferTime() == null || frame8.getTransferTime().getTime() == null ? 0 : frame8.getTransferTime().getTime().valueAsMinutes();
								this.transOverTime9 = frame9.getTransferTime() == null || frame9.getTransferTime().getTime() == null ? 0 : frame9.getTransferTime().getTime().valueAsMinutes();
								this.transOverTime10 = frame10.getTransferTime() == null || frame10.getTransferTime().getTime() == null ? 0 : frame10.getTransferTime().getTime().valueAsMinutes();
								//計算残業時間
								this.calcOverTime1 = frame1.getOverTimeWork() == null || frame1.getOverTimeWork().getCalcTime() == null ? 0 : frame1.getOverTimeWork().getCalcTime().valueAsMinutes();
								this.calcOverTime2 = frame2.getOverTimeWork() == null || frame2.getOverTimeWork().getCalcTime() == null ? 0 : frame2.getOverTimeWork().getCalcTime().valueAsMinutes();
								this.calcOverTime3 = frame3.getOverTimeWork() == null || frame3.getOverTimeWork().getCalcTime() == null ? 0 : frame3.getOverTimeWork().getCalcTime().valueAsMinutes();
								this.calcOverTime4 = frame4.getOverTimeWork() == null || frame4.getOverTimeWork().getCalcTime() == null ? 0 : frame4.getOverTimeWork().getCalcTime().valueAsMinutes();
								this.calcOverTime5 = frame5.getOverTimeWork() == null || frame5.getOverTimeWork().getCalcTime() == null ? 0 : frame5.getOverTimeWork().getCalcTime().valueAsMinutes();
								this.calcOverTime6 = frame6.getOverTimeWork() == null || frame6.getOverTimeWork().getCalcTime() == null ? 0 : frame6.getOverTimeWork().getCalcTime().valueAsMinutes();
								this.calcOverTime7 = frame7.getOverTimeWork() == null || frame7.getOverTimeWork().getCalcTime() == null ? 0 : frame7.getOverTimeWork().getCalcTime().valueAsMinutes();
								this.calcOverTime8 = frame8.getOverTimeWork() == null || frame8.getOverTimeWork().getCalcTime() == null ? 0 : frame8.getOverTimeWork().getCalcTime().valueAsMinutes();
								this.calcOverTime9 = frame9.getOverTimeWork() == null || frame9.getOverTimeWork().getCalcTime() == null ? 0 : frame9.getOverTimeWork().getCalcTime().valueAsMinutes();
								this.calcOverTime10= frame10.getOverTimeWork() == null || frame10.getOverTimeWork().getCalcTime() == null ? 0 : frame10.getOverTimeWork().getCalcTime().valueAsMinutes();
								//計算振替時間
								this.calcTransOverTime1 = frame1.getTransferTime() == null || frame1.getTransferTime().getCalcTime() == null ? 0 : frame1.getTransferTime().getCalcTime().valueAsMinutes();
								this.calcTransOverTime2 = frame2.getTransferTime() == null || frame2.getTransferTime().getCalcTime() == null ? 0 : frame2.getTransferTime().getCalcTime().valueAsMinutes();
								this.calcTransOverTime3 = frame3.getTransferTime() == null || frame3.getTransferTime().getCalcTime() == null ? 0 : frame3.getTransferTime().getCalcTime().valueAsMinutes();
								this.calcTransOverTime4 = frame4.getTransferTime() == null || frame4.getTransferTime().getCalcTime() == null ? 0 : frame4.getTransferTime().getCalcTime().valueAsMinutes();
								this.calcTransOverTime5 = frame5.getTransferTime() == null || frame5.getTransferTime().getCalcTime() == null ? 0 : frame5.getTransferTime().getCalcTime().valueAsMinutes();
								this.calcTransOverTime6 = frame6.getTransferTime() == null || frame6.getTransferTime().getCalcTime() == null ? 0 : frame6.getTransferTime().getCalcTime().valueAsMinutes();
								this.calcTransOverTime7 = frame7.getTransferTime() == null || frame7.getTransferTime().getCalcTime() == null ? 0 : frame7.getTransferTime().getCalcTime().valueAsMinutes();
								this.calcTransOverTime8 = frame8.getTransferTime() == null || frame8.getTransferTime().getCalcTime() == null ? 0 : frame8.getTransferTime().getCalcTime().valueAsMinutes();
								this.calcTransOverTime9 = frame9.getTransferTime() == null || frame9.getTransferTime().getCalcTime() == null ? 0 : frame9.getTransferTime().getCalcTime().valueAsMinutes();
								this.calcTransOverTime10 = frame10.getTransferTime() == null || frame10.getTransferTime().getCalcTime() == null ? 0 : frame10.getTransferTime().getCalcTime().valueAsMinutes();
								//事前残業申請
								this.preOverTimeAppTime1 = frame1.getBeforeApplicationTime() == null ? 0 : frame1.getBeforeApplicationTime().valueAsMinutes();
								this.preOverTimeAppTime2 = frame2.getBeforeApplicationTime() == null ? 0 : frame2.getBeforeApplicationTime().valueAsMinutes();
								this.preOverTimeAppTime3 = frame3.getBeforeApplicationTime() == null ? 0 : frame3.getBeforeApplicationTime().valueAsMinutes();
								this.preOverTimeAppTime4 = frame4.getBeforeApplicationTime() == null ? 0 : frame4.getBeforeApplicationTime().valueAsMinutes();
								this.preOverTimeAppTime5 = frame5.getBeforeApplicationTime() == null ? 0 : frame5.getBeforeApplicationTime().valueAsMinutes();
								this.preOverTimeAppTime6 = frame6.getBeforeApplicationTime() == null ? 0 : frame6.getBeforeApplicationTime().valueAsMinutes();
								this.preOverTimeAppTime7 = frame7.getBeforeApplicationTime() == null ? 0 : frame7.getBeforeApplicationTime().valueAsMinutes();
								this.preOverTimeAppTime8 = frame8.getBeforeApplicationTime() == null ? 0 : frame8.getBeforeApplicationTime().valueAsMinutes();
								this.preOverTimeAppTime9 = frame9.getBeforeApplicationTime() == null ? 0 : frame9.getBeforeApplicationTime().valueAsMinutes();
								this.preOverTimeAppTime10 = frame10.getBeforeApplicationTime() == null ? 0 : frame10.getBeforeApplicationTime().valueAsMinutes();
								//残業乖離時間
								this.divOverTime1  = frame1.getOverTimeWork() == null || frame1.getOverTimeWork().getTime() == null ? 0 : frame1.getOverTimeWork().getDivergenceTime().valueAsMinutes();
								this.divOverTime2  = frame2.getOverTimeWork() == null || frame2.getOverTimeWork().getTime() == null ? 0 : frame2.getOverTimeWork().getDivergenceTime().valueAsMinutes();
								this.divOverTime3  = frame3.getOverTimeWork() == null || frame3.getOverTimeWork().getTime() == null ? 0 : frame3.getOverTimeWork().getDivergenceTime().valueAsMinutes();
								this.divOverTime4  = frame4.getOverTimeWork() == null || frame4.getOverTimeWork().getTime() == null ? 0 : frame4.getOverTimeWork().getDivergenceTime().valueAsMinutes();
								this.divOverTime5  = frame5.getOverTimeWork() == null || frame5.getOverTimeWork().getTime() == null ? 0 : frame5.getOverTimeWork().getDivergenceTime().valueAsMinutes();
								this.divOverTime6  = frame6.getOverTimeWork() == null || frame6.getOverTimeWork().getTime() == null ? 0 : frame6.getOverTimeWork().getDivergenceTime().valueAsMinutes();
								this.divOverTime7  = frame7.getOverTimeWork() == null || frame7.getOverTimeWork().getTime() == null ? 0 : frame7.getOverTimeWork().getDivergenceTime().valueAsMinutes();
								this.divOverTime8  = frame8.getOverTimeWork() == null || frame8.getOverTimeWork().getTime() == null ? 0 : frame8.getOverTimeWork().getDivergenceTime().valueAsMinutes();
								this.divOverTime9  = frame9.getOverTimeWork() == null || frame9.getOverTimeWork().getTime() == null ? 0 : frame9.getOverTimeWork().getDivergenceTime().valueAsMinutes();
								this.divOverTime10 = frame10.getOverTimeWork() == null || frame10.getOverTimeWork().getTime() == null ? 0 : frame10.getOverTimeWork().getDivergenceTime().valueAsMinutes();
								//振替乖離時間
								this.divTransOverTime1 = frame1.getTransferTime() == null || frame1.getTransferTime().getTime() == null ? 0 : frame1.getTransferTime().getDivergenceTime().valueAsMinutes();
								this.divTransOverTime2 = frame2.getTransferTime() == null || frame2.getTransferTime().getTime() == null ? 0 : frame2.getTransferTime().getDivergenceTime().valueAsMinutes();
								this.divTransOverTime3 = frame3.getTransferTime() == null || frame3.getTransferTime().getTime() == null ? 0 : frame3.getTransferTime().getDivergenceTime().valueAsMinutes();
								this.divTransOverTime4 = frame4.getTransferTime() == null || frame4.getTransferTime().getTime() == null ? 0 : frame4.getTransferTime().getDivergenceTime().valueAsMinutes();
								this.divTransOverTime5 = frame5.getTransferTime() == null || frame5.getTransferTime().getTime() == null ? 0 : frame5.getTransferTime().getDivergenceTime().valueAsMinutes();
								this.divTransOverTime6 = frame6.getTransferTime() == null || frame6.getTransferTime().getTime() == null ? 0 : frame6.getTransferTime().getDivergenceTime().valueAsMinutes();
								this.divTransOverTime7 = frame7.getTransferTime() == null || frame7.getTransferTime().getTime() == null ? 0 : frame7.getTransferTime().getDivergenceTime().valueAsMinutes();
								this.divTransOverTime8 = frame8.getTransferTime() == null || frame8.getTransferTime().getTime() == null ? 0 : frame8.getTransferTime().getDivergenceTime().valueAsMinutes();
								this.divTransOverTime9 = frame9.getTransferTime() == null || frame9.getTransferTime().getTime() == null ? 0 : frame9.getTransferTime().getDivergenceTime().valueAsMinutes();
								this.divTransOverTime10 = frame10.getTransferTime() == null || frame10.getTransferTime().getTime() == null ? 0 : frame10.getTransferTime().getDivergenceTime().valueAsMinutes();
							}
						}
					}
					/*----------------------日別実績の残業時間------------------------------*/
					
					/*----------------------日別実績の残業時間帯------------------------------*/
					if(totalWork != null
							&& totalWork.getExcessOfStatutoryTimeOfDaily() != null
							&& totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()
							&& !totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getOverTimeWorkFrameTimeSheet().isEmpty()) {
						Optional<TimeSpanForCalc> span;
						allClearOverTimeTS();
						val overTimeSheet = totalWork.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getOverTimeWorkFrameTimeSheet();
						span = getTimeSpan(overTimeSheet, 1);
						span.ifPresent( tc -> {
							this.overTime1StrClc = tc == null ? 0 : tc.startValue();
							this.overTime1EndClc = tc == null ? 0 : tc.endValue();
						});

						
						span = getTimeSpan(overTimeSheet, 2);
						span.ifPresent( tc -> {
							this.overTime2StrClc = tc == null ? 0 : tc.startValue();
							this.overTime2EndClc = tc == null ? 0 : tc.endValue();
						});

						span = getTimeSpan(overTimeSheet, 3);
						span.ifPresent( tc -> {
							this.overTime3StrClc = tc == null ? 0 : tc.startValue();
							this.overTime3EndClc = tc == null ? 0 : tc.endValue();
						});
				 
						span = getTimeSpan(overTimeSheet, 4);
						span.ifPresent( tc -> {
							this.overTime4StrClc = tc == null ? 0 : tc.startValue();
							this.overTime4EndClc = tc == null ? 0 : tc.endValue();
						});
				 
						span = getTimeSpan(overTimeSheet, 5);
						span.ifPresent( tc -> {
							this.overTime5StrClc = tc == null ? 0 : tc.startValue();
							this.overTime5EndClc = tc == null ? 0 : tc.endValue();
						});
				 
						span = getTimeSpan(overTimeSheet, 6);
						span.ifPresent( tc -> {
							this.overTime6StrClc = tc == null ? 0 : tc.startValue();
							this.overTime6EndClc = tc == null ? 0 : tc.endValue();
						});
				 
						span = getTimeSpan(overTimeSheet, 7);
						span.ifPresent( tc -> {
							this.overTime7StrClc = tc == null ? 0 : tc.startValue();
							this.overTime7EndClc = tc == null ? 0 : tc.endValue();
						});
				 
						span = getTimeSpan(overTimeSheet, 8);
						span.ifPresent( tc -> {
							this.overTime8StrClc = tc == null ? 0 : tc.startValue();
							this.overTime8EndClc = tc == null ? 0 : tc.endValue();
						});
				 
						span = getTimeSpan(overTimeSheet, 9);
						span.ifPresent( tc -> {
							this.overTime9StrClc = tc == null ? 0 : tc.startValue();
							this.overTime9EndClc = tc == null ? 0 : tc.endValue();
						});
				 
						span = getTimeSpan(overTimeSheet, 10);
						span.ifPresent( tc -> {
							this.overTime10StrClc = tc == null ? 0 : tc.startValue();
							this.overTime10EndClc = tc == null ? 0 : tc.endValue();
						});
					}
					/*----------------------日別実績の残業時間帯------------------------------*/
					
				}
			}
		}
	}
	
	private void setValue(Optional<DivergenceTime> frame,int number) {
		if(frame.isPresent()) {
			setPerDivergenceTimeData("divergenceTime",number,frame.get().getDivTime() == null ? 0 : frame.get().getDivTime().valueAsMinutes());
			setPerDivergenceTimeData("deductionTime",number,frame.get().getDeductionTime() == null ? 0 : frame.get().getDeductionTime().valueAsMinutes());
			setPerDivergenceTimeData("afterDeductionTime",number,frame.get().getDivTimeAfterDeduction() == null ? 0 : frame.get().getDivTimeAfterDeduction().valueAsMinutes());
			setPerDivergenceTimeData("reasonCode",number,frame.get().getDivResonCode() == null ? "" : frame.get().getDivResonCode().toString());
			setPerDivergenceTimeData("reason",number,frame.get().getDivReason() == null ? "" : frame.get().getDivReason().toString());
		}
		else {
			setPerDivergenceTimeData("divergenceTime",number,0);
			setPerDivergenceTimeData("deductionTime",number,0);
			setPerDivergenceTimeData("afterDeductionTime",number,0);
			setPerDivergenceTimeData("reasonCode",number,"");
			setPerDivergenceTimeData("reason",number,"");
		}
	}
	
	
	private void setPerDivergenceTimeData(String fieldName ,int number ,Object value) {
		//自分自身の値セット先(フィールド)取得
		Field field = FieldReflection.getField(this.getClass(), fieldName + number);
		//値セット
		FieldReflection.setField(field, this, value);
	}
	
	private Optional<DivergenceTime> getDivergenceTime(DivergenceTimeOfDaily domain,int number){
		return domain.getDivergenceTime().stream().filter(tc -> tc.getDivTimeId() == number).findFirst();
	}
	
	private HolidayWorkFrameTime getHolidayTimeFrame(List<HolidayWorkFrameTime> frames, int frameNo) {
		
		val getFrame = frames.stream().filter(tc -> tc.getHolidayFrameNo().v() == frameNo).findFirst();
		if(getFrame.isPresent()) {
			return getFrame.get();
		}
		else {
			return new HolidayWorkFrameTime(new HolidayWorkFrameNo(frameNo), 
											Finally.of(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0))), 
											Finally.of(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0))), 
											Finally.of(new AttendanceTime(0)));
					
		}
	}
	
	private OverTimeFrameTime getOverTimeFrame(List<OverTimeFrameTime> overTimeFrame, int frameNo) {
		val getFrame = overTimeFrame.stream().filter(tc -> tc.getOverWorkFrameNo().v() == frameNo).findFirst();
		if(getFrame.isPresent()) {
			return getFrame.get();
		}
		else {
			return new OverTimeFrameTime(new OverTimeFrameNo(frameNo),
										 TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
										 TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
										 new AttendanceTime(0),
										 new AttendanceTime(0));
		}
			
	}
	
	private Optional<TimeSpanForCalc> getTimeSpan(List<OverTimeFrameTimeSheet> overTimeSheet, int sheetNo) {
		 return decisionConnectSpan(overTimeSheet.stream()
 				 								 .filter(tc -> tc.getFrameNo().v().intValue() == sheetNo)
 				 								 .map(tc -> tc.getTimeSpan())
 				 								 .collect(Collectors.toList()));
	}

	private void allClearOverTimeTS() {
		this.overTime1StrClc = 0;
		this.overTime1EndClc = 0;
		this.overTime2StrClc = 0;
		this.overTime2EndClc = 0;
		this.overTime3StrClc = 0;
		this.overTime3EndClc = 0;
		this.overTime4StrClc = 0;
		this.overTime4EndClc = 0;
		this.overTime5StrClc = 0;
		this.overTime5EndClc = 0;
		this.overTime6StrClc = 0;
		this.overTime6EndClc = 0;
		this.overTime7StrClc = 0;
		this.overTime7EndClc = 0;
		this.overTime8StrClc = 0;
		this.overTime8EndClc = 0;
		this.overTime9StrClc = 0;
		this.overTime9EndClc = 0;
		this.overTime10StrClc = 0;
		this.overTime10EndClc = 0;
	}
	private void allClearHolidayWorkTS() {
		this.holiWork1StrClc = 0;
		this.holiWork1EndClc = 0;
		this.holiWork2StrClc = 0;
		this.holiWork2EndClc = 0;
		this.holiWork3StrClc = 0;
		this.holiWork3EndClc = 0;
		this.holiWork4StrClc = 0;
		this.holiWork4EndClc = 0;
		this.holiWork5StrClc = 0;
		this.holiWork5EndClc = 0;
		this.holiWork6StrClc = 0;
		this.holiWork6EndClc = 0;
		this.holiWork7StrClc = 0;
		this.holiWork7EndClc = 0;
		this.holiWork8StrClc = 0;
		this.holiWork8EndClc = 0;
		this.holiWork9StrClc = 0;
		this.holiWork9EndClc = 0;
		this.holiWork10StrClc = 0;
		this.holiWork10EndClc = 0;
	}
	
	private Optional<TimeSpanForCalc> decisionConnectSpan(List<TimeSpanForCalc> collect) {
		if(collect.isEmpty()) return Optional.empty();
		if(collect.size() == 1) return Optional.of(collect.get(0));
		return Optional.of(createConnectSpan(collect));
	}
	
	public TimeSpanForCalc createConnectSpan(List<TimeSpanForCalc> collect) {
		TimeSpanForCalc connectSpan = collect.get(0);
		for(TimeSpanForCalc nowTimeSpan : collect) {
			if(!connectSpan.equals(nowTimeSpan) && connectSpan.endValue().intValue() == nowTimeSpan.getStart().valueAsMinutes()) {
				connectSpan = new TimeSpanForCalc(connectSpan.getStart(), nowTimeSpan.getEnd());
			}
		}
		return connectSpan;
	}
	
	private Optional<HolidayWorkFrameTimeSheet> getTimeSheet(List<HolidayWorkFrameTimeSheet> domain, int sheetNo) {
		return domain.stream()
					 .filter(tc -> tc.getHolidayWorkTimeSheetNo().v().intValue() == sheetNo)
					 .findFirst();
	}
	
	private Optional<HolidayWorkMidNightTime> getHolidayMidNightWork(HolidayMidnightWork domain, StaturoryAtrOfHolidayWork statutoryAttr) {
		return domain.getHolidayWorkMidNightTime().stream().filter(tc -> tc.getStatutoryAtr().equals(statutoryAttr)).findFirst();
	}

	
	public AttendanceTimeOfDailyPerformance toDomain() {
		/*日別実績の休憩時間*/
		val breakTime = new BreakTimeOfDaily(DeductionTotalTime.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.toRecordTotalTime), new AttendanceTime(this.calToRecordTotalTime)),
				  			                       TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.toRecordInTime), new AttendanceTime(this.calToRecordInTime)),
				  								   TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.toRecordOutTime), new AttendanceTime(this.calToRecordOutTime))), 
							DeductionTotalTime.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.deductionTotalTime), new AttendanceTime(this.calDeductionTotalTime)),
												  TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.deductionInTime), new AttendanceTime(this.calDeductionInTime)),
												  TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.deductionOutTime), new AttendanceTime(this.calDeductionOutTime))), 
							new BreakTimeGoOutTimes(this.count), 
							new AttendanceTime(this.duringworkTime), 
							Collections.emptyList());
		/*日別実績の休出時間*/
		List<HolidayWorkFrameTime> holiWorkFrameTimeList = new ArrayList<>();
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(1),
					Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime1),new AttendanceTime(this.calcHoliWorkTime1))),
					Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime1),new AttendanceTime(this.calcTransTime1))),
					Finally.of(new AttendanceTime(this.preAppTime1))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(2),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime2),new AttendanceTime(this.calcHoliWorkTime2))),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime2),new AttendanceTime(this.calcTransTime2))),
				Finally.of(new AttendanceTime(this.preAppTime2))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(3),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime3),new AttendanceTime(this.calcHoliWorkTime3))),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime3),new AttendanceTime(this.calcTransTime3))),
				Finally.of(new AttendanceTime(this.preAppTime3))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(4),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime4),new AttendanceTime(this.calcHoliWorkTime4))),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime4),new AttendanceTime(this.calcTransTime4))),
				Finally.of(new AttendanceTime(this.preAppTime4))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(5),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime5),new AttendanceTime(this.calcHoliWorkTime5))),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime5),new AttendanceTime(this.calcTransTime5))),
				Finally.of(new AttendanceTime(this.preAppTime5))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(6),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime6),new AttendanceTime(this.calcHoliWorkTime6))),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime6),new AttendanceTime(this.calcTransTime6))),
				Finally.of(new AttendanceTime(this.preAppTime6))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(7),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime7),new AttendanceTime(this.calcHoliWorkTime7))),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime7),new AttendanceTime(this.calcTransTime7))),
				Finally.of(new AttendanceTime(this.preAppTime7))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(8),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime8),new AttendanceTime(this.calcHoliWorkTime8))),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime8),new AttendanceTime(this.calcTransTime8))),
				Finally.of(new AttendanceTime(this.preAppTime8))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(9),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime9),new AttendanceTime(this.calcHoliWorkTime9))),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime9),new AttendanceTime(this.calcTransTime9))),
				Finally.of(new AttendanceTime(this.preAppTime9))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(10),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime10),new AttendanceTime(this.calcHoliWorkTime10))),
				Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime10),new AttendanceTime(this.calcTransTime10))),
				Finally.of(new AttendanceTime(this.preAppTime10))));
							
		List<HolidayWorkMidNightTime> holidayWorkMidNightTimeList = new ArrayList<>();
		holidayWorkMidNightTimeList.add(new HolidayWorkMidNightTime(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.legHoliWorkMidn),new AttendanceTime(this.calcLegHoliWorkMidn)),
				StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork));
		holidayWorkMidNightTimeList.add(new HolidayWorkMidNightTime(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.illegHoliWorkMidn),new AttendanceTime(this.calcIllegHoliWorkMidn)),
				StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork));
		holidayWorkMidNightTimeList.add(new HolidayWorkMidNightTime(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.pbHoliWorkMidn),new AttendanceTime(this.calcPbHoliWorkMidn)),
				StaturoryAtrOfHolidayWork.PublicHolidayWork));
		
		
		/*日別実績の休出時間帯*/
		List<HolidayWorkFrameTimeSheet> holidayWOrkTimeTS = new ArrayList<>();
		holidayWOrkTimeTS.add(new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(Integer.valueOf(1)),new TimeSpanForCalc(new TimeWithDayAttr(this.holiWork1StrClc),new TimeWithDayAttr(this.holiWork1EndClc))));
		holidayWOrkTimeTS.add(new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(Integer.valueOf(2)),new TimeSpanForCalc(new TimeWithDayAttr(this.holiWork2StrClc),new TimeWithDayAttr(this.holiWork2EndClc))));
		holidayWOrkTimeTS.add(new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(Integer.valueOf(3)),new TimeSpanForCalc(new TimeWithDayAttr(this.holiWork3StrClc),new TimeWithDayAttr(this.holiWork3EndClc))));
		holidayWOrkTimeTS.add(new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(Integer.valueOf(4)),new TimeSpanForCalc(new TimeWithDayAttr(this.holiWork4StrClc),new TimeWithDayAttr(this.holiWork4EndClc))));
		holidayWOrkTimeTS.add(new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(Integer.valueOf(5)),new TimeSpanForCalc(new TimeWithDayAttr(this.holiWork5StrClc),new TimeWithDayAttr(this.holiWork5EndClc))));
		holidayWOrkTimeTS.add(new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(Integer.valueOf(6)),new TimeSpanForCalc(new TimeWithDayAttr(this.holiWork6StrClc),new TimeWithDayAttr(this.holiWork6EndClc))));
		holidayWOrkTimeTS.add(new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(Integer.valueOf(7)),new TimeSpanForCalc(new TimeWithDayAttr(this.holiWork7StrClc),new TimeWithDayAttr(this.holiWork7EndClc))));
		holidayWOrkTimeTS.add(new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(Integer.valueOf(8)),new TimeSpanForCalc(new TimeWithDayAttr(this.holiWork8StrClc),new TimeWithDayAttr(this.holiWork8EndClc))));
		holidayWOrkTimeTS.add(new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(Integer.valueOf(9)),new TimeSpanForCalc(new TimeWithDayAttr(this.holiWork9StrClc),new TimeWithDayAttr(this.holiWork9EndClc))));
		holidayWOrkTimeTS.add(new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(Integer.valueOf(10)),new TimeSpanForCalc(new TimeWithDayAttr(this.holiWork10StrClc),new TimeWithDayAttr(this.holiWork10EndClc))));
		
		val holidayWork = new HolidayWorkTimeOfDaily(holidayWOrkTimeTS,holiWorkFrameTimeList,Finally.of(new HolidayMidnightWork(holidayWorkMidNightTimeList)), new AttendanceTime(this.holiWorkBindTime));
		
		/*日別実績の休暇*/
		val vacation =  new HolidayOfDaily(new AbsenceOfDaily(new AttendanceTime(absenceTime)),
				  new TimeDigestOfDaily(new AttendanceTime(tdvTime),new AttendanceTime(tdvShortageTime)),
				  new YearlyReservedOfDaily(new AttendanceTime(retentionYearlyTime)),
				  new SubstituteHolidayOfDaily(new AttendanceTime(compensatoryLeaveTime),new AttendanceTime(compensatoryLeaveTdvTime)),
				  new OverSalaryOfDaily(new AttendanceTime(excessSalaryiesTime),new AttendanceTime(excessSalaryiesTdvTime)),
				  new SpecialHolidayOfDaily(new AttendanceTime(specialHolidayTime),new AttendanceTime(specialHolidayTdvTime)),
				  new AnnualOfDaily(new AttendanceTime(annualleaveTime), new AttendanceTime(annualleaveTdvTime))
				 );
		/*日別実績の予定時間*/
		val shedule =  new WorkScheduleTimeOfDaily(new WorkScheduleTime(new AttendanceTime(this.workScheduleTime), new AttendanceTime(0), new AttendanceTime(0)),
				   								   new AttendanceTime(this.schedulePreLaborTime),
				   								   new AttendanceTime(this.recorePreLaborTime));
		/*日別実績の所定内時間*/
		val within =  WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(new AttendanceTime(this.workTime),
				   																  new AttendanceTime(this.pefomWorkTime),
				   																  new AttendanceTime(this.prsIncldPrmimTime),
				   																  new WithinStatutoryMidNightTime(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.prsIncldMidnTime),
				   																		  																				  new AttendanceTime(this.calcPrsIncldMidnTime))),
				   																  new AttendanceTime(this.vactnAddTime));
		/*日別実績の残業時間*/
		List<OverTimeFrameTime> list = new ArrayList<>();
		list.add(new OverTimeFrameTime(new OverTimeFrameNo(1),
									   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.overTime1), new AttendanceTime(this.calcOverTime1)),
									   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transOverTime1), new AttendanceTime(this.calcTransOverTime1)),
									   new AttendanceTime(this.preOverTimeAppTime1),
									   new AttendanceTime(0)));
		
		list.add(new OverTimeFrameTime(new OverTimeFrameNo(2),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.overTime2), new AttendanceTime(this.calcOverTime2)),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transOverTime2), new AttendanceTime(this.calcTransOverTime2)),
				   new AttendanceTime(this.preOverTimeAppTime2),
				   new AttendanceTime(0)));
		
		list.add(new OverTimeFrameTime(new OverTimeFrameNo(3),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.overTime3), new AttendanceTime(this.calcOverTime3)),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transOverTime3), new AttendanceTime(this.calcTransOverTime3)),
				   new AttendanceTime(this.preOverTimeAppTime3),
				   new AttendanceTime(0)));
		
		list.add(new OverTimeFrameTime(new OverTimeFrameNo(4),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.overTime4), new AttendanceTime(this.calcOverTime4)),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transOverTime4), new AttendanceTime(this.calcTransOverTime4)),
				   new AttendanceTime(this.preOverTimeAppTime4),
				   new AttendanceTime(0)));
		
		list.add(new OverTimeFrameTime(new OverTimeFrameNo(5),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.overTime5), new AttendanceTime(this.calcOverTime5)),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transOverTime5), new AttendanceTime(this.calcTransOverTime5)),
				   new AttendanceTime(this.preOverTimeAppTime5),
				   new AttendanceTime(0)));
		
		list.add(new OverTimeFrameTime(new OverTimeFrameNo(6),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.overTime6), new AttendanceTime(this.calcOverTime6)),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transOverTime6), new AttendanceTime(this.calcTransOverTime6)),
				   new AttendanceTime(this.preOverTimeAppTime6),
				   new AttendanceTime(0)));
		
		list.add(new OverTimeFrameTime(new OverTimeFrameNo(7),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.overTime7), new AttendanceTime(this.calcOverTime7)),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transOverTime7), new AttendanceTime(this.calcTransOverTime7)),
				   new AttendanceTime(this.preOverTimeAppTime7),
				   new AttendanceTime(0)));
		
		list.add(new OverTimeFrameTime(new OverTimeFrameNo(8),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.overTime8), new AttendanceTime(this.calcOverTime8)),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transOverTime8), new AttendanceTime(this.calcTransOverTime8)),
				   new AttendanceTime(this.preOverTimeAppTime8),
				   new AttendanceTime(0)));
		
		list.add(new OverTimeFrameTime(new OverTimeFrameNo(9),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.overTime9), new AttendanceTime(this.calcOverTime9)),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transOverTime9), new AttendanceTime(this.calcTransOverTime9)),
				   new AttendanceTime(this.preOverTimeAppTime9),
				   new AttendanceTime(0)));
		
		list.add(new OverTimeFrameTime(new OverTimeFrameNo(10),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.overTime10), new AttendanceTime(this.calcOverTime10)),
				   TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transOverTime10), new AttendanceTime(this.calcTransOverTime10)),
				   new AttendanceTime(this.preOverTimeAppTime10),
				   new AttendanceTime(0)));
		

		/*日別実績の残業時間帯*/
		List<OverTimeFrameTimeSheet> timeSheet = new ArrayList<>();
		timeSheet.add(new OverTimeFrameTimeSheet(new TimeSpanForCalc(new TimeWithDayAttr(this.overTime1StrClc),new TimeWithDayAttr(this.overTime1EndClc)),new OverTimeFrameNo(1)));
		timeSheet.add(new OverTimeFrameTimeSheet(new TimeSpanForCalc(new TimeWithDayAttr(this.overTime2StrClc),new TimeWithDayAttr(this.overTime2EndClc)),new OverTimeFrameNo(2)));
		timeSheet.add(new OverTimeFrameTimeSheet(new TimeSpanForCalc(new TimeWithDayAttr(this.overTime3StrClc),new TimeWithDayAttr(this.overTime3EndClc)),new OverTimeFrameNo(3)));
		timeSheet.add(new OverTimeFrameTimeSheet(new TimeSpanForCalc(new TimeWithDayAttr(this.overTime4StrClc),new TimeWithDayAttr(this.overTime4EndClc)),new OverTimeFrameNo(4)));
		timeSheet.add(new OverTimeFrameTimeSheet(new TimeSpanForCalc(new TimeWithDayAttr(this.overTime5StrClc),new TimeWithDayAttr(this.overTime5EndClc)),new OverTimeFrameNo(5)));
		timeSheet.add(new OverTimeFrameTimeSheet(new TimeSpanForCalc(new TimeWithDayAttr(this.overTime6StrClc),new TimeWithDayAttr(this.overTime6EndClc)),new OverTimeFrameNo(6)));
		timeSheet.add(new OverTimeFrameTimeSheet(new TimeSpanForCalc(new TimeWithDayAttr(this.overTime7StrClc),new TimeWithDayAttr(this.overTime7EndClc)),new OverTimeFrameNo(7)));
		timeSheet.add(new OverTimeFrameTimeSheet(new TimeSpanForCalc(new TimeWithDayAttr(this.overTime8StrClc),new TimeWithDayAttr(this.overTime8EndClc)),new OverTimeFrameNo(8)));
		timeSheet.add(new OverTimeFrameTimeSheet(new TimeSpanForCalc(new TimeWithDayAttr(this.overTime9StrClc),new TimeWithDayAttr(this.overTime9EndClc)),new OverTimeFrameNo(9)));
		timeSheet.add(new OverTimeFrameTimeSheet(new TimeSpanForCalc(new TimeWithDayAttr(this.overTime10StrClc),new TimeWithDayAttr(this.overTime10EndClc)),new OverTimeFrameNo(10)));
		
		val overTime = new OverTimeOfDaily(timeSheet, 
				   						   list,
				   						   Finally.of(new ExcessOverTimeWorkMidNightTime(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.ileglMidntOverTime),new AttendanceTime(this.calcIleglMidNOverTime)))),
				   						   new AttendanceTime(this.deformLeglOverTime),
				   						   new FlexTime(TimeDivergenceWithCalculationMinusExist.createTimeWithCalculation(new AttendanceTimeOfExistMinus(this.flexTime), new AttendanceTimeOfExistMinus(this.calcFlexTime)),new AttendanceTime(this.preAppFlexTime)),
				   						   new AttendanceTime(this.overTimeBindTime)
				   						   );
		/*日別実績の乖離時間*/
		List<DivergenceTime> divergenceTimeList = new ArrayList<>();
		divergenceTimeList.add(new DivergenceTime(new AttendanceTime(this.afterDeductionTime1),
												  new AttendanceTime(this.deductionTime1),
												  new AttendanceTime(this.divergenceTime1),
												  1,
												  new DivergenceReasonContent(this.reason1),
												  new DiverdenceReasonCode(this.reasonCode1)));
		
		divergenceTimeList.add(new DivergenceTime(new AttendanceTime(this.afterDeductionTime2),
				  								  new AttendanceTime(this.deductionTime2),
				  								  new AttendanceTime(this.divergenceTime2),
				  								  2,
				  								  new DivergenceReasonContent(this.reason2),
				  								  new DiverdenceReasonCode(this.reasonCode2)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTime(this.afterDeductionTime3),
				  								  new AttendanceTime(this.deductionTime3),
				  								  new AttendanceTime(this.divergenceTime3),
				  								  3,
				  								  new DivergenceReasonContent(this.reason3),
				  								  new DiverdenceReasonCode(this.reasonCode3)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTime(this.afterDeductionTime4),
												  new AttendanceTime(this.deductionTime4),
												  new AttendanceTime(this.divergenceTime4),
												  4,
												  new DivergenceReasonContent(this.reason4),
												  new DiverdenceReasonCode(this.reasonCode4)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTime(this.afterDeductionTime5),
				  								  new AttendanceTime(this.deductionTime5),
				  								  new AttendanceTime(this.divergenceTime5),
				  								  5,
				  								  new DivergenceReasonContent(this.reason5),
				  								  new DiverdenceReasonCode(this.reasonCode5)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTime(this.afterDeductionTime6),
				  								  new AttendanceTime(this.deductionTime6),
				  								  new AttendanceTime(this.divergenceTime6),
				  								  6,
				  								  new DivergenceReasonContent(this.reason6),
				  								  new DiverdenceReasonCode(this.reasonCode6)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTime(this.afterDeductionTime7),
				  								  new AttendanceTime(this.deductionTime7),
				  								  new AttendanceTime(this.divergenceTime7),
				  								  7,
				  								  new DivergenceReasonContent(this.reason7),
				  								  new DiverdenceReasonCode(this.reasonCode7)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTime(this.afterDeductionTime8),
				  								  new AttendanceTime(this.deductionTime8),
				  								  new AttendanceTime(this.divergenceTime8),
				  								  8,
				  								  new DivergenceReasonContent(this.reason8),
				  								  new DiverdenceReasonCode(this.reasonCode8)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTime(this.afterDeductionTime9),
				  								  new AttendanceTime(this.deductionTime9),
				  								  new AttendanceTime(this.divergenceTime9),
				  								  9,
				  								  new DivergenceReasonContent(this.reason9),
				  								  new DiverdenceReasonCode(this.reasonCode9)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTime(this.afterDeductionTime10),
				  								  new AttendanceTime(this.deductionTime10),
				  								  new AttendanceTime(this.divergenceTime10),
				  								  10,
				  								  new DivergenceReasonContent(this.reason10),
				  								  new DiverdenceReasonCode(this.reasonCode10)));
		
		val divergence = new DivergenceTimeOfDaily(divergenceTimeList);
		/*日別実績の遅刻時間*/
		List<LateTimeOfDaily> lateTime = new ArrayList<>();
		for (KrcdtDayLateTime krcdt : getKrcdtDayLateTime()) {
			lateTime.add(krcdt.toDomain());
		}
		/*日別実績の早退時間*/
		List<LeaveEarlyTimeOfDaily> leaveEarly = new ArrayList<>();
		for (KrcdtDayLeaveEarlyTime krcdt : getKrcdtDayLeaveEarlyTime()) {
			leaveEarly.add(krcdt.toDomain());
		}
		/*日別実績の短時間勤務*/
		
		List<ShortWorkTimeOfDaily> test = Collections.emptyList();
		for(KrcdtDayShorttime shortTimeValue : KrcdtDayShorttime) {
			test.add(new ShortWorkTimeOfDaily(new WorkTimes(shortTimeValue.count),
											  DeductionTotalTime.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(toRecordTotalTime), new AttendanceTime(calToRecordTotalTime)), 
													  				TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(toRecordInTime), new AttendanceTime(calToRecordInTime)), 
													  				TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(toRecordOutTime), new AttendanceTime(calToRecordOutTime))),
											  DeductionTotalTime.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(deductionTotalTime), new AttendanceTime(calDeductionTotalTime)), 
													  				TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(deductionInTime), new AttendanceTime(calDeductionInTime)), 
													  				TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(deductionOutTime),new AttendanceTime(calDeductionOutTime))),
											  ChildCareAttribute.decisionValue(shortTimeValue.krcdtDayShorttimePK.childCareAtr)));
		}
		if(test.isEmpty()) {
			test.add( new  ShortWorkTimeOfDaily(new WorkTimes(1),
                    DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)),
                                           TimeWithCalculation.sameTime(new AttendanceTime(0)),
                                           TimeWithCalculation.sameTime(new AttendanceTime(0))),
                    DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)),
                                              TimeWithCalculation.sameTime(new AttendanceTime(0)),
                                              TimeWithCalculation.sameTime(new AttendanceTime(0))),
                    ChildCareAttribute.CARE));

		}
		
		/*日別実績の勤怠時間*/
		// 日別実績の総労働時間
		TotalWorkingTime totalTime = new TotalWorkingTime(new AttendanceTime(this.totalAttTime),
				new AttendanceTime(this.totalCalcTime), new AttendanceTime(this.actWorkTime),
				within, 
				new ExcessOfStatutoryTimeOfDaily(new ExcessOfStatutoryMidNightTime(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(outPrsMidnTime), new AttendanceTime(calcOutPrsMidnTime)),new AttendanceTime(preOutPrsMidnTime)),Optional.of(overTime),Optional.of(holidayWork)), 
				lateTime, 
				leaveEarly,
				breakTime,
				Collections.emptyList(),
				new RaiseSalaryTimeOfDailyPerfor(Collections.emptyList(), Collections.emptyList()),
				new WorkTimes(this.workTimes), new TemporaryTimeOfDaily(),
				test.get(0),
				vacation
				);

		// 日別実績の勤務実績時間
		ActualWorkingTimeOfDaily actual = ActualWorkingTimeOfDaily.of(totalTime, this.midnBindTime, this.totalBindTime,
				this.bindDiffTime, this.diffTimeWorkTime, divergence,this.krcdtDayPremiumTime == null ? new PremiumTimeOfDailyPerformance() : this.krcdtDayPremiumTime.toDomain());
		// 日別実績の勤怠時間
		return new AttendanceTimeOfDailyPerformance(this.krcdtDayTimePK == null ? null : this.krcdtDayTimePK.employeeID, this.krcdtDayTimePK.generalDate,
				shedule, actual,
				new StayingTimeOfDaily(new AttendanceTime(this.aftPcLogoffTime),
						new AttendanceTime(this.bfrPcLogonTime), new AttendanceTime(this.bfrWorkTime),
						new AttendanceTime(this.stayingTime), new AttendanceTime(this.aftLeaveTime)),
				new AttendanceTimeOfExistMinus(this.budgetTimeVariance), new AttendanceTimeOfExistMinus(this.unemployedTime));
		
	}
	
	public List<KrcdtDayLeaveEarlyTime> getKrcdtDayLeaveEarlyTime() {
		return krcdtDayLeaveEarlyTime == null ? new ArrayList<>() : krcdtDayLeaveEarlyTime;
	}

	public List<KrcdtDayLateTime> getKrcdtDayLateTime() {
		return krcdtDayLateTime == null ? new ArrayList<>() : krcdtDayLateTime;
	}
}
