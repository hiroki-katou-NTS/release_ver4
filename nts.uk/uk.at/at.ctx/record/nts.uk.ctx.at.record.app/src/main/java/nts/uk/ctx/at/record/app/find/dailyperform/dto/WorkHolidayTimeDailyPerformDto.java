package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.shr.com.time.TimeWithDayAttr;

/** 日別実績の休出時間 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkHolidayTimeDailyPerformDto {

	/** 休出枠時間帯: 休出枠時間帯 */
//	@AttendanceItemLayout(layout = "A", isList = true, listMaxLength = ?, setFieldWithIndex = "holidayWorkFrameNo")
	private List<HolidayWorkFrameTimeSheetDto> holidyWorkFrameTimeSheet;

	/** 休出深夜: 休出深夜 */
	@AttendanceItemLayout(layout = "B", jpPropertyName="休出深夜")
	private HolidayMidnightWorkDto holidayMidnightWork;

	/** 休出拘束時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "C", jpPropertyName="休出拘束時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer holidayTimeSpentAtWork;

	/** 休出枠時間: 休出枠時間 */
	@AttendanceItemLayout(layout = "D", jpPropertyName="休出枠時間", listMaxLength = 10, indexField = "holidayFrameNo", needCheckIDWithIndex = true)
	private List<HolidayWorkFrameTimeDto> holidayWorkFrameTime;
	
	public static WorkHolidayTimeDailyPerformDto fromOverTimeWorkDailyPerform(HolidayWorkTimeOfDaily domain){
		return domain == null ? null : new WorkHolidayTimeDailyPerformDto(
				ConvertHelper.mapTo(domain.getHolidayWorkFrameTimeSheet(), c -> new HolidayWorkFrameTimeSheetDto(
						getTimeSpan(c.getTimeSheet()), 
						c.getHolidayWorkTimeSheetNo().v())), 
				HolidayMidnightWorkDto.fromHolidayMidnightWork(domain.getHolidayMidNightWork().get()), 
				domain.getHolidayTimeSpentAtWork().valueAsMinutes(), 
				ConvertHelper.mapTo(domain.getHolidayWorkFrameTime(), (c) -> new HolidayWorkFrameTimeDto(
						CalcAttachTimeDto.toTimeWithCal(c.getHolidayWorkTime().get()), 
						CalcAttachTimeDto.toTimeWithCal(c.getTransferTime().get()), 
						getAttendanceTime(c.getBeforeApplicationTime().get()),
						c.getHolidayFrameNo().v())));
	}

	private static TimeSpanForCalcDto getTimeSpan(TimeSpanForCalc c) {
		return c == null ? null : new TimeSpanForCalcDto(c.getStart().valueAsMinutes(), c.getEnd().valueAsMinutes());
	}
	
	private static Integer getAttendanceTime(AttendanceTime time) {
		return time == null ? null : time.valueAsMinutes();
	}
	
	public HolidayWorkTimeOfDaily toDomain() {
		return new HolidayWorkTimeOfDaily(
				ConvertHelper.mapTo(holidyWorkFrameTimeSheet,
						(c) -> new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(c.getHolidayWorkFrameNo()),
								c.getTimeSheet() == null ? null : new TimeSpanForCalc(toTimeWithDayAttr(c.getTimeSheet().getStart()),
										toTimeWithDayAttr(c.getTimeSheet().getEnd())))),
				ConvertHelper.mapTo(holidayWorkFrameTime,
						(c) -> new HolidayWorkFrameTime(new HolidayWorkFrameNo(c.getHolidayFrameNo()),
								createTimeWithCalc(c.getHolidayWorkTime()),
								createTimeWithCalc(c.getTransferTime()),
								c.getBeforeApplicationTime() == null ? Finally.empty() 
										: Finally.of(toAttendanceTime(c.getBeforeApplicationTime())))),
				holidayMidnightWork == null ? Finally.empty() : Finally.of(holidayMidnightWork.toDomain()),
				toAttendanceTime(holidayTimeSpentAtWork));
	}

	private Finally<TimeWithCalculation> createTimeWithCalc(CalcAttachTimeDto c) {
		return c == null ? Finally.empty() : Finally.of(c.createTimeWithCalc());
	}
	
	private TimeWithDayAttr toTimeWithDayAttr(Integer time) {
		return time == null ? null : new TimeWithDayAttr(time);
	}
	
	private AttendanceTime toAttendanceTime(Integer time) {
		return time == null ? null : new AttendanceTime(time);
	}
}
