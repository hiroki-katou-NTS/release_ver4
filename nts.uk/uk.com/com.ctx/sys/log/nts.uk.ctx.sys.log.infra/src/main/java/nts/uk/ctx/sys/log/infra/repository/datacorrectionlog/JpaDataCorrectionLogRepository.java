package nts.uk.ctx.sys.log.infra.repository.datacorrectionlog;

import java.time.Year;
import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.log.dom.datacorrectionlog.DataCorrectionLogRepository;
import nts.uk.ctx.sys.log.infra.entity.datacorrectionlog.SrcdtDataCorrectionLog;
import nts.uk.shr.com.security.audittrail.correction.content.DataCorrectionLog;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class JpaDataCorrectionLogRepository extends JpaRepository implements DataCorrectionLogRepository {

	@Override
	public List<DataCorrectionLog> getAllLogData(List<String> listOperationId, List<String> listEmployeeId, DatePeriod datePeriod) {
		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :listOperationId AND a.employeeId IN :listEmpId AND a.ymdKey >= :startYmd AND a.ymdKey <= :endYmd";
		return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
				.setParameter("listOperationId", listOperationId)
				.setParameter("listEmpId", listEmployeeId)
				.setParameter("startYmd", datePeriod.start())
				.setParameter("endYmd", datePeriod.end()).getList(c -> c.toDomainToView());
	}

	@Override
	public List<DataCorrectionLog> getAllLogData(List<String> listOperationId, List<String> listEmployeeId, YearMonthPeriod ymPeriod) {
		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :listOperationId AND a.employeeId IN :listEmpId "
				+ "AND a.ymKey >= :startYm AND a.ymKey <= :endYm";
		return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
				.setParameter("listOperationId", listOperationId)
				.setParameter("listEmpId", listEmployeeId)
				.setParameter("startYm", ymPeriod.start().v())
				.setParameter("endYm", ymPeriod.end().v()).getList(c -> c.toDomainToView());
	}

	@Override
	public List<DataCorrectionLog> getAllLogData(List<String> listOperationId, List<String> listEmployeeId, Year yearStart, Year yearEnd) {
		String query = "SELECT a FROM SrcdtDataCorrectionLog a WHERE a.pk.operationId IN :listOperationId AND a.employeeId IN :listEmpId "
				+ "AND a.yKey >= :startY AND a.yKey <= :endY";
		return this.queryProxy().query(query, SrcdtDataCorrectionLog.class)
				.setParameter("listOperationId", listOperationId)
				.setParameter("listEmpId", listEmployeeId)
				.setParameter("startYm", yearStart.getValue())
				.setParameter("endYm", yearEnd.getValue()).getList(c -> c.toDomainToView());
	}

}
