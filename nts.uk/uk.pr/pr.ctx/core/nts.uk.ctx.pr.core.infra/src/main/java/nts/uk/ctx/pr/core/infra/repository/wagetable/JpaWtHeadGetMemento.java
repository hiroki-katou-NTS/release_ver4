/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable;

import java.util.List;

import nts.gul.collection.LazyList;
import nts.uk.ctx.pr.core.dom.wagetable.ElementCount;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.WtHeadGetMemento;
import nts.uk.ctx.pr.core.dom.wagetable.WtName;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.ctx.pr.core.infra.entity.wagetable.QwtmtWagetableHead;
import nts.uk.ctx.pr.core.infra.repository.wagetable.element.JpaWtElementGetMemento;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class JpaWageTableHeadGetMemento.
 */
public class JpaWtHeadGetMemento implements WtHeadGetMemento {

	/** The type value. */
	private QwtmtWagetableHead typeValue;

	/**
	 * Instantiates a new jpa wage table head get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaWtHeadGetMemento(QwtmtWagetableHead typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.WageTableHeadGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQwtmtWagetableHeadPK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.WageTableHeadGetMemento#getCode()
	 */
	@Override
	public WtCode getCode() {
		return new WtCode(this.typeValue.getQwtmtWagetableHeadPK().getWageTableCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.WageTableHeadGetMemento#getName()
	 */
	@Override
	public WtName getName() {
		return new WtName(this.typeValue.getWageTableName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.WageTableHeadGetMemento#getMemo()
	 */
	@Override
	public Memo getMemo() {
		return new Memo(this.typeValue.getMemo());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.WtHeadGetMemento#getMode()
	 */
	@Override
	public ElementCount getMode() {
		return ElementCount.valueOf(this.typeValue.getDemensionSet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.WtHeadGetMemento#getElements()
	 */
	@Override
	public List<WtElement> getElements() {
		return LazyList.withMap(() -> this.typeValue.getWagetableElementList(), (entity) -> {
			return new WtElement(new JpaWtElementGetMemento(entity));
		});
	}
}
