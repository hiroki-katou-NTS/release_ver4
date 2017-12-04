/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.common.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezoneSetMemento;

/**
 * The Class FlowRestTimezoneDto.
 */

/**
 * Gets the here after rest set.
 *
 * @return the here after rest set
 */
@Getter
public class FlowRestTimezoneDto implements FlowRestTimezoneSetMemento {

	/** The flow rest set. */
	private List<FlowRestSettingDto> flowRestSet;

	/** The use here after rest set. */
	private boolean useHereAfterRestSet;

	/** The here after rest set. */
	private FlowRestSettingDto hereAfterRestSet;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezoneSetMemento#
	 * setFlowRestSet(java.util.List)
	 */
	@Override
	public void setFlowRestSet(List<FlowRestSetting> set) {
		this.flowRestSet = set.stream().map(item -> {
			FlowRestSettingDto dto = new FlowRestSettingDto();
			item.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezoneSetMemento#
	 * setUseHereAfterRestSet(boolean)
	 */
	@Override
	public void setUseHereAfterRestSet(boolean val) {
		this.useHereAfterRestSet = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezoneSetMemento#
	 * setHereAfterRestSet(nts.uk.ctx.at.shared.dom.worktime.common.
	 * FlowRestSetting)
	 */
	@Override
	public void setHereAfterRestSet(FlowRestSetting set) {
		set.saveToMemento(this.hereAfterRestSet);
	}
}
