package nts.uk.ctx.pr.core.infra.repository.rule.employment.allot;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.classification.ClassificationMasterRespository;

public class JpaClassificationMasterReponsitory extends JpaRepository implements ClassificationMasterRespository {
	/*private final String SELECT_ALL = "SELECT c FORM CmnmtClass c WHERE c.cmnmtClassPK.companyCode = :companyCode ";

	private ClassificationMaster toDomain(CmnmtClass entity) {
		val domain = ClassificationMaster.createFromJavaType(entity.cmnmtClassPK.companyCode,
				entity.cmnmtClassPK.classificationCode, entity.classificationName, entity.classificationOutCode,
				new Memo(entity.memo));
		return domain;

	}

	@Override
	public List<ClassificationMaster> findMaster(String companyCode) {
		List<CmnmtClass> resultList = this.queryProxy().query(SEL_1, CmnmtClass.class)
				.setParameter("companyCode", companyCode).getList();
		return !resultList.isEmpty() ? resultList.stream().map(e -> {
			return toDomain(e);
		}).collect(Collectors.toList()) : new ArrayList<>();

	}*/

}
