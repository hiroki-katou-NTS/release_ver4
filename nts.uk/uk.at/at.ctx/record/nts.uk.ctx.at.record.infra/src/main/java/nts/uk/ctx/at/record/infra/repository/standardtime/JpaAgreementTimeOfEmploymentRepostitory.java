package nts.uk.ctx.at.record.infra.repository.standardtime;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfEmployment;
import nts.uk.ctx.at.record.dom.standardtime.enums.LaborSystemtAtr;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfEmploymentRepostitory;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementTimeEmployment;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementTimeEmploymentPK;

@Stateless
public class JpaAgreementTimeOfEmploymentRepostitory extends JpaRepository
		implements AgreementTimeOfEmploymentRepostitory {

	private static final String DELETE_BY_TWO_KEYS;

	private static final String FIND;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KmkmtAgeementTimeEmployment a ");
		builderString.append("WHERE a.kmkmtAgeementTimeEmploymentPK.companyId = :companyId ");
		builderString.append("AND a.kmkmtAgeementTimeEmploymentPK.employmentCategoryCode = :employmentCategoryCode ");
		builderString.append("AND a.laborSystemAtr = :laborSystemAtr ");
		DELETE_BY_TWO_KEYS = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgeementTimeEmployment a ");
		builderString.append("WHERE a.kmkmtAgeementTimeEmploymentPK.companyId = :companyId ");
		builderString.append("AND a.laborSystemAtr = :laborSystemAtr ");
		FIND = builderString.toString();
	}

	@Override
	public void add(AgreementTimeOfEmployment agreementTimeOfEmployment) {
		this.commandProxy().insert(toEntity(agreementTimeOfEmployment));
	}

	@Override
	public void remove(String companyId, String employmentCategoryCode, LaborSystemtAtr laborSystemAtr) {
		this.getEntityManager().createQuery(DELETE_BY_TWO_KEYS).setParameter("companyId", companyId)
				.setParameter("employmentCategoryCode", employmentCategoryCode)
				.setParameter("laborSystemAtr", laborSystemAtr.value).executeUpdate();
	}

	@Override
	public List<AgreementTimeOfEmployment> find(String companyId, LaborSystemtAtr laborSystemAtr) {
		return this.queryProxy().query(FIND, KmkmtAgeementTimeEmployment.class).setParameter("companyId", companyId)
				.setParameter("laborSystemAtr", laborSystemAtr.value).getList(f -> toDomain(f));
	}

	private KmkmtAgeementTimeEmployment toEntity(AgreementTimeOfEmployment agreementTimeOfEmployment) {
		val entity = new KmkmtAgeementTimeEmployment();

		entity.kmkmtAgeementTimeEmploymentPK = new KmkmtAgeementTimeEmploymentPK();
		entity.kmkmtAgeementTimeEmploymentPK.companyId = agreementTimeOfEmployment.getCompanyId();
		entity.kmkmtAgeementTimeEmploymentPK.employmentCategoryCode = agreementTimeOfEmployment
				.getEmploymentCategoryCode();
		entity.kmkmtAgeementTimeEmploymentPK.basicSettingId = agreementTimeOfEmployment.getBasicSettingId();
		entity.laborSystemAtr = new BigDecimal(agreementTimeOfEmployment.getLaborSystemAtr().value);

		return entity;
	}

	private static AgreementTimeOfEmployment toDomain(KmkmtAgeementTimeEmployment kmkmtAgeementTimeEmployment) {
		AgreementTimeOfEmployment agreementTimeOfEmployment = AgreementTimeOfEmployment.createJavaType(
				kmkmtAgeementTimeEmployment.kmkmtAgeementTimeEmploymentPK.companyId,
				kmkmtAgeementTimeEmployment.kmkmtAgeementTimeEmploymentPK.basicSettingId, 
				kmkmtAgeementTimeEmployment.laborSystemAtr,
				kmkmtAgeementTimeEmployment.kmkmtAgeementTimeEmploymentPK.employmentCategoryCode);
		
		return agreementTimeOfEmployment;
	}
}
