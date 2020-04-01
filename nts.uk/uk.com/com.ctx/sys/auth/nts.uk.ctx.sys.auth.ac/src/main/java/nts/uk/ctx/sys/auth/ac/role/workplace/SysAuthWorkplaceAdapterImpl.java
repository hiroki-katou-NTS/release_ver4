//package nts.uk.ctx.sys.auth.ac.role.workplace;
//
//import nts.arc.time.GeneralDate;
//import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
//import nts.uk.ctx.bs.employee.pub.workplace.master.WorkplacePub;
//import nts.uk.ctx.sys.auth.dom.adapter.workplace.AffWorkplaceHistImport;
//import nts.uk.ctx.sys.auth.dom.adapter.workplace.SysAuthWorkplaceAdapter;
//import nts.uk.ctx.sys.auth.dom.adapter.workplace.WorkplaceInfoImport;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Stateless
//public class SysAuthWorkplaceAdapterImpl implements SysAuthWorkplaceAdapter {
//
//    @Inject
//    private WorkplacePub workplacePub;
//
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
//
//    @Override
//    public Optional<AffWorkplaceHistImport> findWkpByBaseDateAndEmpId(GeneralDate baseDate, String employeeId) {
//        AffWorkplaceHistImport affWorkplaceHistImport = new AffWorkplaceHistImport();
//        Optional<SWkpHistExport> optSWkpHistExport = workplacePub.findBySid(employeeId, baseDate);
//        if (optSWkpHistExport.isPresent() && optSWkpHistExport.get().getWorkplaceId() != null) {
//            affWorkplaceHistImport.setWorkplaceId(optSWkpHistExport.get().getWorkplaceId());
//            return Optional.of(affWorkplaceHistImport);
//        }
//        return Optional.empty();
//    }
//
//}
