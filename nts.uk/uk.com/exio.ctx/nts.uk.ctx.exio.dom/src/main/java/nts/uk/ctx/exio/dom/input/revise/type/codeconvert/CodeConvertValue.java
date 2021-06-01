package nts.uk.ctx.exio.dom.input.revise.type.codeconvert;

import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * コード変換名称
 */
@StringMaxLength(10)
public class CodeConvertValue extends StringPrimitiveValue<PrimitiveValue<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Contructor
	 * 
	 * @param rawValue
	 */
	public CodeConvertValue(String rawValue) {
		super(rawValue);
	}

}
