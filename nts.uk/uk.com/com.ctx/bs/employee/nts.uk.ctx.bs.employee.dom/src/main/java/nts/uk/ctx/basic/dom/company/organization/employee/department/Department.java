/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.dom.company.organization.employee.department;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.bs.employee.dom.common.CompanyId;

/**
 * The Class Department. 部門
 */
@Getter
public class Department extends AggregateRoot {
	
	/** The company id. */
	/* 会社ID */
	private CompanyId companyId;
	
	/** The id. */
	/* 会社コード */
	private String id;
	
	/** The histories. */
	/* 部門履歴  */
	private DepartmentHistory depHistory;
	
	/**
	 * Instantiates a new department.
	 *
	 * @param memento the memento
	 */
	public Department(DepartmentGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.id = memento.getId();
		this.depHistory = memento.getDepHistory();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(DepartmentSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setId(this.id);
		memento.setDepHistory(this.depHistory);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		Department other = (Department) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
