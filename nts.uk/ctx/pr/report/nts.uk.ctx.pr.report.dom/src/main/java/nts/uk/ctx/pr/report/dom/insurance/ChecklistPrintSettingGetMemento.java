/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.insurance;

import nts.uk.ctx.pr.report.dom.company.CompanyCode;

public interface ChecklistPrintSettingGetMemento {

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	CompanyCode getCompanyCode();

	/**
	 * Gets the show category insurance item.
	 *
	 * @return the show category insurance item
	 */
	Boolean getShowCategoryInsuranceItem();

	/**
	 * Gets the show delivery notice amount.
	 *
	 * @return the show delivery notice amount
	 */
	Boolean getShowDeliveryNoticeAmount();

	/**
	 * Gets the show detail.
	 *
	 * @return the show detail
	 */
	Boolean getShowDetail();

	/**
	 * Gets the show office.
	 *
	 * @return the show office
	 */
	Boolean getShowOffice();
	
	/**
	 * Gets the show total.
	 *
	 * @return the show total
	 */
	Boolean getShowTotal();
}
