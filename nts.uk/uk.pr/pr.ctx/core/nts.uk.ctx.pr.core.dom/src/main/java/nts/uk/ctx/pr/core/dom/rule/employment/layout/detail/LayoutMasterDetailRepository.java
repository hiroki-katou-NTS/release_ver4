package nts.uk.ctx.pr.core.dom.rule.employment.layout.detail;

import java.util.List;
import java.util.Optional;

public interface LayoutMasterDetailRepository {

	/**
	 * add a layout master detail
	 * 
	 * @param companyCode
	 * @param startYm
	 * @param stmtCode
	 */
	void add(LayoutMasterDetail layoutMasterDetail);
	
	/**
	 * add list details
	 * @param details
	 */
	void add(List<LayoutMasterDetail> details);

	/**
	 * update a layout master detail
	 */
	void update(LayoutMasterDetail layoutMasterDetail);

	/**
	 * update list details
	 * @param details
	 */
	void update(List<LayoutMasterDetail> details);
	
	/**
	 * delete a layout master detail
	 */
	void remove(String companyCode
			, String layoutCode
			, String historyId
			, int categoryAtr
			, String itemCode);

	void remove(List<LayoutMasterDetail> details);
	
	/**
	 * get Detail
	 * 
	 * @param companyCode
	 * @param layout code
	 * @param start YM
	 * @return category type
	 */
	List<LayoutMasterDetail> getDetails(
			String companyCd, 
			String stmtCd, 
			int startYm);

	List<LayoutMasterDetail> getDetails(String historyId);
	
	List<LayoutMasterDetail> getDetailsBefore(
			String companyCd, 
			String stmtCd, 
			int endYm);
	List<LayoutMasterDetail> getDetailsBefore(
			String companyCd, 
			String stmtCd, 
			String historyId);
	List<LayoutMasterDetail> getDetailsBefore(
			String companyCd, 
			String stmtCd);
	
	List<LayoutMasterDetail> getDetailsByCategory(
			String companyCd, 
			String stmtCd, 
			int startYm,
			int categoryAtr);
	List<LayoutMasterDetail> getDetailsByCategory(
			String historyId,
			int categoryAtr);
	

	List<LayoutMasterDetail> getDetailsByLine(
			String autoLineId);
	
	/**
	 * get detail
	 * 
	 * @param companyCode
	 * @param layoutCode
	 * @param startYM
	 * @param categoryAtr
	 * @param calculationMethod
	 * @param item code
	 * @return
	 */
	Optional<LayoutMasterDetail> getDetail(
			String companyCd, 
			String stmtCd, 
			int startYm, 
			int categoryAtr,
			String itemCd);
	
	/**
	 * get detail
	 * 
	 * @param companyCode
	 * @param layoutCode
	 * @param startYearMonth
	 * @param categoryAttribute
	 * @param calculationMethod
	 * @param item code
	 * @return
	 */
	List<LayoutMasterDetail> getDetailsWithSumScopeAtr(String companyCode, String stmtCode, int startYearMonth, int categoryAttribute, int sumScopeAtr);
	
	/**
	 * find all
	 * 
	 * @param companyCode
	 * @param layout code
	 * @param start YM
	 * @return category type
	 */
	List<LayoutMasterDetail> findAll(
			String companyCd, 
			String stmtCd, 
			int startYm);
}
