package nts.uk.ctx.at.record.app.find.divergence.time;

import java.util.Collections;
//import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.deviationtime.deviationtimeframe.DivergenceTimeRoot;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceTimeSettingFinder.
 */
@Stateless
public class DivergenceTimeSettingFinder {

	/** The div time repo. */
	@Inject
	private DivergenceTimeRepository divTimeRepo;

	/**
	 * Gets the all div time.
	 *
	 * @return the all div time
	 */
	public List<DivergenceTimeDto> getAllDivTime() {
		// Get company id
		String companyId = AppContexts.user().companyId();

		// Get list divergence time
		List<DivergenceTimeRoot> listDivTime = this.divTimeRepo.getAllDivTime(companyId);

		// Check list empty
		if (listDivTime.isEmpty()) {
			return Collections.emptyList();
		}

		// Convert domain to dto
		return listDivTime.stream().map(e -> {
			DivergenceTimeDto dto = new DivergenceTimeDto();
			e.saveToMemento(dto);
			return dto;
		}).sorted((o1, o2) -> o1.getDivergenceTimeNo() >= o2.getDivergenceTimeNo() ? 1 : -1)
		.collect(Collectors.toList());
	}

}
