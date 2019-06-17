/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class ExtraordWorkOTFrameSet.
 */
// 臨時勤務時の残業枠設定
@Getter
@NoArgsConstructor
public class ExtraordWorkOTFrameSet extends WorkTimeDomainObject implements Cloneable{

	/** The ot frame no. */
	// 残業枠NO
	private OTFrameNo otFrameNo;

	/** The in legal work frame no. */
	// 法内残業枠NO
	private OTFrameNo inLegalWorkFrameNo;

	/** The settlement order. */
	// 精算順序
	private SettlementOrder settlementOrder;

	/**
	 * Instantiates a new extraord work OT frame set.
	 *
	 * @param memento the memento
	 */
	public ExtraordWorkOTFrameSet (ExtraordWorkOTFrameSetGetMemento memento) {
		this.otFrameNo = memento.getOtFrameNo();
		this.inLegalWorkFrameNo = memento.getInLegalWorkFrameNo();
		this.settlementOrder = memento.getSettlementOrder();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(ExtraordWorkOTFrameSetSetMemento memento) {
		memento.setOTFrameNo(this.otFrameNo);
		memento.setInLegalWorkFrameNo(this.inLegalWorkFrameNo);
		memento.setSettlementOrder(this.settlementOrder);
	}

	/**
	 * Instantiates a new extraord work OT frame set.
	 *
	 * @param oTFrameNo the o T frame no
	 * @param inLegalWorkFrameNo the in legal work frame no
	 * @param settlementOrder the settlement order
	 */
	public ExtraordWorkOTFrameSet(OTFrameNo oTFrameNo, OTFrameNo inLegalWorkFrameNo,
			SettlementOrder settlementOrder) {
		super();
		this.otFrameNo = oTFrameNo;
		this.inLegalWorkFrameNo = inLegalWorkFrameNo;
		this.settlementOrder = settlementOrder;
	}
	
	@Override
	public ExtraordWorkOTFrameSet clone() {
		ExtraordWorkOTFrameSet cloned = new ExtraordWorkOTFrameSet();
		try {
			cloned.otFrameNo = new OTFrameNo(this.otFrameNo.v());
			cloned.inLegalWorkFrameNo = new OTFrameNo(this.inLegalWorkFrameNo.v());
			cloned.settlementOrder = new SettlementOrder(this.settlementOrder.v());
		}
		catch (Exception e){
			throw new RuntimeException("ExtraordWorkOTFrameSet clone error.");
		}
		return cloned;
	}
}
