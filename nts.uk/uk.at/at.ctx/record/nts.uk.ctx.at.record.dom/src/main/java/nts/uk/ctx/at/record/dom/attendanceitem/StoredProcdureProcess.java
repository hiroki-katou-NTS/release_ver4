package nts.uk.ctx.at.record.dom.attendanceitem;

import java.util.List;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.attendanceitem.StoredProcdureProcessing.DailyStoredProcessResult;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

public interface StoredProcdureProcess {

	public Optional<DailyStoredProcessResult> dailyProcessing(IntegrationOfDaily daily, Optional<WorkType> workType, 
			Optional<WorkTimeSetting>  workTime, Optional<PredetemineTimeSetting> predSet);
	
	public List<AnyItemOfMonthly> monthlyProcessing(String companyId, String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate);
	
	public List<AnyItemOfMonthly> monthlyProcessing(String companyId, String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, Optional<AttendanceTimeOfMonthly> attendanceTime);
	
	public List<AnyItemOfMonthly> monthlyProcessing(String companyId, String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, Optional<AttendanceTimeOfMonthly> attendanceTime, List<AnyItemOfMonthly> monthlyOptionalItems);
}