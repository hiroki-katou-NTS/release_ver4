/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.element;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;

/**
 * The Class WageTableElement.
 */
@Getter
@EqualsAndHashCode(of = { "demensionNo" })
public class WtElement {

	/** The demension no. */
	private DemensionNo demensionNo;

	/** The type. */
	private ElementType type;

	/** The reference code. */
	private String referenceCode;

	/**
	 * Instantiates a new wt element.
	 *
	 * @param demensionNo the demension no
	 * @param type the type
	 */
	public WtElement(DemensionNo demensionNo, ElementType type) {
		super();
		this.demensionNo = demensionNo;
		this.type = type;
	}

	/**
	 * Instantiates a new wt element.
	 *
	 * @param demensionNo the demension no
	 * @param type the type
	 * @param referenceCode the reference code
	 */
	public WtElement(DemensionNo demensionNo, ElementType type, String referenceCode) {
		super();
		this.demensionNo = demensionNo;
		this.type = type;
		this.referenceCode = referenceCode;
	}

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new wage table element.
	 *
	 * @param memento
	 *            the memento
	 */
	public WtElement(WtElementGetMemento memento) {
		this.demensionNo = memento.getDemensionNo();
		this.type = memento.getElementType();
		this.referenceCode = memento.getRefCode();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WtElementSetMemento memento) {
		memento.setDemensionNo(this.demensionNo);
		memento.setElementType(this.type);
		memento.setElementRefCode(this.referenceCode);
	}
}
