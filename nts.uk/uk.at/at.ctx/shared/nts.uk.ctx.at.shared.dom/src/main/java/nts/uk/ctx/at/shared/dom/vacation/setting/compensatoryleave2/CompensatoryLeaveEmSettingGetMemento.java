/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave2;

import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;

/**
 * The Interface CompensatoryLeaveEmSettingGetMemento.
 */
public interface CompensatoryLeaveEmSettingGetMemento {
	 
 	/**
 	 * Gets the company id.
 	 *
 	 * @return the company id
 	 */
 	String getCompanyId(); 
     
     /**
      * Gets the employment code.
      *
      * @return the employment code
      */
     EmploymentCode getEmploymentCode();
     
     /**
      * Gets the checks if is managed.
      *
      * @return the checks if is managed
      */
     ManageDistinct getIsManaged();

     /**
      * Gets the compensatory acquisition use.
      *
      * @return the compensatory acquisition use
      */
     CompensatoryAcquisitionUse getCompensatoryAcquisitionUse();
     
     /**
      * Gets the compensatory digestive time unit.
      *
      * @return the compensatory digestive time unit
      */
     CompensatoryDigestiveTimeUnit getCompensatoryDigestiveTimeUnit();
}
