package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.VerticalTotalOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workamount.WorkAmountOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workclock.WorkClockOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.WorkDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.worktime.WorkTimeOfMonthlyVT;

@Data
/** 期間別の縦計 */
@NoArgsConstructor
@AllArgsConstructor
public class VerticalTotalOfMonthlyDto implements ItemConst, AttendanceItemDataGate {

	/** 勤務時間: 月別実績の勤務時間 */
	private WorkTimeOfMonthlyDto workTime;

	/** 勤務時刻: 月別実績の勤務時刻 */
	private WorkHourOfMonthlyDto workHour;

	/** 勤務日数: 月別実績の勤務日数 */
	private WorkDaysOfMonthlyDto workDays;

	/** 勤務金額 */
	private WorkAmountOfMonthlyDto workAmount;
	
	public VerticalTotalOfMonthly toDomain(){
		return VerticalTotalOfMonthly.of(workDays == null ? new WorkDaysOfMonthly() : workDays.toDomain(), 
										workTime == null ? new WorkTimeOfMonthlyVT() : workTime.toDomain(), 
										workHour == null ? new WorkClockOfMonthly() : workHour.toDomain(), 
										workAmount == null ? new WorkAmountOfMonthly() : workAmount.toDomain());
	}
	
	public static VerticalTotalOfMonthlyDto from(VerticalTotalOfMonthly domain) {
		VerticalTotalOfMonthlyDto dto = new VerticalTotalOfMonthlyDto();
		if(domain != null) {
			dto.setWorkTime(WorkTimeOfMonthlyDto.from(domain.getWorkTime()));
			dto.setWorkDays(WorkDaysOfMonthlyDto.from(domain.getWorkDays()));
			dto.setWorkHour(WorkHourOfMonthlyDto.from(domain.getWorkClock()));
			dto.setWorkAmount(WorkAmountOfMonthlyDto.from(domain.getWorkAmount()));
		}
		return dto;
	}

	@Override
	public AttendanceItemDataGate newInstanceOf(String path) {
		switch (path) {
		case TIME:
			return new WorkTimeOfMonthlyDto();
		case CLOCK:
			return new WorkHourOfMonthlyDto();
		case DAYS:
			return new WorkDaysOfMonthlyDto();
		case AMOUNT:
			return new WorkAmountOfMonthlyDto();
		default:
			break;
		}
		return AttendanceItemDataGate.super.newInstanceOf(path);
	}

	@Override
	public Optional<AttendanceItemDataGate> get(String path) {
		switch (path) {
		case TIME:
			return Optional.ofNullable(workTime);
		case CLOCK:
			return Optional.ofNullable(workHour);
		case DAYS:
			return Optional.ofNullable(workDays);
		case AMOUNT:
			return Optional.ofNullable(workAmount);
		default:
			break;
		}
		return AttendanceItemDataGate.super.get(path);
	}

	@Override
	public void set(String path, AttendanceItemDataGate value) {
		switch (path) {
		case TIME:
			workTime = (WorkTimeOfMonthlyDto) value; break;
		case CLOCK:
			workHour = (WorkHourOfMonthlyDto) value; break;
		case DAYS:
			workDays = (WorkDaysOfMonthlyDto) value; break;
		case AMOUNT:
			workAmount = (WorkAmountOfMonthlyDto) value; break;
		default:
			break;
		}
	}

	
}
