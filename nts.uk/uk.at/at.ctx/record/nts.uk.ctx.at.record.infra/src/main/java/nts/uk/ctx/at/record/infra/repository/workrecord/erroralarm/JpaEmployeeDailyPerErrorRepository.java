package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KrcdtEmpDivErAl;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KrcdtEmpErAlCommon;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KrcdtOtkErAl;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KrcdtSyainDpErList;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

@Stateless
public class JpaEmployeeDailyPerErrorRepository extends JpaRepository implements EmployeeDailyPerErrorRepository {

	@Override
	public void insert(EmployeeDailyPerError eral) {
		// this.commandProxy().insert(KrcdtSyainDpErList.toEntity(employeeDailyPerformanceError));
		// this.getEntityManager().flush();
		String id = IdentifierUtil.randomUniqueId();
		String ccd = AppContexts.user().contractCode();
		try {
			Connection con = this.getEntityManager().unwrap(Connection.class);
			Statement statementI = con.createStatement();
			String erCode = eral.getErrorAlarmWorkRecordCode().v();
			String errorAlarmMessage = eral.getErrorAlarmMessage().isPresent()
					? eral.getErrorAlarmMessage().get().v() : null;
			String insertTableSQL = "INSERT INTO " + checkErType(erCode) 
					+ " ( ID , ERROR_CODE , SID, PROCESSING_DATE , CID, CONTRACT_CD , ERROR_MESSAGE) "
					+ "VALUES( '" + id + "' , '" + erCode
					+ "' , '" + eral.getEmployeeID() + "' , '"
					+ eral.getDate() + "' , '" + eral.getCompanyID() + "' , '" + ccd
					+ "', '" + errorAlarmMessage + "' )";
			statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertTableSQL));

