/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;

/**
 * The Class LaborInsuranceOffice.
 */
@Getter
@Setter
public class UnemployeeInsuranceRateItemSetting {

	/** The company code. */
	private RoundingMethod roundAtr;

	/** The code. */
	private Double rate;

	/**
	 * Instantiates a new unemployee insurance rate item setting.
	 */
	public UnemployeeInsuranceRateItemSetting() {
		super();
	}

	/**
	 * Instantiates a new unemployee insurance rate item setting.
	 *
	 * @param roundAtr
	 *            the round atr
	 * @param rate
	 *            the rate
	 */
	public UnemployeeInsuranceRateItemSetting(RoundingMethod roundAtr, Double rate) {
		super();
		this.roundAtr = roundAtr;
		this.rate = rate;
	}

	/**
	 * Value intial.
	 *
	 * @return the unemployee insurance rate item setting
	 */
	public static final UnemployeeInsuranceRateItemSetting valueIntial() {
		return new UnemployeeInsuranceRateItemSetting(RoundingMethod.Truncation, Double.valueOf(0d));
	}
}
