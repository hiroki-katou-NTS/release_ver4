package nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.WithActualTimeStampDto;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.ActualWorkTimeSheet;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.AttendanceTimeByWorkOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.WorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.primitive.ActualWorkTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.primitive.WorkFrameNo;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_ATTENDANCE_TIME_BY_WORK_NAME)
public class AttendanceTimeByWorkOfDailyDto extends AttendanceItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
	//TODO: not map item id
	/** 社員ID: 社員ID */
	private String employeeId;

	/** 年月日: 年月日 */
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
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
								c.getWorkTime() == null ? null : c.getWorkTime().valueAsMinutes())));
			dto.exsistData();
		}
		
		return dto;
	}
	
	@Override
	public AttendanceTimeByWorkOfDailyDto clone() {
		AttendanceTimeByWorkOfDailyDto dto = new AttendanceTimeByWorkOfDailyDto();
		dto.setEmployeeId(employeeId());
		dto.setYmd(workingDate());
		dto.setWorkTimes(workTimes == null ? null : workTimes.stream().map(t -> t.clone()).collect(Collectors.toList()));
		if(isHaveData()){
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
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		return new AttendanceTimeByWorkOfDaily(employeeId, date,
					ConvertHelper.mapTo(workTimes,
								c -> new WorkTimeOfDaily(new WorkFrameNo(c.getWorkFrameNo()),
										new ActualWorkTimeSheet(getStamp(c.getTimeSheet().getStart()),
												getStamp(c.getTimeSheet().getEnd())),
										new ActualWorkTime(c.getWorkTime() == null ? 0 : c.getWorkTime()))));
	}
	
	private TimeActualStamp getStamp(WithActualTimeStampDto stamp) {
		return stamp == null ? new TimeActualStamp() : stamp.toDomain();
	}
}
