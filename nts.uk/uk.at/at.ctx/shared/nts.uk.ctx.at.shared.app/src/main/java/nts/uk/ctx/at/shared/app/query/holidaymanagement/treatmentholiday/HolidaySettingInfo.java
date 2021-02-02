package nts.uk.ctx.at.shared.app.query.holidaymanagement.treatmentholiday;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.calendar.DayOfWeek;
import nts.uk.ctx.at.shared.dom.holidaymanagement.treatmentholiday.HolidayCheckUnit;
import nts.uk.ctx.at.shared.dom.holidaymanagement.treatmentholiday.StartDateClassification;
import nts.uk.ctx.at.shared.dom.holidaymanagement.treatmentholiday.TreatmentHoliday;
import nts.uk.ctx.at.shared.dom.workrule.weekmanage.WeekStart;

/**
 * 休日の設定情報
 * @author tutk
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class HolidaySettingInfo {

	/**
	 * 休日の扱い
	 */
	private Optional<TreatmentHoliday> treatmentHoliday;
	
	/**
	 * 休日チェック単位
	 */
	private Optional<HolidayCheckUnit> holidayCheckUnit;
	
	
	/**
	 * 計算設定画面名
	 */
	private String calSettingScreenName;
	
	/**
	 * 起算曜日
	 */
	private Optional<DayOfWeek> weekStart;
	
	/**
	 * 起算日区分
	 */
	private Optional<StartDateClassification> startDateClassification;

	public HolidaySettingInfo(Optional<TreatmentHoliday> treatmentHoliday, Optional<HolidayCheckUnit> holidayCheckUnit,
			String calSettingScreenName, Optional<DayOfWeek> weekStart,
			Optional<StartDateClassification> startDateClassification) {
		super();
		this.treatmentHoliday = treatmentHoliday;
		this.holidayCheckUnit = holidayCheckUnit;
		this.calSettingScreenName = calSettingScreenName;
		this.weekStart = weekStart;
		this.startDateClassification = startDateClassification;
	}
	
	
	
}
