package nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition;

import java.util.List;
//import java.util.List;
import java.util.Map;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.Check36AgreementValue;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.find.AttendanceItemConditionPubExport;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

public interface CheckResultMonthlyPub {
	/**
	 *  公休所定日数をチェックする
	 * @param companyId
	 * @param employeeCd
	 * @param employeeId
	 * @param workplaceId
	 * @param isManageComPublicHd
	 * @param yearMonth
	 * @param specHolidayCheckCon
	 * @return
	 */
	boolean checkPublicHoliday(String companyId, String employeeCd , String employeeId,String workplaceId ,boolean isManageComPublicHd, YearMonth yearMonth,SpecHolidayCheckConPubEx specHolidayCheckCon );
	
	/**
	 * 36協定チェック条件をチェックする 
	 * @param companyId
	 * @param employeeId
	 * @param date
	 * @param yearMonth
	 * @param year
	 * @param agreementCheckCon36
	 * @return
	 */
	Check36AgreementValue check36AgreementCondition(String employeeId,YearMonth yearMonth,int closureID,ClosureDate closureDate,AgreementCheckCon36PubEx agreementCheckCon36);
	
	/**
	 * 月別実績をチェックする 
	 * @param yearMonth
	 * @param closureID
	 * @param closureDate
	 * @param employeeID
	 * @param attendanceItemCondition
	 * @return
	 */
	boolean checkPerTimeMonActualResult(YearMonth yearMonth,int closureID, ClosureDate closureDate,String employeeID,AttendanceItemConditionPubExport attendanceItemCondition);
	
	//HoiDD No.257
	/**
	 * 月別実績をチェックする 
	 * @param yearMonth
	 * @param employeeID
	 * @param attendanceItemCondition
	 * @param ListAttendanceIds
	 * @return
	 */
	Map<String, Integer> checkPerTimeMonActualResult(YearMonth yearMonth, String employeeID,AttendanceItemConditionPubExport attendanceItemCondition);
	
	//HoiDD No.257
	/**
	 * 月別実績をチェックする 
	 * @param yearMonth
	 * @param employeeID
	 * @param attendanceItemCondition
	 * @param ListAttendanceIds
	 * @return
	 */
	Map<String,Map<YearMonth,Map<String,Integer>>> checkPerTimeMonActualResult(YearMonthPeriod yearMonth, List<String> employeeID, Map<String, AttendanceItemConditionPubExport> attendanceItemCondition,
			Map<String, Map<YearMonth, Map<String,String>>> resultsData);

}
