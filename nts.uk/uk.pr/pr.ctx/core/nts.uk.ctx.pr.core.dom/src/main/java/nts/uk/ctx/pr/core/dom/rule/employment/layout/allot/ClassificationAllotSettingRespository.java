package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import java.util.List;
import java.util.Optional;

public interface ClassificationAllotSettingRespository {
	Optional<ClassificationAllotSetting> find(String companyCode, String historyId , String classificationCode);
	//find all, return list
	List<ClassificationAllotSetting> findAll(String companyCode);
	
	List<ClassificationAllotSetting> findbyHistoryId(String companyCode, String historyId);
 
									

}
