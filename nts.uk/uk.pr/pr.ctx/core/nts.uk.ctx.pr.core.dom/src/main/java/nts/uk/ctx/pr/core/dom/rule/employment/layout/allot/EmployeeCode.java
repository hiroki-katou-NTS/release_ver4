package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;


@StringMaxLength(10)
public class EmployeeCode extends StringPrimitiveValue<EmployeeCode>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructs.
	 * 
	 * @param rawValue raw value
	 */
	public EmployeeCode(String rawValue) {
		super(rawValue);
	}
}
