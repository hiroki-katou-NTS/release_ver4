package nts.uk.ctx.basic.dom.system.bank;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(15)
public class BankNameKana extends StringPrimitiveValue<BankNameKana>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BankNameKana(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}
