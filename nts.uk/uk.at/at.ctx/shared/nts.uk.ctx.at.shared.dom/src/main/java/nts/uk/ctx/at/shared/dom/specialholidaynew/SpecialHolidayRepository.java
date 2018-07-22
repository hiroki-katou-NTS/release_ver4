package nts.uk.ctx.at.shared.dom.specialholidaynew;

import java.util.List;
import java.util.Optional;

/**
 * Special Holiday Repository
 * 
 * @author tanlv
 *
 */
public interface SpecialHolidayRepository {
	/**
	 * Find By CompanyId
	 * @param companyId
	 * @return
	 */
	List<SpecialHoliday> findByCompanyId(String companyId);
	
	/**
	 * ドメインモデル「特別休暇」を取得する
	 * @param companyId
	 * @param specialHolidayCode
	 * @return
	 */
	Optional<SpecialHoliday> findByCode(String companyId, int specialHolidayCode);
	
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
	
	/**
	 * Delete Special Holiday
	 * @param companyId
	 * @param specialHolidayCode
	 */
	void delete(String companyId, int specialHolidayCode);
	
	Optional<SpecialHoliday> findBySingleCD(String companyID, int specialHolidayCD);
	/**
	 * 特別休暇枠NOから特別休暇を取得する
	 * @param cid
	 * @param absFrameNo 特別休暇枠NO
	 * @return
	 */
	Optional<Integer> findByAbsframeNo(String cid, int absFrameNo);
}
