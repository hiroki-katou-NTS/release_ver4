package nts.uk.ctx.at.request.dom.settting.worktype.history.internal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.uk.ctx.at.request.dom.settting.worktype.history.PlanVacationHistory;
import nts.uk.ctx.at.request.dom.settting.worktype.history.VacationHistoryPolicy;
import nts.uk.ctx.at.request.dom.settting.worktype.history.VacationHistoryRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class VacationHistoryPolicyImpl.
 */
@Stateless
public class VacationHistoryPolicyImpl implements VacationHistoryPolicy {

	/** The history repository. */
	@Inject
	private VacationHistoryRepository historyRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.request.dom.settting.worktype.history.VacationHistoryPolicy
	 * #validate(nts.arc.error.BundledBusinessException, java.lang.Boolean,
	 * nts.uk.ctx.at.request.dom.settting.worktype.history.PlanVacationHistory)
	 */
	@Override
	public void validate(Boolean isCreated, PlanVacationHistory vacationHistory) {

		DatePeriod period = new DatePeriod(vacationHistory.start(), vacationHistory.end());
		Integer count = this.historyRepository.countByDatePeriod(vacationHistory.getCompanyId(),
				vacationHistory.getWorkTypeCode(), period, vacationHistory.identifier());
		// Validate Msg_106
		if (count.intValue() > 0) {
			throw new BusinessException("Msg_106");
		}

		//Validate Msg_976
		if (isCreated) {
			if (this.historyRepository
					.findByWorkTypeCode(vacationHistory.getCompanyId(), vacationHistory.getWorkTypeCode())
					.size() >= 19) {
				throw new BusinessException("Msg_976");
			}
		}
	}
}
