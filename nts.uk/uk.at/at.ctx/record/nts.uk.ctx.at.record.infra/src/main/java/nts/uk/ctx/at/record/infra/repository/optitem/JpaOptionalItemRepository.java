/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.optitem;

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
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.infra.entity.optitem.KrcstOptionalItem;
import nts.uk.ctx.at.record.infra.entity.optitem.KrcstOptionalItemPK;
import nts.uk.ctx.at.record.infra.entity.optitem.KrcstOptionalItemPK_;
import nts.uk.ctx.at.record.infra.entity.optitem.KrcstOptionalItem_;

/**
 * The Class JpaOptionalItemRepository.
 */
@Stateless
public class JpaOptionalItemRepository extends JpaRepository implements OptionalItemRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository#update(nts.uk.ctx
	 * .at.record.dom.optitem.OptionalItem)
	 */
	@Override
	public void update(OptionalItem dom) {

		// Find entity
		KrcstOptionalItem entity = this.queryProxy().find(
				new KrcstOptionalItemPK(dom.getCompanyId().v(), dom.getOptionalItemNo().v()),
				KrcstOptionalItem.class).get();

		// Update entity
		dom.saveToMemento(new JpaOptionalItemSetMemento(entity));
		this.commandProxy().update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository#find(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public OptionalItem find(String companyId, String optionalItemNo) {
		KrcstOptionalItem entity = this.queryProxy()
				.find(new KrcstOptionalItemPK(companyId, optionalItemNo), KrcstOptionalItem.class).get();

		// Return
		return new OptionalItem(new JpaOptionalItemGetMemento(entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository#findAll(java.lang
	 * .String)
	 */
	@Override
	public List<OptionalItem> findAll(String companyId) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<KrcstOptionalItem> cq = builder.createQuery(KrcstOptionalItem.class);

		// From table
		Root<KrcstOptionalItem> root = cq.from(KrcstOptionalItem.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Add where condition
		predicateList.add(builder.equal(
				root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.cid),
				companyId));
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get results
		List<KrcstOptionalItem> results = em.createQuery(cq).getResultList();

		// Check empty
		if (CollectionUtil.isEmpty(results)) {
			return Collections.emptyList();
		}

		// Return
		return results.stream().map(item -> new OptionalItem(new JpaOptionalItemGetMemento(item)))
				.collect(Collectors.toList());
	}

	@Override
	public List<OptionalItem> findByAtr(String companyId, int atr) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<KrcstOptionalItem> cq = builder.createQuery(KrcstOptionalItem.class);

		// From table
		Root<KrcstOptionalItem> root = cq.from(KrcstOptionalItem.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Add where condition
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.cid),
				companyId));
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.optionalItemAtr), atr));
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get results
		List<KrcstOptionalItem> results = em.createQuery(cq).getResultList();

		// Check empty
		if (CollectionUtil.isEmpty(results)) {
			return Collections.emptyList();
		}

		// Return
		return results.stream().map(item -> new OptionalItem(new JpaOptionalItemGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository#findByListNos(
	 * java.lang.String, java.util.List)
	 */
	@Override
	public List<OptionalItem> findByListNos(String companyId, List<String> optionalitemNos) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<KrcstOptionalItem> cq = builder.createQuery(KrcstOptionalItem.class);

		// From table
		Root<KrcstOptionalItem> root = cq.from(KrcstOptionalItem.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Add where condition
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.cid),
				companyId));
		predicateList.add(root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.optionalItemNo)
				.in(optionalitemNos));
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get results
		List<KrcstOptionalItem> results = em.createQuery(cq).getResultList();

		// Check empty
		if (CollectionUtil.isEmpty(results)) {
			return Collections.emptyList();
		}

		// Return
		return results.stream().map(item -> new OptionalItem(new JpaOptionalItemGetMemento(item)))
				.collect(Collectors.toList());
	}

}
