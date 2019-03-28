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
}
