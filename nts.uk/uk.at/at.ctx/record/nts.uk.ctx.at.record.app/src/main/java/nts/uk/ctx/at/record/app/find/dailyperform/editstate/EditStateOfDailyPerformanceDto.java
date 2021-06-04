package nts.uk.ctx.at.record.app.find.dailyperform.editstate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.editstate.EditStateOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.editstate.EditStateSetting;

@AttendanceItemRoot(rootName = ItemConst.DAILY_EDIT_STATE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EditStateOfDailyPerformanceDto extends AttendanceItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
	// TODO: not map item id
	/** 社員ID: 社員ID */
	private String employeeId;

	/** 勤怠項目ID: 勤怠項目ID */
	private int attendanceItemId;

	/** 処理年月日: 年月日 */
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate ymd;

	/** 編集状態: 日別実績の編集状態 */
	// @AttendanceItemLayout(layout = "A", jpPropertyName = "")
	// @AttendanceItemValue(type = ValueType.INTEGER)
	private int editStateSetting;

	public static EditStateOfDailyPerformanceDto getDto(EditStateOfDailyPerformance c) {
		if(c == null) {
			return new EditStateOfDailyPerformanceDto();
		}
		EditStateOfDailyPerformanceDto dto = new EditStateOfDailyPerformanceDto(c.getEmployeeId(), c.getEditState().getAttendanceItemId(), c.getYmd(),
				c.getEditState().getEditStateSetting() == null ? 0 : c.getEditState().getEditStateSetting().value);
		dto.exsistData();
		return dto;
	}
	
	public static EditStateOfDailyPerformanceDto getDto(String employeeID,GeneralDate ymd,EditStateOfDailyAttd c) {
		if(c == null) {
			return new EditStateOfDailyPerformanceDto();
		}
		EditStateOfDailyPerformanceDto dto = new EditStateOfDailyPerformanceDto(employeeID, c.getAttendanceItemId(), ymd,
				c.getEditStateSetting() == null ? 0 : c.getEditStateSetting().value);
		dto.exsistData();
		return dto;
	}
	
	public static EditStateOfDailyPerformanceDto createWith(String employeeId, int itemId, GeneralDate ymd, int state) {
		EditStateOfDailyPerformanceDto dto = new EditStateOfDailyPerformanceDto(employeeId, itemId, ymd, state);
		dto.exsistData();
		return dto;
	}

	@Override
	public EditStateOfDailyPerformanceDto clone() {
		EditStateOfDailyPerformanceDto dto = new EditStateOfDailyPerformanceDto(employeeId(), attendanceItemId, workingDate(), editStateSetting);
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
	public EditStateOfDailyAttd toDomain(String employeeId, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		EditStateOfDailyPerformance domain =  new EditStateOfDailyPerformance(employeeId, attendanceItemId, date, state());
		return domain.getEditState();
	}
	
	public EditStateSetting state(){
		switch (editStateSetting) {
			case 0:
				return EditStateSetting.HAND_CORRECTION_MYSELF;
			case 1:
				return EditStateSetting.HAND_CORRECTION_OTHER;
			default:
				return EditStateSetting.REFLECT_APPLICATION;
		}
	}
}
