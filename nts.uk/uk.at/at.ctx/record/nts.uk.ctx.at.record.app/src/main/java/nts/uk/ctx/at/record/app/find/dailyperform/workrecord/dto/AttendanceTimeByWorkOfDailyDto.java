package nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.WithActualTimeStampDto;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.ActualWorkTimeSheet;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.AttendanceTimeByWorkOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.WorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.primitive.ActualWorkTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.primitive.WorkFrameNo;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;

@Data
@AttendanceItemRoot(rootName = "日別実績の作業別勤怠時間")
public class AttendanceTimeByWorkOfDailyDto extends AttendanceItemCommon {

	//TODO: not map item id
	/** 社員ID: 社員ID */
	private String employeeId;

	/** 年月日: 年月日 */
	private GeneralDate ymd;

	/** 作業一覧: 日別実績の作業時間 */
	//TODO: set list max length
//	@AttendanceItemLayout(layout = "A", jpPropertyName = "", isList = true, listMaxLength = ?, setFieldWithIndex = "workFrameNo")
	private List<WorkTimeOfDailyDto> workTimes;
	
	public static AttendanceTimeByWorkOfDailyDto getDto(AttendanceTimeByWorkOfDaily domain) {
		AttendanceTimeByWorkOfDailyDto dto = new AttendanceTimeByWorkOfDailyDto();
		if(domain != null){
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYmd(domain.getYmd());
			dto.setWorkTimes(ConvertHelper.mapTo(domain.getWorkTimes(), 
					(c) -> new WorkTimeOfDailyDto(
								c.getWorkFrameNo().v(), 
								c.getTimeSheet() == null ? null : new ActualWorkTimeSheetDto(
										WithActualTimeStampDto.toWithActualTimeStamp(c.getTimeSheet().getStart()), 
										WithActualTimeStampDto.toWithActualTimeStamp(c.getTimeSheet().getEnd())), 
								c.getWorkTime().valueAsMinutes())));
			dto.exsistData();
		}
		
		return dto;
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
	public AttendanceTimeByWorkOfDaily toDomain(String employeeId, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		return new AttendanceTimeByWorkOfDaily(employeeId, date,
					workTimes == null ? new ArrayList<>() : ConvertHelper.mapTo(workTimes,
								c -> new WorkTimeOfDaily(new WorkFrameNo(c.getWorkFrameNo()),
										new ActualWorkTimeSheet(getStamp(c.getTimeSheet().getStart()),
												getStamp(c.getTimeSheet().getEnd())),
										new ActualWorkTime(c.getWorkTime()))));
	}
	
	private TimeActualStamp getStamp(WithActualTimeStampDto stamp) {
		return stamp == null ? null : stamp.toDomain();
	}
}
