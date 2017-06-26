package nts.uk.ctx.pr.core.dom.personalinfo.commute;

import java.util.List;
import java.util.Optional;

public interface PersonalCommuteFeeRepository {
	/**
	 * 
	 * @param companyCode
	 * @param personId
	 * @param baseYM
	 * @return list personal commutes
	 */
	List<PersonalCommuteFee> findAll(String companyCode, List<String> personIdList, int baseYM);
	
	/**
	 * FInd all commute of personal
	 * @param companyCode
	 * @param personId
	 * @param baseYearMonth
	 * @return
	 */
	List<PersonalCommuteFee> findAll(String companyCode, String personId, int baseYearMonth);
	
	/**
	 * Get personal commute
	 * 
	 * @param companyCode
	 * @param personId
	 * @param startYearMonth
	 * @return
	 */
	Optional<PersonalCommuteFee> find(String companyCode, String personId, int startYearMonth);
}
