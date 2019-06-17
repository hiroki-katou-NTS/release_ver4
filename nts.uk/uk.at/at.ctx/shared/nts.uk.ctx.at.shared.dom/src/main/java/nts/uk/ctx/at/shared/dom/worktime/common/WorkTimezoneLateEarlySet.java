/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class WorkTimezoneLateEarlySet.
 */
//就業時間帯の遅刻・早退設定
@Getter
@NoArgsConstructor
public class WorkTimezoneLateEarlySet extends WorkTimeDomainObject implements Cloneable{

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
	
	public WorkTimezoneLateEarlySet(EmTimezoneLateEarlyCommonSet commonSet,List<OtherEmTimezoneLateEarlySet> otherClassSets) {
		this.commonSet = commonSet;
		this.otherClassSets = otherClassSets;
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
	
	/**
	 * 共通設定をtrueに変更した「就業時間帯の遅刻・早退設定」を返す
	 * @return
	 */
	public WorkTimezoneLateEarlySet changeCommonSet(boolean flag) {
		return new WorkTimezoneLateEarlySet(new EmTimezoneLateEarlyCommonSet(flag),this.otherClassSets);
	}
	
	@Override
	public WorkTimezoneLateEarlySet clone() {
		WorkTimezoneLateEarlySet cloned = new WorkTimezoneLateEarlySet();
		try {
			cloned.commonSet = this.commonSet.clone();
			cloned.otherClassSets = this.otherClassSets.stream().map(c -> c.clone()).collect(Collectors.toList());
		}
		catch (Exception e){
			throw new RuntimeException("WorkTimezoneLateEarlySet clone error.");
		}
		return cloned;
	}
}
