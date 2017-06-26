package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author hungnm
 *
 */
@StringMaxLength(6000)
public class FormulaContent extends StringPrimitiveValue<FormulaContent> {
	public FormulaContent(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
