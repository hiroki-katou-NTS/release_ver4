/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.element;

import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElementSetMemento;
import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElement;
import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElementPK;

/**
 * The Class JpaWtElementSetMemento.
 */
public class JpaWtElementSetMemento implements WtElementSetMemento {

	/** The type value. */
	private QwtmtWagetableElement typeValue;

	/**
	 * Instantiates a new jpa wt element set memento.
	 *
	 * @param companyCode the company code
	 * @param wageTableCode the wage table code
	 * @param typeValue the type value
	 */
	public JpaWtElementSetMemento(String companyCode, String wageTableCode, QwtmtWagetableElement typeValue) {
		this.typeValue = typeValue;
		QwtmtWagetableElementPK qwtmtWagetableElementPK = new QwtmtWagetableElementPK();
		qwtmtWagetableElementPK.setCcd(companyCode);
		qwtmtWagetableElementPK.setWageTableCd(wageTableCode);
		this.typeValue.setQwtmtWagetableElementPK(qwtmtWagetableElementPK);
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.core.dom.wagetable.element.WageTableElementSetMemento#
	 * setDemensionNo(nts.uk.ctx.pr.core.dom.wagetable.DemensionNo)
	 */
	@Override
	public void setDemensionNo(DemensionNo demensionNo) {
		QwtmtWagetableElementPK qwtmtWagetableElementPK = this.typeValue.getQwtmtWagetableElementPK();
		qwtmtWagetableElementPK.setDemensionNo(demensionNo.value);
		this.typeValue.setQwtmtWagetableElementPK(qwtmtWagetableElementPK);
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.core.dom.wagetable.element.WtElementSetMemento#
	 * setElementType(nts.uk.ctx.pr.core.dom.wagetable.ElementType)
	 */
	@Override
	public void setElementType(ElementType type) {
		this.typeValue.setDemensionType(type.value);

	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.core.dom.wagetable.element.WtElementSetMemento#
	 * setElementRefCode(java.lang.String)
	 */
	@Override
	public void setElementRefCode(String code) {
		this.typeValue.setDemensionRefNo(code);
	}

}
