/*
 * 
 */
package nts.uk.ctx.at.record.infra.entity.divergence.time;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the KRCST_DVGC_TIME database table.
 * 
 */
@Getter
@Setter
@Embeddable
public class KrcstDvgcTimePK implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The no. */
	@Column(name="[NO]")
	private Integer no;

	/** The cid. */
	@Column(name="CID")
	private String cid;

	/**
	 * Instantiates a new krcst dvgc time PK.
	 */
	public KrcstDvgcTimePK() {
	}
	
	/**
	 * Instantiates a new krcst dvgc time PK.
	 *
	 * @param no the no
	 * @param cid the cid
	 */
	public KrcstDvgcTimePK(Integer no, String cid) {
		this.no= no;
		this.cid = cid;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KrcstDvgcTimePK)) {
			return false;
		}
		KrcstDvgcTimePK castOther = (KrcstDvgcTimePK)other;
		return 
			(this.no == castOther.no)
			&& this.cid.equals(castOther.cid);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.no ^ (this.no >>> 32)));
		hash = hash * prime + this.cid.hashCode();
		
		return hash;
	}
}