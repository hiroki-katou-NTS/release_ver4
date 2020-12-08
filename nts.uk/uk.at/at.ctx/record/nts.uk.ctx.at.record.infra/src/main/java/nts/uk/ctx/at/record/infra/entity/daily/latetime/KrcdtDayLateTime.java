package nts.uk.ctx.at.record.infra.entity.daily.latetime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.infra.entity.daily.time.KrcdtDayTimeAtd;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeWithCalculation;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.latetime.LateTimeOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.temporarytime.WorkNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.IntervalExemptionTime;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

@Entity
@Table(name = "KRCDT_DAY_LATETIME")
public class KrcdtDayLateTime extends ContractUkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/* 主キー */
	@EmbeddedId
	public KrcdtDayLateTimePK krcdtDayLateTimePK;

	/* 遅刻時間 */
	@Column(name = "LATE_TIME")
	public int lateTime;
	/* 計算遅刻時間 */
	@Column(name = "CALC_LATE_TIME")
	public int calcLateTime;
	/* 遅刻控除時間 */
	@Column(name = "LATE_DEDCT_TIME")
	public int lateDedctTime;
	/* 計算遅刻控除時間 */
	@Column(name = "CALC_LATE_DEDCT_TIME")
	public int calcLateDedctTime;
	/* 時間年休使用時間 */
	@Column(name = "TIME_ANALLV_USE_TIME")
	public int timeAnallvUseTime;
	/* 時間代休使用時間 */
	@Column(name = "TIME_CMPNSTLV_USE_TIME")
	public int timeCmpnstlvUseTime;
	/* 超過有休使用時間 */
	@Column(name = "OVER_PAY_VACTN_USE_TIME")
	public int overPayVactnUseTime;
	/* 特別休暇使用時間 */
	@Column(name = "SP_VACTN_USE_TIME")
	public int spVactnUseTime;

//	@ManyToOne
//	@JoinColumns(value = {
//			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
//			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
//	public KrcdtDayAttendanceTime krcdtDayAttendanceTime;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = {
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayTimeAtd krcdtDayTime;
	
	@Override
	protected Object getKey() {
		return this.krcdtDayLateTimePK;
	}

	public static KrcdtDayLateTime create(String employeeId, GeneralDate date, LateTimeOfDaily domain) {
		val entity = new KrcdtDayLateTime();
		/* 主キー */
		entity.krcdtDayLateTimePK = new KrcdtDayLateTimePK(employeeId, date, domain.getWorkNo().v());
		entity.setData(domain);
		return entity;
	}
	
	public void setData(LateTimeOfDaily domain){
		if(domain == null){
			return;
		}
		if(domain.getLateTime() != null){
			/* 遅刻時間 */
			this.lateTime = domain.getLateTime().getTime() == null ? 0 : domain.getLateTime().getTime().valueAsMinutes();
			/* 計算遅刻時間 */
			this.calcLateTime = domain.getLateTime().getCalcTime() == null ? this.lateTime : domain.getLateTime().getCalcTime().valueAsMinutes();
		}
		if(domain.getLateDeductionTime() != null){
			/* 遅刻控除時間 */
			this.lateDedctTime = domain.getLateDeductionTime().getTime() == null ? 0 : domain.getLateDeductionTime().getTime().valueAsMinutes();
			/* 計算遅刻控除時間 */
			this.calcLateDedctTime = domain.getLateDeductionTime().getCalcTime() == null ? this.calcLateDedctTime : domain.getLateDeductionTime().getCalcTime().valueAsMinutes();
		}
		TimevacationUseTimeOfDaily vacation = domain.getTimePaidUseTime();
		if(vacation != null){
			/* 時間年休使用時間 */
			this.timeAnallvUseTime = vacation.getTimeAnnualLeaveUseTime() == null ? 0 : vacation.getTimeAnnualLeaveUseTime().valueAsMinutes();
			/* 時間代休使用時間 */
			this.timeCmpnstlvUseTime = vacation.getTimeCompensatoryLeaveUseTime() == null ? 0 : vacation.getTimeCompensatoryLeaveUseTime().valueAsMinutes();
			/* 超過有休使用時間 */
			this.overPayVactnUseTime = vacation.getSixtyHourExcessHolidayUseTime() == null ? 0 : vacation.getSixtyHourExcessHolidayUseTime().valueAsMinutes();
			/* 特別休暇使用時間 */
			this.spVactnUseTime = vacation.getTimeSpecialHolidayUseTime() == null ? 0 : vacation.getTimeSpecialHolidayUseTime().valueAsMinutes();
		}
	}

	public LateTimeOfDaily toDomain() {
		return new LateTimeOfDaily(
				TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.lateTime),
						new AttendanceTime(this.calcLateTime)),
				TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.lateDedctTime),
						new AttendanceTime(this.calcLateDedctTime)),
				new WorkNo(this.krcdtDayLateTimePK.workNo),
				new TimevacationUseTimeOfDaily(new AttendanceTime(this.timeAnallvUseTime),
						new AttendanceTime(this.timeCmpnstlvUseTime), new AttendanceTime(this.overPayVactnUseTime),
						new AttendanceTime(this.spVactnUseTime)),
				new IntervalExemptionTime());
	}

}
