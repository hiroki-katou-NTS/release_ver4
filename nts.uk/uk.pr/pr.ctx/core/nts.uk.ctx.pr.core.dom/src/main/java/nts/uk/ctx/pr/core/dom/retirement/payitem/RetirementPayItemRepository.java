package nts.uk.ctx.pr.core.dom.retirement.payitem;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.core.dom.company.CompanyCode;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface RetirementPayItemRepository {
	
	/**
	 * find retirement payment item by key
	 * @param companyCode company code
	 * @param category indicator category
	 * @param itemCode retirement payment item code
	 * @return retirement payment item if exist
	 */
	Optional<RetirementPayItem> findByKey(String companyCode, IndicatorCategory category, String itemCode);
	
	/**
	 * find retirement payment item by company code
	 * @param companyCode company code
	 * @return list retirement payment item by company code
	 */
	List<RetirementPayItem> findByCompanyCode(String companyCode);
	
	/**
	 * update single retirement payment item
	 * @param payItem item to update
	 */
	void update(RetirementPayItem payItem);
}
