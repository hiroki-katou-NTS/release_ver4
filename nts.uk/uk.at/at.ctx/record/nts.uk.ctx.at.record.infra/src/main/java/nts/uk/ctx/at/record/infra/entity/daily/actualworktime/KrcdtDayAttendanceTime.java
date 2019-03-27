package nts.uk.ctx.at.record.infra.entity.daily.actualworktime;

import java.io.Serializable;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
//import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.ConstraintTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.TotalWorkingTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.temporarytime.TemporaryTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workingtime.StayingTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeGoOutTimes;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTimeOfDaily;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaiseSalaryTimeOfDailyPerfor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.shorttimework.enums.ChildCareAttribute;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.infra.entity.breakorgoout.KrcdtDayBreakTime;
import nts.uk.ctx.at.record.infra.entity.daily.attendanceschedule.KrcdtDayWorkScheTime;
import nts.uk.ctx.at.record.infra.entity.daily.divergencetime.KrcdtDayDivergenceTime;
import nts.uk.ctx.at.record.infra.entity.daily.holidayworktime.KrcdtDayHolidyWork;
import nts.uk.ctx.at.record.infra.entity.daily.holidayworktime.KrcdtDayHolidyWorkTs;
//import nts.uk.ctx.at.record.infra.entity.daily.latetime.KrcdtDayLateTime;
//import nts.uk.ctx.at.record.infra.entity.daily.leaveearlytime.KrcdtDayLeaveEarlyTime;
import nts.uk.ctx.at.record.infra.entity.daily.legalworktime.KrcdtDayPrsIncldTime;
import nts.uk.ctx.at.record.infra.entity.daily.overtimework.KrcdtDayOvertimework;
import nts.uk.ctx.at.record.infra.entity.daily.overtimework.KrcdtDayOvertimeworkTs;
import nts.uk.ctx.at.record.infra.entity.daily.premiumtime.KrcdtDayPremiumTime;
//import nts.uk.ctx.at.record.infra.entity.daily.shortwork.KrcdtDaiShortWorkTime;
//import nts.uk.ctx.at.record.infra.entity.daily.shortwork.KrcdtDayShorttime;
import nts.uk.ctx.at.record.infra.entity.daily.vacation.KrcdtDayVacation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCDT_DAY_ATTENDANCE_TIME")
public class KrcdtDayAttendanceTime extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/* 主キー */
	@EmbeddedId
	public KrcdtDayAttendanceTimePK krcdtDayAttendanceTimePK;
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

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayOvertimework krcdtDayOvertimework;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = {
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayOvertimeworkTs krcdtDayOvertimeworkTs;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayPrsIncldTime krcdtDayPrsIncldTime;

//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcdtDayAttendanceTime", orphanRemoval = true)
//	public List<KrcdtDayLeaveEarlyTime> krcdtDayLeaveEarlyTime;
//
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcdtDayAttendanceTime", orphanRemoval = true)
//	public List<KrcdtDayLateTime> krcdtDayLateTime;
//
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcdtDayAttendanceTime", orphanRemoval = true)
//	public List<KrcdtDaiShortWorkTime> krcdtDaiShortWorkTime;
//	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcdtDayAttendanceTime", orphanRemoval = true)
//	public List<KrcdtDayShorttime> KrcdtDayShorttime;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayHolidyWork krcdtDayHolidyWork;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayHolidyWorkTs krcdtDayHolidyWorkTs;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayWorkScheTime krcdtDayWorkScheTime;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayBreakTime krcdtDayBreakTime;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayDivergenceTime krcdtDayDivergenceTime;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayVacation krcdtDayVacation;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayPremiumTime krcdtDayPremiumTime;
	
	
	@Override
	protected Object getKey() {
		return this.krcdtDayAttendanceTimePK;
	}
    

//	public List<KrcdtDayLeaveEarlyTime> getKrcdtDayLeaveEarlyTime() {
//		return krcdtDayLeaveEarlyTime == null ? new ArrayList<>() : krcdtDayLeaveEarlyTime;
//	}
//
//	public List<KrcdtDayLateTime> getKrcdtDayLateTime() {
//		return krcdtDayLateTime == null ? new ArrayList<>() : krcdtDayLateTime;
//	}


	public static KrcdtDayAttendanceTime create(String employeeId, GeneralDate ymd,
			AttendanceTimeOfDailyPerformance attendanceTime) {
		val entity = new KrcdtDayAttendanceTime();
		entity.krcdtDayAttendanceTimePK = new KrcdtDayAttendanceTimePK(attendanceTime.getEmployeeId(),
				attendanceTime.getYmd());
		entity.setData(attendanceTime);
		return entity;
	}
	
	public void setData(AttendanceTimeOfDailyPerformance attendanceTime){
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
	}

	/**
	 * 
	 * @param ymd
	 *            entityを取得するために使用した年月日
	 * @param entity
	 * @return
	 */
	public AttendanceTimeOfDailyPerformance toDomain() {

		OverTimeOfDaily overTime = this.krcdtDayOvertimework == null  ? null : this.krcdtDayOvertimework.toDomain();
		if(overTime != null) overTime.getOverTimeWorkFrameTimeSheet()
				.addAll(this.krcdtDayOvertimeworkTs != null ? this.krcdtDayOvertimeworkTs.toDomain().getOverTimeWorkFrameTimeSheet() : new ArrayList<>());
		HolidayWorkTimeOfDaily holiday = this.krcdtDayHolidyWork == null ? null : this.krcdtDayHolidyWork.toDomain();
		if(holiday != null) holiday.getHolidayWorkFrameTimeSheet()
				.addAll(this.krcdtDayHolidyWorkTs != null ? this.krcdtDayHolidyWorkTs.toDomain(): new ArrayList<>());
		ExcessOfStatutoryTimeOfDaily excess = new ExcessOfStatutoryTimeOfDaily(
				new ExcessOfStatutoryMidNightTime(
						TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(this.outPrsMidnTime), new AttendanceTime(this.calcOutPrsMidnTime)),
						new AttendanceTime(this.preOutPrsMidnTime)),
				Optional.ofNullable(overTime), Optional.ofNullable(holiday));
		List<LateTimeOfDaily> lateTime = new ArrayList<>();
//		for (KrcdtDayLateTime krcdt : getKrcdtDayLateTime()) {
//			lateTime.add(krcdt.toDomain());
//		}
		List<LeaveEarlyTimeOfDaily> leaveEarly = new ArrayList<>();
//		for (KrcdtDayLeaveEarlyTime krcdt : getKrcdtDayLeaveEarlyTime()) {
//			leaveEarly.add(krcdt.toDomain());
//		}
		
		BreakTimeOfDaily breakTime = this.krcdtDayBreakTime == null ? defaultBreakTime() : createBreakTime();

		// 日別実績の総労働時間
		TotalWorkingTime totalTime = new TotalWorkingTime(new AttendanceTime(this.totalAttTime),
				new AttendanceTime(this.totalCalcTime), new AttendanceTime(this.actWorkTime),
				this.krcdtDayPrsIncldTime == null ? null : this.krcdtDayPrsIncldTime.toDomain(), excess, lateTime, leaveEarly,
				breakTime,
				Collections.emptyList(),
				new RaiseSalaryTimeOfDailyPerfor(Collections.emptyList(), Collections.emptyList()),
				new WorkTimes(this.workTimes), new TemporaryTimeOfDaily(),
				new  ShortWorkTimeOfDaily(new WorkTimes(1),
						 DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)),
								 			   TimeWithCalculation.sameTime(new AttendanceTime(0)),
								 			   TimeWithCalculation.sameTime(new AttendanceTime(0))),
						 DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)),
					 			   			   TimeWithCalculation.sameTime(new AttendanceTime(0)),
					 			   			   TimeWithCalculation.sameTime(new AttendanceTime(0))),
						 ChildCareAttribute.CARE),
				this.krcdtDayVacation == null ? null : this.krcdtDayVacation.toDomain()
				);

		// 日別実績の勤務実績時間
		ActualWorkingTimeOfDaily actual = ActualWorkingTimeOfDaily.of(totalTime, 
																	  this.midnBindTime,
																	  this.totalBindTime,
																	  this.bindDiffTime,
																	  this.diffTimeWorkTime, 
																	  krcdtDayDivergenceTime == null ? new DivergenceTimeOfDaily() : krcdtDayDivergenceTime.toDomain(),
																	  this.krcdtDayPremiumTime == null ? new PremiumTimeOfDailyPerformance() : this.krcdtDayPremiumTime.toDomain());
		// 日別実績の勤怠時間
		return new AttendanceTimeOfDailyPerformance(this.krcdtDayAttendanceTimePK == null ? null : this.krcdtDayAttendanceTimePK.employeeID, this.krcdtDayAttendanceTimePK.generalDate,
				this.krcdtDayWorkScheTime == null ? null : this.krcdtDayWorkScheTime.toDomain(), actual,
				new StayingTimeOfDaily(new AttendanceTimeOfExistMinus(this.aftPcLogoffTime),
						new AttendanceTimeOfExistMinus(this.bfrPcLogonTime), new AttendanceTimeOfExistMinus(this.bfrWorkTime),
						new AttendanceTime(this.stayingTime), new AttendanceTimeOfExistMinus(this.aftLeaveTime)),
				new AttendanceTimeOfExistMinus(this.budgetTimeVariance), new AttendanceTimeOfExistMinus(this.unemployedTime));
	}


	private BreakTimeOfDaily defaultBreakTime() {
		return BreakTimeOfDaily.sameTotalTime(
								DeductionTotalTime.of(
										TimeWithCalculation.sameTime(new AttendanceTime(0)),
										TimeWithCalculation.sameTime(new AttendanceTime(0)),
										TimeWithCalculation.sameTime(new AttendanceTime(0))));
	}


	private BreakTimeOfDaily createBreakTime() {
		return new BreakTimeOfDaily(
				DeductionTotalTime.of(
						TimeWithCalculation.createTimeWithCalculation(
								toAttendanceTime(this.krcdtDayBreakTime.toRecordTotalTime), 
								toAttendanceTime(this.krcdtDayBreakTime.calToRecordTotalTime)), 
						TimeWithCalculation.createTimeWithCalculation(
								toAttendanceTime(this.krcdtDayBreakTime.toRecordInTime), 
								toAttendanceTime(this.krcdtDayBreakTime.calToRecordInTime)), 
						TimeWithCalculation.createTimeWithCalculation(
								toAttendanceTime(this.krcdtDayBreakTime.toRecordOutTime), 
								toAttendanceTime(this.krcdtDayBreakTime.calToRecordOutTime))), 
				DeductionTotalTime.of(
						TimeWithCalculation.createTimeWithCalculation(
								toAttendanceTime(this.krcdtDayBreakTime.deductionTotalTime), 
								toAttendanceTime(this.krcdtDayBreakTime.calDeductionTotalTime)), 
						TimeWithCalculation.createTimeWithCalculation(
								toAttendanceTime(this.krcdtDayBreakTime.deductionInTime), 
								toAttendanceTime(this.krcdtDayBreakTime.calDeductionInTime)), 
						TimeWithCalculation.createTimeWithCalculation(
								toAttendanceTime(this.krcdtDayBreakTime.deductionOutTime), 
								toAttendanceTime(this.krcdtDayBreakTime.calDeductionOutTime))), 
				this.krcdtDayBreakTime.count == null ? null : new BreakTimeGoOutTimes(this.krcdtDayBreakTime.count), 
				new AttendanceTime(this.krcdtDayBreakTime.duringworkTime), 
				new ArrayList<>());
	}

	private AttendanceTime toAttendanceTime(Integer time){
		return time == null ? null : new AttendanceTime(time);
	}
}
