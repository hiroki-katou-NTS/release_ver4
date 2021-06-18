package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.monthly.root.common.CommonDaysOfMonthlyDto;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.times.AttendanceTimesMonth;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.StgGoStgBackDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.TimeConsumpVacationDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.WorkDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.leave.LeaveOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.specificdays.SpecificDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.AbsenceDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.AttendanceDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.HolidayDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.HolidayWorkDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.PredeterminedDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.RecruitmentDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.SpcVacationDaysOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.TemporaryWorkTimesOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.TwoTimesWorkTimesOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.WorkDaysDetailOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.workdays.workdays.WorkTimesOfMonthly;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の勤務日数 */
public class WorkDaysOfMonthlyDto implements ItemConst, AttendanceItemDataGate {

	/** 休業: 月別実績の休業 */
	@AttendanceItemLayout(jpPropertyName = SUSPENS_WORK, layout = LAYOUT_A)
	private LeaveOfMonthlyDto leave;

	/** 休出日数: 月別実績の休出日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = HOLIDAY_WORK, layout = LAYOUT_B)
	private double holidayWorkDays;

	/** 休日日数: 月別実績の休日日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = HOLIDAY, layout = LAYOUT_C)
	private double holidayDays;

	/** 給与用日数: 月別実績の給与用日数 */
	@AttendanceItemLayout(jpPropertyName = STRAIGHT_GO_BACK, layout = LAYOUT_D)
	private StraightDaysOfMonthlyDto straightDays;

	/** 勤務回数: 月別実績の勤務回数 */
	@AttendanceItemValue(type = ValueType.COUNT)
	@AttendanceItemLayout(jpPropertyName = COUNT, layout = LAYOUT_E)
	private int workTimes;

	/** 勤務日数: 月別実績の勤務日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = DAYS, layout = LAYOUT_F)
	private double workDays;

	/** 欠勤日数: 月別実績の欠勤日数 */
	@AttendanceItemLayout(jpPropertyName = ABSENCE, layout = LAYOUT_G)
	private CommonDaysOfMonthlyDto absenceDays;

	/** 出勤日数: 月別実績の出勤日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = ATTENDANCE, layout = LAYOUT_H)
	private double attendanceDays;

	/** 所定日数: 月別実績の所定日数 */
	@AttendanceItemLayout(jpPropertyName = WITHIN_STATUTORY, layout = LAYOUT_I)
	private PredeterminedDaysOfMonthlyDto predetermineDays;

