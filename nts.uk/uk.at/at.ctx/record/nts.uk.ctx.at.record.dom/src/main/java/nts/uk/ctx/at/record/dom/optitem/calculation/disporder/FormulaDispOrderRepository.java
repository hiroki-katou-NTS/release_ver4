/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optitem.calculation.disporder;

import java.util.List;

/**
 * The Interface FormulaDispOrderRepository.
 */
public interface FormulaDispOrderRepository {

	/**
	 * Creates the.
	 *
	 * @param listFormula the list formula
	 */
	void create(List<FormulaDispOrder> listFormula);

	/**
	 * Removes the.
	 *
	 * @param comId the com id
	 * @param optItemNo the opt item no
	 */
	void remove(String comId, Integer optItemNo);

	/**
	 * Find by opt item no.
	 *
	 * @param companyId the company id
	 * @param optItemNo the opt item no
	 * @return the list
	 */
	List<FormulaDispOrder> findByOptItemNo(String companyId, Integer optItemNo);
}
