package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.daily.CalcAtrOfDaily;

/**
 * 計算区分取得用のロジック
 * @author keisuke_hoshina
 */
public class GetCalcAtr {

	/**
	 * 加給時間の計算をするか判定する
	 * @param calcAtr 加給の自動計算区分
	 * @param calcSet 
	 * @param bonusPayAutoCalcSet
	 * @param actualAtr
	 * @return
	 */
	public static boolean isCalc(boolean calcAtr,CalcAtrOfDaily calcSet, BonusPayAutoCalcSet bonusPayAutoCalcSet, ActualWorkTimeSheetAtr actualAtr) {
		if(calcAtr) {
			return bonusPayAutoCalcSet.getCalcAtr(actualAtr, calcSet);
		}
		else {
			return false;
		}
	}
}
