package nts.uk.ctx.at.record.dom.divergence.time.setting;

import java.util.List;

import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceType;

/**
 * The Interface DivergenceTimeGetMemento.
 */
public interface DivergenceTimeGetMemento {

	/**
	 * Gets the divergence time no.
	 *
	 * @return the divergence type
	 */
	int getDivergenceTimeNo();

	/**
	 * Gets the Company id.
	 *
	 * @return the Company id.
	 */
	String getCompanyId();

	/**
	 * Gets the divergence time Usage Set.
	 *
	 * @return the divergence time Usage Set
	 */
	DivergenceTimeUseSet getDivTimeUseSet();

	/**
	 * Gets divergence time name.
	 *
	 * @return divergence time name
	 */
	DivergenceTimeName getDivTimeName();

	/**
	 * Gets the divergence type.
	 *
	 * @return the divergence type
	 */
	DivergenceType getDivType();

	/**
	 * Gets the error cancel method.
	 *
	 * @return the error cancel method
	 */
	DivergenceTimeErrorCancelMethod getErrorCancelMedthod();

	/**
	 * Gets target items.
	 *
	 * @return the target items
	 */
	List<Double> getTargetItems();

}
