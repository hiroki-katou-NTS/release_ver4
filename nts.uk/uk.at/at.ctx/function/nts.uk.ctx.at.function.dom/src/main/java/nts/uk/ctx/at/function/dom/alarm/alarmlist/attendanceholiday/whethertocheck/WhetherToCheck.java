package nts.uk.ctx.at.function.dom.alarm.alarmlist.attendanceholiday.whethertocheck;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
//import nts.uk.ctx.at.function.dom.alarm.alarmlist.attendanceholiday.whethertocheck.narrowlastdays.NarrowLastDays;
//import nts.uk.ctx.at.function.dom.alarm.alarmlist.attendanceholiday.whethertocheck.narrowuntilnext.NarrowUntilNext;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.annualholiday.AnnualHolidayAlarmCondition;
/**
 * チェック対象か判断
 * @author tutk
 *
 */
@Stateless
public class WhetherToCheck {

//	@Inject
//	private NarrowLastDays narrowLastDays;
//	
//	@Inject
//	private NarrowUntilNext narrowUntilNext;
//	
	public boolean whetherToCheck(String companyId,String employeeId,AlarmCheckConditionByCategory alCheckConByCategory) {
//		//ドメインモデル「年休アラームチェック対象者条件」を取得する
//		AnnualHolidayAlarmCondition annualHolidayAlarmCondition = (AnnualHolidayAlarmCondition) alCheckConByCategory.getExtractionCondition();
//		
//		//前回年休付与日数の条件で絞り込む
//		GeneralDate baseDate = GeneralDate.today();
//		boolean checkNarrowLastDays = narrowLastDays.checkNarrowLastDays(employeeId, annualHolidayAlarmCondition,baseDate);
//		//比較結果をチェックする
//		//false：チェック非対象　を返す
//		if(!checkNarrowLastDays)
//			return false;
//		//次回年休付与日までの期間の条件で絞り込む
//		boolean checkNarrowUntilNext = narrowUntilNext.checkNarrowUntilNext(employeeId, annualHolidayAlarmCondition);
//		//確認結果をチェックする
//		//false：チェック非対象　を返す
//		if(!checkNarrowUntilNext)
//			return false;
//		
		return true;
	}
}
