/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.command.optitem;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.optitem.AmountRange;
import nts.uk.ctx.at.record.dom.optitem.CalcRangeCheck;
import nts.uk.ctx.at.record.dom.optitem.CalcResultRangeGetMemento;
import nts.uk.ctx.at.record.dom.optitem.NumberRange;
import nts.uk.ctx.at.record.dom.optitem.TimeRange;

/**
 * The Class CalculationResultRangeDto.
 */
@Getter
@Setter
public class CalcResultRangeDto implements CalcResultRangeGetMemento {

	/** The upper check. */
	// 上限値チェック
	private boolean upperCheck;

	/** The lower check. */
	// 下限値チェック
	private boolean lowerCheck;

	// ===================== Optional ======================= //
	/** The number upper. */
	// 回数範囲
	private BigDecimal numberUpper;

	/** The number lower. */
	private BigDecimal numberLower;

	/** The time upper. */
	// 時間範囲
	private int timeUpper;

	/** The time lower. */
	private int timeLower;

	/** The amount upper. */
	// 金額範囲
	private int amountUpper;

	/** The amount lower. */
	private int amountLower;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrecord.CalculationResultRangeGetMemento#
	 * getUpperLimit()
	 */
	@Override
	public CalcRangeCheck getUpperLimit() {
		return this.upperCheck ? CalcRangeCheck.SET : CalcRangeCheck.NOT_SET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrecord.CalculationResultRangeGetMemento#
	 * getLowerLimit()
	 */
	@Override
	public CalcRangeCheck getLowerLimit() {
		return this.upperCheck ? CalcRangeCheck.SET : CalcRangeCheck.NOT_SET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrecord.CalculationResultRangeGetMemento#
	 * getNumberRange()
	 */
	@Override
	public NumberRange getNumberRange() {
		return new NumberRange(this.numberUpper, this.numberLower);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrecord.CalculationResultRangeGetMemento#
	 * getTimeRange()
	 */
	@Override
	public TimeRange getTimeRange() {
		return new TimeRange(this.timeUpper, this.timeLower);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrecord.CalculationResultRangeGetMemento#
	 * getAmountRange()
	 */
	@Override
	public AmountRange getAmountRange() {
		return new AmountRange(this.amountUpper, this.amountLower);
	}

}
