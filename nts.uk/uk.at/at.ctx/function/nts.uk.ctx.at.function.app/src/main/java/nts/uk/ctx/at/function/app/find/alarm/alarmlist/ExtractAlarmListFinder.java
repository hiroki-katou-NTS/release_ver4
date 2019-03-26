package nts.uk.ctx.at.function.app.find.alarm.alarmlist;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.alarm.alarmlist.ExtractAlarmListService;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.ExtractedAlarmDto;

@Stateless
public class ExtractAlarmListFinder {

	@Inject
	private ExtractAlarmListService extractAlarmListService;

	public ExtractedAlarmDto extractAlarm(ExtractAlarmQuery query) {
		return this.extractAlarmListService.extractAlarm(query.getListEmployee(), query.getAlarmCode(), query.getListPeriodByCategory());
	}
}
