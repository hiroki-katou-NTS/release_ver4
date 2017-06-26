/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

import java.util.List;

/**
 * The Interface WLOutputSettingSetMemento.
 */
public interface WLOutputSettingSetMemento {
	
	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	void setCode(WLOutputSettingCode code);
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	void setName(WLOutputSettingName name);
	
	/**
	 * Sets the category settings.
	 *
	 * @param categorySettings the new category settings
	 */
	void setCategorySettings(List<WLCategorySetting> categorySettings);
	
	/**
	 * Sets the once sheet per person.
	 *
	 * @param onceSheetPerPerson the new once sheet per person
	 */
	void setOnceSheetPerPerson(Boolean onceSheetPerPerson);
	
	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	void setCompanyCode(String companyCode);
}
