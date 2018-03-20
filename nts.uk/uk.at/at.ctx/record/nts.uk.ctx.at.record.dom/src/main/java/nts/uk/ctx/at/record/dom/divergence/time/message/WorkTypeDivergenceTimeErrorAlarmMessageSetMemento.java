package nts.uk.ctx.at.record.dom.divergence.time.message;

import java.util.Optional;

import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * The Interface WorkTypeDivergenceTimeErrorAlarmMessageSetMemento.
 */
public interface WorkTypeDivergenceTimeErrorAlarmMessageSetMemento {
	
	/**
	 * Sets the c id.
	 *
	 * @param cId the new c id
	 */
	public void setCId(CompanyId cId);

	/**
	 * Sets the work type code.
	 *
	 * @param workTypeCode the new work type code
	 */
	public void setWorkTypeCode(WorkTypeCode workTypeCode);

	/**
	 * Sets the divergence time no.
	 *
	 * @param divergenceTimeNo the new divergence time no
	 */
	public void setDivergenceTimeNo(Integer divergenceTimeNo);

	/**
	 * Sets the alarm message.
	 *
	 * @param alarmMessage the new alarm message
	 */
	public void setAlarmMessage(Optional<ErrorAlarmMessage> alarmMessage);

	/**
	 * Sets the error message.
	 *
	 * @param errprMessage the new error message
	 */
	public void setErrorMessage(Optional<ErrorAlarmMessage> errprMessage);
}
