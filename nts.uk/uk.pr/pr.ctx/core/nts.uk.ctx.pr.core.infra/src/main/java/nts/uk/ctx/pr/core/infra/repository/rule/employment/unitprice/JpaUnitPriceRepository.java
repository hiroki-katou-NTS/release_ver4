/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceCode;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceDetail;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceDetail_;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceHeader;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceHeaderPK;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceHeaderPK_;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceHeader_;

/**
 * The Class JpaUnitPriceRepository.
 */
@Stateless
public class JpaUnitPriceRepository extends JpaRepository implements UnitPriceRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository#add(
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPrice)
	 */
	@Override
	public void add(UnitPrice unitPrice) {
		QupmtCUnitpriceHeader entity = new QupmtCUnitpriceHeader();
		unitPrice.saveToMemento(new JpaUnitPriceSetMemento(entity));
		this.commandProxy().insert(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository#
	 * update(nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPrice)
	 */
	@Override
	public void update(UnitPrice unitPrice) {
		QupmtCUnitpriceHeader entity = new QupmtCUnitpriceHeader();
		unitPrice.saveToMemento(new JpaUnitPriceSetMemento(entity));
		this.commandProxy().update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository#
	 * remove(java.lang.String, java.lang.Long)
	 */
	@Override
	public void remove(String companyCode, UnitPriceCode code) {
		this.commandProxy().remove(QupmtCUnitpriceHeader.class,
				new QupmtCUnitpriceHeaderPK(companyCode, code.v()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository#
	 * findAll(int)
	 */
	@Override
	public List<UnitPrice> findAll(String companyCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for indicated stress check.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QupmtCUnitpriceHeader> cq = cb.createQuery(QupmtCUnitpriceHeader.class);
		Root<QupmtCUnitpriceHeader> root = cq.from(QupmtCUnitpriceHeader.class);
		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QupmtCUnitpriceHeader_.qupmtCUnitpriceHeaderPK)
				.get(QupmtCUnitpriceHeaderPK_.ccd), companyCode));

		cq.orderBy(cb.asc(root.get(QupmtCUnitpriceHeader_.qupmtCUnitpriceHeaderPK)
				.get(QupmtCUnitpriceHeaderPK_.cUnitpriceCd)));
		cq.where(predicateList.toArray(new Predicate[] {}));

		return em.createQuery(cq).getResultList().stream()
				.map(item -> new UnitPrice(new JpaUnitPriceGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository#
	 * findById(java.lang.String)
	 */
	@Override
	public Optional<UnitPrice> findByCode(String companyCode, UnitPriceCode code) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		QupmtCUnitpriceHeader entity = em.find(QupmtCUnitpriceHeader.class,
				new QupmtCUnitpriceHeaderPK(companyCode, code.v()));

		// Return
		return Optional.ofNullable(new UnitPrice(new JpaUnitPriceGetMemento(entity)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository#
	 * isDuplicateCode(nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceCode)
	 */
	@Override
	public boolean isDuplicateCode(String companyCode, UnitPriceCode code) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create query condition.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<QupmtCUnitpriceHeader> root = cq.from(QupmtCUnitpriceHeader.class);
		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QupmtCUnitpriceHeader_.qupmtCUnitpriceHeaderPK)
				.get(QupmtCUnitpriceHeaderPK_.ccd), companyCode));
		predicateList.add(cb.equal(root.get(QupmtCUnitpriceHeader_.qupmtCUnitpriceHeaderPK)
				.get(QupmtCUnitpriceHeaderPK_.cUnitpriceCd), code.v()));

		cq.select(cb.count(root));
		cq.where(predicateList.toArray(new Predicate[] {}));

		return em.createQuery(cq).getSingleResult().longValue() > 0L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository#
	 * getCompanyUnitPriceCode(java.lang.Integer)
	 */
	@Override
	public List<UnitPrice> getCompanyUnitPrice(Integer baseDate) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QupmtCUnitpriceDetail> cq = cb.createQuery(QupmtCUnitpriceDetail.class);
		Root<QupmtCUnitpriceDetail> root = cq.from(QupmtCUnitpriceDetail.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.le(root.get(QupmtCUnitpriceDetail_.strYm), baseDate));
		predicateList.add(cb.ge(root.get(QupmtCUnitpriceDetail_.endYm), baseDate));

		cq.where(predicateList.toArray(new Predicate[] {}));
		List<QupmtCUnitpriceDetail> list = em.createQuery(cq).getResultList();

		return list.stream().map(item -> {
			return new UnitPrice(new JpaUnitPriceGetMemento(item.getQupmtCUnitpriceHeader()));
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository#
	 * findByCodes(java.lang.String, java.util.List)
	 */
	@Override
	public List<UnitPrice> findByCodes(String companyCode, List<String> unitPriceCodes) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create query condition.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QupmtCUnitpriceHeader> cq = cb.createQuery(QupmtCUnitpriceHeader.class);
		Root<QupmtCUnitpriceHeader> root = cq.from(QupmtCUnitpriceHeader.class);
		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QupmtCUnitpriceHeader_.qupmtCUnitpriceHeaderPK)
				.get(QupmtCUnitpriceHeaderPK_.ccd), companyCode));
		predicateList.add(cb.and(root.get(QupmtCUnitpriceHeader_.qupmtCUnitpriceHeaderPK)
				.get(QupmtCUnitpriceHeaderPK_.cUnitpriceCd).in(unitPriceCodes)));

		cq.where(predicateList.toArray(new Predicate[] {}));
		List<QupmtCUnitpriceHeader> list = em.createQuery(cq).getResultList();

		return list.stream().map(item -> {
			return new UnitPrice(new JpaUnitPriceGetMemento(item));
		}).collect(Collectors.toList());
	}

}
