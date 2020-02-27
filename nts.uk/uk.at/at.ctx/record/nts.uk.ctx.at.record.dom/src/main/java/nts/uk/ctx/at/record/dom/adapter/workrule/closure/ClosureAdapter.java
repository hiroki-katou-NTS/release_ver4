package nts.uk.ctx.at.record.dom.adapter.workrule.closure;

import java.util.Optional;

public interface ClosureAdapter {
	Optional<PresentClosingPeriodImport> findByClosureId(String cId, int closureId);
}
