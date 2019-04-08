package nts.uk.ctx.at.record.app.find.dailyperform.workinfo.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.workinformation.ScheduleTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.enums.NotUseAttribute;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;

/** 日別実績の勤務情報 */
@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_WORK_INFO_NAME)
public class WorkInformationOfDailyDto extends AttendanceItemCommon {

	/** 勤務実績の勤務情報: 勤務情報 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = ACTUAL)
	private WorkInfoDto actualWorkInfo;

	/** 勤務予定の勤務情報: 勤務情報 */
	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = PLAN)
	private WorkInfoDto planWorkInfo;

	/** 勤務予定時間帯: 予定時間帯 */
	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = PLAN + TIME_ZONE, 
			listMaxLength = 2, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<ScheduleTimeZoneDto> scheduleTimeZone;

	private String employeeId;

	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate date;

	private int calculationState;

	// 直行区分
	private int goStraightAtr;

	// 直帰区分
	private int backStraightAtr;

	private int dayOfWeek;

	public static WorkInformationOfDailyDto getDto(WorkInfoOfDailyPerformance workInfo) {
		WorkInformationOfDailyDto result = new WorkInformationOfDailyDto();
		if (workInfo != null) {
			result.setEmployeeId(workInfo.getEmployeeId());
			result.setDate(workInfo.getYmd());
			result.setActualWorkInfo(createWorkInfo(workInfo.getRecordInfo()));
			result.setBackStraightAtr(workInfo.getBackStraightAtr().value);
			result.setCalculationState(workInfo.getCalculationState().value);
			result.setGoStraightAtr(workInfo.getGoStraightAtr().value);
			result.setPlanWorkInfo(createWorkInfo(workInfo.getScheduleInfo()));
			
			result.setScheduleTimeZone(getScheduleTimeZone(workInfo.getScheduleTimeSheets()));
			result.setDayOfWeek(workInfo.getDayOfWeek().value);
			result.exsistData();
		}
		return result;
	}

	private static List<ScheduleTimeZoneDto> getScheduleTimeZone(List<ScheduleTimeSheet> sheets) {
		return ConvertHelper.mapTo(sheets, sts -> {
			return new ScheduleTimeZoneDto(sts.getWorkNo().v(),
					sts.getAttendance() == null ? 0 : sts.getAttendance().v(),
					sts.getLeaveWork() == null ? 0 : sts.getLeaveWork().v());
		});
	}

	private static WorkInfoDto createWorkInfo(WorkInformation workInfo) {
		return workInfo == null ? null : new WorkInfoDto(workInfo.getWorkTypeCode() == null ? null : workInfo.getWorkTypeCode().v(),
						workInfo.getWorkTimeCode() == null ? null : workInfo.getWorkTimeCode().v());
	}

	@Override
	public String employeeId() {
		return this.employeeId;
	}

	@Override
	public GeneralDate workingDate() {
		return this.date;
	}

	@Override
	public WorkInfoOfDailyPerformance toDomain(String employeeId, GeneralDate date) {
		if (!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		return new WorkInfoOfDailyPerformance(employeeId, getWorkInfo(actualWorkInfo), getWorkInfo(planWorkInfo),
				calculationState == CalculationState.No_Calculated.value ? CalculationState.No_Calculated : CalculationState.Calculated, 
				goStraightAtr == NotUseAttribute.Not_use.value ? NotUseAttribute.Not_use : NotUseAttribute.Use,
				backStraightAtr == NotUseAttribute.Not_use.value ? NotUseAttribute.Not_use : NotUseAttribute.Use, date, 
				ConvertHelper.getEnum(dayOfWeek, DayOfWeek.class),
				ConvertHelper.mapTo(this.getScheduleTimeZone(), 
						(c) -> new ScheduleTimeSheet(c.getNo(), c.getWorking(), c.getLeave()),
						(c) -> c.getLeave() != null && c.getWorking() != null));
	}
	
	

	private WorkInformation getWorkInfo(WorkInfoDto dto) {
		return dto == null ? null : new WorkInformation(dto.getWorkTimeCode() == null || dto.getWorkTimeCode().isEmpty() ? null : dto.getWorkTimeCode(), dto.getWorkTypeCode());
	}

	@Override
	public WorkInformationOfDailyDto clone() {
		WorkInformationOfDailyDto result = new WorkInformationOfDailyDto();
		result.setEmployeeId(employeeId());
		result.setDate(workingDate());
		result.setActualWorkInfo(actualWorkInfo == null ? null : actualWorkInfo.clone());
		result.setBackStraightAtr(backStraightAtr);
		result.setCalculationState(calculationState);
		result.setGoStraightAtr(goStraightAtr);
		result.setPlanWorkInfo(planWorkInfo == null ? null : planWorkInfo.clone());
		
		result.setScheduleTimeZone(ConvertHelper.mapTo(scheduleTimeZone, c -> c.clone()));
		result.setDayOfWeek(dayOfWeek);
		if(this.isHaveData()){
			result.exsistData();
		}
		return result;
	}
}
