package nts.uk.file.pr.app.export.residentialtax.data;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
@AllArgsConstructor
public class ResidentialTaxSlipDto {
	private String resiTaxCode;
	private int yearMonth;
	private BigDecimal taxPayrollMny;
	private BigDecimal taxRetirementMny;
	private BigDecimal taxOverdueMny;
	private BigDecimal taxDemandChargeMny;
	private String address;
	private GeneralDate dueDate;
	private BigDecimal headCount;
	private BigDecimal retirementBonusAmount;
	private BigDecimal cityTaxMny;
	private BigDecimal prefectureTaxMny;
}
