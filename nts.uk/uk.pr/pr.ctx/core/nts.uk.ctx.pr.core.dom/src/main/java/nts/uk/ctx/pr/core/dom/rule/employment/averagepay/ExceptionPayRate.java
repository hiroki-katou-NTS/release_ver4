package nts.uk.ctx.pr.core.dom.rule.employment.averagepay;


import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalRange;

/**
 * 例外時割合
 * Exception Pay Rate
 * @author Doan Duy Hung
 *
 */
@DecimalRange(max="100",min="0")
public class ExceptionPayRate extends DecimalPrimitiveValue<ExceptionPayRate>{
	public ExceptionPayRate(BigDecimal value) {		
		super(value);
	}
}
