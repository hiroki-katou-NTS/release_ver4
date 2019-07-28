package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

/**
 * 戻り値：締め処理すべき集計期間を計算
 * @author shuichi_ishida
 */
@Getter
@Setter
public class CalcPeriodForClosureProcValue {

	/** 終了状態 */
	private CalcPeriodForClosureProcState state;
	/** 締め処理期間 */
	private Optional<ClosurePeriod> closurePeriod;
	
	/**
	 * コンストラクタ
	 */
	public CalcPeriodForClosureProcValue(){
		this.state = CalcPeriodForClosureProcState.NOT_EXIST;
		this.closurePeriod = Optional.empty();
	}
}
