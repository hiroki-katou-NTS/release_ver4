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
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRef;
import nts.uk.ctx.pr.core.dom.wagetable.reference.WtMasterRefRepository;
import nts.uk.ctx.pr.core.infra.entity.wagetable.reference.QwtmtWagetableRefTable;
import nts.uk.ctx.pr.core.infra.entity.wagetable.reference.QwtmtWagetableRefTablePK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.reference.QwtmtWagetableRefTable_;

/**
 * The Class JpaWageTableMasterRefRepository.
 */
@Stateless
public class JpaWtRefRepository extends JpaRepository implements WtMasterRefRepository {

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.wagetable.history.WageTableHistoryRepository#
	 * findAll(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public List<WtMasterRef> findAll(String companyCode) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QwtmtWagetableRefTable> cq = cb.createQuery(QwtmtWagetableRefTable.class);
		Root<QwtmtWagetableRefTable> root = cq.from(QwtmtWagetableRefTable.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QwtmtWagetableRefTable_.qwtmtWagetableRefTablePK)
				.get(QwtmtWagetableRefTablePK_.ccd), companyCode));

		// Add where clause
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Return
		return em.createQuery(cq).getResultList().stream()
				.map(item -> new WtMasterRef(new JpaWtMasterRefGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.wagetable.reference.WageTableMasterRefRepository#
	 * findByCode(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<WtMasterRef> findByCode(String companyCode, String code) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QwtmtWagetableRefTable> cq = cb.createQuery(QwtmtWagetableRefTable.class);
		Root<QwtmtWagetableRefTable> root = cq.from(QwtmtWagetableRefTable.class);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(root.get(QwtmtWagetableRefTable_.qwtmtWagetableRefTablePK)
				.get(QwtmtWagetableRefTablePK_.ccd), companyCode));
		predicateList.add(cb.equal(root.get(QwtmtWagetableRefTable_.qwtmtWagetableRefTablePK)
				.get(QwtmtWagetableRefTablePK_.refTableNo), code));

		// Add where clause
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get result
		List<QwtmtWagetableRefTable> result = em.createQuery(cq).getResultList();

		// Check empty
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// Return
		return Optional
				.of(result.stream().map(item -> new WtMasterRef(new JpaWtMasterRefGetMemento(item)))
						.collect(Collectors.toList()).get(0));
	}

}
