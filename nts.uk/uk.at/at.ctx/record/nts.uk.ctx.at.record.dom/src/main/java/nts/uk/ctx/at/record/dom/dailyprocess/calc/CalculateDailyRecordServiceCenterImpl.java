package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.personnelcostsetting.PersonnelCostSettingAdapter;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck.CalculationErrorCheckService;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.DailyStatutoryWorkingHours;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.attendance.MasterShareBus;
import nts.uk.ctx.at.shared.dom.attendance.MasterShareBus.MasterShareContainer;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.common.TimeOfDay;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CalculateDailyRecordServiceCenterImpl implements CalculateDailyRecordServiceCenter{
	
	//リポジトリ：労働条件
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;

	//リポジトリ；法定労働
	@Inject
	private DailyStatutoryWorkingHours dailyStatutoryWorkingHours;
	
	//計算処理
	@Inject
	private CalculateDailyRecordService calculate;
	
	//エラーチェック処理
	@Inject
	private CalculationErrorCheckService calculationErrorCheckService;
	
	//リポジトリ：勤務情報
	@Inject
	private WorkInformationRepository workInformationRepository;
	
	//割増計算用に追加
	@Inject
	private PersonnelCostSettingAdapter personnelCostSettingAdapter;
	
	//計算を動かすための会社共通設定取得
	@Inject
	private CommonCompanySettingForCalc commonCompanySettingForCalc;
	
	/*回数・時間・任意項目の勤怠項目IDを取得するためのサービス*/
	@Inject
	private AttendanceItemService attendanceItemService;
	
	/*日別実績の編集状態*/
	@Inject
	private EditStateOfDailyPerformanceRepository editStateOfDailyPerformanceRepository;
	
	@Inject
	private DailyCalculationEmployeeService dailyCalculationEmployeeService;
	
	@Override
	//old_process. Don't use!
	public List<IntegrationOfDaily> calculate(List<IntegrationOfDaily> integrationOfDaily){
//		return commonPerCompany(CalculateOption.asDefault(), integrationOfDaily,false,Optional.empty(),Optional.empty(),Optional.empty(),Collections.emptyList()).getIntegrationOfDailyList();
		return calculatePassCompanySetting(CalculateOption.asDefault(), integrationOfDaily, Optional.empty(), ExecutionType.NORMAL_EXECUTION);
	}
	
	@Override
	//会社共通の設定を他のコンテキストで取得できる場合に呼び出す窓口
	public List<IntegrationOfDaily> calculatePassCompanySetting(
			CalculateOption calcOption,
			List<IntegrationOfDaily> integrationOfDaily,
			Optional<ManagePerCompanySet> companySet,
			ExecutionType reCalcAtr){
		if(reCalcAtr.isRerun()) {
			val itemId = attendanceItemService.getTimeAndCountItem(AttendanceItemType.DAILY_ITEM).stream().map(tc -> tc.getItemId()).collect(Collectors.toList());
			List<EditStateOfDailyPerformance> notReCalcItems = new ArrayList<>();
			//時間・回数・任意項目だけ削除したリスト作成
			integrationOfDaily.forEach(tc -> {
				notReCalcItems.clear();
				tc.getEditState().forEach(ts ->{
					if(!itemId.contains(ts.getAttendanceItemId()))
						notReCalcItems.add(ts);
				});
				tc.setEditState(notReCalcItems);
			});
		}
		val result = commonPerCompany(
									  calcOption,
									  integrationOfDaily,
									  false,
									  Optional.empty(),
									  Optional.empty(),
									  companySet,
									  Collections.emptyList()).getLst();
		return result.stream().map(ts -> ts.getIntegrationOfDaily()).collect(Collectors.toList()); 
	}
	
	@Override
	//スケジュール・申請から呼び出す窓口
	public List<IntegrationOfDaily> calculateForSchedule(
			CalculateOption calcOption,
			List<IntegrationOfDaily> integrationOfDaily,
			Optional<ManagePerCompanySet> companySet){
		return commonPerCompany(
				calcOption,
				integrationOfDaily,
				true,
				Optional.empty(),
				Optional.empty(),
				companySet,
				Collections.emptyList())
				.getLst().stream().map(tc -> tc.getIntegrationOfDaily()).collect(Collectors.toList());
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	//就業計算と集計から呼び出す時の窓口
	public ManageProcessAndCalcStateResult calculateForManageState(
			List<IntegrationOfDaily> integrationOfDaily,
			Optional<AsyncCommandHandlerContext> asyncContext,
			List<ClosureStatusManagement> closureList,
			ExecutionType reCalcAtr){
		if(reCalcAtr.isRerun()) {
			//時間・回数の勤怠項目だけ　integrationの編集状態から削除
			val itemId = attendanceItemService.getTimeAndCountItem(AttendanceItemType.DAILY_ITEM).stream().map(tc -> tc.getItemId()).collect(Collectors.toList());
			List<EditStateOfDailyPerformance> notReCalcItems = new ArrayList<>();
			//時間・回数・任意項目だけ削除したリスト作成
			integrationOfDaily.forEach(tc -> {
				notReCalcItems.clear();
				tc.getEditState().forEach(ts ->{
					if(!itemId.contains(ts.getAttendanceItemId()))
						notReCalcItems.add(ts);
				});
				tc.setEditState(notReCalcItems);
				//時間・回数の勤怠項目だけ　編集状態テーブルから削除
				editStateOfDailyPerformanceRepository.deleteByListItemId(tc.getAffiliationInfor().getEmployeeId(),
																		 tc.getAffiliationInfor().getYmd(), 
																		 itemId);
			});

		}
		return commonPerCompany(CalculateOption.asDefault(), integrationOfDaily,true,asyncContext,Optional.empty(),Optional.empty(),closureList);
	}


	@Override
	//更新処理自動実行から呼び出す窓口
	public ManageProcessAndCalcStateResult calculateForclosure(
			List<IntegrationOfDaily> integrationOfDaily,
			ManagePerCompanySet companySet,
			List<ClosureStatusManagement> closureList) {
		val result = commonPerCompany(CalculateOption.asDefault(), integrationOfDaily,true,Optional.empty(),Optional.empty(),Optional.empty(),closureList);
		result.getLst().forEach(listItem ->{
			dailyCalculationEmployeeService.upDateCalcState(listItem);
		});
		return result; 
	}
	
	
	/**
	 * 会社共通の処理達
	 * @param integrationOfDaily 実績データたち
	 * @param companySet 
	 * @param closureList 
	 * @return 計算後実績データ
	 */
	@SuppressWarnings("rawtypes")
	private ManageProcessAndCalcStateResult commonPerCompany(CalculateOption calcOption, List<IntegrationOfDaily> integrationOfDaily,boolean isManageState,
													  Optional<AsyncCommandHandlerContext> asyncContext
													 ,Optional<Consumer<ProcessState>> counter, 
													 Optional<ManagePerCompanySet> companySet, 
													 List<ClosureStatusManagement> closureList) {
		/***会社共通処理***/
		if(integrationOfDaily.isEmpty()) return new ManageProcessAndCalcStateResult(ProcessState.SUCCESS, integrationOfDaily.stream().map(tc -> ManageCalcStateAndResult.failCalc(tc)).collect(Collectors.toList()));
		//社員毎の実績に纏める
		Map<String,List<IntegrationOfDaily>> recordPerEmpId = getPerEmpIdRecord(integrationOfDaily);
		String comanyId = AppContexts.user().companyId();
		//会社共通の設定を
		ManagePerCompanySet companyCommonSetting = companySet.orElseGet(() -> {
			return commonCompanySettingForCalc.getCompanySetting();
		});
		
		boolean mustCleanShareContainer = false;
		if (companyCommonSetting.getShareContainer() == null) {
			mustCleanShareContainer = true;
			MasterShareContainer<String> shareContainer = MasterShareBus.open();
			companyCommonSetting.setShareContainer(shareContainer);
		}
		
		companyCommonSetting.setPersonnelCostSettings(personnelCostSettingAdapter.findAll(comanyId, getDateSpan(integrationOfDaily)));
		

		/***会社共通処理***/
		List<ManageCalcStateAndResult> returnList = new ArrayList<>();
		//社員ごとの処理
		for(Entry<String, List<IntegrationOfDaily>> record: recordPerEmpId.entrySet()) {
			//対象社員の締め取得
			List<ClosureStatusManagement> closureByEmpId = getclosure(record.getKey(), closureList);
			//日毎の処理
			val returnValue = calcOnePerson(calcOption, comanyId,record.getValue(),companyCommonSetting,asyncContext,closureByEmpId);
			// 中断処理　（中断依頼が出されているかチェックする）
			if (asyncContext.isPresent() 
				&& asyncContext.get().hasBeenRequestedToCancel()) {
				//asyncContext.get().finishedAsCancelled();
				return new ManageProcessAndCalcStateResult(ProcessState.INTERRUPTION,Collections.emptyList());
			}
			returnList.addAll(returnValue);
			//人数カウントアップ
//			if(counter.isPresent()) {
//				counter.get().accept(ProcessState.SUCCESS);
//			}
		}

		if (mustCleanShareContainer) {
			companyCommonSetting.getShareContainer().clearAll();
			companyCommonSetting.setShareContainer(null);
		}
		
		return new ManageProcessAndCalcStateResult(ProcessState.SUCCESS,returnList);
		
	}
	

	/**
	 * 対象者の締め一覧を取得する
	 * @param empId 対象者
	 * @param closureList 締め一覧
	 * @return 対象者の締め一覧
	 */
	private List<ClosureStatusManagement> getclosure(String empId,List<ClosureStatusManagement> closureList){
		return closureList.stream().filter(tc -> tc.getEmployeeId().equals(empId)).collect(Collectors.toList());
	}
	
	/**
	 * 日毎のメイン処理
	 * @param comanyId 会社 ID
	 * @param recordList 実績データのリスト
	 * @param companyCommonSetting 会社共通の設定
	 * @param closureByEmpId 
	 * @return　実績データ
	 */
	@SuppressWarnings("rawtypes")
	private List<ManageCalcStateAndResult> calcOnePerson(CalculateOption calcOption, String comanyId, List<IntegrationOfDaily> recordList, ManagePerCompanySet companyCommonSetting,
									Optional<AsyncCommandHandlerContext> asyncContext, List<ClosureStatusManagement> closureByEmpId){
		
		//社員の期間取得
		val integraListByRecordAndEmpId = getIntegrationOfDailyByEmpId(recordList);
		val map = convertMap(recordList);
		List<GeneralDate> sortedymd = recordList.stream()
								  			.sorted((first,second) -> first.getAffiliationInfor().getYmd().compareTo(second.getAffiliationInfor().getYmd()))
								  			.map(tc -> tc.getAffiliationInfor().getYmd())
								  			.collect(Collectors.toList());
		//開始日
		val minGeneralDate = sortedymd.get(0);
		//終了日
		val maxGeneralDate = sortedymd.get(sortedymd.size() - 1);
		
		
		//労働制マスタ取得
		val masterData = workingConditionItemRepository.getBySidAndPeriodOrderByStrDWithDatePeriod(integraListByRecordAndEmpId,maxGeneralDate,minGeneralDate);

		//日ごとループ(1人社員の)
		List<ManageCalcStateAndResult> returnList = new ArrayList<>();
		for(IntegrationOfDaily record:recordList) {
			// 中断処理　（中断依頼が出されているかチェックする）
			if (asyncContext.isPresent() 
				&& asyncContext.get().hasBeenRequestedToCancel()) {
				//asyncContext.get().finishedAsCancelled();
				return Collections.emptyList();
			}
			
			//締め一覧から、ymdが計算可能な日かを判定する
			if(!isCalc(closureByEmpId, record.getAffiliationInfor().getYmd())) {
				returnList.add(ManageCalcStateAndResult.successCalc(record));
				continue;
			}
			
			//nowIntegrationの労働制取得
			Optional<Entry<DateHistoryItem, WorkingConditionItem>> nowWorkingItem = masterData.getItemAtDateAndEmpId(record.getAffiliationInfor().getYmd(),record.getAffiliationInfor().getEmployeeId());
			if(nowWorkingItem.isPresent()) {
				DailyUnit dailyUnit = dailyStatutoryWorkingHours.getDailyUnit(comanyId,
																		record.getAffiliationInfor().getEmploymentCode().toString(),
																		record.getAffiliationInfor().getEmployeeId(),
																		record.getAffiliationInfor().getYmd(),
																		nowWorkingItem.get().getValue().getLaborSystem(),
																		companyCommonSetting.getUsageSetting());
				if(dailyUnit == null || dailyUnit.getDailyTime() == null)
					dailyUnit = new DailyUnit(new TimeOfDay(0));
				//実績計算
				ManageCalcStateAndResult result = calculate.calculate(calcOption, record, 
													   companyCommonSetting,
													   new ManagePerPersonDailySet(Optional.of(nowWorkingItem.get().getValue()), dailyUnit),
													   findAndGetWorkInfo(record.getAffiliationInfor().getEmployeeId(),map,record.getAffiliationInfor().getYmd().addDays(-1)),
													   findAndGetWorkInfo(record.getAffiliationInfor().getEmployeeId(),map,record.getAffiliationInfor().getYmd().addDays(1)));
				if(result.isCalc()) {
					result.getIntegrationOfDaily().getWorkInformation().changeCalcState(CalculationState.Calculated);
				}
				else {
					result.getIntegrationOfDaily().getWorkInformation().changeCalcState(CalculationState.No_Calculated);
				}
				returnList.add(result);
			}
			else {
				returnList.add(ManageCalcStateAndResult.successCalc(record));
			}
		}
		return returnList;
	}
	
	
	/**
	 * 計算可能な日かを判定する
	 * @param closureList　締め一覧
	 * @param ymd　対象年月日
	 * @return　計算してもよい
	 */
	public boolean isCalc(List<ClosureStatusManagement> closureList, GeneralDate ymd) {
		for(ClosureStatusManagement closure : closureList) {
			if(closure.getPeriod().contains(ymd))
				return false;
		}
		return true;
	}
	

	//社員毎の実績にまとめる
	private Map<String, List<IntegrationOfDaily>> getPerEmpIdRecord(List<IntegrationOfDaily> integrationOfDaily) {
		List<String> empIdList = integrationOfDaily.stream().map(tc -> tc.getAffiliationInfor().getEmployeeId()).distinct().collect(Collectors.toList());
		Map<String, List<IntegrationOfDaily>> returnMap = new HashMap<>();
		for(String empId : empIdList) {
			val integrations = integrationOfDaily.stream().filter(tc -> tc.getAffiliationInfor().getEmployeeId().equals(empId)).collect(Collectors.toList());
			returnMap.put(empId, integrations);
		}
		return returnMap;
	}
	
	
	/**
	 * List→Mapへの変換クラス
	 * @param integrationOfDaily 日別実績(Work)
	 * @return integrationOfDailyのMap
	 */
	private Map<GeneralDate, IntegrationOfDaily> convertMap(List<IntegrationOfDaily> integrationOfDaily) {
		Map<GeneralDate, IntegrationOfDaily> map = new HashMap<>();
		integrationOfDaily.forEach(tc ->{
			map.put(tc.getAffiliationInfor().getYmd(), tc);
		});
		return map;
	}
	
	/**
	 * 前日翌日(parameterによってどっちにするか決める)の勤務情報を取得する
	 * @param empId
	 * @param mapIntegration
	 * @param addDays　取得したい日(-1or+1した日を渡す)
	 * @return addDaysの勤務情報
	 */
	private Optional<WorkInfoOfDailyPerformance> findAndGetWorkInfo(String empId, Map<GeneralDate, IntegrationOfDaily> mapIntegration, GeneralDate addDays) {
		if(mapIntegration.containsKey(addDays)) {
			return Optional.of(mapIntegration.get(addDays).getWorkInformation());
		}
		else {
			return workInformationRepository.find(empId, addDays);
		}
	}
	
	/**
	 * 日別実績(WORK)Listから社員毎の計算したい期間を取得する 
	 * @param integrationOfDaily
	 * @return　<社員、計算したい期間
	 */
	private Map<String,DatePeriod> getIntegrationOfDailyByEmpId(List<IntegrationOfDaily> integrationOfDaily) {
		Map<String,DatePeriod> returnMap = new HashMap<>();
		//しゃいんID 一覧取得
		List<String> idList = getAllEmpId(integrationOfDaily);
		idList.forEach(ts -> {
			//特定の社員IDに一致しているintegrationに絞る
			val integrationOfDailys = integrationOfDaily.stream().filter(tc -> tc.getAffiliationInfor().getEmployeeId().equals(ts)).collect(Collectors.toList());
			//特定社員の開始～終了を取得する
			val createdDatePriod = getDateSpan(integrationOfDailys);
			//Map<特定の社員ID、開始～終了>に追加する
			returnMap.put(ts, createdDatePriod);
		});
		return returnMap;
	}
	
	/*
	 * 社員の一覧取得
	 */
	private List<String> getAllEmpId(List<IntegrationOfDaily> integrationOfDaily){
		return integrationOfDaily.stream().distinct().map(tc->tc.getAffiliationInfor().getEmployeeId()).collect(Collectors.toList());
	}
	
	/**
	 * 開始～終了の作成
	 * @param integrationOfDaily 日別実績(WORK)LIST
	 * @return 開始～終了
	 */
	private DatePeriod getDateSpan(List<IntegrationOfDaily> integrationOfDaily) {
		val sortedIntegration = integrationOfDaily.stream().sorted((first,second) -> first.getAffiliationInfor().getYmd().compareTo(second.getAffiliationInfor().getYmd())).collect(Collectors.toList());
		return new DatePeriod(sortedIntegration.get(0).getAffiliationInfor().getYmd(), sortedIntegration.get(sortedIntegration.size() - 1).getAffiliationInfor().getYmd());
	}
	
	/**
	 * エラーチェック(外部から呼ぶ用)
	 * @param integrationList
	 * @return
	 */
	@Override
	public List<IntegrationOfDaily> errorCheck(List<IntegrationOfDaily> integrationList){
		if(integrationList.isEmpty()) return integrationList;
		//会社共通の設定を
		val companyCommonSetting = commonCompanySettingForCalc.getCompanySetting();

		//社員毎の期間取得
		val integraListByRecordAndEmpId = getIntegrationOfDailyByEmpId(integrationList);
		
		List<GeneralDate> sortedymd = integrationList.stream()
								  					 	.sorted((first,second) -> first.getAffiliationInfor().getYmd().compareTo(second.getAffiliationInfor().getYmd()))
								  					 	.map(tc -> tc.getAffiliationInfor().getYmd())
								  					 	.collect(Collectors.toList());
		
		val minGeneralDate = sortedymd.get(0);
		val maxGeneralDate = sortedymd.get(sortedymd.size() - 1);
		
		//労働制マスタ取得
		val masterData = workingConditionItemRepository.getBySidAndPeriodOrderByStrDWithDatePeriod(integraListByRecordAndEmpId,maxGeneralDate,minGeneralDate);
		List<IntegrationOfDaily> returnList = new ArrayList<>();
		for(IntegrationOfDaily integration : integrationList) {

			//nowIntegrationの労働制取得
			Optional<Entry<DateHistoryItem, WorkingConditionItem>> nowWorkingItem = masterData.getItemAtDateAndEmpId(integration.getAffiliationInfor().getYmd(),integration.getAffiliationInfor().getEmployeeId());
			if(nowWorkingItem.isPresent()) {
				DailyUnit dailyUnit = dailyStatutoryWorkingHours.getDailyUnit(AppContexts.user().companyId(),
																		integration.getAffiliationInfor().getEmploymentCode().toString(),
																		integration.getAffiliationInfor().getEmployeeId(),
																		integration.getAffiliationInfor().getYmd(),
																		nowWorkingItem.get().getValue().getLaborSystem());
				if(dailyUnit == null || dailyUnit.getDailyTime() == null)
					dailyUnit = new DailyUnit(new TimeOfDay(0));
				else {
					returnList.add(calculationErrorCheckService.errorCheck(integration, 
																		   new ManagePerPersonDailySet(Optional.of(nowWorkingItem.get().getValue()), dailyUnit),
																		   companyCommonSetting));
				}
			}
			else {
				returnList.add(integration);
			}
		}
		return returnList;
	}


}
