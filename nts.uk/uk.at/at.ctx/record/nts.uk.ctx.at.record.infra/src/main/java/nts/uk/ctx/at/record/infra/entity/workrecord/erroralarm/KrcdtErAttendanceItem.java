package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_DAY_ERAL_ATD")
public class KrcdtErAttendanceItem extends UkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtErAttendanceItemPK krcdtErAttendanceItemPK;
	
	@Column(nullable = false, name = "CID")
	public String cid;
	
	@Column(nullable = false, name = "SID")
	public String sid;
	
	@Column(nullable = false, name = "CONTRACT_CD")
	public String ccd;
	
	@Column(nullable = false, name = "PROCESSING_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate processDate;
	
	@Override
	protected Object getKey() {
		return this.krcdtErAttendanceItemPK;
	}
	
//	@ManyToOne
//	@JoinColumns({ @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)})
//	public KrcdtOtkErAl erOtk;
//	
//	@ManyToOne
//	@JoinColumns({ @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)})
//	public KrcdtEmpDivErAl erDiv;
//	
//	@ManyToOne
//	@JoinColumns({ @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)})
//	public KrcdtSyainDpErList erOth;
	
	public static KrcdtErAttendanceItem toEntity(String id, int attendanceItemId, String cid, 
			String sid, String ccd, GeneralDate processDate){
		KrcdtErAttendanceItem krcdtErAttendanceItem = new KrcdtErAttendanceItem();
		KrcdtErAttendanceItemPK krcdtErAttendanceItemPK = new KrcdtErAttendanceItemPK(id, attendanceItemId);
		krcdtErAttendanceItem.krcdtErAttendanceItemPK = krcdtErAttendanceItemPK;
		krcdtErAttendanceItem.ccd = ccd;
		krcdtErAttendanceItem.cid = cid;
		krcdtErAttendanceItem.sid = cid;
		krcdtErAttendanceItem.processDate = processDate;
		return krcdtErAttendanceItem;
	}
}
