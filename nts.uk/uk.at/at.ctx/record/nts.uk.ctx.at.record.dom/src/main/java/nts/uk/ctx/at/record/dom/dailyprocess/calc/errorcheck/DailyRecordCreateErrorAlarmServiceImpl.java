package nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.BreakTimeStampIncorrectOrderChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.BreakTimeStampLeakageChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.DoubleStampAlgorithm;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.ExitStampCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.ExitStampIncorrectOrderCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.GoingOutStampLeakageChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.GoingOutStampOrderChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.HolidayStampCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.LackOfStampingAlgorithm;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.MissingOfTemporaryStampChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.PCLogOnOffIncorrectOrderCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.PClogOnOffLackOfStamp;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.StampIncorrectOrderAlgorithm;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.TemporaryDoubleStampChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.TemporaryStampOrderChecking;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DailyRecordCreateErrorAlarmServiceImpl implements DailyRecordCreateErrorAlermService{
	
	@Inject
	private LackOfStampingAlgorithm lackOfStamping;
	
	@Inject
	private StampIncorrectOrderAlgorithm stampIncorrectOrderAlgorithm;

	@Inject
	private DoubleStampAlgorithm doubleStampAlgorithm;

	@Inject
	private MissingOfTemporaryStampChecking missingOfTemporaryStampChecking;

	@Inject
	private TemporaryStampOrderChecking temporaryStampOrderChecking;

	@Inject
	private TemporaryDoubleStampChecking temporaryDoubleStampChecking;

	@Inject
	private GoingOutStampLeakageChecking goingOutStampLeakageChecking;

	@Inject
	private GoingOutStampOrderChecking goingOutStampOrderChecking;

	@Inject
	private BreakTimeStampLeakageChecking breakTimeStampLeakageChecking;

	@Inject
	private BreakTimeStampIncorrectOrderChecking breakTimeStampIncorrectOrderChecking;
	
	@Inject
	private ExitStampCheck exitStampCheck;

	@Inject
	private ExitStampIncorrectOrderCheck exitStampIncorrectOrderCheck;

	@Inject
	private PClogOnOffLackOfStamp pClogOnOffLackOfStamp;

	@Inject
	private PCLogOnOffIncorrectOrderCheck pCLogOnOffIncorrectOrderCheck;
	
	@Inject
	private HolidayStampCheck holidayStampCheck;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Override
	//打刻漏れ
	public List<EmployeeDailyPerError> lackOfTimeLeavingStamping(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		String companyId = AppContexts.user().companyId();
		String empId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getAffiliationInfor().getYmd();
		// 出勤系打刻漏れをチェックする
		returnList.add(this.lackOfStamping.lackOfStamping(companyId, empId, targetDate, integrationOfDaily.getWorkInformation(),
				integrationOfDaily.getAttendanceLeave().orElse(null)));
		// 外出系打刻漏れをチェックする
		returnList.addAll(goingOutStampLeakageChecking.goingOutStampLeakageChecking(companyId, empId, targetDate,
				integrationOfDaily.getOutingTime().orElse(null)));
		// 休憩系打刻漏れをチェックする
		integrationOfDaily.getBreakTime().forEach(tc ->{
			returnList.add(breakTimeStampLeakageChecking.breakTimeStampLeakageChecking(companyId, empId, targetDate,tc));
		});
		// 臨時系打刻漏れをチェックする
		returnList.add(missingOfTemporaryStampChecking.missingOfTemporaryStampChecking(companyId, empId, targetDate,
				integrationOfDaily.getTempTime().orElse(null)));
		return returnList;
	}
	
	
	@Override
	//入退門打刻漏れ
	public List<EmployeeDailyPerError> lackOfAttendanceGateStamping(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		// 入退門の打刻漏れをチェックする
		returnList.add(exitStampCheck.exitStampCheck(AppContexts.user().companyId(), integrationOfDaily.getAffiliationInfor().getEmployeeId(), integrationOfDaily.getAffiliationInfor().getYmd(), integrationOfDaily.getAttendanceLeavingGate().orElse(null),
				integrationOfDaily.getWorkInformation()));
		return returnList;
	}
	
	@Override
	//PCログオン打刻漏れ
	public List<EmployeeDailyPerError> lackOfAttendancePCLogOnStamping(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		// PCログオン打刻漏れをチェックする
		returnList.add(this.pClogOnOffLackOfStamp.pClogOnOffLackOfStamp(AppContexts.user().companyId(), integrationOfDaily.getAffiliationInfor().getEmployeeId(), integrationOfDaily.getAffiliationInfor().getYmd(), integrationOfDaily.getPcLogOnInfo().orElse(null),
				integrationOfDaily.getWorkInformation()));
		return returnList;
	}

	
	@Override
	//打刻順序不正
	public List<EmployeeDailyPerError> stampIncorrectOrderAlgorithm(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		// 出勤系打刻順序不正をチェックする
		returnList.add(stampIncorrect(integrationOfDaily));
		//出勤系以外の打刻順序不正をチェックする
		returnList.addAll(stampIncorrectOrderAlgorithmOtherStamp(integrationOfDaily));
		
		return returnList;
	}
	
	@Override
	public EmployeeDailyPerError stampIncorrect(IntegrationOfDaily integrationOfDaily){
		String companyId = AppContexts.user().companyId();
		String empId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getAffiliationInfor().getYmd();
		// 出勤系打刻順序不正をチェックする
		return this.stampIncorrectOrderAlgorithm.stampIncorrectOrder(companyId, empId, targetDate,integrationOfDaily.getAttendanceLeave().orElse(null));
	}
	
	/**
	 * 出退勤打刻以外の順序不正チェック
	 */
	@Override
	public List<EmployeeDailyPerError> stampIncorrectOrderAlgorithmOtherStamp(IntegrationOfDaily integrationOfDaily){
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		String companyId = AppContexts.user().companyId();
		String empId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getAffiliationInfor().getYmd();
		// 外出系打刻順序不正をチェックする
		returnList.addAll(goingOutStampOrderChecking.goingOutStampOrderChecking(companyId, empId, targetDate,
				integrationOfDaily.getOutingTime().orElse(null), integrationOfDaily.getAttendanceLeave().orElse(null), integrationOfDaily.getTempTime().orElse(null)));
		// 休憩系打刻順序不正をチェックする
		integrationOfDaily.getBreakTime().forEach(tc ->{
				returnList.addAll(breakTimeStampIncorrectOrderChecking.breakTimeStampIncorrectOrderChecking(companyId, empId, targetDate,tc));
		});
		// 臨時系打刻順序不正をチェックする
		returnList.add(temporaryStampOrderChecking.temporaryStampOrderChecking(empId, companyId, targetDate,
				integrationOfDaily.getTempTime().orElse(null)));
		// PCログオンログオフの打刻順序不正をチェックする
		returnList.add(this.pCLogOnOffIncorrectOrderCheck.pCLogOnOffIncorrectOrderCheck(companyId, empId, targetDate,
				integrationOfDaily.getPcLogOnInfo().orElse(null), integrationOfDaily.getAttendanceLeave().orElse(null)));
		// 入退門の打刻順序不正をチェックする
		returnList.addAll(exitStampIncorrectOrderCheck.exitStampIncorrectOrderCheck(companyId, empId, targetDate,
				integrationOfDaily.getAttendanceLeavingGate().orElse(null), integrationOfDaily.getAttendanceLeave().orElse(null)));
		return returnList;
	}

	@Override
	//打刻二重チェック
	public List<EmployeeDailyPerError> doubleStampAlgorithm(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		// 出勤系二重打刻をチェックする
		returnList.add(this.doubleStampAlgorithm.doubleStamp(AppContexts.user().companyId(), integrationOfDaily.getAffiliationInfor().getEmployeeId(), integrationOfDaily.getAffiliationInfor().getYmd(), integrationOfDaily.getAttendanceLeave().orElse(null)));
		//ここに外出系、休憩系の二重打刻チェックが入る
		// 臨時系二重打刻をチェックする
		returnList.add(temporaryDoubleStampChecking.temporaryDoubleStampChecking(AppContexts.user().companyId(), integrationOfDaily.getAffiliationInfor().getEmployeeId(), integrationOfDaily.getAffiliationInfor().getYmd(),integrationOfDaily.getTempTime().orElse(null)));
		
		return returnList;
	}
	
	@Override
	//休日打刻
	public Optional<EmployeeDailyPerError> checkHolidayStamp(IntegrationOfDaily integration) {
		//休日チェックをするための事前チェック
		if(integration.getWorkInformation() == null
		|| integration.getWorkInformation().getRecordInfo() == null
		|| integration.getWorkInformation().getRecordInfo().getWorkTypeCode() == null)
			return Optional.empty();
		
		val workType = workTypeRepository.findByDeprecated(AppContexts.user().companyId(), integration.getWorkInformation().getRecordInfo().getWorkTypeCode().toString());
		
		if( integration.getAttendanceLeave() == null
		 ||!integration.getAttendanceLeave().isPresent()
		 ||!workType.isPresent())
			return Optional.empty();
		
		//休日打刻チェック
		val resultError = holidayStampCheck.holidayStamp(AppContexts.user().companyId(), 
														 integration.getAffiliationInfor().getEmployeeId(), 
														 integration.getAffiliationInfor().getYmd(), 
														 workType.get(), 
														 integration.getAttendanceLeave().get());
		if(resultError == null) {
			return Optional.empty();
		}
		else {
			return Optional.of(resultError);
		}
	}
	

}
