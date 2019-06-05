/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.workplace.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;

/**
 * The Class WorkplaceInfo.
 */
@AllArgsConstructor
@Getter
//職場情報
public class WorkplaceInfo extends AggregateRoot {

	/** The company id. */
	//会社ID
	private String companyId;

	/** The history id. */
	//履歴ID
	private String historyId;

	/** The workplace id. */
	//職場ID
	private String workplaceId;

	/** The workplace code. */
	//職場コード
	private WkpCode workplaceCode;

	/** The workplace name. */
	//職場名称
	private WorkplaceName workplaceName;

	/** The wkp generic name. */
	//職場総称
	private WorkplaceGenericName wkpGenericName;

	/** The wkp display name. */
	//職場表示名
	private WorkplaceDisplayName wkpDisplayName;

	/** The outside wkp code. */
	//職場外部コード
	private OutsideWorkplaceCode outsideWkpCode;

	/**
	 * Instantiates a new workplace info.
	 */
	private WorkplaceInfo() {
	}
	
	/**
	 * Instantiates a new workplace info.
	 *
	 * @param memento the memento
	 */
	public WorkplaceInfo(WorkplaceInfoGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.historyId = memento.getHistoryId();
		this.workplaceId = memento.getWorkplaceId();
		this.workplaceCode = memento.getWorkplaceCode();
		this.workplaceName = memento.getWorkplaceName();
		this.wkpGenericName = memento.getWkpGenericName();
		this.wkpDisplayName = memento.getWkpDisplayName();
		this.outsideWkpCode = memento.getOutsideWkpCode();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WorkplaceInfoSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setHistoryId(this.historyId);
		memento.setWorkplaceId(this.workplaceId);
		memento.setWorkplaceCode(this.workplaceCode);
		memento.setWorkplaceName(this.workplaceName);
		memento.setWkpGenericName(this.wkpGenericName);
		memento.setWkpDisplayName(this.wkpDisplayName);
		memento.setOutsideWkpCode(this.outsideWkpCode);
	}

	/**
	 * Clone with hist id.
	 *
	 * @param newHistoryId the new history id
	 * @return the workplace info
	 */
	public WorkplaceInfo cloneWithHistId(String newHistoryId) {
	    WorkplaceInfo newEntity = new WorkplaceInfo();
	    newEntity.companyId = this.companyId;
	    newEntity.historyId = newHistoryId;
	    newEntity.workplaceId = this.workplaceId;
	    newEntity.workplaceCode = this.workplaceCode;
	    newEntity.workplaceName = this.workplaceName;
	    newEntity.wkpGenericName = this.wkpGenericName;
	    newEntity.wkpDisplayName = this.wkpDisplayName;
	    newEntity.outsideWkpCode = this.outsideWkpCode;
		return newEntity;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((historyId == null) ? 0 : historyId.hashCode());
		result = prime * result + ((workplaceId == null) ? 0 : workplaceId.hashCode());
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
		if (!(obj instanceof WorkplaceInfo))
			return false;
		WorkplaceInfo other = (WorkplaceInfo) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (historyId == null) {
			if (other.historyId != null)
				return false;
		} else if (!historyId.equals(other.historyId))
			return false;
		if (workplaceId == null) {
			if (other.workplaceId != null)
				return false;
		} else if (!workplaceId.equals(other.workplaceId))
			return false;
		return true;
	}
	
	
}
