package nts.uk.ctx.at.schedule.ac.common;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.pub.dailyprocess.scheduletime.ScheduleTimePub;
import nts.uk.ctx.at.record.pub.dailyprocess.scheduletime.ScheduleTimePubExport;
import nts.uk.ctx.at.record.pub.dailyprocess.scheduletime.ScheduleTimePubImport;
import nts.uk.ctx.at.schedule.dom.adapter.ScTimeAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.ScTimeImport;
import nts.uk.ctx.at.schedule.dom.adapter.ScTimeParam;

@Stateless
public class ScTimeAdapterImpl implements ScTimeAdapter {
	@Inject
	private ScheduleTimePub scheduleTimePub;
	
	@Override
	public ScTimeImport calculation(ScTimeParam param) {
		ScheduleTimePubImport impTime = new ScheduleTimePubImport();
		ScheduleTimePubExport export = this.scheduleTimePub.calculationScheduleTime(impTime);
		
		return ScTimeImport.builder()
				.actualWorkTime(export.getActualWorkTime())
				.breakTime(export.getBreakTime())
				.childCareTime(export.getChildCareTime())
				.employeeid(export.getEmployeeid())
				.personalExpenceTime(export.getPersonalExpenceTime())
				.preTime(export.getPreTime())
				.totalWorkTime(export.getTotalWorkTime())
				.weekDayTime(export.getWeekDayTime())
				.ymd(export.getYmd())
				.build();
	}

}
