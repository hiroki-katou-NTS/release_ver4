/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.optitem.calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.optitem.calculation.Formula;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtOptItemFormula;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtOptItemFormulaPK;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtOptItemFormulaPK_;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtOptItemFormula_;

/**
 * The Class JpaFormulaRepository.
 */
@Stateless
public class JpaFormulaRepository extends JpaRepository implements FormulaRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository#create(
	 * java.util.List)
	 */
	@Override
	public void create(List<Formula> domains) {
		this.commandProxy().insertAll(domains.stream().map(domain -> {

			// Save to memento
			JpaFormulaSetMemento memento = new JpaFormulaSetMemento();
			domain.saveToMemento(memento);

			// Get entity from memento
			return memento.getEntity();

		}).collect(Collectors.toList()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository#remove(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void remove(String comId, Integer optItemNo) {
		this.commandProxy().removeAll(this.findEntity(comId, optItemNo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository#
	 * findByOptItemNo(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Formula> findByOptItemNo(String companyId, Integer optItemNo) {

		List<KrcmtOptItemFormula> results = this.findEntity(companyId, optItemNo);

		// Return
		if (CollectionUtil.isEmpty(results)) {
			return Collections.emptyList();
		}

		return results.stream().map(item -> new Formula(new JpaFormulaGetMemento(item))).collect(Collectors.toList());
	}

	/**
	 * Find entity.
	 *
	 * @param companyId the company id
	 * @param optItemNo the opt item no
	 * @return the list
	 */
	private List<KrcmtOptItemFormula> findEntity(String companyId, Integer optItemNo) {
		// get entity manager
		EntityManager em = this.getEntityManager();

		// create query
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcmtOptItemFormula> cq = criteriaBuilder.createQuery(KrcmtOptItemFormula.class);

		// select from table
		Root<KrcmtOptItemFormula> root = cq.from(KrcmtOptItemFormula.class);
		cq.select(root);

		// add conditions
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KrcmtOptItemFormula_.krcmtOptItemFormulaPK).get(KrcmtOptItemFormulaPK_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KrcmtOptItemFormula_.krcmtOptItemFormulaPK).get(KrcmtOptItemFormulaPK_.optionalItemNo),
				optItemNo));
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// Get results
		return em.createQuery(cq).getResultList();

	}
	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository#
	 * find(java.lang.String)
	 */
	public List<Formula> find(String companyId) {
		List<KrcmtOptItemFormula> results = this.findEntityByCompanyId(companyId);

		// Return
		if (CollectionUtil.isEmpty(results)) {
			return Collections.emptyList();
		}

		return results.stream().map(item -> new Formula(new JpaFormulaGetMemento(item))).collect(Collectors.toList());
	}
	
	/**
	 * Find entity.
	 *
	 * @param companyId the company id
	 * @param optItemNo the opt item no
	 * @return the list
	 */
	private List<KrcmtOptItemFormula> findEntityByCompanyId(String companyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();

		// create query
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcmtOptItemFormula> cq = criteriaBuilder.createQuery(KrcmtOptItemFormula.class);

		// select from table
		Root<KrcmtOptItemFormula> root = cq.from(KrcmtOptItemFormula.class);
		cq.select(root);

		// add conditions
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KrcmtOptItemFormula_.krcmtOptItemFormulaPK).get(KrcmtOptItemFormulaPK_.cid), companyId));
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// Get results
		return em.createQuery(cq).getResultList();

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository#findById(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Formula findById(String companyId, Integer optItemNo, String formulaId) {
		KrcmtOptItemFormula entity = this.queryProxy()
				.find(new KrcmtOptItemFormulaPK(companyId, optItemNo, formulaId), KrcmtOptItemFormula.class).get();
		return new Formula(new JpaFormulaGetMemento(entity));
	}

}
