package nts.uk.ctx.at.request.infra.repository.application.common;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.infra.entity.application.common.KrqdpApplicationPK_New;
import nts.uk.ctx.at.request.infra.entity.application.common.KrqdtApplication_New;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaApplicationRepository_New extends JpaRepository implements ApplicationRepository_New {
	private static final String SELECT_FROM_APPLICATION = "SELECT a FROM KrqdtApplication_New a"
			+ " WHERE a.krqdpApplicationPK.companyID = :companyID";
	private static final String UPDATE = "UPDATE KrqdtApplication_New a "
			+ "SET a.reversionReason = :reversionReason"
			+ ", a.appReason = :appReason"
			+ ", a.stateReflectionReal = :stateReflectionReal"
			+ " WHERE a.krqdpApplicationPK.appID = :appID AND a.krqdpApplicationPK.companyID = :companyID";
	private static final String SELECT_APP = "SELECT c FROM KrqdtApplication_New c "
			+ "WHERE c.employeeID = :applicantSID "
			+ "AND c.appDate = :appDate "
			+ "AND c.prePostAtr = :prePostAtr "
			+ "AND c.appType = :applicationType "
			+ "ORDER BY c.inputDate DESC";
	private static final String SELECT_BY_DATE = SELECT_FROM_APPLICATION + " AND a.appDate >= :startDate AND a.appDate <= :endDate";
	private static  final String SELECT_BEFORE_APPLICATION = SELECT_FROM_APPLICATION 
			+ " AND a.appDate = :appDate "
			+ " AND a.appType = :applicationType "
			+ " AND a.prePostAtr = :prePostAtr ORDER BY a.inputDate DESC";
	//hoatt
//	private static final String SELECT_APP_BY_SID = SELECT_FROM_APPLICATION + " AND ( a.employeeID = :employeeID Or a.enteredPersonID = :employeeID )"
//			+ " AND a.appDate >= :startDate AND a.appDate <= :endDate and a.appType IN (0,1,2,4,6,10)";
	private static final String SELECT_APP_BY_SID = SELECT_FROM_APPLICATION + " AND ( a.employeeID = :employeeID Or a.enteredPersonID = :employeeID )"
			+ " AND ((a.startDate >= :startDate and a.endDate <= :endDate)"
			+ " OR (a.endDate IS null and a.startDate >= :startDate AND a.startDate <= :endDate))" 
			+ " AND a.appType IN (0,1,2,4,6,10)";
	//hoatt
	private static final String SELECT_APP_BY_REFLECT = SELECT_FROM_APPLICATION + " AND a.stateReflectionReal != 5"
			+ " AND ((a.startDate >= :startDate and a.endDate <= :endDate)"
			+ " OR (a.endDate IS null and a.startDate >= :startDate AND a.startDate <= :endDate))" 
			+ " AND a.appType IN (0,1,2,4,6,10)";
	private static final String SELECT_APP_BY_SIDS = "SELECT a FROM KrqdtApplication_New a" + " WHERE a.employeeID IN :employeeID" + " AND a.appDate >= :startDate AND a.appDate <= :endDate";
	private static final String SELECT_APPLICATION_BY_ID = "SELECT a FROM KrqdtApplication_New a"
			+ " WHERE a.krqdpApplicationPK.appID = :appID AND a.krqdpApplicationPK.companyID = :companyID";
	
	private static final String SELECT_APP_BY_LIST_ID = "SELECT a FROM KrqdtApplication_New a"
			+ " WHERE a.krqdpApplicationPK.appID IN :listAppID AND a.krqdpApplicationPK.companyID = :companyID"
			+ " ORDER BY a.appDate";
	
	private static final String SELECT_APP_BY_CONDS = "SELECT a FROM KrqdtApplication_New a WHERE a.employeeID IN :employeeID AND a.appDate >= :startDate AND a.appDate <= :endDate"
			+ " AND a.prePostAtr = 1 AND (a.stateReflectionReal = 0 OR a.stateReflectionReal = 1) ORDER BY a.appDate ASC, a.inputDate DESC";
	private static final String SELECT_LATE_LEAVE = SELECT_BY_DATE + " O"
			+ "AND a.employeeID = :employeeID "
			+ "AND a.stateReflectionReal = 0 "
			+ "AND a.appType = 9 ORDER BY a.appDate ASC";
	
	@Override
	public Optional<Application_New> findByID(String companyID, String appID) {
		return this.queryProxy().query(SELECT_APPLICATION_BY_ID, KrqdtApplication_New.class)
				.setParameter("appID", appID)
				.setParameter("companyID", companyID)
				.getSingle(x -> x.toDomain());
	}
	@Override
	public List<Application_New> getApp(String applicantSID, GeneralDate appDate, int prePostAtr,
			int appType) {
		return this.queryProxy().query(SELECT_APP, KrqdtApplication_New.class)
				.setParameter("applicantSID", applicantSID)
				.setParameter("appDate", appDate)
				.setParameter("prePostAtr", prePostAtr)
				.setParameter("applicationType", appType)
				.getList(c -> c.toDomain());
	}
	@Override
	public void insert(Application_New application) {
		this.commandProxy().insert(KrqdtApplication_New.fromDomain(application));
		this.getEntityManager().flush();
	}

	@Override
	public void update(Application_New application) {
		this.getEntityManager().createQuery(UPDATE)
			.setParameter("companyID", application.getCompanyID())
			.setParameter("appID", application.getAppID())
			.setParameter("reversionReason", application.getReversionReason().v())
			.setParameter("appReason", application.getAppReason().v())
			.setParameter("stateReflectionReal", application.getReflectionInformation().getStateReflectionReal().value)			
			.executeUpdate();
		this.getEntityManager().flush();
	}

	@Override
	public void updateWithVersion(Application_New application) {
		KrqdtApplication_New krqdtApplication = this.queryProxy()
			.find(new KrqdpApplicationPK_New(application.getCompanyID(), application.getAppID()), KrqdtApplication_New.class).get();
		krqdtApplication.reversionReason = application.getReversionReason().v();
		krqdtApplication.appReason = application.getAppReason().v();
		krqdtApplication.stateReflectionReal = application.getReflectionInformation().getStateReflectionReal().value;
		krqdtApplication.notReasonReal = application.getReflectionInformation().getNotReasonReal().isPresent() ? application.getReflectionInformation().getNotReasonReal().get().value : null;
		this.commandProxy().update(krqdtApplication);
		this.getEntityManager().flush();
	}

	@Override
	public void delete(String companyID, String appID) {
		this.commandProxy().remove(KrqdtApplication_New.class, new KrqdpApplicationPK_New(companyID, appID));
	}
	@Override
	public List<Application_New> getApplicationIdByDate(String companyId, GeneralDate startDate, GeneralDate endDate) {
		List<Application_New> data = this.queryProxy().query(SELECT_BY_DATE, KrqdtApplication_New.class)
				.setParameter("companyID", companyId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList(c -> c.toDomain());
		return data;
	}
	@Override
	public List<Application_New> getBeforeApplication(String companyId, GeneralDate appDate, GeneralDateTime inputDate,
			int appType, int prePostAtr) {
		return this.queryProxy().query(SELECT_BEFORE_APPLICATION, KrqdtApplication_New.class)
				.setParameter("companyID", companyId)
				.setParameter("appDate", appDate)
				.setParameter("applicationType", appType)
				.setParameter("prePostAtr", prePostAtr)				
				.getList(c -> c.toDomain());
	}
	/**
	 * @author hoatt
	 * get list application by sID
	 */
	@Override
	public List<Application_New> getListAppBySID(String companyId, String sID, GeneralDate startDate,
			GeneralDate endDate) {
		return this.queryProxy().query(SELECT_APP_BY_SID, KrqdtApplication_New.class)
				.setParameter("companyID", companyId)
				.setParameter("employeeID", sID)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList(c -> c.toDomain());
	}
	/**
	 * @author hoatt
	 * get List Application By Reflect
	 * wait QA
	 */
	@Override
	public List<Application_New> getListAppByReflect(String companyId, GeneralDate startDate, GeneralDate endDate) {
		return this.queryProxy().query(SELECT_APP_BY_REFLECT, KrqdtApplication_New.class)
				.setParameter("companyID", companyId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList(c -> c.toDomain());
	}
	/**
	 * get List Application Pre
	 */
	@Override
	public List<Application_New> getListAppPre(String companyId, String sID, GeneralDate appDate, int prePostAtr) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Application_New> getApplicationBySIDs(List<String> employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		List<Application_New> data = this.queryProxy().query(SELECT_APP_BY_SIDS, KrqdtApplication_New.class)
				.setParameter("employeeID", employeeID)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList(c -> c.toDomain());
		return data;
	}
	
	/**
	 * Request list No.236
	 */
	@Override
	public List<Application_New> getListApp(String sID, GeneralDate startDate, GeneralDate endDate) {
		List<Application_New> data = this.queryProxy().query(SELECT_APP_BY_CONDS, KrqdtApplication_New.class)
				.setParameter("employeeID", sID)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList(c -> c.toDomain());
		
		return data;
	}
	/**
	 * RequestList 232 param 反映状態   ＝  「反映済み」または「反映待ち」 
	 * RequestList 233 param 反映状態   ＝  「未承認」または「差戻し」
	 * RequestList 234 param 反映状態   ＝  「否認」
	 * RequestList 235 param 反映状態   ＝  「差戻し」
	 */
	private static final String SELECT_LIST_REFSTATUS = "SELECT a FROM KrqdtApplication_New a"
			+ " WHERE a.employeeID = :employeeID "
			+ " AND a.appDate >= :startDate AND a.appDate <= :endDate"
			+ " AND a.stateReflectionReal IN :listReflecInfor"	
			+ " ORDER BY a.appDate ASC,"
			+ " a.prePostAtr DESC";
	
	@Override
	public List<Application_New> getByListRefStatus(String employeeID, GeneralDate startDate, GeneralDate endDate, List<Integer> listReflecInfor) {
		// TODO Auto-generated method stub
		if(listReflecInfor.size()==0) {
			return Collections.emptyList();
		}
		return this.queryProxy().query(SELECT_LIST_REFSTATUS, KrqdtApplication_New.class)
			.setParameter("employeeID", employeeID)
			.setParameter("startDate", startDate)
			.setParameter("endDate", endDate)
			.setParameter("listReflecInfor", listReflecInfor)
			.getList(x -> x.toDomain());
	}
	@Override
	public List<Application_New> findByListID(String companyID, List<String> listAppID) {
		if(CollectionUtil.isEmpty(listAppID)){
			return Collections.emptyList();
		}
		return this.queryProxy().query(SELECT_APP_BY_LIST_ID, KrqdtApplication_New.class)
				.setParameter("listAppID", listAppID)
				.setParameter("companyID", companyID)
				.getList(x -> x.toDomain());
	}
	@Override
	public List<Application_New> getListLateOrLeaveEarly(String companyID, String employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		return this.queryProxy().query(SELECT_LATE_LEAVE, KrqdtApplication_New.class)
				.setParameter("companyID", companyID)
				.setParameter("employeeID", employeeID)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList(x -> x.toDomain());
	}
}
