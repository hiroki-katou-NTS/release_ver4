//package nts.uk.query.ac.department;
//
//import nts.arc.time.GeneralDate;
//import nts.uk.ctx.bs.employee.pub.department.master.DepartmentPub;
//import nts.uk.query.model.department.DepartmentAdapter;
//import nts.uk.query.model.department.DepartmentInfoImport;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Stateless
//public class DepartmentAdapterImpl implements DepartmentAdapter {
//
//    @Inject
//    private DepartmentPub departmentPub;
//
//    @Override
//    public List<DepartmentInfoImport> getAllActiveDepartment(String companyId, GeneralDate baseDate) {
//        return departmentPub.getAllActiveDepartment(companyId, baseDate)
//                .stream().map(d -> new DepartmentInfoImport(
//                        d.getDepartmentId(),
//                        d.getHierarchyCode(),
//                        d.getDepartmentCode(),
//                        d.getDepartmentName(),
//                        d.getDepartmentDisplayName(),
//                        d.getDepartmentGenericName(),
//                        d.getDepartmentExternalCode()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<DepartmentInfoImport> getDepartmentInfoByDepIds(String companyId, List<String> listDepartmentId, GeneralDate baseDate) {
//        return departmentPub.getDepartmentInforByDepIds(companyId, listDepartmentId, baseDate)
//                .stream().map(d -> new DepartmentInfoImport(
//                        d.getDepartmentId(),
//                        d.getHierarchyCode(),
//                        d.getDepartmentCode(),
//                        d.getDepartmentName(),
//                        d.getDepartmentDisplayName(),
//                        d.getDepartmentGenericName(),
//                        d.getDepartmentExternalCode()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<DepartmentInfoImport> getPastDepartmentInfo(String companyId, String depHistId, List<String> listDepartmentId) {
//        return departmentPub.getPastDepartmentInfor(companyId, depHistId, listDepartmentId)
//                .stream().map(d -> new DepartmentInfoImport(
//                        d.getDepartmentId(),
//                        d.getHierarchyCode(),
//                        d.getDepartmentCode(),
//                        d.getDepartmentName(),
//                        d.getDepartmentDisplayName(),
//                        d.getDepartmentGenericName(),
//                        d.getDepartmentExternalCode()))
//                .collect(Collectors.toList());
//    }
//}
