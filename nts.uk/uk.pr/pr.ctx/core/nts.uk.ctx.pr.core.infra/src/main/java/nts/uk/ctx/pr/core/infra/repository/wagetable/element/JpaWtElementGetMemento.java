/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.element;

import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElementGetMemento;
import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElement;

/**
 * The Class JpaWageTableElementGetMemento.
 */
public class JpaWtElementGetMemento implements WtElementGetMemento {

	/** The type value. */
	private QwtmtWagetableElement typeValue;

	/**
	 * Instantiates a new jpa wage table element get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaWtElementGetMemento(QwtmtWagetableElement typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.element.WageTableElementGetMemento#
	 * getDemensionNo()
	 */
	@Override
	public DemensionNo getDemensionNo() {
		return DemensionNo.valueOf(this.typeValue.getQwtmtWagetableElementPK().getDemensionNo());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.core.dom.wagetable.element.WtElementGetMemento#getElementType()
	 */
	@Override
	public ElementType getElementType() {
		return ElementType.valueOf(this.typeValue.getDemensionType());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.core.dom.wagetable.element.WtElementGetMemento#getRefCode()
	 */
	@Override
	public String getRefCode() {
		return this.typeValue.getDemensionRefNo();
	}

}
