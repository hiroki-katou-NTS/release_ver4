/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.optitem.applicable;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.optitem.applicable.EmpCondition;
import nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository;
import nts.uk.ctx.at.record.infra.entity.optitem.applicable.KrcstApplEmpCon;
import nts.uk.ctx.at.record.infra.entity.optitem.applicable.KrcstApplEmpConPK_;
import nts.uk.ctx.at.record.infra.entity.optitem.applicable.KrcstApplEmpCon_;

/**
 * The Class JpaEmpConditionRepository.
 */
@Stateless
public class JpaEmpConditionRepository extends JpaRepository implements EmpConditionRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository#update
	 * (nts.uk.ctx.at.record.dom.optitem.applicable.EmpCondition)
	 */
	@Override
	public void update(EmpCondition dom) {
		List<KrcstApplEmpCon> entities = this.findByItemNo(dom.getCompanyId().v(), dom.getOptItemNo().v());
		JpaEmpConditionSetMemento memento = new JpaEmpConditionSetMemento(entities);
		dom.saveToMemento(memento);

		this.commandProxy().updateAll(memento.getTypeValues());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository#find(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public EmpCondition find(String companyId, Integer optionalItemNo) {

		List<KrcstApplEmpCon> entityEmpCons = this.findByItemNo(companyId, optionalItemNo);

		return new EmpCondition(new JpaEmpConditionGetMemento(companyId, optionalItemNo, entityEmpCons));
	}

	/**
	 * Find by item no.
	 *
	 * @param companyId the company id
	 * @param optionalItemNo the optional item no
	 * @return the list
	 */
	private List<KrcstApplEmpCon> findByItemNo(String companyId, Integer optionalItemNo) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<KrcstApplEmpCon> cq = builder.createQuery(KrcstApplEmpCon.class);

		// From table
		Root<KrcstApplEmpCon> root = cq.from(KrcstApplEmpCon.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Add where condition
		predicateList.add(
				builder.equal(root.get(KrcstApplEmpCon_.krcstApplEmpConPK).get(KrcstApplEmpConPK_.cid), companyId));
		predicateList.add(builder.equal(
				root.get(KrcstApplEmpCon_.krcstApplEmpConPK).get(KrcstApplEmpConPK_.optionalItemNo), optionalItemNo));
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get results
		return em.createQuery(cq).getResultList();
	}

}
