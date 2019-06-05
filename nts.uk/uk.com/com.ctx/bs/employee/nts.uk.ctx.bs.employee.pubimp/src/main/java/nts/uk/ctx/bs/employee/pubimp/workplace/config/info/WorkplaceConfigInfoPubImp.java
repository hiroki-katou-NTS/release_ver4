package nts.uk.ctx.bs.employee.pubimp.workplace.config.info;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.pub.workplace.config.info.JobTitleExport;
import nts.uk.ctx.bs.employee.pub.workplace.config.info.JobTitleHistoryExport;
import nts.uk.ctx.bs.employee.pub.workplace.config.info.WorkPlaceConfigInfoExport;
import nts.uk.ctx.bs.employee.pub.workplace.config.info.WorkPlaceConfigInfoPub;
import nts.uk.ctx.bs.employee.pub.workplace.config.info.WorkplaceHierarchyExport;

@Stateless
public class WorkplaceConfigInfoPubImp implements WorkPlaceConfigInfoPub {

	@Inject
	private WorkplaceConfigInfoRepository wpConfigInfoRepo;
	
	@Inject
	private JobTitleRepository repo;

	@Override
	public List<WorkPlaceConfigInfoExport> findByHistoryIdsAndWplIds(String companyId, List<String> historyIds,
			List<String> workplaceIds) {
		return this.wpConfigInfoRepo.findByHistoryIdsAndWplIds(companyId, historyIds, workplaceIds).stream().map(x -> {
			List<WorkplaceHierarchyExport> lstWkpHierarchy = x.getLstWkpHierarchy().stream()
					.map(hi -> new WorkplaceHierarchyExport(hi.getWorkplaceId(), hi.getHierarchyCode().v()))
					.collect(Collectors.toList());
			return new WorkPlaceConfigInfoExport(x.getCompanyId(), x.getHistoryId(), lstWkpHierarchy);
		}).collect(Collectors.toList());
	}

	@Override
	public List<JobTitleExport> findAllById(String companyId, List<String> positionIds, GeneralDate baseDate) {
		return this.repo.findAllById(companyId, positionIds, baseDate).stream()
				.map(x -> { 
					List<JobTitleHistoryExport> jobTitleHistories = x.getJobTitleHistories().stream()
							.map(item -> new JobTitleHistoryExport(item.identifier(), item.span()))
							.collect(Collectors.toList());
					return new JobTitleExport(x.getCompanyId().toString(), x.getJobTitleId(), jobTitleHistories);})
				.collect(Collectors.toList());
	}

}
