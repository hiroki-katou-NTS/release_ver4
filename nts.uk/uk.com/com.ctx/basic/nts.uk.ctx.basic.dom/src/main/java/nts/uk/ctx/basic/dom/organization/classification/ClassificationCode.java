package nts.uk.ctx.basic.dom.organization.classification;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;


@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(10)
public class ClassificationCode extends CodePrimitiveValue<ClassificationCode> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public ClassificationCode(String rawValue) {
		super(rawValue);
	}

}
