package nts.uk.ctx.at.record.app.find.dailyperform.workinfo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.CalculationState;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.NotUseAttribute;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.ScheduleTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;

/** 日別実績の勤務情報 */
@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_WORK_INFO_NAME)
public class WorkInformationOfDailyDto extends AttendanceItemCommon {

	@Override
	public String rootName() { return DAILY_WORK_INFO_NAME; }
	/***/
	private static final long serialVersionUID = 1L;
	
	/** 勤務実績の勤務情報: 勤務情報 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = ACTUAL)
	private WorkInfoDto actualWorkInfo;

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
	
	private long version;
	
	private Optional<NumberOfDaySuspensionDto> numberDaySuspension;

	public static WorkInformationOfDailyDto getDto(WorkInfoOfDailyPerformance workInfo) {
		WorkInformationOfDailyDto result = new WorkInformationOfDailyDto();
		if (workInfo != null) {
			result.setEmployeeId(workInfo.getEmployeeId());
			result.setDate(workInfo.getYmd());
			result.setActualWorkInfo(createWorkInfo(workInfo.getWorkInformation().getRecordInfo()));
			result.setBackStraightAtr(workInfo.getWorkInformation().getBackStraightAtr().value);
			result.setCalculationState(workInfo.getWorkInformation().getCalculationState().value);
			result.setGoStraightAtr(workInfo.getWorkInformation().getGoStraightAtr().value);
			
			result.setScheduleTimeZone(getScheduleTimeZone(workInfo.getWorkInformation().getScheduleTimeSheets()));
			result.setDayOfWeek(workInfo.getWorkInformation().getDayOfWeek().value);
			result.setNumberDaySuspension(
					workInfo.getWorkInformation().getNumberDaySuspension().map(x -> NumberOfDaySuspensionDto.from(x)));
			result.setVersion(workInfo.getVersion());
			result.exsistData();
		}
		return result;
	}
	
	public static WorkInformationOfDailyDto getDto(String employeeId,GeneralDate ymd, WorkInfoOfDailyAttendance workInfo) {
		WorkInformationOfDailyDto result = new WorkInformationOfDailyDto();
		if (workInfo != null) {
			result.setVersion(workInfo.getVer());
			result.setEmployeeId(employeeId);
			result.setDate(ymd);
			result.setActualWorkInfo(createWorkInfo(workInfo.getRecordInfo()));
			result.setBackStraightAtr(workInfo.getBackStraightAtr().value);
			result.setCalculationState(workInfo.getCalculationState().value);
			result.setGoStraightAtr(workInfo.getGoStraightAtr().value);
			
			result.setScheduleTimeZone(getScheduleTimeZone(workInfo.getScheduleTimeSheets()));
			result.setDayOfWeek(workInfo.getDayOfWeek().value);
			result.setNumberDaySuspension(workInfo.getNumberDaySuspension().map(x -> NumberOfDaySuspensionDto.from(x)));
			result.setVersion(workInfo.getVer());
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
		return workInfo == null ? null : new WorkInfoDto(workInfo.getWorkTypeCode() == null ? null : workInfo.getWorkTypeCode().v(), workInfo.getWorkTimeCodeNotNull().map(m -> m.v()).orElse(null));
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
	public WorkInfoOfDailyAttendance toDomain(String employeeId, GeneralDate date) {
		if (!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		WorkInfoOfDailyPerformance domain = new WorkInfoOfDailyPerformance(
				employeeId, getWorkInfo(actualWorkInfo),
				calculationState == CalculationState.No_Calculated.value ? CalculationState.No_Calculated : CalculationState.Calculated, 
				goStraightAtr == NotUseAttribute.Not_use.value ? NotUseAttribute.Not_use : NotUseAttribute.Use,
				backStraightAtr == NotUseAttribute.Not_use.value ? NotUseAttribute.Not_use : NotUseAttribute.Use, date, 
				ConvertHelper.getEnum(dayOfWeek, DayOfWeek.class),
				ConvertHelper.mapTo(this.getScheduleTimeZone(), 
						(c) -> new ScheduleTimeSheet(c.getNo(), c.getWorking(), c.getLeave()),
						(c) -> c.getLeave() != null && c.getWorking() != null),
				this.numberDaySuspension.map(x -> x.toDomain())
				);
		domain.setVersion(this.version);
		domain.getWorkInformation().setVer(this.version);
		return domain.getWorkInformation();
	}
	
	private WorkInformation getWorkInfo(WorkInfoDto dto) {
		return dto == null ? null : new WorkInformation(dto.getWorkTypeCode(), dto.getWorkTimeCode());
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
		
		result.setScheduleTimeZone(ConvertHelper.mapTo(scheduleTimeZone, c -> c.clone()));
		result.setDayOfWeek(dayOfWeek);
		result.setNumberDaySuspension(numberDaySuspension.map(x -> x.clone()));
		result.version = this.version;
		if(this.isHaveData()){
			result.exsistData();
		}
		return result;
	}

	@Override
	public Optional<AttendanceItemDataGate> get(String path) {
		switch (path) {
		case ACTUAL:
			return Optional.ofNullable(this.actualWorkInfo);
		default:
			return Optional.empty();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends AttendanceItemDataGate> List<T> gets(String path) {
		if (path.equals(PLAN + TIME_ZONE)) {
			return (List<T>) this.scheduleTimeZone;
		}
		return new ArrayList<>();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends AttendanceItemDataGate> void set(String path, List<T> value) {
		if (path.equals(PLAN + TIME_ZONE)) {
			this.scheduleTimeZone = (List<ScheduleTimeZoneDto>) value;
		}
	}

	@Override
	public void set(String path, AttendanceItemDataGate value) {
		switch (path) {
		case ACTUAL:
			this.actualWorkInfo = (WorkInfoDto) value;
			break;
		default:
			break;
		}
	}

	@Override
	public boolean isRoot() { return true; }
	

	@Override
	public AttendanceItemDataGate newInstanceOf(String path) {
		switch (path) {
		case ACTUAL:
			return new WorkInfoDto();
		case (PLAN + TIME_ZONE):
			return new ScheduleTimeZoneDto();
		default:
			return null;
		}
	}

	@Override
	public int size(String path) {
		if (path.equals(PLAN + TIME_ZONE)) {
			return 2;
		}
		return 0;
	}

	@Override
	public PropType typeOf(String path) {
		if (path.equals(PLAN + TIME_ZONE)) {
			return PropType.IDX_LIST;
		}
		
		return PropType.OBJECT;
	}
	
}
