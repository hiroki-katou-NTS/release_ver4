package nts.uk.ctx.at.function.dom.alarm.w4d4alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceImport;
import nts.uk.ctx.at.function.dom.adapter.workrecord.alarmlist.fourweekfourdayoff.W4D4CheckAdapter;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.fourweekfourdayoff.AlarmCheckCondition4W4D;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class W4D4AlarmService {
	
	@Inject
	private AlarmCheckConditionByCategoryRepository alarmCheckConditionByCategoryRepository;
	
	@Inject
	private WorkplaceAdapter workplaceAdapter;
	
	@Inject
	private W4D4CheckAdapter w4D4CheckAdapter;
		
	public List<ValueExtractAlarm> calculateTotal4W4D(EmployeeSearchDto employee, DatePeriod period, String checkConditionCode) {
		String companyID = AppContexts.user().companyId();
		List<ValueExtractAlarm> result = new ArrayList<ValueExtractAlarm>();
		
		Optional<AlarmCheckConditionByCategory> optAlarmCheckConditionByCategory = alarmCheckConditionByCategoryRepository.find(companyID, AlarmCategory.SCHEDULE_4WEEK.value, checkConditionCode);
		if (!optAlarmCheckConditionByCategory.isPresent())
			throw new RuntimeException("Can't find AlarmCheckConditionByCategory with category: 4W4D and code: " + checkConditionCode);
		
		// TODO: Narrow down the target audience
		
		// Acquire company employee's work place history
		WorkplaceImport workplaceImport = workplaceAdapter.getWorlkplaceHistory(employee.getId(), period.end());
		
		AlarmCheckConditionByCategory alarmCheckConditionByCategory = optAlarmCheckConditionByCategory.get();
		AlarmCheckCondition4W4D fourW4DCheckCond = (AlarmCheckCondition4W4D) alarmCheckConditionByCategory.getExtractionCondition();
		
		if (fourW4DCheckCond.isForActualResultsOnly()) {
			Optional<ValueExtractAlarm> optAlarm = this.checkWithActualResults(employee, period);
			if (optAlarm.isPresent())
				result.add(optAlarm.get());
		}
		
		return result;
	}
	
	public Optional<ValueExtractAlarm> checkWithActualResults(EmployeeSearchDto employee, DatePeriod period) {
		return w4D4CheckAdapter.checkHoliday(employee.getWorkplaceId(), employee.getId(), period);
	}
}