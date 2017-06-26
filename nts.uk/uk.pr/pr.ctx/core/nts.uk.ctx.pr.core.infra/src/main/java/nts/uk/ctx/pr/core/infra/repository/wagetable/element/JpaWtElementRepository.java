/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.wagetable.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.ctx.pr.core.dom.wagetable.element.WtElementRepository;
import nts.uk.ctx.pr.core.infra.entity.wagetable.QwtmtWagetableHead;
import nts.uk.ctx.pr.core.infra.entity.wagetable.QwtmtWagetableHead_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElement;
import nts.uk.ctx.pr.core.infra.entity.wagetable.element.QwtmtWagetableElement_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHist;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHistPK_;
import nts.uk.ctx.pr.core.infra.entity.wagetable.history.QwtmtWagetableHist_;

/**
 * The Class JpaWtElementRepository.
 */
@Stateless
public class JpaWtElementRepository extends JpaRepository implements WtElementRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.wagetable.element.WtElementRepository#
	 * findByHistoryId(java.lang.String)
	 */
	@Override
	public Optional<WtElement> findByHistoryId(String hitsoryId) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Query for.
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QwtmtWagetableElement> cq = cb.createQuery(QwtmtWagetableElement.class);
		Root<QwtmtWagetableElement> root = cq.from(QwtmtWagetableElement.class);
		Join<QwtmtWagetableElement, QwtmtWagetableHead> headRoot = root
				.join(QwtmtWagetableElement_.qwtmtWagetableHead, JoinType.LEFT);
		Join<QwtmtWagetableHead, QwtmtWagetableHist> histRoot = headRoot
				.join(QwtmtWagetableHead_.wagetableHistList, JoinType.LEFT);

		// Constructing list of parameters
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Construct condition.
		predicateList.add(cb.equal(histRoot.get(QwtmtWagetableHist_.qwtmtWagetableHistPK)
				.get(QwtmtWagetableHistPK_.histId), hitsoryId));

		// Add where clause
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get result
		List<QwtmtWagetableElement> result = em.createQuery(cq).getResultList();

		// Check empty.
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// Return
		return Optional.of(new WtElement(new JpaWtElementGetMemento(result.get(0))));
	}

}
