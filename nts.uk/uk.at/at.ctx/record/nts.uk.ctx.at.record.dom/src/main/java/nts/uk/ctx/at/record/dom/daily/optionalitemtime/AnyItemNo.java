package nts.uk.ctx.at.record.dom.daily.optionalitemtime;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkNo;

public class AnyItemNo extends IntegerPrimitiveValue<WorkNo>{

	private static final long serialVersionUID = 1L;
	
	public AnyItemNo(Integer rawValue) {
		super(rawValue);
	}
}