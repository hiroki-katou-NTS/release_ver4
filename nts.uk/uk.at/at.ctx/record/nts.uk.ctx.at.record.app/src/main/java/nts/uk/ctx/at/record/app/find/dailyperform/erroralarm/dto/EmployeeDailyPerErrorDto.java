package nts.uk.ctx.at.record.app.find.dailyperform.erroralarm.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;

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
	
	public static EmployeeDailyPerErrorDto getDto(EmployeeDailyPerError domain){
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
	public EmployeeDailyPerErrorDto clone(){
		EmployeeDailyPerErrorDto dto = new EmployeeDailyPerErrorDto();
		dto.setAttendanceItemList(attendanceItemList == null ? null : new ArrayList<>(attendanceItemList));
		dto.setCompanyID(companyID);
		dto.setDate(workingDate());
		dto.setEmployeeID(employeeId());
		dto.setErrorCode(errorCode);
		if(isHaveData()){
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
