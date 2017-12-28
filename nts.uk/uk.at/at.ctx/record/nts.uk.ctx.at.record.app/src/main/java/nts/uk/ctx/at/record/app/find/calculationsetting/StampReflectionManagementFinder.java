package nts.uk.ctx.at.record.app.find.calculationsetting;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.calculationsetting.StampReflectionManagement;
import nts.uk.ctx.at.record.dom.calculationsetting.repository.StampReflectionManagementRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class StampReflectionManagementFinder {

	@Inject
	private StampReflectionManagementRepository repository;

	public StampReflectionManagementDto findByCode() {
		String companyId = AppContexts.user().companyId();
		Optional<StampReflectionManagement> optional = repository.findByCid(companyId);
		if (!optional.isPresent()) {
			return null;
		}
		StampReflectionManagementDto managementDto = new StampReflectionManagementDto(optional.get().getCompanyId(),
				optional.get().getBreakSwitchClass().value, optional.get().getAutoStampReflectionClass().value,
				optional.get().getActualStampOfPriorityClass().value, optional.get().getReflectWorkingTimeClass().value,
				optional.get().getGoBackOutCorrectionClass().value, optional.get().getManagementOfEntrance().value,
				optional.get().getAutoStampForFutureDayClass().value, optional.get().getOutingAtr().value,
				optional.get().getMaxUseCount());
		return managementDto;
	}

}
