package nts.uk.ctx.at.record.infra.entity.worktime;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author nampt 出退勤
 *
 */
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_TIME_LEAVING_WORK")
public class KrcdtTimeLeavingWork extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtTimeLeavingWorkPK krcdtTimeLeavingWorkPK;

	@Column(name = "ATD_ACTUAL_ROUDING_TIME_DAY")
	public Integer attendanceActualRoudingTime;

	@Column(name = "ATD_ACTUAL_TIME")
	public Integer attendanceActualTime;

	@Column(name = "ATD_ACTUAL_PLACE_CODE")
	public String attendanceActualPlaceCode;

	@Column(name = "ATD_ACTUAL_SOURCE_INFO")
	public Integer attendanceActualSourceInfo;

	@Column(name = "ATD_STAMP_ROUDING_TIME_DAY")
	public Integer attendanceStampRoudingTime;

	@Column(name = "ATD_STAMP_TIME")
	public Integer attendanceStampTime;

	@Column(name = "ATD_STAMP_PLACE_CODE")
	public String attendanceStampPlaceCode;

	@Column(name = "ATD_STAMP_SOURCE_INFO")
	public Integer attendanceStampSourceInfo;

	@Column(name = "ATD_NUMBER_STAMP")
	public Integer attendanceNumberStamp;

	@Column(name = "LWK_ACTUAL_ROUDING_TIME_DAY")
	public Integer leaveWorkActualRoundingTime;

	@Column(name = "LWK_ACTUAL_TIME")
	public Integer leaveWorkActualTime;

	@Column(name = "LWK_ACTUAL_PLACE_CODE")
	public String leaveWorkActualPlaceCode;

	@Column(name = "LWK_ACTUAL_SOURCE_INFO")
	public Integer leaveActualSourceInfo;

	@Column(name = "LWK_STAMP_ROUDING_TIME_DAY")
	public Integer leaveWorkStampRoundingTime;

	@Column(name = "LWK_STAMP_TIME")
	public Integer leaveWorkStampTime;

	@Column(name = "LWK_STAMP_PLACE_CODE")
	public String leaveWorkStampPlaceCode;

	@Column(name = "LWK_STAMP_SOURCE_INFO")
	public Integer leaveWorkStampSourceInfo;

	@Column(name = "LWK_NUMBER_STAMP")
	public Integer leaveWorkNumberStamp;

	public TimeLeavingWork toDomain() {
		TimeLeavingWork domain = new TimeLeavingWork(new WorkNo(this.krcdtTimeLeavingWorkPK.workNo),
				Optional.of(new TimeActualStamp(
						getWorkStamp(this.attendanceActualRoudingTime, this.attendanceActualTime, 
								this.attendanceActualPlaceCode, this.attendanceActualSourceInfo),
						getWorkStamp(this.attendanceStampRoudingTime, this.attendanceStampTime,
								this.attendanceStampPlaceCode, this.attendanceStampSourceInfo),
						this.attendanceNumberStamp)),
				Optional.of(new TimeActualStamp(
						getWorkStamp(this.leaveWorkActualRoundingTime, this.leaveWorkActualTime,
								this.leaveWorkActualPlaceCode, this.leaveActualSourceInfo),
						getWorkStamp(this.leaveWorkStampRoundingTime, this.leaveWorkStampTime,
								this.leaveWorkStampPlaceCode, this.leaveWorkStampSourceInfo),
						this.leaveWorkNumberStamp)));
		return domain;
	}

	private WorkStamp getWorkStamp(Integer roudingTime, Integer time, String placeCode, Integer sourceInfo) {
		return new WorkStamp(
				roudingTime == null ? null : new TimeWithDayAttr(roudingTime),
				time == null ? null : new TimeWithDayAttr(time),
				placeCode == null ? null : new WorkLocationCD(placeCode),
				sourceInfo == null ? null : EnumAdaptor.valueOf(sourceInfo, StampSourceInfo.class));
	}

	public static KrcdtTimeLeavingWork toEntity(String employeeId, GeneralDate ymd, TimeLeavingWork domain, int type) {
		TimeActualStamp attendanceStamp = (domain.getAttendanceStamp() != null && domain.getAttendanceStamp().isPresent()) ? domain.getAttendanceStamp().get() : null;
		TimeActualStamp leaveStamp = (domain.getLeaveStamp() != null && domain.getLeaveStamp().isPresent()) ? domain.getLeaveStamp().get() : null;
		KrcdtTimeLeavingWork krcdtTimeLeavingWork = new KrcdtTimeLeavingWork();
		KrcdtTimeLeavingWorkPK krcdtTimeLeavingWorkPK = new KrcdtTimeLeavingWorkPK(employeeId, domain.getWorkNo().v(), ymd, type);
		krcdtTimeLeavingWork.krcdtTimeLeavingWorkPK = krcdtTimeLeavingWorkPK;
		toEntityAttendance(krcdtTimeLeavingWork, attendanceStamp);
		toEntityLeave(krcdtTimeLeavingWork, leaveStamp);
		return krcdtTimeLeavingWork;
//		return new KrcdtTimeLeavingWork(new KrcdtTimeLeavingWorkPK(employeeId, domain.getWorkNo().v(), ymd, type),
//				domain.getAttendanceStamp().get().getActualStamp().getAfterRoundingTime().valueAsMinutes(),
//				domain.getAttendanceStamp().get().getActualStamp().getTimeWithDay().valueAsMinutes(),
//				domain.getAttendanceStamp().get().getActualStamp().getLocationCode().v(),
//				domain.getAttendanceStamp().get().getActualStamp().getStampSourceInfo().value,
//				attendanceStamp == null ? null : attendanceStamp.getAfterRoundingTime().valueAsMinutes(),
//				attendanceStamp == null ? null : attendanceStamp.getTimeWithDay().valueAsMinutes(),
//				attendanceStamp.getLocationCode().v(),
//				attendanceStamp.getStampSourceInfo().value, domain.getAttendanceStamp().get().getNumberOfReflectionStamp(), 
//				domain.getLeaveStamp().get().getActualStamp().getAfterRoundingTime().valueAsMinutes(),
//				domain.getLeaveStamp().get().getActualStamp().getTimeWithDay().valueAsMinutes(),
//				domain.getLeaveStamp().get().getActualStamp().getLocationCode().v(),
//				domain.getLeaveStamp().get().getActualStamp().getStampSourceInfo().value,
//				leaveStamp == null ? null : leaveStamp.getAfterRoundingTime().valueAsMinutes(),
//				leaveStamp == null ? null : leaveStamp.getTimeWithDay().valueAsMinutes(),
//				leaveStamp.getLocationCode().v(),
//				leaveStamp.getStampSourceInfo().value, domain.getLeaveStamp().get().getNumberOfReflectionStamp());

	}
	
	private static void toEntityAttendance(KrcdtTimeLeavingWork krcdtTimeLeavingWork, TimeActualStamp attendanceStamp){
		if (attendanceStamp != null) {
			if(attendanceStamp.getActualStamp() != null && attendanceStamp.getActualStamp().isPresent()){
				val actualStamp = attendanceStamp.getActualStamp().get();
				krcdtTimeLeavingWork.attendanceActualPlaceCode = (actualStamp.getLocationCode() != null && actualStamp.getLocationCode().isPresent()) ? actualStamp.getLocationCode().get().v() : null;
				krcdtTimeLeavingWork.attendanceActualRoudingTime = actualStamp.getAfterRoundingTime() != null ? actualStamp.getAfterRoundingTime().valueAsMinutes() : null;
				krcdtTimeLeavingWork.attendanceActualSourceInfo = actualStamp.getStampSourceInfo() != null ? actualStamp.getStampSourceInfo().value : null;
				krcdtTimeLeavingWork.attendanceActualTime = actualStamp.getTimeWithDay() != null ? actualStamp.getTimeWithDay().v() : null;
			} else {
				krcdtTimeLeavingWork.attendanceActualPlaceCode = null;
				krcdtTimeLeavingWork.attendanceActualRoudingTime = null;
				krcdtTimeLeavingWork.attendanceActualTime = null;
			}
			if (attendanceStamp.getStamp() != null && attendanceStamp.getStamp().isPresent()) {
				val stamp = attendanceStamp.getStamp().get();
				krcdtTimeLeavingWork.attendanceStampPlaceCode = (stamp.getLocationCode() != null && stamp.getLocationCode().isPresent()) ? stamp.getLocationCode().get().v() : null;
				krcdtTimeLeavingWork.attendanceStampRoudingTime = stamp.getAfterRoundingTime() != null ? stamp.getAfterRoundingTime().valueAsMinutes() : null;
				krcdtTimeLeavingWork.attendanceStampSourceInfo = stamp.getStampSourceInfo() != null ? stamp.getStampSourceInfo().value : null;
				krcdtTimeLeavingWork.attendanceStampTime = stamp.getTimeWithDay() != null ? stamp.getTimeWithDay().v() : null;
			} else {
				krcdtTimeLeavingWork.attendanceStampPlaceCode = null;
				krcdtTimeLeavingWork.attendanceStampRoudingTime = null;
				krcdtTimeLeavingWork.attendanceStampTime = null;
			}
			krcdtTimeLeavingWork.attendanceNumberStamp = attendanceStamp.getNumberOfReflectionStamp();
			
		} else {
			krcdtTimeLeavingWork.attendanceActualPlaceCode = null;
			krcdtTimeLeavingWork.attendanceActualRoudingTime = null;
			krcdtTimeLeavingWork.attendanceActualTime = null;
			krcdtTimeLeavingWork.attendanceStampPlaceCode = null;
			krcdtTimeLeavingWork.attendanceStampRoudingTime = null;
			krcdtTimeLeavingWork.attendanceStampTime = null;
		}
		
	}
	
	private static void toEntityLeave(KrcdtTimeLeavingWork krcdtTimeLeavingWork, TimeActualStamp leaveStamp){
		if (leaveStamp != null) {
			if (leaveStamp.getActualStamp() != null && leaveStamp.getActualStamp().isPresent()) {
				WorkStamp actualStamp = leaveStamp.getActualStamp().orElse(null);
				krcdtTimeLeavingWork.leaveWorkActualPlaceCode = (actualStamp.getLocationCode() != null && actualStamp.getLocationCode().isPresent()) ? actualStamp.getLocationCode().get().v() : null;
				krcdtTimeLeavingWork.leaveWorkActualRoundingTime = actualStamp.getAfterRoundingTime() != null ? actualStamp.getAfterRoundingTime().valueAsMinutes() : null;
				krcdtTimeLeavingWork.leaveActualSourceInfo = actualStamp.getStampSourceInfo() != null ? actualStamp.getStampSourceInfo().value : null;
				krcdtTimeLeavingWork.leaveWorkActualTime = actualStamp.getTimeWithDay() != null ? actualStamp.getTimeWithDay().v() : null;
			} else {
				krcdtTimeLeavingWork.leaveWorkActualPlaceCode = null;
				krcdtTimeLeavingWork.leaveWorkActualRoundingTime = null;
				krcdtTimeLeavingWork.leaveWorkActualTime = null;
			}
			if (leaveStamp.getStamp() != null && leaveStamp.getStamp().isPresent()) {
				WorkStamp stamp = leaveStamp.getStamp().orElse(null);
				krcdtTimeLeavingWork.leaveWorkStampPlaceCode = (stamp.getLocationCode() != null && stamp.getLocationCode().isPresent()) ? stamp.getLocationCode().get().v() : null;
				krcdtTimeLeavingWork.leaveWorkStampRoundingTime = stamp.getAfterRoundingTime() != null ? stamp.getAfterRoundingTime().valueAsMinutes() : null;
				krcdtTimeLeavingWork.leaveWorkStampSourceInfo = stamp.getStampSourceInfo() != null ? stamp.getStampSourceInfo().value : null;
				krcdtTimeLeavingWork.leaveWorkStampTime = stamp.getTimeWithDay() != null ? stamp.getTimeWithDay().v() : null;
			} else {
				krcdtTimeLeavingWork.leaveWorkStampPlaceCode = null;
				krcdtTimeLeavingWork.leaveWorkStampRoundingTime = null;
				krcdtTimeLeavingWork.leaveWorkStampTime = null;
			}
			krcdtTimeLeavingWork.leaveWorkNumberStamp = leaveStamp.getNumberOfReflectionStamp();
		} else {
			krcdtTimeLeavingWork.leaveWorkActualPlaceCode = null;
			krcdtTimeLeavingWork.leaveWorkActualRoundingTime = null;
			krcdtTimeLeavingWork.leaveWorkActualTime = null;
			krcdtTimeLeavingWork.leaveWorkStampPlaceCode = null;
			krcdtTimeLeavingWork.leaveWorkStampRoundingTime = null;
			krcdtTimeLeavingWork.leaveWorkStampTime = null;
		}
	}

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDaiLeavingWork daiLeavingWork;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false),
			@JoinColumn(name = "YMD", referencedColumnName = "YMD", insertable = false, updatable = false) })
	public KrcdtDaiTemporaryTime daiTemporaryTime;

	public KrcdtTimeLeavingWork(KrcdtTimeLeavingWorkPK krcdtTimeLeavingWorkPK, Integer attendanceActualRoudingTime,
			Integer attendanceActualTime, String attendanceActualPlaceCode, Integer attendanceActualSourceInfo,
			Integer attendanceStampRoudingTime, Integer attendanceStampTime, String attendanceStampPlaceCode,
			Integer attendanceStampSourceInfo, Integer attendanceNumberStamp, Integer leaveWorkActualRoundingTime,
			Integer leaveWorkActualTime, String leaveWorkActualPlaceCode, Integer leaveActualSourceInfo,
			Integer leaveWorkStampRoundingTime, Integer leaveWorkStampTime, String leaveWorkStampPlaceCode,
			Integer leaveWorkStampSourceInfo, Integer leaveWorkNumberStamp) {
		super();
		this.krcdtTimeLeavingWorkPK = krcdtTimeLeavingWorkPK;
		this.attendanceActualRoudingTime = attendanceActualRoudingTime;
		this.attendanceActualTime = attendanceActualTime;
		this.attendanceActualPlaceCode = attendanceActualPlaceCode;
		this.attendanceActualSourceInfo = attendanceActualSourceInfo;
		this.attendanceStampRoudingTime = attendanceStampRoudingTime;
		this.attendanceStampTime = attendanceStampTime;
		this.attendanceStampPlaceCode = attendanceStampPlaceCode;
		this.attendanceStampSourceInfo = attendanceStampSourceInfo;
		this.attendanceNumberStamp = attendanceNumberStamp;
		this.leaveWorkActualRoundingTime = leaveWorkActualRoundingTime;
		this.leaveWorkActualTime = leaveWorkActualTime;
		this.leaveWorkActualPlaceCode = leaveWorkActualPlaceCode;
		this.leaveActualSourceInfo = leaveActualSourceInfo;
		this.leaveWorkStampRoundingTime = leaveWorkStampRoundingTime;
		this.leaveWorkStampTime = leaveWorkStampTime;
		this.leaveWorkStampPlaceCode = leaveWorkStampPlaceCode;
		this.leaveWorkStampSourceInfo = leaveWorkStampSourceInfo;
		this.leaveWorkNumberStamp = leaveWorkNumberStamp;
	}

	public static List<TimeLeavingWork> toDomain(List<KrcdtTimeLeavingWork> krcdtTimeLeavingWorks) {
		return krcdtTimeLeavingWorks.stream().map(f -> f.toDomain()).collect(Collectors.toList());
	};

