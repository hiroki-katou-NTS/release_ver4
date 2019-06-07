package nts.uk.ctx.at.shared.ac.workplace.export;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.jobtitle.WorkplaceExportAdapter;
import nts.uk.ctx.at.shared.dom.adapter.jobtitle.WorkplaceExportImport;
import nts.uk.ctx.bs.employee.pub.employee.workplace.export.WorkplaceExportPub;

@Stateless
public class WorkplaceExportAdapterImpl implements WorkplaceExportAdapter{

	@Inject
	private WorkplaceExportPub pub;
	
	@Override
	public List<WorkplaceExportImport> getAllWkpConfig(String companyId, List<String> listWkpId, GeneralDate baseDate) {
		return this.pub.getAllWkpConfig(companyId, listWkpId, baseDate).stream()
				.map(x -> new WorkplaceExportImport(x.getWorkplaceId(), x.getHierarchyCd()))
				.collect(Collectors.toList());
	}
}
