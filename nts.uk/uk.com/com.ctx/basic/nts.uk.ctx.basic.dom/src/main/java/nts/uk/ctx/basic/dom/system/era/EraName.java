package nts.uk.ctx.basic.dom.system.era;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(4)
public class EraName extends StringPrimitiveValue<EraName>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EraName(String arg0) {
		super(arg0);
	}
}
