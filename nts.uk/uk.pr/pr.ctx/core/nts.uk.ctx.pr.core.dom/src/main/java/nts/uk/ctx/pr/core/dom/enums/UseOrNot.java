package nts.uk.ctx.pr.core.dom.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UseOrNot {
	/**0:利用しない	 */
	DO_NOT_USE(0),
	/**1:利用する	 */
	USE(1);
	/**
     * value.
     */
    public final int value;    
}
