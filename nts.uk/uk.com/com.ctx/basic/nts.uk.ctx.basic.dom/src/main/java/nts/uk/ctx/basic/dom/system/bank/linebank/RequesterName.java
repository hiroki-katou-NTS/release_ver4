package nts.uk.ctx.basic.dom.system.bank.linebank;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(40)
/**
 * 
 * @author sonnh1
 *
 */
public class RequesterName extends StringPrimitiveValue<RequesterName> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public RequesterName(String arg0) {
		super(arg0);
	}
}
