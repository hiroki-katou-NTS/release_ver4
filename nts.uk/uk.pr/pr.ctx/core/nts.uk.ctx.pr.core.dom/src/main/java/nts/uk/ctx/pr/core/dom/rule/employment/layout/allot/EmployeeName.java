package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import java.util.EmptyStackException;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(20)
public class EmployeeName extends StringPrimitiveValue<EmployeeName> {
	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 
	 */
	public EmployeeName(String rawValue){
		super(rawValue);
	}

}
