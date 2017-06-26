/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.history;

import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.WtValue;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtItemGetMemento;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableMny;

/**
 * The Class JpaWageTableItemGetMemento.
 */
public class JpaWtItemGetMemento implements WtItemGetMemento {

	/** The type value. */
	private QwtmtWagetableMny typeValue;

	/**
	 * Instantiates a new jpa wage table item get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaWtItemGetMemento(QwtmtWagetableMny typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemGetMemento#
	 * getElement1Id()
	 */
	@Override
	public ElementId getElement1Id() {
		return new ElementId(this.typeValue.getQwtmtWagetableMnyPK().getElement1Id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemGetMemento#
	 * getElement2Id()
	 */
	@Override
	public ElementId getElement2Id() {
		return new ElementId(this.typeValue.getQwtmtWagetableMnyPK().getElement2Id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemGetMemento#
	 * getElement3Id()
	 */
	@Override
	public ElementId getElement3Id() {
		return new ElementId(this.typeValue.getQwtmtWagetableMnyPK().getElement3Id());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemGetMemento#
	 * getAmount()
	 */
	@Override
	public WtValue getAmount() {
		return new WtValue(this.typeValue.getValueMny());
	}

}
