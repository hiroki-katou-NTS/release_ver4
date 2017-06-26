package nts.uk.ctx.at.record.dom.divergencetime;

import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(30)
public class DivergenceTimeName extends StringPrimitiveValue<PrimitiveValue<String>>{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public DivergenceTimeName(String rawValue) {
		super(rawValue);
	}

}