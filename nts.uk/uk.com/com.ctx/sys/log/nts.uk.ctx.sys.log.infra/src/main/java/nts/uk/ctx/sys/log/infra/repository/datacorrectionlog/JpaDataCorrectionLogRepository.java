package nts.uk.ctx.sys.log.infra.repository.datacorrectionlog;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.log.dom.datacorrectionlog.DataCorrectionLogRepository;
import nts.uk.ctx.sys.log.infra.entity.datacorrectionlog.SrcdtDataCorrectionLog;
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
		if (targetDataType == null)
			return Collections.emptyList();
		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd";
		return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
				.setParameter("targetDataType", targetDataType.value).setParameter("listEmpId", listEmployeeId)
				.setParameter("startYmd", datePeriod.start()).setParameter("endYmd", datePeriod.end())
				.getList(c -> c.toDomainToView());
	}

	@Override
	public List<DataCorrectionLog> getAllLogData(TargetDataType targetDataType, List<String> listEmployeeId,
			YearMonthPeriod ymPeriod) {
		if (targetDataType == null)
			return Collections.emptyList();
		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId "
				+ "AND a.ymKey >= :startYm AND a.ymKey <= :endYm";
		return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
				.setParameter("targetDataType", targetDataType.value).setParameter("listEmpId", listEmployeeId)
				.setParameter("startYm", ymPeriod.start().v()).setParameter("endYm", ymPeriod.end().v())
				.getList(c -> c.toDomainToView());
	}

	@Override
	public List<DataCorrectionLog> getAllLogData(TargetDataType targetDataType, List<String> listEmployeeId,
			Year yearStart, Year yearEnd) {
		if (targetDataType == null)
			return Collections.emptyList();
		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId "
				+ "AND a.yKey >= :startY AND a.yKey <= :endY";
		return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
				.setParameter("targetDataType", targetDataType.value).setParameter("listEmpId", listEmployeeId)
				.setParameter("startY", yearStart.getValue()).setParameter("endY", yearEnd.getValue())
				.getList(c -> c.toDomainToView());
	}

	@Override
	public List<DataCorrectionLog> findByTargetAndDate(String operationId, List<String> listEmployeeId,
			DatePeriod period, TargetDataType targetDataType) {
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
					return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationId", operationId).setParameter("listEmpId", listEmployeeId)
							.getList(c -> c.toDomainToView());
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId = :operationId AND a.employeeId IN :listEmpId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey";
					return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationId", operationId).setParameter("listEmpId", listEmployeeId)
							.setParameter("startYmd", period.start()).setParameter("endYmd", period.end())
							.getList(c -> c.toDomainToView());
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
					return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationId", operationId)
							.setParameter("targetDataType", targetDataType.value)
							.setParameter("listEmpId", listEmployeeId).getList(c -> c.toDomainToView());
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId = :operationId AND a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey";
					return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationId", operationId)
							.setParameter("targetDataType", targetDataType.value)
							.setParameter("listEmpId", listEmployeeId).setParameter("startYmd", period.start())
							.setParameter("endYmd", period.end()).getList(c -> c.toDomainToView());
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

	@Override
	public List<DataCorrectionLog> findByTargetAndDate(List<String> operationIds, List<String> listEmployeeId,
			DatePeriod period, TargetDataType targetDataType) {
		List<DataCorrectionLog> results = new ArrayList<>();
		if (operationIds.isEmpty())
			return results;
		if (targetDataType == null) {
			if (listEmployeeId == null || listEmployeeId.isEmpty()) {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, 1000, subIdList -> {
						results.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
								.setParameter("operationIds", subIdList).getList(c -> c.toDomainToView()));
					});
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, 1000, subIdList -> {
						results.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
								.setParameter("operationIds", subIdList).setParameter("startYmd", period.start())
								.setParameter("endYmd", period.end()).getList(c -> c.toDomainToView()));
					});
				}
			} else {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.employeeId IN :listEmpId ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, 1000, subIdList -> {
						results.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList).setParameter("listEmpId", listEmployeeId)
							.getList(c -> c.toDomainToView()));
					});
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.employeeId IN :listEmpId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, 1000, subIdList -> {
						results.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList).setParameter("listEmpId", listEmployeeId)
							.setParameter("startYmd", period.start()).setParameter("endYmd", period.end())
							.getList(c -> c.toDomainToView()));
					});
				}
			}
		} else {
			if (listEmployeeId == null || listEmployeeId.isEmpty()) {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.pk.targetDataType = :targetDataType ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, 1000, subIdList -> {
						results.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList)
							.setParameter("targetDataType", targetDataType.value).getList(c -> c.toDomainToView()));
					});
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.pk.targetDataType = :targetDataType AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, 1000, subIdList -> {
						results.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList)
							.setParameter("targetDataType", targetDataType.value)
							.setParameter("startYmd", period.start()).setParameter("endYmd", period.end())
							.getList(c -> c.toDomainToView()));
					});
				}
			} else {
				if (period.start() == null) {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, 1000, subIdList -> {
						results.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList)
							.setParameter("targetDataType", targetDataType.value)
							.setParameter("listEmpId", listEmployeeId).getList(c -> c.toDomainToView()));
					});
				} else {
					String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :operationIds AND a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId AND a.pk.ymdKey >= :startYmd AND a.pk.ymdKey <= :endYmd ORDER BY a.employeeId, a.pk.ymdKey, a.ymKey, a.yKey,a.showOrder";
					CollectionUtil.split(operationIds, 1000, subIdList -> {
						results.addAll(this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
							.setParameter("operationIds", subIdList)
							.setParameter("targetDataType", targetDataType.value)
							.setParameter("listEmpId", listEmployeeId).setParameter("startYmd", period.start())
							.setParameter("endYmd", period.end()).getList(c -> c.toDomainToView()));
					});
				}
			}
		}
		return results;
	}

	@Override
	public List<DataCorrectionLog> getAllLogData(TargetDataType targetDataType, List<String> listEmployeeId,
			YearMonth ym, GeneralDate ymd) {
		if (targetDataType == null)
			return Collections.emptyList();
		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.targetDataType = :targetDataType AND a.employeeId IN :listEmpId AND a.pk.ymdKey = :startYmd AND a.ymKey = :endYm";
		return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
				.setParameter("targetDataType", targetDataType.value).setParameter("listEmpId", listEmployeeId)
				.setParameter("startYmd", ymd).setParameter("endYm", ym)
				.getList(c -> c.toDomainToView());
	}

}
