/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class OverTimeOfTimeZoneSet.
 */
// 残業時間の時間帯設定
@Getter
@Setter
@NoArgsConstructor
public class OverTimeOfTimeZoneSet extends WorkTimeDomainObject implements Cloneable{

	/** The work timezone no. */
	// 就業時間帯NO
	private EmTimezoneNo workTimezoneNo;

	/** The restraint time use. */
	// 拘束時間として扱う
	private boolean restraintTimeUse;

	/** The early OT use. */
	// 早出残業として扱う
	private boolean earlyOTUse;

	/** The timezone. */
	// 時間帯
	private TimeZoneRounding timezone;

	/** The ot frame no. */
	// 残業枠NO
	private OTFrameNo otFrameNo;

	/** The legal O tframe no. */
	// 法定内残業枠NO
	private OTFrameNo legalOTframeNo;

	/** The settlement order. */
	// 精算順序
	private SettlementOrder settlementOrder;
	
	
	/**
	 * Instantiates a new over time of time zone set.
	 *
	 * @param memento the memento
	 */
	public OverTimeOfTimeZoneSet(OverTimeOfTimeZoneSetGetMemento memento) {
		this.workTimezoneNo = memento.getWorkTimezoneNo();
		this.restraintTimeUse = memento.getRestraintTimeUse();
		this.earlyOTUse = memento.getEarlyOTUse();
		this.timezone = memento.getTimezone();
		this.otFrameNo = memento.getOTFrameNo();
		if (memento.getLegalOTframeNo() == null || memento.getLegalOTframeNo().v() == null) {
			this.legalOTframeNo = OTFrameNo.getDefaultData();
		} else {
			this.legalOTframeNo = memento.getLegalOTframeNo();
		}
		if (memento.getSettlementOrder() == null || memento.getSettlementOrder().v() == null) {
			this.settlementOrder = SettlementOrder.getDefaultData();
		} else {
			this.settlementOrder = memento.getSettlementOrder();
		}
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(OverTimeOfTimeZoneSetSetMemento memento){
		memento.setWorkTimezoneNo(this.workTimezoneNo);
		memento.setRestraintTimeUse(this.restraintTimeUse);
		memento.setEarlyOTUse(this.earlyOTUse);
		memento.setTimezone(this.timezone);
		memento.setOTFrameNo(this.otFrameNo);
		memento.setLegalOTframeNo(this.legalOTframeNo);
		memento.setSettlementOrder(this.settlementOrder);
	}
	
	/**
	 * Restore data.
	 *
	 * @param other the other
	 */
	public void restoreData(OverTimeOfTimeZoneSet other) {
		this.workTimezoneNo = other.getWorkTimezoneNo();
		this.restraintTimeUse = other.isRestraintTimeUse();
		this.earlyOTUse = other.isEarlyOTUse();
		this.timezone = other.getTimezone();
		this.otFrameNo = other.getOtFrameNo();
		this.legalOTframeNo = other.getLegalOTframeNo();
		this.settlementOrder = other.getSettlementOrder();
	}

	/**
	 * Correct default data.
	 */
	public void correctDefaultData() {
		this.settlementOrder = null;
		this.legalOTframeNo = null;
	}
	
	@Override
	public OverTimeOfTimeZoneSet clone() {
		OverTimeOfTimeZoneSet cloned = new OverTimeOfTimeZoneSet();
		try {
			cloned.workTimezoneNo = new EmTimezoneNo(this.workTimezoneNo.v());
			cloned.restraintTimeUse = this.restraintTimeUse ? true : false;
			cloned.earlyOTUse = this.earlyOTUse ? true : false ;
			cloned.otFrameNo = new OTFrameNo(this.otFrameNo.v());
			cloned.timezone = this.timezone.clone();
			cloned.legalOTframeNo = new OTFrameNo(this.legalOTframeNo.v());
			cloned.settlementOrder = new SettlementOrder(this.settlementOrder.v());
		}
		catch (Exception e){
			throw new RuntimeException("OverTimeOfTimeZoneSet clone error.");
		}
		return cloned;
	}
}
