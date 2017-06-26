package nts.uk.ctx.basic.dom.company;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
/**
 * 
 * @author lanlt
 *
 */
@StringMaxLength(30)
@StringCharType(CharType.ALPHABET)
public class CompanyNameKana extends StringPrimitiveValue<CompanyNameKana>{
	/**serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 
	 * contructors
	 * @params rawValue  raw value
	 */
	public CompanyNameKana(String rawValue) {
		super(rawValue);
	}
}
