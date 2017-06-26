package nts.uk.ctx.basic.infra.repository.company;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.basic.dom.company.Company;
import nts.uk.ctx.basic.dom.company.CompanyRepository;
import nts.uk.ctx.basic.infra.entity.company.CmnmtCompany;
import nts.uk.ctx.basic.infra.entity.company.CmnmtCompanyPK;

/**
 * 
 * @author lanlt
 *
 */
@Stateless
public class JpaCompanyRepository extends JpaRepository implements CompanyRepository {

	private static final String SEL_NO_WHERE = "SELECT e FROM CmnmtCompany e";
	private static final String SEL_1 = SEL_NO_WHERE + " WHERE e.cmnmtCompanyPk.companyCd <> '0000'";
	private static final String SEL_2 = SEL_NO_WHERE + " WHERE e.cmnmtCompanyPk.companyCd = :companyCd";
	private static final String SEL_4 = SEL_NO_WHERE + " WHERE e.cmnmtCompanyPk.companyCd = :companyCd"
													 + " AND e.use_Kt_Set = :use_Kt_Set";

	private final Company toDomain(CmnmtCompany entity) {
		val domain = Company.createFromJavaType(entity.cmnmtCompanyPk.companyCd, entity.cName, entity.cName_E,
				entity.cName_Abb, entity.cName_Kana, entity.cmyNo, entity.fax_No, entity.postal, entity.president_Name,
				entity.president_Job_Title, entity.tel_No, entity.dep_Work_Place_Set, entity.disp_Atr, entity.address1,
				entity.address2, entity.kn_Address1, entity.kn_Address2, entity.term_Begin_Mon, entity.use_Gr_Set,
				entity.use_Kt_Set, entity.use_Qy_Set, entity.use_Jj_Set, entity.use_Ac_Set, entity.use_Gw_Set,
				entity.use_Hc_Set, entity.use_Lc_Set, entity.use_Bi_Set, entity.use_Rs01_Set, entity.use_Rs02_Set,
				entity.use_Rs03_Set, entity.use_Rs04_Set, entity.use_Rs05_Set, entity.use_Rs06_Set, entity.use_Rs07_Set,
				entity.use_Rs08_Set, entity.use_Rs09_Set, entity.use_Rs10_Set);
		return domain;
	}

	private static CmnmtCompany toEntity(Company domain) {
		CmnmtCompany entity = new CmnmtCompany();
		entity.cmnmtCompanyPk = new CmnmtCompanyPK(domain.getCompanyCode().toString());
		entity.cName = domain.getCompanyName().v();
		entity.cName_E = domain.getCompanyNameGlobal().v();
		entity.cName_Abb = domain.getCompanyNameAbb().v();
		entity.cName_Kana = domain.getCompanyNameKana().v();
		entity.disp_Atr = domain.getDisplayAttribute().value;
		entity.cmyNo = domain.getCorporateMyNumber().v();
		entity.president_Name = domain.getPresidentName().v();
		entity.president_Job_Title = domain.getPresidentJobTitle().v();
		entity.postal = domain.getPostal().v();
		entity.address1 = domain.getAddress().getAddress1().v();
		entity.address2 = domain.getAddress().getAddress2().v();
		entity.kn_Address1 = domain.getAddress().getAddressKana1().v();
		entity.kn_Address2 = domain.getAddress().getAddressKana2().v();
		entity.tel_No = domain.getTelephoneNo().v();
		entity.fax_No = domain.getFaxNo().v();
		entity.dep_Work_Place_Set = domain.getDepWorkPlaceSet().value;
		entity.term_Begin_Mon = domain.getTermBeginMon().v();
		entity.use_Gr_Set = domain.getCompanyUseSet().getUse_Gr_Set().v();
		entity.use_Kt_Set = domain.getCompanyUseSet().getUse_Kt_Set().v();
		entity.use_Qy_Set = domain.getCompanyUseSet().getUse_Qy_Set().v();
		entity.use_Jj_Set = domain.getCompanyUseSet().getUse_Jj_Set().v();
		entity.use_Ac_Set = domain.getCompanyUseSet().getUse_Ac_Set().v();
		entity.use_Gw_Set = domain.getCompanyUseSet().getUse_Gw_Set().v();
		entity.use_Hc_Set = domain.getCompanyUseSet().getUse_Hc_Set().v();
		entity.use_Lc_Set = domain.getCompanyUseSet().getUse_Lc_Set().v();
		entity.use_Bi_Set = domain.getCompanyUseSet().getUse_Bi_Set().v();
		entity.use_Rs01_Set = domain.getCompanyUseSet().getUse_Rs01_Set().v();
		entity.use_Rs02_Set = domain.getCompanyUseSet().getUse_Rs02_Set().v();
		entity.use_Rs03_Set = domain.getCompanyUseSet().getUse_Rs03_Set().v();
		entity.use_Rs04_Set = domain.getCompanyUseSet().getUse_Rs04_Set().v();
		entity.use_Rs05_Set = domain.getCompanyUseSet().getUse_Rs05_Set().v();
		entity.use_Rs06_Set = domain.getCompanyUseSet().getUse_Rs06_Set().v();
		entity.use_Rs07_Set = domain.getCompanyUseSet().getUse_Rs07_Set().v();
		entity.use_Rs08_Set = domain.getCompanyUseSet().getUse_Rs08_Set().v();
		entity.use_Rs09_Set = domain.getCompanyUseSet().getUse_Rs09_Set().v();
		entity.use_Rs10_Set = domain.getCompanyUseSet().getUse_Rs10_Set().v();
		return entity;

	}

	@Override
	public List<Company> getAllCompanys() {
		try {
			return this.queryProxy().query(SEL_1, CmnmtCompany.class).getList(c -> toDomain(c));
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Optional<Company> getCompanyDetail(String companyCd) {
		try {
			return this.queryProxy().query(SEL_2, CmnmtCompany.class)
					.setParameter("companyCd", companyCd)
					.getSingle().map(e -> {
						return Optional.of(toDomain(e));
					}).orElse(Optional.empty());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Optional<Company> getCompanyByUserKtSet(String companyCd, int use_Kt_Set) {
		return this.queryProxy().query(SEL_4, CmnmtCompany.class)
				.setParameter("companyCd", companyCd)
				.setParameter("use_Kt_Set", use_Kt_Set).getSingle().map(e -> {
					return Optional.of(toDomain(e));
				}).orElse(Optional.empty());
	}

	@Override
	public void add(Company company) {
		try {
			this.commandProxy().insert(toEntity(company));
		} catch (Exception e) {
			throw (e);
		}
	}

	@Override
	public void update(Company company) {
		try {
			this.commandProxy().update(toEntity(company));
		} catch (Exception e) {
			throw (e);
		}
	}

	@Override
	public void delete(String companyCode) {
		val objKey = new CmnmtCompanyPK();
		objKey.companyCd = companyCode;
		this.commandProxy().remove(CmnmtCompany.class, objKey);
	}

}
