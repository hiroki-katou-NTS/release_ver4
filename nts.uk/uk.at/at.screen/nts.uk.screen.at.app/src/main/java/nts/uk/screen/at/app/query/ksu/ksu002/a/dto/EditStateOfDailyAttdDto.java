package nts.uk.screen.at.app.query.ksu.ksu002.a.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author chungnt
 *
 */
@NoArgsConstructor
@Data
public class EditStateOfDailyAttdDto {
	/** 勤怠項目ID: 勤怠項目ID */
	private int attendanceItemId;

	/** 編集状態: 日別実績の編集状態 */
	// HAND_CORRECTION_MYSELF(0), 手修正（本人）
	// HAND_CORRECTION_OTHER(1), 手修正（他人）
	// REFLECT_APPLICATION(2), 申請反映
	// IMPRINT(3); 打刻反映
	private int editStateSetting;

	public EditStateOfDailyAttdDto(int attendanceItemId, int editStateSetting) {
		super();
		this.attendanceItemId = attendanceItemId;
		this.editStateSetting = editStateSetting;
	}
}
