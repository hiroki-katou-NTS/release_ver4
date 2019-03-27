package nts.uk.ctx.at.record.infra.repository.editstate;

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
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.infra.entity.editstate.KrcdtDailyRecEditSet;
import nts.uk.ctx.at.record.infra.entity.editstate.KrcdtDailyRecEditSetPK;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class JpaEditStateOfDailyPerformanceRepository extends JpaRepository
		implements EditStateOfDailyPerformanceRepository {

//	private static final String REMOVE_BY_EMPLOYEE;

	private static final String DEL_BY_LIST_KEY;
	
//	private static final String DEL_BY_LIST_ITEM_ID;

	static {
		StringBuilder builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDailyRecEditSet a ");
//		builderString.append("WHERE a.krcdtDailyRecEditSetPK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd = :ymd ");
//		REMOVE_BY_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDailyRecEditSet a ");
		builderString.append("WHERE a.krcdtDailyRecEditSetPK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd IN :processingYmds ");
		DEL_BY_LIST_KEY = builderString.toString();
		
//		builderString = new StringBuilder();
//		builderString.append("DELETE ");
//		builderString.append("FROM KrcdtDailyRecEditSet a ");
//		builderString.append("WHERE a.krcdtDailyRecEditSetPK.employeeId = :employeeId ");
//		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd = :ymd ");
//		builderString.append("AND a.krcdtDailyRecEditSetPK.attendanceItemId IN :itemIdList ");
//		DEL_BY_LIST_ITEM_ID = builderString.toString();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void delete(String employeeId, GeneralDate ymd) {
		
		Connection con = this.getEntityManager().unwrap(Connection.class);
		String sqlQuery = "Delete From KRCDT_DAILY_REC_EDIT_SET Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + ymd + "'" ;
		try {
			con.createStatement().executeUpdate(sqlQuery);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
//		this.getEntityManager().createQuery(REMOVE_BY_EMPLOYEE).setParameter("employeeId", employeeId)
//				.setParameter("ymd", ymd).executeUpdate();
	}

	@Override
	public void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> processingYmds) {
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstEmployeeIds -> {
			CollectionUtil.split(processingYmds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, ymds -> {
				this.getEntityManager().createQuery(DEL_BY_LIST_KEY)
					.setParameter("employeeIds", lstEmployeeIds)
					.setParameter("processingYmds", ymds)
					.executeUpdate();
			});
		});
		this.getEntityManager().flush();
	}

	@Override
	public void deleteBy(List<String> employeeIds, List<GeneralDate> processingYmds) {
		/*StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtDailyRecEditSet a ");
		builderString.append("WHERE a.krcdtDailyRecEditSetPK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd IN :processingYmds ");
		//builderString.append("AND a.krcdtDailyRecEditSetPK.attendanceItemId IN :attendanceItemIds ");
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstEmployeeIds -> {
			CollectionUtil.split(processingYmds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, ymds -> {
				this.getEntityManager().createQuery(builderString.toString())
					.setParameter("employeeIds", lstEmployeeIds)
					.setParameter("processingYmds", ymds)
					//.setParameter("attendanceItemIds", itemIdList)
					.executeUpdate();
			});
		});*/
		//this.getEntityManager().flush();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistEmployeeIds -> {
			CollectionUtil.split(processingYmds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, sublistYMDs -> {
				internalDelete(sublistEmployeeIds, sublistYMDs);
			});
		});
	}
	
	@SneakyThrows
	private void internalDelete(List<String> sublistEmployeeIds, List<GeneralDate> sublistYMDs) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE FROM KRCDT_DAILY_REC_EDIT_SET ");
		builderString.append(" WHERE SID IN ( ");
		builderString.append(sublistEmployeeIds.stream().map(c -> "?").collect(Collectors.joining(", ")));
		builderString.append(" ) AND YMD IN ( ");
		builderString.append(sublistYMDs.stream().map(c -> "?").collect(Collectors.joining(", ")) + ")");
		//builderString.append(" AND ATTENDANCE_ITEM_ID IN (" + itemIdList.stream().map(c -> "?").collect(Collectors.joining(", ")) +") ");
		try (PreparedStatement ps = this.connection().prepareStatement(builderString.toString())) {
			for (int i = 0; i < sublistEmployeeIds.size(); i++) {
				ps.setString(i + 1, sublistEmployeeIds.get(i));
			}
			for (int i = 0; i < sublistYMDs.size(); i++) {
				ps.setDate(i + 1 + sublistEmployeeIds.size(), Date.valueOf(sublistYMDs.get(i).localDate()));
			}
			/*for (int i = 0; i < sublistYMDs.size(); i++) {
				ps.setInt(i + 1 + sublistEmployeeIds.size() + sublistYMDs.size(), itemIdList.get(i));
			}*/
			ps.executeUpdate();
		}

	}

	@Override
	public void add(List<EditStateOfDailyPerformance> editStates) {
		this.commandProxy().insertAll(
						editStates.stream()
								.map(c -> new KrcdtDailyRecEditSet(new KrcdtDailyRecEditSetPK(c.getEmployeeId(),
										c.getYmd(), c.getAttendanceItemId()), c.getEditStateSetting().value))
								.collect(Collectors.toList()));
	}

	@Override
	public List<EditStateOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDailyRecEditSet a ");
		builderString.append("WHERE a.krcdtDailyRecEditSetPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd = :ymd ");
		return this.queryProxy().query(builderString.toString(), KrcdtDailyRecEditSet.class)
				.setParameter("employeeId", employeeId).setParameter("ymd", ymd)
				.getList(c -> toDomain(c));
	}

	private EditStateOfDailyPerformance toDomain(KrcdtDailyRecEditSet c) {
		return new EditStateOfDailyPerformance(c.krcdtDailyRecEditSetPK.employeeId,
				c.krcdtDailyRecEditSetPK.attendanceItemId, c.krcdtDailyRecEditSetPK.processingYmd,
				EnumAdaptor.valueOf(c.editState, EditStateSetting.class));
	}

	@Override
	public void updateByKey(List<EditStateOfDailyPerformance> editStates) {
		this.commandProxy().updateAll(
				editStates.stream()
						.map(c -> new KrcdtDailyRecEditSet(new KrcdtDailyRecEditSetPK(c.getEmployeeId(),
								c.getYmd(), c.getAttendanceItemId()), c.getEditStateSetting().value))
						.collect(Collectors.toList()));
	}

	@Override
	public List<EditStateOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd) {
		List<EditStateOfDailyPerformance> result = new ArrayList<>();
		
		CollectionUtil.split(employeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, empIds -> {
			result.addAll(internalQuery(ymd, empIds));
		});
		return result;
	}
	
	@SneakyThrows
	private List<EditStateOfDailyPerformance> internalQuery(DatePeriod baseDate, List<String> empIds) {
		String subEmp = NtsStatement.In.createParamsString(empIds);
		StringBuilder query = new StringBuilder("SELECT SID, YMD, ATTENDANCE_ITEM_ID, EDIT_STATE FROM KRCDT_DAILY_REC_EDIT_SET");
		query.append(" WHERE YMD <= ? AND YMD >= ? ");
		query.append(" AND SID IN (" + subEmp + ")");
		try (val stmt = this.connection().prepareStatement(query.toString())){
			stmt.setDate(1, Date.valueOf(baseDate.end().localDate()));
			stmt.setDate(2, Date.valueOf(baseDate.start().localDate()));
			for (int i = 0; i < empIds.size(); i++) {
				stmt.setString(i + 3, empIds.get(i));
			}
			return new NtsResultSet(stmt.executeQuery()).getList(rec -> {
				return new EditStateOfDailyPerformance(rec.getString("SID"), rec.getInt("ATTENDANCE_ITEM_ID"),
						rec.getGeneralDate("YMD"), EnumAdaptor.valueOf(rec.getInt("EDIT_STATE"), EditStateSetting.class));
			});
		}
	}

	@Override
	public List<EditStateOfDailyPerformance> finds(Map<String, List<GeneralDate>> param) {
		List<EditStateOfDailyPerformance> result = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT a FROM KrcdtDailyRecEditSet a ");
		query.append("WHERE a.krcdtDailyRecEditSetPK.employeeId IN :employeeId ");
		query.append("AND a.krcdtDailyRecEditSetPK.processingYmd IN :date");
		TypedQueryWrapper<KrcdtDailyRecEditSet> tQuery=  this.queryProxy().query(query.toString(), KrcdtDailyRecEditSet.class);
		CollectionUtil.split(param, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, p -> {
			result.addAll(tQuery.setParameter("employeeId", p.keySet())
					.setParameter("date", p.values().stream().flatMap(List::stream).collect(Collectors.toSet()))
					.getList().stream()
					.filter(c -> p.get(c.krcdtDailyRecEditSetPK.employeeId).contains(c.krcdtDailyRecEditSetPK.processingYmd))
					.collect(Collectors.groupingBy(
							c -> c.krcdtDailyRecEditSetPK.employeeId + c.krcdtDailyRecEditSetPK.processingYmd.toString()))
					.entrySet().stream().map(c -> c.getValue().stream().map(x -> toDomain(x)).collect(Collectors.toList()))
					.flatMap(List::stream).collect(Collectors.toList()));
		});
		return result;
	}

	@Override
	public void addAndUpdate(List<EditStateOfDailyPerformance> editStates) {
		editStates.forEach(x -> {
			if(this.findByKeyId(x.getEmployeeId(), x.getYmd(), x.getAttendanceItemId()).isPresent()){
				this.commandProxy().update(new KrcdtDailyRecEditSet(new KrcdtDailyRecEditSetPK(x.getEmployeeId(),
						x.getYmd(), x.getAttendanceItemId()), x.getEditStateSetting().value));
			}else{
				KrcdtDailyRecEditSet entity = new KrcdtDailyRecEditSet(new KrcdtDailyRecEditSetPK(x.getEmployeeId(),
						x.getYmd(), x.getAttendanceItemId()), x.getEditStateSetting().value);
				this.commandProxy().insert(entity);
			}
		});
	}

	@Override
	public Optional<EditStateOfDailyPerformance> findByKeyId(String employeeId, GeneralDate ymd, Integer id) {
		return this.queryProxy().find(new KrcdtDailyRecEditSetPK(employeeId, ymd, id), KrcdtDailyRecEditSet.class)
				.map(x -> new EditStateOfDailyPerformance(employeeId, id, ymd, EnumAdaptor.valueOf(x.editState, EditStateSetting.class)));
	}

	@Override
	public void updateByKeyFlush(List<EditStateOfDailyPerformance> editStates) {
		this.addAndUpdate(editStates);
		this.getEntityManager().flush();
	}

	@Override
	public List<EditStateOfDailyPerformance> findByItems(String employeeId, GeneralDate ymd, List<Integer> ids) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDailyRecEditSet a ");
		builderString.append("WHERE a.krcdtDailyRecEditSetPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd = :ymd ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.attendanceItemId IN :items ");
		
		List<EditStateOfDailyPerformance> resultList = new ArrayList<>();
		CollectionUtil.split(ids, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(builderString.toString(), KrcdtDailyRecEditSet.class)
					.setParameter("employeeId", employeeId)
					.setParameter("ymd", ymd)
					.setParameter("items", subList)
					.getList(c -> toDomain(c)));
		});
		
		return resultList;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void deleteByListItemId(String employeeId, GeneralDate ymd, List<Integer> itemIdList) {
//		this.getEntityManager().createQuery(DEL_BY_LIST_ITEM_ID).setParameter("employeeId", employeeId)
//		.setParameter("ymd", ymd).setParameter("itemIdList", itemIdList).executeUpdate();
//		this.getEntityManager().flush();
		
		CollectionUtil.split(itemIdList, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String listItemIdString = "(";
			for(int i = 0; i < subList.size(); i++){
				listItemIdString += "'"+ subList.get(i) +"',";
			}
			// remove last , in string and add )
			listItemIdString = listItemIdString.substring(0, listItemIdString.length() - 1) + ")";
			
			Connection con = this.getEntityManager().unwrap(Connection.class);
			String sqlQuery = "Delete From KRCDT_DAILY_REC_EDIT_SET Where SID = " + "'" + employeeId + "'" + " and YMD = " + "'" + ymd + "'"
					+ " and ATTENDANCE_ITEM_ID IN " + listItemIdString;
			try {
				con.createStatement().executeUpdate(sqlQuery);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public List<EditStateOfDailyPerformance> findByEditState(String sid, GeneralDate ymd, List<Integer> ids,
			EditStateSetting editState) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDailyRecEditSet a ");
		builderString.append("WHERE a.krcdtDailyRecEditSetPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd = :ymd ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.attendanceItemId IN :items ");
		builderString.append("AND a.editState = :editState ");
		builderString.append("ORDER BY a.krcdtDailyRecEditSetPK.attendanceItemId");
		return this.queryProxy().query(builderString.toString(), KrcdtDailyRecEditSet.class)
				.setParameter("employeeId", sid)
				.setParameter("ymd", ymd)
				.setParameter("items", ids)
				.setParameter("editState", editState.value)
				.getList(c -> toDomain(c));
	}

}
