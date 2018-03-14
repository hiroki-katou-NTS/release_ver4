package nts.uk.ctx.at.record.dom.fourweekfourdayoff;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class W4D4CheckService {
	
	@Inject
	private WorkInformationRepository workInformationRepository;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	public Optional<AlarmExtractionValue4W4D> checkHoliday(String workplaceID, String employeeID, DatePeriod period) {
		String companyID = AppContexts.user().companyId();

		List<WorkInfoOfDailyPerformance> listWorkInfoOfDailyPerformance = workInformationRepository.findByPeriodOrderByYmd(employeeID, period);
		List<String> listActualWorkTypeCode = listWorkInfoOfDailyPerformance.stream().map(c -> c.getRecordWorkInformation().getWorkTypeCode().v()).collect(Collectors.toList());
		
		val listHolidayWorkType = workTypeRepository.findWorkOneDay(companyID, DeprecateClassification.NotDeprecated.value, WorkTypeUnit.OneDay.value, WorkTypeClassification.Holiday.value);
		List<String> listHolidayWorkTypeCode = listHolidayWorkType.stream().map(c -> c.getWorkTypeCode().v()).collect(Collectors.toList());
		
		int countHoliday = 0;
		for (val workTypeCode: listHolidayWorkTypeCode) {
			if (listActualWorkTypeCode.contains(workTypeCode))
				countHoliday++;
		}
		
		if (countHoliday < 4) {
			String alarmDate = period.start().toString() + "~" + period.end().toString();
			String W4D4 = TextResource.localize("#KAL012_62");
			String alarmComment = TextResource.localize("#KAL012_64");
			String alarmMessage = TextResource.localize("#KAL012_64");
			alarmMessage = String.format(alarmMessage, countHoliday, alarmDate);
			AlarmExtractionValue4W4D result = new AlarmExtractionValue4W4D(workplaceID, employeeID, alarmDate, W4D4, W4D4, alarmMessage, alarmComment);
			return Optional.of(result);
		}
		
		return Optional.empty();
	}
	
}