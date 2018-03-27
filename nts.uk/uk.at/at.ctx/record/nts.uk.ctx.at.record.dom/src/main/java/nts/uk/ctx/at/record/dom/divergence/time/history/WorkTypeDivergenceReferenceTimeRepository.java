package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceType;

/**
 * The Interface WorkTypeDivergenceReferenceTimeRepository.
 */
public interface WorkTypeDivergenceReferenceTimeRepository {

	/**
	 * Find by key.
	 *
	 * @param histId
	 *            the hist id
	 * @param workTypeCode
	 *            the work type code
	 * @param divergenceTimeNo
	 *            the divergence time no
	 * @return the optional
	 */
	Optional<WorkTypeDivergenceReferenceTime> findByKey(String histId, BusinessTypeCode workTypeCode,
			DivergenceType divergenceTimeNo);

	/**
	 * Find all.
	 *
	 * @param histId
	 *            the hist id
	 * @param workTypeCode
	 *            the work type code
	 * @return the list
	 */
	List<WorkTypeDivergenceReferenceTime> findAll(String histId, BusinessTypeCode workTypeCode);

	/**
	 * Update.
	 *
	 * @param domain
	 *            the domain
	 */
	void update(List<WorkTypeDivergenceReferenceTime> listDomain);

	/**
	 * Delete.
	 *
	 * @param domain
	 *            the domain
	 */
	void delete(WorkTypeDivergenceReferenceTime domain);

	/**
	 * Adds the default data when create history.
	 *
	 * @param historyId
	 *            the history id
	 */
	void addDefaultDataWhenCreateHistory(String historyId);

	/**
	 * Copy data from latest history.
	 *
	 * @param targetHistId
	 *            the target hist id
	 * @param destHistId
	 *            the dest hist id
	 */
	void copyDataFromLatestHistory(String targetHistId, String destHistId);
}
