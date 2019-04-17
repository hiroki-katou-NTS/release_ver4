package nts.uk.ctx.at.record.infra.repository.affiliationinformation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.record.infra.entity.affiliationinformation.KrcdtDaiWorkType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

@Stateless
public class JpaWorkTypeOfDailyPerforRepository extends JpaRepository implements WorkTypeOfDailyPerforRepository {

	private static final String FIND_BY_KEY;

//	private static final String REMOVE_BY_KEY;
	
	static {
		StringBuilder builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDaiWorkType a ");
//		builderString.append("WHERE a.krcdtDaiWorkTypePK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtDaiWorkTypePK.ymd = :ymd ");
//		REMOVE_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDaiWorkType a ");
		builderString.append("WHERE a.krcdtDaiWorkTypePK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDaiWorkTypePK.ymd = :ymd ");
		FIND_BY_KEY = builderString.toString();
	}

	@Override
	public void delete(String employeeId, GeneralDate processingDate) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_DAI_WORKTYPE Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + processingDate + "'" ;
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
//		this.getEntityManager().createQuery(REMOVE_BY_KEY).setParameter("employeeId", employeeId)
//				.setParameter("ymd", processingDate).executeUpdate();
//		this.getEntityManager().flush();
	}

	@Override
	public void add(WorkTypeOfDailyPerformance workTypeOfDailyPerformance) {
		// this.commandProxy().insert(KrcdtDaiWorkType.toEntity(workTypeOfDailyPerformance));
		// this.getEntityManager().flush();
		try {
			Connection con = this.getEntityManager().unwrap(Connection.class);
			String insertTableSQL = "INSERT INTO KRCDT_DAI_WORKTYPE ( SID , YMD , WORKTYPE_CODE ) " + "VALUES( '"
					+ workTypeOfDailyPerformance.getEmployeeId() + "' , '" + workTypeOfDailyPerformance.getDate() + "' , '"
					+ workTypeOfDailyPerformance.getWorkTypeCode().v() + "' )";
			Statement statementI = con.createStatement();
			statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertTableSQL));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(WorkTypeOfDailyPerformance workTypeOfDailyPerformance) {
		// Optional<KrcdtDaiWorkType> data =
		// this.queryProxy().query(FIND_BY_KEY, KrcdtDaiWorkType.class)
		// .setParameter("employeeId",
		// workTypeOfDailyPerformance.getEmployeeId())
		// .setParameter("ymd",
		// workTypeOfDailyPerformance.getDate()).getSingle();
		// if (data.isPresent()) {
		// data.get().krcdtDaiWorkTypePK.employeeId =
		// workTypeOfDailyPerformance.getEmployeeId();
		// data.get().krcdtDaiWorkTypePK.ymd =
		// workTypeOfDailyPerformance.getDate();
		// data.get().workTypeCode =
		// workTypeOfDailyPerformance.getWorkTypeCode().v();
		//
		// this.commandProxy().update(data.get());
		// }
		try {
			Connection con = this.getEntityManager().unwrap(Connection.class);

			String updateTableSQL = " UPDATE KRCDT_DAI_WORKTYPE SET WORKTYPE_CODE = '"
					+ workTypeOfDailyPerformance.getWorkTypeCode().v() + "' WHERE SID = '"
					+ workTypeOfDailyPerformance.getEmployeeId() + "' AND YMD = '" + workTypeOfDailyPerformance.getDate() + "'";
			Statement statementU = con.createStatement();
			statementU.executeUpdate(JDBCUtil.toUpdateWithCommonField(updateTableSQL));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<WorkTypeOfDailyPerformance> findByKey(String employeeId, GeneralDate processingDate) {
		Optional<WorkTypeOfDailyPerformance> data = this.queryProxy().query(FIND_BY_KEY, KrcdtDaiWorkType.class)
				.setParameter("employeeId", employeeId).setParameter("ymd", processingDate)
				.getSingle(f -> f.toDomain());
		if (data.isPresent()) {
			return data;
		} else {
			return Optional.empty();
		}
	}

	@Override
	public List<WorkTypeOfDailyPerformance> finds(List<String> employeeId, DatePeriod baseDate) {
		List<WorkTypeOfDailyPerformance> result = new ArrayList<>();

		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(internalQuery(baseDate, empIds));
		});
		return result;
	}
	
	@SneakyThrows
	private List<WorkTypeOfDailyPerformance> internalQuery(DatePeriod baseDate, List<String> empIds) {
		String subEmp = NtsStatement.In.createParamsString(empIds);
		StringBuilder query = new StringBuilder("SELECT SID, YMD, WORKTYPE_CODE FROM KRCDT_DAI_WORKTYPE");
		query.append(" WHERE YMD <= ? AND YMD >= ? ");
		query.append(" AND SID IN (" + subEmp + ")");
		try (val stmt = this.connection().prepareStatement(query.toString())){
			stmt.setDate(1, Date.valueOf(baseDate.end().localDate()));
			stmt.setDate(2, Date.valueOf(baseDate.start().localDate()));
			for (int i = 0; i < empIds.size(); i++) {
				stmt.setString(i + 3, empIds.get(i));
			}
			return new NtsResultSet(stmt.executeQuery()).getList(rec -> {
				return new WorkTypeOfDailyPerformance(rec.getString("SID"), 
						rec.getGeneralDate("YMD"), rec.getString("WORKTYPE_CODE"));
			});
		}
	}

	@Override
	public List<WorkTypeOfDailyPerformance> finds(Map<String, List<GeneralDate>> param) {
		List<KrcdtDaiWorkType> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT af FROM KrcdtDaiWorkType af ");
		query.append("WHERE af.krcdtDaiWorkTypePK.employeeId IN :employeeId ");
		query.append("AND af.krcdtDaiWorkTypePK.ymd IN :date");
		TypedQueryWrapper<KrcdtDaiWorkType> tQuery = this.queryProxy().query(query.toString(), KrcdtDaiWorkType.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList().stream()
					.filter(c -> p.get(c.krcdtDaiWorkTypePK.employeeId).contains(c.krcdtDaiWorkTypePK.ymd))
					.collect(Collectors.toList()));
		});
		return result.stream().map(af -> af.toDomain()).collect(Collectors.toList());
	}

}
