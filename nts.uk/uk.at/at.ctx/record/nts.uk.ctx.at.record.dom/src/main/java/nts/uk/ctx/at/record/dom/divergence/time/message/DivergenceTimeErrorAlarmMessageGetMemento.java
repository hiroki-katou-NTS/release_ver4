package nts.uk.ctx.at.record.dom.divergence.time.message;

import java.util.Optional;

import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Interface DivergenceTimeErrorAlarmMessageGetMemento.
 */
public interface DivergenceTimeErrorAlarmMessageGetMemento {
	
	/**
	 * Gets the c id.
	 *
	 * @return the c id
	 */
	public CompanyId getCId();
	
	/**
	 * Gets the divergence time no.
	 *
	 * @return the divergence time no
	 */
	public Integer getDivergenceTimeNo();
	
	/**
	 * Gets the alarm message.
	 *
	 * @return the alarm message
	 */
	public Optional<ErrorAlarmMessage> getAlarmMessage();
	
	/**
	 * Gets the error message.
	 *
	 * @return the error message
	 */
	public Optional<ErrorAlarmMessage> getErrorMessage();
}
