package nts.uk.ctx.at.shared.dom.attendance;

import java.util.List;

public interface AttendanceItemRepository {
	/**
	 * get possible attendance item
	 * 
	 * @param companyId
	 * @return
	 */
	List<AttendanceItem> getPossibleAttendanceItems(String companyId, List<Integer> lstPossible);
	List<AttendanceItem> getAttendanceItems(String companyId);
	List<AttendanceItem> getAttendanceItems(String companyId, int useAtr);

}
