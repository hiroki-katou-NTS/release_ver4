package nts.uk.ctx.bs.employee.pubimp.workplace.config.info;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.pub.workplace.config.info.WorkPlaceConfigInfoExport;
import nts.uk.ctx.bs.employee.pub.workplace.config.info.WorkPlaceConfigInfoPub;
import nts.uk.ctx.bs.employee.pub.workplace.config.info.WorkplaceHierarchyExport;

@Stateless
public class WorkplaceConfigInfoPubImp implements WorkPlaceConfigInfoPub {

	@Inject
	private WorkplaceConfigInfoRepository wpConfigInfoRepo;

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

}
