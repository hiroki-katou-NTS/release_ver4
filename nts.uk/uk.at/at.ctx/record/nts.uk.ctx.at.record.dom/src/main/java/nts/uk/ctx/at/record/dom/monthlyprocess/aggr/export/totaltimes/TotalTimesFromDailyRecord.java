package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.totaltimes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemConvertFactory;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.attdstatus.AttendanceStatusList;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.workinfo.WorkInfoList;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimes;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesResult;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.UseAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 日別実績から回数集計結果を取得する
 * @author shuichi_ishida
 */
public class TotalTimesFromDailyRecord {

	/** 会社ID */
	private String companyId;
	/** 出勤状態リスト */
	private AttendanceStatusList attendanceStatusList;
	/** 勤務情報リスト */
	private WorkInfoList workInfoList;
	/** 日別実績の勤怠時間リスト */
	private Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyMap;
	/** 日別実績の任意項目リスト */
	private Map<GeneralDate, AnyItemValueOfDaily> anyItemValueOfDailyMap;
	/** 日別実績の勤務種別リスト */
	private Map<GeneralDate, WorkTypeOfDailyPerformance> workTypeOfDailyMap;
	/** 勤務種類リスト */
	private Map<String, WorkType> workTypeMap;
	/** 勤務種類リポジトリ */
	private WorkTypeRepository workTypeRepo;
	/** 任意項目リスト */
	private Map<Integer, OptionalItem> optionalItemMap;

	/**
	 * コンストラクタ
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param dailysPeriod 日別実績の期間
	 * @param attendanceTimeOfDailyRepo 日別実績の勤怠時間リポジトリ
	 * @param anyItemValueOfDailyRepo 日別実績の任意項目リポジトリ
	 * @param timeLeavingOfDailyRepo 日別実績の出退勤リポジトリ
	 * @param workInfoOfDailyRepo 日別実績の勤務情報リポジトリ
	 * @param workTypeOfDailyRepo 日別実績の勤務種別リポジトリ
	 * @param workTypeRepo 勤務種類リポジトリ
	 * @param optionalItemRepo 任意項目リポジトリ
	 */
	public TotalTimesFromDailyRecord(
			String companyId,
			String employeeId,
			DatePeriod dailysPeriod,
			AttendanceTimeRepository attendanceTimeOfDailyRepo,
			AnyItemValueOfDailyRepo anyItemValueOfDailyRepo,
			TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyRepo,
			WorkInformationRepository workInfoOfDailyRepo,
			WorkTypeOfDailyPerforRepository workTypeOfDailyRepo,
			WorkTypeRepository workTypeRepo,
			OptionalItemRepository optionalItemRepo){

		this.companyId = companyId;
		this.attendanceStatusList = new AttendanceStatusList(
				employeeId, dailysPeriod, attendanceTimeOfDailyRepo, timeLeavingOfDailyRepo);
		this.workInfoList = new WorkInfoList(employeeId, dailysPeriod, workInfoOfDailyRepo);
		this.workTypeOfDailyMap = new HashMap<>();
		this.workTypeMap = new HashMap<>();
		this.optionalItemMap = new HashMap<>();
		val optionalItemList = optionalItemRepo.findAll(companyId);
		for (val optionalItem : optionalItemList){
			this.optionalItemMap.putIfAbsent(optionalItem.getOptionalItemNo().v(), optionalItem);
		}
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		this.setData(
				attendanceTimeOfDailyRepo.findByPeriodOrderByYmd(employeeId, dailysPeriod),
				anyItemValueOfDailyRepo.finds(Arrays.asList(employeeId), dailysPeriod),
				workTypeOfDailyRepo.finds(employeeIds, dailysPeriod),
				workTypeRepo);
	}

