package nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalMaxValue;
import nts.arc.primitive.constraint.DecimalMinValue;

/**
 * 
 * @author nampt
 * 休憩枠NO
 *
 */
@DecimalMaxValue("10")
@DecimalMinValue("1")
public class BreakFrameNo extends DecimalPrimitiveValue<BreakFrameNo> {

	private static final long serialVersionUID = 1L;
	
	public BreakFrameNo(BigDecimal rawValue) {
		super(rawValue);
	}

}
