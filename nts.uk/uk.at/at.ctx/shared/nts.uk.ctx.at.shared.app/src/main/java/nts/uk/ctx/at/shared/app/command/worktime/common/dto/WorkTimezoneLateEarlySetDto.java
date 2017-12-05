/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.worktime.common.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Value;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneLateEarlyCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.OtherEmTimezoneLateEarlySet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneLateEarlySetGetMemento;

/**
 * The Class WorkTimezoneLateEarlySetDto.
 */
@Value
public class WorkTimezoneLateEarlySetDto implements WorkTimezoneLateEarlySetGetMemento {

	/** The common set. */
	private EmTimezoneLateEarlyCommonSetDto commonSet;

	/** The other class set. */
	private List<OtherEmTimezoneLateEarlySetDto> otherClassSet;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.
	 * WorkTimezoneLateEarlySetGetMemento#getCommonSet()
	 */
	@Override
	public EmTimezoneLateEarlyCommonSet getCommonSet() {
		return new EmTimezoneLateEarlyCommonSet(this.commonSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.
	 * WorkTimezoneLateEarlySetGetMemento#getOtherClassSet()
	 */
	@Override
	public List<OtherEmTimezoneLateEarlySet> getOtherClassSet() {
		return this.otherClassSet.stream().map(item -> new OtherEmTimezoneLateEarlySet(item))
				.collect(Collectors.toList());
	}
}
