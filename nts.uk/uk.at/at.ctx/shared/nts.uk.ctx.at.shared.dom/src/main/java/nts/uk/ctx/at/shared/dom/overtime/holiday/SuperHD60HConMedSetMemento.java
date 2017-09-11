/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.overtime.holiday;

import java.util.List;

import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.overtime.premium.extra.PremiumExtra60HRate;

/**
 * The Interface SuperHD60HConMedSetMemento.
 */
public interface SuperHD60HConMedSetMemento {
	
	/**
	 * Sets the company id.
	 *
	 * @param companyId the new company id
	 */
	public void setCompanyId(CompanyId companyId);
	
	
	/**
	 * Sets the time rounding setting.
	 *
	 * @param timeRoundingSetting the new time rounding setting
	 */
	public void setTimeRoundingSetting(TimeRoundingSetting timeRoundingSetting);
	
	
	/**
	 * Sets the super holiday occurrence unit.
	 *
	 * @param superHolidayOccurrenceUnit the new super holiday occurrence unit
	 */
	public void setSuperHolidayOccurrenceUnit(SuperHDOccUnit superHolidayOccurrenceUnit);
	
	
	/**
	 * Sets the premium extra 60 H rates.
	 *
	 * @param premiumExtra60HRates the new premium extra 60 H rates
	 */
	public void setPremiumExtra60HRates(List<PremiumExtra60HRate> premiumExtra60HRates);

}
