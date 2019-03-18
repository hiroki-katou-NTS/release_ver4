package nts.uk.ctx.at.shared.dom.yearholidaygrant;

import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * the length service repository interface
 * @author yennth
 *
 */
public interface LengthServiceRepository {
	/**
	 * 
	 * @param companyId
	 * @param conditionNo
	 * @param yearHolidayCode
	 * @param grantYearHolidayNo
	 * @return
	 */
	Optional<LengthServiceTbl> find(String companyId, String yearHolidayCode, int grantNum);
	
	/**
	 * Gets the year holiday grant by code.
	 *
	 * @param companyId the company id
	 * @param conditionNo the condition No
	 * @return the holiday grant by codes
	 */
	List<LengthServiceTbl> findByCode(String companyId, String yearHolidayCode);
	
	Map<String, List<LengthServiceTbl>> findByCode(String companyId, List<String> yearHolidayCode);
	
	/**
	 * Adds the holiday grant.
	 *
	 * @param holidayGrant the holiday grant
	 */
	void add(LengthServiceTbl holidayGrant);
	
	/**
	 * Update the holiday grant.
	 *
	 * @param holidayGrant the holiday grant
	 */
	void update(LengthServiceTbl holidayGrant);
	
	/**
	 * Removes the holiday grant.
	 *
	 * @param companyId the company id
	 * @param conditionNo the condition No
	 * @param yearHolidayCode the year holiday code
	 * @return the holiday grant by codes
	 */
//	void remove(String companyId, int grantNum, String yearHolidayCode);
	
	void remove(String companyId, String yearHolidayCode);
	
	void remove(String companyId);
	
	void remove(String companyId, String yearHolidayCode, List<Integer> grantNums);
}
