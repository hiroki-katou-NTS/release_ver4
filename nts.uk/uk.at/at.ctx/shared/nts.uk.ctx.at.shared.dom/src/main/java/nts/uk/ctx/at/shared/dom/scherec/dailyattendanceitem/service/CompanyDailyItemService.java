package nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.service;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.AttItemName;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.enums.DailyAttendanceAtr;

public interface CompanyDailyItemService {

	/**
	 * 会社の日次項目を取得する
	 * 
	 * @param cid
	 *            会社ID
	 * @param roleId
	 *            ロールID
	 * @param attendanceItemIds
	 *            List＜勤怠項目ID＞
	 * @param attribute
	 *            List＜日次勤怠項目の属性＞
	 * @return
	 */
	List<AttItemName> getDailyItems(String cid, Optional<String> authorityId, List<Integer> attendanceItemIds,
			List<DailyAttendanceAtr> itemAtrs);
	
	List<AttItemName> getDailyItemsNew(String cid, Optional<String> authorityId);
	
	/**
	 * Find by attendance items.
	 *
	 * @param attendanceItems the attendance items
	 * @return the list
	 */
	List<DailyItemDto> findByAttendanceItems(String companyId, List<Integer> attendanceItems);

}
