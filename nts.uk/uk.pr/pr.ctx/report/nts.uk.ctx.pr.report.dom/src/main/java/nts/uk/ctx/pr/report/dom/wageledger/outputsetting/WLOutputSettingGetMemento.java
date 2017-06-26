/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

import java.util.List;

/**
 * The Interface WLOutputSettingGetMemento.
 */
public interface WLOutputSettingGetMemento {
	
	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	WLOutputSettingCode getCode();
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	WLOutputSettingName getName();
	
	/**
	 * Gets the once sheet per person.
	 *
	 * @return the once sheet per person
	 */
	Boolean getOnceSheetPerPerson();
	
	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	String getCompanyCode();
	
	/**
	 * Gets the category settings.
	 *
	 * @return the category settings
	 */
	List<WLCategorySetting> getCategorySettings();

}
