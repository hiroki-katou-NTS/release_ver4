package nts.uk.ctx.basic.dom.system.bank.linebank;

import lombok.Getter;
/**
 * 
 * @author sonnh1
 *
 */
@Getter
public class Consignor {
	private ConsignorCode consignorCode;
	private ConsignorMemo consignorMemo;
	
	public Consignor(ConsignorCode consignorCode, ConsignorMemo consignorMemo) {
		super();
		this.consignorCode = consignorCode;
		this.consignorMemo = consignorMemo;
	}
}
	