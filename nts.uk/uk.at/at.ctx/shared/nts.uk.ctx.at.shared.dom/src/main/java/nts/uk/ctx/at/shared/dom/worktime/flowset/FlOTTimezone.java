/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.SettlementOrder;

/**
 * The Class FlowOTTimezone.
 */
// 流動残業時間帯
@Getter
public class FlOTTimezone extends DomainObject {

	/** The worktime no. */
	// 就業時間帯NO
	private Integer worktimeNo;

	/** The restrict time. */
	// 拘束時間として扱う
	private boolean restrictTime;

	/** The OT frame no. */
	// 残業枠NO
	private OvertimeWorkFrameNo oTFrameNo;

	/** The flow time setting. */
	// 流動時間設定
	private FlTimeSetting flowTimeSetting;

	/** The in legal OT frame no. */
	// 法定内残業枠NO
	private OvertimeWorkFrameNo inLegalOTFrameNo;

	/** The settlement order. */
	// 精算順序
	private SettlementOrder settlementOrder;

	/**
	 * Instantiates a new flow OT timezone.
	 *
	 * @param memento
	 *            the memento
	 */
	public FlOTTimezone(FlOTTimezoneGetMemento memento) {
		this.worktimeNo = memento.getWorktimeNo();
		this.restrictTime = memento.getRestrictTime();
		this.oTFrameNo = memento.getOTFrameNo();
		this.flowTimeSetting = memento.getFlowTimeSetting();
		this.inLegalOTFrameNo = memento.getInLegalOTFrameNo();
		this.settlementOrder = memento.getSettlementOrder();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(FlOTTimezoneSetMemento memento) {
		memento.setWorktimeNo(this.worktimeNo);
		memento.setRestrictTime(this.restrictTime);
		memento.setOTFrameNo(this.oTFrameNo);
		memento.setFlowTimeSetting(this.flowTimeSetting);
		memento.setInLegalOTFrameNo(this.inLegalOTFrameNo);
		memento.setSettlementOrder(this.settlementOrder);
	}
}
