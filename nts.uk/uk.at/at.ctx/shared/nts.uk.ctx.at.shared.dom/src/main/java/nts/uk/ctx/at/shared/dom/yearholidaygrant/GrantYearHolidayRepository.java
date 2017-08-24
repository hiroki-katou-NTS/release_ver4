package nts.uk.ctx.at.shared.dom.yearholidaygrant;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author TanLV
 *
 */
public interface GrantYearHolidayRepository {
	
	/**
	 * 
	 * @param companyId
	 * @param conditionNo
	 * @param yearHolidayCode
	 * @param grantYearHolidayNo
	 * @return
	 */
	Optional<GrantHdTbl> find(String companyId, int conditionNo, String yearHolidayCode, int grantYearHolidayNo);
	
	/**
	 * Gets the year holiday grant by code.
	 *
	 * @param companyId the company id
	 * @param conditionNo the condition No
	 * @return the holiday grant by codes
	 */
	List<GrantHdTbl> findByCode(String companyId, int conditionNo, String yearHolidayCode);
	
	/**
	 * Adds the holiday grant.
	 *
	 * @param holidayGrant the holiday grant
	 */
	void add(GrantHdTbl holidayGrant);
	
	/**
	 * Update the holiday grant.
	 *
	 * @param holidayGrant the holiday grant
	 */
	void update(GrantHdTbl holidayGrant);
	
	/**
	 * Removes the holiday grant.
	 *
	 * @param companyId the company id
	 * @param conditionNo the condition No
	 * @param yearHolidayCode the year holiday code
	 * @return the holiday grant by codes
	 */
	void remove(String companyId, int grantYearHolidayNo, int conditionNo, String yearHolidayCode);
	
}
