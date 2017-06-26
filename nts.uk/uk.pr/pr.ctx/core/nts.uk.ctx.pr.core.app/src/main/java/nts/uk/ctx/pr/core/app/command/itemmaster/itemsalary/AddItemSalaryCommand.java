package nts.uk.ctx.pr.core.app.command.itemmaster.itemsalary;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalary;

@Getter
@Setter
public class AddItemSalaryCommand {
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
		return ItemSalary.createFromJavaType(itemCode, taxAtr, socialInsAtr, laborInsAtr, fixPayAtr, applyForAllEmpFlg,
				applyForMonthlyPayEmp, applyForDaymonthlyPayEmp, applyForDaylyPayEmp, applyForHourlyPayEmp, avePayAtr,
				errRangeLowAtr, errRangeLow, errRangeHighAtr, errRangeHigh, alRangeLowAtr, alRangeLow, alRangeHighAtr,
				alRangeHigh, memo, limitMnyAtr, limitMnyRefItemCode, limitMny);
	}
}
