package nts.uk.ctx.basic.dom.organization.classification;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(20)
public class ClassificationName extends StringPrimitiveValue<ClassificationName> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8807988305434200565L;

	public ClassificationName(String rawValue) {
		super(rawValue);
	}

}
