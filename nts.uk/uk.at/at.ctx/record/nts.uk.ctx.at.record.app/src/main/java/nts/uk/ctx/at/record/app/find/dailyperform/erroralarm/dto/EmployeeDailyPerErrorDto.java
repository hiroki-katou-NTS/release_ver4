package nts.uk.ctx.at.record.app.find.dailyperform.erroralarm.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.layer.ws.json.serializer.GeneralDateDeserializer;
import nts.arc.layer.ws.json.serializer.GeneralDateSerializer;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.date.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;

@AttendanceItemRoot(rootName = "社員の日別実績エラー一覧")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDailyPerErrorDto extends AttendanceItemCommon {

	//TODO: item id not map
	
	/** 会社ID: 会社ID */
	private String companyID;

	/** エラー発生社員: 社員ID */
	private String employeeID;

	/** 処理年月日: 年月日 */
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate date;

	/** エラー: 勤務実績のエラーアラームコード */
//	@AttendanceItemLayout(layout = "A", jpPropertyName = "")
//	@AttendanceItemValue()
	private String errorCode;

	/** 項目一覧: 勤怠項目ID */
	//TODO: set list max value
//	@AttendanceItemLayout(layout = "A", jpPropertyName = "", isList = true, listMaxLength = ?)
//	@AttendanceItemValue(type = ValueType.INTEGER)
	private List<Integer> attendanceItemList;
	
	public  static EmployeeDailyPerErrorDto getDto(EmployeeDailyPerError domain){
		EmployeeDailyPerErrorDto dto = new EmployeeDailyPerErrorDto();
		if(domain != null){
			dto.setAttendanceItemList(domain.getAttendanceItemList());
			dto.setCompanyID(domain.getCompanyID());
			dto.setDate(domain.getDate());
			dto.setEmployeeID(domain.getEmployeeID());
			dto.setErrorCode(domain.getErrorAlarmWorkRecordCode() == null ? null : domain.getErrorAlarmWorkRecordCode().v());
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public String employeeId() {
		return this.employeeID;
	}

	@Override
	public GeneralDate workingDate() {
		return this.date;
	}
	
	@Override
	public EmployeeDailyPerError toDomain(String employeeId, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		return new EmployeeDailyPerError(companyID, employeeId, date,
				errorCode == null ? null : new ErrorAlarmWorkRecordCode(errorCode), attendanceItemList, 0);
	}
}
