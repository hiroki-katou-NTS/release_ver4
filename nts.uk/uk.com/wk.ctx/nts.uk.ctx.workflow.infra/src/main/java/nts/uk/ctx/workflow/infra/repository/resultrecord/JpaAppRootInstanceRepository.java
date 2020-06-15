package nts.uk.ctx.workflow.infra.repository.resultrecord;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import lombok.SneakyThrows;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.database.DatabaseProduct;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstanceRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.infra.entity.resultrecord.FullJoinAppRootInstance;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance.WwfdtApvRootInstanceDaily;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance.WwfdtApvPhaseInstanceDaily;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance.WwfdtApvFrameInstanceDaily;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance.WwfdtApvApproveInstanceDaily;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance.WwfdpApvPhaseInstanceDailyPK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance.WwfdpApvFrameInstanceDailyPK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance.WwfdpApvApproveInstanceDailyPK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance.WwfdtApvRootInstanceMonthly;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance.WwfdtApvPhaseInstanceMonthly;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance.WwfdtApvFrameInstanceMonthly;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance.WwfdtApvApproveInstanceMonthly;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance.WwfdpApvPhaseInstanceMonthlyPK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance.WwfdpApvFrameInstanceMonthlyPK;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance.WwfdpApvApproveInstanceMonthlyPK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaAppRootInstanceRepository extends JpaRepository implements AppRootInstanceRepository {
		
	private WwfdtApvRootInstanceDaily fromDomainDaily(AppRootInstance appRootInstance){
		return WwfdtApvRootInstanceDaily.fromDomain(appRootInstance);
	}
	
	private WwfdtApvRootInstanceMonthly fromDomainMonthly(AppRootInstance appRootInstance){
		return WwfdtApvRootInstanceMonthly.fromDomain(appRootInstance);
	}
	
	private List<AppRootInstance> toDomain(List<FullJoinAppRootInstance> listFullJoin){
		return listFullJoin.stream().collect(Collectors.groupingBy(FullJoinAppRootInstance::getRootID))
						.entrySet().stream().map(x -> {
					FullJoinAppRootInstance first = x.getValue().get(0);
					String companyID =  first.getCompanyID();
					String rootID = first.getRootID();
					GeneralDate startDate = first.getStartDate();
					GeneralDate endDate = first.getEndDate();
					RecordRootType rootType = EnumAdaptor.valueOf(first.getRootType(), RecordRootType.class);
					String employeeID = first.getEmployeeID();
					List<AppPhaseInstance> listAppPhase = x.getValue().stream()
							.collect(Collectors.groupingBy(FullJoinAppRootInstance::getPhaseOrder))
							.entrySet().stream().map(y -> {
						Integer phaseOrder  = y.getValue().get(0).getPhaseOrder();
						ApprovalForm approvalForm =  EnumAdaptor.valueOf(y.getValue().get(0).getApprovalForm(), ApprovalForm.class);
						List<AppFrameInstance> listAppFrame = y.getValue().stream()
								.collect(Collectors.groupingBy(FullJoinAppRootInstance::getFrameOrder))
								.entrySet().stream().map(z -> { 
							Integer frameOrder = z.getValue().get(0).getFrameOrder();
							Boolean confirmAtr = z.getValue().get(0).getConfirmAtr()==1?true:false;
							List<String> approvalIDLst = z.getValue().stream().map(t -> t.getApproverChildID()).collect(Collectors.toList());
							return new AppFrameInstance(frameOrder, confirmAtr, approvalIDLst);
						}).collect(Collectors.toList());
						return new AppPhaseInstance(phaseOrder, approvalForm, listAppFrame);
					}).collect(Collectors.toList());
					return new AppRootInstance(rootID, companyID, employeeID, new DatePeriod(startDate, endDate), rootType, listAppPhase);
				}).collect(Collectors.toList());
	}
	
	@SneakyThrows
	private FullJoinAppRootInstance createFullJoinAppRootInstanceDaily(NtsResultRecord rs){
				return new FullJoinAppRootInstance(
						rs.getString("ROOT_ID"), 
						rs.getString("CID"), 
						rs.getString("EMPLOYEE_ID"), 
						GeneralDate.fromString(rs.getString("START_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						GeneralDate.fromString(rs.getString("END_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						1, 
						Integer.valueOf(rs.getString("PHASE_ORDER")), 
						Integer.valueOf(rs.getString("APPROVAL_FORM")), 
						Integer.valueOf(rs.getString("FRAME_ORDER")), 
						Integer.valueOf(rs.getString("CONFIRM_ATR")), 
						rs.getString("APPROVER_CHILD_ID"));
	}
	
	@SneakyThrows
	private FullJoinAppRootInstance createFullJoinAppRootInstanceMonthly(NtsResultRecord rs){
				return new FullJoinAppRootInstance(
						rs.getString("ROOT_ID"), 
						rs.getString("CID"), 
						rs.getString("EMPLOYEE_ID"), 
						GeneralDate.fromString(rs.getString("START_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						GeneralDate.fromString(rs.getString("END_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						2, 
						Integer.valueOf(rs.getString("PHASE_ORDER")), 
						Integer.valueOf(rs.getString("APPROVAL_FORM")), 
						Integer.valueOf(rs.getString("FRAME_ORDER")), 
						Integer.valueOf(rs.getString("CONFIRM_ATR")), 
						rs.getString("APPROVER_CHILD_ID"));
	}

	
	@SneakyThrows
	private List<FullJoinAppRootInstance> createFullJoinAppRootInstance(ResultSet rs){
		List<FullJoinAppRootInstance> listFullData = new ArrayList<>();
		while (rs.next()) {
			listFullData.add(new FullJoinAppRootInstance(
						rs.getString("ROOT_ID"), 
						rs.getString("CID"), 
						rs.getString("EMPLOYEE_ID"), 
						GeneralDate.fromString(rs.getString("START_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						GeneralDate.fromString(rs.getString("END_DATE"), "yyyy-MM-dd HH:mm:ss"), 
						Integer.valueOf(rs.getString("ROOT_TYPE")), 
						Integer.valueOf(rs.getString("PHASE_ORDER")), 
						Integer.valueOf(rs.getString("APPROVAL_FORM")), 
						Integer.valueOf(rs.getString("FRAME_ORDER")), 
						Integer.valueOf(rs.getString("CONFIRM_ATR")), 
						rs.getString("APPROVER_CHILD_ID")));
		}
		return listFullData;
	}

	@SneakyThrows
	private List<FullJoinAppRootInstance> createFullJoinAppRootInstanceResponse(List<Object[]> rs) {
		List<FullJoinAppRootInstance> listFullData = new ArrayList<>();
		listFullData.addAll(rs.stream().parallel().map(mapper ->
			new FullJoinAppRootInstance(String.valueOf(mapper[0]), String.valueOf(mapper[1]),
					String.valueOf(mapper[2]),
					GeneralDate.localDate(((Timestamp)mapper[3]).toLocalDateTime().toLocalDate()),
					GeneralDate.localDate(((Timestamp)mapper[4]).toLocalDateTime().toLocalDate()),
					Integer.valueOf(String.valueOf(mapper[5])), Integer.valueOf(String.valueOf(mapper[6])),
					Integer.valueOf(String.valueOf(mapper[7])), Integer.valueOf(String.valueOf(mapper[8])),
					Integer.valueOf(String.valueOf(mapper[9])), String.valueOf(mapper[10]))).collect(Collectors.toList()));
		return listFullData;
	}

	
	
	@Override
	public void insert(AppRootInstance appRootInstance) {
		// 日次の場合
		if (appRootInstance.getRootType().value == 1) {
			this.commandProxy().insert(fromDomainDaily(appRootInstance));
			this.getEntityManager().flush();
		}
		// 月次の場合
		else {
			this.commandProxy().insert(fromDomainMonthly(appRootInstance));
			this.getEntityManager().flush();
		}
	}

	@Override
	public void update(AppRootInstance appRootInstance) {
		// 日次の場合
		if (appRootInstance.getRootType().value == 1) {
			this.commandProxy().update(fromDomainDaily(appRootInstance));
			this.getEntityManager().flush();
		}
		// 月次の場合
		else {
			this.commandProxy().update(fromDomainMonthly(appRootInstance));
			this.getEntityManager().flush();
		}
	}

	@Override
	public void delete(AppRootInstance appRootInstance) {
		// 日次の場合
		if (appRootInstance.getRootType().value == 1) {
			this.commandProxy().remove(WwfdtApvRootInstanceDaily.class, appRootInstance.getRootID());
			this.getEntityManager().flush();
		}
		// 月次の場合
		else {
			this.commandProxy().remove(WwfdtApvRootInstanceMonthly.class, appRootInstance.getRootID());
			this.getEntityManager().flush();
		}
	}
	
	
	
	private final String DELETE_DAILY
			= " delete "
			+ " from WWFDT_DAY_APV_RT_INSTANCE as rt" 
			+ " inner join WWFDT_DAY_APV_PH_INSTANCE as ph"
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " inner join WWFDT_DAY_APV_FR_INSTANCE as fr"
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER"
			+ " inner join WWFDT_DAY_APV_AP_INSTANCE as ap"
			+ " on fr.ROOT_ID = ap.ROOT_ID" 
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER" 
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER";

	private final String DELETE_DAILY_SQL
			= " delete "
			+ " from WWFDT_DAY_APV_RT_INSTANCE as rt" 
			+ " inner join WWFDT_DAY_APV_PH_INSTANCE as ph"
			+ " with (index(WWFDI_DAY_APV_PH_INSTANCE)) " 
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " inner join WWFDT_DAY_APV_FR_INSTANCE as fr"
			+ " with (index(WWFDI_DAY_APV_RT_INSTANCE)) " 
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER"
			+ " inner join WWFDT_DAY_APV_AP_INSTANCE as ap"
			+ " with (index(WWFDI_DAY_APV_AP_INSTANCE)) " 
			+ " on fr.ROOT_ID = ap.ROOT_ID" 
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER" 
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER";

	@Override
	public void deleteDailyFromDate(String employeeID, GeneralDate date) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(DELETE_DAILY_SQL);
		} else {
			sql.append(DELETE_DAILY);
		}
		sql.append(" where rt.EMPLOYEE_ID = @sid ");
		sql.append(" and rt.START_DATE >= @date ");

		jdbcProxy().query(sql.toString())
					.paramString("sid", employeeID)
					.paramDate("date", date);
	} 

	
	private final String DELETE_MONTHLY
			= " delete "
			+ " from WWFDT_MON_APV_RT_INSTANCE as rt" 
			+ " inner join WWFDT_MON_APV_PH_INSTANCE as ph"
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " inner join WWFDT_MON_APV_FR_INSTANCE as fr"
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER"
			+ " inner join WWFDT_MON_APV_AP_INSTANCE as ap"
			+ " on fr.ROOT_ID = ap.ROOT_ID" 
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER" 
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER";

	private final String DELETE_MONTHLY_SQL
			= " delete "
			+ " from WWFDT_MON_APV_RT_INSTANCE as rt" 
			+ " inner join WWFDT_MON_APV_PH_INSTANCE as ph"
			+ " with (index(WWFDI_MON_APV_PH_INSTANCE)) " 
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " inner join WWFDT_MON_APV_FR_INSTANCE as fr"
			+ " with (index(WWFDI_MON_APV_RT_INSTANCE)) " 
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER"
			+ " inner join WWFDT_MON_APV_AP_INSTANCE as ap"
			+ " with (index(WWFDI_MON_APV_AP_INSTANCE)) " 
			+ " on fr.ROOT_ID = ap.ROOT_ID" 
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER" 
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER";

	@Override
	public void deleteMonthlyFromDate(String employeeID, GeneralDate date) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(DELETE_MONTHLY_SQL);
		} else {
			sql.append(DELETE_MONTHLY);
		}
		sql.append(" where rt.EMPLOYEE_ID = @sid ");
		sql.append(" and rt.START_DATE >= @date ");

		jdbcProxy().query(sql.toString())
					.paramString("sid", employeeID)
					.paramDate("date", date);		
	}

	
	
	private static final String FIND_DAY_INSTANCE 
			= " select rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.START_DATE, rt.END_DATE, "
					+ " ph.PHASE_ORDER, ph.APPROVAL_FORM, "
					+ " fr.FRAME_ORDER, fr.CONFIRM_ATR, "
					+ " ap.APPROVER_CHILD_ID "
			+ " from WWFDT_DAY_APV_RT_INSTANCE as rt" 
			+ " left join WWFDT_DAY_APV_PH_INSTANCE as ph"
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " left join WWFDT_DAY_APV_FR_INSTANCE as fr"
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER"
			+ " left join WWFDT_DAY_APV_AP_INSTANCE as ap"
			+ " on fr.ROOT_ID = ap.ROOT_ID" 
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER" 
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER";

	private static final String FIND_DAY_INSTANCE_SQL 
			= " select rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.START_DATE, rt.END_DATE, "
					+ " ph.PHASE_ORDER, ph.APPROVAL_FORM, "
					+ " fr.FRAME_ORDER, fr.CONFIRM_ATR, "
					+ " ap.APPROVER_CHILD_ID "
			+ " from WWFDT_DAY_APV_RT_INSTANCE as rt" 
			+ " left join WWFDT_DAY_APV_PH_INSTANCE as ph"
			+ " with (index(WWFDI_DAY_APV_PH_INSTANCE)) " 
			+ " on rt.ROOT_ID = ph.ROOT_ID"
			+ " left join WWFDT_DAY_APV_FR_INSTANCE as fr" 
			+ " with (index(WWFDI_DAY_APV_FR_INSTANCE)) "
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER"
			+ " left join WWFDT_DAY_APV_AP_INSTANCE as ap"
			+ " with (index(WWFDI_DAY_APV_AP_INSTANCE)) "
			+ " on fr.ROOT_ID = ap.ROOT_ID" 
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER" 
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER";

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceDailyByTarget(List<String> employeeIDLst, GeneralDate date) {
		return findAppRootInstanceDailyByTarget(employeeIDLst, new DatePeriod(date, date));
	}

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceDailyByTarget(List<String> employeeIDLst, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_DAY_INSTANCE_SQL);
		} else {
			sql.append(FIND_DAY_INSTANCE);
		}
		sql.append(" where rt.EMPLOYEE_ID in @sids ");
		sql.append(" and rt.START_DATE <= @endDate ");
		sql.append(" and rt.END_DATE >= @startDate ");

		return NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return toDomain(jdbcProxy().query(sql.toString())
					.paramString("sids", employeeIDs)
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.getList(rec -> createFullJoinAppRootInstanceDaily(rec)));
		});
	}
	
	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceDailyByApprover(List<String> approverIDLst, GeneralDate date) {
		return findAppRootInstanceDailyByApprover(approverIDLst, new DatePeriod(date, date));
	}

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceDailyByApprover(List<String> approverIDLst, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_DAY_INSTANCE_SQL);
		} else {
			sql.append(FIND_DAY_INSTANCE);
		}
		sql.append(" where ap.APPROVER_CHILD_ID in @sids ");
		sql.append(" and rt.START_DATE <= @endDate ");
		sql.append(" and rt.END_DATE >= @startDate ");

		return NtsStatement.In.split(approverIDLst, approverIDs -> {
			return toDomain(jdbcProxy().query(sql.toString())
					.paramString("sids", approverIDs)
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.getList(rec -> createFullJoinAppRootInstanceDaily(rec)));
		});
	}

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceDailyByApproverTarget(String approverID, List<String> employeeIDLst, GeneralDate date) {
		return findAppRootInstanceDailyByApproverTarget(approverID, employeeIDLst, new DatePeriod(date, date));
	}

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceDailyByApproverTarget(String approverID, List<String> employeeIDLst, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_DAY_INSTANCE_SQL);
		} else {
			sql.append(FIND_DAY_INSTANCE);
		}
		sql.append(" where rt.EMPLOYEE_ID in @sids ");
		sql.append(" and rt.START_DATE <= @endDate ");
		sql.append(" and rt.END_DATE >= @startDate ");
		sql.append(" and ap.APPROVER_CHILD_ID = @sid ");

		return NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return toDomain(jdbcProxy().query(sql.toString())
					.paramString("sids", employeeIDs)
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.paramString("sid", approverID)
					.getList(rec -> createFullJoinAppRootInstanceDaily(rec)));
		});
	}

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceDailyNewestBelow(String employeeID, GeneralDate date) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select top 1 rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.START_DATE, rt.END_DATE, ph.PHASE_ORDER, ph.APPROVAL_FORM, fr.FRAME_ORDER, fr.CONFIRM_ATR, ap.APPROVER_CHILD_ID ");
		sql.append(" from WWFDT_DAY_APV_RT_INSTANCE as rt" );
		sql.append(" left join WWFDT_DAY_APV_PH_INSTANCE as ph");
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(" with (index(WWFDI_DAY_APV_PH_INSTANCE)) " );
		}
		sql.append(" on rt.ROOT_ID = ph.ROOT_ID");
		sql.append(" left join WWFDT_DAY_APV_FR_INSTANCE as fr" );
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(" with (index(WWFDI_DAY_APV_FR_INSTANCE)) ");
		}
		sql.append(" on ph.ROOT_ID = fr.ROOT_ID" );
		sql.append(" and ph.PHASE_ORDER = fr.PHASE_ORDER");
		sql.append(" left join WWFDT_DAY_APV_AP_INSTANCE as ap");
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(" with (index(WWFDI_DAY_APV_AP_INSTANCE)) ");
		}
		sql.append(" on fr.ROOT_ID = ap.ROOT_ID" );
		sql.append(" and fr.PHASE_ORDER = ap.PHASE_ORDER" );
		sql.append(" and fr.FRAME_ORDER = ap.FRAME_ORDER");

		sql.append(" where rt.EMPLOYEE_ID = @sid ");
		sql.append(" and rt.START_DATE <= @date ");

		return toDomain(jdbcProxy().query(sql.toString())
				.paramString("sid", employeeID)
				.paramDate("date", date)
				.getList(rec -> createFullJoinAppRootInstanceDaily(rec)));
	}


	
	private static final String FIND_MON_INSTANCE 
			= " select rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.START_DATE, rt.END_DATE, "
					+ " ph.PHASE_ORDER, ph.APPROVAL_FORM, "
					+ " fr.FRAME_ORDER, fr.CONFIRM_ATR, "
					+ " ap.APPROVER_CHILD_ID "
			+ " from WWFDT_MON_APV_RT_INSTANCE as rt" 
			+ " left join WWFDT_MON_APV_PH_INSTANCE as ph"
			+ " on rt.ROOT_ID = ph.ROOT_ID" 
			+ " left join WWFDT_MON_APV_FR_INSTANCE as fr"
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER"
			+ " left join WWFDT_MON_APV_AP_INSTANCE as ap"
			+ " on fr.ROOT_ID = ap.ROOT_ID" 
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER" 
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER";

	private static final String FIND_MON_INSTANCE_SQL 
			= " select rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.START_DATE, rt.END_DATE, "
					+ " ph.PHASE_ORDER, ph.APPROVAL_FORM, "
					+ " fr.FRAME_ORDER, fr.CONFIRM_ATR, "
			+ " ap.APPROVER_CHILD_ID "
			+ " from WWFDT_MON_APV_RT_INSTANCE as rt" 
			+ " left join WWFDT_MON_APV_PH_INSTANCE as ph"
			+ " with (index(WWFDI_MON_APV_PH_INSTANCE)) " 
			+ " on rt.ROOT_ID = ph.ROOT_ID"
			+ " left join WWFDT_MON_APV_FR_INSTANCE as fr" 
			+ " with (index(WWFDI_MON_APV_FR_INSTANCE)) "
			+ " on ph.ROOT_ID = fr.ROOT_ID" 
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER"
			+ " left join WWFDT_MON_APV_AP_INSTANCE as ap"
			+ " with (index(WWFDI_MON_APV_AP_INSTANCE)) "
			+ " on fr.ROOT_ID = ap.ROOT_ID" 
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER" 
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER";

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceMonthlyByTarget(List<String> employeeIDLst, GeneralDate date) {
		return findAppRootInstanceMonthlyByTarget(employeeIDLst, new DatePeriod(date, date));
	}
	
	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceMonthlyByTarget(List<String> employeeIDLst, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_MON_INSTANCE_SQL);
		} else {
			sql.append(FIND_MON_INSTANCE);
		}
		sql.append(" where rt.EMPLOYEE_ID in @sids ");
		sql.append(" and rt.START_DATE <= @endDate ");
		sql.append(" and rt.END_DATE >= @startDate ");

		return NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return toDomain(jdbcProxy().query(sql.toString())
					.paramString("sids", employeeIDs)
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.getList(rec -> createFullJoinAppRootInstanceMonthly(rec)));
		});
	}

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceMonthlyByApprover(List<String> approverIDLst, GeneralDate date) {
		return findAppRootInstanceMonthlyByApprover(approverIDLst, new DatePeriod(date, date));
	}

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceMonthlyByApprover(List<String> approverIDLst, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_MON_INSTANCE_SQL);
		} else {
			sql.append(FIND_MON_INSTANCE);
		}
		sql.append(" where rt.START_DATE <= @endDate ");
		sql.append(" and rt.END_DATE >= @startDate ");
		sql.append(" and ap.APPROVER_CHILD_ID in @sids ");

		return NtsStatement.In.split(approverIDLst, approverIDs -> {
			return toDomain(jdbcProxy().query(sql.toString())
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.paramString("sids", approverIDs)
					.getList(rec -> createFullJoinAppRootInstanceMonthly(rec)));
		});

	}

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceMonthlyByApproverTarget(String approverID, List<String> employeeIDLst, GeneralDate date) {
		return findAppRootInstanceMonthlyByApproverTarget(approverID, employeeIDLst, new DatePeriod(date, date));
	}

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceMonthlyByApproverTarget(String approverID, List<String> employeeIDLst, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(FIND_MON_INSTANCE_SQL);
		} else {
			sql.append(FIND_MON_INSTANCE);
		}
		sql.append(" where rt.EMPLOYEE_ID in @sids ");
		sql.append(" and rt.START_DATE <= @endDate ");
		sql.append(" and rt.END_DATE >= @startDate ");
		sql.append(" and ap.APPROVER_CHILD_ID = @sid ");

		return NtsStatement.In.split(employeeIDLst, employeeIDs -> {
			return toDomain(jdbcProxy().query(sql.toString())
					.paramString("sids", employeeIDs)
					.paramDate("startDate", period.start())
					.paramDate("endDate", period.end())
					.paramString("sid", approverID)
					.getList(rec -> createFullJoinAppRootInstanceMonthly(rec)));
		});
	}

	@Override
	@SneakyThrows
	public List<AppRootInstance> findAppRootInstanceMonthlyNewestBelow(String employeeID, GeneralDate date) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select top 1 rt.ROOT_ID, rt.CID, rt.EMPLOYEE_ID, rt.START_DATE, rt.END_DATE, ph.PHASE_ORDER, ph.APPROVAL_FORM, fr.FRAME_ORDER, fr.CONFIRM_ATR, ap.APPROVER_CHILD_ID ");
		sql.append(" from WWFDT_MON_APV_RT_INSTANCE as rt" );
		sql.append(" left join WWFDT_MON_APV_PH_INSTANCE as ph");
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(" with (index(WWFDI_MON_APV_PH_INSTANCE)) " );
		}
		sql.append(" on rt.ROOT_ID = ph.ROOT_ID");
		sql.append(" left join WWFDT_MON_APV_FR_INSTANCE as fr" );
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(" with (index(WWFDI_MON_APV_FR_INSTANCE)) ");
		}
		sql.append(" on ph.ROOT_ID = fr.ROOT_ID" );
		sql.append(" and ph.PHASE_ORDER = fr.PHASE_ORDER");
		sql.append(" left join WWFDT_MON_APV_AP_INSTANCE as ap");
		if (this.database().is(DatabaseProduct.MSSQLSERVER)) {
			// SQLServerの場合の処理
			sql.append(" with (index(WWFDI_MON_APV_AP_INSTANCE)) ");
		}
		sql.append(" on fr.ROOT_ID = ap.ROOT_ID" );
		sql.append(" and fr.PHASE_ORDER = ap.PHASE_ORDER" );
		sql.append(" and fr.FRAME_ORDER = ap.FRAME_ORDER");

		sql.append(" where rt.EMPLOYEE_ID = @sid ");
		sql.append(" and rt.START_DATE <= @date ");

		return toDomain(jdbcProxy().query(sql.toString())
				.paramString("sid", employeeID)
				.paramDate("date", date)
				.getList(rec -> createFullJoinAppRootInstanceMonthly(rec)));
	}

	
	
	private final String DAILY_APPROVAL_TARGET 
			= " select rt.EMPLOYEE_ID "
			+ " from WWFDT_DAY_APV_RT_INSTANCE rt "
			+ " left join WWFDT_DAY_APV_PH_INSTANCE ph "
			+ " on rt.ROOT_ID = ph.ROOT_ID "
			+ " left join WWFDT_DAY_APV_FR_INSTANCE fr "
			+ " on ph.ROOT_ID = fr.ROOT_ID "
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER "
			+ " left join WWFDT_DAY_APV_AP_INSTANCE ap "
			+ " on fr.ROOT_ID = ap.ROOT_ID "
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER "
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER ";
	
	private final String MONTHLY_APPROVAL_TARGET 
			= " select rt.EMPLOYEE_ID "
			+ " from WWFDT_MON_APV_RT_INSTANCE rt "
			+ " left join WWFDT_MON_APV_PH_INSTANCE ph "
			+ " on rt.ROOT_ID = ph.ROOT_ID "
			+ " left join WWFDT_MON_APV_FR_INSTANCE fr "
			+ " on ph.ROOT_ID = fr.ROOT_ID "
			+ " and ph.PHASE_ORDER = fr.PHASE_ORDER "
			+ " left join WWFDT_MON_APV_AP_INSTANCE ap "
			+ " on fr.ROOT_ID = ap.ROOT_ID "
			+ " and fr.PHASE_ORDER = ap.PHASE_ORDER "
			+ " and fr.FRAME_ORDER = ap.FRAME_ORDER ";

	private final String FIND_OFFICIAL_TARGET 
			= " where ap.APPROVER_CHILD_ID = 'approverID'"
			+ " and rt.END_DATE >= @startDate"
			+ " and rt.START_DATE <= @endDate";
	
	private final String FIND_INSTEAD_TARGET 
			= " where ap.APPROVER_CHILD_ID IN"
			+ " (SELECT c.SID FROM CMMMT_AGENT c "
			+ " where c.AGENT_SID1 = @approverID "
			+ " and c.START_DATE <= @sysDate "
			+ " and c.END_DATE >= @sysDate)"
			+ " and rt.END_DATE >= @startDate"
			+ " and rt.START_DATE <= @endDate";
	
	@Override
	public List<String> findDailyApprovalTarget(String approverID, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		sql.append(DAILY_APPROVAL_TARGET);
		sql.append(FIND_OFFICIAL_TARGET);
		sql.append(" union ");
		sql.append(DAILY_APPROVAL_TARGET);
		sql.append(FIND_INSTEAD_TARGET);
		
		return new NtsStatement(sql.toString(), this.jdbcProxy())
				.paramString("approverID", approverID)
				.paramString("startDate", period.start().toString("yyyy-MM-dd"))
				.paramString("endDate", period.end().toString("yyyy-MM-dd"))
				.paramString("sysDate", GeneralDate.today().toString("yyyy-MM-dd"))
				.getList(rec -> rec.getString("EMPLOYEE_ID")).stream().distinct().collect(Collectors.toList());
	}

	@Override
	public List<String> findMonthlyApprovalTarget(String approverID, DatePeriod period) {
		StringBuilder sql = new StringBuilder();
		sql.append(MONTHLY_APPROVAL_TARGET);
		sql.append(FIND_OFFICIAL_TARGET);
		sql.append(" union ");
		sql.append(MONTHLY_APPROVAL_TARGET);
		sql.append(FIND_INSTEAD_TARGET);
		
		return new NtsStatement(sql.toString(), this.jdbcProxy())
				.paramString("approverID", approverID)
				.paramString("startDate", period.start().toString("yyyy-MM-dd"))
				.paramString("endDate", period.end().toString("yyyy-MM-dd"))
				.paramString("sysDate", GeneralDate.today().toString("yyyy-MM-dd"))
				.getList(rec -> rec.getString("EMPLOYEE_ID")).stream().distinct().collect(Collectors.toList());
	}


}
