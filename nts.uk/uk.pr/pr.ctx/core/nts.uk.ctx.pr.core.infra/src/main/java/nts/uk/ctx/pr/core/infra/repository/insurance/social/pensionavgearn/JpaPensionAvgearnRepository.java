/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.pensionavgearn;

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
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnRepository;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAmount;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAmountPK;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAmountPK_;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAmount_;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionavgearn.QismtPensionAvgearnD;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionrate.QismtPensionRate;

/**
 * The Class JpaPensionAvgearnRepository.
 */
@Stateless
public class JpaPensionAvgearnRepository extends JpaRepository implements PensionAvgearnRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnRepository#update(nts.uk.ctx.pr.core.dom.insurance.social.
	 * pensionavgearn.PensionAvgearn)
	 */
	@Override
	public void update(List<PensionAvgearn> pensionAvgearns, String ccd, String officeCd) {
		// Find PensionRate
		QismtPensionRate pensionRate = this.queryProxy()
				.find(pensionAvgearns.get(0).getHistoryId(), QismtPensionRate.class).get();

		List<QismtPensionAvgearnD> qismtPensionAvgearnDList = new ArrayList<>();
		List<QismtPensionAmount> qismtPensionAmountList = new ArrayList<>();

		pensionAvgearns.forEach(item -> {
			QismtPensionAvgearnD avgearnDetail = new QismtPensionAvgearnD();
			QismtPensionAmount pensionAmount = new QismtPensionAmount();

			item.saveToMemento(
					new JpaPensionAvgearnSetMemento(ccd, officeCd, avgearnDetail, pensionAmount));

			qismtPensionAvgearnDList.add(avgearnDetail);
			qismtPensionAmountList.add(pensionAmount);
		});

		// Set Updated pensionAvgearnList
		pensionRate.setQismtPensionAvgearnDList(qismtPensionAvgearnDList);
		pensionRate.setQismtPensionAmountList(qismtPensionAmountList);

		// Update PensionRate
		this.commandProxy().update(pensionRate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnRepository#find(java.lang.String)
	 */
	@Override
	public List<PensionAvgearn> findById(String historyId) {

		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<QismtPensionAmount> cq = cb.createQuery(QismtPensionAmount.class);

		Root<QismtPensionAmount> root = cq.from(QismtPensionAmount.class);

		cq.select(root);

		List<Predicate> listpredicate = new ArrayList<>();

		listpredicate.add(cb.equal(root.get(QismtPensionAmount_.qismtPensionAmountPK)
				.get(QismtPensionAmountPK_.histId), historyId));

		cq.where(listpredicate.toArray(new Predicate[] {}));

		cq.orderBy(cb.asc(root.get(QismtPensionAmount_.qismtPensionAmountPK)
				.get(QismtPensionAmountPK_.pensionGrade)));

		TypedQuery<QismtPensionAmount> query = em.createQuery(cq);

		List<PensionAvgearn> listPensionAvgearn = query.getResultList().stream()
				.map(entity -> toDomain(entity.getQismtPensionAvgearnD(), entity))
				.collect(Collectors.toList());

		return listPensionAvgearn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnRepository#findbyOfficeCode(java.lang.String,
	 * java.util.List)
	 */
	@Override
	public List<PensionAvgearn> findbyOfficeCodes(String companyCode, List<String> officeCodes) {

		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<QismtPensionAmount> cq = cb.createQuery(QismtPensionAmount.class);

		Root<QismtPensionAmount> root = cq.from(QismtPensionAmount.class);

		cq.select(root);

		List<Predicate> listpredicate = new ArrayList<>();

		listpredicate.add(cb.equal(root.get(QismtPensionAmount_.ccd), companyCode));
		listpredicate.add(root.get(QismtPensionAmount_.siOfficeCd).in(officeCodes));

		cq.where(listpredicate.toArray(new Predicate[] {}));

		cq.orderBy(cb.asc(root.get(QismtPensionAmount_.qismtPensionAmountPK)
				.get(QismtPensionAmountPK_.pensionGrade)));

		TypedQuery<QismtPensionAmount> query = em.createQuery(cq);

		List<PensionAvgearn> listPensionAvgearn = query.getResultList().stream()
				.map(entity -> toDomain(entity.getQismtPensionAvgearnD(), entity))
				.collect(Collectors.toList());

		return listPensionAvgearn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.
	 * PensionAvgearnRepository#find(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.Integer)
	 */
	@Override
	public Optional<PensionAvgearn> find(String histId, String ccd, String officeCd,
			Integer pensionGrade) {
		return this.queryProxy()
				.find(new QismtPensionAmountPK(histId, BigDecimal.valueOf(pensionGrade)),
						QismtPensionAmount.class)
				.map(entity -> toDomain(entity.getQismtPensionAvgearnD(), entity));
	}

	/**
	 * To domain.
	 *
	 * @param avgearnDetail
	 *            the entity
	 * @return the pension avgearn
	 */
	private PensionAvgearn toDomain(QismtPensionAvgearnD avgearnDetail,
			QismtPensionAmount pensionAmount) {
		PensionAvgearn domain = new PensionAvgearn(
				new JpaPensionAvgearnGetMemento(avgearnDetail, pensionAmount));
		return domain;
	}

}
