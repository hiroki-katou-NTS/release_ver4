package nts.uk.ctx.at.record.infra.entity.daily.actualworktime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.TotalWorkingTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.temporarytime.TemporaryTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workingtime.StayingTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaiseSalaryTimeOfDailyPerfor;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.infra.entity.daily.attendanceschedule.KrcdtDayWorkScheTime;
import nts.uk.ctx.at.record.infra.entity.daily.holidayworktime.KrcdtDayHolidyWork;
import nts.uk.ctx.at.record.infra.entity.daily.holidayworktime.KrcdtDayHolidyWorkTs;
import nts.uk.ctx.at.record.infra.entity.daily.latetime.KrcdtDayLateTime;
import nts.uk.ctx.at.record.infra.entity.daily.leaveearlytime.KrcdtDayLeaveEarlyTime;
import nts.uk.ctx.at.record.infra.entity.daily.legalworktime.KrcdtDayPrsIncldTime;
import nts.uk.ctx.at.record.infra.entity.daily.overtimework.KrcdtDayOvertimework;
import nts.uk.ctx.at.record.infra.entity.daily.overtimework.KrcdtDayOvertimeworkTs;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
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

	@OneToOne
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayOvertimework krcdtDayOvertimework;

	@OneToOne
	@JoinColumns(value = {
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayOvertimeworkTs krcdtDayOvertimeworkTs;

	@OneToOne
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayPrsIncldTime krcdtDayPrsIncldTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcdtDayAttendanceTime")
	public List<KrcdtDayLeaveEarlyTime> krcdtDayLeaveEarlyTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "krcdtDayAttendanceTime")
	public List<KrcdtDayLateTime> krcdtDayLateTime;

	@OneToOne
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayHolidyWork krcdtDayHolidyWork;

	@OneToOne
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayHolidyWorkTs krcdtDayHolidyWorkTs;

	@OneToOne
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayWorkScheTime krcdtDayWorkScheTime;

	@Override
	protected Object getKey() {
		return this.krcdtDayAttendanceTimePK;
	}

	public static KrcdtDayAttendanceTime create(String employeeId, GeneralDate ymd,
			AttendanceTimeOfDailyPerformance attendanceTime) {
		val entity = new KrcdtDayAttendanceTime();
		entity.krcdtDayAttendanceTimePK = new KrcdtDayAttendanceTimePK(attendanceTime.getEmployeeId(),
				attendanceTime.getYmd());
		/* 総労働時間 */
		entity.totalAttTime = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getTotalTime()
				.valueAsMinutes();
		/* 総計算時間 */
		entity.totalCalcTime = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getTotalCalcTime()
				.valueAsMinutes();
		/* 実働時間 */
		entity.actWorkTime = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getTotalCalcTime()
				.valueAsMinutes();
		/* 勤務回数 */
		entity.workTimes = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getWorkTimes().v();
		/* 総拘束時間 */
		entity.totalBindTime = attendanceTime.getActualWorkingTimeOfDaily().getConstraintTime().getTotalConstraintTime()
				.valueAsMinutes();
		/* 深夜拘束時間 */
		entity.midnBindTime = attendanceTime.getActualWorkingTimeOfDaily().getConstraintTime()
				.getLateNightConstraintTime().valueAsMinutes();
		/* 拘束差異時間 */
		entity.bindDiffTime = attendanceTime.getActualWorkingTimeOfDaily().getConstraintDifferenceTime()
				.valueAsMinutes();
		/* 時差勤務時間 */
		entity.diffTimeWorkTime = attendanceTime.getActualWorkingTimeOfDaily().getTimeDifferenceWorkingHours()
				.valueAsMinutes();
		/* 所定外深夜時間 */
		entity.outPrsMidnTime = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime()
				.getExcessOfStatutoryTimeOfDaily().getExcessOfStatutoryMidNightTime().getTime().getCalcTime()
				.valueAsMinutes();
		/* 事前所定外深夜時間 */
		entity.preOutPrsMidnTime = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime()
				.getExcessOfStatutoryTimeOfDaily().getExcessOfStatutoryMidNightTime().getBeforeApplicationTime()
				.valueAsMinutes();
		/* 予実差異時間 */
		entity.budgetTimeVariance = attendanceTime.getBudgetTimeVariance().valueAsMinutes();
		/* 不就労時間 */
		entity.unemployedTime = attendanceTime.getUnEmployedTime().valueAsMinutes();
		/* 滞在時間 */
		entity.stayingTime = attendanceTime.getStayingTime().getStayingTime().valueAsMinutes();
		/* 出勤前時間 */
		entity.bfrWorkTime = attendanceTime.getStayingTime().getBeforeWoringTime().valueAsMinutes();
		/* 退勤後時間 */
		entity.aftLeaveTime = attendanceTime.getStayingTime().getAfterLeaveTime().valueAsMinutes();
		/* PCログオン前時間 */
		entity.bfrPcLogonTime = attendanceTime.getStayingTime().getBeforePCLogOnTime().valueAsMinutes();
		/* PCログオフ後時間 */
		entity.aftPcLogoffTime = attendanceTime.getStayingTime().getAfterPCLogOffTime().valueAsMinutes();
		return entity;
	}

	/**
	 * 
	 * @param ymd
	 *            entityを取得するために使用した年月日
	 * @param entity
	 * @return
	 */
	public AttendanceTimeOfDailyPerformance toDomain(GeneralDate ymd) {

		OverTimeOfDaily overTime = this.krcdtDayOvertimework.toDomain();
		overTime.getOverTimeWorkFrameTimeSheet()
				.addAll(this.krcdtDayOvertimeworkTs.toDomain().getOverTimeWorkFrameTimeSheet());
		HolidayWorkTimeOfDaily holiday = this.krcdtDayHolidyWork.toDomain();
		holiday.getHolidayWorkFrameTimeSheet().addAll(this.krcdtDayHolidyWorkTs.toDomain());
		ExcessOfStatutoryTimeOfDaily excess = new ExcessOfStatutoryTimeOfDaily(
				new ExcessOfStatutoryMidNightTime(
						TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.outPrsMidnTime),
								new AttendanceTime(this.preOutPrsMidnTime)),
						new AttendanceTime(this.preOutPrsMidnTime)),
				Optional.of(overTime), Optional.of(holiday));
		List<LateTimeOfDaily> lateTime = new ArrayList<>();
		for (KrcdtDayLateTime krcdt : this.krcdtDayLateTime) {
			lateTime.add(krcdt.toDomain());
		}
		List<LeaveEarlyTimeOfDaily> leaveEarly = new ArrayList<>();
		for (KrcdtDayLeaveEarlyTime krcdt : this.krcdtDayLeaveEarlyTime) {
			leaveEarly.add(krcdt.toDomain());
		}

		// 日別実績の総労働時間
		TotalWorkingTime totalTime = new TotalWorkingTime(new AttendanceTime(this.totalAttTime),
				new AttendanceTime(this.totalCalcTime), new AttendanceTime(this.actWorkTime),
				this.krcdtDayPrsIncldTime.toDomain(), excess, lateTime, leaveEarly,
				BreakTimeOfDaily
						.sameTotalTime(DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)),
								TimeWithCalculation.sameTime(new AttendanceTime(0)),
								TimeWithCalculation.sameTime(new AttendanceTime(0)))),
				Collections.emptyList(),
				new RaiseSalaryTimeOfDailyPerfor(Collections.emptyList(), Collections.emptyList()),
				new WorkTimes(this.workTimes), new TemporaryTimeOfDaily());

		// 日別実績の勤務実績時間
		ActualWorkingTimeOfDaily actual = ActualWorkingTimeOfDaily.of(totalTime, this.midnBindTime, this.totalBindTime,
				this.bindDiffTime, this.diffTimeWorkTime);
		// 日別実績の勤怠時間
		return new AttendanceTimeOfDailyPerformance(this.krcdtDayAttendanceTimePK.employeeID, ymd,
				this.krcdtDayWorkScheTime.toDomain(), actual,
				new StayingTimeOfDaily(new AttendanceTime(this.aftPcLogoffTime),
						new AttendanceTime(this.bfrPcLogonTime), new AttendanceTime(this.bfrWorkTime),
						new AttendanceTime(this.stayingTime), new AttendanceTime(this.aftLeaveTime)),
				new AttendanceTime(this.budgetTimeVariance), new AttendanceTime(this.unemployedTime));
	}

}
