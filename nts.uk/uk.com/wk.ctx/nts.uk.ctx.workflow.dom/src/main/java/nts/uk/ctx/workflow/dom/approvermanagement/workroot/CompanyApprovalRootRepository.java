package nts.uk.ctx.workflow.dom.approvermanagement.workroot;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface CompanyApprovalRootRepository {

	/**
	 * get All Company Approval Root
	 * 
	 * @param companyId
	 * @return
	 */
	List<CompanyApprovalRoot> getAllComApprovalRoot(String companyId);

	/**
	 * get ComApprovalRoot
	 * 
	 * @param companyId
	 * @param approvalId
	 * @param historyId
	 * @return
	 */
	Optional<CompanyApprovalRoot> getComApprovalRoot(String companyId, String approvalId, String historyId);

	/** 
	 * get Company Approval Root By End date
	 * 
	 * @param companyId
	 * @param endDate
	 * @param applicationType
	 * @param employmentRootAtr
	 * @return
	 */
	List<CompanyApprovalRoot> getComApprovalRootByEdate(String companyId, GeneralDate endDate, Integer applicationType, int employmentRootAtr);

	/**
	 * add Company Approval Root
	 * 
	 * @param comAppRoot
	 */
	void addComApprovalRoot(CompanyApprovalRoot comAppRoot);
	/**
	 * add All Company Approval Root
	 * 
	 * @param comAppRoot
	 */
	void addAllComApprovalRoot(List<CompanyApprovalRoot> comAppRoot);

	/**
	 * update Company Approval Root
	 * 
	 * @param comAppRoot
	 */
	void updateComApprovalRoot(CompanyApprovalRoot comAppRoot);
	/**
	 * update All Company Approval Root
	 * 
	 * @param comAppRoot
	 */
	void updateAllComApprovalRoot(List<CompanyApprovalRoot> comAppRoot);

	/**
	 * delete Company Approval Root
	 * 
	 * @param companyId
	 * @param approvalId
	 * @param historyId
	 */
	void deleteComApprovalRoot(String companyId, String approvalId, String historyId);

	/**
	 * ドメインモデル「会社別就業承認ルート」を取得する 
	 * @param companyID
	 * @param date
	 * @param appType
	 * @param rootAt
	 * @return
	 */
	Optional<CompanyApprovalRoot> findByBaseDate(String companyID, GeneralDate date, ApplicationType appType, EmploymentRootAtr rootAt);

	/**
	 * ドメインモデル「会社別就業承認ルート」を取得する(共通）
	 * @param companyID
	 * @param date
	 * @param appType
	 * @return
	 */
	Optional<CompanyApprovalRoot> findByBaseDateOfCommon(String companyID, GeneralDate date);
	
	/**
	 * ドメインモデル「会社別就業承認ルート」を取得する
	 * 
	 * @param cid
	 * @param baseDate
	 * @return
	 */
	List<CompanyApprovalRoot> findByBaseDate(String cid, GeneralDate baseDate);
	/**
	 * get Company Approval Root By type
	 * @param companyId
	 * @param applicationType
	 * @param employmentRootAtr
	 * @return
	 */
	List<CompanyApprovalRoot> getComApprovalRootByType(String companyId, Integer applicationType, int employmentRootAtr);
	
	/**
	 * getComAppRootLast
	 * @param companyID
	 * @param endDate
	 * @return
	 */
	List<CompanyApprovalRoot> getComAppRootLast(String companyID,GeneralDate endDate);
	
	List<CompanyApprovalRoot> getComAppRoot(String companyID, GeneralDate date, 
			Integer employmentRootAtr, Integer confirmRootAtr);
	
	List<CompanyApprovalRoot> findEmpByConfirm(String companyID, ConfirmationRootType confirmType, GeneralDate date);
}
