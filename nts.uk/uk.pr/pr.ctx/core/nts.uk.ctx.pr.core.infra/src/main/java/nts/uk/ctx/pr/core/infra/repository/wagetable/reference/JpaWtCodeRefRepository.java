/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.reference;

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
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRef;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtCodeRefRepository;
import nts.uk.ctx.pr.core.infra.entity.wagetable.reference.QwtmtWagetableRefCd;
import nts.uk.ctx.pr.core.infra.entity.wagetable.reference.QwtmtWagetableRefCdPK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.reference.QwtmtWagetableRefCd_;

/**
 * The Class JpaWageTableCodeRefRepository.
 */
@Stateless
public class JpaWtCodeRefRepository extends JpaRepository implements WtCodeRefRepository {

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistoryRepository#
	 * findAll(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public List<WtCodeRef> findAll(String companyCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QwtmtWagetableRefCd> cq = cb.createQuery(QwtmtWagetableRefCd.class);
		Root<QwtmtWagetableRefCd> root = cq.from(QwtmtWagetableRefCd.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QwtmtWagetableRefCd_.qwtmtWagetableRefCdPK)
				.get(QwtmtWagetableRefCdPK_.ccd), companyCode));

		// Add where clause
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Return
		return em.createQuery(cq).getResultList().stream()
				.map(item -> new WtCodeRef(new JpaWtCodeRefGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableCodeRefRepository#
	 * findByCode(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<WtCodeRef> findByCode(String companyCode, String code) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QwtmtWagetableRefCd> cq = cb.createQuery(QwtmtWagetableRefCd.class);
		Root<QwtmtWagetableRefCd> root = cq.from(QwtmtWagetableRefCd.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QwtmtWagetableRefCd_.qwtmtWagetableRefCdPK)
				.get(QwtmtWagetableRefCdPK_.ccd), companyCode));
		predicateList.add(cb.equal(root.get(QwtmtWagetableRefCd_.qwtmtWagetableRefCdPK)
				.get(QwtmtWagetableRefCdPK_.refCdNo), code));

		// Add where clause
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get result
		List<QwtmtWagetableRefCd> result = em.createQuery(cq).getResultList();

		// Check empty.
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// Return
		return Optional
				.of(result.stream().map(item -> new WtCodeRef(new JpaWtCodeRefGetMemento(item)))
						.collect(Collectors.toList()).get(0));
	}

}
