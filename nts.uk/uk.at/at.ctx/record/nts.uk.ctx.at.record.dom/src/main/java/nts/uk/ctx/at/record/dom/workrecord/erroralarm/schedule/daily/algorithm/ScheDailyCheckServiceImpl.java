package nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.adapter.workschedule.WorkScheduleWorkInforAdapter;
import nts.uk.ctx.at.record.dom.adapter.workschedule.WorkScheduleWorkInforImport;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.ErrorAlarmCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.WorkRecordExtractingCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.CheckedCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.CompareRange;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.CompareSingleValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.service.ErAlWorkRecordCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.daily.algorithm.AlarmMessageValues;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.CompareOperatorText;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConvertCompareTypeToText;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.mastercheck.algorithm.StatusOfEmployeeAdapterAl;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.mastercheck.algorithm.WorkPlaceHistImportAl;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.mastercheck.algorithm.WorkPlaceIdAndPeriodImportAl;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.CondContinuousTime;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.CondContinuousTimeZone;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.CondContinuousWrkType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.CondTime;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.DaiCheckItemType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.ExtraCondScheDayRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.ExtractionCondScheduleDay;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.FixedCheckSDailyItems;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.FixedExtracSDailyItemsRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.FixedExtractSDailyConRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.FixedExtractionSDailyCon;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.FixedExtractionSDailyItems;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.RangeToCheck;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.ScheDailyCheckService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.schedule.daily.TimeZoneTargetRange;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.ContinuousCount;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.AddOrSubAttendanceItem;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.CalCountForConsecutivePeriodChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.CompareValueRangeChecking;
import nts.uk.ctx.at.shared.dom.adapter.attendanceitemname.AttendanceItemNameAdapter;
import nts.uk.ctx.at.shared.dom.adapter.attendanceitemname.MonthlyAttendanceItemNameDto;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckInfor;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckType;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ExtractionAlarmPeriodDate;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ExtractionResultDetail;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ResultOfEachCondition;
import nts.uk.ctx.at.shared.dom.dailyattdcal.converter.DailyRecordShareFinder;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.worktime.AttendanceTimeOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class ScheDailyCheckServiceImpl implements ScheDailyCheckService {
	@Inject
	private ManagedParallelWithContext parallelManager;
	@Inject
	private AttendanceItemNameAdapter attendanceItemNameAdapter;
	@Inject
	private WorkTypeRepository workTypeRep;
	@Inject
	private WorkTimeSettingRepository workTimeRep;
	@Inject
	private WorkScheduleWorkInforAdapter workScheduleWorkInforAdapter;
	@Inject 
	private ExtraCondScheDayRepository extraCondScheDayRepository;
	@Inject
	private FixedExtractSDailyConRepository fixedCondScheDayReporisoty;
	@Inject
	private FixedExtracSDailyItemsRepository fixedExtracSDailyItemsRepository;
	@Inject
	private DailyRecordShareFinder dailyRecordShareFinder;
	@Inject 
	private CalCountForConsecutivePeriodChecking calcCountForConsecutivePeriodChecking;
	@Inject
	private CompareValueRangeChecking compareValueRangeChecking;
	@Inject
	private AddOrSubAttendanceItem addOrSubAttendanceItem;
	@Inject
	private ConvertCompareTypeToText convertComparaToText;
	
	@Override
	public void extractScheDailyCheck(String cid, List<String> lstSid, DatePeriod dPeriod, String errorDailyCheckId,
			String listOptionalItem, String listFixedItem,
			List<WorkPlaceHistImportAl> getWplByListSidAndPeriod, List<StatusOfEmployeeAdapterAl> lstStatusEmp,
			List<ResultOfEachCondition> lstResultCondition, List<AlarmListCheckInfor> lstCheckType,
			Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		// チェックする前にデータ準備
		ScheDailyPrepareData prepareData = prepareDataBeforeChecking(cid, lstSid, dPeriod, errorDailyCheckId, listOptionalItem, listFixedItem);
		
		parallelManager.forEach(CollectionUtil.partitionBySize(lstSid, 100), emps -> {

			synchronized (this) {
				if (shouldStop.get()) {
					return;
				}
			}
			
			// Input．List＜社員ID＞をループ
			for(String sid : lstSid) {
				// 任意抽出条件のアラーム値を作成する
				OutputCheckResult checkTab2 = extractAlarmConditionTab2(
						sid, 
						prepareData.getWorkScheduleWorkInfos(), 
						prepareData.getScheCondItems(), 
						lstStatusEmp, 
						dPeriod, 
						getWplByListSidAndPeriod,
						prepareData.getListWorkType(),
						prepareData.getListIntegrationDai());
				lstResultCondition.addAll(checkTab2.getLstResultCondition());
				lstCheckType.addAll(checkTab2.getLstCheckType());
				
				// 固定抽出条件のアラーム値を作成する
				OutputCheckResult checkTab3 = extractAlarmConditionTab3(
						sid,
						dPeriod,
						prepareData.getFixedScheCondItems(),
						prepareData.getFixedItems(),
						prepareData.getListWorkType(),
						prepareData.getListWorktime(),
						getWplByListSidAndPeriod,
						prepareData.getWorkScheduleWorkInfos(),
						lstStatusEmp,
						prepareData.getListIntegrationDai());
				lstResultCondition.addAll(checkTab3.getLstResultCondition());
				lstCheckType.addAll(checkTab3.getLstCheckType());
			}
			
			synchronized (this) {
				counter.accept(emps.size());
			}
		});
	}
	
	/**
	 * チェックする前にデータ準備
	 * @param cid
	 * @param lstSid
	 * @param dPeriod
	 * @param errorDailyCheckId
	 * @param extractConditionWorkRecord
	 * @param errorDailyCheckCd
	 * @return
	 */
	private ScheDailyPrepareData prepareDataBeforeChecking(String cid, List<String> lstSid, DatePeriod dPeriod, 
			String errorDailyCheckId, String listOptionalItem, String listFixedItem) {
		// スケジュール日次の勤怠項目を取得する
		int typeOfItem = 0; // スケジュール
		Map<Integer, String> attendanceItems = attendanceItemNameAdapter.getAttendanceItemNameAsMapName(typeOfItem);
		
		// <<Public>> 勤務種類をすべて取得する
		List<WorkType> listWorkType = workTypeRep.findByCompanyId(cid);
		
		// 社員ID(List)、期間を設定して勤務予定を取得する
		List<WorkScheduleWorkInforImport> workScheduleWorkInfos = workScheduleWorkInforAdapter.getBy(lstSid, dPeriod);
		
		// 会社で使用できる就業時間帯を全件を取得する
		List<WorkTimeSetting> listWorktime = workTimeRep.findByCompanyId(cid);
		
		// スケジュール日次の任意抽出条件のデータを取得する
		List<ExtractionCondScheduleDay> scheCondItems = new ArrayList<>();//extraCondScheDayRepository.getScheAnyCondDay("", cid, listOptionalItem, true);
		
		// スケジュール日次の固定抽出条件のデータを取得する
		List<FixedExtractionSDailyCon> fixedScheCondItems = new ArrayList<>();//fixedCondScheDayReporisoty.getScheFixCondDay("", cid, listFixedItem, true);
		
		// ドメインモデル「スケジュール日次の固有抽出項目」を取得
		List<FixedExtractionSDailyItems> fixedItems = new ArrayList<>();//fixedExtracSDailyItemsRepository.getScheFixItemDay("", cid);
		
		//社員と期間を条件に日別実績を取得する
		List<IntegrationOfDaily> listIntegrationDai = dailyRecordShareFinder.findByListEmployeeId(lstSid, dPeriod);
				
		return ScheDailyPrepareData.builder()
				.attendanceItems(attendanceItems)
				.listWorkType(listWorkType)
				.listWorktime(listWorktime)
				.workScheduleWorkInfos(workScheduleWorkInfos)
				.scheCondItems(scheCondItems)
				.fixedScheCondItems(fixedScheCondItems)
				.fixedItems(fixedItems)
				.listIntegrationDai(listIntegrationDai)
				.build();
	}
	
	/**
	 * Tab2: チェック条件
	 */
	private OutputCheckResult extractAlarmConditionTab2(
			String sid, 
			List<WorkScheduleWorkInforImport> lstWorkScheduleWorkInfos, 
			List<ExtractionCondScheduleDay> scheCondItems, 
			List<StatusOfEmployeeAdapterAl> lstStatusEmp, 
			DatePeriod dPeriod,
			List<WorkPlaceHistImportAl> getWplByListSidAndPeriod,
			List<WorkType> listWorkType,
			List<IntegrationOfDaily> listIntegrationDai) {
		OutputCheckResult result = new OutputCheckResult(new ArrayList<>(), new ArrayList<>());
		
		// Input．List＜スケジュール日次の任意抽出条件＞をループ
		for (ExtractionCondScheduleDay scheCondItem: scheCondItems) {
			// カウント＝0
			int count = 0;
			
			// Input．期間．開始日からループする
			List<GeneralDate> listDate = dPeriod.datesBetween();
			for(int day = 0; day < listDate.size(); day++) {
				GeneralDate exDate = listDate.get(day);
				// 社員の会社所属状況をチェック
				List<StatusOfEmployeeAdapterAl> statusOfEmp = lstStatusEmp.stream()
						.filter(x -> x.getEmployeeId().equals(sid) 
								&& !x.getListPeriod().stream()
									.filter(y -> y.start().beforeOrEquals(exDate) && y.end().afterOrEquals(exDate)).collect(Collectors.toList()).isEmpty())
						.collect(Collectors.toList());
				if (statusOfEmp.isEmpty()) {
					count = 0;
					continue;
				}
				
				List<IntegrationOfDaily> lstDaily = listIntegrationDai.stream()
						.filter(x -> x.getEmployeeId().equals(sid) && x.getYmd().equals(exDate))
						.collect(Collectors.toList());			
				IntegrationOfDaily integrationDaily = null;
				if(!lstDaily.isEmpty()) {
					integrationDaily = lstDaily.get(0);
				}
				
				// 勤務予定を探す
				Optional<WorkScheduleWorkInforImport> workScheduleWorks = lstWorkScheduleWorkInfos.stream()
						.filter(x -> x.getEmployeeId().equals(sid) && x.getYmd().equals(exDate)).findFirst();
				if (!workScheduleWorks.isPresent()) {
					count = 0;
					continue;
				}
				
				int continuousPeriod = 0;
				boolean applicableAtr = false;
				List<String> lstWorkTypeCode = new ArrayList<>();
				CheckedCondition checkedCondition = null;
				
				// 勤務種類でフィルタする
				// param = 勤務種類の条件 & 勤務種類コード
				if (DaiCheckItemType.CONTINUOUS_WORK == scheCondItem.getCheckItemType()) {
					CondContinuousWrkType condContinuousWrkType = (CondContinuousWrkType)scheCondItem.getScheduleCheckCond();
					lstWorkTypeCode = condContinuousWrkType.getWrkTypeCds();
					continuousPeriod = condContinuousWrkType.getPeriod().v();
					applicableAtr = checkContinuousWrkType(condContinuousWrkType, scheCondItem.getTargetWrkType(), listWorkType);
				}
				
				// ループ中のスケジュール日次の任意抽出条件．時間のチェック条件をチェック
				if (DaiCheckItemType.TIME == scheCondItem.getCheckItemType()) {
					CondTime condTime = (CondTime) scheCondItem.getScheduleCheckCond();
					checkedCondition = condTime.getCheckedCondition();
					lstWorkTypeCode = condTime.getWrkTypeCds();
					// 予定時間をチェック
					applicableAtr = checkTime(condTime.getCheckedCondition(), workScheduleWorks.get(), integrationDaily.getAttendanceTimeOfDailyPerformance());
				}
				
				if (DaiCheckItemType.CONTINUOUS_TIME == scheCondItem.getCheckItemType()) {
					CondContinuousTime condContinuousTime = (CondContinuousTime) scheCondItem.getScheduleCheckCond();
					checkedCondition = condContinuousTime.getCheckedCondition();
					lstWorkTypeCode = condContinuousTime.getWrkTypeCds();
				}
				
				// ループ中のスケジュール日次の任意抽出条件．連続時間帯の抽出条件をチェック
				if (DaiCheckItemType.CONTINUOUS_TIMEZONE == scheCondItem.getCheckItemType()) {
					CondContinuousTimeZone condContinuousTimeZone = (CondContinuousTimeZone)scheCondItem.getScheduleCheckCond();
					lstWorkTypeCode = condContinuousTimeZone.getWrkTypeCds();
					continuousPeriod = condContinuousTimeZone.getPeriod().v();
					// 勤務予定の就業時間帯があるかチェック
					applicableAtr = checkContinuousTimeZone(condContinuousTimeZone, scheCondItem.getTimeZoneTargetRange());
				}
				
				// スケジュール日次の任意抽出条件．チェック項目種類をチェック
				Optional<ContinuousCount> optContinuousCount = Optional.empty();
				if (DaiCheckItemType.TIME != scheCondItem.getCheckItemType()) {
					// 連続期間のカウントを計算
					//【Input】
					//　・カウント　＝　取得したカウント　（最初は↑でセットしたカウント）
					//　・連続期間　＝　ループ中のスケジュール日次の任意抽出条件．連続期間
					//　・エラー発生区分　＝　取得した該当区分
					//【Output】
					//　・カウント
					//　・Optional<連続カウント＞
					boolean errorAtr = true; //TODO i don't know
					optContinuousCount = calcCountForConsecutivePeriodChecking.getContinuousCount(count, continuousPeriod, errorAtr, exDate);
				}
				
				//・該当区分　＝＝　True　AND　ループ中のスケジュール日次の任意抽出条件．チェック項目種類　＝＝　「時間」
				//OR
				//・ループ中のスケジュール日次の任意抽出条件．チェック項目種類　！＝　「時間」　AND　Optional<連続カウント>　！＝　Empty
				if ((applicableAtr && DaiCheckItemType.TIME == scheCondItem.getCheckItemType())
						|| (DaiCheckItemType.TIME != scheCondItem.getCheckItemType() && optContinuousCount.isPresent())) {
					String alarmContent = createAlarmContent(
							scheCondItem.getCheckItemType(), 
							listWorkType, lstWorkTypeCode, 
							checkedCondition, 
							continuousPeriod, 
							optContinuousCount.isPresent() ? optContinuousCount.get().getConsecutiveYears() : 0);
					String checkValue = createCheckValue(
							scheCondItem.getCheckItemType(), 
							lstWorkTypeCode, 
							listWorkType, 
							optContinuousCount.isPresent() ? optContinuousCount.get().getConsecutiveYears() : 0);
					
					// スケジュール日次のアラーム抽出値を作成
					List<ResultOfEachCondition> listResultCond = this.createExtractAlarm(sid,
							exDate,
							scheCondItem.getName().v(),
							alarmContent,
							Optional.ofNullable(scheCondItem.getErrorAlarmMessage() != null && scheCondItem.getErrorAlarmMessage().isPresent() ? scheCondItem.getErrorAlarmMessage().get().v() : ""),
							checkValue,
							String.valueOf(scheCondItem.getSortOrder()),
							AlarmListCheckType.FreeCheck,
							getWplByListSidAndPeriod,
							scheCondItem.getCheckItemType(),
							optContinuousCount.isPresent() ? optContinuousCount.get().getConsecutiveYears() : 0);
					if (!listResultCond.isEmpty()) {
						result.getLstResultCondition().addAll(listResultCond);
					}
				}
			}
			
			// 各チェック条件の結果を作成
			// - use value object: 各チェック条件の結果
		}
		
		// 作成したList＜各チェック条件の結果＞を返す
		return result;
	}
	
	/**
	 * create extract alarm
	 */
	private List<ResultOfEachCondition> createExtractAlarm(String sid,
			GeneralDate day,
			String alarmName,
			String alarmContent,
			Optional<String> alarmMess,
			String checkValue,
			String alarmCode, AlarmListCheckType checkType,
			List<WorkPlaceHistImportAl> getWplByListSidAndPeriod,
			DaiCheckItemType dailyCheckType,
			int consecutiveYears) {
		List<ResultOfEachCondition> listResultCond = new ArrayList<>();
		// ・職場ID　＝　Input．List＜職場ID＞をループ中の年月日から探す
		String wplId = "";
		Optional<WorkPlaceHistImportAl> optWorkPlaceHistImportAl = getWplByListSidAndPeriod.stream().filter(x -> x.getEmployeeId().equals(sid)).findFirst();
		if(optWorkPlaceHistImportAl.isPresent()) {
			Optional<WorkPlaceIdAndPeriodImportAl> optWorkPlaceIdAndPeriodImportAl = optWorkPlaceHistImportAl.get().getLstWkpIdAndPeriod().stream()
					.filter(x -> x.getDatePeriod().start()
							.beforeOrEquals(day) 
							&& x.getDatePeriod().end()
							.afterOrEquals(day)).findFirst();
			if(optWorkPlaceIdAndPeriodImportAl.isPresent()) {
				wplId = optWorkPlaceIdAndPeriodImportAl.get().getWorkplaceId();
			}
		}
		
		// アラーム値日付 =
		// チェック項目種類　＝＝　「時間」　－＞ループ中の年月日
		ExtractionAlarmPeriodDate extractionAlarmPeriodDate = new ExtractionAlarmPeriodDate(Optional.of(day), Optional.empty());
		if (DaiCheckItemType.TIME != dailyCheckType) {
			// チェック項目種類　！＝　「時間」　－＞ループ中の年月日.ADD(-取得した連続カウント）
			//TODO addDays or Year
			extractionAlarmPeriodDate = new ExtractionAlarmPeriodDate(Optional.of(day.addDays(-consecutiveYears)), Optional.empty());
		}
		
		// 抽出結果詳細
		ExtractionResultDetail detail = new ExtractionResultDetail(sid, 
				extractionAlarmPeriodDate, 
				alarmName, 
				alarmContent, 
				GeneralDateTime.now(), 
				Optional.ofNullable(wplId), 
				alarmMess, 
				Optional.ofNullable(checkValue));
		List<ResultOfEachCondition> lstResultTmp = listResultCond.stream()
				.filter(x -> x.getCheckType().value == checkType.value && x.getNo().equals(alarmCode)).collect(Collectors.toList());
		List<ExtractionResultDetail> listDetail = new ArrayList<>();
		if(lstResultTmp.isEmpty()) {
			listDetail.add(detail);
			listResultCond.add(new ResultOfEachCondition(EnumAdaptor.valueOf(1, AlarmListCheckType.class), alarmCode, 
					listDetail));	
		} else {
			listResultCond.stream().forEach(x -> x.getLstResultDetail().add(detail));
		}
		
		return listResultCond;
	}
	
	/**
	 * create alarm content (アラーム内容)
	 * @return
	 */
	private String createAlarmContent(
			DaiCheckItemType dailyCheckType,
			List<WorkType> listWorkType,
			List<String> lstWorkTypeCode,
			CheckedCondition checkedCondition,
			int conPeriod,
			int consecutiveYears) {
		String content = "";
		CompareOperatorText compareOperatorText = null;
		int compare = 0;
		String startValue = "";
		String endValue = "";
		String consecutiveYearStr = String.valueOf(consecutiveYears);
		String conPeriodStr = String.valueOf(conPeriod);
		
		if (checkedCondition != null) {
			compare = checkedCondition instanceof CompareSingleValue 
					? ((CompareSingleValue) checkedCondition).getCompareOpertor().value
					: ((CompareRange) checkedCondition).getCompareOperator().value;
					
			compareOperatorText = convertComparaToText.convertCompareType(compare);
			
			startValue = checkedCondition instanceof CompareSingleValue 
							? ((CompareSingleValue) checkedCondition).getValue().toString()
							: ((CompareRange) checkedCondition).getStartValue().toString();
			endValue = checkedCondition instanceof CompareRange  ? ((CompareRange) checkedCondition).getEndValue().toString() : null;
		}
		
		// チェック項目種類　＝＝　「時間」　OR　　「連続時間」 #KAL010_1013　※1
		if (DaiCheckItemType.TIME == dailyCheckType || DaiCheckItemType.CONTINUOUS_TIMEZONE == dailyCheckType) {
			// ※1内容:  対象勤務：{0}条件：予約時間{1}{2}　実績：{3}
			// {0}: ループ中のスケジュール日次の任意抽出条件．勤務種類の条件．予実比較による絞り込み方法
			String variable0 = "";
			if(compare <= 5) {
				variable0 = compareOperatorText.getCompareLeft() + startValue;
			} else {
				if (compare > 5 && compare <= 7) {
					variable0 = startValue + compareOperatorText.getCompareLeft()
							+ compareOperatorText.getCompareright() + endValue;
				} else {
					variable0 = startValue + compareOperatorText.getCompareLeft()
							+ ", " + compareOperatorText.getCompareright() + endValue;
				}
			}
			
			// {1}: チェック条件　（例：　＜＞8：00）
			String variable1 = "";
			// {2}: 
				// チェック項目種類　＝＝　「時間」　－＞""
				// チェック項目種類　！＝　「時間」  －＞ #KAL010_1015　（{0}: ループ中のスケジュール日次の任意抽出条件．連続期間）
			String variable2 = TextResource.localize("KAL010_1013", conPeriodStr);
			// {3}:
				// チェック項目種類　＝＝　「時間」　－＞探した勤務予定．勤怠時間．勤務時間．総労働時間
			String variable3 = "";
			if (DaiCheckItemType.TIME == dailyCheckType) {
				for(int i = 0; i < lstWorkTypeCode.size(); i++) {
					String wtCode = lstWorkTypeCode.get(i);
					Optional<WorkType> optWt = listWorkType.stream().filter(x -> x.getWorkTypeCode().v().equals(wtCode))
							.findFirst();
					if(optWt.isPresent()) variable3 = variable3 + ", " + optWt.get().getName().v();
				}
			} else {
				// チェック項目種類　！＝　「時間」　－＞　取得した連続カウント　+　#KAL010_1017 
				variable3 = consecutiveYears + TextResource.localize("KAL010_1017");
			}
			// 例1：対象勤務：選択　条件：予約時間＜＞8：00　実績：10：00
			// 例２：対象勤務：選択　条件：予約時間＜＞8：00/5日連続　実績：10日連続
			content = TextResource.localize("KAL010_1013", variable0, variable1, variable2, variable3);
		} else { 
			// チェック項目種類　！＝　「時間」　OR　　「連続時間」－＞#KAL010_1016　※2
			// ※2内容：対象勤務：{0}/{1}日連続　実績：{2}日連続
			// {0}: ループ中のスケジュール日次の任意抽出条件．勤務種類の条件．予実比較による絞り込み方法
			String param0 = "";
			if(compare <= 5) {
				param0 = compareOperatorText.getCompareLeft() + startValue;
			} else {
				if (compare > 5 && compare <= 7) {
					param0 = startValue + compareOperatorText.getCompareLeft()
							+ compareOperatorText.getCompareright() + endValue;
				} else {
					param0 = startValue + compareOperatorText.getCompareLeft()
							+ ", " + compareOperatorText.getCompareright() + endValue;
				}
			}
			
			// {1}: ループ中のスケジュール日次の任意抽出条件．連続期間
			String param1 = conPeriodStr;
			// {2}:　取得した連続カウント
			String param2 = consecutiveYearStr;
			// 例：対象勤務：選択/5日連続　実績：6日連続
			content = TextResource.localize("KAL010_1016", param0, param1, param2);
		}
		
		return content;		
	}
	
	/**
	 * create check value (チェック対象値)
	 * @return
	 */
	private String createCheckValue(DaiCheckItemType dailyCheckType, List<String> lstWorkTypeCode, List<WorkType> listWorkType, int consecutiveYear) {
		String consecutiveYearStr = String.valueOf(consecutiveYear);
		// チェック対象値 = 
			// チェック項目種類　==　時間　－＞#KAL010_1025
		if (DaiCheckItemType.TIME == dailyCheckType) {
			// {0} = 探した勤務予定．勤怠時間．勤務時間．総労働時間
			String param = "";
			if(!lstWorkTypeCode.isEmpty()) {
				for(int i = 0; i < lstWorkTypeCode.size(); i++) {
					String wtCode = lstWorkTypeCode.get(i);
					Optional<WorkType> optWt = listWorkType.stream().filter(x -> x.getWorkTypeCode().v().equals(wtCode))
							.findFirst();
					if(optWt.isPresent()) param = param + "," + optWt.get().getName().v();
				}
			}
			
			return TextResource.localize("KAL010_1025", param);
		}
		
		// チェック項目種類　！=　時間　－＞#KAL010_1026
		//　{0} = 取得した連続カウント
		String param = consecutiveYearStr;
		return TextResource.localize("KAL010_1026", param);
	}
	
	/**
	 * 勤務種類でフィルタする
	 * TODO
	 * @return
	 */
	private boolean checkContinuousWrkType(CondContinuousWrkType condContinuousWrkType, RangeToCheck targetWrkType, List<WorkType> listWorkType) {
		// Input．勤務種類の条件．予実比較による絞り込み方法をチェック
		switch (targetWrkType) {
		case ALL:
			return true;
		case CHOICE:
			// 選択した勤務種類コードに存在するかないかをチェック
			Optional<WorkType> optWt = listWorkType.stream().filter(x -> condContinuousWrkType.getWrkTypeCds().contains(x.getWorkTypeCode())).findFirst();
			return optWt.isPresent();
		case OTHER:
			// 選択した勤務種類コードに存在するかないかをチェック
			Optional<WorkType> optWtOther = listWorkType.stream().filter(x -> condContinuousWrkType.getWrkTypeCds().contains(x.getWorkTypeCode())).findFirst();
			return !optWtOther.isPresent();
		default:
			break;
		}
		
		return false;
	}
	
	/**
	 * 予定時間をチェック
	 * Input 勤務予定, スケジュール日次の任意抽出条件
	 * @param scheCondDay
	 */
	private boolean checkTime(CheckedCondition checkedCondition, WorkScheduleWorkInforImport workScheduleWorkInfos, Optional<AttendanceTimeOfDailyAttendance> attendanceTimeOfDailyPerformance) {
		if (!attendanceTimeOfDailyPerformance.isPresent()) {
			return false;
		}
		
		AttendanceTimeOfDailyAttendance attendanceTimeOfDaily = attendanceTimeOfDailyPerformance.get();
		Double targetValue = getTargetValue(attendanceTimeOfDaily.getActualWorkingTimeOfDaily().getTotalWorkingTime().getActualTime().v());
		
		boolean isValid = true;
		
		// 集計値が条件に当てはまるかチェックする
		// 値を範囲と比較する
		if (checkedCondition instanceof CompareRange) {
			CompareRange compareRange = (CompareRange) checkedCondition;
			isValid = compareValueRangeChecking.checkCompareRange(compareRange, targetValue);
		}
		
		// 値を単一値と比較する
		if (checkedCondition instanceof CompareSingleValue) {
			CompareSingleValue compareSingleValue = (CompareSingleValue) checkedCondition;
			
			isValid = compareValueRangeChecking.checkCompareSingleRange(compareSingleValue, targetValue);
		}
		
		// 値を集計した値と比較する
		// TODO
		
		return isValid;
	}
	
	private Double getTargetValue(Integer target) {
		return Double.valueOf(target);
	}
	
	private boolean checkContinuousTimeZone(CondContinuousTimeZone condContinuousTimeZone, TimeZoneTargetRange timeZoneTargetRange) {
		// 勤務予定の就業時間帯があるかチェック
		if (condContinuousTimeZone.getWrkTimeCds().isEmpty()) {
			return false;
		}
		
		// 就業時間帯でフィルタする
		//【Input】
		//　・就業時間帯の条件　＝　ループ中のスケジュール日次の任意抽出条件．就業時間帯の条件
		//　・就業時間帯コード　＝　探した勤務予定．勤務予報．勤務情報．就業時間帯コード
		//【Output】
		//　・該当区分　（Default：False）
		return checkByWorkingTime(condContinuousTimeZone, timeZoneTargetRange);
	}
	
	/**
	 * 就業時間帯でフィルタする
	 * @param condContinuousTimeZone
	 */
	private boolean checkByWorkingTime(CondContinuousTimeZone condContinuousTimeZone, TimeZoneTargetRange timeZoneTargetRange) {
		if (condContinuousTimeZone.getWrkTimeCds().isEmpty()) {
			return false;
		}
		
		switch (timeZoneTargetRange) {
		case CHOICE:
			// Input．就業時間帯コードは対象の就業時間帯に存在するかチェック
			// 存在する
			if (true) {
				//TODO
				return true;
			}
			break;
		case OTHER:
			// Input．就業時間帯コードは対象の就業時間帯に存在するかチェック
			// 存在しない
			if (false) {
				return true;
			}
			break;
		default:
			break;
		}
		
		
		return false;
	}
	
	/**
	 * Tab3: スケジュール日次の固有抽出条件
	 */
	private OutputCheckResult extractAlarmConditionTab3(
			String sid,
			DatePeriod dPeriod,
			List<FixedExtractionSDailyCon> fixedScheCondItems,
			List<FixedExtractionSDailyItems> fixedItems,
			List<WorkType> listWorkType,
			List<WorkTimeSetting> listWorktime,
			List<WorkPlaceHistImportAl> getWplByListSidAndPeriod,
			List<WorkScheduleWorkInforImport> workScheduleWorkInfos,
			List<StatusOfEmployeeAdapterAl> lstStatusEmp,
			List<IntegrationOfDaily> listIntegrationDai) {
		String alarmMessage = new String();
		String alarmTarget = new String();
		
		List<GeneralDate> listDate = dPeriod.datesBetween();
		for(int day = 0; day < listDate.size(); day++) {
			GeneralDate exDate = listDate.get(day);
			
			// 社員の会社所属状況をチェック
			List<StatusOfEmployeeAdapterAl> statusOfEmp = lstStatusEmp.stream()
					.filter(x -> x.getEmployeeId().equals(sid) 
							&& !x.getListPeriod().stream()
								.filter(y -> y.start().beforeOrEquals(exDate) && y.end().afterOrEquals(exDate)).collect(Collectors.toList()).isEmpty())
					.collect(Collectors.toList());
			if(statusOfEmp.isEmpty()) continue;
			
			List<IntegrationOfDaily> lstDaily = listIntegrationDai.stream()
					.filter(x -> x.getEmployeeId().equals(sid) && x.getYmd().equals(exDate))
					.collect(Collectors.toList());			
			IntegrationOfDaily integrationDaily = null;
			if(!lstDaily.isEmpty()) {
				integrationDaily = lstDaily.get(0);
			}
			
			// 勤務予定(Work)を探す
			Optional<WorkScheduleWorkInforImport> workScheduleWorks = workScheduleWorkInfos.stream()
					.filter(x -> x.getEmployeeId().equals(sid) && x.getYmd().equals(exDate)).findFirst();
			if (!workScheduleWorks.isPresent()) {
				continue;
			}
			
			// Input．List＜スケジュール日次の固有抽出条件＞をループする
			for (FixedExtractionSDailyCon fixScheCondItem: fixedScheCondItems) {
				// ループ中のスケジュール日次の固有抽出条件をチェック
				FixedCheckSDailyItems fixedAtr = fixScheCondItem.getFixedCheckDayItems();
				
				switch (fixedAtr) {
				case SCHEDULE_CREATE_NOTCREATE:
					// NO1 = ：スケジュール未作成
					// Input．勤務予定が存在するかチェックする
					if (!workScheduleWorks.isPresent()) {
						// 取得できない場合
						//・アラーム表示値　＝　#KAL010_1004
						//・チェック対象値　＝　#KAL010_1018
						alarmMessage = TextResource.localize("KAL010_1004");
						alarmTarget = TextResource.localize("KAL010_1018");
					}
					break;
				case WORKTYPE_NOTREGISTED:
					// NO2 = ：勤務種類未登録
					// Input．日別勤怠の勤務情報が存在するかチェックする
					WorkTypeCode wkType = integrationDaily.getWorkInformation().getRecordInfo().getWorkTypeCode();
					Optional<WorkType> listWk = listWorkType.stream()
							.filter(x -> x.getWorkTypeCode().equals(wkType)).findFirst();
					if(!listWk.isPresent()) {
						alarmMessage = TextResource.localize("KAL010_7", wkType.v());
						alarmTarget = TextResource.localize("KAL010_1020", wkType.v());
					}
					break;
				case WORKTIME_NOTREGISTED:
					// NO3 = ：就業時間帯未登録
					WorkTimeCode wkTime = integrationDaily.getWorkInformation().getRecordInfo().getWorkTimeCode();
					if(wkTime == null) break;
					Optional<WorkTimeSetting> optWtime = listWorktime.stream()
							.filter(x -> x.getWorktimeCode().equals(wkTime)).findFirst();
					if(!optWtime.isPresent()) {
						alarmMessage = TextResource.localize("KAL010_9", wkTime.v());
						alarmTarget = TextResource.localize("KAL010_77", wkTime.v());
					}
					break;
				case OVERLAP_TIMEZONE:
					// NO4 = ：時間帯の重複
					// Input．List＜勤務予定＞から前日の勤務予定を探す
					// 条件： 年月日　＝　Input．年月日．Add（－１）
					workScheduleWorks = workScheduleWorkInfos.stream()
							.filter(x -> x.getYmd().equals(exDate.addDays(-1))).findFirst();
					if (!workScheduleWorks.isPresent()) {
						// 社員ID(List)、期間を設定して勤務予定を取得する
						// 【条件】
						//	・年月日　Between　Input．期間
						//	・社員ID　＝　Input．List＜社員ID＞
						workScheduleWorkInfos.stream().filter(x -> x.getYmd().equals(exDate));

					} else {
						// Input．List＜勤務予定＞から当日の勤務予定を探す
						// 条件： 年月日　＝　Input．年月日
						workScheduleWorks = workScheduleWorkInfos.stream().filter(x -> x.getYmd().equals(exDate)).findFirst();
						
					}
					break;
				case WORK_MULTI_TIME:
					
					break;
				case WORK_ON_SCHEDULEDAY:
					
					break;
				case SCHEDULE_UNDECIDED:
					
					break;
				default:
					break;
				}
				
				// 取得した「アラーム表示値」をチェック
			}
		}
		
		return null;
	}

}
