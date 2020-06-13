package nts.uk.ctx.workflow.infra.repository.resultrecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.layer.infra.data.database.DatabaseProduct;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirmRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtAppApvApproverState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtAppApvFrameState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtAppApvPhaseState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.WwfdtAppApvRootState;
import nts.uk.ctx.workflow.infra.entity.resultrecord.FullJoinAppRootConfirm;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm.WwfdpApvFrameConfirmDailyPK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm.WwfdpApvPhaseConfirmDailyPK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm.WwfdtApvFrameConfirmDaily;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm.WwfdtApvPhaseConfirmDaily;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm.WwfdtApvRootConfirmDaily;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm.WwfdpApvFrameConfirmMonthlyPK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm.WwfdpApvPhaseConfirmMonthlyPK;
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
		return listFullJoin.stream().collect(Collectors.groupingBy(FullJoinAppRootConfirm::getRootID)).entrySet()
				.stream().map(x -> {
					String companyID = x.getValue().get(0).getCompanyID();
					String rootID = x.getValue().get(0).getRootID();
					GeneralDate recordDate = x.getValue().get(0).getRecordDate();
					RecordRootType rootType = EnumAdaptor.valueOf(x.getValue().get(0).getRootType(),
							RecordRootType.class);
					String employeeID = x.getValue().get(0).getEmployeeID();
					Integer yearMonth = x.getValue().get(0).getYearMonth();
					Integer closureID = x.getValue().get(0).getClosureID();
					Integer closureDay = x.getValue().get(0).getClosureDay();
					Integer lastDayFlg = x.getValue().get(0).getLastDayFlg();
					List<AppPhaseConfirm> listAppPhase = new ArrayList<>();
					Optional<FullJoinAppRootConfirm> isEmptyConfirm = x.getValue().stream()
							.filter(y1 -> y1.getPhaseOrder() == null).findAny();

					if (!isEmptyConfirm.isPresent()) {
						listAppPhase = x.getValue().stream()
								.collect(Collectors.groupingBy(FullJoinAppRootConfirm::getPhaseOrder)).entrySet()
								.stream().map(y -> {
									Integer phaseOrder = y.getValue().get(0).getPhaseOrder();
									ApprovalBehaviorAtr appPhaseAtr = EnumAdaptor
											.valueOf(y.getValue().get(0).getAppPhaseAtr(), ApprovalBehaviorAtr.class);
									List<AppFrameConfirm> listAppFrame = y.getValue().stream()
											.collect(Collectors.groupingBy(FullJoinAppRootConfirm::getFrameOrder))
											.entrySet().stream().map(z -> {
												Integer frameOrder = z.getValue().get(0).getFrameOrder();
												Optional<String> frameApproverID = Optional
														.ofNullable(z.getValue().get(0).getApproverID());
												Optional<String> representerID = Optional
														.ofNullable(z.getValue().get(0).getRepresenterID());
												GeneralDate approvalDate = z.getValue().get(0).getApprovalDate();
												return new AppFrameConfirm(frameOrder, frameApproverID, representerID,
														approvalDate);
											}).collect(Collectors.toList());
									return new AppPhaseConfirm(phaseOrder, appPhaseAtr, listAppFrame);
								}).collect(Collectors.toList());
					}
					return new AppRootConfirm(rootID, companyID, employeeID, recordDate, rootType, listAppPhase,
							yearMonth == null ? Optional.empty() : Optional.of(new YearMonth(yearMonth)),
							closureID == null ? Optional.empty() : Optional.of(closureID),
							closureDay == null ? Optional.empty()
									: Optional.of(new ClosureDate(closureDay, lastDayFlg == 1)));
				}).collect(Collectors.toList());
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

	@Override
	public void delete(AppRootConfirm appRootConfirm) {
		// 日次の場合
		if (appRootConfirm.getRootType().value == 1) {
			this.commandProxy().remove(WwfdtApvRootConfirmDaily.class, appRootConfirm.getRootID());
		}
		// 月次の場合
		else {
			this.commandProxy().remove(WwfdtApvRootConfirmMonthly.class, appRootConfirm.getRootID());
		}
	}

	private final String DELETE_DAILY
			= " delete "
			+ " from WWFDT_DAY_APV_RT_CONFIRM as rt" 
			+ " inner join WWFDT_DAY_APV_PH_CONFIRM as ph"
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " inner join WWFDT_DAY_APV_FR_CONFIRM as fr"
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER";

	private final String DELETE_DAILY_SQL
			= " delete "
			+ " from WWFDT_DAY_APV_RT_CONFIRM as rt" 
			+ " inner join WWFDT_DAY_APV_PH_CONFIRM as ph"
			+ " with (index(WWFDI_DAY_APV_PH_CONFIRM)) " 
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " inner join WWFDT_DAY_APV_FR_CONFIRM as fr"
			+ " with (index(WWFDI_DAY_APV_RT_CONFIRM)) " 
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER";
	

	@Override
	public void deleteAppRootConfirmDaily(String employeeID, GeneralDate date) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(DELETE_DAILY_SQL);
		} else {
			sql.append(DELETE_DAILY);
		}
		sql.append(" where rt.EMPLOYEE_ID in @sid ");
		sql.append(" and rt.RECORD_DATE = @date ");

		jdbcProxy().query(sql.toString())
				.paramString("sid", employeeID)
				.paramDate("date", date);
	} 
	
	
	private final String DELETE_MONTHLY
	= " delete "
	+ " from WWFDT_APP_MON_RT_CONFIRM as rt" 
	+ " inner join WWFDT_APP_MON_PH_CONFIRM as ph"
	+ " on rt.ROOT_ID = ph.ROOT_ID" 
	+ " inner join WWFDT_APP_MON_FR_CONFIRM as fr"
	+ " on ph.ROOT_ID = fr.ROOT_ID" 
	+ " and ph.PHASE_ORDER = fr.PHASE_ORDER";

	private final String DELETE_MONTHLY_SQL
	= " delete "
	+ " from WWFDT_MON_APV_RT_CONFIRM as rt" 
	+ " inner join WWFDT_MON_APV_PH_CONFIRM as ph"
	+ " with (index(WWFDI_MON_APV_PH_CONFIRM)) " 
	+ " on rt.ROOT_ID = ph.ROOT_ID" 
	+ " inner join WWFDT_MON_APV_FR_CONFIRM as fr"
	+ " with (index(WWFDI_MON_APV_RT_CONFIRM)) " 
	+ " on ph.ROOT_ID = fr.ROOT_ID" 
	+ " and ph.PHASE_ORDER = fr.PHASE_ORDER";

	@Override
	public void deleteAppRootConfirmMonthly(String employeeID, ClosureMonth closureMonth) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(DELETE_MONTHLY_SQL);
		} else {
			sql.append(DELETE_MONTHLY);
		}
		sql.append(" where rt.EMPLOYEE_ID in @sid ");
		sql.append(" and rt.YEARMONTH = @yearMonth ");
		sql.append(" and rt.CLOSURE_ID = @closureId ");
		sql.append(" and rt.CLOSURE_DAY = @closureDay ");
		sql.append(" and rt.IS_LAST_DAY = @isLastDay ");

		jdbcProxy().query(sql.toString())
				.paramString("sid", employeeID)
				.paramInt("yearMonth", closureMonth.yearMonth().v())
				.paramInt("closureId", closureMonth.closureId())
				.paramInt("closureDay", closureMonth.closureDate().getClosureDay().v())
				.paramInt("isLastDay", closureMonth.closureDate().getLastDayOfMonth() ? 1 : 0);
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
		sql.append("and rt.EMPLOYEE_ID IN ( ");
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
			+ " from WWFDT_APP_MON_RT_CONFIRM as rt" 
			+ " left join WWFDT_APP_MON_PH_CONFIRM as ph"
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " left join WWFDT_APP_MON_FR_CONFIRM as fr"
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER";

	private static final String FIND_MON_CONFIRM_SQL 
			= " select rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.YEARMONTH, rt.CLOSURE_ID, rt.CLOSURE_DAY, rt.LAST_DAY_FLG, "
					+ " ph.PHASE_ORDER, ph.APP_PHASE_ATR, "
					+ " fr.FRAME_ORDER, fr.APPROVER_ID, fr.REPRESENTER_ID, fr.APPROVAL_DATE "
			+ " from WWFDT_APP_MON_RT_CONFIRM as rt" 
			+ " left join WWFDT_APP_MON_PH_CONFIRM as ph"
			+ " with (index(WWFDI_APP_MON_PH_CONFIRM)) " 
			+ " on rt.ROOT_ID = ph.ROOT_ID"
			+ " left join WWFDT_APP_MON_FR_CONFIRM as fr" 
			+ " with (index(WWFDI_APP_MON_FR_CONFIRM)) "
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
