package nts.uk.query.ac.workplace;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.employee.workplace.export.WorkplaceExportPub;
//import nts.uk.ctx.bs.employee.pub.workplace.master.WorkplacePub;
import nts.uk.query.model.workplace.QueryWorkplaceAdapter;
import nts.uk.query.model.workplace.WorkplaceInfoImport;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class QueryWorkplaceAdapterImpl implements QueryWorkplaceAdapter {

//    @Inject
//    private WorkplacePub workplacePub;
    @Inject
    private WorkplaceExportPub wkpPub;

//    @Override
//    public List<WorkplaceInfoImport> getAllActiveWorkplaceInfo(String companyId, GeneralDate baseDate) {
//        return workplacePub.getAllActiveWorkplaceInfor(companyId, baseDate)
//                .stream()
//                .map(w -> new WorkplaceInfoImport(
//                        w.getWorkplaceId(),
//                        w.getHierarchyCode(),
//                        w.getWorkplaceCode(),
//                        w.getWorkplaceName(),
//                        w.getWorkplaceDisplayName(),
//                        w.getWorkplaceGenericName(),
//                        w.getWorkplaceExternalCode()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<WorkplaceInfoImport> getWorkplaceInfoByWkpIds(String companyId, List<String> listWorkplaceId, GeneralDate baseDate) {
//        return workplacePub.getWorkplaceInforByWkpIds(companyId, listWorkplaceId, baseDate)
//                .stream()
//                .map(w -> new WorkplaceInfoImport(
//                        w.getWorkplaceId(),
//                        w.getHierarchyCode(),
//                        w.getWorkplaceCode(),
//                        w.getWorkplaceName(),
//                        w.getWorkplaceDisplayName(),
//                        w.getWorkplaceGenericName(),
//                        w.getWorkplaceExternalCode()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<WorkplaceInfoImport> getPastWorkplaceInfo(String companyId, String historyId, List<String> listWorkplaceId) {
//        return workplacePub.getPastWorkplaceInfor(companyId, historyId, listWorkplaceId)
//                .stream()
//                .map(w -> new WorkplaceInfoImport(
//                        w.getWorkplaceId(),
//                        w.getHierarchyCode(),
//                        w.getWorkplaceCode(),
//                        w.getWorkplaceName(),
//                        w.getWorkplaceDisplayName(),
//                        w.getWorkplaceGenericName(),
//                        w.getWorkplaceExternalCode()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<String> getAllChildrenOfWorkplaceId(String companyId, GeneralDate baseDate, String parentWorkplaceId) {
//        return workplacePub.getAllChildrenOfWorkplaceId(companyId, baseDate, parentWorkplaceId);
//    }
//
//    @Override
//    public List<String> getWorkplaceIdAndChildren(String companyId, GeneralDate baseDate, String workplaceId) {
//        return workplacePub.getWorkplaceIdAndChildren(companyId, baseDate, workplaceId);
//    }

	@Override
	public List<WorkplaceInfoImport> getWkpInfoByWkpIds_OLD(String companyId, List<String> listWorkplaceId,
			GeneralDate baseDate) {
		return wkpPub.getWkpConfigRQ560(companyId, listWorkplaceId, baseDate).stream()
				.map(c-> new WorkplaceInfoImport(c.getWorkplaceId(), "", "", c.getWorkplaceName(), "", "", ""))
				.collect(Collectors.toList());
	}

}
