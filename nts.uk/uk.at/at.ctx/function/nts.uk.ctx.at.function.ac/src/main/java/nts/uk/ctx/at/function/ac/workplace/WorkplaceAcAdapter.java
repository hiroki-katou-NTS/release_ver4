package nts.uk.ctx.at.function.ac.workplace;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceIdName;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceImport;
import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.ctx.bs.employee.pub.workplace.WorkPlaceInfoExport;

@Stateless
public class WorkplaceAcAdapter implements WorkplaceAdapter {

	@Inject
	private SyWorkplacePub workplacePub;

	@Override
	public Optional<WorkplaceImport> getWorlkplaceHistory(String employeeId, GeneralDate baseDate) {

		Optional<SWkpHistExport> workplaceHistOpt = workplacePub.findBySid(employeeId, baseDate);
		if (!workplaceHistOpt.isPresent())
			return Optional.empty();
		SWkpHistExport wp = workplaceHistOpt.get();
		return Optional.of(WorkplaceImport.builder().dateRange(wp.getDateRange()).employeeId(wp.getEmployeeId())
				.wkpDisplayName(wp.getWkpDisplayName()).workplaceCode(wp.getWorkplaceCode())
				.workplaceId(wp.getWorkplaceId()).workplaceName(wp.getWorkplaceName()).build());
	}

	@Override
	public List<WorkplaceIdName> findWkpByWkpId(String companyId, GeneralDate baseDate, List<String> wkpIds) {
		List<WorkPlaceInfoExport> listWorkPlaceInfoExport = workplacePub.findWkpByWkpId(companyId, baseDate, wkpIds);
		return listWorkPlaceInfoExport.stream().map(e -> new WorkplaceIdName(e.getWorkplaceId(), e.getWorkPlaceName()))
				.collect(Collectors.toList());
	}

}
