package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;

/**
 * 戻り値：フレックス不足から年休を欠勤を控除する
 * @author shuichu_ishida
 */
@Getter
public class DeductFromFlexShortageValue {

	/** 月別実績の月の計算 */
	@Setter
	private MonthlyCalculation monthlyCalculation; 
	/** エラーメッセージID */
	private List<String> errorMessageIds;
	
	/**
	 * コンストラクタ
	 */
	public DeductFromFlexShortageValue(){
		this.monthlyCalculation = new MonthlyCalculation();
		this.errorMessageIds = new ArrayList<>();
	}
}
