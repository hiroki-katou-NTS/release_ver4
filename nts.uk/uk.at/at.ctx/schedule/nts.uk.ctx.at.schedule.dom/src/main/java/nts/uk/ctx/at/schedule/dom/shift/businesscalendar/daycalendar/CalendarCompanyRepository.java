package nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface CalendarCompanyRepository {
	/**
	 * get all clendar company
	 * @param companyId
	 * @return
	 */
	List<CalendarCompany> getAllCalendarCompany(String companyId);
	
	List<Integer> getCalendarCompanySetByYear(String companyId, String year);
	
	List<CalendarCompany> getCalendarCompanyByYearMonth(String companyId, String yearMonth);
	
	/**
	 * add new clendar company
	 * @param clendarCompany
	 */
	void addCalendarCompany(CalendarCompany calendarCompany);
	
	/**
	 * update clendar company
	 * @param clendarCompany
	 */
	void updateCalendarCompany(CalendarCompany calendarCompany);
	/**
	 * delete clendar company
	 * @param clendarCompany
	 */
	void deleteCalendarCompany(String companyId,GeneralDate date);
	
	void deleteCalendarCompanyByYearMonth(String companyId, String yearMonth);
	
	/**
	 * find clendar company by date
	 * @param clendarCompany
	 */
	Optional<CalendarCompany> findCalendarCompanyByDate(String companyId,GeneralDate date);
	
	List<CalendarCompany> getLstByDateWorkAtr(String companyId, GeneralDate date, int workingDayAtr);
}
