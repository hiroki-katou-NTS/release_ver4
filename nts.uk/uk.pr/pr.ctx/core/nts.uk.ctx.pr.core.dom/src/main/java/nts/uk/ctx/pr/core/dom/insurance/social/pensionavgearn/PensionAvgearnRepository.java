/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn;

import java.util.List;
import java.util.Optional;

/**
 * The Interface PensionAvgearnRepository.
 */
public interface PensionAvgearnRepository {

	/**
	 * Update.
	 *
	 * @param pensionAvgearns the pension avgearns
	 * @param ccd the ccd
	 * @param officeCd the office cd
	 */
	void update(List<PensionAvgearn> pensionAvgearns, String ccd, String officeCd);

	/**
	 * Find.
	 *
	 * @param historyId the history id
	 * @return the list
	 */
	List<PensionAvgearn> findById(String historyId);

	/**
	 * Findby office code.
	 *
	 * @param companyCode the company code
	 * @param officeCodes the office codes
	 * @return the list
	 */
	List<PensionAvgearn> findbyOfficeCodes(String companyCode, List<String> officeCodes);

	/**
	 * Find.
	 *
	 * @param histId the hist id
	 * @param ccd the ccd
	 * @param officeCd the office cd
	 * @param pensionGrade the pension grade
	 * @return the optional
	 */
	Optional<PensionAvgearn> find(String histId, String ccd, String officeCd, Integer pensionGrade);

}
