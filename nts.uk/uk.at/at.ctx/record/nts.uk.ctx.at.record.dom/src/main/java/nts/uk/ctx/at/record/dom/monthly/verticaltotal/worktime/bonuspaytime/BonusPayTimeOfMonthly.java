package nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.bonuspaytime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 月別実績の加給時間
 * @author shuichu_ishida
 */
@Getter
public class BonusPayTimeOfMonthly {

	/** 加給時間 */
	private Map<Integer, AggregateBonusPayTime> bonusPayTime;
	
	/**
	 * コンストラクタ
	 */
	public BonusPayTimeOfMonthly(){
		
		this.bonusPayTime = new HashMap<>();
	}
	
	/**
	 * ファクトリー
	 * @param bonusPayTime 加給時間
	 * @return 月別実績の加給時間
	 */
	public static BonusPayTimeOfMonthly of(List<AggregateBonusPayTime> bonusPayTime){
		
		val domain = new BonusPayTimeOfMonthly();
		for (val aggrBonusPayTime : bonusPayTime){
			val bonusPayFrameNo = Integer.valueOf(aggrBonusPayTime.getBonusPayFrameNo());
			domain.bonusPayTime.putIfAbsent(bonusPayFrameNo, aggrBonusPayTime);
		}
		return domain;
	}
	
	/**
	 * 集計
	 * @param workType 勤務種類
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 */
	public void aggregate(WorkType workType, AttendanceTimeOfDailyPerformance attendanceTimeOfDaily){
	
		if (workType == null) return;
		if (attendanceTimeOfDaily == null) return;
		
		// 休出かどうか判断
		boolean isHolidayWork = workType.getDailyWork().isHolidayWork();
		
		val totalWorkingtime = attendanceTimeOfDaily.getActualWorkingTimeOfDaily().getTotalWorkingTime();
		val raiseSalaryTime = totalWorkingtime.getRaiseSalaryTimeOfDailyPerfor();
		val bonusPayTimes = raiseSalaryTime.getRaisingSalaryTimes();
		val specDayBonusPayTimes = raiseSalaryTime.getAutoCalRaisingSalarySettings();

		//*****（未）　加給時間のクラス利用が不整合になっていて、正しいメンバが参照できない。
		
		// 加給時間ごとの集計
		for (val bonusPayTime : bonusPayTimes){
			val bonusPayNo = Integer.valueOf(bonusPayTime.getBonusPayTimeItemNo());
			this.bonusPayTime.putIfAbsent(bonusPayNo, new AggregateBonusPayTime(bonusPayNo.intValue()));
			val targetBonusPayTime = this.bonusPayTime.get(bonusPayNo);
			
			if (isHolidayWork){

			}
			else {

			}
		}
		
		// 特定日加給時間ごとの集計
		for (val specDayBonusPayTime : specDayBonusPayTimes){
			val bonusPayNo = Integer.valueOf(specDayBonusPayTime.getBonusPayTimeItemNo());
			this.bonusPayTime.putIfAbsent(bonusPayNo, new AggregateBonusPayTime(bonusPayNo.intValue()));
			val targetBonusPayTime = this.bonusPayTime.get(bonusPayNo);
			
			if (isHolidayWork){
				
			}
			else {
				
			}
		}
	}

	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(BonusPayTimeOfMonthly target){
		
		for (val bonusPayValue : this.bonusPayTime.values()){
			val frameNo = bonusPayValue.getBonusPayFrameNo();
			if (target.bonusPayTime.containsKey(frameNo)){
				val targetBonusPayValue = target.bonusPayTime.get(frameNo);
				bonusPayValue.addMinutesToBonusPayTime(targetBonusPayValue.getBonusPayTime().v());
				bonusPayValue.addMinutesToSpecificBonusPayTime(targetBonusPayValue.getSpecificBonusPayTime().v());
				bonusPayValue.addMinutesToHolidayWorkBonusPayTime(targetBonusPayValue.getHolidayWorkBonusPayTime().v());
				bonusPayValue.addMinutesToHolidayWorkSpecificBonusPayTime(targetBonusPayValue.getHolidayWorkSpecificBonusPayTime().v());
			}
		}
		for (val targetBonusPayValue : target.bonusPayTime.values()){
			val frameNo = targetBonusPayValue.getBonusPayFrameNo();
			this.bonusPayTime.putIfAbsent(frameNo, targetBonusPayValue);
		}
	}
}
