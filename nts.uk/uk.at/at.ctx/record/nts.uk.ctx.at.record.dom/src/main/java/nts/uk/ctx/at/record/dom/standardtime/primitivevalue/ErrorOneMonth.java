package nts.uk.ctx.at.record.dom.standardtime.primitivevalue;

import nts.arc.primitive.TimeDurationPrimitiveValue;
import nts.arc.primitive.constraint.TimeRange;

/**
 * 
 * @author nampt
 *
 */
@TimeRange(max="744:00", min = "00:00")
public class ErrorOneMonth extends TimeDurationPrimitiveValue<ErrorOneMonth>{
	
	public ErrorOneMonth(int rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;

}