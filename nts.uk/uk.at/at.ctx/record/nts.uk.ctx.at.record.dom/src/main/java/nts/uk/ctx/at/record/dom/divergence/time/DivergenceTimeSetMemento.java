package nts.uk.ctx.at.record.dom.divergence.time;

import java.util.List;

/**
 * The Interface DivergenceTimeSetMemento.
 */
public interface DivergenceTimeSetMemento {

	/**
	 * Sets the new divergence time no.
	 *
	 * @param DivergenceTimeNo the new divergence time no
	 */
	void setDivergenceTimeNo(int DivergenceTimeNo);

	/**
	 * Sets the new Company id.
	 *
	 * @param CompanyId the new company id
	 */
	void setCompanyId(String CompanyId);

	/**
	 * Sets the new divergence time Usage Set.
	 *
	 * @param divTimeUset the new div time use set
	 */
	void setDivTimeUseSet(DivergenceTimeUseSet divTimeUset);

	/**
	 * Sets divergence time name.
	 *
	 * @param divTimeName the new div time name
	 */
	void setDivTimeName(DivergenceTimeName divTimeName);

	/**
	 * Sets the new divergence type.
	 *
	 * @param divType the new div type
	 */
	void setDivType(DivergenceType divType);

	/**
	 * Sets the new error cancel method.
	 *
	 * @param errorCancelMedthod the new error cancel medthod
	 */
	void setErrorCancelMedthod(DivergenceTimeErrorCancelMethod errorCancelMedthod);

	/**
	 * Sets tarset items.
	 *
	 * @param targetItems the new tarset items
	 */
	void setTarsetItems(List<Double> targetItems);
}
