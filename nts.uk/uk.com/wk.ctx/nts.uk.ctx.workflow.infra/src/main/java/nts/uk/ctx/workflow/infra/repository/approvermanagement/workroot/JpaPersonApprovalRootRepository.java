package nts.uk.ctx.workflow.infra.repository.approvermanagement.workroot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmationRootType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot.WwfmtPsApprovalRoot;
import nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot.WwfmtPsApprovalRootPK;
/**
 * 
 * @author hoatt
 *
 */
@RequestScoped
public class JpaPersonApprovalRootRepository extends JpaRepository implements PersonApprovalRootRepository{

	 private static final String FIND_ALL = "SELECT c FROM WwfmtPsApprovalRoot c";
	 private static final String FIND_BY_CID = FIND_ALL
			   + " WHERE c.wwfmtPsApprovalRootPK.companyId = :companyId";
	 private static final String FIN_BY_EMP = FIND_ALL
			   + " WHERE c.wwfmtPsApprovalRootPK.companyId = :companyId"
			   + " AND c.wwfmtPsApprovalRootPK.employeeId = :employeeId";
	 private static final String SELECT_PS_APR_BY_ENDATE = FIN_BY_EMP
			   + " AND c.endDate = :endDate" 
			   + " AND c.employmentRootAtr = :employmentRootAtr"
			   + " AND c.applicationType = :applicationType";
	 private static final String SELECT_PS_APR_BY_ENDATE_APP_NULL = FIN_BY_EMP 
			   + " AND c.endDate = :endDate"
			   + " AND c.employmentRootAtr = :employmentRootAtr"
			   + " AND c.applicationType IS NULL";
	 private static final String SELECT_PS_APR_BY_ENDATE_CONFIRM = FIN_BY_EMP 
			   + " AND c.endDate = :endDate"
			   + " AND c.confirmationRootType = :confirmationRootType"
			   + " AND c.employmentRootAtr = :employmentRootAtr";
	 private static final String FIND_BY_BASEDATE = FIN_BY_EMP
			   + " AND c.startDate <= :baseDate"
			   + " AND c.endDate >= :baseDate"
			   + " AND c.employmentRootAtr = :rootAtr" 
			   + " AND c.applicationType = :appType";
	 private static final String FIND_BY_BASEDATE_OF_COM = FIN_BY_EMP
			   + " AND c.startDate <= :baseDate"
			   + " AND c.endDate >= :baseDate"
			   + " AND c.employmentRootAtr = 0";
	 private static final String FIND_ALL_BY_BASEDATE = FIND_ALL + " WHERE c.wwfmtPsApprovalRootPK.companyId = :companyId"
			   + " AND c.startDate <= :baseDate"
			   + " AND c.endDate >= :baseDate"
			   + " ORDER BY c.wwfmtPsApprovalRootPK.employeeId";
	 private static final String FIND_BY_APP_TYPE = FIN_BY_EMP
			   + " AND c.employmentRootAtr = :employmentRootAtr"
			   + " AND c.applicationType = :applicationType"
			   + " ORDER BY c.startDate DESC";
	 private static final String SELECT_PSAPR_BY_APP_NULL = FIN_BY_EMP 
			   + " AND c.employmentRootAtr = :employmentRootAtr"
			   + " AND c.applicationType IS NULL"
			   + " ORDER BY c.startDate DESC";
	 private static final String FIND_BY_CFR_TYPE = FIN_BY_EMP 
			   + " AND c.confirmationRootType = :confirmationRootType"
			   + " AND c.employmentRootAtr = :employmentRootAtr"
			   + " ORDER BY c.startDate DESC";
	 private static final String FIND_PS_APP_LASTEST = FIN_BY_EMP
			 + " AND c.endDate = :endDate";
	 private static final String FIND_BY_DATE_EMP_CONFIRM = FIND_BY_CID 
				+ " AND c.startDate <= :baseDate"
				+ " AND c.endDate >= :baseDate"
				+ " AND c.confirmationRootType = :confirmationRootType"
				+ " AND c.employmentRootAtr = :employmentRootAtr";
	private static final String FIND_BY_DATE_EMP = FIND_BY_CID 
				+ " AND c.startDate <= :baseDate"
				+ " AND c.endDate >= :baseDate"
				+ " AND c.employmentRootAtr = :employmentRootAtr";
	 private static final String FIND_COMMON_PS_APP_LASTEST = FIN_BY_EMP
			 + " AND c.employmentRootAtr = 0 "
			 + " AND c.applicationType IS NULL"
			 + " AND c.startDate = (SELECT MAX(c1.startDate) FROM WwfmtPsApprovalRoot c1 WHERE c1.wwfmtPsApprovalRootPK.companyId = :companyId AND c1.wwfmtPsApprovalRootPK.employeeId = :employeeId AND c1.employmentRootAtr = 0 AND c1.applicationType IS NULL)";
	 private static final String FIND_MONTHLY_PS_APP_LASTEST = FIN_BY_EMP
			 + " AND c.employmentRootAtr = 2"
			 + " AND c.confirmationRootType = 1"
			 + " AND c.startDate = (SELECT MAX(c1.startDate) FROM WwfmtPsApprovalRoot c1 WHERE c1.wwfmtPsApprovalRootPK.companyId = :companyId AND c1.wwfmtPsApprovalRootPK.employeeId = :employeeId AND c1.employmentRootAtr = 2 AND c1.confirmationRootType = 1)";
	 private static final String FIND_PART_HISTORY = FIN_BY_EMP
			 + " AND ((c.employmentRootAtr = 0 AND (c.applicationType IS NULL)) OR (c.employmentRootAtr = 2 AND c.confirmationRootType = 1))"
			 + " ORDER BY c.startDate DESC";
	 private static final String SELECT_PS_APR_BY_STARTDATE = FIN_BY_EMP
			 + " AND c.startDate = :startDate";
	 private static final String FIND_BY_EMP_CONFIRM = FIN_BY_EMP
			 + " AND c.startDate <= :baseDate"
			 + " AND c.endDate >= :baseDate"
			 + " AND c.confirmationRootType = :confirmationRootType"
			 + " AND c.employmentRootAtr = 2";
	 private static final String GET_ALL__MODE_COM = "SELECT c FROM WwfmtPsApprovalRoot c"
			 + " WHERE c.wwfmtPsApprovalRootPK.companyId = :companyId"
			 + " AND c.wwfmtPsApprovalRootPK.employeeId = :employeeId"
			 + " ORDER BY c.startDate DESC";
	 private static final String GET_ALL_MODE_PRI_CM = "SELECT c FROM WwfmtPsApprovalRoot c"
			 + " WHERE c.wwfmtPsApprovalRootPK.companyId = :companyId"
			 + " AND c.wwfmtPsApprovalRootPK.employeeId = :employeeId"
			 + " AND c.employmentRootAtr = 0"
			 + " ORDER BY c.startDate DESC";
	 private static final String GET_ALL_MODE_PRI_AP = "SELECT c FROM WwfmtPsApprovalRoot c"
			 + " WHERE c.wwfmtPsApprovalRootPK.companyId = :companyId"
			 + " AND c.wwfmtPsApprovalRootPK.employeeId = :employeeId"
			 + " AND c.employmentRootAtr = 1"
			 + " AND c.applicationType = :applicationType"
			 + " ORDER BY c.startDate DESC";
	 private static final String GET_ALL_MODE_PRI_CF = "SELECT c FROM WwfmtPsApprovalRoot c"
			 + " WHERE c.wwfmtPsApprovalRootPK.companyId = :companyId"
			 + " AND c.wwfmtPsApprovalRootPK.employeeId = :employeeId"
			 + " AND c.employmentRootAtr = 2"
			 + " AND c.confirmationRootType = :confirmationRootType"
			 + " ORDER BY c.startDate DESC";
	/**
	 * get all Person Approval Root
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	@Override
	public List<PersonApprovalRoot> getAllPsApprovalRoot(String companyId, String employeeId) {
		return this.queryProxy().query(FIN_BY_EMP, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.getList(c->toDomainPsApR(c));
	}
	/**
	 * delete Person Approval Root
	 * @param companyId
	 * @param employeeId
	 * @param historyId
	 */
	@Override
	public void deletePsApprovalRoot(String companyId, String approvalId, String employeeId, String historyId) {
		WwfmtPsApprovalRootPK comPK = new WwfmtPsApprovalRootPK(companyId, approvalId, employeeId, historyId);
		this.commandProxy().remove(WwfmtPsApprovalRoot.class,comPK);
		this.getEntityManager().flush();
	}
	/**
	 * add Person Approval Root
	 * @param psAppRoot
	 */
	@Override
	public void addPsApprovalRoot(PersonApprovalRoot psAppRoot) {
		this.commandProxy().insert(toEntityPsApR(psAppRoot));
	}
	/**
	 * add All Person Approval Root
	 * @param psAppRoot
	 */
	@Override
	public void addAllPsApprovalRoot(List<PersonApprovalRoot> psAppRoot) {
		List<WwfmtPsApprovalRoot> lstEntity = new ArrayList<>();
		for (PersonApprovalRoot ps : psAppRoot) {
			lstEntity.add(toEntityPsApR(ps));
		}
		this.commandProxy().insertAll(lstEntity);
		this.getEntityManager().flush();
	}
	/**
	 * update Person Approval Root
	 * @param psAppRoot
	 */
	@Override
	public void updatePsApprovalRoot(PersonApprovalRoot psAppRoot) {
		WwfmtPsApprovalRoot a = toEntityPsApR(psAppRoot);
		WwfmtPsApprovalRoot x = this.queryProxy().find(a.wwfmtPsApprovalRootPK, WwfmtPsApprovalRoot.class).get();
		x.setStartDate(a.startDate);
		x.setEndDate(a.endDate);
		x.setApplicationType(a.applicationType);
		x.setBranchId(a.branchId);
		x.setAnyItemAppId(a.anyItemAppId);
		x.setConfirmationRootType(a.confirmationRootType);
		x.setEmploymentRootAtr(a.employmentRootAtr);
		this.commandProxy().update(x);
		this.getEntityManager().flush();
	}
	/**
	 * update All Person Approval Root
	 * @param psAppRoot
	 */
	@Override
	public void updateAllPsApprovalRoot(List<PersonApprovalRoot> psAppRoot) {
		List<WwfmtPsApprovalRoot> lstEntity = new ArrayList<>();
		for (PersonApprovalRoot ps : psAppRoot) {
			WwfmtPsApprovalRoot a = toEntityPsApR(ps);
			WwfmtPsApprovalRoot x = this.queryProxy().find(a.wwfmtPsApprovalRootPK, WwfmtPsApprovalRoot.class).get();
			x.setStartDate(a.startDate);
			x.setEndDate(a.endDate);
			x.setApplicationType(a.applicationType);
			x.setBranchId(a.branchId);
			x.setAnyItemAppId(a.anyItemAppId);
			x.setConfirmationRootType(a.confirmationRootType);
			x.setEmploymentRootAtr(a.employmentRootAtr);
			lstEntity.add(x);
		}
		
		this.commandProxy().updateAll(lstEntity);
	}
	/**
	 * get Person Approval Root By End date
	 * @param companyId
	 * @param employeeId
	 * @param endDate
	 * @return
	 */
	@Override
	public List<PersonApprovalRoot> getPsApprovalRootByEdate(String companyId, String employeeId, GeneralDate endDate, Integer applicationType, int employmentRootAtr) {
		//common
		if(employmentRootAtr == 0){
			return this.queryProxy().query(SELECT_PS_APR_BY_ENDATE_APP_NULL, WwfmtPsApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("employeeId", employeeId)
					.setParameter("endDate", endDate)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainPsApR(c));
		}
		if(employmentRootAtr == 2){//confirm
			return this.queryProxy().query(SELECT_PS_APR_BY_ENDATE_CONFIRM, WwfmtPsApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("employeeId", employeeId)
					.setParameter("endDate", endDate)
					.setParameter("confirmationRootType", applicationType)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainPsApR(c));
		}
		//15 app type
		return this.queryProxy().query(SELECT_PS_APR_BY_ENDATE, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.setParameter("endDate", endDate)
				.setParameter("applicationType", applicationType)
				.setParameter("employmentRootAtr", employmentRootAtr)
				.getList(c->toDomainPsApR(c));
	}
	/**
	 * get PsApprovalRoot
	 * @param companyId
	 * @param approvalId
	 * @param employeeId
	 * @param historyId
	 * @return
	 */
	@Override
	public Optional<PersonApprovalRoot> getPsApprovalRoot(String companyId, String approvalId, String employeeId, String historyId) {
		WwfmtPsApprovalRootPK pk = new WwfmtPsApprovalRootPK(companyId, approvalId, employeeId, historyId);
		return this.queryProxy().find(pk, WwfmtPsApprovalRoot.class).map(c->toDomainPsApR(c));
	}
	
	/**
	 * 個人別就業承認ルート」を取得する
	 * 就業ルート区分(申請か、確認か、任意項目か)
	 * @param cid
	 * @param sid
	 * @param baseDate
	 * @param appType
	 */
	@Override
	public Optional<PersonApprovalRoot> findByBaseDate(String companyID, String employeeID, GeneralDate date, ApplicationType appType, EmploymentRootAtr rootAtr) {
		return this.queryProxy().query(FIND_BY_BASEDATE, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyID)
				.setParameter("employeeId", employeeID)
				.setParameter("baseDate", date)
				.setParameter("appType", appType.value)
				.setParameter("rootAtr", rootAtr.value)
				.getSingle(c->toDomainPsApR(c));
	}
	
	/**
	 * 個人別就業承認ルート」を取得する
	 * 就業ルート区分(共通)
	 * @param cid
	 * @param sid
	 * @param baseDate
	 * @param appType
	 */
	@Override
	public Optional<PersonApprovalRoot> findByBaseDateOfCommon(String companyID, String employeeID, GeneralDate baseDate) {
		return this.queryProxy().query(FIND_BY_BASEDATE_OF_COM, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyID)
				.setParameter("employeeId", employeeID)
				.setParameter("baseDate", baseDate)
				.getSingle(c->toDomainPsApR(c));
	}
	
	
	/**
	 * convert entity WwfmtPsApprovalRoot to domain PersonApprovalRoot
	 * @param entity
	 * @return
	 */
	private PersonApprovalRoot toDomainPsApR(WwfmtPsApprovalRoot entity){
		val domain = PersonApprovalRoot.convert(entity.wwfmtPsApprovalRootPK.companyId,
				entity.wwfmtPsApprovalRootPK.approvalId,
				entity.wwfmtPsApprovalRootPK.employeeId,
				entity.wwfmtPsApprovalRootPK.historyId,
				entity.applicationType,
				entity.startDate,
				entity.endDate,
				entity.branchId,
				entity.anyItemAppId,
				entity.confirmationRootType,
				entity.employmentRootAtr);
		return domain;
	}
	/**
	 * convert domain PersonApprovalRoot to entity WwfmtPsApprovalRoot
	 * @param domain
	 * @return
	 */
	private WwfmtPsApprovalRoot toEntityPsApR(PersonApprovalRoot domain){
		val entity = new WwfmtPsApprovalRoot();
		entity.wwfmtPsApprovalRootPK = new WwfmtPsApprovalRootPK(domain.getCompanyId(), domain.getApprovalId(), domain.getEmployeeId(), domain.getEmploymentAppHistoryItems().get(0).getHistoryId());
		entity.startDate = domain.getEmploymentAppHistoryItems().get(0).start();
		entity.endDate = domain.getEmploymentAppHistoryItems().get(0).end();
		entity.applicationType = (domain.getApplicationType() == null ? null : domain.getApplicationType().value);
		entity.branchId = domain.getBranchId();
		entity.anyItemAppId = domain.getAnyItemApplicationId();
		entity.confirmationRootType = (domain.getConfirmationRootType() == null ? null : domain.getConfirmationRootType().value);
		entity.employmentRootAtr = domain.getEmploymentRootAtr().value;
		return entity;
	}
	@Override
	public List<PersonApprovalRoot> findAllByBaseDate(String companyId, GeneralDate baseDate) {
		List<PersonApprovalRoot> data = this.queryProxy().query(FIND_ALL_BY_BASEDATE, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("baseDate", baseDate)
				.getList(c->toDomainPsApR(c));
		return data;
	}
	/**
	 * get Person Approval Root By type
	 * @param companyId
	 * @param employeeId
	 * @param applicationType
	 * @param employmentRootAtr
	 * @return
	 */
	@Override
	public List<PersonApprovalRoot> getPsApprovalRootByType(String companyId, String employeeId,
			Integer applicationType, int employmentRootAtr) {
		//common
		if(employmentRootAtr == 0){
			return this.queryProxy().query(SELECT_PSAPR_BY_APP_NULL, WwfmtPsApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("employeeId", employeeId)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainPsApR(c));
		}
		//confirm
		if(employmentRootAtr == 2){
			return this.queryProxy().query(FIND_BY_CFR_TYPE, WwfmtPsApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("employeeId", employeeId)
					.setParameter("confirmationRootType", applicationType)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainPsApR(c));
		}
		//15 app type
		return this.queryProxy().query(FIND_BY_APP_TYPE, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.setParameter("applicationType", applicationType)
				.setParameter("employmentRootAtr", employmentRootAtr)
				.getList(c->toDomainPsApR(c));
	}
	@Override
	public List<PersonApprovalRoot> getPsAppRootLastest(String companyId, String employeeId, GeneralDate endDate) {
		
		return this.queryProxy().query(FIND_PS_APP_LASTEST,WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.setParameter("endDate", endDate)
				.getList(c -> toDomainPsApR(c));
	}
	@Override
	public List<PersonApprovalRoot> getPsAppRoot(String companyID, GeneralDate date, Integer employmentRootAtr,
			Integer confirmRootAtr) {
		if(confirmRootAtr==null){
			return this.queryProxy().query(FIND_BY_DATE_EMP, WwfmtPsApprovalRoot.class)
					.setParameter("companyId", companyID)
					.setParameter("baseDate", date)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c -> toDomainPsApR(c));
		}
		return this.queryProxy().query(FIND_BY_DATE_EMP_CONFIRM, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyID)
				.setParameter("baseDate", date)
				.setParameter("confirmationRootType", confirmRootAtr)
				.setParameter("employmentRootAtr", employmentRootAtr)
				.getList(c -> toDomainPsApR(c));
	}

	@Override
	public Optional<PersonApprovalRoot> getNewestCommonPsAppRoot(String companyId, String employeeId){
		return this.queryProxy().query(FIND_COMMON_PS_APP_LASTEST, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.getSingle(c->toDomainPsApR(c));
	}
	
	@Override
	public Optional<PersonApprovalRoot> getNewestMonthlyPsAppRoot(String companyId, String employeeId){
		return this.queryProxy().query(FIND_MONTHLY_PS_APP_LASTEST, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.getSingle(c->toDomainPsApR(c));
	}

	@Override
	public List<PersonApprovalRoot> getPastHistory(String companyId, String employeeId){
		return this.queryProxy().query(FIND_PART_HISTORY, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.getList(c->toDomainPsApR(c));
	}

	@Override
	public List<PersonApprovalRoot> getPsApprovalRootBySdate(String companyId, String employeeId, GeneralDate startDate){
		return this.queryProxy().query(SELECT_PS_APR_BY_STARTDATE, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.setParameter("startDate", startDate)
				.getList(c->toDomainPsApR(c));
	}
	@Override
	public List<PersonApprovalRoot> findEmpByConfirm(String companyID, String employeeID,
			ConfirmationRootType confirmType, GeneralDate date) {
		return this.queryProxy().query(FIND_BY_EMP_CONFIRM, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyID)
				.setParameter("employeeId", employeeID)
				.setParameter("baseDate", date)
				.setParameter("confirmationRootType", confirmType.value)
				.getList(c->toDomainPsApR(c));
	}
	@Override
	public Optional<PersonApprovalRoot> getHistLastestCom(String companyId, String employeeId) {
		List<PersonApprovalRoot> lst =  this.queryProxy().query(GET_ALL__MODE_COM, WwfmtPsApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.getList(c->toDomainPsApR(c));
		return !lst.isEmpty()? Optional.of(lst.get(0)) : Optional.empty();
	}
	@Override
	public Optional<PersonApprovalRoot> getHistLastestPri(String companyId, String employeeId, int employmentRootAtr,
			Integer applicationType) {
		List<PersonApprovalRoot> lst = new ArrayList<>();
		if(employmentRootAtr == EmploymentRootAtr.COMMON.value){
			lst = this.queryProxy().query(GET_ALL_MODE_PRI_CM, WwfmtPsApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("employeeId", employeeId)
					.getList(c->toDomainPsApR(c));
		}else if(employmentRootAtr == EmploymentRootAtr.APPLICATION.value){
			lst = this.queryProxy().query(GET_ALL_MODE_PRI_AP, WwfmtPsApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("employeeId", employeeId)
					.setParameter("applicationType", applicationType)
					.getList(c->toDomainPsApR(c));
		}else{
			lst = this.queryProxy().query(GET_ALL_MODE_PRI_CF, WwfmtPsApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("employeeId", employeeId)
					.setParameter("confirmationRootType", applicationType)
					.getList(c->toDomainPsApR(c));
		}
		return !lst.isEmpty()? Optional.of(lst.get(0)) : Optional.empty();
	}
}
