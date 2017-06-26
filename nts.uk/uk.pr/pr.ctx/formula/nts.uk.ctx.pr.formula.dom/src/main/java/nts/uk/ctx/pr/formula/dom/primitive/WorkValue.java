package nts.uk.ctx.pr.formula.dom.primitive;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

/**
 * @author hungnm
 *
 */
@DecimalRange(min = "-9999999999.99", max = "9999999999.99")
public class WorkValue extends DecimalPrimitiveValue<WorkValue> {
	public WorkValue(BigDecimal rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
