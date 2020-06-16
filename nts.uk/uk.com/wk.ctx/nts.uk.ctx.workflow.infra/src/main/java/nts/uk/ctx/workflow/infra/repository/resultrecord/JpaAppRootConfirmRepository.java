package nts.uk.ctx.workflow.infra.repository.resultrecord;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.layer.infra.data.database.DatabaseProduct;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirmRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.infra.entity.resultrecord.FullJoinAppRootConfirm;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm.WwfdtApvFrameConfirmDaily;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm.WwfdtApvPhaseConfirmDaily;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm.WwfdtApvRootConfirmDaily;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm.WwfdtApvFrameConfirmMonthly;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm.WwfdtApvPhaseConfirmMonthly;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm.WwfdtApvRootConfirmMonthly;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.closure.ClosureMonth;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaAppRootConfirmRepository extends JpaRepository implements AppRootConfirmRepository {

	private List<AppRootConfirm> toDomain(List<FullJoinAppRootConfirm> listFullJoin) {
		return listFullJoin.stream().collect(Collectors.groupingBy(r -> r.getRootID()))
				.entrySet().stream()
				.map(r -> {
					String appId = r.getKey();
					List<FullJoinAppRootConfirm> fullJoinsInRoot = r.getValue();
					return toDomainRoot(appId, fullJoinsInRoot);
				}).collect(Collectors.toList());
	}
	
	private static AppRootConfirm toDomainRoot(String appId, List<FullJoinAppRootConfirm> fullJoinInRoot) {
		FullJoinAppRootConfirm first = fullJoinInRoot.get(0);
		List<AppPhaseConfirm> phases = fullJoinInRoot.stream().collect(Collectors.groupingBy(p -> p.getPhaseOrder()))
				.entrySet().stream()
				.map(p -> {
					Integer phaseOrder = p.getKey();
					List<FullJoinAppRootConfirm> fullJoinInPhase = p.getValue();
					return toDomainPhase(appId, phaseOrder, fullJoinInPhase);
				}).collect(Collectors.toList());
		return AppRootConfirm.builder()
				.rootID(first.getRootID())
				.companyID(first.getCompanyID())
				.employeeID(first.getEmployeeID())
				.recordDate(first.getRecordDate())
				.rootType(RecordRootType.of(first.getRootType()))
				.listAppPhase(phases)
				.yearMonth(Optional.ofNullable(first.getYearMonth()).map(ym -> YearMonth.of(ym)))
				.closureID(Optional.ofNullable(first.getClosureID()))
				.closureDate(Optional.ofNullable(first.getClosureDay()).map(cd -> new ClosureDate(cd, first.getLastDayFlg() == 1)))
				.build();
	}
	
	
	private static AppPhaseConfirm toDomainPhase(String appId, Integer phaseOrder, List<FullJoinAppRootConfirm> fullJoinInPhase) {
		FullJoinAppRootConfirm first = fullJoinInPhase.get(0);
		List<AppFrameConfirm> frames = fullJoinInPhase.stream().collect(Collectors.groupingBy(f -> f.getFrameOrder()))
				.entrySet().stream()
				.map(f -> {
					Integer frameOrder = f.getKey();
					List<FullJoinAppRootConfirm> fullJoinInFrame = f.getValue();
					return toDomainFrame(appId, phaseOrder, frameOrder, fullJoinInFrame);
				}).collect(Collectors.toList());
		return AppPhaseConfirm.builder()
				.phaseOrder(first.getPhaseOrder())
				.appPhaseAtr(ApprovalBehaviorAtr.of(first.getAppPhaseAtr()))
				.listAppFrame(frames)				
				.build();
				
	}

	
	
	private static AppFrameConfirm toDomainFrame(String appId, Integer phaseOrder, Integer frameOrder, List<FullJoinAppRootConfirm> fullJoinInFrame) {
		FullJoinAppRootConfirm first = fullJoinInFrame.get(0);
		return AppFrameConfirm.builder()
				.frameOrder(first.getFrameOrder())
				.approverID(Optional.ofNullable(first.getApproverID()))
				.representerID(Optional.ofNullable(first.getRepresenterID()))
				.approvalDate(first.getApprovalDate())
				.build();
	}


	private FullJoinAppRootConfirm createFullJoinAppRootConfirmDaily(NtsResultRecord rs) {
		return new FullJoinAppRootConfirm(
				rs.getString("ROOT_ID"), 
				rs.getString("CID"), 
				rs.getString("EMPLOYEE_ID"),
				rs.getGeneralDate("RECORD_DATE"), 
				1, 
				null,
				null, 
				null, 
				null, 
				rs.getInt("PHASE_ORDER"),
				rs.getInt("APP_PHASE_ATR"), 
				rs.getInt("FRAME_ORDER"), 
				rs.getString("APPROVER_ID"),
				rs.getString("REPRESENTER_ID"), 
				rs.getGeneralDate("APPROVAL_DATE"));
	}
	
	private FullJoinAppRootConfirm createFullJoinAppRootConfirmMonthly(NtsResultRecord rs) {
		return new FullJoinAppRootConfirm(
				rs.getString("ROOT_ID"), 
				rs.getString("CID"), 
				rs.getString("EMPLOYEE_ID"),
				new ClosureMonth(new YearMonth(
						rs.getInt("YEARMONTH")), 
						rs.getInt("CLOSURE_ID"), 
						new ClosureDate(
								rs.getInt("CLOSURE_DAY"), 
								rs.getBoolean("LAST_DAY_FLG")) ).defaultPeriod().end(),
				2, 
				rs.getInt("YEARMONTH"),
				rs.getInt("CLOSURE_ID"), 
				rs.getInt("CLOSURE_DAY"), 
				rs.getInt("LAST_DAY_FLG"), 
				rs.getInt("PHASE_ORDER"),
				rs.getInt("APP_PHASE_ATR"), 
				rs.getInt("FRAME_ORDER"), 
				rs.getString("APPROVER_ID"),
				rs.getString("REPRESENTER_ID"), 
				rs.getGeneralDate("APPROVAL_DATE"));
	}

	
	
	@Override
	public void insert(AppRootConfirm appRootConfirm) {
		// 日次の場合
		if (appRootConfirm.getRootType().value == 1) {
			this.commandProxy().insert(WwfdtApvRootConfirmDaily.fromDomain(appRootConfirm));
			WwfdtApvPhaseConfirmDaily.fromDomain(appRootConfirm).forEach(p -> {
				this.commandProxy().insert(p);
			});
			WwfdtApvFrameConfirmDaily.fromDomain(appRootConfirm).forEach(f -> {
				this.commandProxy().insert(f);
			});
		}
		// 月次の場合
		else {
			this.commandProxy().insert(WwfdtApvRootConfirmMonthly.fromDomain(appRootConfirm));
			WwfdtApvPhaseConfirmMonthly.fromDomain(appRootConfirm).forEach(p -> {
				this.commandProxy().insert(p);
			});
			WwfdtApvFrameConfirmMonthly.fromDomain(appRootConfirm).forEach(f -> {
				this.commandProxy().insert(f);
			});
		}
	}

	@Override
	public void update(AppRootConfirm appRootConfirm) {
		// 日次の場合
		if (appRootConfirm.getRootType().value == 1) {
			this.commandProxy().update(WwfdtApvRootConfirmDaily.fromDomain(appRootConfirm));
			WwfdtApvPhaseConfirmDaily.fromDomain(appRootConfirm).forEach(p -> {
				this.commandProxy().update(p);
			});
			WwfdtApvFrameConfirmDaily.fromDomain(appRootConfirm).forEach(f -> {
				this.commandProxy().update(f);
			});
		}
		// 月次の場合
		else {
			this.commandProxy().update(WwfdtApvRootConfirmMonthly.fromDomain(appRootConfirm));
			WwfdtApvPhaseConfirmMonthly.fromDomain(appRootConfirm).forEach(p -> {
				this.commandProxy().update(p);
			});
			WwfdtApvFrameConfirmMonthly.fromDomain(appRootConfirm).forEach(f -> {
				this.commandProxy().update(f);
			});
		}
	}

	private static final List<String> DELETE_DAILY_TABLES = Arrays.asList(
			"delete from WWFDT_DAY_APV_RT_CONFIRM",
			"delete from WWFDT_DAY_APV_PH_CONFIRM",
			"delete from WWFDT_DAY_APV_FR_CONFIRM",
			"delete from WWFDT_DAY_APV_AP_CONFIRM"
	);
	private static final List<String> DELETE_MONTHLY_TABLES = Arrays.asList(
			"delete from WWFDT_MON_APV_RT_CONFIRM",
			"delete from WWFDT_MON_APV_PH_CONFIRM",
			"delete from WWFDT_MON_APV_FR_CONFIRM",
			"delete from WWFDT_MON_APV_AP_CONFIRM"
	);
	
	
	@Override
	public void delete(AppRootConfirm appRootConfirm) {
		List<String> targetTables = appRootConfirm.getRootType().value == 1
				?DELETE_DAILY_TABLES   //日次の場合
				:DELETE_MONTHLY_TABLES;//月次の場合
		delete(targetTables,appRootConfirm.getRootID());
	}

	private void delete(List<String> targetTables, String rootID) {
		targetTables.forEach(table ->{
			String sql = table + " where ROOT_ID = @rootId "; 
			jdbcProxy().query(sql).paramString("rootId", rootID);				
		});
	}

	@Override
	public void deleteAppRootConfirmDaily(String employeeID, GeneralDate date) {
		findAppRootConfirmDaily(employeeID, date).ifPresent(result ->{
			deleteAppRootConfirm(DELETE_DAILY_TABLES, result);
		});
	} 
	
	@Override
	public void deleteAppRootConfirmMonthly(String employeeID, ClosureMonth closureMonth) {
		findAppRootConfirmMonthly(employeeID, closureMonth).ifPresent(result ->{
			deleteAppRootConfirm(DELETE_MONTHLY_TABLES, result);
		});
	}

	private void deleteAppRootConfirm(List<String> targetTables, AppRootConfirm confirm) {
		targetTables.forEach(ts ->{
			String sql = ts + " where ROOT_ID = @rootID";
			jdbcProxy().query(sql).paramString("rootId", confirm.getRootID());
		});
	}

	
	
	@Override
	public void createNewStatus(String companyID, String employeeID, GeneralDate date, RecordRootType rootType) {
		
		String rootID = UUID.randomUUID().toString();
		AppRootConfirm appRootInstanceNew = new AppRootConfirm(rootID, companyID, employeeID, date, rootType,
				Collections.emptyList(), Optional.empty(), Optional.empty(), Optional.empty());
		this.insert(appRootInstanceNew);
	}

	
	@Override
	public Optional<AppRootConfirm> findAppRootConfirmDaily(String employeeID, GeneralDate date) {
		List<AppRootConfirm> listAppRootConfirm = internalQueryDaily(Arrays.asList(employeeID), new DatePeriod(date, date));
		if (CollectionUtil.isEmpty(listAppRootConfirm)) {
			return Optional.empty();
		} else {
			return Optional.of(listAppRootConfirm.get(0));
		}
	}

	@Override
	public List<AppRootConfirm> findAppRootConfirmDaily(String employeeID, DatePeriod period) {
		return internalQueryDaily(Arrays.asList(employeeID), period);
	}

	@Override
	public List<AppRootConfirm> findAppRootConfirmDaily(List<String> employeeIDLst,	GeneralDate date) {
		return internalQueryDaily(employeeIDLst, new DatePeriod(date, date));
	}

	@Override
	public List<AppRootConfirm> findAppRootConfirmDaily(List<String> employeeIDLst, DatePeriod period) {
		return internalQueryDaily(employeeIDLst, period);
	}

	private static final String FIND_DAY_CONFIRM 
			= " select rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.RECORD_DATE, "
					+ " ph.PHASE_ORDER, ph.APP_PHASE_ATR, "
					+ " fr.FRAME_ORDER, fr.APPROVER_ID, fr.REPRESENTER_ID, fr.APPROVAL_DATE "
			+ " from WWFDT_DAY_APV_RT_CONFIRM as rt" 
			+ " left join WWFDT_DAY_APV_PH_CONFIRM as ph"
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " left join WWFDT_DAY_APV_FR_CONFIRM as fr"
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER";

	private static final String FIND_DAY_CONFIRM_SQL 
			= " select rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.RECORD_DATE, "
					+ " ph.PHASE_ORDER, ph.APP_PHASE_ATR, "
					+ " fr.FRAME_ORDER, fr.APPROVER_ID, fr.REPRESENTER_ID, fr.APPROVAL_DATE "
			+ " from WWFDT_DAY_APV_RT_CONFIRM as rt" 
			+ " left join WWFDT_DAY_APV_PH_CONFIRM as ph"
			+ " with (index(WWFDI_DAY_APV_PH_CONFIRM)) " 
			+ " on rt.ROOT_ID = ph.ROOT_ID"
			+ " left join WWFDT_DAY_APV_FR_CONFIRM as fr" 
			+ " with (index(WWFDI_DAY_APV_FR_CONFIRM)) "
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER";

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private List<AppRootConfirm> internalQueryDaily(List<String> employeeIDLst, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_DAY_CONFIRM_SQL);
		} else {
			sql.append(FIND_DAY_CONFIRM);
		}
		sql.append(" where rt.EMPLOYEE_ID in @sids ");
		sql.append(" and rt.RECORD_DATE between @startDate and @endDate");
		
		return NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return toDomain(jdbcProxy().query(sql.toString())
					.paramString("sids", employeeIDs)
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.getList(rec -> createFullJoinAppRootConfirmDaily(rec)));
		});
	}
	
	
	@Override
	public Optional<AppRootConfirm> findAppRootConfirmMonthly(String employeeID, ClosureMonth closureMonth) {
		List<AppRootConfirm> listAppRootConfirm = internalQueryMonthly(Arrays.asList(employeeID), Arrays.asList(closureMonth));
		if (CollectionUtil.isEmpty(listAppRootConfirm)) {
			return Optional.empty();
		} else {
			return Optional.of(listAppRootConfirm.get(0));
		}
	}

	@Override
	public List<AppRootConfirm> findAppRootConfirmMonthly(String employeeID, List<ClosureMonth> closureMonthLst) {
		return internalQueryMonthly(Arrays.asList(employeeID), closureMonthLst);
	}

	@Override
	public List<AppRootConfirm> findAppRootConfirmMonthly(List<String> employeeIDLst, ClosureMonth closureMonth) {
		return internalQueryMonthly(employeeIDLst, Arrays.asList(closureMonth));
	}

	@Override
	public List<AppRootConfirm> findAppRootConfirmMonthly(List<String> employeeIDLst, List<ClosureMonth> closureMonthLst) {
		return internalQueryMonthly(employeeIDLst, closureMonthLst);
	}
	
	@Override
	public List<AppRootConfirm> findAppRootConfirmMonthly(String employeeID, YearMonth yearMonth) {
		return findAppRootConfirmMonthly(Arrays.asList(employeeID), yearMonth);
	}

	@Override
	public List<AppRootConfirm> findAppRootConfirmMonthly(List<String> employeeIDLst, YearMonth yearMonth) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_MON_CONFIRM_SQL);
		} else {
			sql.append(FIND_MON_CONFIRM);
		}
		sql.append(" and rt.EMPLOYEE_ID in @sids ");
		sql.append(" and rt.YEARMONTH in @yearmonth ");

		return NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return toDomain(jdbcProxy().query(sql.toString())
					.paramString("sids", employeeIDs)
					.paramString("yearmonth", yearMonth.toString())
					.getList(rec -> createFullJoinAppRootConfirmMonthly(rec)));
		});
	}


	private static final String FIND_MON_CONFIRM 
			= " select rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.YEARMONTH, rt.CLOSURE_ID, rt.CLOSURE_DAY, rt.LAST_DAY_FLG, "
					+ " ph.PHASE_ORDER, ph.APP_PHASE_ATR, "
					+ " fr.FRAME_ORDER, fr.APPROVER_ID, fr.REPRESENTER_ID, fr.APPROVAL_DATE "
			+ " from WWFDT_MON_APV_RT_CONFIRM as rt" 
			+ " left join WWFDT_MON_APV_PH_CONFIRM as ph"
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " left join WWFDT_MON_APV_FR_CONFIRM as fr"
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER";

	private static final String FIND_MON_CONFIRM_SQL 
			= " select rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.YEARMONTH, rt.CLOSURE_ID, rt.CLOSURE_DAY, rt.LAST_DAY_FLG, "
					+ " ph.PHASE_ORDER, ph.APP_PHASE_ATR, "
					+ " fr.FRAME_ORDER, fr.APPROVER_ID, fr.REPRESENTER_ID, fr.APPROVAL_DATE "
			+ " from WWFDT_MON_APV_RT_CONFIRM as rt" 
			+ " left join WWFDT_MON_APV_PH_CONFIRM as ph"
			+ " with (index(WWFDI_MON_APV_PH_CONFIRM)) " 
			+ " on rt.ROOT_ID = ph.ROOT_ID"
			+ " left join WWFDT_MON_APV_FR_CONFIRM as fr" 
			+ " with (index(WWFDI_MON_APV_FR_CONFIRM)) "
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER";

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private List<AppRootConfirm> internalQueryMonthly(List<String> employeeIDLst, List<ClosureMonth> closureMonths) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_MON_CONFIRM_SQL);
		} else {
			sql.append(FIND_MON_CONFIRM);
		}
		sql.append(" where rt.EMPLOYEE_ID in @sids ");
		sql.append(" and (");
		for (int i = 0; i < closureMonths.size(); i++) {
			if (i != 0)
				sql.append(" OR ");
			whereClosureMonth(i, sql);
		}
		sql.append(" )");

		return NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			
			NtsStatement stmt = jdbcProxy().query(sql.toString());
			stmt.paramString("sids", employeeIDs);
			
			for (int i = 0; i < closureMonths.size(); i++) {
				stmt.paramInt("yearMonth" + i, closureMonths.get(i).yearMonth().v());
				stmt.paramInt("closureId" + i, closureMonths.get(i).closureId());
				stmt.paramInt("closureDay" + i, closureMonths.get(i).closureDate().getClosureDay().v());
				stmt.paramInt("isLastDay" + i, closureMonths.get(i).closureDate().getLastDayOfMonth() ? 1 : 0);
			}
			
			return toDomain(jdbcProxy().query(sql.toString())
					.getList(rec -> createFullJoinAppRootConfirmMonthly(rec)));
		});
	}

	private static void whereClosureMonth(int index, StringBuilder sb) {
		sb.append("(")
			.append(" rt.YEARMONTH = @yearMonth").append(index)
			.append(" and rt.CLOSURE_ID = @closureId").append(index)
			.append(" and rt.CLOSURE_DAY = @closureDay").append(index)
			.append(" and rt.LAST_DAY_FLG = @isLastDay").append(index)
			.append(")");
	}


}
