package nts.uk.ctx.at.record.dom.dailyprocess.calc.ootsuka;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.worktime.common.HolidayCalculation;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkCalcSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 日別計算の大塚専用処理
 * @author keisuke_hoshina
 *
 */
public interface OotsukaProcessService {

	//大塚1日年休時に計算するための勤種変更
	public WorkType getOotsukaWorkType(WorkType workType,Optional<FixedWorkCalcSetting> calcMethodOfFixWork,TimeLeavingOfDailyPerformance attendanceLeaving, HolidayCalculation holidayCalculation);
	
	//大塚モードであるか判定する
	public boolean decisionOotsukaMode(WorkType workType,Optional<FixedWorkCalcSetting> calcMethodOfFixWork,TimeLeavingOfDailyPerformance attendanceLeaving, HolidayCalculation holidayCalculation);
	
	//大塚モード処理(日別実績の計算)
	//&大塚モード処理(計算項目を置き換え)
	public IntegrationOfDaily integrationConverter(IntegrationOfDaily fromStamp, IntegrationOfDaily fromPcLogInfo);
	
//	//IW専用処理(打刻補正用
//	public Optional<TimeLeavingOfDailyPerformance> iWProcessForStamp(Optional<TimeLeavingOfDailyPerformance> timeLeavingOfDailyPerformance, String employeeId,GeneralDate targetDate,Optional<PredetemineTimeSetting> predetermineTimeSet,WorkType workType,WorkTimeCode workTimeCode);
//
//	//IW専用処理(休憩時刻補正用)
//	public Optional<BreakTimeOfDailyPerformance> convertBreakTimeSheetForOOtsuka(Optional<BreakTimeOfDailyPerformance> breakTimeOfDailyPerformance,WorkType workType,WorkTimeCode workTimeCode);
	
//	//IWか判断処理
//	public boolean isIWWorkTimeAndCode(WorkType workType,WorkTimeCode workTimeCode);
	
}
