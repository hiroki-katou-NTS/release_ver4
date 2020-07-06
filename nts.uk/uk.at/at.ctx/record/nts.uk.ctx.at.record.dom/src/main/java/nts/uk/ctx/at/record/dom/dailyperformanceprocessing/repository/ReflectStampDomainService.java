package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.NewReflectStampOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.createdailyoneday.EmbossingExecutionFlag;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.dailyperformanceprocessing.repository.RecreateFlag;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrorMessageInfo;

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
	
	// 2.打刻を取得して反映する 
	/**
	 * 
	 * @param companyID 会社ID
	 * @param employeeID 社員ID
	 * @param processingDate 年月日
	 * @param executionType 再作成フラグ
	 * @param flag 打刻実行フラグ（未反映のみ, 全部）
	 * @param empCalAndSumExecLogID
	 * @param workInfoOfDailyPerformance 日別実績の勤務情報
	 * @param affInfoOfDaily 日別実績の所属情報
	 * @param workTypeOfDaily 日別実績の勤務種別
	 * @param calcOfDaily 日別実績の計算区分
	 * @return
	 */
	public List<ErrorMessageInfo> acquireReflectEmbossingNew(String companyID, String employeeID,
			GeneralDate processingDate,ExecutionTypeDaily executionType,EmbossingExecutionFlag flag,
			String empCalAndSumExecLogID,
			Optional<WorkInfoOfDailyPerformance> workInfoOfDailyPerformance,
			Optional<CalAttrOfDailyPerformance> calcOfDaily,
			Optional<AffiliationInforOfDailyPerfor> affInfoOfDaily,
			Optional<WorkTypeOfDailyPerformance> workTypeOfDaily);
}
