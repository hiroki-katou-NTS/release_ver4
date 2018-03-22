/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.statutory.worktime_new.employment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpDeforLaborSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpDeforLaborSettingRepository;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.employment.KshstEmpDeforLarSet;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.employment.KshstEmpDeforLarSetPK_;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.employment.KshstEmpDeforLarSet_;

/**
 * The Class JpaEmpDeforLaborSettingRepository.
 */
@Stateless
public class JpaEmpDeforLaborSettingRepository extends JpaRepository implements EmpDeforLaborSettingRepository {

	/* 
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpDeforLaborSettingRepository#add(nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpDeforLaborSetting)
	 */
	@Override
	public void add(EmpDeforLaborSetting setting) {
		commandProxy().insert(this.toEntity(setting));
	}

	/* 
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpDeforLaborSettingRepository#update(nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpDeforLaborSetting)
	 */
	@Override
	public void update(EmpDeforLaborSetting setting) {
		commandProxy().update(this.toEntity(setting));
	}

	/* 
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpDeforLaborSettingRepository#delete(nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpDeforLaborSetting)
	 */
	@Override
	public void delete(EmpDeforLaborSetting empMentDeforLaborSetting) {
		commandProxy().remove(empMentDeforLaborSetting);
	}

	/* 
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpDeforLaborSettingRepository#findByCidAndEmplCodeAndYear(java.lang.String, java.lang.String, nts.uk.ctx.at.shared.dom.common.Year)
	 */
	@Override
	public Optional<EmpDeforLaborSetting> findByCidAndEmplCodeAndYear(String cid, String emplCode, Year year) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<KshstEmpDeforLarSet> cq = cb.createQuery(KshstEmpDeforLarSet.class);
		Root<KshstEmpDeforLarSet> root = cq.from(KshstEmpDeforLarSet.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();
		predicateList.add(cb.equal(root.get(KshstEmpDeforLarSet_.kshstEmpDeforLarSetPK).get(KshstEmpDeforLarSetPK_.cid), cid));
		predicateList.add(cb.equal(root.get(KshstEmpDeforLarSet_.kshstEmpDeforLarSetPK).get(KshstEmpDeforLarSetPK_.year), year));
		predicateList.add(cb.equal(root.get(KshstEmpDeforLarSet_.kshstEmpDeforLarSetPK).get(KshstEmpDeforLarSetPK_.empCd), emplCode));

		cq.where(predicateList.toArray(new Predicate[] {}));
		return Optional.ofNullable(this.toDomain(em.createQuery(cq).getSingleResult()));
	}

	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the kshst emp defor lar set
	 */
	private KshstEmpDeforLarSet toEntity(EmpDeforLaborSetting domain) {
		JpaEmpDeforLaborSettingSetMemento memento = new JpaEmpDeforLaborSettingSetMemento();
		domain.saveToMemento(memento);
		return memento.getEntity();
	}
	
	/**
	 * To domain.
	 *
	 * @param entity the entities
	 * @return the emp defor labor setting
	 */
	private EmpDeforLaborSetting toDomain(KshstEmpDeforLarSet entity) {
		if (entity == null) {
			return null;
		}
		return new EmpDeforLaborSetting(new JpaEmpDeforLaborSettingGetMemento(entity));
	}

}
