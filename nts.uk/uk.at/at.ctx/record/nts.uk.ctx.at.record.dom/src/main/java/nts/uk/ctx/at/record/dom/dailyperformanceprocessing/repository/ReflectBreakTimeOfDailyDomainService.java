package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;

public interface ReflectBreakTimeOfDailyDomainService {
		//就業時間帯の休憩時間帯を日別実績に反映する
	public BreakTimeOfDailyPerformance reflectBreakTime(String companyId,String employeeID, GeneralDate processingDate,String empCalAndSumExecLogID,TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, 
				WorkInfoOfDailyPerformance WorkInfo);
		//就業時間帯の休憩時間帯を日別実績に写す
	public BreakTimeOfDailyPerformance reflectBreakTimeZone(String companyId, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance,
	
	
	public Optional<BreakTimeOfDailyPerformance> getBreakTime(String companyId, String employeeID, GeneralDate processingDate,
			WorkInfoOfDailyPerformance WorkInfo);
	
}
