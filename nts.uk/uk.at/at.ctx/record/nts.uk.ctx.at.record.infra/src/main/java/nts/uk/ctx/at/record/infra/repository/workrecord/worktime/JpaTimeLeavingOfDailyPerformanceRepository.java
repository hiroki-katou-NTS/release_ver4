package nts.uk.ctx.at.record.infra.repository.workrecord.worktime;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtDaiLeavingWork;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtDaiLeavingWorkPK;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtTimeLeavingWork;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtTimeLeavingWorkPK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

@Stateless
public class JpaTimeLeavingOfDailyPerformanceRepository extends JpaRepository
		implements TimeLeavingOfDailyPerformanceRepository {

	private static final String REMOVE_BY_EMPLOYEE;

	private static final String REMOVE_TIME_LEAVING_WORK;

	private static final String FIND_BY_KEY;

	private static final String FIND_BY_PERIOD_ORDER_BY_YMD;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiLeavingWork a ");
		builderString.append("WHERE a.krcdtDaiLeavingWorkPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiLeavingWorkPK.ymd = :ymd ");
		REMOVE_BY_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtTimeLeavingWork a ");
		builderString.append("WHERE a.krcdtTimeLeavingWorkPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtTimeLeavingWorkPK.ymd = :ymd ");
		builderString.append("AND a.krcdtTimeLeavingWorkPK.timeLeavingType = :timeLeavingType ");
		REMOVE_TIME_LEAVING_WORK = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiLeavingWork a ");
		builderString.append("WHERE a.krcdtDaiLeavingWorkPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiLeavingWorkPK.ymd = :ymd ");
		FIND_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiLeavingWork a ");
		builderString.append("WHERE a.krcdtDaiLeavingWorkPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiLeavingWorkPK.ymd >= :start ");
		builderString.append("AND a.krcdtDaiLeavingWorkPK.ymd <= :end ");
		builderString.append("ORDER BY a.krcdtDaiLeavingWorkPK.ymd ");
		FIND_BY_PERIOD_ORDER_BY_YMD = builderString.toString();
	}

	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		this.getEntityManager().createQuery(REMOVE_TIME_LEAVING_WORK).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).setParameter("timeLeavingType", 0).executeUpdate();
		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEE).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).executeUpdate();
		this.getEntityManager().flush();
	}

	@Override
	public Optional<TimeLeavingOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd) {
		return this.queryProxy().query(FIND_BY_KEY, KrcdtDaiLeavingWork.class).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).getSingle(f -> f.toDomain());
	}

	@Override
	public List<TimeLeavingOfDailyPerformance> findbyPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {
		return this.queryProxy().query(FIND_BY_PERIOD_ORDER_BY_YMD, KrcdtDaiLeavingWork.class)
				.setParameter("employeeId", employeeId).setParameter("start", datePeriod.start())
				.setParameter("end", datePeriod.end()).getList(f -> f.toDomain());
	}

	@Override
	public void update(TimeLeavingOfDailyPerformance domain) {
		if (domain == null) {
			return;
		}
		KrcdtDaiLeavingWork entity = getDailyLeaving(domain.getEmployeeId(), domain.getYmd());
		List<KrcdtTimeLeavingWork> timeWorks = entity.timeLeavingWorks;
		entity.workTimes = domain.getWorkTimes() == null ? null : domain.getWorkTimes().v();
		domain.getTimeLeavingWorks().stream().forEach(c -> {
			KrcdtTimeLeavingWork krcdtTimeLeavingWork = timeWorks.stream()
					.filter(x -> x.krcdtTimeLeavingWorkPK.workNo == c.getWorkNo().v()
								&& x.krcdtTimeLeavingWorkPK.timeLeavingType == 0).findFirst().orElse(null);
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
//			krcdtTimeLeavingWork.krcdtTimeLeavingWorkPK.timeLeavingType = 0;
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
		this.getEntityManager().flush();
	}

	private KrcdtDaiLeavingWork getDailyLeaving(String employee, GeneralDate date) {
		KrcdtDaiLeavingWork krcdtDaiTemporaryTime = this.queryProxy().query(FIND_BY_KEY, KrcdtDaiLeavingWork.class)
				.setParameter("employeeId", employee).setParameter("ymd", date).getSingle().orElse(null);
		if (krcdtDaiTemporaryTime == null) {
			krcdtDaiTemporaryTime = new KrcdtDaiLeavingWork();
			krcdtDaiTemporaryTime.krcdtDaiLeavingWorkPK = new KrcdtDaiLeavingWorkPK(employee, date);
			krcdtDaiTemporaryTime.timeLeavingWorks = new ArrayList<>();
		}
		return krcdtDaiTemporaryTime;
	}

	@Override
	public void insert(TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance) {
		if (timeLeavingOfDailyPerformance == null) {
			return;
		}
//		 this.commandProxy().insert(KrcdtDaiLeavingWork.toEntity(timeLeavingOfDailyPerformance));
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
								? timeLeavingWork.getAttendanceStamp().get().getStamp().get()
										.getAfterRoundingTime().valueAsMinutes()
								: null;				
				Integer attStampTime = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent())
								? timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay()
										.valueAsMinutes()
								: null;
				Integer attStampSource = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent())
								? timeLeavingWork.getAttendanceStamp().get().getStamp().get()
										.getStampSourceInfo().value
								: null;
				String attStampLocationCode = (timeLeavingWork.getAttendanceStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent()
						&& timeLeavingWork.getAttendanceStamp().get().getStamp().get().getLocationCode()
								.isPresent())
										? "'" + timeLeavingWork.getAttendanceStamp().get().getStamp().get()
												.getLocationCode().get().v() + "'"
										: null;
				// TimeLeavingWork - leaveStamp - numberOfReflectionStamp
				Integer attNumberReflec = timeLeavingWork.getAttendanceStamp().isPresent()
										? timeLeavingWork.getAttendanceStamp().get().getNumberOfReflectionStamp()
										: null;
												
				// TimeLeavingWork - leaveStamp - actualStamp
				Integer leaveActualRoundingTime = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getActualStamp().isPresent())
								? timeLeavingWork.getLeaveStamp().get().getActualStamp().get()
										.getAfterRoundingTime().valueAsMinutes()
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
						&& timeLeavingWork.getLeaveStamp().get().getActualStamp().get().getLocationCode()
								.isPresent())
										? "'" + timeLeavingWork.getLeaveStamp().get().getActualStamp().get()
												.getLocationCode().get().v() + "'"
										: null;			
												
				// TimeLeavingWork - leaveStamp - stamp							
				Integer leaveStampRoundingTime = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent())
								? timeLeavingWork.getLeaveStamp().get().getStamp().get()
										.getAfterRoundingTime().valueAsMinutes()
								: null;				
				Integer leaveStampTime = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent())
								? timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeWithDay()
										.valueAsMinutes()
								: null;
				Integer leaveStampSource = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent())
								? timeLeavingWork.getLeaveStamp().get().getStamp().get()
										.getStampSourceInfo().value
								: null;
				String leaveStampLocationCode = (timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp().get().getLocationCode()
								.isPresent())
										? "'" + timeLeavingWork.getLeaveStamp().get().getStamp().get()
												.getLocationCode().get().v() + "'"
										: null;
												
				// TimeLeavingWork - leaveStamp - numberOfReflectionStamp
				Integer leaveNumberReflec = timeLeavingWork.getLeaveStamp().isPresent()
										? timeLeavingWork.getLeaveStamp().get().getNumberOfReflectionStamp()
										: null;
										
				String insertTimeLeaving = "INSERT INTO KRCDT_TIME_LEAVING_WORK ( SID , WORK_NO , YMD , TIME_LEAVING_TYPE, ATD_ACTUAL_ROUDING_TIME_DAY, ATD_ACTUAL_TIME , ATD_ACTUAL_PLACE_CODE , "
						+ " ATD_ACTUAL_SOURCE_INFO, ATD_STAMP_ROUDING_TIME_DAY , ATD_STAMP_TIME , ATD_STAMP_PLACE_CODE, ATD_STAMP_SOURCE_INFO, ATD_NUMBER_STAMP, LWK_ACTUAL_ROUDING_TIME_DAY, "
						+ " LWK_ACTUAL_TIME, LWK_ACTUAL_PLACE_CODE , LWK_ACTUAL_SOURCE_INFO, LWK_STAMP_ROUDING_TIME_DAY, LWK_STAMP_TIME, LWK_STAMP_PLACE_CODE , LWK_STAMP_SOURCE_INFO, LWK_NUMBER_STAMP ) "
						+ "VALUES( '" + timeLeavingOfDailyPerformance.getEmployeeId() + "' , "
						+ timeLeavingWork.getWorkNo().v() + " , '"
						+ timeLeavingOfDailyPerformance.getYmd() + "', "
						+ 0 + ", "
						+ attActualRoundingTime + ", "
						+ attActualTime + ", "
						+ attActualStampLocationCode + " , "
						+ attActualStampSource + ", "
						+ attStampRoundingTime + ", "
						+ attStampTime + ", "
						+ attStampLocationCode + " , "
						+ attStampSource + ", "
						+ attNumberReflec + ", "
						+ leaveActualRoundingTime + ", "
						+ leaveActualTime + ", "
						+ leaveActualStampLocationCode + " , "
						+ leaveActualStampSource + " , "
						+ leaveStampRoundingTime + ", "
						+ leaveStampTime + ", "
						+ leaveStampLocationCode + " , "
						+ leaveStampSource + ", "
						+ leaveNumberReflec + " )";
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
		StringBuilder query = new StringBuilder("SELECT a, c from KrcdtDaiLeavingWork a LEFT JOIN a.timeLeavingWorks c ");
		query.append(" WHERE a.krcdtDaiLeavingWorkPK.employeeId IN :employeeId ");
		query.append(" AND a.krcdtDaiLeavingWorkPK.ymd <= :end AND a.krcdtDaiLeavingWorkPK.ymd >= :start");
		TypedQueryWrapper<Object[]> tQuery=  this.queryProxy().query(query.toString(), Object[].class);
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(tQuery.setParameter("employeeId", empIds)
							.setParameter("start", ymd.start())
							.setParameter("end", ymd.end())
							.getList());
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
		StringBuilder query = new StringBuilder("SELECT a, c from KrcdtDaiLeavingWork a LEFT JOIN a.timeLeavingWorks c ");
		query.append(" WHERE a.krcdtDaiLeavingWorkPK.employeeId IN :employeeId ");
		query.append(" AND a.krcdtDaiLeavingWorkPK.ymd IN :date");
		TypedQueryWrapper<Object[]> tQuery=  this.queryProxy().query(query.toString(), Object[].class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
							.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
							.getList().stream()
							.filter(c -> {
								KrcdtDaiLeavingWork af = (KrcdtDaiLeavingWork) c[0];
								return p.get(af.krcdtDaiLeavingWorkPK.employeeId).contains(af.krcdtDaiLeavingWorkPK.ymd);
							}).collect(Collectors.toList()));
		});
		return toDomainFromJoin(result);
	}

	private List<TimeLeavingOfDailyPerformance> toDomainFromJoin(List<Object[]> result) {
		return result.stream().collect(Collectors.groupingBy(c1 -> c1[0], Collectors.collectingAndThen(Collectors.toList(), 
					list -> list.stream().filter(c -> c[1] != null).map(c -> (KrcdtTimeLeavingWork) c[1]).collect(Collectors.toList()))))
				.entrySet().stream().map(e -> KrcdtDaiLeavingWork.toDomain((KrcdtDaiLeavingWork) e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}

}
