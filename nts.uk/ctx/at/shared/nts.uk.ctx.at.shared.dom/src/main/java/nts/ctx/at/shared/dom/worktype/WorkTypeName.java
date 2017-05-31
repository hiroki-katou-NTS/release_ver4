package nts.ctx.at.shared.dom.worktype;

import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;
@StringMaxLength(12)
public class WorkTypeName extends StringPrimitiveValue<PrimitiveValue<String>>{
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public WorkTypeName(String rawValue) {
		super(rawValue);
	}
}
