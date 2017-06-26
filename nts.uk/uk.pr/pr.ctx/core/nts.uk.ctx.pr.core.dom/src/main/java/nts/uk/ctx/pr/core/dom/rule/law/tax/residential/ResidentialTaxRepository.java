/**
 * 
 */
package nts.uk.ctx.pr.core.dom.rule.law.tax.residential;

import java.util.List;
import java.util.Optional;

/**
 * @author lanlt
 *
 */
public interface ResidentialTaxRepository {
	/**
	 * SEL_1
	 * get list residental by company code
	 * @param companyCode
	 * @return List<ResidentalTax>
	 */
	List<ResidentialTax> getAllResidentialTax(String companyCode);
	/**
	 * SEL_2
	 * get list residental by company code, !=resiTaxCode, resiTaxReportCode
	 * @param companyCode, resiTaxCode, resiTaxReportCode
	 * @return List<ResidentalTax>
	 */
	List<ResidentialTax> getAllResidentialTax(String companyCode, String resiTaxCode,  String  resiTaxReportCode);
	/**
	 * SEL_3
	 * get list residental by company code, resiTaxCode
	 * @param companyCode, resiTaxCode
	 * @return Optional<ResidentialTax>
	 */
	Optional<ResidentialTax> getResidentialTax(String companyCode, String resiTaxCode);
	/**
	 * SEL_5
	 * get list residental by company code, resiTaxReportCode
	 * @param companyCode, resiTaxCode
	 * @return List<String> contains resitaxCode
	 */
	List<?> getAllResidentialTaxCode(String companyCode, String resiTaxReportCode);

	void add(ResidentialTax residentalTax);
	
	//void update(String companyCode,String resiTaxCode);
	void delele(String companyCode,String resiTaxCode);
	
	//UPD_1
	void update(ResidentialTax residentalTax);
	
	//UPD-2
	void update(String companyCode,String resiTaxCode, String resiTaxReportCode);
	

}
