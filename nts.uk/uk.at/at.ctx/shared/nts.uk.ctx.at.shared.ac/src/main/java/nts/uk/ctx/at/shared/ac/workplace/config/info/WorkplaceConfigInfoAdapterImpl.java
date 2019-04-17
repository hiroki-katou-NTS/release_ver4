package nts.uk.ctx.at.shared.ac.workplace.config.info;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.adapter.workplace.config.info.WorkPlaceConfigInfoImport;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.info.WorkplaceConfigInfoAdapter;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.info.WorkplaceHierarchyImport;
import nts.uk.ctx.bs.employee.pub.workplace.config.info.WorkPlaceConfigInfoPub;

@Stateless
public class WorkplaceConfigInfoAdapterImpl implements WorkplaceConfigInfoAdapter {
	@Inject
	private WorkPlaceConfigInfoPub wpConfigInfoPub;

	@Override
	public List<WorkPlaceConfigInfoImport> findByHistoryIdsAndWplIds(String companyId, List<String> historyIds,
			List<String> workplaceIds) {
		return this.wpConfigInfoPub.findByHistoryIdsAndWplIds(companyId, historyIds, workplaceIds).stream().map(x -> {
			List<WorkplaceHierarchyImport> lstWkpHierarchy = x.getLstWkpHierarchy().stream()
					.map(hi -> new WorkplaceHierarchyImport(hi.getWorkplaceId(), hi.getHierarchyCode()))
					.collect(Collectors.toList());
			return new WorkPlaceConfigInfoImport(x.getCompanyId(), x.getHistoryId(), lstWkpHierarchy);
		}).collect(Collectors.toList());
	}

}
