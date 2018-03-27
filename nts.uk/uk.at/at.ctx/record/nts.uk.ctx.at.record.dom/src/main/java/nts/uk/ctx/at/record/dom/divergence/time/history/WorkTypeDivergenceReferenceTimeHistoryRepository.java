package nts.uk.ctx.at.record.dom.divergence.time.history;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Interface WorkTypeDivergenceReferenceTimeHistoryRepository.
 */
public interface WorkTypeDivergenceReferenceTimeHistoryRepository {

	/**
	 * Find by key.
	 *
	 * @param histId
	 *            the hist id
	 * @return the work type divergence reference time history
	 */
	WorkTypeDivergenceReferenceTimeHistory findByKey(String histId);

	/**
	 * Count by date period.
	 *
	 * @param companyId
	 *            the company id
	 * @param workTypeCode
	 *            the work type code
	 * @param datePeriod
	 *            the date period
	 * @param histId
	 *            the hist id
	 * @return the integer
	 */
	public Integer countByDatePeriod(String companyId, BusinessTypeCode workTypeCode, DatePeriod datePeriod,
			String histId);

	/**
	 * Find all.
	 *
	 * @param companyId
	 *            the company id
	 * @param workTypeCode
	 *            the work type code
	 * @return the work type divergence reference time history
	 */
	WorkTypeDivergenceReferenceTimeHistory findAll(String companyId, BusinessTypeCode workTypeCode);

	/**
	 * Adds the.
	 *
	 * @param domain
	 *            the domain
	 */
	void add(WorkTypeDivergenceReferenceTimeHistory domain);

	/**
	 * Update.
	 *
	 * @param domain
	 *            the domain
	 */
	void update(WorkTypeDivergenceReferenceTimeHistory domain);

	/**
	 * Delete.
	 *
	 * @param domain
	 *            the domain
	 */
	void delete(WorkTypeDivergenceReferenceTimeHistory domain);

	/**
	 * Find latest hist.
	 *
	 * @param companyId
	 *            the company id
	 * @param workTypeCode
	 *            the work type code
	 * @return the work type divergence reference time history
	 */
	WorkTypeDivergenceReferenceTimeHistory findLatestHist(String companyId, BusinessTypeCode workTypeCode);
}
