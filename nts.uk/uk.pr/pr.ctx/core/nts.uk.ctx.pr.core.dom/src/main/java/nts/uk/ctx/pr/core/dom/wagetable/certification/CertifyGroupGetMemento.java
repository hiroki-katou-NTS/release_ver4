/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.certification;

import java.util.Set;

/**
 * The Interface CertifyGroupGetMemento.
 */
public interface CertifyGroupGetMemento {

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	String getCompanyCode();

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	CertifyGroupCode getCode();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	CertifyGroupName getName();

	/**
	 * Gets the multi apply set.
	 *
	 * @return the multi apply set
	 */
	MultipleTargetSetting getMultiApplySet();

	/**
	 * Gets the certifies.
	 *
	 * @return the certifies
	 */
	Set<Certification> getCertifies();

}
