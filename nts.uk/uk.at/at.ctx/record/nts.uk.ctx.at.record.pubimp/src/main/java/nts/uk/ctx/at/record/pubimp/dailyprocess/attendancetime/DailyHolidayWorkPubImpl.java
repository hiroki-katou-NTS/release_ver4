package nts.uk.ctx.at.record.pubimp.dailyprocess.attendancetime;

import java.util.List;

import javax.ejb.Stateless;

import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.HolidayWorkTimeSheet;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.DailyHolidayWorkPub;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.DailyHolidayWorkPubExport;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.DailyHolidayWorkPubImport;

@Stateless
public class DailyHolidayWorkPubImpl implements DailyHolidayWorkPub{
	
	@Override
	public DailyHolidayWorkPubExport calcHolidayWorkTransTime(DailyHolidayWorkPubImport imp) {
		List<HolidayWorkFrameTime> result = HolidayWorkTimeSheet.transProcess(imp.getWorkType(), imp.getAfterCalcUpperTimeList(), imp.getEachWorkTimeSet(), imp.getEachCompanyTimeSet());
		return DailyHolidayWorkPubExport.create(result);
	}
}
