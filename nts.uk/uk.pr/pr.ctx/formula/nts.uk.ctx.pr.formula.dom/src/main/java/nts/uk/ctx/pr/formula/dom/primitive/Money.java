package nts.uk.ctx.pr.formula.dom.primitive;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

/**
 * @author hungnm
 *
 */
@DecimalRange(min = "-999999999", max = "9999999999")
public class Money extends DecimalPrimitiveValue<Money> {
	public Money(BigDecimal rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
