package nts.uk.ctx.at.shared.dom.adapter.workplace.config;

import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface WorkplaceConfigAdapter {
	Optional<WorkPlaceConfigImport> findByBaseDate(String companyId, GeneralDate baseDate);
}
