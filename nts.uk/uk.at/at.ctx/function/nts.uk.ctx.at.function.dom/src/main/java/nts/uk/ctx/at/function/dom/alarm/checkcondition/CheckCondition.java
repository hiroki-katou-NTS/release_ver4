package nts.uk.ctx.at.function.dom.alarm.checkcondition;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.ExtractionRangeBase;
import nts.uk.ctx.at.shared.dom.alarmList.AlarmCategory;

/**
 * @author dxthuong
 * チェック条件
 */
@Getter
public class CheckCondition  extends DomainObject {
	/**
	 * Alarm category
	 */
	private AlarmCategory alarmCategory;
	/**
	 * list alarm check codition code
	 * @see AlarmCheckConditionByCategory
	 */
	private List<String> checkConditionList = new ArrayList<String>();

	/**
	 * Extraction Range abstract class
	 */
	private List<ExtractionRangeBase> extractPeriodList;

	public CheckCondition(AlarmCategory alarmCategory,
	                      List<String> checkConditionList, List<ExtractionRangeBase>  extractPeriodList) {
		super();
		this.alarmCategory = alarmCategory;
		this.checkConditionList = checkConditionList;
		this.extractPeriodList = extractPeriodList;
	}
	/**
	 * 日次
	 * @return
	 */
	public boolean isDaily() {
		return this.alarmCategory == AlarmCategory.DAILY;
	}

	/**
	 * 月次
	 * @return
	 */
	public boolean isMonthly() {
		return this.alarmCategory == AlarmCategory.MONTHLY;
	}
	/**
	 * 複数月
	 * @return
	 */
	public boolean isMultipleMonth() {
		return this.alarmCategory == AlarmCategory.MULTIPLE_MONTH;
	}
	/**
	 * スケジュール4週
	 * @return
	 */
	public boolean is4W4D() {
		return this.alarmCategory == AlarmCategory.SCHEDULE_4WEEK;
	}
	/**
	 * 工数チェック
	 * @return
	 */
	public boolean isManHourCheck() {
		return this.alarmCategory == AlarmCategory.MAN_HOUR_CHECK;
	}
	/**
	 * ３６協定
	 * @return
	 */
	public boolean isAgrrement() {
		return this.alarmCategory== AlarmCategory.AGREEMENT;
	}
	/**
	 * 年休
	 * @return
	 */
	public boolean isAttHoliday() {
		return this.alarmCategory== AlarmCategory.ATTENDANCE_RATE_FOR_HOLIDAY;
	}
	
	/**
	 * 申請承認
	 * @return
	 */
	public boolean isApplication() {
		return this.alarmCategory== AlarmCategory.APPLICATION_APPROVAL;
	}
	
	/**
	 * マスタチェック
	 * @return
	 */
	public boolean isMasterChk() {
		return this.alarmCategory== AlarmCategory.MASTER_CHECK;
	}
	
	/**
	 * スケジュール日次
	 * @return
	 */
	public boolean isScheduleDaily() {
		return this.alarmCategory== AlarmCategory.SCHEDULE_DAILY;
	}
	
	/**
	 * スケジュール月次
	 * @return
	 */
	public boolean isScheduleMonthly() {
		return this.alarmCategory== AlarmCategory.SCHEDULE_MONTHLY;
	}
	
	/**
	 * スケジュール年間
	 * @return
	 */
	public boolean isScheduleYear() {
		return this.alarmCategory== AlarmCategory.SCHEDULE_YEAR;
	}
	
	/**
	 * 週次
	 * @return
	 */
	public boolean isWeekly() {
		return this.alarmCategory== AlarmCategory.WEEKLY;
	}
}
