/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.app.command.optitem.calculation;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingGetMemento;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingItem;
import nts.uk.ctx.at.record.dom.optitem.calculation.MinusSegment;
import nts.uk.ctx.at.record.dom.optitem.calculation.OperatorAtr;

/**
 * The Class FormulaSettingDto.
 */
@Getter
@Setter
public class FormulaSettingDto implements FormulaSettingGetMemento {

	/** The minus segment. */
	// マイナス区分
	private int minusSegment;

	/** The operator. */
	// 演算子
	private int operator;

	/** The left item. */
	// 計算式設定項目
	private FormulaSettingItemDto leftItem;

	/** The right item. */
	// 計算式設定項目
	private FormulaSettingItemDto rightItem;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingGetMemento#
	 * getMinusSegment()
	 */
	@Override
	public MinusSegment getMinusSegment() {
		return EnumAdaptor.valueOf(this.minusSegment, MinusSegment.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingGetMemento#
	 * getOperatorAtr()
	 */
	@Override
	public OperatorAtr getOperatorAtr() {
		return EnumAdaptor.valueOf(this.operator, OperatorAtr.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingGetMemento#
	 * getLeftItem()
	 */
	@Override
	public FormulaSettingItem getLeftItem() {
		return new FormulaSettingItem(this.leftItem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingGetMemento#
	 * getRightItem()
	 */
	@Override
	public FormulaSettingItem getRightItem() {
		return new FormulaSettingItem(this.rightItem);
	}

}
