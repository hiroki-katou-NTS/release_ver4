/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.find.optitem.calculation;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingItem;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingSetMemento;
import nts.uk.ctx.at.record.dom.optitem.calculation.MinusSegment;
import nts.uk.ctx.at.record.dom.optitem.calculation.OperatorAtr;

/**
 * The Class FormulaSettingDto.
 */
@Getter
@Setter
public class FormulaSettingDto implements FormulaSettingSetMemento {

	/** The minus segment. */
	// マイナス区分
	private int minusSegment;

	/** The operator. */
	// 演算子
	private int operator;

	/** The lef item. */
	// 計算式設定項目
	private FormulaSettingItemDto lefItem;

	/** The right item. */
	// 計算式設定項目
	private FormulaSettingItemDto rightItem;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingSetMemento#
	 * setMinusSegment(nts.uk.ctx.at.record.dom.optitem.calculation.
	 * MinusSegment)
	 */
	@Override
	public void setMinusSegment(MinusSegment segment) {
		this.minusSegment = segment.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingSetMemento#
	 * setOperatorAtr(nts.uk.ctx.at.record.dom.optitem.calculation.OperatorAtr)
	 */
	@Override
	public void setOperatorAtr(OperatorAtr operator) {
		this.operator = operator.value;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingSetMemento#
	 * setLeftItem(nts.uk.ctx.at.record.dom.optitem.calculation.
	 * FormulaSettingItem)
	 */
	@Override
	public void setLeftItem(FormulaSettingItem item) {
		item.saveToMemento(this.lefItem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingSetMemento#
	 * setRightItem(nts.uk.ctx.at.record.dom.optitem.calculation.
	 * FormulaSettingItem)
	 */
	@Override
	public void setRightItem(FormulaSettingItem item) {
		item.saveToMemento(this.rightItem);
	}

}
