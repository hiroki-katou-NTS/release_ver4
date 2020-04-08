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
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_DAY_ERAL_OTK_ATD")
public class KrcdtErOtkAtd extends KrcdtErAttendanceItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static KrcdtErOtkAtd toEntity(String id, int attendanceItemId, String cid, 
			String sid, String ccd, GeneralDate processDate){
		KrcdtErOtkAtd krcdtErAttendanceItem = new KrcdtErOtkAtd();
		KrcdtErAttendanceItemPK krcdtErAttendanceItemPK = new KrcdtErAttendanceItemPK(id, attendanceItemId);
		krcdtErAttendanceItem.krcdtErAttendanceItemPK = krcdtErAttendanceItemPK;
		krcdtErAttendanceItem.ccd = ccd;
		krcdtErAttendanceItem.cid = cid;
		krcdtErAttendanceItem.sid = sid;
		krcdtErAttendanceItem.processDate = processDate;
		return krcdtErAttendanceItem;
	}
}
