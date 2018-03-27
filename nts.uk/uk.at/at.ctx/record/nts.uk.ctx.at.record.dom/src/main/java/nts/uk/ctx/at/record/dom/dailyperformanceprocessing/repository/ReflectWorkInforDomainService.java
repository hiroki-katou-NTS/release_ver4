package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.AffiliationInforState;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;

/**
 * 
 * @author nampt
 * 日別実績処理
 * 作成処理
 * ⑥１日の日別実績の作成処理
 * 勤務情報を反映する
 * 
 *
 */
public interface ReflectWorkInforDomainService {

	void reflectWorkInformation(String companyID, String employeeID, GeneralDate processingDate, String empCalAndSumExecLogID, ExecutionType reCreateAttr, boolean reCreateWorkType);
	
	AffiliationInforState createAffiliationInforOfDailyPerfor(String companyId, String employeeId, GeneralDate day,String empCalAndSumExecLogID);
	
	/**
	 * 特定日を日別実績に反映する
	 * @param companyId
	 * @param employeeId
	 * @param day
	 * @param workPlaceID
	 * @return
	 */
	SpecificDateAttrOfDailyPerfor reflectSpecificDate(String companyId, String employeeId, GeneralDate day, String workPlaceID);
	
	/**
	 * 休業を日別実績に反映する
	 * @param employeeId
	 * @param day
	 * @return
	 */
	Optional<WorkInfoOfDailyPerformance> reflectHolidayOfDailyPerfor(String companyId, String employeeId, GeneralDate day);
	
	/**
	 * 勤務種別を反映する
	 * @param employeeId
	 * @param day
	 * @return
	 */
	WorkTypeOfDailyPerformance reflectWorkType(String employeeId, GeneralDate day, String empCalAndSumExecLogID);
}
