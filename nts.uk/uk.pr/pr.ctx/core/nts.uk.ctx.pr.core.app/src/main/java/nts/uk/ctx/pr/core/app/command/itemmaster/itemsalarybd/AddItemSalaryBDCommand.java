package nts.uk.ctx.pr.core.app.command.itemmaster.itemsalarybd;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalarybd.ItemSalaryBD;

@Getter
@Setter
public class AddItemSalaryBDCommand {
	private String itemCode;
	private String itemBreakdownCode;
	private String itemBreakdownName;
	private String itemBreakdownAbName;
	private String uniteCode;
	private int zeroDispSet;
	private int itemDispAtr;
	private int errRangeLowAtr;
	private BigDecimal errRangeLow;
	private int errRangeHighAtr;
	private BigDecimal errRangeHigh;
	private int alRangeLowAtr;
	private BigDecimal alRangeLow;
	private int alRangeHighAtr;
	private BigDecimal alRangeHigh;


	public ItemSalaryBD toDomain() {
		return ItemSalaryBD.createFromJavaType(itemCode, itemBreakdownCode, itemBreakdownName, itemBreakdownAbName, uniteCode,
				zeroDispSet, itemDispAtr, errRangeLowAtr, errRangeLow, errRangeHighAtr, errRangeHigh, alRangeLowAtr,
				alRangeLow, alRangeHighAtr, alRangeHigh);

	}
}
