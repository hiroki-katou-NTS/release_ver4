package nts.uk.ctx.at.record.dom.divergence.time;

import java.util.List;

/**
 * The Interface DivergenceTimeRepository.
 */
public interface DivergenceTimeRepository {

	/**
	 * Gets the all div time.
	 *
	 * @param companyId the company id
	 * @return the all div time
	 */
	List<DivergenceTime> getAllDivTime(String companyId);

	/**
	 * Gets the div time info.
	 *
	 * @param companyId the company id
	 * @param divTimeNo the div time no
	 * @return the div time info
	 */
	DivergenceTime getDivTimeInfo(String companyId, int divTimeNo);

	/**
	 * Find attendance id.
	 *
	 * @param companyId the company id
	 * @param divTimeNo the div time no
	 * @return the list
	 */
	List<Integer> findAttendanceId(String companyId, int divTimeNo);

	/**
	 * Update.
	 *
	 * @param divTimeDomain the div time domain
	 */
	void update (DivergenceTime divTimeDomain);

}
