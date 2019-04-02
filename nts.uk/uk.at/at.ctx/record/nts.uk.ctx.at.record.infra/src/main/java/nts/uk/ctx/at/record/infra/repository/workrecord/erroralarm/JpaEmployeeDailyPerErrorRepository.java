package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KrcdtErAttendanceItem;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KrcdtErAttendanceItemPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KrcdtSyainDpErList;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

@Stateless
public class JpaEmployeeDailyPerErrorRepository extends JpaRepository implements EmployeeDailyPerErrorRepository {

	private static final String FIND_ERROR_CODE;

	private static final String FIND_ERROR_CODE_BY_PERIOD;

	private static final String FIND_BY_PERIOD_ORDER_BY_YMD;

//	private static final String REMOVE_DATA;

//	private static final String REMOVE_DATA_ATTENDANCE_ITEM;

	private static final String CHECK_EXIST_CODE_BY_LIST_DATE;

	private static final String CHECK_EMPLOYEE_HAS_ERROR_ON_PROCESSING_DATE;

	private static final String REMOVE_BY_CID_SID_DATE_ERRORCODE;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.processingDate = :processingDate ");
		builderString.append("AND a.employeeId = :employeeId ");
		builderString.append("AND a.errorCode = :errorCode ");
		FIND_ERROR_CODE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.processingDate >= :start ");
		builderString.append("AND a.processingDate <= :end ");
		builderString.append("AND a.errorCode = :errorCode ");
		FIND_ERROR_CODE_BY_PERIOD = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.processingDate >= :start ");
		builderString.append("AND a.processingDate <= :end ");
		builderString.append("ORDER BY a.processingDate ");
		FIND_BY_PERIOD_ORDER_BY_YMD = builderString.toString();

//		builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtSyainDpErList a ");
//		builderString.append("WHERE a.employeeId = :employeeId ");
//		builderString.append("AND a.processingDate = :start ");
//		REMOVE_DATA = builderString.toString();
//
//		builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtErAttendanceItem a ");
//		builderString.append("WHERE a.krcdtErAttendanceItemPK.iD = :iD ");
//		REMOVE_DATA_ATTENDANCE_ITEM = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.companyID = :companyID ");
		builderString.append("AND a.processingDate IN :processingDates ");
		CHECK_EXIST_CODE_BY_LIST_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.processingDate = :processingDate ");
		CHECK_EMPLOYEE_HAS_ERROR_ON_PROCESSING_DATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.companyID = :companyID ");
		builderString.append("AND a.employeeId = :employeeId ");
		builderString.append("AND a.processingDate = :processingDate ");
		builderString.append("AND a.errorCode = :errorCode ");
		REMOVE_BY_CID_SID_DATE_ERRORCODE = builderString.toString();

	}

	@Override
	public void insert(EmployeeDailyPerError employeeDailyPerformanceError) {
		// this.commandProxy().insert(KrcdtSyainDpErList.toEntity(employeeDailyPerformanceError));
		// this.getEntityManager().flush();
		String id = IdentifierUtil.randomUniqueId();
		try {
			Connection con = this.getEntityManager().unwrap(Connection.class);
			Statement statementI = con.createStatement();
			String errorAlarmMessage = employeeDailyPerformanceError.getErrorAlarmMessage().isPresent()
					? "'" + employeeDailyPerformanceError.getErrorAlarmMessage().get().v() + "'" : null;
			String insertTableSQL = "INSERT INTO KRCDT_SYAIN_DP_ER_LIST ( ID , ERROR_CODE , SID, PROCESSING_DATE , CID , ERROR_CANCELABLE , ERROR_MESSAGE ) "
					+ "VALUES( '" + id + "' , '" + employeeDailyPerformanceError.getErrorAlarmWorkRecordCode().v()
					+ "' , '" + employeeDailyPerformanceError.getEmployeeID() + "' , '"
					+ employeeDailyPerformanceError.getDate() + "' , '" + employeeDailyPerformanceError.getCompanyID()
					+ "' , " + employeeDailyPerformanceError.getErrorCancelAble() + " , " + errorAlarmMessage + " )";
			statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertTableSQL));

			for (Integer attendanceItemId : employeeDailyPerformanceError.getAttendanceItemList()) {
				String insertAttendanceItem = "INSERT INTO KRCDT_ER_ATTENDANCE_ITEM ( ID , ATTENDANCE_ITEM_ID ) "
						+ "VALUES( '" + id + "' , " + attendanceItemId + " )";
				statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertAttendanceItem));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void insert(List<EmployeeDailyPerError> errors) {
		if (errors.isEmpty()) {
			return;
		}
		this.commandProxy().insertAll(errors.stream().map(e -> KrcdtSyainDpErList.toEntity(e)).collect(Collectors.toList()));
		 this.getEntityManager().flush();
	}

	@Override
	public boolean checkExistErrorCode(String employeeID, GeneralDate processingDate, String errorCode) {
		return this.queryProxy().query(FIND_ERROR_CODE, long.class).setParameter("processingDate", processingDate)
				.setParameter("employeeId", employeeID).setParameter("errorCode", errorCode).getSingle().get() > 0;
	}

	@Override
	public boolean checkExistErrorCodeByPeriod(String employeeID, DatePeriod datePeriod, String errorCode) {
		return this.queryProxy().query(FIND_ERROR_CODE_BY_PERIOD, long.class).setParameter("employeeId", employeeID)
				.setParameter("start", datePeriod.start()).setParameter("end", datePeriod.end())
				.setParameter("errorCode", errorCode).getSingle().get() > 0;
	}

	@Override
	public List<EmployeeDailyPerError> find(String employeeID, GeneralDate processingDate) {
		List<KrcdtSyainDpErList> result = findEntities(employeeID, processingDate);
		if (!result.isEmpty()) {
			List<EmployeeDailyPerError> results = new ArrayList<>();
			Map<String, List<KrcdtSyainDpErList>> items = result.stream().collect(Collectors.groupingBy(x -> x.id));
			items.forEach((key, value) -> {
				results.add(KrcdtSyainDpErList.toDomainForRes(value));
			});
			return results;
		}

		return new ArrayList<>();
	}

	private List<KrcdtSyainDpErList> findEntities(String employeeID, GeneralDate processingDate) {
		
		try (PreparedStatement statement = this.connection().prepareStatement(
				"SELECT a.*,b.ATTENDANCE_ITEM_ID FROM KRCDT_SYAIN_DP_ER_LIST a"
						+ " LEFT JOIN KRCDT_ER_ATTENDANCE_ITEM b"
						+ " ON b.ID = a.ID "
						+ " WHERE a.SID = ?"
						+ " AND  a.PROCESSING_DATE = ?")){
			statement.setString(1, employeeID);
			statement.setDate(2, Date.valueOf(processingDate.toLocalDate()));
			return new NtsResultSet(statement.executeQuery()).getList(rec -> {
				val entity = new KrcdtSyainDpErList();
				entity.errorCode = rec.getString("ERROR_CODE");
				entity.employeeId = rec.getString("SID");
				entity.processingDate = rec.getGeneralDate("PROCESSING_DATE");
				entity.companyID = rec.getString("CID");
				entity.errorCancelable = rec.getInt("ERROR_CANCELABLE");
				entity.errorAlarmMessage = rec.getString("ERROR_MESSAGE");
				entity.id = rec.getString("ID");
				if(rec.getInt("ATTENDANCE_ITEM_ID") != null) {
					KrcdtErAttendanceItem krcdtErAttendanceItem = new KrcdtErAttendanceItem();
					KrcdtErAttendanceItemPK krcdtErAttendanceItemPK = new KrcdtErAttendanceItemPK();
					krcdtErAttendanceItemPK.iD = rec.getString("ID");
					krcdtErAttendanceItemPK.attendanceItemId = rec.getInt("ATTENDANCE_ITEM_ID");
					krcdtErAttendanceItem.krcdtErAttendanceItemPK = krcdtErAttendanceItemPK;
					entity.krcdtErAttendanceItem = krcdtErAttendanceItem;
				}
				
				return entity;
			});
		} catch (SQLException ex) {
				throw new RuntimeException(ex);
		}
		
//		StringBuilder builderString = new StringBuilder();
//		builderString.append("SELECT a ");
//		builderString.append("FROM KrcdtSyainDpErList a ");
//		builderString.append("WHERE a.employeeId = :employeeId ");
//		builderString.append("AND a.processingDate = :ymd ");
//		List<KrcdtSyainDpErList> result = this.queryProxy().query(builderString.toString(), KrcdtSyainDpErList.class)
//				.setParameter("employeeId", employeeID).setParameter("ymd", processingDate).getList();
//		return result;
	}

	@Override
	public List<EmployeeDailyPerError> findByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {
		return this.queryProxy().query(FIND_BY_PERIOD_ORDER_BY_YMD, KrcdtSyainDpErList.class)
				.setParameter("employeeId", employeeId).setParameter("start", datePeriod.start())
				.setParameter("end", datePeriod.end()).getList().stream()
				.collect(Collectors.groupingBy(c -> c.employeeId + c.processingDate.toString())).entrySet().stream()
				.map(c -> c.getValue().stream().map(item -> item.toDomain()).collect(Collectors.toList()))
				.flatMap(List::stream).collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDailyPerError> finds(List<String> employeeID, DatePeriod processingDate) {
		//fix response 192
		String GET_BY_LIST_EMP_AND_PERIOD = "SELECT a.*, b.* FROM KRCDT_SYAIN_DP_ER_LIST a"
				+ " JOIN KRCDT_ER_ATTENDANCE_ITEM b"
				+ " ON b.ID = a.ID "
				+ " WHERE a.PROCESSING_DATE <= ?"
				+ " AND a.PROCESSING_DATE >= ?"
				+ " AND  a.SID IN (";

		//fix exceeding max condition of IN statement
		List<EmployeeDailyPerError> resultReturn = new ArrayList<>();
		CollectionUtil.split(employeeID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
            StringBuilder stringBuilder = new StringBuilder();
			int size = subList.size();
			for (int i = 0; i < size; i++) {
				stringBuilder.append("?");
				if (i < subList.size() - 1) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append(")");
			String QUERY = GET_BY_LIST_EMP_AND_PERIOD + stringBuilder.toString();
			try (PreparedStatement statement = this.connection().prepareStatement(QUERY)) {
				statement.setDate(1, Date.valueOf(processingDate.end().localDate()));
				statement.setDate(2, Date.valueOf(processingDate.start().localDate()));
				for (int i = 0; i < size; i++) {
					statement.setString(i + 3, subList.get(i));
				}
				List<EmployeeDailyPerError> results = new NtsResultSet(statement.executeQuery()).getList(rs ->
						new EmployeeDailyPerError(
								rs.getString("ID"),
								rs.getString("CID"),
								rs.getString("SID"),
								rs.getGeneralDate("PROCESSING_DATE"),
								new ErrorAlarmWorkRecordCode(rs.getString("ERROR_CODE")),
								Arrays.asList(rs.getInt("ATTENDANCE_ITEM_ID")),
								rs.getInt("ERROR_CANCELABLE"),
								rs.getString("ERROR_MESSAGE")));

				Map<String, List<EmployeeDailyPerError>> groupResult = results.stream().collect(Collectors.groupingBy(EmployeeDailyPerError::getId));
				groupResult.forEach((key, value) -> {
					List<Integer> id = value.stream().flatMap(x -> x.getAttendanceItemList().stream()).collect(Collectors.toList());
					value.get(0).setAttendanceItemList(id);
					resultReturn.add(value.get(0));
				});
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		return resultReturn;
//				builderString.append("SELECT a ");
//				builderString.append("FROM KrcdtSyainDpErList a ");
//				builderString.append("WHERE a.employeeId IN :employeeId ");
//				builderString.append("AND a.processingDate <= :end ");
//				builderString.append("AND a.processingDate >= :start ");
//				return this.queryProxy().query(builderString.toString(), KrcdtSyainDpErList.class)
//						.setParameter("employeeId", employeeID).setParameter("end", processingDate.end())
//						.setParameter("start", processingDate.start()).getList().stream()
//						.collect(Collectors.groupingBy(c -> c.employeeId + c.processingDate.toString())).entrySet().stream()
//						.map(c -> c.getValue().stream().map(item -> item.toDomain()).collect(Collectors.toList()))
//						.flatMap(List::stream).collect(Collectors.toList());
	}

	@Override
	public void removeParam(String sid, GeneralDate date) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_SYAIN_DP_ER_LIST Where SID = " + "'" + sid + "'" + " and PROCESSING_DATE = " + "'" + date + "'" ;
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
//   	List<KrcdtSyainDpErList> result = findEntities(sid, date);
// 		if (!result.isEmpty()) {
// 		commandProxy().removeAll(result);
// 		}
	}

	@Override
	public void removeParam(Map<String, List<GeneralDate>> param) {
		List<KrcdtSyainDpErList> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT a FROM KrcdtSyainDpErList a");
		query.append(" WHERE a.employeeId IN :employeeId");
		query.append(" AND a.processingDate IN :date");
		TypedQueryWrapper<KrcdtSyainDpErList> tQuery = this.queryProxy().query(query.toString(),
				KrcdtSyainDpErList.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList().stream().filter(c -> p.get(c.employeeId).contains(c.processingDate))
					.collect(Collectors.toList()));
		});
		if (!result.isEmpty()) {
			commandProxy().removeAll(result);
		}
	}
	
	@Override
	@SneakyThrows
	public void removeContinuosErrorIn(String sid, DatePeriod date, String code) {
		String query = new String("DELETE FROM KRCDT_SYAIN_DP_ER_LIST WHERE ERROR_CODE = ? AND SID = ? AND PROCESSING_DATE >= ? AND PROCESSING_DATE <= ?");
		try (PreparedStatement statement = this.connection().prepareStatement(query)) {
			statement.setString(1, code);
			statement.setString(2, sid);
			statement.setDate(3, Date.valueOf(date.start().toLocalDate()));
			statement.setDate(4, Date.valueOf(date.end().toLocalDate()));
			statement.executeUpdate();
		}
	}

	@Override
	public List<EmployeeDailyPerError> findList(String companyID, String employeeID) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.companyID = :companyId ");
		return this.queryProxy().query(builderString.toString(), KrcdtSyainDpErList.class)
				.setParameter("employeeId", employeeID).setParameter("companyId", companyID).getList(x -> x.toDomain());
	}

	@Override
	public boolean checkExistRecordErrorListDate(String companyID, String employeeID, List<GeneralDate> lstDate) {
		LongAdder counter = new LongAdder();
		CollectionUtil.split(lstDate, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			if (this.queryProxy().query(CHECK_EXIST_CODE_BY_LIST_DATE, long.class)
					.setParameter("employeeId", employeeID)
					.setParameter("companyID", companyID)
					.setParameter("processingDates", subList)
					.getSingle().get() > 0) {
				counter.add(1);
			}
		});
		return counter.intValue() > 0;
	}

	@Override
	public boolean checkEmployeeHasErrorOnProcessingDate(String employeeID, GeneralDate processingDate) {
		return this.queryProxy().query(CHECK_EMPLOYEE_HAS_ERROR_ON_PROCESSING_DATE, long.class)
				.setParameter("employeeId", employeeID).setParameter("processingDate", processingDate).getSingle()
				.get() > 0;
	}

	@Override
	public boolean checkExistErrorByDate(String companyId, String employeeId, GeneralDate date) {

		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.companyID = :companyId ");
		builderString.append("AND a.processingDate = :date ");
		Optional<KrcdtSyainDpErList> entyti = this.queryProxy()
				.query(builderString.toString(), KrcdtSyainDpErList.class).setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId).setParameter("date", date).getSingle();

		return entyti.isPresent() ? true : false;
	}

	@Override
	public void removeByCidSidDateAndCode(String companyID, String employeeID, GeneralDate date, String errorCode) {
		this.getEntityManager().createQuery(REMOVE_BY_CID_SID_DATE_ERRORCODE).setParameter("companyID", companyID)
				.setParameter("employeeId", employeeID).setParameter("processingDate", date)
				.setParameter("errorCode", errorCode).executeUpdate();
		this.getEntityManager().flush();
	}

	private static final String CHECK_ERROR_BY_DATE = "SELECT a FROM KrcdtSyainDpErList a "
			+ "WHERE a.employeeId = :employeeId "
			+ "AND a.companyID = :companyId "
			+ "AND a.processingDate >= :strDate "
			+ "AND a.processingDate <= :endDate ";
	
	@Override
	public boolean checkErrorByPeriodDate(String companyId, String employeeId, GeneralDate strDate, GeneralDate endDate) {
		return this.queryProxy().query(CHECK_ERROR_BY_DATE, KrcdtSyainDpErList.class)
				.setParameter("companyId", companyId)
				.setParameter("employeeId", employeeId)
				.setParameter("strDate", strDate)
				.setParameter("endDate", endDate)
				.getList().size() > 0;
	}

}
