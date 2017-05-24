/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.compensleave;

/**
 * The Interface ComCompensLeaveGetMemento.
 */
public interface EmpCompensLeaveGetMemento {

	/**
	 * Gets the company id.
	 *
	 * @return the company id
	 */
	String getCompanyId();

	
	/**
	 * Gets the emp contract type code.
	 *
	 * @return the emp contract type code
	 */
	String getEmpContractTypeCode();
	
	/**
	 * Gets the setting.
	 *
	 * @return the setting
	 */
	CompensatoryLeaveSetting getSetting();

}
