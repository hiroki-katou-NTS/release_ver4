/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.core.dom.base.simplehistory.Master;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class WageTableHead.
 */
@Getter
public class WtHead extends DomainObject implements Master {

	/** The company code. */
	private String companyCode;

	/** The code. */
	private WtCode code;

	/** The name. */
	@Setter
	private WtName name;

	/** The mode. */
	private ElementCount mode;

	/** The elements. */
	private List<WtElement> elements;

	/** The memo. */
	@Setter
	private Memo memo;

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new wage table head.
	 *
	 * @param memento
	 *            the memento
	 */
	public WtHead(WtHeadGetMemento memento) {
		this.companyCode = memento.getCompanyCode();
		this.code = memento.getCode();
		this.name = memento.getName();
		this.mode = memento.getMode();
		this.elements = memento.getElements();
		this.memo = memento.getMemo();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WtHeadSetMemento memento) {
		memento.setCompanyCode(this.companyCode);
		memento.setCode(this.code);
		memento.setName(this.name);
		memento.setMode(this.mode);
		memento.setElements(this.elements);
		memento.setMemo(this.memo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WtHead other = (WtHead) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		return true;
	}
}
