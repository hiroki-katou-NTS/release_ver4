package nts.uk.ctx.basic.dom.organization.position;
/*
 * Code of JobCode
 */

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(10)
public class JobCode extends CodePrimitiveValue<JobCode>{
	/**
	 * JOBCD
	 */
	private static final long serialVersionUID = 1L;

	public JobCode(String rawValue) {
		super(rawValue);
	}

}