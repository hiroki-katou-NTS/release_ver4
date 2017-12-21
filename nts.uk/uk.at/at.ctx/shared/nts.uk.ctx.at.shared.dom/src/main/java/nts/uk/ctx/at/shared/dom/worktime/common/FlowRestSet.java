/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.fluidbreaktimeset.FlowRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.fluidbreaktimeset.RestClockManageAtr;
import nts.uk.ctx.at.shared.dom.worktimeset.common.FlowRestClockCalcMethod;

/**
 * The Class FlowRestSet.
 */
//流動休憩設定

/**
 * Gets the calculate method.
 *
 * @return the calculate method
 */
@Getter
public class FlowRestSet extends DomainObject {

	/** The use stamp. */
	//打刻を併用する
	private boolean useStamp;
	
	/** The use stamp calc method. */
	//打刻併用時の計算方法
	private FlowRestClockCalcMethod useStampCalcMethod;
	
	/** The time manager set atr. */
	//時刻管理設定区分
	private RestClockManageAtr 	timeManagerSetAtr;
	
	/** The calculate method. */
	//計算方法
	private FlowRestCalcMethod calculateMethod;

//	/**
//	 * Instantiates a new flow rest set.
//	 *
//	 * @param memento the memento
//	 */
//	public FlowRestSet (FlowRestSetGetMemento memento) {
//		this.useStamp = memento.getUseStamp();
//		this.useStampCalcMethod = memento.getUseStampCalcMethod();
//		this.timeManagerSetAtr = memento.getTimeManagerSetAtr();
//		this.calculateMethod = memento.getCalculateMethod();
//	}
//
//	/**
//	 * Save to memento.
//	 *
//	 * @param memento the memento
//	 */
//	public void saveToMemento(FlowRestSetSetMemento memento) {
//		memento.setUseStamp(this.useStamp);
//		memento.setUseStampCalcMethod(this.useStampCalcMethod);
//		memento.setTimeManagerSetAtr(this.timeManagerSetAtr);
//		memento.setCalculateMethod(this.calculateMethod);
//	}
}
