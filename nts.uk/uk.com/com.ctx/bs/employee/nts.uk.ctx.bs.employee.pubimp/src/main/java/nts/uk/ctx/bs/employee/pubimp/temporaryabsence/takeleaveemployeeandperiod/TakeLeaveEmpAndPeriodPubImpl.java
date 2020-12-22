package nts.uk.ctx.bs.employee.pubimp.temporaryabsence.takeleaveemployeeandperiod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.temporaryabsence.TempAbsenceHisItem;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.takeleaveemployeeandperiod.TakeLeaveEmployeeAndPeriod;
import nts.uk.ctx.bs.employee.pub.temporaryabsence.takeleaveemployeeandperiod.TakeLeaveEmpAndPeriodExport;
import nts.uk.ctx.bs.employee.pub.temporaryabsence.takeleaveemployeeandperiod.TakeLeaveEmpAndPeriodPub;
import nts.uk.ctx.bs.employee.pub.temporaryabsence.takeleaveemployeeandperiod.TempAbsenceHisItemExport;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class TakeLeaveEmpAndPeriodPubImpl implements TakeLeaveEmpAndPeriodPub {
	@Inject
	private TakeLeaveEmployeeAndPeriod takeLeaveEmployeeAndPeriod;

	@Override
	public List<TakeLeaveEmpAndPeriodExport> takeLeaveEmpAndPeriodPub(List<String> listEmp, DatePeriod period) {
		Map<DateHistoryItem, TempAbsenceHisItem> data = takeLeaveEmployeeAndPeriod.takeLeaveEmployeeAndPeriod(listEmp, period);
		return convertToDomain(data);
	}
	
	private List<TakeLeaveEmpAndPeriodExport> convertToDomain(Map<DateHistoryItem, TempAbsenceHisItem> mapData) {
		List<TakeLeaveEmpAndPeriodExport> listTakeLeaveEmpAndPeriodExport = new ArrayList<>();
		for (Map.Entry<DateHistoryItem, TempAbsenceHisItem> entry : mapData.entrySet()) {
			TakeLeaveEmpAndPeriodExport takeLeaveEmpAndPeriodExport = new TakeLeaveEmpAndPeriodExport(
					entry.getKey().identifier(),
					new DatePeriod(entry.getKey().start(), entry.getKey().end()),
					convertToTempAbsenceHisItem(entry.getValue()));
			listTakeLeaveEmpAndPeriodExport.add(takeLeaveEmpAndPeriodExport);
	    }
		return listTakeLeaveEmpAndPeriodExport;
	}

	private TempAbsenceHisItemExport convertToTempAbsenceHisItem(TempAbsenceHisItem domain) {
		return new TempAbsenceHisItemExport(domain.getTempAbsenceFrNo().v().intValue(), domain.getHistoryId(),
				domain.getEmployeeId(), domain.getRemarks().v(), domain.getSoInsPayCategory(),
				domain.getFamilyMemberId());
	}

}
