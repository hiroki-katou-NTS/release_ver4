package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.Optional;

public interface DivergenceReferenceTimeUsageUnitRepository {

	/**
	 * Update.
	 *
	 * @param domain
	 *            the domain
	 */
	void update(DivergenceReferenceTimeUsageUnit domain);

	/**
	 * Find by id.
	 *
	 * @param companyId
	 *            the company id
	 * @return the divergence reference time usage unit
	 */
	Optional<DivergenceReferenceTimeUsageUnit> findByCompanyId(String companyId);
}
