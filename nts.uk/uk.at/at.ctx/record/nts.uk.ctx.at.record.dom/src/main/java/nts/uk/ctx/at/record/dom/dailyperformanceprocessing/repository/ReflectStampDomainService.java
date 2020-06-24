package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.NewReflectStampOutput;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.dailyperformanceprocessing.repository.RecreateFlag;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;

/**
 * 
 * @author nampt 日別実績処理 作成処理 ⑥１日の日別実績の作成処理 2.打刻を取得して反映する
 * 
 *
 */
public interface ReflectStampDomainService {

	// ReflectStampOutput reflectStampInfo(String companyID, String employeeID,
	// GeneralDate processingDate,
	// WorkInfoOfDailyPerformance workInfoOfDailyPerformance,
	// TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, String
	// empCalAndSumExecLogID,
	// ExecutionType reCreateAttr);
	public NewReflectStampOutput reflectStampInfo(String companyID, String employeeID, GeneralDate processingDate,
			WorkInfoOfDailyPerformance workInfoOfDailyPerformance,
			TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, String empCalAndSumExecLogID,
			Optional<CalAttrOfDailyPerformance> calcOfDaily,
			Optional<AffiliationInforOfDailyPerfor> affInfoOfDaily,
			Optional<WorkTypeOfDailyPerformance> workTypeOfDaily,RecreateFlag recreateFlag);

	// 2.打刻を取得して反映する 
	// fixbug 105926
	// class acquireReflectEmbossing gan giong class reflectStampInfo
	public NewReflectStampOutput acquireReflectEmbossing(String companyID, String employeeID,
			GeneralDate processingDate, Optional<WorkInfoOfDailyPerformance> workInfoOfDailyPerformance,
			TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, String empCalAndSumExecLogID,
			Optional<CalAttrOfDailyPerformance> calcOfDaily,
			Optional<AffiliationInforOfDailyPerfor> affInfoOfDaily,
			Optional<WorkTypeOfDailyPerformance> workTypeOfDaily,RecreateFlag recreateFlag);
}
