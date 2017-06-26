package nts.uk.ctx.basic.dom.system.bank.branch;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(20)
public class BankBranchName extends StringPrimitiveValue<BankBranchName>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BankBranchName(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
