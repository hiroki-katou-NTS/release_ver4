package nts.uk.ctx.at.record.infra.repository.breakorgoout;

import java.sql.Connection;
import java.sql.Date;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.infra.entity.breakorgoout.KrcdtDaiBreakTime;
import nts.uk.ctx.at.record.infra.entity.breakorgoout.KrcdtDaiBreakTimePK;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class JpaBreakTimeOfDailyPerformanceRepository extends JpaRepository
		implements BreakTimeOfDailyPerformanceRepository {

//	private static final String REMOVE_BY_EMPLOYEE;

	private static final String DEL_BY_LIST_KEY;

	private static final String SELECT_BY_EMPLOYEE_AND_DATE;

	private static final String FIND;

	private static final String REMOVE_BY_BREAKTYPE;

	static {
		StringBuilder builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDaiBreakTime a ");
//		builderString.append("WHERE a.krcdtDaiBreakTimePK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtDaiBreakTimePK.ymd = :ymd ");
//		REMOVE_BY_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiBreakTime a ");
		builderString.append("WHERE a.krcdtDaiBreakTimePK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDaiBreakTimePK.ymd IN :ymds ");
		DEL_BY_LIST_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiBreakTime a ");
		builderString.append("WHERE a.krcdtDaiBreakTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiBreakTimePK.ymd = :ymd ");
		SELECT_BY_EMPLOYEE_AND_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiBreakTime a ");
		builderString.append("WHERE a.krcdtDaiBreakTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiBreakTimePK.ymd = :ymd ");
		builderString.append("AND a.krcdtDaiBreakTimePK.breakType = :breakType ");
		FIND = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiBreakTime a ");
		builderString.append("WHERE a.krcdtDaiBreakTimePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiBreakTimePK.ymd = :ymd ");
		builderString.append("AND a.krcdtDaiBreakTimePK.breakType = :breakType ");
		REMOVE_BY_BREAKTYPE = builderString.toString();
	}

	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		try (val statement = this.connection().prepareStatement(
					"delete from KRCDT_DAI_BREAK_TIME_TS where SID = ? and YMD = ?")) {
			statement.setString(1, employeeId);
			statement.setDate(2, Date.valueOf(ymd.toLocalDate()));
			statement.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> ymds) {
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistEmployeeIds -> {
			CollectionUtil.split(ymds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistYMDs -> {
				this.getEntityManager().createQuery(DEL_BY_LIST_KEY)
					.setParameter("employeeIds", sublistEmployeeIds)
					.setParameter("ymds", sublistYMDs)
					.executeUpdate();
			});
		});
		this.getEntityManager().flush();
	}
	
	@Override
	public void deleteRecord1And2By(List<String> employeeIds, List<GeneralDate> ymds) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDaiBreakTime a ");
		builderString.append("WHERE a.krcdtDaiBreakTimePK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDaiBreakTimePK.ymd IN :ymds ");
		builderString.append("AND a.krcdtDaiBreakTimePK.breakType = 0 ");
		builderString.append("AND a.krcdtDaiBreakTimePK.breakFrameNo = 1 OR  a.krcdtDaiBreakTimePK.breakFrameNo = 2");
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistEmployeeIds -> {
			CollectionUtil.split(ymds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistYMDs -> {
				this.getEntityManager().createQuery(builderString.toString())
					.setParameter("employeeIds", sublistEmployeeIds)
					.setParameter("ymds", sublistYMDs)
					.executeUpdate();
			});
		});
	}

	@Override
	public List<BreakTimeOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd) {
		List<KrcdtDaiBreakTime> krcdtDaiBreakTimes = findEntities(employeeId, ymd);
		
		if (krcdtDaiBreakTimes == null || krcdtDaiBreakTimes.isEmpty()) {
			return new ArrayList<>();
		}
		return group(krcdtDaiBreakTimes);
	}

	@SneakyThrows
	private List<KrcdtDaiBreakTime> findEntities(String employeeId, GeneralDate ymd) {
		List<KrcdtDaiBreakTime> krcdtDaiBreakTimes = null; 

		try (val statement = this.connection().prepareStatement("select * FROM KRCDT_DAI_BREAK_TIME_TS where SID = ? and YMD = ?")) {
			statement.setString(1, employeeId);
			statement.setDate(2, Date.valueOf(ymd.toLocalDate()));
			krcdtDaiBreakTimes = new NtsResultSet(statement.executeQuery()).getList(rec -> {
				val entity = new KrcdtDaiBreakTime();
				entity.krcdtDaiBreakTimePK = new KrcdtDaiBreakTimePK();
				entity.krcdtDaiBreakTimePK.employeeId = rec.getString("SID");
				entity.krcdtDaiBreakTimePK.ymd = rec.getGeneralDate("YMD");
				entity.krcdtDaiBreakTimePK.breakType = rec.getInt("BREAK_TYPE");
				entity.krcdtDaiBreakTimePK.breakFrameNo = rec.getInt("BREAK_FRAME_NO");
				entity.startStampTime = rec.getInt("STR_STAMP_TIME");
				entity.endStampTime = rec.getInt("END_STAMP_TIME");
				return entity;
			});
		}

		if (krcdtDaiBreakTimes == null || krcdtDaiBreakTimes.isEmpty()) {
			return new ArrayList<>();
		}
		
		return krcdtDaiBreakTimes;
	}

	private List<BreakTimeOfDailyPerformance> group(List<KrcdtDaiBreakTime> krcdtDaiBreakTimes) {
		return krcdtDaiBreakTimes.stream().collect(Collectors.groupingBy(item -> item.krcdtDaiBreakTimePK.breakType))
				.entrySet().stream().map(c -> toDtomain(c.getKey(), c.getValue())).collect(Collectors.toList());
	}

	private BreakTimeOfDailyPerformance toDtomain(Integer type, List<KrcdtDaiBreakTime> value) {
		return new BreakTimeOfDailyPerformance(value.get(0).krcdtDaiBreakTimePK.employeeId,
				EnumAdaptor.valueOf(type, BreakType.class),
				value.stream()
						.map(item -> new BreakTimeSheet(new BreakFrameNo(item.krcdtDaiBreakTimePK.breakFrameNo),
								toTimeWithDayAttr(item.startStampTime), toTimeWithDayAttr(item.endStampTime),
								new AttendanceTime(0)))
						.collect(Collectors.toList()),
				value.get(0).krcdtDaiBreakTimePK.ymd);
	}

	private TimeWithDayAttr toTimeWithDayAttr(Integer time) {
		return time == null ? null : new TimeWithDayAttr(time);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void insert(BreakTimeOfDailyPerformance breakTimes) {
//		commandProxy().insertAll(KrcdtDaiBreakTime.toEntity(breakTimes));
//		this.getEntityManager().flush();
		Connection con = this.getEntityManager().unwrap(Connection.class);
		try {
			Statement statementI = con.createStatement();
			for(BreakTimeSheet breakTimeSheet : breakTimes.getBreakTimeSheets()){
				String insertTableSQL = "INSERT INTO KRCDT_DAI_BREAK_TIME_TS ( SID , YMD , BREAK_TYPE, BREAK_FRAME_NO , STR_STAMP_TIME , END_STAMP_TIME ) "
						+ "VALUES( '" + breakTimes.getEmployeeId() + "' , '"
						+ breakTimes.getYmd() + "' , "
						+ breakTimes.getBreakType().value + " , "
						+ breakTimeSheet.getBreakFrameNo().v() + " , "
						+ breakTimeSheet.getStartTime().valueAsMinutes() + " , "
						+ breakTimeSheet.getEndTime().valueAsMinutes() + " )";
				statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertTableSQL));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void insert(List<BreakTimeOfDailyPerformance> breakTimes) {
		commandProxy().insertAll(breakTimes.stream().map(c -> KrcdtDaiBreakTime.toEntity(c)).flatMap(List::stream)
				.collect(Collectors.toList()));
		this.getEntityManager().flush();
	}

	@Override
	public void update(BreakTimeOfDailyPerformance breakTimes) {
		if(breakTimes == null){ return;}

		update(Arrays.asList(breakTimes));
//		Connection con = this.getEntityManager().unwrap(Connection.class);
//		try {
//			for(BreakTimeSheet breakTimeSheet : breakTimes.getBreakTimeSheets()){
//				int result = 0;
//				String selectTableSQL = "SELECT COUNT(*) from KRCDT_DAI_BREAK_TIME_TS WHERE SID = '"
//						+ breakTimes.getEmployeeId() + "' AND YMD = '" + breakTimes.getYmd() + "'" + " AND BREAK_TYPE = " 
//						+ breakTimes.getBreakType().value 
//						+ " AND BREAK_FRAME_NO = " + breakTimeSheet.getBreakFrameNo().v();
//				Statement statement = con.createStatement();
//				ResultSet rs = statement.executeQuery(selectTableSQL);
//				while (rs.next()) {
//				    result = rs.getInt("COUNT(*)");
//				}
//				if (result > 0) {
//					String insertTableSQL = "INSERT INTO KRCDT_DAI_BREAK_TIME_TS ( SID , YMD , BREAK_TYPE, BREAK_FRAME_NO , STR_STAMP_TIME , END_STAMP_TIME ) "
//							+ "VALUES( '" + breakTimes.getEmployeeId() + "' , '"
//							+ breakTimes.getYmd() + "' , "
//							+ breakTimes.getBreakType().value + " , "
//							+ breakTimeSheet.getBreakFrameNo().v() + " , "
//							+ breakTimeSheet.getStartTime().valueAsMinutes() + " , "
//							+ breakTimeSheet.getEndTime().valueAsMinutes() + " )";
//					Statement statementI = con.createStatement();
//					statementI.executeUpdate(insertTableSQL);
//				} else {
//					String updateTableSQL = " UPDATE KRCDT_DAI_BREAK_TIME_TS SET STR_STAMP_TIME = "
//							+ breakTimeSheet.getStartTime().valueAsMinutes() + " AND END_STAMP_TIME = " + breakTimeSheet.getEndTime().valueAsMinutes()
//							+ " WHERE SID = '"
//							+ breakTimes.getEmployeeId() + "' AND YMD = '" + breakTimes.getYmd() + "'" + " AND BREAK_TYPE = " 
//							+ breakTimes.getBreakType().value 
//							+ " AND BREAK_FRAME_NO = " + breakTimeSheet.getBreakFrameNo().v();
//					Statement statementU = con.createStatement();
//					statementU.executeUpdate(updateTableSQL);
//				}
//			}
//		} catch (Exception e) {
//			
//		}
	}

	@Override
	public void update(List<BreakTimeOfDailyPerformance> breakTimes) {
		List<KrcdtDaiBreakTime> all = breakTimes.stream().map(c -> KrcdtDaiBreakTime.toEntity(c)).flatMap(List::stream)
				.collect(Collectors.toList());
		if (!all.isEmpty()) {
			List<KrcdtDaiBreakTime> krcdtDaiBreakTimes = this.queryProxy().query(SELECT_BY_EMPLOYEE_AND_DATE, KrcdtDaiBreakTime.class)
																			.setParameter("employeeId", breakTimes.get(0).getEmployeeId())
																			.setParameter("ymd", breakTimes.get(0).getYmd())
																			.getList();
			internalUpdate(all, krcdtDaiBreakTimes);
//			commandProxy().updateAll(toRemove);
//			commandProxy().removeAll(toRemove);
			// commandProxy().removeAll(toRemove);
		} else {
			this.delete(breakTimes.get(0).getEmployeeId(), breakTimes.get(0).getYmd());
		}
		// commandProxy().updateAll(breakTimes.stream().map(c ->
		// KrcdtDaiBreakTime.toEntity(c)).flatMap(List::stream)
		// .collect(Collectors.toList()));
//		this.getEntityManager().flush();
	}

	private void internalUpdate(List<KrcdtDaiBreakTime> all, List<KrcdtDaiBreakTime> krcdtDaiBreakTimes) {
		List<KrcdtDaiBreakTime> toUpdate = all.stream()
				.filter(c -> c.endStampTime != null && c.startStampTime != null).collect(Collectors.toList());
		List<KrcdtDaiBreakTime> toRemove = krcdtDaiBreakTimes.stream()
				.filter(c -> !toUpdate.stream().filter(tu -> tu.krcdtDaiBreakTimePK.breakFrameNo == c.krcdtDaiBreakTimePK.breakFrameNo
															&& tu.krcdtDaiBreakTimePK.breakType == c.krcdtDaiBreakTimePK.breakType)
									.findFirst().isPresent())
				.collect(Collectors.toList());
		
//			toRemove.stream().forEach(c -> {
//				commandProxy().remove(getEntityManager().merge(c));
//			});
		commandProxy().updateAll(toUpdate);
		toRemove.stream().forEach(c -> {
			commandProxy().remove(c);
		});
	}
	
	@Override
	public void updateV2(List<BreakTimeOfDailyPerformance> breakTimes) {
		List<KrcdtDaiBreakTime> all = breakTimes.stream().map(c -> KrcdtDaiBreakTime.toEntity(c)).flatMap(List::stream)
				.collect(Collectors.toList());
		if (!all.isEmpty()) {
			commandProxy().updateAll(all);
		}
	}

	@Override
	public List<BreakTimeOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd) {
		List<BreakTimeOfDailyPerformance> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT a FROM KrcdtDaiBreakTime a ");
		query.append("WHERE a.krcdtDaiBreakTimePK.employeeId IN :employeeId ");
		query.append("AND a.krcdtDaiBreakTimePK.ymd <= :end AND a.krcdtDaiBreakTimePK.ymd >= :start ");
		TypedQueryWrapper<KrcdtDaiBreakTime> tQuery = this.queryProxy().query(query.toString(), KrcdtDaiBreakTime.class);
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(tQuery.setParameter("employeeId", empIds)
					.setParameter("end", ymd.end())
					.setParameter("start", ymd.start()).getList().stream()
					.collect(Collectors.groupingBy(c -> c.krcdtDaiBreakTimePK.employeeId + c.krcdtDaiBreakTimePK.ymd.toString()))
					.entrySet().stream().map(c -> group(c.getValue())).flatMap(List::stream).collect(Collectors.toList()));
		});
		return result;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public Optional<BreakTimeOfDailyPerformance> find(String employeeId, GeneralDate ymd, int breakType) {

		List<KrcdtDaiBreakTime> krcdtDaiBreakTimes = this.queryProxy().query(FIND, KrcdtDaiBreakTime.class)
				.setParameter("employeeId", employeeId).setParameter("ymd", ymd).setParameter("breakType", breakType)
				.getList();
		if (krcdtDaiBreakTimes == null || krcdtDaiBreakTimes.isEmpty()) {
			return Optional.empty();
		}

		return Optional.ofNullable(group(krcdtDaiBreakTimes).get(0));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void deleteByBreakType(String employeeId, GeneralDate ymd, int breakType) {
		this.getEntityManager().createQuery(REMOVE_BY_BREAKTYPE).setParameter("employeeId", employeeId)
				.setParameter("ymd", ymd).setParameter("breakType", breakType).executeUpdate();
		this.getEntityManager().flush();
	}

	@Override
	public List<BreakTimeOfDailyPerformance> finds(Map<String, List<GeneralDate>> param) {
		List<BreakTimeOfDailyPerformance> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT a FROM KrcdtDaiBreakTime a ");
		query.append("WHERE a.krcdtDaiBreakTimePK.employeeId IN :employeeId ");
		query.append("AND a.krcdtDaiBreakTimePK.ymd IN :date");
		TypedQueryWrapper<KrcdtDaiBreakTime> tQuery = this.queryProxy().query(query.toString(), KrcdtDaiBreakTime.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList().stream()
					.filter(c -> p.get(c.krcdtDaiBreakTimePK.employeeId).contains(c.krcdtDaiBreakTimePK.ymd))
					.collect(Collectors.groupingBy(c -> c.krcdtDaiBreakTimePK.employeeId + c.krcdtDaiBreakTimePK.ymd.toString()))
					.entrySet().stream().map(c -> group(c.getValue())).flatMap(List::stream).collect(Collectors.toList()));
		});
		return result;
	}

	@Override
	public void updateForEachOfType(BreakTimeOfDailyPerformance breakTime) {
		Connection con = this.getEntityManager().unwrap(Connection.class);
		try {
			for(BreakTimeSheet breakTimeSheet : breakTime.getBreakTimeSheets()){
			
				String updateTableSQL = " UPDATE KRCDT_DAI_BREAK_TIME_TS SET STR_STAMP_TIME = "
						+ breakTimeSheet.getStartTime().valueAsMinutes() + " AND END_STAMP_TIME = " + breakTimeSheet.getEndTime().valueAsMinutes()
						+ " WHERE SID = '"
						+ breakTime.getEmployeeId() + "' AND YMD = '" + breakTime.getYmd() + "'" + " AND BREAK_TYPE = " 
						+ breakTime.getBreakType().value 
						+ " AND BREAK_FRAME_NO = " + breakTimeSheet.getBreakFrameNo().v();
				Statement statementU = con.createStatement();
				statementU.executeUpdate(updateTableSQL);
			}
		} catch (Exception e) {
			
		}
	}

}
