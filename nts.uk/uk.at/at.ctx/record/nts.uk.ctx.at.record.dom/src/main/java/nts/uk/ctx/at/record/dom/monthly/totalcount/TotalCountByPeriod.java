package nts.uk.ctx.at.record.dom.monthly.totalcount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.MonthlyAggregationErrorInfo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.totaltimes.TotalTimesFromDailyRecord;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 期間別の回数集計
 * @author shuichi_ishida
 */
@Getter
public class TotalCountByPeriod implements Cloneable {

	/** 回数集計 */
	private Map<Integer, TotalCount> totalCountList;

	/** エラー情報 */
	private List<MonthlyAggregationErrorInfo> errorInfos;
	
	/**
	 * コンストラクタ
	 */
	public TotalCountByPeriod(){
		
		this.totalCountList = new HashMap<>();
		
		this.errorInfos = new ArrayList<>();
	}

	/**
	 * ファクトリー
	 * @param totalCountList 回数集計
	 * @return 期間別の回数集計
	 */
	public static TotalCountByPeriod of(
			List<TotalCount> totalCountList){
		
		TotalCountByPeriod domain = new TotalCountByPeriod();
		for (val totalCount : totalCountList){
			int totalCountNo = totalCount.getTotalCountNo();
			domain.totalCountList.putIfAbsent(totalCountNo, totalCount);
		}
		return domain;
	}
	
	@Override
	public TotalCountByPeriod clone() {
		TotalCountByPeriod cloned = new TotalCountByPeriod();
		try {
			for (val totalCount : this.totalCountList.entrySet()){
				cloned.totalCountList.put(totalCount.getKey(), totalCount.getValue().clone());
			}
		}
		catch (Exception e){
			throw new RuntimeException("TotalCountbyPeriod clone error.");
		}
		return cloned;
	}
	
	/**
	 * 回数集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param companySets 月別集計で必要な会社別設定
	 * @param monthlyCalcDailys 月の計算中の日別実績データ
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void totalize(
			String companyId,
			String employeeId,
			DatePeriod period,
			MonAggrCompanySettings companySets,
			MonthlyCalculatingDailys monthlyCalcDailys,
			RepositoriesRequiredByMonthlyAggr repositories){

		// 日別実績から回数集計結果を取得する準備をする
		TotalTimesFromDailyRecord algorithm = new TotalTimesFromDailyRecord(
				companyId,
				period,
				new ArrayList<>(monthlyCalcDailys.getAttendanceTimeOfDailyMap().values()),
				monthlyCalcDailys.getAnyItemValueOfDailyList(),
				new ArrayList<>(monthlyCalcDailys.getTimeLeaveOfDailyMap().values()),
				new ArrayList<>(monthlyCalcDailys.getWorkInfoOfDailyMap().values()),
				new ArrayList<>(monthlyCalcDailys.getWorkTypeOfDailyMap().values()),
				companySets.getAllWorkTypeMap(),
				repositories.getWorkType(),
				companySets.getOptionalItemMap());
		
		// 回数集計マスタを取得
		val totalTimesList = companySets.getTotalTimesList();
		
		// 回数集計処理
		val results = algorithm.getResults(
				totalTimesList,
				period,
				repositories.getAttendanceItemConverter());
		
		// 回数集計結果を返す
		for (val result : results.entrySet()){
			this.totalCountList.putIfAbsent(
					result.getKey(),
					TotalCount.of(
							result.getKey(),
							new AttendanceDaysMonth(result.getValue().getCount().v()),
							new AttendanceTimeMonth(result.getValue().getTime().v())));
		}
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(TotalCountByPeriod target){

		for (val totalCount : this.totalCountList.values()){
			val totalCountNo = totalCount.getTotalCountNo();
			if (target.totalCountList.containsKey(totalCountNo)){
				totalCount.sum(target.totalCountList.get(totalCountNo));
			}
		}
		for (val targetTotalCount : target.getTotalCountList().values()){
			val totalCountNo = targetTotalCount.getTotalCountNo();
			this.totalCountList.putIfAbsent(totalCountNo, targetTotalCount);
		}
	}
}
