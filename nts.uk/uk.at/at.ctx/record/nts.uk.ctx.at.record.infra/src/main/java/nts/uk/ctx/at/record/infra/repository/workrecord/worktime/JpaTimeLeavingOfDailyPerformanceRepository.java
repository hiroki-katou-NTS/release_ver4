package nts.uk.ctx.at.record.infra.repository.workrecord.worktime;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
//import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtWorkScheduleTime;
//import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtWorkScheduleTimePK;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtDaiLeavingWork;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtDaiLeavingWorkPK;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtTimeLeavingWork;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtTimeLeavingWorkPK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class JpaTimeLeavingOfDailyPerformanceRepository extends JpaRepository
		implements TimeLeavingOfDailyPerformanceRepository {

//	private static final String REMOVE_BY_EMPLOYEE;
//
//	private static final String REMOVE_TIME_LEAVING_WORK;

	private static final String FIND_BY_KEY;

//	private static final String FIND_BY_PERIOD_ORDER_BY_YMD;

	static {
		StringBuilder builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDaiLeavingWork a ");
//		builderString.append("WHERE a.krcdtDaiLeavingWorkPK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtDaiLeavingWorkPK.ymd = :ymd ");
//		REMOVE_BY_EMPLOYEE = builderString.toString();
//
//		builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtTimeLeavingWork a ");
//		builderString.append("WHERE a.krcdtTimeLeavingWorkPK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtTimeLeavingWorkPK.ymd = :ymd ");
//		builderString.append("AND a.krcdtTimeLeavingWorkPK.timeLeavingType = :timeLeavingType ");
//		REMOVE_TIME_LEAVING_WORK = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiLeavingWork a ");
		builderString.append("WHERE a.krcdtDaiLeavingWorkPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiLeavingWorkPK.ymd = :ymd ");
		FIND_BY_KEY = builderString.toString();

//		builderString = new StringBuilder();
//		builderString.append("SELECT a ");
//		builderString.append("FROM KrcdtDaiLeavingWork a ");
//		builderString.append("WHERE a.krcdtDaiLeavingWorkPK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtDaiLeavingWorkPK.ymd >= :start ");
//		builderString.append("AND a.krcdtDaiLeavingWorkPK.ymd <= :end ");
//		builderString.append("ORDER BY a.krcdtDaiLeavingWorkPK.ymd ");
//		FIND_BY_PERIOD_ORDER_BY_YMD = builderString.toString();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		try (val timeLeavingWorkStatement = this.connection().prepareStatement(
					"delete from KRCDT_TIME_LEAVING_WORK where SID = ? and YMD = ? and TIME_LEAVING_TYPE = ?")) {
			timeLeavingWorkStatement.setString(1, employeeId);
			timeLeavingWorkStatement.setDate(2, Date.valueOf(ymd.toLocalDate()));
			timeLeavingWorkStatement.setInt(3, 0);
			timeLeavingWorkStatement.execute();
			
			try (val statement = this.connection().prepareStatement(
					"delete from KRCDT_DAI_LEAVING_WORK where SID = ? and YMD = ?")) {
				statement.setString(1, employeeId);
				statement.setDate(2, Date.valueOf(ymd.toLocalDate()));
				statement.execute();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<TimeLeavingOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd) {
		return this.queryProxy().query(FIND_BY_KEY, KrcdtDaiLeavingWork.class).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).getSingle(f -> f.toDomain());
	}

	@Override
	@SneakyThrows
	public List<TimeLeavingOfDailyPerformance> findbyPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {

		List<KrcdtTimeLeavingWork> scheduleTimes = new ArrayList<>();

		try (PreparedStatement sqlSchedule = this.connection().prepareStatement(
				"select * from KRCDT_TIME_LEAVING_WORK where SID = ? and YMD >= ? and YMD <= ? order by YMD ")) {
			sqlSchedule.setString(1, employeeId);
			sqlSchedule.setDate(2, Date.valueOf(datePeriod.start().localDate()));
			sqlSchedule.setDate(3, Date.valueOf(datePeriod.end().localDate()));

			scheduleTimes = new NtsResultSet(sqlSchedule.executeQuery()).getList(rec -> {
				KrcdtTimeLeavingWork entity = new KrcdtTimeLeavingWork();
				entity.krcdtTimeLeavingWorkPK = new KrcdtTimeLeavingWorkPK(rec.getString("SID"), rec.getInt("WORK_NO"),
						rec.getGeneralDate("YMD"), rec.getInt("TIME_LEAVING_TYPE"));
				entity.attendanceActualRoudingTime = rec.getInt("ATD_ACTUAL_ROUDING_TIME_DAY");
				entity.attendanceActualTime = rec.getInt("ATD_ACTUAL_TIME");
				entity.attendanceActualPlaceCode = rec.getString("ATD_ACTUAL_PLACE_CODE");
				entity.attendanceActualSourceInfo = rec.getInt("ATD_ACTUAL_SOURCE_INFO");

				entity.attendanceStampRoudingTime = rec.getInt("ATD_STAMP_ROUDING_TIME_DAY");
				entity.attendanceStampTime = rec.getInt("ATD_STAMP_TIME");
				entity.attendanceStampPlaceCode = rec.getString("ATD_STAMP_PLACE_CODE");
				entity.attendanceStampSourceInfo = rec.getInt("ATD_STAMP_SOURCE_INFO");

				entity.attendanceNumberStamp = rec.getInt("ATD_NUMBER_STAMP");

				entity.leaveWorkActualRoundingTime = rec.getInt("LWK_ACTUAL_ROUDING_TIME_DAY");
				entity.leaveWorkActualTime = rec.getInt("LWK_ACTUAL_TIME");
				entity.leaveWorkActualPlaceCode = rec.getString("LWK_ACTUAL_PLACE_CODE");
				entity.leaveActualSourceInfo = rec.getInt("LWK_ACTUAL_SOURCE_INFO");

				entity.leaveWorkStampRoundingTime = rec.getInt("LWK_STAMP_ROUDING_TIME_DAY");
				entity.leaveWorkStampTime = rec.getInt("LWK_STAMP_TIME");
				entity.leaveWorkStampPlaceCode = rec.getString("LWK_STAMP_PLACE_CODE");
				entity.leaveWorkStampSourceInfo = rec.getInt("LWK_STAMP_SOURCE_INFO");

				entity.leaveWorkNumberStamp = rec.getInt("LWK_NUMBER_STAMP");

				return entity;
			});
		}

		List<KrcdtTimeLeavingWork> newScheduleTimes = scheduleTimes;

		try (PreparedStatement sqlScheduleNew = this.connection().prepareStatement(
				"select * from KRCDT_DAI_LEAVING_WORK where SID = ? and YMD >= ? and YMD <= ? order by YMD ")) {
			sqlScheduleNew.setString(1, employeeId);
			sqlScheduleNew.setDate(2, Date.valueOf(datePeriod.start().localDate()));
			sqlScheduleNew.setDate(3, Date.valueOf(datePeriod.end().localDate()));

			return new NtsResultSet(sqlScheduleNew.executeQuery()).getList(newRec -> {
				GeneralDate ymd = newRec.getGeneralDate("YMD");
				return new TimeLeavingOfDailyPerformance(employeeId, new WorkTimes(newRec.getInt("WORK_TIMES")),
						newScheduleTimes.stream().filter(c -> c.krcdtTimeLeavingWorkPK.ymd.equals(ymd))
								.map(item -> item.toDomain()).collect(Collectors.toList()),
						ymd);
			});
		}

		// return this.queryProxy().query(FIND_BY_PERIOD_ORDER_BY_YMD,
		// KrcdtDaiLeavingWork.class)
		// .setParameter("employeeId", employeeId).setParameter("start",
		// datePeriod.start())
		// .setParameter("end", datePeriod.end()).getList(f -> f.toDomain());
	}

	@Override
	public void update(TimeLeavingOfDailyPerformance domain) {
		if (domain == null) {
			return;
		}
		
		internalUpdate(domain, getDailyLeaving(domain.getEmployeeId(), domain.getYmd()));
		// this.getEntityManager().flush();
	}

	private void internalUpdate(TimeLeavingOfDailyPerformance domain, KrcdtDaiLeavingWork entity) {
		List<KrcdtTimeLeavingWork> timeWorks = entity.timeLeavingWorks;
		entity.workTimes = domain.getWorkTimes() == null ? null : domain.getWorkTimes().v();
		domain.getTimeLeavingWorks().stream().forEach(c -> {
			KrcdtTimeLeavingWork krcdtTimeLeavingWork = timeWorks.stream()
					.filter(x -> x.krcdtTimeLeavingWorkPK.workNo == c.getWorkNo().v()
							&& x.krcdtTimeLeavingWorkPK.timeLeavingType == 0)
					.findFirst().orElse(null);
			boolean isNew = krcdtTimeLeavingWork == null;
			if (isNew) {
				krcdtTimeLeavingWork = new KrcdtTimeLeavingWork();
				krcdtTimeLeavingWork.krcdtTimeLeavingWorkPK = new KrcdtTimeLeavingWorkPK(domain.getEmployeeId(),
						c.getWorkNo().v(), domain.getYmd(), 0);
			}
			if (c.getAttendanceStamp().isPresent()) {
				TimeActualStamp attendanceStamp = c.getAttendanceStamp().get();
				WorkStamp attendanceActualS = attendanceStamp.getActualStamp().orElse(null);
				WorkStamp attendanceS = attendanceStamp.getStamp().orElse(null);
				if (attendanceActualS != null) {
					krcdtTimeLeavingWork.attendanceActualRoudingTime = attendanceActualS.getAfterRoundingTime() == null
							? null : attendanceActualS.getAfterRoundingTime().valueAsMinutes();
					krcdtTimeLeavingWork.attendanceActualTime = attendanceActualS.getTimeWithDay() == null ? null
							: attendanceActualS.getTimeWithDay().valueAsMinutes();
					krcdtTimeLeavingWork.attendanceActualPlaceCode = !attendanceActualS.getLocationCode().isPresent()
							? null : attendanceActualS.getLocationCode().get().v();
					krcdtTimeLeavingWork.attendanceActualSourceInfo = attendanceActualS.getStampSourceInfo() == null ? 0
							: attendanceActualS.getStampSourceInfo().value;
				} else {
					krcdtTimeLeavingWork.attendanceActualRoudingTime = null;
					krcdtTimeLeavingWork.attendanceActualTime = null;
					krcdtTimeLeavingWork.attendanceActualPlaceCode = null;
					krcdtTimeLeavingWork.attendanceActualSourceInfo = null;
				}
				if (attendanceS != null) {
					krcdtTimeLeavingWork.attendanceStampRoudingTime = attendanceS.getAfterRoundingTime() == null ? null
							: attendanceS.getAfterRoundingTime().valueAsMinutes();
					krcdtTimeLeavingWork.attendanceStampTime = attendanceS.getTimeWithDay() == null ? null
							: attendanceS.getTimeWithDay().valueAsMinutes();
					krcdtTimeLeavingWork.attendanceStampPlaceCode = !attendanceS.getLocationCode().isPresent() ? null
							: attendanceS.getLocationCode().get().v();
					krcdtTimeLeavingWork.attendanceStampSourceInfo = attendanceS.getStampSourceInfo() == null ? 0
							: attendanceS.getStampSourceInfo().value;
				} else {
					krcdtTimeLeavingWork.attendanceStampRoudingTime = null;
					krcdtTimeLeavingWork.attendanceStampTime = null;
					krcdtTimeLeavingWork.attendanceStampPlaceCode = null;
					krcdtTimeLeavingWork.attendanceStampSourceInfo = null;
				}
				krcdtTimeLeavingWork.attendanceNumberStamp = attendanceStamp.getNumberOfReflectionStamp();
			}
			if (c.getLeaveStamp().isPresent()) {
				TimeActualStamp ls = c.getLeaveStamp().get();
				WorkStamp as = ls.getActualStamp().orElse(null);
				WorkStamp s = ls.getStamp().orElse(null);
				if (as != null) {
					krcdtTimeLeavingWork.leaveWorkActualRoundingTime = as.getAfterRoundingTime() == null ? null
							: as.getAfterRoundingTime().valueAsMinutes();
					krcdtTimeLeavingWork.leaveWorkActualTime = as.getTimeWithDay() == null ? null
							: as.getTimeWithDay().valueAsMinutes();
					krcdtTimeLeavingWork.leaveWorkActualPlaceCode = !as.getLocationCode().isPresent() ? null
							: as.getLocationCode().get().v();
					krcdtTimeLeavingWork.leaveActualSourceInfo = as.getStampSourceInfo() == null ? 0
							: as.getStampSourceInfo().value;
				} else {
					krcdtTimeLeavingWork.leaveWorkActualRoundingTime = null;
					krcdtTimeLeavingWork.leaveWorkActualTime = null;
					krcdtTimeLeavingWork.leaveWorkActualPlaceCode = null;
					krcdtTimeLeavingWork.leaveActualSourceInfo = null;
				}
				if (s != null) {
					krcdtTimeLeavingWork.leaveWorkStampRoundingTime = s.getAfterRoundingTime() == null ? null
							: s.getAfterRoundingTime().valueAsMinutes();
					krcdtTimeLeavingWork.leaveWorkStampTime = s.getTimeWithDay() == null ? null
							: s.getTimeWithDay().valueAsMinutes();
					krcdtTimeLeavingWork.leaveWorkStampPlaceCode = !s.getLocationCode().isPresent() ? null
							: s.getLocationCode().get().v();
					krcdtTimeLeavingWork.leaveWorkStampSourceInfo = s.getStampSourceInfo() == null ? 0
							: s.getStampSourceInfo().value;
				} else {
					krcdtTimeLeavingWork.leaveWorkStampRoundingTime = null;
					krcdtTimeLeavingWork.leaveWorkStampTime = null;
					krcdtTimeLeavingWork.leaveWorkStampPlaceCode = null;
					krcdtTimeLeavingWork.leaveWorkStampSourceInfo = null;
				}
				krcdtTimeLeavingWork.leaveWorkNumberStamp = ls.getNumberOfReflectionStamp();

			}
			// krcdtTimeLeavingWork.krcdtTimeLeavingWorkPK.timeLeavingType = 0;
			krcdtTimeLeavingWork.daiLeavingWork = entity;
			if (isNew) {
				timeWorks.add(krcdtTimeLeavingWork);
			}
		});

		entity.timeLeavingWorks = timeWorks.isEmpty() ? null : timeWorks;
		this.commandProxy().update(entity);
		if (!timeWorks.isEmpty()) {
			this.commandProxy().updateAll(entity.timeLeavingWorks);
		}
	}

	private KrcdtDaiLeavingWork getDailyLeaving(String employee, GeneralDate date) {
		try (val statement = this.connection().prepareStatement(
					"select * FROM KRCDT_DAI_LEAVING_WORK where SID = ? and YMD = ?")) {
			statement.setString(1, employee);
			statement.setDate(2, Date.valueOf(date.localDate()));
			Optional<KrcdtDaiLeavingWork> krcdtDaiBreakTimes = new NtsResultSet(statement.executeQuery())
					.getSingle(rec -> {
						val entity = new KrcdtDaiLeavingWork();
						entity.krcdtDaiLeavingWorkPK = new KrcdtDaiLeavingWorkPK(employee, date);
						entity.workTimes = rec.getInt("WORK_TIMES");
						entity.timeLeavingWorks = getTimeLeavingWork(employee, date);
						return entity;
					});

			if (!krcdtDaiBreakTimes.isPresent()) {
				return getDefault(employee, date);
			}

			return krcdtDaiBreakTimes.get();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private KrcdtDaiLeavingWork getDefault(String employee, GeneralDate date) {
		KrcdtDaiLeavingWork defaultV = new KrcdtDaiLeavingWork();
		defaultV.krcdtDaiLeavingWorkPK = new KrcdtDaiLeavingWorkPK(employee, date);
		defaultV.timeLeavingWorks = new ArrayList<>();
		return defaultV;
	}

	private List<KrcdtTimeLeavingWork> getTimeLeavingWork(String employee, GeneralDate date) {
		try (PreparedStatement statement = this.connection().prepareStatement(
					"select * FROM KRCDT_TIME_LEAVING_WORK where SID = ? and YMD = ? and TIME_LEAVING_TYPE = ?")) {

			statement.setString(1, employee);
			statement.setDate(2, Date.valueOf(date.localDate()));
			statement.setInt(3, 0);
			List<KrcdtTimeLeavingWork> krcdtTimeLeaveWorks = new NtsResultSet(statement.executeQuery()).getList(rec -> {
				val entity = new KrcdtTimeLeavingWork();
				entity.krcdtTimeLeavingWorkPK = new KrcdtTimeLeavingWorkPK();
				entity.krcdtTimeLeavingWorkPK.employeeId = employee;
				entity.krcdtTimeLeavingWorkPK.ymd = date;
				entity.krcdtTimeLeavingWorkPK.timeLeavingType = 0;
				entity.krcdtTimeLeavingWorkPK.workNo = rec.getInt("WORK_NO");
				entity.attendanceActualRoudingTime = rec.getInt("ATD_ACTUAL_ROUDING_TIME_DAY");
				entity.attendanceActualTime = rec.getInt("ATD_ACTUAL_TIME");
				entity.attendanceActualPlaceCode = rec.getString("ATD_ACTUAL_PLACE_CODE");
				entity.attendanceActualSourceInfo = rec.getInt("ATD_ACTUAL_SOURCE_INFO");
				entity.attendanceStampRoudingTime = rec.getInt("ATD_STAMP_ROUDING_TIME_DAY");
				entity.attendanceStampTime = rec.getInt("ATD_STAMP_TIME");
				entity.attendanceStampPlaceCode = rec.getString("ATD_STAMP_PLACE_CODE");
				entity.attendanceStampSourceInfo = rec.getInt("ATD_STAMP_SOURCE_INFO");
				entity.attendanceNumberStamp = rec.getInt("ATD_NUMBER_STAMP");
				entity.leaveWorkActualRoundingTime = rec.getInt("LWK_ACTUAL_ROUDING_TIME_DAY");
				entity.leaveWorkActualTime = rec.getInt("LWK_ACTUAL_TIME");
				entity.leaveWorkActualPlaceCode = rec.getString("LWK_ACTUAL_PLACE_CODE");
				entity.leaveActualSourceInfo = rec.getInt("LWK_ACTUAL_SOURCE_INFO");
				entity.leaveWorkStampRoundingTime = rec.getInt("LWK_STAMP_ROUDING_TIME_DAY");
				entity.leaveWorkStampTime = rec.getInt("LWK_STAMP_TIME");
				entity.leaveWorkStampPlaceCode = rec.getString("LWK_STAMP_PLACE_CODE");
				entity.leaveWorkStampSourceInfo = rec.getInt("LWK_STAMP_SOURCE_INFO");
				entity.leaveWorkNumberStamp = rec.getInt("LWK_NUMBER_STAMP");
				return entity;
			});

			return krcdtTimeLeaveWorks;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void insert(TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance) {
		if (timeLeavingOfDailyPerformance == null) {
			return;
		}
		// this.commandProxy().insert(KrcdtDaiLeavingWork.toEntity(timeLeavingOfDailyPerformance));
		// this.getEntityManager().flush();
		try {
			Connection con = this.getEntityManager().unwrap(Connection.class);
			Statement statementI = con.createStatement();

			String insertTableSQL = "INSERT INTO KRCDT_DAI_LEAVING_WORK ( SID , YMD , WORK_TIMES ) " + "VALUES( '"
					+ timeLeavingOfDailyPerformance.getEmployeeId() + "' , '" + timeLeavingOfDailyPerformance.getYmd()
					+ "' , " + timeLeavingOfDailyPerformance.getWorkTimes().v() + " )";
			statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertTableSQL));

			for (TimeLeavingWork timeLeavingWork : timeLeavingOfDailyPerformance.getTimeLeavingWorks()) {
				// TimeLeavingWork - attendanceStamp - actualStamp
				Integer attActualRoundingTime = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getActualStamp().isPresent())
								? timeLeavingWork.getAttendanceStamp().get().getActualStamp().get()
										.getAfterRoundingTime().valueAsMinutes()
								: null;
				Integer attActualTime = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getActualStamp().isPresent())
								? timeLeavingWork.getAttendanceStamp().get().getActualStamp().get().getTimeWithDay()
										.valueAsMinutes()
								: null;
				Integer attActualStampSource = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getActualStamp().isPresent())
								? timeLeavingWork.getAttendanceStamp().get().getActualStamp().get()
										.getStampSourceInfo().value
								: null;
				String attActualStampLocationCode = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getActualStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getActualStamp().get().getLocationCode()
								.isPresent())
										? "'" + timeLeavingWork.getAttendanceStamp().get().getActualStamp().get()
												.getLocationCode().get().v() + "'"
										: null;

				// TimeLeavingWork - attendanceStamp - stamp
				Integer attStampRoundingTime = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent())
								? timeLeavingWork.getAttendanceStamp().get().getStamp().get().getAfterRoundingTime()
										.valueAsMinutes()
								: null;
				Integer attStampTime = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent())
								? timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay()
										.valueAsMinutes()
								: null;
				Integer attStampSource = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent())
								? timeLeavingWork.getAttendanceStamp().get().getStamp().get().getStampSourceInfo().value
								: null;
				String attStampLocationCode = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getStamp().get().getLocationCode().isPresent())
								? "'" + timeLeavingWork.getAttendanceStamp().get().getStamp().get().getLocationCode()
										.get().v() + "'"
								: null;
				// TimeLeavingWork - leaveStamp - numberOfReflectionStamp
				Integer attNumberReflec = timeLeavingWork.getAttendanceStamp().isPresent()
						? timeLeavingWork.getAttendanceStamp().get().getNumberOfReflectionStamp() : null;

				// TimeLeavingWork - leaveStamp - actualStamp
				Integer leaveActualRoundingTime = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getActualStamp().isPresent())
								? timeLeavingWork.getLeaveStamp().get().getActualStamp().get().getAfterRoundingTime()
										.valueAsMinutes()
								: null;
				Integer leaveActualTime = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getActualStamp().isPresent())
								? timeLeavingWork.getLeaveStamp().get().getActualStamp().get().getTimeWithDay()
										.valueAsMinutes()
								: null;
				Integer leaveActualStampSource = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getActualStamp().isPresent())
								? timeLeavingWork.getLeaveStamp().get().getActualStamp().get()
										.getStampSourceInfo().value
								: null;
				String leaveActualStampLocationCode = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getActualStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getActualStamp().get().getLocationCode().isPresent())
								? "'" + timeLeavingWork.getLeaveStamp().get().getActualStamp().get().getLocationCode()
										.get().v() + "'"
								: null;

				// TimeLeavingWork - leaveStamp - stamp
				Integer leaveStampRoundingTime = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent())
								? timeLeavingWork.getLeaveStamp().get().getStamp().get().getAfterRoundingTime()
										.valueAsMinutes()
								: null;
				Integer leaveStampTime = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent())
								? timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeWithDay()
										.valueAsMinutes()
								: null;
				Integer leaveStampSource = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent())
								? timeLeavingWork.getLeaveStamp().get().getStamp().get().getStampSourceInfo().value
								: null;
				String leaveStampLocationCode = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp().get().getLocationCode().isPresent()) ? "'"
								+ timeLeavingWork.getLeaveStamp().get().getStamp().get().getLocationCode().get().v()
								+ "'" : null;

				// TimeLeavingWork - leaveStamp - numberOfReflectionStamp
				Integer leaveNumberReflec = timeLeavingWork.getLeaveStamp().isPresent()
						? timeLeavingWork.getLeaveStamp().get().getNumberOfReflectionStamp() : null;

				String insertTimeLeaving = "INSERT INTO KRCDT_TIME_LEAVING_WORK ( SID , WORK_NO , YMD , TIME_LEAVING_TYPE, ATD_ACTUAL_ROUDING_TIME_DAY, ATD_ACTUAL_TIME , ATD_ACTUAL_PLACE_CODE , "
						+ " ATD_ACTUAL_SOURCE_INFO, ATD_STAMP_ROUDING_TIME_DAY , ATD_STAMP_TIME , ATD_STAMP_PLACE_CODE, ATD_STAMP_SOURCE_INFO, ATD_NUMBER_STAMP, LWK_ACTUAL_ROUDING_TIME_DAY, "
						+ " LWK_ACTUAL_TIME, LWK_ACTUAL_PLACE_CODE , LWK_ACTUAL_SOURCE_INFO, LWK_STAMP_ROUDING_TIME_DAY, LWK_STAMP_TIME, LWK_STAMP_PLACE_CODE , LWK_STAMP_SOURCE_INFO, LWK_NUMBER_STAMP ) "
						+ "VALUES( '" + timeLeavingOfDailyPerformance.getEmployeeId() + "' , "
						+ timeLeavingWork.getWorkNo().v() + " , '" + timeLeavingOfDailyPerformance.getYmd() + "', " + 0
						+ ", " + attActualRoundingTime + ", " + attActualTime + ", " + attActualStampLocationCode
						+ " , " + attActualStampSource + ", " + attStampRoundingTime + ", " + attStampTime + ", "
						+ attStampLocationCode + " , " + attStampSource + ", " + attNumberReflec + ", "
						+ leaveActualRoundingTime + ", " + leaveActualTime + ", " + leaveActualStampLocationCode + " , "
						+ leaveActualStampSource + " , " + leaveStampRoundingTime + ", " + leaveStampTime + ", "
						+ leaveStampLocationCode + " , " + leaveStampSource + ", " + leaveNumberReflec + " )";
				statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertTimeLeaving));
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void add(TimeLeavingOfDailyPerformance timeLeaving) {
		KrcdtDaiLeavingWork entity = KrcdtDaiLeavingWork.toEntity(timeLeaving);
		commandProxy().insert(entity);
		commandProxy().insertAll(entity.timeLeavingWorks);
	}
	//
	// @Override
	// public void update(TimeLeavingOfDailyPerformance timeLeaving) {
	// KrcdtDaiLeavingWork entity = KrcdtDaiLeavingWork.toEntity(timeLeaving);
	// commandProxy().update(entity);
	// commandProxy().updateAll(entity.timeLeavingWorks);
	// }

	@Override
	public List<TimeLeavingOfDailyPerformance> finds(List<String> employeeIds, DatePeriod ymd) {
		List<Object[]> result = new ArrayList<>();
		StringBuilder query = new StringBuilder(
				"SELECT a, c from KrcdtDaiLeavingWork a LEFT JOIN a.timeLeavingWorks c ");
		query.append(" WHERE a.krcdtDaiLeavingWorkPK.employeeId IN :employeeId ");
		query.append(" AND a.krcdtDaiLeavingWorkPK.ymd <= :end AND a.krcdtDaiLeavingWorkPK.ymd >= :start");
		TypedQueryWrapper<Object[]> tQuery = this.queryProxy().query(query.toString(), Object[].class);
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(tQuery.setParameter("employeeId", empIds).setParameter("start", ymd.start())
					.setParameter("end", ymd.end()).getList());
		});
		return toDomainFromJoin(result);
	}

	@Override
	public void updateFlush(TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance) {
		this.update(timeLeavingOfDailyPerformance);
		this.getEntityManager().flush();
	}

	@Override
	public List<TimeLeavingOfDailyPerformance> finds(Map<String, List<GeneralDate>> param) {
		List<Object[]> result = new ArrayList<>();
		StringBuilder query = new StringBuilder(
				"SELECT a, c from KrcdtDaiLeavingWork a LEFT JOIN a.timeLeavingWorks c ");
		query.append(" WHERE a.krcdtDaiLeavingWorkPK.employeeId IN :employeeId ");
		query.append(" AND a.krcdtDaiLeavingWorkPK.ymd IN :date");
		TypedQueryWrapper<Object[]> tQuery = this.queryProxy().query(query.toString(), Object[].class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList().stream().filter(c -> {
						KrcdtDaiLeavingWork af = (KrcdtDaiLeavingWork) c[0];
						return p.get(af.krcdtDaiLeavingWorkPK.employeeId).contains(af.krcdtDaiLeavingWorkPK.ymd);
					}).collect(Collectors.toList()));
		});
		return toDomainFromJoin(result);
	}

	private List<TimeLeavingOfDailyPerformance> toDomainFromJoin(List<Object[]> result) {
		return result.stream()
				.collect(Collectors.groupingBy(c1 -> c1[0],
						Collectors.collectingAndThen(Collectors.toList(),
								list -> list.stream().filter(c -> c[1] != null).map(c -> (KrcdtTimeLeavingWork) c[1])
										.collect(Collectors.toList()))))
				.entrySet().stream()
				.map(e -> KrcdtDaiLeavingWork.toDomain((KrcdtDaiLeavingWork) e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteTimeNoBy(List<String> employeeIds, List<GeneralDate> processingYmds, int no) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE FROM KrcdtTimeLeavingWork a ");
		builderString.append("WHERE a.krcdtTimeLeavingWorkPK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtTimeLeavingWorkPK.ymd IN :processingYmds ");
		builderString.append("AND a.krcdtTimeLeavingWorkPK.workNo = :no ");
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstEmployeeIds -> {
			CollectionUtil.split(processingYmds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, ymds -> {
				this.getEntityManager().createQuery(builderString.toString())
					.setParameter("employeeIds", lstEmployeeIds)
					.setParameter("processingYmds", ymds)
					.setParameter("no", no)
					.executeUpdate();
			});
		});
	}
	
	@Override
	public void deleteScheStamp(List<String> employeeIds, List<GeneralDate> processingYmds) {
		
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstEmployeeIds -> {
			CollectionUtil.split(processingYmds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, ymds -> {
				internalUpdate(lstEmployeeIds, ymds);
			});
		});
	}

	@SneakyThrows
	private void internalUpdate(List<String> lstEmployeeIds, List<GeneralDate> ymds) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("UPDATE KRCDT_TIME_LEAVING_WORK a ");
		builderString.append(" SET ATD_STAMP_ROUDING_TIME_DAY = NULL, ATD_STAMP_TIME = NULL, ATD_STAMP_PLACE_CODE = NULL, ATD_STAMP_SOURCE_INFO = NULL");
		builderString.append(" SET LWK_STAMP_ROUDING_TIME_DAY = NULL, LWK_STAMP_TIME = NULL, LWK_STAMP_PLACE_CODE = NULL, LWK_STAMP_SOURCE_INFO = NULL");
		builderString.append(" WHERE SID IN ("+ lstEmployeeIds.stream().map(c -> "?").collect(Collectors.joining(", ")) +") ");
		builderString.append(" AND YMD IN (" + ymds.stream().map(c -> "?").collect(Collectors.joining(", ")) + ") ");
		builderString.append(" AND WORK_NO = 1 AND TIME_LEAVING_TYPE = 0");
		try (PreparedStatement ps = this.connection().prepareStatement(builderString.toString())) {
			for (int i = 0; i < lstEmployeeIds.size(); i++) {
				ps.setString(i + 1, lstEmployeeIds.get(i));
			}
			for (int i = 0; i < ymds.size(); i++) {
				ps.setDate(i + 1 + lstEmployeeIds.size(), Date.valueOf(ymds.get(i).localDate()));
			}
			ps.executeUpdate();
		}
	}

	@Override
	public void update(List<TimeLeavingOfDailyPerformance> domains) {
		List<KrcdtDaiLeavingWork> olds = finds(domains.stream().map(c -> c.getEmployeeId()).collect(Collectors.toList()), 
												domains.stream().map(c -> c.getYmd()).collect(Collectors.toList()));
		
		domains.stream().forEach(d -> {
			KrcdtDaiLeavingWork e = olds.stream().filter(o -> o.krcdtDaiLeavingWorkPK.employeeId.equals(d.getEmployeeId())
																&& o.krcdtDaiLeavingWorkPK.ymd.equals(d.getYmd()))
										.findFirst().orElseGet(() -> getDefault(d.getEmployeeId(), d.getYmd()));
			internalUpdate(d, e);
		});
	}
	
	private List<KrcdtDaiLeavingWork> finds(List<String> employeeIds, List<GeneralDate> ymds) {
		List<KrcdtDaiLeavingWork> result = new ArrayList<>();
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a FROM KrcdtDaiLeavingWork a");
		builderString.append(" WHERE a.krcdtDaiLeavingWorkPK.employeeId IN :employeeIds");
		builderString.append(" AND a.krcdtDaiLeavingWorkPK.ymd IN :processingYmds");
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistEmployeeIds -> {
			CollectionUtil.split(ymds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistYMDs -> {
				result.addAll(this.getEntityManager().createQuery(builderString.toString(), KrcdtDaiLeavingWork.class)
										.setParameter("employeeIds", sublistEmployeeIds)
										.setParameter("processingYmds", sublistYMDs).getResultList());
			});
		});
		
		return result;
	}

}
