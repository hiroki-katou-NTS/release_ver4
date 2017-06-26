package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output;

import java.util.Optional;
/**
 * 
 * @author phongtq
 *
 */
public interface ResidentTaxPaymentDataRepository {
	/**
	 * Find Resident Tax Payment by CompanyCode, ResidentTaxCode and YearMonth
	 * @param companyCode
	 * @param residentTaxCode
	 * @param yearMonth
	 * @return
	 */
	Optional<ResidentTaxPaymentData> find(String companyCode, String residentTaxCode, int yearMonth);
	
	/**
	 * Add Resident Tax Payment to dbo.QTXMT_RESIDENTIAL_TAXSLIP
	 * @param companyCode
	 * @param domain
	 */
	void add(String companyCode, ResidentTaxPaymentData domain);
	
	/**
	 * Uppdate Resident Tax Payment to dbo.QTXMT_RESIDENTIAL_TAXSLIP
	 * @param companyCode
	 * @param domain
	 */
	void update(String companyCode, ResidentTaxPaymentData domain);
}
