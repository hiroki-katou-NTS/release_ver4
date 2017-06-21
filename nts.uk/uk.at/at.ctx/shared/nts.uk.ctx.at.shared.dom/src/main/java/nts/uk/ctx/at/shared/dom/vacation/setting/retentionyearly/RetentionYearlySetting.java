/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;

/**
 * The Class RetentionYearlySetting.
 */

/**
 * Gets the leave as work days.
 *
 * @return the leave as work days
 */
@Getter
public class RetentionYearlySetting extends AggregateRoot {
	
	/** The company id. */
	private String companyId;
	
	/** The upper limit setting. */
	private UpperLimitSetting upperLimitSetting;
	
	/** The leave as work days. */
	private Boolean leaveAsWorkDays;
	
	/**
	 * Instantiates a new retention yearly setting.
	 *
	 * @param memento the memento
	 */
	public RetentionYearlySetting(RetentionYearlySettingGetMemento memento) {
		this.companyId = memento.getCompanyId();
		this.upperLimitSetting = memento.getUpperLimitSetting();
		this.leaveAsWorkDays = memento
				.getLeaveAsWorkDays();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(RetentionYearlySettingSetMemento memento){
		memento.setLeaveAsWorkDays(this.leaveAsWorkDays);
		memento.setCompanyId(this.companyId);
		memento.setUpperLimitSetting(this.upperLimitSetting);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
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
		RetentionYearlySetting other = (RetentionYearlySetting) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		return true;
	}
	
}
