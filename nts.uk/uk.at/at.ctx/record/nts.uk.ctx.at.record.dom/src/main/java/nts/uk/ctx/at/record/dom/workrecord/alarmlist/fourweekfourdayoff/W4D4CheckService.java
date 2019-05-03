package nts.uk.ctx.at.record.dom.workrecord.alarmlist.fourweekfourdayoff;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
//import javax.inject.Inject;

import lombok.val;
//import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class W4D4CheckService {
	
//	@Inject
//	private WorkInformationRepository workInformationRepository;
//	
//	@Inject
//	private WorkTypeRepository workTypeRepository;
	
	public Optional<AlarmExtractionValue4W4D> checkHoliday(String workplaceID, String employeeID, DatePeriod period,List<String> listHolidayWorkTypeCode,List<InfoCheckNotRegisterDto> listWorkInfoOfDailyPerformance) {
//		String companyID = AppContexts.user().companyId();

		List<String> listActualWorkTypeCode = listWorkInfoOfDailyPerformance.stream().map(c -> c.getWorkTypeCode()).collect(Collectors.toList());
		
//		val listHolidayWorkType = workTypeRepository.findWorkOneDay(companyID, DeprecateClassification.NotDeprecated.value, WorkTypeUnit.OneDay.value, WorkTypeClassification.Holiday.value);
//		List<String> listHolidayWorkTypeCode = listHolidayWorkType.stream().map(c -> c.getWorkTypeCode().v()).collect(Collectors.toList());
		
		int countHoliday = 0;
		for (val workTypeCode: listActualWorkTypeCode) {
			if (listHolidayWorkTypeCode.contains(workTypeCode))
				countHoliday++;
		}
		
		if (countHoliday < 4) {
			String checkedValue = TextResource.localize("KAL010_63",String.valueOf(countHoliday));
//			String alarmDate = period.start().toString() + "~" + period.end().toString();
			String W4D4 = TextResource.localize("KAL010_62");
			String alarmComment = TextResource.localize("KAL010_64");
//			String alarmMessage = TextResource.localize("KAL010_63");
//			alarmMessage = TextResource.localize("KAL010_63",countHoliday+"日","(" +alarmDate +")");
			String alarmMessage = "";
			AlarmExtractionValue4W4D result = new AlarmExtractionValue4W4D(workplaceID, employeeID, period, W4D4, W4D4, alarmMessage, alarmComment,checkedValue);
			return Optional.of(result);
		}
		
		return Optional.empty();
	}
	
}