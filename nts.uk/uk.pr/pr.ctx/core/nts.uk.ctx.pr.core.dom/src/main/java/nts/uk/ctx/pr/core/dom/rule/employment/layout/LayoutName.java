package nts.uk.ctx.pr.core.dom.rule.employment.layout;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(20)
public class LayoutName extends StringPrimitiveValue<LayoutName> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue raw value
	 */
	public LayoutName(String rawValue) {
		super(rawValue);
	}

}