	/**
	 * コンストラクタ
	 * @param companyId 会社ID
	 * @param dailysPeriod 日別実績の期間
	 * @param attendanceTimeOfDailys 日別実績の勤怠時間リスト
	 * @param anyItemValueOfDailyList 日別実績の任意項目リスト
	 * @param timeLeavingOfDailys 日別実績の出退勤リスト
	 * @param workInfoOfDailys 日別実績の勤務情報リスト
	 * @param workTypeOfDailys 日別実績の勤務種別リスト
	 * @param workTypeMap 勤務種類リスト
	 * @param workTypeRepo 勤務種類リポジトリ
	 * @param optionalItemMap 任意項目リスト
	 */
	public TotalTimesFromDailyRecord(
			String companyId,
			DatePeriod dailysPeriod,
			List<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailys,
			List<AnyItemValueOfDaily> anyItemValueOfDailyList,
			List<TimeLeavingOfDailyPerformance> timeLeavingOfDailys,
			List<WorkInfoOfDailyPerformance> workInfoOfDailys,
			List<WorkTypeOfDailyPerformance> workTypeOfDailys,
			Map<String, WorkType> workTypeMap,
			WorkTypeRepository workTypeRepo,
			Map<Integer, OptionalItem> optionalItemMap){
		
		this.companyId = companyId;
		this.attendanceStatusList = new AttendanceStatusList(attendanceTimeOfDailys, timeLeavingOfDailys);
		this.workInfoList = new WorkInfoList(workInfoOfDailys);
		this.workTypeMap = workTypeMap;
		this.optionalItemMap = optionalItemMap;
		this.setData(attendanceTimeOfDailys, anyItemValueOfDailyList, workTypeOfDailys, workTypeRepo);
	}
	
	/**
	 * データ設定
	 * @param attendanceTimeOfDailys 日別実績の勤怠時間リスト
	 * @param anyItemValueOfDailyList 日別実績の任意項目リスト
	 * @param workTypeOfDailyList 日別実績の勤務種別リスト
	 * @param workTypeRepo 勤務種類リポジトリ
	 */
	private void setData(
			List<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailys,
			List<AnyItemValueOfDaily> anyItemValueOfDailyList,
			List<WorkTypeOfDailyPerformance> workTypeOfDailyList,
			WorkTypeRepository workTypeRepo){
		
		this.attendanceTimeOfDailyMap = new HashMap<>();
		for (val attendanceTimeOfDaily : attendanceTimeOfDailys){
			val ymd = attendanceTimeOfDaily.getYmd();
			this.attendanceTimeOfDailyMap.putIfAbsent(ymd, attendanceTimeOfDaily);
		}
		this.anyItemValueOfDailyMap = new HashMap<>();
		for (val anyItemValueOfDaily : anyItemValueOfDailyList){
			val ymd = anyItemValueOfDaily.getYmd();
			this.anyItemValueOfDailyMap.putIfAbsent(ymd, anyItemValueOfDaily);
		}
		this.workTypeOfDailyMap = new HashMap<>();
		for (val workTypeOfDaily : workTypeOfDailyList) {
			val ymd = workTypeOfDaily.getDate();
			this.workTypeOfDailyMap.putIfAbsent(ymd, workTypeOfDaily);
		}
		this.workTypeRepo = workTypeRepo;
	}

	/**
	 * 勤務種類の取得
	 * @param workTypeCode 勤務種類コード
	 * @return 勤務種類
	 */
	private WorkType getWorkType(String workTypeCode){
		
		if (this.workTypeMap.containsKey(workTypeCode)) return this.workTypeMap.get(workTypeCode);
		
		val workTypeOpt = this.workTypeRepo.findByPK(this.companyId, workTypeCode);
		if (workTypeOpt.isPresent()){
			this.workTypeMap.put(workTypeCode, workTypeOpt.get());
		}
		else {
			this.workTypeMap.put(workTypeCode, null);
		}
		return this.workTypeMap.get(workTypeCode);
	}
	
