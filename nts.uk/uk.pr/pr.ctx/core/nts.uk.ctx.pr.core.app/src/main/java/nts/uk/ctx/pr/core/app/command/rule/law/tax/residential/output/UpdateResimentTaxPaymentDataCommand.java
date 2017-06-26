package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.output;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@NoArgsConstructor
@Data
public class UpdateResimentTaxPaymentDataCommand {
	private String resimentTaxCode;
	
	private int yearMonth;
	
	private BigDecimal taxPayRollMoney;
	
	private BigDecimal taxBonusMoney;
	
	private BigDecimal taxOverDueMoney;
	
	private BigDecimal taxDemandChargeMoyney;
	
	private String address;
	
	private GeneralDate dueDate;
	
	private BigDecimal headcount;
	
	private BigDecimal retirementBonusAmout;
	
	private BigDecimal cityTaxMoney;
	
	private BigDecimal prefectureTaxMoney;
}
