package nts.uk.ctx.at.record.app.find.dailyperform.shorttimework.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkingTimeSheet;
import nts.uk.ctx.at.record.dom.shorttimework.enums.ChildCareAttribute;
import nts.uk.ctx.at.record.dom.shorttimework.primitivevalue.ShortWorkTimFrameNo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_SHORT_TIME_NAME)
public class ShortTimeOfDailyDto extends AttendanceItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
	/** 社員ID: 社員ID */
	private String employeeId;

	/** 年月日: 年月日 */
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate ymd;

	/** 時間帯: 短時間勤務時間帯 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = TIME_ZONE, 
			indexField = DEFAULT_INDEX_FIELD_NAME, listMaxLength = 2, 
			enumField = DEFAULT_ENUM_FIELD_NAME, removeConflictEnum = true)
	private List<ShortWorkTimeSheetDto> shortWorkingTimeSheets;

	public static ShortTimeOfDailyDto getDto(ShortTimeOfDailyPerformance domain){
		ShortTimeOfDailyDto result = new ShortTimeOfDailyDto();
		if (domain != null) {
			result.setEmployeeId(domain.getEmployeeId());
			result.setYmd(domain.getYmd());
			result.setShortWorkingTimeSheets(ConvertHelper.mapTo(domain.getShortWorkingTimeSheets(),
					(c) -> new ShortWorkTimeSheetDto(c.getShortWorkTimeFrameNo().v(), 
														c.getChildCareAttr() == null ? 0 : c.getChildCareAttr().value,
														c.getStartTime() == null ? null : c.getStartTime().valueAsMinutes(),
														c.getEndTime() == null ? null : c.getEndTime().valueAsMinutes(),
														c.getDeductionTime() == null ? null : c.getDeductionTime().valueAsMinutes(),
														c.getShortTime() == null ? null : c.getShortTime().valueAsMinutes())));
			result.exsistData();
		}
		return result;
	}

	@Override
	public ShortTimeOfDailyDto clone(){
		ShortTimeOfDailyDto result = new ShortTimeOfDailyDto();
		result.setEmployeeId(employeeId());
		result.setYmd(workingDate());
		result.setShortWorkingTimeSheets(shortWorkingTimeSheets == null ? null 
				: shortWorkingTimeSheets.stream().map(t -> t.clone()).collect(Collectors.toList()));
		if (isHaveData()) {
			result.exsistData();
		}
		return result;
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
	public ShortTimeOfDailyPerformance toDomain(String emp, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (emp == null) {
			emp = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		return new ShortTimeOfDailyPerformance(
					emp,
					ConvertHelper.mapTo(shortWorkingTimeSheets,
							(c) -> new ShortWorkingTimeSheet(new ShortWorkTimFrameNo(c.getNo()),
									c.getAttr() == ChildCareAttribute.CHILD_CARE.value 
											? ChildCareAttribute.CHILD_CARE : ChildCareAttribute.CARE,
									createTimeWithDayAttr(c.getStartTime()), createTimeWithDayAttr(c.getEndTime()),
									createAttendanceTime(c.getDeductionTime()), createAttendanceTime(c.getShortTime()))),
					date);
	}

	private TimeWithDayAttr createTimeWithDayAttr(Integer c) {
		return c == null ? TimeWithDayAttr.THE_PRESENT_DAY_0000 : new TimeWithDayAttr(c);
	}
	
	private AttendanceTime createAttendanceTime(Integer c) {
		return c == null ? AttendanceTime.ZERO : new AttendanceTime(c);
	}
}
