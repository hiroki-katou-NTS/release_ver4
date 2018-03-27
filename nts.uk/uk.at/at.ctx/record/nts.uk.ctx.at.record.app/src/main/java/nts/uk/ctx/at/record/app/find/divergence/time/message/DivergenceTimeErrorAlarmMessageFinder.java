package nts.uk.ctx.at.record.app.find.divergence.time.message;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessageRepository;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceTimeErrorAlarmMessageFinder.
 */
@Stateless
public class DivergenceTimeErrorAlarmMessageFinder {

	/** The repository. */
	@Inject
	private DivergenceTimeErrorAlarmMessageRepository repository;

	/**
	 * Find by divergence time no.
	 *
	 * @param divergenceTimeNo
	 *            the divergence time no
	 * @return the divergence time error alarm message dto
	 */
	public DivergenceTimeErrorAlarmMessageDto findByDivergenceTimeNo(Integer divergenceTimeNo) {
		CompanyId companyId = new CompanyId(AppContexts.user().companyId());
		Optional<DivergenceTimeErrorAlarmMessage> opt = repository.findByDivergenceTimeNo(companyId, divergenceTimeNo);
		if (opt.isPresent()) {
			String errorMessage = null;
			String alarmMessage = null;

			// check if alarm message exist
			if (opt.get().getAlarmMessage().isPresent()) {
				alarmMessage = opt.get().getAlarmMessage().get().v();
			}

			// check if error message exist
			if (opt.get().getErrorMessage().isPresent()) {
				errorMessage = opt.get().getErrorMessage().get().v();
			}

			DivergenceTimeErrorAlarmMessageDto dto = new DivergenceTimeErrorAlarmMessageDto(opt.get().getCId(),
					opt.get().getDivergenceTimeNo(), alarmMessage, errorMessage);
			return dto;
		}
		return null;
	}
}
