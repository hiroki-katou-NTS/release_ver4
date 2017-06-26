package nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.distribute;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DistributeWay {
	/** 0:割合で計算*/
	CALCULATED_PERCENTAGE(0),
	/** 1:日数控除*/
	DEDUCTION_FOR_DAYS(1),
	/** 2:計算式*/
	CALCULATION_FORMULA(2);
	
	public final int value;
}
