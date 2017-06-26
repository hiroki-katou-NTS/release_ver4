package nts.uk.ctx.basic.dom.company;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

/**
 * Code of Company
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(4)
public class CompanyCode extends CodePrimitiveValue<CompanyCode>{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public CompanyCode(String rawValue) {
		super(rawValue);
	}

}
