package nts.uk.ctx.at.record.dom.workrecord.log;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(250)
public class UseCaseName  extends StringPrimitiveValue<UseCaseName>{

	public UseCaseName(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

}
