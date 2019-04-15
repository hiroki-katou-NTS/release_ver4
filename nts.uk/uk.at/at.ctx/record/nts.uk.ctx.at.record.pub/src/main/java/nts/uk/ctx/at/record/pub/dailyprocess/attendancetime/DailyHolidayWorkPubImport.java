package nts.uk.ctx.at.record.pub.dailyprocess.attendancetime;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

@Getter
public class DailyHolidayWorkPubImport {

	//勤務種類
	WorkType workType;
	
	//休出枠時間
	List<HolidayWorkFrameTime> afterCalcUpperTimeList;
	
	//就業時間帯毎の代休設定
	Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet;
	
	//会社共通の代休設定
	Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet;

	/**
	 * Constructor 
	 */
	private DailyHolidayWorkPubImport(WorkType workType, List<HolidayWorkFrameTime> afterCalcUpperTimeList,
			Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
			Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		super();
		this.workType = workType;
		this.afterCalcUpperTimeList = afterCalcUpperTimeList;
		this.eachWorkTimeSet = eachWorkTimeSet;
		this.eachCompanyTimeSet = eachCompanyTimeSet;
	}
	
	public static DailyHolidayWorkPubImport create(WorkType workType, List<HolidayWorkFrameTime> afterCalcUpperTimeList,
						 Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
						 Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		return new DailyHolidayWorkPubImport(workType, afterCalcUpperTimeList, eachWorkTimeSet, eachCompanyTimeSet);
	}
	
	
	
}
