package nts.uk.ctx.pr.core.infra.repository.rule.law.tax.residential.input;

import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input.PersonResiTax;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input.PersonResiTaxRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.input.PprmtPersonResiTax;
import nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.input.PprmtPersonResiTaxPK;

@Stateless
public class JpaPersonResiTaxRepository extends JpaRepository implements PersonResiTaxRepository {
	private final String SEL_1 = "SELECT c FROM PprmtPersonResiTax c "
			+ "WHERE c.pprmtPersonResiTaxPK.companyCode = :CCD" + " AND c.pprmtPersonResiTaxPK.personId = :PID"
			+ " AND c.pprmtPersonResiTaxPK.yearKey = :Y_K";

	private final String SEL_5 = "SELECT c.pprmtPersonResiTaxPK.personId FROM PprmtPersonResiTax c "
			+ "WHERE c.pprmtPersonResiTaxPK.companyCode = :CCD" + " AND c.residenceCode = :RESIDENCE_CD"
			+ " AND c.pprmtPersonResiTaxPK.yearKey = :Y_K";

	private final String UPD_2 = "UPDATE PprmtPersonResiTax c SET c.residenceCode = :RESIDENCE_CD WHERE  c.pprmtPersonResiTaxPK.companyCode = :CCD"
			+ " AND c.pprmtPersonResiTaxPK.personId = :PID AND c.pprmtPersonResiTaxPK.yearKey = :Y_K";

	private PprmtPersonResiTax toEntity(PersonResiTax domain) {
		val entity = new PprmtPersonResiTax();

		entity.pprmtPersonResiTaxPK = new PprmtPersonResiTaxPK();
		entity.pprmtPersonResiTaxPK.companyCode = domain.getCompanyCode().v();
		entity.pprmtPersonResiTaxPK.yearKey = domain.getYearKey().v();
		entity.pprmtPersonResiTaxPK.personId = domain.getPersonId().v();
		entity.residenceTaxAve = domain.getResidenceTaxAve().v();
		entity.residenceTaxBn = domain.getResidenceTaxBn().v();
		entity.residenceTaxLevyAtr = domain.getResidenceTaxLevyAtr().value;
		entity.residenceTaxLumpAtr = domain.getResidenceTaxLumpAtr().value;
		entity.residenceTaxLumpYm = domain.getResidenceTaxLumpYm().v();
		entity.residenceCode = domain.getResidenceCode().v();
		entity.residenceTax01 = domain.getResidenceTax().get(0).getValue().v();
		entity.residenceTax02 = domain.getResidenceTax().get(1).getValue().v();
		entity.residenceTax03 = domain.getResidenceTax().get(2).getValue().v();
		entity.residenceTax04 = domain.getResidenceTax().get(3).getValue().v();
		entity.residenceTax05 = domain.getResidenceTax().get(4).getValue().v();
		entity.residenceTax06 = domain.getResidenceTax().get(5).getValue().v();
		entity.residenceTax07 = domain.getResidenceTax().get(6).getValue().v();
		entity.residenceTax08 = domain.getResidenceTax().get(7).getValue().v();
		entity.residenceTax09 = domain.getResidenceTax().get(8).getValue().v();
		entity.residenceTax10 = domain.getResidenceTax().get(9).getValue().v();
		entity.residenceTax11 = domain.getResidenceTax().get(10).getValue().v();
		entity.residenceTax12 = domain.getResidenceTax().get(11).getValue().v();
		return entity;
	}

	public static PersonResiTax toDomain(PprmtPersonResiTax entity) {
		PersonResiTax domain = PersonResiTax.createFromJavaType(entity.pprmtPersonResiTaxPK.companyCode,
				entity.pprmtPersonResiTaxPK.personId, entity.pprmtPersonResiTaxPK.yearKey, entity.residenceCode,
				entity.residenceTaxBn, entity.residenceTaxAve, entity.residenceTaxLumpAtr, entity.residenceTaxLumpYm,
				entity.residenceTaxLevyAtr);
		domain.createResidenceTaxFromJavaType(1, entity.residenceTax01, 2, entity.residenceTax02, 3,
				entity.residenceTax03, 4, entity.residenceTax04, 5, entity.residenceTax05, 6, entity.residenceTax06, 7,
				entity.residenceTax07, 8, entity.residenceTax08, 9, entity.residenceTax09, 10, entity.residenceTax10,
				11, entity.residenceTax11, 12, entity.residenceTax12);
		return domain;
	}

	public static String convertpersonID(PprmtPersonResiTax entity) {
		return entity.pprmtPersonResiTaxPK.personId;
	}

	@Override
	public List<PersonResiTax> findAll(String companyCode, String personId, int yearKey) {
		return this.queryProxy().query(SEL_1, PprmtPersonResiTax.class).setParameter("CCD", companyCode)
				.setParameter("PID", personId).setParameter("Y_K", yearKey).getList(c -> toDomain(c));
	}

	@Override
	public void update(PersonResiTax domain) {
		this.commandProxy().update(toEntity(domain));
	}

	@Override
	public void remove(String companyCode, String personId, int yearKey) {
		PprmtPersonResiTaxPK pprmtPersonResiTaxPK = new PprmtPersonResiTaxPK(companyCode, personId, yearKey);
		this.commandProxy().remove(PprmtPersonResiTax.class, pprmtPersonResiTaxPK);
	}

	@Override
	public List<?> findByResidenceCode(String companyCode, String residenceCode, int yearKey) {
		return this.queryProxy().query(SEL_5, PprmtPersonResiTax.class).setParameter("CCD", companyCode)
				.setParameter("RESIDENCE_CD", residenceCode).setParameter("Y_K", yearKey).getList();
	}

	@Override
	public void updateResendence(String companyCode, String resendenceCode, String personID, int yearKey) {
		this.getEntityManager().createQuery(UPD_2).setParameter("CCD", companyCode).setParameter("Y_K", yearKey)
				.setParameter("PID", personID).setParameter("RESIDENCE_CD", resendenceCode).executeUpdate();

	}
}
