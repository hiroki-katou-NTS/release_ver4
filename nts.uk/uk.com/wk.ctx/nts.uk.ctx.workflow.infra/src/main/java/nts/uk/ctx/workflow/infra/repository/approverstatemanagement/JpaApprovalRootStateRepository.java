package nts.uk.ctx.workflow.infra.repository.approverstatemanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.database.DatabaseProduct;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
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
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.FullJoinAppApvState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtAppApvApproverState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtAppApvFrameState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtAppApvPhaseState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtAppApvRootState;
import nts.uk.ctx.workflow.infra.entity.resultrecord.FullJoinAppRootConfirm;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaApprovalRootStateRepository extends JpaRepository implements ApprovalRootStateRepository {

	@SneakyThrows
	private FullJoinAppApvState createFullJoinAppApvState(NtsResultRecord rs){
				return new FullJoinAppApvState(
						rs.getString("CID"), 
						rs.getString("APP_ID"), 
						rs.getString("EMPLOYEE_ID"), 
						GeneralDate.fromString(rs.getString("APP_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						rs.getInt("PHASE_ORDER"), 
						rs.getInt("APP_PHASE_ATR"), 
						rs.getInt("APPROVAL_FORM"), 
						rs.getInt("FRAME_ORDER"), 
						rs.getInt("APP_FRAME_ATR"), 
						rs.getInt("CONFIRM_ATR"), 
						rs.getString("APPROVER_ID"), 
						rs.getString("REPRESENTER_ID"), 
						GeneralDate.fromString(rs.getString("APPROVAL_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						rs.getString("APPROVAL_REASON"), 
						rs.getString("APPROVER_CHILD_ID"));
	}
	
	private static List<ApprovalRootState> toDomain(List<FullJoinAppApvState> listFullJoin){
		return listFullJoin.stream().collect(Collectors.groupingBy(r -> r.getAppID()))
				.entrySet().stream()
				.map(r -> {
					String appId = r.getKey();
					List<FullJoinAppApvState> fullJoinsInRoot = r.getValue();
					return toDomainRoot(appId, fullJoinsInRoot);
				}).collect(Collectors.toList());
	}

	private static ApprovalRootState toDomainRoot(String appId, List<FullJoinAppApvState> fullJoinsInRoot) {
		FullJoinAppApvState first = fullJoinsInRoot.get(0);
		//↓Phase以降の情報はNULLのときがある
		List<ApprovalPhaseState> phases = new ArrayList<ApprovalPhaseState>();
		if(first.getPhaseOrder() != null) {
			phases = fullJoinsInRoot.stream().collect(Collectors.groupingBy(p -> p.getPhaseOrder()))
					.entrySet().stream()
					.map(p -> {
						Integer phaseOrder = p.getKey();
						List<FullJoinAppApvState> fullJoinInPhase = p.getValue();
						return toDomainPhase(appId, phaseOrder, fullJoinInPhase);
					}).collect(Collectors.toList());
		}	
		return ApprovalRootState.builder()
				.CompanyID(first.getCompanyID())
				.rootStateID(appId)
				.employeeID(first.getEmployeeID())
				.approvalRecordDate(first.getAppDate())
				.listApprovalPhaseState(phases)
				.build();
	}

	private static ApprovalPhaseState toDomainPhase(String appId, Integer phaseOrder, List<FullJoinAppApvState> fullJoinInPhase) {
		FullJoinAppApvState first = fullJoinInPhase.get(0);
		List<ApprovalFrame> frames = fullJoinInPhase.stream().collect(Collectors.groupingBy(f -> f.getFrameOrder()))
				.entrySet().stream()
				.map(f -> {
					Integer frameOrder = f.getKey();
					List<FullJoinAppApvState> fullJoinInFrame = f.getValue();
					return toDomainFrame(appId, phaseOrder, frameOrder, fullJoinInFrame);
				}).collect(Collectors.toList());
		return ApprovalPhaseState.builder()
				.rootStateID(appId)
				.phaseOrder(phaseOrder)
				.approvalAtr(ApprovalBehaviorAtr.of(first.getAppPhaseAtr()))
				.approvalForm(ApprovalForm.of(first.getApprovalForm()))
				.listApprovalFrame(frames)
				.build();
	}

	private static ApprovalFrame toDomainFrame(String appId, Integer phaseOrder, Integer frameOrder, List<FullJoinAppApvState> fullJoinInFrame) {
		FullJoinAppApvState first = fullJoinInFrame.get(0);
		List<ApproverState> approvers = fullJoinInFrame.stream().collect(Collectors.groupingBy(a -> a.getApproverChildID()))
				.entrySet().stream()
				.map(a -> {
					List<FullJoinAppApvState> fullJoinInApprover = a.getValue();
					return toDomainApprover(appId, phaseOrder, frameOrder,
							fullJoinInApprover);
				}).collect(Collectors.toList());
		return ApprovalFrame.builder()
				.rootStateID(appId)
				.phaseOrder(phaseOrder)
				.frameOrder(first.getFrameOrder())
				.approvalAtr(ApprovalBehaviorAtr.of(first.getAppFrameAtr()))
				.confirmAtr(ConfirmPerson.of(first.getConfirmAtr()))
				.approverID(first.getApproverID())
				.representerID(first.getRepresenterID())
				.approvalDate(first.getApprovalDate())
				.approvalReason(first.getApprovalReason())
				.listApproverState(approvers)
				.build();
	}

	private static ApproverState toDomainApprover(String appId, Integer phaseOrder, Integer frameOrder,List<FullJoinAppApvState> fullJoinInApprover) {
		FullJoinAppApvState first = fullJoinInApprover.get(0);
		return ApproverState.builder()
				.rootStateID(appId)
				.phaseOrder(phaseOrder)
				.frameOrder(frameOrder)
				.approverID(first.getApproverChildID())
				.companyID(first.getCompanyID())
				.date(first.getAppDate())
				.build();
	}

	
	@Override
	public void insert(ApprovalRootState approvalRootState) {
		this.commandProxy().insert(WwfdtAppApvRootState.fromDomain(approvalRootState));
		WwfdtAppApvPhaseState.fromDomain(approvalRootState).forEach(p -> {
			this.commandProxy().insert(p);
		});
		WwfdtAppApvFrameState.fromDomain(approvalRootState).forEach(f -> {
			this.commandProxy().insert(f);
		});
		WwfdtAppApvApproverState.fromDomain(approvalRootState).forEach(a -> {
			this.commandProxy().insert(a);
		});
	}


	@Override
	public void update(ApprovalRootState approvalRootState) {
		this.commandProxy().update(WwfdtAppApvRootState.fromDomain(approvalRootState));
		WwfdtAppApvPhaseState.fromDomain(approvalRootState).forEach(p -> {
			this.commandProxy().update(p);
		});
		WwfdtAppApvFrameState.fromDomain(approvalRootState).forEach(f -> {
			this.commandProxy().update(f);
		});
		WwfdtAppApvApproverState.fromDomain(approvalRootState).forEach(a -> {
			this.commandProxy().update(a);
		});
	}
	
	private static List<String> DELETE_TABLE = Arrays.asList(
			"delete from WWFDT_APP_APV_RT_STATE",
			"delete from WWFDT_APP_APV_PH_STATE",
			"delete from WWFDT_APP_APV_FR_STATE",
			"delete from WWFDT_APP_APV_AP_STATE"
	);
	@Override
	public void delete(String rootStateID) {
		DELETE_TABLE.forEach(targetTable ->{
			String sqlDelete = targetTable + " where rt.APP_ID = @appId ";
			jdbcProxy().query(sqlDelete.toString()).paramString("appId", rootStateID);
		});
	}
	
	private static final String FIND_APP_STATE 
			= " select rt.CID, rt.APP_ID, rt.EMPLOYEE_ID, rt.APP_DATE, "
				+ " ph.PHASE_ORDER, ph.APP_PHASE_ATR, ph.APPROVAL_FORM, "
				+ " fr.FRAME_ORDER, fr.APP_FRAME_ATR, fr.CONFIRM_ATR, fr.APPROVER_ID, fr.REPRESENTER_ID, fr.APPROVAL_DATE, fr.APPROVAL_REASON, "
				+ " ap.APPROVER_CHILD_ID "
			+ " from WWFDT_APP_APV_RT_STATE as rt" 
			+ " left join WWFDT_APP_APV_PH_STATE as ph"
			+ " on rt.APP_ID = ph.APP_ID"
			+ " left join WWFDT_APP_APV_FR_STATE as fr" 
			+ " on ph.APP_ID = fr.APP_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER"
			+ " left join WWFDT_APP_APV_AP_STATE as ap"
			+ " on fr.APP_ID = ap.APP_ID" 
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER" 
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER";

	private static final String FIND_APP_STATE_SQL 
			= " select rt.CID, rt.APP_ID, rt.EMPLOYEE_ID, rt.APP_DATE, "
				+ " ph.PHASE_ORDER, ph.APP_PHASE_ATR, ph.APPROVAL_FORM, "
				+ " fr.FRAME_ORDER, fr.APP_FRAME_ATR, fr.CONFIRM_ATR, fr.APPROVER_ID, fr.REPRESENTER_ID, fr.APPROVAL_DATE, fr.APPROVAL_REASON, "
				+ " ap.APPROVER_CHILD_ID , ap.CID"
			+ " from WWFDT_APP_APV_RT_STATE as rt" 
			+ " left join WWFDT_APP_APV_PH_STATE as ph"
			+ " with (index(WWFDI_APP_APV_PH_STATE)) " 
			+ " on rt.APP_ID = ph.APP_ID"
			+ " left join WWFDT_APP_APV_FR_STATE as fr" 
			+ " with (index(WWFDI_APP_APV_FR_STATE)) "
			+ " on ph.APP_ID = fr.APP_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER"
			+ " left join WWFDT_APP_APV_AP_STATE as ap"
			+ " with (index(WWFDI_APP_APV_AP_STATE)) "
			+ " on fr.APP_ID = ap.APP_ID" 
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER" 
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER";

	private static final String FIND_OFFICIAL_APP
			= " and ap.APPROVER_CHILD_ID = @approverID";

	private static final String FIND_INSTEAD_APP 
			= " and ap.APPROVER_CHILD_ID in"
			+ " (select c.SID FROM CMMMT_AGENT c "
			+ " where c.AGENT_SID1 = @approverID "
			+ " and c.START_DATE <= @sysDate "
			+ " and c.END_DATE >= @sysDate)";

	
	@Override
	public Optional<ApprovalRootState> findByID(String appID) {
		List<ApprovalRootState> listAppRootState = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_APP_STATE_SQL);
		} else {
			sql.append(FIND_APP_STATE);
		}
		sql.append(" where rt.APP_ID = @appId ");
		
		listAppRootState = toDomain(new NtsStatement(sql.toString(), this.jdbcProxy())
					.paramString("appId", appID)
					.getList(rec -> createFullJoinAppApvState(rec)));
		if (CollectionUtil.isEmpty(listAppRootState)) {
			return Optional.empty();
		} else {
			return Optional.of(listAppRootState.get(0));
		}
	}
	
	@Override
	public List<ApprovalPhaseState> findAppApvMaxPhaseStateByID(String appID) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_APP_STATE_SQL);
		} else {
			sql.append(FIND_APP_STATE);
		}
		sql.append(" where rt.APP_ID = @appId ");
		sql.append(" and ph.APP_PHASE_ATR = 1 "); 
		sql.append(" order by ph.PHASE_ORDER desc ");
		
		return toDomain(new NtsStatement(sql.toString(), this.jdbcProxy())
				.paramString("appId", appID)
				.getList(rec -> createFullJoinAppApvState(rec))).get(0)
				.getListApprovalPhaseState();
	}
	
	@Override
	public List<ApprovalRootState> findAppApvRootStateByIDApprover(List<String> appIDLst, String approverID) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_APP_STATE_SQL);
		} else {
			sql.append(FIND_APP_STATE);
		}
		sql.append(" where rt.APP_ID in @appIDs");
		sql.append(" and rt.APP_DATE >= @startDate ");
		sql.append(" and rt.APP_DATE <= @endDate ");
		//正規ルートの申請
		sql.append(FIND_OFFICIAL_APP);
		
		sql.append(" union ");
		
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_APP_STATE_SQL);
		} else {
			sql.append(FIND_APP_STATE);
		}
		sql.append(" where rt.APP_ID in @appIDs");
		sql.append(" and rt.APP_DATE >= @startDate ");
		sql.append(" and rt.APP_DATE <= @endDate ");
		//代行ルートの申請
		sql.append(FIND_INSTEAD_APP);
		
		return NtsStatement.In.split(appIDLst, appIDs -> {
			return toDomain(new NtsStatement(sql.toString(), this.jdbcProxy())
					.paramString("appIDs", appIDs)
					.paramString("startDate", GeneralDate.today().toString("yyyy-MM-dd"))
					.paramString("endDate", GeneralDate.today().toString("yyyy-MM-dd"))
					.paramString("sysDate", GeneralDate.today().toString("yyyy-MM-dd"))
					.paramString("approverID", approverID)
					.getList(rec -> createFullJoinAppApvState(rec)));
		});
	}

	@Override
	public List<ApprovalRootState> findAppApvRootStateByEmployee(DatePeriod period, String employeeID) {
		return findAppApvRootStateByEmployee(period, Arrays.asList(employeeID));
	}

	@Override
	public List<ApprovalRootState> findAppApvRootStateByEmployee(DatePeriod period, List<String> employeeIDLst) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_APP_STATE_SQL);
		} else {
			sql.append(FIND_APP_STATE);
		}
		sql.append(" where rt.EMPLOYEE_ID in @employeeIDs ");
		sql.append(" and rt.APP_DATE >= @startDate ");
		sql.append(" and rt.APP_DATE <= @endDate ");

		return NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return toDomain(new NtsStatement(sql.toString(), this.jdbcProxy())
					.paramString("employeeIDs", employeeIDs)
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.getList(rec -> createFullJoinAppApvState(rec)));
		});
	}

	@Override
	public List<ApprovalRootState> findAppApvRootStateByEmployee(List<GeneralDate> dates, List<String> employeeIDLst) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_APP_STATE_SQL);
		} else {
			sql.append(FIND_APP_STATE);
		}
		sql.append(" where rt.EMPLOYEE_ID in @employeeIDs ");
		sql.append(" and rt.APP_DATE in @dates ");
		return NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return toDomain(new NtsStatement(sql.toString(), this.jdbcProxy())
					.paramString("employeeIDs", employeeIDs)
					.paramDate("dates", dates)
					.getList(rec -> createFullJoinAppApvState(rec)));
		});
	}

	@Override
	public List<ApprovalRootState> findAppApvRootStateByApprover(GeneralDate date, String approverID) {
		return findAppApvRootStateByApprover(new DatePeriod(date, date), approverID);
	}

	@Override
	public List<ApprovalRootState> findAppApvRootStateByApprover(DatePeriod period, String approverID) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_APP_STATE_SQL);
		} else {
			sql.append(FIND_APP_STATE);
		}
		sql.append(" where rt.APP_DATE >= @startDate ");
		sql.append(" and rt.APP_DATE <= @endDate ");
		//正規ルートの申請
		sql.append(FIND_OFFICIAL_APP);
		
		sql.append(" union ");
		
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_APP_STATE_SQL);
		} else {
			sql.append(FIND_APP_STATE);
		}
		sql.append(" where rt.APP_DATE >= @startDate ");
		sql.append(" and rt.APP_DATE <= @endDate ");
		//代行ルートの申請
		sql.append(FIND_INSTEAD_APP);
		
		return toDomain(new NtsStatement(sql.toString(), this.jdbcProxy())
				.paramDate("startDate", period.start())
				.paramDate("endDate", period.end())
				.paramString("sysDate", GeneralDate.today().toString("yyyy-MM-dd"))
				.paramString("approverID", approverID)
				.getList(rec -> createFullJoinAppApvState(rec)));
	}

	@Override
	public List<ApprovalRootState> findAppApvRootStateByApprover(List<String> approverIDLst, DatePeriod period, 
			boolean unapprovalStatus, boolean approvalStatus, boolean denialStatus, boolean agentApprovalStatus, boolean remandStatus, boolean cancelStatus) {
		//Phase承認状態
		List<Integer> lstPhaseStt = new ArrayList<>();
		if(unapprovalStatus || approvalStatus || denialStatus || agentApprovalStatus){
			lstPhaseStt.add(0);
		}
		if(approvalStatus || agentApprovalStatus || denialStatus){
			lstPhaseStt.add(1);
		}
		if(denialStatus || remandStatus){
			lstPhaseStt.add(3);
		}
		if(denialStatus || cancelStatus){
			lstPhaseStt.add(2);
			lstPhaseStt.add(4);
		}
		//Frame承認状態
		List<Integer> lstFrameStt = new ArrayList<>();
		if(unapprovalStatus || approvalStatus || denialStatus || remandStatus || cancelStatus){
			lstFrameStt.add(0);
		}
		if(approvalStatus || denialStatus || agentApprovalStatus || remandStatus || cancelStatus){
			lstFrameStt.add(1);
		}
		if(denialStatus || remandStatus || cancelStatus){
			lstFrameStt.add(2);
			lstFrameStt.add(3);
			lstFrameStt.add(4);
		}

		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_APP_STATE_SQL);
		} else {
			sql.append(FIND_APP_STATE);
		}
		sql.append(" where rt.APP_DATE >= @startDate ");
		sql.append(" and rt.APP_DATE <= @endDate ");
		sql.append(" and ph.APP_PHASE_ATR in @phaseAtr ");
		sql.append(" and fr.APP_FRAME_ATR in @frameAtr ");
		sql.append(" and ap.APPROVER_CHILD_ID in @approverIDs");
		
		return NtsStatement.In.split(approverIDLst, approverIDs -> {
			return toDomain(new NtsStatement(sql.toString(), this.jdbcProxy())
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.paramInt("phaseAtr", lstPhaseStt)
					.paramInt("frameAtr", lstFrameStt)
					.paramString("approverIDs", approverIDs)
					.getList(rec -> createFullJoinAppApvState(rec)));
		});


	}

	@Override
	public boolean checkAppShouldApproval(DatePeriod period) {
		String loginSID = AppContexts.user().employeeId();
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) ");
		sql.append(" from( ");
			sql.append(" select ap.APP_ID ,ap.PHASE_ORDER ");
			sql.append(" from WWFDT_APP_APV_AP_STATE ap ");
			sql.append(" inner join WWFDT_APP_APV_FR_STATE fr ");
			sql.append(" on ap.APP_ID = fr.APP_ID and ap.PHASE_ORDER = fr.PHASE_ORDER and ap.FRAME_ORDER = fr.FRAME_ORDER ");
			sql.append(" where ap.APPROVER_CHILD_ID = @loginSID ");
			sql.append(" and fr.APP_FRAME_ATR = @frameAtr ");
			sql.append(" and ap.APP_DATE >= @startDate ");
			sql.append(" and ap.APP_DATE <= @endDate ");
			sql.append(" union all ");
			sql.append(" select ap.APP_ID ,ap.PHASE_ORDER ");
			sql.append(" from WWFDT_APP_APV_AP_STATE ap ");
			sql.append(" inner join WWFDT_APP_APV_FR_STATE fr ");
			sql.append(" on ap.APP_ID = fr.APP_ID and ap.PHASE_ORDER = fr.PHASE_ORDER and ap.FRAME_ORDER = fr.FRAME_ORDER ");
			sql.append(" inner join CMMMT_AGENT ag ");
			sql.append(" on ap.APPROVER_CHILD_ID = ag.SID " );
			sql.append(" where fr.APP_FRAME_ATR = @frameAtr ");
			sql.append(" and ap.APP_DATE >= @startDate ");
			sql.append(" and ap.APP_DATE <= @endDate ");
			sql.append(" and ag.START_DATE >= @sysDate ");
			sql.append(" and ag.END_DATE <= @sysDate ");
			sql.append(" and ag.AGENT_APP_TYPE1 = @appType1 ");
			sql.append(" and ag.AGENT_SID1 = @agentSID1 ");
		sql.append(" ) as apfr");
		sql.append(" inner join ( ");
			sql.append(" select ph.APP_ID ,min(PHASE_ORDER) AS NOW_PHASE_ORDER ");
			sql.append(" from WWFDT_APP_APV_PH_STATE ph ");
			sql.append(" where ph.APP_PHASE_ATR in ('0','3') ");
			sql.append(" group by ph.APP_ID ");
		sql.append(" ) ph ");
		sql.append(" on apfr.APP_ID = ph.APP_ID");
		sql.append(" and apfr.PHASE_ORDER = ph.NOW_PHASE_ORDER");
		
		
		
		List<Integer> result = new NtsStatement(sql.toString(), this.jdbcProxy())
				.paramString("loginSID", loginSID)
				.paramInt("frameAtr", 0)
				.paramDate("startDate", period.start())
				.paramDate("endDate", period.end())
				.paramDate("sysDate", period.end())
				.paramInt("appType1", 1)
				.paramString("agentSID1", loginSID)
		 		.getList(rec -> {
		 			return new Integer(
		 					rec.getInt("COUNT"));	
		 		});
		if(result.get(0) > 0) {
			return true;
		}
		else {

			return false;
		}
	}
}