	/** 特定日数: 月別実績の特定日数 */
	@AttendanceItemLayout(jpPropertyName = SPECIFIC, layout = LAYOUT_J, listMaxLength = 10, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<AggregateSpecificDaysDto> specificDays;

	/** 二回勤務回数: 月別実績の二回勤務回数 */
	@AttendanceItemValue(type = ValueType.COUNT)
	@AttendanceItemLayout(jpPropertyName = TWO_TIMES + COUNT, layout = LAYOUT_K)
	private int twoTimesWorkTimes;

	/** 臨時勤務回数: 月別実績の臨時勤務回数 */
	@AttendanceItemValue(type = ValueType.COUNT)
	@AttendanceItemLayout(jpPropertyName = TEMPORARY + COUNT, layout = LAYOUT_L)
	private int temporaryWorkTimes;

	/** 臨時勤務時間: 月別実績の臨時勤務回数 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = TEMPORARY + TIME, layout = LAYOUT_L)
	private int temporaryWorkTime;
	
	/** 振出日数: 月別実績の振出日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = TRANSFER, layout = LAYOUT_M)
	private double transferdays;

	/** 特別休暇日数: 月別実績の特別休暇日数 */
	@AttendanceItemLayout(jpPropertyName = SPECIAL + HOLIDAY, layout = LAYOUT_N)
	private CommonDaysOfMonthlyDto specialHolidays;

	/** 時間消化休暇時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = TIME_DIGESTION + TIME, layout = LAYOUT_O)
	private int timeDisgestTime;
	
	/** 時間消化休暇日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = TIME_DIGESTION + DAYS, layout = LAYOUT_P)
	private double timeDisgestDays;
	
	public static WorkDaysOfMonthlyDto from(WorkDaysOfMonthly domain) {
		WorkDaysOfMonthlyDto dto = new WorkDaysOfMonthlyDto();
		if(domain != null) {
			dto.setLeave(LeaveOfMonthlyDto.from(domain.getLeave()));
			dto.setHolidayWorkDays(domain.getHolidayWorkDays() == null || domain.getHolidayWorkDays().getDays() == null
					? 0 : domain.getHolidayWorkDays().getDays().v());
			dto.setHolidayDays(domain.getHolidayDays() == null || domain.getHolidayDays().getDays() == null 
					? 0 : domain.getHolidayDays().getDays().v());
			dto.setStraightDays(StraightDaysOfMonthlyDto.from(domain.getStraightDays()));
			dto.setWorkTimes(domain.getWorkTimes() == null || domain.getWorkTimes().getTimes() == null
					? 0 : domain.getWorkTimes().getTimes().v());
			dto.setWorkDays(domain.getWorkDays() == null || domain.getWorkDays().getDays() == null 
					? 0 : domain.getWorkDays().getDays().v());
			dto.setAbsenceDays(CommonDaysOfMonthlyDto.from(domain.getAbsenceDays()));
			dto.setAttendanceDays(domain.getAttendanceDays() == null || domain.getAttendanceDays().getDays() == null 
					? 0 : domain.getAttendanceDays().getDays().v());
			dto.setPredetermineDays(PredeterminedDaysOfMonthlyDto.from(domain.getPredetermineDays()));
			dto.setSpecificDays(ConvertHelper.mapTo(domain.getSpecificDays().getSpecificDays(), c -> AggregateSpecificDaysDto.from(c.getValue())));
			dto.setTwoTimesWorkTimes(domain.getTwoTimesWorkTimes() == null || domain.getTwoTimesWorkTimes().getTimes() == null 
					? 0 : domain.getTwoTimesWorkTimes().getTimes().v());
			dto.setTemporaryWorkTimes(domain.getTemporaryWorkTimes() == null || domain.getTemporaryWorkTimes().getTimes() == null 
					? 0 : domain.getTemporaryWorkTimes().getTimes().v());
			dto.setTemporaryWorkTime(domain.getTemporaryWorkTimes() == null || domain.getTemporaryWorkTimes().getTime() == null 
					? 0 : domain.getTemporaryWorkTimes().getTime().v());
			dto.setTransferdays(domain.getRecruitmentDays().getDays().v());
			dto.setSpecialHolidays(CommonDaysOfMonthlyDto.from(domain.getSpecialVacationDays()));
			dto.setTimeDisgestTime(domain.getTimeConsumpDays().getTime().valueAsMinutes());
			dto.setTimeDisgestDays(domain.getTimeConsumpDays().getDays().v());
		}
		return dto;
	}

	public WorkDaysOfMonthly toDomain() {
		return WorkDaysOfMonthly.of(
						AttendanceDaysOfMonthly.of(new AttendanceDaysMonth(attendanceDays)),
						absenceDays == null ? new AbsenceDaysOfMonthly() : absenceDays.toAbsenceDays(), 
						predetermineDays == null ? new PredeterminedDaysOfMonthly() : predetermineDays.toDomain(),
						WorkDaysDetailOfMonthly.of(new AttendanceDaysMonth(workDays)),
						HolidayDaysOfMonthly.of(new AttendanceDaysMonth(holidayDays)), 
						SpecificDaysOfMonthly.of(ConvertHelper.mapTo(specificDays, c -> c.toDomain())),
						HolidayWorkDaysOfMonthly.of(new AttendanceDaysMonth(holidayWorkDays)),
						straightDays == null ? new StgGoStgBackDaysOfMonthly() : StgGoStgBackDaysOfMonthly.of(
								new AttendanceDaysMonth(straightDays.getStraightGo()), 
								new AttendanceDaysMonth(straightDays.getStraightBack()),
								new AttendanceDaysMonth(straightDays.getStraightGoBack())),
						WorkTimesOfMonthly.of(new AttendanceTimesMonth(workTimes)),
						TwoTimesWorkTimesOfMonthly.of(new AttendanceTimesMonth(twoTimesWorkTimes)), 
						TemporaryWorkTimesOfMonthly.of(
								new AttendanceTimesMonth(temporaryWorkTimes), 
								new AttendanceTimeMonth(temporaryWorkTime)),
						leave == null ? new LeaveOfMonthly() : leave.toDomain(),
						RecruitmentDaysOfMonthly.of(new AttendanceDaysMonth(transferdays)),
						specialHolidays == null ? new SpcVacationDaysOfMonthly() : specialHolidays.toSpcVacationDays(),
						TimeConsumpVacationDaysOfMonthly.of(
								new AttendanceDaysMonth(timeDisgestDays), 
								new AttendanceTimeMonth(timeDisgestTime)));
	}


	@Override
	public Optional<ItemValue> valueOf(String path) {
		switch (path) {
		case HOLIDAY_WORK:
			return Optional.of(ItemValue.builder().value(holidayWorkDays).valueType(ValueType.DAYS));
		case HOLIDAY:
			return Optional.of(ItemValue.builder().value(holidayDays).valueType(ValueType.DAYS));
		case COUNT:
			return Optional.of(ItemValue.builder().value(workTimes).valueType(ValueType.COUNT));
		case DAYS:
			return Optional.of(ItemValue.builder().value(workDays).valueType(ValueType.DAYS));
		case ATTENDANCE:
			return Optional.of(ItemValue.builder().value(attendanceDays).valueType(ValueType.DAYS));
		case (TWO_TIMES + COUNT):
			return Optional.of(ItemValue.builder().value(twoTimesWorkTimes).valueType(ValueType.COUNT));
		case (TEMPORARY + COUNT):
			return Optional.of(ItemValue.builder().value(temporaryWorkTimes).valueType(ValueType.COUNT));
		case (TEMPORARY + TIME):
			return Optional.of(ItemValue.builder().value(temporaryWorkTime).valueType(ValueType.TIME));
		case TRANSFER:
			return Optional.of(ItemValue.builder().value(transferdays).valueType(ValueType.DAYS));
		case (TIME_DIGESTION + TIME):
			return Optional.of(ItemValue.builder().value(timeDisgestTime).valueType(ValueType.TIME));
		case (TIME_DIGESTION + DAYS):
			return Optional.of(ItemValue.builder().value(timeDisgestDays).valueType(ValueType.DAYS));
		default:
			break;
		}
		return AttendanceItemDataGate.super.valueOf(path);
	}

	@Override
	public AttendanceItemDataGate newInstanceOf(String path) {
		switch (path) {
		case SUSPENS_WORK:
			return new LeaveOfMonthlyDto();
		case STRAIGHT_GO_BACK:
			return new StraightDaysOfMonthlyDto();
		case ABSENCE:
			return new CommonDaysOfMonthlyDto();
		case WITHIN_STATUTORY:
			return new PredeterminedDaysOfMonthlyDto();
		case (SPECIAL + HOLIDAY):
			return new CommonDaysOfMonthlyDto();
		case SPECIFIC:
			return new AggregateSpecificDaysDto();
		default:
			break;
		}
		return AttendanceItemDataGate.super.newInstanceOf(path);
	}

	@Override
	public Optional<AttendanceItemDataGate> get(String path) {
		switch (path) {
		case SUSPENS_WORK:
			return Optional.ofNullable(leave);
		case STRAIGHT_GO_BACK:
			return Optional.ofNullable(straightDays);
		case ABSENCE:
			return Optional.ofNullable(absenceDays);
		case WITHIN_STATUTORY:
			return Optional.ofNullable(predetermineDays);
		case (SPECIAL + HOLIDAY):
			return Optional.ofNullable(specialHolidays);
		default:
			return Optional.empty();
		}
	}

	@Override
	public int size(String path) {
		if (SPECIFIC.equals(path)) {
			return 10;
		}
		return AttendanceItemDataGate.super.size(path);
	}

	@Override
	public PropType typeOf(String path) {
		switch (path) {
		case HOLIDAY_WORK:
		case HOLIDAY:
		case COUNT:
		case DAYS:
		case ATTENDANCE:
		case (TWO_TIMES + COUNT):
		case (TEMPORARY + COUNT):
		case (TEMPORARY + TIME):
		case TRANSFER:
		case (TIME_DIGESTION + TIME):
		case (TIME_DIGESTION + DAYS):
			return PropType.VALUE;
		case SPECIFIC:
			return PropType.IDX_LIST;
		default:
			break;
		}
		return AttendanceItemDataGate.super.typeOf(path);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AttendanceItemDataGate> List<T> gets(String path) {
		if (SPECIFIC.equals(path)) {
			return (List<T>) specificDays;
		}
		return AttendanceItemDataGate.super.gets(path);
	}

	@Override
	public void set(String path, ItemValue value) {
		switch (path) {
		case HOLIDAY_WORK:
			holidayWorkDays = value.valueOrDefault(0d); break;
		case HOLIDAY:
			holidayDays = value.valueOrDefault(0d); break;
		case COUNT:
			workTimes = value.valueOrDefault(0); break;
		case DAYS:
			workDays = value.valueOrDefault(0d); break;
		case ATTENDANCE:
			attendanceDays = value.valueOrDefault(0d); break;
		case (TWO_TIMES + COUNT):
			twoTimesWorkTimes = value.valueOrDefault(0); break;
		case (TEMPORARY + COUNT):
			temporaryWorkTimes = value.valueOrDefault(0); break;
		case (TEMPORARY + TIME):
			temporaryWorkTime = value.valueOrDefault(0); break;
		case TRANSFER:
			transferdays = value.valueOrDefault(0d); break;
		case (TIME_DIGESTION + TIME):
			timeDisgestTime = value.valueOrDefault(0); break;
		case (TIME_DIGESTION + DAYS):
			timeDisgestDays = value.valueOrDefault(0d); break;
		default:
			break;
		}
	}

	@Override
	public void set(String path, AttendanceItemDataGate value) {
		switch (path) {
		case SUSPENS_WORK:
			leave = (LeaveOfMonthlyDto) value; break;
		case STRAIGHT_GO_BACK:
			straightDays = (StraightDaysOfMonthlyDto) value; break;
		case ABSENCE:
			absenceDays = (CommonDaysOfMonthlyDto) value; break;
		case WITHIN_STATUTORY:
			predetermineDays = (PredeterminedDaysOfMonthlyDto) value; break;
		case (SPECIAL + HOLIDAY):
			specialHolidays = (CommonDaysOfMonthlyDto) value; break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AttendanceItemDataGate> void set(String path, List<T> value) {
		if (SPECIFIC.equals(path)) {
			specificDays = (List<AggregateSpecificDaysDto>) value;
		}
	}

}
