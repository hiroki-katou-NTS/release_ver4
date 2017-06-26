package nts.uk.ctx.at.record.dom.divergencetime;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;
@DecimalRange(min="0",max="5999")
public class Time extends DecimalPrimitiveValue<PrimitiveValue<BigDecimal>> {
	private static final long serialVersionUID = 1L;
	public Time(BigDecimal rawValue) {
		super(rawValue);
	}
}
