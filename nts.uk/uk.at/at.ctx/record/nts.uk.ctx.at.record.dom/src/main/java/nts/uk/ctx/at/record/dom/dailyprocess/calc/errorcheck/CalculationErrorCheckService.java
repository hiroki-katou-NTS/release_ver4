package nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck;

import java.util.List;

import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;

/**
 * ドメインサービス：日別計算のエラーチェック処理
 * @author keisuke_hoshina
 *
 */
public interface CalculationErrorCheckService {
	
	/**
	 * エラーチェック
	 * @param integrationOfDaily 日別実績(WORK)
	 * @param errorAlarm 
	 */
	public IntegrationOfDaily errorCheck(IntegrationOfDaily integrationOfDaily, List<ErrorAlarmWorkRecord> errorAlarm);
}
