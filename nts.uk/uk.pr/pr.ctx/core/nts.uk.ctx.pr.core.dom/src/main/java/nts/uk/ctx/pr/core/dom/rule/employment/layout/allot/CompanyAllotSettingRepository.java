package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import java.util.List;
import java.util.Optional;

public interface CompanyAllotSettingRepository {
	//
	Optional<CompanyAllotSetting> find(String companyCode);
	//find all, return list
	List<CompanyAllotSetting> findAll(String companyCode);
	
	//find Item with max Date 
	Optional<CompanyAllotSetting> findMax(String companyCode, int endDate);
	
	//find max end_date
	Optional<CompanyAllotSetting> maxStart(String companyCode);
	
	//Find previous History 
	Optional<CompanyAllotSetting> getPreviousHistory(String companyCode, int endDate);
	/**
	 * find all company allot Setting by company code, start date
	 * 
	 * @return Company Allot Setting master
	 */
	//List<CompanyAllotSetting> getCompanyAllotSetting(String companyCode);
	
	/**
	 * update company allot Setting
	 * 
	 * @param CompanyAllotSetting
	 */
	void update(CompanyAllotSetting companyAllotSetting);
	
	/**
	 * insert into  company allot Setting
	 * 
	 * @param CompanyAllotSetting
	 */
	void insert(CompanyAllotSetting companyAllotSetting);
	
	
	/**
	 * Delete company allot
	 * 
	 * 
	 */
	void remove(String historyId);
}
