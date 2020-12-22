package nts.uk.ctx.at.function.ac.temporaryabsence.takeleaveemployeeandperiod;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.temporaryabsence.takeleaveemployeeandperiod.TakeLeaveEmpAndPeriodAdapter;
import nts.uk.ctx.at.shared.dom.dailyresult.findperiodchangeleavehis.TakeLeaveEmpAndPeriodShared;
import nts.uk.ctx.at.shared.dom.dailyresult.findperiodchangeleavehis.TempAbsenceHisItemShared;
import nts.uk.ctx.bs.employee.pub.temporaryabsence.takeleaveemployeeandperiod.TakeLeaveEmpAndPeriodExport;
import nts.uk.ctx.bs.employee.pub.temporaryabsence.takeleaveemployeeandperiod.TakeLeaveEmpAndPeriodPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class TakeLeaveEmpAndPeriodAc implements TakeLeaveEmpAndPeriodAdapter {
	
	@Inject
	private TakeLeaveEmpAndPeriodPub takeLeaveEmpAndPeriodPub;
	
	@Override
	public List<TakeLeaveEmpAndPeriodShared> takeLeaveEmpAndPeriodPub(List<String> listEmp, DatePeriod period) {
		List<TakeLeaveEmpAndPeriodExport> listTakeLeaveEmpAndPeriodExport = takeLeaveEmpAndPeriodPub.takeLeaveEmpAndPeriodPub(listEmp, period);
		return listTakeLeaveEmpAndPeriodExport.stream().map(c->convertToTakeLeaveEmpAndPeriodExport(c)).collect(Collectors.toList());
	}
	
	private TakeLeaveEmpAndPeriodShared convertToTakeLeaveEmpAndPeriodExport(TakeLeaveEmpAndPeriodExport export) {
		return new TakeLeaveEmpAndPeriodShared(
				export.getHisId(),
				export.getPeriod(), 
				new TempAbsenceHisItemShared(
						export.getTempAbsenceHisItemExport().getTempAbsenceFrNo(), 
						export.getTempAbsenceHisItemExport().getHistoryId(),
						export.getTempAbsenceHisItemExport().getEmployeeId(), 
						export.getTempAbsenceHisItemExport().getRemarks(), 
						export.getTempAbsenceHisItemExport().getSoInsPayCategory(), 
						export.getTempAbsenceHisItemExport().getFamilyMemberId()));
	}

}
