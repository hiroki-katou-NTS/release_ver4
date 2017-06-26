package nts.uk.ctx.pr.core.app.command.itemmaster.itemdeduct;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeduct.ItemDeduct;

/**
 * @author sonnlb
 *
 */
@Getter
@Setter
public class AddItemDeductCommand {
	private String itemCode;
	private int deductAtr;
	private int errRangeLowAtr;
	private BigDecimal errRangeLow;
	private int errRangeHighAtr;
	private BigDecimal errRangeHigh;
	private int alRangeLowAtr;
	private BigDecimal alRangeLow;
	private int alRangeHighAtr;
	private BigDecimal alRangeHigh;
	private String memo;

	public ItemDeduct toDomain() {
		return ItemDeduct.createFromJavaType(itemCode, deductAtr, errRangeLowAtr, errRangeLow, errRangeHighAtr,
				errRangeHigh, alRangeLowAtr, alRangeLow, alRangeHighAtr, alRangeHigh, memo);
	}
}
