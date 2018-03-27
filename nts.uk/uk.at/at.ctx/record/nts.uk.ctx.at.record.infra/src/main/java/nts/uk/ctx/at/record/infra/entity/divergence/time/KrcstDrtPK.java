package nts.uk.ctx.at.record.infra.entity.divergence.time;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the KRCST_DRT database table.
 * 
 */
@Embeddable
public class KrcstDrtPK implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The hist id. */
	@Column(name="HIST_ID")
	private String histId;

	/** The dvgc time no. */
	@Column(name="DVGC_TIME_NO")
	private Integer dvgcTimeNo;

	/**
	 * Instantiates a new krcst drt PK.
	 */
	public KrcstDrtPK() {
	}
	
	/**
	 * Instantiates a new krcst drt PK.
	 *
	 * @param histId the hist id
	 * @param dvgcTimeNo the dvgc time no
	 */
	public KrcstDrtPK(String histId, Integer dvgcTimeNo) {
		this.histId = histId;
		this.dvgcTimeNo = dvgcTimeNo;
	}
	
	/**
	 * Gets the hist id.
	 *
	 * @return the hist id
	 */
	public String getHistId() {
		return this.histId;
	}
	
	/**
	 * Sets the hist id.
	 *
	 * @param histId the new hist id
	 */
	public void setHistId(String histId) {
		this.histId = histId;
	}
	
	/**
	 * Gets the dvgc time no.
	 *
	 * @return the dvgc time no
	 */
	public Integer getDvgcTimeNo() {
		return this.dvgcTimeNo;
	}
	
	/**
	 * Sets the dvgc time no.
	 *
	 * @param dvgcTimeNo the new dvgc time no
	 */
	public void setDvgcTimeNo(Integer dvgcTimeNo) {
		this.dvgcTimeNo = dvgcTimeNo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KrcstDrtPK)) {
			return false;
		}
		KrcstDrtPK castOther = (KrcstDrtPK)other;
		return 
			this.histId.equals(castOther.histId)
			&& (this.dvgcTimeNo == castOther.dvgcTimeNo);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.histId.hashCode();
		hash = hash * prime + ((int) (this.dvgcTimeNo ^ (this.dvgcTimeNo >>> 32)));
		
		return hash;
	}
}