package nts.uk.ctx.bs.person.dom.person.setting.selectionitem.selection;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(10)
@StringCharType(CharType.ALPHABET)
public class SelectionCD extends StringPrimitiveValue<SelectionCD>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelectionCD(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
