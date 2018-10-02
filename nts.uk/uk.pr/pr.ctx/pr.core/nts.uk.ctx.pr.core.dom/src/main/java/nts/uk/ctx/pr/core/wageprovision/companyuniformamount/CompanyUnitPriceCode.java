package nts.uk.ctx.pr.core.wageprovision.companyuniformamount;

import nts.arc.primitive.constraint.IntegerMaxValue;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.StringPrimitiveValue;

/**
* 会社単価コード
*/
@StringMaxLength(4)
@StringCharType(CharType.NUMERIC)
public class CompanyUnitPriceCode extends StringPrimitiveValue<CompanyUnitPriceCode>
{
    
    private static final long serialVersionUID = 1L;
    
    public CompanyUnitPriceCode(String rawValue)
    {
         super(rawValue);
    }
    
}
