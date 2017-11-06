/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optitem.calculation;

import java.util.List;

/**
 * The Interface FormulaRepository.
 */
public interface FormulaRepository {

	/**
	 * Creates the.
	 *
	 * @param domains the domains
	 */
	void create(List<Formula> domains);

	/**
	 * Removes the.
	 *
	 * @param comId the com id
	 * @param optItemNo the opt item no
	 */
	void remove(String comId, String optItemNo);

	/**
	 * Find by opt item no.
	 *
	 * @param companyId the company id
	 * @param optItemNo the opt item no
	 * @return the list
	 */
	List<Formula> findByOptItemNo(String companyId, String optItemNo);
}
