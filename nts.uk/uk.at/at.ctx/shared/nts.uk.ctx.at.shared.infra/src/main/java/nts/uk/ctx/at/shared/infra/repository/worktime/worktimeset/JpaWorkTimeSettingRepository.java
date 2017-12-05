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
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.infra.entity.worktime.worktimeset.KshmtWorkTimeSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.worktimeset.KshmtWorkTimeSetPK_;
import nts.uk.ctx.at.shared.infra.entity.worktime.worktimeset.KshmtWorkTimeSet_;

@Stateless
public class JpaWorkTimeSettingRepository extends JpaRepository implements WorkTimeSettingRepository {

	@Override
	public List<WorkTimeSetting> findByCompanyID(String companyID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkTimeSetting> findAll(String companyId) {
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
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		List<KshmtWorkTimeSet> lstKwtstWorkTimeSet = em.createQuery(cq).getResultList();

		return lstKwtstWorkTimeSet.stream().map(item -> {
			WorkTimeSetting worktimeSetting = new WorkTimeSetting(new JpaWorkTimeSettingGetMemento(item));
			return worktimeSetting;
		}).collect(Collectors.toList());
	}

	@Override
	public List<WorkTimeSetting> findByCodes(String companyID, List<String> codes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<WorkTimeSetting> findByCode(String companyId, String worktimeCode) {
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

		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KshmtWorkTimeSet_.kshmtWorkTimeSetPK).get(KshmtWorkTimeSetPK_.worktimeCd), worktimeCode));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		KshmtWorkTimeSet kwtstWorkTimeSet = em.createQuery(cq).getSingleResult();

		return Optional.of(new WorkTimeSetting(new JpaWorkTimeSettingGetMemento(kwtstWorkTimeSet)));
	}

	@Override
	public List<WorkTimeSetting> findByCodeList(String companyID, List<String> siftCDs) {
		// TODO Auto-generated method stub
		return null;
	}

}
