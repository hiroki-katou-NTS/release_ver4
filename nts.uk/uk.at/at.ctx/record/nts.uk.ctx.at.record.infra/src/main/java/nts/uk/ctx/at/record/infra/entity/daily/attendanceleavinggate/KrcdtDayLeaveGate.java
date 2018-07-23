package nts.uk.ctx.at.record.infra.entity.daily.attendanceleavinggate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The persistent class for the KRCDT_DAY_LEAVE_GATE database table.
 * 
 */
@Entity
@Table(name = "KRCDT_DAY_LEAVE_GATE")
// @NamedQuery(name="KrcdtDayLeaveGate.findAll", query="SELECT k FROM
// KrcdtDayLeaveGate k")
public class KrcdtDayLeaveGate extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtDayLeaveGatePK id;

	@Column(name = "ATTENDANCE_PLACE_CODE")
	public String attendancePlaceCode;

	@Column(name = "ATTENDANCE_STAMP_SOURCE")
	public Integer attendanceStampSource;

	@Column(name = "ATTENDANCE_TIME")
	public Integer attendanceTime;

	@Column(name = "LEAVE_PLACE_CODE")
	public String leavePlaceCode;

	@Column(name = "LEAVE_STAMP_SOURCE")
	public Integer leaveStampSource;

	@Column(name = "LEAVE_TIME")
	public Integer leaveTime;

	public KrcdtDayLeaveGate() {
	}

	public KrcdtDayLeaveGate(KrcdtDayLeaveGatePK id) {
		super();
		this.id = id;
	}
	
	public static List<KrcdtDayLeaveGate> from(AttendanceLeavingGateOfDaily domain){
		return domain.getAttendanceLeavingGates().stream().map(al -> 
			from(domain.getEmployeeId(), domain.getYmd(), al)
		).collect(Collectors.toList());
	}
	
	public static KrcdtDayLeaveGate from(String eId, GeneralDate ymd, AttendanceLeavingGate domain) {
		KrcdtDayLeaveGate entity = new KrcdtDayLeaveGate(new KrcdtDayLeaveGatePK(eId, ymd, domain.getWorkNo().v()));
		entity.setData(domain);
		return entity;
	}
	
	public void setData(AttendanceLeavingGate al) {
		if(al.getAttendance().isPresent()){
			al.getAttendance().ifPresent(a -> {
				this.attendancePlaceCode = a.getLocationCode().isPresent() ? a.getLocationCode().get().v() : null;
				this.attendanceTime = getTime(a.getTimeWithDay());
				this.attendanceStampSource = getSourceStamp(a.getStampSourceInfo());
			});
		} else {
			this.attendancePlaceCode = null;
			this.attendanceTime = null;
			this.attendanceStampSource = null;
		}
		if(al.getLeaving().isPresent()){
			al.getLeaving().ifPresent(a -> {
				this.leavePlaceCode = a.getLocationCode().isPresent() ? a.getLocationCode().get().v() : null;
				this.leaveTime = getTime(a.getTimeWithDay());
				this.leaveStampSource = getSourceStamp(a.getStampSourceInfo());
			});
		} else {
			this.leavePlaceCode = null;
			this.leaveTime = null;
			this.leaveStampSource = null;
		}
		
	}

	private static Integer getSourceStamp(StampSourceInfo a) {
		return a == null ? null : a.value;
	}

	private static Integer getTime(TimeWithDayAttr a) {
		return a == null ? null : a.valueAsMinutes();
	}

	public AttendanceLeavingGate toDomain() {
		return new AttendanceLeavingGate(new WorkNo(id.alNo),
				new WorkStamp(toTimeWithDay(attendanceTime), toTimeWithDay(attendanceTime),
						toWorkLocationCD(attendancePlaceCode), toWorkLocationCD(attendanceStampSource)),
				new WorkStamp(toTimeWithDay(leaveTime), toTimeWithDay(leaveTime), toWorkLocationCD(leavePlaceCode),
						toWorkLocationCD(leaveStampSource)));
	}


	private TimeWithDayAttr toTimeWithDay(Integer time) {
		return time == null ? null : new TimeWithDayAttr(time);
	}

	private WorkLocationCD toWorkLocationCD(String placeCode) {
		return placeCode == null ? null : new WorkLocationCD(placeCode);
	}

	private StampSourceInfo toWorkLocationCD(Integer stampSource) {
		return stampSource == null ? null : EnumAdaptor.valueOf(stampSource, StampSourceInfo.class);
	}

	@Override
	protected Object getKey() {
		return this.id;
	}
}