package nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck;

import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManagePerCompanySet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManagePerPersonDailySet;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;

/**
 * ドメインサービス：日別計算のエラーチェック処理
 * @author keisuke_hoshina
 *
 */
public interface CalculationErrorCheckService {
	
	/**
	 * エラーチェック
	 * @param integrationOfDaily 日別実績(WORK)
	 * @param managePerPersonDailySet 
	 * @param errorAlarm 
	 */
	public IntegrationOfDaily errorCheck(IntegrationOfDaily integrationOfDaily, ManagePerPersonDailySet managePerPersonDailySet, ManagePerCompanySet master);
}
