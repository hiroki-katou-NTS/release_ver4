package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.EmployeeGeneralInfoImport;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.PeriodInMasterList;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;

/**
 * @author nampt 日別実績処理 作成処理 ⑥１日の日別実績の作成処理 再設定
 *
 */
public interface ResetDailyPerforDomainService {

	void resetDailyPerformance(String companyID, String employeeID, GeneralDate processingDate,
		    String empCalAndSumExecLogID, ExecutionType reCreateAttr, PeriodInMasterList periodInMasterList, EmployeeGeneralInfoImport employeeGeneralInfoImport,
		    RecreateFlag recreateFlag,Optional<WorkInfoOfDailyPerformance> workInfoOfDailyPerformance);

}
