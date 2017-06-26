package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author hungnm
 *
 */
@StringMaxLength(20)
public class FormulaName extends StringPrimitiveValue<FormulaName> {
	public FormulaName(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
