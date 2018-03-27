package nts.uk.ctx.at.record.dom.divergence.time.message;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Interface WorkTypeDivergenceTimeErrorAlarmMessageRepository.
 */
public interface WorkTypeDivergenceTimeErrorAlarmMessageRepository {

	/**
	 * Gets the by divergence time no.
	 *
	 * @param divergenceTimeNo the divergence time no
	 * @param cId the c id
	 * @param workTypeCode the work type code
	 * @return the by divergence time no
	 */
	public Optional<WorkTypeDivergenceTimeErrorAlarmMessage> getByDivergenceTimeNo(Integer divergenceTimeNo, CompanyId cId,
			BusinessTypeCode workTypeCode);

	/**
	 * Adds the.
	 *
	 * @param message
	 *            the message
	 */
	public void add(WorkTypeDivergenceTimeErrorAlarmMessage message);

	/**
	 * Update.
	 *
	 * @param message
	 *            the message
	 */
	public void update(WorkTypeDivergenceTimeErrorAlarmMessage message);
}
