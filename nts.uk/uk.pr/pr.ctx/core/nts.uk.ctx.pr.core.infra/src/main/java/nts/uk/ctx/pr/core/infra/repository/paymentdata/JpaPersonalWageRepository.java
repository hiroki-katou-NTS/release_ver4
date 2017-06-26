package nts.uk.ctx.pr.core.infra.repository.paymentdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.PersonalWage;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.PersonalWageRepository;
import nts.uk.ctx.pr.core.infra.entity.personalinfo.wage.PprmtPersonWage;
import nts.uk.ctx.pr.core.infra.entity.personalinfo.wage.PprmtPersonWagePK;

@Stateless
public class JpaPersonalWageRepository extends JpaRepository implements PersonalWageRepository {

	private final String SELECT_LIST_BY_YEAR_MONTH = "SELECT c FROM PprmtPersonWage c WHERE c.pprmtPersonWagePK.ccd = :CCD and c.pprmtPersonWagePK.pId IN :PID and c.pprmtPersonWagePK.strYm <= :BASEYM and c.endYm >= :BASEYM";
	private final String SEL_1 = "SELECT c FROM PprmtPersonWage c WHERE c.pprmtPersonWagePK.ccd = :companyCode and c.pprmtPersonWagePK.pId = :personId and c.pprmtPersonWagePK.strYm <= :baseYearMonth and c.endYm >= :baseYearMonth";


	@Override
	public List<PersonalWage> findAll(String companyCode, List<String> personIds, int baseYm) {
		List<PersonalWage> results = new ArrayList<>();
		CollectionUtil.split(personIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, personIdList -> {
			this.queryProxy().query(SELECT_LIST_BY_YEAR_MONTH, PprmtPersonWage.class)
			.setParameter("ccd", companyCode).setParameter("pIds", personIdList).setParameter("baseYm", baseYm)
			.getList().stream().forEach(e -> results.add(toDomain(e)));
		});
		
		return results;

	}

	private static PersonalWage toDomain(PprmtPersonWage entity) {
		PersonalWage domain = PersonalWage.createFromJavaType(entity.val, entity.pprmtPersonWagePK.pId,
				entity.pprmtPersonWagePK.ccd, entity.pprmtPersonWagePK.ctgAtr, entity.pprmtPersonWagePK.strYm,
				entity.endYm, entity.pprmtPersonWagePK.pWageCd);
		//entity.toDomain(domain);
		return domain;
	}

	@Override
	public Optional<PersonalWage> find(String companyCode, String personId, int categoryAttribute, String wageCode,
			int startYearMonth) {
		Optional<PersonalWage> result = this.queryProxy()
				.find(new PprmtPersonWagePK(companyCode, personId, categoryAttribute, wageCode, startYearMonth),
						PprmtPersonWage.class)
				.map(c -> toDomain(c));
		return result;
	}

	@Override
	public List<PersonalWage> findAll(String companyCode, String personId, int baseYearMonth) {
		return this.queryProxy().query(SEL_1, PprmtPersonWage.class)
				.setParameter("companyCode", companyCode)
				.setParameter("personId", personId)
				.setParameter("baseYearMonth", baseYearMonth)
				.getList(x -> toDomain(x));
	}

}
