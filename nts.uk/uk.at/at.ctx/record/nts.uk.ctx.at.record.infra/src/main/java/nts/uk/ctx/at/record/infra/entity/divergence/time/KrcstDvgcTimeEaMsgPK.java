package nts.uk.ctx.at.record.infra.entity.divergence.time;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the KRCST_DVGC_TIME_EA_MSG database table.
 * 
 */
@Getter
@Setter
@Embeddable
public class KrcstDvgcTimeEaMsgPK implements Serializable {

	/** The Constant serialVersionUID. */
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	/** The cid. */
	@Column(name = "CID")
	private String cid;

	/** The dvgc time no. */
	@Column(name = "DVGC_TIME_NO")
	private Integer dvgcTimeNo;

	/**
	 * Instantiates a new krcst dvgc time ea msg PK.
	 */
	public KrcstDvgcTimeEaMsgPK() {
	}

	/**
	 * Instantiates a new krcst dvgc time ea msg PK.
	 *
	 * @param cid
	 *            the cid
	 * @param dvgcTimeNo
	 *            the dvgc time no
	 */
	public KrcstDvgcTimeEaMsgPK(String cid, Integer dvgcTimeNo) {
		this.cid = cid;
		this.dvgcTimeNo = dvgcTimeNo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KrcstDvgcTimeEaMsgPK)) {
			return false;
		}
		KrcstDvgcTimeEaMsgPK castOther = (KrcstDvgcTimeEaMsgPK) other;
		return this.cid.equals(castOther.cid) && (this.dvgcTimeNo == castOther.dvgcTimeNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cid.hashCode();
		hash = hash * prime + ((int) (this.dvgcTimeNo ^ (this.dvgcTimeNo >>> 32)));

		return hash;
	}
}