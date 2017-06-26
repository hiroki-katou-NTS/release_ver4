package nts.uk.ctx.pr.formula.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author hungnm
 *
 */
@StringMaxLength(4)
public class ItemCode extends StringPrimitiveValue<ItemCode> {
	public ItemCode(String rawValue) {
		super(rawValue);
	}

	private static final long serialVersionUID = 1L;
}
