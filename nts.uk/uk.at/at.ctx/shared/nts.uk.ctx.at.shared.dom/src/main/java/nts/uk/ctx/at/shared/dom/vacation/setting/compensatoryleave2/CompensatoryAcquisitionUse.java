/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave2;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.vacation.setting.ApplyPermission;
import nts.uk.ctx.at.shared.dom.vacation.setting.ExpirationTime;

/**
 * The Class CompensatoryAcquisitionUse.
 */
@Getter
public class CompensatoryAcquisitionUse extends DomainObject {
    
	//使用期限
	/** The expiration time. */
	private ExpirationTime expirationTime;
	
	//先取り許可
	/** The preemption permit. */
	private ApplyPermission preemptionPermit;
	
	/**
	 * Instantiates a new compensatory acquisition use.
	 *
	 * @param memento the memento
	 */
	public CompensatoryAcquisitionUse(CompensatoryAcquisitionUseGetMemento memento) {
		this.expirationTime = memento.getExpirationTime();
		this.preemptionPermit = memento.getPreemptionPermit();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(CompensatoryAcquisitionUseSetMemento memento) {
		memento.setExpirationTime(this.expirationTime);
		memento.setPreemptionPermit(this.preemptionPermit);
	}
}
