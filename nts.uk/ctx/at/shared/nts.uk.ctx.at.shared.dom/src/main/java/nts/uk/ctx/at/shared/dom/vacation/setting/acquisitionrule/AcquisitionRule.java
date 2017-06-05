/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.acquisitionrule;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;

@Getter
public class AcquisitionRule extends DomainObject {

	/** The company id. */
	private String companyId;

	/** The setting classification. */
	@Setter
	private int category;

	/** The acquisition order. */
	@Setter
	private List<AcquisitionOrder> acquisitionOrder;

	/**
	 * Instantiates a new vacation acquisition rule.
	 *
	 * @param memento
	 *            the memento
	 */
	public AcquisitionRule(AcquisitionRuleGetMemento memento) {
		super();
		this.companyId = memento.getCompanyId();
		this.category = memento.getCategory();
		this.acquisitionOrder = memento.getAcquisitionOrder();

	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(AcquisitionRuleSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setCategory(this.category);
		memento.setAcquisitionOrder(this.acquisitionOrder);

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
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
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
		AcquisitionRule other = (AcquisitionRule) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		return true;
	}

}
