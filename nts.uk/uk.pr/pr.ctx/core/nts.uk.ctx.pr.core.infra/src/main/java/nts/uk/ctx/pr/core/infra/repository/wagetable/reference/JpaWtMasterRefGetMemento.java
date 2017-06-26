/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.reference;

import nts.uk.ctx.pr.core.dom.wagetable.WtElementRefNo;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRefGetMemento;
import nts.uk.ctx.pr.core.infra.entity.wagetable.reference.QwtmtWagetableRefTable;

/**
 * The Class JpaWageTableMasterRefGetMemento.
 */
public class JpaWtMasterRefGetMemento implements WtMasterRefGetMemento {

	/** The type value. */
	private QwtmtWagetableRefTable typeValue;

	/**
	 * Instantiates a new jpa wage table master ref get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaWtMasterRefGetMemento(QwtmtWagetableRefTable typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefGetMemento#
	 * getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQwtmtWagetableRefTablePK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefGetMemento#
	 * getRefNo()
	 */
	@Override
	public WtElementRefNo getRefNo() {
		return new WtElementRefNo(this.typeValue.getQwtmtWagetableRefTablePK().getRefTableNo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefGetMemento#
	 * getRefName()
	 */
	@Override
	public String getRefName() {
		return this.typeValue.getRefTableName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefGetMemento#
	 * getWageRefTable()
	 */
	@Override
	public String getWageRefTable() {
		return this.typeValue.getWageRefTable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefGetMemento#
	 * getWageRefField()
	 */
	@Override
	public String getWageRefField() {
		return this.typeValue.getWageRefField();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefGetMemento#
	 * getWageRefDispField()
	 */
	@Override
	public String getWageRefDispField() {
		return this.typeValue.getWageRefDispField();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefGetMemento#
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
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefGetMemento#
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
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefGetMemento#
	 * getWageRefQuery()
	 */
	@Override
	public String getWageRefQuery() {
		return this.typeValue.getWageRefQuery();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefGetMemento#
	 * getWagePersonQuery()
	 */
	@Override
	public String getWagePersonQuery() {
		return this.typeValue.getWagePersonQuery();
	}

}
