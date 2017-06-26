package nts.uk.file.pr.app.export.residentialtax;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.file.pr.app.export.residentialtax.data.CompanyDto;
import nts.uk.file.pr.app.export.residentialtax.data.PaymentDetailDto;
import nts.uk.file.pr.app.export.residentialtax.data.PersonResitaxDto;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentialTaxDto;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentialTaxSlipDto;
import nts.uk.file.pr.app.export.residentialtax.data.RetirementPaymentDto;

public interface ResidentialTaxReportRepository {
	/**
	 * Find all resident tax
	 * @param companyCode
	 * @param yearMonth
	 * @param residenceCodeList
	 * @return
	 */
	List<ResidentialTaxDto> findResidentTax(String companyCode, List<String> residenceTaxCodeList);

	CompanyDto findCompany(String companyCode);

	CompanyDto findRegalCompany(String companyCode, String regalDocCompanyCode);


	List<ResidentialTaxSlipDto> findResidentialTaxSlip(String companyCode, int yearMonth,
			List<String> residentTaxCodeList);

	List<PaymentDetailDto> findPaymentDetail(String companyCode, List<String> personIdList, PayBonusAtr salary,
			int processingYearMonth, CategoryAtr deduction, String string);

	List<RetirementPaymentDto> findRetirementPaymentList(String companyCode, List<String> personIdList,
			GeneralDate baseRangeStartYearMonth, GeneralDate baseRangeEndYearMonth);

	List<PersonResitaxDto> findPersonResidentTax(String companyCode, int yearMonth, List<String> residentTaxCodeList);
	
	Optional<String> findPersonText(String companyCode);

}
