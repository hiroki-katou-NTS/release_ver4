package nts.uk.ctx.pr.core.dom.rule.law.tax.commutelimit;

import java.util.List;
import java.util.Optional;

/**
 * @author tuongvc
 *
 */
public interface CommuteNoTaxLimitRepository {

	/**
	 * Get all CommuteNoTaxLimit
	 * 
	 * @param companyCode
	 * @return CommuteNoTaxLimit
	 */
	List<CommuteNoTaxLimit> getCommuteNoTaxLimitByCompanyCode(String companyCode);
	
	/**
	 * Get  CommuteNoTaxLimit
	 * 
	 * @param companyCode
	 * @return CommuteNoTaxLimit
	 */
	Optional<CommuteNoTaxLimit> getCommuteNoTaxLimit(String companyCode, String taxLimitCode);

	/**
	 * add CommuteNoTaxLimit to DB
	 * 
	 * @param commuteNoTaxLimit
	 */
	void add(CommuteNoTaxLimit commuteNoTaxLimit);

	/**
	 * update CommuteNoTaxLimit to db
	 * 
	 * @param commuteNoTaxLimit
	 */
	void update(CommuteNoTaxLimit commuteNoTaxLimit);

	/**
	 * delete CommuteNoTaxLimit to db
	 * 
	 * @param companyCode
	 */
	void delele(String companyCode, String commuNoTaxLimitCode);

}
