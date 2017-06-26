package nts.uk.file.pr.app.export.residentialtax.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResidentialTaxDto {
	private String residenceTaxCode;
	private String resiTaxAutonomy;
	private String registeredName;
	private String companyAccountNo;
	private String companySpecifiedNo;
	private String cordinatePostalCode;
	private String cordinatePostOffice;
	private String resiTaxAutonomyKnName;
}
