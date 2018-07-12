package nts.uk.ctx.at.record.pubimp.dailyprocess.scheduletime;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.pub.dailyprocess.scheduletime.ScheduleTimePub;
import nts.uk.ctx.at.record.pub.dailyprocess.scheduletime.ScheduleTimePubExport;
import nts.uk.ctx.at.record.pub.dailyprocess.scheduletime.ScheduleTimePubImport;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

@Stateless
public class ScheduleTimePubImpl implements ScheduleTimePub{

	@Override
	public ScheduleTimePubExport calculationScheduleTime(ScheduleTimePubImport impTime) {
		
		return new ScheduleTimePubExport(impTime.getEmployeeId(),
										 GeneralDate.today(),
										 new AttendanceTime(0),
										 new AttendanceTime(0),
										 new AttendanceTime(0),
										 new AttendanceTime(0),
										 new AttendanceTime(0),
										 new AttendanceTime(0),
										 Collections.emptyList()
										 );
	}

}