package nts.uk.ctx.workflow.infra.repository.approverstatemanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApproverState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.RootType;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdpApprovalFramePK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdpApprovalPhaseStatePK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdpApprovalRootStatePK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdpApproverStatePK;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtAppRootStateSimple;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtApprovalFrame;
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
public class JpaApprovalRootStateRepository extends JpaRepository implements ApprovalRootStateRepository {
	
	private static final String BASIC_SELECT;

	private static final String SELECT_APP_BY_ID;
	private static final String SELECT_CF_DAY_BY_ID;
	private static final String SELECT_CF_MONTH_BY_ID;
	
//	private static final String SELECT_APP_BY_IDS;
//	private static final String SELECT_CF_DAY_BY_IDS;
//	private static final String SELECT_CF_MONTH_BY_IDS;
//	
//	private static final String SELECT_APPS_BY_ID;
	
	private static final String SELECT_APP_BY_DATE;
	private static final String SELECT_CF_DAY_BY_DATE;
	private static final String SELECT_CF_MONTH_BY_DATE;
	
	private static final String SELECT_SIMPLE_APP_BY_DATE;
//	private static final String SELECT_SIMPLE_CF_DAY_BY_DATE;
//	private static final String SELECT_SIMPLE_CF_MONTH_BY_DATE;
	
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
			+ " WHERE a.wwfdpApprovalPhaseStatePK.rootStateID =: appID"
			+ " AND a.approvalAtr = 1 ORDER BY a.phaseOrder DESC";
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT root.ROOT_STATE_ID, root.HIST_ID, root.EMPLOYEE_ID, root.APPROVAL_RECORD_DATE,");
		builderString.append("phase.PHASE_ORDER, phase.APP_PHASE_ATR, phase.APPROVAL_FORM,");
		builderString.append("frame.FRAME_ORDER, frame.APP_FRAME_ATR, frame.CONFIRM_ATR, frame.APPROVER_ID, frame.REPRESENTER_ID, frame.APPROVAL_DATE, frame.APPROVAL_REASON,");
		builderString.append("approver.APPROVER_CHILD_ID, approver.CID FROM WWFDT_APPROVAL_ROOT_STATE root ");
		builderString.append("LEFT JOIN WWFDT_APPROVAL_PHASE_ST phase ");
		builderString.append("ON root.ROOT_STATE_ID = phase.ROOT_STATE_ID ");
		builderString.append("LEFT JOIN WWFDT_APPROVAL_FRAME frame ");
		builderString.append("ON phase.ROOT_STATE_ID = frame.ROOT_STATE_ID ");
		builderString.append("AND phase.PHASE_ORDER = frame.PHASE_ORDER ");
		builderString.append("LEFT JOIN WWFDT_APPROVER_STATE approver ");
		builderString.append("ON frame.ROOT_STATE_ID = approver.ROOT_STATE_ID ");
		builderString.append("AND frame.PHASE_ORDER = approver.PHASE_ORDER ");
		builderString.append("AND frame.FRAME_ORDER = approver.FRAME_ORDER");
		BASIC_SELECT = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootState e");
		builderString.append(" WHERE e.wwfdpApprovalRootStatePK.rootStateID = :rootStateID");
		SELECT_APP_BY_ID = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootDay e");
		builderString.append(" WHERE e.wwfdpApprovalRootDayPK.rootStateID = :rootStateID");
		SELECT_CF_DAY_BY_ID = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM WwfdtApprovalRootMonth e");
		builderString.append(" WHERE e.wwfdpApprovalRootMonthPK.rootStateID = :rootStateID");
		SELECT_CF_MONTH_BY_ID = builderString.toString();
		
//		builderString = new StringBuilder();
//		builderString.append("SELECT e");
//		builderString.append(" FROM WwfdtApprovalRootState e");
//		builderString.append(" WHERE e.wwfdpApprovalRootStatePK.rootStateID IN :rootStateIDs");
//		SELECT_APP_BY_IDS = builderString.toString();
//		
//		builderString = new StringBuilder();
//		builderString.append("SELECT e");
//		builderString.append(" FROM WwfdtApprovalRootDay e");
//		builderString.append(" WHERE e.wwfdpApprovalRootDayPK.rootStateID IN :rootStateIDs");
//		SELECT_CF_DAY_BY_IDS = builderString.toString();
//		
//		builderString = new StringBuilder();
//		builderString.append("SELECT e");
//		builderString.append(" FROM WwfdtApprovalRootMonth e");
//		builderString.append(" WHERE e.wwfdpApprovalRootMonthPK.rootStateID IN :rootStateIDs");
//		SELECT_CF_MONTH_BY_IDS = builderString.toString();
//		
//		builderString = new StringBuilder();
//		builderString.append("SELECT c");
//		builderString.append(" FROM WwfdtApprovalRootState c");
//		builderString.append(" WHERE c.wwfdpApprovalRootStatePK.rootStateID IN ");
//		builderString.append("(SELECT DISTINCT a.wwfdpApprovalRootStatePK.rootStateID");
//		builderString.append(" FROM WwfdtAppRootStateSimple a");
//		builderString.append(" JOIN WwfdtAppStateSimple b ");
//		builderString.append(" ON a.wwfdpApprovalRootStatePK.rootStateID = b.wwfdpApproverStatePK.rootStateID");
//		builderString.append(" WHERE (b.wwfdpApproverStatePK.approverID = :approverID");
//		builderString.append(" OR b.wwfdpApproverStatePK.approverID IN");
//		builderString.append(" (SELECT d.cmmmtAgentPK.employeeId FROM CmmmtAgent d WHERE d.agentSid1 = :approverID))");
//		builderString.append(" AND a.wwfdpApprovalRootStatePK.rootStateID IN :rootStateIDs)");
//		SELECT_APPS_BY_ID = builderString.toString();
		
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
		
//		builderString = new StringBuilder();
//		builderString.append("SELECT e");
//		builderString.append(" FROM WwfdtAppRootDaySimple e");
//		builderString.append(" WHERE e.recordDate >= :startDate");
//		builderString.append(" AND e.recordDate <= :endDate");
//		SELECT_SIMPLE_CF_DAY_BY_DATE = builderString.toString();
//		
//		builderString = new StringBuilder();
//		builderString.append("SELECT e");
//		builderString.append(" FROM WwfdtAppRootMonthSimple e");
//		builderString.append(" WHERE e.recordDate >= :startDate");
//		builderString.append(" AND e.recordDate <= :endDate");
//		SELECT_SIMPLE_CF_MONTH_BY_DATE = builderString.toString();
		
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
		builderString.append(" WHERE c.wwfdpApprovalRootStatePK.rootStateID IN ");
		builderString.append("(SELECT DISTINCT a.wwfdpApprovalRootStatePK.rootStateID");
		builderString.append(" FROM WwfdtAppRootStateSimple a JOIN WwfdtAppStateSimple b ");
		builderString.append(" ON a.wwfdpApprovalRootStatePK.rootStateID = b.wwfdpApproverStatePK.rootStateID ");
		builderString.append(" WHERE (b.wwfdpApproverStatePK.approverID = :approverID");
		builderString.append(" OR b.wwfdpApproverStatePK.approverID IN");
		builderString.append(" (SELECT d.cmmmtAgentPK.employeeId FROM CmmmtAgent d WHERE d.agentSid1 = :approverID))");
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
		builderString.append(" AND :startDate <= d.endDate AND :endDate >= d.startDate))");
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
		builderString.append(" (SELECT d.cmmmtAgentPK.employeeId FROM CmmmtAgent d WHERE d.agentSid1 = :approverID))");
		builderString.append(" AND b.companyID = :companyID");
		builderString.append(" AND b.recordDate >= :startDate AND b.recordDate <= :endDate)");
		SELECT_CFS_MONTH_BY_APPROVER = builderString.toString();
		
	}
	
	@Override
	public Optional<ApprovalRootState> findByID(String rootStateID, Integer rootType) {
		switch (rootType) {
		case 1:
			return this.queryProxy().query(SELECT_CF_DAY_BY_ID, WwfdtApprovalRootDay.class)
					.setParameter("rootStateID", rootStateID).getSingle(x -> x.toDomain());
		case 2:
			return this.queryProxy().query(SELECT_CF_MONTH_BY_ID, WwfdtApprovalRootMonth.class)
					.setParameter("rootStateID", rootStateID).getSingle(x -> x.toDomain());
		default:
			Connection con = this.getEntityManager().unwrap(Connection.class);
			try {
				List<ApprovalRootState> listAppRootState = new ArrayList<>();
				String query = BASIC_SELECT + 
						" WHERE root.ROOT_STATE_ID = 'rootStateID'";
				query = query.replaceFirst("rootStateID", rootStateID);
				List<WwfdtFullJoinState> listFullData = new ArrayList<>();
				try (PreparedStatement pstatement = con.prepareStatement(query)) {
					ResultSet rs = pstatement.executeQuery();
					while (rs.next()) {
						listFullData.add(new WwfdtFullJoinState(
								rs.getString("ROOT_STATE_ID"), 
								rs.getString("HIST_ID"), 
								rs.getString("EMPLOYEE_ID"), 
								GeneralDate.fromString(rs.getString("APPROVAL_RECORD_DATE"), "yyyy-MM-dd"), 
								Integer.valueOf(rs.getString("PHASE_ORDER")), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APPROVAL_FORM")), ApprovalForm.class), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APP_PHASE_ATR")), ApprovalBehaviorAtr.class), 
								Integer.valueOf(rs.getString("FRAME_ORDER")), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APP_FRAME_ATR")), ApprovalBehaviorAtr.class), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("CONFIRM_ATR")), ConfirmPerson.class), 
								rs.getString("APPROVER_ID"), 
								rs.getString("REPRESENTER_ID"), 
								rs.getString("APPROVAL_DATE") != null ? GeneralDate.fromString(rs.getString("APPROVAL_DATE"), "yyyy-MM-dd HH:mm:ss") : null, 
								rs.getString("APPROVAL_REASON"), 
								rs.getString("APPROVER_CHILD_ID"),
								rs.getString("CID")));
					}
				}
				listAppRootState = this.convertFromSQLResult(listFullData);
				if(CollectionUtil.isEmpty(listAppRootState)){
					return Optional.empty();
				} else {
					return Optional.of(listAppRootState.get(0));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return Optional.empty();
			}
		}
	}
	
	@Override
	public List<ApprovalRootState> findEmploymentApps(List<String> rootStateIDs, String approverID) {
		String companyID = AppContexts.user().companyId();
		List<ApprovalRootState> result = new ArrayList<>();
		Connection con = this.getEntityManager().unwrap(Connection.class);
		CollectionUtil.split(rootStateIDs, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			try {
				String rootStateIDLst = "";
				for(int i = 0; i < subList.size(); i++){
					rootStateIDLst+="'"+subList.get(i)+"'";
					if(i != (subList.size()-1)){
						rootStateIDLst+=",";
					}
				}
				String query = 
						"SELECT root.ROOT_STATE_ID, root.HIST_ID, root.EMPLOYEE_ID, root.APPROVAL_RECORD_DATE, " +
						"phaseJoin.PHASE_ORDER, phaseJoin.APPROVAL_FORM, phaseJoin.APP_PHASE_ATR, " +
						"phaseJoin.FRAME_ORDER, phaseJoin.APP_FRAME_ATR, phaseJoin.CONFIRM_ATR, phaseJoin.APPROVER_ID, phaseJoin.REPRESENTER_ID, " +
						"phaseJoin.APPROVAL_DATE, phaseJoin.APPROVAL_REASON, phaseJoin.APPROVER_CHILD_ID " +
						"FROM WWFDT_APPROVAL_ROOT_STATE root " +
						"LEFT JOIN " +
						"(SELECT phase.ROOT_STATE_ID, phase.PHASE_ORDER, phase.APPROVAL_FORM, phase.APP_PHASE_ATR, " +
						"frameJoin.FRAME_ORDER, frameJoin.APP_FRAME_ATR, frameJoin.CONFIRM_ATR, frameJoin.APPROVER_ID, frameJoin.REPRESENTER_ID, " +
						"frameJoin.APPROVAL_DATE, frameJoin.APPROVAL_REASON, frameJoin.APPROVER_CHILD_ID " +
						"FROM WWFDT_APPROVAL_PHASE_ST phase " +
						"LEFT JOIN ( " +
						"SELECT frame.ROOT_STATE_ID, frame.PHASE_ORDER, frame.FRAME_ORDER, frame.APP_FRAME_ATR, frame.CONFIRM_ATR, frame.APPROVER_ID, " +
						"frame.REPRESENTER_ID, frame.APPROVAL_DATE, frame.APPROVAL_REASON, approver.APPROVER_CHILD_ID " +
						"FROM WWFDT_APPROVAL_FRAME frame " +
						"LEFT JOIN " +
						"WWFDT_APPROVER_STATE approver " +
						"ON frame.ROOT_STATE_ID = approver.ROOT_STATE_ID " +
						"AND frame.PHASE_ORDER = approver.PHASE_ORDER " +
						"AND frame.FRAME_ORDER = approver.FRAME_ORDER) " +
						"AS frameJoin " +
						"ON phase.ROOT_STATE_ID = frameJoin.ROOT_STATE_ID " +
						"AND phase.PHASE_ORDER = frameJoin.PHASE_ORDER) " +
						"AS phaseJoin " +
						"ON root.ROOT_STATE_ID = phaseJoin.ROOT_STATE_ID " +
						"WHERE root.ROOT_STATE_ID IN ( " +
						"SELECT DISTINCT c.ROOT_STATE_ID FROM ( " +
						"SELECT a.ROOT_STATE_ID FROM WWFDT_APPROVER_STATE a WHERE a.APPROVER_CHILD_ID = 'approverID' " + 
						"UNION ALL " +
						"SELECT b.ROOT_STATE_ID FROM WWFDT_APPROVER_STATE b WHERE b.APPROVER_CHILD_ID IN " +
						"( SELECT c.SID FROM CMMMT_AGENT c where c.AGENT_SID1 = 'approverID' )) c " +
						"WHERE c.ROOT_STATE_ID IN (rootStateIDs))";
				query = query.replaceAll("approverID", approverID);
				query = query.replaceFirst("rootStateIDs", rootStateIDLst);
				List<WwfdtFullJoinState> listFullData = new ArrayList<>();
				try (PreparedStatement pstatement = con.prepareStatement(query)) {
					ResultSet rs = pstatement.executeQuery();
					while (rs.next()) {
						listFullData.add(new WwfdtFullJoinState(
								rs.getString("ROOT_STATE_ID"), 
								rs.getString("HIST_ID"), 
								rs.getString("EMPLOYEE_ID"), 
								GeneralDate.fromString(rs.getString("APPROVAL_RECORD_DATE"), "yyyy-MM-dd"), 
								Integer.valueOf(rs.getString("PHASE_ORDER")), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APPROVAL_FORM")), ApprovalForm.class), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APP_PHASE_ATR")), ApprovalBehaviorAtr.class), 
								Integer.valueOf(rs.getString("FRAME_ORDER")), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APP_FRAME_ATR")), ApprovalBehaviorAtr.class), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("CONFIRM_ATR")), ConfirmPerson.class), 
								rs.getString("APPROVER_ID"), 
								rs.getString("REPRESENTER_ID"), 
								rs.getString("APPROVAL_DATE") != null ? GeneralDate.fromString(rs.getString("APPROVAL_DATE"), "yyyy-MM-dd HH:mm:ss") : null, 
								rs.getString("APPROVAL_REASON"), 
								rs.getString("APPROVER_CHILD_ID"),
								companyID));
					}
				}
				List<ApprovalRootState> entityRoot = listFullData.stream().collect(Collectors.groupingBy(WwfdtFullJoinState::getRootStateID)).entrySet()
						.stream().map(x -> {
							String rootStateID = x.getValue().get(0).getRootStateID();
							String historyID = x.getValue().get(0).getHistoryID();
							GeneralDate recordDate = x.getValue().get(0).getRecordDate();
							String employeeID = x.getValue().get(0).getEmployeeID();
							List<ApprovalPhaseState> listAppPhase =
							x.getValue().stream().collect(Collectors.groupingBy(WwfdtFullJoinState::getPhaseOrder)).entrySet()
							.stream().map(y -> {
								Integer phaseOrder  = y.getValue().get(0).getPhaseOrder();
								ApprovalForm approvalForm = y.getValue().get(0).getApprovalForm();
								ApprovalBehaviorAtr appPhaseAtr =  y.getValue().get(0).getAppPhaseAtr();
								List<ApprovalFrame> listAppFrame =
								y.getValue().stream().collect(Collectors.groupingBy(WwfdtFullJoinState::getFrameOrder)).entrySet()
								.stream().map(z -> { 
									Integer frameOrder = z.getValue().get(0).getFrameOrder();
									ConfirmPerson confirmAtr = z.getValue().get(0).getConfirmAtr();
									String frameApproverID =  z.getValue().get(0).getApproverID();
									String representerID = z.getValue().get(0).getRepresenterID();
									GeneralDate approvalDate = z.getValue().get(0).getApprovalDate();
									String approvalReason = z.getValue().get(0).getApprovalReason();
									ApprovalBehaviorAtr appFrameAtr = z.getValue().get(0).getAppFrameAtr();
									List<ApproverState> listApprover = z.getValue().stream().collect(Collectors.groupingBy(WwfdtFullJoinState::getApproverChildID)).entrySet()
									.stream().map(t -> {
										return new ApproverState(
												rootStateID,
												phaseOrder,
												frameOrder,
												t.getValue().get(0).getApproverChildID(),
												companyID,
												recordDate);
									}).collect(Collectors.toList());
									return new ApprovalFrame(
											rootStateID,
											phaseOrder,
											frameOrder,
											appFrameAtr,
											confirmAtr,
											listApprover,
											frameApproverID,
											representerID,
											approvalDate,
											approvalReason
									);
								}).collect(Collectors.toList());
								return new ApprovalPhaseState(
											rootStateID,
											phaseOrder,
											appPhaseAtr,
											approvalForm,
											listAppFrame
										);
							}).collect(Collectors.toList());
							return new ApprovalRootState(rootStateID, RootType.EMPLOYMENT_APPLICATION, historyID, recordDate, employeeID, listAppPhase);
						}).collect(Collectors.toList());
				result.addAll(entityRoot);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		return result;
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
	public void update(ApprovalRootState approvalRootState, Integer rootType) {
		switch (rootType) {
		case 1:
			WwfdtApprovalRootDay wwfdtApprovalRootDay = this.queryProxy()
					.find(new WwfdpApprovalRootDayPK(approvalRootState.getRootStateID()), WwfdtApprovalRootDay.class).get();
			wwfdtApprovalRootDay.listWwfdtApprovalPhaseDay = approvalRootState.getListApprovalPhaseState().stream()
					.map(x -> updateEntityWwfdtApprovalPhaseDay(x)).collect(Collectors.toList());
			this.commandProxy().update(wwfdtApprovalRootDay);
			break;
		case 2:
			WwfdtApprovalRootMonth wwfdtApprovalRootMonth = this.queryProxy()
					.find(new WwfdpApprovalRootMonthPK(approvalRootState.getRootStateID()), WwfdtApprovalRootMonth.class).get();
			wwfdtApprovalRootMonth.listWwfdtApprovalPhaseMonth = approvalRootState.getListApprovalPhaseState().stream()
					.map(x -> updateEntityWwfdtApprovalPhaseMonth(x)).collect(Collectors.toList());
			this.commandProxy().update(wwfdtApprovalRootMonth);
			break;
		default:
			WwfdtApprovalRootState wwfdtApprovalRootState = this.queryProxy()
					.find(new WwfdpApprovalRootStatePK(approvalRootState.getRootStateID()), WwfdtApprovalRootState.class).get();
			wwfdtApprovalRootState.listWwfdtApprovalPhaseState = approvalRootState.getListApprovalPhaseState().stream()
					.map(x -> updateEntityWwfdtApprovalPhaseState(x)).collect(Collectors.toList());
			this.commandProxy().update(wwfdtApprovalRootState);
		}
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
			this.commandProxy().remove(WwfdtApprovalRootState.class, new WwfdpApprovalRootStatePK(rootStateID));
		}
	}
	
	private WwfdtApprovalPhaseState updateEntityWwfdtApprovalPhaseState(ApprovalPhaseState approvalPhaseState) {
		WwfdtApprovalPhaseState wwfdtApprovalPhaseState = this.queryProxy().find(
				new WwfdpApprovalPhaseStatePK(
						approvalPhaseState.getRootStateID(), 
						approvalPhaseState.getPhaseOrder()), WwfdtApprovalPhaseState.class).get();
		wwfdtApprovalPhaseState.approvalAtr = approvalPhaseState.getApprovalAtr().value;
		wwfdtApprovalPhaseState.approvalForm = approvalPhaseState.getApprovalForm().value;
		wwfdtApprovalPhaseState.listWwfdtApprovalFrame = approvalPhaseState.getListApprovalFrame().stream()
				.map(x -> updateEntityWwfdtApprovalFrame(x)).collect(Collectors.toList());
		return wwfdtApprovalPhaseState;
	}

	private WwfdtApprovalFrame updateEntityWwfdtApprovalFrame(ApprovalFrame approvalFrame){
		WwfdtApprovalFrame wwfdtApprovalFrame = this.queryProxy().find(
				new WwfdpApprovalFramePK(
						approvalFrame.getRootStateID(), 
						approvalFrame.getPhaseOrder(), 
						approvalFrame.getFrameOrder()), WwfdtApprovalFrame.class).get();
		wwfdtApprovalFrame.approvalAtr = approvalFrame.getApprovalAtr().value;
		wwfdtApprovalFrame.confirmAtr = approvalFrame.getConfirmAtr().value;
		wwfdtApprovalFrame.approverID = approvalFrame.getApproverID();
		wwfdtApprovalFrame.representerID = approvalFrame.getRepresenterID();
		wwfdtApprovalFrame.approvalDate = approvalFrame.getApprovalDate();
		wwfdtApprovalFrame.approvalReason = approvalFrame.getApprovalReason();
		wwfdtApprovalFrame.listWwfdtApproverState = approvalFrame.getListApproverState().stream()
				.map(x -> updateEntityWwfdtApproverState(x)).collect(Collectors.toList());
		return wwfdtApprovalFrame;
	}

	private WwfdtApproverState updateEntityWwfdtApproverState(ApproverState approverState) {
		WwfdtApproverState wwfdtApproverState = this.queryProxy().find(
				new WwfdpApproverStatePK(
						approverState.getRootStateID(), 
						approverState.getPhaseOrder(), 
						approverState.getFrameOrder(), 
						approverState.getApproverID()), WwfdtApproverState.class).get();
		return wwfdtApproverState;
	}
	
	private WwfdtApprovalPhaseDay updateEntityWwfdtApprovalPhaseDay(ApprovalPhaseState approvalPhaseState) {
		WwfdtApprovalPhaseDay wwfdtApprovalPhaseDay = this.queryProxy().find(
				new WwfdpApprovalPhaseDayPK(
						approvalPhaseState.getRootStateID(), 
						approvalPhaseState.getPhaseOrder()), WwfdtApprovalPhaseDay.class).get();
		wwfdtApprovalPhaseDay.approvalAtr = approvalPhaseState.getApprovalAtr().value;
		wwfdtApprovalPhaseDay.approvalForm = approvalPhaseState.getApprovalForm().value;
		wwfdtApprovalPhaseDay.listWwfdtApprovalFrameDay = approvalPhaseState.getListApprovalFrame().stream()
				.map(x -> updateEntityWwfdtApprovalFrameDay(x)).collect(Collectors.toList());
		return wwfdtApprovalPhaseDay;
	}

	private WwfdtApprovalFrameDay updateEntityWwfdtApprovalFrameDay(ApprovalFrame approvalFrame){
		WwfdtApprovalFrameDay wwfdtApprovalFrameDay = this.queryProxy().find(
				new WwfdpApprovalFrameDayPK(
						approvalFrame.getRootStateID(), 
						approvalFrame.getPhaseOrder(), 
						approvalFrame.getFrameOrder()), WwfdtApprovalFrameDay.class).get();
		wwfdtApprovalFrameDay.approvalAtr = approvalFrame.getApprovalAtr().value;
		wwfdtApprovalFrameDay.confirmAtr = approvalFrame.getConfirmAtr().value;
		wwfdtApprovalFrameDay.approverID = approvalFrame.getApproverID();
		wwfdtApprovalFrameDay.representerID = approvalFrame.getRepresenterID();
		wwfdtApprovalFrameDay.approvalDate = approvalFrame.getApprovalDate();
		wwfdtApprovalFrameDay.approvalReason = approvalFrame.getApprovalReason();
		wwfdtApprovalFrameDay.listWwfdtApproverDay = approvalFrame.getListApproverState().stream()
				.map(x -> updateEntityWwfdtApproverDay(x)).collect(Collectors.toList());
		return wwfdtApprovalFrameDay;
	}

	private WwfdtApproverDay updateEntityWwfdtApproverDay(ApproverState approverState) {
		WwfdtApproverDay wwfdtApproverDay = this.queryProxy().find(
				new WwfdpApproverDayPK(
						approverState.getRootStateID(), 
						approverState.getPhaseOrder(), 
						approverState.getFrameOrder(), 
						approverState.getApproverID()), WwfdtApproverDay.class).get();
		return wwfdtApproverDay;
	}
	
	private WwfdtApprovalPhaseMonth updateEntityWwfdtApprovalPhaseMonth(ApprovalPhaseState approvalPhaseState) {
		WwfdtApprovalPhaseMonth wwfdtApprovalPhaseMonth = this.queryProxy().find(
				new WwfdpApprovalPhaseMonthPK(
						approvalPhaseState.getRootStateID(), 
						approvalPhaseState.getPhaseOrder()), WwfdtApprovalPhaseMonth.class).get();
		wwfdtApprovalPhaseMonth.approvalAtr = approvalPhaseState.getApprovalAtr().value;
		wwfdtApprovalPhaseMonth.approvalForm = approvalPhaseState.getApprovalForm().value;
		wwfdtApprovalPhaseMonth.listWwfdtApprovalFrameMonth = approvalPhaseState.getListApprovalFrame().stream()
				.map(x -> updateEntityWwfdtApprovalFrameMonth(x)).collect(Collectors.toList());
		return wwfdtApprovalPhaseMonth;
	}

	private WwfdtApprovalFrameMonth updateEntityWwfdtApprovalFrameMonth(ApprovalFrame approvalFrame){
		WwfdtApprovalFrameMonth wwfdtApprovalFrameMonth = this.queryProxy().find(
				new WwfdpApprovalFrameMonthPK(
						approvalFrame.getRootStateID(), 
						approvalFrame.getPhaseOrder(), 
						approvalFrame.getFrameOrder()), WwfdtApprovalFrameMonth.class).get();
		wwfdtApprovalFrameMonth.approvalAtr = approvalFrame.getApprovalAtr().value;
		wwfdtApprovalFrameMonth.confirmAtr = approvalFrame.getConfirmAtr().value;
		wwfdtApprovalFrameMonth.approverID = approvalFrame.getApproverID();
		wwfdtApprovalFrameMonth.representerID = approvalFrame.getRepresenterID();
		wwfdtApprovalFrameMonth.approvalDate = approvalFrame.getApprovalDate();
		wwfdtApprovalFrameMonth.approvalReason = approvalFrame.getApprovalReason();
		wwfdtApprovalFrameMonth.listWwfdtApproverMonth = approvalFrame.getListApproverState().stream()
				.map(x -> updateEntityWwfdtApproverMonth(x)).collect(Collectors.toList());
		return wwfdtApprovalFrameMonth;
	}

	private WwfdtApproverMonth updateEntityWwfdtApproverMonth(ApproverState approverState) {
		WwfdtApproverMonth wwfdtApproverMonth = this.queryProxy().find(
				new WwfdpApproverMonthPK(
						approverState.getRootStateID(), 
						approverState.getPhaseOrder(), 
						approverState.getFrameOrder(), 
						approverState.getApproverID()), WwfdtApproverMonth.class).get();
		return wwfdtApproverMonth;
	}

	@Override
	public List<ApprovalRootState> findEmployeeAppByApprovalRecordDate(GeneralDate startDate, GeneralDate endDate,
			String approverID, Integer rootType) {
		List<ApprovalRootState> result  = new ArrayList<>();
		switch (rootType) {
		case 1:
			result = this.queryProxy().query(SELECT_CF_DAY_BY_DATE, WwfdtApprovalRootDay.class)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList(x -> x.toDomain());
			break;
		case 2:
			result = this.queryProxy().query(SELECT_CF_MONTH_BY_DATE, WwfdtApprovalRootMonth.class)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList(x -> x.toDomain());
			break;
		default:
			result = this.queryProxy().query(SELECT_APP_BY_DATE, WwfdtApprovalRootState.class)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getList(x -> x.toDomain());
		}
		return result;
	}

	@Override
	public List<ApprovalRootState> findAppByEmployeeIDRecordDate(GeneralDate startDate, GeneralDate endDate,
			String employeeID, Integer rootType) {
		switch (rootType) {
		case 1:
			return this.queryProxy().query(SELECT_CF_DAY_BY_EMP_DATE, WwfdtApprovalRootDay.class)
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate)
					.setParameter("employeeID", employeeID).getList(x -> x.toDomain());
		case 2:
			return this.queryProxy().query(SELECT_CF_MONTH_BY_EMP_DATE, WwfdtApprovalRootMonth.class)
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate)
					.setParameter("employeeID", employeeID).getList(x -> x.toDomain());
		default:
			return this.queryProxy().query(SELECT_APP_BY_EMP_DATE, WwfdtApprovalRootState.class)
					.setParameter("startDate", startDate)
					.setParameter("endDate", endDate)
					.setParameter("employeeID", employeeID).getList(x -> x.toDomain());
		}
	}

	@Override
	public List<ApprovalRootState> getRootStateByApproverDate(String companyID, String approverID, GeneralDate date){
		List<String> listAppRootStateSimp = 
				this.queryProxy().query(SELECT_SIMPLE_APP_BY_DATE, WwfdtAppRootStateSimple.class)
					.setParameter("startDate", date)
					.setParameter("endDate", date).getList(x -> x.wwfdpApprovalRootStatePK.rootStateID);
		List<ApprovalRootState> listAppRootState = new ArrayList<>();
		Connection con = this.getEntityManager().unwrap(Connection.class);
		CollectionUtil.split(listAppRootStateSimp, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			try {
				String rootStateIDLst = "";
				if(CollectionUtil.isEmpty(subList)){
					rootStateIDLst = "''";
				} else {
					for(int i = 0; i < subList.size(); i++){
						rootStateIDLst+="'"+subList.get(i)+"'";
						if(i != (subList.size()-1)){
							rootStateIDLst+=",";
						}
					}
				}
				String query = BASIC_SELECT + 
						" WHERE root.ROOT_STATE_ID IN (rootStateIDs)" +
						" AND approver.CID = 'companyID'";
				query = query.replaceFirst("rootStateIDs", rootStateIDLst);
				query = query.replaceAll("companyID", companyID);
				List<WwfdtFullJoinState> listFullData = new ArrayList<>();
				try (PreparedStatement pstatement = con.prepareStatement(query)) {
					ResultSet rs = pstatement.executeQuery();
					while (rs.next()) {
						listFullData.add(new WwfdtFullJoinState(
								rs.getString("ROOT_STATE_ID"), 
								rs.getString("HIST_ID"), 
								rs.getString("EMPLOYEE_ID"), 
								GeneralDate.fromString(rs.getString("APPROVAL_RECORD_DATE"), "yyyy-MM-dd"), 
								Integer.valueOf(rs.getString("PHASE_ORDER")), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APPROVAL_FORM")), ApprovalForm.class), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APP_PHASE_ATR")), ApprovalBehaviorAtr.class), 
								Integer.valueOf(rs.getString("FRAME_ORDER")), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APP_FRAME_ATR")), ApprovalBehaviorAtr.class), 
								EnumAdaptor.valueOf(Integer.valueOf(rs.getString("CONFIRM_ATR")), ConfirmPerson.class), 
								rs.getString("APPROVER_ID"), 
								rs.getString("REPRESENTER_ID"), 
								rs.getString("APPROVAL_DATE") != null ? GeneralDate.fromString(rs.getString("APPROVAL_DATE"), "yyyy-MM-dd HH:mm:ss") : null, 
								rs.getString("APPROVAL_REASON"), 
								rs.getString("APPROVER_CHILD_ID"),
								rs.getString("CID")));
					}
				}
				listAppRootState.addAll(this.convertFromSQLResult(listFullData));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		return listAppRootState;
	}

	@Override
	public List<ApprovalRootState> findEmployeeAppByApprovalRecordDateAndNoRootType(String companyID, 
			GeneralDate startDate, GeneralDate endDate, String approverID) {
		List<ApprovalRootState> result  = new ArrayList<>();
		result.addAll(this.queryProxy().query(SELECT_APPS_BY_APPROVER, WwfdtApprovalRootState.class)
				.setParameter("companyID", companyID)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("approverID", approverID)
				.getList(x -> x.toDomain()));
		result.addAll(this.queryProxy().query(SELECT_CFS_DAY_BY_APPROVER, WwfdtApprovalRootDay.class)
				.setParameter("companyID", companyID)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("approverID", approverID)
				.getList(x -> x.toDomain()));
		result.addAll(this.queryProxy().query(SELECT_CFS_MONTH_BY_APPROVER, WwfdtApprovalRootMonth.class)
				.setParameter("companyID", companyID)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("approverID", approverID)
				.getList(x -> x.toDomain()));
		return result;
	}

	@Override
	public List<ApprovalRootState> findAppByListEmployeeIDRecordDate(GeneralDate startDate, GeneralDate endDate,
			List<String> employeeIDs, Integer rootType) {
		return this.queryProxy().query(SELECT_BY_LIST_EMP_DATE, WwfdtApprovalRootState.class)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("rootType", rootType)
				.setParameter("employeeID", employeeIDs).getList(x -> x.toDomain());
	}

	@Override
	public List<ApprovalRootState> findAppByListEmployeeIDAndListRecordDate(List<GeneralDate> approvalRecordDates,
			List<String> employeeIDs, Integer rootType) {
		switch (rootType) {
		case 1:
			return this.queryProxy().query(SELECT_CFS_DAY_BY_EMP_AND_DATES, WwfdtApprovalRootDay.class)
				.setParameter("recordDate", approvalRecordDates)
				.setParameter("employeeID", employeeIDs)
				.getList(x -> x.toDomain());
		case 2:
			return this.queryProxy().query(SELECT_CFS_MONTH_BY_EMP_AND_DATES, WwfdtApprovalRootMonth.class)
					.setParameter("recordDate", approvalRecordDates)
					.setParameter("employeeID", employeeIDs)
					.getList(x -> x.toDomain());
		default:
			return this.queryProxy().query(SELECT_APPS_BY_EMP_AND_DATES, WwfdtApprovalRootState.class)
					.setParameter("recordDate", approvalRecordDates)
					.setParameter("employeeID", employeeIDs)
					.getList(x -> x.toDomain());
		}
	}

	@Override
	public List<ApprovalRootState> findByApprover(String companyID, GeneralDate startDate, GeneralDate endDate, 
			String approverID, Integer rootType) {
		List<ApprovalRootState> result  = new ArrayList<>();
		switch (rootType) {
		case 1:
			result = this.queryProxy().query(SELECT_CFS_DAY_BY_APPROVER, WwfdtApprovalRootDay.class)
				.setParameter("companyID", companyID)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("approverID", approverID)
				.getList(x -> x.toDomain());
			break;
		case 2:
			result = this.queryProxy().query(SELECT_CFS_MONTH_BY_APPROVER, WwfdtApprovalRootMonth.class)
				.setParameter("companyID", companyID)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("approverID", approverID)
				.getList(x -> x.toDomain());
			break;
		default:
			result = this.queryProxy().query(SELECT_APPS_BY_APPROVER, WwfdtApprovalRootState.class)
				.setParameter("companyID", companyID)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("approverID", approverID)
				.getList(x -> x.toDomain());
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
		List<WwfdpApprovalRootDayPK> rootDayKeyList = this.queryProxy().query(SELECT_CF_DAY_BY_EMP_DATE_SP, WwfdtAppRootDaySimple.class)
				.setParameter("startDate", date)
				.setParameter("endDate", date)
				.setParameter("employeeID", employeeID)
				.getList(x -> new WwfdpApprovalRootDayPK(x.wwfdpApprovalRootDayPK.rootStateID));
		this.commandProxy().removeAll(WwfdtApprovalRootDay.class, rootDayKeyList);
	}

	@Override
	public List<ApprovalPhaseState> findPhaseApprovalMax(String appID) {
		return this.queryProxy().query(FIND_PHASE_APPROVAL_MAX, WwfdtApprovalPhaseState.class)
				.setParameter("appID", appID)
				.getList(c->toDomainPhase(c));
	}
	private ApprovalPhaseState toDomainPhase(WwfdtApprovalPhaseState entity){
		return new ApprovalPhaseState(entity.wwfdpApprovalPhaseStatePK.rootStateID, 
				entity.wwfdpApprovalPhaseStatePK.phaseOrder,
				EnumAdaptor.valueOf(entity.approvalAtr, ApprovalBehaviorAtr.class),
				EnumAdaptor.valueOf(entity.approvalForm, ApprovalForm.class), 
				null);
	}
	
	private List<ApprovalRootState> convertFromSQLResult(List<WwfdtFullJoinState> listFullData){
		return listFullData.stream().collect(Collectors.groupingBy(WwfdtFullJoinState::getRootStateID)).entrySet()
				.stream().map(x -> {
					String rootStateID = x.getValue().get(0).getRootStateID();
					String historyID = x.getValue().get(0).getHistoryID();
					GeneralDate recordDate = x.getValue().get(0).getRecordDate();
					String employeeID = x.getValue().get(0).getEmployeeID();
					List<ApprovalPhaseState> listAppPhase =
					x.getValue().stream().collect(Collectors.groupingBy(WwfdtFullJoinState::getPhaseOrder)).entrySet()
					.stream().map(y -> {
						Integer phaseOrder  = y.getValue().get(0).getPhaseOrder();
						ApprovalForm approvalForm = y.getValue().get(0).getApprovalForm();
						ApprovalBehaviorAtr appPhaseAtr =  y.getValue().get(0).getAppPhaseAtr();
						List<ApprovalFrame> listAppFrame =
						y.getValue().stream().collect(Collectors.groupingBy(WwfdtFullJoinState::getFrameOrder)).entrySet()
						.stream().map(z -> { 
							Integer frameOrder = z.getValue().get(0).getFrameOrder();
							ConfirmPerson confirmAtr = z.getValue().get(0).getConfirmAtr();
							String frameApproverID =  z.getValue().get(0).getApproverID();
							String representerID = z.getValue().get(0).getRepresenterID();
							GeneralDate approvalDate = z.getValue().get(0).getApprovalDate();
							String approvalReason = z.getValue().get(0).getApprovalReason();
							ApprovalBehaviorAtr appFrameAtr = z.getValue().get(0).getAppFrameAtr();
							List<ApproverState> listApprover = z.getValue().stream().collect(Collectors.groupingBy(WwfdtFullJoinState::getApproverChildID)).entrySet()
							.stream().map(t -> {
								return new ApproverState(
										rootStateID,
										phaseOrder,
										frameOrder,
										t.getValue().get(0).getApproverChildID(),
										t.getValue().get(0).getCompanyID(),
										recordDate);
							}).collect(Collectors.toList());
							return new ApprovalFrame(
									rootStateID,
									phaseOrder,
									frameOrder,
									appFrameAtr,
									confirmAtr,
									listApprover,
									frameApproverID,
									representerID,
									approvalDate,
									approvalReason
							);
						}).collect(Collectors.toList());
						return new ApprovalPhaseState(
									rootStateID,
									phaseOrder,
									appPhaseAtr,
									approvalForm,
									listAppFrame
								);
					}).collect(Collectors.toList());
					return new ApprovalRootState(rootStateID, RootType.EMPLOYMENT_APPLICATION, historyID, recordDate, employeeID, listAppPhase);
				}).collect(Collectors.toList());
	}

	@Override
	public List<ApprovalRootState> getByApproverPeriod(String companyID, String approverID, DatePeriod period) {
		List<ApprovalRootState> listAppRootState = new ArrayList<>();
		Connection con = this.getEntityManager().unwrap(Connection.class);
		try {
			String query = BASIC_SELECT + 
					" LEFT JOIN KRQDT_APPLICATION app" +
					" ON root.ROOT_STATE_ID = app.APP_ID" +
					" WHERE approver.APPROVER_CHILD_ID = 'approverID'" +
					" AND approver.CID = 'companyID'" +
					" AND app.APP_DATE >= 'startDate'" +
					" AND app.APP_DATE <= 'endDate'";
			query = query.replaceAll("startDate", period.start().toString("yyyy-MM-dd"));
			query = query.replaceAll("endDate", period.end().toString("yyyy-MM-dd"));
			query = query.replaceAll("approverID", approverID);
			query = query.replaceAll("companyID", companyID);
			List<WwfdtFullJoinState> listFullData = new ArrayList<>();
			try (PreparedStatement pstatement = con.prepareStatement(query)) {
				ResultSet rs = pstatement.executeQuery();
				while (rs.next()) {
					listFullData.add(new WwfdtFullJoinState(
							rs.getString("ROOT_STATE_ID"), 
							rs.getString("HIST_ID"), 
							rs.getString("EMPLOYEE_ID"), 
							GeneralDate.fromString(rs.getString("APPROVAL_RECORD_DATE"), "yyyy-MM-dd"), 
							Integer.valueOf(rs.getString("PHASE_ORDER")), 
							EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APPROVAL_FORM")), ApprovalForm.class), 
							EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APP_PHASE_ATR")), ApprovalBehaviorAtr.class), 
							Integer.valueOf(rs.getString("FRAME_ORDER")), 
							EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APP_FRAME_ATR")), ApprovalBehaviorAtr.class), 
							EnumAdaptor.valueOf(Integer.valueOf(rs.getString("CONFIRM_ATR")), ConfirmPerson.class), 
							rs.getString("APPROVER_ID"), 
							rs.getString("REPRESENTER_ID"), 
							rs.getString("APPROVAL_DATE") != null ? GeneralDate.fromString(rs.getString("APPROVAL_DATE"), "yyyy-MM-dd HH:mm:ss") : null, 
							rs.getString("APPROVAL_REASON"), 
							rs.getString("APPROVER_CHILD_ID"),
							rs.getString("CID")));
				}
			}
			listAppRootState = this.convertFromSQLResult(listFullData);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listAppRootState;
	}

	@Override
	public List<ApprovalRootState> getByApproverAgentPeriod(String companyID, String approverID, DatePeriod period, DatePeriod agentPeriod) {
		List<ApprovalRootState> listAppRootState = new ArrayList<>();
		Connection con = this.getEntityManager().unwrap(Connection.class);
		try {
			String query = BASIC_SELECT + 
					" LEFT JOIN KRQDT_APPLICATION app" +
					" ON root.ROOT_STATE_ID = app.APP_ID" +
					" WHERE approver.APPROVER_CHILD_ID = 'approverID'" +
					" AND approver.CID = 'companyID'" +
					" AND app.APP_DATE >= 'startDate'" +
					" AND app.APP_DATE <= 'endDate'" +
					" AND app.APP_DATE >= 'agentStartDate'" +
					" AND app.APP_DATE <= 'agentEndDate'";
			query = query.replaceAll("startDate", period.start().toString("yyyy-MM-dd"));
			query = query.replaceAll("endDate", period.end().toString("yyyy-MM-dd"));
			query = query.replaceAll("agentStartDate", agentPeriod.start().toString("yyyy-MM-dd"));
			query = query.replaceAll("agentEndDate", agentPeriod.end().toString("yyyy-MM-dd"));
			query = query.replaceAll("approverID", approverID);
			query = query.replaceAll("companyID", companyID);
			List<WwfdtFullJoinState> listFullData = new ArrayList<>();
			try (PreparedStatement pstatement = con.prepareStatement(query)) {
				ResultSet rs = pstatement.executeQuery();
				while (rs.next()) {
					listFullData.add(new WwfdtFullJoinState(
							rs.getString("ROOT_STATE_ID"), 
							rs.getString("HIST_ID"), 
							rs.getString("EMPLOYEE_ID"), 
							GeneralDate.fromString(rs.getString("APPROVAL_RECORD_DATE"), "yyyy-MM-dd"), 
							Integer.valueOf(rs.getString("PHASE_ORDER")), 
							EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APPROVAL_FORM")), ApprovalForm.class), 
							EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APP_PHASE_ATR")), ApprovalBehaviorAtr.class), 
							Integer.valueOf(rs.getString("FRAME_ORDER")), 
							EnumAdaptor.valueOf(Integer.valueOf(rs.getString("APP_FRAME_ATR")), ApprovalBehaviorAtr.class), 
							EnumAdaptor.valueOf(Integer.valueOf(rs.getString("CONFIRM_ATR")), ConfirmPerson.class), 
							rs.getString("APPROVER_ID"), 
							rs.getString("REPRESENTER_ID"), 
							rs.getString("APPROVAL_DATE") != null ? GeneralDate.fromString(rs.getString("APPROVAL_DATE"), "yyyy-MM-dd HH:mm:ss") : null, 
							rs.getString("APPROVAL_REASON"), 
							rs.getString("APPROVER_CHILD_ID"),
							rs.getString("CID")));
				}
			}
			listAppRootState = this.convertFromSQLResult(listFullData);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listAppRootState;
	}
}
