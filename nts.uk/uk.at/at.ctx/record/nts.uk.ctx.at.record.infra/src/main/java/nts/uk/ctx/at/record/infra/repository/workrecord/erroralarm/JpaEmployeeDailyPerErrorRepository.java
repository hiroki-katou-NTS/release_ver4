package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KrcdtSyainDpErList;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaEmployeeDailyPerErrorRepository extends JpaRepository implements EmployeeDailyPerErrorRepository {

	private static final String FIND_ERROR_CODE;

	private static final String FIND_ERROR_CODE_BY_PERIOD;

	private static final String FIND_BY_PERIOD_ORDER_BY_YMD;

	private static final String REMOVE_DATA;
	
	private static final String REMOVE_DATA_ATTENDANCE_ITEM;

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

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.processingDate = :start ");
		REMOVE_DATA = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtErAttendanceItem a ");
		builderString.append("WHERE a.krcdtErAttendanceItemPK.iD = :iD ");
		REMOVE_DATA_ATTENDANCE_ITEM = builderString.toString();

	}

	@Override
	public void insert(EmployeeDailyPerError employeeDailyPerformanceError) {
//		if (KrcdtSyainDpErList.toEntity(employeeDailyPerformanceError).size() > 1) {
//			this.commandProxy().insert(KrcdtSyainDpErList.toEntity(employeeDailyPerformanceError).get(0));
//		} else {
			this.commandProxy().insert(KrcdtSyainDpErList.toEntity(employeeDailyPerformanceError));
//		}
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
			return result.stream().map(item -> item.toDomain()).collect(Collectors.toList());
		}

		return new ArrayList<>();
	}

	private List<KrcdtSyainDpErList> findEntities(String employeeID, GeneralDate processingDate) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.employeeId = :employeeId ");
		builderString.append("AND a.processingDate = :ymd ");
		List<KrcdtSyainDpErList> result = this.queryProxy().query(builderString.toString(), KrcdtSyainDpErList.class)
				.setParameter("employeeId", employeeID).setParameter("ymd", processingDate).getList();
		return result;
	}

	@Override
	public List<EmployeeDailyPerError> findByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {
		return this.queryProxy().query(FIND_BY_PERIOD_ORDER_BY_YMD, KrcdtSyainDpErList.class)
				.setParameter("employeeId", employeeId).setParameter("start", datePeriod.start())
				.setParameter("end", datePeriod.end()).getList().stream()
				.collect(Collectors.groupingBy(
						c -> c.employeeId + c.processingDate.toString()))
				.entrySet().stream().map(c -> c.getValue().stream().map(item -> item.toDomain())
						.collect(Collectors.toList())).flatMap(List::stream).collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDailyPerError> finds(List<String> employeeID, DatePeriod processingDate) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.employeeId IN :employeeId ");
		builderString.append("AND a.processingDate <= :end ");
		builderString.append("AND a.processingDate >= :start ");
		return this.queryProxy().query(builderString.toString(), KrcdtSyainDpErList.class)
				.setParameter("employeeId", employeeID).setParameter("end", processingDate.end())
				.setParameter("start", processingDate.start()).getList().stream()
				.collect(Collectors.groupingBy(
						c -> c.employeeId + c.processingDate.toString()))
				.entrySet().stream().map(c -> c.getValue().stream().map(item -> item.toDomain())
						.collect(Collectors.toList())).flatMap(List::stream).collect(Collectors.toList());
	}

	@Override
	public void removeParam(String sid, GeneralDate date) {
		List<KrcdtSyainDpErList> result = findEntities(sid, date);
		if(!result.isEmpty()){
			commandProxy().removeAll(result);
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
				.setParameter("employeeId", employeeID).setParameter("companyId", companyID)
				.getList(x -> x.toDomain());
	}
	

}
