//package nts.uk.ctx.bs.employee.dom.department.master;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * 
// * @author HungTT
// *
// */
//public interface DepartmentInformationRepository {
//
//	public List<DepartmentInformation> getAllDepartmentByCompany(String companyId, String depHistId);
//	
//	public List<DepartmentInformation> getAllActiveDepartmentByCompany(String companyId, String depHistId);
//	
//	public Optional<DepartmentInformation> getDepartmentByKey(String companyId, String depHistId, String depId);
//	
//	public Optional<DepartmentInformation> getDeletedDepartmentByCode(String companyId, String depHistId, String depCode);
//	
//	public Optional<DepartmentInformation> getActiveDepartmentByCode(String companyId, String depHistId, String depCode);
//	
//	public List<DepartmentInformation> getActiveDepartmentByDepIds(String companyId, String depHistId, List<String> listDepartmentId);
//
//	List<DepartmentInformation> getAllDepartmentByDepIds(String companyId, String depHistId, List<String> listDepartmentId);
//	
//	public void addDepartment(DepartmentInformation department);
//	
//	public void addDepartments(List<DepartmentInformation> listDepartment);
//	
//	public void updateDepartment(DepartmentInformation department);
//
//	public void deleteDepartmentInforOfHistory(String companyId, String depHistId);
//	
//	public void deleteDepartmentInfor(String companyId, String depHistId, String depId);
//
//}
