package nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerMaxValue;
import nts.arc.primitive.constraint.IntegerMinValue;

/**
 * 
 * @author nampt
 * 外出枠NO
 *
 */
@IntegerMinValue(1)
@IntegerMaxValue(10)
public class OutingFrameNo extends IntegerPrimitiveValue<OutingFrameNo> {
	
	private static final long serialVersionUID = 1L;
	
	public OutingFrameNo(Integer rawValue) {
		super(rawValue);
	}

}
