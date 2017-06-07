/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.vacation.setting.retentionyearly.find.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.RetentionYearlySettingSetMemento;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.UpperLimitSetting;

/**
 * Gets the upper limit setting.
 *
 * @return the upper limit setting
 */
@Getter
@Setter
public class RetentionYearlyFindDto implements RetentionYearlySettingSetMemento{
	
	/** The company id. */
	private String companyId;
	
	/** The upper limit setting. */
	private UpperLimitSettingFindDto upperLimitSetting;
	
	/** The leave as work days. */
	private Boolean leaveAsWorkDays;

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.
	 * RetentionYearlySettingSetMemento#setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.
	 * RetentionYearlySettingSetMemento#setUpperLimitSetting(nts.uk.ctx.at.
	 * shared.dom.vacation.setting.retentionyearly.UpperLimitSetting)
	 */
	@Override
	public void setUpperLimitSetting(UpperLimitSetting upperLimitSetting) {
		this.upperLimitSetting = new UpperLimitSettingFindDto();
		this.upperLimitSetting.setMaxDaysCumulation(upperLimitSetting.getMaxDaysCumulation().v());
		this.upperLimitSetting.setRetentionYearsAmount(upperLimitSetting.getRetentionYearsAmount().v());
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.
	 * RetentionYearlySettingSetMemento#
	 * setcanAddToCumulationYearlyAsNormalWorkDay(java.lang.Boolean)
	 */
	@Override
	public void setLeaveAsWorkDays(Boolean leaveAsWorkDays) {
		this.leaveAsWorkDays = leaveAsWorkDays;
	}

}
