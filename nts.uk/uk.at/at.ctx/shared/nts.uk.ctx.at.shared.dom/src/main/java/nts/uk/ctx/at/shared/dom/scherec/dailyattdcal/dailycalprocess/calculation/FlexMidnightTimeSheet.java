package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.AddSetting;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.AutoCalcOfLeaveEarlySetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.vacationusetime.VacationClass;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workingstyle.flex.SettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.withinworkinghours.WithinWorkTimeFrame;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.midnighttimezone.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.DailyUnit;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * フレックス深夜時間帯
 * @author shuichi_ishida
 */
@Getter
public class FlexMidnightTimeSheet {

	/** 所定内 */
	private List<MidNightTimeSheetForCalc> within;
	/** 所定外 */
	private List<MidNightTimeSheetForCalc> without;
	
	/**
	 * コンストラクタ
	 */
	public FlexMidnightTimeSheet(){
		this.within = new ArrayList<>();
		this.without = new ArrayList<>();
	}
	
	/**
	 * フレックス深夜時間帯の作成
	 * @param flexWithinWorkTimeSheet フレックス就業時間内時間帯
	 * @param holidayCalcMethodSet 休暇の計算方法の設定
	 * @param autoCalcAtr 時間外の自動計算区分
	 * @param workType 勤務種類
	 * @param flexCalcMethod フレックス勤務の設定
	 * @param predetermineTimeSet 計算用所定時間設定
	 * @param vacationClass 休暇クラス
	 * @param timevacationUseTimeOfDaily 休暇使用合計残時間未割当
	 * @param statutoryDivision 法定内区分
	 * @param siftCode 就業時間帯コード
	 * @param autoCalcOfLeaveEarlySetting 遅刻早退の自動計算設定
	 * @param addSetting 加算設定
	 * @param holidayAddtionSet 休暇加算時間設定
	 * @param dailyUnit 法定労働時間
	 * @param commonSetting 就業時間帯の共通設定
	 * @param flexUpper 時間外の上限設定
	 * @param conditionItem 労働条件項目
	 * @param predetermineTimeSetByPersonInfo 計算用所定時間（個人）
	 * @param coreTimeSetting コアタイム時間帯設定
	 * @return フレックス深夜時間帯
	 */
	public static FlexMidnightTimeSheet create(
			FlexWithinWorkTimeSheet flexWithinWorkTimeSheet,
			HolidayCalcMethodSet holidayCalcMethodSet,
			AutoCalAtrOvertime autoCalcAtr,
			WorkType workType,
			SettingOfFlexWork flexCalcMethod,
			PredetermineTimeSetForCalc predetermineTimeSet,
			VacationClass vacationClass,
			AttendanceTime timevacationUseTimeOfDaily,
			StatutoryDivision statutoryDivision,
			Optional<WorkTimeCode> siftCode,
			AutoCalcOfLeaveEarlySetting autoCalcOfLeaveEarlySetting,
			AddSetting addSetting,
			HolidayAddtionSet holidayAddtionSet,
			DailyUnit  dailyUnit,
			Optional<WorkTimezoneCommonSet> commonSetting,
			TimeLimitUpperLimitSetting flexUpper,//こいつは残さないとだめ,
			WorkingConditionItem conditionItem,
			Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
			Optional<CoreTimeSetting> coreTimeSetting,
			NotUseAtr lateEarlyMinusAtr) {

		// クラスを作成する
		FlexMidnightTimeSheet domain = new FlexMidnightTimeSheet();
		// 所定外開始時刻の計算
		Optional<TimeWithDayAttr> withoutStartTimeOpt = flexWithinWorkTimeSheet.calcWithoutStartTime(
				holidayCalcMethodSet,
				autoCalcAtr,
				workType,
				flexCalcMethod,
				predetermineTimeSet,
				vacationClass,
				timevacationUseTimeOfDaily,
				statutoryDivision,
				siftCode,
				autoCalcOfLeaveEarlySetting,
				addSetting,
				holidayAddtionSet,
				dailyUnit,
				commonSetting,
				flexUpper,
				conditionItem,
				predetermineTimeSetByPersonInfo,
				coreTimeSetting,
				lateEarlyMinusAtr);
		if (!withoutStartTimeOpt.isPresent()) return domain;
		TimeWithDayAttr withoutStartTime = withoutStartTimeOpt.get();
		// 就業時間内時間枠を取得
		for (WithinWorkTimeFrame timeFrame : flexWithinWorkTimeSheet.getWithinWorkTimeFrame()){
			if (!timeFrame.getMidNightTimeSheet().getTimeSheets().isEmpty()) continue;
			for (MidNightTimeSheetForCalc midnightTimeSheet : timeFrame.getMidNightTimeSheet().getTimeSheets()){
				// 深夜時間帯.時間帯と所定外開始時刻を比較する
				TimeSpanForDailyCalc timeSpan = midnightTimeSheet.getTimeSheet();
				MidNightTimeSheetForCalc newMidnightTimeSheet = new MidNightTimeSheetForCalc(
						midnightTimeSheet.getTimeSheet(),
						midnightTimeSheet.getRounding(),
						new ArrayList<>(timeFrame.getRecordedTimeSheet()),
						new ArrayList<>(timeFrame.getDeductionTimeSheet()));
				newMidnightTimeSheet.trimRecordedAndDeductionToSelfRange();
				if (timeSpan.getEnd().lessThanOrEqualTo(withoutStartTime)){
					// 深夜時間帯を所定内に追加する
					domain.within.add(newMidnightTimeSheet);
				}
				else if (timeSpan.getStart().greaterThanOrEqualTo(withoutStartTime)){
					// 深夜時間帯を所定外に追加する
					domain.without.add(newMidnightTimeSheet);
				}
				else if (timeSpan.contains(withoutStartTime)){
					// 計算用深夜時間帯（所定内）の作成
					MidNightTimeSheetForCalc withinMidnight = new MidNightTimeSheetForCalc(
							new TimeSpanForDailyCalc(timeSpan.getStart(), withoutStartTime),
							midnightTimeSheet.getRounding(),
							new ArrayList<>(timeFrame.getRecordedTimeSheet()),
							new ArrayList<>(timeFrame.getDeductionTimeSheet()));
					withinMidnight.trimRecordedAndDeductionToSelfRange();
					// 計算用深夜時間帯（所定外）の作成
					MidNightTimeSheetForCalc withoutMidnight = new MidNightTimeSheetForCalc(
							new TimeSpanForDailyCalc(withoutStartTime, timeSpan.getEnd()),
							midnightTimeSheet.getRounding(),
							new ArrayList<>(timeFrame.getRecordedTimeSheet()),
							new ArrayList<>(timeFrame.getDeductionTimeSheet()));
					withoutMidnight.trimRecordedAndDeductionToSelfRange();
					// 作成した計算用深夜時間帯を所定内、所定外にそれぞれ追加する
					domain.within.add(withinMidnight);
					domain.without.add(withoutMidnight);
				}
			}
		}
		// フレックス深夜時間帯を返す
		return domain;
	}
	
	/**
	 * 深夜時間を累計する
	 * @param midnightTimeSheets 計算用深夜時間帯(List)
	 * @return 累計深夜時間
	 */
	public static AttendanceTime sumMidnightTime(List<MidNightTimeSheetForCalc> midnightTimeSheets){
		
		// 累計深夜時間　←　０
		int result = 0;
		// パラメータ：計算用深夜時間帯(List)を確認する
		if (midnightTimeSheets.size() > 0){
			for (MidNightTimeSheetForCalc midnightTimeSheet : midnightTimeSheets){
				// 深夜時間の計算
				result += midnightTimeSheet.calcTotalTime().valueAsMinutes();
			}
		}
		// 累計深夜時間を返す
		return new AttendanceTime(result);
	}
}
