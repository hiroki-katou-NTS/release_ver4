package nts.uk.ctx.pr.core.app.command.itemmaster.itemattend;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttend;

@Getter
@Setter
public class AddItemAttendCommand {
	private String itemCode;
	private int avePayAtr;
	private int itemAtr;
	private int errRangeLowAtr;
	private BigDecimal errRangeLow;
	private int errRangeHighAtr;
	private BigDecimal errRangeHigh;
	private int alRangeLowAtr;
	private BigDecimal alRangeLow;
	private int alRangeHighAtr;
	private BigDecimal alRangeHigh;
	private int workDaysScopeAtr;
	private String memo;

	public ItemAttend toDomain() {
		return ItemAttend.createFromJavaType(itemCode, avePayAtr, itemAtr, errRangeLowAtr, errRangeLow, errRangeHighAtr,
				errRangeHigh, alRangeLowAtr, alRangeLow, alRangeHighAtr, alRangeHigh, workDaysScopeAtr, memo);
	}
}
