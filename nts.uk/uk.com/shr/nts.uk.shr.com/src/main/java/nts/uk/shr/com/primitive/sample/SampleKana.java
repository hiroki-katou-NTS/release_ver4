package nts.uk.shr.com.primitive.sample;

import nts.arc.primitive.KanaPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(10)
public class SampleKana extends KanaPrimitiveValue<SampleKana> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public SampleKana(String rawValue) {
		super(rawValue);
	}

}
