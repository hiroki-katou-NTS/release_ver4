package nts.uk.ctx.bs.employee.pubimp.workplace.config;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.pub.workplace.config.WorkPlaceConfigExport;
import nts.uk.ctx.bs.employee.pub.workplace.config.WorkPlaceConfigPub;
import nts.uk.ctx.bs.employee.pub.workplace.config.WorkplaceConfigHistoryExport;

@Stateless
public class WorkplaceConfigPubImp implements WorkPlaceConfigPub {
	
	
	@Inject
	private WorkplaceConfigRepository configRepo;

	@Override
	public Optional<WorkPlaceConfigExport> findByBaseDate(String companyId, GeneralDate baseDate) {
		return this.configRepo.findByBaseDate(companyId, baseDate).map(x -> {
			List<WorkplaceConfigHistoryExport> wkpConfigHistory = x.getWkpConfigHistory().stream()
					.map(his -> new WorkplaceConfigHistoryExport(his.identifier(), his.span()))
					.collect(Collectors.toList());
			return new WorkPlaceConfigExport(x.getCompanyId(), wkpConfigHistory);
		});
	}

}
