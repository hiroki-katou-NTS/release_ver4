package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;


@StringCharType(CharType.NUMERIC)
@StringMaxLength(3)
public class DepartmentCode  extends CodePrimitiveValue<DepartmentCode> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DepartmentCode(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}



}
