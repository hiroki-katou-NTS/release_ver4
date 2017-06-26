package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerMaxValue;
import nts.arc.primitive.constraint.IntegerMinValue;
/**
 * 
 * @author sonnh1
 *
 */
@IntegerMaxValue(9999)
@IntegerMinValue(1900)
public class YearKey extends IntegerPrimitiveValue<YearKey> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public YearKey(Integer rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
