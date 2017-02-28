package nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.output.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;

@AllArgsConstructor
@Data
public class ResimentTaxPaymentDataDto {
	private String resimentTaxCode;
	
	private int yearMonth;
	
	private BigDecimal taxPayRollMoney;
	
	private BigDecimal taxBonusMoney;
	
	private BigDecimal taxOverDueMoney;
	
	private BigDecimal taxDemandChargeMoyney;
	
	private String address;
	
	private GeneralDate dueDate;
	
	private int headcount;
	
	private BigDecimal retirementBonusAmout;
	
	private BigDecimal cityTaxMoney;
	
	private BigDecimal prefectureTaxMoney;
}
