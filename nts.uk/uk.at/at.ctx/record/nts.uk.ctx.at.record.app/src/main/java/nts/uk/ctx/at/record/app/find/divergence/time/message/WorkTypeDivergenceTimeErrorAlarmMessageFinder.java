package nts.uk.ctx.at.record.app.find.divergence.time.message;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageRepository;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WorkTypeDivergenceTimeErrorAlarmMessageFinder.
 */
@Stateless
public class WorkTypeDivergenceTimeErrorAlarmMessageFinder {

	/** The repository. */
	@Inject
	private WorkTypeDivergenceTimeErrorAlarmMessageRepository repository;

	/**
	 * Find by work type div time err alarm msg.
	 *
	 * @param divergenceTimeNo
	 *            the divergence time no
	 * @param workTypeCode
	 *            the work type code
	 * @return the work type divergence time error alarm message dto
	 */
	public WorkTypeDivergenceTimeErrorAlarmMessageDto findByWorkTypeDivTimeErrAlarmMsg(Integer divergenceTimeNo,
			BusinessTypeCode workTypeCode) {
		CompanyId companyId = new CompanyId(AppContexts.user().companyId());
		Optional<WorkTypeDivergenceTimeErrorAlarmMessage> opt = this.repository.getByDivergenceTimeNo(divergenceTimeNo,
				companyId, workTypeCode);

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

			WorkTypeDivergenceTimeErrorAlarmMessageDto dto = new WorkTypeDivergenceTimeErrorAlarmMessageDto(
					opt.get().getCId(), opt.get().getWorkTypeCode().v(), opt.get().getDivergenceTimeNo(), alarmMessage,
					errorMessage);
			return dto;
		}
		return null;
	}
}
