/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.NotUseAtr;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class HolidayCalculation.
 */
//休暇時の計算
@Getter
@NoArgsConstructor
public class HolidayCalculation extends WorkTimeDomainObject implements Cloneable{

	/** The is calculate. */
	//計算する
	private NotUseAtr isCalculate;
	
	/**
	 * Instantiates a new holiday calculation.
	 *
	 * @param memento the memento
	 */
	public HolidayCalculation(HolidayCalculationGetMemento memento) {
		this.isCalculate = memento.getIsCalculate();
	}
	
	/**
	 * Save to mememto.
	 *
	 * @param memento the memento
	 */
	public void saveToMememto(HolidayCalculationSetMemento memento){
		memento.setIsCalculate(this.isCalculate);
	}

	/**
	 * Default Constctor
	 * @param isCalculate
	 */
	public HolidayCalculation(NotUseAtr isCalculate) {
		super();
		this.isCalculate = isCalculate;
	}

	@Override
	public HolidayCalculation clone() {
		HolidayCalculation cloned = new HolidayCalculation();
		try {
			cloned.isCalculate = NotUseAtr.valueOf(this.isCalculate.value);
		}
		catch (Exception e){
			throw new RuntimeException("HolidayCalculation clone error.");
		}
		return cloned;
	}
}
