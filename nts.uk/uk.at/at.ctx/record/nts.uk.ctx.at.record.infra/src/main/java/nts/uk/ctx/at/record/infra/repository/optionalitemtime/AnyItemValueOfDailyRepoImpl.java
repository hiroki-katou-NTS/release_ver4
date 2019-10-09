package nts.uk.ctx.at.record.infra.repository.optionalitemtime;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemAmount;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemNo;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemTime;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemTimes;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValue;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.infra.entity.daily.anyitem.KrcdtDayAnyItemValueMerge;
import nts.uk.ctx.at.record.infra.entity.daily.time.KrcdtDayTimePK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AnyItemValueOfDailyRepoImpl extends JpaRepository implements AnyItemValueOfDailyRepo {
	
	private static final String REMOVE_BY_EMPLOYEE;
	
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDayAnyItemValueMerge a ");
		builderString.append("WHERE a.krcdtDayTimePk.employeeID = :employeeId ");
		builderString.append("AND a.krcdtDayTimePk.generalDate = :processingDate ");
		REMOVE_BY_EMPLOYEE = builderString.toString();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<AnyItemValueOfDaily> find(String employeeId, GeneralDate baseDate) {
		Optional<KrcdtDayAnyItemValueMerge> anyEntity = this.queryProxy()
				.find(new KrcdtDayTimePK(employeeId, baseDate), KrcdtDayAnyItemValueMerge.class);
		if (anyEntity.isPresent()) {
			return Optional.of(anyEntity.get().toDomainAnyItemValueOfDaily());
		}
		return Optional.empty();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<AnyItemValueOfDaily> find(String employeeId, List<GeneralDate> baseDate) {
		if(baseDate.isEmpty()) {
			return new ArrayList<>();
		}
		StringBuilder query = new StringBuilder("SELECT op FROM KrcdtDayAnyItemValueMerge op");
		query.append(" WHERE op.krcdtDayTimePk.employeeID = :empId");
		query.append(" AND op.krcdtDayTimePk.generalDate IN :date");
		
		List<KrcdtDayAnyItemValueMerge> resultList = new ArrayList<>();
		CollectionUtil.split(baseDate, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstDate -> {
			resultList.addAll(queryProxy()
				.query(query.toString(), KrcdtDayAnyItemValueMerge.class)
				.setParameter("empId", employeeId)
				.setParameter("date", lstDate)
				.getList());
		});
		if(!resultList.isEmpty()) {
			return resultList.stream().map(op -> op.toDomainAnyItemValueOfDaily()).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<AnyItemValueOfDaily> find(String employeeId) {
		StringBuilder query = new StringBuilder("SELECT op FROM KrcdtDayAnyItemValueMerge op");
		query.append(" WHERE op.krcdtDayTimePk.employeeID = :empId");
		List<KrcdtDayAnyItemValueMerge> result = queryProxy()
				.query(query.toString(), KrcdtDayAnyItemValueMerge.class)
				.setParameter("empId", employeeId)
				.getList();
		if(!result.isEmpty()) {
			return result.stream().map(op -> op.toDomainAnyItemValueOfDaily()).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	@SneakyThrows
	public List<AnyItemValueOfDaily> finds(List<String> employeeIds, DatePeriod baseDate) {
		List<AnyItemValueOfDaily> result = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds ->{
			try (PreparedStatement stmt = this.connection().prepareStatement(
						"SELECT * FROM KRCDT_DAY_ANYITEMVALUE_MERGE op" 
						+" WHERE YMD >= ?"
						+" AND YMD <= ?"
						+" AND SID IN (" + empIds.stream().map(s -> "?").collect(Collectors.joining(",")) + ")")
				) {

				stmt.setDate(1, Date.valueOf(baseDate.start().toLocalDate()));
				stmt.setDate(2, Date.valueOf(baseDate.end().toLocalDate()));
				for(int i = 0 ; i < empIds.size() ; i++) {
					stmt.setString(i+3,empIds.get(i));
				}
				
				result.addAll(new NtsResultSet(stmt.executeQuery()).getList(rec ->{
					List<AnyItemValue> value = new ArrayList<>();
					for (int i = 1; i <= 200; i++){
						Double count = rec.getDouble("COUNT_VALUE_"+i);
						Integer money = rec.getInt("MONEY_VALUE_"+i);
						Integer Time = rec.getInt("TIME_VALUE_"+i);
						value.add(new AnyItemValue(new AnyItemNo(i), 
													Optional.ofNullable(count == null ? null : new AnyItemTimes(BigDecimal.valueOf(count))),
													Optional.ofNullable(money == null ? null : new AnyItemAmount(money)),
													Optional.ofNullable(Time == null ? null : new AnyItemTime(Time))
													));
					}
					return new AnyItemValueOfDaily(rec.getString("SID"), rec.getGeneralDate("YMD"), value);
				}));
				
			}catch(Exception e) {
				throw new RuntimeException(e);
			}
		});
		return result;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<AnyItemValueOfDaily> finds(Map<String, List<GeneralDate>> param) {
		List<AnyItemValueOfDaily> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT op FROM KrcdtDayAnyItemValueMerge op");
		query.append(" WHERE op.krcdtDayTimePk.employeeID IN :employeeId");
		query.append(" AND op.krcdtDayTimePk.generalDate IN :date");
		TypedQueryWrapper<KrcdtDayAnyItemValueMerge> tQuery = this.queryProxy()
				.query(query.toString(), KrcdtDayAnyItemValueMerge.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery
					.setParameter("employeeId", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList().stream().filter(c -> p.get(c.getKrcdtDayTimePk().employeeID).contains(c.getKrcdtDayTimePk().generalDate))
					.map(op -> op.toDomainAnyItemValueOfDaily()).collect(Collectors.toList()));
		});
		return result;
	}

	@Override
	public void update(AnyItemValueOfDaily domain) {
		if (domain == null) return;
		
		// キー
		val key = new KrcdtDayTimePK(domain.getEmployeeId(), domain.getYmd());

		// 登録・更新
		KrcdtDayAnyItemValueMerge entity = this.getEntityManager().find(KrcdtDayAnyItemValueMerge.class, key);
		if (entity == null) {
			entity = new KrcdtDayAnyItemValueMerge();
			entity.setKrcdtDayTimePk(key);
			entity.toEntityAnyItemValueOfDaily(domain);
			this.getEntityManager().persist(entity);
		}
		else {
			// 既存のデータがあれば、消してから更新する
			entity.clearAllValues();
			entity.toEntityAnyItemValueOfDaily(domain);
		}
	}

	@Override
	public void add(AnyItemValueOfDaily domain) {
		if (domain == null) return;
		this.persistAndUpdate(domain);
	}

	@Override
	public void persistAndUpdate(AnyItemValueOfDaily domain) {
		if (domain == null) return;
		
		// キー
		val key = new KrcdtDayTimePK(domain.getEmployeeId(), domain.getYmd());

		// 登録・更新
		KrcdtDayAnyItemValueMerge entity = this.getEntityManager().find(KrcdtDayAnyItemValueMerge.class, key);
		if (entity == null) {
			entity = new KrcdtDayAnyItemValueMerge();
			entity.setKrcdtDayTimePk(key);
			entity.toEntityAnyItemValueOfDaily(domain);
			this.getEntityManager().persist(entity);
		}
		else {
			entity.toEntityAnyItemValueOfDaily(domain);
		}
	}
	
	@Override
	public void remove(AnyItemValueOfDaily domain) {
		this.removeWithJdbc(domain.getEmployeeId(), domain.getYmd());
	}
	
	@SneakyThrows
	private void removeWithJdbc(String employeeId, GeneralDate baseDate) {
		try (val statement = this.connection().prepareStatement(
				"DELETE FROM KRCDT_DAY_ANYITEMVALUE_MERGE"
				+ " WHERE SID = ? AND YMD = ?")) {
			statement.setString(1, employeeId);
			statement.setDate(2, Date.valueOf(baseDate.localDate()));
			statement.executeUpdate();
		}
	}
	
	@Override
	public void removeByEmployeeIdAndDate(String employeeId, GeneralDate processingDate) {
		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEE)
				.setParameter("employeeId", employeeId)
				.setParameter("processingDate", processingDate)
				.executeUpdate();
		this.getEntityManager().flush();
	}

	// fix bug 107004
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void deleteAnyItemValueOfDaily(String employeeId, GeneralDate ymd) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_DAY_ANYITEMVALUE_MERGE Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + ymd + "'" ;
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
//		this.getEntityManager().createQuery(REMOVE_BY_KEY).setParameter("employeeId", employeeId)
//				.setParameter("ymd", baseDate).executeUpdate();
//		this.getEntityManager().flush();
	}
}
