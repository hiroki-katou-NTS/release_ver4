package nts.uk.ctx.at.record.pubimp.workrecord.erroralarm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.EmployeeDailyPerErrorPub;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.EmployeeDailyPerErrorPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.EmployeeErrorPubExport;
//import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class EmployeeDailyPerErrorPubImpl implements EmployeeDailyPerErrorPub {
	@Inject
	private EmployeeDailyPerErrorRepository repo;

	@Override
	public List<EmployeeDailyPerErrorPubExport> getByErrorCode(String employeeId, DatePeriod datePeriod,
			List<String> errorCodes) {
//		String companyID = AppContexts.user().companyId();
		List<EmployeeDailyPerError> employeeDailyList = repo.findByPeriodOrderByYmd(employeeId, datePeriod);
		
		employeeDailyList = employeeDailyList.stream().filter(
				e -> errorCodes.contains(e.getErrorAlarmWorkRecordCode().v()))
				.collect(Collectors.toList());
		
		return employeeDailyList.stream().map(e ->new EmployeeDailyPerErrorPubExport(e.getCompanyID(), e.getEmployeeID(), e.getDate(),
				e.getErrorAlarmWorkRecordCode().v(), e.getAttendanceItemList(), e.getErrorCancelAble())).collect(Collectors.toList());
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<EmployeeErrorPubExport> checkEmployeeErrorOnProcessingDate(String employeeId, DatePeriod datePeriod) {
		
		List<EmployeeErrorPubExport> employeeErrorPubExportList = new ArrayList<>();
		
		List<GeneralDate> daysBetween = this.getDaysBetween(datePeriod.start(), datePeriod.end());
		
		for (GeneralDate processingDate : daysBetween){
			boolean hasError = this.repo.checkEmployeeHasErrorOnProcessingDate(employeeId, processingDate);
			EmployeeErrorPubExport export = new EmployeeErrorPubExport(processingDate, hasError);
			employeeErrorPubExportList.add(export);
		}
		
		return employeeErrorPubExportList;
	}

	private List<GeneralDate> getDaysBetween(GeneralDate startDate, GeneralDate endDate) {
		List<GeneralDate> daysBetween = new ArrayList<>();

		while (startDate.beforeOrEquals(endDate)) {
			daysBetween.add(startDate);
			GeneralDate temp = startDate.addDays(1);
			startDate = temp;
		}

		return daysBetween;
	}
	
	@Override
	public List<EmployeeDailyPerErrorPubExport> getByErrorCode(List<String> employeeId, DatePeriod datePeriod,
			List<String> errorCodes) {
		List<EmployeeDailyPerError> employeeDailyList = repo.finds(employeeId, datePeriod);
		
		employeeDailyList = employeeDailyList.stream().filter(e -> errorCodes.contains(e.getErrorAlarmWorkRecordCode().v()))
				.collect(Collectors.toList());
		
		return employeeDailyList.stream().map(e -> new EmployeeDailyPerErrorPubExport(e.getCompanyID(), e.getEmployeeID(), e.getDate(),
				e.getErrorAlarmWorkRecordCode().v(), e.getAttendanceItemList(), e.getErrorCancelAble())).collect(Collectors.toList());
	}

}
