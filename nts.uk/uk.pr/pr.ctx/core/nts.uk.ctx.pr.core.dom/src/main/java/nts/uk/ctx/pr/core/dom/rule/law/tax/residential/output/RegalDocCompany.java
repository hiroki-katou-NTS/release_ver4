package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;

/**
 * @author sonnlb
 *
 */
@Getter
public class RegalDocCompany extends AggregateRoot {
	private CompanyCode companyCode;
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

	public static RegalDocCompany createFromJavaType(String companyCode, String reganDocCompanyCode,
			String reganDocCompanyName, String reganDocCompanyNameForStatutory,
			String reganDocCompanyNameForStatutoryAbbreviation, Long corporateNumber, String companyRepresentativeName,
			String companyRepresentativeTitle, String bindingDepartmentCode, String salaryPayerZipCode,
			String salaryPayerAddress1, String salaryPayerAddress2, String salaryPayerAddressKana1,
			String salaryPayerAddressKana2, String salaryPayerTelephoneNumber, String accountantFullName,
			String contactPersonBelongingSectionStaff, String contactName, String contactPhoneNumber,
			String accountingOfficeName, String accountingBusinessPhoneNumber, String salaryPaymentMethod1,
			String salaryPaymentMethod2, String salaryPaymentMethod3, String businessLine1, String businessLine2,
			String businessLine3, String jurisdictionTaxOffice, String nameOfFinancialInstitution,
			String financialInstitutionLocation, String inhabitantTaxReportDestinationCode, String Notes) {

		return new RegalDocCompany(new CompanyCode(companyCode), reganDocCompanyCode, reganDocCompanyName,
				reganDocCompanyNameForStatutory, reganDocCompanyNameForStatutoryAbbreviation, corporateNumber,
				companyRepresentativeName, companyRepresentativeTitle, bindingDepartmentCode, salaryPayerZipCode,
				salaryPayerAddress1, salaryPayerAddress2, salaryPayerAddressKana1, salaryPayerAddressKana2,
				salaryPayerTelephoneNumber, accountantFullName, contactPersonBelongingSectionStaff, contactName,
				contactPhoneNumber, accountingOfficeName, accountingBusinessPhoneNumber, salaryPaymentMethod1,
				salaryPaymentMethod2, salaryPaymentMethod3, businessLine1, businessLine2, businessLine3,
				jurisdictionTaxOffice, nameOfFinancialInstitution, financialInstitutionLocation,
				inhabitantTaxReportDestinationCode, Notes);
	}

	public RegalDocCompany(CompanyCode companyCode, String reganDocCompanyCode, String reganDocCompanyName,
			String reganDocCompanyNameForStatutory, String reganDocCompanyNameForStatutoryAbbreviation,
			Long corporateNumber, String companyRepresentativeName, String companyRepresentativeTitle,
			String bindingDepartmentCode, String salaryPayerZipCode, String salaryPayerAddress1,
			String salaryPayerAddress2, String salaryPayerAddressKana1, String salaryPayerAddressKana2,
			String salaryPayerTelephoneNumber, String accountantFullName, String contactPersonBelongingSectionStaff,
			String contactName, String contactPhoneNumber, String accountingOfficeName,
			String accountingBusinessPhoneNumber, String salaryPaymentMethod1, String salaryPaymentMethod2,
			String salaryPaymentMethod3, String businessLine1, String businessLine2, String businessLine3,
			String jurisdictionTaxOffice, String nameOfFinancialInstitution, String financialInstitutionLocation,
			String inhabitantTaxReportDestinationCode, String notes) {
		super();
		this.companyCode = companyCode;
		this.reganDocCompanyCode = reganDocCompanyCode;
		this.reganDocCompanyName = reganDocCompanyName;
		this.reganDocCompanyNameForStatutory = reganDocCompanyNameForStatutory;
		this.reganDocCompanyNameForStatutoryAbbreviation = reganDocCompanyNameForStatutoryAbbreviation;
		this.corporateNumber = corporateNumber;
		this.companyRepresentativeName = companyRepresentativeName;
		this.companyRepresentativeTitle = companyRepresentativeTitle;
		this.bindingDepartmentCode = bindingDepartmentCode;
		this.salaryPayerZipCode = salaryPayerZipCode;
		this.salaryPayerAddress1 = salaryPayerAddress1;
		this.salaryPayerAddress2 = salaryPayerAddress2;
		this.salaryPayerAddressKana1 = salaryPayerAddressKana1;
		this.salaryPayerAddressKana2 = salaryPayerAddressKana2;
		this.salaryPayerTelephoneNumber = salaryPayerTelephoneNumber;
		this.accountantFullName = accountantFullName;
		this.contactPersonBelongingSectionStaff = contactPersonBelongingSectionStaff;
		this.contactName = contactName;
		this.contactPhoneNumber = contactPhoneNumber;
		this.accountingOfficeName = accountingOfficeName;
		this.accountingBusinessPhoneNumber = accountingBusinessPhoneNumber;
		this.salaryPaymentMethod1 = salaryPaymentMethod1;
		this.salaryPaymentMethod2 = salaryPaymentMethod2;
		this.salaryPaymentMethod3 = salaryPaymentMethod3;
		this.businessLine1 = businessLine1;
		this.businessLine2 = businessLine2;
		this.businessLine3 = businessLine3;
		this.jurisdictionTaxOffice = jurisdictionTaxOffice;
		this.nameOfFinancialInstitution = nameOfFinancialInstitution;
		this.financialInstitutionLocation = financialInstitutionLocation;
		this.inhabitantTaxReportDestinationCode = inhabitantTaxReportDestinationCode;
		Notes = notes;
	}

}
