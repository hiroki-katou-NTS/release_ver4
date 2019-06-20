package nts.uk.ctx.at.record.infra.entity.monthly.mergetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.information.care.MonCareHdMinutes;
import nts.uk.ctx.at.record.dom.monthly.information.care.MonCareHdNumber;
import nts.uk.ctx.at.record.dom.monthly.information.care.MonCareHdRemain;
import nts.uk.ctx.at.record.dom.monthly.information.childnursing.MonChildHdMinutes;
import nts.uk.ctx.at.record.dom.monthly.information.childnursing.MonChildHdNumber;
import nts.uk.ctx.at.record.dom.monthly.information.childnursing.MonChildHdRemain;
import nts.uk.ctx.at.record.dom.monthly.mergetable.MonthMergeKey;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.SpecialHolidayRemainDataMerge;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AttendanceDaysMonthToTal;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.RemainDataDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveAttdRateDays;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveGrant;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveMaxRemainingTime;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveRemainingNumber;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveUndigestedNumber;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveUsedDays;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveUsedNumber;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AttendanceRate;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.HalfDayAnnLeaRemainingNum;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.HalfDayAnnLeaUsedNum;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.HalfDayAnnualLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.RealAnnualLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.TimeAnnualLeaveUsedTime;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.UndigestedAnnualLeaveDays;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.UndigestedTimeAnnualLeaveTime;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.DayOffDayAndTimes;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.DayOffRemainDayAndTimes;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.MonthlyDayoffRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.RemainDataTimesMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RealReserveLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.ReserveLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.ReserveLeaveGrant;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.ReserveLeaveRemainingDetail;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.ReserveLeaveRemainingNumber;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.ReserveLeaveUndigestedNumber;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.ReserveLeaveUsedNumber;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.ActualSpecialLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.ActualSpecialLeaveRemain;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.ActualSpecialLeaveRemainDay;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialLeavaRemainTime;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialLeave;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialLeaveGrantUseDay;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialLeaveRemain;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialLeaveRemainDay;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialLeaveUnDigestion;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialLeaveUseDays;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialLeaveUseNumber;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialLeaveUseTimes;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.UseNumber;
import nts.uk.ctx.at.shared.dom.common.Day;
import nts.uk.ctx.at.shared.dom.common.days.MonthlyDays;
import nts.uk.ctx.at.shared.dom.common.days.YearlyDays;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveGrantDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveRemainingDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveUsedDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.RemainingMinutes;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.RemainingTimes;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.UsedMinutes;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.UsedTimes;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveGrantDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveRemainingDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveUsedDayNumber;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 残数系
 * @author lanlt
 */
