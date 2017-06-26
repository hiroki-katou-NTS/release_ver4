package nts.uk.ctx.pr.core.app.command.itemmaster.itemattend;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttend;

@Getter
@Setter
public class UpdateItemAttendCommand {

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
		return ItemAttend.createFromJavaType(this.itemCode, this.avePayAtr, this.itemAtr, this.errRangeLowAtr,
				this.errRangeLow, this.errRangeHighAtr, this.errRangeHigh, this.alRangeLowAtr, this.alRangeLow,
				this.alRangeHighAtr, this.alRangeHigh, this.workDaysScopeAtr, this.memo);
	}
}
