/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.certification;

import java.util.Set;

/**
 * The Interface CertifyGroupSetMemento.
 */
public interface CertifyGroupSetMemento {

	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	void setCompanyCode(String companyCode);

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	void setCode(CertifyGroupCode code);

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	void setName(CertifyGroupName name);

	/**
	 * Sets the multi apply set.
	 *
	 * @param multiApplySet the new multi apply set
	 */
	void setMultiApplySet(MultipleTargetSetting multiApplySet);

	/**
	 * Sets the certifies.
	 *
	 * @param certifies the new certifies
	 */
	void setCertifies(Set<Certification> certifies);

}
