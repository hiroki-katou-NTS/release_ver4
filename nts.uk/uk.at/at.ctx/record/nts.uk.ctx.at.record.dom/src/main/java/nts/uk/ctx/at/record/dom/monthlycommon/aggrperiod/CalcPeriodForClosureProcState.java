package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import lombok.AllArgsConstructor;

/**
 * 終了状態：締め処理すべき集計期間を計算
 * @author shuichi_ishida
 */
@AllArgsConstructor
public enum CalcPeriodForClosureProcState {
	/** 締め処理期間あり */
	EXIST(0),
	/** 締め処理期間なし */
	NOT_EXIST(1),
	/** 締め処理済み */
	PROCESSED(2);

	public final int value;
}
