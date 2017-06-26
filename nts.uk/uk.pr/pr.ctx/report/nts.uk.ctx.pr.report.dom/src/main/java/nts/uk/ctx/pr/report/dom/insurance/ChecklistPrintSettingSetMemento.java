/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.insurance;

/**
 * The Interface ChecklistPrintSettingSetMemento.
 */
public interface ChecklistPrintSettingSetMemento {

	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	void setCompanyCode(String companyCode);

	/**
	 * Sets the show category insurance item.
	 *
	 * @param showCategoryInsuranceItem the new show category insurance item
	 */
	void   setShowCategoryInsuranceItem(Boolean showCategoryInsuranceItem);

	/**
	 * Sets the show delivery notice amount.
	 *
	 * @param showDeliveryNoticeAmount the new show delivery notice amount
	 */
	void setShowDeliveryNoticeAmount(Boolean showDeliveryNoticeAmount);

	/**
	 * Sets the show detail.
	 *
	 * @param showDetail the new show detail
	 */
	void setShowDetail(Boolean showDetail);

	/**
	 * Sets the show office.
	 *
	 * @param showOffice the new show office
	 */
	void setShowOffice(Boolean showOffice);

	/**
	 * Sets the show total.
	 *
	 * @param showTotal the new show total
	 */
	void setShowTotal(Boolean showTotal);
}
