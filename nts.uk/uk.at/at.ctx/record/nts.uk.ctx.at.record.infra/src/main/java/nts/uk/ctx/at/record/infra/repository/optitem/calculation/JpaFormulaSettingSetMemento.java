/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.optitem.calculation;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingItem;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaSettingSetMemento;
import nts.uk.ctx.at.record.dom.optitem.calculation.MinusSegment;
import nts.uk.ctx.at.record.dom.optitem.calculation.OperatorAtr;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtFormulaSetting;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtOptItemFormulaPK;

/**
 * The Class JpaFormulaSettingGetMemento.
 */
@Getter
public class JpaFormulaSettingSetMemento implements FormulaSettingSetMemento {

	/** The setting. */
	private KrcmtFormulaSetting setting;

	/**
	 * Instantiates a new jpa formula setting set memento.
	 *
	 * @param formulaPk the formula pk
	 */
	public JpaFormulaSettingSetMemento(KrcmtOptItemFormulaPK formulaPk) {
		this.setting = new KrcmtFormulaSetting(formulaPk);
	}

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
		this.setting.setMinusSegment(segment.value);
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
		this.setting.setOperator(operator.value);
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
		this.setting.setLeftInputVal(item.getInputValue().v());
		this.setting.setLeftFormulaItemId(item.getFormulaItemId().v());
		this.setting.setLeftSetMethod(item.getSettingMethod().value);
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
		this.setting.setRightInputVal(item.getInputValue().v());
		this.setting.setRightFormulaItemId(item.getFormulaItemId().v());
		this.setting.setRightSetMethod(item.getSettingMethod().value);
	}

}
