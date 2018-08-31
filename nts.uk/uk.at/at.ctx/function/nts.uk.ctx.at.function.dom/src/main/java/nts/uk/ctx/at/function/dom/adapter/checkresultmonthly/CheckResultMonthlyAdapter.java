package nts.uk.ctx.at.function.dom.adapter.checkresultmonthly;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto.AttendanceItemConAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.AgreementCheckCon36FunImport;
import nts.uk.ctx.at.function.dom.adapter.monthlycheckcondition.SpecHolidayCheckConFunImport;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

public interface CheckResultMonthlyAdapter {
	
	boolean checkPublicHoliday(String companyId, String employeeCd , String employeeId,String workplaceId ,boolean isManageComPublicHd, YearMonth yearMonth,SpecHolidayCheckConFunImport specHolidayCheckCon );
	
	Check36AgreementValueImport check36AgreementCondition(String employeeId,YearMonth yearMonth,int closureID,ClosureDate closureDate,AgreementCheckCon36FunImport agreementCheckCon36);
	
	boolean checkPerTimeMonActualResult(YearMonth yearMonth,int closureID, ClosureDate closureDate,String employeeID,AttendanceItemConAdapterDto attendanceItemCondition);
	
	// Call RQ 436
	List<MonthlyRecordValuesImport> getListMonthlyRecords(String employeeId ,YearMonthPeriod period, List<Integer> itemIds);
	
	// Process employee have many record alarm
	List<Check36AgreementValueImport> check36AgreementConditions(String employeeId,List<MonthlyRecordValuesImport> monthlyRecords,AgreementCheckCon36FunImport agreementCheckCon36, Optional<Closure> closure);
	
	Map<String, Integer> checkPerTimeMonActualResult(YearMonth yearMonth, Optional<ClosureId> closureID,
			Optional<ClosureDate> closureDate, String employeeID, AttendanceItemConAdapterDto attendanceItemCondition, List<Integer> attendanceIds);
}
