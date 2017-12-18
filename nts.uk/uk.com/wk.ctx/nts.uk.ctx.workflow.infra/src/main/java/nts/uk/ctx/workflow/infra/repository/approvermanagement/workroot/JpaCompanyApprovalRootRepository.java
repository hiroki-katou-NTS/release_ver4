package nts.uk.ctx.workflow.infra.repository.approvermanagement.workroot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot.WwfmtComApprovalRoot;
import nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot.WwfmtComApprovalRootPK;
/**
 * 
 * @author hoatt
 *
 */
@Stateless
public class JpaCompanyApprovalRootRepository extends JpaRepository implements CompanyApprovalRootRepository{

	private final String FIND_BY_ALL = "SELECT c FROM WwfmtComApprovalRoot c";
	private final String FIND_BY_CID = FIND_BY_ALL
	   + " WHERE c.wwfmtComApprovalRootPK.companyId = :companyId";
	private final String FIND_BY_DATE = FIND_BY_CID 
	   + " AND c.endDate = :endDate"
	   + " AND c.applicationType = :applicationType"
	   + " AND c.employmentRootAtr = :employmentRootAtr";
	private final String FIND_BY_DATE_CFR = FIND_BY_CID 
			   + " AND c.endDate = :endDate"
			   + " AND c.confirmationRootType = :confirmationRootType"
			   + " AND c.employmentRootAtr = :employmentRootAtr";
	private final String SELECT_COM_APR_BY_DATE_APP_NULL = FIND_BY_CID 
				   + " AND c.endDate = :endDate"
				   + " AND c.employmentRootAtr = :employmentRootAtr"
				   + " AND c.applicationType IS NULL";
	private final String FIND_BY_BASEDATE = FIND_BY_CID
				+ " AND c.startDate <= :baseDate"
				+ " AND c.endDate >= :baseDate"
				+ " AND c.employmentRootAtr = :rootAtr" 
				+ " AND c.applicationType = :appType";
	private final String FIND_BY_BASEDATE_OF_COM = FIND_BY_CID
				+ " AND c.startDate <= :baseDate"
				+ " AND c.endDate >= :baseDate"
				+ " AND c.employmentRootAtr = 0";
	
