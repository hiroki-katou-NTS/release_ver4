package nts.uk.ctx.at.record.infra.entity.divergence.time;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the KRCST_DVGC_ATTENDANCE database table.
 * 
 */
@Getter
@Setter
@Embeddable
public class KrcstDvgcAttendancePK implements Serializable {
	
	/** The Constant serialVersionUID. */
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	/** The no. */
	@Column(name = "[NO]")
	private Integer no;

	/** The cid. */
	@Column(name = "CID")
	private String cid;

	/** The attendance id. */
	@Column(name = "ATTENDANCE_ID")
	private Integer attendanceId;

	/**
	 * Instantiates a new krcst dvgc attendance PK.
	 */
	public KrcstDvgcAttendancePK() {
	}

	/**
	 * Instantiates a new krcst dvgc attendance PK.
	 *
	 * @param no the no
	 * @param cid the cid
	 * @param attendanceId the attendance id
	 */
	public KrcstDvgcAttendancePK(Integer no, String cid, Integer attendanceId) {
		this.no = no;
		this.cid = cid;
		this.attendanceId = attendanceId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KrcstDvgcAttendancePK)) {
			return false;
		}
		KrcstDvgcAttendancePK castOther = (KrcstDvgcAttendancePK) other;
		return (this.no == castOther.no) && this.cid.equals(castOther.cid)
				&& (this.attendanceId == castOther.attendanceId);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.no ^ (this.no >>> 32)));
		hash = hash * prime + this.cid.hashCode();
		hash = hash * prime + ((int) (this.attendanceId ^ (this.attendanceId >>> 32)));

		return hash;
	}
}