package nts.uk.ctx.at.record.infra.repository.daily.attendanceleavinggate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.AttendanceLeavingGateOfDailyRepo;
import nts.uk.ctx.at.record.infra.entity.daily.attendanceleavinggate.KrcdtDayLeaveGate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class AttendanceLeavingGateOfDailyRepoImpl extends JpaRepository implements AttendanceLeavingGateOfDailyRepo {

//	private static final String REMOVE_BY_KEY;

	static {
//		StringBuilder builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDayLeaveGate a ");
//		builderString.append("WHERE a.id.sid = :employeeId ");
//		builderString.append("AND a.id.ymd = :ymd ");
//		REMOVE_BY_KEY = builderString.toString();
	}

	@Override
	public Optional<AttendanceLeavingGateOfDaily> find(String employeeId, GeneralDate baseDate) {
		List<AttendanceLeavingGate> alGate = findQuery(employeeId, baseDate).getList(c -> c.toDomain());
		if (!alGate.isEmpty()) {
			return Optional.of(new AttendanceLeavingGateOfDaily(employeeId, baseDate, alGate));
		}
		return Optional.empty();
	}

	@Override
	public List<AttendanceLeavingGateOfDaily> find(String employeeId, List<GeneralDate> baseDate) {
		if (baseDate.isEmpty()) {
			return Collections.emptyList();
		}
		List<AttendanceLeavingGateOfDaily> resultList = new ArrayList<>();
		CollectionUtil.split(baseDate, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(toList(this.queryProxy()
				.query("SELECT al FROM KrcdtDayLeaveGate al WHERE al.id.sid = :sid AND al.id.ymd IN :ymd", KrcdtDayLeaveGate.class)
					.setParameter("ymd", subList)
					.setParameter("sid", employeeId)
					.getList()
					.stream()));
		});
		return resultList;
	}

	@Override
	public List<AttendanceLeavingGateOfDaily> find(String employeeId) {
		return toList(this.queryProxy()
				.query("SELECT al FROM KrcdtDayLeaveGate al WHERE al.id.sid = :sid", KrcdtDayLeaveGate.class)
				.setParameter("sid", employeeId)
				.getList().stream());
	}

	@Override
	public List<AttendanceLeavingGateOfDaily> finds(List<String> employeeId, DatePeriod baseDate) {
		if (employeeId.isEmpty()) {
			return Collections.emptyList();
		}
		List<AttendanceLeavingGateOfDaily> result = new ArrayList<>();
		String query = "SELECT al FROM KrcdtDayLeaveGate al WHERE al.id.sid IN :sid AND al.id.ymd <= :end AND al.id.ymd >= :start";
		TypedQueryWrapper<KrcdtDayLeaveGate> tQuery=  this.queryProxy().query(query, KrcdtDayLeaveGate.class);
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(toList(tQuery.setParameter("sid", empIds)
										.setParameter("end", baseDate.end())
										.setParameter("start", baseDate.start())
										.getList().stream()));
		});
		return result;
	}

	@Override
	public List<AttendanceLeavingGateOfDaily> finds(Map<String, List<GeneralDate>> param) {
		if (param.isEmpty()) {
			return Collections.emptyList();
		}
		List<AttendanceLeavingGateOfDaily> result = new ArrayList<>();
		String query = "SELECT al FROM KrcdtDayLeaveGate al WHERE al.id.sid IN :sid AND al.id.ymd IN :date";
		TypedQueryWrapper<KrcdtDayLeaveGate> tQuery=  this.queryProxy().query(query, KrcdtDayLeaveGate.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(toList(tQuery.setParameter("sid", p.keySet())
										.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
										.getList().stream()
										.filter(c -> p.get(c.id.sid).contains(c.id.ymd))
										));
		});
		return result;
	}

	@Override
	public void update(AttendanceLeavingGateOfDaily domain) {
		List<KrcdtDayLeaveGate> entities = findQuery(domain.getEmployeeId(), domain.getYmd()).getList();
		if(entities.isEmpty()) {
			add(domain);
		} else {
			List<Integer> nos = domain.getAttendanceLeavingGates().stream().map(c -> c.getWorkNo().v()).collect(Collectors.toList());
			List<KrcdtDayLeaveGate> toDelete = entities.stream()
					.filter(c -> !nos.contains(c.id.alNo)).collect(Collectors.toList());
			this.commandProxy().removeAll(toDelete);
			domain.getAttendanceLeavingGates().stream().forEach(c -> {
				Optional<KrcdtDayLeaveGate> entityOp = entities.stream().filter(e -> e.id.alNo == c.getWorkNo().v())
																		.findFirst();
				if(entityOp.isPresent()) {
					KrcdtDayLeaveGate entity = entityOp.get();
					entity.setData(c);
					commandProxy().update(entity);
				} else {
					commandProxy().insert(KrcdtDayLeaveGate.from(domain.getEmployeeId(), domain.getYmd(), c));
				}
			});
		}
	}

	@Override
	public void add(AttendanceLeavingGateOfDaily domain) {
//		this.commandProxy().insertAll(KrcdtDayLeaveGate.from(domain));
		try {
			Connection con = this.getEntityManager().unwrap(Connection.class);
			Statement statementI = con.createStatement();
			for(AttendanceLeavingGate leavingGate : domain.getAttendanceLeavingGates()){
				
				// AttendanceLeavingGate - attendance
				String attPlaceCode = leavingGate.getAttendance().isPresent() && leavingGate.getAttendance().get().getLocationCode().isPresent()
						? "'" + leavingGate.getAttendance().get().getLocationCode().get().v() + "'" : null;
				Integer attStampSource = leavingGate.getAttendance().isPresent() ? leavingGate.getAttendance().get().getStampSourceInfo().value : null;
				Integer attTime = leavingGate.getAttendance().isPresent() ? leavingGate.getAttendance().get().getTimeWithDay().valueAsMinutes() : null;
				
				// AttendanceLeavingGate - leave
				String leavePlaceCode = leavingGate.getLeaving().isPresent() && leavingGate.getLeaving().get().getLocationCode().isPresent()
						? "'" + leavingGate.getLeaving().get().getLocationCode().get().v() + "'" : null;
				Integer leaveStampSource = leavingGate.getLeaving().isPresent() ? leavingGate.getLeaving().get().getStampSourceInfo().value : null;
				Integer leaveTime = leavingGate.getLeaving().isPresent() ? leavingGate.getLeaving().get().getTimeWithDay().valueAsMinutes() : null;
				String insertTableSQL = "INSERT INTO KRCDT_DAY_LEAVE_GATE ( SID , YMD , AL_NO, ATTENDANCE_PLACE_CODE , ATTENDANCE_STAMP_SOURCE , ATTENDANCE_TIME , LEAVE_PLACE_CODE , LEAVE_STAMP_SOURCE , LEAVE_TIME ) "
						+ "VALUES( '" + domain.getEmployeeId() + "' , '"
						+ domain.getYmd() + "' , "
						+ leavingGate.getWorkNo().v() + " , "
						+ attPlaceCode + " , "
						+ attStampSource + " , "
						+ attTime + " , "
						+ leavePlaceCode + " , "
						+ leaveStampSource + " , "
						+ leaveTime + " )";
				statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertTableSQL));
			}
		} catch (Exception e) {
			
		}
	}

	@Override
	public void remove(AttendanceLeavingGateOfDaily domain) {
		removeByKey(domain.getEmployeeId(), domain.getYmd());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void removeByKey(String employeeId, GeneralDate baseDate) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_DAY_LEAVE_GATE Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + baseDate + "'" ;
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
//		this.getEntityManager().createQuery(REMOVE_BY_KEY).setParameter("employeeId", employeeId)
//				.setParameter("ymd", baseDate).executeUpdate();
//		this.getEntityManager().flush();
	}
	


	private TypedQueryWrapper<KrcdtDayLeaveGate> findQuery(String employeeId, GeneralDate baseDate){
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDayLeaveGate a ");
		builderString.append("WHERE a.id.sid = :employeeId ");
		builderString.append("AND a.id.ymd = :ymd ");
		return this.queryProxy().query(builderString.toString(), KrcdtDayLeaveGate.class)
				.setParameter("employeeId", employeeId).setParameter("ymd", baseDate);
	}

	private List<AttendanceLeavingGateOfDaily> toList(Stream<KrcdtDayLeaveGate> query) {
		return query.collect(Collectors.groupingBy(c -> c.id.sid + c.id.ymd.toString(), Collectors.toList()))
				.entrySet().stream()
				.map(c -> new AttendanceLeavingGateOfDaily(c.getValue().get(0).id.sid, c.getValue().get(0).id.ymd,
						c.getValue().stream().map(pc -> pc.toDomain()).collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

}
