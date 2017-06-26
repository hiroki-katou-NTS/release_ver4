package nts.uk.ctx.basic.dom.company;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;
/**
 * 
 * @author lanlt
 *
 */
@IntegerRange(min = 1, max = 12)
public class TermBeginMon extends IntegerPrimitiveValue<TermBeginMon>{
	/**serialVersionUID*/
	private static final long serialVersionUID = 1L;
	/**
	 * @param rawValue
	 */
	public TermBeginMon(int rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}



}
