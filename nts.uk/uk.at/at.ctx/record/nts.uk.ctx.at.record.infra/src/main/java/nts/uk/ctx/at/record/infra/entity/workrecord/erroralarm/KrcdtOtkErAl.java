package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author tin
 *
 */
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_DAY_OTK_ERAL")
public class KrcdtOtkErAl extends KrcdtEmpErAlCommon implements Serializable {

	private static final long serialVersionUID = 1L;

//	@Getter
//	@OneToMany(mappedBy = "erOtk", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//	public List<KrcdtErAttendanceItem> erAttendanceItem;

	public KrcdtOtkErAl(String id, String errorCode, String employeeId, GeneralDate processingDate, String companyID,
			String errorAlarmMessage, String contractCode, List<KrcdtErAttendanceItem> erAttendanceItem) {
		super(id, errorCode, employeeId, processingDate, companyID, errorAlarmMessage, contractCode, erAttendanceItem);
		
		this.erAttendanceItem = erAttendanceItem;
	}

	public static KrcdtOtkErAl toEntity(EmployeeDailyPerError er) {
		String ccd = AppContexts.user().contractCode();
		
		String id = IdentifierUtil.randomUniqueId();
		return new KrcdtOtkErAl(id, er.getErrorAlarmWorkRecordCode().v(),
				er.getEmployeeID(), er.getDate(),
				er.getCompanyID(),
				er.getErrorAlarmMessage().map(c -> c.v()).orElse(null), ccd, 
				er.getAttendanceItemList().stream()
						.map(item -> KrcdtErAttendanceItem.toEntity(id, item, 
									er.getCompanyID(), er.getEmployeeID(), ccd, er.getDate()))
						.collect(Collectors.toList())
				);
	}

	public static EmployeeDailyPerError toDomainForRes(List<KrcdtOtkErAl> entities) {
		return new EmployeeDailyPerError(entities.get(0).companyID, entities.get(0).employeeId,
				entities.get(0).processingDate, entities.get(0).errorCode, 
				entities.get(0).erAttendanceItem == null ? new ArrayList<>() : entities.get(0).erAttendanceItem
							.stream().map(c -> c.krcdtErAttendanceItemPK.attendanceItemId)
							.collect(Collectors.toList()),
				0, entities.get(0).errorAlarmMessage);
	}
	
}
