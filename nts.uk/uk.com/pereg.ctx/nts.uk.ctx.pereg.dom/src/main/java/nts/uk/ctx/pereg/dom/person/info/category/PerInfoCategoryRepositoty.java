package nts.uk.ctx.pereg.dom.person.info.category;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pereg.dom.person.info.daterangeitem.DateRangeItem;

public interface PerInfoCategoryRepositoty {

	List<PersonInfoCategory> getAllPerInfoCategory(String companyId, String contractCd);

	Optional<PersonInfoCategory> getPerInfoCategory(String perInfoCategoryId, String contractCd);

	void addPerInfoCtgRoot(PersonInfoCategory perInfoCtg, String contractCd);

	void addPerInfoCtgWithListCompany(PersonInfoCategory perInfoCtg, String contractCd, List<String> companyIdList);

	void updatePerInfoCtg(PersonInfoCategory perInfoCtg, String contractCd);

	String getPerInfoCtgCodeLastest(String contractCd);

	boolean checkCtgNameIsUnique(String companyId, String newCtgName, String ctgId);

	List<String> getPerInfoCtgIdList(List<String> companyIdList, String categoryCd);
	
	/**
	 * 
	 * @param companyId
	 * @return
	 */
	List<PersonInfoCategory> getAllPerInfoCtg(String companyId);
	
	List<PersonInfoCategory> getAllPerInfoCtgOtherEmp(String companyId );

	void addDateRangeItemRoot(DateRangeItem dateRangeItem);

	void addListDateRangeItem(List<DateRangeItem> dateRangeItems);

	// vinhpx: start
	DateRangeItem getDateRangeItemByCtgId(String perInfoCtgId);
	
	List<PersonInfoCategory> getPerInfoCtgByParentCode(String parentCtgCd, String contractCd);
	
	List<PersonInfoCategory> getPerInfoCtgByParentCdWithOrder(String parentCtgCd, String contractCd, String companyId, boolean isASC);

	List<PersonInfoCategory> getPerInfoCategoryByName(String companyId, String contractCd, String name);
	
	// vinhpx: end
	
	//laitv
	DateRangeItem getDateRangeItemByCategoryId(String perInfoCtgId);
	
	/**
	 * Get category by category code
	 * @param categoryCD
	 * @param companyID
	 * @return
	 */
	Optional<PersonInfoCategory> getPerInfoCategoryByCtgCD(String categoryCD, String companyID);
}
