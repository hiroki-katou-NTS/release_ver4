/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * The Class FlowRestTimezone.
 */
// 流動休憩時間帯
@Getter
public class FlowRestTimezone extends DomainObject {

	/** The flow rest sets. */
	// 流動休憩設定
	private List<FlowRestSetting> flowRestSets;

	/** The use here after rest set. */
	// 設定以降の休憩を使用する
	private boolean useHereAfterRestSet;

	/** The here after rest set. */
	// 設定以降の休憩設定
	private FlowRestSetting hereAfterRestSet;

	/**
	 * Instantiates a new flow rest timezone.
	 *
	 * @param memento the memento
	 */
	public FlowRestTimezone(FlowRestTimezoneGetMemento memento) {
		this.flowRestSets = memento.getFlowRestSet();
		this.useHereAfterRestSet = memento.getUseHereAfterRestSet();
		this.hereAfterRestSet = memento.getHereAfterRestSet();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(FlowRestTimezoneSetMemento memento) {
		memento.setFlowRestSet(this.flowRestSets);
		memento.setUseHereAfterRestSet(this.useHereAfterRestSet);
		memento.setHereAfterRestSet(this.hereAfterRestSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		super.validate();
		this.validateAfterRestSetting();
		this.validateOverlapFlowRestSets();
	}

	/**
	 * Validate after rest setting.
	 */
	private void validateAfterRestSetting() {
		// Validate hereAfterRestSet.flowPassageTime > 0 when useHereAfterRestSet is true
		if (this.useHereAfterRestSet) {
			if (this.hereAfterRestSet.getFlowPassageTime().lessThanOrEqualTo(0)) {
				throw new BusinessException("Msg_871");
			}
		}
	}

	/**
	 * Validate overlap flow rest sets.
	 */
	private void validateOverlapFlowRestSets() {
		// Validate flowRestSets.flowPassageTime must not be duplicated
		Set<AttendanceTime> setFlowPassageTime = this.flowRestSets.stream()
				.map(flowRestSet -> flowRestSet.getFlowPassageTime())
				.collect(Collectors.toSet());
		// If Set size < List size => there're duplicated value
		if (setFlowPassageTime.size() < this.flowRestSets.size()) {
			throw new BusinessException("Msg_869");
		}
	}
}
