/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.InsuranceAmount;

/**
 * The Class HealthInsuranceAvgearnValue.
 */
@Getter
@Setter
public class HealthInsuranceAvgearnValue {

	/** The health basic mny. */
	private InsuranceAmount healthBasicMny;

	/** The health general mny. */
	private CommonAmount healthGeneralMny;

	/** The health nursing mny. */
	private CommonAmount healthNursingMny;

	/** The health specific mny. */
	private InsuranceAmount healthSpecificMny;

	/**
	 * Instantiates a new health insurance avgearn value.
	 */
	public HealthInsuranceAvgearnValue() {
		super();
	}

	/**
	 * Instantiates a new health insurance avgearn value.
	 *
	 * @param healthBasicMny
	 *            the health basic mny
	 * @param healthGeneralMny
	 *            the health general mny
	 * @param healthNursingMny
	 *            the health nursing mny
	 * @param healthSpecificMny
	 *            the health specific mny
	 */
	public HealthInsuranceAvgearnValue(InsuranceAmount healthBasicMny, CommonAmount healthGeneralMny,
			CommonAmount healthNursingMny, InsuranceAmount healthSpecificMny) {
		super();
		this.healthBasicMny = healthBasicMny;
		this.healthGeneralMny = healthGeneralMny;
		this.healthNursingMny = healthNursingMny;
		this.healthSpecificMny = healthSpecificMny;
	}

}
