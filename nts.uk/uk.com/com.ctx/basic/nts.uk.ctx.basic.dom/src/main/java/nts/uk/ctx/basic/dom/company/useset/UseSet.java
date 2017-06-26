package nts.uk.ctx.basic.dom.company.useset;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;
/**
 * 
 * @author lanlt
 *
 */
@IntegerRange(min = 0, max = 1)
public class UseSet extends IntegerPrimitiveValue<UseSet>{
	
	/**serialVersionUID	 */
	private static final long serialVersionUID = 1L;
	/**
	 * constructors
	 * @param rawValue
	 */
	public UseSet(Integer rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	

}
