package nts.uk.ctx.pr.core.infra.repository.rule.law.tax.residential;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.ResidentialTax;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.ResidentialTaxRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.QtxmtResidentialTax;
import nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.QtxmtResidentialTaxPk;

/**
 * 
 * @author lanlt
 *
 */
@Stateless
public class JpaResidentialTaxRepository extends JpaRepository implements ResidentialTaxRepository {
	private final String SEL_1 = "SELECT c FROM QtxmtResidentialTax c WHERE c.qtxmtResidentialTaxPk.companyCd =:companyCd";
	private final String SEL_2 = "SELECT c FROM QtxmtResidentialTax c WHERE c.qtxmtResidentialTaxPk.companyCd =:companyCd AND c.qtxmtResidentialTaxPk.resiTaxCode <>:resiTaxCode AND c.resiTaxReportCode =:resiTaxReportCode ";
	// key CCD RESI_TAX_CD
	private final String SEL_3 = "SELECT c FROM QtxmtResidentialTax c WHERE c.qtxmtResidentialTaxPk.companyCd =:companyCd AND c.qtxmtResidentialTaxPk.resiTaxCode =:resiTaxCode";
	private final String SEL_5 = "SELECT c.qtxmtResidentialTaxPk.resiTaxCode FROM QtxmtResidentialTax c WHERE c.qtxmtResidentialTaxPk.companyCd =:companyCd AND c.resiTaxReportCode =:resiTaxReportCode";
	private final String UPD_2 = "UPDATE QtxmtResidentialTax c SET c.resiTaxReportCode = :resiTaxReportCode WHERE  c.qtxmtResidentialTaxPk.companyCd = :companyCd"
			+ " AND c.qtxmtResidentialTaxPk.resiTaxCode =:resiTaxCode";
	private static ResidentialTax toDomain(QtxmtResidentialTax entity) {
		val domain = ResidentialTax.createFromJavaType(entity.qtxmtResidentialTaxPk.companyCd, entity.companyAccountNo,
				entity.companySpecifiedNo, entity.cordinatePostOffice, entity.cordinatePostalCode, entity.memo,
				entity.prefectureCode, entity.registeredName, entity.resiTaxAutonomy, entity.resiTaxAutonomyKana,
				entity.qtxmtResidentialTaxPk.resiTaxCode, entity.resiTaxReportCode);
		return domain;
	}

	private static QtxmtResidentialTax toEntity(ResidentialTax domain) {
		QtxmtResidentialTax entity = new QtxmtResidentialTax();
		entity.qtxmtResidentialTaxPk = new QtxmtResidentialTaxPk(domain.getCompanyCode().v(),
				domain.getResiTaxCode().v());
		entity.companyAccountNo = domain.getCompanyAccountNo().v();
		entity.companySpecifiedNo = domain.getCompanySpecifiedNo().v();
		entity.cordinatePostalCode = domain.getCordinatePostalCode().v();
		entity.cordinatePostOffice = domain.getCordinatePostOffice().v();
		entity.memo = domain.getMemo().v();
		entity.prefectureCode = domain.getPrefectureCode().v();
		entity.registeredName = domain.getRegisteredName().v();
		entity.resiTaxReportCode = domain.getResiTaxReportCode().v();
		entity.resiTaxAutonomy = domain.getResiTaxAutonomy().v();
		entity.resiTaxAutonomyKana = domain.getResiTaxAutonomyKana().v();
		return entity;
	}

	// by companyCode
	@Override
	public List<ResidentialTax> getAllResidentialTax(String companyCode) {
		List<ResidentialTax> list = this.queryProxy().query(SEL_1, QtxmtResidentialTax.class)
				.setParameter("companyCd", companyCode).getList(c -> toDomain(c));
		return list;
	}

	@Override
	public void add(ResidentialTax residentalTax) {
		this.commandProxy().insert(toEntity(residentalTax));

	}

	// UPD_1
	@Override
	public void update(ResidentialTax residentalTax) {
		try {
			this.commandProxy().update(toEntity(residentalTax));
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void delele(String companyCode, String resiTaxCode) {
		val objectKey = new QtxmtResidentialTaxPk();
		objectKey.companyCd = companyCode;
		objectKey.resiTaxCode = resiTaxCode;
		this.commandProxy().remove(QtxmtResidentialTax.class, objectKey);
	}

	@Override
	public List<ResidentialTax> getAllResidentialTax(String companyCd, String resiTaxCode, String resiTaxReportCode) {
		return this.queryProxy().query(SEL_2, QtxmtResidentialTax.class).setParameter("companyCd", companyCd)
				.setParameter("resiTaxCode", resiTaxCode).setParameter("resiTaxReportCode", resiTaxReportCode)
				.getList(c -> toDomain(c));

	}

	@Override
	public Optional<ResidentialTax> getResidentialTax(String companyCd, String resiTaxCode) {
		return this.queryProxy().query(SEL_3, QtxmtResidentialTax.class).setParameter("companyCd", companyCd)
				.setParameter("resiTaxCode", resiTaxCode).getSingle(c -> toDomain(c));

	}

	@Override
	public List<?> getAllResidentialTaxCode(String companyCd, String resiTaxReportCode) {
		return this.queryProxy().query(SEL_5, QtxmtResidentialTax.class).setParameter("companyCd", companyCd)
				.setParameter("resiTaxReportCode", resiTaxReportCode).getList();
	}

	@Override
	public void update(String companyCode, String resiTaxCode, String resiTaxReportCode) {
		this.getEntityManager().createQuery(UPD_2).setParameter("companyCd", companyCode).setParameter("resiTaxCode", resiTaxCode)
		.setParameter("resiTaxReportCode", resiTaxReportCode).executeUpdate();		
	}

}
