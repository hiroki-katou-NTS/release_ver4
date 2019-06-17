/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.common.RestClockManageAtr;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class FlowRestSet.
 */
//流動休憩設定
@Getter
@NoArgsConstructor
public class FlowRestSet extends WorkTimeDomainObject implements Cloneable{

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

	/**
	 * Instantiates a new flow rest set.
	 *
	 * @param memento the memento
	 */
	public FlowRestSet (FlowRestSetGetMemento memento) {
		this.useStamp = memento.getUseStamp();
		this.useStampCalcMethod = memento.getUseStampCalcMethod();
		this.timeManagerSetAtr = memento.getTimeManagerSetAtr();
		this.calculateMethod = memento.getCalculateMethod();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FlowRestSetSetMemento memento) {
		memento.setUseStamp(this.useStamp);
		memento.setUseStampCalcMethod(this.useStampCalcMethod);
		memento.setTimeManagerSetAtr(this.timeManagerSetAtr);
		memento.setCalculateMethod(this.calculateMethod);
	}
	
	@Override
	public FlowRestSet clone() {
		FlowRestSet cloned = new FlowRestSet();
		try {
			cloned.useStamp = this.useStamp ? true : false;
			cloned.useStampCalcMethod = FlowRestClockCalcMethod.valueOf(this.useStampCalcMethod.value);
			cloned.timeManagerSetAtr = RestClockManageAtr.valueOf(this.timeManagerSetAtr.value);
			cloned.calculateMethod = FlowRestCalcMethod.valueOf(this.calculateMethod.value);
		}
		catch (Exception e){
			throw new RuntimeException("FlowRestSet clone error.");
		}
		return cloned;
	}
}
