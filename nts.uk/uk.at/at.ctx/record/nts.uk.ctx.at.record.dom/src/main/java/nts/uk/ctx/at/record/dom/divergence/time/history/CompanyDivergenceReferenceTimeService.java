package nts.uk.ctx.at.record.dom.divergence.time.history;

import java.util.Date;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeErrorCancelMethod;
import nts.uk.ctx.at.record.dom.divergence.time.JudgmentResult;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReason;
import nts.uk.ctx.at.record.dom.divergencetime.DiverdenceReasonCode;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * The Interface CompanyDivergenceReferenceTimeService.
 */
public interface CompanyDivergenceReferenceTimeService {

	/**
	 * Check divergence time.
	 *
	 * @param userId the user id
	 * @param companyId the company id
	 * @param processDate the process date
	 * @param divergenceTimeNo the divergence time no
	 * @param DivergenceTimeOccurred the divergence time occurred
	 */
	//乖離時間をチェックする
	public JudgmentResultDetermineRefTime CheckDivergenceTime(String userId, GeneralDate processDate, int divergenceTimeNo, JudgmentResult checkCategory, AttendanceTime DivergenceTimeOccurred, DiverdenceReasonCode divReasonCode, DivergenceReason divReason, DivergenceTimeErrorCancelMethod divTimeErrotCancelMethod);
	//乖離時間をチェックする
	
	
}
