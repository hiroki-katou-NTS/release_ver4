/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.statutory.worktime.workplaceNew;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.StatutoryWorkTimeSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.WorkingTimeSettingNew;

/**
 * The Class WkpDeforLaborWorkHour.
 */

@Getter
// 職場別変形労働労働時間
public class WkpDeforLaborWorkHour extends AggregateRoot implements StatutoryWorkTimeSetting {
	
	/** The company id. */
	private CompanyId companyId;
	
	/** The workplace id. */
	private WorkplaceId workplaceId;
	
	/** The working time setting new. */
	private WorkingTimeSettingNew workingTimeSettingNew;
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.StatutoryWorkTimeSetting#getWorkingTimeSettingNew()
	 */
	@Override
	public WorkingTimeSettingNew getWorkingTimeSettingNew() {
		return workingTimeSettingNew;
	}

	/**
	 * Instantiates a new wkp defor labor work hour.
	 *
	 * @param memento
	 *            the memento
	 */
	public WkpDeforLaborWorkHour(WkpDeforLaborWorkHour memento) {
		this.companyId = memento.getCompanyId();
		this.workplaceId = memento.getWorkplaceId();
		this.workingTimeSettingNew = memento.getWorkingTimeSettingNew();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WkpDeforLaborWorkHourSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setWorkplaceId(this.workplaceId);
		memento.setWorkingTimeSettingNew(this.workingTimeSettingNew);
	}	
}
