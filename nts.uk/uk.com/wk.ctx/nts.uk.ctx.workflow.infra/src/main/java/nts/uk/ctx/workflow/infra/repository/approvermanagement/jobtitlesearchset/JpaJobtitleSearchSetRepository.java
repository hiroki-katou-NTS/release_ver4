package nts.uk.ctx.workflow.infra.repository.approvermanagement.jobtitlesearchset;

import java.util.Objects;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.JobtitleSearchSet;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.JobtitleSearchSetRepository;
import nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot.WwfstJobtitleSearchSet;
import nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot.WwfstJobtitleSearchSetPK;

@Stateless
public class JpaJobtitleSearchSetRepository extends JpaRepository implements JobtitleSearchSetRepository {

	@Override
	public Optional<JobtitleSearchSet> finById(String cid, String jobtitleId) {
		WwfstJobtitleSearchSet entity = this.getEntityManager().find(WwfstJobtitleSearchSet.class,
				new WwfstJobtitleSearchSetPK(cid, jobtitleId));
		if (Objects.isNull(entity)) {
			return Optional.empty();
		}
		return Optional.of(this.toDomain(entity));
	}

	/**
	 * convert entity to domain
	 * @param entity
	 * @return
	 */
	private JobtitleSearchSet toDomain(WwfstJobtitleSearchSet entity) {
		return JobtitleSearchSet.createSimpleFromJavaType(entity.wwfstJobtitleSearchSetPK.companyId,
				entity.wwfstJobtitleSearchSetPK.jobId, entity.searchSetFlg);
	}
	/**
	 * convert from domain to entity
	 * @param domain
	 * @return
	 * @author yennth
	 */
	private WwfstJobtitleSearchSet toEntityJob(JobtitleSearchSet domain){
		val entity = new WwfstJobtitleSearchSet();
		entity.wwfstJobtitleSearchSetPK = new WwfstJobtitleSearchSetPK(domain.getCompanyId(), domain.getJobId());
		entity.searchSetFlg = domain.getSearchSetFlg().value;
		return entity;
	}
	
	/**
	 * update job title search set
	 * @author yennth
	 */
	@Override
	public void update(JobtitleSearchSet jobSearch) {
		WwfstJobtitleSearchSet entity = toEntityJob(jobSearch);
		WwfstJobtitleSearchSet oldEntity = this.queryProxy().find(entity.wwfstJobtitleSearchSetPK, WwfstJobtitleSearchSet.class).get();
		oldEntity.searchSetFlg = entity.searchSetFlg;
		this.commandProxy().update(oldEntity);
	}
	/**
	 * insert job title search set
	 * @author yennth
	 */
	@Override
	public void insert(JobtitleSearchSet jobSearch) {
		WwfstJobtitleSearchSet entity = toEntityJob(jobSearch);
		this.commandProxy().insert(entity);
	}

}
