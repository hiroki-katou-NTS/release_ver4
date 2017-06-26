package nts.uk.ctx.pr.core.infra.repository.personalinfo.commute;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.personalinfo.commute.PersonalCommuteFee;
import nts.uk.ctx.pr.core.dom.personalinfo.commute.PersonalCommuteFeeRepository;
import nts.uk.ctx.pr.core.dom.personalinfo.commute.PersonalCommuteValue;
import nts.uk.ctx.pr.core.infra.entity.personalinfo.commute.PprmtPersonCommute;
import nts.uk.ctx.pr.core.infra.entity.personalinfo.commute.PprmtPersonCommutePK;

@Stateless
public class JpaPersonalCommuteFeeRepository extends JpaRepository implements PersonalCommuteFeeRepository {

	private final String SELECT_BY_CCD_PID_STRYM_ENDYM = "SELECT c FROM PprmtPersonCommute c WHERE c.pprmtPersonCommutePK.ccd = :ccd and c.pprmtPersonCommutePK.pId IN :pIds and c.pprmtPersonCommutePK.strYm <= :baseYm and c.endYm >= :baseYm";
	private final String SEL_1 = "SELECT c FROM PprmtPersonCommute c WHERE c.pprmtPersonCommutePK.ccd = :ccd and c.pprmtPersonCommutePK.pId = :pId and c.pprmtPersonCommutePK.strYm <= :baseYm and c.endYm >= :baseYm";

	@Override
	public List<PersonalCommuteFee> findAll(String companyCode, List<String> personIds, int baseYM) {
		List<PersonalCommuteFee> results = new ArrayList<>();
		CollectionUtil.split(personIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, personIdList -> {
			this.queryProxy().query(SELECT_BY_CCD_PID_STRYM_ENDYM, PprmtPersonCommute.class)
			.setParameter("ccd", companyCode).setParameter("pIds", personIdList).setParameter("baseYm", baseYM)
			.getList().stream().forEach(e -> results.add(toDomain(e)));
		});
		return results;
	}

	private static PersonalCommuteFee toDomain(PprmtPersonCommute entity) {
		PersonalCommuteValue commute1 = PersonalCommuteValue.createFromJavaType(entity.commuteCycle1,
				entity.commuAllowance1, entity.commuMeansAtr1, entity.payStartYm1, entity.useOrNot1);
		PersonalCommuteValue commute2 = PersonalCommuteValue.createFromJavaType(entity.commuteCycle2,
				entity.commuAllowance2, entity.commuMeansAtr2, entity.payStartYm2, entity.useOrNot2);
		PersonalCommuteValue commute3 = PersonalCommuteValue.createFromJavaType(entity.commuteCycle3,
				entity.commuAllowance3, entity.commuMeansAtr3, entity.payStartYm3, entity.useOrNot3);
		PersonalCommuteValue commute4 = PersonalCommuteValue.createFromJavaType(entity.commuteCycle4,
				entity.commuAllowance4, entity.commuMeansAtr4, entity.payStartYm4, entity.useOrNot4);
		PersonalCommuteValue commute5 = PersonalCommuteValue.createFromJavaType(entity.commuteCycle5,
				entity.commuAllowance5, entity.commuMeansAtr5, entity.payStartYm5, entity.useOrNot5);
		PersonalCommuteFee domain = PersonalCommuteFee.createFromJavaType(entity.commuNotaxLimitPriNo,
				entity.commuNotaxLimitPubNo, commute1, commute2, commute3, commute4, commute5,
				entity.pprmtPersonCommutePK.pId);
		//entity.toDomain(domain);
		return domain;
	}

	@Override
	public Optional<PersonalCommuteFee> find(String companyCode, String personId, int startYearMonth) {
		return this.queryProxy()
				.find(new PprmtPersonCommutePK(companyCode, personId, startYearMonth), PprmtPersonCommute.class)
				.map(c -> toDomain(c));
	}

	@Override
	public List<PersonalCommuteFee> findAll(String companyCode, String personId, int baseYearMonth) {
		return this.queryProxy().query(SEL_1, PprmtPersonCommute.class)
				.setParameter("ccd", companyCode)
				.setParameter("pId", personId)
				.setParameter("baseYm", baseYearMonth)
				.getList(x -> toDomain(x));
	}

}
