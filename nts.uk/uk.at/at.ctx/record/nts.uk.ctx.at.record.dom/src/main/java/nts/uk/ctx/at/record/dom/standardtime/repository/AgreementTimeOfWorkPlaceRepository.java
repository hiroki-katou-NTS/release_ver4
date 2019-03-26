package nts.uk.ctx.at.record.dom.standardtime.repository;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfWorkPlace;
import nts.uk.ctx.at.record.dom.standardtime.enums.LaborSystemtAtr;

public interface AgreementTimeOfWorkPlaceRepository {
	
	void add(AgreementTimeOfWorkPlace agreementTimeOfWorkPlace);
	
	void remove(String workplaceId, LaborSystemtAtr laborSystemAtr);
	
	Optional<String> find(String workplaceId , LaborSystemtAtr laborSystemAtr);
	
	List<String> findWorkPlaceSetting(LaborSystemtAtr laborSystemAtr);
	
	List<AgreementTimeOfWorkPlace> findWorkPlaceSetting(List<String> workplaceId);
}
