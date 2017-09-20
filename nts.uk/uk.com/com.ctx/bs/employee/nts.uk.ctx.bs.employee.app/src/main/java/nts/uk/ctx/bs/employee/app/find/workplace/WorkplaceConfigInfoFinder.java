/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.find.workplace;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.app.find.workplace.dto.WorkplaceConfigInfoDto;
import nts.uk.ctx.bs.employee.dom.workplace.configinfo.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.configinfo.WorkplaceConfigInfoRepository;

@Stateless
public class WorkplaceConfigInfoFinder {

	@Inject
	private WorkplaceConfigInfoRepository workplaceConfigInfoRepository;
	
	public List<WorkplaceConfigInfoDto> findAll(String companyId,String historyId){
		Optional<WorkplaceConfigInfo> workplaceConfigInfo = workplaceConfigInfoRepository.find(companyId, historyId);
		//TODO convert to tree data
		return null;
	}
}
