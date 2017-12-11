/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * The Class FlowOTSet.
 */
//流動残業設定
@Getter
public class FlOTSet extends DomainObject {

	/** The fixed change atr. */
	//所定変動区分
	private FixedChangeAtr fixedChangeAtr;

	/**
	 * Instantiates a new flow OT set.
	 *
	 * @param memento the memento
	 */
	public FlOTSet(FlOTGetMemento memento) {
		this.fixedChangeAtr = memento.getFixedChangeAtr();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FlOTSetMemento memento) {
		memento.setFixedChangeAtr(this.fixedChangeAtr);
	}
}
