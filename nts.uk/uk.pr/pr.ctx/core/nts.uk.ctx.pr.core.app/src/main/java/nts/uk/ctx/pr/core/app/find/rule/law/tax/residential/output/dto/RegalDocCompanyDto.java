package nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.output.dto;

import lombok.Value;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.RegalDocCompany;

@Value
public class RegalDocCompanyDto {
	private String companyCode;
	private String reganDocCompanyCode;
	private String reganDocCompanyName;
	private String reganDocCompanyNameForStatutory;
	private String reganDocCompanyNameForStatutoryAbbreviation;
	private Long corporateNumber;
	private String companyRepresentativeName;
	private String companyRepresentativeTitle;
	private String bindingDepartmentCode;
	private String salaryPayerZipCode;
	private String salaryPayerAddress1;
	private String salaryPayerAddress2;
	private String salaryPayerAddressKana1;
	private String salaryPayerAddressKana2;
	private String salaryPayerTelephoneNumber;
	private String accountantFullName;
	private String contactPersonBelongingSectionStaff;
	private String contactName;
	private String contactPhoneNumber;
	private String accountingOfficeName;
	private String accountingBusinessPhoneNumber;
	private String salaryPaymentMethod1;
	private String salaryPaymentMethod2;
	private String salaryPaymentMethod3;
	private String businessLine1;
	private String businessLine2;
	private String businessLine3;
	private String jurisdictionTaxOffice;
	private String nameOfFinancialInstitution;
	private String financialInstitutionLocation;
	private String inhabitantTaxReportDestinationCode;
	private String Notes;

	public static RegalDocCompanyDto fromDomain(RegalDocCompany domain) {
		return new RegalDocCompanyDto(domain.getCompanyCode().v(), domain.getReganDocCompanyCode(),
				domain.getReganDocCompanyName(), domain.getReganDocCompanyNameForStatutory(),
				domain.getReganDocCompanyNameForStatutoryAbbreviation(), domain.getCorporateNumber(),
				domain.getCompanyRepresentativeName(), domain.getCompanyRepresentativeTitle(),
				domain.getBindingDepartmentCode(), domain.getSalaryPayerZipCode(), domain.getSalaryPayerAddress1(),
				domain.getSalaryPayerAddress2(), domain.getSalaryPayerAddressKana1(),
				domain.getSalaryPayerAddressKana2(), domain.getSalaryPayerTelephoneNumber(),
				domain.getAccountantFullName(), domain.getContactPersonBelongingSectionStaff(), domain.getContactName(),
				domain.getContactPhoneNumber(), domain.getAccountingOfficeName(),
				domain.getAccountingBusinessPhoneNumber(), domain.getSalaryPaymentMethod1(),
				domain.getSalaryPaymentMethod2(), domain.getSalaryPaymentMethod3(), domain.getBusinessLine1(),
				domain.getBusinessLine2(), domain.getBusinessLine3(), domain.getJurisdictionTaxOffice(),
				domain.getNameOfFinancialInstitution(), domain.getFinancialInstitutionLocation(),
				domain.getInhabitantTaxReportDestinationCode(), domain.getNotes());
	}

}
