/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class StampBreakCalculation.
 */
// 打刻から休憩を計算
@Getter
@NoArgsConstructor
public class StampBreakCalculation extends WorkTimeDomainObject implements Cloneable{

	/** The use private go out rest. */
	// 私用外出を休憩として扱う
	private boolean usePrivateGoOutRest;

	/** The use asso go out rest. */
	// 組合外出を休憩として扱う
	private boolean useAssoGoOutRest;

	/**
	 * Instantiates a new stamp break calculation.
	 *
	 * @param usePrivateGoOutRest
	 *            the use private go out rest
	 * @param useAssoGoOutRest
	 *            the use asso go out rest
	 */
	public StampBreakCalculation(boolean usePrivateGoOutRest, boolean useAssoGoOutRest) {
		super();
		this.usePrivateGoOutRest = usePrivateGoOutRest;
		this.useAssoGoOutRest = useAssoGoOutRest;
	}

	/**
	 * Instantiates a new stamp break calculation.
	 *
	 * @param memento
	 *            the memento
	 */
	public StampBreakCalculation(StampBreakCalculationGetMemento memento) {
		this.usePrivateGoOutRest = memento.getUsePrivateGoOutRest();
		this.useAssoGoOutRest = memento.getUseAssoGoOutRest();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(StampBreakCalculationSetMemento memento) {
		memento.setUsePrivateGoOutRest(this.usePrivateGoOutRest);
		memento.setUseAssoGoOutRest(this.useAssoGoOutRest);
	}

	public void setDefaultValue() {
		this.useAssoGoOutRest = false;
		this.usePrivateGoOutRest = false;
	}
	
	@Override
	public StampBreakCalculation clone() {
		StampBreakCalculation cloned = new StampBreakCalculation();
		try {
			cloned.usePrivateGoOutRest = this.usePrivateGoOutRest ? true : false ;
			cloned.useAssoGoOutRest = this.useAssoGoOutRest ? true : false ;
		}
		catch (Exception e){
			throw new RuntimeException("StampBreakCalculation clone error.");
		}
		return cloned;
	}
}
