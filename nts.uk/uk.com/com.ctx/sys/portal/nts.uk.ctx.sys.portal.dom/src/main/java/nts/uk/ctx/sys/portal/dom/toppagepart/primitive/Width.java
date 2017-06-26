package nts.uk.ctx.sys.portal.dom.toppagepart.primitive;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * @author LamDT
 */
@IntegerRange(min = 1, max = 99)
public class Width extends IntegerPrimitiveValue<Width> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public Width(Integer rawValue) {
		super(rawValue);
	}
	
}
