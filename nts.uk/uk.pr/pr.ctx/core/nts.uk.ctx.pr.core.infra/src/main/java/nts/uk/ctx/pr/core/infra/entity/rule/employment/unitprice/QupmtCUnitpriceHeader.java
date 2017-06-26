/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class QupmtCUnitpriceHeader.
 */
@Getter
@Setter
@Entity
@Table(name = "QUPMT_C_UNITPRICE_HEADER")
public class QupmtCUnitpriceHeader extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The qupmt C unitprice header PK. */
	@EmbeddedId
	protected QupmtCUnitpriceHeaderPK qupmtCUnitpriceHeaderPK;

	/** The c unitprice name. */
	@Column(name = "C_UNITPRICE_NAME")
	private String cUnitpriceName;

	/** The qupmt C unitprice hist list. */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "qupmtCUnitpriceHeader")
	private List<QupmtCUnitpriceDetail> qupmtCUnitpriceHistList;

	/**
	 * Instantiates a new qupmt C unitprice header.
	 */
	public QupmtCUnitpriceHeader() {
		super();
	}

	/**
	 * Instantiates a new qupmt C unitprice header.
	 *
	 * @param qupmtCUnitpriceHeaderPK
	 *            the qupmt C unitprice header PK
	 */
	public QupmtCUnitpriceHeader(QupmtCUnitpriceHeaderPK qupmtCUnitpriceHeaderPK) {
		this.qupmtCUnitpriceHeaderPK = qupmtCUnitpriceHeaderPK;
	}

	/**
	 * Instantiates a new qupmt C unitprice header.
	 *
	 * @param ccd
	 *            the ccd
	 * @param cUnitpriceCd
	 *            the c unitprice cd
	 */
	public QupmtCUnitpriceHeader(String ccd, String cUnitpriceCd) {
		this.qupmtCUnitpriceHeaderPK = new QupmtCUnitpriceHeaderPK(ccd, cUnitpriceCd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (qupmtCUnitpriceHeaderPK != null ? qupmtCUnitpriceHeaderPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof QupmtCUnitpriceHeader)) {
			return false;
		}
		QupmtCUnitpriceHeader other = (QupmtCUnitpriceHeader) object;
		if ((this.qupmtCUnitpriceHeaderPK == null && other.qupmtCUnitpriceHeaderPK != null)
				|| (this.qupmtCUnitpriceHeaderPK != null
						&& !this.qupmtCUnitpriceHeaderPK.equals(other.qupmtCUnitpriceHeaderPK))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.qupmtCUnitpriceHeaderPK;
	}
}
