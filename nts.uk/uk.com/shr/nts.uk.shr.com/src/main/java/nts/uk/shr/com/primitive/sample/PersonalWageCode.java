package nts.uk.shr.com.primitive.sample;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(2)
public class PersonalWageCode extends StringPrimitiveValue<PersonalWageCode> {

    public PersonalWageCode(String rawValue) {
        super(rawValue);
    }

    private static final long serialVersionUID = 1L; 
}
