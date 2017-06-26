package nts.uk.ctx.sys.portal.dom.placement.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author LamDT
 */
@StringMaxLength(2000)
public class Url extends StringPrimitiveValue<Url> {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public Url(String rawValue) {
		super(rawValue);
	}
}
