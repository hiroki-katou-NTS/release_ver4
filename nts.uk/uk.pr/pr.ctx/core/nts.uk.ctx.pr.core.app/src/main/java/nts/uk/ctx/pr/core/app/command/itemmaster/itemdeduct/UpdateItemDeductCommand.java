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
public class UpdateItemDeductCommand {
	
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
		return ItemDeduct.createFromJavaType( this.itemCode, this.deductAtr, this.errRangeLowAtr,
				this.errRangeLow, this.errRangeHighAtr, this.errRangeHigh, this.alRangeLowAtr, this.alRangeLow,
				this.alRangeHighAtr, this.alRangeHigh, this.memo);
	}
	
}