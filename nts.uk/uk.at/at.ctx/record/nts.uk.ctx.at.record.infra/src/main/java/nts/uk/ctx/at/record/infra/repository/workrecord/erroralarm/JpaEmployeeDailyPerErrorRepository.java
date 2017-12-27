package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KrcdtSyainDpErList;

@Stateless
public class JpaEmployeeDailyPerErrorRepository extends JpaRepository implements EmployeeDailyPerErrorRepository {

	private static final String FIND_ERROR_CODE;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.krcdtSyainDpErListPK.processingDate = :processingDate ");
		builderString.append("AND a.krcdtSyainDpErListPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtSyainDpErListPK.errorCode = :errorCode ");
		FIND_ERROR_CODE = builderString.toString();
	}

	@Override
	public void insert(EmployeeDailyPerError employeeDailyPerformanceError) {
		KrcdtSyainDpErList.toEntity(employeeDailyPerformanceError).forEach(f -> this.commandProxy().insert(f));
	}

	@Override
	public boolean checkExistErrorCode(String employeeID, GeneralDate processingDate, String errorCode) {
		return this.queryProxy().query(FIND_ERROR_CODE, long.class).setParameter("processingDate", processingDate)
				.setParameter("employeeId", employeeID).setParameter("errorCode", errorCode).getSingle().get() > 0;
	}

	@Override
	public EmployeeDailyPerError find(String employeeID, GeneralDate processingDate) {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtSyainDpErList a ");
		builderString.append("WHERE a.krcdtSyainDpErListPK.employeeId = :employeeId ");
		builderString.append("AND a.krcdtSyainDpErListPK.processingDate = :ymd ");
		List<KrcdtSyainDpErList> result = this.queryProxy()
				.query(builderString.toString(), KrcdtSyainDpErList.class).setParameter("employeeId", employeeID)
				.setParameter("ymd", processingDate).getList();
		if (!result.isEmpty()) {
			return new EmployeeDailyPerError(result.get(0).krcdtSyainDpErListPK.companyID,
					result.get(0).krcdtSyainDpErListPK.employeeId, result.get(0).krcdtSyainDpErListPK.processingDate,
					new ErrorAlarmWorkRecordCode(result.get(0).krcdtSyainDpErListPK.errorCode),
					result.stream().map(c -> c.attendanceItemId).collect(Collectors.toList()),
					result.get(0).errorCancelable);
		}

		return null;
	}

}
