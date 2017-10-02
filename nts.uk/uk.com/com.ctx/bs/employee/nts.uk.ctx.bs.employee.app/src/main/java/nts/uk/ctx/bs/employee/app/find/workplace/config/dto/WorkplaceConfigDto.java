/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.find.workplace.config.dto;

import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistory;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigSetMemento;

public class WorkplaceConfigDto implements WorkplaceConfigSetMemento {

	/** The company id. */
	//会社ID
	public String companyId;

	/** The wkp config history. */
	//履歴
	public List<WorkplaceConfigHistoryDto> wkpConfigHistory;

	@Override
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Override
	public void setWkpConfigHistory(List<WorkplaceConfigHistory> wkpConfigHistory) {
		this.wkpConfigHistory = wkpConfigHistory.stream().map(item->{
			WorkplaceConfigHistoryDto wkpConfigDto = new WorkplaceConfigHistoryDto();
			item.saveToMemento(wkpConfigDto);
			return wkpConfigDto;
		}).collect(Collectors.toList());
	}

}
