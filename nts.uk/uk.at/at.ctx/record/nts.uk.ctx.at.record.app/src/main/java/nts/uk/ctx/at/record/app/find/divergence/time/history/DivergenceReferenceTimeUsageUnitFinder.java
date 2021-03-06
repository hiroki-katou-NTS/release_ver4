package nts.uk.ctx.at.record.app.find.divergence.time.history;

import java.math.BigDecimal;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeUsageUnit;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeUsageUnitRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceReferenceTimeUsageUnitFinder.
 */
@Stateless
public class DivergenceReferenceTimeUsageUnitFinder {

	/** The divergen reference time usage unit repo. */
	@Inject
	private DivergenceReferenceTimeUsageUnitRepository divergenReferenceTimeUsageUnitRepo;

	/**
	 * Find by company id.
	 *
	 * @return the divergence reference time usage unit dto
	 */
	public DivergenceReferenceTimeUsageUnitDto findByCompanyId() {
		String companyId = AppContexts.user().companyId();
		Optional<DivergenceReferenceTimeUsageUnit> opt = divergenReferenceTimeUsageUnitRepo.findByCompanyId(companyId);
		DivergenceReferenceTimeUsageUnitDto dto;
		if (opt.isPresent()) {
			dto = new DivergenceReferenceTimeUsageUnitDto(opt.get().getCId(),
					opt.get().isWorkTypeUseSet() ? BigDecimal.ONE : BigDecimal.ZERO);
		} else {
			// set default value
			dto = new DivergenceReferenceTimeUsageUnitDto(companyId, BigDecimal.ZERO);
		}

		return dto;
	}
}
