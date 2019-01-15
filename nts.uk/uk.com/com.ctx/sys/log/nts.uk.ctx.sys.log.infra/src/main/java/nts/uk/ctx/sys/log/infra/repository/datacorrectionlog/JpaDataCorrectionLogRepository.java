package nts.uk.ctx.sys.log.infra.repository.datacorrectionlog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.log.dom.datacorrectionlog.DataCorrectionLogRepository;
import nts.uk.ctx.sys.log.infra.entity.datacorrectionlog.SrcdtDataCorrectionLog;
import nts.uk.ctx.sys.log.infra.entity.datacorrectionlog.SrcdtDataCorrectionLogPk;
import nts.uk.shr.com.security.audittrail.correction.content.DataCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.TargetDataType;
import nts.uk.shr.com.security.audittrail.correction.processor.DataCorrectionLogWriter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class JpaDataCorrectionLogRepository extends JpaRepository
		implements DataCorrectionLogRepository, DataCorrectionLogWriter {

	@Override
	public List<DataCorrectionLog> getAllLogData(TargetDataType targetDataType, List<String> listEmployeeId,
			DatePeriod datePeriod) {
		if (targetDataType == null || listEmployeeId.isEmpty())
			return Collections.emptyList();
		Connection con = this.getEntityManager().unwrap(Connection.class);
//		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd";
		List<DataCorrectionLog> resultList = new ArrayList<>();
		CollectionUtil.split(listEmployeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String listEmp = "(";
			for(int i = 0; i < subList.size(); i++){
				listEmp += "'"+ subList.get(i) +"',";
			}
			// remove last , in string and add )
			listEmp = listEmp.substring(0, listEmp.length() - 1) + ")";
			String query = "SELECT OPERATION_ID, USER_ID, TARGET_DATA_TYPE, ITEM_ID, USER_NAME, SID, YMD_KEY, YM_KEY, Y_KEY, STRING_KEY, CORRECTION_ATTR, ITEM_NAME, RAW_VALUE_BEFORE_ID, VIEW_VALUE_BEFORE, RAW_VALUE_AFTER_ID, VIEW_VALUE_AFTER, VALUE_DATA_TYPE, SHOW_ORDER, NOTE"
					+ " FROM SRCDT_DATA_CORRECTION_LOG "
					+ " WHERE TARGET_DATA_TYPE = "+ "'" + targetDataType.value + "'" 
					+ " AND SID IN " + listEmp 
					+ " AND YMD_KEY >= " + "'" + datePeriod.start() + "'" 
					+ " AND YMD_KEY <= " + "'" + datePeriod.end() + "'";
			try {
				ResultSet rs = con.createStatement().executeQuery(query);
				while (rs.next()) {
					String operationId = rs.getString("OPERATION_ID");
					String userId = rs.getString("USER_ID");
					String sId = rs.getString("SID");
					String userName = rs.getString("USER_NAME");
					int targetDtType = rs.getInt("TARGET_DATA_TYPE");
					String stringKey = rs.getString("STRING_KEY");
					int correctionAttr = rs.getInt("CORRECTION_ATTR");
					String itemId = rs.getString("ITEM_ID");
					String itemName = rs.getString("ITEM_NAME");
					String viewValueBefore = rs.getString("VIEW_VALUE_BEFORE");
					String viewValueAfter = rs.getString("VIEW_VALUE_AFTER");
					String rawValueBefore = rs.getString("RAW_VALUE_BEFORE_ID");
					String rawValueAfter = rs.getString("RAW_VALUE_AFTER_ID");
					int showOrder = rs.getInt("SHOW_ORDER");
					String note = rs.getString("NOTE");
					GeneralDate ymdKey = GeneralDate.fromString(rs.getString("YMD_KEY"), "yyyy-MM-dd");
					Integer ymKey = Integer.valueOf(rs.getInt("YM_KEY"));
					Integer yKey = Integer.valueOf(rs.getInt("Y_KEY"));
					Integer valueType = Integer.valueOf(rs.getInt("VALUE_DATA_TYPE"));
					
					SrcdtDataCorrectionLogPk pk = new SrcdtDataCorrectionLogPk(operationId, userId, targetDtType,
							itemId, ymdKey);
					SrcdtDataCorrectionLog srcdtDataCorrectionLog = new SrcdtDataCorrectionLog(pk, userName, sId, ymKey,
							yKey, stringKey, correctionAttr, itemName, rawValueBefore, viewValueBefore, rawValueAfter,
							viewValueAfter, valueType, showOrder, note);
					
					resultList.add(srcdtDataCorrectionLog.toDomainToView());
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		return resultList;
	}

	@Override
	public List<DataCorrectionLog> getAllLogData(TargetDataType targetDataType, List<String> listEmployeeId,
			YearMonthPeriod ymPeriod) {
		if (targetDataType == null || listEmployeeId.isEmpty())
			return Collections.emptyList();
		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId "
				+ "AND a.ymKey >= :startYm AND a.ymKey <= :endYm";
		List<DataCorrectionLog> resultList = new ArrayList<>();
		CollectionUtil.split(listEmployeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
					.setParameter("targetDataType", targetDataType.value).setParameter("listEmpId", subList)
					.setParameter("startYm", ymPeriod.start().v()).setParameter("endYm", ymPeriod.end().v())
					.getList(c -> c.toDomainToView()));
		});
		return resultList;
	}

	@Override
	public List<DataCorrectionLog> getAllLogData(TargetDataType targetDataType, List<String> listEmployeeId,
			Year yearStart, Year yearEnd) {
		if (targetDataType == null || listEmployeeId.isEmpty())
			return Collections.emptyList();
		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId "
				+ "AND a.yKey >= :startY AND a.yKey <= :endY";
		List<DataCorrectionLog> resultList = new ArrayList<>();
		CollectionUtil.split(listEmployeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
					.setParameter("targetDataType", targetDataType.value).setParameter("listEmpId", subList)
					.setParameter("startY", yearStart.getValue()).setParameter("endY", yearEnd.getValue())
					.getList(c -> c.toDomainToView()));
		});
		return resultList;
	}
	
	private static Comparator<SrcdtDataCorrectionLog> SORT_BY_FOUR_FIELDS = (o1, o2) -> {
		int tmp = o1.employeeId.compareTo(o2.employeeId);
		if (tmp != 0) return tmp;
		tmp = o1.pk.ymdKey.compareTo(o2.pk.ymdKey);
		if (tmp != 0) return tmp;
		tmp = o1.ymKey.compareTo(o2.ymKey);
		if (tmp != 0) return tmp;
		return o1.yKey.compareTo(o2.yKey);
	};

	@Override
	public List<DataCorrectionLog> findByTargetAndDate(String operationId, List<String> listEmployeeId,
			DatePeriod period, TargetDataType targetDataType) {
		List<SrcdtDataCorrectionLog> entities;
		if (targetDataType == null) {
			if (listEmployeeId == null || listEmployeeId.isEmpty()) {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId = :operationId ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey";
					return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationId", operationId).getList(c -> c.toDomainToView());
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId = :operationId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey";
					return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationId", operationId).setParameter("startYmd", period.start())
							.setParameter("endYmd", period.end()).getList(c -> c.toDomainToView());
				}
			} else {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId = :operationId AND a.employeeId IN :listEmpId ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey";
					entities = new ArrayList<>();
					CollectionUtil.split(listEmployeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationId", operationId).setParameter("listEmpId", subList)
							.getList());
					});
					entities.sort(SORT_BY_FOUR_FIELDS);
					return entities.stream().map(c -> c.toDomainToView()).collect(Collectors.toList());
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId = :operationId AND a.employeeId IN :listEmpId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey";
					entities = new ArrayList<>();
					CollectionUtil.split(listEmployeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
								.setParameter("operationId", operationId).setParameter("listEmpId", subList)
								.setParameter("startYmd", period.start()).setParameter("endYmd", period.end())
								.getList());
					});
					entities.sort(SORT_BY_FOUR_FIELDS);
					return entities.stream().map(c -> c.toDomainToView()).collect(Collectors.toList());
				}
			}
		} else {
			if (listEmployeeId == null || listEmployeeId.isEmpty()) {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId = :operationId AND a.pk.targetDataType = :targetDataType ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey";
					return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationId", operationId)
							.setParameter("targetDataType", targetDataType.value).getList(c -> c.toDomainToView());
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId = :operationId AND a.pk.targetDataType = :targetDataType AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey";
					return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationId", operationId)
							.setParameter("targetDataType", targetDataType.value)
							.setParameter("startYmd", period.start()).setParameter("endYmd", period.end())
							.getList(c -> c.toDomainToView());
				}
			} else {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId = :operationId AND a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey";
					entities = new ArrayList<>();
					CollectionUtil.split(listEmployeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationId", operationId)
							.setParameter("targetDataType", targetDataType.value)
							.setParameter("listEmpId", subList).getList());
					});
					entities.sort(SORT_BY_FOUR_FIELDS);
					return entities.stream().map(c -> c.toDomainToView()).collect(Collectors.toList());
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId = :operationId AND a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey";
					entities = new ArrayList<>();
					CollectionUtil.split(listEmployeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
								.setParameter("operationId", operationId)
								.setParameter("targetDataType", targetDataType.value)
								.setParameter("listEmpId", subList).setParameter("startYmd", period.start())
								.setParameter("endYmd", period.end()).getList());
					});
					entities.sort(SORT_BY_FOUR_FIELDS);
					return entities.stream().map(c -> c.toDomainToView()).collect(Collectors.toList());
				}
			}
		}

	}

	@Override
	public void save(List<DataCorrectionLog> dataCorrectionLog) {
		List<SrcdtDataCorrectionLog> entities = dataCorrectionLog.stream()
				.map(x -> SrcdtDataCorrectionLog.fromDomain(x)).collect(Collectors.toList());
		this.commandProxy().insertAll(entities);
	}
	
	private static Comparator<SrcdtDataCorrectionLog> SORT_BY_FIVE_FIELDS = (o1, o2) -> {
		int tmp = o1.employeeId.compareTo(o2.employeeId);
		if (tmp != 0) return tmp;
		tmp = o1.pk.ymdKey.compareTo(o2.pk.ymdKey);
		if (tmp != 0) return tmp;
		tmp = o1.ymKey.compareTo(o2.ymKey);
		if (tmp != 0) return tmp;
		tmp = o1.yKey.compareTo(o2.yKey);
		if (tmp != 0) return tmp;
		return o1.showOrder - o2.showOrder;
	};

	@Override
	public List<DataCorrectionLog> findByTargetAndDate(List<String> operationIds, List<String> listEmployeeId,
			DatePeriod period, TargetDataType targetDataType) {
		List<DataCorrectionLog> results = new ArrayList<>();
		if (operationIds.isEmpty())
			return results;
		List<SrcdtDataCorrectionLog> entities = new ArrayList<>();
		if (targetDataType == null) {
			if (listEmployeeId == null || listEmployeeId.isEmpty()) {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
								.setParameter("operationIds", subIdList).getList());
					});
					entities.sort(SORT_BY_FIVE_FIELDS);
					results.addAll(entities.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
								.setParameter("operationIds", subIdList).setParameter("startYmd", period.start())
								.setParameter("endYmd", period.end()).getList());
					});
					entities.sort(SORT_BY_FIVE_FIELDS);
					results.addAll(entities.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
				}
			} else {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.employeeId IN :listEmpId ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList).setParameter("listEmpId", listEmployeeId)
							.getList());
					});
					entities.sort(SORT_BY_FIVE_FIELDS);
					results.addAll(entities.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.employeeId IN :listEmpId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList).setParameter("listEmpId", listEmployeeId)
							.setParameter("startYmd", period.start()).setParameter("endYmd", period.end())
							.getList());
					});
					entities.sort(SORT_BY_FIVE_FIELDS);
					results.addAll(entities.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
				}
			}
		} else {
			if (listEmployeeId == null || listEmployeeId.isEmpty()) {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.pk.targetDataType = :targetDataType ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList)
							.setParameter("targetDataType", targetDataType.value).getList());
					});
					entities.sort(SORT_BY_FIVE_FIELDS);
					results.addAll(entities.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.pk.targetDataType = :targetDataType AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList)
							.setParameter("targetDataType", targetDataType.value)
							.setParameter("startYmd", period.start()).setParameter("endYmd", period.end())
							.getList());
					});
					entities.sort(SORT_BY_FIVE_FIELDS);
					results.addAll(entities.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
				}
			} else {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList)
							.setParameter("targetDataType", targetDataType.value)
							.setParameter("listEmpId", listEmployeeId).getList());
					});
					entities.sort(SORT_BY_FIVE_FIELDS);
					results.addAll(entities.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIdList -> {
						entities.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList)
							.setParameter("targetDataType", targetDataType.value)
							.setParameter("listEmpId", listEmployeeId).setParameter("startYmd", period.start())
							.setParameter("endYmd", period.end()).getList());
					});
					entities.sort(SORT_BY_FIVE_FIELDS);
					results.addAll(entities.stream().map(c -> c.toDomain()).collect(Collectors.toList()));
				}
			}
		}
		return results;
	}

	@Override
	public List<DataCorrectionLog> getAllLogData(TargetDataType targetDataType, List<String> listEmployeeId,
			YearMonth ym, GeneralDate ymd) {
		if (targetDataType == null || listEmployeeId.isEmpty())
			return Collections.emptyList();
		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId AND a.pk.ymdKey = :startYmd AND a.ymKey = :endYm";
		List<DataCorrectionLog> resultList = new ArrayList<>();
		CollectionUtil.split(listEmployeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
				.setParameter("targetDataType", targetDataType.value).setParameter("listEmpId", subList)
				.setParameter("startYmd", ymd).setParameter("endYm", ym)
				.getList(c -> c.toDomainToView()));
		});
		return resultList;
	}
	
	

}
