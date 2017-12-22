/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.holidaysetting.workplace;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.PublicHolidayMonthSetting;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year;


/**
 * The Class WorkplaceMonthDaySetting.
 */
// 職場月間日数設定
@Getter
@Setter
public class WorkplaceMonthDaySetting extends AggregateRoot{
	
	/** The companyId. */
	// 会社ID
	private CompanyId companyId;
	
	/** The management year. */
	// 管理年度
	private Year managementYear;
	
	/** The workplace ID. */
	// 職場ID
	private String workplaceId;
	
	/** The public holiday month settings. */
	// 月間公休日数
	private List<PublicHolidayMonthSetting> publicHolidayMonthSettings;
	
	/**
	 * Instantiates a new workplace month day setting.
	 *
	 * @param memento the memento
	 */
	public WorkplaceMonthDaySetting(WorkplaceMonthDaySettingGetMemento memento){
		this.companyId = memento.getCompanyId();
		this.managementYear = memento.getManagementYear();
		this.workplaceId = memento.getWorkplaceID();
		this.publicHolidayMonthSettings = memento.getPublicHolidayMonthSettings();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WorkplaceMonthDaySettingSetMemento memento) {
		memento.setCompanyId(this.companyId);
		memento.setManagementYear(this.managementYear);
		memento.setWorkplaceID(this.workplaceId);
		memento.setPublicHolidayMonthSettings(this.publicHolidayMonthSettings);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((managementYear == null) ? 0 : managementYear.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		WorkplaceMonthDaySetting other = (WorkplaceMonthDaySetting) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (managementYear == null) {
			if (other.managementYear != null)
				return false;
		} else if (!managementYear.equals(other.managementYear))
			return false;
		if (workplaceId == null) {
			if (other.workplaceId != null)
				return false;
		} else if (!workplaceId.equals(other.workplaceId))
			return false;
		return true;
	}
}
