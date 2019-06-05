/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.dom.jobtitle.sequence;

import java.util.List;
import java.util.Optional;

/**
 * The Interface SequenceMasterRepository.
 */
public interface SequenceMasterRepository {
	
	/**
	 * Adds the.
	 *
	 * @param domain the domain
	 */
	void add(SequenceMaster domain);
	
	/**
	 * Update.
	 *
	 * @param domain the domain
	 */
	void update(SequenceMaster domain);
	
	/**
	 * Removes the.
	 *
	 * @param companyId the company id
	 * @param sequenceCode the sequence code
	 */
	void remove(String companyId, String sequenceCode);
	
	/**
	 * Find by company id.
	 *
	 * @param companyId the company id
	 * @return the list
	 */
	List<SequenceMaster> findByCompanyId(String companyId);
	
	/**
	 * Find by sequence code.
	 *
	 * @param companyId the company id
	 * @param sequenceCode the sequence code
	 * @return the optional
	 */
	Optional<SequenceMaster> findBySequenceCode(String companyId, String sequenceCode);
	
	/**
	 * Find max order.
	 *
	 * @return the int
	 */
	int findMaxOrder();
	
	/**
	 * Update order.
	 */
	void updateOrder(List<SequenceMaster> listSequenceMaster);

	List<SequenceMaster> findAll(String companyId, String sequenceCode);
}
