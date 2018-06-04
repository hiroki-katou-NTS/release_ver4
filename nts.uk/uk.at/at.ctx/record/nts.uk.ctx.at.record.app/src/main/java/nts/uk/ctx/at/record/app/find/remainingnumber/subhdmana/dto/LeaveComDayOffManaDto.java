package nts.uk.ctx.at.record.app.find.remainingnumber.subhdmana.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.LeaveComDayOffManagement;

/**
 * @author hiep.ld
 *
 */
@AllArgsConstructor
@Data
public class LeaveComDayOffManaDto {
	private String leaveID;

	// 代休ID
	private String comDayOffID;

	// 使用日数
	private String usedDays;

	// 使用時間数
	private int usedHours;

	// 対象選択区分
	private int targetSelectionAtr;

	public static LeaveComDayOffManaDto convertToDto(LeaveComDayOffManagement leaveData) {
		return new LeaveComDayOffManaDto(leaveData.getLeaveID(), leaveData.getComDayOffID(),
				leaveData.getUsedDays().v().toString(), leaveData.getUsedHours().v().intValue(),
				leaveData.getTargetSelectionAtr().value);
	}
}
