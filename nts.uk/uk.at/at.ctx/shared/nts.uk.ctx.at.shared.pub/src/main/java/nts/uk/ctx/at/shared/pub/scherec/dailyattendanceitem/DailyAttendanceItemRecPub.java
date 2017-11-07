package nts.uk.ctx.at.shared.pub.scherec.dailyattendanceitem;

import java.util.List;

public interface DailyAttendanceItemRecPub {
	
	List<DailyAttendanceItemRecPubDto> getDailyAttendanceItem(String companyId, List<Integer> dailyAttendanceItemIds);
	
	List<DailyAttendanceItemRecPubDto> getDailyAttendanceItemList(String companyId);
	
	/**
	 * Find daily attendance item by attribute(勤�?�?目属�?�)
	 * +DailyAttendanceAtr-勤�?�?目属�?�:
	 * 	0: コー�?
	 * 	1: マスタを参照する
	 * 	2: 回数
	 * 	3: 金�?
	 * 	4: 区�?
	 * 	5: 時間
	 * 	6: 時刻
	 * 	7: �?�?
	 * @param companyId company id
	 * @param dailyAttendanceAtr daily attendance attribute (勤�?�?目属�?�)
	 * @return list of daily attendance item
	 */
	List<DailyAttendanceItemRecPubDto> getDailyAttendanceItemList(String companyId, List<Integer> dailyAttendanceAtrs);
}
