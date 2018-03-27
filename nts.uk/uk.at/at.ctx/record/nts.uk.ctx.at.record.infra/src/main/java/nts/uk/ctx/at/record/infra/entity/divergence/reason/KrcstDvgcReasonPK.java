package nts.uk.ctx.at.record.infra.entity.divergence.reason;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the KRCST_DVGC_REASON database table.
 * 
 */
@Getter
@Setter
@Embeddable
public class KrcstDvgcReasonPK implements Serializable {
	
	/** The Constant serialVersionUID. */
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	/** The no. */
	@Column(name="[NO]")
	private Integer no;

	/** The cid. */
	@Column(name="CID")
	private String cid;

	/** The reason code. */
	@Column(name="REASON_CD")
	private String reasonCode;

	/**
	 * Instantiates a new krcst dvgc reason PK.
	 */
	public KrcstDvgcReasonPK() {
	}
	
	/**
	 * Instantiates a new krcst dvgc reason PK.
	 *
	 * @param no the no
	 * @param cid the cid
	 * @param reasonCode the reason code
	 */
	public KrcstDvgcReasonPK(Integer no, String cid, String reasonCode) {
		this.no = no;
		this.cid = cid;
		this.reasonCode = reasonCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KrcstDvgcReasonPK)) {
			return false;
		}
		KrcstDvgcReasonPK castOther = (KrcstDvgcReasonPK)other;
		return 
			(this.no == castOther.no)
			&& this.cid.equals(castOther.cid)
			&& this.reasonCode.equals(castOther.reasonCode);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.no ^ (this.no >>> 32)));
		hash = hash * prime + this.cid.hashCode();
		hash = hash * prime + this.reasonCode.hashCode();
		
		return hash;
	}
}