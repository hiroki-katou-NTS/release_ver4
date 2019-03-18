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
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;
import nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot.WwfmtWpApprovalRoot;
import nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot.WwfmtWpApprovalRootPK;
/**
 * 
 * @author hoatt
 *
 */
@RequestScoped
public class JpaWorkplaceApprovalRootRepository extends JpaRepository implements WorkplaceApprovalRootRepository{

	private static final String FIND_BY_ALL = "SELECT c FROM WwfmtWpApprovalRoot c";
	private static final String FIND_BY_CID = FIND_BY_ALL
			+ " WHERE c.wwfmtWpApprovalRootPK.companyId = :companyId";
	private static final String FIND_BY_WKPID = FIND_BY_ALL
			+ " WHERE c.wwfmtWpApprovalRootPK.companyId = :companyId"
			+ " AND c.wwfmtWpApprovalRootPK.workplaceId = :workplaceId";
	private static final String SELECT_WPAPR_BY_EDATE = FIND_BY_WKPID
			+ " AND c.endDate = :endDate"
			+ " AND c.employmentRootAtr = :employmentRootAtr"
			+ " AND c.applicationType = :applicationType";
	private static final String SELECT_WPAPR_BY_EDATE_APP_NULL = FIND_BY_WKPID
			   + " AND c.endDate = :endDate"
			   + " AND c.employmentRootAtr = :employmentRootAtr"
			   + " AND c.applicationType IS NULL";
	private static final String SELECT_WPAPR_BY_EDATE_CONFIRM = FIND_BY_WKPID
			   + " AND c.endDate = :endDate" 
			   + " AND c.confirmationRootType = :confirmationRootType"
			   + " AND c.employmentRootAtr = :employmentRootAtr";
	private static final String FIND_BY_BASEDATE = FIND_BY_WKPID
			+ " AND c.startDate <= :baseDate"
			+ " AND c.endDate >= :baseDate"
			+ " AND c.employmentRootAtr = :rootAtr" 
			+ " AND c.applicationType = :appType";
	private static final String FIND_BY_BASEDATE_OF_COM = FIND_BY_WKPID
			+ " AND c.startDate <= :baseDate"
			+ " AND c.endDate >= :baseDate"
			+ " AND c.employmentRootAtr = 0";
	private static final String FIND_ALL_BY_BASEDATE = FIND_BY_ALL + " WHERE  c.wwfmtWpApprovalRootPK.companyId = :companyId"
			+ " AND c.startDate <= :baseDate"
			+ " AND c.endDate >= :baseDate"
			+ " ORDER BY  c.wwfmtWpApprovalRootPK.workplaceId ";
	private static final String FIND_BY_APP_TYPE = FIND_BY_WKPID
			+ " AND c.employmentRootAtr = :employmentRootAtr"
			+ " AND c.applicationType = :applicationType"
			+ " ORDER BY c.startDate DESC";
	private static final String SELECT_WPAPR_BY_APP_NULL = FIND_BY_WKPID
			   + " AND c.employmentRootAtr = :employmentRootAtr"
			   + " AND c.applicationType IS NULL"
			   + " ORDER BY c.startDate DESC";
	private static final String FIND_BY_CFR_TYPE = FIND_BY_WKPID
			   + " AND c.confirmationRootType = :confirmationRootType"
			   + " AND c.employmentRootAtr = :employmentRootAtr"
			   + " ORDER BY c.startDate DESC";
	private static final String FIND_WP_APP_LAST = FIND_BY_WKPID
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
	private static final String FIND_BY_EMP_CONFIRM = FIND_BY_WKPID
			 + " AND c.startDate <= :baseDate"
			 + " AND c.endDate >= :baseDate"
			 + " AND c.confirmationRootType = :confirmationRootType"
			 + " AND c.employmentRootAtr = 2";
	/**
	 * get All Workplace Approval Root
	 * @param companyId
	 * @param workplaceId
	 * @return
	 */
	@Override
	public List<WorkplaceApprovalRoot> getAllWpApprovalRoot(String companyId, String workplaceId) {
		return this.queryProxy().query(FIND_BY_WKPID, WwfmtWpApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("workplaceId", workplaceId)
				.getList(c->toDomainWpApR(c));
	}
	
	/**
	 * get WpApprovalRoot
	 * @param companyId
	 * @param approvalId
	 * @param workplaceId
	 * @param historyId
	 * @return
	 */
	@Override
	public Optional<WorkplaceApprovalRoot> getWpApprovalRoot(String companyId, String approvalId, String workplaceId, String historyId) {
		WwfmtWpApprovalRootPK pk = new WwfmtWpApprovalRootPK(companyId, approvalId, workplaceId, historyId);
		return this.queryProxy().find(pk, WwfmtWpApprovalRoot.class).map(c->toDomainWpApR(c));
	}
	
	/**
	 * get Workplace Approval Root By End date
	 * @param companyId
	 * @param workplaceId
	 * @param endDate
	 * @return
	 */
	@Override
	public List<WorkplaceApprovalRoot> getWpApprovalRootByEdate(String companyId, String workplaceId, GeneralDate endDate, Integer applicationType, int employmentRootAtr) {
		//common
		if(employmentRootAtr == 0){
			return this.queryProxy().query(SELECT_WPAPR_BY_EDATE_APP_NULL, WwfmtWpApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("workplaceId", workplaceId)
					.setParameter("endDate", endDate)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainWpApR(c));
		}
		//confirm
		if(employmentRootAtr == 2){
			return this.queryProxy().query(SELECT_WPAPR_BY_EDATE_CONFIRM, WwfmtWpApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("workplaceId", workplaceId)
					.setParameter("endDate", endDate)
					.setParameter("confirmationRootType", applicationType)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainWpApR(c));
		}
		//15 app type
		return this.queryProxy().query(SELECT_WPAPR_BY_EDATE, WwfmtWpApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("workplaceId", workplaceId)
				.setParameter("endDate", endDate)
				.setParameter("applicationType", applicationType)
				.setParameter("employmentRootAtr", employmentRootAtr)
				.getList(c->toDomainWpApR(c));
	}
	
	/**
	 * ドメインモデル「職場別就業承認ルート」を取得する(就業ルート区分(申請か、確認か、任意項目か))
	 * @param cid
	 * @param workplaceId
	 * @param baseDate
	 * @param appType
	 * @return WorkplaceApprovalRoots
	 */
	@Override
	public Optional<WorkplaceApprovalRoot> findByBaseDate(String companyID, String workplaceID, GeneralDate date, ApplicationType appType, EmploymentRootAtr rootAtr) {
		return this.queryProxy().query(FIND_BY_BASEDATE, WwfmtWpApprovalRoot.class)
				.setParameter("companyId", companyID)
				.setParameter("workplaceId", workplaceID)
				.setParameter("baseDate", date)
				.setParameter("appType", appType.value)
				.setParameter("rootAtr", rootAtr.value)
				.getSingle(c->toDomainWpApR(c));
	}
	
	/**
	 * ドメインモデル「職場別就業承認ルート」を取得する(共通の)
	 * @param cid
	 * @param workplaceId
	 * @param baseDate
	 * @param appType
	 * @return WorkplaceApprovalRoots
	 */
	@Override
	public Optional<WorkplaceApprovalRoot> findByBaseDateOfCommon(String companyID, String workplaceID, GeneralDate date) {
		return this.queryProxy().query(FIND_BY_BASEDATE_OF_COM, WwfmtWpApprovalRoot.class)
				.setParameter("companyId", companyID)
				.setParameter("workplaceId", workplaceID)
				.setParameter("baseDate", date)
				.getSingle(c->toDomainWpApR(c));
	}
	
	/**
	 * add Workplace Approval Root
	 * @param wpAppRoot
	 */
	@Override
	public void addWpApprovalRoot(WorkplaceApprovalRoot wpAppRoot) {
		this.commandProxy().insert(toEntityWpApR(wpAppRoot));
	}
	/**
	 * add All Workplace Approval Root
	 * @param wpAppRoot
	 */
	@Override
	public void addAllWpApprovalRoot(List<WorkplaceApprovalRoot> wpAppRoot) {
		List<WwfmtWpApprovalRoot> lstEntity = new ArrayList<>();
		for (WorkplaceApprovalRoot wp : wpAppRoot) {
			lstEntity.add(toEntityWpApR(wp));
		}
		this.commandProxy().insertAll(lstEntity);
	}
	/**
	 * update Workplace Approval Root
	 * @param wpAppRoot
	 */
	@Override
	public void updateWpApprovalRoot(WorkplaceApprovalRoot wpAppRoot) {
		WwfmtWpApprovalRoot a = toEntityWpApR(wpAppRoot);
		WwfmtWpApprovalRoot x = this.queryProxy().find(a.wwfmtWpApprovalRootPK, WwfmtWpApprovalRoot.class).get();
		x.setStartDate(a.startDate);
		x.setEndDate(a.endDate);
		x.setApplicationType(a.applicationType);
		x.setBranchId(a.branchId);
		x.setAnyItemAppId(a.anyItemAppId);
		x.setConfirmationRootType(a.confirmationRootType);
		x.setEmploymentRootAtr(a.employmentRootAtr);
		this.commandProxy().update(x);
	}
	/**
	 * update All Workplace Approval Root
	 * @param wpAppRoot
	 */
	@Override
	public void updateAllWpApprovalRoot(List<WorkplaceApprovalRoot> wpAppRoot) {
		List<WwfmtWpApprovalRoot> lstEntity = new ArrayList<>();
		for (WorkplaceApprovalRoot wp : wpAppRoot) {
			WwfmtWpApprovalRoot a = toEntityWpApR(wp);
			WwfmtWpApprovalRoot x = this.queryProxy().find(a.wwfmtWpApprovalRootPK, WwfmtWpApprovalRoot.class).get();
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
	 * delete Person Approval Root
	 * @param companyId
	 * @param workplaceId
	 * @param historyId
	 */
	@Override
	public void deleteWpApprovalRoot(String companyId, String approvalId, String workplaceId, String historyId) {
		WwfmtWpApprovalRootPK comPK = new WwfmtWpApprovalRootPK(companyId, approvalId, workplaceId, historyId);
		this.commandProxy().remove(WwfmtWpApprovalRoot.class,comPK);
	}
	
	/**
	 * convert entity WwfmtWpApprovalRoot to domain WorkplaceApprovalRoot
	 * @param entity
	 * @return
	 */
	private WorkplaceApprovalRoot toDomainWpApR(WwfmtWpApprovalRoot entity){
		val domain = WorkplaceApprovalRoot.convert(entity.wwfmtWpApprovalRootPK.companyId,
				entity.wwfmtWpApprovalRootPK.approvalId,
				entity.wwfmtWpApprovalRootPK.workplaceId,
				entity.wwfmtWpApprovalRootPK.historyId,
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
	 * convert domain WorkplaceApprovalRoot to entity WwfmtWpApprovalRoot
	 * @param domain
	 * @return
	 */
	private WwfmtWpApprovalRoot toEntityWpApR(WorkplaceApprovalRoot domain){
		val entity = new WwfmtWpApprovalRoot();
		entity.wwfmtWpApprovalRootPK = new WwfmtWpApprovalRootPK(domain.getCompanyId(), domain.getApprovalId(), domain.getWorkplaceId(), domain.getEmploymentAppHistoryItems().get(0).getHistoryId());
		entity.startDate = domain.getEmploymentAppHistoryItems().get(0).start();
		entity.endDate = domain.getEmploymentAppHistoryItems().get(0).end();
		entity.applicationType = (domain.getApplicationType() == null ? null : domain.getApplicationType().value);
		entity.branchId = domain.getBranchId();
		entity.anyItemAppId = domain.getAnyItemApplicationId();
		entity.confirmationRootType = (domain.getConfirmationRootType() == null ? null : domain.getConfirmationRootType().value);
		entity.employmentRootAtr = domain.getEmploymentRootAtr().value;
		return entity;
	}
	
	public List<WorkplaceApprovalRoot> findAllByBaseDate(String companyId, GeneralDate baseDate){
		List<WorkplaceApprovalRoot> data = this.queryProxy().query(FIND_ALL_BY_BASEDATE, WwfmtWpApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("baseDate", baseDate)
				.getList(c->toDomainWpApR(c));
		return data;
	}
	/**
	 * get Work place Approval Root By type
	 * @param companyId
	 * @param workplaceId
	 * @param applicationType
	 * @param employmentRootAtr
	 * @return
	 */
	@Override
	public List<WorkplaceApprovalRoot> getWpApprovalRootByType(String companyId, String workplaceId, Integer applicationType,
			int employmentRootAtr) {
		//common
		if(employmentRootAtr == 0){
			return this.queryProxy().query(SELECT_WPAPR_BY_APP_NULL, WwfmtWpApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("workplaceId", workplaceId)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainWpApR(c));
		}
		//confirm
		if(employmentRootAtr == 2){
			return this.queryProxy().query(FIND_BY_CFR_TYPE, WwfmtWpApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("workplaceId", workplaceId)
					.setParameter("confirmationRootType", applicationType)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainWpApR(c));
		}
		//15 app type
		return this.queryProxy().query(FIND_BY_APP_TYPE, WwfmtWpApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("workplaceId", workplaceId)
				.setParameter("applicationType", applicationType)
				.setParameter("employmentRootAtr", employmentRootAtr)
				.getList(c->toDomainWpApR(c));
	}

	@Override
	public List<WorkplaceApprovalRoot> getWpAppRootLast(String companyId, String workplaceId, GeneralDate endDate) {
		
		return this.queryProxy().query(FIND_WP_APP_LAST, WwfmtWpApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("workplaceId", workplaceId)
				.setParameter("endDate", endDate)
				.getList(c -> toDomainWpApR(c));
	}

	@Override
	public List<WorkplaceApprovalRoot> getWpAppRoot(String companyID, GeneralDate date, 
			Integer employmentRootAtr, Integer confirmRootAtr) {
		if(confirmRootAtr==null){
			return this.queryProxy().query(FIND_BY_DATE_EMP, WwfmtWpApprovalRoot.class)
					.setParameter("companyId", companyID)
					.setParameter("baseDate", date)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c -> toDomainWpApR(c));
		}
		return this.queryProxy().query(FIND_BY_DATE_EMP_CONFIRM, WwfmtWpApprovalRoot.class)
				.setParameter("companyId", companyID)
				.setParameter("baseDate", date)
				.setParameter("confirmationRootType", confirmRootAtr)
				.setParameter("employmentRootAtr", employmentRootAtr)
				.getList(c -> toDomainWpApR(c));
	}

	@Override
	public List<WorkplaceApprovalRoot> findEmpByConfirm(String companyID, String workplaceID, 
			ConfirmationRootType confirmType, GeneralDate date) {
		return this.queryProxy().query(FIND_BY_EMP_CONFIRM, WwfmtWpApprovalRoot.class)
				.setParameter("companyId", companyID)
				.setParameter("workplaceId", workplaceID)
				.setParameter("baseDate", date)
				.setParameter("confirmationRootType", confirmType.value)
				.getList(c->toDomainWpApR(c));
	}
}
