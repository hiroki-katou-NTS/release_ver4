/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workrecord.monthcal.employment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class KrcstEmpRegMCalSetPK.
 */

/**
 * Gets the emp cd.
 *
 * @return the emp cd
 */
@Getter

/**
 * Sets the emp cd.
 *
 * @param empCd the new emp cd
 */
@Setter
@Embeddable
public class KrcstEmpRegMCalSetPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cid. */
	@Column(name = "CID")
	private String cid;

	/** The emp cd. */
	@Column(name = "EMP_CD")
	private String empCd;

	/**
	 * Instantiates a new krcst emp reg M cal set PK.
	 */
	public KrcstEmpRegMCalSetPK() {
		super();
	}

	/**
	 * Instantiates a new krcst emp reg M cal set PK.
	 *
	 * @param cid the cid
	 * @param empCd the emp cd
	 */
	public KrcstEmpRegMCalSetPK(String cid, String empCd) {
		super();
		this.cid = cid;
		this.empCd = empCd;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (cid != null ? cid.hashCode() : 0);
		hash += (empCd != null ? empCd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KrcstEmpRegMCalSetPK)) {
			return false;
		}
		KrcstEmpRegMCalSetPK other = (KrcstEmpRegMCalSetPK) object;
		if ((this.cid == null && other.cid != null)
				|| (this.cid != null && !this.cid.equals(other.cid))) {
			return false;
		}
		if ((this.empCd == null && other.empCd != null)
				|| (this.empCd != null && !this.empCd.equals(other.empCd))) {
			return false;
		}
		return true;
	}	

}
