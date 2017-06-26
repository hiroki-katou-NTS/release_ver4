package nts.uk.ctx.pr.core.dom.paymentdata.dataitem;

import lombok.AllArgsConstructor;

/**
 * 修正フラグ
 * 
 * @author vunv
 *
 */
@AllArgsConstructor
public enum CorrectFlag {
	/**
	 * 0:修正なし
	 */
	NO_MODIFY(0),

	/**
	 * 1:修正あり
	 */
	MODIFY(1);

	public int value;
}
