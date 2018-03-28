/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class WorkTimezoneLateEarlySet.
 */
//就業時間帯の遅刻・早退設定
@Getter
public class WorkTimezoneLateEarlySet extends WorkTimeDomainObject {

	/** The common set. */
	//共通設定
	private EmTimezoneLateEarlyCommonSet commonSet;
	
	/** The other class set. */
	//区分別設定
	private List<OtherEmTimezoneLateEarlySet> otherClassSets;

	/**
	 * Instantiates a new work timezone late early set.
	 *
	 * @param memento the memento
	 */
	public WorkTimezoneLateEarlySet(WorkTimezoneLateEarlySetGetMemento memento) {
		this.commonSet = memento.getCommonSet();
		this.otherClassSets = memento.getOtherClassSet();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WorkTimezoneLateEarlySetSetMemento memento) {
		memento.setCommonSet(this.commonSet);
		memento.setOtherClassSet(this.otherClassSets);
	}
	
	/**
	 * 
	 * @return
	 */
	public OtherEmTimezoneLateEarlySet getOtherEmTimezoneLateEarlySet(LateEarlyAtr lateEarlyAtr) {
		if(lateEarlyAtr.isLATE()) {
			return this.otherClassSets.stream().filter(t -> t.getLateEarlyAtr().isLATE()).collect(Collectors.toList()).get(0);
		}else {
			return this.otherClassSets.stream().filter(t -> t.getLateEarlyAtr().isEARLY()).collect(Collectors.toList()).get(0);
		}
	}
	
}
