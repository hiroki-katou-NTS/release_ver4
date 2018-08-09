package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.ws.json.serializer.GeneralDateDeserializer;
import nts.arc.layer.ws.json.serializer.GeneralDateSerializer;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.date.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;

/** 日別実績の勤怠時間 */
@Getter
@Setter
@AttendanceItemRoot(rootName = ItemConst.DAILY_ATTENDANCE_TIME_NAME)
public class AttendanceTimeDailyPerformDto extends AttendanceItemCommon {

	/** 年月日: 年月日 */
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate date;

	/** 社員ID: 社員ID */
	private String employeeID;

	/** 実績時間: 日別実績の勤務実績時間 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = ACTUAL)
	private ActualWorkTimeDailyPerformDto actualWorkTime;

	/** 勤務予定時間: 日別実績の勤務予定時間 */
	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = PLAN)
	private WorkScheduleTimeDailyPerformDto scheduleTime;

	/** 滞在時間: 日別実績の滞在時間 */
	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = STAYING)
	private StayingTimeDto stayingTime;

	/** 医療時間: 日別実績の医療時間 */
	@AttendanceItemLayout(layout = LAYOUT_D, jpPropertyName = MEDICAL, enumField = DEFAULT_ENUM_FIELD_NAME)
	private MedicalTimeDailyPerformDto medicalTime;

	/** 予実差異時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_E, jpPropertyName = PLAN_ACTUAL_DIFF)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer budgetTimeVariance;

	/** 不就労時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_F, jpPropertyName = UNEMPLOYED)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer unemployedTime;
	
	public static AttendanceTimeDailyPerformDto getDto(AttendanceTimeOfDailyPerformance domain) {
		AttendanceTimeDailyPerformDto items = new AttendanceTimeDailyPerformDto();
		if(domain != null){
			items.setEmployeeID(domain.getEmployeeId());
			items.setDate(domain.getYmd());
			items.setActualWorkTime(ActualWorkTimeDailyPerformDto.toActualWorkTime(domain.getActualWorkingTimeOfDaily()));
			//items.setBudgetTimeVariance(domain.getBudgetTimeVariance().valueAsMinutes());
			items.setBudgetTimeVariance(getAttendanceTime(domain.getBudgetTimeVariance()));
			items.setDate(domain.getYmd());
			items.setEmployeeID(domain.getEmployeeId());
			items.setMedicalTime(MedicalTimeDailyPerformDto.fromMedicalCareTime(domain.getMedicalCareTime()));
			items.setScheduleTime(WorkScheduleTimeDailyPerformDto.fromWorkScheduleTime(domain.getWorkScheduleTimeOfDaily()));
			items.setStayingTime(StayingTimeDto.fromStayingTime(domain.getStayingTime()));
			items.setUnemployedTime(getAttendanceTime(domain.getUnEmployedTime()));
			items.exsistData();
		}
		return items;
	}
	private static Integer getAttendanceTime(AttendanceTimeOfExistMinus domain) {
		return domain == null ? null : domain.valueAsMinutes();
	}

	@Override
	public String employeeId() {
		return this.employeeID;
	}

	@Override
	public GeneralDate workingDate() {
		return this.date;
	}
	
	@Override
	public AttendanceTimeOfDailyPerformance toDomain(String emp, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (emp == null) {
			emp = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		return new AttendanceTimeOfDailyPerformance(emp, date,
				scheduleTime == null ? null : scheduleTime.toDomain(), 
				actualWorkTime == null ? null : actualWorkTime.toDomain(),
				stayingTime == null ? null : stayingTime.toDomain(), 
				budgetTimeVariance == null ? null : new AttendanceTimeOfExistMinus(budgetTimeVariance),
				unemployedTime == null ? null : new AttendanceTimeOfExistMinus(unemployedTime));
	}
}
