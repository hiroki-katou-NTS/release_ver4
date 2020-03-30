//package nts.uk.ctx.bs.employee.pub.workplace.master;
//
//import java.util.List;
//import java.util.Optional;
//
//import nts.arc.time.GeneralDate;
//import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
//
//public interface WorkplacePub {
//
//	/**
//	 * [No.559]運用している職場の情報をすべて取得する
//	 * 
//	 * @param companyId
//	 * @param baseDate
//	 * @return
//	 */
//	public List<WorkplaceInforExport> getAllActiveWorkplaceInfor(String companyId, GeneralDate baseDate);
//
//	/**
//	 * [No.560]職場IDから職場の情報をすべて取得する
//	 * 
//	 * @param companyId
//	 * @param listWorkplaceId
//	 * @param baseDate
//	 * @return
//	 */
//	public List<WorkplaceInforExport> getWorkplaceInforByWkpIds(String companyId, List<String> listWorkplaceId,
//			GeneralDate baseDate);
//
//	/**
//	 * [No.561]過去の職場の情報を取得する
//	 * 
//	 * @param companyId
//	 * @param historyId
//	 * @param listWorkplaceId
//	 * @return
//	 */
//	public List<WorkplaceInforExport> getPastWorkplaceInfor(String companyId, String historyId,
//			List<String> listWorkplaceId);
//
//	/**
//	 * [No.567]職場の下位職場を取得する
//	 *
//	 * @param companyId
//	 * @param baseDate
//	 * @param parentWorkplaceId
//	 * @return
//	 */
//	public List<String> getAllChildrenOfWorkplaceId(String companyId, GeneralDate baseDate, String parentWorkplaceId);
//
//	/**
//	 * [No.573]職場の下位職場を基準職場を含めて取得する
//	 *
//	 * @param companyId
//	 * @param baseDate
//	 * @param workplaceId
//	 * @return
//	 */
//	public List<String> getWorkplaceIdAndChildren(String companyId, GeneralDate baseDate, String workplaceId);
//
//	/**
//	 * RequestList No.30
//	 * @param employeeId
//	 * @param baseDate
//	 * @return
//	 */
//	Optional<SWkpHistExport> findBySid(String employeeId, GeneralDate baseDate);
//	/**
//	 * [No.560]職場IDから職場の情報をすべて取得する_HoaTT
//	 * Doi ung su dung table cu
//	 * @param companyId
//	 * @param listWorkplaceId
//	 * @param baseDate
//	 * @return
//	 */
////	public List<WorkplaceInforExport> getWkpInforByWkpIds_OLD(String companyId, List<String> listWkpId,	GeneralDate baseDate);
//}
