package nts.uk.ctx.pr.core.app.command.itemmaster.itemsalary;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalary;

@Getter
@Setter
public class UpdateItemSalaryCommand {

	private String itemCode;
	private int taxAtr;
	private int socialInsAtr;
	private int laborInsAtr;
	private int fixPayAtr;
	private int applyForAllEmpFlg;
	private int applyForMonthlyPayEmp;
	private int applyForDaymonthlyPayEmp;
	private int applyForDaylyPayEmp;
	private int applyForHourlyPayEmp;
	private int avePayAtr;
	private int errRangeLowAtr;
	private BigDecimal errRangeLow;
	private int errRangeHighAtr;
	private BigDecimal errRangeHigh;
	private int alRangeLowAtr;
	private BigDecimal alRangeLow;
	private int alRangeHighAtr;
	private BigDecimal alRangeHigh;
	private String memo;
	private int limitMnyAtr;
	private String limitMnyRefItemCode;
	private BigDecimal limitMny;

	public ItemSalary toDomain() {
		return ItemSalary.createFromJavaType(this.itemCode, this.taxAtr, this.socialInsAtr, this.laborInsAtr,
				this.fixPayAtr, this.applyForAllEmpFlg, this.applyForMonthlyPayEmp, this.applyForDaymonthlyPayEmp,
				this.applyForDaylyPayEmp, this.applyForHourlyPayEmp, this.avePayAtr, this.errRangeLowAtr,
				this.errRangeLow, this.errRangeHighAtr, this.errRangeHigh, this.alRangeLowAtr, this.alRangeLow,
				this.alRangeHighAtr, this.alRangeHigh, this.memo, this.limitMnyAtr, this.limitMnyRefItemCode,
				this.limitMny);
	}

}
