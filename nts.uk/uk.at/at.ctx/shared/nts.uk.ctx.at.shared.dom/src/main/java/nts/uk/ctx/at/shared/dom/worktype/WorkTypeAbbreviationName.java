package nts.uk.ctx.at.shared.dom.worktype;

import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;
@StringMaxLength(6)
public class WorkTypeAbbreviationName extends StringPrimitiveValue<PrimitiveValue<String>>{
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public WorkTypeAbbreviationName(String rawValue) {
		super(rawValue);
	}
}
