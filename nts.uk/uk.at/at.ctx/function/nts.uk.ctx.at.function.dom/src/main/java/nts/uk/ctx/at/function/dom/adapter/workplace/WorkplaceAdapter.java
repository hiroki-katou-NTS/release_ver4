package nts.uk.ctx.at.function.dom.adapter.workplace;

import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface WorkplaceAdapter {
	Optional<WorkplaceImport> getWorlkplaceHistory(String employeeId, GeneralDate baseDate);
}
