package nts.uk.ctx.at.shared.dom.specialholiday;

import java.util.List;


public interface SpecialHolidayRepository {

	/**
	 * Find by Company Id
	 * @param companyId
	 * @return
	 */
	List<SpecialHoliday> findByCompanyId(String companyId);

	/**
	 * Delete Special Holiday
	 * @param companyId
	 * @param specialHolidayCode
	 */
	void delete(String companyId, int specialHolidayCode);

	/**
	 * Add Special Holiday
	 * @param specialHoliday
	 */
	void add(SpecialHoliday specialHoliday);

	/**
	 * Update Special Holiday
	 * @param specialHoliday
	 */
	void update(SpecialHoliday specialHoliday);
	
	
	boolean checkExists(String companyCode, int specialHolidayCode);
}
