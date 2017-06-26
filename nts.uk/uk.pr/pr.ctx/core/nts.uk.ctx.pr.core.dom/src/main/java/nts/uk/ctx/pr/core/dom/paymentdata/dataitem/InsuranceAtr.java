package nts.uk.ctx.pr.core.dom.paymentdata.dataitem;

import lombok.AllArgsConstructor;

/**
 * 保対象区分
 * 
 * @author vunv
 *
 */
@AllArgsConstructor
public enum InsuranceAtr {
	/**
	 * 0:対象外
	 */
	UN_SUBJECT(0),

	/**
	 * 1:対象
	 */
	SUBJECT(1);

	public int value;

}
