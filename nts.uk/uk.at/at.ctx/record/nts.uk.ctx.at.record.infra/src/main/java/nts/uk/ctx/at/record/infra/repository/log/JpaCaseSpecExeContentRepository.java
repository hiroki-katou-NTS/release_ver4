package nts.uk.ctx.at.record.infra.repository.log;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CaseSpecExeContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CaseSpecExeContentRepository;
import nts.uk.ctx.at.record.infra.entity.log.KrcstCaseSpecExeContent;

@Stateless
public class JpaCaseSpecExeContentRepository extends JpaRepository implements CaseSpecExeContentRepository {
	
	private final String SELECT_FROM_CASE = " SELECT c FROM KrcstCaseSpecExeContent c ";
	
	private final String SELECT_CASE_BY_CODE =SELECT_FROM_CASE
			+ " WHERE c.krcstCaseSpecExeContentPK.caseSpecExeContentID = :caseSpecExeContentID";

	@Override
	public Optional<CaseSpecExeContent> getCaseSpecExeContentById(String caseSpecExeContentID) {
		Optional<CaseSpecExeContent> data = this.queryProxy().query(SELECT_CASE_BY_CODE,KrcstCaseSpecExeContent.class)
				.setParameter("caseSpecExeContentID", caseSpecExeContentID)
				.getSingle(c-> c.toDomain());
		return data;
	}

	@Override
	public List<CaseSpecExeContent> getAllCaseSpecExeContent() {
		
		TypedQueryWrapper<KrcstCaseSpecExeContent> data1 = this.queryProxy().query(SELECT_FROM_CASE,KrcstCaseSpecExeContent.class);
		
		
		List<CaseSpecExeContent> data = this.queryProxy().query(SELECT_FROM_CASE,KrcstCaseSpecExeContent.class)
				.getList(c->c.toDomain());
		
		return data;
	}
	
	

}
