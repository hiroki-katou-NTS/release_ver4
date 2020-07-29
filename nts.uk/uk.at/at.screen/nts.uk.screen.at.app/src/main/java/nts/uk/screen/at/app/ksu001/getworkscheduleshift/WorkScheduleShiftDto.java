package nts.uk.screen.at.app.ksu001.getworkscheduleshift;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;

/**
 * @author laitv
 * 	勤務予定（シフト）dto
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkScheduleShiftDto {
	
	// 社員ID
	public String employeeId;
	// 年月日
	public GeneralDate date;
	// データがあるか
	public boolean haveData;
	// 実績か
	public boolean achievements;
	// 確定済みか
	public boolean confirmed;
	// 勤務予定が必要か
	public boolean needToWork;
	// 応援か
	public Integer supportCategory;
	// シフトコード
	public String shiftCode;
	// シフト名称
	public String shiftName;
	// シフトの編集状態
	public ShiftEditStateDto shiftEditState;
	// 出勤休日区分
	public Integer workHolidayCls;

}
