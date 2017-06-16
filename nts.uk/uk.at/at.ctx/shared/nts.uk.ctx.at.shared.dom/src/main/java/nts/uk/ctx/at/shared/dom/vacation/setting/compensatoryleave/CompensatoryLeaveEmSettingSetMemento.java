/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave;

import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;

/**
 * The Interface CompensatoryLeaveEmSettingSetMemento.
 */
public interface CompensatoryLeaveEmSettingSetMemento {
	 
 	/**
 	 * Sets the company id.
 	 *
 	 * @param companyId the new company id
 	 */
 	void setCompanyId(String companyId);

     /**
      * Sets the employment code.
      *
      * @param employmentCode the new employment code
      */
     void setEmploymentCode(EmploymentCode employmentCode); 

     /**
      * Sets the checks if is managed.
      *
      * @param isManaged the new checks if is managed
      */
     void setIsManaged(ManageDistinct isManaged);

     /**
      * Sets the compensatory acquisition use.
      *
      * @param compensatoryAcquisitionUse the new compensatory acquisition use
      */
     void setCompensatoryAcquisitionUse(CompensatoryAcquisitionUse compensatoryAcquisitionUse); 
     
     /**
      * Sets the compensatory digestive time unit.
      *
      * @param compensatoryDigestiveTimeUnit the new compensatory digestive time unit
      */
     void setCompensatoryDigestiveTimeUnit(CompensatoryDigestiveTimeUnit compensatoryDigestiveTimeUnit);
}
