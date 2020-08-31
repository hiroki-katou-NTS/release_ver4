package nts.uk.ctx.at.function.dom.attendancetype;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemDailyWorkSchedule;
/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface AttendanceTypeRepository {
	/**
	 * get attendance Id by screen use atr
	 * 画面で利用できる勤怠項目一覧
	 * @param companyID
	 * @param screenUseAtr
	 * @return
	 */
	public List<AttendanceType> getItemByScreenUseAtr(String companyID, int screenUseAtr);
	
	/**
	 * 
	 * @param companyId
	 * @param screenUseAtr
	 * @param attendanceItemType
	 * @return
	 */
	public List<AttendanceType> getItemByAtrandType(String companyId, int screenUseAtr, int attendanceItemType);
	
	/**
	 * 
	 * @param companyId
	 * @param attendanceItemType
	 * @return
	 */
	public List<AttendanceType> getItemByAtrandType(String companyId, int attendanceItemType);
	
	/**
	 * 
	 * @param companyId
	 * @param screenUseAtr
	 * @param attendanceItemType
	 * @return
	 */
	public List<AttendanceType> getItemByAtrandType(String companyId, List<ScreenUseAtr> screenUseAtr, int attendanceItemType);
	
	/**
	 * 
	 * @param companyId
	 * @param screenUseAtr
	 * @param attendanceItemType
	 * @param attendanceIds
	 * @return
	 */
	public List<AttendanceType> getItemByAtrandType(String companyId, List<ScreenUseAtr> screenUseAtr, int attendanceItemType, List<Integer> AttendanceIds);
	
	public List<AttendanceType> getDailyAttendanceItem(String companyId, Optional<String> layoutId, List<OutputItemDailyWorkSchedule> outputItem); 
	
}
