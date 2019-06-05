package nts.uk.ctx.at.shared.dom.adapter.jobtitle;

import java.util.List;


public interface JobTitleInfoAdapter {
	List<JobTitleInfoImport> findByJobIds(String companyId, List<String> jobIds, String historyId);
	
	List<SequenceMasterImport> findAll(String companyId, String sequenceCode);
}
