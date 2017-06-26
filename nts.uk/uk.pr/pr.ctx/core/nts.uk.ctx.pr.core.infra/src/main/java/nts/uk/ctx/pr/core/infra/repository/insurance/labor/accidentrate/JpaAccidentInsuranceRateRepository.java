/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.accidentrate;

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
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate.QismtWorkAccidentInsu;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate.QismtWorkAccidentInsuPK_;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.accidentrate.QismtWorkAccidentInsu_;

/**
 * The Class JpaAccidentInsuranceRateRepository.
 */
@Stateless
public class JpaAccidentInsuranceRateRepository extends JpaRepository
	implements AccidentInsuranceRateRepository {
	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateRepository#add(nts.uk.ctx.pr.core.dom.insurance.
	 * labor.accidentrate.AccidentInsuranceRate)
	 */
	@Override
	public void add(AccidentInsuranceRate rate) {
		this.commandProxy().insertAll(this.toEntity(rate));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateRepository#update(nts.uk.ctx.pr.core.dom.insurance.
	 * labor.accidentrate.AccidentInsuranceRate)
	 */
	@Override
	public void update(AccidentInsuranceRate rate) {
		this.commandProxy().updateAll(this.toEntity(rate));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateRepository#remove(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void remove(String historyId) {
		List<QismtWorkAccidentInsu> lstRateRemove = this.findDataById(historyId);
		this.commandProxy().removeAll(lstRateRemove);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateRepository#findAll(int)
	 */
	@Override
	public List<AccidentInsuranceRate> findAll(String companyCode) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QISMT_WORK_ACCIDENT_INSU (QismtWorkAccidentInsu SQL)
		CriteriaQuery<QismtWorkAccidentInsu> cq = criteriaBuilder
			.createQuery(QismtWorkAccidentInsu.class);

		// root data
		Root<QismtWorkAccidentInsu> root = cq.from(QismtWorkAccidentInsu.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq String
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(QismtWorkAccidentInsu_.qismtWorkAccidentInsuPK)
				.get(QismtWorkAccidentInsuPK_.ccd), companyCode));

		// eq type business 1st
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(QismtWorkAccidentInsu_.qismtWorkAccidentInsuPK)
				.get(QismtWorkAccidentInsuPK_.waInsuCd), BusinessTypeEnum.Biz1St.value));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// set order
		cq.orderBy(criteriaBuilder.desc(root.get(QismtWorkAccidentInsu_.strYm)));

		// exclude select
		TypedQuery<QismtWorkAccidentInsu> query = em.createQuery(cq);

		// Return
		return query.getResultList().stream().map(item -> this.toDomainHistory(item))
			.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateRepository#findById(java.lang.String)
	 */
	@Override
	public Optional<AccidentInsuranceRate> findById(String historyId) {
		return Optional.ofNullable(this.toDomain(this.findDataById(historyId)));
	}

	/**
	 * Find data by id.
	 *
	 * @param companyCode
	 *            the company code
	 * @param historyId
	 *            the history id
	 * @return the list
	 */
	public List<QismtWorkAccidentInsu> findDataById(String historyId) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QISMT_WORK_ACCIDENT_INSU (QismtWorkAccidentInsu SQL)
		CriteriaQuery<QismtWorkAccidentInsu> cq = criteriaBuilder
			.createQuery(QismtWorkAccidentInsu.class);

		// root data
		Root<QismtWorkAccidentInsu> root = cq.from(QismtWorkAccidentInsu.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq historyId
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(QismtWorkAccidentInsu_.qismtWorkAccidentInsuPK)
				.get(QismtWorkAccidentInsuPK_.histId), historyId));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// set order
		TypedQuery<QismtWorkAccidentInsu> query = em.createQuery(cq);

		return query.getResultList();
	}

	/**
	 * To entity.
	 *
	 * @param rate
	 *            the rate
	 * @return the list
	 */
	public List<QismtWorkAccidentInsu> toEntity(AccidentInsuranceRate rate) {
		List<QismtWorkAccidentInsu> lstQismtWorkAccidentInsu = new ArrayList<>();
		for (int index = BusinessTypeEnum.Biz1St.index; index <= BusinessTypeEnum.Biz10Th.index; index++) {
			lstQismtWorkAccidentInsu.add(new QismtWorkAccidentInsu());
		}
		rate.saveToMemento(new JpaAccidentInsuranceRateSetMemento(lstQismtWorkAccidentInsu));
		return lstQismtWorkAccidentInsu;
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the accident insurance rate
	 */
	public AccidentInsuranceRate toDomain(List<QismtWorkAccidentInsu> entity) {
		return new AccidentInsuranceRate(new JpaAccidentInsuranceRateGetMemento(entity));
	}

	/**
	 * To domain history.
	 *
	 * @param entity
	 *            the entity
	 * @return the accident insurance rate
	 */
	public AccidentInsuranceRate toDomainHistory(QismtWorkAccidentInsu entity) {
		return new AccidentInsuranceRate(new JpaAccidentInsuranceHistoryGetMemento(entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateRepository#findFirstData(java.lang.String)
	 */
	@Override
	public Optional<AccidentInsuranceRate> findFirstData(String companyCode) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QISMT_WORK_ACCIDENT_INSU (QismtWorkAccidentInsu SQL)
		CriteriaQuery<QismtWorkAccidentInsu> cq = criteriaBuilder
			.createQuery(QismtWorkAccidentInsu.class);

		// root data
		Root<QismtWorkAccidentInsu> root = cq.from(QismtWorkAccidentInsu.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq String
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(QismtWorkAccidentInsu_.qismtWorkAccidentInsuPK)
				.get(QismtWorkAccidentInsuPK_.ccd), companyCode));

		// eq business type 1st
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(QismtWorkAccidentInsu_.qismtWorkAccidentInsuPK)
				.get(QismtWorkAccidentInsuPK_.waInsuCd), BusinessTypeEnum.Biz1St.value));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// set order
		cq.orderBy(criteriaBuilder.desc(root.get(QismtWorkAccidentInsu_.strYm)));

		// exclude select
		TypedQuery<QismtWorkAccidentInsu> query = em.createQuery(cq);
		List<QismtWorkAccidentInsu> lstQismtWorkAccidentInsu = query.getResultList();

		if (CollectionUtil.isEmpty(lstQismtWorkAccidentInsu)) {
			return Optional.empty();
		}

		return this.findById(lstQismtWorkAccidentInsu.get(BusinessTypeEnum.Biz1St.index)
			.getQismtWorkAccidentInsuPK().getHistId());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.
	 * AccidentInsuranceRateRepository#findBetweenUpdate(nts.uk.ctx.core.dom.
	 * company.String, nts.arc.time.YearMonth, java.lang.String)
	 */
	@Override
	public Optional<AccidentInsuranceRate> findBetweenUpdate(String companyCode,
		YearMonth yearMonth, String historyId) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call QISMT_WORK_ACCIDENT_INSU (QismtWorkAccidentInsu SQL)
		CriteriaQuery<QismtWorkAccidentInsu> cq = criteriaBuilder
			.createQuery(QismtWorkAccidentInsu.class);

		// root data
		Root<QismtWorkAccidentInsu> root = cq.from(QismtWorkAccidentInsu.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq String
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(QismtWorkAccidentInsu_.qismtWorkAccidentInsuPK)
				.get(QismtWorkAccidentInsuPK_.ccd), companyCode));

		// not eq historyId
		lstpredicateWhere
			.add(criteriaBuilder.notEqual(root.get(QismtWorkAccidentInsu_.qismtWorkAccidentInsuPK)
				.get(QismtWorkAccidentInsuPK_.histId), historyId));

		// eq business type 1st
		lstpredicateWhere
			.add(criteriaBuilder.equal(root.get(QismtWorkAccidentInsu_.qismtWorkAccidentInsuPK)
				.get(QismtWorkAccidentInsuPK_.waInsuCd), BusinessTypeEnum.Biz1St.value));

		// le start year month
		lstpredicateWhere
			.add(criteriaBuilder.le(root.get(QismtWorkAccidentInsu_.strYm), yearMonth.v()));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// set order
		cq.orderBy(criteriaBuilder.desc(root.get(QismtWorkAccidentInsu_.strYm)));

		// exclude select
		TypedQuery<QismtWorkAccidentInsu> query = em.createQuery(cq);
		List<QismtWorkAccidentInsu> lstQismtWorkAccidentInsu = query.getResultList();

		// check empty data
		if (CollectionUtil.isEmpty(lstQismtWorkAccidentInsu)) {
			return Optional.empty();
		}

		return this.findById(lstQismtWorkAccidentInsu.get(BusinessTypeEnum.Biz1St.index)
			.getQismtWorkAccidentInsuPK().getHistId());
	}

}
