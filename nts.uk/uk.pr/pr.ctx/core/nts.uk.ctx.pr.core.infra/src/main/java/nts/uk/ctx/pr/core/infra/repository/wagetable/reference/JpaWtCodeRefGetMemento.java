/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.reference;

import nts.uk.ctx.pr.core.dom.wagetable.WtElementRefNo;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRefGetMemento;
import nts.uk.ctx.pr.core.infra.entity.wagetable.reference.QwtmtWagetableRefCd;

/**
 * The Class JpaWageTableCodeRefGetMemento.
 */
public class JpaWtCodeRefGetMemento implements WtCodeRefGetMemento {

	/** The type value. */
	private QwtmtWagetableRefCd typeValue;

	/**
	 * Instantiates a new jpa wage table code ref get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaWtCodeRefGetMemento(QwtmtWagetableRefCd typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableCodeRefGetMemento#
	 * getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQwtmtWagetableRefCdPK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableCodeRefGetMemento#
	 * getRefNo()
	 */
	@Override
	public WtElementRefNo getRefNo() {
		return new WtElementRefNo(this.typeValue.getQwtmtWagetableRefCdPK().getRefCdNo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableCodeRefGetMemento#
	 * getRefName()
	 */
	@Override
	public String getRefName() {
		return this.typeValue.getRefCdName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableCodeRefGetMemento#
	 * getWageRefValue()
	 */
	@Override
	public String getWageRefValue() {
		return this.typeValue.getWageRefValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableCodeRefGetMemento#
	 * getWagePersonTable()
	 */
	@Override
	public String getWagePersonTable() {
		return this.typeValue.getWagePersonTable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableCodeRefGetMemento#
	 * getWagePersonField()
	 */
	@Override
	public String getWagePersonField() {
		return this.typeValue.getWagePersonField();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableCodeRefGetMemento#
	 * getWagePersonQuery()
	 */
	@Override
	public String getWagePersonQuery() {
		return this.typeValue.getWagePersonQuery();
	}

}