//	public static KrcdtTimeLeavingWork toEntity(TimeLeavingWork timeLeavingWork, String employeeId, GeneralDate ymd, int workNo, int type) {
//		return new KrcdtTimeLeavingWork(new KrcdtTimeLeavingWorkPK(employeeId, workNo, ymd, type),
//				timeLeavingWork.getAttendanceStamp().get().getActualStamp().getAfterRoundingTime().v(),
//				timeLeavingWork.getAttendanceStamp().get().getActualStamp().getTimeWithDay().v(),
//				timeLeavingWork.getAttendanceStamp().get().getActualStamp().getLocationCode().v(),
//				timeLeavingWork.getAttendanceStamp().get().getActualStamp().getStampSourceInfo().value,
//				timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent() ?  timeLeavingWork.getAttendanceStamp().get().getStamp().get().getAfterRoundingTime().v() : null,
//				timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent() ?  timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay().v() : null,
//				timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent() ? timeLeavingWork.getAttendanceStamp().get().getStamp().get().getLocationCode().v() : null,
//				timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent() ?  timeLeavingWork.getAttendanceStamp().get().getStamp().get().getStampSourceInfo().value : null,
//				timeLeavingWork.getAttendanceStamp().get().getNumberOfReflectionStamp(), 
//				timeLeavingWork.getLeaveStamp().get().getActualStamp().getAfterRoundingTime().v(),
//				timeLeavingWork.getLeaveStamp().get().getActualStamp().getTimeWithDay().v(),
//				timeLeavingWork.getLeaveStamp().get().getActualStamp().getLocationCode().v(), 
//				timeLeavingWork.getLeaveStamp().get().getActualStamp().getStampSourceInfo().value,
//				timeLeavingWork.getLeaveStamp().get().getStamp().isPresent() ?  timeLeavingWork.getLeaveStamp().get().getStamp().get().getAfterRoundingTime().v() : null,
//				timeLeavingWork.getLeaveStamp().get().getStamp().isPresent() ?  timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeWithDay().v() : null,
//				timeLeavingWork.getLeaveStamp().get().getStamp().isPresent() ? timeLeavingWork.getLeaveStamp().get().getStamp().get().getLocationCode().v() : null,
//				timeLeavingWork.getLeaveStamp().get().getStamp().isPresent() ?  timeLeavingWork.getLeaveStamp().get().getStamp().get().getStampSourceInfo().value : null,
//				timeLeavingWork.getLeaveStamp().get().getNumberOfReflectionStamp());
//	}

	@Override
	protected Object getKey() {
		return this.krcdtTimeLeavingWorkPK;
	}

}
