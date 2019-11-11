package nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface CalendarWorkPlaceRepository {
	
	/**
	 * get all clendar workplace
	 * @param companyId
	 * @return
	 */
	List<CalendarWorkplace> getAllCalendarWorkplace(String workPlaceId);
	
	List<Integer> getCalendarWorkPlaceSetByYear(String workPlaceId, String year);
	
	List<CalendarWorkplace> getCalendarWorkPlaceByYearMonth(String workPlaceId, String yearMonth);
	
	/**
	 * add new clendar workplace
	 * @param clendarworkplace
	 */
	void addCalendarWorkplace(CalendarWorkplace calendarWorkplace);
	
	/**
	 * update clendar workplace
	 * @param clendarworkplace
	 */
	void updateCalendarWorkplace(CalendarWorkplace calendarWorkplace);
	/**
	 * delete clendar workplace
	 * @param clendarworkplace
	 */
	void deleteCalendarWorkplace(String workPlaceId,GeneralDate date);
	
	void deleteCalendarWorkPlaceByYearMonth(String workPlaceId, String yearMonth);
	
	/**
	 * find clendar workplace by date
	 * @param clendarworkplace
	 */
	Optional<CalendarWorkplace> findCalendarWorkplaceByDate(String workPlaceId,GeneralDate date);
	
	List<CalendarWorkplace> getLstByDateWorkAtr(String workPlaceId, GeneralDate date, int workingDayAtr);
}
