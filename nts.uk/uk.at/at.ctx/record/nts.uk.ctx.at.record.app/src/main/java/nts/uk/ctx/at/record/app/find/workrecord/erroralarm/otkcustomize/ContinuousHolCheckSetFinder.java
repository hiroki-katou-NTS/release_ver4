package nts.uk.ctx.at.record.app.find.workrecord.erroralarm.otkcustomize;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.workrecord.erroralarm.otkcustomize.ContinuousHolCheckSet;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.otkcustomize.repo.ContinuousHolCheckSetRepo;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author tuannt-nws
 *
 */
@Stateless
public class ContinuousHolCheckSetFinder {

	/** The continous hol check set repo. */
	@Inject
	private ContinuousHolCheckSetRepo continousHolCheckSetRepo;

	/** The work type reposity. */
	@Inject
	private WorkTypeRepository workTypeReposity;

	/**
	 * Find continous hol check set.
	 *
	 * @return the continous hol check set dto
	 */
	public ContinuousHolCheckSetDto findContinousHolCheckSet() {
		String companyId = AppContexts.user().companyId();
		Optional<ContinuousHolCheckSet> optionalDomain = this.continousHolCheckSetRepo.find(companyId);
		if (optionalDomain.isPresent()) {
			ContinuousHolCheckSet domain = optionalDomain.get();
			return new ContinuousHolCheckSetDto(
					domain.getTargetWorkType().stream().map(e -> e.v()).collect(Collectors.toList()),
					domain.getIgnoreWorkType().stream().map(e -> e.v()).collect(Collectors.toList()), domain.isUseAtr(),
					domain.getDisplayMessege().v(), domain.getMaxContinuousDays().v());
		}
		return new ContinuousHolCheckSetDto();
	}

	/**
	 * Find all work type.
	 *
	 * @return the list
	 */
	public List<OtkWorkTypeDto> findAllWorkType() {
		return this.workTypeReposity.findByCompanyId(AppContexts.user().companyId()).stream()
				.map(e -> new OtkWorkTypeDto(e.getWorkTypeCode().v(), e.getName().v(), e.getDeprecate().value)).collect(Collectors.toList());
	}

}
