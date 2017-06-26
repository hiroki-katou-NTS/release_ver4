/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.healthavgearn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnRepository;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealInsuAvgearnD;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealthInsuAmount;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealthInsuAmountPK;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealthInsuAmountPK_;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthavgearn.QismtHealthInsuAmount_;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthrate.QismtHealthInsuRate;

/**
 * The Class JpaHealthInsuranceAvgearnRepository.
 */
@Stateless
public class JpaHealthInsuranceAvgearnRepository extends JpaRepository
		implements HealthInsuranceAvgearnRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnRepository#update(nts.uk.ctx.pr.core.dom.insurance.
	 * social.healthavgearn.HealthInsuranceAvgearn)
	 */
	@Override
	public void update(List<HealthInsuranceAvgearn> healthInsuranceAvgearns, String ccd,
			String officeCd) {

		// Find healthInsuRate
		Optional<QismtHealthInsuRate> optHealthInsuRate = this.queryProxy()
				.find(healthInsuranceAvgearns.get(0).getHistoryId(), QismtHealthInsuRate.class);

		if (!optHealthInsuRate.isPresent()) {
			// TODO: throw exception!?
			return;
		}

		// Get ins rate
		QismtHealthInsuRate healthInsuRate = optHealthInsuRate.get();

		List<QismtHealInsuAvgearnD> qismtHealInsuAvgearnDList = new ArrayList<>();
		List<QismtHealthInsuAmount> qismtHealthInsuAmountList = new ArrayList<>();

		healthInsuranceAvgearns.stream().forEach(item -> {
			QismtHealInsuAvgearnD healInsuAvgearnD = new QismtHealInsuAvgearnD();
			QismtHealthInsuAmount insuAmount = new QismtHealthInsuAmount();

			item.saveToMemento(new JpaHealthInsuranceAvgearnSetMemento(ccd, officeCd,
					healInsuAvgearnD, insuAmount));

			qismtHealInsuAvgearnDList.add(healInsuAvgearnD);
			qismtHealthInsuAmountList.add(insuAmount);
		});

		// Set Updated HealthInsuAvgearnList
		healthInsuRate.setQismtHealInsuAvgearnDList(qismtHealInsuAvgearnDList);
		healthInsuRate.setQismtHealthInsuAmountList(qismtHealthInsuAmountList);

		// Update healthInsuRate
		this.commandProxy().update(healthInsuRate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnRepository#findById(java.lang.String)
	 */
	@Override
	public List<HealthInsuranceAvgearn> findById(String historyId) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<QismtHealthInsuAmount> cq = cb.createQuery(QismtHealthInsuAmount.class);

		Root<QismtHealthInsuAmount> root = cq.from(QismtHealthInsuAmount.class);

		cq.select(root);

		List<Predicate> listpredicate = new ArrayList<>();

		listpredicate.add(cb.equal(root.get(QismtHealthInsuAmount_.qismtHealthInsuAmountPK)
				.get(QismtHealthInsuAmountPK_.histId), historyId));

		cq.where(listpredicate.toArray(new Predicate[] {}));

		cq.orderBy(cb.asc(root.get(QismtHealthInsuAmount_.qismtHealthInsuAmountPK)
				.get(QismtHealthInsuAmountPK_.healthInsuGrade)));

		TypedQuery<QismtHealthInsuAmount> query = em.createQuery(cq);

		List<HealthInsuranceAvgearn> listHealthInsuranceAvgearn = query.getResultList().stream()
				.map(entity -> toDomain(entity.getQismtHealInsuAvgearnD(), entity))
				.collect(Collectors.toList());

		return listHealthInsuranceAvgearn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnRepository#find(java.lang.String,
	 * java.lang.Integer)
	 */
	@Override
	public Optional<HealthInsuranceAvgearn> find(String companyCode, String officeCode,
			String historyId, Integer grade) {
		return this.queryProxy()
				.find(new QismtHealthInsuAmountPK(companyCode, officeCode, historyId,
						BigDecimal.valueOf(grade)), QismtHealthInsuAmount.class)
				.map(c -> toDomain(c.getQismtHealInsuAvgearnD(), c));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.
	 * HealthInsuranceAvgearnRepository#findByOffice(java.lang.String,
	 * java.util.List)
	 */
	@Override
	public List<HealthInsuranceAvgearn> findByOfficeCodes(String companyCode, List<String> officeCodes) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<QismtHealthInsuAmount> cq = cb.createQuery(QismtHealthInsuAmount.class);

		Root<QismtHealthInsuAmount> root = cq.from(QismtHealthInsuAmount.class);

		cq.select(root);

		List<Predicate> listpredicate = new ArrayList<>();

		listpredicate.add(cb.equal(root.get(QismtHealthInsuAmount_.ccd), companyCode));
		listpredicate.add(root.get(QismtHealthInsuAmount_.siOfficeCd).in(officeCodes));

		cq.where(listpredicate.toArray(new Predicate[] {}));

		cq.orderBy(cb.asc(root.get(QismtHealthInsuAmount_.qismtHealthInsuAmountPK)
				.get(QismtHealthInsuAmountPK_.healthInsuGrade)));

		TypedQuery<QismtHealthInsuAmount> query = em.createQuery(cq);

		List<HealthInsuranceAvgearn> listHealthInsuranceAvgearn = query.getResultList().stream()
				.map(entity -> toDomain(entity.getQismtHealInsuAvgearnD(), entity))
				.collect(Collectors.toList());

		return listHealthInsuranceAvgearn;
	}

	/**
	 * To domain.
	 *
	 * @param insuAmount
	 *            the entity
	 * @return the health insurance avgearn
	 */
	private HealthInsuranceAvgearn toDomain(QismtHealInsuAvgearnD healInsuAvgearnD,
			QismtHealthInsuAmount insuAmount) {
		HealthInsuranceAvgearn domain = new HealthInsuranceAvgearn(
				new JpaHealthInsuranceAvgearnGetMemento(healInsuAvgearnD, insuAmount));
		return domain;
	}

}
