package nts.uk.ctx.pr.core.dom.rule.employment.layout.category;

import java.util.List;
import java.util.Optional;


public interface LayoutMasterCategoryRepository {

	/**
	 * add LayoutMasterCategory
	 * 
	 * @param companyCode
	 * @param layoutCode
	 * @param startYm
	 */
	void add(LayoutMasterCategory layoutMasterCategory);
	/**
	 * add list categories
	 * @param categories
	 */
	void add(List<LayoutMasterCategory> categories);
	/**
	 * update LayoutMasterCategory
	 * 
	 * @param companyCode
	 * @param stmtCode
	 * @param startYm
	 * @param categoryAtr
	 */
	void update(LayoutMasterCategory layoutMasterCategory);
	/**
	 * update list categories
	 * @param categories
	 */
	void update(List<LayoutMasterCategory> categories);
	
	/**
	 * delete a LayoutMasterCategory
	 * 
	 * @param companyCode
	 * @param stmtCode
	 * @param startYm
	 * @param categoryAtr
	 */
	void remove(String companyCode, String stmtCode, String historyId, int categoryAtr);
/**
 * 
 * @param companyCode
 * @param stmtCode
 * @param startYm
 */
	void removeAllCategory(List<LayoutMasterCategory> categories);
	/**
	 * get Categories
	 * 
	 * @param companyCode
	 * @param stmtCode
	 * @param startYm
	 * @return List Categories
	 */
	List<LayoutMasterCategory> getCategories(String companyCode, String stmtCode, int startYm);
	List<LayoutMasterCategory> getCategories(String companyCode, String stmtCode,  String historyId);
	
	List<LayoutMasterCategory> getCategories(String historyId);
	List<LayoutMasterCategory> getCategoriesBefore(String companyCode, String stmtCode);

	List<LayoutMasterCategory> getCategoriesBefore(String companyCode, String stmtCode, int endYm);
	List<LayoutMasterCategory> getCategoriesBefore(String companyCode, String stmtCode, String historyId);
	
	/**
	 * Find layout master category
	 * @param companyCode
	 * @param stmtCode
	 * @param startYearMonth
	 * @param categoryAtr
	 * @return
	 */
	Optional<LayoutMasterCategory> find(String companyCode, String stmtCode, String historyId, int categoryAtr);
}
