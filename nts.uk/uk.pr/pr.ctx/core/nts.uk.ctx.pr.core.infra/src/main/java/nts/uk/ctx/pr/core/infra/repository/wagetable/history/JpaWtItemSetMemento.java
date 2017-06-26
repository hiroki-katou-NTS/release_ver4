/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.history;

import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.WtValue;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtItemSetMemento;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableMny;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableMnyPK;

/**
 * The Class JpaWageTableItemSetMemento.
 */
public class JpaWtItemSetMemento implements WtItemSetMemento {

	/** The type value. */
	private QwtmtWagetableMny typeValue;

	/**
	 * Instantiates a new jpa wage table item set memento.
	 *
	 * @param ccd
	 *            the ccd
	 * @param wageTableCd
	 *            the wage table cd
	 * @param histId
	 *            the hist id
	 * @param typeValue
	 *            the type value
	 */
	public JpaWtItemSetMemento(String ccd, String wageTableCd, String histId,
			QwtmtWagetableMny typeValue) {
		this.typeValue = typeValue;
		QwtmtWagetableMnyPK qwtmtWagetableMnyPK = new QwtmtWagetableMnyPK();
		qwtmtWagetableMnyPK.setCcd(ccd);
		qwtmtWagetableMnyPK.setWageTableCd(wageTableCd);
		qwtmtWagetableMnyPK.setHistId(histId);
		this.typeValue.setQwtmtWagetableMnyPK(qwtmtWagetableMnyPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemSetMemento#
	 * setElement1Id(nts.uk.ctx.pr.core.dom.wagetable.ElementId)
	 */
	@Override
	public void setElement1Id(ElementId element1Id) {
		if (element1Id == null) {
			return;
		}
		QwtmtWagetableMnyPK qwtmtWagetableMnyPK = this.typeValue.getQwtmtWagetableMnyPK();
		qwtmtWagetableMnyPK.setElement1Id(element1Id.v());
		this.typeValue.setQwtmtWagetableMnyPK(qwtmtWagetableMnyPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemSetMemento#
	 * setElement2Id(nts.uk.ctx.pr.core.dom.wagetable.ElementId)
	 */
	@Override
	public void setElement2Id(ElementId element2Id) {
		if (element2Id == null) {
			return;
		}
		QwtmtWagetableMnyPK qwtmtWagetableMnyPK = this.typeValue.getQwtmtWagetableMnyPK();
		qwtmtWagetableMnyPK.setElement2Id(element2Id.v());
		this.typeValue.setQwtmtWagetableMnyPK(qwtmtWagetableMnyPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemSetMemento#
	 * setElement3Id(nts.uk.ctx.pr.core.dom.wagetable.ElementId)
	 */
	@Override
	public void setElement3Id(ElementId element3Id) {
		if (element3Id == null) {
			return;
		}
		QwtmtWagetableMnyPK qwtmtWagetableMnyPK = this.typeValue.getQwtmtWagetableMnyPK();
		qwtmtWagetableMnyPK.setElement3Id(element3Id.v());
		this.typeValue.setQwtmtWagetableMnyPK(qwtmtWagetableMnyPK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableItemSetMemento#
	 * setAmount(java.math.BigDecimal)
	 */
	@Override
	public void setAmount(WtValue amount) {
		this.typeValue.setValueMny(amount.v());
	}

}
