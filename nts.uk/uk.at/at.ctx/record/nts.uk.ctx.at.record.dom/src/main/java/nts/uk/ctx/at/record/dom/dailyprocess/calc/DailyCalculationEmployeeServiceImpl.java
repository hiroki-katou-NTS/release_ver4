package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;
import java.util.Optional;
//import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
//import nts.arc.task.data.TaskDataSetter;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.repo.AttendanceTimeByWorkOfDailyRepository;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.OutingTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.calculationattribute.repo.CalAttrOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.AttendanceLeavingGateOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerformRepo;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
//import nts.uk.ctx.at.record.dom.dailyprocess.calc.DailyCalculationServiceImpl.StateHolder;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
//import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
//import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
//import nts.uk.ctx.at.record.dom.optitem.applicable.EmpCondition;
//import nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository;
//import nts.uk.ctx.at.record.dom.optitem.calculation.Formula;
//import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository;
import nts.uk.ctx.at.record.dom.raisesalarytime.repo.SpecificDateAttrOfDailyPerforRepo;
import nts.uk.ctx.at.record.dom.shorttimework.repo.ShortTimeOfDailyPerformanceRepository;
//import nts.uk.ctx.at.record.dom.statutoryworkinghours.DailyStatutoryWorkingHours;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagementRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.service.ErAlCheckService;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPersonRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.shr.com.context.AppContexts;
//import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;
//import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
//import nts.uk.shr.com.context.AppContexts;
//import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
//import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
//import nts.uk.shr.com.context.AppContexts;
//import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス：日別計算　（社員の日別実績を計算）
 * @author keisuke_hoshina
 */
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class DailyCalculationEmployeeServiceImpl implements DailyCalculationEmployeeService {

	//*****（未）　以下、日別実績の勤怠情報など、日別計算のデータ更新に必要なリポジトリを列記。
	/** リポジトリ：日別実績の勤怠時間 */
	@Inject
	private AttendanceTimeRepository attendanceTimeRepository;
	
	/** リポジトリ：日別実績の勤務情報 */
	@Inject
	private WorkInformationRepository workInformationRepository;  
	
	/** リポジトリ：日別実績の計算区分 */
	@Inject
	private CalAttrOfDailyPerformanceRepository calAttrOfDailyPerformanceRepository;
	
	/** リポジトリ：日別実績の所属情報 */
	@Inject
	private AffiliationInforOfDailyPerforRepository affiliationInforOfDailyPerforRepository;
	
	/** リポジトリ：日別実績の勤務種別 */
	@Inject
	private WorkTypeOfDailyPerforRepository workTypeOfDailyPerforRepository;
	
	/** リポジトリ：日別実績のPCログオン情報 */
	@Inject
	private PCLogOnInfoOfDailyRepo pcLogOnInfoOfDailyRepo; 
	
	/** リポジトリ:社員の日別実績エラー一覧 */
	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepository;
	
	/** リポジトリ：日別実績の外出時間帯 */
	@Inject
	private OutingTimeOfDailyPerformanceRepository outingTimeOfDailyPerformanceRepository;
	
	/** リポジトリ：日別実績の休憩時間帯 */
	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDailyPerformanceRepository; 
	
	/** リポジトリ：日別実績の作業別勤怠時間 */
	@Inject
	private AttendanceTimeByWorkOfDailyRepository attendanceTimeByWorkOfDailyRepository;
	
	/** リポジトリ：日別実績の出退勤 */
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyPerformanceRepository;
	
	/** リポジトリ：日別実績の短時間勤務時間帯 */
	@Inject
	private ShortTimeOfDailyPerformanceRepository shortTimeOfDailyPerformanceRepository;
	
	/** リポジトリ：日別実績の特定日区分 */
	@Inject
	private SpecificDateAttrOfDailyPerforRepo specificDateAttrOfDailyPerforRepo;
	
	/** リポジトリ：日別実績の入退門 */
	@Inject
	private AttendanceLeavingGateOfDailyRepo attendanceLeavingGateOfDailyRepo;
	
	/** リポジトリ：日別実績の任意項目 */
	@Inject
	private AnyItemValueOfDailyRepo anyItemValueOfDailyRepo;
	
	/** リポジトリ：日別実績のの編集状態 */
	@Inject
	private EditStateOfDailyPerformanceRepository editStateOfDailyPerformanceRepository;
	
	/** リポジトリ：日別実績の臨時出退勤 */
	@Inject
	private TemporaryTimeOfDailyPerformanceRepository temporaryTimeOfDailyPerformanceRepository;
	
	/** リポジトリ：日別実績の備考 */
	@Inject
	private RemarksOfDailyPerformRepo remarksRepository;
	
	@Inject 
	private ErAlCheckService determineErrorAlarmWorkRecordService;
	
	
	//ドメインサービス：計算用ストアド実行用
	@Inject
	private AdTimeAndAnyItemAdUpService adTimeAndAnyItemAdUpService; 
	/*日別計算　マネージャークラス*/
	@Inject
	private CalculateDailyRecordServiceCenter calculateDailyRecordServiceCenter;
	
	/*〆状態*/
	@Inject
	private ClosureStatusManagementRepository closureStatusManagementRepository;
	
	@Inject
	private CommonCompanySettingForCalc commonCompanySettingForCalc;
	
	/** リポジトリ：対象者ログ */
	@Inject
	private TargetPersonRepository targetPersonRepository;
	
	@Inject
	private ManagedParallelWithContext parallel;
	
	@Inject
	private InterimRemainDataMngRegisterDateChange interimData;
	
	/**
	 * 社員の日別実績を計算
	 * @param asyncContext 同期コマンドコンテキスト
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param datePeriod 期間
	 * @param empCalAndSumExecLogID 就業計算と集計実行ログID
	 * @param executionType 実行種別　（通常、再実行）
	 */
	@SuppressWarnings("rawtypes")
	@Override
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void calculate(AsyncCommandHandlerContext asyncContext, List<String> employeeIds,DatePeriod datePeriod, Consumer<ProcessState> counter,ExecutionType reCalcAtr, String empCalAndSumExecLogID) {
		
		String cid = AppContexts.user().companyId();
		
		this.parallel.forEach(employeeIds, employeeId -> {
			
			// 中断処理　（中断依頼が出されているかチェックする）
			if (asyncContext.hasBeenRequestedToCancel()) {
				counter.accept(ProcessState.INTERRUPTION);
				return;
			}
			
			//日別実績(WORK取得)
			List<IntegrationOfDaily> createList = createIntegrationOfDaily(employeeId,datePeriod);
			
			//締め一覧取得
			List<ClosureStatusManagement> closureList = getClosureList(Arrays.asList(employeeId), datePeriod);
			
			ManageProcessAndCalcStateResult afterCalcRecord;
			if (createList.isEmpty()) {
				counter.accept(ProcessState.SUCCESS);
				
				//１：日別計算(ENUM)
				//0:計算完了
				targetPersonRepository.updateWithContent(employeeId, empCalAndSumExecLogID, 1, 0);
			} else {
				//計算処理を呼ぶ
				afterCalcRecord = calculateDailyRecordServiceCenter.calculateForManageState(createList, Optional.of(asyncContext),closureList,reCalcAtr);
				//１：日別計算(ENUM)
				//0:計算完了
				targetPersonRepository.updateWithContent(employeeId, empCalAndSumExecLogID, 1, 0);
				
				List<IntegrationOfDaily> result = afterCalcRecord.getLst().stream().map(tc -> tc.getIntegrationOfDaily()).collect(Collectors.toList());
				//データ更新
				for(IntegrationOfDaily value:result) {
					updateRecord(value);
				}
				//計算状態更新
				for(ManageCalcStateAndResult stateInfo : afterCalcRecord.getLst()) {
					upDateCalcState(stateInfo);
				}
				
				//暫定データ
				interimData.registerDateChange(cid , employeeId, datePeriod.datesBetween());
				//
				counter.accept(afterCalcRecord.getPs() == ProcessState.SUCCESS?ProcessState.SUCCESS:ProcessState.INTERRUPTION);
			}
		});
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void updateRecord(IntegrationOfDaily value) {
		// データ更新
		if(value.getAttendanceTimeOfDailyPerformance().isPresent()) {
			employeeDailyPerErrorRepository.removeParam(value.getAttendanceTimeOfDailyPerformance().get().getEmployeeId(), 
					value.getAttendanceTimeOfDailyPerformance().get().getYmd());
			this.registAttendanceTime(value.getAffiliationInfor().getEmployeeId(),value.getAffiliationInfor().getYmd(),
									  value.getAttendanceTimeOfDailyPerformance().get(),value.getAnyItemValue());
			determineErrorAlarmWorkRecordService.createEmployeeDailyPerError(value.getEmployeeError());
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void upDateCalcState(ManageCalcStateAndResult stateInfo) {
		stateInfo.getIntegrationOfDaily().getWorkInformation().changeCalcState(stateInfo.isCalc?CalculationState.Calculated:CalculationState.No_Calculated);
		workInformationRepository.updateByKeyFlush(stateInfo.getIntegrationOfDaily().getWorkInformation());
	}
	
	/**
	 * 全社員の〆状態リストの取得
	 * @param employeeId 社員ID一覧
	 * @param datePeriod　処理対象期間
	 * @return　〆状態リスト
	 */
	private List<ClosureStatusManagement> getClosureList(List<String> employeeId, DatePeriod datePeriod) {
		return closureStatusManagementRepository.getByIdListAndDatePeriod(employeeId, datePeriod);
	}

	@SuppressWarnings("rawtypes")
	public ProcessState calculateForOnePerson(AsyncCommandHandlerContext asyncContext, String employeeId,DatePeriod datePeriod,Optional<Consumer<ProcessState>> counter) {
		//実績取得
		List<IntegrationOfDaily> createList = createIntegrationList(Arrays.asList(employeeId),datePeriod);
		//実績が無かった時用のカウントアップ
		if(createList.isEmpty()) 
			return ProcessState.SUCCESS; 
		
		//締め一覧取得
		List<ClosureStatusManagement> closureList = getClosureList(Arrays.asList(employeeId),datePeriod);
		
		ManagePerCompanySet companySet =  commonCompanySettingForCalc.getCompanySetting(); 
		//計算処理
		val afterCalcRecord = calculateDailyRecordServiceCenter.calculateForclosure(createList,companySet ,closureList);
		
		
		for(ManageCalcStateAndResult value:afterCalcRecord.getLst()) {
			// データ更新
			//*****（未）　日別実績の勤怠情報だけを更新する場合。まとめて更新するなら、integrationOfDailyを入出できるよう調整する。
			if(value.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().isPresent()) {
				employeeDailyPerErrorRepository.removeParam(value.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().get().getEmployeeId(), 
						value.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().get().getYmd());
				this.registAttendanceTime(employeeId.toString(),value.getIntegrationOfDaily().getAffiliationInfor().getYmd(),
										  value.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().get(),value.getIntegrationOfDaily().getAnyItemValue());
				determineErrorAlarmWorkRecordService.createEmployeeDailyPerError(value.getIntegrationOfDaily().getEmployeeError());
			}
		}
		//計算状態更新
		for(ManageCalcStateAndResult stateInfo : afterCalcRecord.getLst()) {
			upDateCalcState(stateInfo);
		}
		return afterCalcRecord.getPs();
	}

	/**
	 * 実績データ取得
	 * @param employeeId
	 * @param datePeriod
	 * @return
	 */
	private List<IntegrationOfDaily> createIntegrationList(List<String> employeeId, DatePeriod datePeriod) {
		List<IntegrationOfDaily> returnList = new ArrayList<>();
		for(String empId:employeeId) {
			returnList.addAll(createIntegrationOfDaily(empId, datePeriod));
		}
		return returnList;
	}

	/**
	 * データ更新
	 * @param attendanceTime 日別実績の勤怠時間
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void registAttendanceTime(String empId,GeneralDate ymd,AttendanceTimeOfDailyPerformance attendanceTime, Optional<AnyItemValueOfDaily> anyItem){
		adTimeAndAnyItemAdUpService.addAndUpdate(empId,ymd,Optional.of(attendanceTime), anyItem);	
	}
	
	/**
	 * 日別実績(WORK)の作成
	 * @param employeeId
	 * @param datePeriod
	 * @return
	 */
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private List<IntegrationOfDaily> createIntegrationOfDaily(String employeeId, DatePeriod datePeriod) {
		val attendanceTimeList= workInformationRepository.findByPeriodOrderByYmd(employeeId, datePeriod);
		
		List<IntegrationOfDaily> returnList = new ArrayList<>();

		for(WorkInfoOfDailyPerformance attendanceTime : attendanceTimeList) {
			/** リポジトリ：日別実績の勤務情報 */
			val workInf = workInformationRepository.find(employeeId, attendanceTime.getYmd());  
			/** リポジトリ：日別実績.日別実績の計算区分 */
			val calAttr = calAttrOfDailyPerformanceRepository.find(employeeId, attendanceTime.getYmd());
			
			/** リポジトリ：日別実績の所属情報 */
			val affiInfo = affiliationInforOfDailyPerforRepository.findByKey(employeeId, attendanceTime.getYmd());

			/** リポジトリ：日別実績の勤務種別 */
			val businessType = workTypeOfDailyPerforRepository.findByKey(employeeId, attendanceTime.getYmd());
			if(!workInf.isPresent() || !affiInfo.isPresent() || !businessType.isPresent())//calAttr == null
				continue;
			returnList.add(
				new IntegrationOfDaily(
					workInf.get(),
					calAttr,
					affiInfo.get(),
					businessType,
					pcLogOnInfoOfDailyRepo.find(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績のPCログオン情報 */
					employeeDailyPerErrorRepository.findByPeriodOrderByYmd(employeeId, datePeriod),/** リポジトリ:社員の日別実績エラー一覧 */
					outingTimeOfDailyPerformanceRepository.findByEmployeeIdAndDate(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績の外出時間帯 */
					breakTimeOfDailyPerformanceRepository.findByKey(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績の休憩時間帯 */
					attendanceTimeRepository.find(employeeId, attendanceTime.getYmd()),
					attendanceTimeByWorkOfDailyRepository.find(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績の作業別勤怠時間 */
					timeLeavingOfDailyPerformanceRepository.findByKey(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績の出退勤 */
					shortTimeOfDailyPerformanceRepository.find(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績の短時間勤務時間帯 */
					specificDateAttrOfDailyPerforRepo.find(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績の特定日区分 */
					attendanceLeavingGateOfDailyRepo.find(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績の入退門 */
					anyItemValueOfDailyRepo.find(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績の任意項目 */
					editStateOfDailyPerformanceRepository.findByKey(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績のの編集状態 */
					temporaryTimeOfDailyPerformanceRepository.findByKey(employeeId, attendanceTime.getYmd()),/** リポジトリ：日別実績の臨時出退勤 */
					remarksRepository.getRemarks(employeeId, attendanceTime.getYmd())
					));
		}
		return returnList;
	}
}
