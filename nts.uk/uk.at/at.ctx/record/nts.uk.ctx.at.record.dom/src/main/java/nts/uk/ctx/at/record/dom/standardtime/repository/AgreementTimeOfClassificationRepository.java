package nts.uk.ctx.at.record.dom.standardtime.repository;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfClassification;
import nts.uk.ctx.at.record.dom.standardtime.enums.LaborSystemtAtr;

public interface AgreementTimeOfClassificationRepository {

	void add(AgreementTimeOfClassification agreementTimeOfClassification);
	
	void remove(String companyId, LaborSystemtAtr laborSystemAtr, String classificationCode);
	
	List<AgreementTimeOfClassification> find(String companyId, LaborSystemtAtr laborSystemAtr);
	
	List<String> findClassificationSetting(String companyId, LaborSystemtAtr laborSystemAtr);
	
	Optional<String> findEmploymentBasicSettingID(String companyId, LaborSystemtAtr laborSystemAtr, String classificationCode);
	
	List<AgreementTimeOfClassification> find(String companyId, List<String> classificationCode);
}
