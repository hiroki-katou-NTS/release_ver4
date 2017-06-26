package nts.uk.ctx.pr.formula.app.find.formula;

import java.math.BigDecimal;

import lombok.Value;
import nts.uk.ctx.pr.formula.dom.formula.BasicPayroll;

/**
 * @author nampt
 *
 */
@Value
public class BasicPayrollDto {

	private BigDecimal timeNotationSetting;

	private BigDecimal baseDay;

	private BigDecimal baseHour;

	public static BasicPayrollDto fromDomain(BasicPayroll basicPayroll) {
		return new BasicPayrollDto(basicPayroll.getTimeNotationSetting().v(), basicPayroll.getBaseDays().v(),
				basicPayroll.getBaseHours().v());
	}
}
