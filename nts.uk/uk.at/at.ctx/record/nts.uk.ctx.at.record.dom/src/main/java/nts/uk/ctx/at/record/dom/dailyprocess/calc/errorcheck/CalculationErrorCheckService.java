package nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck;

import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;

/**
 * ドメインサービス：日別計算のエラーチェック処理
 * @author keisuke_hoshina
 *
 */
public interface CalculationErrorCheckService {
	
	/**
	 * エラーチェック
	 * @param integrationOfDaily 日別実績(WORK)
	 */
	public IntegrationOfDaily errorCheck(IntegrationOfDaily integrationOfDaily);
}
