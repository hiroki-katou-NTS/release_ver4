package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayMidnightWork;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkMidNightTime;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;

/** 休出深夜 */
@Data
@AllArgsConstructor
public class HolidayMidnightWorkDto {

	/** 休出深夜時間: 休出深夜時間 */
	// @AttendanceItemLayout(layout="A", jpPropertyName="休出深夜時間")
	// private HolidayWorkMidNightTimeDto holidayWorkMidNightTime;

	/** 法定内: 計算付き時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "法定内休出")
	private CalcAttachTimeDto withinPrescribedHolidayWork;

	/** 法定外: 計算付き時間 */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "法定外休出")
	private CalcAttachTimeDto excessOfStatutoryHolidayWork;

	/** 祝日: 計算付き時間 */
	@AttendanceItemLayout(layout = "C", jpPropertyName = "祝日休出")
	private CalcAttachTimeDto publicHolidayWork;

	public static HolidayMidnightWorkDto fromHolidayMidnightWork(HolidayMidnightWork domain) {
		return domain == null ? null
				: new HolidayMidnightWorkDto(
						getWorkTime(domain.getHolidayWorkMidNightTime(),
								StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork),
						getWorkTime(domain.getHolidayWorkMidNightTime(),
								StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork),
						getWorkTime(domain.getHolidayWorkMidNightTime(), StaturoryAtrOfHolidayWork.PublicHolidayWork));
	}

	private static CalcAttachTimeDto getWorkTime(List<HolidayWorkMidNightTime> source, StaturoryAtrOfHolidayWork type) {
		return source.stream().filter(c -> c.getStatutoryAtr() == type).findFirst()
				.map(c -> new CalcAttachTimeDto(c.getTime().getCalcTime().valueAsMinutes(),
						c.getTime().getTime().valueAsMinutes()))
				.orElse(null);
	}

	public HolidayMidnightWork toDomain() {
		return new HolidayMidnightWork(Arrays.asList(new HolidayWorkMidNightTime(
				TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(withinPrescribedHolidayWork.getTime()),
						new AttendanceTime(withinPrescribedHolidayWork.getCalcTime())),
				StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork),
				new HolidayWorkMidNightTime(
						TimeWithCalculation.createTimeWithCalculation(
								new AttendanceTime(excessOfStatutoryHolidayWork.getTime()),
								new AttendanceTime(excessOfStatutoryHolidayWork.getCalcTime())),
						StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork),
				new HolidayWorkMidNightTime(
						TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(publicHolidayWork.getTime()),
								new AttendanceTime(publicHolidayWork.getCalcTime())),
						StaturoryAtrOfHolidayWork.PublicHolidayWork)));
	}
}
