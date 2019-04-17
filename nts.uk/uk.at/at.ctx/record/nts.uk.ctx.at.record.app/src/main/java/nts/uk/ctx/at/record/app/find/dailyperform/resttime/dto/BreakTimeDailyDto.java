package nts.uk.ctx.at.record.app.find.dailyperform.resttime.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.TimeSheetDto;
import nts.uk.ctx.at.record.app.find.dailyperform.common.TimeStampDto;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_BREAK_TIME_NAME)
public class BreakTimeDailyDto extends AttendanceItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
	private String employeeId;
	
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate ymd;
	
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = TIME_ZONE, 
			listMaxLength = 10, indexField = DEFAULT_INDEX_FIELD_NAME, needCheckIDWithMethod = DEFAULT_CHECK_ENUM_METHOD)
	private List<TimeSheetDto> timeZone;

	/** 休憩種類 */
	private int attr;

	public String enumText() {
		switch (this.attr) {
		case 0:
			return E_WORK_REF;
		case 1:
			return E_SCHEDULE_REF;
		default:
			return EMPTY_STRING;
		}
	}
	
	public static BreakTimeDailyDto getDto(BreakTimeOfDailyPerformance x) {
		BreakTimeDailyDto dto = new BreakTimeDailyDto();
		if(x != null){
			dto.setEmployeeId(x.getEmployeeId());
			dto.setYmd(x.getYmd());
			dto.setAttr(x.getBreakType() == null ? 0 : x.getBreakType().value);
			dto.setTimeZone(ConvertHelper.mapTo(x.getBreakTimeSheets(), 
													(c) -> new TimeSheetDto(
														c.getBreakFrameNo().v().intValue(),
														getTimeStamp(c.getStartTime()),
														getTimeStamp(c.getEndTime()),
														c.getBreakTime() == null ? 0 : c.getBreakTime().valueAsMinutes())));
			dto.exsistData();
		}
		return dto;
	}
	
	@Override
	public BreakTimeDailyDto clone() {
		BreakTimeDailyDto dto = new BreakTimeDailyDto();
		dto.setEmployeeId(employeeId());
		dto.setYmd(workingDate());
		dto.setAttr(attr);
		dto.setTimeZone(ConvertHelper.mapTo(timeZone, t -> t.clone()));
		if(isHaveData()){
			dto.exsistData();
		}
		return dto;
	}
	
	private static TimeStampDto getTimeStamp(TimeWithDayAttr c) {
		return c == null ? null : new TimeStampDto(c.valueAsMinutes(), null, null, 0);
	}

	@Override
	public String employeeId() {
		return this.employeeId;
	}

	@Override
	public GeneralDate workingDate() {
		return this.ymd;
	}
	
	@Override
	public BreakTimeOfDailyPerformance toDomain(String emp, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		
		return new BreakTimeOfDailyPerformance(emp,
					attr == BreakType.REFER_SCHEDULE.value ? BreakType.REFER_SCHEDULE : BreakType.REFER_WORK_TIME,
					timeZone.stream().filter(c -> judgNotNull(c)).map(c -> toTimeSheet(c)).collect(Collectors.toList()),
					date);
	}
	
	private boolean judgNotNull(TimeSheetDto d){
		return d != null && ((d.getEnd() != null && d.getEnd().getTimesOfDay() != null) || (d.getStart() != null && d.getStart().getTimesOfDay() != null));
	}
	
	private BreakTimeSheet toTimeSheet(TimeSheetDto d){
		return new BreakTimeSheet(new BreakFrameNo(d.getNo()),
				createWorkStamp(d.getStart()),
				createWorkStamp(d.getEnd()),
				new AttendanceTime(d.getBreakTime()));
	}

	private TimeWithDayAttr createWorkStamp(TimeStampDto d) {
		return d == null || d.getTimesOfDay() == null ? null : new TimeWithDayAttr(d.getTimesOfDay());
	}
}
