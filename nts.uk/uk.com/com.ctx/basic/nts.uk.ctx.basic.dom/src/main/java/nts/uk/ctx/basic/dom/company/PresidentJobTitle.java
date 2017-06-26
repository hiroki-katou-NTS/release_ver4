package nts.uk.ctx.basic.dom.company;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;
/**
 *@author lanlt
 */
@StringMaxLength(20)
/*@StringCharType(CharType.ALPHA_NUMERIC)*/
public class PresidentJobTitle extends StringPrimitiveValue<PresidentJobTitle> {
	/**serialVersionUID*/
	private static final long serialVersionUID = 1L;	
	/**
	 * contructors
	 * @param rawValue raw Value
	 */
	public PresidentJobTitle(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
