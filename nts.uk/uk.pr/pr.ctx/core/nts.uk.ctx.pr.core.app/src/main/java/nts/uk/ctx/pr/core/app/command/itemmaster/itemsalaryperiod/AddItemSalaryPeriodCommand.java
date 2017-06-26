package nts.uk.ctx.pr.core.app.command.itemmaster.itemsalaryperiod;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalaryperiod.ItemSalaryPeriod;

@Getter
@Setter
public class AddItemSalaryPeriodCommand {
	public String itemCode;
	public int periodAtr;
	public int strY;
	public int endY;
	public int cycleAtr;
	public int cycle01Atr;
	public int cycle02Atr;
	public int cycle03Atr;
	public int cycle04Atr;
	public int cycle05Atr;
	public int cycle06Atr;
	public int cycle07Atr;
	public int cycle08Atr;
	public int cycle09Atr;
	public int cycle10Atr;
	public int cycle11Atr;
	public int cycle12Atr;

	public ItemSalaryPeriod toDomain() {
		return ItemSalaryPeriod.createFromJavaType(itemCode, periodAtr, strY, endY, cycleAtr, cycle01Atr, cycle02Atr,
				cycle03Atr, cycle04Atr, cycle05Atr, cycle06Atr, cycle07Atr, cycle08Atr, cycle09Atr, cycle10Atr,
				cycle11Atr, cycle12Atr);
	}


}
