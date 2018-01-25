/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flowset;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class FlowRestTimezone.
 */
// 流動休憩時間帯
@Getter
public class FlowRestTimezone extends WorkTimeDomainObject {

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
	 * Constructor
	 * 
	 * @param flowRestSets
	 *            The flow rest sets.
	 * @param useHereAfterRestSet
	 *            The use here after rest set.
	 * @param hereAfterRestSet
	 *            The here after rest set.
	 */
	public FlowRestTimezone(List<FlowRestSetting> flowRestSets, boolean useHereAfterRestSet,
			FlowRestSetting hereAfterRestSet) {
		super();
		this.flowRestSets = flowRestSets;
		this.useHereAfterRestSet = useHereAfterRestSet;
		this.hereAfterRestSet = hereAfterRestSet;
	}

	/**
	 * Instantiates a new flow rest timezone.
	 *
	 * @param memento
	 *            the memento
	 */
	public FlowRestTimezone(FlowRestTimezoneGetMemento memento) {
		this.flowRestSets = memento.getFlowRestSet();
		this.useHereAfterRestSet = memento.getUseHereAfterRestSet();
		this.hereAfterRestSet = memento.getHereAfterRestSet();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
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
		this.validateAfterRestSetting();
		this.validateOverlapFlowRestSets();
		super.validate();
	}

	/**
	 * Validate after rest setting.
	 */
	private void validateAfterRestSetting() {
		// Validate hereAfterRestSet.flowPassageTime > 0 when
		// useHereAfterRestSet is true
		if (this.useHereAfterRestSet) {
			if (this.hereAfterRestSet.getFlowPassageTime().lessThanOrEqualTo(0)) {
				this.bundledBusinessExceptions.addMessage("Msg_871");
			}
		}
	}

	/**
	 * Validate overlap flow rest sets.
	 */
	private void validateOverlapFlowRestSets() {
		// Validate flowRestSets.flowPassageTime must not be duplicated
		Set<AttendanceTime> setFlowPassageTime = this.flowRestSets.stream()
				.map(flowRestSet -> flowRestSet.getFlowPassageTime()).collect(Collectors.toSet());
		// If Set size < List size => there're duplicated value
		if (setFlowPassageTime.size() < this.flowRestSets.size()) {
			this.bundledBusinessExceptions.addMessage("Msg_869");
		}
	}

}