	private final String FIND_ALL_BY_BASEDATE = FIND_BY_CID
			+ " AND c.startDate <= :baseDate"
			+ " AND c.endDate >= :baseDate";
	private final String FIND_BY_APP_TYPE = FIND_BY_CID 
			   + " AND c.applicationType = :applicationType"
			   + " AND c.employmentRootAtr = :employmentRootAtr"
			   + " ORDER BY c.startDate DESC";
	private final String FIND_BY_CFR_TYPE = FIND_BY_CID 
			   + " AND c.confirmationRootType = :confirmationRootType"
			   + " AND c.employmentRootAtr = :employmentRootAtr"
			   + " ORDER BY c.startDate DESC";
	private final String SELECT_COM_APR_APP_NULL = FIND_BY_CID 
				   + " AND c.employmentRootAtr = :employmentRootAtr"
				   + " AND c.applicationType IS NULL"
				   + " ORDER BY c.startDate DESC";
	/**
	 * get All Company Approval Root
	 * @param companyId
	 * @return
	 */
	@Override
	public List<CompanyApprovalRoot> getAllComApprovalRoot(String companyId) {
		return this.queryProxy().query(FIND_BY_CID, WwfmtComApprovalRoot.class)
				.setParameter("companyId", companyId)
				.getList(c->toDomainComApR(c));
	}
	/**
	 * get ComApprovalRoot
	 * @param companyId
	 * @param approvalId
	 * @param historyId
	 * @return
	 */
	@Override
	public Optional<CompanyApprovalRoot> getComApprovalRoot(String companyId, String approvalId, String historyId) {
		WwfmtComApprovalRootPK pk = new WwfmtComApprovalRootPK(companyId, approvalId, historyId);
		return this.queryProxy().find(pk, WwfmtComApprovalRoot.class).map(c->toDomainComApR(c));
	}
	/**
	 * get Company Approval Root By End date
	 * @param companyId
	 * @param endDate
	 * @return
	 */
	@Override
	public List<CompanyApprovalRoot> getComApprovalRootByEdate(String companyId, GeneralDate endDate, Integer applicationType, int employmentRootAtr) {
		//common
		if(employmentRootAtr == 0){
			return this.queryProxy().query(SELECT_COM_APR_BY_DATE_APP_NULL, WwfmtComApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("endDate", endDate)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainComApR(c));
		}
		//confirm
		if(employmentRootAtr == 2){
			return this.queryProxy().query(FIND_BY_DATE_CFR, WwfmtComApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("endDate", endDate)
					.setParameter("confirmationRootType", applicationType)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainComApR(c));
		}
		//15 app type
		return this.queryProxy().query(FIND_BY_DATE, WwfmtComApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("endDate", endDate)
				.setParameter("applicationType", applicationType)
				.setParameter("employmentRootAtr", employmentRootAtr)
				.getList(c->toDomainComApR(c));
	}
	
	/**
	 * ドメインモデル「会社別就業承認ルート」を取得する(就業ルート区分(申請か、確認か、任意項目か))
	 * @param cid
	 * @param workplaceId
	 * @param baseDate
	 * @param appType
	 * @return WorkplaceApprovalRoots
	 */
	@Override
	public Optional<CompanyApprovalRoot> findByBaseDate(String companyID, GeneralDate date, ApplicationType appType, EmploymentRootAtr rootAt) {
		return this.queryProxy().query(FIND_BY_BASEDATE, WwfmtComApprovalRoot.class)
				.setParameter("companyId", companyID)
				.setParameter("baseDate", date)
				.setParameter("appType", appType.value)
				.setParameter("rootAtr", rootAt.value)
				.getSingle(c->toDomainComApR(c));
	}
	
	/**
	 * ドメインモデル「会社別就業承認ルート」を取得する(共通の)
	 * @param cid
	 * @param workplaceId
	 * @param baseDate
	 * @param appType
	 * @return WorkplaceApprovalRoots
	 */
	@Override
	public List<CompanyApprovalRoot> findByBaseDateOfCommon(String companyID, GeneralDate date) {
		return this.queryProxy().query(FIND_BY_BASEDATE_OF_COM, WwfmtComApprovalRoot.class)
				.setParameter("companyId", companyID)
				.setParameter("baseDate", date)
				.getList(c->toDomainComApR(c));
	}
	
	/**
	 * add Company Approval Root
	 * @param comAppRoot
	 */
	@Override
	public void addComApprovalRoot(CompanyApprovalRoot comAppRoot) {
		this.commandProxy().insert(toEntityComApR(comAppRoot));
	}
	/**
	 * add All Company Approval Root
	 * @param comAppRoot
	 */
	@Override
	public void addAllComApprovalRoot(List<CompanyApprovalRoot> comAppRoot) {
		List<WwfmtComApprovalRoot> lstEntity = new ArrayList<>();
		for (CompanyApprovalRoot com : comAppRoot) {
			lstEntity.add(toEntityComApR(com));
		}
		this.commandProxy().insertAll(lstEntity);
	}
	/**
	 * update All Company Approval Root
	 * @param comAppRoot
	 */
	@Override
	public void updateAllComApprovalRoot(List<CompanyApprovalRoot> comAppRoot) {
		List<WwfmtComApprovalRoot> lstEntity = new ArrayList<>();
		for (CompanyApprovalRoot com : comAppRoot) {
			WwfmtComApprovalRoot a = toEntityComApR(com);
			WwfmtComApprovalRoot x = this.queryProxy().find(a.wwfmtComApprovalRootPK, WwfmtComApprovalRoot.class).get();
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
	 * update Company Approval Root
	 * 
	 * @param comAppRoot
	 */
	@Override
	public void updateComApprovalRoot(CompanyApprovalRoot comAppRoot) {
			WwfmtComApprovalRoot a = toEntityComApR(comAppRoot);
			WwfmtComApprovalRoot x = this.queryProxy().find(a.wwfmtComApprovalRootPK, WwfmtComApprovalRoot.class).get();
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
	 * delete Company Approval Root
	 * @param companyId
	 * @param approvalId
	 * @param historyId
	 */
	@Override
	public void deleteComApprovalRoot(String companyId, String approvalId, String historyId) {
		WwfmtComApprovalRootPK comPK = new WwfmtComApprovalRootPK(companyId, approvalId, historyId);
		this.commandProxy().remove(WwfmtComApprovalRoot.class,comPK);
	}
	/**
	 * convert entity WwfmtComApprovalRoot to domain CompanyApprovalRoot
	 * @param entity
	 * @return
	 */
	private CompanyApprovalRoot toDomainComApR(WwfmtComApprovalRoot entity){
		val domain = CompanyApprovalRoot.convert(entity.wwfmtComApprovalRootPK.companyId,
				entity.wwfmtComApprovalRootPK.approvalId,
				entity.wwfmtComApprovalRootPK.historyId,
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
	 * convert domain CompanyApprovalRoot to entity WwfmtComApprovalRoot
	 * @param domain
	 * @return
	 */
	private WwfmtComApprovalRoot toEntityComApR(CompanyApprovalRoot domain){
		val entity = new WwfmtComApprovalRoot();
		entity.wwfmtComApprovalRootPK = new WwfmtComApprovalRootPK(domain.getCompanyId(), domain.getApprovalId(), domain.getHistoryId());
		entity.startDate = domain.getPeriod().getStartDate();
		entity.endDate = domain.getPeriod().getEndDate();
		entity.applicationType = (domain.getApplicationType() == null ? null : domain.getApplicationType().value);
		entity.branchId = domain.getBranchId();
		entity.anyItemAppId = domain.getAnyItemApplicationId();
		entity.confirmationRootType = (domain.getConfirmationRootType() == null ? null : domain.getConfirmationRootType().value);
		entity.employmentRootAtr = domain.getEmploymentRootAtr().value;
		return entity;
	}
	@Override
	public List<CompanyApprovalRoot> findByBaseDate(String cid, GeneralDate baseDate) {
		return this.queryProxy().query(FIND_ALL_BY_BASEDATE, WwfmtComApprovalRoot.class)
				.setParameter("companyId", cid)
				.setParameter("baseDate", baseDate)
				.getList(c->toDomainComApR(c));
	}
	/**
	 * get Company Approval Root By type
	 * @param companyId
	 * @param applicationType
	 * @param employmentRootAtr
	 * @return
	 */
	@Override
	public List<CompanyApprovalRoot> getComApprovalRootByType(String companyId, Integer applicationType,
			int employmentRootAtr) {
		//common
		if(employmentRootAtr == 0){
			return this.queryProxy().query(SELECT_COM_APR_APP_NULL, WwfmtComApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainComApR(c));
		}
		//confirm
		if(employmentRootAtr == 2){
			return this.queryProxy().query(FIND_BY_CFR_TYPE, WwfmtComApprovalRoot.class)
					.setParameter("companyId", companyId)
					.setParameter("confirmationRootType", applicationType)
					.setParameter("employmentRootAtr", employmentRootAtr)
					.getList(c->toDomainComApR(c));
		}
		//15 app type
		return this.queryProxy().query(FIND_BY_APP_TYPE, WwfmtComApprovalRoot.class)
				.setParameter("companyId", companyId)
				.setParameter("applicationType", applicationType)
				.setParameter("employmentRootAtr", employmentRootAtr)
				.getList(c->toDomainComApR(c));
	}
}
