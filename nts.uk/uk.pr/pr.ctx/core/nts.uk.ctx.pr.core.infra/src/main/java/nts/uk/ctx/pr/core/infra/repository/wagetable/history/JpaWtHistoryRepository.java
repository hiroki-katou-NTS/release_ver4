/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.history;

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

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistory;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryRepository;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableCd;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableCdPK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableCd_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableEleHist;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableEleHistPK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableEleHist_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHist;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHistPK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHist_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableMny;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableMnyPK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableMny_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableNum;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableNumPK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableNum_;
import nts.uk.ctx.pr.core.infra.repository.base.JpaSimpleHistoryBase;

/**
 * The Class JpaWageTableHistoryRepository.
 */
@Stateless
public class JpaWtHistoryRepository extends JpaSimpleHistoryBase implements WtHistoryRepository {

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistoryRepository#
	 * findAll(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public List<WtHistory> findAll(String companyCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QwtmtWagetableHist> cq = cb.createQuery(QwtmtWagetableHist.class);
		Root<QwtmtWagetableHist> root = cq.from(QwtmtWagetableHist.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(
				root.get(QwtmtWagetableHist_.qwtmtWagetableHistPK).get(QwtmtWagetableHistPK_.ccd),
				companyCode));

		// Add where clause
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Add order clause
		cq.orderBy(cb.desc(root.get(QwtmtWagetableHist_.strYm)));

		// Return
		return em.createQuery(cq).getResultList().stream()
				.map(item -> new WtHistory(new JpaWtHistoryGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * deleteHistory(java.lang.String)
	 */
	@Override
	public void deleteHistory(String uuid) {
		// Delete base.
		this.deleteByUuid(QwtmtWagetableHist.class, uuid, QwtmtWagetableHist_.qwtmtWagetableHistPK,
				QwtmtWagetableHistPK_.histId);

		// Delete element.
		this.deleteByUuid(QwtmtWagetableEleHist.class, uuid,
				QwtmtWagetableEleHist_.qwtmtWagetableEleHistPK, QwtmtWagetableEleHistPK_.histId);

		// Delete ref code.
		this.deleteByUuid(QwtmtWagetableCd.class, uuid, QwtmtWagetableCd_.qwtmtWagetableCdPK,
				QwtmtWagetableCdPK_.histId);

		// Delete interval.
		this.deleteByUuid(QwtmtWagetableNum.class, uuid, QwtmtWagetableNum_.qwtmtWagetableNumPK,
				QwtmtWagetableNumPK_.histId);

		// Delete money.
		this.deleteByUuid(QwtmtWagetableMny.class, uuid, QwtmtWagetableMny_.qwtmtWagetableMnyPK,
				QwtmtWagetableMnyPK_.histId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findLastestHistoryByMasterCode(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<WtHistory> findLastestHistoryByMasterCode(String companyCode,
			String masterCode) {
		// Return first element
		return this.findByIndex(0, companyCode, masterCode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findHistoryByUuid(java.lang.String)
	 */
	@Override
	public Optional<WtHistory> findHistoryByUuid(String uuid) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QwtmtWagetableHist> cq = cb.createQuery(QwtmtWagetableHist.class);
		Root<QwtmtWagetableHist> root = cq.from(QwtmtWagetableHist.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QwtmtWagetableHist_.qwtmtWagetableHistPK)
				.get(QwtmtWagetableHistPK_.histId), uuid));

		// Add where clause
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get result
		List<QwtmtWagetableHist> result = em.createQuery(cq).getResultList();

		// Check empty.
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// Return
		return Optional.of(new WtHistory(new JpaWtHistoryGetMemento(result.get(0))));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * addHistory(nts.uk.ctx.pr.core.dom.base.simplehistory.History)
	 */
	@Override
	public void addHistory(WtHistory history) {
		QwtmtWagetableHist entity = new QwtmtWagetableHist();
		history.saveToMemento(new JpaWtHistorySetMemento(entity));
		this.commandProxy().update(entity);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * updateHistory(nts.uk.ctx.pr.core.dom.base.simplehistory.History)
	 */
	@Override
	public void updateHistory(WtHistory unitPriceHistory) {
		QwtmtWagetableHist entity = new QwtmtWagetableHist();
		unitPriceHistory.saveToMemento(new JpaWtHistorySetMemento(entity));
		this.commandProxy().update(entity);
	}

	/**
	 * Find by index.
	 *
	 * @param index
	 *            the index
	 * @param companyCode
	 *            the company code
	 * @param wageTableCode
	 *            the wage table code
	 * @return the optional
	 */
	private Optional<WtHistory> findByIndex(int index, String companyCode, String wageTableCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for indicated stress check.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QwtmtWagetableHist> cq = cb.createQuery(QwtmtWagetableHist.class);
		Root<QwtmtWagetableHist> root = cq.from(QwtmtWagetableHist.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(
				root.get(QwtmtWagetableHist_.qwtmtWagetableHistPK).get(QwtmtWagetableHistPK_.ccd),
				companyCode));
		predicateList.add(cb.equal(root.get(QwtmtWagetableHist_.qwtmtWagetableHistPK)
				.get(QwtmtWagetableHistPK_.wageTableCd), wageTableCode));

		// Add where clause
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Add order clause
		cq.orderBy(cb.desc(root.get(QwtmtWagetableHist_.strYm)));

		// Get result
		List<QwtmtWagetableHist> result = em.createQuery(cq).getResultList();

		// Check empty.
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// Return
		return Optional.of(new WtHistory(new JpaWtHistoryGetMemento(result.get(index))));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistoryRepository#
	 * isValidDateRange(nts.uk.ctx.core.dom.company.CompanyCode,
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceCode,
	 * nts.arc.time.YearMonth)
	 */
	@Override
	public boolean isValidDateRange(String companyCode, String wageTableCode, Integer startMonth) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create query condition.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<QwtmtWagetableHist> root = cq.from(QwtmtWagetableHist.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(
				root.get(QwtmtWagetableHist_.qwtmtWagetableHistPK).get(QwtmtWagetableHistPK_.ccd),
				companyCode));
		predicateList.add(cb.equal(root.get(QwtmtWagetableHist_.qwtmtWagetableHistPK)
				.get(QwtmtWagetableHistPK_.wageTableCd), wageTableCode));
		predicateList.add(cb.ge(root.get(QwtmtWagetableHist_.strYm), startMonth));

		cq.select(cb.count(root));

		// Add where clause
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Return
		return em.createQuery(cq).getSingleResult().longValue() == 0L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository#
	 * findAllHistoryByMasterCode(java.lang.String, java.lang.String)
	 */
	@Override
	public List<WtHistory> findAllHistoryByMasterCode(String companyCode, String masterCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for indicated stress check.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QwtmtWagetableHist> cq = cb.createQuery(QwtmtWagetableHist.class);
		Root<QwtmtWagetableHist> root = cq.from(QwtmtWagetableHist.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(
				root.get(QwtmtWagetableHist_.qwtmtWagetableHistPK).get(QwtmtWagetableHistPK_.ccd),
				companyCode));
		predicateList.add(cb.equal(root.get(QwtmtWagetableHist_.qwtmtWagetableHistPK)
				.get(QwtmtWagetableHistPK_.wageTableCd), masterCode));

		// Add order clause
		cq.orderBy(cb.desc(root.get(QwtmtWagetableHist_.strYm)));

		// Add where clause
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Return
		return em.createQuery(cq).getResultList().stream()
				.map(item -> new WtHistory(new JpaWtHistoryGetMemento(item)))
				.collect(Collectors.toList());
	}

}
