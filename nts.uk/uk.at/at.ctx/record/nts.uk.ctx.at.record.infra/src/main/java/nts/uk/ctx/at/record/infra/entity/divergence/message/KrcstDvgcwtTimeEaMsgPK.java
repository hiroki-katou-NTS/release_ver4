package nts.uk.ctx.at.record.infra.entity.divergence.message;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the KRCST_WTDVGC_TIME_EA_MSG database table.
 * 
 */
@Getter
@Setter
@Embeddable
public class KrcstDvgcwtTimeEaMsgPK implements Serializable {
	
	/** The Constant serialVersionUID. */
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	/** The cid. */
	@Column(name="CID")
	private String cid;

	/** The dvgc time no. */
	@Column(name="DVGC_TIME_NO")
	private Integer dvgcTimeNo;

	/** The worktype cd. */
	@Column(name="WORKTYPE_CD")
	private String worktypeCd;

	/**
	 * Instantiates a new krcst wtdvgc time ea msg PK.
	 */
	public KrcstDvgcwtTimeEaMsgPK() {
	}
	
	/**
	 * Instantiates a new krcst wtdvgc time ea msg PK.
	 *
	 * @param cid the cid
	 * @param dvgcTimeNo the dvgc time no
	 * @param worktypeCd the worktype cd
	 */
	public KrcstDvgcwtTimeEaMsgPK(String cid, Integer dvgcTimeNo, String worktypeCd) {
		this.cid = cid;
		this.dvgcTimeNo = dvgcTimeNo;
		this.worktypeCd = worktypeCd;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KrcstDvgcwtTimeEaMsgPK)) {
			return false;
		}
		KrcstDvgcwtTimeEaMsgPK castOther = (KrcstDvgcwtTimeEaMsgPK)other;
		return 
			this.cid.equals(castOther.cid)
			&& (this.dvgcTimeNo == castOther.dvgcTimeNo)
			&& this.worktypeCd.equals(castOther.worktypeCd);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cid.hashCode();
		hash = hash * prime + ((int) (this.dvgcTimeNo ^ (this.dvgcTimeNo >>> 32)));
		hash = hash * prime + this.worktypeCd.hashCode();
		
		return hash;
	}
}