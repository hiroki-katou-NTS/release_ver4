package nts.uk.ctx.pr.core.dom.rule.employment.layout;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringCharType(CharType.NUMERIC)
@StringMaxLength(2)
public class LayoutCode extends CodePrimitiveValue<LayoutCode> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param rawValue
	 */
	public LayoutCode(String rawValue) {
		super(rawValue);
	}


}
