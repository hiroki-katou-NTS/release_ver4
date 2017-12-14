package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ReflectStampOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.StampReflectRangeOutput;
import nts.uk.ctx.at.record.dom.stamp.StampItem;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
//打刻を反映する
public interface ReflectEmbossingDomainService {
	
	public ReflectStampOutput reflectStamp(WorkInfoOfDailyPerformance WorkInfo, TimeLeavingOfDailyPerformance timeDailyPer,List<StampItem> lstStampItem, StampReflectRangeOutput s,  GeneralDate date, String employeeId);
}
