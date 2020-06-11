package nts.uk.ctx.bs.employee.infra.repository.employee.history;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyInfo;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyInfoRepository;
import nts.uk.ctx.bs.employee.infra.entity.employee.history.BsymtAffCompanyInfo;
import nts.uk.ctx.bs.employee.infra.entity.employee.history.BsymtAffCompanyInfoPk;

@Stateless
public class AffCompanyInfoRepositoryImp extends JpaRepository implements AffCompanyInfoRepository {

	private static final String SELECT_NO_PARAM = String.join(" ", "SELECT c FROM BsymtAffCompanyInfo c");

	private static final String SELECT_BY_HISTID = String.join(" ", SELECT_NO_PARAM,
			"WHERE c.bsymtAffCompanyInfoPk.historyId = :histId");

//	 Merge BSYMT_AFF_COM_HIST To BSYMT_AFF_COM_INFO  because response
//	 new Insert Method ↓
//	         ClassName    : AffCompanyHistRepositoryImp
//	         MethodName : addToMerge 
//	@Override
//	public void add(AffCompanyInfo domain) {
//		commandProxy().insert(toEntity(domain));
//		this.getEntityManager().flush();
//	}

	@Override
	public void update(AffCompanyInfo domain) {
		BsymtAffCompanyInfo entity = this.queryProxy().query(SELECT_BY_HISTID, BsymtAffCompanyInfo.class)
				.setParameter("histId", domain.getHistoryId()).getSingleOrNull();
		if (entity != null) {
			entity.sid = domain.getSid();
			entity.adoptionDate = domain.getAdoptionDate();
			entity.retirementAllowanceCalcStartDate = domain.getRetirementAllowanceCalcStartDate();
			entity.recruitmentCategoryCode = domain.getRecruitmentClassification().v();
			
			this.commandProxy().update(entity);
		}
	}

	@Override
	public void remove(AffCompanyInfo domain) {
		this.remove(domain.getHistoryId());
	}

	@Override
	public void remove(String histId) {
		this.commandProxy().remove(BsymtAffCompanyInfo.class, new BsymtAffCompanyInfoPk(histId));
	}

	@Override
	public AffCompanyInfo getAffCompanyInfoByHistId(String histId) {
		return this.queryProxy().query(SELECT_BY_HISTID, BsymtAffCompanyInfo.class).setParameter("histId", histId)
				.getSingle(m -> toDomain(m)).orElse(null);
	}

	private AffCompanyInfo toDomain(BsymtAffCompanyInfo entity) {
		return AffCompanyInfo.createFromJavaType(entity.sid, entity.bsymtAffCompanyInfoPk.historyId, entity.recruitmentCategoryCode,
				entity.adoptionDate, entity.retirementAllowanceCalcStartDate);
	}

	private BsymtAffCompanyInfo toEntity(AffCompanyInfo domain) {
		BsymtAffCompanyInfoPk entityPk = new BsymtAffCompanyInfoPk(domain.getHistoryId());

		return new BsymtAffCompanyInfo(entityPk, domain.getSid(), domain.getRecruitmentClassification().v(), domain.getAdoptionDate(),
				domain.getRetirementAllowanceCalcStartDate(), null);
	}
}
