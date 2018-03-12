package nts.uk.ctx.at.record.app.find.dailyperform.shorttimework.dto;

import java.util.List;

import lombok.Data;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkingTimeSheet;
import nts.uk.ctx.at.record.dom.shorttimework.enums.ChildCareAttribute;
import nts.uk.ctx.at.record.dom.shorttimework.primitivevalue.ShortWorkTimFrameNo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.com.time.TimeWithDayAttr;

@AttendanceItemRoot(rootName = "日別実績の短時間勤務時間帯")
@Data
public class ShortTimeOfDailyDto extends AttendanceItemCommon {

	/** 社員ID: 社員ID */
	private String employeeId;

	/** 年月日: 年月日 */
	private GeneralDate ymd;

	/** 時間帯: 短時間勤務時間帯 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "時間帯", listMaxLength = 2, 
			indexField = "shortWorkTimeFrameNo", enumField = "childCareAttr", removeConflictEnum = true)
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
		return new ShortTimeOfDailyPerformance(
					emp,
					ConvertHelper.mapTo(shortWorkingTimeSheets,
							(c) -> new ShortWorkingTimeSheet(new ShortWorkTimFrameNo(c.getShortWorkTimeFrameNo()),
									c.getChildCareAttr() == null ? ChildCareAttribute.CHILD_CARE : ConvertHelper.getEnum(c.getChildCareAttr(), ChildCareAttribute.class),
									createTimeWithDayAttr(c.getStartTime()), createTimeWithDayAttr(c.getEndTime()),
									createAttendanceTime(c.getDeductionTime()), createAttendanceTime(c.getShortTime()))),
					date);
	}

	private TimeWithDayAttr createTimeWithDayAttr(Integer c) {
		return c == null ? null : new TimeWithDayAttr(c);
	}
	
	private AttendanceTime createAttendanceTime(Integer c) {
		return c == null ? null : new AttendanceTime(c);
	}
}
