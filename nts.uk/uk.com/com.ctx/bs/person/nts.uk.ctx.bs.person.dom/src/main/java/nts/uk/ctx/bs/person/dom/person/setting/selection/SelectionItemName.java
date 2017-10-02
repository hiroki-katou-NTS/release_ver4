package nts.uk.ctx.bs.person.dom.person.setting.selection;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(30)
@StringCharType(CharType.ALPHABET)
public class SelectionItemName extends StringPrimitiveValue<SelectionItemName>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectionItemName(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
