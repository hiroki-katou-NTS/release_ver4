package nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition;

import java.util.List;
import java.util.Map;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.AttendanceItemCondition;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 月別実績をチェックする
 * @author tutk
 *
 */
public interface PerTimeMonActualResultService {
	
	boolean checkPerTimeMonActualResult(YearMonth yearMonth,int closureID, ClosureDate closureDate,String employeeID,AttendanceItemCondition attendanceItemCondition);

	//HoiDD No.257
	Map<String, Integer> checkPerTimeMonActualResult(YearMonth yearMonth, String employeeID, AttendanceItemCondition attendanceItemCondition);
			
}
	