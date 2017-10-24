/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.monthlyattendanceitem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.monthlyattendanceitem.MonthlyAttendanceItem;
import nts.uk.ctx.at.record.dom.monthlyattendanceitem.MonthlyAttendanceItemAtr;
import nts.uk.ctx.at.record.dom.monthlyattendanceitem.MonthlyAttendanceItemRepository;
import nts.uk.ctx.at.record.infra.entity.monthlyattendanceitem.KrcmtMonAttendanceItem;
import nts.uk.ctx.at.record.infra.entity.monthlyattendanceitem.KrcmtMonAttendanceItemPK_;
import nts.uk.ctx.at.record.infra.entity.monthlyattendanceitem.KrcmtMonAttendanceItem_;

/**
 * The Class JpaMonthlyAttendanceItemRepository.
 */
@Stateless
public class JpaMonthlyAttendanceItemRepository extends JpaRepository implements MonthlyAttendanceItemRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.monthlyattendanceitem.
	 * MonthlyAttendanceItemRepository#findByAtr(java.lang.String, int)
	 */
	@Override
	public List<MonthlyAttendanceItem> findByAtr(String companyId, MonthlyAttendanceItemAtr itemAtr) {
		// get entity manager
		EntityManager em = this.getEntityManager();

		// create query
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcmtMonAttendanceItem> cq = criteriaBuilder.createQuery(KrcmtMonAttendanceItem.class);

		// select from table
		Root<KrcmtMonAttendanceItem> root = cq.from(KrcmtMonAttendanceItem.class);
		cq.select(root);

		// add conditions
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KrcmtMonAttendanceItem_.krcmtMonAttendanceItemPK).get(KrcmtMonAttendanceItemPK_.cid),
				companyId));
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(KrcmtMonAttendanceItem_.mAtdItemAtr), itemAtr));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// Get results
		return em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.monthlyattendanceitem.
	 * MonthlyAttendanceItemRepository#findAll(java.lang.String)
	 */
	@Override
	public List<MonthlyAttendanceItem> findAll(String companyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();

		// create query
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcmtMonAttendanceItem> cq = criteriaBuilder.createQuery(KrcmtMonAttendanceItem.class);

		// select from table
		Root<KrcmtMonAttendanceItem> root = cq.from(KrcmtMonAttendanceItem.class);
		cq.select(root);

		// add conditions
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KrcmtMonAttendanceItem_.krcmtMonAttendanceItemPK).get(KrcmtMonAttendanceItemPK_.cid),
				companyId));
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// Get results
		return em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
				.collect(Collectors.toList());
	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the monthly attendance item
	 */
	private MonthlyAttendanceItem toDomain(KrcmtMonAttendanceItem entity) {
		return new MonthlyAttendanceItem(new JpaMonthlyAttendanceItemGetMemento(entity));
	}
}
