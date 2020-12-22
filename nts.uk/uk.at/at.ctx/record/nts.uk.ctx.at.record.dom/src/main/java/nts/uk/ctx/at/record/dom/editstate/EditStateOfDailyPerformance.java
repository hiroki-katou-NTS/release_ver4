package nts.uk.ctx.at.record.dom.editstate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;

/**
 * 
 * @author nampt
 * 日別実績の編集状態 - root
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EditStateOfDailyPerformance extends AggregateRoot {
	
	/** 社員ID: 社員ID */
	private String employeeId;
	
	/** 勤怠項目ID: 勤怠項目ID */
	private int attendanceItemId;
	
	/** 処理年月日: 年月日 */
	private GeneralDate ymd;
	
	/** 編集状態: 日別実績の編集状態 */
	@Setter
	private EditStateSetting editStateSetting;
	
	public boolean isHandCorrect() {
		return this.editStateSetting == EditStateSetting.HAND_CORRECTION_MYSELF
				|| this.editStateSetting == EditStateSetting.HAND_CORRECTION_OTHER;
	}

}
