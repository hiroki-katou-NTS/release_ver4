package nts.uk.ctx.at.record.dom.standardtime.repository;

import java.util.List;

import nts.uk.ctx.at.shared.dom.standardtime.AgreementTimeOfWorkPlace;
import nts.uk.ctx.at.shared.dom.standardtime.BasicAgreementSetting;

public interface AgreementTimeOfWorkPlaceDomainService {
	
	List<String> add(AgreementTimeOfWorkPlace agreementTimeOfWorkPlace, BasicAgreementSetting basicAgreementSetting);
	
	List<String> update(BasicAgreementSetting basicAgreementSetting, AgreementTimeOfWorkPlace agreementTimeOfWorkPlace);
	
	void remove(String basicSettingId, String workPlaceId, int laborSystemAtr);

}
