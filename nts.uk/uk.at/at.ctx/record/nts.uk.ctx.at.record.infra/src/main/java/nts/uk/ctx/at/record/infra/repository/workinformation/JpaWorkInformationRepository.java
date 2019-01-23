package nts.uk.ctx.at.record.infra.repository.workinformation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.enums.NotUseAttribute;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtDaiPerWorkInfo;
import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtDaiPerWorkInfoPK;
import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtWorkScheduleTime;
import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtWorkScheduleTimePK;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author lamdt
 *
 */
@Stateless
public class JpaWorkInformationRepository extends JpaRepository implements WorkInformationRepository {

	private static final String FIND_BY_PERIOD_ORDER_BY_YMD_AND_EMPS;

	private static final String FIND_BY_EMPLOYEE_ID = "SELECT a FROM KrcdtDaiPerWorkInfo a "
			+ " WHERE a.krcdtDaiPerWorkInfoPK.employeeId = :employeeId ";
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiPerWorkInfo a ");
		builderString.append("WHERE a.krcdtDaiPerWorkInfoPK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDaiPerWorkInfoPK.ymd >= :startDate ");
		builderString.append("AND a.krcdtDaiPerWorkInfoPK.ymd <= :endDate ");
		builderString.append("ORDER BY a.krcdtDaiPerWorkInfoPK.ymd ");
		FIND_BY_PERIOD_ORDER_BY_YMD_AND_EMPS = builderString.toString();
	}

	private String FIND_BY_WORKTYPE_PERIOD = "SELECT c.krcdtDaiPerWorkInfoPK.ymd" + " FROM KrcdtDaiPerWorkInfo c"
			+ " WHERE c.krcdtDaiPerWorkInfoPK.ymd >= :startDate" + " AND c.krcdtDaiPerWorkInfoPK.ymd <= :endDate"
			+ " AND c.recordWorkWorktypeCode = :workTypeCode" + " AND c.krcdtDaiPerWorkInfoPK.employeeId = :employeeId";

	private String FIND_BY_LIST_DATE = "SELECT c " + " FROM KrcdtDaiPerWorkInfo c"
			+ " WHERE c.krcdtDaiPerWorkInfoPK.ymd IN :dates" + " AND c.krcdtDaiPerWorkInfoPK.employeeId = :employeeId";

	@Override
	@SneakyThrows
	public Optional<WorkInfoOfDailyPerformance> find(String employeeId, GeneralDate ymd) {
		
		List<KrcdtWorkScheduleTime> scheduleTimes;
		try (PreparedStatement sqlSchedule = this.connection().prepareStatement(
				"select * from KRCDT_WORK_SCHEDULE_TIME"
				+ " where SID = ? and YMD = ?")) {
			sqlSchedule.setString(1, employeeId);
			sqlSchedule.setDate(2, Date.valueOf(ymd.localDate()));
			scheduleTimes = new NtsResultSet(sqlSchedule.executeQuery()).getList(rec -> {
				KrcdtWorkScheduleTime entity = new KrcdtWorkScheduleTime();
				entity.krcdtWorkScheduleTimePK = new KrcdtWorkScheduleTimePK(
						rec.getString("SID"),
						rec.getGeneralDate("YMD"),
						rec.getInt("WORK_NO"));
				entity.attendance = rec.getInt("ATTENDANCE");
				entity.leaveWork = rec.getInt("LEAVE_WORK");
				return entity;
			});
		};
		
		Optional<KrcdtDaiPerWorkInfo> workInfo;
		try (PreparedStatement sqlWorkInfo = this.connection().prepareStatement(
				"select * from KRCDT_DAI_PER_WORK_INFO"
				+ " where SID = ? and YMD = ?")) {
			sqlWorkInfo.setString(1, employeeId);
			sqlWorkInfo.setDate(2, Date.valueOf(ymd.localDate()));
			
			 workInfo = new NtsResultSet(sqlWorkInfo.executeQuery()).getSingle(rec -> {
				KrcdtDaiPerWorkInfo entity = new KrcdtDaiPerWorkInfo();
				entity.krcdtDaiPerWorkInfoPK = new KrcdtDaiPerWorkInfoPK(
						rec.getString("SID"), rec.getGeneralDate("YMD"));
				entity.recordWorkWorktypeCode = rec.getString("RECORD_WORK_WORKTYPE_CODE");
				entity.recordWorkWorktimeCode = rec.getString("RECORD_WORK_WORKTIME_CODE");
				entity.scheduleWorkWorktypeCode = rec.getString("SCHEDULE_WORK_WORKTYPE_CODE");
				entity.scheduleWorkWorktimeCode = rec.getString("SCHEDULE_WORK_WORKTIME_CODE");
				entity.calculationState = rec.getInt("CALCULATION_STATE");
				entity.goStraightAttribute = rec.getInt("GO_STRAIGHT_ATR");
				entity.backStraightAttribute = rec.getInt("BACK_STRAIGHT_ATR");
				entity.dayOfWeek = rec.getInt("DAY_OF_WEEK");
				entity.scheduleTimes = scheduleTimes;
				return entity;
			});
		}

		return workInfo.map(c -> c.toDomain());
	}

	@Override
	public List<WorkInfoOfDailyPerformance> findByEmployeeId(String employeeId) {
		return this.queryProxy().query(FIND_BY_EMPLOYEE_ID, KrcdtDaiPerWorkInfo.class)
				.setParameter("employeeId", employeeId).getList(c -> c.toDomain());
	}

	@Override
	public void delete(String employeeId, GeneralDate ymd) {

		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_WORK_SCHEDULE_TIME Where SID = " + "'" + employeeId + "'" + " and YMD = "
				+ "'" + ymd + "'";
		String perWorkInfo = "Delete From KRCDT_DAI_PER_WORK_INFO Where SID = " + "'" + employeeId + "'" + " and YMD = "
				+ "'" + ymd + "'";
		try {
			con.createStatement().executeUpdate(sqlQuery);
			con.createStatement().executeUpdate(perWorkInfo);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// this.getEntityManager().createQuery(DEL_BY_KEY_ID).setParameter("employeeId",
		// employeeId).setParameter("ymd", ymd)
		// .executeUpdate();
		//
		// this.getEntityManager().createQuery(DEL_BY_KEY).setParameter("employeeId",
		// employeeId).setParameter("ymd", ymd)
		// .executeUpdate();
		// this.getEntityManager().flush();
	}

	@Override
	@SneakyThrows
	public List<WorkInfoOfDailyPerformance> findByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {

		List<KrcdtWorkScheduleTime> scheduleTimes = new ArrayList<>();

		try (PreparedStatement sqlSchedule = this.connection().prepareStatement(
				"select * from KRCDT_WORK_SCHEDULE_TIME where SID = ? and YMD >= ? and YMD <= ? order by YMD ")) {
			sqlSchedule.setString(1, employeeId);
			sqlSchedule.setDate(2, Date.valueOf(datePeriod.start().localDate()));
			sqlSchedule.setDate(3, Date.valueOf(datePeriod.end().localDate()));
			scheduleTimes = new NtsResultSet(sqlSchedule.executeQuery()).getList(rec -> {
				KrcdtWorkScheduleTime entity = new KrcdtWorkScheduleTime();
				entity.krcdtWorkScheduleTimePK = new KrcdtWorkScheduleTimePK(rec.getString("SID"),
						rec.getGeneralDate("YMD"), rec.getInt("WORK_NO"));
				entity.attendance = rec.getInt("ATTENDANCE");
				entity.leaveWork = rec.getInt("LEAVE_WORK");
				return entity;
			});
		}

		List<KrcdtWorkScheduleTime> scheduleTimess = scheduleTimes;
		try (PreparedStatement statement = this.connection()
				.prepareStatement(" select * from KRCDT_DAI_PER_WORK_INFO h " + " where SID = ? " + " and YMD <= ? "
						+ " and YMD >= ? " + " order by YMD ")) {

			statement.setString(1, employeeId);
			statement.setDate(2, Date.valueOf(datePeriod.end().localDate()));
			statement.setDate(3, Date.valueOf(datePeriod.start().localDate()));

			return new NtsResultSet(statement.executeQuery()).getList(rec -> {
				GeneralDate ymd = rec.getGeneralDate("YMD");
				int calcState = rec.getInt("CALCULATION_STATE"), goStraight = rec.getInt("GO_STRAIGHT_ATR"),
						backStraight = rec.getInt("BACK_STRAIGHT_ATR"), dayOfWeek = rec.getInt("DAY_OF_WEEK");
				return new WorkInfoOfDailyPerformance(employeeId,
						new WorkInformation(rec.getString("RECORD_WORK_WORKTIME_CODE"),
								rec.getString("RECORD_WORK_WORKTYPE_CODE")),
						new WorkInformation(rec.getString("SCHEDULE_WORK_WORKTIME_CODE"),
								rec.getString("SCHEDULE_WORK_WORKTYPE_CODE")),
						calcState == CalculationState.Calculated.value ? CalculationState.Calculated
								: CalculationState.No_Calculated,
						goStraight == NotUseAttribute.Use.value ? NotUseAttribute.Use : NotUseAttribute.Not_use,
						backStraight == NotUseAttribute.Use.value ? NotUseAttribute.Use : NotUseAttribute.Not_use, ymd,
						EnumAdaptor.valueOf(dayOfWeek, DayOfWeek.class),
						scheduleTimess.stream().filter(c -> c.krcdtWorkScheduleTimePK.ymd.equals(ymd))
								.map(c -> c.toDomain()).collect(Collectors.toList()));
			});
		}
		// return this.queryProxy().query(FIND_BY_PERIOD_ORDER_BY_YMD,
		// KrcdtDaiPerWorkInfo.class)
		// .setParameter("employeeId", employeeId).setParameter("startDate",
		// datePeriod.start())
		// .setParameter("endDate", datePeriod.end()).getList(f ->
		// f.toDomain());
	}

	@Override
	@SneakyThrows
	public List<WorkInfoOfDailyPerformance> findByPeriodOrderByYmdDesc(String employeeId, DatePeriod datePeriod) {
		List<KrcdtWorkScheduleTime> scheduleTimes;
		try (PreparedStatement sqlSchedule = this.connection().prepareStatement(
					"select * from KRCDT_WORK_SCHEDULE_TIME where SID = ? and YMD >= ? and YMD <= ? order by YMD desc")) {
			sqlSchedule.setString(1, employeeId);
			sqlSchedule.setDate(2, Date.valueOf(datePeriod.start().localDate()));
			sqlSchedule.setDate(3, Date.valueOf(datePeriod.end().localDate()));
			scheduleTimes = new NtsResultSet(sqlSchedule.executeQuery()).getList(rec -> {
				KrcdtWorkScheduleTime entity = new KrcdtWorkScheduleTime();
				entity.krcdtWorkScheduleTimePK = new KrcdtWorkScheduleTimePK(rec.getString("SID"),
						rec.getGeneralDate("YMD"), rec.getInt("WORK_NO"));
				entity.attendance = rec.getInt("ATTENDANCE");
				entity.leaveWork = rec.getInt("LEAVE_WORK");
				return entity;
			});
		}
		
		try (PreparedStatement sqlWorkInfo = this.connection().prepareStatement(
					"select * from KRCDT_DAI_PER_WORK_INFO where SID = ? and YMD >= ? and YMD <= ? order by YMD desc ")) {
			sqlWorkInfo.setString(1, employeeId);
			sqlWorkInfo.setDate(2, Date.valueOf(datePeriod.start().localDate()));
			sqlWorkInfo.setDate(3, Date.valueOf(datePeriod.end().localDate()));

			return new NtsResultSet(sqlWorkInfo.executeQuery()).getList(rec -> {
				GeneralDate ymd = rec.getGeneralDate("YMD");
				int calcState = rec.getInt("CALCULATION_STATE"), goStraight = rec.getInt("GO_STRAIGHT_ATR"),
						backStraight = rec.getInt("BACK_STRAIGHT_ATR"), dayOfWeek = rec.getInt("DAY_OF_WEEK");
				return new WorkInfoOfDailyPerformance(employeeId,
						new WorkInformation(rec.getString("RECORD_WORK_WORKTIME_CODE"),
								rec.getString("RECORD_WORK_WORKTYPE_CODE")),
						new WorkInformation(rec.getString("SCHEDULE_WORK_WORKTIME_CODE"),
								rec.getString("SCHEDULE_WORK_WORKTYPE_CODE")),
						calcState == CalculationState.Calculated.value ? CalculationState.Calculated
								: CalculationState.No_Calculated,
						goStraight == NotUseAttribute.Use.value ? NotUseAttribute.Use : NotUseAttribute.Not_use,
						backStraight == NotUseAttribute.Use.value ? NotUseAttribute.Use : NotUseAttribute.Not_use, ymd,
						EnumAdaptor.valueOf(dayOfWeek, DayOfWeek.class),
						scheduleTimes.stream().filter(c -> c.krcdtWorkScheduleTimePK.ymd.equals(ymd))
								.map(c -> c.toDomain()).collect(Collectors.toList()));
			});
		}
	}

	@Override
	public List<WorkInfoOfDailyPerformance> findByListEmployeeId(List<String> employeeIds, DatePeriod ymds) {
		List<Object[]> result = new ArrayList<>();
		StringBuilder query = new StringBuilder(
				"SELECT af, c from KrcdtDaiPerWorkInfo af LEFT JOIN af.scheduleTimes c ");
		query.append(" WHERE af.krcdtDaiPerWorkInfoPK.employeeId IN :employeeId ");
		query.append(" AND af.krcdtDaiPerWorkInfoPK.ymd <= :end AND af.krcdtDaiPerWorkInfoPK.ymd >= :start");
		TypedQueryWrapper<Object[]> tQuery = this.queryProxy().query(query.toString(), Object[].class);
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(tQuery.setParameter("employeeId", empIds).setParameter("start", ymds.start())
					.setParameter("end", ymds.end()).getList());
		});
		return toDomainFromJoin(result);
	}

	@Override
	public void updateByKey(WorkInfoOfDailyPerformance domain) {

		if (domain != null) {
			KrcdtDaiPerWorkInfo data = this.findKrcdtDaiPerWorkInfoWithJdbc(domain.getEmployeeId(), domain.getYmd())
					.orElseGet(() -> new KrcdtDaiPerWorkInfo(
							new KrcdtDaiPerWorkInfoPK(domain.getEmployeeId(), domain.getYmd())));

			internalUpdate(domain, data);
		}
	}

	private void internalUpdate(WorkInfoOfDailyPerformance domain, KrcdtDaiPerWorkInfo data) {
		if (domain.getRecordInfo() != null) {
			data.recordWorkWorktimeCode = domain.getRecordInfo().getWorkTimeCode() == null ? null
					: domain.getRecordInfo().getWorkTimeCode().v();
			data.recordWorkWorktypeCode = domain.getRecordInfo().getWorkTypeCode().v();
		}
		if (domain.getScheduleInfo() != null) {
			data.scheduleWorkWorktimeCode = domain.getScheduleInfo().getWorkTimeCode() == null ? null
					: domain.getScheduleInfo().getWorkTimeCode().v();
			data.scheduleWorkWorktypeCode = domain.getScheduleInfo().getWorkTypeCode().v();
		}
		data.calculationState = domain.getCalculationState().value;
		data.backStraightAttribute = domain.getBackStraightAtr().value;
		data.goStraightAttribute = domain.getGoStraightAtr().value;
		data.dayOfWeek = domain.getDayOfWeek().value;
		if(domain.getScheduleTimeSheets().isEmpty()){
			data.scheduleTimes.forEach(c -> {
				this.commandProxy().remove(getEntityManager().merge(c));
			});
			data.scheduleTimes = new ArrayList<>();
//				this.getEntityManager().flush();
		} else {
			if (data.scheduleTimes == null || data.scheduleTimes.isEmpty()) {
				data.scheduleTimes = domain.getScheduleTimeSheets().stream()
						.map(c -> new KrcdtWorkScheduleTime(
								new KrcdtWorkScheduleTimePK(domain.getEmployeeId(), domain.getYmd(),
										c.getWorkNo().v()),
								c.getAttendance().valueAsMinutes(), c.getLeaveWork().valueAsMinutes()))
						.collect(Collectors.toList());
			} else {
				data.scheduleTimes.stream().forEach(st -> {
					domain.getScheduleTimeSheets().stream()
							.filter(dst -> dst.getWorkNo().v() == st.krcdtWorkScheduleTimePK.workNo).findFirst()
							.ifPresent(dst -> {
								st.attendance = dst.getAttendance().valueAsMinutes();
								st.leaveWork = dst.getLeaveWork().valueAsMinutes();
							});
				});
				this.commandProxy().updateAll(data.scheduleTimes);
			}
		}

		this.commandProxy().update(data);
	}
	
	@Override
	public void update(List<WorkInfoOfDailyPerformance> domains) {
		List<KrcdtDaiPerWorkInfo> olds = this.findKrcdtDaiPerWorkInfoWithJdbc(domains.stream().map(c -> c.getEmployeeId()).collect(Collectors.toList()), 
																				domains.stream().map(c -> c.getYmd()).collect(Collectors.toList()));
		domains.stream().forEach(d -> {
			KrcdtDaiPerWorkInfo data = olds.stream().filter(o -> o.krcdtDaiPerWorkInfoPK.employeeId.equals(d.getEmployeeId()) 
																&& o.krcdtDaiPerWorkInfoPK.ymd.equals(d.getYmd()))
													.findFirst().orElseGet(() -> new KrcdtDaiPerWorkInfo(new KrcdtDaiPerWorkInfoPK(d.getEmployeeId(), d.getYmd())));

			internalUpdate(d, data);
		});
	}
	
	@Override
	public void updateV2(List<WorkInfoOfDailyPerformance> domains) {
		List<KrcdtDaiPerWorkInfo> olds = this.finds(domains.stream().map(c -> c.getEmployeeId()).collect(Collectors.toList()), 
													domains.stream().map(c -> c.getYmd()).collect(Collectors.toList()));
		domains.stream().forEach(d -> {
			KrcdtDaiPerWorkInfo data = olds.stream().filter(o -> o.krcdtDaiPerWorkInfoPK.employeeId.equals(d.getEmployeeId()) 
																&& o.krcdtDaiPerWorkInfoPK.ymd.equals(d.getYmd()))
													.findFirst().orElseGet(() -> new KrcdtDaiPerWorkInfo(new KrcdtDaiPerWorkInfoPK(d.getEmployeeId(), d.getYmd())));

			internalUpdate(d, data);
		});
	}
	
	private List<KrcdtDaiPerWorkInfo> finds(List<String> employeeIds, List<GeneralDate> ymds) {
		List<KrcdtDaiPerWorkInfo> result = new ArrayList<>();
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a FROM KrcdtDaiPerWorkInfo a");
		builderString.append(" WHERE a.krcdtDaiPerWorkInfoPK.employeeId IN :employeeIds");
		builderString.append(" AND a.krcdtDaiPerWorkInfoPK.ymd IN :processingYmds");
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistEmployeeIds -> {
			CollectionUtil.split(ymds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistYMDs -> {
				result.addAll(this.getEntityManager().createQuery(builderString.toString(), KrcdtDaiPerWorkInfo.class)
										.setParameter("employeeIds", sublistEmployeeIds)
										.setParameter("processingYmds", sublistYMDs).getResultList());
			});
		});
		
		return result;
	}
	
	@SneakyThrows
	private List<KrcdtDaiPerWorkInfo> findKrcdtDaiPerWorkInfoWithJdbc(List<String> employeeIds, List<GeneralDate> ymds) {
		List<KrcdtDaiPerWorkInfo> result = new ArrayList<>();
		
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistEmployeeIds -> {
			CollectionUtil.split(ymds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistYMDs -> {
				getWorkInfo(sublistEmployeeIds, sublistYMDs);
				result.addAll(this.getWorkInfo(sublistEmployeeIds, sublistYMDs));
			});
		});
		
		return result;
	}

	@SneakyThrows
	private List<KrcdtDaiPerWorkInfo> getWorkInfo(List<String> sublistEmployeeIds, List<GeneralDate> sublistYMDs) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("select * from KRCDT_DAI_PER_WORK_INFO ");
		builderString.append("where SID IN (");
		builderString.append(sublistEmployeeIds.stream().map(e -> "?").collect(Collectors.joining(",")));
		builderString.append(") AND YMD IN ( ");
		builderString.append(sublistYMDs.stream().map(e -> "?").collect(Collectors.joining(",")));
		builderString.append(") ");
		try (PreparedStatement stmtFindById = this.connection().prepareStatement(builderString.toString())) {
			for(int i = 0; i < sublistEmployeeIds.size(); i++){
				stmtFindById.setString(i + 1, sublistEmployeeIds.get(i));
			}
			for(int i = 0; i < sublistEmployeeIds.size(); i++){
				stmtFindById.setDate(i + 1 + sublistEmployeeIds.size(), Date.valueOf(sublistYMDs.get(i).toLocalDate()));
			}

			return new NtsResultSet(stmtFindById.executeQuery()).getList(rec -> {
				return convertToEntity(rec);
			});
		}
	}

	@SneakyThrows
	private Optional<KrcdtDaiPerWorkInfo> findKrcdtDaiPerWorkInfoWithJdbc(String employeeId, GeneralDate ymd) {
		
		try (PreparedStatement stmtFindById = this.connection().prepareStatement(
				"select * from KRCDT_DAI_PER_WORK_INFO"
				+ " where SID = ? and YMD = ?")) {
			stmtFindById.setString(1, employeeId);
			stmtFindById.setDate(2, Date.valueOf(ymd.toLocalDate()));

			return new NtsResultSet(stmtFindById.executeQuery()).getSingle(rec -> {
				return convertToEntity(rec);
			});
		}
	}

	private KrcdtDaiPerWorkInfo convertToEntity(NtsResultRecord rec) {
		KrcdtDaiPerWorkInfoPK pk = new KrcdtDaiPerWorkInfoPK();
		pk.employeeId = rec.getString("SID");
		pk.ymd = rec.getGeneralDate("YMD");
		
		KrcdtDaiPerWorkInfo entity = new KrcdtDaiPerWorkInfo();
		entity.krcdtDaiPerWorkInfoPK = pk;
		entity.recordWorkWorktypeCode = rec.getString("RECORD_WORK_WORKTYPE_CODE");
		entity.recordWorkWorktimeCode = rec.getString("RECORD_WORK_WORKTIME_CODE");
		entity.scheduleWorkWorktypeCode = rec.getString("SCHEDULE_WORK_WORKTYPE_CODE");
		entity.scheduleWorkWorktimeCode = rec.getString("SCHEDULE_WORK_WORKTIME_CODE");
		entity.calculationState = rec.getInt("CALCULATION_STATE");
		entity.goStraightAttribute = rec.getInt("GO_STRAIGHT_ATR");
		entity.backStraightAttribute = rec.getInt("BACK_STRAIGHT_ATR");
		entity.dayOfWeek = rec.getInt("DAY_OF_WEEK");
		try (PreparedStatement stmtSche = this.connection().prepareStatement(
					"select * from KRCDT_WORK_SCHEDULE_TIME"
					+ " where SID = ? and YMD = ?")) {
			stmtSche.setString(1, pk.employeeId);
			stmtSche.setDate(2, Date.valueOf(pk.ymd.toLocalDate()));
			entity.scheduleTimes = new NtsResultSet(stmtSche.executeQuery()).getList(rs -> {
				KrcdtWorkScheduleTimePK pks = new KrcdtWorkScheduleTimePK();
				pks.employeeId = rs.getString("SID");
				pks.ymd = rs.getGeneralDate("YMD");
				pks.workNo = rs.getInt("WORK_NO");
				
				KrcdtWorkScheduleTime es = new KrcdtWorkScheduleTime();
				es.krcdtWorkScheduleTimePK = pks;
				es.attendance = rs.getInt("ATTENDANCE");
				es.leaveWork = rs.getInt("LEAVE_WORK");
				
				return es;
			});
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return entity;
	}

	@Override
	public void insert(WorkInfoOfDailyPerformance workInfoOfDailyPerformance) {
		this.commandProxy().insert(KrcdtDaiPerWorkInfo.toEntity(workInfoOfDailyPerformance));
	}

	@Override
	public void updateByKeyFlush(WorkInfoOfDailyPerformance workInfoOfDailyPerformance) {
		this.updateByKey(workInfoOfDailyPerformance);
		this.getEntityManager().flush();

	}

	@Override
	public List<WorkInfoOfDailyPerformance> finds(Map<String, List<GeneralDate>> param) {
		List<Object[]> result = new ArrayList<>();
		StringBuilder query = new StringBuilder(
				"SELECT af, c from KrcdtDaiPerWorkInfo af LEFT JOIN af.scheduleTimes c ");
		query.append(" WHERE af.krcdtDaiPerWorkInfoPK.employeeId IN :employeeId ");
		query.append(" AND af.krcdtDaiPerWorkInfoPK.ymd IN :date");
		TypedQueryWrapper<Object[]> tQuery = this.queryProxy().query(query.toString(), Object[].class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList().stream().filter(c -> {
						KrcdtDaiPerWorkInfo af = (KrcdtDaiPerWorkInfo) c[0];
						return p.get(af.krcdtDaiPerWorkInfoPK.employeeId).contains(af.krcdtDaiPerWorkInfoPK.ymd);
					}).collect(Collectors.toList()));
		});
		return toDomainFromJoin(result);
	}

	private List<WorkInfoOfDailyPerformance> toDomainFromJoin(List<Object[]> result) {
		return result.stream()
				.collect(Collectors.groupingBy(c1 -> c1[0],
						Collectors.collectingAndThen(Collectors.toList(),
								list -> list.stream().filter(c -> c[1] != null).map(c -> (KrcdtWorkScheduleTime) c[1])
										.collect(Collectors.toList()))))
				.entrySet().stream()
				.map(e -> KrcdtDaiPerWorkInfo.toDomain((KrcdtDaiPerWorkInfo) e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}

	@Override
	public List<WorkInfoOfDailyPerformance> findByPeriodOrderByYmdAndEmps(List<String> employeeIds,
			DatePeriod datePeriod) {
		if(employeeIds.isEmpty())
			return Collections.emptyList();
		List<WorkInfoOfDailyPerformance> resultList = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(FIND_BY_PERIOD_ORDER_BY_YMD_AND_EMPS, KrcdtDaiPerWorkInfo.class)
				.setParameter("employeeIds", subList)
				.setParameter("startDate", datePeriod.start())
				.setParameter("endDate", datePeriod.end()).getList(f -> f.toDomain()));
		});
		resultList.sort(Comparator.comparing(WorkInfoOfDailyPerformance::getYmd));
		return resultList;
	}

	@Override
	public List<GeneralDate> getByWorkTypeAndDatePeriod(String employeeId, String workTypeCode, DatePeriod period) {
		List<GeneralDate> lstOutput = this.queryProxy()
				.query(FIND_BY_WORKTYPE_PERIOD, GeneralDate.class)
				.setParameter("startDate", period.start())
				.setParameter("endDate", period.end())
				.setParameter("workTypeCode", workTypeCode)
				.setParameter("employeeId", employeeId)
				.getList();
		return lstOutput;
	}

	@Override
	public List<WorkInfoOfDailyPerformance> findByListDate(String employeeId, List<GeneralDate> dates) {
		if(dates.isEmpty())
			return Collections.emptyList();
		
		List<WorkInfoOfDailyPerformance> resultList = new ArrayList<>();
		CollectionUtil.split(dates, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(FIND_BY_LIST_DATE, KrcdtDaiPerWorkInfo.class)
				.setParameter("employeeId", employeeId)
				.setParameter("dates", subList)
				.getList(f -> f.toDomain()));
		});
		
		return resultList;
	}

}