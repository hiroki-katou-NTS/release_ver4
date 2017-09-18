/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optitem.calculation;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class FormulaSettingItem.
 */
// 計算式設定
@Getter
public class FormulaSettingItem extends DomainObject {

	/** The setting method. */
	// 設定方法
	private SettingMethod settingMethod;

	/** The disp order. */
	// 順番
	private SettingItemOrder dispOrder;

	// ===================== Optional ======================= //
	/** The input value. */
	// 入力値
	private InputValue inputValue;

	/** The formula item id. */
	// 計算式項目ID
	private FormulaId formulaItemId;

	/**
	 * Instantiates a new formula setting item.
	 *
	 * @param memento the memento
	 */
	public FormulaSettingItem(FormulaSettingItemGetMemento memento) {
		super();
		this.settingMethod = memento.getSettingMethod();
		this.dispOrder = memento.getSettingItemOrder();
		this.inputValue = memento.getInputValue();
		this.formulaItemId = memento.getFormulaId();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FormulaSettingItemSetMemento memento) {
		memento.setSettingMethod(this.settingMethod);
		memento.setSettingItemOrder(this.dispOrder);
		memento.setInputValue(this.inputValue);
		memento.setFormulaId(this.formulaItemId);
	}

}
