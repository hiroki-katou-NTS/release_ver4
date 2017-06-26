package nts.uk.ctx.basic.dom.organization.department;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 
 *部門総称
 */
@StringMaxLength(40)
public class DepartmentGenericName extends StringPrimitiveValue<DepartmentGenericName>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public DepartmentGenericName(String rawValue) {
		super(rawValue);
	}

}
