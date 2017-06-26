package nts.uk.ctx.pr.core.app.find.rule.law.tax.residential;
import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.ResidentialTax;

/**
 * 
 * @author lanlt
 *
 */
@Value
public class ResidentialTaxDetailDto {
	private String companyCode;
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

	public static ResidentialTaxDetailDto fromDomain(ResidentialTax domain) {
		return new ResidentialTaxDetailDto(domain.getCompanyCode().v(), domain.getResiTaxCode().v(),
				domain.getResiTaxAutonomy().v(),domain.getResiTaxAutonomyKana().v(), domain.getPrefectureCode().v(), domain.getResiTaxReportCode().v(),
				domain.getRegisteredName().v(), domain.getCompanyAccountNo().v(), domain.getCompanySpecifiedNo().v(),
				domain.getCordinatePostalCode().v(), domain.getCordinatePostOffice().v(), domain.getMemo().v());
	}
}

