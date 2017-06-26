package nts.uk.ctx.basic.dom.organization.employment;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;
/**
 * 雇用名称
 */
@StringMaxLength(20)
public class EmploymentName extends StringPrimitiveValue<EmploymentName> {
	/**serialVersionUID	 */
	private static final long serialVersionUID = 1L;
	
	public EmploymentName(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}
}
