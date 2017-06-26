package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import java.util.List;

public interface ClassificationAllotSettingHeaderRepository {
	// List<ClassificationAllotSettingHeader> findbyHistoryId(String
	// companyCode, String historyId );
	// find all, return list
	List<ClassificationAllotSettingHeader> findAll(String companyCode);

	void update(ClassificationAllotSettingHeader classificationAllotSettingHeader);

}