			for (Integer attendanceItemId : eral.getAttendanceItemList()) {
				String insertAttendanceItem = "INSERT INTO KRCDT_DAY_ERAL_ATD ( ID , ATTENDANCE_ITEM_ID , SID, PROCESSING_DATE , CID , CONTRACT_CD ) "
						+ "VALUES( '" + id + "', '" + attendanceItemId  + "' , '" + eral.getEmployeeID() + "' , '"
								+ eral.getDate() + "' , '" + eral.getCompanyID() + "' , '" + ccd + "' )";
				statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertAttendanceItem));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String checkErType(String erCode) {
		if (erCode.startsWith("D")) {
			return "KRCDT_DAY_DG_ERAL";
		}
		
		if (erCode.startsWith("OTK")) {
			return "KRCDT_DAY_OTK_ERAL";
		}
		
		return "KRCDT_DAY_ERAL";
	}
	
	private String checkErTypeJpa(String erCode) {
		if (erCode.startsWith("D")) {
			return "KrcdtEmpDivErAl";
		}
		
		if (erCode.startsWith("OTK")) {
			return "KrcdtOtkErAl";
		}
		
		return "KrcdtSyainDpErList";
	}

	@Override
	public void insert(List<EmployeeDailyPerError> errors) {
		if (errors.isEmpty()) {
			return;
		}
		this.commandProxy().insertAll(errors.stream().map(e -> convertToEntity(e)).collect(Collectors.toList()));
//		 this.getEntityManager().flush();
	}

	@SuppressWarnings("unchecked")
	private <T extends KrcdtEmpErAlCommon> T convertToEntity(EmployeeDailyPerError e) {
		if (e.getErrorAlarmWorkRecordCode().v().startsWith("D")) {
			return (T) KrcdtEmpDivErAl.toEntity(e);
		}
	
		if (e.getErrorAlarmWorkRecordCode().v().startsWith("OTK")) {
			return (T) KrcdtOtkErAl.toEntity(e);
		}
	
		return (T) KrcdtSyainDpErList.toEntity(e);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public boolean checkExistErrorCode(String employeeID, GeneralDate processingDate, String errorCode) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) FROM ");
		builderString.append(checkErTypeJpa(errorCode));
		builderString.append(" a WHERE a.processingDate = :processingDate ");
		builderString.append("AND a.employeeId = :employeeId ");
		builderString.append("AND a.errorCode = :errorCode ");
		String query = builderString.toString();
		return this.queryProxy().query(query, long.class).setParameter("processingDate", processingDate)
				.setParameter("employeeId", employeeID).setParameter("errorCode", errorCode).getSingle().get() > 0;
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public boolean checkExistErrorCodeByPeriod(String employeeID, DatePeriod datePeriod, String errorCode) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) FROM ");
		builderString.append(checkErTypeJpa(errorCode));
		builderString.append(" a WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.processingDate >= :start ");
		builderString.append("AND a.processingDate <= :end ");
		builderString.append("AND a.errorCode = :errorCode ");
		String query = builderString.toString();
		return this.queryProxy().query(query, long.class).setParameter("employeeId", employeeID)
				.setParameter("start", datePeriod.start()).setParameter("end", datePeriod.end())
				.setParameter("errorCode", errorCode).getSingle().get() > 0;
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<EmployeeDailyPerError> find(String employeeID, GeneralDate processingDate) {
		List<EmployeeDailyPerError> result = new ArrayList<>();
		
		result.addAll(findErAl(employeeID, processingDate, "XXXX"));
		result.addAll(findErAl(employeeID, processingDate, "DXXX"));
		result.addAll(findErAl(employeeID, processingDate, "OTKX"));
		
		return result;
	}

	private List<EmployeeDailyPerError> findErAl(String employeeID, GeneralDate processingDate, String type) {
		
		try (PreparedStatement statement = this.connection().prepareStatement(
				"SELECT a.*,b.ATTENDANCE_ITEM_ID FROM " 
						+ checkErType(type)
						+ " a LEFT JOIN KRCDT_ER_ATTENDANCE_ITEM b"
						+ " ON b.ID = a.ID "
						+ " WHERE a.SID = ?"
						+ " AND  a.PROCESSING_DATE = ?")){
			statement.setString(1, employeeID);
			statement.setDate(2, Date.valueOf(processingDate.toLocalDate()));
			return new NtsResultSet(statement.executeQuery()).getList(rec -> {
				
				val dom = new EmployeeDailyPerError(rec.getString("ID"), rec.getString("CID"), employeeID, processingDate, 
						new ErrorAlarmWorkRecordCode(rec.getString("ERROR_CODE")), new ArrayList<>(), 
						0, rec.getString("ERROR_MESSAGE"));
				
				if(rec.getInt("ATTENDANCE_ITEM_ID") != null) {
					dom.setAttendanceItemList(Arrays.asList(rec.getInt("ATTENDANCE_ITEM_ID")));
				}
				
				return dom;
			});
		} catch (SQLException ex) {
				throw new RuntimeException(ex);
		}
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<EmployeeDailyPerError> findByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {

		List<EmployeeDailyPerError> result = new ArrayList<>();
		
		result.addAll(findByPeriodOrderByYmdBy(employeeId, datePeriod, KrcdtSyainDpErList.class));
		result.addAll(findByPeriodOrderByYmdBy(employeeId, datePeriod, KrcdtOtkErAl.class));
		result.addAll(findByPeriodOrderByYmdBy(employeeId, datePeriod, KrcdtEmpDivErAl.class));
		
		return result;
	}
	
	private <T extends KrcdtEmpErAlCommon> List<EmployeeDailyPerError> findByPeriodOrderByYmdBy(String employeeId, DatePeriod datePeriod, Class<T> className) {

		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a FROM ");
		builderString.append(className.getSimpleName());
		builderString.append(" a WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.processingDate >= :start ");
		builderString.append("AND a.processingDate <= :end ");
		builderString.append("ORDER BY a.processingDate ");
		String FIND_BY_PERIOD_ORDER_BY_YMD = builderString.toString();
		
		return this.queryProxy().query(FIND_BY_PERIOD_ORDER_BY_YMD, className)
				.setParameter("employeeId", employeeId).setParameter("start", datePeriod.start())
				.setParameter("end", datePeriod.end()).getList().stream()
				.collect(Collectors.groupingBy(c -> c.employeeId + c.processingDate.toString())).entrySet().stream()
				.map(c -> c.getValue().stream().map(item -> item.toDomain()).collect(Collectors.toList()))
				.flatMap(List::stream).collect(Collectors.toList());
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<EmployeeDailyPerError> finds(List<String> employeeID, DatePeriod processingDate) {
		List<EmployeeDailyPerError> result = new ArrayList<>();

		result.addAll(findsBy(employeeID, processingDate, "XXXX"));
		result.addAll(findsBy(employeeID, processingDate, "DXXX"));
		result.addAll(findsBy(employeeID, processingDate, "OTKX"));
		
		return result;
	}
	
	private List<EmployeeDailyPerError> findsBy(List<String> employeeID, DatePeriod processingDate, String codeType) {
		//fix response 192
		String GET_BY_LIST_EMP_AND_PERIOD = "SELECT a.*, b.* FROM "
				+ checkErType(codeType)
				+ " a LEFT JOIN KRCDT_ER_ATTENDANCE_ITEM b"
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
								0,
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
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<EmployeeDailyPerError> finds(Map<String, List<GeneralDate>> param) {

		List<EmployeeDailyPerError> result = new ArrayList<>();
		result.addAll(findsBy(param, KrcdtSyainDpErList.class));
		result.addAll(findsBy(param, KrcdtEmpDivErAl.class));
		result.addAll(findsBy(param, KrcdtOtkErAl.class));
		
		return result;
	}
	
	private <T extends KrcdtEmpErAlCommon> List<EmployeeDailyPerError> findsBy(Map<String, List<GeneralDate>> param, Class<T> className) {
		List<EmployeeDailyPerError> result = new ArrayList<>();
		
		findsBy(param, className, (p, e) -> {
			result.addAll(e.stream().filter(c -> p.get(c.employeeId).contains(c.processingDate))
					.collect(Collectors.groupingBy(c -> c.employeeId + c.processingDate.toString()))
					.entrySet().stream().map(c -> group(c.getValue()))
					.flatMap(List::stream)
					.collect(Collectors.toList()));
		});
		return result;
	}
	
	private <T extends KrcdtEmpErAlCommon> void findsBy(Map<String, List<GeneralDate>> param, Class<T> className, 
			BiConsumer<Map<String, List<GeneralDate>>, List<T>> consumer) {
		StringBuilder query = new StringBuilder("SELECT a FROM ");
		query.append(className.getSimpleName());
		query.append(" a WHERE a.employeeId IN :employeeId ");
		query.append("AND a.processingDate IN :date");
		TypedQueryWrapper<T> tQuery = this.queryProxy().query(query.toString(), className);
		
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			List<T> entities = tQuery.setParameter("employeeId", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList();
			
			consumer.accept(p, entities);
			
		});
	}
	
	private <T extends KrcdtEmpErAlCommon>List<EmployeeDailyPerError> group(List<T> entities) {
		return entities.stream().map(c -> c.toDomain()).collect(Collectors.toList());
	}
	
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<EmployeeDailyPerError> findsWithLeftJoin(List<String> employeeID, DatePeriod processingDate) {
		return finds(employeeID, processingDate);
	}

	@Override
	public void removeParam(String sid, GeneralDate date) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_DAY_OTK_ERAL Where SID = " + "'" + sid + "'" + " and PROCESSING_DATE = " + "'" + date + "'" ;
		try (val st = con.createStatement()) {
			st.executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		String sqlQuery1 = "Delete From KRCDT_DAY_DG_ERAL Where SID = " + "'" + sid + "'" + " and PROCESSING_DATE = " + "'" + date + "'" ;
		try (val st = con.createStatement()) {
			st.executeUpdate(sqlQuery1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		String sqlQuery2 = "Delete From KRCDT_DAY_ERAL Where SID = " + "'" + sid + "'" + " and PROCESSING_DATE = " + "'" + date + "'" ;
		try (val st = con.createStatement()) {
			st.executeUpdate(sqlQuery2);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		String sqlQuery4 = "Delete From KRCDT_ER_ATTENDANCE_ITEM Where SID = " + "'" + sid + "'" + " and PROCESSING_DATE = " + "'" + date + "'" ;
		try (val st = con.createStatement()) {
			st.executeUpdate(sqlQuery4);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void removeParam(Map<String, List<GeneralDate>> param) {
		List<KrcdtEmpErAlCommon> result = new ArrayList<>();
		
		findsBy(param, KrcdtEmpDivErAl.class, (p, e) -> result.addAll(e));
		findsBy(param, KrcdtOtkErAl.class, (p, e) -> result.addAll(e));
		findsBy(param, KrcdtSyainDpErList.class, (p, e) -> result.addAll(e));

		if (!result.isEmpty()) {
			commandProxy().removeAll(result);
		}
	}
	
	@Override
	@SneakyThrows
	public void removeContinuosErrorIn(String sid, DatePeriod date, String code) {
		String query = new String("DELETE FROM KRCDT_DAY_OTK_ERAL WHERE ERROR_CODE = ? AND SID = ? AND PROCESSING_DATE >= ? AND PROCESSING_DATE <= ?");
		try (PreparedStatement statement = this.connection().prepareStatement(query)) {
			statement.setString(1, code);
			statement.setString(2, sid);
			statement.setDate(3, Date.valueOf(date.start().toLocalDate()));
			statement.setDate(4, Date.valueOf(date.end().toLocalDate()));
			statement.executeUpdate();
		}
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<EmployeeDailyPerError> findList(String companyID, String employeeID) {
		List<EmployeeDailyPerError> result = new ArrayList<>();

		result.addAll(findsBy(companyID, employeeID, KrcdtEmpDivErAl.class));
		result.addAll(findsBy(companyID, employeeID, KrcdtOtkErAl.class));
		result.addAll(findsBy(companyID, employeeID, KrcdtSyainDpErList.class));
		
		return result;
	}

	private <T extends KrcdtEmpErAlCommon> List<EmployeeDailyPerError> findsBy(String companyID, String employeeID, Class<T> className) {
		StringBuilder builderString = new StringBuilder();
		
		builderString.append("SELECT a FROM ");
		builderString.append(className.getSimpleName());
		builderString.append(" a WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.companyID = :companyId ");
		
		return this.queryProxy().query(builderString.toString(), className)
				.setParameter("employeeId", employeeID).setParameter("companyId", companyID).getList(x -> x.toDomain());
	}
	
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public boolean checkExistRecordErrorListDate(String companyID, String employeeID, List<GeneralDate> lstDate) {
		Map<String, List<GeneralDate>> mapper = new HashMap<>();
		mapper.put(employeeID, lstDate);
		
		List<KrcdtEmpErAlCommon> e = new ArrayList<>();
		
		findsBy(mapper, KrcdtOtkErAl.class, (p, en) -> e.addAll(en));
		findsBy(mapper, KrcdtEmpDivErAl.class, (p, en) -> e.addAll(en));
		findsBy(mapper, KrcdtSyainDpErList.class, (p, en) -> e.addAll(en));
		
		return !e.isEmpty();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean checkEmployeeHasErrorOnProcessingDate(String employeeID, GeneralDate processingDate) {
		return checkExistErrorByDate(AppContexts.user().companyId(), employeeID, processingDate);
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public boolean checkExistErrorByDate(String companyId, String employeeId, GeneralDate date) {

		return checkErrorByPeriodDate(companyId, employeeId, date, date);
	}

	@Override
	public void removeByCidSidDateAndCode(String companyID, String employeeID, GeneralDate date, String errorCode) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE FROM ");
		builderString.append(checkErTypeJpa(errorCode));
		builderString.append(" a WHERE a.companyID = :companyID ");
		builderString.append("AND a.employeeId = :employeeId ");
		builderString.append("AND a.processingDate = :processingDate ");
		builderString.append("AND a.errorCode = :errorCode ");
		String REMOVE_BY_CID_SID_DATE_ERRORCODE = builderString.toString();
		
		this.getEntityManager().createQuery(REMOVE_BY_CID_SID_DATE_ERRORCODE).setParameter("companyID", companyID)
				.setParameter("employeeId", employeeID).setParameter("processingDate", date)
				.setParameter("errorCode", errorCode).executeUpdate();
		this.getEntityManager().flush();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean checkErrorByPeriodDate(String companyId, String employeeId, GeneralDate strDate, GeneralDate endDate) {
		DatePeriod period = new DatePeriod(strDate, endDate);
		
		return !this.findByPeriodOrderByYmdBy(employeeId, period, KrcdtSyainDpErList.class).isEmpty() ||
				!this.findByPeriodOrderByYmdBy(employeeId, period, KrcdtEmpDivErAl.class).isEmpty() ||
				!this.findByPeriodOrderByYmdBy(employeeId, period, KrcdtOtkErAl.class).isEmpty();
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<EmployeeDailyPerError> getByEmpIDAndPeriod(List<String> employeeID, DatePeriod processingDate) {
		List<EmployeeDailyPerError> result = new ArrayList<>();
		
		result.addAll(getBy(employeeID, processingDate, "XXXX"));
		result.addAll(getBy(employeeID, processingDate, "DXXX"));
		result.addAll(getBy(employeeID, processingDate, "OTKX"));
		
		return result;
	}
	
	private List<EmployeeDailyPerError> getBy(List<String> employeeID, DatePeriod processingDate, String erCode) {
		if(employeeID.isEmpty())
			return Collections.emptyList();
		
		String GET_BY_LIST_EMP_AND_PERIOD = "SELECT a.*,b.* FROM "
				+ checkErType(erCode)
				+ " a LEFT JOIN KRCDT_ER_ATTENDANCE_ITEM b"
				+ " ON b.ID = a.ID "
				+ " WHERE a.PROCESSING_DATE <= ?"
				+ " AND a.PROCESSING_DATE >= ?"
				+ " AND  a.SID IN (" ;
		
		for(int i = 0;i<employeeID.size();i++) {
			GET_BY_LIST_EMP_AND_PERIOD = GET_BY_LIST_EMP_AND_PERIOD+"?";
			if(i < employeeID.size()-1) {
				GET_BY_LIST_EMP_AND_PERIOD = GET_BY_LIST_EMP_AND_PERIOD+",";
			}
		}
		GET_BY_LIST_EMP_AND_PERIOD = GET_BY_LIST_EMP_AND_PERIOD+")";
		try (PreparedStatement statement = this.connection().prepareStatement(GET_BY_LIST_EMP_AND_PERIOD)) {
			statement.setDate(1, Date.valueOf(processingDate.end().localDate()));
			statement.setDate(2, Date.valueOf(processingDate.start().localDate()));
			for(int i = 0;i<employeeID.size();i++) {
				statement.setString(i+3, employeeID.get(i));
			}
			
			List<EmployeeDailyPerError> results =  new NtsResultSet(statement.executeQuery()).getList(rs -> { 
				return new EmployeeDailyPerError(
						rs.getString("ID"),
						rs.getString("CID"), 
						rs.getString("SID"),
						rs.getGeneralDate("PROCESSING_DATE"),
						new ErrorAlarmWorkRecordCode(rs.getString("ERROR_CODE")),
						Arrays.asList(rs.getInt("ATTENDANCE_ITEM_ID")),
						0,
						rs.getString("ERROR_MESSAGE"));
			});
			
			List<EmployeeDailyPerError> resultReturn = new ArrayList<>();
			Map<String, List<EmployeeDailyPerError>> groupResult = results.stream().collect(Collectors.groupingBy(x -> x.getId()));
			groupResult.forEach((key, value) ->{
				 List<Integer> id = value.stream().flatMap(x -> x.getAttendanceItemList().stream()).collect(Collectors.toList());
				 value.get(0).setAttendanceItemList(id);
				 resultReturn.add(value.get(0));
			});
			return resultReturn;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void removeNotOTK(Map<String, List<GeneralDate>> param) {
		List<KrcdtEmpErAlCommon> result = new ArrayList<>();

		findsBy(param, KrcdtEmpDivErAl.class, (p,e) -> {
			result.addAll(e);
		});
		findsBy(param, KrcdtSyainDpErList.class, (p,e) -> {
			result.addAll(e);
		});
		
		if (!result.isEmpty()) {
			commandProxy().removeAll(result);
		}

	}

	@Override
	public void update(List<EmployeeDailyPerError> employeeDailyPerformanceError) {
		if (employeeDailyPerformanceError.isEmpty()) {
			return;
		}
		this.commandProxy().updateAll(employeeDailyPerformanceError.stream().map(e -> convertToEntity(e))
				.collect(Collectors.toList()));
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<EmployeeDailyPerError> findsByCodeLst(List<String> employeeIDLst, DatePeriod period, List<String> codeLst) {
		List<EmployeeDailyPerError> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT a FROM KrcdtSyainDpErList a ");
		query.append("WHERE a.employeeId IN :employeeId ");
		query.append("AND a.processingDate >= :startDate ");
		query.append("AND a.processingDate <= :endDate ");
		query.append("AND a.errorCode IN :codeLst");
		TypedQueryWrapper<KrcdtSyainDpErList> tQuery = this.queryProxy().query(query.toString(), KrcdtSyainDpErList.class);
		
		CollectionUtil.split(employeeIDLst, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, employeeID -> {
			result.addAll(tQuery
					.setParameter("employeeId", employeeID)
					.setParameter("startDate", period.start())
					.setParameter("endDate", period.end())
					.setParameter("codeLst", codeLst)
					.getList(x -> x.toDomain()));
		});
		return result;
	}

}
