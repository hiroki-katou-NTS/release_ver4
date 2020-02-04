package nts.uk.ctx.workflow.infra.repository.approverstatemanagement;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApproverInfor;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdpApprovalPhaseStatePK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdpApprovalRootStatePK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdpApproverStatePK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtAppRootStateSimple;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtApprovalPhaseState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtApprovalRootState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtApproverState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtFullJoinState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmday.WwfdpApprovalFrameDayPK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmday.WwfdpApprovalPhaseDayPK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmday.WwfdpApprovalRootDayPK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmday.WwfdpApproverDayPK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmday.WwfdtAppRootDaySimple;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmday.WwfdtApprovalFrameDay;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmday.WwfdtApprovalPhaseDay;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmday.WwfdtApprovalRootDay;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmday.WwfdtApproverDay;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmmonth.WwfdpApprovalFrameMonthPK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmmonth.WwfdpApprovalPhaseMonthPK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmmonth.WwfdpApprovalRootMonthPK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmmonth.WwfdpApproverMonthPK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmmonth.WwfdtApprovalFrameMonth;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmmonth.WwfdtApprovalPhaseMonth;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmmonth.WwfdtApprovalRootMonth;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmmonth.WwfdtApproverMonth;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaApprovalRootStateRepository extends JpaRepository implements ApprovalRootStateRepository {

	private static final String BASIC_SELECT;

	private static final String SELECT_APP_BY_ID;
	private static final String SELECT_CF_DAY_BY_ID;
	private static final String SELECT_CF_MONTH_BY_ID;

	// private static final String SELECT_APP_BY_IDS;
	// private static final String SELECT_CF_DAY_BY_IDS;
	// private static final String SELECT_CF_MONTH_BY_IDS;
	//
	// private static final String SELECT_APPS_BY_ID;

	private static final String SELECT_APP_BY_DATE;
	private static final String SELECT_CF_DAY_BY_DATE;
	private static final String SELECT_CF_MONTH_BY_DATE;

	private static final String SELECT_SIMPLE_APP_BY_DATE;
	// private static final String SELECT_SIMPLE_CF_DAY_BY_DATE;
	// private static final String SELECT_SIMPLE_CF_MONTH_BY_DATE;

	private static final String SELECT_APP_BY_EMP_DATE;
	private static final String SELECT_CF_DAY_BY_EMP_DATE;
	private static final String SELECT_CF_MONTH_BY_EMP_DATE;

	private static final String SELECT_CF_DAY_BY_EMP_DATE_SP;

	private static final String SELECT_BY_LIST_EMP_DATE;

	private static final String SELECT_APPS_BY_EMP_AND_DATES;
	private static final String SELECT_CFS_DAY_BY_EMP_AND_DATES;
	private static final String SELECT_CFS_MONTH_BY_EMP_AND_DATES;

	private static final String SELECT_APPS_BY_APPROVER;
	private static final String SELECT_CFS_DAY_BY_APPROVER;
	private static final String SELECT_CFS_MONTH_BY_APPROVER;
	private static final String FIND_PHASE_APPROVAL_MAX = "SELECT a FROM WwfdtApprovalPhaseState a"
			+ " WHERE a.wwfdpApprovalPhaseStatePK.rootStateID = :appID"
			+ " AND a.approvalAtr = 1 ORDER BY a.wwfdpApprovalPhaseStatePK.phaseOrder ASC";
	static {
		StringBuilder builderString = new StringBuilder();
		/*builderString.append("SELECT root.ROOT_STATE_ID, root.EMPLOYEE_ID, root.APPROVAL_RECORD_DATE,");
		builderString.append("phase.PHASE_ORDER, phase.APP_PHASE_ATR, phase.APPROVAL_FORM,");
		builderString.append(
				"frame.FRAME_ORDER, frame.APP_FRAME_ATR, frame.CONFIRM_ATR, frame.APPROVER_ID, frame.REPRESENTER_ID, frame.APPROVAL_DATE, frame.APPROVAL_REASON,");
		builderString.append("approver.APPROVER_CHILD_ID, approver.CID FROM WWFDT_APPROVAL_ROOT_STATE root ");
		builderString.append("LEFT JOIN WWFDT_APPROVAL_PHASE_ST phase ");
		builderString.append("ON root.ROOT_STATE_ID = phase.ROOT_STATE_ID ");
		builderString.append("LEFT JOIN WWFDT_APPROVAL_FRAME frame ");
		builderString.append("ON phase.ROOT_STATE_ID = frame.ROOT_STATE_ID ");
		builderString.append("AND phase.PHASE_ORDER = frame.PHASE_ORDER ");
		builderString.append("LEFT JOIN WWFDT_APPROVER_STATE approver ");
		builderString.append("ON frame.ROOT_STATE_ID = approver.ROOT_STATE_ID ");
		builderString.append("AND frame.PHASE_ORDER = approver.PHASE_ORDER ");
		builderString.append("AND frame.FRAME_ORDER = approver.FRAME_ORDER");*/
		
		builderString.append("SELECT root.ROOT_STATE_ID, root.EMPLOYEE_ID, root.APPROVAL_RECORD_DATE, ");
		builderString.append("phase.PHASE_ORDER, phase.APP_PHASE_ATR, phase.APPROVAL_FORM, ");
		builderString.append("approver.APPROVER_ORDER, approver.APPROVER_ID, approver.APPROVAL_ATR, approver.CONFIRM_ATR, ");
		builderString.append("approver.AGENT_ID, approver.APPROVAL_DATE, approver.APPROVAL_REASON, approver.APP_DATE ");
		builderString.append("FROM WWFDT_APPROVAL_ROOT_STATE root ");
		builderString.append("LEFT JOIN WWFDT_APPROVAL_PHASE_ST phase ");
		builderString.append("ON root.ROOT_STATE_ID = phase.ROOT_STATE_ID "); 
		builderString.append("LEFT JOIN WWFDT_APPROVER_STATE approver ");
		builderString.append("ON phase.ROOT_STATE_ID = approver.ROOT_STATE_ID ");
		builderString.append("AND phase.PHASE_ORDER = approver.PHASE_ORDER");
		
		BASIC_SELECT = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootState e");
		builderString.append(" WHERE e.rootStateID = :rootStateID");
		SELECT_APP_BY_ID = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootDay e");
		builderString.append(" WHERE e.rootStateID = :rootStateID");
		SELECT_CF_DAY_BY_ID = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootMonth e");
		builderString.append(" WHERE e.rootStateID = :rootStateID");
		SELECT_CF_MONTH_BY_ID = builderString.toString();

		// builderString = new StringBuilder();
		// builderString.append("SELECT e");
		// builderString.append(" FROM WwfdtApprovalRootState e");
		// builderString.append(" WHERE e.wwfdpApprovalRootStatePK.rootStateID
		// IN :rootStateIDs");
		// SELECT_APP_BY_IDS = builderString.toString();
		//
		// builderString = new StringBuilder();
		// builderString.append("SELECT e");
		// builderString.append(" FROM WwfdtApprovalRootDay e");
		// builderString.append(" WHERE e.wwfdpApprovalRootDayPK.rootStateID IN
		// :rootStateIDs");
		// SELECT_CF_DAY_BY_IDS = builderString.toString();
		//
		// builderString = new StringBuilder();
		// builderString.append("SELECT e");
		// builderString.append(" FROM WwfdtApprovalRootMonth e");
		// builderString.append(" WHERE e.wwfdpApprovalRootMonthPK.rootStateID
		// IN :rootStateIDs");
		// SELECT_CF_MONTH_BY_IDS = builderString.toString();
		//
		// builderString = new StringBuilder();
		// builderString.append("SELECT c");
		// builderString.append(" FROM WwfdtApprovalRootState c");
		// builderString.append(" WHERE c.wwfdpApprovalRootStatePK.rootStateID
		// IN ");
		// builderString.append("(SELECT DISTINCT
		// a.wwfdpApprovalRootStatePK.rootStateID");
		// builderString.append(" FROM WwfdtAppRootStateSimple a");
		// builderString.append(" JOIN WwfdtAppStateSimple b ");
		// builderString.append(" ON a.wwfdpApprovalRootStatePK.rootStateID =
		// b.wwfdpApproverStatePK.rootStateID");
		// builderString.append(" WHERE (b.wwfdpApproverStatePK.approverID =
		// :approverID");
		// builderString.append(" OR b.wwfdpApproverStatePK.approverID IN");
		// builderString.append(" (SELECT d.cmmmtAgentPK.employeeId FROM
		// CmmmtAgent d WHERE d.agentSid1 = :approverID))");
		// builderString.append(" AND a.wwfdpApprovalRootStatePK.rootStateID IN
		// :rootStateIDs)");
		// SELECT_APPS_BY_ID = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootState e");
		builderString.append(" WHERE e.recordDate >= :startDate");
		builderString.append(" AND e.recordDate <= :endDate");
		SELECT_APP_BY_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootDay e");
		builderString.append(" WHERE e.recordDate >= :startDate");
		builderString.append(" AND e.recordDate <= :endDate");
		SELECT_CF_DAY_BY_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootMonth e");
		builderString.append(" WHERE e.recordDate >= :startDate");
		builderString.append(" AND e.recordDate <= :endDate");
		SELECT_CF_MONTH_BY_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtAppRootStateSimple e");
		builderString.append(" WHERE e.recordDate >= :startDate");
		builderString.append(" AND e.recordDate <= :endDate");
		SELECT_SIMPLE_APP_BY_DATE = builderString.toString();

		// builderString = new StringBuilder();
		// builderString.append("SELECT e");
		// builderString.append(" FROM WwfdtAppRootDaySimple e");
		// builderString.append(" WHERE e.recordDate >= :startDate");
		// builderString.append(" AND e.recordDate <= :endDate");
		// SELECT_SIMPLE_CF_DAY_BY_DATE = builderString.toString();
		//
		// builderString = new StringBuilder();
		// builderString.append("SELECT e");
		// builderString.append(" FROM WwfdtAppRootMonthSimple e");
		// builderString.append(" WHERE e.recordDate >= :startDate");
		// builderString.append(" AND e.recordDate <= :endDate");
		// SELECT_SIMPLE_CF_MONTH_BY_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootState e");
		builderString.append(" WHERE e.recordDate >= :startDate");
		builderString.append(" AND e.recordDate <= :endDate");
		builderString.append(" AND e.employeeID = :employeeID");
		SELECT_APP_BY_EMP_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootDay e");
		builderString.append(" WHERE e.recordDate >= :startDate");
		builderString.append(" AND e.recordDate <= :endDate");
		builderString.append(" AND e.employeeID = :employeeID");
		SELECT_CF_DAY_BY_EMP_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootMonth e");
		builderString.append(" WHERE e.recordDate >= :startDate");
		builderString.append(" AND e.recordDate <= :endDate");
		builderString.append(" AND e.employeeID = :employeeID");
		SELECT_CF_MONTH_BY_EMP_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtAppRootDaySimple e");
		builderString.append(" WHERE e.recordDate >= :startDate");
		builderString.append(" AND e.recordDate <= :endDate");
		builderString.append(" AND e.employeeID = :employeeID");
		SELECT_CF_DAY_BY_EMP_DATE_SP = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootState e");
		builderString.append(" WHERE e.recordDate >= :startDate");
		builderString.append(" AND e.recordDate <= :endDate");
		builderString.append(" AND e.rootType = :rootType");
		builderString.append(" AND e.employeeID IN :employeeID");
		SELECT_BY_LIST_EMP_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootState e");
		builderString.append(" WHERE e.recordDate IN :recordDate");
		builderString.append(" AND e.employeeID IN :employeeID");
		SELECT_APPS_BY_EMP_AND_DATES = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootDay e");
		builderString.append(" WHERE e.recordDate IN :recordDate");
		builderString.append(" AND e.employeeID IN :employeeID");
		SELECT_CFS_DAY_BY_EMP_AND_DATES = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootMonth e");
		builderString.append(" WHERE e.recordDate IN :recordDate");
		builderString.append(" AND e.employeeID IN :employeeID");
		SELECT_CFS_MONTH_BY_EMP_AND_DATES = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT c");
		builderString.append(" FROM WwfdtApprovalRootState c");
		builderString.append(" WHERE c.rootStateID IN ");
		builderString.append("(SELECT DISTINCT a.wwfdpApprovalRootStatePK.rootStateID");
		builderString.append(" FROM WwfdtAppRootStateSimple a JOIN WwfdtAppStateSimple b ");
		builderString.append(" ON a.rootStateID = b.wwfdpApproverStatePK.rootStateID ");
		builderString.append(" WHERE (b.wwfdpApproverStatePK.approverID = :approverID");
		builderString.append(" OR b.wwfdpApproverStatePK.approverID IN");
		builderString.append(" (SELECT d.cmmmtAgentPK.employeeId FROM CmmmtAgent d WHERE d.agentSid1 = :approverID");
		builderString.append(" AND :systemDate <= d.endDate AND :systemDate >= d.startDate))");
		builderString.append(" AND b.companyID = :companyID");
		builderString.append(" AND b.recordDate >= :startDate AND b.recordDate <= :endDate)");
		SELECT_APPS_BY_APPROVER = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT c");
		builderString.append(" FROM WwfdtApprovalRootDay c");
		builderString.append(" WHERE c.wwfdpApprovalRootDayPK.rootStateID IN ");
		builderString.append("(SELECT DISTINCT a.wwfdpApprovalRootDayPK.rootStateID");
		builderString.append(" FROM WwfdtAppRootDaySimple a JOIN WwfdtApproverDaySimple b ");
		builderString.append(" ON a.wwfdpApprovalRootDayPK.rootStateID = b.wwfdpApproverDayPK.rootStateID ");
		builderString.append(" WHERE (b.wwfdpApproverDayPK.approverID = :approverID");
		builderString.append(" OR b.wwfdpApproverDayPK.approverID IN");
		builderString.append(" (SELECT d.cmmmtAgentPK.employeeId FROM CmmmtAgent d WHERE d.agentSid1 = :approverID");
		builderString.append(" AND :systemDate <= d.endDate AND :systemDate >= d.startDate))");
		builderString.append(" AND b.companyID = :companyID");
		builderString.append(" AND b.recordDate >= :startDate AND b.recordDate <= :endDate)");
		SELECT_CFS_DAY_BY_APPROVER = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT c");
		builderString.append(" FROM WwfdtApprovalRootMonth c");
		builderString.append(" WHERE c.wwfdpApprovalRootMonthPK.rootStateID IN ");
		builderString.append("(SELECT DISTINCT a.wwfdpApprovalRootMonthPK.rootStateID");
		builderString.append(" FROM WwfdtApprovalRootMonth a JOIN WwfdtApproverMonth b ");
		builderString.append(" ON a.wwfdpApprovalRootMonthPK.rootStateID = b.wwfdpApproverMonthPK.rootStateID ");
		builderString.append(" WHERE (b.wwfdpApproverMonthPK.approverID = :approverID");
		builderString.append(" OR b.wwfdpApproverMonthPK.approverID IN");
		builderString.append(" (SELECT d.cmmmtAgentPK.employeeId FROM CmmmtAgent d WHERE d.agentSid1 = :approverID");
		builderString.append(" AND :systemDate <= d.endDate AND :systemDate >= d.startDate))");
		builderString.append(" AND b.companyID = :companyID");
		builderString.append(" AND b.recordDate >= :startDate AND b.recordDate <= :endDate)");
		SELECT_CFS_MONTH_BY_APPROVER = builderString.toString();

	}

	@Override
	@SneakyThrows
	public Optional<ApprovalRootState> findByID(String rootStateID, Integer rootType) {
		String query = BASIC_SELECT + " WHERE root.ROOT_STATE_ID = 'rootStateID'";
		query = query.replaceFirst("rootStateID", rootStateID);
		try (PreparedStatement pstatement = this.connection().prepareStatement(query)) {
			List<WwfdtFullJoinState> listFullData = WwfdtFullJoinState.fromResultSet(pstatement.executeQuery());
			List<ApprovalRootState> listAppRootState = WwfdtFullJoinState.toDomain(listFullData);
			if (CollectionUtil.isEmpty(listAppRootState)) {
				return Optional.empty();
			} else {
				return Optional.of(listAppRootState.get(0));
			}
		}
		
	}

	@Override
	public List<ApprovalRootState> findEmploymentApps(List<String> rootStateIDs, String approverID) {
		String companyID = AppContexts.user().companyId();
		List<ApprovalRootState> result = new ArrayList<>();
		CollectionUtil.split(rootStateIDs, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {

			internalQuery(approverID, companyID, result, subList);

		});
		return result;
	}

	@SneakyThrows
	private void internalQuery(String approverID, String companyID, List<ApprovalRootState> result,
			List<String> subList) {
		GeneralDate sysDate = GeneralDate.today();
		String rootStateIDLst = "";

		for (int i = 0; i < subList.size(); i++) {
			rootStateIDLst += "'" + subList.get(i) + "'";
			if (i != (subList.size() - 1)) {
				rootStateIDLst += ",";
			}
		}
		String query = "SELECT root.ROOT_STATE_ID, root.EMPLOYEE_ID, root.APPROVAL_RECORD_DATE, "
				+ "phaseJoin.PHASE_ORDER, phaseJoin.APPROVAL_FORM, phaseJoin.APP_PHASE_ATR, "
				+ "phaseJoin.FRAME_ORDER, phaseJoin.APP_FRAME_ATR, phaseJoin.CONFIRM_ATR, phaseJoin.APPROVER_ID, phaseJoin.REPRESENTER_ID, "
				+ "phaseJoin.APPROVAL_DATE, phaseJoin.APPROVAL_REASON, phaseJoin.APPROVER_CHILD_ID "
				+ "FROM WWFDT_APPROVAL_ROOT_STATE root " + "LEFT JOIN "
				+ "(SELECT phase.ROOT_STATE_ID, phase.PHASE_ORDER, phase.APPROVAL_FORM, phase.APP_PHASE_ATR, "
				+ "frameJoin.FRAME_ORDER, frameJoin.APP_FRAME_ATR, frameJoin.CONFIRM_ATR, frameJoin.APPROVER_ID, frameJoin.REPRESENTER_ID, "
				+ "frameJoin.APPROVAL_DATE, frameJoin.APPROVAL_REASON, frameJoin.APPROVER_CHILD_ID "
				+ "FROM WWFDT_APPROVAL_PHASE_ST phase " + "LEFT JOIN ( "
				+ "SELECT frame.ROOT_STATE_ID, frame.PHASE_ORDER, frame.FRAME_ORDER, frame.APP_FRAME_ATR, frame.CONFIRM_ATR, frame.APPROVER_ID, "
				+ "frame.REPRESENTER_ID, frame.APPROVAL_DATE, frame.APPROVAL_REASON, approver.APPROVER_CHILD_ID "
				+ "FROM WWFDT_APPROVAL_FRAME frame " + "LEFT JOIN " + "WWFDT_APPROVER_STATE approver "
				+ "ON frame.ROOT_STATE_ID = approver.ROOT_STATE_ID " + "AND frame.PHASE_ORDER = approver.PHASE_ORDER "
				+ "AND frame.FRAME_ORDER = approver.FRAME_ORDER) " + "AS frameJoin "
				+ "ON phase.ROOT_STATE_ID = frameJoin.ROOT_STATE_ID "
				+ "AND phase.PHASE_ORDER = frameJoin.PHASE_ORDER) " + "AS phaseJoin "
				+ "ON root.ROOT_STATE_ID = phaseJoin.ROOT_STATE_ID " + "WHERE root.ROOT_STATE_ID IN ( "
				+ "SELECT DISTINCT c.ROOT_STATE_ID FROM ( "
				+ "SELECT a.ROOT_STATE_ID FROM WWFDT_APPROVER_STATE a WHERE a.APPROVER_CHILD_ID = 'approverID' "
				+ "UNION ALL " + "SELECT b.ROOT_STATE_ID FROM WWFDT_APPROVER_STATE b WHERE b.APPROVER_CHILD_ID IN "
				+ "( SELECT c.SID FROM CMMMT_AGENT c where c.AGENT_SID1 = 'approverID' and c.START_DATE <= 'sysDate' and c.END_DATE >= 'sysDate')) c "
				+ "WHERE c.ROOT_STATE_ID IN (rootStateIDs))";

		query = query.replaceAll("approverID", approverID);
		query = query.replaceFirst("rootStateIDs", rootStateIDLst);
		query = query.replaceAll("sysDate", sysDate.toString());
		List<WwfdtFullJoinState> listFullData = new ArrayList<>();
		try (PreparedStatement pstatement = this.connection().prepareStatement(query)) {
			listFullData = WwfdtFullJoinState.fromResultSet(pstatement.executeQuery());
		}
		List<ApprovalRootState> entityRoot = WwfdtFullJoinState.toDomain(listFullData);
		result.addAll(entityRoot);
	}

	@Override
	public Optional<ApprovalRootState> findEmploymentApp(String rootStateID) {
		return this.queryProxy().query(SELECT_APP_BY_ID, WwfdtApprovalRootState.class)
				.setParameter("rootStateID", rootStateID).getSingle(x -> x.toDomain());
	}

	@Override
	public void insert(String companyID, ApprovalRootState approvalRootState, Integer rootType) {
		switch (rootType) {
		case 1:
			this.commandProxy().insert(WwfdtApprovalRootDay.fromDomain(companyID, approvalRootState));
			break;
		case 2:
			this.commandProxy().insert(WwfdtApprovalRootMonth.fromDomain(companyID, approvalRootState));
			break;
		default:
			this.commandProxy().insert(WwfdtApprovalRootState.fromDomain(companyID, approvalRootState));
		}
		this.getEntityManager().flush();
	}

	@Override
	public void update(ApprovalRootState root, Integer rootType) {
		WwfdtApprovalRootState wwfdtApprovalRootState = this.queryProxy()
				.find(root.getRootStateID(),WwfdtApprovalRootState.class).get();
		wwfdtApprovalRootState.listWwfdtPhase = root.getListApprovalPhaseState().stream()
				.map(x -> updateEntityWwfdtApprovalPhaseState(root.getRootStateID(), x)).collect(Collectors.toList());
		this.commandProxy().update(wwfdtApprovalRootState);
		this.getEntityManager().flush();
	}

	@Override
	public void delete(String rootStateID, Integer rootType) {
		switch (rootType) {
		case 1:
			this.commandProxy().remove(WwfdtApprovalRootDay.class, new WwfdpApprovalRootDayPK(rootStateID));
			break;
		case 2:
			this.commandProxy().remove(WwfdtApprovalRootMonth.class, new WwfdpApprovalRootMonthPK(rootStateID));
			break;
		default:
			this.commandProxy().remove(WwfdtApprovalRootState.class, rootStateID);
		}
	}

	private WwfdtApprovalPhaseState updateEntityWwfdtApprovalPhaseState(String rootId, ApprovalPhaseState phase) {
		WwfdtApprovalPhaseState entityPhase = this.queryProxy().find(
				new WwfdpApprovalPhaseStatePK(rootId, phase.getPhaseOrder()), WwfdtApprovalPhaseState.class).get();
		entityPhase.approvalAtr = phase.getApprovalAtr().value;
		entityPhase.approvalForm = phase.getApprovalForm().value;
		List<WwfdtApproverState> lstEntityApprover = new ArrayList<>();
		for (ApprovalFrame frame : phase.getListApprovalFrame()) {
			lstEntityApprover.addAll(this.updateEntityWwfdtApprovalFrame(rootId, phase.getPhaseOrder(), frame));
		}
		entityPhase.listWwfdtApprover = lstEntityApprover;
		return entityPhase;
	}

	private List<WwfdtApproverState> updateEntityWwfdtApprovalFrame(String rootId, int phaseOrder, ApprovalFrame frame) {
		List<ApproverInfor> lstApprover = frame.getLstApproverInfo();
		List<WwfdtApproverState> lstEntityApprover = new ArrayList<>();
		for (ApproverInfor approver : lstApprover) {
			WwfdtApproverState entityApprover = this.queryProxy()
					.find(new WwfdpApproverStatePK(rootId, phaseOrder,
							frame.getFrameOrder(), approver.getApproverID()), WwfdtApproverState.class)
					.get();
			entityApprover.approvalAtr = approver.getApprovalAtr().value;
			entityApprover.confirmAtr = frame.getConfirmAtr().value;
			entityApprover.wwfdpApprovrStatePK.approverId = approver.getApproverID();
			entityApprover.agentID = approver.getAgentID();
			entityApprover.approvalDate = approver.getApprovalDate();
			entityApprover.approvalReason = approver.getApprovalReason();
			lstEntityApprover.add(entityApprover);
		}
		return lstEntityApprover;
	}

	private WwfdtApprovalPhaseDay updateEntityWwfdtApprovalPhaseDay(String rootId, ApprovalPhaseState phase) {
		WwfdtApprovalPhaseDay wwfdtApprovalPhaseDay = this.queryProxy().find(
				new WwfdpApprovalPhaseDayPK(rootId, phase.getPhaseOrder()),
				WwfdtApprovalPhaseDay.class).get();
		wwfdtApprovalPhaseDay.approvalAtr = phase.getApprovalAtr().value;
		wwfdtApprovalPhaseDay.approvalForm = phase.getApprovalForm().value;
		wwfdtApprovalPhaseDay.listWwfdtApprovalFrameDay = phase.getListApprovalFrame().stream()
				.map(x -> updateEntityWwfdtApprovalFrameDay(rootId, phase.getPhaseOrder(), x)).collect(Collectors.toList());
		return wwfdtApprovalPhaseDay;
	}

	private WwfdtApprovalFrameDay updateEntityWwfdtApprovalFrameDay(String rootId, int phaseOrder, ApprovalFrame frame) {
		WwfdtApprovalFrameDay entityFrame = this
				.queryProxy().find(new WwfdpApprovalFrameDayPK(rootId,
						phaseOrder, frame.getFrameOrder()), WwfdtApprovalFrameDay.class)
				.get();
		entityFrame.confirmAtr = frame.getConfirmAtr().value;
		
		
		
//		entityFrame.approverID = frame.getApproverID();
//		entityFrame.representerID = frame.getRepresenterID();
//		entityFrame.approvalDate = frame.getApprovalDate();
//		entityFrame.approvalReason = frame.getApprovalReason();
//		
//		
//		entityFrame.approvalAtr = frame.getApprovalAtr().value;
//		List<ApproverInfor> lstApprover = frame.getLstApproverInfo();
//		List<WwfdtApproverDay> lstEntityApprover = new ArrayList<>();
//		for (ApproverInfor approver : lstApprover) {
//			
//			
//			
//			
//			
//			
//			
//			entityApprover.approvalAtr = approver.getApprovalAtr().value;
//			entityApprover.confirmAtr = frame.getConfirmAtr().value;
//			entityApprover.wwfdpApprovrStatePK.approverId = approver.getApproverID();
//			entityApprover.agentID = approver.getAgentID();
//			entityApprover.approvalDate = approver.getApprovalDate();
//			entityApprover.approvalReason = approver.getApprovalReason();
//			lstEntityApprover.add(entityApprover);
//		}
//		
//		
//		
//		entityFrame.listWwfdtApproverDay = frame.getListApproverState().stream()
//				.map(x -> updateEntityWwfdtApproverDay(x)).collect(Collectors.toList());
		return entityFrame;
	}

	private WwfdtApproverDay updateEntityWwfdtApproverDay(String rootId, int phaseOrder, int frameOrder, ApproverInfor approverState) {
		WwfdtApproverDay wwfdtApproverDay = this.queryProxy()
				.find(new WwfdpApproverDayPK(rootId, phaseOrder,
						frameOrder, approverState.getApproverID()), WwfdtApproverDay.class)
				.get();
		return wwfdtApproverDay;
	}

	private WwfdtApprovalPhaseMonth updateEntityWwfdtApprovalPhaseMonth(String rootId, ApprovalPhaseState phase) {
		WwfdtApprovalPhaseMonth wwfdtApprovalPhaseMonth = this.queryProxy().find(
				new WwfdpApprovalPhaseMonthPK(rootId, phase.getPhaseOrder()),
				WwfdtApprovalPhaseMonth.class).get();
		wwfdtApprovalPhaseMonth.approvalAtr = phase.getApprovalAtr().value;
		wwfdtApprovalPhaseMonth.approvalForm = phase.getApprovalForm().value;
		wwfdtApprovalPhaseMonth.listWwfdtApprovalFrameMonth = phase.getListApprovalFrame().stream()
				.map(x -> updateEntityWwfdtApprovalFrameMonth(rootId, phase.getPhaseOrder(), x)).collect(Collectors.toList());
		return wwfdtApprovalPhaseMonth;
	}

	private WwfdtApprovalFrameMonth updateEntityWwfdtApprovalFrameMonth(String rootId, int phaseOrder, ApprovalFrame frame) {
		WwfdtApprovalFrameMonth entityFrame = this
				.queryProxy().find(new WwfdpApprovalFrameMonthPK(rootId,
						phaseOrder, frame.getFrameOrder()), WwfdtApprovalFrameMonth.class)
				.get();
//		entityFrame.approvalAtr = frame.getApprovalAtr().value;
		entityFrame.confirmAtr = frame.getConfirmAtr().value;
//		entityFrame.approverID = frame.getApproverID();
//		entityFrame.representerID = frame.getRepresenterID();
//		entityFrame.approvalDate = frame.getApprovalDate();
//		entityFrame.approvalReason = frame.getApprovalReason();
		entityFrame.listWwfdtApproverMonth = frame.getLstApproverInfo().stream()
				.map(x -> updateEntityWwfdtApproverMonth(rootId, phaseOrder,frame.getFrameOrder(), x)).collect(Collectors.toList());
		return entityFrame;
	}

	private WwfdtApproverMonth updateEntityWwfdtApproverMonth(String rootId, int phaseOrder, int frameOrder, ApproverInfor approverState) {
		WwfdtApproverMonth wwfdtApproverMonth = this.queryProxy()
				.find(new WwfdpApproverMonthPK(rootId, phaseOrder,
						frameOrder, approverState.getApproverID()), WwfdtApproverMonth.class)
				.get();
		return wwfdtApproverMonth;
	}

	@Override
	public List<ApprovalRootState> findEmployeeAppByApprovalRecordDate(GeneralDate startDate, GeneralDate endDate,
			String approverID, Integer rootType) {
		List<ApprovalRootState> result = new ArrayList<>();
		switch (rootType) {
		case 1:
			result = this.queryProxy().query(SELECT_CF_DAY_BY_DATE, WwfdtApprovalRootDay.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).getList(x -> x.toDomain());
			break;
		case 2:
			result = this.queryProxy().query(SELECT_CF_MONTH_BY_DATE, WwfdtApprovalRootMonth.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).getList(x -> x.toDomain());
			break;
		default:
			result = this.queryProxy().query(SELECT_APP_BY_DATE, WwfdtApprovalRootState.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).getList(x -> x.toDomain());
		}
		return result;
	}

	@Override
	public List<ApprovalRootState> findAppByEmployeeIDRecordDate(GeneralDate startDate, GeneralDate endDate,
			String employeeID, Integer rootType) {
		switch (rootType) {
		case 1:
			return this.queryProxy().query(SELECT_CF_DAY_BY_EMP_DATE, WwfdtApprovalRootDay.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate)
					.setParameter("employeeID", employeeID).getList(x -> x.toDomain());
		case 2:
			return this.queryProxy().query(SELECT_CF_MONTH_BY_EMP_DATE, WwfdtApprovalRootMonth.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate)
					.setParameter("employeeID", employeeID).getList(x -> x.toDomain());
		default:
			return this.queryProxy().query(SELECT_APP_BY_EMP_DATE, WwfdtApprovalRootState.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate)
					.setParameter("employeeID", employeeID).getList(x -> x.toDomain());
		}
	}

	@Override
	public List<ApprovalRootState> getRootStateByApproverDate(String companyID, String approverID, GeneralDate date) {
		List<String> listAppRootStateSimp = this.queryProxy()
				.query(SELECT_SIMPLE_APP_BY_DATE, WwfdtAppRootStateSimple.class).setParameter("startDate", date)
				.setParameter("endDate", date).getList(x -> x.wwfdpApprovalRootStatePK.rootStateID);
		List<ApprovalRootState> listAppRootState = new ArrayList<>();
		CollectionUtil.split(listAppRootStateSimp, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			internalQuery2(companyID, listAppRootState, subList);
		});
		return listAppRootState;
	}

	@SneakyThrows
	private void internalQuery2(String companyID, List<ApprovalRootState> listAppRootState, List<String> subList) {
		String rootStateIDLst = "";
		if (CollectionUtil.isEmpty(subList)) {
			rootStateIDLst = "''";
		} else {
			for (int i = 0; i < subList.size(); i++) {
				rootStateIDLst += "'" + subList.get(i) + "'";
				if (i != (subList.size() - 1)) {
					rootStateIDLst += ",";
				}
			}
		}
		String query = BASIC_SELECT + " WHERE root.ROOT_STATE_ID IN (rootStateIDs)" + " AND approver.CID = 'companyID'";
		query = query.replaceFirst("rootStateIDs", rootStateIDLst);
		query = query.replaceAll("companyID", companyID);
		List<WwfdtFullJoinState> listFullData = new ArrayList<>();
		try (PreparedStatement pstatement = this.connection().prepareStatement(query)) {
			listFullData = WwfdtFullJoinState.fromResultSet(pstatement.executeQuery());
		}
		listAppRootState.addAll(WwfdtFullJoinState.toDomain(listFullData));
	}

	@Override
	public List<ApprovalRootState> findEmployeeAppByApprovalRecordDateAndNoRootType(String companyID,
			GeneralDate startDate, GeneralDate endDate, String approverID) {
		List<ApprovalRootState> result = new ArrayList<>();
		result.addAll(this.queryProxy().query(SELECT_APPS_BY_APPROVER, WwfdtApprovalRootState.class)
				.setParameter("companyID", companyID).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).setParameter("approverID", approverID)
				.setParameter("systemDate", GeneralDate.today()).getList(x -> x.toDomain()));
		result.addAll(this.queryProxy().query(SELECT_CFS_DAY_BY_APPROVER, WwfdtApprovalRootDay.class)
				.setParameter("companyID", companyID).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).setParameter("approverID", approverID)
				.setParameter("systemDate", GeneralDate.today()).getList(x -> x.toDomain()));
		result.addAll(this.queryProxy().query(SELECT_CFS_MONTH_BY_APPROVER, WwfdtApprovalRootMonth.class)
				.setParameter("companyID", companyID).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).setParameter("approverID", approverID)
				.setParameter("systemDate", GeneralDate.today()).getList(x -> x.toDomain()));
		return result;
	}

	@Override
	public List<ApprovalRootState> findAppByListEmployeeIDRecordDate(GeneralDate startDate, GeneralDate endDate,
			List<String> employeeIDs, Integer rootType) {
		return this.queryProxy().query(SELECT_BY_LIST_EMP_DATE, WwfdtApprovalRootState.class)
				.setParameter("startDate", startDate).setParameter("endDate", endDate)
				.setParameter("rootType", rootType).setParameter("employeeID", employeeIDs).getList(x -> x.toDomain());
	}

	@Override
	public List<ApprovalRootState> findAppByListEmployeeIDAndListRecordDate(List<GeneralDate> approvalRecordDates,
			List<String> employeeIDs, Integer rootType) {
		switch (rootType) {
		case 1:
			return this.queryProxy().query(SELECT_CFS_DAY_BY_EMP_AND_DATES, WwfdtApprovalRootDay.class)
					.setParameter("recordDate", approvalRecordDates).setParameter("employeeID", employeeIDs)
					.getList(x -> x.toDomain());
		case 2:
			return this.queryProxy().query(SELECT_CFS_MONTH_BY_EMP_AND_DATES, WwfdtApprovalRootMonth.class)
					.setParameter("recordDate", approvalRecordDates).setParameter("employeeID", employeeIDs)
					.getList(x -> x.toDomain());
		default:
			return this.queryProxy().query(SELECT_APPS_BY_EMP_AND_DATES, WwfdtApprovalRootState.class)
					.setParameter("recordDate", approvalRecordDates).setParameter("employeeID", employeeIDs)
					.getList(x -> x.toDomain());
		}
	}

	@Override
	public List<ApprovalRootState> findByApprover(String companyID, GeneralDate startDate, GeneralDate endDate,
			String approverID, Integer rootType) {
		List<ApprovalRootState> result = new ArrayList<>();
		switch (rootType) {
		case 1:
			result = this.queryProxy().query(SELECT_CFS_DAY_BY_APPROVER, WwfdtApprovalRootDay.class)
					.setParameter("companyID", companyID).setParameter("startDate", startDate)
					.setParameter("endDate", endDate).setParameter("approverID", approverID)
					.setParameter("systemDate", GeneralDate.today()).getList(x -> x.toDomain());
			break;
		case 2:
			result = this.queryProxy().query(SELECT_CFS_MONTH_BY_APPROVER, WwfdtApprovalRootMonth.class)
					.setParameter("companyID", companyID).setParameter("startDate", startDate)
					.setParameter("endDate", endDate).setParameter("approverID", approverID)
					.setParameter("systemDate", GeneralDate.today()).getList(x -> x.toDomain());
			break;
		default:
			result = this.queryProxy().query(SELECT_APPS_BY_APPROVER, WwfdtApprovalRootState.class)
					.setParameter("companyID", companyID).setParameter("startDate", startDate)
					.setParameter("endDate", endDate).setParameter("approverID", approverID)
					.setParameter("systemDate", GeneralDate.today()).getList(x -> x.toDomain());
		}
		return result;
	}

	@Override
	public List<ApprovalRootState> findEmployeeAppByApprovalRecordDateNew(GeneralDate startDate, GeneralDate endDate,
			Integer rootType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteConfirmDay(String employeeID, GeneralDate date) {
		List<WwfdpApprovalRootDayPK> rootDayKeyList = this.queryProxy()
				.query(SELECT_CF_DAY_BY_EMP_DATE_SP, WwfdtAppRootDaySimple.class).setParameter("startDate", date)
				.setParameter("endDate", date).setParameter("employeeID", employeeID)
				.getList(x -> new WwfdpApprovalRootDayPK(x.wwfdpApprovalRootDayPK.rootStateID));
		this.commandProxy().removeAll(WwfdtApprovalRootDay.class, rootDayKeyList);
	}

	@Override
	public List<ApprovalPhaseState> findPhaseApprovalMax(String appID) {
		return this.queryProxy().query(FIND_PHASE_APPROVAL_MAX, WwfdtApprovalPhaseState.class)
				.setParameter("appID", appID).getList(c -> c.toDomain());
	}

	// private ApprovalPhaseState toDomainPhase(WwfdtApprovalPhaseState entity)
	// {
	// return new
	// ApprovalPhaseState(entity.wwfdpApprovalPhaseStatePK.rootStateID,
	// entity.wwfdpApprovalPhaseStatePK.phaseOrder,
	// EnumAdaptor.valueOf(entity.approvalAtr, ApprovalBehaviorAtr.class),
	// EnumAdaptor.valueOf(entity.approvalForm, ApprovalForm.class),
	// null);
	// }

	@Override
	@SneakyThrows
	public List<ApprovalRootState> getByApproverPeriod(String companyID, String approverID, DatePeriod period) {
		List<ApprovalRootState> listAppRootState = new ArrayList<>();

		String query = BASIC_SELECT + " LEFT JOIN KRQDT_APPLICATION app" + " ON root.ROOT_STATE_ID = app.APP_ID"
				+ " WHERE approver.APPROVER_ID = 'approverID'"
				+ " AND app.APP_DATE >= 'startDate'" + " AND app.APP_DATE <= 'endDate'";

		query = query.replaceAll("startDate", period.start().toString("yyyy-MM-dd"));
		query = query.replaceAll("endDate", period.end().toString("yyyy-MM-dd"));
		query = query.replaceAll("approverID", approverID);
		query = query.replaceAll("companyID", companyID);
		try (PreparedStatement pstatement = this.connection().prepareStatement(query)) {
			List<WwfdtFullJoinState> listFullData = WwfdtFullJoinState.fromResultSet(pstatement.executeQuery());
			listAppRootState = WwfdtFullJoinState.toDomain(listFullData);
		}
		return listAppRootState;
	}

	@Override
	@SneakyThrows
	public List<ApprovalRootState> getByApproverAgentPeriod(String companyID, String approverID, DatePeriod period,
			DatePeriod agentPeriod) {

		String query = BASIC_SELECT + " LEFT JOIN KRQDT_APPLICATION app" + " ON root.ROOT_STATE_ID = app.APP_ID"
				+ " WHERE approver.APPROVER_CHILD_ID = 'approverID'" + " AND approver.CID = 'companyID'"
				+ " AND app.APP_DATE >= 'startDate'" + " AND app.APP_DATE <= 'endDate'"
				+ " AND app.APP_DATE >= 'agentStartDate'" + " AND app.APP_DATE <= 'agentEndDate'";

		query = query.replaceAll("startDate", period.start().toString("yyyy-MM-dd"));
		query = query.replaceAll("endDate", period.end().toString("yyyy-MM-dd"));
		query = query.replaceAll("agentStartDate", agentPeriod.start().toString("yyyy-MM-dd"));
		query = query.replaceAll("agentEndDate", agentPeriod.end().toString("yyyy-MM-dd"));
		query = query.replaceAll("approverID", approverID);
		query = query.replaceAll("companyID", companyID);
		List<ApprovalRootState> listApprovalRootState = new ArrayList<>();
		try (PreparedStatement pstatement = this.connection().prepareStatement(query)) {
			List<WwfdtFullJoinState> listFullData = WwfdtFullJoinState.fromResultSet(pstatement.executeQuery());
			listApprovalRootState = WwfdtFullJoinState.toDomain(listFullData);
		}
		return listApprovalRootState;
	}

	@Override
	public List<ApprovalRootState> findEmploymentAppCMM045(List<String> lstApproverID, DatePeriod period,
			boolean unapprovalStatus, boolean approvalStatus, boolean denialStatus, boolean agentApprovalStatus,
			boolean remandStatus, boolean cancelStatus) {
		String companyID = AppContexts.user().companyId();
		List<ApprovalRootState> result = new ArrayList<>();
		CollectionUtil.split(lstApproverID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			internalQuery045(companyID, result, subList, period, unapprovalStatus, approvalStatus, denialStatus,
					agentApprovalStatus, remandStatus, cancelStatus);
		});
		return result;
	}

	@SneakyThrows
	private void internalQuery045(String companyID, List<ApprovalRootState> result, List<String> lstApproverID,
			DatePeriod period, boolean unapprovalStatus, boolean approvalStatus, boolean denialStatus,
			boolean agentApprovalStatus, boolean remandStatus, boolean cancelStatus) {
		// Phase
		List<Integer> lstPhaseStt = new ArrayList<>();

		if (unapprovalStatus || approvalStatus || denialStatus || agentApprovalStatus) {
			lstPhaseStt.add(0);
		}

		if (approvalStatus || agentApprovalStatus || denialStatus) {
			lstPhaseStt.add(1);
		}

		if (denialStatus || remandStatus) {
			lstPhaseStt.add(3);
		}

		if (denialStatus || cancelStatus) {
			lstPhaseStt.add(2);
			lstPhaseStt.add(4);
		}
		// Frame
		List<Integer> lstFrameStt = new ArrayList<>();

		if (unapprovalStatus || denialStatus || remandStatus || cancelStatus || approvalStatus) {
			lstFrameStt.add(0);
		}

		if (approvalStatus || agentApprovalStatus || denialStatus || remandStatus || cancelStatus) {
			lstFrameStt.add(1);
		}

		if (denialStatus || remandStatus || cancelStatus) {
			lstFrameStt.add(2);
			lstFrameStt.add(3);
			lstFrameStt.add(4);
		}

		String query = "";
		String lstPhase = NtsStatement.In.createParamsString(lstPhaseStt);
		String lstFrame = NtsStatement.In.createParamsString(lstFrameStt);
		String lstIds = NtsStatement.In.createParamsString(lstApproverID);

		query = "SELECT root.ROOT_STATE_ID, root.EMPLOYEE_ID, root.APPROVAL_RECORD_DATE, "
				+ "phase.PHASE_ORDER, phase.APPROVAL_FORM, phase.APP_PHASE_ATR, "
				+ "frame.FRAME_ORDER, frame.APP_FRAME_ATR, frame.CONFIRM_ATR, frame.APPROVER_ID, frame.REPRESENTER_ID, frame.APPROVAL_DATE, frame.APPROVAL_REASON, "
				+ "approver.APPROVER_CHILD_ID " + "FROM WWFDT_APPROVAL_ROOT_STATE root "
				+ "LEFT JOIN WWFDT_APPROVAL_PHASE_ST phase ON root.ROOT_STATE_ID = phase.ROOT_STATE_ID "
				+ "LEFT JOIN WWFDT_APPROVAL_FRAME frame ON phase.ROOT_STATE_ID = frame.ROOT_STATE_ID AND phase.PHASE_ORDER = frame.PHASE_ORDER "
				+ "LEFT JOIN WWFDT_APPROVER_STATE approver ON frame.ROOT_STATE_ID = approver.ROOT_STATE_ID AND frame.PHASE_ORDER = approver.PHASE_ORDER AND frame.FRAME_ORDER = approver.FRAME_ORDER "
				+ "WHERE root.ROOT_STATE_ID IN (SELECT DISTINCT a.ROOT_STATE_ID " + "FROM WWFDT_APPROVER_STATE a "
				+ "inner join WWFDT_APPROVAL_FRAME b on b.ROOT_STATE_ID = a.ROOT_STATE_ID and b.PHASE_ORDER = a.PHASE_ORDER "
				+ "inner join WWFDT_APPROVAL_PHASE_ST c on a.ROOT_STATE_ID = c.ROOT_STATE_ID and a.PHASE_ORDER = c.PHASE_ORDER "
				+ "and a.APPROVAL_RECORD_DATE >= ?  and a.APPROVAL_RECORD_DATE <= ? " + "and c.APP_PHASE_ATR IN ("
				+ lstPhase + ") " + "and b.APP_FRAME_ATR IN (" + lstFrame + ") " + "and a.APPROVER_CHILD_ID IN ("
				+ lstIds + ") )";

		List<WwfdtFullJoinState> listFullData = new ArrayList<>();

		try (val pstatement = this.connection().prepareStatement(query)) {
			pstatement.setString(1, period.start().toString("yyyy-MM-dd"));
			pstatement.setString(2, period.end().toString("yyyy-MM-dd"));

			for (int i = 0; i < lstPhaseStt.size(); i++) {
				pstatement.setInt(i + 3, lstPhaseStt.get(i));
			}

			for (int i = 0; i < lstFrameStt.size(); i++) {
				pstatement.setInt(i + 3 + lstPhaseStt.size(), lstFrameStt.get(i));
			}

			for (int i = 0; i < lstApproverID.size(); i++) {
				pstatement.setString(i + 3 + lstPhaseStt.size() + lstFrameStt.size(), lstApproverID.get(i));
			}

			listFullData.addAll(WwfdtFullJoinState.fromResultSet(pstatement.executeQuery()));
		}

		List<ApprovalRootState> entityRoot = WwfdtFullJoinState.toDomain(listFullData);
		result.addAll(entityRoot);
	}

	@Override
	public List<String> resultKTG002Mobile(GeneralDate startDate, GeneralDate endDate, String approverID,
			Integer rootType, String companyID) {
		String loginSID = AppContexts.user().employeeId();
		GeneralDate baseDate = GeneralDate.today();
		List<String> data = new ArrayList<>();
		String SELECT = "SELECT SYONIN.ROOT_STATE_ID FROM ( "
				+ "SELECT APS.ROOT_STATE_ID AS ROOT_STATE_ID,APS.PHASE_ORDER AS PHASE_ORDER "
				+ "FROM WWFDT_APPROVER_STATE APS " + "INNER JOIN " + "WWFDT_APPROVAL_FRAME AF "
				+ "ON APS.ROOT_STATE_ID = AF.ROOT_STATE_ID " + "AND APS.PHASE_ORDER = AF.PHASE_ORDER "
				+ "AND APS.FRAME_ORDER = AF.FRAME_ORDER " + "WHERE APS.CID = ? " + "AND APS.APPROVER_CHILD_ID = ? "
				+ "AND AF.APP_FRAME_ATR = '0' " + "AND APS.APPROVAL_RECORD_DATE >= ? "
				+ "AND APS.APPROVAL_RECORD_DATE <= ? " + "UNION ALL "
				+ "SELECT APS.ROOT_STATE_ID AS ROOT_STATE_ID,APS.PHASE_ORDER AS PHASE_ORDER "
				+ "FROM WWFDT_APPROVER_STATE APS " + "INNER JOIN WWFDT_APPROVAL_FRAME AF "
				+ "ON APS.ROOT_STATE_ID = AF.ROOT_STATE_ID " + "AND APS.PHASE_ORDER = AF.PHASE_ORDER "
				+ "AND APS.FRAME_ORDER = AF.FRAME_ORDER " + "INNER JOIN CMMMT_AGENT AG "
				+ "ON APS.APPROVER_CHILD_ID = AG.SID " + "WHERE APS.CID = ? " + "AND AF.APP_FRAME_ATR = '0' "
				+ "AND APS.APPROVAL_RECORD_DATE >= ? " + "AND APS.APPROVAL_RECORD_DATE <= ? "
				+ "AND AG.START_DATE <= ? " + "AND AG.END_DATE >= ? " + "AND AG.AGENT_APP_TYPE1 = '0' "
				+ "AND AG.AGENT_SID1 = ? ) " + "AS SYONIN " + "INNER JOIN ( "
				+ "SELECT AP.ROOT_STATE_ID AS ROOT_STATE_ID, MIN(PHASE_ORDER) AS NOW_PHASE_ORDER "
				+ "FROM WWFDT_APPROVAL_PHASE_ST AP " + "WHERE AP.APP_PHASE_ATR IN ('0','3') "
				+ "GROUP BY AP.ROOT_STATE_ID )" + "AS NOWFAS " + "ON SYONIN.ROOT_STATE_ID = NOWFAS.ROOT_STATE_ID "
				+ "AND SYONIN.PHASE_ORDER = NOWFAS.NOW_PHASE_ORDER ";
		try (PreparedStatement stmt = this.connection().prepareStatement(SELECT)) {
			stmt.setString(1, companyID);
			stmt.setString(2, loginSID);
			stmt.setDate(3, Date.valueOf(startDate.localDate()));
			stmt.setDate(4, Date.valueOf(endDate.localDate()));
			stmt.setString(5, companyID);
			stmt.setDate(6, Date.valueOf(startDate.localDate()));
			stmt.setDate(7, Date.valueOf(endDate.localDate()));
			stmt.setDate(8, Date.valueOf(baseDate.localDate()));
			stmt.setDate(9, Date.valueOf(baseDate.localDate()));
			stmt.setString(10, loginSID);
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				// result.getString(SELECT);
				data.add(result.getString("ROOT_STATE_ID"));
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return data;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean resultKTG002(GeneralDate startDate, GeneralDate endDate, String approverID, Integer rootType,
			String companyID) {
		String loginSID = AppContexts.user().employeeId();
		GeneralDate baseDate = GeneralDate.today();
		String SELECT = "SELECT COUNT (*) FROM ( "
				+ "SELECT APS.ROOT_STATE_ID AS ROOT_STATE_ID,APS.PHASE_ORDER AS PHASE_ORDER "
				+ "FROM WWFDT_APPROVER_STATE APS " + "INNER JOIN " + "WWFDT_APPROVAL_FRAME AF "
				+ "ON APS.ROOT_STATE_ID = AF.ROOT_STATE_ID " + "AND APS.PHASE_ORDER = AF.PHASE_ORDER "
				+ "AND APS.FRAME_ORDER = AF.FRAME_ORDER " + "WHERE APS.CID = ? " + "AND APS.APPROVER_CHILD_ID = ? "
				+ "AND AF.APP_FRAME_ATR = '0' " + "AND APS.APPROVAL_RECORD_DATE >= ? "
				+ "AND APS.APPROVAL_RECORD_DATE <= ? " + "UNION ALL "
				+ "SELECT APS.ROOT_STATE_ID AS ROOT_STATE_ID,APS.PHASE_ORDER AS PHASE_ORDER "
				+ "FROM WWFDT_APPROVER_STATE APS " + "INNER JOIN WWFDT_APPROVAL_FRAME AF "
				+ "ON APS.ROOT_STATE_ID = AF.ROOT_STATE_ID " + "AND APS.PHASE_ORDER = AF.PHASE_ORDER "
				+ "AND APS.FRAME_ORDER = AF.FRAME_ORDER " + "INNER JOIN CMMMT_AGENT AG "
				+ "ON APS.APPROVER_CHILD_ID = AG.SID " + "WHERE APS.CID = ? " + "AND AF.APP_FRAME_ATR = '0' "
				+ "AND APS.APPROVAL_RECORD_DATE >= ? " + "AND APS.APPROVAL_RECORD_DATE <= ? "
				+ "AND AG.START_DATE <= ? " + "AND AG.END_DATE >= ? " + "AND AG.AGENT_APP_TYPE1 = '0' "
				+ "AND AG.AGENT_SID1 = ? ) " + "AS SYONIN " + "INNER JOIN ( "
				+ "SELECT AP.ROOT_STATE_ID AS ROOT_STATE_ID, MIN(PHASE_ORDER) AS NOW_PHASE_ORDER "
				+ "FROM WWFDT_APPROVAL_PHASE_ST AP " + "WHERE AP.APP_PHASE_ATR IN ('0','3') "
				+ "GROUP BY AP.ROOT_STATE_ID )" + "AS NOWFAS " + "ON SYONIN.ROOT_STATE_ID = NOWFAS.ROOT_STATE_ID "
				+ "AND SYONIN.PHASE_ORDER = NOWFAS.NOW_PHASE_ORDER ";
		try (PreparedStatement stmt = this.connection().prepareStatement(SELECT)) {
			stmt.setString(1, companyID);
			stmt.setString(2, loginSID);
			stmt.setDate(3, Date.valueOf(startDate.localDate()));
			stmt.setDate(4, Date.valueOf(endDate.localDate()));
			stmt.setString(5, companyID);
			stmt.setDate(6, Date.valueOf(startDate.localDate()));
			stmt.setDate(7, Date.valueOf(endDate.localDate()));
			stmt.setDate(8, Date.valueOf(baseDate.localDate()));
			stmt.setDate(9, Date.valueOf(baseDate.localDate()));
			stmt.setString(10, loginSID);
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				if (result.getInt(1) > 0) {
					// co data
					return true;
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return false;

	}
}
