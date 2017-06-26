package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input;

import java.util.List;

/**
 * 
 * @author sonnh1
 *
 */
public interface PersonResiTaxRepository {
	/**
	 * 
	 * @param companyCode
	 * @param personId
	 * @param yearKey
	 * @return
	 */
	List<PersonResiTax> findAll(String companyCode, String personId, int yearKey);

	/**
	 * 
	 * @param companyCode
	 * @param residenceCode
	 * @param yearKey
	 * @return List<String> contains of personId, type string
	 */
	List<?> findByResidenceCode(String companyCode, String residenceCode, int yearKey);

	/**
	 * UPD-1
	 * 
	 * @param domain
	 */
	void update(PersonResiTax domain);

	/**
	 * UPD-2
	 * @param resendenceCode
	 * update field resendenceCode in table PersonResiTax
	 * 
	 */
	void updateResendence(String companyCode,String resendenceCode, String personID, int yearKey);

	/**
	 * 
	 * @param companyCode
	 * @param personId
	 * @param yearKey
	 */
	void remove(String companyCode, String personId, int yearKey);
}
