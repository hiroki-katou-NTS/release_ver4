package nts.uk.ctx.at.shared.ac.workplace.config;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.WorkPlaceConfigImport;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.WorkplaceConfigAdapter;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.WorkplaceConfigHistoryImport;
import nts.uk.ctx.bs.employee.pub.workplace.config.WorkPlaceConfigExport;
import nts.uk.ctx.bs.employee.pub.workplace.config.WorkPlaceConfigPub;

@Stateless
public class WorkplaceConfigAdapterImpl implements WorkplaceConfigAdapter {
	
	@Inject
	private WorkPlaceConfigPub wpConfigPub;

	@Override
	public Optional<WorkPlaceConfigImport> findByBaseDate(String companyId, GeneralDate baseDate) {

		Optional<WorkPlaceConfigExport> wpconfigOpt = this.wpConfigPub.findByBaseDate(companyId, baseDate);
		if (wpconfigOpt.isPresent()) {
			WorkPlaceConfigExport wpConfig = wpconfigOpt.get();
			List<WorkplaceConfigHistoryImport> wkpConfigHistory = wpConfig.getWkpConfigHistory().stream()
					.map(x -> new WorkplaceConfigHistoryImport(x.getHistoryId(), x.getPeriod()))
					.collect(Collectors.toList());

			return Optional.of(new WorkPlaceConfigImport(wpConfig.getCompanyId(), wkpConfigHistory));
		}

		return Optional.empty();
	}

}
