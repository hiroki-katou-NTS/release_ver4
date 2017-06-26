package nts.uk.ctx.pr.formula.dom.primitive;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

/**
 * @author hungnm
 *
 */
@DecimalRange(min = "0", max = "999")
public class PremiumRate extends DecimalPrimitiveValue<PremiumRate> {
	public PremiumRate(BigDecimal rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
