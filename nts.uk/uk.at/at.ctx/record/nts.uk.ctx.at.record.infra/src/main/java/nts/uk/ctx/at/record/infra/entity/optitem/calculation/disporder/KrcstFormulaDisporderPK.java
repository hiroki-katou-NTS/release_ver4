/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.entity.optitem.calculation.disporder;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class KrcstFormulaDisporderPK.
 */
@Getter
@Setter
@Embeddable
public class KrcstFormulaDisporderPK implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cid. */
	@Column(name = "CID")
	private String cid;

	/** The optional item no. */
	@Column(name = "OPTIONAL_ITEM_NO")
	private String optionalItemNo;

	/** The formula id. */
	@Column(name = "FORMULA_ID")
	private String formulaId;

	/**
	 * Instantiates a new krcst formula disporder PK.
	 */
	public KrcstFormulaDisporderPK() {
		super();
	}

	/**
	 * Instantiates a new krcst formula disporder PK.
	 *
	 * @param cid the cid
	 * @param optionalItemNo the optional item no
	 * @param formulaId the formula id
	 */
	public KrcstFormulaDisporderPK(String cid, String optionalItemNo, String formulaId) {
		this.cid = cid;
		this.optionalItemNo = optionalItemNo;
		this.formulaId = formulaId;
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
		hash += (optionalItemNo != null ? optionalItemNo.hashCode() : 0);
		hash += (formulaId != null ? formulaId.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KrcstFormulaDisporderPK)) {
			return false;
		}
		KrcstFormulaDisporderPK other = (KrcstFormulaDisporderPK) object;
		if ((this.cid == null && other.cid != null) || (this.cid != null && !this.cid.equals(other.cid))) {
			return false;
		}
		if ((this.optionalItemNo == null && other.optionalItemNo != null)
				|| (this.optionalItemNo != null && !this.optionalItemNo.equals(other.optionalItemNo))) {
			return false;
		}
		if ((this.formulaId == null && other.formulaId != null)
				|| (this.formulaId != null && !this.formulaId.equals(other.formulaId))) {
			return false;
		}
		return true;
	}

}
