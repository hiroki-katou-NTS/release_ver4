package nts.uk.ctx.pr.transfer.dom.bank;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 
 * @author HungTT - 銀行名
 *
 */
@StringMaxLength(20)
public class BankName extends StringPrimitiveValue<BankName> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BankName(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}