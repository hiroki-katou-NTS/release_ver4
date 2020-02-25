package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

/**
 * 
 * @author nampt
 *
 */
//@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_DAY_ERAL_SU_ATD")
public class KrcdtErSuAtd extends KrcdtErAttendanceItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static KrcdtErSuAtd toEntity(String id, int attendanceItemId, String cid, 
			String sid, String ccd, GeneralDate processDate){
		KrcdtErSuAtd krcdtErAttendanceItem = new KrcdtErSuAtd();
		KrcdtErAttendanceItemPK krcdtErAttendanceItemPK = new KrcdtErAttendanceItemPK(id, attendanceItemId);
		krcdtErAttendanceItem.krcdtErAttendanceItemPK = krcdtErAttendanceItemPK;
		krcdtErAttendanceItem.ccd = ccd;
		krcdtErAttendanceItem.cid = cid;
		krcdtErAttendanceItem.sid = cid;
		krcdtErAttendanceItem.processDate = processDate;
		return krcdtErAttendanceItem;
	}
}