@Getter
@Setter
@Entity
@Table(name = "KRCDT_MON_REMAIN")
public class KrcdtMonRemain extends UkJpaEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public static final JpaEntityMapper<KrcdtMonRemain> MAPPER = new JpaEntityMapper<>(KrcdtMonRemain.class);

	/** プライマリキー */
	@EmbeddedId
	public KrcdtMonMergePk krcdtMonRemainPk;

	/** 締め処理状態 */
	@Column(name = "CLOSURE_STATUS")
	public int closureStatus;
	/** 開始年月日 */
	@Column(name = "START_DATE")
	public GeneralDate startDate;
	/** 終了年月日 */
	@Column(name = "END_DATE")
	public GeneralDate endDate;
	
	/* KRCDT_MON_ANNLEA_REMAIN - エンティティ：年休月別残数データ */
	
	/** 年休使用日数 */
	@Column(name = "AL_USED_DAYS")
	public double annleaUsedDays;
	/** 年休使用日数付与前 */
	@Column(name = "AL_USED_DAYS_BEFORE")
	public double annleaUsedDaysBefore;
	/** 年休使用日数付与後 */
	@Column(name = "AL_USED_DAYS_AFTER")
	public Double annleaUsedDaysAfter;
	/** 使用時間 */
	@Column(name = "AL_USED_MINUTES")
	public Integer annleaUsedMinutes;
	/** 使用時間付与前 */
	@Column(name = "AL_USED_MINUTES_BEFORE")
	public Integer annleaUsedMinutesBefore;
	/** 使用時間付与後 */
	@Column(name = "AL_USED_MINUTES_AFTER")
	public Integer annleaUsedMinutesAfter;
	/** 使用回数 */
	@Column(name = "AL_USED_TIMES")
	public Integer annleaUsedTimes;

	/** 実年休使用日数 */
	@Column(name = "AL_FACT_USED_DAYS")
	public double annleaFactUsedDays;
	/** 実年休使用日数付与前 */
	@Column(name = "AL_FACT_USED_DAYS_BEFORE")
	public double annleaFactUsedDaysBefore;
	/** 実年休使用日数付与後 */
	@Column(name = "AL_FACT_USED_DAYS_AFTER")
	public Double annleaFactUsedDaysAfter;
	/** 実使用時間 */
	@Column(name = "AL_FACT_USED_MINUTES")
	public Integer annleaFactUsedMinutes;
	/** 実使用時間付与前 */
	@Column(name = "AL_FACT_USED_MINUTES_BEFORE")
	public Integer annleaFactUsedMinutesBefore;
	/** 実使用時間付与後 */
	@Column(name = "AL_FACT_USED_MINUTES_AFTER")
	public Integer annleaFactUsedMinutesAfter;
	/** 実使用回数 */
	@Column(name = "AL_FACT_USED_TIMES")
	public Integer annleaFactUsedTimes;

	/** 合計残日数 */
	@Column(name = "AL_REM_DAYS")
	public double annleaRemainingDays;
	/** 合計残時間 */
	@Column(name = "AL_REM_MINUTES")
	public Integer annleaRemainingMinutes;
	/** 実合計残日数 */
	@Column(name = "AL_FACT_REM_DAYS")
	public double annleaFactRemainingDays;
	/** 実合計残時間 */
	@Column(name = "AL_FACT_REM_MINUTES")
	public Integer annleaFactRemainingMinutes;

	/** 合計残日数付与前 */
	@Column(name = "AL_REM_DAYS_BEFORE")
	public double annleaRemainingDaysBefore;
	/** 合計残時間付与前 */
	@Column(name = "AL_REM_MINUTES_BEFORE")
	public Integer annleaRemainingMinutesBefore;
	/** 実合計残日数付与前 */
	@Column(name = "AL_FACT_REM_DAYS_BEFORE")
	public double annleaFactRemainingDaysBefore;
	/** 実合計残時間付与前 */
	@Column(name = "AL_FACT_REM_MINUTES_BEFORE")
	public Integer annleaFactRemainingMinutesBefore;

	/** 合計残日数付与後 */
	@Column(name = "AL_REM_DAYS_AFTER")
	public Double annleaRemainingDaysAfter;
	/** 合計残時間付与後 */
	@Column(name = "AL_REM_MINUTES_AFTER")
	public Integer annleaRemainingMinutesAfter;
	/** 実合計残日数付与後 */
	@Column(name = "AL_FACT_REM_DAYS_AFTER")
	public Double annleaFactRemainingDaysAfter;
	/** 実合計残時間付与後 */
	@Column(name = "AL_FACT_REM_MINUTES_AFTER")
	public Integer annleaFactRemainingMinutesAfter;

	/** 未消化日数 */
	@Column(name = "AL_UNUSED_DAYS")
	public double annleaUnusedDays;
	/** 未消化時間 */
	@Column(name = "AL_UNUSED_MINUTES")
	public Integer annleaUnusedMinutes;
	/** 所定日数 */
	@Column(name = "AL_PREDETERMINED_DAYS")
	public double annleaPredeterminedDays;
	/** 労働日数 */
	@Column(name = "AL_LABOR_DAYS")
	public double annleaLaborDays;
	/** 控除日数 */
	@Column(name = "AL_DEDUCTION_DAYS")
	public double annleaDeductionDays;

	/** 半日年休使用回数 */
	@Column(name = "AL_HALF_USED_TIMES")
	public Integer annleaHalfUsedTimes;
	/** 半日年休使用回数付与前 */
	@Column(name = "AL_HALF_USED_TIMES_BEFORE")
	public Integer annleaHalfUsedTimesBefore;
	/** 半日年休使用回数付与後 */
	@Column(name = "AL_HALF_USED_TIMES_AFTER")
	public Integer annleaHalfUsedTimesAfter;
	/** 半日年休残回数 */
	@Column(name = "AL_HALF_REM_TIMES")
	public Integer annleaHalfRemainingTimes;
	/** 半日年休残回数付与前 */
	@Column(name = "AL_HALF_REM_TIMES_BEFORE")
	public Integer annleaHalfRemainingTimesBefore;
	/** 半日年休残回数付与後 */
	@Column(name = "AL_HALF_REM_TIMES_AFTER")
	public Integer annleaHalfRemainingTimesAfter;

	/** 実半日年休使用回数 */
	@Column(name = "AL_FACT_HALF_USED_TIMES")
	public Integer annleaFactHalfUsedTimes;
	/** 実半日年休使用回数付与前 */
	@Column(name = "AL_FACT_HALF_USED_TIMES_BE")
	public Integer annleaFactHalfUsedTimesBefore;
	/** 実半日年休使用回数付与後 */
	@Column(name = "AL_FACT_HALF_USED_TIMES_AF")
	public Integer annleaFactHalfUsedTimesAfter;
	/** 実半日年休残回数 */
	@Column(name = "AL_FACT_HALF_REM_TIMES")
	public Integer annleaFactHalfRemainingTimes;
	/** 実半日年休残回数付与前 */
	@Column(name = "AL_FACT_HALF_REM_TIMES_BE")
	public Integer annleaFactHalfRemainingTimesBefore;
	/** 実半日年休残回数付与後 */
	@Column(name = "AL_FACT_HALF_REM_TIMES_AF")
	public Integer annleaFactHalfRemainingTimesAfter;

	/** 時間年休上限残時間 */
	@Column(name = "AL_TIME_REM_MINUTES")
	public Integer annleaTimeRemainingMinutes;
	/** 時間年休上限残時間付与前 */
	@Column(name = "AL_TIME_REM_MINUTES_BEFORE")
	public Integer annleaTimeRemainingMinutesBefore;
	/** 時間年休上限残時間付与後 */
	@Column(name = "AL_TIME_REM_MINUTES_AFTER")
	public Integer annleaTimeRemainingMinutesAfter;
	/** 実時間年休上限残時間 */
	@Column(name = "AL_FACT_TIME_REM_MINUTES")
	public Integer annleaFactTimeRemainingMinutes;
	/** 実時間年休上限残時間付与前 */
	@Column(name = "AL_FACT_TIME_REM_MINUTES_BE")
	public Integer annleaFactTimeRemainingMinutesBefore;
	/** 実時間年休上限残時間付与後 */
	@Column(name = "AL_FACT_TIME_REM_MINUTES_AF")
	public Integer annleaFactTimeRemainingMinutesAfter;

	/** 付与区分 */
	@Column(name = "AL_GRANT_ATR")
	public int annleaGrantAtr;
	/** 付与日数 */
	@Column(name = "AL_GRANT_DAYS")
	public Double annleaGrantDays;
	/** 付与所定日数 */
	@Column(name = "AL_GRANT_PREDETERMINED_DAYS")
	public Double annleaGrantPredeterminedDays;
	/** 付与労働日数 */
	@Column(name = "AL_GRANT_LABOR_DAYS")
	public Double annleaGrantLaborDays;
	/** 付与控除日数 */
	@Column(name = "AL_GRANT_DEDUCTION_DAYS")
	public Double annleaGrantDeductionDays;
	/** 控除日数付与前 */
	@Column(name = "AL_DEDUCTION_DAYS_BEFORE")
	public Double annleaDeductionDaysBefore;
	/** 控除日数付与後 */
	@Column(name = "AL_DEDUCTION_DAYS_AFTER")
	public Double annleaDeductionDaysAfter;
	/** 出勤率 */
	@Column(name = "AL_ATTENDANCE_RATE")
	public Double annleaAttendanceRate;

	/* KRCDT_MON_RSVLEA_REMAIN */

	/** 使用日数 */
	@Column(name = "RL_USED_DAYS")
	public double rsvleaUsedDays;
	/** 使用日数付与前 */
	@Column(name = "RL_USED_DAYS_BEFORE")
	public double rsvleaUsedDaysBefore;
	/** 使用日数付与後 */
	@Column(name = "RL_USED_DAYS_AFTER")
	public Double rsvleaUsedDaysAfter;
	/** 実使用日数 */
	@Column(name = "RL_FACT_USED_DAYS")
	public double rsvleaFactUsedDays;
	/** 実使用日数付与前 */
	@Column(name = "RL_FACT_USED_DAYS_BEFORE")
	public double rsvleaFactUsedDaysBefore;
	/** 実使用日数付与後 */
	@Column(name = "RL_FACT_USED_DAYS_AFTER")
	public Double rsvleaFactUsedDaysAfter;
	/** 合計残日数 */
	@Column(name = "RL_REM_DAYS")
	public double rsvleaRemainingDays;
	/** 実合計残日数 */
	@Column(name = "RL_FACT_REM_DAYS")
	public double rsvleaFactRemainingDays;
	/** 合計残日数付与前 */
	@Column(name = "RL_REM_DAYS_BEFORE")
	public double rsvleaRemainingDaysBefore;
	/** 実合計残日数付与前 */
	@Column(name = "RL_FACT_REM_DAYS_BEFORE")
	public double rsvleaFactRemainingDaysBefore;
	/** 合計残日数付与後 */
	@Column(name = "RL_REM_DAYS_AFTER")
	public Double rsvleaRemainingDaysAfter;
	/** 実合計残日数付与後 */
	@Column(name = "RL_FACT_REM_DAYS_AFTER")
	public Double rsvleaFactRemainingDaysAfter;
	/** 未消化日数 */
	@Column(name = "RL_NOT_USED_DAYS")
	public double rsvleaNotUsedDays;
	/** 付与区分 */
	@Column(name = "RL_GRANT_ATR")
	public int rsvleaGrantAtr;
	/** 付与日数 */
	@Column(name = "RL_GRANT_DAYS")
	public Double rsvleaGrantDays;

	/* KRCDT_MON_SP_REMAIN */
	
	/** 特別休暇月別残数データ．特別休暇．使用数．使用日数 */
	@Column(name = "SP_USED_DAYS_1")
	public double useDays1;
	@Column(name = "SP_USED_DAYS_2")
	public double useDays2;
	@Column(name = "SP_USED_DAYS_3")
	public double useDays3;
	@Column(name = "SP_USED_DAYS_4")
	public double useDays4;
	@Column(name = "SP_USED_DAYS_5")
	public double useDays5;
	@Column(name = "SP_USED_DAYS_6")
	public double useDays6;
	@Column(name = "SP_USED_DAYS_7")
	public double useDays7;
	@Column(name = "SP_USED_DAYS_8")
	public double useDays8;
	@Column(name = "SP_USED_DAYS_9")
	public double useDays9;
	@Column(name = "SP_USED_DAYS_10")
	public double useDays10;
	@Column(name = "SP_USED_DAYS_11")
	public double useDays11;
	@Column(name = "SP_USED_DAYS_12")
	public double useDays12;
	@Column(name = "SP_USED_DAYS_13")
	public double useDays13;
	@Column(name = "SP_USED_DAYS_14")
	public double useDays14;
	@Column(name = "SP_USED_DAYS_15")
	public double useDays15;
	@Column(name = "SP_USED_DAYS_16")
	public double useDays16;
	@Column(name = "SP_USED_DAYS_17")
	public double useDays17;
	@Column(name = "SP_USED_DAYS_18")
	public double useDays18;
	@Column(name = "SP_USED_DAYS_19")
	public double useDays19;
	@Column(name = "SP_USED_DAYS_20")
	public double useDays20;

	/** 特別休暇月別残数データ．特別休暇．使用数．使用日数．特別休暇使用日数付与前 */
	@Column(name = "SP_USED_DAYS_BEFORE_1")
	public double beforeUseDays1;
	@Column(name = "SP_USED_DAYS_BEFORE_2")
	public double beforeUseDays2;
	@Column(name = "SP_USED_DAYS_BEFORE_3")
	public double beforeUseDays3;
	@Column(name = "SP_USED_DAYS_BEFORE_4")
	public double beforeUseDays4;
	@Column(name = "SP_USED_DAYS_BEFORE_5")
	public double beforeUseDays5;
	@Column(name = "SP_USED_DAYS_BEFORE_6")
	public double beforeUseDays6;
	@Column(name = "SP_USED_DAYS_BEFORE_7")
	public double beforeUseDays7;
	@Column(name = "SP_USED_DAYS_BEFORE_8")
	public double beforeUseDays8;
	@Column(name = "SP_USED_DAYS_BEFORE_9")
	public double beforeUseDays9;
	@Column(name = "SP_USED_DAYS_BEFORE_10")
	public double beforeUseDays10;
	@Column(name = "SP_USED_DAYS_BEFORE_11")
	public double beforeUseDays11;
	@Column(name = "SP_USED_DAYS_BEFORE_12")
	public double beforeUseDays12;
	@Column(name = "SP_USED_DAYS_BEFORE_13")
	public double beforeUseDays13;
	@Column(name = "SP_USED_DAYS_BEFORE_14")
	public double beforeUseDays14;
	@Column(name = "SP_USED_DAYS_BEFORE_15")
	public double beforeUseDays15;
	@Column(name = "SP_USED_DAYS_BEFORE_16")
	public double beforeUseDays16;
	@Column(name = "SP_USED_DAYS_BEFORE_17")
	public double beforeUseDays17;
	@Column(name = "SP_USED_DAYS_BEFORE_18")
	public double beforeUseDays18;
	@Column(name = "SP_USED_DAYS_BEFORE_19")
	public double beforeUseDays19;
	@Column(name = "SP_USED_DAYS_BEFORE_20")
	public double beforeUseDays20;

	/** 特別休暇月別残数データ．特別休暇．使用数．使用日数． 特別休暇使用日数付与後 */
	@Column(name = "SP_USED_DAYS_AFTER_1")
	public Double afterUseDays1;
	@Column(name = "SP_USED_DAYS_AFTER_2")
	public Double afterUseDays2;
	@Column(name = "SP_USED_DAYS_AFTER_3")
	public Double afterUseDays3;
	@Column(name = "SP_USED_DAYS_AFTER_4")
	public Double afterUseDays4;
	@Column(name = "SP_USED_DAYS_AFTER_5")
	public Double afterUseDays5;
	@Column(name = "SP_USED_DAYS_AFTER_6")
	public Double afterUseDays6;
	@Column(name = "SP_USED_DAYS_AFTER_7")
	public Double afterUseDays7;
	@Column(name = "SP_USED_DAYS_AFTER_8")
	public Double afterUseDays8;
	@Column(name = "SP_USED_DAYS_AFTER_9")
	public Double afterUseDays9;
	@Column(name = "SP_USED_DAYS_AFTER_10")
	public Double afterUseDays10;
	@Column(name = "SP_USED_DAYS_AFTER_11")
	public Double afterUseDays11;
	@Column(name = "SP_USED_DAYS_AFTER_12")
	public Double afterUseDays12;
	@Column(name = "SP_USED_DAYS_AFTER_13")
	public Double afterUseDays13;
	@Column(name = "SP_USED_DAYS_AFTER_14")
	public Double afterUseDays14;
	@Column(name = "SP_USED_DAYS_AFTER_15")
	public Double afterUseDays15;
	@Column(name = "SP_USED_DAYS_AFTER_16")
	public Double afterUseDays16;
	@Column(name = "SP_USED_DAYS_AFTER_17")
	public Double afterUseDays17;
	@Column(name = "SP_USED_DAYS_AFTER_18")
	public Double afterUseDays18;
	@Column(name = "SP_USED_DAYS_AFTER_19")
	public Double afterUseDays19;
	@Column(name = "SP_USED_DAYS_AFTER_20")
	public Double afterUseDays20;

	/** 特別休暇月別残数データ．特別休暇．使用数．使用時間．使用時間 */
	@Column(name = "SP_USED_MINUTES_1")
	public Integer useMinutes1;
	@Column(name = "SP_USED_MINUTES_2")
	public Integer useMinutes2;
	@Column(name = "SP_USED_MINUTES_3")
	public Integer useMinutes3;
	@Column(name = "SP_USED_MINUTES_4")
	public Integer useMinutes4;
	@Column(name = "SP_USED_MINUTES_5")
	public Integer useMinutes5;
	@Column(name = "SP_USED_MINUTES_6")
	public Integer useMinutes6;
	@Column(name = "SP_USED_MINUTES_7")
	public Integer useMinutes7;
	@Column(name = "SP_USED_MINUTES_8")
	public Integer useMinutes8;
	@Column(name = "SP_USED_MINUTES_9")
	public Integer useMinutes9;
	@Column(name = "SP_USED_MINUTES_10")
	public Integer useMinutes10;
	@Column(name = "SP_USED_MINUTES_11")
	public Integer useMinutes11;
	@Column(name = "SP_USED_MINUTES_12")
	public Integer useMinutes12;
	@Column(name = "SP_USED_MINUTES_13")
	public Integer useMinutes13;
	@Column(name = "SP_USED_MINUTES_14")
	public Integer useMinutes14;
	@Column(name = "SP_USED_MINUTES_15")
	public Integer useMinutes15;
	@Column(name = "SP_USED_MINUTES_16")
	public Integer useMinutes16;
	@Column(name = "SP_USED_MINUTES_17")
	public Integer useMinutes17;
	@Column(name = "SP_USED_MINUTES_18")
	public Integer useMinutes18;
	@Column(name = "SP_USED_MINUTES_19")
	public Integer useMinutes19;
	@Column(name = "SP_USED_MINUTES_20")
	public Integer useMinutes20;

	/** 特別休暇月別残数データ．特別休暇．使用数．使用時間． 使用時間付与前 */
	@Column(name = "SP_USED_MINUTES_BEFORE_1")
	public Integer beforeUseMinutes1;
	@Column(name = "SP_USED_MINUTES_BEFORE_2")
	public Integer beforeUseMinutes2;
	@Column(name = "SP_USED_MINUTES_BEFORE_3")
	public Integer beforeUseMinutes3;
	@Column(name = "SP_USED_MINUTES_BEFORE_4")
	public Integer beforeUseMinutes4;
	@Column(name = "SP_USED_MINUTES_BEFORE_5")
	public Integer beforeUseMinutes5;
	@Column(name = "SP_USED_MINUTES_BEFORE_6")
	public Integer beforeUseMinutes6;
	@Column(name = "SP_USED_MINUTES_BEFORE_7")
	public Integer beforeUseMinutes7;
	@Column(name = "SP_USED_MINUTES_BEFORE_8")
	public Integer beforeUseMinutes8;
	@Column(name = "SP_USED_MINUTES_BEFORE_9")
	public Integer beforeUseMinutes9;
	@Column(name = "SP_USED_MINUTES_BEFORE_10")
	public Integer beforeUseMinutes10;
	@Column(name = "SP_USED_MINUTES_BEFORE_11")
	public Integer beforeUseMinutes11;
	@Column(name = "SP_USED_MINUTES_BEFORE_12")
	public Integer beforeUseMinutes12;
	@Column(name = "SP_USED_MINUTES_BEFORE_13")
	public Integer beforeUseMinutes13;
	@Column(name = "SP_USED_MINUTES_BEFORE_14")
	public Integer beforeUseMinutes14;
	@Column(name = "SP_USED_MINUTES_BEFORE_15")
	public Integer beforeUseMinutes15;
	@Column(name = "SP_USED_MINUTES_BEFORE_16")
	public Integer beforeUseMinutes16;
	@Column(name = "SP_USED_MINUTES_BEFORE_17")
	public Integer beforeUseMinutes17;
	@Column(name = "SP_USED_MINUTES_BEFORE_18")
	public Integer beforeUseMinutes18;
	@Column(name = "SP_USED_MINUTES_BEFORE_19")
	public Integer beforeUseMinutes19;
	@Column(name = "SP_USED_MINUTES_BEFORE_20")
	public Integer beforeUseMinutes20;

	/** 特別休暇月別残数データ．特別休暇．使用数．使用時間 ．使用時間付与後 */
	@Column(name = "SP_USED_MINUTES_AFTER_1")
	public Integer afterUseMinutes1;
	@Column(name = "SP_USED_MINUTES_AFTER_2")
	public Integer afterUseMinutes2;
	@Column(name = "SP_USED_MINUTES_AFTER_3")
	public Integer afterUseMinutes3;
	@Column(name = "SP_USED_MINUTES_AFTER_4")
	public Integer afterUseMinutes4;
	@Column(name = "SP_USED_MINUTES_AFTER_5")
	public Integer afterUseMinutes5;
	@Column(name = "SP_USED_MINUTES_AFTER_6")
	public Integer afterUseMinutes6;
	@Column(name = "SP_USED_MINUTES_AFTER_7")
	public Integer afterUseMinutes7;
	@Column(name = "SP_USED_MINUTES_AFTER_8")
	public Integer afterUseMinutes8;
	@Column(name = "SP_USED_MINUTES_AFTER_9")
	public Integer afterUseMinutes9;
	@Column(name = "SP_USED_MINUTES_AFTER_10")
	public Integer afterUseMinutes10;
	@Column(name = "SP_USED_MINUTES_AFTER_11")
	public Integer afterUseMinutes11;
	@Column(name = "SP_USED_MINUTES_AFTER_12")
	public Integer afterUseMinutes12;
	@Column(name = "SP_USED_MINUTES_AFTER_13")
	public Integer afterUseMinutes13;
	@Column(name = "SP_USED_MINUTES_AFTER_14")
	public Integer afterUseMinutes14;
	@Column(name = "SP_USED_MINUTES_AFTER_15")
	public Integer afterUseMinutes15;
	@Column(name = "SP_USED_MINUTES_AFTER_16")
	public Integer afterUseMinutes16;
	@Column(name = "SP_USED_MINUTES_AFTER_17")
	public Integer afterUseMinutes17;
	@Column(name = "SP_USED_MINUTES_AFTER_18")
	public Integer afterUseMinutes18;
	@Column(name = "SP_USED_MINUTES_AFTER_19")
	public Integer afterUseMinutes19;
	@Column(name = "SP_USED_MINUTES_AFTER_20")
	public Integer afterUseMinutes20;

	/** 特別休暇月別残数データ．特別休暇．使用数．使用時間: 使用回数 */
	@Column(name = "SP_USED_TIMES_1")
	public Integer useTimes1;
	@Column(name = "SP_USED_TIMES_2")
	public Integer useTimes2;
	@Column(name = "SP_USED_TIMES_3")
	public Integer useTimes3;
	@Column(name = "SP_USED_TIMES_4")
	public Integer useTimes4;
	@Column(name = "SP_USED_TIMES_5")
	public Integer useTimes5;
	@Column(name = "SP_USED_TIMES_6")
	public Integer useTimes6;
	@Column(name = "SP_USED_TIMES_7")
	public Integer useTimes7;
	@Column(name = "SP_USED_TIMES_8")
	public Integer useTimes8;
	@Column(name = "SP_USED_TIMES_9")
	public Integer useTimes9;
	@Column(name = "SP_USED_TIMES_10")
	public Integer useTimes10;
	@Column(name = "SP_USED_TIMES_11")
	public Integer useTimes11;
	@Column(name = "SP_USED_TIMES_12")
	public Integer useTimes12;
	@Column(name = "SP_USED_TIMES_13")
	public Integer useTimes13;
	@Column(name = "SP_USED_TIMES_14")
	public Integer useTimes14;
	@Column(name = "SP_USED_TIMES_15")
	public Integer useTimes15;
	@Column(name = "SP_USED_TIMES_16")
	public Integer useTimes16;
	@Column(name = "SP_USED_TIMES_17")
	public Integer useTimes17;
	@Column(name = "SP_USED_TIMES_18")
	public Integer useTimes18;
	@Column(name = "SP_USED_TIMES_19")
	public Integer useTimes19;
	@Column(name = "SP_USED_TIMES_20")
	public Integer useTimes20;
	
	/** 特別休暇月別残数データ．実特別休暇．使用数．使用日数 .実特別休暇使用日数 */
	@Column(name = "SP_FACT_USED_DAYS_1")
	public double factUseDays1;
	@Column(name = "SP_FACT_USED_DAYS_2")
	public double factUseDays2;
	@Column(name = "SP_FACT_USED_DAYS_3")
	public double factUseDays3;
	@Column(name = "SP_FACT_USED_DAYS_4")
	public double factUseDays4;
	@Column(name = "SP_FACT_USED_DAYS_5")
	public double factUseDays5;
	@Column(name = "SP_FACT_USED_DAYS_6")
	public double factUseDays6;
	@Column(name = "SP_FACT_USED_DAYS_7")
	public double factUseDays7;
	@Column(name = "SP_FACT_USED_DAYS_8")
	public double factUseDays8;
	@Column(name = "SP_FACT_USED_DAYS_9")
	public double factUseDays9;
	@Column(name = "SP_FACT_USED_DAYS_10")
	public double factUseDays10;
	@Column(name = "SP_FACT_USED_DAYS_11")
	public double factUseDays11;
	@Column(name = "SP_FACT_USED_DAYS_12")
	public double factUseDays12;
	@Column(name = "SP_FACT_USED_DAYS_13")
	public double factUseDays13;
	@Column(name = "SP_FACT_USED_DAYS_14")
	public double factUseDays14;
	@Column(name = "SP_FACT_USED_DAYS_15")
	public double factUseDays15;
	@Column(name = "SP_FACT_USED_DAYS_16")
	public double factUseDays16;
	@Column(name = "SP_FACT_USED_DAYS_17")
	public double factUseDays17;
	@Column(name = "SP_FACT_USED_DAYS_18")
	public double factUseDays18;
	@Column(name = "SP_FACT_USED_DAYS_19")
	public double factUseDays19;
	@Column(name = "SP_FACT_USED_DAYS_20")
	public double factUseDays20;

	/** 特別休暇月別残数データ．実特別休暇．使用数．使用日数. 使用日数付与前 */
	@Column(name = "SP_FACT_USED_DAYS_BE_1")
	public double beforeFactUseDays1;
	@Column(name = "SP_FACT_USED_DAYS_BE_2")
	public double beforeFactUseDays2;
	@Column(name = "SP_FACT_USED_DAYS_BE_3")
	public double beforeFactUseDays3;
	@Column(name = "SP_FACT_USED_DAYS_BE_4")
	public double beforeFactUseDays4;
	@Column(name = "SP_FACT_USED_DAYS_BE_5")
	public double beforeFactUseDays5;
	@Column(name = "SP_FACT_USED_DAYS_BE_6")
	public double beforeFactUseDays6;
	@Column(name = "SP_FACT_USED_DAYS_BE_7")
	public double beforeFactUseDays7;
	@Column(name = "SP_FACT_USED_DAYS_BE_8")
	public double beforeFactUseDays8;
	@Column(name = "SP_FACT_USED_DAYS_BE_9")
	public double beforeFactUseDays9;
	@Column(name = "SP_FACT_USED_DAYS_BE_10")
	public double beforeFactUseDays10;
	@Column(name = "SP_FACT_USED_DAYS_BE_11")
	public double beforeFactUseDays11;
	@Column(name = "SP_FACT_USED_DAYS_BE_12")
	public double beforeFactUseDays12;
	@Column(name = "SP_FACT_USED_DAYS_BE_13")
	public double beforeFactUseDays13;
	@Column(name = "SP_FACT_USED_DAYS_BE_14")
	public double beforeFactUseDays14;
	@Column(name = "SP_FACT_USED_DAYS_BE_15")
	public double beforeFactUseDays15;
	@Column(name = "SP_FACT_USED_DAYS_BE_16")
	public double beforeFactUseDays16;
	@Column(name = "SP_FACT_USED_DAYS_BE_17")
	public double beforeFactUseDays17;
	@Column(name = "SP_FACT_USED_DAYS_BE_18")
	public double beforeFactUseDays18;
	@Column(name = "SP_FACT_USED_DAYS_BE_19")
	public double beforeFactUseDays19;
	@Column(name = "SP_FACT_USED_DAYS_BE_20")
	public double beforeFactUseDays20;
	
	/** 特別休暇月別残数データ．実特別休暇．使用数．使用日数.使用日数付与後 */
	@Column(name = "SP_FACT_USED_DAYS_AF_1")
	public Double afterFactUseDays1;
	@Column(name = "SP_FACT_USED_DAYS_AF_2")
	public Double afterFactUseDays2;
	@Column(name = "SP_FACT_USED_DAYS_AF_3")
	public Double afterFactUseDays3;
	@Column(name = "SP_FACT_USED_DAYS_AF_4")
	public Double afterFactUseDays4;
	@Column(name = "SP_FACT_USED_DAYS_AF_5")
	public Double afterFactUseDays5;
	@Column(name = "SP_FACT_USED_DAYS_AF_6")
	public Double afterFactUseDays6;
	@Column(name = "SP_FACT_USED_DAYS_AF_7")
	public Double afterFactUseDays7;
	@Column(name = "SP_FACT_USED_DAYS_AF_8")
	public Double afterFactUseDays8;
	@Column(name = "SP_FACT_USED_DAYS_AF_9")
	public Double afterFactUseDays9;
	@Column(name = "SP_FACT_USED_DAYS_AF_10")
	public Double afterFactUseDays10;
	@Column(name = "SP_FACT_USED_DAYS_AF_11")
	public Double afterFactUseDays11;
	@Column(name = "SP_FACT_USED_DAYS_AF_12")
	public Double afterFactUseDays12;
	@Column(name = "SP_FACT_USED_DAYS_AF_13")
	public Double afterFactUseDays13;
	@Column(name = "SP_FACT_USED_DAYS_AF_14")
	public Double afterFactUseDays14;
	@Column(name = "SP_FACT_USED_DAYS_AF_15")
	public Double afterFactUseDays15;
	@Column(name = "SP_FACT_USED_DAYS_AF_16")
	public Double afterFactUseDays16;
	@Column(name = "SP_FACT_USED_DAYS_AF_17")
	public Double afterFactUseDays17;
	@Column(name = "SP_FACT_USED_DAYS_AF_18")
	public Double afterFactUseDays18;
	@Column(name = "SP_FACT_USED_DAYS_AF_19")
	public Double afterFactUseDays19;
	@Column(name = "SP_FACT_USED_DAYS_AF_20")
	public Double afterFactUseDays20;

	/** 特別休暇月別残数データ．実特別休暇．使用数．使用時間.使用時間 */
	@Column(name = "SP_FACT_USED_MINUTES_1")
	public Integer factUseMinutes1;
	@Column(name = "SP_FACT_USED_MINUTES_2")
	public Integer factUseMinutes2;
	@Column(name = "SP_FACT_USED_MINUTES_3")
	public Integer factUseMinutes3;
	@Column(name = "SP_FACT_USED_MINUTES_4")
	public Integer factUseMinutes4;
	@Column(name = "SP_FACT_USED_MINUTES_5")
	public Integer factUseMinutes5;
	@Column(name = "SP_FACT_USED_MINUTES_6")
	public Integer factUseMinutes6;
	@Column(name = "SP_FACT_USED_MINUTES_7")
	public Integer factUseMinutes7;
	@Column(name = "SP_FACT_USED_MINUTES_8")
	public Integer factUseMinutes8;
	@Column(name = "SP_FACT_USED_MINUTES_9")
	public Integer factUseMinutes9;
	@Column(name = "SP_FACT_USED_MINUTES_10")
	public Integer factUseMinutes10;
	@Column(name = "SP_FACT_USED_MINUTES_11")
	public Integer factUseMinutes11;
	@Column(name = "SP_FACT_USED_MINUTES_12")
	public Integer factUseMinutes12;
	@Column(name = "SP_FACT_USED_MINUTES_13")
	public Integer factUseMinutes13;
	@Column(name = "SP_FACT_USED_MINUTES_14")
	public Integer factUseMinutes14;
	@Column(name = "SP_FACT_USED_MINUTES_15")
	public Integer factUseMinutes15;
	@Column(name = "SP_FACT_USED_MINUTES_16")
	public Integer factUseMinutes16;
	@Column(name = "SP_FACT_USED_MINUTES_17")
	public Integer factUseMinutes17;
	@Column(name = "SP_FACT_USED_MINUTES_18")
	public Integer factUseMinutes18;
	@Column(name = "SP_FACT_USED_MINUTES_19")
	public Integer factUseMinutes19;
	@Column(name = "SP_FACT_USED_MINUTES_20")
	public Integer factUseMinutes20;

	/** 特別休暇月別残数データ．実特別休暇．使用数．使用時間.使用時間付与前 */
	@Column(name = "SP_FACT_USED_MINUTES_BE_1")
	public Integer beforeFactUseMinutes1;
	@Column(name = "SP_FACT_USED_MINUTES_BE_2")
	public Integer beforeFactUseMinutes2;
	@Column(name = "SP_FACT_USED_MINUTES_BE_3")
	public Integer beforeFactUseMinutes3;
	@Column(name = "SP_FACT_USED_MINUTES_BE_4")
	public Integer beforeFactUseMinutes4;
	@Column(name = "SP_FACT_USED_MINUTES_BE_5")
	public Integer beforeFactUseMinutes5;
	@Column(name = "SP_FACT_USED_MINUTES_BE_6")
	public Integer beforeFactUseMinutes6;
	@Column(name = "SP_FACT_USED_MINUTES_BE_7")
	public Integer beforeFactUseMinutes7;
	@Column(name = "SP_FACT_USED_MINUTES_BE_8")
	public Integer beforeFactUseMinutes8;
	@Column(name = "SP_FACT_USED_MINUTES_BE_9")
	public Integer beforeFactUseMinutes9;
	@Column(name = "SP_FACT_USED_MINUTES_BE_10")
	public Integer beforeFactUseMinutes10;
	@Column(name = "SP_FACT_USED_MINUTES_BE_11")
	public Integer beforeFactUseMinutes11;
	@Column(name = "SP_FACT_USED_MINUTES_BE_12")
	public Integer beforeFactUseMinutes12;
	@Column(name = "SP_FACT_USED_MINUTES_BE_13")
	public Integer beforeFactUseMinutes13;
	@Column(name = "SP_FACT_USED_MINUTES_BE_14")
	public Integer beforeFactUseMinutes14;
	@Column(name = "SP_FACT_USED_MINUTES_BE_15")
	public Integer beforeFactUseMinutes15;
	@Column(name = "SP_FACT_USED_MINUTES_BE_16")
	public Integer beforeFactUseMinutes16;
	@Column(name = "SP_FACT_USED_MINUTES_BE_17")
	public Integer beforeFactUseMinutes17;
	@Column(name = "SP_FACT_USED_MINUTES_BE_18")
	public Integer beforeFactUseMinutes18;
	@Column(name = "SP_FACT_USED_MINUTES_BE_19")
	public Integer beforeFactUseMinutes19;
	@Column(name = "SP_FACT_USED_MINUTES_BE_20")
	public Integer beforeFactUseMinutes20;

	/** 特別休暇月別残数データ．実特別休暇．使用数．使用時間. 使用時間付与後 */
	@Column(name = "SP_FACT_USED_MINUTES_AF_1")
	public Integer afterFactUseMinutes1;
	@Column(name = "SP_FACT_USED_MINUTES_AF_2")
	public Integer afterFactUseMinutes2;
	@Column(name = "SP_FACT_USED_MINUTES_AF_3")
	public Integer afterFactUseMinutes3;
	@Column(name = "SP_FACT_USED_MINUTES_AF_4")
	public Integer afterFactUseMinutes4;
	@Column(name = "SP_FACT_USED_MINUTES_AF_5")
	public Integer afterFactUseMinutes5;
	@Column(name = "SP_FACT_USED_MINUTES_AF_6")
	public Integer afterFactUseMinutes6;
	@Column(name = "SP_FACT_USED_MINUTES_AF_7")
	public Integer afterFactUseMinutes7;
	@Column(name = "SP_FACT_USED_MINUTES_AF_8")
	public Integer afterFactUseMinutes8;
	@Column(name = "SP_FACT_USED_MINUTES_AF_9")
	public Integer afterFactUseMinutes9;
	@Column(name = "SP_FACT_USED_MINUTES_AF_10")
	public Integer afterFactUseMinutes10;
	@Column(name = "SP_FACT_USED_MINUTES_AF_11")
	public Integer afterFactUseMinutes11;
	@Column(name = "SP_FACT_USED_MINUTES_AF_12")
	public Integer afterFactUseMinutes12;
	@Column(name = "SP_FACT_USED_MINUTES_AF_13")
	public Integer afterFactUseMinutes13;
	@Column(name = "SP_FACT_USED_MINUTES_AF_14")
	public Integer afterFactUseMinutes14;
	@Column(name = "SP_FACT_USED_MINUTES_AF_15")
	public Integer afterFactUseMinutes15;
	@Column(name = "SP_FACT_USED_MINUTES_AF_16")
	public Integer afterFactUseMinutes16;
	@Column(name = "SP_FACT_USED_MINUTES_AF_17")
	public Integer afterFactUseMinutes17;
	@Column(name = "SP_FACT_USED_MINUTES_AF_18")
	public Integer afterFactUseMinutes18;
	@Column(name = "SP_FACT_USED_MINUTES_AF_19")
	public Integer afterFactUseMinutes19;
	@Column(name = "SP_FACT_USED_MINUTES_AF_20")
	public Integer afterFactUseMinutes20;

	/** 特別休暇月別残数データ．実特別休暇．使用数．使用時間.使用回数 */
	@Column(name = "SP_FACT_USED_TIMES_1")
	public Integer factUseTimes1;
	@Column(name = "SP_FACT_USED_TIMES_2")
	public Integer factUseTimes2;
	@Column(name = "SP_FACT_USED_TIMES_3")
	public Integer factUseTimes3;
	@Column(name = "SP_FACT_USED_TIMES_4")
	public Integer factUseTimes4;
	@Column(name = "SP_FACT_USED_TIMES_5")
	public Integer factUseTimes5;
	@Column(name = "SP_FACT_USED_TIMES_6")
	public Integer factUseTimes6;
	@Column(name = "SP_FACT_USED_TIMES_7")
	public Integer factUseTimes7;
	@Column(name = "SP_FACT_USED_TIMES_8")
	public Integer factUseTimes8;
	@Column(name = "SP_FACT_USED_TIMES_9")
	public Integer factUseTimes9;
	@Column(name = "SP_FACT_USED_TIMES_10")
	public Integer factUseTimes10;
	@Column(name = "SP_FACT_USED_TIMES_11")
	public Integer factUseTimes11;
	@Column(name = "SP_FACT_USED_TIMES_12")
	public Integer factUseTimes12;
	@Column(name = "SP_FACT_USED_TIMES_13")
	public Integer factUseTimes13;
	@Column(name = "SP_FACT_USED_TIMES_14")
	public Integer factUseTimes14;
	@Column(name = "SP_FACT_USED_TIMES_15")
	public Integer factUseTimes15;
	@Column(name = "SP_FACT_USED_TIMES_16")
	public Integer factUseTimes16;
	@Column(name = "SP_FACT_USED_TIMES_17")
	public Integer factUseTimes17;
	@Column(name = "SP_FACT_USED_TIMES_18")
	public Integer factUseTimes18;
	@Column(name = "SP_FACT_USED_TIMES_19")
	public Integer factUseTimes19;
	@Column(name = "SP_FACT_USED_TIMES_20")
	public Integer factUseTimes20;

	/** 特別休暇月別残数データ．特別休暇．残数.日数 */
	@Column(name = "SP_REM_DAYS_1")
	public double remainDays1;
	@Column(name = "SP_REM_DAYS_2")
	public double remainDays2;
	@Column(name = "SP_REM_DAYS_3")
	public double remainDays3;
	@Column(name = "SP_REM_DAYS_4")
	public double remainDays4;
	@Column(name = "SP_REM_DAYS_5")
	public double remainDays5;
	@Column(name = "SP_REM_DAYS_6")
	public double remainDays6;
	@Column(name = "SP_REM_DAYS_7")
	public double remainDays7;
	@Column(name = "SP_REM_DAYS_8")
	public double remainDays8;
	@Column(name = "SP_REM_DAYS_9")
	public double remainDays9;
	@Column(name = "SP_REM_DAYS_10")
	public double remainDays10;
	@Column(name = "SP_REM_DAYS_11")
	public double remainDays11;
	@Column(name = "SP_REM_DAYS_12")
	public double remainDays12;
	@Column(name = "SP_REM_DAYS_13")
	public double remainDays13;
	@Column(name = "SP_REM_DAYS_14")
	public double remainDays14;
	@Column(name = "SP_REM_DAYS_15")
	public double remainDays15;
	@Column(name = "SP_REM_DAYS_16")
	public double remainDays16;
	@Column(name = "SP_REM_DAYS_17")
	public double remainDays17;
	@Column(name = "SP_REM_DAYS_18")
	public double remainDays18;
	@Column(name = "SP_REM_DAYS_19")
	public double remainDays19;
	@Column(name = "SP_REM_DAYS_20")
	public double remainDays20;

	/** 特別休暇月別残数データ．特別休暇．残数.時間 */
	@Column(name = "SP_REM_MINUTES_1")
	public Integer remainMinutes1;
	@Column(name = "SP_REM_MINUTES_2")
	public Integer remainMinutes2;
	@Column(name = "SP_REM_MINUTES_3")
	public Integer remainMinutes3;
	@Column(name = "SP_REM_MINUTES_4")
	public Integer remainMinutes4;
	@Column(name = "SP_REM_MINUTES_5")
	public Integer remainMinutes5;
	@Column(name = "SP_REM_MINUTES_6")
	public Integer remainMinutes6;
	@Column(name = "SP_REM_MINUTES_7")
	public Integer remainMinutes7;
	@Column(name = "SP_REM_MINUTES_8")
	public Integer remainMinutes8;
	@Column(name = "SP_REM_MINUTES_9")
	public Integer remainMinutes9;
	@Column(name = "SP_REM_MINUTES_10")
	public Integer remainMinutes10;
	@Column(name = "SP_REM_MINUTES_11")
	public Integer remainMinutes11;
	@Column(name = "SP_REM_MINUTES_12")
	public Integer remainMinutes12;
	@Column(name = "SP_REM_MINUTES_13")
	public Integer remainMinutes13;
	@Column(name = "SP_REM_MINUTES_14")
	public Integer remainMinutes14;
	@Column(name = "SP_REM_MINUTES_15")
	public Integer remainMinutes15;
	@Column(name = "SP_REM_MINUTES_16")
	public Integer remainMinutes16;
	@Column(name = "SP_REM_MINUTES_17")
	public Integer remainMinutes17;
	@Column(name = "SP_REM_MINUTES_18")
	public Integer remainMinutes18;
	@Column(name = "SP_REM_MINUTES_19")
	public Integer remainMinutes19;
	@Column(name = "SP_REM_MINUTES_20")
	public Integer remainMinutes20;

	/** 特別休暇月別残数データ．実特別休暇．残数. 日数 */
	@Column(name = "SP_FACT_REM_DAYS_1")
	public double factRemainDays1;
	@Column(name = "SP_FACT_REM_DAYS_2")
	public double factRemainDays2;
	@Column(name = "SP_FACT_REM_DAYS_3")
	public double factRemainDays3;
	@Column(name = "SP_FACT_REM_DAYS_4")
	public double factRemainDays4;
	@Column(name = "SP_FACT_REM_DAYS_5")
	public double factRemainDays5;
	@Column(name = "SP_FACT_REM_DAYS_6")
	public double factRemainDays6;
	@Column(name = "SP_FACT_REM_DAYS_7")
	public double factRemainDays7;
	@Column(name = "SP_FACT_REM_DAYS_8")
	public double factRemainDays8;
	@Column(name = "SP_FACT_REM_DAYS_9")
	public double factRemainDays9;
	@Column(name = "SP_FACT_REM_DAYS_10")
	public double factRemainDays10;
	@Column(name = "SP_FACT_REM_DAYS_11")
	public double factRemainDays11;
	@Column(name = "SP_FACT_REM_DAYS_12")
	public double factRemainDays12;
	@Column(name = "SP_FACT_REM_DAYS_13")
	public double factRemainDays13;
	@Column(name = "SP_FACT_REM_DAYS_14")
	public double factRemainDays14;
	@Column(name = "SP_FACT_REM_DAYS_15")
	public double factRemainDays15;
	@Column(name = "SP_FACT_REM_DAYS_16")
	public double factRemainDays16;
	@Column(name = "SP_FACT_REM_DAYS_17")
	public double factRemainDays17;
	@Column(name = "SP_FACT_REM_DAYS_18")
	public double factRemainDays18;
	@Column(name = "SP_FACT_REM_DAYS_19")
	public double factRemainDays19;
	@Column(name = "SP_FACT_REM_DAYS_20")
	public double factRemainDays20;

	/** 特別休暇月別残数データ．実特別休暇．残数.時間 */
	@Column(name = "SP_FACT_REM_MINUTES_1")
	public Integer factRemainMinutes1;
	@Column(name = "SP_FACT_REM_MINUTES_2")
	public Integer factRemainMinutes2;
	@Column(name = "SP_FACT_REM_MINUTES_3")
	public Integer factRemainMinutes3;
	@Column(name = "SP_FACT_REM_MINUTES_4")
	public Integer factRemainMinutes4;
	@Column(name = "SP_FACT_REM_MINUTES_5")
	public Integer factRemainMinutes5;
	@Column(name = "SP_FACT_REM_MINUTES_6")
	public Integer factRemainMinutes6;
	@Column(name = "SP_FACT_REM_MINUTES_7")
	public Integer factRemainMinutes7;
	@Column(name = "SP_FACT_REM_MINUTES_8")
	public Integer factRemainMinutes8;
	@Column(name = "SP_FACT_REM_MINUTES_9")
	public Integer factRemainMinutes9;
	@Column(name = "SP_FACT_REM_MINUTES_10")
	public Integer factRemainMinutes10;
	@Column(name = "SP_FACT_REM_MINUTES_11")
	public Integer factRemainMinutes11;
	@Column(name = "SP_FACT_REM_MINUTES_12")
	public Integer factRemainMinutes12;
	@Column(name = "SP_FACT_REM_MINUTES_13")
	public Integer factRemainMinutes13;
	@Column(name = "SP_FACT_REM_MINUTES_14")
	public Integer factRemainMinutes14;
	@Column(name = "SP_FACT_REM_MINUTES_15")
	public Integer factRemainMinutes15;
	@Column(name = "SP_FACT_REM_MINUTES_16")
	public Integer factRemainMinutes16;
	@Column(name = "SP_FACT_REM_MINUTES_17")
	public Integer factRemainMinutes17;
	@Column(name = "SP_FACT_REM_MINUTES_18")
	public Integer factRemainMinutes18;
	@Column(name = "SP_FACT_REM_MINUTES_19")
	public Integer factRemainMinutes19;
	@Column(name = "SP_FACT_REM_MINUTES_20")
	public Integer factRemainMinutes20;

	/** 特別休暇月別残数データ．特別休暇．残数付与前.日数 */
	@Column(name = "SP_REM_DAYS_BEFORE_1")
	public double beforeRemainDays1;
	@Column(name = "SP_REM_DAYS_BEFORE_2")
	public double beforeRemainDays2;
	@Column(name = "SP_REM_DAYS_BEFORE_3")
	public double beforeRemainDays3;
	@Column(name = "SP_REM_DAYS_BEFORE_4")
	public double beforeRemainDays4;
	@Column(name = "SP_REM_DAYS_BEFORE_5")
	public double beforeRemainDays5;
	@Column(name = "SP_REM_DAYS_BEFORE_6")
	public double beforeRemainDays6;
	@Column(name = "SP_REM_DAYS_BEFORE_7")
	public double beforeRemainDays7;
	@Column(name = "SP_REM_DAYS_BEFORE_8")
	public double beforeRemainDays8;
	@Column(name = "SP_REM_DAYS_BEFORE_9")
	public double beforeRemainDays9;
	@Column(name = "SP_REM_DAYS_BEFORE_10")
	public double beforeRemainDays10;
	@Column(name = "SP_REM_DAYS_BEFORE_11")
	public double beforeRemainDays11;
	@Column(name = "SP_REM_DAYS_BEFORE_12")
	public double beforeRemainDays12;
	@Column(name = "SP_REM_DAYS_BEFORE_13")
	public double beforeRemainDays13;
	@Column(name = "SP_REM_DAYS_BEFORE_14")
	public double beforeRemainDays14;
	@Column(name = "SP_REM_DAYS_BEFORE_15")
	public double beforeRemainDays15;
	@Column(name = "SP_REM_DAYS_BEFORE_16")
	public double beforeRemainDays16;
	@Column(name = "SP_REM_DAYS_BEFORE_17")
	public double beforeRemainDays17;
	@Column(name = "SP_REM_DAYS_BEFORE_18")
	public double beforeRemainDays18;
	@Column(name = "SP_REM_DAYS_BEFORE_19")
	public double beforeRemainDays19;
	@Column(name = "SP_REM_DAYS_BEFORE_20")
	public double beforeRemainDays20;

	/** 特別休暇月別残数データ．特別休暇．残数付与前.時間 */
	@Column(name = "SP_REM_MINUTES_BEFORE_1")
	public Integer beforeRemainMinutes1;
	@Column(name = "SP_REM_MINUTES_BEFORE_2")
	public Integer beforeRemainMinutes2;
	@Column(name = "SP_REM_MINUTES_BEFORE_3")
	public Integer beforeRemainMinutes3;
	@Column(name = "SP_REM_MINUTES_BEFORE_4")
	public Integer beforeRemainMinutes4;
	@Column(name = "SP_REM_MINUTES_BEFORE_5")
	public Integer beforeRemainMinutes5;
	@Column(name = "SP_REM_MINUTES_BEFORE_6")
	public Integer beforeRemainMinutes6;
	@Column(name = "SP_REM_MINUTES_BEFORE_7")
	public Integer beforeRemainMinutes7;
	@Column(name = "SP_REM_MINUTES_BEFORE_8")
	public Integer beforeRemainMinutes8;
	@Column(name = "SP_REM_MINUTES_BEFORE_9")
	public Integer beforeRemainMinutes9;
	@Column(name = "SP_REM_MINUTES_BEFORE_10")
	public Integer beforeRemainMinutes10;
	@Column(name = "SP_REM_MINUTES_BEFORE_11")
	public Integer beforeRemainMinutes11;
	@Column(name = "SP_REM_MINUTES_BEFORE_12")
	public Integer beforeRemainMinutes12;
	@Column(name = "SP_REM_MINUTES_BEFORE_13")
	public Integer beforeRemainMinutes13;
	@Column(name = "SP_REM_MINUTES_BEFORE_14")
	public Integer beforeRemainMinutes14;
	@Column(name = "SP_REM_MINUTES_BEFORE_15")
	public Integer beforeRemainMinutes15;
	@Column(name = "SP_REM_MINUTES_BEFORE_16")
	public Integer beforeRemainMinutes16;
	@Column(name = "SP_REM_MINUTES_BEFORE_17")
	public Integer beforeRemainMinutes17;
	@Column(name = "SP_REM_MINUTES_BEFORE_18")
	public Integer beforeRemainMinutes18;
	@Column(name = "SP_REM_MINUTES_BEFORE_19")
	public Integer beforeRemainMinutes19;
	@Column(name = "SP_REM_MINUTES_BEFORE_20")
	public Integer beforeRemainMinutes20;
	
	/** 特別休暇月別残数データ．実特別休暇．残数付与前.日数 */
	@Column(name = "SP_FACT_REM_DAYS_BE_1")
	public double beforeFactRemainDays1;
	@Column(name = "SP_FACT_REM_DAYS_BE_2")
	public double beforeFactRemainDays2;
	@Column(name = "SP_FACT_REM_DAYS_BE_3")
	public double beforeFactRemainDays3;
	@Column(name = "SP_FACT_REM_DAYS_BE_4")
	public double beforeFactRemainDays4;
	@Column(name = "SP_FACT_REM_DAYS_BE_5")
	public double beforeFactRemainDays5;
	@Column(name = "SP_FACT_REM_DAYS_BE_6")
	public double beforeFactRemainDays6;
	@Column(name = "SP_FACT_REM_DAYS_BE_7")
	public double beforeFactRemainDays7;
	@Column(name = "SP_FACT_REM_DAYS_BE_8")
	public double beforeFactRemainDays8;
	@Column(name = "SP_FACT_REM_DAYS_BE_9")
	public double beforeFactRemainDays9;
	@Column(name = "SP_FACT_REM_DAYS_BE_10")
	public double beforeFactRemainDays10;
	@Column(name = "SP_FACT_REM_DAYS_BE_11")
	public double beforeFactRemainDays11;
	@Column(name = "SP_FACT_REM_DAYS_BE_12")
	public double beforeFactRemainDays12;
	@Column(name = "SP_FACT_REM_DAYS_BE_13")
	public double beforeFactRemainDays13;
	@Column(name = "SP_FACT_REM_DAYS_BE_14")
	public double beforeFactRemainDays14;
	@Column(name = "SP_FACT_REM_DAYS_BE_15")
	public double beforeFactRemainDays15;
	@Column(name = "SP_FACT_REM_DAYS_BE_16")
	public double beforeFactRemainDays16;
	@Column(name = "SP_FACT_REM_DAYS_BE_17")
	public double beforeFactRemainDays17;
	@Column(name = "SP_FACT_REM_DAYS_BE_18")
	public double beforeFactRemainDays18;
	@Column(name = "SP_FACT_REM_DAYS_BE_19")
	public double beforeFactRemainDays19;
	@Column(name = "SP_FACT_REM_DAYS_BE_20")
	public double beforeFactRemainDays20;

	/** 特別休暇月別残数データ．実特別休暇．残数付与前.時間 */
	@Column(name = "SP_FACT_REM_MINUTES_BE_1")
	public Integer beforeFactRemainMinutes1;
	@Column(name = "SP_FACT_REM_MINUTES_BE_2")
	public Integer beforeFactRemainMinutes2;
	@Column(name = "SP_FACT_REM_MINUTES_BE_3")
	public Integer beforeFactRemainMinutes3;
	@Column(name = "SP_FACT_REM_MINUTES_BE_4")
	public Integer beforeFactRemainMinutes4;
	@Column(name = "SP_FACT_REM_MINUTES_BE_5")
	public Integer beforeFactRemainMinutes5;
	@Column(name = "SP_FACT_REM_MINUTES_BE_6")
	public Integer beforeFactRemainMinutes6;
	@Column(name = "SP_FACT_REM_MINUTES_BE_7")
	public Integer beforeFactRemainMinutes7;
	@Column(name = "SP_FACT_REM_MINUTES_BE_8")
	public Integer beforeFactRemainMinutes8;
	@Column(name = "SP_FACT_REM_MINUTES_BE_9")
	public Integer beforeFactRemainMinutes9;
	@Column(name = "SP_FACT_REM_MINUTES_BE_10")
	public Integer beforeFactRemainMinutes10;
	@Column(name = "SP_FACT_REM_MINUTES_BE_11")
	public Integer beforeFactRemainMinutes11;
	@Column(name = "SP_FACT_REM_MINUTES_BE_12")
	public Integer beforeFactRemainMinutes12;
	@Column(name = "SP_FACT_REM_MINUTES_BE_13")
	public Integer beforeFactRemainMinutes13;
	@Column(name = "SP_FACT_REM_MINUTES_BE_14")
	public Integer beforeFactRemainMinutes14;
	@Column(name = "SP_FACT_REM_MINUTES_BE_15")
	public Integer beforeFactRemainMinutes15;
	@Column(name = "SP_FACT_REM_MINUTES_BE_16")
	public Integer beforeFactRemainMinutes16;
	@Column(name = "SP_FACT_REM_MINUTES_BE_17")
	public Integer beforeFactRemainMinutes17;
	@Column(name = "SP_FACT_REM_MINUTES_BE_18")
	public Integer beforeFactRemainMinutes18;
	@Column(name = "SP_FACT_REM_MINUTES_BE_19")
	public Integer beforeFactRemainMinutes19;
	@Column(name = "SP_FACT_REM_MINUTES_BE_20")
	public Integer beforeFactRemainMinutes20;

	/** 特別休暇月別残数データ．特別休暇．残数付与後.日数 */
	@Column(name = "SP_REM_DAYS_AFTER_1")
	public Double afterRemainDays1;
	@Column(name = "SP_REM_DAYS_AFTER_2")
	public Double afterRemainDays2;
	@Column(name = "SP_REM_DAYS_AFTER_3")
	public Double afterRemainDays3;
	@Column(name = "SP_REM_DAYS_AFTER_4")
	public Double afterRemainDays4;
	@Column(name = "SP_REM_DAYS_AFTER_5")
	public Double afterRemainDays5;
	@Column(name = "SP_REM_DAYS_AFTER_6")
	public Double afterRemainDays6;
	@Column(name = "SP_REM_DAYS_AFTER_7")
	public Double afterRemainDays7;
	@Column(name = "SP_REM_DAYS_AFTER_8")
	public Double afterRemainDays8;
	@Column(name = "SP_REM_DAYS_AFTER_9")
	public Double afterRemainDays9;
	@Column(name = "SP_REM_DAYS_AFTER_10")
	public Double afterRemainDays10;
	@Column(name = "SP_REM_DAYS_AFTER_11")
	public Double afterRemainDays11;
	@Column(name = "SP_REM_DAYS_AFTER_12")
	public Double afterRemainDays12;
	@Column(name = "SP_REM_DAYS_AFTER_13")
	public Double afterRemainDays13;
	@Column(name = "SP_REM_DAYS_AFTER_14")
	public Double afterRemainDays14;
	@Column(name = "SP_REM_DAYS_AFTER_15")
	public Double afterRemainDays15;
	@Column(name = "SP_REM_DAYS_AFTER_16")
	public Double afterRemainDays16;
	@Column(name = "SP_REM_DAYS_AFTER_17")
	public Double afterRemainDays17;
	@Column(name = "SP_REM_DAYS_AFTER_18")
	public Double afterRemainDays18;
	@Column(name = "SP_REM_DAYS_AFTER_19")
	public Double afterRemainDays19;
	@Column(name = "SP_REM_DAYS_AFTER_20")
	public Double afterRemainDays20;

	/** 特別休暇月別残数データ．特別休暇．残数付与後.時間 */
	@Column(name = "SP_REM_MINUTES_AFTER_1")
	public Integer afterRemainMinutes1;
	@Column(name = "SP_REM_MINUTES_AFTER_2")
	public Integer afterRemainMinutes2;
	@Column(name = "SP_REM_MINUTES_AFTER_3")
	public Integer afterRemainMinutes3;
	@Column(name = "SP_REM_MINUTES_AFTER_4")
	public Integer afterRemainMinutes4;
	@Column(name = "SP_REM_MINUTES_AFTER_5")
	public Integer afterRemainMinutes5;
	@Column(name = "SP_REM_MINUTES_AFTER_6")
	public Integer afterRemainMinutes6;
	@Column(name = "SP_REM_MINUTES_AFTER_7")
	public Integer afterRemainMinutes7;
	@Column(name = "SP_REM_MINUTES_AFTER_8")
	public Integer afterRemainMinutes8;
	@Column(name = "SP_REM_MINUTES_AFTER_9")
	public Integer afterRemainMinutes9;
	@Column(name = "SP_REM_MINUTES_AFTER_10")
	public Integer afterRemainMinutes10;
	@Column(name = "SP_REM_MINUTES_AFTER_11")
	public Integer afterRemainMinutes11;
	@Column(name = "SP_REM_MINUTES_AFTER_12")
	public Integer afterRemainMinutes12;
	@Column(name = "SP_REM_MINUTES_AFTER_13")
	public Integer afterRemainMinutes13;
	@Column(name = "SP_REM_MINUTES_AFTER_14")
	public Integer afterRemainMinutes14;
	@Column(name = "SP_REM_MINUTES_AFTER_15")
	public Integer afterRemainMinutes15;
	@Column(name = "SP_REM_MINUTES_AFTER_16")
	public Integer afterRemainMinutes16;
	@Column(name = "SP_REM_MINUTES_AFTER_17")
	public Integer afterRemainMinutes17;
	@Column(name = "SP_REM_MINUTES_AFTER_18")
	public Integer afterRemainMinutes18;
	@Column(name = "SP_REM_MINUTES_AFTER_19")
	public Integer afterRemainMinutes19;
	@Column(name = "SP_REM_MINUTES_AFTER_20")
	public Integer afterRemainMinutes20;

	/** 特別休暇月別残数データ．実特別休暇．残数付与後. 日数 */
	@Column(name = "SP_FACT_REM_DAYS_AF_1")
	public Double afterFactRemainDays1;
	@Column(name = "SP_FACT_REM_DAYS_AF_2")
	public Double afterFactRemainDays2;
	@Column(name = "SP_FACT_REM_DAYS_AF_3")
	public Double afterFactRemainDays3;
	@Column(name = "SP_FACT_REM_DAYS_AF_4")
	public Double afterFactRemainDays4;
	@Column(name = "SP_FACT_REM_DAYS_AF_5")
	public Double afterFactRemainDays5;
	@Column(name = "SP_FACT_REM_DAYS_AF_6")
	public Double afterFactRemainDays6;
	@Column(name = "SP_FACT_REM_DAYS_AF_7")
	public Double afterFactRemainDays7;
	@Column(name = "SP_FACT_REM_DAYS_AF_8")
	public Double afterFactRemainDays8;
	@Column(name = "SP_FACT_REM_DAYS_AF_9")
	public Double afterFactRemainDays9;
	@Column(name = "SP_FACT_REM_DAYS_AF_10")
	public Double afterFactRemainDays10;
	@Column(name = "SP_FACT_REM_DAYS_AF_11")
	public Double afterFactRemainDays11;
	@Column(name = "SP_FACT_REM_DAYS_AF_12")
	public Double afterFactRemainDays12;
	@Column(name = "SP_FACT_REM_DAYS_AF_13")
	public Double afterFactRemainDays13;
	@Column(name = "SP_FACT_REM_DAYS_AF_14")
	public Double afterFactRemainDays14;
	@Column(name = "SP_FACT_REM_DAYS_AF_15")
	public Double afterFactRemainDays15;
	@Column(name = "SP_FACT_REM_DAYS_AF_16")
	public Double afterFactRemainDays16;
	@Column(name = "SP_FACT_REM_DAYS_AF_17")
	public Double afterFactRemainDays17;
	@Column(name = "SP_FACT_REM_DAYS_AF_18")
	public Double afterFactRemainDays18;
	@Column(name = "SP_FACT_REM_DAYS_AF_19")
	public Double afterFactRemainDays19;
	@Column(name = "SP_FACT_REM_DAYS_AF_20")
	public Double afterFactRemainDays20;

	/** 特別休暇月別残数データ．実特別休暇．残数付与後. 時間 */
	@Column(name = "SP_FACT_REM_MINUTES_AF_1")
	public Integer afterFactRemainMinutes1;
	@Column(name = "SP_FACT_REM_MINUTES_AF_2")
	public Integer afterFactRemainMinutes2;
	@Column(name = "SP_FACT_REM_MINUTES_AF_3")
	public Integer afterFactRemainMinutes3;
	@Column(name = "SP_FACT_REM_MINUTES_AF_4")
	public Integer afterFactRemainMinutes4;
	@Column(name = "SP_FACT_REM_MINUTES_AF_5")
	public Integer afterFactRemainMinutes5;
	@Column(name = "SP_FACT_REM_MINUTES_AF_6")
	public Integer afterFactRemainMinutes6;
	@Column(name = "SP_FACT_REM_MINUTES_AF_7")
	public Integer afterFactRemainMinutes7;
	@Column(name = "SP_FACT_REM_MINUTES_AF_8")
	public Integer afterFactRemainMinutes8;
	@Column(name = "SP_FACT_REM_MINUTES_AF_9")
	public Integer afterFactRemainMinutes9;
	@Column(name = "SP_FACT_REM_MINUTES_AF_10")
	public Integer afterFactRemainMinutes10;
	@Column(name = "SP_FACT_REM_MINUTES_AF_11")
	public Integer afterFactRemainMinutes11;
	@Column(name = "SP_FACT_REM_MINUTES_AF_12")
	public Integer afterFactRemainMinutes12;
	@Column(name = "SP_FACT_REM_MINUTES_AF_13")
	public Integer afterFactRemainMinutes13;
	@Column(name = "SP_FACT_REM_MINUTES_AF_14")
	public Integer afterFactRemainMinutes14;
	@Column(name = "SP_FACT_REM_MINUTES_AF_15")
	public Integer afterFactRemainMinutes15;
	@Column(name = "SP_FACT_REM_MINUTES_AF_16")
	public Integer afterFactRemainMinutes16;
	@Column(name = "SP_FACT_REM_MINUTES_AF_17")
	public Integer afterFactRemainMinutes17;
	@Column(name = "SP_FACT_REM_MINUTES_AF_18")
	public Integer afterFactRemainMinutes18;
	@Column(name = "SP_FACT_REM_MINUTES_AF_19")
	public Integer afterFactRemainMinutes19;
	@Column(name = "SP_FACT_REM_MINUTES_AF_20")
	public Integer afterFactRemainMinutes20;

	/** 特別休暇月別残数データ．特別休暇．未消化数．未消化日数.未消化日数 */
	@Column(name = "SP_NOT_USED_DAYS_1")
	public double notUseDays1;
	@Column(name = "SP_NOT_USED_DAYS_2")
	public double notUseDays2;
	@Column(name = "SP_NOT_USED_DAYS_3")
	public double notUseDays3;
	@Column(name = "SP_NOT_USED_DAYS_4")
	public double notUseDays4;
	@Column(name = "SP_NOT_USED_DAYS_5")
	public double notUseDays5;
	@Column(name = "SP_NOT_USED_DAYS_6")
	public double notUseDays6;
	@Column(name = "SP_NOT_USED_DAYS_7")
	public double notUseDays7;
	@Column(name = "SP_NOT_USED_DAYS_8")
	public double notUseDays8;
	@Column(name = "SP_NOT_USED_DAYS_9")
	public double notUseDays9;
	@Column(name = "SP_NOT_USED_DAYS_10")
	public double notUseDays10;
	@Column(name = "SP_NOT_USED_DAYS_11")
	public double notUseDays11;
	@Column(name = "SP_NOT_USED_DAYS_12")
	public double notUseDays12;
	@Column(name = "SP_NOT_USED_DAYS_13")
	public double notUseDays13;
	@Column(name = "SP_NOT_USED_DAYS_14")
	public double notUseDays14;
	@Column(name = "SP_NOT_USED_DAYS_15")
	public double notUseDays15;
	@Column(name = "SP_NOT_USED_DAYS_16")
	public double notUseDays16;
	@Column(name = "SP_NOT_USED_DAYS_17")
	public double notUseDays17;
	@Column(name = "SP_NOT_USED_DAYS_18")
	public double notUseDays18;
	@Column(name = "SP_NOT_USED_DAYS_19")
	public double notUseDays19;
	@Column(name = "SP_NOT_USED_DAYS_20")
	public double notUseDays20;

	/** 特別休暇月別残数データ．特別休暇．未消化数．未消化時間.未消化時間 */
	@Column(name = "SP_NOT_USED_MINUTES_1")
	public Integer notUseMinutes1;
	@Column(name = "SP_NOT_USED_MINUTES_2")
	public Integer notUseMinutes2;
	@Column(name = "SP_NOT_USED_MINUTES_3")
	public Integer notUseMinutes3;
	@Column(name = "SP_NOT_USED_MINUTES_4")
	public Integer notUseMinutes4;
	@Column(name = "SP_NOT_USED_MINUTES_5")
	public Integer notUseMinutes5;
	@Column(name = "SP_NOT_USED_MINUTES_6")
	public Integer notUseMinutes6;
	@Column(name = "SP_NOT_USED_MINUTES_7")
	public Integer notUseMinutes7;
	@Column(name = "SP_NOT_USED_MINUTES_8")
	public Integer notUseMinutes8;
	@Column(name = "SP_NOT_USED_MINUTES_9")
	public Integer notUseMinutes9;
	@Column(name = "SP_NOT_USED_MINUTES_10")
	public Integer notUseMinutes10;
	@Column(name = "SP_NOT_USED_MINUTES_11")
	public Integer notUseMinutes11;
	@Column(name = "SP_NOT_USED_MINUTES_12")
	public Integer notUseMinutes12;
	@Column(name = "SP_NOT_USED_MINUTES_13")
	public Integer notUseMinutes13;
	@Column(name = "SP_NOT_USED_MINUTES_14")
	public Integer notUseMinutes14;
	@Column(name = "SP_NOT_USED_MINUTES_15")
	public Integer notUseMinutes15;
	@Column(name = "SP_NOT_USED_MINUTES_16")
	public Integer notUseMinutes16;
	@Column(name = "SP_NOT_USED_MINUTES_17")
	public Integer notUseMinutes17;
	@Column(name = "SP_NOT_USED_MINUTES_18")
	public Integer notUseMinutes18;
	@Column(name = "SP_NOT_USED_MINUTES_19")
	public Integer notUseMinutes19;
	@Column(name = "SP_NOT_USED_MINUTES_20")
	public Integer notUseMinutes20;

	/** 付与区分 */
	@Column(name = "SP_GRANT_ATR_1")
	public int grantAtr1;
	@Column(name = "SP_GRANT_ATR_2")
	public int grantAtr2;
	@Column(name = "SP_GRANT_ATR_3")
	public int grantAtr3;
	@Column(name = "SP_GRANT_ATR_4")
	public int grantAtr4;
	@Column(name = "SP_GRANT_ATR_5")
	public int grantAtr5;
	@Column(name = "SP_GRANT_ATR_6")
	public int grantAtr6;
	@Column(name = "SP_GRANT_ATR_7")
	public int grantAtr7;
	@Column(name = "SP_GRANT_ATR_8")
	public int grantAtr8;
	@Column(name = "SP_GRANT_ATR_9")
	public int grantAtr9;
	@Column(name = "SP_GRANT_ATR_10")
	public int grantAtr10;
	@Column(name = "SP_GRANT_ATR_11")
	public int grantAtr11;
	@Column(name = "SP_GRANT_ATR_12")
	public int grantAtr12;
	@Column(name = "SP_GRANT_ATR_13")
	public int grantAtr13;
	@Column(name = "SP_GRANT_ATR_14")
	public int grantAtr14;
	@Column(name = "SP_GRANT_ATR_15")
	public int grantAtr15;
	@Column(name = "SP_GRANT_ATR_16")
	public int grantAtr16;
	@Column(name = "SP_GRANT_ATR_17")
	public int grantAtr17;
	@Column(name = "SP_GRANT_ATR_18")
	public int grantAtr18;
	@Column(name = "SP_GRANT_ATR_19")
	public int grantAtr19;
	@Column(name = "SP_GRANT_ATR_20")
	public int grantAtr20;
	
	/** 特別休暇月別残数データ．特別休暇付与情報.付与日数 */
	@Column(name = "SP_GRANT_DAYS_1")
	public Double grantDays1;
	@Column(name = "SP_GRANT_DAYS_2")
	public Double grantDays2;
	@Column(name = "SP_GRANT_DAYS_3")
	public Double grantDays3;
	@Column(name = "SP_GRANT_DAYS_4")
	public Double grantDays4;
	@Column(name = "SP_GRANT_DAYS_5")
	public Double grantDays5;
	@Column(name = "SP_GRANT_DAYS_6")
	public Double grantDays6;
	@Column(name = "SP_GRANT_DAYS_7")
	public Double grantDays7;
	@Column(name = "SP_GRANT_DAYS_8")
	public Double grantDays8;
	@Column(name = "SP_GRANT_DAYS_9")
	public Double grantDays9;
	@Column(name = "SP_GRANT_DAYS_10")
	public Double grantDays10;
	@Column(name = "SP_GRANT_DAYS_11")
	public Double grantDays11;
	@Column(name = "SP_GRANT_DAYS_12")
	public Double grantDays12;
	@Column(name = "SP_GRANT_DAYS_13")
	public Double grantDays13;
	@Column(name = "SP_GRANT_DAYS_14")
	public Double grantDays14;
	@Column(name = "SP_GRANT_DAYS_15")
	public Double grantDays15;
	@Column(name = "SP_GRANT_DAYS_16")
	public Double grantDays16;
	@Column(name = "SP_GRANT_DAYS_17")
	public Double grantDays17;
	@Column(name = "SP_GRANT_DAYS_18")
	public Double grantDays18;
	@Column(name = "SP_GRANT_DAYS_19")
	public Double grantDays19;
	@Column(name = "SP_GRANT_DAYS_20")
	public Double grantDays20;

	/* KRCDT_MON_DAYOFF_REMAIN */
	
	/** 発生日数 */
	@Column(name = "DO_OCCURRED_DAYS")
	public double dayOffOccurredDays;
	/** 発生時間 */
	@Column(name = "DO_OCCURRED_TIMES")
	public Integer dayOffOccurredTimes;
	/** 使用日数 */
	@Column(name = "DO_USED_DAYS")
	public double dayOffUsedDays;
	/** 使用時間 */
	@Column(name = "DO_USED_MINUTES")
	public Integer dayOffUsedMinutes;
	/** 残日数 */
	@Column(name = "DO_REMAINING_DAYS")
	public double dayOffRemainingDays;
	/** 残時間 */
	@Column(name = "DO_REMAINING_MINUTES")
	public Integer dayOffRemainingMinutes;
	/** 繰越日数 */
	@Column(name = "DO_CARRYFORWARD_DAYS")
	public double dayOffCarryforwardDays;
	/** 繰越時間 */
	@Column(name = "DO_CARRYFORWARD_MINUTES")
	public Integer dayOffCarryforwardMinutes;
	/** 未消化日数 */
	@Column(name = "DO_UNUSED_DAYS")
	public double dayOffUnUsedDays;
	/** 未消化時間 */
	@Column(name = "DO_UNUSED_TIMES")
	public Integer dayOffUnUsedTimes;

	/* KRCDT_MON_SUBOFHD_REMAIN */
	
	/** 発生日数 */
	@Column(name = "SB_OCCURRED_DAYS")
	public double subofHdOccurredDays;
	/** 使用日数 */
	@Column(name = "SB_USED_DAYS")
	public double subofHdUsedDays;
	/** 残日数 */
	@Column(name = "SB_REMAINING_DAYS")
	public double subofHdRemainingDays;
	/** 繰越日数 */
	@Column(name = "SB_CARRYFORWARD_DAYS")
	public double subofHdCarryForWardDays;
	/** 未消化日数 */
	@Column(name = "SB_UNUSED_DAYS")
	public double subofHdUnUsedDays;

	/* KRCDT_MON_SUBOFHD_REMAIN */
	
	/** 使用日数 */
	@Column(name = "CH_USED_DAYS")
	public double childUsedDays;
	/** 使用日数付与前 */
	@Column(name = "CH_USED_DAYS_BEFORE")
	public double childUsedDaysBefore;
	/** 使用日数付与後 */
	@Column(name = "CH_USED_DAYS_AFTER")
	public Double childUsedDaysAfter;
	/** 使用時間 */
	@Column(name = "CH_USED_MINUTES")
	public Integer childUsedMinutes;
	/** 使用時間付与前 */
	@Column(name = "CH_USED_MINUTES_BEFORE")
	public Integer childUsedMinutesBefore;
	/** 使用時間付与後 */
	@Column(name = "CH_USED_MINUTES_AFTER")
	public Integer childUsedMinutesAfter;
	
	/* KRCDT_MON_CARE_HD_REMAIN */

	/** 使用日数 */
	@Column(name = "CA_USED_DAYS")
	public double careUsedDays;
	/** 使用日数付与前 */
	@Column(name = "CA_USED_DAYS_BEFORE")
	public double careUsedDaysBefore;
	/** 使用日数付与後 */
	@Column(name = "CA_USED_DAYS_AFTER")
	public Double careUsedDaysAfter;
	/** 使用時間 */
	@Column(name = "CA_USED_MINUTES")
	public Integer careUsedMinutes;
	/** 使用時間付与前 */
	@Column(name = "CA_USED_MINUTES_BEFORE")
	public Integer careUsedMinutesBefore;
	/** 使用時間付与後 */
	@Column(name = "CA_USED_MINUTES_AFTER")
	public Integer careUsedMinutesAfter;
	
	@Override
	protected Object getKey() {
		return krcdtMonRemainPk;
	}

	public KrcdtMonRemain(){
		this.startDate = GeneralDate.min();
		this.endDate = GeneralDate.max();
	}
	
	public MonthMergeKey toDomainKey() {
		MonthMergeKey key = new MonthMergeKey();
		key.setEmployeeId(this.krcdtMonRemainPk.getEmployeeId());
		key.setYearMonth(new YearMonth(this.krcdtMonRemainPk.getYearMonth()));
		key.setClosureId(EnumAdaptor.valueOf(this.krcdtMonRemainPk.getClosureId(), ClosureId.class));
		key.setClosureDate(new ClosureDate(this.krcdtMonRemainPk.getClosureDay(),
			(this.krcdtMonRemainPk.getIsLastDay() == 1)));
		return key;
	}
	
	public void toEntityRemainMerge(RemainMerge domain) {
		this.toEntityMonAnnleaRemain(domain.getAnnLeaRemNumEachMonth());
		this.toEntityRsvLeaRemNumEachMonth(domain.getRsvLeaRemNumEachMonth());
		this.toEntitySpeRemain(domain.getSpecialHolidayRemainDataMerge());
		this.toEntityDayOffRemainDayAndTimes(domain.getMonthlyDayoffRemainData());
		this.toEntityAbsenceLeaveRemainData(domain.getAbsenceLeaveRemainData());
		this.toEntityCareRemainData(domain.getMonCareHdRemain());
		this.toEntityChildRemainData(domain.getMonChildHdRemain());
	}

	public RemainMerge toDomain(){
		RemainMerge domains = new RemainMerge();
		domains.setMonthMergeKey(new MonthMergeKey(this.krcdtMonRemainPk.getEmployeeId(), 
								new YearMonth(this.getKrcdtMonRemainPk().getYearMonth()), 
								EnumAdaptor.valueOf(this.getKrcdtMonRemainPk().getClosureId(), ClosureId.class), 
								new ClosureDate(this.getKrcdtMonRemainPk().getClosureDay(), 
										this.getKrcdtMonRemainPk().getIsLastDay() == 1 ? true : false)));
		domains.setAnnLeaRemNumEachMonth(this.toDomainAnnLeaRemNumEachMonth());
		domains.setRsvLeaRemNumEachMonth(this.toDomainRsvLeaRemNumEachMonth());
		domains.setSpecialHolidayRemainDataMerge(this.toDomainSpecialHolidayRemainData());
		domains.setMonthlyDayoffRemainData(this.toDomainMonthlyDayoffRemainData());
		domains.setAbsenceLeaveRemainData(this.toDomainAbsenceLeaveRemainData());
		domains.setMonCareHdRemain(this.toDomainMonCareHdRemain());
		domains.setMonChildHdRemain(this.toDomainMonChildHdRemain());
		return domains;
	}
	
	/** KRCDT_MON_ANNLEA_REMAIN - エンティティ：年休月別残数データ */
	public void toEntityMonAnnleaRemain(AnnLeaRemNumEachMonth domain) {
		
		this.deleteMonAnnleaRemain();
		if (domain == null) return;
		
		val normal = domain.getAnnualLeave();
		val normalUsed = normal.getUsedNumber();
		val real = domain.getRealAnnualLeave();
		val realUsed = real.getUsedNumber();

		this.closureStatus = domain.getClosureStatus().value;
		this.startDate = domain.getClosurePeriod().start();
		this.endDate = domain.getClosurePeriod().end();

		// 年休：使用数
		this.annleaUsedDays = normalUsed.getUsedDays().getUsedDays().v();
		this.annleaUsedDaysBefore = normalUsed.getUsedDays().getUsedDaysBeforeGrant().v();
		if (normalUsed.getUsedDays().getUsedDaysAfterGrant().isPresent()) {
			this.annleaUsedDaysAfter = normalUsed.getUsedDays().getUsedDaysAfterGrant().get().v();
		}
		if (normalUsed.getUsedTime().isPresent()) {
			val normalUsedTime = normalUsed.getUsedTime().get();
			this.annleaUsedMinutes = normalUsedTime.getUsedTime().v();
			this.annleaUsedMinutesBefore = normalUsedTime.getUsedTimeBeforeGrant().v();
			if (normalUsedTime.getUsedTimeAfterGrant().isPresent()) {
				this.annleaUsedMinutesAfter = normalUsedTime.getUsedTimeAfterGrant().get().v();
			}
			this.annleaUsedTimes = normalUsedTime.getUsedTimes().v();
		}

		// 実年休：使用数
		this.annleaFactUsedDays = realUsed.getUsedDays().getUsedDays().v();
		this.annleaFactUsedDaysBefore = realUsed.getUsedDays().getUsedDaysBeforeGrant().v();
		if (realUsed.getUsedDays().getUsedDaysAfterGrant().isPresent()) {
			this.annleaFactUsedDaysAfter = realUsed.getUsedDays().getUsedDaysAfterGrant().get().v();
		}
		if (realUsed.getUsedTime().isPresent()) {
			val realUsedTime = realUsed.getUsedTime().get();
			this.annleaFactUsedMinutes = realUsedTime.getUsedTime().v();
			this.annleaFactUsedMinutesBefore = realUsedTime.getUsedTimeBeforeGrant().v();
			if (realUsedTime.getUsedTimeAfterGrant().isPresent()) {
				this.annleaFactUsedMinutesAfter = realUsedTime.getUsedTimeAfterGrant().get().v();
			}
			this.annleaFactUsedTimes = realUsedTime.getUsedTimes().v();
		}

		// 年休：残数
		this.annleaRemainingDays = normal.getRemainingNumber().getTotalRemainingDays().v();
		if (normal.getRemainingNumber().getTotalRemainingTime().isPresent()) {
			this.annleaRemainingMinutes = normal.getRemainingNumber().getTotalRemainingTime().get().v();
		}
		this.annleaRemainingDaysBefore = normal.getRemainingNumberBeforeGrant().getTotalRemainingDays().v();
		if (normal.getRemainingNumberBeforeGrant().getTotalRemainingTime().isPresent()) {
			this.annleaRemainingMinutesBefore = normal.getRemainingNumberBeforeGrant().getTotalRemainingTime().get()
					.v();
		}
		if (normal.getRemainingNumberAfterGrant().isPresent()) {
			val normalRemainAfter = normal.getRemainingNumberAfterGrant().get();
			this.annleaRemainingDaysAfter = normalRemainAfter.getTotalRemainingDays().v();
			if (normalRemainAfter.getTotalRemainingTime().isPresent()) {
				this.annleaRemainingMinutesAfter = normalRemainAfter.getTotalRemainingTime().get().v();
			}
		}

		// 実年休：残数
		this.annleaFactRemainingDays = real.getRemainingNumber().getTotalRemainingDays().v();
		if (real.getRemainingNumber().getTotalRemainingTime().isPresent()) {
			this.annleaFactRemainingMinutes = real.getRemainingNumber().getTotalRemainingTime().get().v();
		}
		this.annleaFactRemainingDaysBefore = real.getRemainingNumberBeforeGrant().getTotalRemainingDays().v();
		if (real.getRemainingNumberBeforeGrant().getTotalRemainingTime().isPresent()) {
			this.annleaFactRemainingMinutesBefore = real.getRemainingNumberBeforeGrant().getTotalRemainingTime().get()
					.v();
		}
		if (real.getRemainingNumberAfterGrant().isPresent()) {
			val realRemainAfter = real.getRemainingNumberAfterGrant().get();
			this.annleaFactRemainingDaysAfter = realRemainAfter.getTotalRemainingDays().v();
			if (realRemainAfter.getTotalRemainingTime().isPresent()) {
				this.annleaFactRemainingMinutesAfter = realRemainAfter.getTotalRemainingTime().get().v();
			}
		}

		// 年休：未消化数
		val normalUndigest = normal.getUndigestedNumber();
		this.annleaUnusedDays = normalUndigest.getUndigestedDays().getUndigestedDays().v();
		if (normalUndigest.getUndigestedTime().isPresent()) {
			this.annleaUnusedMinutes = normalUndigest.getUndigestedTime().get().getUndigestedTime().v();
		}

		// 出勤率日数
		val attendanceRateDays = domain.getAttendanceRateDays();
		this.annleaPredeterminedDays = attendanceRateDays.getPrescribedDays().v().intValue();
		this.annleaLaborDays = attendanceRateDays.getWorkingDays().v().intValue();
		this.annleaDeductionDays = attendanceRateDays.getDeductedDays().v().intValue();

		// 半日年休
		if (domain.getHalfDayAnnualLeave().isPresent()) {
			val normalHalf = domain.getHalfDayAnnualLeave().get();
			this.annleaHalfUsedTimes = normalHalf.getUsedNum().getTimes().v();
			this.annleaHalfUsedTimesBefore = normalHalf.getUsedNum().getTimesBeforeGrant().v();
			if (normalHalf.getUsedNum().getTimesAfterGrant().isPresent()) {
				this.annleaHalfUsedTimesAfter = normalHalf.getUsedNum().getTimesAfterGrant().get().v();
			}
			this.annleaHalfRemainingTimes = normalHalf.getRemainingNum().getTimes().v();
			this.annleaHalfRemainingTimesBefore = normalHalf.getRemainingNum().getTimesBeforeGrant().v();
			if (normalHalf.getRemainingNum().getTimesAfterGrant().isPresent()) {
				this.annleaHalfRemainingTimesAfter = normalHalf.getRemainingNum().getTimesAfterGrant().get().v();
			}
		}

		// 実半日年休
		if (domain.getRealHalfDayAnnualLeave().isPresent()) {
			val realHalf = domain.getRealHalfDayAnnualLeave().get();
			this.annleaFactHalfUsedTimes = realHalf.getUsedNum().getTimes().v();
			this.annleaFactHalfUsedTimesBefore = realHalf.getUsedNum().getTimesBeforeGrant().v();
			if (realHalf.getUsedNum().getTimesAfterGrant().isPresent()) {
				this.annleaFactHalfUsedTimesAfter = realHalf.getUsedNum().getTimesAfterGrant().get().v();
			}
			this.annleaFactHalfRemainingTimes = realHalf.getRemainingNum().getTimes().v();
			this.annleaFactHalfRemainingTimesBefore = realHalf.getRemainingNum().getTimesBeforeGrant().v();
			if (realHalf.getRemainingNum().getTimesAfterGrant().isPresent()) {
				this.annleaFactHalfRemainingTimesAfter = realHalf.getRemainingNum().getTimesAfterGrant().get().v();
			}
		}

		// 上限残時間
		if (domain.getMaxRemainingTime().isPresent()) {
			val normalMax = domain.getMaxRemainingTime().get();
			this.annleaTimeRemainingMinutes = normalMax.getTime().v();
			this.annleaTimeRemainingMinutesBefore = normalMax.getTimeBeforeGrant().v();
			if (normalMax.getTimeAfterGrant().isPresent()) {
				this.annleaTimeRemainingMinutesAfter = normalMax.getTimeAfterGrant().get().v();
			}
		}

		// 実上限残時間
		if (domain.getRealMaxRemainingTime().isPresent()) {
			val realMax = domain.getRealMaxRemainingTime().get();
			this.annleaFactTimeRemainingMinutes = realMax.getTime().v();
			this.annleaFactTimeRemainingMinutesBefore = realMax.getTimeBeforeGrant().v();
			if (realMax.getTimeAfterGrant().isPresent()) {
				this.annleaFactTimeRemainingMinutesAfter = realMax.getTimeAfterGrant().get().v();
			}
		}

		// 付与区分
		this.annleaGrantAtr = (domain.isGrantAtr() ? 1 : 0);

		// 年休付与情報
		if (domain.getAnnualLeaveGrant().isPresent()) {
			val grantInfo = domain.getAnnualLeaveGrant().get();
			this.annleaGrantDays = grantInfo.getGrantDays().v();
			this.annleaGrantPredeterminedDays = grantInfo.getGrantPrescribedDays().v();
			this.annleaGrantLaborDays = grantInfo.getGrantWorkingDays().v();
			this.annleaGrantDeductionDays = grantInfo.getGrantDeductedDays().v();
			this.annleaDeductionDaysBefore = grantInfo.getDeductedDaysBeforeGrant().v();
			this.annleaDeductionDaysAfter = grantInfo.getDeductedDaysAfterGrant().v();
			this.annleaAttendanceRate = grantInfo.getAttendanceRate().v() == null? null: grantInfo.getAttendanceRate().v().doubleValue();
		}
	}

	public void deleteMonAnnleaRemain(){
		this.annleaUsedDays = 0.0;
		this.annleaUsedDaysBefore = 0.0;
		this.annleaUsedDaysAfter = null;
		this.annleaUsedMinutes = null;
		this.annleaUsedMinutesBefore = null;
		this.annleaUsedMinutesAfter = null;
		this.annleaUsedTimes = null;
		this.annleaFactUsedDays = 0.0;
		this.annleaFactUsedDaysBefore = 0.0;
		this.annleaFactUsedDaysAfter = null;
		this.annleaFactUsedMinutes = null;
		this.annleaFactUsedMinutesBefore = null;
		this.annleaFactUsedMinutesAfter = null;
		this.annleaFactUsedTimes = null;
		this.annleaRemainingDays = 0.0;
		this.annleaRemainingMinutes = null;
		this.annleaFactRemainingDays = 0.0;
		this.annleaFactRemainingMinutes = null;
		this.annleaRemainingDaysBefore = 0.0;
		this.annleaRemainingMinutesBefore = null;
		this.annleaFactRemainingDaysBefore = 0.0;
		this.annleaFactRemainingMinutesBefore = null;
		this.annleaRemainingDaysAfter = null;
		this.annleaRemainingMinutesAfter = null;
		this.annleaFactRemainingDaysAfter = null;
		this.annleaFactRemainingMinutesAfter = null;
		this.annleaUnusedDays = 0.0;
		this.annleaUnusedMinutes = null;
		this.annleaPredeterminedDays = 0;
		this.annleaLaborDays = 0;
		this.annleaDeductionDays = 0;
		this.annleaHalfUsedTimes = null;
		this.annleaHalfUsedTimesBefore = null;
		this.annleaHalfUsedTimesAfter = null;
		this.annleaHalfRemainingTimes = null;
		this.annleaHalfRemainingTimesBefore = null;
		this.annleaHalfRemainingTimesAfter = null;
		this.annleaFactHalfUsedTimes = null;
		this.annleaFactHalfUsedTimesBefore = null;
		this.annleaFactHalfUsedTimesAfter = null;
		this.annleaFactHalfRemainingTimes = null;
		this.annleaFactHalfRemainingTimesBefore = null;
		this.annleaFactHalfRemainingTimesAfter = null;
		this.annleaTimeRemainingMinutes = null;
		this.annleaTimeRemainingMinutesBefore = null;
		this.annleaTimeRemainingMinutesAfter = null;
		this.annleaFactTimeRemainingMinutes = null;
		this.annleaFactTimeRemainingMinutesBefore = null;
		this.annleaFactTimeRemainingMinutesAfter = null;
		this.annleaGrantAtr = 0;
		this.annleaGrantDays = null;
		this.annleaGrantPredeterminedDays = null;
		this.annleaGrantLaborDays = null;
		this.annleaGrantDeductionDays = null;
		this.annleaDeductionDaysBefore = null;
		this.annleaDeductionDaysAfter = null;
		this.annleaAttendanceRate = null;
	}
	
	/** KRCDT_MON_RSVLEA_REMAIN */
	public void toEntityRsvLeaRemNumEachMonth(RsvLeaRemNumEachMonth domain) {

		this.deleteRsvLeaRemNumEachMonth();
		if (domain == null) return;
		
		val normal = domain.getReserveLeave();
		val normalUsed = normal.getUsedNumber();
		val real = domain.getRealReserveLeave();
		val realUsed = real.getUsedNumber();

		this.closureStatus = domain.getClosureStatus().value;
		this.startDate = domain.getClosurePeriod().start();
		this.endDate = domain.getClosurePeriod().end();
		
		// 積立年休：使用数
		this.rsvleaUsedDays = normalUsed.getUsedDays().v();
		this.rsvleaUsedDaysBefore = normalUsed.getUsedDaysBeforeGrant().v();
		if (normalUsed.getUsedDaysAfterGrant().isPresent()) {
			this.rsvleaUsedDaysAfter = normalUsed.getUsedDaysAfterGrant().get().v();
		}

		// 実積立年休：使用数
		this.rsvleaFactUsedDays = realUsed.getUsedDays().v();
		this.rsvleaFactUsedDaysBefore = realUsed.getUsedDaysBeforeGrant().v();
		if (realUsed.getUsedDaysAfterGrant().isPresent()) {
			this.rsvleaFactUsedDaysAfter = realUsed.getUsedDaysAfterGrant().get().v();
		}

		// 積立年休：残数
		this.rsvleaRemainingDays = normal.getRemainingNumber().getTotalRemainingDays().v();
		this.rsvleaRemainingDaysBefore = normal.getRemainingNumberBeforeGrant().getTotalRemainingDays().v();
		if (normal.getRemainingNumberAfterGrant().isPresent()) {
			val normalRemainAfter = normal.getRemainingNumberAfterGrant().get();
			this.rsvleaRemainingDaysAfter = normalRemainAfter.getTotalRemainingDays().v();
		}

		// 実積立年休：残数
		this.rsvleaFactRemainingDays = real.getRemainingNumber().getTotalRemainingDays().v();
		this.rsvleaFactRemainingDaysBefore = real.getRemainingNumberBeforeGrant().getTotalRemainingDays().v();
		if (real.getRemainingNumberAfterGrant().isPresent()) {
			val realRemainAfter = real.getRemainingNumberAfterGrant().get();
			this.rsvleaFactRemainingDaysAfter = realRemainAfter.getTotalRemainingDays().v();
		}

		// 積立年休：未消化数
		val normalUndigest = normal.getUndigestedNumber();
		this.rsvleaNotUsedDays = normalUndigest.getUndigestedDays().v();

		// 付与区分
		this.rsvleaGrantAtr = (domain.isGrantAtr() ? 1 : 0);

		// 積立年休付与情報
		if (domain.getReserveLeaveGrant().isPresent()) {
			val grantInfo = domain.getReserveLeaveGrant().get();
			this.rsvleaGrantDays = grantInfo.getGrantDays().v();
		}
	}
	
	public void deleteRsvLeaRemNumEachMonth(){
		this.rsvleaUsedDays = 0.0;
		this.rsvleaUsedDaysBefore = 0.0;
		this.rsvleaUsedDaysAfter = null;
		this.rsvleaFactUsedDays = 0.0;
		this.rsvleaFactUsedDaysBefore = 0.0;
		this.rsvleaFactUsedDaysAfter = null;
		this.rsvleaRemainingDays = 0.0;
		this.rsvleaFactRemainingDays = 0.0;
		this.rsvleaRemainingDaysBefore = 0.0;
		this.rsvleaFactRemainingDaysBefore = 0.0;
		this.rsvleaRemainingDaysAfter = null;
		this.rsvleaFactRemainingDaysAfter = null;
		this.rsvleaNotUsedDays = 0.0;
		this.rsvleaGrantAtr = 0;
		this.rsvleaGrantDays = null;
	}

	/** KRCDT_MON_SP_REMAIN **/
	public void toEntitySpeRemain(SpecialHolidayRemainDataMerge domain) {
		this.toEntityMonthSpeRemain1(domain.getSpecialHolidayRemainData1());
		this.toEntityMonthSpeRemain2(domain.getSpecialHolidayRemainData2());
		this.toEntityMonthSpeRemain3(domain.getSpecialHolidayRemainData3());
		this.toEntityMonthSpeRemain4(domain.getSpecialHolidayRemainData4());
		this.toEntityMonthSpeRemain5(domain.getSpecialHolidayRemainData5());
		this.toEntityMonthSpeRemain6(domain.getSpecialHolidayRemainData6());
		this.toEntityMonthSpeRemain7(domain.getSpecialHolidayRemainData7());
		this.toEntityMonthSpeRemain8(domain.getSpecialHolidayRemainData8());
		this.toEntityMonthSpeRemain9(domain.getSpecialHolidayRemainData9());
		this.toEntityMonthSpeRemain10(domain.getSpecialHolidayRemainData10());
		this.toEntityMonthSpeRemain11(domain.getSpecialHolidayRemainData11());
		this.toEntityMonthSpeRemain12(domain.getSpecialHolidayRemainData12());
		this.toEntityMonthSpeRemain13(domain.getSpecialHolidayRemainData13());
		this.toEntityMonthSpeRemain14(domain.getSpecialHolidayRemainData14());
		this.toEntityMonthSpeRemain15(domain.getSpecialHolidayRemainData15());
		this.toEntityMonthSpeRemain16(domain.getSpecialHolidayRemainData16());
		this.toEntityMonthSpeRemain17(domain.getSpecialHolidayRemainData17());
		this.toEntityMonthSpeRemain18(domain.getSpecialHolidayRemainData18());
		this.toEntityMonthSpeRemain19(domain.getSpecialHolidayRemainData19());
		this.toEntityMonthSpeRemain20(domain.getSpecialHolidayRemainData20());
	}
	
	public void toEntitySpeRemain(SpecialHolidayRemainData domain) {
		switch (domain.getSpecialHolidayCd()){
		case 1:		this.toEntityMonthSpeRemain1(domain);	break;
		case 2:		this.toEntityMonthSpeRemain2(domain);	break;
		case 3:		this.toEntityMonthSpeRemain3(domain);	break;
		case 4:		this.toEntityMonthSpeRemain4(domain);	break;
		case 5:		this.toEntityMonthSpeRemain5(domain);	break;
		case 6:		this.toEntityMonthSpeRemain6(domain);	break;
		case 7:		this.toEntityMonthSpeRemain7(domain);	break;
		case 8:		this.toEntityMonthSpeRemain8(domain);	break;
		case 9:		this.toEntityMonthSpeRemain9(domain);	break;
		case 10:	this.toEntityMonthSpeRemain10(domain);	break;
		case 11:	this.toEntityMonthSpeRemain11(domain);	break;
		case 12:	this.toEntityMonthSpeRemain12(domain);	break;
		case 13:	this.toEntityMonthSpeRemain13(domain);	break;
		case 14:	this.toEntityMonthSpeRemain14(domain);	break;
		case 15:	this.toEntityMonthSpeRemain15(domain);	break;
		case 16:	this.toEntityMonthSpeRemain16(domain);	break;
		case 17:	this.toEntityMonthSpeRemain17(domain);	break;
		case 18:	this.toEntityMonthSpeRemain18(domain);	break;
		case 19:	this.toEntityMonthSpeRemain19(domain);	break;
		case 20:	this.toEntityMonthSpeRemain20(domain);	break;
		default:	break;
		}
	}
	
	public void toEntitySpeRemains(List<SpecialHolidayRemainData> domains) {
		this.deleteAllSpeRemains();
		for (val domain : domains) this.toEntitySpeRemain(domain);
	}
	
	public void deleteAllSpeRemains(){
		for (int i = 1; i <= 20; i++) this.deleteSpeRemain(i);
	}

	public void deleteSpeRemain(int speCode){
		switch (speCode){
		case 1:		this.toEntityMonthSpeRemain1(null);		break;
		case 2:		this.toEntityMonthSpeRemain2(null);		break;
		case 3:		this.toEntityMonthSpeRemain3(null);		break;
		case 4:		this.toEntityMonthSpeRemain4(null);		break;
		case 5:		this.toEntityMonthSpeRemain5(null);		break;
		case 6:		this.toEntityMonthSpeRemain6(null);		break;
		case 7:		this.toEntityMonthSpeRemain7(null);		break;
		case 8:		this.toEntityMonthSpeRemain8(null);		break;
		case 9:		this.toEntityMonthSpeRemain9(null);		break;
		case 10:	this.toEntityMonthSpeRemain10(null);	break;
		case 11:	this.toEntityMonthSpeRemain11(null);	break;
		case 12:	this.toEntityMonthSpeRemain12(null);	break;
		case 13:	this.toEntityMonthSpeRemain13(null);	break;
		case 14:	this.toEntityMonthSpeRemain14(null);	break;
		case 15:	this.toEntityMonthSpeRemain15(null);	break;
		case 16:	this.toEntityMonthSpeRemain16(null);	break;
		case 17:	this.toEntityMonthSpeRemain17(null);	break;
		case 18:	this.toEntityMonthSpeRemain18(null);	break;
		case 19:	this.toEntityMonthSpeRemain19(null);	break;
		case 20:	this.toEntityMonthSpeRemain20(null);	break;
		default:	break;
		}
	}
	
	private EntitySpeRemainAtom toEntityMonthSpeRemain(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom result = new EntitySpeRemainAtom();
		
		if (domain == null) return result;

		val specialLeave = domain.getSpecialLeave();
		val actualSpecial = domain.getActualSpecial();
		if (specialLeave == null) return result;
		if (actualSpecial == null) return result;
		
		this.closureStatus = domain.getClosureStatus().value;
		this.startDate = domain.getClosurePeriod().start();
		this.endDate = domain.getClosurePeriod().end();
		
		// 特別休暇：使用数
		val specialUseNumber = specialLeave.getUseNumber();
		result.useDays = specialUseNumber.getUseDays().getUseDays().v();
		result.beforeUseDays = specialUseNumber.getUseDays().getBeforeUseGrantDays().v();
		if (specialUseNumber.getUseDays().getAfterUseGrantDays().isPresent()){
			result.afterUseDays = specialUseNumber.getUseDays().getAfterUseGrantDays().get().v();
		}
		if (specialUseNumber.getUseTimes().isPresent()){
			val specialUseTime = specialUseNumber.getUseTimes().get();
			result.useMinutes = specialUseTime.getUseTimes().v();
			result.beforeUseMinutes = specialUseTime.getBeforeUseGrantTimes().v();
			if (specialUseTime.getAfterUseGrantTimes().isPresent()){
				result.afterUseMinutes = specialUseTime.getAfterUseGrantTimes().get().v();
			}
			result.useTimes = specialUseTime.getUseNumber().v();
		}
		
		// 実特別休暇：使用数
		val actualUseNumber = actualSpecial.getUseNumber();
		result.factUseDays = actualUseNumber.getUseDays().getUseDays().v();
		result.beforeFactUseDays = actualUseNumber.getUseDays().getBeforeUseGrantDays().v();
		if (actualUseNumber.getUseDays().getAfterUseGrantDays().isPresent()){
			result.afterFactUseDays = actualUseNumber.getUseDays().getAfterUseGrantDays().get().v();
		}
		if (actualUseNumber.getUseTimes().isPresent()){
			val actualUseTime = actualUseNumber.getUseTimes().get();
			result.factUseMinutes = actualUseTime.getUseTimes().v();
			result.beforeFactUseMinutes = actualUseTime.getBeforeUseGrantTimes().v();
			if (actualUseTime.getAfterUseGrantTimes().isPresent()){
				result.afterFactUseMinutes = actualUseTime.getAfterUseGrantTimes().get().v();
			}
			result.factUseTimes = actualUseTime.getUseNumber().v();
		}
		
		// 特別休暇：残数
		val specialRemain = specialLeave.getRemain();
		result.remainDays = specialRemain.getDays().v();
		if (specialRemain.getTime().isPresent()){
			result.remainMinutes = specialRemain.getTime().get().v();
		}
		
		// 実特別休暇：残数
		val actualRemain = actualSpecial.getRemain();
		result.factRemainDays = actualRemain.getDays().v();
		if (actualRemain.getTime().isPresent()){
			result.factRemainMinutes = actualRemain.getTime().get().v();
		}
		
		// 特別休暇：残数付与前
		val specialRemainBefore = specialLeave.getBeforeRemainGrant();
		result.beforeRemainDays = specialRemainBefore.getDays().v();
		if (specialRemainBefore.getTime().isPresent()){
			result.beforeRemainMinutes = specialRemainBefore.getTime().get().v();
		}
		
		// 実特別休暇：残数付与前
		val actualRemainBefore = actualSpecial.getBeforRemainGrant();
		result.beforeFactRemainDays = actualRemainBefore.getDays().v();
		if (actualRemainBefore.getTime().isPresent()){
			result.beforeFactRemainMinutes = actualRemainBefore.getTime().get().v();
		}
		
		// 特別休暇：残数付与後
		if (specialLeave.getAfterRemainGrant().isPresent()){
			val specialRemainAfter = specialLeave.getAfterRemainGrant().get();
			result.afterRemainDays = specialRemainAfter.getDays().v();
			if (specialRemainAfter.getTime().isPresent()){
				result.afterRemainMinutes = specialRemainAfter.getTime().get().v();
			}
		}
		
		// 実特別休暇：残数付与後
		if (actualSpecial.getAfterRemainGrant().isPresent()){
			val actualRemainAfter = actualSpecial.getAfterRemainGrant().get();
			result.afterFactRemainDays = actualRemainAfter.getDays().v();
			if (actualRemainAfter.getTime().isPresent()){
				result.afterFactRemainMinutes = actualRemainAfter.getTime().get().v();
			}
		}
		
		// 特別休暇：未消化数
		val undegest = specialLeave.getUnDegestionNumber();
		result.notUseDays = undegest.getDays().v();
		if (undegest.getTimes().isPresent()){
			result.notUseMinutes = undegest.getTimes().get().v();
		}
		
		// 付与区分
		result.grantAtr = (domain.isGrantAtr() ? 1 : 0);
		
		// 特別休暇付与情報：付与日数
		if (domain.getGrantDays().isPresent()){
			result.grantDays = domain.getGrantDays().get().v();
		}
		
		return result;
	}

	private void toEntityMonthSpeRemain1(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays1 = entity.useDays;
		this.beforeUseDays1 = entity.beforeUseDays;
		this.afterUseDays1 = entity.afterUseDays;
		this.useMinutes1 = entity.useMinutes;
		this.beforeUseMinutes1 = entity.beforeUseMinutes;
		this.afterUseMinutes1 = entity.afterUseMinutes;
		this.useTimes1 = entity.useTimes;
		this.factUseDays1 = entity.factUseDays;
		this.beforeFactUseDays1 = entity.beforeFactUseDays;
		this.afterFactUseDays1 = entity.afterFactUseDays;
		this.factUseMinutes1 = entity.factUseMinutes;
		this.beforeFactUseMinutes1 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes1 = entity.afterFactUseMinutes;
		this.factUseTimes1 = entity.factUseTimes;
		this.remainDays1 = entity.remainDays;
		this.remainMinutes1 = entity.remainMinutes;
		this.factRemainDays1 = entity.factRemainDays;
		this.factRemainMinutes1 = entity.factRemainMinutes;
		this.beforeRemainDays1 = entity.beforeRemainDays;
		this.beforeRemainMinutes1 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays1 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes1 = entity.beforeFactRemainMinutes;
		this.afterRemainDays1 = entity.afterRemainDays;
		this.afterRemainMinutes1 = entity.afterRemainMinutes;
		this.afterFactRemainDays1 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes1 = entity.afterFactRemainMinutes;
		this.notUseDays1 = entity.notUseDays;
		this.notUseMinutes1 = entity.notUseMinutes;
		this.grantAtr1 = entity.grantAtr;
		this.grantDays1 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain2(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays2 = entity.useDays;
		this.beforeUseDays2 = entity.beforeUseDays;
		this.afterUseDays2 = entity.afterUseDays;
		this.useMinutes2 = entity.useMinutes;
		this.beforeUseMinutes2 = entity.beforeUseMinutes;
		this.afterUseMinutes2 = entity.afterUseMinutes;
		this.useTimes2 = entity.useTimes;
		this.factUseDays2 = entity.factUseDays;
		this.beforeFactUseDays2 = entity.beforeFactUseDays;
		this.afterFactUseDays2 = entity.afterFactUseDays;
		this.factUseMinutes2 = entity.factUseMinutes;
		this.beforeFactUseMinutes2 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes2 = entity.afterFactUseMinutes;
		this.factUseTimes2 = entity.factUseTimes;
		this.remainDays2 = entity.remainDays;
		this.remainMinutes2 = entity.remainMinutes;
		this.factRemainDays2 = entity.factRemainDays;
		this.factRemainMinutes2 = entity.factRemainMinutes;
		this.beforeRemainDays2 = entity.beforeRemainDays;
		this.beforeRemainMinutes2 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays2 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes2 = entity.beforeFactRemainMinutes;
		this.afterRemainDays2 = entity.afterRemainDays;
		this.afterRemainMinutes2 = entity.afterRemainMinutes;
		this.afterFactRemainDays2 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes2 = entity.afterFactRemainMinutes;
		this.notUseDays2 = entity.notUseDays;
		this.notUseMinutes2 = entity.notUseMinutes;
		this.grantAtr2 = entity.grantAtr;
		this.grantDays2 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain3(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays3 = entity.useDays;
		this.beforeUseDays3 = entity.beforeUseDays;
		this.afterUseDays3 = entity.afterUseDays;
		this.useMinutes3 = entity.useMinutes;
		this.beforeUseMinutes3 = entity.beforeUseMinutes;
		this.afterUseMinutes3 = entity.afterUseMinutes;
		this.useTimes3 = entity.useTimes;
		this.factUseDays3 = entity.factUseDays;
		this.beforeFactUseDays3 = entity.beforeFactUseDays;
		this.afterFactUseDays3 = entity.afterFactUseDays;
		this.factUseMinutes3 = entity.factUseMinutes;
		this.beforeFactUseMinutes3 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes3 = entity.afterFactUseMinutes;
		this.factUseTimes3 = entity.factUseTimes;
		this.remainDays3 = entity.remainDays;
		this.remainMinutes3 = entity.remainMinutes;
		this.factRemainDays3 = entity.factRemainDays;
		this.factRemainMinutes3 = entity.factRemainMinutes;
		this.beforeRemainDays3 = entity.beforeRemainDays;
		this.beforeRemainMinutes3 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays3 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes3 = entity.beforeFactRemainMinutes;
		this.afterRemainDays3 = entity.afterRemainDays;
		this.afterRemainMinutes3 = entity.afterRemainMinutes;
		this.afterFactRemainDays3 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes3 = entity.afterFactRemainMinutes;
		this.notUseDays3 = entity.notUseDays;
		this.notUseMinutes3 = entity.notUseMinutes;
		this.grantAtr3 = entity.grantAtr;
		this.grantDays3 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain4(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays4 = entity.useDays;
		this.beforeUseDays4 = entity.beforeUseDays;
		this.afterUseDays4 = entity.afterUseDays;
		this.useMinutes4 = entity.useMinutes;
		this.beforeUseMinutes4 = entity.beforeUseMinutes;
		this.afterUseMinutes4 = entity.afterUseMinutes;
		this.useTimes4 = entity.useTimes;
		this.factUseDays4 = entity.factUseDays;
		this.beforeFactUseDays4 = entity.beforeFactUseDays;
		this.afterFactUseDays4 = entity.afterFactUseDays;
		this.factUseMinutes4 = entity.factUseMinutes;
		this.beforeFactUseMinutes4 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes4 = entity.afterFactUseMinutes;
		this.factUseTimes4 = entity.factUseTimes;
		this.remainDays4 = entity.remainDays;
		this.remainMinutes4 = entity.remainMinutes;
		this.factRemainDays4 = entity.factRemainDays;
		this.factRemainMinutes4 = entity.factRemainMinutes;
		this.beforeRemainDays4 = entity.beforeRemainDays;
		this.beforeRemainMinutes4 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays4 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes4 = entity.beforeFactRemainMinutes;
		this.afterRemainDays4 = entity.afterRemainDays;
		this.afterRemainMinutes4 = entity.afterRemainMinutes;
		this.afterFactRemainDays4 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes4 = entity.afterFactRemainMinutes;
		this.notUseDays4 = entity.notUseDays;
		this.notUseMinutes4 = entity.notUseMinutes;
		this.grantAtr4 = entity.grantAtr;
		this.grantDays4 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain5(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays5 = entity.useDays;
		this.beforeUseDays5 = entity.beforeUseDays;
		this.afterUseDays5 = entity.afterUseDays;
		this.useMinutes5 = entity.useMinutes;
		this.beforeUseMinutes5 = entity.beforeUseMinutes;
		this.afterUseMinutes5 = entity.afterUseMinutes;
		this.useTimes5 = entity.useTimes;
		this.factUseDays5 = entity.factUseDays;
		this.beforeFactUseDays5 = entity.beforeFactUseDays;
		this.afterFactUseDays5 = entity.afterFactUseDays;
		this.factUseMinutes5 = entity.factUseMinutes;
		this.beforeFactUseMinutes5 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes5 = entity.afterFactUseMinutes;
		this.factUseTimes5 = entity.factUseTimes;
		this.remainDays5 = entity.remainDays;
		this.remainMinutes5 = entity.remainMinutes;
		this.factRemainDays5 = entity.factRemainDays;
		this.factRemainMinutes5 = entity.factRemainMinutes;
		this.beforeRemainDays5 = entity.beforeRemainDays;
		this.beforeRemainMinutes5 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays5 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes5 = entity.beforeFactRemainMinutes;
		this.afterRemainDays5 = entity.afterRemainDays;
		this.afterRemainMinutes5 = entity.afterRemainMinutes;
		this.afterFactRemainDays5 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes5 = entity.afterFactRemainMinutes;
		this.notUseDays5 = entity.notUseDays;
		this.notUseMinutes5 = entity.notUseMinutes;
		this.grantAtr5 = entity.grantAtr;
		this.grantDays5 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain6(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays6 = entity.useDays;
		this.beforeUseDays6 = entity.beforeUseDays;
		this.afterUseDays6 = entity.afterUseDays;
		this.useMinutes6 = entity.useMinutes;
		this.beforeUseMinutes6 = entity.beforeUseMinutes;
		this.afterUseMinutes6 = entity.afterUseMinutes;
		this.useTimes6 = entity.useTimes;
		this.factUseDays6 = entity.factUseDays;
		this.beforeFactUseDays6 = entity.beforeFactUseDays;
		this.afterFactUseDays6 = entity.afterFactUseDays;
		this.factUseMinutes6 = entity.factUseMinutes;
		this.beforeFactUseMinutes6 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes6 = entity.afterFactUseMinutes;
		this.factUseTimes6 = entity.factUseTimes;
		this.remainDays6 = entity.remainDays;
		this.remainMinutes6 = entity.remainMinutes;
		this.factRemainDays6 = entity.factRemainDays;
		this.factRemainMinutes6 = entity.factRemainMinutes;
		this.beforeRemainDays6 = entity.beforeRemainDays;
		this.beforeRemainMinutes6 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays6 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes6 = entity.beforeFactRemainMinutes;
		this.afterRemainDays6 = entity.afterRemainDays;
		this.afterRemainMinutes6 = entity.afterRemainMinutes;
		this.afterFactRemainDays6 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes6 = entity.afterFactRemainMinutes;
		this.notUseDays6 = entity.notUseDays;
		this.notUseMinutes6 = entity.notUseMinutes;
		this.grantAtr6 = entity.grantAtr;
		this.grantDays6 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain7(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays7 = entity.useDays;
		this.beforeUseDays7 = entity.beforeUseDays;
		this.afterUseDays7 = entity.afterUseDays;
		this.useMinutes7 = entity.useMinutes;
		this.beforeUseMinutes7 = entity.beforeUseMinutes;
		this.afterUseMinutes7 = entity.afterUseMinutes;
		this.useTimes7 = entity.useTimes;
		this.factUseDays7 = entity.factUseDays;
		this.beforeFactUseDays7 = entity.beforeFactUseDays;
		this.afterFactUseDays7 = entity.afterFactUseDays;
		this.factUseMinutes7 = entity.factUseMinutes;
		this.beforeFactUseMinutes7 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes7 = entity.afterFactUseMinutes;
		this.factUseTimes7 = entity.factUseTimes;
		this.remainDays7 = entity.remainDays;
		this.remainMinutes7 = entity.remainMinutes;
		this.factRemainDays7 = entity.factRemainDays;
		this.factRemainMinutes7 = entity.factRemainMinutes;
		this.beforeRemainDays7 = entity.beforeRemainDays;
		this.beforeRemainMinutes7 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays7 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes7 = entity.beforeFactRemainMinutes;
		this.afterRemainDays7 = entity.afterRemainDays;
		this.afterRemainMinutes7 = entity.afterRemainMinutes;
		this.afterFactRemainDays7 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes7 = entity.afterFactRemainMinutes;
		this.notUseDays7 = entity.notUseDays;
		this.notUseMinutes7 = entity.notUseMinutes;
		this.grantAtr7 = entity.grantAtr;
		this.grantDays7 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain8(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays8 = entity.useDays;
		this.beforeUseDays8 = entity.beforeUseDays;
		this.afterUseDays8 = entity.afterUseDays;
		this.useMinutes8 = entity.useMinutes;
		this.beforeUseMinutes8 = entity.beforeUseMinutes;
		this.afterUseMinutes8 = entity.afterUseMinutes;
		this.useTimes8 = entity.useTimes;
		this.factUseDays8 = entity.factUseDays;
		this.beforeFactUseDays8 = entity.beforeFactUseDays;
		this.afterFactUseDays8 = entity.afterFactUseDays;
		this.factUseMinutes8 = entity.factUseMinutes;
		this.beforeFactUseMinutes8 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes8 = entity.afterFactUseMinutes;
		this.factUseTimes8 = entity.factUseTimes;
		this.remainDays8 = entity.remainDays;
		this.remainMinutes8 = entity.remainMinutes;
		this.factRemainDays8 = entity.factRemainDays;
		this.factRemainMinutes8 = entity.factRemainMinutes;
		this.beforeRemainDays8 = entity.beforeRemainDays;
		this.beforeRemainMinutes8 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays8 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes8 = entity.beforeFactRemainMinutes;
		this.afterRemainDays8 = entity.afterRemainDays;
		this.afterRemainMinutes8 = entity.afterRemainMinutes;
		this.afterFactRemainDays8 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes8 = entity.afterFactRemainMinutes;
		this.notUseDays8 = entity.notUseDays;
		this.notUseMinutes8 = entity.notUseMinutes;
		this.grantAtr8 = entity.grantAtr;
		this.grantDays8 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain9(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays9 = entity.useDays;
		this.beforeUseDays9 = entity.beforeUseDays;
		this.afterUseDays9 = entity.afterUseDays;
		this.useMinutes9 = entity.useMinutes;
		this.beforeUseMinutes9 = entity.beforeUseMinutes;
		this.afterUseMinutes9 = entity.afterUseMinutes;
		this.useTimes9 = entity.useTimes;
		this.factUseDays9 = entity.factUseDays;
		this.beforeFactUseDays9 = entity.beforeFactUseDays;
		this.afterFactUseDays9 = entity.afterFactUseDays;
		this.factUseMinutes9 = entity.factUseMinutes;
		this.beforeFactUseMinutes9 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes9 = entity.afterFactUseMinutes;
		this.factUseTimes9 = entity.factUseTimes;
		this.remainDays9 = entity.remainDays;
		this.remainMinutes9 = entity.remainMinutes;
		this.factRemainDays9 = entity.factRemainDays;
		this.factRemainMinutes9 = entity.factRemainMinutes;
		this.beforeRemainDays9 = entity.beforeRemainDays;
		this.beforeRemainMinutes9 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays9 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes9 = entity.beforeFactRemainMinutes;
		this.afterRemainDays9 = entity.afterRemainDays;
		this.afterRemainMinutes9 = entity.afterRemainMinutes;
		this.afterFactRemainDays9 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes9 = entity.afterFactRemainMinutes;
		this.notUseDays9 = entity.notUseDays;
		this.notUseMinutes9 = entity.notUseMinutes;
		this.grantAtr9 = entity.grantAtr;
		this.grantDays9 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain10(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays10 = entity.useDays;
		this.beforeUseDays10 = entity.beforeUseDays;
		this.afterUseDays10 = entity.afterUseDays;
		this.useMinutes10 = entity.useMinutes;
		this.beforeUseMinutes10 = entity.beforeUseMinutes;
		this.afterUseMinutes10 = entity.afterUseMinutes;
		this.useTimes10 = entity.useTimes;
		this.factUseDays10 = entity.factUseDays;
		this.beforeFactUseDays10 = entity.beforeFactUseDays;
		this.afterFactUseDays10 = entity.afterFactUseDays;
		this.factUseMinutes10 = entity.factUseMinutes;
		this.beforeFactUseMinutes10 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes10 = entity.afterFactUseMinutes;
		this.factUseTimes10 = entity.factUseTimes;
		this.remainDays10 = entity.remainDays;
		this.remainMinutes10 = entity.remainMinutes;
		this.factRemainDays10 = entity.factRemainDays;
		this.factRemainMinutes10 = entity.factRemainMinutes;
		this.beforeRemainDays10 = entity.beforeRemainDays;
		this.beforeRemainMinutes10 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays10 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes10 = entity.beforeFactRemainMinutes;
		this.afterRemainDays10 = entity.afterRemainDays;
		this.afterRemainMinutes10 = entity.afterRemainMinutes;
		this.afterFactRemainDays10 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes10 = entity.afterFactRemainMinutes;
		this.notUseDays10 = entity.notUseDays;
		this.notUseMinutes10 = entity.notUseMinutes;
		this.grantAtr10 = entity.grantAtr;
		this.grantDays10 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain11(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays11 = entity.useDays;
		this.beforeUseDays11 = entity.beforeUseDays;
		this.afterUseDays11 = entity.afterUseDays;
		this.useMinutes11 = entity.useMinutes;
		this.beforeUseMinutes11 = entity.beforeUseMinutes;
		this.afterUseMinutes11 = entity.afterUseMinutes;
		this.useTimes11 = entity.useTimes;
		this.factUseDays11 = entity.factUseDays;
		this.beforeFactUseDays11 = entity.beforeFactUseDays;
		this.afterFactUseDays11 = entity.afterFactUseDays;
		this.factUseMinutes11 = entity.factUseMinutes;
		this.beforeFactUseMinutes11 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes11 = entity.afterFactUseMinutes;
		this.factUseTimes11 = entity.factUseTimes;
		this.remainDays11 = entity.remainDays;
		this.remainMinutes11 = entity.remainMinutes;
		this.factRemainDays11 = entity.factRemainDays;
		this.factRemainMinutes11 = entity.factRemainMinutes;
		this.beforeRemainDays11 = entity.beforeRemainDays;
		this.beforeRemainMinutes11 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays11 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes11 = entity.beforeFactRemainMinutes;
		this.afterRemainDays11 = entity.afterRemainDays;
		this.afterRemainMinutes11 = entity.afterRemainMinutes;
		this.afterFactRemainDays11 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes11 = entity.afterFactRemainMinutes;
		this.notUseDays11 = entity.notUseDays;
		this.notUseMinutes11 = entity.notUseMinutes;
		this.grantAtr11 = entity.grantAtr;
		this.grantDays11 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain12(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays12 = entity.useDays;
		this.beforeUseDays12 = entity.beforeUseDays;
		this.afterUseDays12 = entity.afterUseDays;
		this.useMinutes12 = entity.useMinutes;
		this.beforeUseMinutes12 = entity.beforeUseMinutes;
		this.afterUseMinutes12 = entity.afterUseMinutes;
		this.useTimes12 = entity.useTimes;
		this.factUseDays12 = entity.factUseDays;
		this.beforeFactUseDays12 = entity.beforeFactUseDays;
		this.afterFactUseDays12 = entity.afterFactUseDays;
		this.factUseMinutes12 = entity.factUseMinutes;
		this.beforeFactUseMinutes12 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes12 = entity.afterFactUseMinutes;
		this.factUseTimes12 = entity.factUseTimes;
		this.remainDays12 = entity.remainDays;
		this.remainMinutes12 = entity.remainMinutes;
		this.factRemainDays12 = entity.factRemainDays;
		this.factRemainMinutes12 = entity.factRemainMinutes;
		this.beforeRemainDays12 = entity.beforeRemainDays;
		this.beforeRemainMinutes12 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays12 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes12 = entity.beforeFactRemainMinutes;
		this.afterRemainDays12 = entity.afterRemainDays;
		this.afterRemainMinutes12 = entity.afterRemainMinutes;
		this.afterFactRemainDays12 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes12 = entity.afterFactRemainMinutes;
		this.notUseDays12 = entity.notUseDays;
		this.notUseMinutes12 = entity.notUseMinutes;
		this.grantAtr12 = entity.grantAtr;
		this.grantDays12 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain13(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays13 = entity.useDays;
		this.beforeUseDays13 = entity.beforeUseDays;
		this.afterUseDays13 = entity.afterUseDays;
		this.useMinutes13 = entity.useMinutes;
		this.beforeUseMinutes13 = entity.beforeUseMinutes;
		this.afterUseMinutes13 = entity.afterUseMinutes;
		this.useTimes13 = entity.useTimes;
		this.factUseDays13 = entity.factUseDays;
		this.beforeFactUseDays13 = entity.beforeFactUseDays;
		this.afterFactUseDays13 = entity.afterFactUseDays;
		this.factUseMinutes13 = entity.factUseMinutes;
		this.beforeFactUseMinutes13 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes13 = entity.afterFactUseMinutes;
		this.factUseTimes13 = entity.factUseTimes;
		this.remainDays13 = entity.remainDays;
		this.remainMinutes13 = entity.remainMinutes;
		this.factRemainDays13 = entity.factRemainDays;
		this.factRemainMinutes13 = entity.factRemainMinutes;
		this.beforeRemainDays13 = entity.beforeRemainDays;
		this.beforeRemainMinutes13 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays13 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes13 = entity.beforeFactRemainMinutes;
		this.afterRemainDays13 = entity.afterRemainDays;
		this.afterRemainMinutes13 = entity.afterRemainMinutes;
		this.afterFactRemainDays13 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes13 = entity.afterFactRemainMinutes;
		this.notUseDays13 = entity.notUseDays;
		this.notUseMinutes13 = entity.notUseMinutes;
		this.grantAtr13 = entity.grantAtr;
		this.grantDays13 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain14(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays14 = entity.useDays;
		this.beforeUseDays14 = entity.beforeUseDays;
		this.afterUseDays14 = entity.afterUseDays;
		this.useMinutes14 = entity.useMinutes;
		this.beforeUseMinutes14 = entity.beforeUseMinutes;
		this.afterUseMinutes14 = entity.afterUseMinutes;
		this.useTimes14 = entity.useTimes;
		this.factUseDays14 = entity.factUseDays;
		this.beforeFactUseDays14 = entity.beforeFactUseDays;
		this.afterFactUseDays14 = entity.afterFactUseDays;
		this.factUseMinutes14 = entity.factUseMinutes;
		this.beforeFactUseMinutes14 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes14 = entity.afterFactUseMinutes;
		this.factUseTimes14 = entity.factUseTimes;
		this.remainDays14 = entity.remainDays;
		this.remainMinutes14 = entity.remainMinutes;
		this.factRemainDays14 = entity.factRemainDays;
		this.factRemainMinutes14 = entity.factRemainMinutes;
		this.beforeRemainDays14 = entity.beforeRemainDays;
		this.beforeRemainMinutes14 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays14 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes14 = entity.beforeFactRemainMinutes;
		this.afterRemainDays14 = entity.afterRemainDays;
		this.afterRemainMinutes14 = entity.afterRemainMinutes;
		this.afterFactRemainDays14 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes14 = entity.afterFactRemainMinutes;
		this.notUseDays14 = entity.notUseDays;
		this.notUseMinutes14 = entity.notUseMinutes;
		this.grantAtr14 = entity.grantAtr;
		this.grantDays14 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain15(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays15 = entity.useDays;
		this.beforeUseDays15 = entity.beforeUseDays;
		this.afterUseDays15 = entity.afterUseDays;
		this.useMinutes15 = entity.useMinutes;
		this.beforeUseMinutes15 = entity.beforeUseMinutes;
		this.afterUseMinutes15 = entity.afterUseMinutes;
		this.useTimes15 = entity.useTimes;
		this.factUseDays15 = entity.factUseDays;
		this.beforeFactUseDays15 = entity.beforeFactUseDays;
		this.afterFactUseDays15 = entity.afterFactUseDays;
		this.factUseMinutes15 = entity.factUseMinutes;
		this.beforeFactUseMinutes15 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes15 = entity.afterFactUseMinutes;
		this.factUseTimes15 = entity.factUseTimes;
		this.remainDays15 = entity.remainDays;
		this.remainMinutes15 = entity.remainMinutes;
		this.factRemainDays15 = entity.factRemainDays;
		this.factRemainMinutes15 = entity.factRemainMinutes;
		this.beforeRemainDays15 = entity.beforeRemainDays;
		this.beforeRemainMinutes15 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays15 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes15 = entity.beforeFactRemainMinutes;
		this.afterRemainDays15 = entity.afterRemainDays;
		this.afterRemainMinutes15 = entity.afterRemainMinutes;
		this.afterFactRemainDays15 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes15 = entity.afterFactRemainMinutes;
		this.notUseDays15 = entity.notUseDays;
		this.notUseMinutes15 = entity.notUseMinutes;
		this.grantAtr15 = entity.grantAtr;
		this.grantDays15 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain16(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays16 = entity.useDays;
		this.beforeUseDays16 = entity.beforeUseDays;
		this.afterUseDays16 = entity.afterUseDays;
		this.useMinutes16 = entity.useMinutes;
		this.beforeUseMinutes16 = entity.beforeUseMinutes;
		this.afterUseMinutes16 = entity.afterUseMinutes;
		this.useTimes16 = entity.useTimes;
		this.factUseDays16 = entity.factUseDays;
		this.beforeFactUseDays16 = entity.beforeFactUseDays;
		this.afterFactUseDays16 = entity.afterFactUseDays;
		this.factUseMinutes16 = entity.factUseMinutes;
		this.beforeFactUseMinutes16 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes16 = entity.afterFactUseMinutes;
		this.factUseTimes16 = entity.factUseTimes;
		this.remainDays16 = entity.remainDays;
		this.remainMinutes16 = entity.remainMinutes;
		this.factRemainDays16 = entity.factRemainDays;
		this.factRemainMinutes16 = entity.factRemainMinutes;
		this.beforeRemainDays16 = entity.beforeRemainDays;
		this.beforeRemainMinutes16 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays16 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes16 = entity.beforeFactRemainMinutes;
		this.afterRemainDays16 = entity.afterRemainDays;
		this.afterRemainMinutes16 = entity.afterRemainMinutes;
		this.afterFactRemainDays16 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes16 = entity.afterFactRemainMinutes;
		this.notUseDays16 = entity.notUseDays;
		this.notUseMinutes16 = entity.notUseMinutes;
		this.grantAtr16 = entity.grantAtr;
		this.grantDays16 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain17(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays17 = entity.useDays;
		this.beforeUseDays17 = entity.beforeUseDays;
		this.afterUseDays17 = entity.afterUseDays;
		this.useMinutes17 = entity.useMinutes;
		this.beforeUseMinutes17 = entity.beforeUseMinutes;
		this.afterUseMinutes17 = entity.afterUseMinutes;
		this.useTimes17 = entity.useTimes;
		this.factUseDays17 = entity.factUseDays;
		this.beforeFactUseDays17 = entity.beforeFactUseDays;
		this.afterFactUseDays17 = entity.afterFactUseDays;
		this.factUseMinutes17 = entity.factUseMinutes;
		this.beforeFactUseMinutes17 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes17 = entity.afterFactUseMinutes;
		this.factUseTimes17 = entity.factUseTimes;
		this.remainDays17 = entity.remainDays;
		this.remainMinutes17 = entity.remainMinutes;
		this.factRemainDays17 = entity.factRemainDays;
		this.factRemainMinutes17 = entity.factRemainMinutes;
		this.beforeRemainDays17 = entity.beforeRemainDays;
		this.beforeRemainMinutes17 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays17 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes17 = entity.beforeFactRemainMinutes;
		this.afterRemainDays17 = entity.afterRemainDays;
		this.afterRemainMinutes17 = entity.afterRemainMinutes;
		this.afterFactRemainDays17 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes17 = entity.afterFactRemainMinutes;
		this.notUseDays17 = entity.notUseDays;
		this.notUseMinutes17 = entity.notUseMinutes;
		this.grantAtr17 = entity.grantAtr;
		this.grantDays17 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain18(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays18 = entity.useDays;
		this.beforeUseDays18 = entity.beforeUseDays;
		this.afterUseDays18 = entity.afterUseDays;
		this.useMinutes18 = entity.useMinutes;
		this.beforeUseMinutes18 = entity.beforeUseMinutes;
		this.afterUseMinutes18 = entity.afterUseMinutes;
		this.useTimes18 = entity.useTimes;
		this.factUseDays18 = entity.factUseDays;
		this.beforeFactUseDays18 = entity.beforeFactUseDays;
		this.afterFactUseDays18 = entity.afterFactUseDays;
		this.factUseMinutes18 = entity.factUseMinutes;
		this.beforeFactUseMinutes18 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes18 = entity.afterFactUseMinutes;
		this.factUseTimes18 = entity.factUseTimes;
		this.remainDays18 = entity.remainDays;
		this.remainMinutes18 = entity.remainMinutes;
		this.factRemainDays18 = entity.factRemainDays;
		this.factRemainMinutes18 = entity.factRemainMinutes;
		this.beforeRemainDays18 = entity.beforeRemainDays;
		this.beforeRemainMinutes18 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays18 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes18 = entity.beforeFactRemainMinutes;
		this.afterRemainDays18 = entity.afterRemainDays;
		this.afterRemainMinutes18 = entity.afterRemainMinutes;
		this.afterFactRemainDays18 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes18 = entity.afterFactRemainMinutes;
		this.notUseDays18 = entity.notUseDays;
		this.notUseMinutes18 = entity.notUseMinutes;
		this.grantAtr18 = entity.grantAtr;
		this.grantDays18 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain19(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays19 = entity.useDays;
		this.beforeUseDays19 = entity.beforeUseDays;
		this.afterUseDays19 = entity.afterUseDays;
		this.useMinutes19 = entity.useMinutes;
		this.beforeUseMinutes19 = entity.beforeUseMinutes;
		this.afterUseMinutes19 = entity.afterUseMinutes;
		this.useTimes19 = entity.useTimes;
		this.factUseDays19 = entity.factUseDays;
		this.beforeFactUseDays19 = entity.beforeFactUseDays;
		this.afterFactUseDays19 = entity.afterFactUseDays;
		this.factUseMinutes19 = entity.factUseMinutes;
		this.beforeFactUseMinutes19 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes19 = entity.afterFactUseMinutes;
		this.factUseTimes19 = entity.factUseTimes;
		this.remainDays19 = entity.remainDays;
		this.remainMinutes19 = entity.remainMinutes;
		this.factRemainDays19 = entity.factRemainDays;
		this.factRemainMinutes19 = entity.factRemainMinutes;
		this.beforeRemainDays19 = entity.beforeRemainDays;
		this.beforeRemainMinutes19 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays19 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes19 = entity.beforeFactRemainMinutes;
		this.afterRemainDays19 = entity.afterRemainDays;
		this.afterRemainMinutes19 = entity.afterRemainMinutes;
		this.afterFactRemainDays19 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes19 = entity.afterFactRemainMinutes;
		this.notUseDays19 = entity.notUseDays;
		this.notUseMinutes19 = entity.notUseMinutes;
		this.grantAtr19 = entity.grantAtr;
		this.grantDays19 = entity.grantDays;
	}

	private void toEntityMonthSpeRemain20(SpecialHolidayRemainData domain) {

		EntitySpeRemainAtom entity = this.toEntityMonthSpeRemain(domain);
		this.useDays20 = entity.useDays;
		this.beforeUseDays20 = entity.beforeUseDays;
		this.afterUseDays20 = entity.afterUseDays;
		this.useMinutes20 = entity.useMinutes;
		this.beforeUseMinutes20 = entity.beforeUseMinutes;
		this.afterUseMinutes20 = entity.afterUseMinutes;
		this.useTimes20 = entity.useTimes;
		this.factUseDays20 = entity.factUseDays;
		this.beforeFactUseDays20 = entity.beforeFactUseDays;
		this.afterFactUseDays20 = entity.afterFactUseDays;
		this.factUseMinutes20 = entity.factUseMinutes;
		this.beforeFactUseMinutes20 = entity.beforeFactUseMinutes;
		this.afterFactUseMinutes20 = entity.afterFactUseMinutes;
		this.factUseTimes20 = entity.factUseTimes;
		this.remainDays20 = entity.remainDays;
		this.remainMinutes20 = entity.remainMinutes;
		this.factRemainDays20 = entity.factRemainDays;
		this.factRemainMinutes20 = entity.factRemainMinutes;
		this.beforeRemainDays20 = entity.beforeRemainDays;
		this.beforeRemainMinutes20 = entity.beforeRemainMinutes;
		this.beforeFactRemainDays20 = entity.beforeFactRemainDays;
		this.beforeFactRemainMinutes20 = entity.beforeFactRemainMinutes;
		this.afterRemainDays20 = entity.afterRemainDays;
		this.afterRemainMinutes20 = entity.afterRemainMinutes;
		this.afterFactRemainDays20 = entity.afterFactRemainDays;
		this.afterFactRemainMinutes20 = entity.afterFactRemainMinutes;
		this.notUseDays20 = entity.notUseDays;
		this.notUseMinutes20 = entity.notUseMinutes;
		this.grantAtr20 = entity.grantAtr;
		this.grantDays20 = entity.grantDays;
	}
	
	/** KRCDT_MON_DAYOFF_REMAIN **/
	public void toEntityDayOffRemainDayAndTimes(MonthlyDayoffRemainData domain) {
		
		this.deleteDayOffRemainDayAndTimes();
		if (domain == null) return;
		
		this.closureStatus = domain.getClosureStatus().value;
		this.startDate = domain.getStartDate();
		this.endDate = domain.getEndDate();
		
		this.dayOffOccurredDays = domain.getOccurrenceDayTimes().getDay().v();
		if (domain.getOccurrenceDayTimes().getTime().isPresent()){
			this.dayOffOccurredTimes = domain.getOccurrenceDayTimes().getTime().get().v();
		}
		this.dayOffUsedDays = domain.getUseDayTimes().getDay().v();
		if (domain.getUseDayTimes().getTime().isPresent()){
			this.dayOffUsedMinutes = domain.getUseDayTimes().getTime().get().v();
		}
		this.dayOffRemainingDays = domain.getRemainingDayTimes().getDays().v();
		if (domain.getRemainingDayTimes().getTimes().isPresent()){
			this.dayOffRemainingMinutes = domain.getRemainingDayTimes().getTimes().get().v();
		}
		this.dayOffCarryforwardDays = domain.getCarryForWardDayTimes().getDays().v();
		if (domain.getCarryForWardDayTimes().getTimes().isPresent()){
			this.dayOffCarryforwardMinutes = domain.getCarryForWardDayTimes().getTimes().get().v();
		}
		this.dayOffUnUsedDays = domain.getUnUsedDayTimes().getDay().v();
		if (domain.getUnUsedDayTimes().getTime().isPresent()){
			this.dayOffUnUsedTimes = domain.getUnUsedDayTimes().getTime().get().v();
		}
	}
	
	public void deleteDayOffRemainDayAndTimes(){
		this.dayOffOccurredDays = 0.0;
		this.dayOffOccurredTimes = null;
		this.dayOffUsedDays = 0.0;
		this.dayOffUsedMinutes = null;
		this.dayOffRemainingDays = 0.0;
		this.dayOffRemainingMinutes = null;
		this.dayOffCarryforwardDays = 0.0;
		this.dayOffCarryforwardMinutes = null;
		this.dayOffUnUsedDays = 0.0;
		this.dayOffUnUsedTimes = null;
	}
	
	/** KRCDT_MON_SUBOFHD_REMAIN */
	public void toEntityAbsenceLeaveRemainData(AbsenceLeaveRemainData domain) {
		this.deleteAbsenceLeaveRemainData();
		if (domain == null) return;
		this.closureStatus = domain.getClosureStatus().value;
		this.startDate = domain.getStartDate();
		this.endDate = domain.getEndDate();
		this.subofHdOccurredDays = domain.getOccurredDay().v();
		this.subofHdUsedDays = domain.getUsedDays().v();
		this.subofHdRemainingDays = domain.getRemainingDays().v();
		this.subofHdCarryForWardDays = domain.getCarryforwardDays().v();
		this.subofHdUnUsedDays = domain.getUnUsedDays().v();
	}
	
	public void deleteAbsenceLeaveRemainData(){
		this.subofHdOccurredDays = 0.0;
		this.subofHdUsedDays = 0.0;
		this.subofHdRemainingDays = 0.0;
		this.subofHdCarryForWardDays = 0.0;
		this.subofHdUnUsedDays = 0.0;
	}
	
	/** KRCDT_MON_CHILD_HD_REMAIN **/
	public void toEntityChildRemainData(MonChildHdRemain domain){
		this.deleteChildRemainData();
		if (domain == null) return;
		this.closureStatus = domain.getClosureStatus().value;
		this.startDate = domain.getStartDate();
		this.endDate = domain.getEndDate();
		this.childUsedDays = domain.getUsedDays().v();
		this.childUsedDaysBefore = domain.getUsedDaysBefore().v();
		this.childUsedDaysAfter = domain.getUsedDaysAfter().v();
		this.childUsedMinutes = domain.getUsedMinutes().v();
		this.childUsedMinutesBefore = domain.getUsedMinutesBefore().v();
		this.childUsedMinutesAfter = domain.getUsedMinutesAfter().v();
	}
	
	public void deleteChildRemainData(){
		this.childUsedDays = 0.0;
		this.childUsedDaysBefore = 0.0;
		this.childUsedDaysAfter = null;
		this.childUsedMinutes = null;
		this.childUsedMinutesBefore = null;
		this.childUsedMinutesAfter = null;
	}
	
	/** KRCDT_MON_CARE_HD_REMAIN **/
	public void toEntityCareRemainData(MonCareHdRemain domain){
		this.deleteCareRemainData();
		if (domain == null) return;
		this.closureStatus = domain.getClosureStatus().value;
		this.startDate = domain.getStartDate();
		this.endDate = domain.getEndDate();
		this.careUsedDays = domain.getUsedDays().v();
		this.careUsedDaysBefore = domain.getUsedDaysBefore().v();
		this.careUsedDaysAfter = domain.getUsedDaysAfter().v();
		this.careUsedMinutes = domain.getUsedMinutes().v();
		this.careUsedMinutesBefore = domain.getUsedMinutesBefore().v();
		this.careUsedMinutesAfter = domain.getUsedMinutesAfter().v();
	}
	
	public void deleteCareRemainData(){
		this.careUsedDays = 0.0;
		this.careUsedDaysBefore = 0.0;
		this.careUsedDaysAfter = null;
		this.careUsedMinutes = null;
		this.careUsedMinutesBefore = null;
		this.careUsedMinutesAfter = null;
	}
	
	/**
	 * KRCDT_MON_ANNLEA_REMAIN
	 * ドメインに変換
	 * @return 年休月別残数データ
	 */
	public AnnLeaRemNumEachMonth toDomainAnnLeaRemNumEachMonth(){
		
		// 年休：使用時間
		UsedMinutes valUsedMinutesAfter = null;
		if (this.annleaUsedMinutesAfter != null){
			valUsedMinutesAfter = new UsedMinutes(this.annleaUsedMinutesAfter);
		}
		TimeAnnualLeaveUsedTime valUsedTime = null;
		if (this.annleaUsedTimes != null &&
			this.annleaUsedMinutes != null &&
			this.annleaUsedMinutesBefore != null){
			valUsedTime = TimeAnnualLeaveUsedTime.of(
					new UsedTimes(this.annleaUsedTimes),
					new UsedMinutes(this.annleaUsedMinutes),
					new UsedMinutes(this.annleaUsedMinutesBefore),
					Optional.ofNullable(valUsedMinutesAfter));
		}
		
		// 年休：残数付与後
		AnnualLeaveRemainingNumber valRemainAfter = null;
		if (this.annleaRemainingDaysAfter != null){
			RemainingMinutes valRemainMinutesAfter = null;
			if (this.annleaRemainingMinutesAfter != null){
				valRemainMinutesAfter = new RemainingMinutes(this.annleaRemainingMinutesAfter);
			}
			valRemainAfter = AnnualLeaveRemainingNumber.of(
					new AnnualLeaveRemainingDayNumber(this.annleaRemainingDaysAfter),
					Optional.ofNullable(valRemainMinutesAfter),
					new ArrayList<>());
		}
		
		// 年休
		AnnualLeaveUsedDayNumber valUsedDaysAfter = null;
		if (this.annleaUsedDaysAfter != null){
			valUsedDaysAfter = new AnnualLeaveUsedDayNumber(this.annleaUsedDaysAfter);
		}
		RemainingMinutes valRemainMinutes = null;
		if (this.annleaRemainingMinutes != null){
			valRemainMinutes = new RemainingMinutes(this.annleaRemainingMinutes);
		}
		RemainingMinutes valRemainMinutesBefore = null;
		if (this.annleaRemainingMinutesBefore != null){
			valRemainMinutesBefore = new RemainingMinutes(this.annleaRemainingMinutesBefore);
		}
		UndigestedTimeAnnualLeaveTime valUnusedMinutes = null;
		if (this.annleaUnusedMinutes != null){
			valUnusedMinutes = UndigestedTimeAnnualLeaveTime.of(new UsedMinutes(this.annleaUnusedMinutes));
		}
		AnnualLeave annualLeave = AnnualLeave.of(
				AnnualLeaveUsedNumber.of(
						AnnualLeaveUsedDays.of(
								new AnnualLeaveUsedDayNumber(this.annleaUsedDays),
								new AnnualLeaveUsedDayNumber(this.annleaUsedDaysBefore),
								Optional.ofNullable(valUsedDaysAfter)),
						Optional.ofNullable(valUsedTime)),
				AnnualLeaveRemainingNumber.of(
						new AnnualLeaveRemainingDayNumber(this.annleaRemainingDays),
						Optional.ofNullable(valRemainMinutes),
						new ArrayList<>()),
				AnnualLeaveRemainingNumber.of(
						new AnnualLeaveRemainingDayNumber(this.annleaRemainingDaysBefore),
						Optional.ofNullable(valRemainMinutesBefore),
						new ArrayList<>()),
				Optional.ofNullable(valRemainAfter),
				AnnualLeaveUndigestedNumber.of(
						UndigestedAnnualLeaveDays.of(new AnnualLeaveUsedDayNumber(this.annleaUnusedDays)),
						Optional.ofNullable(valUnusedMinutes)));

		// 実年休：使用時間
		UsedMinutes valFactUsedMinutesAfter = null;
		if (this.annleaFactUsedMinutesAfter != null){
			valFactUsedMinutesAfter = new UsedMinutes(this.annleaFactUsedMinutesAfter);
		}
		TimeAnnualLeaveUsedTime valFactUsedTime = null;
		if (this.annleaFactUsedTimes != null &&
			this.annleaFactUsedMinutes != null &&
			this.annleaFactUsedMinutesBefore != null){
			valFactUsedTime = TimeAnnualLeaveUsedTime.of(
					new UsedTimes(this.annleaFactUsedTimes),
					new UsedMinutes(this.annleaFactUsedMinutes),
					new UsedMinutes(this.annleaFactUsedMinutesBefore),
					Optional.ofNullable(valFactUsedMinutesAfter));
		}
		
		// 実年休：残数付与後
		AnnualLeaveRemainingNumber valFactRemainAfter = null;
		if (this.annleaFactRemainingDaysAfter != null){
			RemainingMinutes valFactRemainMinutesAfter = null;
			if (this.annleaFactRemainingMinutesAfter != null){
				valFactRemainMinutesAfter = new RemainingMinutes(this.annleaFactRemainingMinutesAfter);
			}
			valFactRemainAfter = AnnualLeaveRemainingNumber.of(
					new AnnualLeaveRemainingDayNumber(this.annleaFactRemainingDaysAfter),
					Optional.ofNullable(valFactRemainMinutesAfter),
					new ArrayList<>());
		}
		
		// 実年休
		AnnualLeaveUsedDayNumber valFactUsedDaysAfter = null;
		if (this.annleaFactUsedDaysAfter != null){
			valFactUsedDaysAfter = new AnnualLeaveUsedDayNumber(this.annleaFactUsedDaysAfter);
		}
		RemainingMinutes valFactRemainMinutes = null;
		if (this.annleaFactRemainingMinutes != null){
			valFactRemainMinutes = new RemainingMinutes(this.annleaFactRemainingMinutes);
		}
		RemainingMinutes valFactRemainMinutesBefore = null;
		if (this.annleaFactRemainingMinutesBefore != null){
			valFactRemainMinutesBefore = new RemainingMinutes(this.annleaFactRemainingMinutesBefore);
		}
		RealAnnualLeave realAnnualLeave = RealAnnualLeave.of(
				AnnualLeaveUsedNumber.of(
						AnnualLeaveUsedDays.of(
								new AnnualLeaveUsedDayNumber(this.annleaFactUsedDays),
								new AnnualLeaveUsedDayNumber(this.annleaFactUsedDaysBefore),
								Optional.ofNullable(valFactUsedDaysAfter)),
						Optional.ofNullable(valFactUsedTime)),
				AnnualLeaveRemainingNumber.of(
						new AnnualLeaveRemainingDayNumber(this.annleaFactRemainingDays),
						Optional.ofNullable(valFactRemainMinutes),
						new ArrayList<>()),
				AnnualLeaveRemainingNumber.of(
						new AnnualLeaveRemainingDayNumber(this.annleaFactRemainingDaysBefore),
						Optional.ofNullable(valFactRemainMinutesBefore),
						new ArrayList<>()),
				Optional.ofNullable(valFactRemainAfter));
		
		// 半日年休
		HalfDayAnnualLeave halfDayAnnualLeave = null;
		if (this.annleaHalfRemainingTimes != null &&
			this.annleaHalfRemainingTimesBefore != null &&
			this.annleaHalfUsedTimes != null &&
			this.annleaHalfUsedTimesBefore != null){
			RemainingTimes valHalfRemainTimesAfter = null;
			if (this.annleaHalfRemainingTimesAfter != null){
				valHalfRemainTimesAfter = new RemainingTimes(this.annleaHalfRemainingTimesAfter);
			}
			UsedTimes valHalfUsedTimesAfter = null;
			if (this.annleaHalfUsedTimesAfter != null){
				valHalfUsedTimesAfter = new UsedTimes(this.annleaHalfUsedTimesAfter);
			}
			halfDayAnnualLeave = HalfDayAnnualLeave.of(
					HalfDayAnnLeaRemainingNum.of(
							new RemainingTimes(this.annleaHalfRemainingTimes),
							new RemainingTimes(this.annleaHalfRemainingTimesBefore),
							Optional.ofNullable(valHalfRemainTimesAfter)),
					HalfDayAnnLeaUsedNum.of(
							new UsedTimes(this.annleaHalfUsedTimes),
							new UsedTimes(this.annleaHalfUsedTimesBefore),
							Optional.ofNullable(valHalfUsedTimesAfter)));
		}
		
		// 実半日年休
		HalfDayAnnualLeave realHalfDayAnnualLeave = null;
		if (this.annleaFactHalfRemainingTimes != null &&
			this.annleaFactHalfRemainingTimesBefore != null &&
			this.annleaFactHalfUsedTimes != null &&
			this.annleaFactHalfUsedTimesBefore != null){
			RemainingTimes valFactHalfRemainTimesAfter = null;
			if (this.annleaFactHalfRemainingTimesAfter != null){
				valFactHalfRemainTimesAfter = new RemainingTimes(this.annleaFactHalfRemainingTimesAfter);
			}
			UsedTimes valFactHalfUsedTimesAfter = null;
			if (this.annleaFactHalfUsedTimesAfter != null){
				valFactHalfUsedTimesAfter = new UsedTimes(this.annleaFactHalfUsedTimesAfter);
			}
			realHalfDayAnnualLeave = HalfDayAnnualLeave.of(
					HalfDayAnnLeaRemainingNum.of(
							new RemainingTimes(this.annleaFactHalfRemainingTimes),
							new RemainingTimes(this.annleaFactHalfRemainingTimesBefore),
							Optional.ofNullable(valFactHalfRemainTimesAfter)),
					HalfDayAnnLeaUsedNum.of(
							new UsedTimes(this.annleaFactHalfUsedTimes),
							new UsedTimes(this.annleaFactHalfUsedTimesBefore),
							Optional.ofNullable(valFactHalfUsedTimesAfter)));
		}
		
		// 年休付与情報
		AnnualLeaveGrant annualLeaveGrant = null;
		if (this.annleaGrantDays != null &&
			this.annleaGrantLaborDays != null &&
			this.annleaGrantPredeterminedDays != null &&
			this.annleaGrantDeductionDays != null &&
			this.annleaDeductionDaysBefore != null &&
			this.annleaDeductionDaysAfter != null &&
			this.annleaAttendanceRate != null){
			annualLeaveGrant = AnnualLeaveGrant.of(
					new AnnualLeaveGrantDayNumber(this.annleaGrantDays),
					new YearlyDays((double)this.annleaGrantLaborDays),
					new YearlyDays((double)this.annleaGrantPredeterminedDays),
					new YearlyDays((double)this.annleaGrantDeductionDays),
					new MonthlyDays((double)this.annleaDeductionDaysBefore),
					new MonthlyDays((double)this.annleaDeductionDaysAfter),
					new AttendanceRate(this.annleaAttendanceRate));
		}
		
		// 上限残時間
		AnnualLeaveMaxRemainingTime maxRemainingTime = null;
		if (this.annleaTimeRemainingMinutes != null &&
			this.annleaTimeRemainingMinutesBefore != null){
			RemainingMinutes valTimeRemainMinutesAfter = null;
			if (this.annleaTimeRemainingMinutesAfter != null){
				valTimeRemainMinutesAfter = new RemainingMinutes(this.annleaTimeRemainingMinutesAfter);
			}
			maxRemainingTime = AnnualLeaveMaxRemainingTime.of(
					new RemainingMinutes(this.annleaTimeRemainingMinutes),
					new RemainingMinutes(this.annleaTimeRemainingMinutesBefore),
					Optional.ofNullable(valTimeRemainMinutesAfter));
		}
		
		// 実上限残時間
		AnnualLeaveMaxRemainingTime realMaxRemainingTime = null;
		if (this.annleaFactTimeRemainingMinutes != null &&
			this.annleaFactTimeRemainingMinutesBefore != null){
			RemainingMinutes valFactTimeRemainMinutesAfter = null;
			if (this.annleaFactTimeRemainingMinutesAfter != null){
				valFactTimeRemainMinutesAfter = new RemainingMinutes(this.annleaFactTimeRemainingMinutesAfter);
			}
			realMaxRemainingTime = AnnualLeaveMaxRemainingTime.of(
					new RemainingMinutes(this.annleaFactTimeRemainingMinutes),
					new RemainingMinutes(this.annleaFactTimeRemainingMinutesBefore),
					Optional.ofNullable(valFactTimeRemainMinutesAfter));
		}
		
		return AnnLeaRemNumEachMonth.of(
				this.krcdtMonRemainPk.getEmployeeId(),
				new YearMonth(this.krcdtMonRemainPk.getYearMonth()),
				EnumAdaptor.valueOf(this.krcdtMonRemainPk.getClosureId(), ClosureId.class),
				new ClosureDate(this.krcdtMonRemainPk.getClosureDay(), (this.krcdtMonRemainPk.getIsLastDay() != 0)),
				new DatePeriod(this.startDate, this.endDate),
				EnumAdaptor.valueOf(this.closureStatus, ClosureStatus.class),
				annualLeave,
				realAnnualLeave,
				Optional.ofNullable(halfDayAnnualLeave),
				Optional.ofNullable(realHalfDayAnnualLeave),
				Optional.ofNullable(annualLeaveGrant),
				Optional.ofNullable(maxRemainingTime),
				Optional.ofNullable(realMaxRemainingTime),
				AnnualLeaveAttdRateDays.of(
						new MonthlyDays((double)this.annleaLaborDays),
						new MonthlyDays((double)this.annleaPredeterminedDays),
						new MonthlyDays((double)this.annleaDeductionDays)),
				(this.annleaGrantAtr != 0));
	}
	
	/**
	 * KRCDT_MON_RSVLEA_REMAIN
	 * ドメインに変換
	 * @return 年休月別残数データ
	 */
	public RsvLeaRemNumEachMonth toDomainRsvLeaRemNumEachMonth(){
		
		// 積立年休月別残数明細を分類する
		List<ReserveLeaveRemainingDetail> normalDetail = new ArrayList<>();
		List<ReserveLeaveRemainingDetail> normalDetailBefore = new ArrayList<>();
		List<ReserveLeaveRemainingDetail> normalDetailAfter = new ArrayList<>();
		List<ReserveLeaveRemainingDetail> realDetail = new ArrayList<>();
		List<ReserveLeaveRemainingDetail> realDetailBefore = new ArrayList<>();
		List<ReserveLeaveRemainingDetail> realDetailAfter = new ArrayList<>();
		
		// 積立年休：残数付与後
		ReserveLeaveRemainingNumber valRemainAfter = null;
		if (this.rsvleaRemainingDaysAfter != null){
			valRemainAfter = ReserveLeaveRemainingNumber.of(
					new ReserveLeaveRemainingDayNumber(this.rsvleaRemainingDaysAfter),
					normalDetailAfter);
		}
		
		// 積立年休
		ReserveLeaveUsedDayNumber valUsedDaysAfter = null;
		if (this.rsvleaUsedDaysAfter != null){
			valUsedDaysAfter = new ReserveLeaveUsedDayNumber(this.rsvleaUsedDaysAfter);
		}
		ReserveLeave reserveLeave = ReserveLeave.of(
				ReserveLeaveUsedNumber.of(
						new ReserveLeaveUsedDayNumber(this.rsvleaUsedDays),
						new ReserveLeaveUsedDayNumber(this.rsvleaUsedDaysBefore),
						Optional.ofNullable(valUsedDaysAfter)),
				ReserveLeaveRemainingNumber.of(
						new ReserveLeaveRemainingDayNumber(this.rsvleaRemainingDays),
						normalDetail),
				ReserveLeaveRemainingNumber.of(
						new ReserveLeaveRemainingDayNumber(this.rsvleaRemainingDaysBefore),
						normalDetailBefore),
				Optional.ofNullable(valRemainAfter),
				ReserveLeaveUndigestedNumber.of(
						new ReserveLeaveRemainingDayNumber(this.rsvleaNotUsedDays)));

		// 実積立年休：残数付与後
		ReserveLeaveRemainingNumber valFactRemainAfter = null;
		if (this.rsvleaFactRemainingDaysAfter != null){
			valFactRemainAfter = ReserveLeaveRemainingNumber.of(
					new ReserveLeaveRemainingDayNumber(this.rsvleaFactRemainingDaysAfter),
					realDetailAfter);
		}
		
		// 実積立年休
		ReserveLeaveUsedDayNumber valFactUsedDaysAfter = null;
		if (this.rsvleaFactUsedDaysAfter != null){
			valFactUsedDaysAfter = new ReserveLeaveUsedDayNumber(this.rsvleaFactUsedDaysAfter);
		}
		RealReserveLeave realReserveLeave = RealReserveLeave.of(
				ReserveLeaveUsedNumber.of(
						new ReserveLeaveUsedDayNumber(this.rsvleaFactUsedDays),
						new ReserveLeaveUsedDayNumber(this.rsvleaFactUsedDaysBefore),
						Optional.ofNullable(valFactUsedDaysAfter)),
				ReserveLeaveRemainingNumber.of(
						new ReserveLeaveRemainingDayNumber(this.rsvleaFactRemainingDays),
						realDetail),
				ReserveLeaveRemainingNumber.of(
						new ReserveLeaveRemainingDayNumber(this.rsvleaFactRemainingDaysBefore),
						realDetailBefore),
				Optional.ofNullable(valFactRemainAfter));
		
		// 積立年休付与情報
		ReserveLeaveGrant reserveLeaveGrant = null;
		if (this.rsvleaGrantDays != null){
			reserveLeaveGrant = ReserveLeaveGrant.of(
					new ReserveLeaveGrantDayNumber(this.rsvleaGrantDays));
		}
		
		return RsvLeaRemNumEachMonth.of(
				this.krcdtMonRemainPk.getEmployeeId(),
				new YearMonth(this.krcdtMonRemainPk.getYearMonth()),
				EnumAdaptor.valueOf(this.krcdtMonRemainPk.getClosureId(), ClosureId.class),
				new ClosureDate(this.krcdtMonRemainPk.getClosureDay(), (this.krcdtMonRemainPk.getIsLastDay() != 0)),
				new DatePeriod(this.startDate, this.endDate),
				EnumAdaptor.valueOf(this.closureStatus, ClosureStatus.class),
				reserveLeave,
				realReserveLeave,
				Optional.ofNullable(reserveLeaveGrant),
				(this.rsvleaGrantAtr != 0));
	}
	
	/**
	 * KRCDT_MON_SP_REMAIN
	 * 特別休暇月別残数データ
	 */
	public SpecialHolidayRemainDataMerge toDomainSpecialHolidayRemainData() {
		SpecialHolidayRemainDataMerge merge = new SpecialHolidayRemainDataMerge();
		merge.setSpecialHolidayRemainData1	(this.toDomainSpecialHolidayRemainData1	());
		merge.setSpecialHolidayRemainData2	(this.toDomainSpecialHolidayRemainData2	());
		merge.setSpecialHolidayRemainData3	(this.toDomainSpecialHolidayRemainData3	());
		merge.setSpecialHolidayRemainData4	(this.toDomainSpecialHolidayRemainData4	());
		merge.setSpecialHolidayRemainData5	(this.toDomainSpecialHolidayRemainData5	());
		merge.setSpecialHolidayRemainData6	(this.toDomainSpecialHolidayRemainData6	());
		merge.setSpecialHolidayRemainData7	(this.toDomainSpecialHolidayRemainData7	());
		merge.setSpecialHolidayRemainData8	(this.toDomainSpecialHolidayRemainData8	());
		merge.setSpecialHolidayRemainData9	(this.toDomainSpecialHolidayRemainData9	());
		merge.setSpecialHolidayRemainData10	(this.toDomainSpecialHolidayRemainData10());
		merge.setSpecialHolidayRemainData11	(this.toDomainSpecialHolidayRemainData11());
		merge.setSpecialHolidayRemainData12	(this.toDomainSpecialHolidayRemainData12());
		merge.setSpecialHolidayRemainData13	(this.toDomainSpecialHolidayRemainData13());
		merge.setSpecialHolidayRemainData14	(this.toDomainSpecialHolidayRemainData14());
		merge.setSpecialHolidayRemainData15	(this.toDomainSpecialHolidayRemainData15());
		merge.setSpecialHolidayRemainData16	(this.toDomainSpecialHolidayRemainData16());
		merge.setSpecialHolidayRemainData17	(this.toDomainSpecialHolidayRemainData17());
		merge.setSpecialHolidayRemainData18	(this.toDomainSpecialHolidayRemainData18());
		merge.setSpecialHolidayRemainData19	(this.toDomainSpecialHolidayRemainData19());
		merge.setSpecialHolidayRemainData20	(this.toDomainSpecialHolidayRemainData20());
		return merge;
	}
	
	public List<SpecialHolidayRemainData> toDomainSpecialHolidayRemainList(){
		List<SpecialHolidayRemainData> results = new ArrayList<>();
		results.add(this.toDomainSpecialHolidayRemainData1());
		results.add(this.toDomainSpecialHolidayRemainData2());
		results.add(this.toDomainSpecialHolidayRemainData3());
		results.add(this.toDomainSpecialHolidayRemainData4());
		results.add(this.toDomainSpecialHolidayRemainData5());
		results.add(this.toDomainSpecialHolidayRemainData6());
		results.add(this.toDomainSpecialHolidayRemainData7());
		results.add(this.toDomainSpecialHolidayRemainData8());
		results.add(this.toDomainSpecialHolidayRemainData9());
		results.add(this.toDomainSpecialHolidayRemainData10());
		results.add(this.toDomainSpecialHolidayRemainData11());
		results.add(this.toDomainSpecialHolidayRemainData12());
		results.add(this.toDomainSpecialHolidayRemainData13());
		results.add(this.toDomainSpecialHolidayRemainData14());
		results.add(this.toDomainSpecialHolidayRemainData15());
		results.add(this.toDomainSpecialHolidayRemainData16());
		results.add(this.toDomainSpecialHolidayRemainData17());
		results.add(this.toDomainSpecialHolidayRemainData18());
		results.add(this.toDomainSpecialHolidayRemainData19());
		results.add(this.toDomainSpecialHolidayRemainData20());
		return results;
	}
	
	public Optional<SpecialHolidayRemainData> toDomainSpecialHolidayRemain(int speCode){
		switch (speCode){
		case 1:
			return Optional.of(this.toDomainSpecialHolidayRemainData1());
		case 2:
			return Optional.of(this.toDomainSpecialHolidayRemainData2());
		case 3:
			return Optional.of(this.toDomainSpecialHolidayRemainData3());
		case 4:
			return Optional.of(this.toDomainSpecialHolidayRemainData4());
		case 5:
			return Optional.of(this.toDomainSpecialHolidayRemainData5());
		case 6:
			return Optional.of(this.toDomainSpecialHolidayRemainData6());
		case 7:
			return Optional.of(this.toDomainSpecialHolidayRemainData7());
		case 8:
			return Optional.of(this.toDomainSpecialHolidayRemainData8());
		case 9:
			return Optional.of(this.toDomainSpecialHolidayRemainData9());
		case 10:
			return Optional.of(this.toDomainSpecialHolidayRemainData10());
		case 11:
			return Optional.of(this.toDomainSpecialHolidayRemainData11());
		case 12:
			return Optional.of(this.toDomainSpecialHolidayRemainData12());
		case 13:
			return Optional.of(this.toDomainSpecialHolidayRemainData13());
		case 14:
			return Optional.of(this.toDomainSpecialHolidayRemainData14());
		case 15:
			return Optional.of(this.toDomainSpecialHolidayRemainData15());
		case 16:
			return Optional.of(this.toDomainSpecialHolidayRemainData16());
		case 17:
			return Optional.of(this.toDomainSpecialHolidayRemainData17());
		case 18:
			return Optional.of(this.toDomainSpecialHolidayRemainData18());
		case 19:
			return Optional.of(this.toDomainSpecialHolidayRemainData19());
		case 20:
			return Optional.of(this.toDomainSpecialHolidayRemainData20());
		default:
			return Optional.empty();
		}
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData(
			int dataNo,
			double useDays,
			double beforeUseDays,
			Double afterUseDays,
			Integer useMinutes,
			Integer beforeUseMinutes,
			Integer afterUseMinutes,
			Integer useTimes,
			double factUseDays,
			double beforeFactUseDays,
			Double afterFactUseDays,
			Integer factUseMinutes,
			Integer beforeFactUseMinutes,
			Integer afterFactUseMinutes,
			Integer factUseTimes,
			double remainDays,
			Integer remainMinutes,
			double factRemainDays,
			Integer factRemainMinutes,
			double beforeRemainDays,
			Integer beforeRemainMinutes,
			double beforeFactRemainDays,
			Integer beforeFactRemainMinutes,
			Double afterRemainDays,
			Integer afterRemainMinutes,
			Double afterFactRemainDays,
			Integer afterFactRemainMinutes,
			double notUseDays,
			Integer notUseMinutes,
			int grantAtr,
			Double grantDays){
		
		// 実特別休暇の各属性
		SpecialLeavaRemainTime valFactRemainTimes = null;
		if (factRemainMinutes != null){
			valFactRemainTimes = new SpecialLeavaRemainTime(factRemainMinutes);
		}
		SpecialLeavaRemainTime valBeforeFactRemainTimes = null;
		if (beforeFactRemainMinutes != null){
			valBeforeFactRemainTimes = new SpecialLeavaRemainTime(beforeFactRemainMinutes);
		}
		SpecialLeaveRemainDay valAfterFactUseDays = null;
		if (afterFactUseDays != null){
			valAfterFactUseDays = new SpecialLeaveRemainDay(afterFactUseDays);
		}
		SpecialLeaveUseTimes actualUseTime = null;
		if (factUseTimes != null &&
			factUseMinutes != null &&
			beforeFactUseMinutes != null){
			SpecialLeavaRemainTime valAfterFactUseTimes = null;
			if (afterFactUseMinutes != null){
				valAfterFactUseTimes = new SpecialLeavaRemainTime(afterFactUseMinutes);
			}
			actualUseTime = new SpecialLeaveUseTimes(
					new UseNumber(factUseTimes),
					new SpecialLeavaRemainTime(factUseMinutes),
					new SpecialLeavaRemainTime(beforeFactUseMinutes),
					Optional.ofNullable(valAfterFactUseTimes));
		}
		ActualSpecialLeaveRemain actualAfterRemainGrant = null;
		if (afterFactRemainDays != null){
			SpecialLeavaRemainTime valAfterFactRemainTimes = null;
			if (afterFactRemainMinutes != null){
				valAfterFactRemainTimes = new SpecialLeavaRemainTime(afterFactRemainMinutes);
			}
			actualAfterRemainGrant = new ActualSpecialLeaveRemain(
					new ActualSpecialLeaveRemainDay(afterFactRemainDays),
					Optional.ofNullable(valAfterFactRemainTimes));
		}
		
		// 実特別休暇
		ActualSpecialLeave actualSpecial = new ActualSpecialLeave(
				new ActualSpecialLeaveRemain(
						new ActualSpecialLeaveRemainDay(factRemainDays),
						Optional.ofNullable(valFactRemainTimes)),
				new ActualSpecialLeaveRemain(
						new ActualSpecialLeaveRemainDay(beforeFactRemainDays),
						Optional.ofNullable(valBeforeFactRemainTimes)),
				new SpecialLeaveUseNumber(
						new SpecialLeaveUseDays(
								new SpecialLeaveRemainDay(factUseDays),
								new SpecialLeaveRemainDay(beforeFactUseDays),
								Optional.ofNullable(valAfterFactUseDays)),
						Optional.ofNullable(actualUseTime)),
				Optional.ofNullable(actualAfterRemainGrant));
		
		// 特別休暇の各属性
		SpecialLeavaRemainTime valRemainTimes = null;
		if (remainMinutes != null){
			valRemainTimes = new SpecialLeavaRemainTime(remainMinutes);
		}
		SpecialLeavaRemainTime valBeforeRemainTimes = null;
		if (beforeRemainMinutes != null){
			valBeforeRemainTimes = new SpecialLeavaRemainTime(beforeRemainMinutes);
		}
		SpecialLeaveRemainDay valAfterUseDays = null;
		if (afterUseDays != null){
			valAfterUseDays = new SpecialLeaveRemainDay(afterUseDays);
		}
		SpecialLeaveUseTimes specialUseTime = null;
		if (useTimes != null &&
			useMinutes != null &&
			beforeUseMinutes != null){
			SpecialLeavaRemainTime valAfterUseTimes = null;
			if (afterUseMinutes != null){
				valAfterUseTimes = new SpecialLeavaRemainTime(afterUseMinutes);
			}
			specialUseTime = new SpecialLeaveUseTimes(
					new UseNumber(useTimes),
					new SpecialLeavaRemainTime(useMinutes),
					new SpecialLeavaRemainTime(beforeUseMinutes),
					Optional.ofNullable(valAfterUseTimes));
		}
		SpecialLeavaRemainTime valNotUseTime = null;
		if (notUseMinutes != null){
			valNotUseTime = new SpecialLeavaRemainTime(notUseMinutes);
		}
		SpecialLeaveRemain specialAfterRemainGrant = null;
		if (afterRemainDays != null){
			SpecialLeavaRemainTime valAfterRemainTimes = null;
			if (afterRemainMinutes != null){
				valAfterRemainTimes = new SpecialLeavaRemainTime(afterRemainMinutes);
			}
			specialAfterRemainGrant = new SpecialLeaveRemain(
					new SpecialLeaveRemainDay(afterRemainDays),
					Optional.ofNullable(valAfterRemainTimes));
		}
		
		// 特別休暇
		SpecialLeave specialLeave = new SpecialLeave(
				new SpecialLeaveRemain(
						new SpecialLeaveRemainDay(remainDays),
						Optional.ofNullable(valRemainTimes)),
				new SpecialLeaveRemain(
						new SpecialLeaveRemainDay(beforeRemainDays),
						Optional.ofNullable(valBeforeRemainTimes)),
				new SpecialLeaveUseNumber(
						new SpecialLeaveUseDays(
								new SpecialLeaveRemainDay(useDays),
								new SpecialLeaveRemainDay(beforeUseDays),
								Optional.ofNullable(valAfterUseDays)),
						Optional.ofNullable(specialUseTime)),
				new SpecialLeaveUnDigestion(
						new SpecialLeaveRemainDay(notUseDays),
						Optional.ofNullable(valNotUseTime)), 
				Optional.ofNullable(specialAfterRemainGrant));
		
		// 付与日数
		SpecialLeaveGrantUseDay valGrantDays = null;
		if (grantDays != null){
			valGrantDays = new SpecialLeaveGrantUseDay(grantDays);
		}
		
		// 特別休暇月別残数データ//
		return new SpecialHolidayRemainData(
				this.krcdtMonRemainPk.getEmployeeId(),
				new YearMonth(this.krcdtMonRemainPk.getYearMonth()),
				this.krcdtMonRemainPk.getClosureId(),
				new ClosureDate(this.krcdtMonRemainPk.getClosureDay(), this.krcdtMonRemainPk.getIsLastDay() == 1),
				new DatePeriod(this.startDate, this.endDate),
				EnumAdaptor.valueOf(this.closureStatus, ClosureStatus.class),
				dataNo,
				actualSpecial,
				specialLeave,
				Optional.ofNullable(valGrantDays),
				(grantAtr == 1));
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData1(){
		return this.toDomainSpecialHolidayRemainData(
				1,
				this.useDays1,
				this.beforeUseDays1,
				this.afterUseDays1,
				this.useMinutes1,
				this.beforeUseMinutes1,
				this.afterUseMinutes1,
				this.useTimes1,
				this.factUseDays1,
				this.beforeFactUseDays1,
				this.afterFactUseDays1,
				this.factUseMinutes1,
				this.beforeFactUseMinutes1,
				this.afterFactUseMinutes1,
				this.factUseTimes1,
				this.remainDays1,
				this.remainMinutes1,
				this.factRemainDays1,
				this.factRemainMinutes1,
				this.beforeRemainDays1,
				this.beforeRemainMinutes1,
				this.beforeFactRemainDays1,
				this.beforeFactRemainMinutes1,
				this.afterRemainDays1,
				this.afterRemainMinutes1,
				this.afterFactRemainDays1,
				this.afterFactRemainMinutes1,
				this.notUseDays1,
				this.notUseMinutes1,
				this.grantAtr1,
				this.grantDays1);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData2(){
		return this.toDomainSpecialHolidayRemainData(
				2,
				this.useDays2,
				this.beforeUseDays2,
				this.afterUseDays2,
				this.useMinutes2,
				this.beforeUseMinutes2,
				this.afterUseMinutes2,
				this.useTimes2,
				this.factUseDays2,
				this.beforeFactUseDays2,
				this.afterFactUseDays2,
				this.factUseMinutes2,
				this.beforeFactUseMinutes2,
				this.afterFactUseMinutes2,
				this.factUseTimes2,
				this.remainDays2,
				this.remainMinutes2,
				this.factRemainDays2,
				this.factRemainMinutes2,
				this.beforeRemainDays2,
				this.beforeRemainMinutes2,
				this.beforeFactRemainDays2,
				this.beforeFactRemainMinutes2,
				this.afterRemainDays2,
				this.afterRemainMinutes2,
				this.afterFactRemainDays2,
				this.afterFactRemainMinutes2,
				this.notUseDays2,
				this.notUseMinutes2,
				this.grantAtr2,
				this.grantDays2);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData3(){
		return this.toDomainSpecialHolidayRemainData(
				3,
				this.useDays3,
				this.beforeUseDays3,
				this.afterUseDays3,
				this.useMinutes3,
				this.beforeUseMinutes3,
				this.afterUseMinutes3,
				this.useTimes3,
				this.factUseDays3,
				this.beforeFactUseDays3,
				this.afterFactUseDays3,
				this.factUseMinutes3,
				this.beforeFactUseMinutes3,
				this.afterFactUseMinutes3,
				this.factUseTimes3,
				this.remainDays3,
				this.remainMinutes3,
				this.factRemainDays3,
				this.factRemainMinutes3,
				this.beforeRemainDays3,
				this.beforeRemainMinutes3,
				this.beforeFactRemainDays3,
				this.beforeFactRemainMinutes3,
				this.afterRemainDays3,
				this.afterRemainMinutes3,
				this.afterFactRemainDays3,
				this.afterFactRemainMinutes3,
				this.notUseDays3,
				this.notUseMinutes3,
				this.grantAtr3,
				this.grantDays3);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData4(){
		return this.toDomainSpecialHolidayRemainData(
				4,
				this.useDays4,
				this.beforeUseDays4,
				this.afterUseDays4,
				this.useMinutes4,
				this.beforeUseMinutes4,
				this.afterUseMinutes4,
				this.useTimes4,
				this.factUseDays4,
				this.beforeFactUseDays4,
				this.afterFactUseDays4,
				this.factUseMinutes4,
				this.beforeFactUseMinutes4,
				this.afterFactUseMinutes4,
				this.factUseTimes4,
				this.remainDays4,
				this.remainMinutes4,
				this.factRemainDays4,
				this.factRemainMinutes4,
				this.beforeRemainDays4,
				this.beforeRemainMinutes4,
				this.beforeFactRemainDays4,
				this.beforeFactRemainMinutes4,
				this.afterRemainDays4,
				this.afterRemainMinutes4,
				this.afterFactRemainDays4,
				this.afterFactRemainMinutes4,
				this.notUseDays4,
				this.notUseMinutes4,
				this.grantAtr4,
				this.grantDays4);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData5(){
		return this.toDomainSpecialHolidayRemainData(
				5,
				this.useDays5,
				this.beforeUseDays5,
				this.afterUseDays5,
				this.useMinutes5,
				this.beforeUseMinutes5,
				this.afterUseMinutes5,
				this.useTimes5,
				this.factUseDays5,
				this.beforeFactUseDays5,
				this.afterFactUseDays5,
				this.factUseMinutes5,
				this.beforeFactUseMinutes5,
				this.afterFactUseMinutes5,
				this.factUseTimes5,
				this.remainDays5,
				this.remainMinutes5,
				this.factRemainDays5,
				this.factRemainMinutes5,
				this.beforeRemainDays5,
				this.beforeRemainMinutes5,
				this.beforeFactRemainDays5,
				this.beforeFactRemainMinutes5,
				this.afterRemainDays5,
				this.afterRemainMinutes5,
				this.afterFactRemainDays5,
				this.afterFactRemainMinutes5,
				this.notUseDays5,
				this.notUseMinutes5,
				this.grantAtr5,
				this.grantDays5);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData6(){
		return this.toDomainSpecialHolidayRemainData(
				6,
				this.useDays6,
				this.beforeUseDays6,
				this.afterUseDays6,
				this.useMinutes6,
				this.beforeUseMinutes6,
				this.afterUseMinutes6,
				this.useTimes6,
				this.factUseDays6,
				this.beforeFactUseDays6,
				this.afterFactUseDays6,
				this.factUseMinutes6,
				this.beforeFactUseMinutes6,
				this.afterFactUseMinutes6,
				this.factUseTimes6,
				this.remainDays6,
				this.remainMinutes6,
				this.factRemainDays6,
				this.factRemainMinutes6,
				this.beforeRemainDays6,
				this.beforeRemainMinutes6,
				this.beforeFactRemainDays6,
				this.beforeFactRemainMinutes6,
				this.afterRemainDays6,
				this.afterRemainMinutes6,
				this.afterFactRemainDays6,
				this.afterFactRemainMinutes6,
				this.notUseDays6,
				this.notUseMinutes6,
				this.grantAtr6,
				this.grantDays6);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData7(){
		return this.toDomainSpecialHolidayRemainData(
				7,
				this.useDays7,
				this.beforeUseDays7,
				this.afterUseDays7,
				this.useMinutes7,
				this.beforeUseMinutes7,
				this.afterUseMinutes7,
				this.useTimes7,
				this.factUseDays7,
				this.beforeFactUseDays7,
				this.afterFactUseDays7,
				this.factUseMinutes7,
				this.beforeFactUseMinutes7,
				this.afterFactUseMinutes7,
				this.factUseTimes7,
				this.remainDays7,
				this.remainMinutes7,
				this.factRemainDays7,
				this.factRemainMinutes7,
				this.beforeRemainDays7,
				this.beforeRemainMinutes7,
				this.beforeFactRemainDays7,
				this.beforeFactRemainMinutes7,
				this.afterRemainDays7,
				this.afterRemainMinutes7,
				this.afterFactRemainDays7,
				this.afterFactRemainMinutes7,
				this.notUseDays7,
				this.notUseMinutes7,
				this.grantAtr7,
				this.grantDays7);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData8(){
		return this.toDomainSpecialHolidayRemainData(
				8,
				this.useDays8,
				this.beforeUseDays8,
				this.afterUseDays8,
				this.useMinutes8,
				this.beforeUseMinutes8,
				this.afterUseMinutes8,
				this.useTimes8,
				this.factUseDays8,
				this.beforeFactUseDays8,
				this.afterFactUseDays8,
				this.factUseMinutes8,
				this.beforeFactUseMinutes8,
				this.afterFactUseMinutes8,
				this.factUseTimes8,
				this.remainDays8,
				this.remainMinutes8,
				this.factRemainDays8,
				this.factRemainMinutes8,
				this.beforeRemainDays8,
				this.beforeRemainMinutes8,
				this.beforeFactRemainDays8,
				this.beforeFactRemainMinutes8,
				this.afterRemainDays8,
				this.afterRemainMinutes8,
				this.afterFactRemainDays8,
				this.afterFactRemainMinutes8,
				this.notUseDays8,
				this.notUseMinutes8,
				this.grantAtr8,
				this.grantDays8);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData9(){
		return this.toDomainSpecialHolidayRemainData(
				9,
				this.useDays9,
				this.beforeUseDays9,
				this.afterUseDays9,
				this.useMinutes9,
				this.beforeUseMinutes9,
				this.afterUseMinutes9,
				this.useTimes9,
				this.factUseDays9,
				this.beforeFactUseDays9,
				this.afterFactUseDays9,
				this.factUseMinutes9,
				this.beforeFactUseMinutes9,
				this.afterFactUseMinutes9,
				this.factUseTimes9,
				this.remainDays9,
				this.remainMinutes9,
				this.factRemainDays9,
				this.factRemainMinutes9,
				this.beforeRemainDays9,
				this.beforeRemainMinutes9,
				this.beforeFactRemainDays9,
				this.beforeFactRemainMinutes9,
				this.afterRemainDays9,
				this.afterRemainMinutes9,
				this.afterFactRemainDays9,
				this.afterFactRemainMinutes9,
				this.notUseDays9,
				this.notUseMinutes9,
				this.grantAtr9,
				this.grantDays9);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData10(){
		return this.toDomainSpecialHolidayRemainData(
				10,
				this.useDays10,
				this.beforeUseDays10,
				this.afterUseDays10,
				this.useMinutes10,
				this.beforeUseMinutes10,
				this.afterUseMinutes10,
				this.useTimes10,
				this.factUseDays10,
				this.beforeFactUseDays10,
				this.afterFactUseDays10,
				this.factUseMinutes10,
				this.beforeFactUseMinutes10,
				this.afterFactUseMinutes10,
				this.factUseTimes10,
				this.remainDays10,
				this.remainMinutes10,
				this.factRemainDays10,
				this.factRemainMinutes10,
				this.beforeRemainDays10,
				this.beforeRemainMinutes10,
				this.beforeFactRemainDays10,
				this.beforeFactRemainMinutes10,
				this.afterRemainDays10,
				this.afterRemainMinutes10,
				this.afterFactRemainDays10,
				this.afterFactRemainMinutes10,
				this.notUseDays10,
				this.notUseMinutes10,
				this.grantAtr10,
				this.grantDays10);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData11(){
		return this.toDomainSpecialHolidayRemainData(
				11,
				this.useDays11,
				this.beforeUseDays11,
				this.afterUseDays11,
				this.useMinutes11,
				this.beforeUseMinutes11,
				this.afterUseMinutes11,
				this.useTimes11,
				this.factUseDays11,
				this.beforeFactUseDays11,
				this.afterFactUseDays11,
				this.factUseMinutes11,
				this.beforeFactUseMinutes11,
				this.afterFactUseMinutes11,
				this.factUseTimes11,
				this.remainDays11,
				this.remainMinutes11,
				this.factRemainDays11,
				this.factRemainMinutes11,
				this.beforeRemainDays11,
				this.beforeRemainMinutes11,
				this.beforeFactRemainDays11,
				this.beforeFactRemainMinutes11,
				this.afterRemainDays11,
				this.afterRemainMinutes11,
				this.afterFactRemainDays11,
				this.afterFactRemainMinutes11,
				this.notUseDays11,
				this.notUseMinutes11,
				this.grantAtr11,
				this.grantDays11);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData12(){
		return this.toDomainSpecialHolidayRemainData(
				12,
				this.useDays12,
				this.beforeUseDays12,
				this.afterUseDays12,
				this.useMinutes12,
				this.beforeUseMinutes12,
				this.afterUseMinutes12,
				this.useTimes12,
				this.factUseDays12,
				this.beforeFactUseDays12,
				this.afterFactUseDays12,
				this.factUseMinutes12,
				this.beforeFactUseMinutes12,
				this.afterFactUseMinutes12,
				this.factUseTimes12,
				this.remainDays12,
				this.remainMinutes12,
				this.factRemainDays12,
				this.factRemainMinutes12,
				this.beforeRemainDays12,
				this.beforeRemainMinutes12,
				this.beforeFactRemainDays12,
				this.beforeFactRemainMinutes12,
				this.afterRemainDays12,
				this.afterRemainMinutes12,
				this.afterFactRemainDays12,
				this.afterFactRemainMinutes12,
				this.notUseDays12,
				this.notUseMinutes12,
				this.grantAtr12,
				this.grantDays12);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData13(){
		return this.toDomainSpecialHolidayRemainData(
				13,
				this.useDays13,
				this.beforeUseDays13,
				this.afterUseDays13,
				this.useMinutes13,
				this.beforeUseMinutes13,
				this.afterUseMinutes13,
				this.useTimes13,
				this.factUseDays13,
				this.beforeFactUseDays13,
				this.afterFactUseDays13,
				this.factUseMinutes13,
				this.beforeFactUseMinutes13,
				this.afterFactUseMinutes13,
				this.factUseTimes13,
				this.remainDays13,
				this.remainMinutes13,
				this.factRemainDays13,
				this.factRemainMinutes13,
				this.beforeRemainDays13,
				this.beforeRemainMinutes13,
				this.beforeFactRemainDays13,
				this.beforeFactRemainMinutes13,
				this.afterRemainDays13,
				this.afterRemainMinutes13,
				this.afterFactRemainDays13,
				this.afterFactRemainMinutes13,
				this.notUseDays13,
				this.notUseMinutes13,
				this.grantAtr13,
				this.grantDays13);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData14(){
		return this.toDomainSpecialHolidayRemainData(
				14,
				this.useDays14,
				this.beforeUseDays14,
				this.afterUseDays14,
				this.useMinutes14,
				this.beforeUseMinutes14,
				this.afterUseMinutes14,
				this.useTimes14,
				this.factUseDays14,
				this.beforeFactUseDays14,
				this.afterFactUseDays14,
				this.factUseMinutes14,
				this.beforeFactUseMinutes14,
				this.afterFactUseMinutes14,
				this.factUseTimes14,
				this.remainDays14,
				this.remainMinutes14,
				this.factRemainDays14,
				this.factRemainMinutes14,
				this.beforeRemainDays14,
				this.beforeRemainMinutes14,
				this.beforeFactRemainDays14,
				this.beforeFactRemainMinutes14,
				this.afterRemainDays14,
				this.afterRemainMinutes14,
				this.afterFactRemainDays14,
				this.afterFactRemainMinutes14,
				this.notUseDays14,
				this.notUseMinutes14,
				this.grantAtr14,
				this.grantDays14);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData15(){
		return this.toDomainSpecialHolidayRemainData(
				15,
				this.useDays15,
				this.beforeUseDays15,
				this.afterUseDays15,
				this.useMinutes15,
				this.beforeUseMinutes15,
				this.afterUseMinutes15,
				this.useTimes15,
				this.factUseDays15,
				this.beforeFactUseDays15,
				this.afterFactUseDays15,
				this.factUseMinutes15,
				this.beforeFactUseMinutes15,
				this.afterFactUseMinutes15,
				this.factUseTimes15,
				this.remainDays15,
				this.remainMinutes15,
				this.factRemainDays15,
				this.factRemainMinutes15,
				this.beforeRemainDays15,
				this.beforeRemainMinutes15,
				this.beforeFactRemainDays15,
				this.beforeFactRemainMinutes15,
				this.afterRemainDays15,
				this.afterRemainMinutes15,
				this.afterFactRemainDays15,
				this.afterFactRemainMinutes15,
				this.notUseDays15,
				this.notUseMinutes15,
				this.grantAtr15,
				this.grantDays15);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData16(){
		return this.toDomainSpecialHolidayRemainData(
				16,
				this.useDays16,
				this.beforeUseDays16,
				this.afterUseDays16,
				this.useMinutes16,
				this.beforeUseMinutes16,
				this.afterUseMinutes16,
				this.useTimes16,
				this.factUseDays16,
				this.beforeFactUseDays16,
				this.afterFactUseDays16,
				this.factUseMinutes16,
				this.beforeFactUseMinutes16,
				this.afterFactUseMinutes16,
				this.factUseTimes16,
				this.remainDays16,
				this.remainMinutes16,
				this.factRemainDays16,
				this.factRemainMinutes16,
				this.beforeRemainDays16,
				this.beforeRemainMinutes16,
				this.beforeFactRemainDays16,
				this.beforeFactRemainMinutes16,
				this.afterRemainDays16,
				this.afterRemainMinutes16,
				this.afterFactRemainDays16,
				this.afterFactRemainMinutes16,
				this.notUseDays16,
				this.notUseMinutes16,
				this.grantAtr16,
				this.grantDays16);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData17(){
		return this.toDomainSpecialHolidayRemainData(
				17,
				this.useDays17,
				this.beforeUseDays17,
				this.afterUseDays17,
				this.useMinutes17,
				this.beforeUseMinutes17,
				this.afterUseMinutes17,
				this.useTimes17,
				this.factUseDays17,
				this.beforeFactUseDays17,
				this.afterFactUseDays17,
				this.factUseMinutes17,
				this.beforeFactUseMinutes17,
				this.afterFactUseMinutes17,
				this.factUseTimes17,
				this.remainDays17,
				this.remainMinutes17,
				this.factRemainDays17,
				this.factRemainMinutes17,
				this.beforeRemainDays17,
				this.beforeRemainMinutes17,
				this.beforeFactRemainDays17,
				this.beforeFactRemainMinutes17,
				this.afterRemainDays17,
				this.afterRemainMinutes17,
				this.afterFactRemainDays17,
				this.afterFactRemainMinutes17,
				this.notUseDays17,
				this.notUseMinutes17,
				this.grantAtr17,
				this.grantDays17);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData18(){
		return this.toDomainSpecialHolidayRemainData(
				18,
				this.useDays18,
				this.beforeUseDays18,
				this.afterUseDays18,
				this.useMinutes18,
				this.beforeUseMinutes18,
				this.afterUseMinutes18,
				this.useTimes18,
				this.factUseDays18,
				this.beforeFactUseDays18,
				this.afterFactUseDays18,
				this.factUseMinutes18,
				this.beforeFactUseMinutes18,
				this.afterFactUseMinutes18,
				this.factUseTimes18,
				this.remainDays18,
				this.remainMinutes18,
				this.factRemainDays18,
				this.factRemainMinutes18,
				this.beforeRemainDays18,
				this.beforeRemainMinutes18,
				this.beforeFactRemainDays18,
				this.beforeFactRemainMinutes18,
				this.afterRemainDays18,
				this.afterRemainMinutes18,
				this.afterFactRemainDays18,
				this.afterFactRemainMinutes18,
				this.notUseDays18,
				this.notUseMinutes18,
				this.grantAtr18,
				this.grantDays18);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData19(){
		return this.toDomainSpecialHolidayRemainData(
				19,
				this.useDays19,
				this.beforeUseDays19,
				this.afterUseDays19,
				this.useMinutes19,
				this.beforeUseMinutes19,
				this.afterUseMinutes19,
				this.useTimes19,
				this.factUseDays19,
				this.beforeFactUseDays19,
				this.afterFactUseDays19,
				this.factUseMinutes19,
				this.beforeFactUseMinutes19,
				this.afterFactUseMinutes19,
				this.factUseTimes19,
				this.remainDays19,
				this.remainMinutes19,
				this.factRemainDays19,
				this.factRemainMinutes19,
				this.beforeRemainDays19,
				this.beforeRemainMinutes19,
				this.beforeFactRemainDays19,
				this.beforeFactRemainMinutes19,
				this.afterRemainDays19,
				this.afterRemainMinutes19,
				this.afterFactRemainDays19,
				this.afterFactRemainMinutes19,
				this.notUseDays19,
				this.notUseMinutes19,
				this.grantAtr19,
				this.grantDays19);
	}
	
	private SpecialHolidayRemainData toDomainSpecialHolidayRemainData20(){
		return this.toDomainSpecialHolidayRemainData(
				20,
				this.useDays20,
				this.beforeUseDays20,
				this.afterUseDays20,
				this.useMinutes20,
				this.beforeUseMinutes20,
				this.afterUseMinutes20,
				this.useTimes20,
				this.factUseDays20,
				this.beforeFactUseDays20,
				this.afterFactUseDays20,
				this.factUseMinutes20,
				this.beforeFactUseMinutes20,
				this.afterFactUseMinutes20,
				this.factUseTimes20,
				this.remainDays20,
				this.remainMinutes20,
				this.factRemainDays20,
				this.factRemainMinutes20,
				this.beforeRemainDays20,
				this.beforeRemainMinutes20,
				this.beforeFactRemainDays20,
				this.beforeFactRemainMinutes20,
				this.afterRemainDays20,
				this.afterRemainMinutes20,
				this.afterFactRemainDays20,
				this.afterFactRemainMinutes20,
				this.notUseDays20,
				this.notUseMinutes20,
				this.grantAtr20,
				this.grantDays20);
	}
	
	/**
	 * KRCDT_MON_DAYOFF_REMAIN
	 * 代休月別残数データ
	 * @return MonthlyDayoffRemainData
	 */
	public MonthlyDayoffRemainData toDomainMonthlyDayoffRemainData() {
		
		return new MonthlyDayoffRemainData(
				this.krcdtMonRemainPk.getEmployeeId(),
				new YearMonth(this.krcdtMonRemainPk.getYearMonth()),
				this.krcdtMonRemainPk.getClosureId(),
				this.krcdtMonRemainPk.getClosureDay(),
				(this.krcdtMonRemainPk.getIsLastDay() != 0),
				EnumAdaptor.valueOf(closureStatus, ClosureStatus.class),
				this.startDate,
				this.endDate,
				new DayOffDayAndTimes(
						new RemainDataDaysMonth(this.dayOffOccurredDays),
						this.dayOffOccurredTimes == null ? Optional.empty() : Optional.ofNullable(
								new RemainDataTimesMonth(this.dayOffOccurredTimes))),
				new DayOffDayAndTimes(
						new RemainDataDaysMonth(this.dayOffUsedDays), 
						this.dayOffUsedMinutes == null ? Optional.empty() : Optional.ofNullable(
								new RemainDataTimesMonth(this.dayOffUsedMinutes))),
				new DayOffRemainDayAndTimes(
						new AttendanceDaysMonthToTal(this.dayOffRemainingDays), 
						this.dayOffRemainingMinutes == null ? Optional.empty() : Optional.ofNullable(
								new RemainingMinutes(this.dayOffRemainingMinutes))),
				new DayOffRemainDayAndTimes(
						new AttendanceDaysMonthToTal(this.dayOffCarryforwardDays), 
						this.dayOffCarryforwardMinutes == null ? Optional.empty() : Optional.ofNullable(
								new RemainingMinutes(this.dayOffCarryforwardMinutes))),
				new DayOffDayAndTimes(
						new RemainDataDaysMonth(this.dayOffUnUsedDays),
						this.dayOffUnUsedTimes == null ? Optional.empty() : Optional.ofNullable(
								new RemainDataTimesMonth(this.dayOffUnUsedTimes))));
	}
	
	/**
	 * KRCDT_MON_SUBOFHD_REMAIN
	 * 振休使用日数合計
	 * @return AbsenceLeaveRemainData
	 */
	public AbsenceLeaveRemainData toDomainAbsenceLeaveRemainData() {
		
		return new AbsenceLeaveRemainData(
				this.krcdtMonRemainPk.getEmployeeId(),
				new YearMonth(this.krcdtMonRemainPk.getYearMonth()),
				this.krcdtMonRemainPk.getClosureId(),
				this.krcdtMonRemainPk.getClosureDay(),
				(this.krcdtMonRemainPk.getIsLastDay() != 0),
				EnumAdaptor.valueOf(this.closureStatus, ClosureStatus.class),
				this.startDate,
				this.endDate,
				new RemainDataDaysMonth(this.subofHdOccurredDays),
				new RemainDataDaysMonth(this.subofHdUsedDays),
				new AttendanceDaysMonthToTal(this.subofHdRemainingDays),
				new AttendanceDaysMonthToTal(this.subofHdCarryForWardDays),
				new RemainDataDaysMonth(this.subofHdUnUsedDays));
	}
	
	/**
	 * KRCDT_MON_CHILD_HD_REMAIN
	 * 子の看護月別残数データ
	 * @return MonChildHdRemain
	 */
	public MonChildHdRemain toDomainMonChildHdRemain(){
		
		return new MonChildHdRemain(
				this.krcdtMonRemainPk.getEmployeeId(),
				new YearMonth(this.krcdtMonRemainPk.getYearMonth()),
				EnumAdaptor.valueOf(this.krcdtMonRemainPk.getClosureId(), ClosureId.class),
				new Day(this.krcdtMonRemainPk.getClosureDay()),
				this.krcdtMonRemainPk.getIsLastDay(),
				EnumAdaptor.valueOf(this.closureStatus, ClosureStatus.class),
				this.startDate,
				this.endDate,
				new MonChildHdNumber(this.childUsedDays),
				new MonChildHdNumber(this.childUsedDaysBefore),
				new MonChildHdNumber(this.childUsedDaysAfter == null ? 0.0 : this.childUsedDaysAfter),
				new MonChildHdMinutes(this.childUsedMinutes == null ? 0 : this.childUsedMinutes),
				new MonChildHdMinutes(this.childUsedMinutesBefore == null ? 0 : this.childUsedMinutesBefore),
				new MonChildHdMinutes(this.childUsedMinutesAfter == null ? 0 : this.childUsedMinutesAfter));
	}
	
	/**
	 * KRCDT_MON_CARE_HD_REMAIN
	 * 介護休暇月別残数データ
	 * @return MonCareHdRemain
	 */
	public MonCareHdRemain toDomainMonCareHdRemain(){
	
		return new MonCareHdRemain(
				this.krcdtMonRemainPk.getEmployeeId(),
				new YearMonth(this.krcdtMonRemainPk.getYearMonth()),
				EnumAdaptor.valueOf(this.krcdtMonRemainPk.getClosureId(), ClosureId.class),
				new Day(this.krcdtMonRemainPk.getClosureDay()),
				this.krcdtMonRemainPk.getIsLastDay(),
				EnumAdaptor.valueOf(this.closureStatus, ClosureStatus.class),
				this.startDate,
				this.endDate,
				new MonCareHdNumber(this.careUsedDays),
				new MonCareHdNumber(this.careUsedDaysBefore),
				new MonCareHdNumber(this.careUsedDaysAfter == null ? 0.0 : this.careUsedDaysAfter),
				new MonCareHdMinutes(this.careUsedMinutes == null ? 0 : this.careUsedMinutes),
				new MonCareHdMinutes(this.careUsedMinutesBefore == null ? 0 : this.careUsedMinutesBefore),
				new MonCareHdMinutes(this.careUsedMinutesAfter == null ? 0 : this.careUsedMinutesAfter));
	}
}
