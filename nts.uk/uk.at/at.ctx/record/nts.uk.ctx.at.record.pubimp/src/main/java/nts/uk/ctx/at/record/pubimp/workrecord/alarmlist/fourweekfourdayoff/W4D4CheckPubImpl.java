package nts.uk.ctx.at.record.pubimp.workrecord.alarmlist.fourweekfourdayoff;

import java.util.Optional;
import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.uk.ctx.at.record.dom.workrecord.alarmlist.fourweekfourdayoff.AlarmExtractionValue4W4D;
import nts.uk.ctx.at.record.dom.workrecord.alarmlist.fourweekfourdayoff.W4D4CheckService;
import nts.uk.ctx.at.record.pub.workrecord.alarmlist.fourweekfourdayoff.AlarmExtractionValue4W4DExport;
import nts.uk.ctx.at.record.pub.workrecord.alarmlist.fourweekfourdayoff.W4D4CheckPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class W4D4CheckPubImpl implements W4D4CheckPub {
	@Inject
	private W4D4CheckService checkService;

	@Override
	public Optional<AlarmExtractionValue4W4DExport> checkHoliday(String workplaceID, String employeeID,
			DatePeriod period) {
		Optional<AlarmExtractionValue4W4D> optAlarmExtract = checkService.checkHoliday(workplaceID, employeeID, period);
		
		if (optAlarmExtract.isPresent()) {
			AlarmExtractionValue4W4D alarmExtract = optAlarmExtract.get();

			return Optional.of(new AlarmExtractionValue4W4DExport(workplaceID, employeeID, alarmExtract.getDatePeriod(),
					alarmExtract.getClassification(), alarmExtract.getAlarmItem(), alarmExtract.getAlarmValueMessage(),
					alarmExtract.getComment()));
		} else {

			return Optional.empty();
		}

	}

}
