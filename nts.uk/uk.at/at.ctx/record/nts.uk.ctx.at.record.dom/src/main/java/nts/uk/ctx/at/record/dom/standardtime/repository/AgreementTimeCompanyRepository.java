package nts.uk.ctx.at.record.dom.standardtime.repository;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfCompany;
import nts.uk.ctx.at.record.dom.standardtime.enums.LaborSystemtAtr;

public interface AgreementTimeCompanyRepository {

	void add(AgreementTimeOfCompany agreementTimeOfCompany);
	
	Optional<AgreementTimeOfCompany> find(String companyId, LaborSystemtAtr laborSystemAtr);
	
	List<AgreementTimeOfCompany> find(String companyId);
	
}
