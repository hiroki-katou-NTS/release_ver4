package nts.uk.ctx.at.request.infra.repository.application.common;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflect;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflectDaily;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.ReflectionInformation_New;
import nts.uk.ctx.at.request.infra.entity.application.common.KrqdpApplicationPK_New;
import nts.uk.ctx.at.request.infra.entity.application.common.KrqdtApplication_New;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaApplicationRepository_New extends JpaRepository implements ApplicationRepository_New {

	/**
	 * use lesser value for nested split WHERE IN parameters to make sure total
	 * parameters < 2100
	 */
	private static final int SPLIT_650 = 650;

	private static final String SELECT_FROM_APPLICATION = "SELECT a FROM KrqdtApplication_New a"
			+ " WHERE a.krqdpApplicationPK.companyID = :companyID";
	private static final String SELECT_APP = "SELECT c FROM KrqdtApplication_New c "
			+ "WHERE c.employeeID = :applicantSID " + "AND c.appDate = :appDate " + "AND c.prePostAtr = :prePostAtr "
			+ "AND c.appType = :applicationType " + "ORDER BY c.inputDate DESC";
	private static final String SELECT_BY_DATE = SELECT_FROM_APPLICATION
			+ " AND a.appDate >= :startDate AND a.appDate <= :endDate";
	private static final String SELECT_BEFORE_APPLICATION = SELECT_FROM_APPLICATION + " AND a.employeeID = :employeeID"
			+ " AND a.appDate = :appDate " + " AND a.appType = :applicationType "
			+ " AND a.prePostAtr = :prePostAtr ORDER BY a.inputDate DESC";

	// hoatt
	private static final String SELECT_BY_LIST_SID = SELECT_FROM_APPLICATION
			+ " AND ( a.employeeID IN :lstSID OR a.enteredPersonID IN :lstSID )"
			+ " AND a.endDate >= :startDate AND a.startDate <= :endDate and a.appType IN (0,1,2,4,6,10)";
	// hoatt
	private static final String SELECT_BY_LIST_APPLICANT = SELECT_FROM_APPLICATION + " AND a.employeeID IN :lstSID"
			+ " AND a.endDate >= :startDate AND a.startDate <= :endDate and a.appType IN :lstType";

	private static final String SELECT_APP_BY_SIDS = "SELECT a FROM KrqdtApplication_New a"
			+ " WHERE a.employeeID IN :employeeID" + " AND a.appDate >= :startDate AND a.appDate <= :endDate";
	private static final String SELECT_APPLICATION_BY_ID = "SELECT a FROM KrqdtApplication_New a"
			+ " WHERE a.krqdpApplicationPK.appID = :appID AND a.krqdpApplicationPK.companyID = :companyID";

	private static final String SELECT_APP_BY_LIST_ID = "SELECT a FROM KrqdtApplication_New a"
			+ " WHERE a.krqdpApplicationPK.appID IN :listAppID AND a.krqdpApplicationPK.companyID = :companyID"
			+ " ORDER BY a.appDate";

	private static final String SELECT_APP_BY_CONDS = "SELECT a FROM KrqdtApplication_New a WHERE a.employeeID = :employeeID AND a.appDate >= :startDate AND a.appDate <= :endDate"
			+ " AND a.prePostAtr = 1 AND (a.stateReflectionReal = 0 OR a.stateReflectionReal = 1) ORDER BY a.appDate ASC, a.inputDate DESC";

	private static final String SELECT_LATE_LEAVE = SELECT_BY_DATE + " " + "AND a.employeeID = :employeeID "
			+ "AND a.stateReflectionReal = 0 " + "AND a.appType = 9 ORDER BY a.appDate ASC";

	private static final String SELECT_BY_SID_PERIOD_APPTYPE = "SELECT c FROM KrqdtApplication_New c "
			+ " WHERE c.employeeID = :employeeID" + " AND c.appDate >= :startDate" + " AND c.appDate <= :endDate"
			+ " AND c.stateReflectionReal IN :stateReflectionReals" + " AND c.appType IN :appTypes";
	// hoatt
	private static final String FIND_BY_REF_PERIOD_TYPE = "SELECT c FROM KrqdtApplication_New c"
			+ " WHERE c.krqdpApplicationPK.companyID = :companyID" + " AND c.employeeID = :employeeID"
			+ " AND c.appDate >= :startDate" + " AND c.appDate <= :endDate" + " AND c.prePostAtr = :prePostAtr"
			+ " AND c.appType = :appType" + " AND c.stateReflectionReal IN :lstRef"
			+ " ORDER BY c.appType ASC, c.inputDate DESC";

	@Override
	public Optional<Application_New> findByID(String companyID, String appID) {
		return this.queryProxy().query(SELECT_APPLICATION_BY_ID, KrqdtApplication_New.class)
				.setParameter("appID", appID).setParameter("companyID", companyID).getSingle(x -> x.toDomain());
	}

	@Override
	public List<Application_New> getApp(String applicantSID, GeneralDate appDate, int prePostAtr, int appType) {
		return this.queryProxy().query(SELECT_APP, KrqdtApplication_New.class)
				.setParameter("applicantSID", applicantSID).setParameter("appDate", appDate)
				.setParameter("prePostAtr", prePostAtr).setParameter("applicationType", appType)
				.getList(c -> c.toDomain());
	}

	@Override
	public void insert(Application_New application) {
		this.commandProxy().insert(KrqdtApplication_New.fromDomain(application));
		this.getEntityManager().flush();
	}

	@Override
	public void update(Application_New application) {
		KrqdtApplication_New krqdtApplication = this.queryProxy()
				.find(new KrqdpApplicationPK_New(application.getCompanyID(), application.getAppID()),
						KrqdtApplication_New.class)
				.get();
		krqdtApplication.reversionReason = application.getReversionReason().v();
		krqdtApplication.appReason = application.getAppReason().v();
		krqdtApplication.stateReflection = application.getReflectionInformation().getStateReflection().value;
		krqdtApplication.stateReflectionReal = application.getReflectionInformation().getStateReflectionReal().value;
		krqdtApplication.notReasonReal = application.getReflectionInformation().getNotReasonReal().isPresent()
				? application.getReflectionInformation().getNotReasonReal().get().value : null;
		this.commandProxy().update(krqdtApplication);
		this.getEntityManager().flush();
	}

	@Override
	public void updateWithVersion(Application_New application) {
		KrqdtApplication_New krqdtApplication = this.queryProxy()
				.find(new KrqdpApplicationPK_New(application.getCompanyID(), application.getAppID()),
						KrqdtApplication_New.class)
				.get();
		krqdtApplication.reversionReason = application.getReversionReason().v();
		krqdtApplication.appReason = application.getAppReason().v();
		krqdtApplication.stateReflection = application.getReflectionInformation().getStateReflection().value;
		krqdtApplication.stateReflectionReal = application.getReflectionInformation().getStateReflectionReal().value;
		krqdtApplication.notReasonReal = application.getReflectionInformation().getNotReasonReal().isPresent()
				? application.getReflectionInformation().getNotReasonReal().get().value
				: krqdtApplication.notReasonReal;
		krqdtApplication.dateTimeReflection = application.getReflectionInformation().getDateTimeReflection().isPresent()
				? application.getReflectionInformation().getDateTimeReflection().get()
				: krqdtApplication.dateTimeReflection;
		krqdtApplication.dateTimeReflectionReal = application.getReflectionInformation().getDateTimeReflectionReal()
				.isPresent() ? application.getReflectionInformation().getDateTimeReflectionReal().get()
						: krqdtApplication.dateTimeReflectionReal;
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
				.setParameter("companyID", companyId).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getList(c -> c.toDomain());
		return data;
	}

	@Override
	public List<Application_New> getBeforeApplication(String companyId, String employeeID, GeneralDate appDate,
			int appType, int prePostAtr) {
		return this.queryProxy().query(SELECT_BEFORE_APPLICATION, KrqdtApplication_New.class)
				.setParameter("companyID", companyId).setParameter("employeeID", employeeID)
				.setParameter("appDate", appDate).setParameter("applicationType", appType)
				.setParameter("prePostAtr", prePostAtr).getList(c -> c.toDomain());
	}

	/**
	 * @author hoatt get list application by sID
	 */
	@SneakyThrows
	@Override
	public List<Application_New> getListAppBySID(String companyId, String sID, GeneralDate startDate,
			GeneralDate endDate) {

		String sql = "select * from KRQDT_APPLICATION" + " where CID = ?" + " and APPLICANTS_SID = ?"
				+ " and APP_START_DATE <= ? and APP_END_DATE >= ?" + " and APP_TYPE in (0,1,2,4,6,10)";

		try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {

			stmt.setString(1, companyId);
			stmt.setString(2, sID);
			stmt.setDate(3, Date.valueOf(endDate.localDate()));
			stmt.setDate(4, Date.valueOf(startDate.localDate()));

			return new NtsResultSet(stmt.executeQuery()).getList(rec -> {

				KrqdtApplication_New ent = new KrqdtApplication_New();
				ent.krqdpApplicationPK = new KrqdpApplicationPK_New();
				ent.krqdpApplicationPK.companyID = rec.getString("CID");
				ent.krqdpApplicationPK.appID = rec.getString("APP_ID");
				ent.version = rec.getLong("EXCLUS_VER");
				ent.prePostAtr = rec.getInt("PRE_POST_ATR");
				ent.inputDate = rec.getGeneralDateTime("INPUT_DATE");
				ent.enteredPersonID = rec.getString("ENTERED_PERSON_SID");
				ent.reversionReason = rec.getString("REASON_REVERSION");
				ent.appDate = rec.getGeneralDate("APP_DATE");
				ent.appReason = rec.getString("APP_REASON");
				ent.appType = rec.getInt("APP_TYPE");
				ent.employeeID = rec.getString("APPLICANTS_SID");
				ent.startDate = rec.getGeneralDate("APP_START_DATE");
				ent.endDate = rec.getGeneralDate("APP_END_DATE");
				ent.stateReflection = rec.getInt("REFLECT_PLAN_STATE");
				ent.stateReflectionReal = rec.getInt("REFLECT_PER_STATE");
				ent.forcedReflection = rec.getInt("REFLECT_PLAN_ENFORCE_ATR");
				ent.forcedReflectionReal = rec.getInt("REFLECT_PER_ENFORCE_ATR");
				ent.notReason = rec.getInt("REFLECT_PLAN_SCHE_REASON");
				ent.notReasonReal = rec.getInt("REFLECT_PER_SCHE_REASON");
				ent.dateTimeReflection = rec.getGeneralDateTime("REFLECT_PLAN_TIME");
				ent.dateTimeReflectionReal = rec.getGeneralDateTime("REFLECT_PER_TIME");

				return ent.toDomain();

			});
		}
	}

	/**
	 * @author hoatt get List Application phuc vu CMM045
	 */
	@Override
	public List<Application_New> getListAppModeApprCMM045(String companyId, DatePeriod period, List<String> lstAppId,
			boolean unapprovalStatus, boolean approvalStatus, boolean denialStatus, boolean agentApprovalStatus,
			boolean remandStatus, boolean cancelStatus, List<Integer> lstType) {
		if (lstAppId.isEmpty()) {
			return new ArrayList<>();
		}
		List<Integer> lstState = new ArrayList<>();
		if (unapprovalStatus || approvalStatus || agentApprovalStatus || remandStatus) {
			lstState.add(0);
		}
		if (approvalStatus || agentApprovalStatus) {
			lstState.add(1);
		}
		if (approvalStatus || agentApprovalStatus) {
			lstState.add(2);
		}
		if (agentApprovalStatus || cancelStatus) {
			lstState.add(3);
			lstState.add(4);
		}
		if (denialStatus || agentApprovalStatus) {
			lstState.add(6);
		}

		List<Application_New> lstResult = new ArrayList<>();
		CollectionUtil.split(lstAppId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subListId -> {
			String subListAppId = NtsStatement.In.createParamsString(subListId);
			String lstTypeString = NtsStatement.In.createParamsString(lstType);
			String lstStateString = NtsStatement.In.createParamsString(lstState);

			String sql = "SELECT app.EXCLUS_VER, app.CID, app.APP_ID, app.PRE_POST_ATR, app.INPUT_DATE, app.ENTERED_PERSON_SID, app.REASON_REVERSION, app.APP_DATE, app.APP_REASON, "
					+ "app.APP_TYPE, app.APPLICANTS_SID, app.APP_START_DATE, app.APP_END_DATE, app.REFLECT_PLAN_STATE, app.REFLECT_PER_STATE, app.REFLECT_PLAN_ENFORCE_ATR, "
					+ "app.REFLECT_PER_ENFORCE_ATR, app.REFLECT_PLAN_SCHE_REASON, app.REFLECT_PER_SCHE_REASON, app.REFLECT_PLAN_TIME, app.REFLECT_PER_TIME "
					+ "FROM KRQDT_APPLICATION app " + "WHERE app.APP_ID IN (" + subListAppId + ") AND app.APP_TYPE IN ("
					+ lstTypeString + ") AND app.REFLECT_PER_STATE IN (" + lstStateString + ") AND app.CID = ?";

			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				int sizeListId = subListId.size();
				int sizeLstType = lstType.size();
				int sizeLstState = lstState.size();

				for (int i = 0; i < sizeListId; i++) {
					stmt.setString(i + 1, subListId.get(i));
				}

				for (int i = 0; i < sizeLstType; i++) {
					stmt.setInt(i + sizeListId + 1, lstType.get(i));
				}

				for (int i = 0; i < sizeLstState; i++) {
					stmt.setInt(i + sizeListId + sizeLstType + 1, lstState.get(i));
				}

				stmt.setString(sizeListId + sizeLstType + sizeLstState + 1, companyId);

				lstResult.addAll(new NtsResultSet(stmt.executeQuery()).getList(rs -> new KrqdtApplication_New(
						new KrqdpApplicationPK_New(rs.getString("CID"), rs.getString("APP_ID")),
						rs.getLong("EXCLUS_VER"), rs.getInt("PRE_POST_ATR"), rs.getGeneralDateTime("INPUT_DATE"),
						rs.getString("ENTERED_PERSON_SID"), rs.getString("REASON_REVERSION"),
						rs.getGeneralDate("APP_DATE"), rs.getString("APP_REASON"), rs.getInt("APP_TYPE"),
						rs.getString("APPLICANTS_SID"), rs.getGeneralDate("APP_START_DATE"),
						rs.getGeneralDate("APP_END_DATE"), rs.getInt("REFLECT_PLAN_STATE"),
						rs.getInt("REFLECT_PER_STATE"), rs.getInt("REFLECT_PLAN_ENFORCE_ATR"),
						rs.getInt("REFLECT_PER_ENFORCE_ATR"), rs.getInt("REFLECT_PLAN_SCHE_REASON"),
						rs.getInt("REFLECT_PER_SCHE_REASON"), rs.getGeneralDateTime("REFLECT_PLAN_TIME"),
						rs.getGeneralDateTime("REFLECT_PER_TIME")).toDomain()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		return lstResult;

	}

	/**
	 * get List Application Pre
	 */
	@Override
	public List<Application_New> getListAppPre(String companyId, String sID, GeneralDate appDate, int prePostAtr) {
		// TODO Auto-generated method stub
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<Application_New> getApplicationBySIDs(List<String> employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		List<Application_New> data = new ArrayList<>();
		CollectionUtil.split(employeeID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			data.addAll(this.queryProxy().query(SELECT_APP_BY_SIDS, KrqdtApplication_New.class)
					.setParameter("employeeID", subList).setParameter("startDate", startDate)
					.setParameter("endDate", endDate).getList(c -> c.toDomain()));
		});
		return data;
	}

	/**
	 * Request list No.236
	 */
	@Override
	public List<Application_New> getListApp(String sID, GeneralDate startDate, GeneralDate endDate) {
		List<Application_New> data = this.queryProxy().query(SELECT_APP_BY_CONDS, KrqdtApplication_New.class)
				.setParameter("employeeID", sID).setParameter("startDate", startDate).setParameter("endDate", endDate)
				.getList(c -> c.toDomain());

		return data;
	}

	/**
	 * RequestList 232 param 反映状態 ＝ 「反映済み」または「反映待ち」 RequestList 233 param 反映状態 ＝
	 * 「未承認」または「差戻し」 RequestList 234 param 反映状態 ＝ 「否認」 RequestList 235 param
	 * 反映状態 ＝ 「差戻し」
	 */
	private static final String SELECT_LIST_REFSTATUS = "SELECT a FROM KrqdtApplication_New a"
			+ " WHERE a.krqdpApplicationPK.companyID =:companyID" + " AND a.employeeID = :employeeID "
			+ " AND a.appDate >= :startDate AND a.appDate <= :endDate"
			+ " AND a.stateReflectionReal IN :listReflecInfor" + " ORDER BY a.appDate ASC," + " a.prePostAtr DESC";

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Application_New> getByListRefStatus(String companyID, String employeeID, GeneralDate startDate,
			GeneralDate endDate, List<Integer> listReflecInfor) {
		// TODO Auto-generated method stub
		if (listReflecInfor.size() == 0) {
			return Collections.emptyList();
		}
		List<Application_New> resultList = new ArrayList<>();
		CollectionUtil.split(listReflecInfor, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(SELECT_LIST_REFSTATUS, KrqdtApplication_New.class)
					.setParameter("companyID", companyID).setParameter("employeeID", employeeID)
					.setParameter("startDate", startDate).setParameter("endDate", endDate)
					.setParameter("listReflecInfor", subList).getList(x -> x.toDomain()));
		});
		resultList.sort(Comparator.comparing(Application_New::getPrePostAtr));
		return resultList;
	}

	@Override
	public List<Application_New> findByListID(String companyID, List<String> listAppID) {
		if (CollectionUtil.isEmpty(listAppID)) {
			return Collections.emptyList();
		}
		List<Application_New> resultList = new ArrayList<>();
		CollectionUtil.split(listAppID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(SELECT_APP_BY_LIST_ID, KrqdtApplication_New.class)
					.setParameter("listAppID", subList).setParameter("companyID", companyID)
					.getList(x -> x.toDomain()));
		});
		resultList.sort(Comparator.comparing(Application_New::getAppDate));
		return resultList;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Application_New> getListLateOrLeaveEarly(String companyID, String employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		return this.queryProxy().query(SELECT_LATE_LEAVE, KrqdtApplication_New.class)
				.setParameter("companyID", companyID).setParameter("employeeID", employeeID)
				.setParameter("startDate", startDate).setParameter("endDate", endDate).getList(x -> x.toDomain());
	}

	@Override
	public List<Application_New> getByPeriodReflectType(String sid, DatePeriod dateData, List<Integer> reflect,
			List<Integer> appType) {
		List<Application_New> resultList = new ArrayList<>();
		/*CollectionUtil.split(reflect, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstReflect -> {最大２
			CollectionUtil.split(appType, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstAppType -> {最大１５*/
		String subIn1 = NtsStatement.In.createParamsString(appType);
		String subIn2 = NtsStatement.In.createParamsString(reflect);
		String sql = "SELECT * FROM KRQDT_APPLICATION "
				+ "WHERE APPLICANTS_SID = ? "
				+ " AND APP_START_DATE <= ? "
				+ " AND APP_END_DATE >= ? "
				+ " AND APP_TYPE IN (" + subIn1 + ") "
				+ " AND REFLECT_PER_STATE IN (" + subIn2 + ") "
				+ " ORDER BY INPUT_DATE ASC";
		try(val stmt = this.connection().prepareStatement(sql)){
			
			stmt.setString(1, sid);
			stmt.setDate(2, Date.valueOf(dateData.end().localDate()));
			stmt.setDate(3, Date.valueOf(dateData.start().localDate()));
			int inCount = 3;
			for (int i = 0; i < appType.size(); i++) {
				stmt.setInt(inCount + i + 1, appType.get(i));
			}
			inCount +=  appType.size();
			for (int i = 0; i < reflect.size(); i++) {
				stmt.setInt(inCount + i + 1, reflect.get(i));
			}
			List<Application_New> resultListTmp = entityToDomain(stmt);
			resultList.addAll(resultListTmp);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
			/*});
		});*/
		return resultList;
	}

	/**
	 * @author hoatt 申請者ID＝社員ID（リスト） または 入力者ID＝社員ID（リスト） get By List SID
	 * @param companyId
	 * @param lstSID
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	@Override
	public List<Application_New> getByListSID(String companyId, List<String> lstSID, GeneralDate sDate,
			GeneralDate eDate) {
		if (lstSID.isEmpty()) {
			return new ArrayList<>();
		}
		List<KrqdtApplication_New> resultList = new ArrayList<>();
		CollectionUtil.split(lstSID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(SELECT_BY_LIST_SID, KrqdtApplication_New.class)
					.setParameter("companyID", companyId).setParameter("lstSID", subList)
					.setParameter("startDate", sDate).setParameter("endDate", eDate).getList());
		});
		return resultList.stream().map(c -> c.toDomain()).collect(Collectors.toList());
	}

	/**
	 * @author hoatt 申請者ID＝社員ID（リスト） get By List Applicant
	 * @param companyId
	 * @param lstSID
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	@Override
	public List<Application_New> getByListApplicant(String companyId, List<String> lstSID, GeneralDate sDate,
			GeneralDate eDate, List<Integer> lstType) {
		if (lstSID.isEmpty() || lstType.isEmpty()) {
			return new ArrayList<>();
		}
		List<KrqdtApplication_New> resultList = new ArrayList<>();
		CollectionUtil.split(lstSID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(SELECT_BY_LIST_APPLICANT, KrqdtApplication_New.class)
					.setParameter("companyID", companyId).setParameter("lstSID", subList)
					.setParameter("startDate", sDate).setParameter("endDate", eDate).setParameter("lstType", lstType)
					.getList());
		});
		return resultList.stream().map(c -> c.toDomain()).collect(Collectors.toList());
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Application_New> getListAppByType(String companyId, String employeeID, GeneralDate startDate,
			GeneralDate endDate, int prePostAtr, int appType, List<Integer> lstRef) {
		if (lstRef.isEmpty()) {
			return new ArrayList<>();
		}
		List<Application_New> resultList = new ArrayList<>();
		CollectionUtil.split(lstRef, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(FIND_BY_REF_PERIOD_TYPE, KrqdtApplication_New.class)
					.setParameter("companyID", companyId).setParameter("employeeID", employeeID)
					.setParameter("prePostAtr", prePostAtr).setParameter("startDate", startDate)
					.setParameter("endDate", endDate).setParameter("appType", appType).setParameter("lstRef", subList)
					.getList(c -> c.toDomain()));
		});
		resultList.sort((o1, o2) -> {
			int tmp = o1.getAppType().value - o2.getAppType().value;
			if (tmp != 0)
				return tmp;
			return o2.getInputDate().compareTo(o1.getInputDate()); // DESC
		});
		return resultList;
	}

	@Override
	@SneakyThrows
	public List<Application_New> getAppForReflect(String sid, DatePeriod dateData, List<Integer> recordStatus,
			List<Integer> scheStatus, List<Integer> appType) {
		List<Application_New> resultList = new ArrayList<>();
		CollectionUtil.split(recordStatus, SPLIT_650, lstRefReal -> {
			String subIn1 = NtsStatement.In.createParamsString(lstRefReal);
			CollectionUtil.split(scheStatus, SPLIT_650, lstRef -> {
				String subIn2 = NtsStatement.In.createParamsString(lstRef);
				CollectionUtil.split(appType, SPLIT_650, lstAppType -> {
					String subIn3 = NtsStatement.In.createParamsString(lstAppType);
					String sql = "SELECT * FROM KRQDT_APPLICATION " + "WHERE  APPLICANTS_SID = ? "
							+ " AND APP_START_DATE <= ? " + " AND APP_END_DATE >= ? " + " AND APP_TYPE IN (" + subIn3
							+ ") " + " AND (REFLECT_PLAN_STATE IN (" + subIn1 + ") " + " OR REFLECT_PER_STATE IN ("
							+ subIn2 + "))" + " ORDER BY INPUT_DATE ASC";
					try (val stmt = this.connection().prepareStatement(sql)) {
						stmt.setString(1, sid);
						stmt.setDate(2, Date.valueOf(dateData.end().localDate()));
						stmt.setDate(3, Date.valueOf(dateData.start().localDate()));
						int inCount = 3;
						for (int i = 0; i < lstAppType.size(); i++) {
							stmt.setInt(inCount + i + 1, lstAppType.get(i));
						}
						inCount += lstAppType.size();
						for (int i = 0; i < lstRefReal.size(); i++) {
							stmt.setInt(inCount + i + 1, lstRefReal.get(i));
						}
						inCount += lstRefReal.size();
						for (int i = 0; i < lstRef.size(); i++) {
							stmt.setInt(inCount + i + 1, lstRef.get(i));
						}
						List<Application_New> resultListTmp = entityToDomain(stmt);
						resultList.addAll(resultListTmp);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				});
			});
		});
		return resultList;
	}

	@Override
	@SneakyThrows
	public List<Application_New> getByListDateReflectType(String sid, List<GeneralDate> dateData, List<Integer> reflect,
			List<Integer> appType) {
		List<Application_New> resultList = new ArrayList<>();
		CollectionUtil.split(dateData, SPLIT_650, lstDate -> {
			String subIn1 = NtsStatement.In.createParamsString(lstDate);
			CollectionUtil.split(reflect, SPLIT_650, lstRef -> {
				String subIn2 = NtsStatement.In.createParamsString(lstRef);
				CollectionUtil.split(appType, SPLIT_650, lstApp -> {
					String subIn3 = NtsStatement.In.createParamsString(lstApp);
					String sql = "SELECT * FROM KRQDT_APPLICATION " + "WHERE APPLICANTS_SID = ?" + " AND APP_DATE IN ("
							+ subIn1 + ") " + " AND REFLECT_PER_STATE IN (" + subIn2 + ")" + " AND APP_TYPE IN ("
							+ subIn3 + ") ";
					try (val stmt = this.connection().prepareStatement(sql)) {
						stmt.setString(1, sid);
						for (int i = 0; i < lstDate.size(); i++) {
							stmt.setDate(i + 2, Date.valueOf(lstDate.get(i).localDate()));
						}
						for (int i = 0; i < lstRef.size(); i++) {
							stmt.setInt(lstDate.size() + i + 2, lstRef.get(i));
						}
						int inCount = lstDate.size() + lstRef.size() + 1;
						for (int i = 0; i < lstApp.size(); i++) {
							stmt.setInt(inCount + i + 1, lstApp.get(i));
						}

						List<Application_New> resultListTmp = entityToDomain(stmt);
						resultList.addAll(resultListTmp);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				});
			});
		});
		return resultList;
	}

	private List<Application_New> entityToDomain(java.sql.PreparedStatement stmt) throws SQLException {
		List<Application_New> resultListTmp = new NtsResultSet(stmt.executeQuery()).getList(x -> {
			ReflectionInformation_New reflectInfor = new ReflectionInformation_New(
					EnumAdaptor.valueOf(x.getInt("REFLECT_PLAN_STATE"), ReflectedState_New.class),
					EnumAdaptor.valueOf(x.getInt("REFLECT_PER_STATE"), ReflectedState_New.class),
					EnumAdaptor.valueOf(x.getInt("REFLECT_PLAN_ENFORCE_ATR"), NotUseAtr.class),
					EnumAdaptor.valueOf(x.getInt("REFLECT_PER_ENFORCE_ATR"), NotUseAtr.class),
					x.getInt("REFLECT_PLAN_SCHE_REASON") == null ? Optional.empty()
							: Optional.ofNullable(EnumAdaptor.valueOf(x.getInt("REFLECT_PLAN_SCHE_REASON"),
									ReasonNotReflect.class)),
					x.getInt("REFLECT_PER_SCHE_REASON") == null ? Optional.empty()
							: Optional.ofNullable(EnumAdaptor.valueOf(x.getInt("REFLECT_PER_SCHE_REASON"),
									ReasonNotReflectDaily.class)),
					Optional.ofNullable(x.getGeneralDateTime("REFLECT_PLAN_TIME")),
					Optional.ofNullable(x.getGeneralDateTime("REFLECT_PER_TIME")));
			return new Application_New(x.getLong("EXCLUS_VER"), x.getString("CID"), x.getString("APP_ID"),
					EnumAdaptor.valueOf(x.getInt("PRE_POST_ATR"), PrePostAtr.class), x.getGeneralDateTime("INPUT_DATE"),
					x.getString("ENTERED_PERSON_SID"), new AppReason(x.getString("REASON_REVERSION")),
					x.getGeneralDate("APP_DATE"), new AppReason(x.getString("APP_REASON")),
					EnumAdaptor.valueOf(x.getInt("APP_TYPE"), ApplicationType.class), x.getString("APPLICANTS_SID"),
					Optional.ofNullable(x.getGeneralDate("APP_START_DATE")),
					Optional.ofNullable(x.getGeneralDate("APP_END_DATE")), reflectInfor);
		});
		return resultListTmp;
	}

	@Override
	@SneakyThrows
	public Map<String, Integer> getParamCMMS45(String companyId, String configName, List<String> subName) {
		String lstsubName = "";
		for (int i = 0; i < subName.size(); i++) {
			lstsubName += "'" + subName.get(i) + "'";
			if (i != (subName.size() - 1)) {
				lstsubName += ",";
			}
		}
		String sql = "SELECT * FROM CISCT_SYSTEM_CONFIG_K " + "WHERE CID = 'companyId' "
				+ "AND CONFIG_NAME = 'configName' " + "AND SUB_NAME IN (lstsubName)";
		sql = sql.replaceAll("companyId", companyId);
		sql = sql.replaceAll("configName", configName);
		sql = sql.replaceAll("lstsubName", lstsubName);
		Map<String, Integer> mapResult = new HashMap<>();
		try (PreparedStatement pstatement = this.connection().prepareStatement(sql)) {
			ResultSet rs = pstatement.executeQuery();
			while (rs.next()) {
				mapResult.put(rs.getString("SUB_NAME"), Integer.valueOf(rs.getString("CONFIG_VALUE")));
			}
		}
		return mapResult;
	}
}