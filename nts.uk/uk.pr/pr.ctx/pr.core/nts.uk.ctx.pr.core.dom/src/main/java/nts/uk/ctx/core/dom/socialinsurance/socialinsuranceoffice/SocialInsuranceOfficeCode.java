package nts.uk.ctx.core.dom.socialinsurance.socialinsuranceoffice;

import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.ZeroPaddedCode;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.StringPrimitiveValue;

/**
 * 社会保険事業所コード
 */
@StringMaxLength(12)
@ZeroPaddedCode
@StringCharType(CharType.NUMERIC)
public class SocialInsuranceOfficeCode extends StringPrimitiveValue<SocialInsuranceOfficeCode> {

    private static final long serialVersionUID = 1L;

    public SocialInsuranceOfficeCode(String rawValue) {
        super(rawValue);
    }

}
