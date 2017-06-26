package nts.uk.ctx.basic.dom.system.era;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
@StringMaxLength(1)
@StringCharType(CharType.ANY_HALF_WIDTH)
public class EraMark extends StringPrimitiveValue<EraMark> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EraMark(String arg0) {
		// TODO Auto-generated constructor stub
		super(arg0);
	}

}
