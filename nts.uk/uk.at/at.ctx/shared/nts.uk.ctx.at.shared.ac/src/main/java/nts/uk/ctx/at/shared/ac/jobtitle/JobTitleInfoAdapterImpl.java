package nts.uk.ctx.at.shared.ac.jobtitle;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.adapter.jobtitle.JobTitleInfoAdapter;
import nts.uk.ctx.at.shared.dom.adapter.jobtitle.JobTitleInfoImport;
import nts.uk.ctx.at.shared.dom.adapter.jobtitle.SequenceMasterImport;
import nts.uk.ctx.bs.employee.pub.jobtitle.SyJobTitlePub;

@Stateless
public class JobTitleInfoAdapterImpl implements JobTitleInfoAdapter{

	@Inject
	private SyJobTitlePub pub;
	
	@Override
	public List<JobTitleInfoImport> findByJobIds(String companyId, List<String> jobIds,
				String historyId) {
					return this.pub.findByJobIds(companyId, jobIds, historyId)
							.stream().map(x ->{ 
							return new JobTitleInfoImport(x.getCompanyId(), x.getJobTitleHistoryId(), x.isManager(), 
									x.getJobTitleId(), x.getJobTitleCode(), x.getJobTitleName(), x.getSequenceCode());})
							.collect(Collectors.toList());
	    }

	@Override
	public List<SequenceMasterImport> findAll(String companyId, String sequenceCode) {
		return this.pub.findAllSequen(companyId, sequenceCode).stream()
				.map(x -> {
			return new SequenceMasterImport(x.getCompanyId(), x.getOrder(), x.getSequenceCode(), x.getSequenceName());
		}).collect(Collectors.toList());
	}
	
}
