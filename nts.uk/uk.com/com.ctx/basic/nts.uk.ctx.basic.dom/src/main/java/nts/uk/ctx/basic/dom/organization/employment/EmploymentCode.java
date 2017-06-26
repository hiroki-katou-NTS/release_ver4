package nts.uk.ctx.basic.dom.organization.employment;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;
/**
 * 雇用コード
 */
@StringMaxLength(10)
@StringCharType(CharType.ALPHA_NUMERIC)
public class EmploymentCode extends CodePrimitiveValue<EmploymentCode> {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	public EmploymentCode(String rawValue) {
		super(rawValue);
	}
}
