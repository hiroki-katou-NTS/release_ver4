package nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;

public interface LeaveComDayOffManaRepository {
	
	void add(LeaveComDayOffManagement domain);
	
	void update(LeaveComDayOffManagement domain);
	
	void delete(String sid, GeneralDate occDate, GeneralDate digestDate);
	
	List<LeaveComDayOffManagement> getByLeaveID(String sid, GeneralDate occDate);
	
	List<LeaveComDayOffManagement> getByListDate(String sid, List<GeneralDate> lstDate);
	
	List<LeaveComDayOffManagement> getBycomDayOffID(String sid,  GeneralDate digestDate);
	
	void insertAll(List<LeaveComDayOffManagement> entitiesLeave);
	
	void deleteByLeaveId(String sid, GeneralDate occDate);
	
	void deleteByComDayOffID(String sid,  GeneralDate digestDate);

	List<LeaveComDayOffManagement> getByListComLeaveID(String sid, DatePeriod period);
	List<LeaveComDayOffManagement> getByListComId(String sid,  DatePeriod period);
	/**
	 * Delete 休出代休紐付け管理 by comDayOffId
	 * @param comDayOffId
	 */
	void deleteByComDayOffId(String sid,  GeneralDate digestDate);
	
	/**
	 * 
	 * @param sid
	 * @param lstOccDate
	 * @param lstDigestDate
	 * @return
	 */
	List<LeaveComDayOffManagement> getByListOccDigestDate(String sid, List<GeneralDate> lstOccDate, List<GeneralDate> lstDigestDate);
}
