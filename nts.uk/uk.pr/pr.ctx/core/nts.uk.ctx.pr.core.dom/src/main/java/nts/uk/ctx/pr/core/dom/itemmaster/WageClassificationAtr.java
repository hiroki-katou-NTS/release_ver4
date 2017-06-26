package nts.uk.ctx.pr.core.dom.itemmaster;

import lombok.AllArgsConstructor;

/** 賃金対象区分 */
@AllArgsConstructor
public enum WageClassificationAtr {
	/**0:対象外 */
	UN_SUBJECT(0),
	/**1:対象 */
	SUBJECT(1);
	
	public final int value;
}
