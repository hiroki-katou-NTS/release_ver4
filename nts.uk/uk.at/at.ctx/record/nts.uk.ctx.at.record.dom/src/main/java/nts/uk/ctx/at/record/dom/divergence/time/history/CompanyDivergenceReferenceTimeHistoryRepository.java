package nts.uk.ctx.at.record.dom.divergence.time.history;

import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Interface CompanyDivergenceReferenceTimeHistoryRepository.
 */
public interface CompanyDivergenceReferenceTimeHistoryRepository {

	/**
	 * Count by date period.
	 *
	 * @param companyId the company id
	 * @param datePeriod the date period
	 * @param histId the hist id
	 * @return the integer
	 */
	Integer countByDatePeriod(String companyId, DatePeriod datePeriod, String histId);
	
	/**
	 * Find latest hist.
	 *
	 * @param companyId the company id
	 * @return the company divergence reference time history
	 */
	CompanyDivergenceReferenceTimeHistory findLatestHist(String companyId);
	
	/**
	 * Find by hist id.
	 *
	 * @param histId the hist id
	 * @return the company divergence reference time history
	 */
	CompanyDivergenceReferenceTimeHistory findByHistId(String histId);
	
	/**
	 * Find all.
	 *
	 * @param companyId the company id
	 * @return the list
	 */
	CompanyDivergenceReferenceTimeHistory findAll(String companyId);
	
	/**
	 * Adds the.
	 *
	 * @param domain the domain
	 */
	void add(CompanyDivergenceReferenceTimeHistory domain);
	
	/**
	 * Update.
	 *
	 * @param domain the domain
	 */
	void update(CompanyDivergenceReferenceTimeHistory domain);
	
	/**
	 * Delete.
	 *
	 * @param domain the domain
	 */
	void delete(CompanyDivergenceReferenceTimeHistory domain);
}
