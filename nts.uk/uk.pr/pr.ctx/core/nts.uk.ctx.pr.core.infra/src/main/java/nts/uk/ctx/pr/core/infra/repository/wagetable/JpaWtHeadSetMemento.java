/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable;

import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.pr.core.dom.wagetable.ElementCount;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.WtHeadSetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.WtName;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.ctx.pr.core.infra.entity.wagetable.QwtmtWagetableHead;
import nts.uk.ctx.pr.core.infra.entity.wagetable.QwtmtWagetableHeadPK;
import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElement;
import nts.uk.ctx.pr.core.infra.repository.wagetable.element.JpaWtElementSetMemento;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class JpaWageTableHeadSetMemento.
 */
public class JpaWtHeadSetMemento implements WtHeadSetMemento {

	/** The type value. */
	private QwtmtWagetableHead typeValue;

	/**
	 * Instantiates a new jpa wage table head set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaWtHeadSetMemento(QwtmtWagetableHead typeValue) {
		this.typeValue = typeValue;
	}

	/**
	 * Sets the company code.
	 *
	 * @param companyCode
	 *            the new company code
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		QwtmtWagetableHeadPK qwtmtWagetableHeadPK = new QwtmtWagetableHeadPK();
		qwtmtWagetableHeadPK.setCcd(companyCode);
		this.typeValue.setQwtmtWagetableHeadPK(qwtmtWagetableHeadPK);
	}

	/**
	 * Sets the code.
	 *
	 * @param code
	 *            the new code
	 */
	@Override
	public void setCode(WtCode code) {
		QwtmtWagetableHeadPK qwtmtWagetableHeadPK = this.typeValue.getQwtmtWagetableHeadPK();
		qwtmtWagetableHeadPK.setWageTableCd(code.v());
		this.typeValue.setQwtmtWagetableHeadPK(qwtmtWagetableHeadPK);
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	@Override
	public void setName(WtName name) {
		this.typeValue.setWageTableName(name.v());
	}

	/**
	 * Sets the memo.
	 *
	 * @param memo
	 *            the new memo
	 */
	@Override
	public void setMemo(Memo memo) {
		if (memo != null) {
			this.typeValue.setMemo(memo.v());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.WtHeadSetMemento#setMode(nts.uk.ctx.pr.
	 * core.dom.wagetable.ElementCount)
	 */
	@Override
	public void setMode(ElementCount mode) {
		this.typeValue.setDemensionSet(mode.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.WtHeadSetMemento#setElements(java.util.
	 * List)
	 */
	@Override
	public void setElements(List<WtElement> elements) {
		// Get pk info
		QwtmtWagetableHeadPK qwtmtWagetableHeadPK = this.typeValue.getQwtmtWagetableHeadPK();

		// Convert to entity list.
		List<QwtmtWagetableElement> wagetableElementList = elements.stream().map(item -> {
			// Create entity.
			QwtmtWagetableElement entity = new QwtmtWagetableElement();

			// Transfer data.
			item.saveToMemento(new JpaWtElementSetMemento(qwtmtWagetableHeadPK.getCcd(),
					qwtmtWagetableHeadPK.getWageTableCd(), entity));

			// Return
			return entity;
		}).collect(Collectors.toList());

		// Set data
		this.typeValue.setWagetableElementList(wagetableElementList);
	}
}
