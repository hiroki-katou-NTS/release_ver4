package nts.uk.ctx.pr.core.dom.personalinfo.wage.wagename;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(2)
public class PersonalWageCode extends StringPrimitiveValue<PersonalWageCode> {

    public PersonalWageCode(String rawValue) {
        super(rawValue);
    }

    private static final long serialVersionUID = 1L;
}
