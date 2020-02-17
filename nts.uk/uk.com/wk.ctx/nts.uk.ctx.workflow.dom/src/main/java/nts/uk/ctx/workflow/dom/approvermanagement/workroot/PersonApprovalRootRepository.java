package nts.uk.ctx.workflow.dom.approvermanagement.workroot;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

/**
 * 
 * @author hoatt
 *
 */
public interface PersonApprovalRootRepository {

	/**
	 * get all Person Approval Root
	 * 
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	List<PersonApprovalRoot> getPsRootStart(String companyId, String employeeId, int sysAtr,
			List<Integer> lstAppType, List<Integer> lstNoticeID, List<String> lstEventID);

	/**
	 * delete Person Approval Root
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param  historyId
	 */
	void deletePsApprovalRoot(String companyId, String approvalId, String employeeId, String historyId);

	/**
	 * add Person Approval Root
	 * 
	 * @param psAppRoot
	 */
	void addPsApprovalRoot(PersonApprovalRoot psAppRoot);
	/**
	 * add All Person Approval Root
	 * 
	 * @param psAppRoot
	 */
	void addAllPsApprovalRoot(List<PersonApprovalRoot> psAppRoot);

	/**
	 * update Person Approval Root
	 * 
	 * @param psAppRoot
	 */
	void updatePsApprovalRoot(PersonApprovalRoot psAppRoot);
	/**
	 * update All Person Approval Root
	 * 
	 * @param psAppRoot
	 */
	void updateAllPsApprovalRoot(List<PersonApprovalRoot> psAppRoot);

	/**
	 * get Person Approval Root By End date
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param endDate
	 * @param applicationType
	 * @return
	 */
	List<PersonApprovalRoot> getPsApprovalRootByEdate(String companyId, String employeeId, GeneralDate endDate,
			Integer applicationType, int employmentRootAtr, String id);

	/**
	 * get PsApprovalRoot
	 * 
	 * @param companyId
	 * @param approvalId
	 * @param employeeId
	 * @param historyId
	 * @return
	 */
	Optional<PersonApprovalRoot> getPsApprovalRoot(String companyId, String approvalId, String employeeId,
			String historyId);

	/**
	 * 個人別承認ルート
	 * @param companyID 会社ID
	 * @param employeeID 社員ID
	 * @param date 基準日
	 * @param rootAtr 承認ルート区分
	 * @param targetType 対象申請
	 * @param sysAtr システム区分
	 * @return
	 */
	Optional<PersonApprovalRoot> findByBaseDate(String companyID, String employeeID, GeneralDate date, EmploymentRootAtr rootAtr,
			String targetType, int sysAtr);

	/**
	 * 個人別承認ルート common
	 * @param companyID 会社ID
	 * @param employeeID 社員ID
	 * @param baseDate 基準日
	 * @param sysAtr システム区分
	 * @return
	 */
	Optional<PersonApprovalRoot> findByBaseDateOfCommon(String companyID, String employeeID, GeneralDate baseDate, int sysAtr);
	/**
	 * ドメインモデル「個人別就業承認ルート」を取得する(lấy dữ liệu domain「個人別就業承認ルート」)
	 * @param companyId
	 * @param baseDate ・期間．開始日 <= 基準日  ・期間．終了日 >= 基準日
	 * @return
	 */
	List<PersonApprovalRoot> findAllByBaseDate(String companyId, GeneralDate baseDate, int sysAtr);
	/**
	 * get Person Approval Root By type
	 * @param companyId
	 * @param employeeId
	 * @param applicationType
	 * @param employmentRootAtr
	 * @return
	 */
	List<PersonApprovalRoot> getPsApprovalRootByType(String companyId, String employeeId, Integer applicationType,
			int employmentRootAtr, String id);
	/**
	 * getPsAppRootLastest
	 * @param companyId
	 * @param employeeId
	 * @param endDate
	 * @return
	 */
	List<PersonApprovalRoot> getPsAppRootLastest(String companyId, String employeeId,GeneralDate endDate);
	
	List<PersonApprovalRoot> getPsAppRoot(String companyID, GeneralDate date, 
			Integer employmentRootAtr, Integer confirmRootAtr);

	/**
	 * getNewestCommonPsAppRoot
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	Optional<PersonApprovalRoot> getNewestCommonPsAppRoot(String companyId, String employeeId);

	/**
	 * getNewestMonthlyPsAppRoot
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	Optional<PersonApprovalRoot> getNewestMonthlyPsAppRoot(String companyId, String employeeId);

	/**
	 * getPastHistory
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	List<PersonApprovalRoot> getPastHistory(String companyId, String employeeId);

	/**
	 * getPsApprovalRootBySdate
	 * @param companyId
	 * @param employeeId
	 * @param startDate
	 * @return
	 */
	List<PersonApprovalRoot> getPsApprovalRootBySdate(String companyId, String employeeId, GeneralDate startDate);
	
	Optional<PersonApprovalRoot> getHistLastestCom(String companyId, String employeeId);
	
	Optional<PersonApprovalRoot> getHistLastestPri(String companyId, String employeeId, int employmentRootAtr, Integer appType, String id);
	//get by endDate
	List<PersonApprovalRoot> getByEndDate(String companyId, String employeeId, int sysAtr, GeneralDate endDate);
	
	List<PersonApprovalRoot> findByBaseDateJinji(String companyId, GeneralDate baseDate,
			List<Integer> lstNoticeID, List<String> lstEventID);
}
