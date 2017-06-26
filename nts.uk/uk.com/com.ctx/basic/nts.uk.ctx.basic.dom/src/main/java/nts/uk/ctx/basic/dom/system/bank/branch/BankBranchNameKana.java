package nts.uk.ctx.basic.dom.system.bank.branch;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(15)
public class BankBranchNameKana extends StringPrimitiveValue<BankBranchNameKana> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BankBranchNameKana(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
