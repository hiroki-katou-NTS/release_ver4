package nts.uk.ctx.at.schedule.dom.schedule.alarm.continuouswork.continuousattendance;

import lombok.Value;
import nts.arc.layer.dom.objecttype.DomainValue;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.continuouswork.NumberOfConsecutiveDays;
/**
 * 連続出勤できる上限日数
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務予定.勤務予定.勤務予定のアラームチェック.連続勤務.連続出勤.連続出勤できる上限日数
 * @author lan_lt
 *
 */
@Value
public class MaxNumberDaysOfContinuousAttendance implements DomainValue{
	//日数
	private NumberOfConsecutiveDays numberOfDays;

}
