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
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.record.infra.entity.daily.attendanceleavinggate.KrcdtDayTsLogon;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.entranceandexit.LogOnInfo;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.data.jdbc.JDBCUtil;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class PCLogOnInfoOfDailyRepoImpl extends JpaRepository implements PCLogOnInfoOfDailyRepo {

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<PCLogOnInfoOfDaily> find(String employeeId, GeneralDate baseDate) {
		List<LogOnInfo> logOnInfo = findQuery(employeeId, baseDate).getList(c -> c.toDomain());
		if (!logOnInfo.isEmpty()) {
			return Optional.of(new PCLogOnInfoOfDaily(employeeId, baseDate, logOnInfo));
		}
		return Optional.empty();
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<PCLogOnInfoOfDaily> find(String employeeId, List<GeneralDate> baseDate) {
		if (baseDate.isEmpty()) {
			return Collections.emptyList();
		}
		List<PCLogOnInfoOfDaily> resultList = new ArrayList<>();
		CollectionUtil.split(baseDate, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(toList(this.queryProxy()
					.query("SELECT al FROM KrcdtDayTsLogon al WHERE al.id.sid = :sid AND al.id.ymd IN :ymd",
							KrcdtDayTsLogon.class)
					.setParameter("ymd", subList).setParameter("sid", employeeId).getList().stream()));
		});
		return resultList;
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<PCLogOnInfoOfDaily> find(String employeeId) {
		return toList(this.queryProxy()
				.query("SELECT al FROM KrcdtDayTsLogon al WHERE al.id.sid = :sid", KrcdtDayTsLogon.class)
				.setParameter("sid", employeeId).getList().stream());
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<PCLogOnInfoOfDaily> finds(List<String> employeeId, DatePeriod baseDate) {
		if (employeeId.isEmpty()) {
			return Collections.emptyList();
		}
		List<PCLogOnInfoOfDaily> result = new ArrayList<>();
		String query = "SELECT al FROM KrcdtDayTsLogon al WHERE al.id.sid IN :sid AND al.id.ymd <= :end AND al.id.ymd >= :start";
		TypedQueryWrapper<KrcdtDayTsLogon> tQuery=  this.queryProxy().query(query, KrcdtDayTsLogon.class);
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(toList(tQuery.setParameter("sid", empIds)
										.setParameter("end", baseDate.end())
										.setParameter("start", baseDate.start()).getList().stream()));
		});
		return result;
	}

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<PCLogOnInfoOfDaily> finds(Map<String, List<GeneralDate>> param) {
		if (param.isEmpty()) {
			return Collections.emptyList();
		}
		List<PCLogOnInfoOfDaily> result = new ArrayList<>();
		String query = "SELECT al FROM KrcdtDayTsLogon al WHERE al.id.sid IN :sid AND al.id.ymd IN :date";
		TypedQueryWrapper<KrcdtDayTsLogon> tQuery=  this.queryProxy().query(query, KrcdtDayTsLogon.class);
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
	public void update(PCLogOnInfoOfDaily domain) {
		List<KrcdtDayTsLogon> entities = findQuery(domain.getEmployeeId(), domain.getYmd()).getList();
		if(entities.isEmpty()) {
			add(domain);
		} else {
			List<Integer> nos = domain.getTimeZone().getLogOnInfo().stream().map(c -> c.getWorkNo().v()).collect(Collectors.toList());
			List<KrcdtDayTsLogon> toDelete = entities.stream()
					.filter(c -> !nos.contains(c.id.pcLogNo)).collect(Collectors.toList());
			this.commandProxy().removeAll(toDelete);
			domain.getTimeZone().getLogOnInfo().stream().forEach(c -> {
				Optional<KrcdtDayTsLogon> entityOp = entities.stream().filter(e -> e.id.pcLogNo == c.getWorkNo().v())
																		.findFirst();
				if(entityOp.isPresent()) {
					KrcdtDayTsLogon entity = entityOp.get();
					entity.setData(c);
					commandProxy().update(entity);
				} else {
					commandProxy().insert(KrcdtDayTsLogon.from(domain.getEmployeeId(), domain.getYmd(), c));
				}
			});
		}
	}

	@Override
	public void add(PCLogOnInfoOfDaily domain) {
//		this.commandProxy().insertAll(KrcdtDayTsLogon.from(domain));
		try {
			Connection con = this.getEntityManager().unwrap(Connection.class);
			Statement statementI = con.createStatement();
			for(LogOnInfo logOnInfo : domain.getTimeZone().getLogOnInfo()){
				Integer logOff = logOnInfo.getLogOff().isPresent() ? logOnInfo.getLogOff().get().valueAsMinutes() : null;
				Integer logOn = logOnInfo.getLogOn().isPresent() ? logOnInfo.getLogOn().get().valueAsMinutes() : null;
				
				String insertTableSQL = "INSERT INTO KRCDT_DAY_TS_LOGON ( SID , YMD , PC_LOG_NO, LOGOFF_TIME , LOGON_TIME ) "
						+ "VALUES( '" + domain.getEmployeeId() + "' , '"
						+ domain.getYmd() + "' , "
						+ logOnInfo.getWorkNo().v() + " , "
						+ logOff + " , "
						+ logOn + " )";
				statementI.executeUpdate(JDBCUtil.toInsertWithCommonField(insertTableSQL));
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void removeByKey(String employeeId, GeneralDate baseDate) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_DAY_TS_LOGON Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + baseDate + "'" ;
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
//		this.getEntityManager().createQuery(REMOVE_BY_KEY).setParameter("employeeId", employeeId)
//				.setParameter("ymd", baseDate).executeUpdate();
//		this.getEntityManager().flush();
	}

	private TypedQueryWrapper<KrcdtDayTsLogon> findQuery(String employeeId, GeneralDate baseDate){
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDayTsLogon a ");
		builderString.append("WHERE a.id.sid = :employeeId ");
		builderString.append("AND a.id.ymd = :ymd ");
		return this.queryProxy().query(builderString.toString(), KrcdtDayTsLogon.class)
				.setParameter("employeeId", employeeId)
				.setParameter("ymd", baseDate);
	}

	private List<PCLogOnInfoOfDaily> toList(Stream<KrcdtDayTsLogon> query) {
		return query.collect(Collectors.groupingBy(c -> c.id.sid + c.id.ymd.toString(), Collectors.toList()))
				.entrySet().stream()
				.map(c -> new PCLogOnInfoOfDaily(c.getValue().get(0).id.sid, c.getValue().get(0).id.ymd,
						c.getValue().stream().map(pc -> pc.toDomain()).collect(Collectors.toList())))
				.collect(Collectors.toList());
	}
	
}
