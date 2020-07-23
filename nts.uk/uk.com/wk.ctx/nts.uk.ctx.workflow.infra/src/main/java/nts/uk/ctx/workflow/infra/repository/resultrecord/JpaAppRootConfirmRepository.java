package nts.uk.ctx.workflow.infra.repository.resultrecord;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.RequiredArgsConstructor;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.JdbcProxy;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
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
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
		//↓Phase以降の情報はNULLのときがある
		List<AppPhaseConfirm> phases = new ArrayList<AppPhaseConfirm>();
		if(first.getPhaseOrder() != null) {
			phases = fullJoinInRoot.stream().collect(Collectors.groupingBy(p -> p.getPhaseOrder()))
					.entrySet().stream()
					.map(p -> {
						Integer phaseOrder = p.getKey();
						List<FullJoinAppRootConfirm> fullJoinInPhase = p.getValue();
						return toDomainPhase(appId, phaseOrder, fullJoinInPhase);
					}).collect(Collectors.toList());
		}	
		
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
		List<AppFrameConfirm> frames = new ArrayList<>();
		
		if (first.getFrameOrder() != null) {
			frames = fullJoinInPhase.stream().collect(Collectors.groupingBy(f -> f.getFrameOrder()))
				.entrySet().stream()
				.map(f -> {
					Integer frameOrder = f.getKey();
					List<FullJoinAppRootConfirm> fullJoinInFrame = f.getValue();
					return toDomainFrame(appId, phaseOrder, frameOrder, fullJoinInFrame);
				}).collect(Collectors.toList());
		}
		
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
		delete(appRootConfirm);
		insert(appRootConfirm);
	}

	private static final List<String> DELETE_DAILY_TABLES = Arrays.asList(
			"delete from WWFDT_DAY_APV_RT_CONFIRM",
			"delete from WWFDT_DAY_APV_PH_CONFIRM",
			"delete from WWFDT_DAY_APV_FR_CONFIRM"
	);
	private static final List<String> DELETE_MONTHLY_TABLES = Arrays.asList(
			"delete from WWFDT_MON_APV_RT_CONFIRM",
			"delete from WWFDT_MON_APV_PH_CONFIRM",
			"delete from WWFDT_MON_APV_FR_CONFIRM"
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
            String sql = table + " where ROOT_ID = @rootID ";
            new NtsStatement(sql, this.jdbcProxy()).paramString("rootID", rootID).execute();           
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
        targetTables.forEach(table ->{
            String sql = table + " where ROOT_ID = @rootID ";
            new NtsStatement(sql, this.jdbcProxy()).paramString("rootID", confirm.getRootID()).execute();
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

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private List<AppRootConfirm> internalQueryDaily(List<String> employeeIDLst, DatePeriod period) {
		
		val query = new InternalQueryDaily(jdbcProxy(), employeeIDLst, period);
		List<FullJoinAppRootConfirm> fullJoins = query.execute();
		
		return toDomain(fullJoins);
		
	}
	
	@RequiredArgsConstructor
	public static class InternalQueryDaily {
		
		private final JdbcProxy proxy;
		private final List<String> employeeIds;
		private final DatePeriod period;
		
		public List<FullJoinAppRootConfirm> execute() {
			List<List<Object>> rootsList = roots();
			Map<String, List<List<Object>>> phasesMap = phases();
			Map<String, List<List<Object>>> framesMap = frames();
			return InternalQueryDaily.fullJoin(rootsList, phasesMap, framesMap);
		}
		
		private List<List<Object>> roots() {

			String tableName = "WWFDT_DAY_APV_RT_CONFIRM";
			val columns = Arrays.asList("ROOT_ID", "CID", "EMPLOYEE_ID", "RECORD_DATE");
			
			return fetch(tableName, columns);
		}
		
		private Map<String, List<List<Object>>> phases() {
			
			String tableName = "WWFDT_DAY_APV_PH_CONFIRM";
			val columns = Arrays.asList("ROOT_ID", "PHASE_ORDER", "APP_PHASE_ATR");
			
			return fetch(tableName, columns).stream()
					.collect(Collectors.groupingBy(e -> (String) e.get(0)));
		}

		private Map<String, List<List<Object>>> frames() {

			String tableName = "WWFDT_DAY_APV_FR_CONFIRM";
			val columns = Arrays.asList("ROOT_ID", "PHASE_ORDER", "FRAME_ORDER", "APPROVER_ID", "REPRESENTER_ID", "APPROVAL_DATE");

			return fetch(tableName, columns).stream()
					.collect(Collectors.groupingBy(e -> (String) e.get(0)));
		}
		
		private static String buildSql(String tableName, List<String> columns) {
			
			return "select " + columns.stream().collect(Collectors.joining(", "))
					+ " from " + tableName
					+ " where EMPLOYEE_ID in @sids"
					+ " and RECORD_DATE between @start and @end";
		}
		
		private NtsStatement setParams(NtsStatement statement, List<String> subEmployeeIds) {
			
			return statement.paramString("sids", subEmployeeIds)
				.paramDate("start", period.start())
				.paramDate("end", period.end());
		}

		private List<List<Object>> fetch(String tableName, List<String> columns) {
			
			String sql = buildSql(tableName, columns);
			
			return NtsStatement.In.split(employeeIds, subEmpIds -> {
				return setParams(proxy.query(sql), subEmpIds)
						.getList(rec -> rec.getObjects(columns));
			});
		}

		private static List<FullJoinAppRootConfirm> fullJoin(
				List<List<Object>> rootsList,
				Map<String, List<List<Object>>> phasesMap,
				Map<String, List<List<Object>>> framesMap) {
			
			List<FullJoinAppRootConfirm> fullJoins = new ArrayList<>();
			
			for (val root : rootsList) {
				
				String rootID = (String) root.get(0);
				String companyID = (String) root.get(1);
				String employeeID = (String) root.get(2);
				GeneralDate recordDate = getDate(root.get(3));
				
				val fjRoot = FullJoinAppRootConfirm.dailyRoot(rootID, companyID, employeeID, recordDate);
				
				val phases = phasesMap.get(rootID);
				if (phases == null) {
					fullJoins.add(fjRoot);
					continue;
				}
				
				for (val phase : phases) {
					
					// 0 は ROOT_ID
					Integer phaseOrder = getInt(phase.get(1));
					Integer appPhaseAtr = getInt(phase.get(2));
					val fjPhase = fjRoot.phase(phaseOrder, appPhaseAtr);
					
					val frames = framesMap.get(rootID);
					if (frames == null) {
						fullJoins.add(fjPhase);
						continue;
					}
					
					val framesOfPhase = frames.stream()
							.filter(f -> f.get(1) == phaseOrder)
							.collect(toList());
					
					if (framesOfPhase.isEmpty()) {
						fullJoins.add(fjPhase);
						continue;
					}
					
					for (val frame : framesOfPhase) {
						
						// 0 は ROOT_ID
						Integer frameOrder = getInt(frame.get(2));
						String approverID = (String) frame.get(3);
						String representerID = (String) frame.get(4);
						GeneralDate approvalDate = getDate(frame.get(5));
						val fjFrame = fjPhase.frame(frameOrder, approverID, representerID, approvalDate);
						
						fullJoins.add(fjFrame);
					}
				}
			}
			return fullJoins;
		}
		
		private static GeneralDate getDate(Object value) {
			return GeneralDate.legacyDate((Timestamp) value);
		}
		
		private static Integer getInt(Object value) {
			return value == null ? null : ((BigDecimal) value).intValue();
		}
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
		sql.append(FIND_MON_CONFIRM);
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


	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private List<AppRootConfirm> internalQueryMonthly(List<String> employeeIDLst, List<ClosureMonth> closureMonths) {
		StringBuilder sql = new StringBuilder();
		sql.append(FIND_MON_CONFIRM);
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
			stmt = stmt.paramString("sids", employeeIDs);
			
			for (int i = 0; i < closureMonths.size(); i++) {
				stmt = stmt	.paramInt("yearMonth" + i, closureMonths.get(i).yearMonth().v())
							.paramInt("closureId" + i, closureMonths.get(i).closureId())
							.paramInt("closureDay" + i, closureMonths.get(i).closureDate().getClosureDay().v())
							.paramInt("isLastDay" + i, closureMonths.get(i).closureDate().getLastDayOfMonth() ? 1 : 0);
			}
			return toDomain(stmt.getList(rec -> createFullJoinAppRootConfirmMonthly(rec)));
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
