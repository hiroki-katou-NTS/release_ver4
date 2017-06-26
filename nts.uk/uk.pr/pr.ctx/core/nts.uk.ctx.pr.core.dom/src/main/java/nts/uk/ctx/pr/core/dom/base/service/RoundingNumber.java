package nts.uk.ctx.pr.core.dom.base.service;

import java.math.BigDecimal;

import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;

public abstract class RoundingNumber {

	 /**
 	 * Health rounding.
 	 *
 	 * @param roudingMethod the rouding method
 	 * @param roundValue the round value
 	 * @param roundType the round type
 	 * @return the big decimal
 	 */
	public abstract BigDecimal healthRounding(RoundingMethod roudingMethod, BigDecimal roundValue, double roundType);
	
	 /**
 	 * Pension rounding.
 	 *
 	 * @param roudingMethod the rouding method
 	 * @param roundValue the round value
 	 * @return the big decimal
 	 */
	public abstract BigDecimal pensionRounding(RoundingMethod roudingMethod, BigDecimal roundValue);
}
