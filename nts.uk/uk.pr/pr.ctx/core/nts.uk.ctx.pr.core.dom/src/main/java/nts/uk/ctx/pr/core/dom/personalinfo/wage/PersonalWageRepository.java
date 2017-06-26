package nts.uk.ctx.pr.core.dom.personalinfo.wage;

import java.util.List;
import java.util.Optional;

public interface PersonalWageRepository {
	/**
	 * 
	 * @param companyCode
	 * @param personId
	 * @param baseYm
	 * @return list of personal wage
	 */
	List<PersonalWage> findAll(String companyCode, List<String> personIdList, int baseYm);
	
	/**
	 * Find all wage of 1 person.
	 * @param companyCode companyCode
	 * @param personId personId
	 * @param baseYearMonth baseYearMonth
	 * @return list of PersonalWage
	 */
	List<PersonalWage> findAll(String companyCode, String personId, int baseYearMonth);
	
	Optional<PersonalWage> find(String companyCode, String personId, int categoryAttribute, String wageCode, int startYearMonth);
	
}
