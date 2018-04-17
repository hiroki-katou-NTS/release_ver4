/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.statutory.worktime.workplaceNew;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.StatutoryWorkTimeSet;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.WorkingTimeSetting;

/**
 * The Class WkpNormalWorkingHour.
 */
@Getter
// 職場別通常勤務労働時間
public class WkpRegularLaborTime extends AggregateRoot implements StatutoryWorkTimeSet {

	/** The company id. */
	// 会社ID
	private CompanyId companyId;

	/** The workplace id. */
	// 職場ID
	private WorkplaceId workplaceId;

	/** The working time setting new. */
	// 時間
	private WorkingTimeSetting workingTimeSet;

	/**
	 * Instantiates a new wkp normal working hour.
	 *
	 * @param memento
	 *            the memento
	 */
	public WkpRegularLaborTime(WkpRegularLaborTimeGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.workplaceId = memento.getWorkplaceId();
		this.workingTimeSet = memento.getWorkingTimeSet();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WkpRegularLaborTimeSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setWorkplaceId(this.workplaceId);
		memento.setWorkingTimeSet(this.workingTimeSet);
	}
	
}
