package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.EmployeeGeneralInfoImport;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationsetting.StampReflectionManagement;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.AffiliationInforState;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ClosureOfDailyPerOutPut;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.PeriodInMasterList;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectWorkInforDomainServiceImpl.ExitStatus;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * 
 * @author nampt 日別実績処理 作成処理 ⑥１日の日別実績の作成処理 勤務情報を反映する
 * 
 *
 */
public interface ReflectWorkInforDomainService {
	//KIF use
	void reflectWorkInformation(String companyID, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, ExecutionType reCreateAttr, boolean reCreateWorkType, boolean reCreateWorkPlace,
			EmployeeGeneralInfoImport employeeGeneralInfoImport,
			Optional<StampReflectionManagement> stampReflectionManagement,
			Map<String, Map<String, WorkingConditionItem>> mapWorkingConditionItem,
    		Map<String, Map<String, DateHistoryItem>> mapDateHistoryItem, PeriodInMasterList periodInMasterList,RecreateFlag recreateFlag,Optional<WorkInfoOfDailyPerformance> optDaily);
	//KBT use
	void reflectWorkInformationWithNoInfoImport(String companyID, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, ExecutionType reCreateAttr , boolean reCreateWorkType , boolean reCreateWorkPlace, 
			Optional<StampReflectionManagement> stampReflectionManagement,RecreateFlag recreateFlag,Optional<WorkInfoOfDailyPerformance> optDaily);

	AffiliationInforState createAffiliationInforOfDailyPerfor(String companyId, String employeeId, GeneralDate day,
			String empCalAndSumExecLogID);

	/**
	 * 特定日を日別実績に反映する
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param day
	 * @param workPlaceID
	 * @return
	 */
	SpecificDateAttrOfDailyPerfor reflectSpecificDate(String companyId, String employeeId, GeneralDate day,
			String workPlaceID, PeriodInMasterList periodInMasterList);

	/**
	 * 休業を日別実績に反映する
	 * 
	 * @param employeeId
	 * @param day
	 * @return
	 */
	ClosureOfDailyPerOutPut reflectHolidayOfDailyPerfor(String companyId, String employeeId, GeneralDate day,
			String empCalAndSumExecLogID, WorkInfoOfDailyPerformance workInfoOfDailyPerformance);

	/**
	 * 勤務種別を反映する
	 * 
	 * @param employeeId
	 * @param day
	 * @return
	 */
	WorkTypeOfDailyPerformance reflectWorkType(String companyId, String employeeId, GeneralDate day, String empCalAndSumExecLogID);

	/**
	 * 計算区分を日別実績に反映する
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param day
	 * @return
	 */
	CalAttrOfDailyPerformance reflectCalAttOfDaiPer(String companyId, String employeeId, GeneralDate day,
			AffiliationInforOfDailyPerfor affiliationInforOfDailyPerfor, PeriodInMasterList periodInMasterList);
	/**
	 * 自動打刻セットする
	 * @param companyId
	 * @param workInfoOfDailyPerformanceUpdate
	 * @param workingConditionItem
	 * @param timeLeavingOptional
	 * @param employeeID
	 * @param day
	 * @param stampReflectionManagement
	 * @return
	 */
	TimeLeavingOfDailyPerformance createStamp(String companyId,
			WorkInfoOfDailyPerformance workInfoOfDailyPerformanceUpdate,
			Optional<WorkingConditionItem> workingConditionItem, TimeLeavingOfDailyPerformance timeLeavingOptional,
			String employeeID, GeneralDate day, Optional<StampReflectionManagement> stampReflectionManagement);
	
	/**
	 * 所属情報を反映する
	 * @param companyId
	 * @param employeeId
	 * @param day
	 * @param empCalAndSumExecLogID
	 * @param employeeGeneralInfoImport
	 * @return
	 */
	public AffiliationInforState createAffiliationInforState(String companyId, String employeeId,
			GeneralDate day, String empCalAndSumExecLogID, EmployeeGeneralInfoImport employeeGeneralInfoImport);
	
	WorkInfoOfDailyPerformance reflect(String companyId, String employeeId, GeneralDate day, String empCalAndSumExecLogID,
			ExecutionType reCreateAttr, boolean reCreateWorkType, EmployeeGeneralInfoImport employeeGeneralInfoImport,
			Optional<StampReflectionManagement> stampReflectionManagement,
			Map<String, Map<String, WorkingConditionItem>> mapWorkingConditionItem,
			Map<String, Map<String, DateHistoryItem>> mapDateHistoryItem, PeriodInMasterList periodInMasterList , TimeLeavingOfDailyPerformance timeLeavingOptional,
			RecreateFlag recreateFlag,Optional<WorkInfoOfDailyPerformance> optDaily);
    WorkInfoOfDailyPerformance reflectWithNoInfoImport(String companyId, String employeeId, GeneralDate day,
			String empCalAndSumExecLogID, ExecutionType reCreateAttr, boolean reCreateWorkType,
			Optional<StampReflectionManagement> stampReflectionManagement, TimeLeavingOfDailyPerformance timeLeavingOptional,RecreateFlag recreateFlag,Optional<WorkInfoOfDailyPerformance> optDaily);
    
    public ExitStatus reCreateWorkType(String employeeId, GeneralDate day, String empCalAndSumExecLogID,
            boolean reCreateWorkType, boolean reCreateWorkPlace);
}
