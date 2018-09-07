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
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck.CalculationErrorCheckService;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository;
import nts.uk.ctx.at.record.dom.divergencetime.service.MasterShareBus;
import nts.uk.ctx.at.record.dom.divergencetime.service.MasterShareBus.MasterShareContainer;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.optitem.PerformanceAtr;
import nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.DailyStatutoryWorkingHours;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrule.specific.SpecificWorkRuleRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.BPUnitUseSettingRepository;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository;
import nts.uk.ctx.at.shared.dom.ot.zerotime.ZeroTimeRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CalculateDailyRecordServiceCenterImpl implements CalculateDailyRecordServiceCenter{
	//休暇加算設定
	@Inject
	private HolidayAddtionRepository holidayAddtionRepository;
	//総拘束時間
	@Inject
	private SpecificWorkRuleRepository specificWorkRuleRepository;
	//会社ごとの代休設定
	@Inject
	private CompensLeaveComSetRepository compensLeaveComSetRepository;
	//乖離
	@Inject 
	private DivergenceTimeRepository divergenceTimeRepository;
	//エラーアラーム設定
	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository;
	
	//リポジトリ：労働条件
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;

	//リポジトリ；法定労働
	@Inject
	private DailyStatutoryWorkingHours dailyStatutoryWorkingHours;
	
	@Inject
	private CalculateDailyRecordService calculate;
	
	@Inject
	private CalculationErrorCheckService calculationErrorCheckService;
	
	//↓以下任意項目の計算の為に追加
	@Inject
	private OptionalItemRepository optionalItemRepository;
	
	@Inject
	private FormulaRepository formulaRepository;
	
	@Inject
	private EmpConditionRepository empConditionRepository;

	@Inject
	private BPUnitUseSettingRepository bPUnitUseSettingRepository; 
	
	@Inject
	private WorkInformationRepository workInformationRepository;
	
	@Inject
	private ZeroTimeRepository zeroTimeRepository;
	
	@Override
	//old_process. Don't use!
	public List<IntegrationOfDaily> calculate(List<IntegrationOfDaily> integrationOfDaily){
		return commonPerCompany(CalculateOption.asDefault(), integrationOfDaily,false,Optional.empty(),Optional.empty(),Optional.empty(),Collections.emptyList()).getIntegrationOfDailyList();
	}
	
	@Override
	//会社共通の設定を他のコンテキストで取得できる場合に呼び出す窓口
	public List<IntegrationOfDaily> calculatePassCompanySetting(
			CalculateOption calcOption,
			List<IntegrationOfDaily> integrationOfDaily,
			Optional<ManagePerCompanySet> companySet){
		return commonPerCompany(
				calcOption,
				integrationOfDaily,
				false,
				Optional.empty(),
				Optional.empty(),
				companySet,
				Collections.emptyList())
				.getIntegrationOfDailyList();
	}
	
	@Override
	//更新処理自動実行から呼び出す窓口
	public List<IntegrationOfDaily> calculateForclosure(List<IntegrationOfDaily> integrationOfDaily,Optional<ManagePerCompanySet> companySet,List<ClosureStatusManagement> closureList){
		return commonPerCompany(CalculateOption.asDefault(), integrationOfDaily,false,Optional.empty(),Optional.empty(),Optional.empty(),closureList).getIntegrationOfDailyList();
	}
	
	@Override
	//就業計算と集計から呼び出す時の窓口
	public CalcStatus calculateForManageState(List<IntegrationOfDaily> integrationOfDaily,Optional<AsyncCommandHandlerContext> asyncContext,Optional<Consumer<ProcessState>> counter,List<ClosureStatusManagement> closureList){
		return commonPerCompany(CalculateOption.asDefault(), integrationOfDaily,true,asyncContext,counter,Optional.empty(),closureList);
	}
	
	/**
	 * 会社共通の処理達
	 * @param integrationOfDaily 実績データたち
	 * @param companySet 
	 * @param closureList 
	 * @return 計算後実績データ
	 */
	private CalcStatus commonPerCompany(CalculateOption calcOption, List<IntegrationOfDaily> integrationOfDaily,boolean isManageState,
													  Optional<AsyncCommandHandlerContext> asyncContext
													 ,Optional<Consumer<ProcessState>> counter, 
													 Optional<ManagePerCompanySet> companySet, 
													 List<ClosureStatusManagement> closureList) {
		/***会社共通処理***/
		if(integrationOfDaily.isEmpty()) return new CalcStatus(ProcessState.SUCCESS, integrationOfDaily);
		//社員毎の実績に纏める
		Map<String,List<IntegrationOfDaily>> recordPerEmpId = getPerEmpIdRecord(integrationOfDaily);
		String comanyId = AppContexts.user().companyId();
		//会社共通の設定を
		MasterShareContainer shareContainer = MasterShareBus.open();
		
		ManagePerCompanySet companyCommonSetting = companySet.orElseGet(() -> {
			List<ErrorAlarmWorkRecord> errorAlermWorkRecords;
			if (calcOption.isSchedule()) {
				errorAlermWorkRecords = Collections.emptyList();
			} else {
				errorAlermWorkRecords = errorAlarmWorkRecordRepository.getAllErAlCompanyAndUseAtrV2(comanyId, true);
			}
		
			return new ManagePerCompanySet(
					holidayAddtionRepository.findByCompanyId(comanyId),
					holidayAddtionRepository.findByCId(comanyId),
					specificWorkRuleRepository.findCalcMethodByCid(comanyId),
					compensLeaveComSetRepository.find(comanyId),
					divergenceTimeRepository.getAllDivTime(comanyId),
					errorAlermWorkRecords,
					bPUnitUseSettingRepository.getSetting(comanyId),
					zeroTimeRepository.findByCId(comanyId));
		});
		
		companyCommonSetting.setShareContainer(shareContainer);
		
		/*----------------------------------任意項目の計算に必要なデータ取得-----------------------------------------------*/
		if (!calcOption.isSchedule()) {
			String companyId = AppContexts.user().companyId();
			//AggregateRoot「任意項目」取得
			List<OptionalItem> optionalItems = optionalItemRepository.findUsedByPerformanceAtr(companyId, PerformanceAtr.DAILY_PERFORMANCE);
			companyCommonSetting.setOptionalItems(optionalItems);
			//任意項目NOのlist作成
			List<Integer> optionalItemNoList = optionalItems.stream().map(oi -> oi.getOptionalItemNo().v()).collect(Collectors.toList());
			//計算式を取得
			companyCommonSetting.setFormulaList(formulaRepository.find(companyId));
			//適用する雇用条件の取得
			companyCommonSetting.setEmpCondition(empConditionRepository.findAll(companyId, optionalItemNoList));
		}
		/*----------------------------------任意項目の計算に必要なデータ取得-----------------------------------------------*/
		/***会社共通処理***/
		List<IntegrationOfDaily> returnList = new ArrayList<>();
		//社員ごとの処理
		for(Entry<String, List<IntegrationOfDaily>> record: recordPerEmpId.entrySet()) {
			//対象社員の締め取得
			List<ClosureStatusManagement> closureByEmpId = getclosure(record.getKey(), closureList);
			//日毎の処理
			val returnValue = calcOnePerson(calcOption, comanyId,record.getValue(),companyCommonSetting,asyncContext,closureByEmpId);
			// 中断処理　（中断依頼が出されているかチェックする）
			if (asyncContext.isPresent() 
				&& asyncContext.get().hasBeenRequestedToCancel()) {
				asyncContext.get().finishedAsCancelled();
				return new CalcStatus(ProcessState.INTERRUPTION,Collections.emptyList());
			}
			returnList.addAll(returnValue.getIntegrationOfDailyList());
			//人数カウントアップ
			if(counter.isPresent()) {
				counter.get().accept(ProcessState.SUCCESS);
			}
		}

		shareContainer.clearAll();
		shareContainer= null;
		return new CalcStatus(ProcessState.SUCCESS,returnList);
		
	}

	/**
	 * 対象者の締め一覧を取得する
	 * @param empId 対象者
	 * @param closureList 締め一覧
	 * @return 対象者の締め一覧
	 */
	private List<ClosureStatusManagement> getclosure(String empId,List<ClosureStatusManagement> closureList){
		return closureList.stream().filter(tc -> tc.getEmployeeId() == empId).collect(Collectors.toList());
	}
	
	/**
	 * 日毎のメイン処理
	 * @param comanyId 会社 ID
	 * @param recordList 実績データのリスト
	 * @param companyCommonSetting 会社共通の設定
	 * @param closureByEmpId 
	 * @return　実績データ
	 */
	private CalcStatus calcOnePerson(CalculateOption calcOption, String comanyId, List<IntegrationOfDaily> recordList, ManagePerCompanySet companyCommonSetting,
									Optional<AsyncCommandHandlerContext> asyncContext, List<ClosureStatusManagement> closureByEmpId){
		
		//社員の期間取得
		val integraListByRecordAndEmpId = getIntegrationOfDailyByEmpId(recordList);
		val map = convertMap(recordList);
		List<GeneralDate> sortedymd = recordList.stream()
								  			.sorted((first,second) -> first.getAffiliationInfor().getYmd().compareTo(second.getAffiliationInfor().getYmd()))
								  			.map(tc -> tc.getAffiliationInfor().getYmd())
								  			.collect(Collectors.toList());
		
		val maxGeneralDate = sortedymd.get(0);
		val minGeneralDate = sortedymd.get(sortedymd.size() - 1);
		
		
		//労働制マスタ取得
		val masterData = workingConditionItemRepository.getBySidAndPeriodOrderByStrDWithDatePeriod(integraListByRecordAndEmpId,maxGeneralDate,minGeneralDate);
		
		//日ごとループ(1人社員の)
		List<IntegrationOfDaily> returnList = new ArrayList<>();
		for(IntegrationOfDaily record:recordList) {
			// 中断処理　（中断依頼が出されているかチェックする）
			if (asyncContext.isPresent() 
				&& asyncContext.get().hasBeenRequestedToCancel()) {
				asyncContext.get().finishedAsCancelled();
				return new CalcStatus(ProcessState.INTERRUPTION,Collections.emptyList());
			}
			
			//締め一覧から、ymdが計算可能な日かを判定する
			if(!isCalc(closureByEmpId, record.getAffiliationInfor().getYmd())) {
				returnList.add(record);
				continue;
			}
			
			//nowIntegrationの労働制取得
			Optional<Entry<DateHistoryItem, WorkingConditionItem>> nowWorkingItem = masterData.getItemAtDateAndEmpId(record.getAffiliationInfor().getYmd(),record.getAffiliationInfor().getEmployeeId());
			if(nowWorkingItem.isPresent()) {
				companyCommonSetting.setPersonInfo(Optional.of(nowWorkingItem.get().getValue()));
				val dailyUnit = dailyStatutoryWorkingHours.getDailyUnit(comanyId,
																		record.getAffiliationInfor().getEmploymentCode().toString(),
																		record.getAffiliationInfor().getEmployeeId(),
																		record.getAffiliationInfor().getYmd(),
																		nowWorkingItem.get().getValue().getLaborSystem());
				if(dailyUnit == null) {
					returnList.add(record);
				}
				else {
					companyCommonSetting.setDailyUnit(dailyUnit);
					//実績計算
					returnList.add(calculate.calculate(calcOption, record, 
													   companyCommonSetting,
													   findAndGetWorkInfo(record.getAffiliationInfor().getEmployeeId(),map,record.getAffiliationInfor().getYmd().addDays(-1)),
													   findAndGetWorkInfo(record.getAffiliationInfor().getEmployeeId(),map,record.getAffiliationInfor().getYmd().addDays(1))));
				}
			}
			else {
				returnList.add(record);
			}
		}

		return new CalcStatus(ProcessState.SUCCESS,returnList);
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
		val companyCommonSetting = new ManagePerCompanySet(holidayAddtionRepository.findByCompanyId(AppContexts.user().companyId()),
														   holidayAddtionRepository.findByCId(AppContexts.user().companyId()),
														   specificWorkRuleRepository.findCalcMethodByCid(AppContexts.user().companyId()),
														   compensLeaveComSetRepository.find(AppContexts.user().companyId()),
														   divergenceTimeRepository.getAllDivTime(AppContexts.user().companyId()),
														   errorAlarmWorkRecordRepository.getListErrorAlarmWorkRecord(AppContexts.user().companyId()),
														   bPUnitUseSettingRepository.getSetting(AppContexts.user().companyId()),
														   zeroTimeRepository.findByCId(AppContexts.user().companyId()));

		//社員毎の期間取得
		val integraListByRecordAndEmpId = getIntegrationOfDailyByEmpId(integrationList);
		
		List<GeneralDate> sortedymd = integrationList.stream()
								  					 	.sorted((first,second) -> first.getAffiliationInfor().getYmd().compareTo(second.getAffiliationInfor().getYmd()))
								  					 	.map(tc -> tc.getAffiliationInfor().getYmd())
								  					 	.collect(Collectors.toList());
		
		val maxGeneralDate = sortedymd.get(0);
		val minGeneralDate = sortedymd.get(sortedymd.size() - 1);
		//労働制マスタ取得
		val masterData = workingConditionItemRepository.getBySidAndPeriodOrderByStrDWithDatePeriod(integraListByRecordAndEmpId,maxGeneralDate,minGeneralDate);
		List<IntegrationOfDaily> returnList = new ArrayList<>();
		for(IntegrationOfDaily integration : integrationList) {

			//nowIntegrationの労働制取得
			Optional<Entry<DateHistoryItem, WorkingConditionItem>> nowWorkingItem = masterData.getItemAtDateAndEmpId(integration.getAffiliationInfor().getYmd(),integration.getAffiliationInfor().getEmployeeId());
			if(nowWorkingItem.isPresent()) {
				companyCommonSetting.setPersonInfo(Optional.of(nowWorkingItem.get().getValue()));
				val dailyUnit = dailyStatutoryWorkingHours.getDailyUnit(AppContexts.user().companyId(),
																		integration.getAffiliationInfor().getEmploymentCode().toString(),
																		integration.getAffiliationInfor().getEmployeeId(),
																		integration.getAffiliationInfor().getYmd(),
																		nowWorkingItem.get().getValue().getLaborSystem());
				if(dailyUnit == null) {
					returnList.add(integration);
				}
				else {
					companyCommonSetting.setDailyUnit(dailyUnit);
					returnList.add(calculationErrorCheckService.errorCheck(integration, companyCommonSetting));
				}
			}
			else {
				returnList.add(integration);
			}
		}
		return returnList;
	}

}
