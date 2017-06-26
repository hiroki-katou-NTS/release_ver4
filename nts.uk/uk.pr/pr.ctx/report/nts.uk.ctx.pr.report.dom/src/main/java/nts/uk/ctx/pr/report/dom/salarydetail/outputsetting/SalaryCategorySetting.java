/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryCategory;

/**
 * The Class SalaryCategorySetting.
 */
@Getter
public class SalaryCategorySetting extends DomainObject {

	/** The category. */
	private SalaryCategory category;

	/** The items. */
	private List<SalaryOutputItem> items;

	/**
	 * Instantiates a new salary category setting.
	 *
	 * @param memento the memento
	 */
	public SalaryCategorySetting(SalaryCategorySettingGetMemento memento) {
		super();
		this.category = memento.getSalaryCategory();
		this.items = memento.getSalaryOutputItems();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(SalaryCategorySettingSetMemento memento) {
		memento.setSalaryOutputItems(this.items);
		memento.setSalaryCategory(this.category);
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
		result = prime * result + ((category == null) ? 0 : category.hashCode());
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
		SalaryCategorySetting other = (SalaryCategorySetting) obj;
		if (category != other.category)
			return false;
		return true;
	}
}
