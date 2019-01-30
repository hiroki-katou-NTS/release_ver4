package nts.uk.ctx.at.function.ac.fixedcheckitem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.fixedcheckitem.FixedCheckItemAdapter;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.record.pub.fixedcheckitem.FixedCheckItemPub;
import nts.uk.ctx.at.record.pub.fixedcheckitem.ValueExtractAlarmWRPubExport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
public class FixedCheckItemAcFinder implements FixedCheckItemAdapter {
	
	@Inject
	private FixedCheckItemPub fixedCheckItemPub;

	@Override
	public Optional<ValueExtractAlarm>  checkWorkTypeNotRegister(String workplaceID,String employeeID, GeneralDate date, String workTypeCD) {
		Optional<ValueExtractAlarmWRPubExport> data =fixedCheckItemPub.checkWorkTypeNotRegister(workplaceID,employeeID, date, workTypeCD);
		
		if(data.isPresent())
			return Optional.of(convertToExport(data.get()));
		return Optional.empty();
		
	}

	@Override
	public Optional<ValueExtractAlarm> checkWorkTimeNotRegister(String workplaceID,String employeeID, GeneralDate date, String workTimeCD) {
		Optional<ValueExtractAlarmWRPubExport> data =fixedCheckItemPub.checkWorkTimeNotRegister(workplaceID,employeeID, date, workTimeCD);
		if(data.isPresent())
			return Optional.of(convertToExport(data.get()));
		return Optional.empty();
	}

	@Override
	public List<ValueExtractAlarm> checkPrincipalUnconfirm(String workplaceID, String employeeID,
			GeneralDate startDate, GeneralDate endDate) {
		return fixedCheckItemPub.checkPrincipalUnconfirm(workplaceID, employeeID, startDate, endDate)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
	}

	@Override
	public List<ValueExtractAlarm> checkAdminUnverified(String workplaceID, String employeeID,
			GeneralDate startDate, GeneralDate endDate) {
		return fixedCheckItemPub.checkAdminUnverified(workplaceID, employeeID, startDate, endDate)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
	}

	@Override
	public List<ValueExtractAlarm> checkingData(String workplaceID,String employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		return fixedCheckItemPub.checkingData(workplaceID,employeeID, startDate, endDate)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
	}
	
	private ValueExtractAlarm convertToExport(ValueExtractAlarmWRPubExport export) {
		return new ValueExtractAlarm(
				export.getWorkplaceID().orElse(null),
				export.getEmployeeID(),
				export.getAlarmValueDate().toString(),
				export.getClassification(),
				export.getAlarmItem(),
				export.getAlarmValueMessage(),
				export.getComment().orElse(null)
				);
	}

	@Override
	public List<ValueExtractAlarm> checkAdminUnverified(String workplaceID, String employeeID, DatePeriod datePeriod) {
		return fixedCheckItemPub.checkAdminUnverified(workplaceID, employeeID, datePeriod )
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
	}

	@Override
	public List<ValueExtractAlarm> checkContinuousVacation(String employeeID, DatePeriod datePeriod) {
		return fixedCheckItemPub.checkContinuousVacation(employeeID, datePeriod)
				.stream().map(c->convertToExport(c)).collect(Collectors.toList());
	}
	

}
