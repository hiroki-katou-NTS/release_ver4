package nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductperiod;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeductperiod.ItemDeductPeriod;

@Getter
@Setter
public class AddItemDeductPeriodCommand {
	private String itemCode;
	private int periodAtr;
	private int strY;
	private int endY;
	private int cycleAtr;
	private int cycle01Atr;
	private int cycle02Atr;
	private int cycle03Atr;
	private int cycle04Atr;
	private int cycle05Atr;
	private int cycle06Atr;
	private int cycle07Atr;
	private int cycle08Atr;
	private int cycle09Atr;
	private int cycle10Atr;
	private int cycle11Atr;
	private int cycle12Atr;

	//convert command data type to domain type
	public ItemDeductPeriod toDomain() {
		return ItemDeductPeriod.createFromJavaType(itemCode, periodAtr, strY, endY, cycleAtr, cycle01Atr, cycle02Atr,
				cycle03Atr, cycle04Atr, cycle05Atr, cycle06Atr, cycle07Atr, cycle08Atr, cycle09Atr, cycle10Atr,
				cycle11Atr, cycle12Atr);
	}

}
