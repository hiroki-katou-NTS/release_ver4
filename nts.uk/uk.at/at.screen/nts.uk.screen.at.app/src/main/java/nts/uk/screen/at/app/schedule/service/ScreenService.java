package nts.uk.screen.at.app.schedule.service;

import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface ScreenService {

	Optional<GeneralDate> getProcessingYM(String cId, int closureId);
}
