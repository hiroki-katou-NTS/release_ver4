/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.subst;

import java.util.List;
import java.util.Optional;

/**
 * The Interface EmpSubstVacationRepository.
 */
public interface EmpSubstVacationRepository {

	/**
	 * Adds the.
	 *
	 * @param setting the setting
	 */
    void add(EmpSubstVacation setting);

	/**
	 * Update.
	 *
	 * @param setting the setting
	 */
    void update(EmpSubstVacation setting);

	/**
	 * Removes the.
	 *
	 * @param companyId the company id
	 */
    void remove(String companyId);

	/**
	 * Find all.
	 *
	 * @param companyId the company id
	 * @return the list
	 */
	List<EmpSubstVacation> findAll(String companyId);

	/**
	 * Find by id.
	 *
	 * @param companyId the company id
	 * @return the optional
	 */
	Optional<EmpSubstVacation> findById(String companyId);

}
