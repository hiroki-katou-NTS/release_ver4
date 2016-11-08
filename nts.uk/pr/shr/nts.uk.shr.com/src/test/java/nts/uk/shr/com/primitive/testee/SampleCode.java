package nts.uk.shr.com.primitive.testee;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLengh;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLengh(4)
public class SampleCode extends CodePrimitiveValue<SampleCode> {

    /** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public SampleCode(String rawValue) {
        super(rawValue);
    }
    
}
