package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculationMinusExist;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTimeSheet;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.shr.com.time.TimeWithDayAttr;

/** 日別実績の残業時間 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverTimeWorkDailyPerformDto {

	/** 残業枠時間: 残業枠時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "残業枠時間", listMaxLength = 10, indexField = "overtimeFrameNo")
	private List<OverTimeFrameTimeDto> overTimeFrameTime;

	/** 残業枠時間帯: 残業枠時間帯 */
	// @AttendanceItemLayout(layout = "B", isList = true, listMaxLength = ?,
	// setFieldWithIndex = "overtimeFrameNo")
	private List<OverTimeFrameTimeSheetDto> overTimeFrameTimeSheet;

	/** 所定外深夜時間: 法定外残業深夜時間 */
	@AttendanceItemLayout(layout = "C", jpPropertyName = "所定外深夜時間")
	private ExcessOverTimeWorkMidNightTimeDto excessOfStatutoryMidNightTime;

	/** 残業拘束時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "D", jpPropertyName = "残業拘束時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer overTimeSpentAtWork;

	/** 変形法定内残業: 勤怠時間 */
	@AttendanceItemLayout(layout = "E", jpPropertyName = "変形法定内残業")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer irregularWithinPrescribedOverTimeWork;

	/** フレックス時間: フレックス時間 */
	
	@AttendanceItemLayout(layout = "F", jpPropertyName = "フレックス時間")
	private FlexTimeDto flexTime;

	public static OverTimeWorkDailyPerformDto fromOverTimeWorkDailyPerform(OverTimeOfDaily domain) {
		return domain == null ? null
				: new OverTimeWorkDailyPerformDto(
						ConvertHelper.mapTo(domain.getOverTimeWorkFrameTime(),
										c -> new OverTimeFrameTimeDto(CalcAttachTimeDto.toTimeWithCal(c.getTransferTime()),
												CalcAttachTimeDto.toTimeWithCal(c.getOverTimeWork()),
												getAttendanceTime(c.getBeforeApplicationTime()),
												getAttendanceTime(c.getOrderTime()), c.getOverWorkFrameNo().v())),
						ConvertHelper.mapTo(domain.getOverTimeWorkFrameTimeSheet(),
										c -> new OverTimeFrameTimeSheetDto(
												new TimeSpanForCalcDto(getAttendanceTime(c.getTimeSpan().getStart()),
														getAttendanceTime(c.getTimeSpan().getEnd())),
												c.getFrameNo().v())),
						ExcessOverTimeWorkMidNightTimeDto
								.fromOverTimeWorkDailyPerform(domain.getExcessOverTimeWorkMidNightTime().isPresent()
																? domain.getExcessOverTimeWorkMidNightTime().get() : null),
						getAttendanceTime(domain.getOverTimeWorkSpentAtWork()),
						getAttendanceTime(domain.getIrregularWithinPrescribedOverTimeWork()),
						domain.getFlexTime() == null ? null
								: new FlexTimeDto(CalcAttachTimeDto.toTimeWithCal(domain.getFlexTime().getFlexTime()),
										getAttendanceTime(domain.getFlexTime().getBeforeApplicationTime())));
	}

	private static Integer getAttendanceTime(AttendanceTime time) {
		return time == null ? null : time.valueAsMinutes();
	}

	private static Integer getAttendanceTime(TimeWithDayAttr time) {
		return time == null ? null : time.valueAsMinutes();
	}

	public OverTimeOfDaily toDomain() {
		return new OverTimeOfDaily(
				ConvertHelper.mapTo(overTimeFrameTimeSheet,
						(c) -> new OverTimeFrameTimeSheet(createTimeSheet(c.getTimeSheet()),
								new OverTimeFrameNo(c.getOvertimeFrameNo()))),
				ConvertHelper.mapTo(overTimeFrameTime,
						(c) -> new OverTimeFrameTime(new OverTimeFrameNo(c.getOvertimeFrameNo()),
								createTimeWithCalc(c.getOvertime()), createTimeWithCalc(c.getTransferTime()),
								toAttendanceTime(c.getBeforeApplicationTime()), toAttendanceTime(c.getOrderTime()))),
				excessOfStatutoryMidNightTime == null ? Finally.empty() : Finally.of(excessOfStatutoryMidNightTime.toDomain()),
				toAttendanceTime(irregularWithinPrescribedOverTimeWork),
				new FlexTime(createTimeWithCalcMinus(), flexTime == null ? null : toAttendanceTime(flexTime.getBeforeApplicationTime())),
				toAttendanceTime(overTimeSpentAtWork));
	}

	private TimeWithCalculationMinusExist createTimeWithCalcMinus() {
		return flexTime == null || flexTime.getFlexTime() == null ? null
				: TimeWithCalculationMinusExist.sameTime(toAttendanceTimeOfExistMinus(flexTime.getFlexTime().getTime()));
	}

	private TimeSpanForCalc createTimeSheet(TimeSpanForCalcDto c) {
		return c == null ? null : new TimeSpanForCalc(toTimeWithDayAttr(c.getStart()), toTimeWithDayAttr(c.getEnd()));
	}

	private TimeWithCalculation createTimeWithCalc(CalcAttachTimeDto c) {
		return c == null ? null : c.createTimeWithCalc();
	}

	private AttendanceTime toAttendanceTime(Integer time) {
		return time == null ? null : new AttendanceTime(time);
	}

	private TimeWithDayAttr toTimeWithDayAttr(Integer time) {
		return time == null ? null : new TimeWithDayAttr(time);
	}

	private AttendanceTimeOfExistMinus toAttendanceTimeOfExistMinus(Integer time) {
		return time == null ? null : new AttendanceTimeOfExistMinus(time);
	}
}
