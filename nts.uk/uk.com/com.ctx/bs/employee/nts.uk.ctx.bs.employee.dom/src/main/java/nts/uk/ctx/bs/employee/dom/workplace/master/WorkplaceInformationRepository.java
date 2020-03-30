//package nts.uk.ctx.bs.employee.dom.workplace.master;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * 
// * @author HungTT
// *
// */
//public interface WorkplaceInformationRepository {
//
//	public List<WorkplaceInformation> getAllWorkplaceByCompany(String companyId, String wkpHistId);
//	
//	public List<WorkplaceInformation> getAllActiveWorkplaceByCompany(String companyId, String wkpHistId);
//	
//	public Optional<WorkplaceInformation> getWorkplaceByKey(String companyId, String wkpHistId, String wkpId);
//	
//	public Optional<WorkplaceInformation> getDeletedWorkplaceByCode(String companyId, String wkpHistId, String wkpCode);
//	
//	public Optional<WorkplaceInformation> getActiveWorkplaceByCode(String companyId, String wkpHistId, String wkpCode);
//	
//	public List<WorkplaceInformation> getActiveWorkplaceByWkpIds(String companyId, String wkpHistId, List<String> listWorkplaceId);
//
//	List<WorkplaceInformation> getAllWorkplaceByWkpIds(String companyId, String wkpHistId, List<String> listWorkplaceId);
//	
//	public void addWorkplace(WorkplaceInformation workplace);
//	
//	public void addWorkplaces(List<WorkplaceInformation> listWorkplace);
//	
//	public void updateWorkplace(WorkplaceInformation workplace);
//	
//	public void deleteWorkplaceInforOfHistory(String companyId, String wkpHistId);
//	
//	public void deleteWorkplaceInfor(String companyId, String wkpHistId, String wkpId);
//
//}
