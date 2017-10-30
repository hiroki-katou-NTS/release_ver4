/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.ot.autocalsetting.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.job.JobAutoCalSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.job.JobAutoCalSettingRepository;
import nts.uk.ctx.at.shared.infra.entity.ot.autocalsetting.job.KshmtAutoJobCalSet;
import nts.uk.ctx.at.shared.infra.entity.ot.autocalsetting.job.KshmtAutoJobCalSetPK;
import nts.uk.ctx.at.shared.infra.entity.ot.autocalsetting.job.KshmtAutoJobCalSetPK_;
import nts.uk.ctx.at.shared.infra.entity.ot.autocalsetting.job.KshmtAutoJobCalSet_;

/**
 * The Class JpaJobAutoCalSettingRepository.
 */
@Stateless
public class JpaJobAutoCalSettingRepository extends JpaRepository implements JobAutoCalSettingRepository {
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.JobAutoCalSettingRepository#update(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.JobAutoCalSetting)
	 */
	@Override
	public void update(JobAutoCalSetting jobAutoCalSetting) {
		this.commandProxy().update(this.toEntity(jobAutoCalSetting));
		this.getEntityManager().flush();

	}

	/**
	 * To entity.
	 *
	 * @param jobAutoCalSetting the job auto cal setting
	 * @return the kshmt auto job cal set
	 */
	private KshmtAutoJobCalSet toEntity(JobAutoCalSetting jobAutoCalSetting) {
		Optional<KshmtAutoJobCalSet> optinal = this.queryProxy().find(
				new KshmtAutoJobCalSetPK(jobAutoCalSetting.getCompanyId().v(), jobAutoCalSetting.getJobId().v()),
				KshmtAutoJobCalSet.class);
		KshmtAutoJobCalSet entity = null;
		if (optinal.isPresent()) {
			entity = optinal.get();
		} else {
			entity = new KshmtAutoJobCalSet();
		}
		JpaJobAutoCalSettingSetMemento memento = new JpaJobAutoCalSettingSetMemento(entity);
		jobAutoCalSetting.saveToMemento(memento);
		return entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.JobAutoCalSettingRepository#getAllJobAutoCalSetting(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<JobAutoCalSetting> getJobAutoCalSetting(String companyId, String jobId) {
		KshmtAutoJobCalSetPK kshmtAutoJobCalSetPK = new KshmtAutoJobCalSetPK(companyId, jobId);

		Optional<KshmtAutoJobCalSet> optKshmtAutoJobCalSet = this.queryProxy().find(kshmtAutoJobCalSetPK,
				KshmtAutoJobCalSet.class);

		if (!optKshmtAutoJobCalSet.isPresent()) {
			return Optional.empty();
		}

		return Optional.of(new JobAutoCalSetting(new JpaJobAutoCalSettingGetMemento(optKshmtAutoJobCalSet.get())));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.JobAutoCalSettingRepository#delete(java.lang.String, java.lang.String)
	 */
	@Override
	public void delete(String cid, String jobId) {
		this.commandProxy().remove(KshmtAutoJobCalSet.class, new KshmtAutoJobCalSetPK(cid, jobId));

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.JobAutoCalSettingRepository#add(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.JobAutoCalSetting)
	 */
	@Override
	public void add(JobAutoCalSetting jobAutoCalSetting) {
		this.commandProxy().insert(this.toEntity(jobAutoCalSetting));
		this.getEntityManager().flush();

	}
	
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.ot.autocalsetting.job.JobAutoCalSettingRepository#getAllJobAutoCalSetting(java.lang.String)
	 */
	@Override
	public List<JobAutoCalSetting> getAllJobAutoCalSetting(String companyId) {
		EntityManager em = this.getEntityManager();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<KshmtAutoJobCalSet> cq = builder.createQuery(KshmtAutoJobCalSet.class);
		Root<KshmtAutoJobCalSet> root = cq.from(KshmtAutoJobCalSet.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		predicateList.add(builder.equal(root.get(KshmtAutoJobCalSet_.kshmtAutoJobCalSetPK)
				.get(KshmtAutoJobCalSetPK_.cid), companyId));

		cq.where(predicateList.toArray(new Predicate[] {}));
		return em.createQuery(cq).getResultList().stream()
				.map(entity -> new JobAutoCalSetting(new JpaJobAutoCalSettingGetMemento(entity)))
				.collect(Collectors.toList());
	}

}
