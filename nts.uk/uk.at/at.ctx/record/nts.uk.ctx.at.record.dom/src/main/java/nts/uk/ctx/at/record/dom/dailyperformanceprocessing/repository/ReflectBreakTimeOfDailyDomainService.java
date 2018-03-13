package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;

public interface ReflectBreakTimeOfDailyDomainService {
		//就業時間帯の休憩時間帯を日別実績に反映する
		public BreakTimeOfDailyPerformance reflectBreakTime(String companyId,String employeeID, GeneralDate processingDate,TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, 
				WorkInfoOfDailyPerformance WorkInfo);
}
