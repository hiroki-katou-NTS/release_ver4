package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class KrcdtEmpErAlCommon extends UkJpaEntity {

	@Id
	@Column(name = "ID")
	public String id;

	@Column(name = "ERROR_CODE")
	public String errorCode;

	@Column(name = "SID")
	public String employeeId;

	@Column(name = "PROCESSING_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate processingDate;

	@Column(name = "CID")
	public String companyID;
	
	@Column(name = "CONTRACT_CD")
	public String ccd;

	@Column(name = "ERROR_MESSAGE")
	public String errorAlarmMessage;
	
	@Transient
	public List<KrcdtErAttendanceItem> erAttendanceItem;

	@Override
	protected Object getKey() {
		return this.id;
	}

	public KrcdtEmpErAlCommon(String id, String errorCode, String employeeId, GeneralDate processingDate,
			String companyID, String errorAlarmMessage, String contractCode, List<KrcdtErAttendanceItem> erAttendanceItem) {
		super();
		this.id = id;
		this.errorCode = errorCode;
		this.employeeId = employeeId;
		this.processingDate = processingDate;
		this.companyID = companyID;
		this.errorAlarmMessage = errorAlarmMessage;
		this.ccd = contractCode;
		this.erAttendanceItem = erAttendanceItem;
	}

	public EmployeeDailyPerError toDomain() {
		return new EmployeeDailyPerError(this.companyID, this.employeeId,
				this.processingDate, this.errorCode, erAttendanceItem.stream()
						.map(c -> c.krcdtErAttendanceItemPK.attendanceItemId).collect(Collectors.toList()),
				0, this.errorAlarmMessage);
	}
	
	public List<KrcdtErAttendanceItem> getErAttendanceItem() {
		return erAttendanceItem;
	}
	
	public void setErAttendanceItem(List<KrcdtErAttendanceItem> er) {
		erAttendanceItem = er;
	}
}
