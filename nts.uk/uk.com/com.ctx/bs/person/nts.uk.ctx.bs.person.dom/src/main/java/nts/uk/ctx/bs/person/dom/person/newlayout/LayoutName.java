package nts.uk.ctx.bs.person.dom.person.newlayout;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

@StringCharType(CharType.ALPHABET)
@StringMaxLength(20)
public class LayoutName extends StringPrimitiveValue<LayoutName>{
	
	private static final long serialVersionUID = 1L;
	
	public LayoutName(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
