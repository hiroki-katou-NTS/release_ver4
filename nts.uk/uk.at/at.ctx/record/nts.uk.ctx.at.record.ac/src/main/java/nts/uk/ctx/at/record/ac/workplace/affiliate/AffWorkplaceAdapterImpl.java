/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.ac.workplace.affiliate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffAtWorkplaceImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkPlaceSidImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceDto;
//import nts.uk.ctx.bs.employee.pub.workplace.AffAtWorkplaceExport;
import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class AffWorkplaceAdapterImpl.
 */
@Stateless
public class AffWorkplaceAdapterImpl implements AffWorkplaceAdapter {

	/** The wkp pub. */
	@Inject
	private SyWorkplacePub wkpPub;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter#findBySid(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<AffWorkplaceDto> findBySid(String employeeId, GeneralDate baseDate) {

		Optional<SWkpHistExport> opSWkpHistExport = this.wkpPub.findBySid(employeeId, baseDate);
		
		AffWorkplaceDto affWorkplaceDto = new AffWorkplaceDto();
		
		if(opSWkpHistExport.isPresent()){
		affWorkplaceDto = AffWorkplaceDto.builder().workplaceId(opSWkpHistExport.get().getWorkplaceId())
				.workplaceCode(opSWkpHistExport.get().getWorkplaceCode())
				.workplaceName(opSWkpHistExport.get().getWorkplaceName()).build();
		}
		
		return Optional.ofNullable(affWorkplaceDto);
	}

	@Override
	public Optional<AffWorkPlaceSidImport> findBySidAndDate(String employeeId, GeneralDate baseDate) {
		Optional<SWkpHistExport> opSWkpHistExport = this.wkpPub.findBySid(employeeId, baseDate);
		AffWorkPlaceSidImport affWorkPlaceSidImport = new AffWorkPlaceSidImport();
		if(opSWkpHistExport.isPresent() && opSWkpHistExport != null){
			affWorkPlaceSidImport = new AffWorkPlaceSidImport(opSWkpHistExport.get().getDateRange(),
					opSWkpHistExport.get().getEmployeeId(),
					opSWkpHistExport.get().getWorkplaceId(),
					opSWkpHistExport.get().getWorkplaceCode(),
					opSWkpHistExport.get().getWorkplaceName(),
					opSWkpHistExport.get().getWkpDisplayName());
		} else {
			return Optional.empty();
		}
		
		return Optional.of(affWorkPlaceSidImport);
	}
	
	@Override
	public List<String> findAffiliatedWorkPlaceIdsToRoot(String companyId,String employeeId, GeneralDate baseDate) {
		return this.wkpPub.findWpkIdsBySid(companyId ,employeeId, baseDate);
	}
	
	@Override
	public Map<GeneralDate, Map<String, List<String>>> findAffiliatedWorkPlaceIdsToRoot(String companyId, List<String> employeeId, DatePeriod baseDate){
		return this.wkpPub.findWpkIdsBySids(companyId, employeeId, baseDate);
	}

	@Override
	public List<AffAtWorkplaceImport> findBySIdAndBaseDate(List<String> employeeIds, GeneralDate baseDate) {
		return this.wkpPub.findBySIdAndBaseDateV2(employeeIds, baseDate).stream().map(item -> {
			return new AffAtWorkplaceImport(item.getEmployeeId(), item.getWorkplaceId(), item.getHistoryID(), item.getNormalWorkplaceID());
		}).collect(Collectors.toList());
	}

	@Override
	public List<String> findParentWpkIdsByWkpId(String companyId, String workPlaceId, GeneralDate baseDate) {
		return this.wkpPub.findParentWpkIdsByWkpId(companyId, workPlaceId, baseDate);
	}

	@Override
	public List<DatePeriod> getLstPeriod(String companyId, DatePeriod period) {
		List<DatePeriod> datePeriods = this.wkpPub.getLstPeriod(companyId, period);
		return datePeriods;
	}
	
}
