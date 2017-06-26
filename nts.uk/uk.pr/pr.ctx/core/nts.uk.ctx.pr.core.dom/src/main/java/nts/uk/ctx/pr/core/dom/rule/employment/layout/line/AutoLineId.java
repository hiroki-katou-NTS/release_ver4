package nts.uk.ctx.pr.core.dom.rule.employment.layout.line;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(36)
public class AutoLineId extends StringPrimitiveValue<AutoLineId>{

	public AutoLineId(String rawValue) {
		super(rawValue);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
