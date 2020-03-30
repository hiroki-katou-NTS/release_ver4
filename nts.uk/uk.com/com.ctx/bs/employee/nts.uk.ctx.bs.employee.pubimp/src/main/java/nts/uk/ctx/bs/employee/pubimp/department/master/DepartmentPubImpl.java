//package nts.uk.ctx.bs.employee.pubimp.department.master;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.arc.time.GeneralDate;
//import nts.uk.ctx.bs.employee.dom.department.master.service.DepartmentExportSerivce;
//import nts.uk.ctx.bs.employee.pub.department.master.DepartmentExport;
//import nts.uk.ctx.bs.employee.pub.department.master.DepartmentInforExport;
//import nts.uk.ctx.bs.employee.pub.department.master.DepartmentPub;
//
//@Stateless
//public class DepartmentPubImpl implements DepartmentPub {
//
//	@Inject
//	private DepartmentExportSerivce depExpService;
//
//	@Override
//	public List<DepartmentInforExport> getDepartmentInforByDepIds(String companyId, List<String> listDepartmentId,
//			GeneralDate baseDate) {
//		return depExpService.getDepartmentInforFromDepIds(companyId, listDepartmentId, baseDate).stream()
//				.map(i -> new DepartmentInforExport(i.getDepartmentId(), i.getHierarchyCode(), i.getDepartmentCode(),
//						i.getDepartmentName(), i.getDisplayName(), i.getGenericName(), i.getExternalCode()))
//				.collect(Collectors.toList());
//	}
//
//	@Override
//	public List<DepartmentInforExport> getAllActiveDepartment(String companyId, GeneralDate baseDate) {
//		return depExpService.getAllActiveDepartment(companyId, baseDate).stream()
//				.map(i -> new DepartmentInforExport(i.getDepartmentId(), i.getHierarchyCode().v(),
//						i.getDepartmentCode().v(), i.getDepartmentName().v(), i.getDepartmentDisplayName().v(),
//						i.getDepartmentGeneric().v(),
//						i.getDepartmentExternalCode().isPresent() ? i.getDepartmentExternalCode().get().v() : null))
//				.collect(Collectors.toList());
//	}
//
//	@Override
//	public List<DepartmentInforExport> getPastDepartmentInfor(String companyId, String depHistId,
//			List<String> listDepartmentId) {
//		return depExpService.getPastDepartmentInfor(companyId, depHistId, listDepartmentId).stream()
//				.map(i -> new DepartmentInforExport(i.getDepartmentId(), i.getHierarchyCode(), i.getDepartmentCode(),
//						i.getDepartmentName(), i.getDisplayName(), i.getGenericName(), i.getExternalCode()))
//				.collect(Collectors.toList());
//	}
//	
//    // for salary qmm016, 017
//	@Override
//	public List<DepartmentExport> getDepartmentByCompanyIdAndBaseDate(String companyId, GeneralDate baseDate) {
//		return depExpService.getAllActiveDepartment(companyId, baseDate).stream().map(item -> {
//			return DepartmentExport.builder().companyId(item.getCompanyId()).depHistoryId(item.getDepartmentHistoryId())
//					.departmentId(item.getDepartmentId()).departmentCode(item.getDepartmentCode().v())
//					.departmentName(item.getDepartmentName().v()).depDisplayName(item.getDepartmentDisplayName().v())
//					.depGenericName(item.getDepartmentGeneric().v())
//					.outsideDepCode(item.getDepartmentExternalCode().map(ec -> ec.v())).build();
//		}).collect(Collectors.toList());
//	}
//
//	@Override
//	public List<String> getAllChildDepartmentId(String companyId, GeneralDate baseDate, String parentDepartmentId) {
//		return depExpService.getAllChildDepartmentId(companyId, baseDate, parentDepartmentId);
//	}
//
//	@Override
//	public List<String> getDepartmentIdAndChildren(String companyId, GeneralDate baseDate, String departmentId) {
//		return depExpService.getDepartmentIdAndChildren(companyId, baseDate, departmentId);
//	}
//
//}
