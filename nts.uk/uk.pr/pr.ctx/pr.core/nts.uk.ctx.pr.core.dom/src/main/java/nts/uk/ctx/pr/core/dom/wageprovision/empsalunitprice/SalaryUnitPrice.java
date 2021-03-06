package nts.uk.ctx.pr.core.dom.wageprovision.empsalunitprice;

import nts.arc.primitive.constraint.DecimalMantissaMaxLength;
import nts.arc.primitive.constraint.DecimalMinValue;
import nts.arc.primitive.constraint.DecimalMaxValue;
import nts.arc.primitive.DecimalPrimitiveValue;

import java.math.BigDecimal;

/**
 * 給与単価金額
 */
@DecimalMinValue("-99999999.99")
@DecimalMaxValue("99999999.99")
@DecimalMantissaMaxLength(2)
public class SalaryUnitPrice extends DecimalPrimitiveValue<SalaryUnitPrice> {

    private static final long serialVersionUID = 1L;

    public SalaryUnitPrice(BigDecimal rawValue) {
        super(rawValue);
    }

}
