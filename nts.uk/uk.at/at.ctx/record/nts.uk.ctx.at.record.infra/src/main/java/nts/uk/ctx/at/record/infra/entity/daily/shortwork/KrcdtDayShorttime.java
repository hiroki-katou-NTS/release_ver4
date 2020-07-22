package nts.uk.ctx.at.record.infra.entity.daily.shortwork;

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

import lombok.NoArgsConstructor;
import lombok.val;
import nts.arc.layer.infra.data.jdbc.JdbcProxy;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.shorttimework.enums.ChildCareAttribute;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.infra.entity.daily.time.KrcdtDayTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author keisuke_hoshina
 *
 */
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_DAY_SHORTTIME")
public class KrcdtDayShorttime extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L; 
	
	public static final JpaEntityMapper<KrcdtDayShorttime> MAPPER = new JpaEntityMapper<>(KrcdtDayShorttime.class);
	
	public static class Query {
		public static List<KrcdtDayShorttime> find(JdbcProxy proxy, KrcdtDayTime.Query.Key key) {
			return KrcdtDayTime.Query.query(proxy, "KRCDT_DAY_SHORTTIME", key, MAPPER);
		}
	}

	public KrcdtDayTime.Query.RecordKey getRecordKey() {
		return new KrcdtDayTime.Query.RecordKey(this.krcdtDayShorttimePK.sid, this.krcdtDayShorttimePK.ymd);
	}
	
	@EmbeddedId
	public KrcdtDayShorttimePK krcdtDayShorttimePK;

	@Column(name ="TO_RECORD_TOTAL_TIME")
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
	
	@Column(name ="COUNT")
	public Integer count;
	
//	@ManyToOne
//	@JoinColumns(value = {
//			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
//			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
//	public KrcdtDayAttendanceTime krcdtDayAttendanceTime;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = {
			@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDayTime krcdtDayTime;
	
	@Override
	protected Object getKey() {
		return this.krcdtDayShorttimePK;
	}
	
	/**
	 * Entityへの変換 
	 */
	public static KrcdtDayShorttime toEntity(String employeeId, GeneralDate ymd,
			AttendanceTimeOfDailyPerformance attendanceTime) {
		val entity = new KrcdtDayShorttime();
		entity.krcdtDayShorttimePK = new KrcdtDayShorttimePK(attendanceTime.getEmployeeId(), attendanceTime.getYmd(),
															 attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getShotrTimeOfDaily().getChildCareAttribute().value);
		entity.setData(attendanceTime);
		return entity;
	}

	/**
	 * PrimaryKey以外の値セット 
	 */
	public void setData(AttendanceTimeOfDailyPerformance attendanceTime) {
		
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
		
		this.count = 0;
		
		if(attendanceTime != null) {
			if(attendanceTime.getActualWorkingTimeOfDaily() != null) {
				if(attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime() != null) {
					if(attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getShotrTimeOfDaily() != null) {
						val recordTime = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getShotrTimeOfDaily().getTotalTime();
						val dedTime = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getShotrTimeOfDaily().getTotalDeductionTime();
						val workTimes = attendanceTime.getActualWorkingTimeOfDaily().getTotalWorkingTime().getShotrTimeOfDaily().getWorkTimes();
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
							this.deductionOutTime = dedTime.getExcessOfStatutoryTotalTime().getTime() == null ? 0 : dedTime.getExcessOfStatutoryTotalTime().getTime().valueAsMinutes();
							this.calDeductionOutTime = dedTime.getExcessOfStatutoryTotalTime().getCalcTime() == null ? 0 : dedTime.getExcessOfStatutoryTotalTime().getCalcTime().valueAsMinutes();
						}
						if(workTimes != null) {
							this.count = workTimes.v() == null ? 0 : workTimes.v();
						}
					}
				}
			}
		}

	}
	
	/**
	 * ドメインへの変換
	 * @return 日別実績の休憩時間
	 */
	public ShortWorkTimeOfDaily toDomain() {
		return new ShortWorkTimeOfDaily(
									new WorkTimes(this.count),
									DeductionTotalTime.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.toRecordTotalTime), new AttendanceTime(this.calToRecordTotalTime)),
														  TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.toRecordInTime), new AttendanceTime(this.calToRecordInTime)),
														  TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.toRecordOutTime), new AttendanceTime(this.calToRecordOutTime))), 
									DeductionTotalTime.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.deductionTotalTime), new AttendanceTime(this.calDeductionTotalTime)),
											  			  TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.deductionInTime), new AttendanceTime(this.calDeductionInTime)),
											  			  TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.deductionOutTime), new AttendanceTime(this.calDeductionOutTime))),
									ChildCareAttribute.valueOf(this.krcdtDayShorttimePK.childCareAtr.toString())
									);
	}
}
