package nts.uk.ctx.pr.core.infra.repository.personalinfo.employmentcontract;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.personalinfo.employmentcontract.PersonalEmploymentContract;
import nts.uk.ctx.pr.core.dom.personalinfo.employmentcontract.PersonalEmploymentContractRepository;
import nts.uk.ctx.pr.core.infra.entity.personalinfo.employmentcontract.PclmtPersonEmpContract;
import nts.uk.ctx.pr.core.infra.entity.personalinfo.employmentcontract.PclmtPersonEmpContractPK;

@Stateless
public class JpaPersonalEmploymentContractRepository extends JpaRepository
		implements PersonalEmploymentContractRepository {
	private final String SELECT_BY_CCD_PID_STRD_ENDD = "SELECT c FROM PclmtPersonEmpContract c"
			+ " WHERE c.pclmtPersonEmpContractPK.ccd = :ccd" + " and c.pclmtPersonEmpContractPK.pId IN :pIds"
			+ " and c.pclmtPersonEmpContractPK.strD <= :baseYmd" + " and c.endD >= :baseYmd";
	
	private final String SEL_2 = "SELECT c FROM PclmtPersonEmpContract c"
			+ " WHERE c.pclmtPersonEmpContractPK.ccd = :ccd" 
			+ " and c.pclmtPersonEmpContractPK.pId = :pId"
			+ " and c.pclmtPersonEmpContractPK.strD <= :baseYmd" 
			+ " and c.endD >= :baseYmd";
	

	@Override
	public List<PersonalEmploymentContract> findAll(String companyCode, List<String> personIds, GeneralDate baseYmd) {
		List<PersonalEmploymentContract> results = new ArrayList<>();
		CollectionUtil.split(personIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, personIdList -> {
			this.queryProxy().query(SELECT_BY_CCD_PID_STRD_ENDD, PclmtPersonEmpContract.class)
			.setParameter("ccd", companyCode).setParameter("pIds", personIdList).setParameter("baseYmd", baseYmd)
			.getList().stream().forEach(e -> results.add(toDomain(e)));
		});
		return results;
	}

	private static PersonalEmploymentContract toDomain(PclmtPersonEmpContract entity) {
		val domain = PersonalEmploymentContract.createFromJavaType(entity.payrollSystem,
				entity.pclmtPersonEmpContractPK.pId, entity.pclmtPersonEmpContractPK.pId,
				entity.pclmtPersonEmpContractPK.strD, entity.endD);
		//entity.toDomain(domain);
		return domain;
	}

	@Override
	public Optional<PersonalEmploymentContract> find(String companyCode, String personId, GeneralDate baseYmd) {
		return this.queryProxy()
				.find(new PclmtPersonEmpContractPK(companyCode, personId, baseYmd), PclmtPersonEmpContract.class)
				.map(c -> toDomain(c));
	}

	@Override
	public Optional<PersonalEmploymentContract> findActive(String companyCode, String personId, GeneralDate baseYmd) {
		List<PersonalEmploymentContract> empContractList = this.queryProxy().query(SEL_2, PclmtPersonEmpContract.class)
				.setParameter("ccd", companyCode)
				.setParameter("pId", personId)
				.setParameter("baseYmd", baseYmd)
				.getList(e -> toDomain(e));
		
		if (empContractList.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(empContractList.get(0));
	}
}
