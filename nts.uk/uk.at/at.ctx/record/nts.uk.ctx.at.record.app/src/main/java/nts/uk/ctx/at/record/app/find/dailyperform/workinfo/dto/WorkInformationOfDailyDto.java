package nts.uk.ctx.at.record.app.find.dailyperform.workinfo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.ScheduleTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInformation;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.enums.NotUseAttribute;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;

/** 日別実績の勤務情報 */
@Data
@AttendanceItemRoot(rootName = "日別実績の勤務情報")
public class WorkInformationOfDailyDto extends AttendanceItemCommon {

	/** 勤務実績の勤務情報: 勤務情報 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "勤務実績の勤務情報")
	private WorkInfoDto actualWorkInfo;

	/** 勤務予定の勤務情報: 勤務情報 */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "勤務予定の勤務情報")
	private WorkInfoDto planWorkInfo;

	/** 勤務予定時間帯: 予定時間帯 */
	@AttendanceItemLayout(layout = "C", jpPropertyName = "勤務予定時間帯", listMaxLength = 2, indexField = "workNo")
	private List<ScheduleTimeZoneDto> scheduleTimeZone;
	
	private String employeeId;
	
	private GeneralDate date;

	private int calculationState;

	// 直行区分
	private int goStraightAtr;

	// 直帰区分
	private int backStraightAtr;
	
	public static WorkInformationOfDailyDto getDto(WorkInfoOfDailyPerformance workInfo) {
		WorkInformationOfDailyDto result = new WorkInformationOfDailyDto();
		if (workInfo != null) {
			result.setEmployeeId(workInfo.getEmployeeId());
			result.setDate(workInfo.getYmd());
			result.setActualWorkInfo(createWorkInfo(workInfo.getRecordWorkInformation()));
			result.setBackStraightAtr(workInfo.getBackStraightAtr() == null ? 0 : workInfo.getBackStraightAtr().value);
			result.setCalculationState(workInfo.getCalculationState() == null ? 01 : workInfo.getCalculationState().value);
			result.setGoStraightAtr(workInfo.getGoStraightAtr() == null ? 0 : workInfo.getGoStraightAtr().value);
			result.setPlanWorkInfo(createWorkInfo(workInfo.getScheduleWorkInformation()));
			result.setScheduleTimeZone(getScheduleTimeZone(workInfo.getScheduleTimeSheets()));
			result.exsistData();
		}
		return result;
	}

	private static List<ScheduleTimeZoneDto> getScheduleTimeZone(List<ScheduleTimeSheet> sheets) {
		return sheets == null ? new ArrayList<>() : sheets.stream().map(sts -> {
			return new ScheduleTimeZoneDto(
					sts.getWorkNo() == null ? null : sts.getWorkNo().v().intValue(), 
					sts.getAttendance() == null ? null : sts.getAttendance().v(),
					sts.getLeaveWork() == null ? null : sts.getLeaveWork().v());
		}).sorted((s1, s2) -> s1.getWorkNo().compareTo(s2.getWorkNo())).collect(Collectors.toList());
	}

	private static WorkInfoDto createWorkInfo(WorkInformation workInfo) {
		return workInfo == null ? null : new WorkInfoDto(
					workInfo.getWorkTypeCode() == null ? null : workInfo.getWorkTypeCode().v(),
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
		if(!this.isHaveData()) {
			return null;
		}
		return new WorkInfoOfDailyPerformance(employeeId, getWorkInfo(actualWorkInfo),
					getWorkInfo(planWorkInfo),
					ConvertHelper.getEnum(calculationState, CalculationState.class),
					ConvertHelper.getEnum(goStraightAtr, NotUseAttribute.class),
					ConvertHelper.getEnum(backStraightAtr, NotUseAttribute.class), date,
					ConvertHelper.mapTo(this.getScheduleTimeZone(), (c) -> 
							new ScheduleTimeSheet(
									c.getWorkNo(), 
									c.getWorking() == null ?  0 : c.getWorking(), 
									c.getLeave() == null ? 0: c.getLeave())));
	}

	private WorkInformation getWorkInfo(WorkInfoDto dto) {
		return dto == null ? null : new WorkInformation(dto.getWorkTimeCode(), dto.getWorkTypeCode());
	}
}
