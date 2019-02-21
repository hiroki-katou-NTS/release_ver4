/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.ac.find.workplace;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.workplace.AffAtWorkplaceExport;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.ctx.bs.employee.pub.workplace.WorkPlaceInfoExport;
import nts.uk.ctx.sys.gateway.dom.adapter.syworkplace.GwSyWorkplaceAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.syworkplace.SWkpHistImport;

/**
 * The Class SyWorkplaceAdapterImpl.
 */
@Stateless
public class GwSyWorkplaceAdapterImpl implements GwSyWorkplaceAdapter{
	
	/** The sy workplace pub. */
	@Inject
	private SyWorkplacePub syWorkplacePub;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.adapter.syworkplace.SyWorkplaceAdapter#findBySid(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<SWkpHistImport> findBySid(String companyId, String employeeId, GeneralDate baseDate) {
		List<AffAtWorkplaceExport> lstWkp = this.syWorkplacePub.findBySIdAndBaseDate(Arrays.asList(employeeId), baseDate);
		List<String> listWkpId = lstWkp.stream().map(item->{
			return item.getWorkplaceId();
		}).collect(Collectors.toList());
		List<WorkPlaceInfoExport> lstInfoExport = this.syWorkplacePub.findWkpByWkpId(companyId,baseDate,listWkpId);
		if (!lstInfoExport.isEmpty()) {
			return lstInfoExport.stream().map(wkp -> {
				return new SWkpHistImport(wkp.getWorkplaceId(), wkp.getWorkPlaceName());
			}).collect(Collectors.toList());
		} else {
			return Arrays.asList();
		}
	}
	
}
