package nts.uk.ctx.basic.dom.organization.department;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 
 *部門名称
 */
@StringMaxLength(20)
public class DepartmentName extends StringPrimitiveValue<DepartmentName>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public DepartmentName(String rawValue) {
		super(rawValue);
	}

}
