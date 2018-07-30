package nts.uk.ctx.at.schedule.ac.common;


import java.util.List;
import java.util.stream.Collectors;

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
	public List<ScTimeImport> calculation(List<ScTimeParam> params) {
		List<ScheduleTimePubImport> impTime = params.stream().map(param -> new ScheduleTimePubImport(param.getEmployeeId(), param.getTargetDate(),
				param.getWorkTypeCode(), param.getWorkTimeCode(), param.getStartClock(), param.getEndClock(),
				param.getBreakStartTime(), param.getBreakEndTime(), param.getChildCareStartTime(),
				param.getChildCareEndTime())).collect(Collectors.toList());
		List<ScheduleTimePubExport> exports = this.scheduleTimePub.calclationScheduleTimeForMultiPeople(impTime);
		
		return exports.stream().map(export -> ScTimeImport.builder()
				.actualWorkTime(export.getActualWorkTime())
				.breakTime(export.getBreakTime())
				.childCareTime(export.getChildCareTime())
				.employeeid(export.getEmployeeid())
				.personalExpenceTime(export.getPersonalExpenceTime())
				.preTime(export.getPreTime())
				.totalWorkTime(export.getTotalWorkTime())
				.weekDayTime(export.getWeekDayTime())
				.ymd(export.getYmd())
				.personalExpenceTime(export.getPersonalExpenceTime())
				.build()).collect(Collectors.toList());
	}
	
	@Override
	public ScTimeImport calculation(ScTimeParam param) {
		ScheduleTimePubImport impTime = new ScheduleTimePubImport(param.getEmployeeId(), param.getTargetDate(),
				param.getWorkTypeCode(), param.getWorkTimeCode(), param.getStartClock(), param.getEndClock(),
				param.getBreakStartTime(), param.getBreakEndTime(), param.getChildCareStartTime(),
				param.getChildCareEndTime());
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
				.personalExpenceTime(export.getPersonalExpenceTime())
				.build();
	}

}
