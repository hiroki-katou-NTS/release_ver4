/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.worktimeset;

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
import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingCondition;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtWorkTimeSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtWorkTimeSetPK;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtWorkTimeSetPK_;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtWorkTimeSet_;

/**
 * The Class JpaWorkTimeSettingRepository.
 */
@Stateless
public class JpaWorkTimeSettingRepository extends JpaRepository implements WorkTimeSettingRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository#
	 * findByCompanyId(java.lang.String)
	 */
	@Override
	public List<WorkTimeSetting> findByCompanyId(String companyId) {
		return this.findWithCondition(companyId, new WorkTimeSettingCondition(null, null, null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository#
	 * findByCodes(java.lang.String, java.util.List)
	 */
	@Override
	public List<WorkTimeSetting> findByCodes(String companyID, List<String> codes) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkTimeSet> cq = criteriaBuilder.createQuery(KshmtWorkTimeSet.class);
		Root<KshmtWorkTimeSet> root = cq.from(KshmtWorkTimeSet.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KshmtWorkTimeSet_.kshmtWorkTimeSetPK).get(KshmtWorkTimeSetPK_.cid), companyID));
		lstpredicateWhere
				.add(root.get(KshmtWorkTimeSet_.kshmtWorkTimeSetPK).get(KshmtWorkTimeSetPK_.worktimeCd).in(codes));
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		List<KshmtWorkTimeSet> lstKwtstWorkTimeSet = em.createQuery(cq).getResultList();

		return lstKwtstWorkTimeSet.stream().map(item -> {
			WorkTimeSetting worktimeSetting = new WorkTimeSetting(new JpaWorkTimeSettingGetMemento(item));
			return worktimeSetting;
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository#
	 * findByCode(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<WorkTimeSetting> findByCode(String companyId, String worktimeCode) {
		Optional<KshmtWorkTimeSet> entity = this.findByPk(companyId, worktimeCode);

		return entity.isPresent() ? Optional.of(new WorkTimeSetting(new JpaWorkTimeSettingGetMemento(entity.get())))
				: Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository#
	 * save(nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting)
	 */
	@Override
	public void add(WorkTimeSetting domain) {
		KshmtWorkTimeSet entity = new KshmtWorkTimeSet();
		domain.saveToMemento(new JpaWorkTimeSettingSetMemento(entity));
		this.commandProxy().insert(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository#
	 * update(nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting)
	 */
	@Override
	public void update(WorkTimeSetting domain) {
		KshmtWorkTimeSetPK pk = new KshmtWorkTimeSetPK(domain.getCompanyId(), domain.getWorktimeCode().v());

		Optional<KshmtWorkTimeSet> op = this.queryProxy().find(pk, KshmtWorkTimeSet.class);
		KshmtWorkTimeSet entity = op.get();
		domain.saveToMemento(new JpaWorkTimeSettingSetMemento(entity));
		this.commandProxy().update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository#
	 * remove(java.lang.String, java.lang.String)
	 */
	@Override
	public void remove(String companyId, String workTimeCode) {
		this.commandProxy().remove(KshmtWorkTimeSet.class, new KshmtWorkTimeSetPK(companyId, workTimeCode));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository#
	 * findWithCondition(java.lang.String,
	 * nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr,
	 * nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet,
	 * nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr)
	 */
	@Override
	public List<WorkTimeSetting> findWithCondition(String companyId, WorkTimeSettingCondition condition) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkTimeSet> cq = criteriaBuilder.createQuery(KshmtWorkTimeSet.class);
		Root<KshmtWorkTimeSet> root = cq.from(KshmtWorkTimeSet.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KshmtWorkTimeSet_.kshmtWorkTimeSetPK).get(KshmtWorkTimeSetPK_.cid), companyId));
		if (condition.getWorkTimeDailyAtr() != null) {
			lstpredicateWhere.add(
					criteriaBuilder.equal(root.get(KshmtWorkTimeSet_.dailyWorkAtr), condition.getWorkTimeDailyAtr()));
		}
		if (condition.getWorkTimeMethodSet() != null) {
			lstpredicateWhere.add(criteriaBuilder.equal(root.get(KshmtWorkTimeSet_.worktimeSetMethod),
					condition.getWorkTimeMethodSet()));
		}
		if (!condition.getIsAbolish() && condition.getIsAbolish() != null) {
			lstpredicateWhere.add(
					criteriaBuilder.equal(root.get(KshmtWorkTimeSet_.abolitionAtr), AbolishAtr.NOT_ABOLISH.value));
		}
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		List<KshmtWorkTimeSet> lstKwtstWorkTimeSet = em.createQuery(cq).getResultList();

		return lstKwtstWorkTimeSet.stream().map(item -> {
			WorkTimeSetting worktimeSetting = new WorkTimeSetting(new JpaWorkTimeSettingGetMemento(item));
			return worktimeSetting;
		}).collect(Collectors.toList());
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository#getListWorkTimeSetByListCode(java.lang.String, java.util.List)
	 */
	@Override
	public List<WorkTimeSetting> getListWorkTimeSetByListCode(String companyId, List<String> workTimeCodes) {
		if(workTimeCodes.isEmpty()){
			return this.findWithCondition(companyId, new WorkTimeSettingCondition(null, null, null));
		}
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtWorkTimeSet> cq = criteriaBuilder.createQuery(KshmtWorkTimeSet.class);
		Root<KshmtWorkTimeSet> root = cq.from(KshmtWorkTimeSet.class);

		// select root
		cq.select(root);
		
		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KshmtWorkTimeSet_.kshmtWorkTimeSetPK).get(KshmtWorkTimeSetPK_.cid), companyId));
		lstpredicateWhere.add(root.get(KshmtWorkTimeSet_.kshmtWorkTimeSetPK).get(KshmtWorkTimeSetPK_.cid).in(workTimeCodes));
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		List<KshmtWorkTimeSet> lstKwtstWorkTimeSet = em.createQuery(cq).getResultList();
		
		return lstKwtstWorkTimeSet.stream().map(item -> {
			WorkTimeSetting worktimeSetting = new WorkTimeSetting(new JpaWorkTimeSettingGetMemento(item));
			return worktimeSetting;
		}).collect(Collectors.toList());
	}

	/**
	 * Find by pk.
	 *
	 * @param companyId the company id
	 * @param worktimeCode the worktime code
	 * @return the optional
	 */
	private Optional<KshmtWorkTimeSet> findByPk(String companyId, String worktimeCode) {
		KshmtWorkTimeSetPK pk = new KshmtWorkTimeSetPK(companyId, worktimeCode);
		return this.queryProxy().find(pk, KshmtWorkTimeSet.class);
	}
}
