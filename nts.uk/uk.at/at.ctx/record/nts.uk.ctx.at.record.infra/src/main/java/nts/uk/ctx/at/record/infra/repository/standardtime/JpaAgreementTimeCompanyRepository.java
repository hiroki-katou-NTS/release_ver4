package nts.uk.ctx.at.record.infra.repository.standardtime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfCompany;
import nts.uk.ctx.at.record.dom.standardtime.enums.LaborSystemtAtr;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeCompanyRepository;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementTimeCompany;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementTimeCompanyPK;

@Stateless
public class JpaAgreementTimeCompanyRepository extends JpaRepository implements AgreementTimeCompanyRepository {

	private static final String FIND;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgeementTimeCompany a ");
		builderString.append("WHERE a.kmkmtAgeementTimeCompanyPK.companyId = :companyId ");
		builderString.append("AND a.laborSystemAtr = :laborSystemAtr ");
		FIND = builderString.toString();
	}

	@Override
	public Optional<AgreementTimeOfCompany> find(String companyId, LaborSystemtAtr laborSystemAtr) {
		return this.queryProxy().query(FIND, KmkmtAgeementTimeCompany.class).setParameter("companyId", companyId)
				.setParameter("laborSystemAtr", laborSystemAtr.value).getSingle(f -> toDomain(f));
	}
	
	@Override
	public List<AgreementTimeOfCompany> find(String companyId) {
		String query = "SELECT a FROM KmkmtAgeementTimeCompany a WHERE a.kmkmtAgeementTimeCompanyPK.companyId = :companyId ";
		
		return this.queryProxy().query(query, KmkmtAgeementTimeCompany.class).setParameter("companyId", companyId).getList(f -> toDomain(f));
	}

	@Override
	public void add(AgreementTimeOfCompany agreementTimeOfCompany) {
		this.commandProxy().insert(toEntity(agreementTimeOfCompany));
	}

	private KmkmtAgeementTimeCompany toEntity(AgreementTimeOfCompany agreementTimeOfCompany) {
		val entity = new KmkmtAgeementTimeCompany();

		entity.kmkmtAgeementTimeCompanyPK = new KmkmtAgeementTimeCompanyPK();
		entity.kmkmtAgeementTimeCompanyPK.companyId = agreementTimeOfCompany.getCompanyId();
		entity.kmkmtAgeementTimeCompanyPK.basicSettingId = agreementTimeOfCompany.getBasicSettingId();
		entity.laborSystemAtr = new BigDecimal(agreementTimeOfCompany.getLaborSystemAtr().value);

		return entity;
	}

	private static AgreementTimeOfCompany toDomain(KmkmtAgeementTimeCompany kmkmtAgeementTimeCompany) {
		AgreementTimeOfCompany agreementTimeOfCompany = AgreementTimeOfCompany.createFromJavaType(
				kmkmtAgeementTimeCompany.kmkmtAgeementTimeCompanyPK.companyId,
				kmkmtAgeementTimeCompany.kmkmtAgeementTimeCompanyPK.basicSettingId,
				kmkmtAgeementTimeCompany.laborSystemAtr);
		return agreementTimeOfCompany;
	}

}
