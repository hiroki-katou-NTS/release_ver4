package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author hungnm
 *
 */
@StringMaxLength(3)
public class EasyFormulaCode extends StringPrimitiveValue<EasyFormulaCode> {
	public EasyFormulaCode(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
