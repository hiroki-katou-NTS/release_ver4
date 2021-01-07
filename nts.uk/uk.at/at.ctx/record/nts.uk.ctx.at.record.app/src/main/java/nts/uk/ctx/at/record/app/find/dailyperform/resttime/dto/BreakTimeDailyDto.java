package nts.uk.ctx.at.record.app.find.dailyperform.resttime.dto;

import java.util.ArrayList;
import java.util.Collections;
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
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate.PropType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakgoout.BreakFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.AttendanceItemCommon;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_BREAK_TIME_NAME)
public class BreakTimeDailyDto extends AttendanceItemCommon {

	@Override
	public String rootName() { return DAILY_BREAK_TIME_NAME; }
	/***/
	private static final long serialVersionUID = 1L;
	
	private String employeeId;
	
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate ymd;

	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = TIME_ZONE, listMaxLength = 10, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<TimeSheetDto> timeZone;
		
	public static BreakTimeDailyDto getDto(BreakTimeOfDailyPerformance x) {
		BreakTimeDailyDto dto = new BreakTimeDailyDto();
		if(x != null){
			dto.setEmployeeId(x.getEmployeeId());
			dto.setYmd(x.getYmd());
			dto.setTimeZone(ConvertHelper.mapTo(x.getTimeZone().getBreakTimeSheets(), 
													(c) -> new TimeSheetDto(
														c.getBreakFrameNo().v().intValue(),
														getTimeStamp(c.getStartTime()),
														getTimeStamp(c.getEndTime()),
														c.getBreakTime() == null ? 0 : c.getBreakTime().valueAsMinutes())));
			dto.exsistData();
		}
		return dto;
	}
	public static BreakTimeDailyDto getDto(String employeeID, GeneralDate ymd, BreakTimeOfDailyAttd x) {
		BreakTimeDailyDto dto = new BreakTimeDailyDto();
		if(x != null){
			dto.setEmployeeId(employeeID);
			dto.setYmd(ymd);
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
		dto.setTimeZone(ConvertHelper.mapTo(timeZone, t -> t.clone()));
		if(isHaveData()){
			dto.exsistData();
		}
		return dto;
	}
	
	private static TimeStampDto getTimeStamp(TimeWithDayAttr c) {
		return c == null ? null : new TimeStampDto(c.valueAsMinutes(), null,0);
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
	public BreakTimeOfDailyAttd toDomain(String emp, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		
		BreakTimeOfDailyPerformance domain =  new BreakTimeOfDailyPerformance(emp, date,
					timeZone.stream().filter(c -> judgNotNull(c)).map(c -> toTimeSheet(c)).collect(Collectors.toList()));
		return domain.getTimeZone();
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
	
	@Override
	public AttendanceItemDataGate newInstanceOf(String path) {
		if (path.equals(TIME_ZONE)) {
			return new TimeSheetDto();
		}
		return super.newInstanceOf(path);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends AttendanceItemDataGate> List<T> gets(String path) {
		if (path.equals(TIME_ZONE)) {
			return (List<T>) this.timeZone;
		}
		
		return super.gets(path);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends AttendanceItemDataGate> void set(String path, List<T> value) {
		if (path.equals(TIME_ZONE)) {
			this.timeZone = (List<TimeSheetDto>) value;
		}
	}
	
	@Override
	public int size(String path) {
		if (path.equals(TIME_ZONE)) {
			return 10;
		}
		return 0;
	}
	
	@Override
	public PropType typeOf(String path) {
		if (path.equals(TIME_ZONE)) {
			return PropType.IDX_LIST;
		}
		return PropType.OBJECT;
	}
}