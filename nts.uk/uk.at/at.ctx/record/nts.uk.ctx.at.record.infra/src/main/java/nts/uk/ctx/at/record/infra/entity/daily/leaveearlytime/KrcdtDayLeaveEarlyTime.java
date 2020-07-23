package nts.uk.ctx.at.record.infra.entity.daily.leaveearlytime; 

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.val;
import nts.arc.layer.infra.data.jdbc.JdbcProxy;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.latetime.IntervalExemptionTime;
//import nts.uk.ctx.at.record.infra.entity.daily.actualworktime.KrcdtDayAttendanceTime;
import nts.uk.ctx.at.record.infra.entity.daily.time.KrcdtDayTime;
import nts.uk.ctx.at.record.infra.entity.daily.time.KrcdtDayTime.Query;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCDT_DAY_LEAVEEARLYTIME")
public class KrcdtDayLeaveEarlyTime  extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final JpaEntityMapper<KrcdtDayLeaveEarlyTime> MAPPER = new JpaEntityMapper<>(KrcdtDayLeaveEarlyTime.class);
	
	public static class Query {
		public static List<KrcdtDayLeaveEarlyTime> find(JdbcProxy proxy, KrcdtDayTime.Query.Key key) {
			return KrcdtDayTime.Query.query(proxy, "KRCDT_DAY_LEAVEEARLYTIME", key, MAPPER);
		}
	}

	public KrcdtDayTime.Query.RecordKey getRecordKey() {
		return new KrcdtDayTime.Query.RecordKey(this.krcdtDayLeaveEarlyTimePK.employeeID, this.krcdtDayLeaveEarlyTimePK.generalDate);
	}
	
	/*主キー*/
	@EmbeddedId
	public KrcdtDayLeaveEarlyTimePK krcdtDayLeaveEarlyTimePK;
	
	/*早退時間*/
	@Column(name = "LEAVEEARLY_TIME")
	public int leaveEarlyTime;
	/*計算早退時間*/
	@Column(name = "CALC_LEAVEEARLY_TIME")
	public int calcLeaveEarlyTime;
	/*早退控除時間*/
	@Column(name = "LEAVEEARLY_DEDCT_TIME")
	public int leaveEarlyDedctTime;
	/*計算早退控除時間*/
	@Column(name = "CALC_LEAVEEARLY_DEDCT_TIME")
	public int calcLeaveEarlyDedctTime;
	/*時間年休使用時間*/
	@Column(name = "TIME_ANALLV_USE_TIME")
	public int timeAnallvUseTime;
	/*時間代休使用時間*/
	@Column(name = "TIME_CMPNSTLV_USE_TIME")
	public int timeCmpnstlvUseTime;
	/*超過有休使用時間*/
	@Column(name = "OVER_PAY_VACTN_USE_TIME")
	public int overPayVactnUseTime;
	/*特別休暇使用時間*/
	@Column(name = "SP_VACTN_USE_TIME")
	public int spVactnUseTime;
	
//	@ManyToOne
//	@JoinColumns(value = { 
//			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
//			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false)
//	})
//	public KrcdtDayAttendanceTime krcdtDayAttendanceTime;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = { 
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false)
	})
	public KrcdtDayTime krcdtDayTime;
	
	@Override
	protected Object getKey() {
		return this.krcdtDayLeaveEarlyTimePK;
	}
	
	public static KrcdtDayLeaveEarlyTime create(String employeeId, GeneralDate ymd, LeaveEarlyTimeOfDaily leaveEarlyTime) {
		val entity = new KrcdtDayLeaveEarlyTime();
		entity.krcdtDayLeaveEarlyTimePK = new KrcdtDayLeaveEarlyTimePK(employeeId, ymd, leaveEarlyTime.getWorkNo().v().intValue());
		entity.setData(leaveEarlyTime);
		return entity;
	}
	
	public void setData(LeaveEarlyTimeOfDaily leaveEarlyTime){
		if(leaveEarlyTime == null){
			return;
		}
		if(leaveEarlyTime.getLeaveEarlyTime() != null){
			TimeWithCalculation leaveEarly = leaveEarlyTime.getLeaveEarlyTime();
			//早退時間
			this.leaveEarlyTime = leaveEarly.getTime() == null ? 0 : leaveEarly.getTime().valueAsMinutes();
			this.calcLeaveEarlyTime = leaveEarly.getCalcTime() == null ? 0 : leaveEarly.getCalcTime().valueAsMinutes();
		}
		if(leaveEarlyTime.getLeaveEarlyDeductionTime() != null){
			TimeWithCalculation leaveEarlyDedct = leaveEarlyTime.getLeaveEarlyDeductionTime();
			//早退控除時間
			this.leaveEarlyDedctTime = leaveEarlyDedct.getTime() == null ? 0 : leaveEarlyDedct.getTime().valueAsMinutes();
			this.calcLeaveEarlyDedctTime = leaveEarlyDedct.getCalcTime() == null ? 0 : leaveEarlyDedct.getCalcTime().valueAsMinutes();
		}
		//休暇使用時間
		if(leaveEarlyTime.getTimePaidUseTime() != null){
			TimevacationUseTimeOfDaily vacationUse = leaveEarlyTime.getTimePaidUseTime();
			//年休
			this.timeAnallvUseTime = vacationUse.getTimeAnnualLeaveUseTime() == null ? 0 : vacationUse.getTimeAnnualLeaveUseTime().valueAsMinutes();
			//代休
			this.timeCmpnstlvUseTime = vacationUse.getTimeCompensatoryLeaveUseTime() == null ? 0 : vacationUse.getTimeCompensatoryLeaveUseTime().valueAsMinutes();
			//超過
			this.overPayVactnUseTime = vacationUse.getSixtyHourExcessHolidayUseTime() == null ? 0 : vacationUse.getSixtyHourExcessHolidayUseTime().valueAsMinutes();
			//特別休暇
			this.spVactnUseTime = vacationUse.getTimeSpecialHolidayUseTime() == null ? 0 : vacationUse.getTimeSpecialHolidayUseTime().valueAsMinutes();
		}
	}
	
	
	
	public LeaveEarlyTimeOfDaily toDomain() {
		TimevacationUseTimeOfDaily timeVacation = new TimevacationUseTimeOfDaily(new AttendanceTime(this.timeAnallvUseTime),
																				 new AttendanceTime(this.timeCmpnstlvUseTime),
																				 new AttendanceTime(this.overPayVactnUseTime),
																				 new AttendanceTime(this.spVactnUseTime));
		
		return new LeaveEarlyTimeOfDaily(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.leaveEarlyTime), new AttendanceTime(this.calcLeaveEarlyTime)),
										 TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.leaveEarlyDedctTime), new AttendanceTime(this.calcLeaveEarlyDedctTime)),
										 new WorkNo(this.krcdtDayLeaveEarlyTimePK == null ? null : this.krcdtDayLeaveEarlyTimePK.workNo),
										 timeVacation,
										 new IntervalExemptionTime(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0)));
	}
}
