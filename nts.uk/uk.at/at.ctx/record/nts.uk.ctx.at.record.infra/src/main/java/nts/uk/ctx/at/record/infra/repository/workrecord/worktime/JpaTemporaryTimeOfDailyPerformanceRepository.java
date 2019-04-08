package nts.uk.ctx.at.record.infra.repository.workrecord.worktime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtDaiTemporaryTime;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtDaiTemporaryTimePK;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtTimeLeavingWork;
import nts.uk.ctx.at.record.infra.entity.worktime.KrcdtTimeLeavingWorkPK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class JpaTemporaryTimeOfDailyPerformanceRepository extends JpaRepository
		implements TemporaryTimeOfDailyPerformanceRepository {

//	private static final String REMOVE_BY_EMPLOYEE;
//	
//	private static final String REMOVE_TIME_LEAVING_WORK;
	
	private static final String DEL_BY_LIST_KEY;

	private static final String FIND_BY_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDaiTemporaryTime a ");
//		builderString.append("WHERE a.krcdtDaiTemporaryTimePK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtDaiTemporaryTimePK.ymd = :ymd ");
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
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiTemporaryTime a ");
		builderString.append("WHERE a.krcdtDaiTemporaryTimePK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDaiTemporaryTimePK.ymd IN :ymds ");
		DEL_BY_LIST_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiTemporaryTime a ");
		builderString.append("WHERE a.krcdtDaiTemporaryTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiTemporaryTimePK.ymd = :ymd ");
		FIND_BY_KEY = builderString.toString();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_TIME_LEAVING_WORK Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + ymd + "'" + "and TIME_LEAVING_TYPE = 1" ;
		String daiLeavingWorkQuery = "Delete From KRCDT_DAI_TEMPORARY_TIME Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + ymd + "'" ;
		try {
			con.createStatement().executeUpdate(sqlQuery);
			con.createStatement().executeUpdate(daiLeavingWorkQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
//		this.getEntityManager().createQuery(REMOVE_TIME_LEAVING_WORK).setParameter("employeeId", employeeId)
//				.setParameter("ymd", ymd).setParameter("timeLeavingType", 1).executeUpdate();
//		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEE).setParameter("employeeId", employeeId)
//				.setParameter("ymd", ymd).executeUpdate();
//		this.getEntityManager().flush();
	}

	@Override
	public void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> ymds) {
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstEmpIds -> {
			CollectionUtil.split(ymds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstYMD -> {
				this.getEntityManager().createQuery(DEL_BY_LIST_KEY)
					.setParameter("employeeIds", lstEmpIds)
					.setParameter("processingYmds", lstYMD).executeUpdate();	
			});
		});
		this.getEntityManager().flush();
	}

	@Override
	public Optional<TemporaryTimeOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd) {
		return this.queryProxy().query(FIND_BY_KEY, KrcdtDaiTemporaryTime.class).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).getSingle(f -> f.toDomain());
	}
	
	@Override
	public void update(TemporaryTimeOfDailyPerformance domain) {
		if(domain==null){
			return;
		}
		
		KrcdtDaiTemporaryTime krcdtDaiTemporaryTime = getDailyTemporary(domain.getEmployeeId(), domain.getYmd());
		
		List<KrcdtTimeLeavingWork> timeWorks = krcdtDaiTemporaryTime.timeLeavingWorks;
 		krcdtDaiTemporaryTime.workTimes = domain.getWorkTimes().v();
 		domain.getTimeLeavingWorks().stream().forEach(c -> {
 			KrcdtTimeLeavingWork krcdtTimeLeavingWork = timeWorks.stream()
 					.filter(x -> x.krcdtTimeLeavingWorkPK.workNo == c.getWorkNo().v()
 								&& x.krcdtTimeLeavingWorkPK.timeLeavingType == 1).findFirst().orElse(null);
 			boolean isNew = krcdtTimeLeavingWork == null;
 			if(isNew){
 				krcdtTimeLeavingWork = new KrcdtTimeLeavingWork();
 				krcdtTimeLeavingWork.krcdtTimeLeavingWorkPK = new KrcdtTimeLeavingWorkPK(domain.getEmployeeId(), c.getWorkNo().v(), domain.getYmd(), 1);
 			}
 			if(c.getAttendanceStamp().isPresent()){
 				TimeActualStamp attendanceStamp = c.getAttendanceStamp().get();
 				WorkStamp attendActualStamp = attendanceStamp.getActualStamp().orElse(null);
 				WorkStamp attendStamp = attendanceStamp.getStamp().orElse(null);
 				if(attendActualStamp != null){
 					krcdtTimeLeavingWork.attendanceActualRoudingTime = attendActualStamp.getAfterRoundingTime() == null 
 							? null : attendActualStamp.getAfterRoundingTime().valueAsMinutes();
 	 				krcdtTimeLeavingWork.attendanceActualTime = attendActualStamp.getTimeWithDay() == null 
 	 						? null : attendActualStamp.getTimeWithDay().valueAsMinutes();
 	 				krcdtTimeLeavingWork.attendanceActualPlaceCode = !attendActualStamp.getLocationCode().isPresent() ? null 
 	 						: attendActualStamp.getLocationCode().get().v();
 	 				krcdtTimeLeavingWork.attendanceActualSourceInfo = attendActualStamp.getStampSourceInfo() == null ? 0 
 	 						: attendActualStamp.getStampSourceInfo().value;
 				}
 				if(attendStamp != null){
 					krcdtTimeLeavingWork.attendanceStampRoudingTime = attendStamp.getAfterRoundingTime() == null 
 							? null : attendStamp.getAfterRoundingTime().valueAsMinutes();
 	 				krcdtTimeLeavingWork.attendanceStampTime= attendStamp.getTimeWithDay() == null 
 	 						? null : attendStamp.getTimeWithDay().valueAsMinutes();
 	 				krcdtTimeLeavingWork.attendanceStampPlaceCode = !attendStamp.getLocationCode().isPresent() ? null 
 	 						: attendStamp.getLocationCode().get().v();
 	 				krcdtTimeLeavingWork.attendanceStampSourceInfo = attendStamp.getStampSourceInfo() == null ? 0 
 	 						: attendStamp.getStampSourceInfo().value;
 				}
 				krcdtTimeLeavingWork.attendanceNumberStamp = attendanceStamp.getNumberOfReflectionStamp();
 			}
 			if(c.getLeaveStamp().isPresent()){
 				WorkStamp leaveActualStamp = c.getLeaveStamp().get().getActualStamp().orElse(null);
 				WorkStamp leaveStamp = c.getLeaveStamp().get().getStamp().orElse(null);
 				if(leaveActualStamp != null){
 					krcdtTimeLeavingWork.leaveWorkActualRoundingTime = leaveActualStamp.getAfterRoundingTime() == null 
 							? null : leaveActualStamp.getAfterRoundingTime().valueAsMinutes();
 	 				krcdtTimeLeavingWork.leaveWorkActualTime = leaveActualStamp.getTimeWithDay() == null 
 	 						? null : leaveActualStamp.getTimeWithDay().valueAsMinutes();
 	 				krcdtTimeLeavingWork.leaveWorkActualPlaceCode = !leaveActualStamp.getLocationCode().isPresent() ? null 
 	 						: leaveActualStamp.getLocationCode().get().v();
 	 				krcdtTimeLeavingWork.leaveActualSourceInfo = leaveActualStamp.getStampSourceInfo() == null ? 0 
 	 						: leaveActualStamp.getStampSourceInfo().value;
 				}
 				if(leaveStamp != null){
 					krcdtTimeLeavingWork.leaveWorkStampRoundingTime = leaveStamp.getAfterRoundingTime() == null 
 							? null : leaveStamp.getAfterRoundingTime().valueAsMinutes();
 	 				krcdtTimeLeavingWork.leaveWorkStampTime= leaveStamp.getTimeWithDay() == null 
 	 						? null : leaveStamp.getTimeWithDay().valueAsMinutes();
 	 				krcdtTimeLeavingWork.leaveWorkStampPlaceCode = !leaveStamp.getLocationCode().isPresent() ? null 
 	 						: leaveStamp.getLocationCode().get().v();
 	 				krcdtTimeLeavingWork.leaveWorkStampSourceInfo = leaveStamp.getStampSourceInfo() == null ? 0 
 	 						: leaveStamp.getStampSourceInfo().value;
 				}
 				krcdtTimeLeavingWork.leaveWorkNumberStamp = c.getLeaveStamp().get().getNumberOfReflectionStamp();
 			}
 			krcdtTimeLeavingWork.daiTemporaryTime = krcdtDaiTemporaryTime;
// 			krcdtTimeLeavingWork.krcdtTimeLeavingWorkPK.timeLeavingType = 1;
 			if(isNew){
 				timeWorks.add(krcdtTimeLeavingWork);
 			}
 		});
 		krcdtDaiTemporaryTime.timeLeavingWorks = timeWorks.isEmpty() ? null : timeWorks;
 		this.commandProxy().update(krcdtDaiTemporaryTime);
 		if(!timeWorks.isEmpty()){
 			this.commandProxy().updateAll(krcdtDaiTemporaryTime.timeLeavingWorks);
 		}
		this.getEntityManager().flush();
	 	
	}

	private KrcdtDaiTemporaryTime getDailyTemporary(String employee, GeneralDate date) {
		KrcdtDaiTemporaryTime krcdtDaiTemporaryTime = this.queryProxy().query(FIND_BY_KEY, KrcdtDaiTemporaryTime.class)
				.setParameter("employeeId", employee)
			.setParameter("ymd", date).getSingle().orElse(null);
		if(krcdtDaiTemporaryTime == null){
			krcdtDaiTemporaryTime = new KrcdtDaiTemporaryTime();
			krcdtDaiTemporaryTime.krcdtDaiTemporaryTimePK = new KrcdtDaiTemporaryTimePK(employee, date);
			krcdtDaiTemporaryTime.timeLeavingWorks = new ArrayList<>();
		}
		return krcdtDaiTemporaryTime;
	}

	@Override
	public void insert(TemporaryTimeOfDailyPerformance temporaryTimeOfDailyPerformance) {
		if(temporaryTimeOfDailyPerformance==null){
			return;
		}
		this.commandProxy().insert(KrcdtDaiTemporaryTime.toEntity(temporaryTimeOfDailyPerformance));
		this.getEntityManager().flush();
	}

	@Override
	public void add(TemporaryTimeOfDailyPerformance temporaryTime) {
		KrcdtDaiTemporaryTime entity = KrcdtDaiTemporaryTime.toEntity(temporaryTime);
		commandProxy().insert(entity);
		commandProxy().insertAll(entity.timeLeavingWorks);
	}

	@Override
	public List<TemporaryTimeOfDailyPerformance> findbyPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM KrcdtDaiTemporaryTime a ");
		query.append("WHERE a.krcdtDaiTemporaryTimePK.employeeId = :employeeId ");
		query.append("AND a.krcdtDaiTemporaryTimePK.ymd >= :start ");
		query.append("AND a.krcdtDaiTemporaryTimePK.ymd <= :end ");
		query.append("ORDER BY a.krcdtDaiTemporaryTimePK.ymd ");
		return queryProxy().query(query.toString(), KrcdtDaiTemporaryTime.class)
				.setParameter("employeeId", employeeId)
				.setParameter("start", datePeriod.start())
				.setParameter("end", datePeriod.end())
				.getList().stream()
				.map(f -> f.toDomain()).collect(Collectors.toList());
	}
	
	@Override
	public List<TemporaryTimeOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd) {
		List<Object[]> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT a, c from KrcdtDaiTemporaryTime a LEFT JOIN a.timeLeavingWorks c ");
		query.append(" WHERE a.krcdtDaiTemporaryTimePK.employeeId IN :employeeId ");
		query.append(" AND a.krcdtDaiTemporaryTimePK.ymd <= :end AND a.krcdtDaiTemporaryTimePK.ymd >= :start");
		TypedQueryWrapper<Object[]> tQuery=  this.queryProxy().query(query.toString(), Object[].class);
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(tQuery.setParameter("employeeId", empIds)
					.setParameter("start", ymd.start())
					.setParameter("end", ymd.end())
					.getList());
		});
		return toDomainFromJoin(result);
	}
	
	@Override
	public List<TemporaryTimeOfDailyPerformance> finds(Map<String, List<GeneralDate>> param) {
		List<Object[]> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT a, c from KrcdtDaiTemporaryTime a LEFT JOIN a.timeLeavingWorks c ");
		query.append(" WHERE a.krcdtDaiTemporaryTimePK.employeeId IN :employeeId ");
		query.append(" AND a.krcdtDaiTemporaryTimePK.ymd IN :date");
		TypedQueryWrapper<Object[]> tQuery=  this.queryProxy().query(query.toString(), Object[].class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList().stream()
					.filter(c -> {
						KrcdtDaiTemporaryTime af = (KrcdtDaiTemporaryTime) c[0];
						return p.get(af.krcdtDaiTemporaryTimePK.employeeId).contains(af.krcdtDaiTemporaryTimePK.ymd);
					}).collect(Collectors.toList()));
		});
		return toDomainFromJoin(result);
	}

	private List<TemporaryTimeOfDailyPerformance> toDomainFromJoin(List<Object[]> result) {
		return result.stream().collect(Collectors.groupingBy(c1 -> c1[0], Collectors.collectingAndThen(Collectors.toList(), 
					list -> list.stream().filter(c -> c[1] != null).map(c -> (KrcdtTimeLeavingWork) c[1]).collect(Collectors.toList()))))
				.entrySet().stream().map(e -> KrcdtDaiTemporaryTime.toDomain((KrcdtDaiTemporaryTime) e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}

//	@Override
//	public void update(TemporaryTimeOfDailyPerformance temporaryTime) {
//		KrcdtDaiTemporaryTime entity = KrcdtDaiTemporaryTime.toEntity(temporaryTime);
//		commandProxy().update(entity);
//		commandProxy().updateAll(entity.timeLeavingWorks);
//	}

}
