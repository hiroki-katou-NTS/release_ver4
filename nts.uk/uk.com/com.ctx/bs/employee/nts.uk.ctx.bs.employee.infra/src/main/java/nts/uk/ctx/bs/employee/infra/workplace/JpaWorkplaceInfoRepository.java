/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.workplace;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
@Stateless
public class JpaWorkplaceInfoRepository implements WorkplaceInfoRepository {

	@Override
	public Optional<WorkplaceInfo> find(String companyId, String wkpId, String historyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(WorkplaceInfo workplaceInfo) {
		// TODO Auto-generated method stub
		
	}

}
