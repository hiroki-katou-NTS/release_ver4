package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayMidnightWork;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkMidNightTime;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;

/** 休出深夜 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayMidnightWorkDto {

	 /** 休出深夜時間: 休出深夜時間 */
//	 @AttendanceItemLayout(layout="A", jpPropertyName="休出深夜時間")
//	 private HolidayWorkMidNightTimeDto holidayWorkMidNightTime;

	/** 法定内: 計算付き時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName="法定内休出")
	private CalcAttachTimeDto withinPrescribedHolidayWork;

	/** 法定外: 計算付き時間 */
	@AttendanceItemLayout(layout = "B", jpPropertyName="法定外休出")
	private CalcAttachTimeDto excessOfStatutoryHolidayWork;

	/** 祝日: 計算付き時間 */
	@AttendanceItemLayout(layout = "C", jpPropertyName="祝日休出")
	private CalcAttachTimeDto publicHolidayWork;
	
	public static HolidayMidnightWorkDto fromHolidayMidnightWork(HolidayMidnightWork domain){
		return domain == null ? null : new HolidayMidnightWorkDto(getWorkTime(
				domain.getHolidayWorkMidNightTime(), StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork), 
				getWorkTime(domain.getHolidayWorkMidNightTime(), StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork), 
				getWorkTime(domain.getHolidayWorkMidNightTime(), StaturoryAtrOfHolidayWork.PublicHolidayWork));
	}
	
	private static CalcAttachTimeDto getWorkTime(List<HolidayWorkMidNightTime> source, StaturoryAtrOfHolidayWork type){
		return source.stream().filter(c -> c.getStatutoryAtr() == type).findFirst().map(c -> 
														CalcAttachTimeDto.toTimeWithCal(c.getTime())).orElse(null);
	}
	
	public HolidayMidnightWork toDomain() {
		return withinPrescribedHolidayWork == null ? null : new HolidayMidnightWork(Arrays.asList(
				newMidNightTime(withinPrescribedHolidayWork, StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork),
				newMidNightTime(excessOfStatutoryHolidayWork, StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork),
				newMidNightTime(publicHolidayWork, StaturoryAtrOfHolidayWork.PublicHolidayWork)));
	}

	public HolidayWorkMidNightTime newMidNightTime(CalcAttachTimeDto time, StaturoryAtrOfHolidayWork attr) {
		return new HolidayWorkMidNightTime(time.createTimeWithCalc(), attr);
	}
}
