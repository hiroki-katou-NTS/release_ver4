package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.ResidentialTax;
/**
 * UpdateResidentalTaxCommand
 * @author lanlt
 *
 */
@Getter
public class UpdateResidentialTaxCommand {
	private String resiTaxCode;
	private String resiTaxAutonomy;
	private String resiTaxAutonomyKana;
	private String prefectureCode;
	private String resiTaxReportCode;
	private String registeredName;
	private String companyAccountNo;
	private String companySpecifiedNo;
	private String cordinatePostalCode;
	private String cordinatePostOffice;
	private String memo;
	/**
	 * Convert to domain object from command values
	 * @return
	 */
	public ResidentialTax toDomain(String companyCode){
		return ResidentialTax.createFromJavaType(companyCode, resiTaxCode,
				resiTaxAutonomy, resiTaxAutonomyKana, prefectureCode,
				resiTaxReportCode, registeredName,
				companyAccountNo,companySpecifiedNo,
				cordinatePostOffice, cordinatePostalCode, memo);
	}	


}