	/**
	 * 回数集計結果情報を取得する
	 * @param totalTimesList 回数集計
	 * @param period 期間
	 * @param attendanceItemConverter 勤怠項目値変換
	 * @return 回数集計結果情報
	 */
	public Optional<TotalTimesResult> getResult(TotalTimes totalTimes, DatePeriod period,
			AttendanceItemConvertFactory attendanceItemConverter) {
		
		List<TotalTimes> totalTimesList = new ArrayList<>();
		totalTimesList.add(totalTimes);
		
		val results = this.getResults(totalTimesList, period, attendanceItemConverter);
		val totalCountNo = totalTimes.getTotalCountNo();
		if (!results.containsKey(totalCountNo)) return Optional.empty();
		return Optional.of(results.get(totalCountNo));
	}
	
	/**
	 * 回数集計結果情報を取得する
	 * @param totalTimesList 回数集計リスト
	 * @param period 期間
	 * @param attendanceItemConverter 勤怠項目値変換
	 * @return 回数集計結果情報マップ（回数集計NO別）
	 */
	public Map<Integer, TotalTimesResult> getResults(List<TotalTimes> totalTimesList, DatePeriod period,
			AttendanceItemConvertFactory attendanceItemConverter) {
		
		Map<Integer, TotalTimesResult> results = new HashMap<>();

		// 結果情報マップを初期化する
		for (val totalTimes : totalTimesList){
			if (totalTimes.getUseAtr() == UseAtr.NotUse) continue;
			val totalCountNo = totalTimes.getTotalCountNo();
			results.put(totalCountNo, new TotalTimesResult());
		}
		
		val dailyConverter = attendanceItemConverter.createDailyConverter(this.optionalItemMap);
		
		// 「期間」を取得
		GeneralDate procDate = period.start();
		for ( ; procDate.beforeOrEquals(period.end()); procDate = procDate.addDays(1)){

			// 勤務情報が取得できない日は、集計しない
			val workInfoOpt = this.workInfoList.getRecord(procDate);
			if (!workInfoOpt.isPresent()) continue;
			val workInfo = workInfoOpt.get();
			
			// 勤務種類が取得できない日は、集計しない
			if (workInfo.getWorkTypeCode() == null) continue;
			String workTypeCd = workInfo.getWorkTypeCode().v();
			val workType = this.getWorkType(workTypeCd);
			if (workType == null) continue;

			// 就業時間帯を確認する
			String workTimeCd = "";
			if (workInfo.getWorkTimeCode() != null) workTimeCd = workInfo.getWorkTimeCode().v();
			
			// 勤務種別コードを確認する
			String workTypeCode = "";
			if (this.workTypeOfDailyMap.containsKey(procDate)) {
				if (this.workTypeOfDailyMap.get(procDate).getWorkTypeCode() != null) {
					workTypeCode = this.workTypeOfDailyMap.get(procDate).getWorkTypeCode().v();
				}
			}
			
			for (val totalTimes : totalTimesList){
				if (totalTimes.getUseAtr() == UseAtr.NotUse) continue;
				val totalCountNo = totalTimes.getTotalCountNo();
				val totalCondition = totalTimes.getTotalCondition();
				val result = results.get(totalCountNo);
				
				// 大塚カスタマイズの条件判断
				if (result.checkOotsukaCustomize(totalCountNo, workTypeCode) == false) continue;
				
				// 勤務情報の判断
				boolean isTargetWork = false;
				if (totalTimes.getSummaryList().isPresent()){
					val summaryList = totalTimes.getSummaryList().get();
					switch (totalTimes.getSummaryAtr()){
					case COMBINATION:
						boolean isExistWorkType = false;
						for (val sumWorkType : summaryList.getWorkTypeCodes()){
							if (sumWorkType.compareTo(workTypeCd) == 0){
								isExistWorkType = true;
								break;
							}
						}
						boolean isExistWorkTime = false;
						for (val sumWorkTime : summaryList.getWorkTimeCodes()){
							if (sumWorkTime.compareTo(workTimeCd) == 0){
								isExistWorkTime = true;
								break;
							}
						}
						if (isExistWorkType && isExistWorkTime) isTargetWork = true;
						break;
					case WORKINGTIME:
						for (val sumWorkTime : summaryList.getWorkTimeCodes()){
							if (sumWorkTime.compareTo(workTimeCd) == 0){
								isTargetWork = true;
								break;
							}
						}
						break;
					case DUTYTYPE:
						for (val sumWorkType : summaryList.getWorkTypeCodes()){
							if (sumWorkType.compareTo(workTypeCd) == 0){
								isTargetWork = true;
								break;
							}
						}
						break;
					}
				}
				if (!isTargetWork) continue;
				
				// 出勤状態の判断
				boolean isTargetAttendance = false;
				switch (totalTimes.getSummaryAtr()){
				case COMBINATION:
				case WORKINGTIME:
					// 出勤状態を判断する
					if (this.attendanceStatusList.isAttendanceDay(procDate)) isTargetAttendance = true;
					break;
				case DUTYTYPE:
					isTargetAttendance = true;
					break;
				}
				if (!isTargetAttendance) continue;
				
				if (totalTimes.getTotalCondition().getAtdItemId() != null){

					// 「日別実績の勤怠時間」を取得
					if (!this.attendanceTimeOfDailyMap.containsKey(procDate)) continue;
					val attendanceTimeOfDaily = this.attendanceTimeOfDailyMap.get(procDate);
					
					// 日別実績を回数集計用のクラスに変換
					DailyRecordToAttendanceItemConverter dailyItems =
							dailyConverter.withAttendanceTime(attendanceTimeOfDaily);
					if (this.anyItemValueOfDailyMap.containsKey(procDate)){
						dailyItems = dailyItems.withAnyItems(this.anyItemValueOfDailyMap.get(procDate));
					}
					
					// 勤務時間の判断
					val itemValOpt = dailyItems.convert(totalCondition.getAtdItemId());
					if (!itemValOpt.isPresent()) continue;
					Double itemVal = null;
					if (itemValOpt.get().getValueType().isInteger()){
						Integer intItemVal =  itemValOpt.get().value();
						if (intItemVal != null) itemVal = intItemVal.doubleValue();
						if (itemVal == null) itemVal = 0.0;
					}
					if (itemValOpt.get().getValueType().isDouble()){
						itemVal = itemValOpt.get().value();
						if (itemVal == null) itemVal = 0.0;
					}
					if (itemVal == null) continue;
					boolean isTargetValue = true;
					if (totalCondition.getLowerLimitSettingAtr() == UseAtr.Use){
						// 下限未満なら、集計しない
						if (totalCondition.getThresoldLowerLimit() != null){
							if (itemVal < totalCondition.getThresoldLowerLimit().v().doubleValue()){
								isTargetValue = false;
							}
						}
					}
					if (totalCondition.getUpperLimitSettingAtr() == UseAtr.Use){
						// 上限以上なら、集計しない
						if (totalCondition.getThresoldUpperLimit() != null){
							if (itemVal >= totalCondition.getThresoldUpperLimit().v().doubleValue()){
								isTargetValue = false;
							}
						}
					}
					if (!isTargetValue) continue;
				}

				// 回数を取得
				double count = 1.0;
				switch (workType.getAttendanceHolidayAttr()){
				case MORNING:
				case AFTERNOON:
					count = 0.5;
					break;
				default:
					break;
				}
				switch (totalTimes.getCountAtr()){
				case ONEDAY:
					// カウント区分＝1回なら、0.5日も1回としてカウントする
					if (count > 0.0) count = 1.0;
					break;
				case HALFDAY:
					// カウント区分＝0.5回なら、日数をそのまま回数とする
					break;
				}
				if (count == 0.0) continue;
				
				// 回数・時間を加算する
				result.addCount(count);
				result.addTime(this.attendanceStatusList.getTotalTime(procDate).v());
			}
		}
		
		// 回数集計結果情報を返す
		return results;
	}
}
