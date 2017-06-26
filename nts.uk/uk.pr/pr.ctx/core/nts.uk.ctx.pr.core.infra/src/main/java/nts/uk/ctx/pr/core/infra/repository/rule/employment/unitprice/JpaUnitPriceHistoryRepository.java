/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.rule.employment.unitprice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistory;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistoryRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceDetail;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceDetailPK_;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceDetail_;

/**
 * The Class JpaUnitPriceHistoryRepository.
 */
@Stateless
public class JpaUnitPriceHistoryRepository extends JpaRepository implements UnitPriceHistoryRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * deleteHistory(java.lang.String)
	 */
	@Override
	public void deleteHistory(String histId) {
		EntityManager em = this.getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		// create delete
		CriteriaDelete<QupmtCUnitpriceDetail> delete = cb.createCriteriaDelete(QupmtCUnitpriceDetail.class);

		// set the root class
		Root<QupmtCUnitpriceDetail> root = delete.from(QupmtCUnitpriceDetail.class);

		// set where clause
		delete.where(cb.equal(
				root.get(QupmtCUnitpriceDetail_.qupmtCUnitpriceDetailPK).get(QupmtCUnitpriceDetailPK_.histId), histId));

		// perform update
		em.createQuery(delete).executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryRepository#findAll(int)
	 */
	@Override
	public List<UnitPriceHistory> findAll(CompanyCode companyCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for indicated stress check.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QupmtCUnitpriceDetail> cq = cb.createQuery(QupmtCUnitpriceDetail.class);
		Root<QupmtCUnitpriceDetail> root = cq.from(QupmtCUnitpriceDetail.class);
		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(
				cb.equal(root.get(QupmtCUnitpriceDetail_.qupmtCUnitpriceDetailPK).get(QupmtCUnitpriceDetailPK_.ccd),
						companyCode.v()));

		cq.orderBy(cb.desc(root.get(QupmtCUnitpriceDetail_.strYm)));
		cq.where(predicateList.toArray(new Predicate[] {}));

		return em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findLastestHistoryByMasterCode(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<UnitPriceHistory> findLastestHistoryByMasterCode(String companyCode, String unitPriceCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for indicated stress check.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QupmtCUnitpriceDetail> cq = cb.createQuery(QupmtCUnitpriceDetail.class);
		Root<QupmtCUnitpriceDetail> root = cq.from(QupmtCUnitpriceDetail.class);
		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(
				cb.equal(root.get(QupmtCUnitpriceDetail_.qupmtCUnitpriceDetailPK).get(QupmtCUnitpriceDetailPK_.ccd),
						companyCode));
		predicateList.add(cb.equal(
				root.get(QupmtCUnitpriceDetail_.qupmtCUnitpriceDetailPK).get(QupmtCUnitpriceDetailPK_.cUnitpriceCd),
				unitPriceCode));

		cq.orderBy(cb.desc(root.get(QupmtCUnitpriceDetail_.strYm)));

		cq.where(predicateList.toArray(new Predicate[] {}));

		List<QupmtCUnitpriceDetail> result = em.createQuery(cq).getResultList();

		// Check empty.
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		return Optional.of(this.toDomain(result.get(0)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findAllHistoryByMasterCode(java.lang.String, java.lang.String)
	 */
	@Override
	public List<UnitPriceHistory> findAllHistoryByMasterCode(String companyCode, String masterCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for indicated stress check.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QupmtCUnitpriceDetail> cq = cb.createQuery(QupmtCUnitpriceDetail.class);
		Root<QupmtCUnitpriceDetail> root = cq.from(QupmtCUnitpriceDetail.class);
		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(
				cb.equal(root.get(QupmtCUnitpriceDetail_.qupmtCUnitpriceDetailPK).get(QupmtCUnitpriceDetailPK_.ccd),
						companyCode));
		predicateList.add(cb.equal(
				root.get(QupmtCUnitpriceDetail_.qupmtCUnitpriceDetailPK).get(QupmtCUnitpriceDetailPK_.cUnitpriceCd),
				masterCode));

		cq.orderBy(cb.desc(root.get(QupmtCUnitpriceDetail_.strYm)));
		cq.where(predicateList.toArray(new Predicate[] {}));

		return em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findHistoryByUuid(java.lang.String)
	 */
	@Override
	public Optional<UnitPriceHistory> findHistoryByUuid(String uuid) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for indicated stress check.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QupmtCUnitpriceDetail> cq = cb.createQuery(QupmtCUnitpriceDetail.class);
		Root<QupmtCUnitpriceDetail> root = cq.from(QupmtCUnitpriceDetail.class);
		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(
				root.get(QupmtCUnitpriceDetail_.qupmtCUnitpriceDetailPK).get(QupmtCUnitpriceDetailPK_.histId), uuid));
		cq.where(predicateList.toArray(new Predicate[] {}));

		List<QupmtCUnitpriceDetail> result = em.createQuery(cq).getResultList();

		// Check empty.
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// Return
		return Optional.of(this.toDomain(result.get(0)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * addHistory(nts.uk.ctx.pr.core.dom.base.simplehistory.History)
	 */
	@Override
	public void addHistory(UnitPriceHistory history) {
		this.commandProxy().update(this.toEntity(history));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * updateHistory(nts.uk.ctx.pr.core.dom.base.simplehistory.History)
	 */
	@Override
	public void updateHistory(UnitPriceHistory unitPriceHistory) {
		this.commandProxy().update(this.toEntity(unitPriceHistory));
	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the unit price history
	 */
	private UnitPriceHistory toDomain(QupmtCUnitpriceDetail entity) {
		UnitPriceHistory domain = new UnitPriceHistory(new JpaUnitPriceHistoryGetMemento(entity));
		return domain;

	}

	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the qupmt C unitprice detail
	 */
	private QupmtCUnitpriceDetail toEntity(UnitPriceHistory domain) {
		QupmtCUnitpriceDetail entity = new QupmtCUnitpriceDetail();
		domain.saveToMemento(new JpaUnitPriceHistorySetMemento(entity));
		return entity;
	}

}
