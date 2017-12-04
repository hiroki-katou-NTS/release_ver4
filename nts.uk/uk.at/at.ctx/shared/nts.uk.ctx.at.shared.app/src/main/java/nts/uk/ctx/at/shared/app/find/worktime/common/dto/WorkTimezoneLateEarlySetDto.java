/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.common.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneLateEarlyCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneLateEarlyCommonSetSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.common.OtherEmTimezoneLateEarlySet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneLateEarlySetSetMemento;

/**
 * The Class WorkTimezoneLateEarlySetDto.
 */
@Getter
@Setter
public class WorkTimezoneLateEarlySetDto implements WorkTimezoneLateEarlySetSetMemento{

	/** The common set. */
	private EmTimezoneLateEarlyCommonSetDto commonSet;
	
	/** The other class set. */
	private List<OtherEmTimezoneLateEarlySetDto> otherClassSet;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.
	 * WorkTimezoneLateEarlySetSetMemento#setCommonSet(nts.uk.ctx.at.shared.dom.
	 * worktime.common.EmTimezoneLateEarlyCommonSet)
	 */
	@Override
	public void setCommonSet(EmTimezoneLateEarlyCommonSet set) {
		set.saveToMemento(this.commonSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.
	 * WorkTimezoneLateEarlySetSetMemento#setOtherClassSet(java.util.List)
	 */
	@Override
	public void setOtherClassSet(List<OtherEmTimezoneLateEarlySet> list) {
		this.otherClassSet = list.stream().map(domain->{
			OtherEmTimezoneLateEarlySetDto dto =new OtherEmTimezoneLateEarlySetDto();
			domain.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
		
	}

	
	
}
