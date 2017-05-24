/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensleave;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class KclstEmpCompensLeavePK.
 */
@Getter
@Setter
@Embeddable
public class KclstEmpCompensLeavePK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cid. */
	@Column(name = "CID")
	private String cid;

	/** The contract type cd. */
	@Column(name = "CONTRACT_TYPE_CD")
	private String contractTypeCd;

	/**
	 * Instantiates a new kclst emp compens leave PK.
	 */
	public KclstEmpCompensLeavePK() {
		super();
	}

	/**
	 * Instantiates a new kclst emp compens leave PK.
	 *
	 * @param cid
	 *            the cid
	 * @param contractTypeCd
	 *            the contract type cd
	 */
	public KclstEmpCompensLeavePK(String cid, String contractTypeCd) {
		this.cid = cid;
		this.contractTypeCd = contractTypeCd;
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
		hash += (contractTypeCd != null ? contractTypeCd.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KclstEmpCompensLeavePK)) {
			return false;
		}
		KclstEmpCompensLeavePK other = (KclstEmpCompensLeavePK) object;
		if ((this.cid == null && other.cid != null)
				|| (this.cid != null && !this.cid.equals(other.cid))) {
			return false;
		}
		if ((this.contractTypeCd == null && other.contractTypeCd != null)
				|| (this.contractTypeCd != null
						&& !this.contractTypeCd.equals(other.contractTypeCd))) {
			return false;
		}
		return true;
	}

}
