package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculationMinusExist;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTimeSheet;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemValue;
import nts.uk.ctx.at.shared.app.util.attendanceitem.type.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.shr.com.time.TimeWithDayAttr;

/** 日別実績の残業時間 */
@Data
@AllArgsConstructor
public class OverTimeWorkDailyPerformDto {

	/** 残業枠時間: 残業枠時間 */
	@AttendanceItemLayout(layout = "A", isList = true, jpPropertyName = "残業枠時間")
	private List<OverTimeFrameTimeDto> overTimeFrameTime;

	/** 残業枠時間帯: 残業枠時間帯 */
	// @AttendanceItemLayout(layout = "B", isList = true)
	private List<OverTimeFrameTimeSheetDto> overTimeFrameTimeSheet;

	/** 所定外深夜時間: 法定外残業深夜時間 */
	@AttendanceItemLayout(layout = "C", jpPropertyName = "所定外深夜時間")
	private ExcessOverTimeWorkMidNightTimeDto excessOfStatutoryMidNightTime;

	/** 残業拘束時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "D", jpPropertyName = "残業拘束時間")
	@AttendanceItemValue(itemId = 745, type = ValueType.INTEGER)
	private Integer overTimeSpentAtWork;

	/** 変形法定内残業: 勤怠時間 */
	@AttendanceItemLayout(layout = "E", jpPropertyName = "変形法定内残業")
	@AttendanceItemValue(itemId = 551, type = ValueType.INTEGER)
	private Integer irregularWithinPrescribedOverTimeWork;

	/** フレックス時間: フレックス時間 */
	@AttendanceItemLayout(layout = "F", jpPropertyName = "フレックス時間")
	private FlexTimeDto flexTime;

	public static OverTimeWorkDailyPerformDto fromOverTimeWorkDailyPerform(OverTimeOfDaily domain) {
		return domain == null ? null
				: new OverTimeWorkDailyPerformDto(
						domain.getOverTimeWorkFrameTime().stream()
								.map(c -> new OverTimeFrameTimeDto(
										new CalcAttachTimeDto(c.getTransferTime().getCalcTime().valueAsMinutes(),
												c.getTransferTime().getTime().valueAsMinutes()),
										new CalcAttachTimeDto(c.getOverTimeWork().getCalcTime().valueAsMinutes(),
												c.getOverTimeWork().getTime().valueAsMinutes()),
										c.getBeforeApplicationTime().valueAsMinutes(),
										c.getOrderTime().valueAsMinutes(), c.getOverWorkFrameNo().v()))
								.collect(
										Collectors.toList()),
						domain.getOverTimeWorkFrameTimeSheet().stream()
								.map(c -> new OverTimeFrameTimeSheetDto(
										new TimeSpanForCalcDto(c.getTimeSpan().getStart().valueAsMinutes(),
												c.getTimeSpan().getEnd().valueAsMinutes()),
										c.getFrameNo().v()))
								.collect(Collectors.toList()),
						ExcessOverTimeWorkMidNightTimeDto
								.fromOverTimeWorkDailyPerform(domain.getExcessOverTimeWorkMidNightTime().get()),
						domain.getOverTimeWorkSpentAtWork().valueAsMinutes(),
						domain.getIrregularWithinPrescribedOverTimeWork().valueAsMinutes(),
						new FlexTimeDto(
								new CalcAttachTimeDto(domain.getFlexTime().getFlexTime().getCalcTime().valueAsMinutes(),
										domain.getFlexTime().getFlexTime().getTime().valueAsMinutes()),
								domain.getFlexTime().getBeforeApplicationTime().valueAsMinutes()));
	}

	public OverTimeOfDaily toDomain() {
		return new OverTimeOfDaily(
				ConvertHelper.mapTo(overTimeFrameTimeSheet,
						(c) -> new OverTimeFrameTimeSheet(
								new TimeSpanForCalc(new TimeWithDayAttr(c.getTimeSheet().getStart()),
										new TimeWithDayAttr(c.getTimeSheet().getEnd())),
								new OverTimeFrameNo(c.getOvertimeFrameNo()))),
				ConvertHelper.mapTo(overTimeFrameTime, (c) -> new OverTimeFrameTime(
						new OverTimeFrameNo(c.getOvertimeFrameNo()),
						TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(c.getOvertime().getTime()),
								new AttendanceTime(c.getOvertime().getCalcTime())),
						TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(c.getTransferTime().getTime()),
								new AttendanceTime(c.getTransferTime().getCalcTime())),
						new AttendanceTime(c.getBeforeApplicationTime()), new AttendanceTime(c.getOrderTime()))),
				Finally.of(excessOfStatutoryMidNightTime.toDomain()),
				new AttendanceTime(irregularWithinPrescribedOverTimeWork), new FlexTime(
						TimeWithCalculationMinusExist.createTimeWithCalculation(
								new AttendanceTimeOfExistMinus(flexTime.getFlexTime().getTime()),
								new AttendanceTimeOfExistMinus(flexTime.getFlexTime().getCalcTime())),
						new AttendanceTime(flexTime.getBeforeApplicationTime())),
				new AttendanceTime(overTimeSpentAtWork));
	}
}
