package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.input;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input.PersonResiTax;

@Data
@NoArgsConstructor
public class UpdatePersonResiTaxCommand {
	private List<ResidenceTaxCommand> residenceTax;
	private BigDecimal residenceTaxBn;
	private BigDecimal residenceTaxAve;
	private int yearKey;
	private String residenceCode;
	private String personId;
	private int residenceTaxLumpAtr;
	private int residenceTaxLumpYm;
	private int residenceTaxLevyAtr;
	

	public PersonResiTax toDomain(String companyCode) {
		PersonResiTax domain =  PersonResiTax.createFromJavaType(companyCode, this.personId, 
				this.yearKey, this.residenceCode, this.residenceTaxBn, this.residenceTaxAve, 
				this.residenceTaxLumpAtr, this.residenceTaxLumpYm, this.residenceTaxLevyAtr);
		domain.createResidenceTaxFromJavaType(1, residenceTax.get(0).getValue(), 2, residenceTax.get(1).getValue(), 3, residenceTax.get(2).getValue(), 
				4, residenceTax.get(3).getValue(), 5, residenceTax.get(4).getValue(), 6, residenceTax.get(5).getValue(), 7, residenceTax.get(6).getValue(),
				8, residenceTax.get(7).getValue(), 9, residenceTax.get(8).getValue(), 10, residenceTax.get(9).getValue(), 11, residenceTax.get(10).getValue(), 12, residenceTax.get(11).getValue());
		return domain;
	}
}
