/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;

/**
 * The Class PensionAvgearnValue.
 */
@Getter
@Setter
public class PensionAvgearnValue {

	/** The male amount. */
	private CommonAmount maleAmount;

	/** The female amount. */
	private CommonAmount femaleAmount;

	/** The unknown amount. */
	private CommonAmount unknownAmount;

	/**
	 * Instantiates a new pension avgearn value.
	 */
	public PensionAvgearnValue() {
		super();
	}

	/**
	 * Instantiates a new pension avgearn value.
	 *
	 * @param maleAmount
	 *            the male amount
	 * @param femaleAmount
	 *            the female amount
	 * @param unknownAmount
	 *            the unknown amount
	 */
	public PensionAvgearnValue(CommonAmount maleAmount, CommonAmount femaleAmount, CommonAmount unknownAmount) {
		super();

		// Validate required item
		if (maleAmount == null || femaleAmount == null || unknownAmount == null) {
			throw new BusinessException("ER001");
		}

		this.maleAmount = maleAmount;
		this.femaleAmount = femaleAmount;
		this.unknownAmount = unknownAmount;
	}

}
