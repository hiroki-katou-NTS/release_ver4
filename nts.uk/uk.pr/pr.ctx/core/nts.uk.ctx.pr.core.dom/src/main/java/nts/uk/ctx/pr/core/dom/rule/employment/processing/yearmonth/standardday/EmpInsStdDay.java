package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.standardday;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

@IntegerRange(min = 1, max = 31)
public class EmpInsStdDay extends IntegerPrimitiveValue<EmpInsStdDay> {

	private static final long serialVersionUID = 1L;

	public EmpInsStdDay(Integer rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
