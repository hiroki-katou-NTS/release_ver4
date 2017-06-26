package nts.uk.ctx.basic.dom.company;

import java.util.List;
import java.util.Optional;
/**
 * 
 * @author lanlt
 *
 */

public interface CompanyRepository {
	/**
	 * SEL_1
	 * @param companyCd
	 * @return Optional<Company>
	 */
	List<Company> getAllCompanys();
	
	/**
	 * SEL_2
	 * find a company by 
	 * @param companyCode
	 * @return Optional<Company>
	 */
	Optional<Company> getCompanyDetail(String companyCd);
	
	/**
	 * SEL_4
	 * @param companyCd
	 * @param use_Kt_Set
	 * @return Optional<Company>
	 */
	Optional<Company> getCompanyByUserKtSet(String companyCd, int use_Kt_Set);														
	/**
	 * Add a company
	 * @param company
	 */
	
	void add(Company company);
	/**
	 * Update a copany
	 * @param company
	 */
	void update(Company company);
	/**
	 * Delete a company
	 * @param companyCode
	 */
	void delete(String companyCd);
}

