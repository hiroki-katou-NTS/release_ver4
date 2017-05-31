package nts.ctx.at.shared.dom.worktype;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(3)
public class WorkTypeCode extends CodePrimitiveValue<WorkTypeCode> {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public WorkTypeCode(String rawValue) {
		super(rawValue);
	}

}
