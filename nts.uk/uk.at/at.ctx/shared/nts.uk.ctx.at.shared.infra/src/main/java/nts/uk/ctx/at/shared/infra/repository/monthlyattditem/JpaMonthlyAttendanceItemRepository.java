/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.monthlyattditem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.query.TypedQueryWrapper;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.monthlyattditem.MonthlyAttendanceItem;
import nts.uk.ctx.at.shared.dom.monthlyattditem.MonthlyAttendanceItemAtr;
import nts.uk.ctx.at.shared.dom.monthlyattditem.MonthlyAttendanceItemRepository;
import nts.uk.ctx.at.shared.infra.entity.monthlyattditem.KrcmtMonAttendanceItem;
import nts.uk.ctx.at.shared.infra.entity.monthlyattditem.KrcmtMonAttendanceItemPK;
import nts.uk.ctx.at.shared.infra.entity.monthlyattditem.KrcmtMonAttendanceItemPK_;
import nts.uk.ctx.at.shared.infra.entity.monthlyattditem.KrcmtMonAttendanceItem_;

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
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(KrcmtMonAttendanceItem_.mAtdItemAtr), itemAtr.value));

		// set order
		cq.orderBy(criteriaBuilder.asc(root.get(KrcmtMonAttendanceItem_.dispNo)));

		// set conditions
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
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<MonthlyAttendanceItem> findByAttendanceItemId(String companyId, List<Integer> attendanceItemIds) {
		StringBuilder builderString = new StringBuilder();	
		if(CollectionUtil.isEmpty(attendanceItemIds))
			return Collections.emptyList();
		builderString.append("SELECT b");
		builderString.append(" FROM KrcmtMonAttendanceItem b");
		builderString.append(" WHERE b.krcmtMonAttendanceItemPK.mAtdItemId IN :attendanceItemIds");
		builderString.append(" AND b.krcmtMonAttendanceItemPK.cid = :companyId");
		
		List<MonthlyAttendanceItem> resultList = new ArrayList<>();
		CollectionUtil.split(attendanceItemIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(builderString.toString(), KrcmtMonAttendanceItem.class)
								  .setParameter("attendanceItemIds", subList)
								  .setParameter("companyId", companyId)
								  .getList(f -> toDomain(f)));
		});
		return resultList;
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<MonthlyAttendanceItem> findByAttendanceItemIdAndAtr(String companyId, List<Integer> attendanceItemIds, 
			List<Integer> itemAtrs) {
		
		List<Integer> listItemIds = new ArrayList<>(Optional.ofNullable(attendanceItemIds).orElse(Collections.emptyList()));
		List<Integer> listAttrs = new ArrayList<>(Optional.ofNullable(itemAtrs).orElse(Collections.emptyList()));
		
		boolean checkAttItems = !CollectionUtil.isEmpty(listItemIds);
		boolean checkItemAtr = !CollectionUtil.isEmpty(listAttrs);
		if (!checkAttItems) listItemIds.add(0);
		if (!checkItemAtr) listAttrs.add(0);
		
		StringBuilder builderString = new StringBuilder();	
		builderString.append("SELECT b");
		builderString.append(" FROM KrcmtMonAttendanceItem b");
		builderString.append(" WHERE");
		builderString.append(" b.krcmtMonAttendanceItemPK.cid = :companyId");
		if (checkAttItems) {
			builderString.append(" AND b.krcmtMonAttendanceItemPK.mAtdItemId IN :attendanceItemIds");
		}
		if (checkItemAtr) {
			builderString.append(" AND b.mAtdItemAtr IN :itemAtrs");
		}
		
		List<MonthlyAttendanceItem> resultList = new ArrayList<>();
		CollectionUtil.split(listItemIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstIds -> {
			CollectionUtil.split(listAttrs, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstAtrs -> {
				TypedQueryWrapper<KrcmtMonAttendanceItem> query = this.queryProxy().query(builderString.toString(), KrcmtMonAttendanceItem.class);
				query.setParameter("companyId", companyId);
				if (checkAttItems) {
					query.setParameter("attendanceItemIds", attendanceItemIds);
				}
				if (checkItemAtr) {
					query.setParameter("itemAtrs", itemAtrs);
				}
				resultList.addAll(query.getList(f -> toDomain(f)));
			});
		});
		return resultList;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<MonthlyAttendanceItem> findByAtrPrimitiveValue(String companyId, MonthlyAttendanceItemAtr itemAtr) {
		List<Integer> listAttdID = new ArrayList<>();
		switch(itemAtr) {
		case TIME ://時間 1,2,16,17,20,22,24,40,48,49
			listAttdID.add(1);
			listAttdID.add(2);
			listAttdID.add(16);
			listAttdID.add(17);
			listAttdID.add(20);
			listAttdID.add(22);
			listAttdID.add(24);
			listAttdID.add(40);
			listAttdID.add(48);
			listAttdID.add(49);
			
			
		break;
		case NUMBER ://回数 12,19,32,33,34,43
			listAttdID.add(12);
			listAttdID.add(19);
			listAttdID.add(32);
			listAttdID.add(33);
			listAttdID.add(34);
			listAttdID.add(43);
		break;
		case DAYS ://日数 18,23,28,29,30,37,38,39,45,46,47,50,51,52,53
			listAttdID.add(18);
			listAttdID.add(23);
			listAttdID.add(28);
			listAttdID.add(29);
			listAttdID.add(30);
			listAttdID.add(37);
			listAttdID.add(38);
			listAttdID.add(39);
			listAttdID.add(45);
			listAttdID.add(46);
			listAttdID.add(47);
			listAttdID.add(50);
			listAttdID.add(51);
			listAttdID.add(52);
			listAttdID.add(53);
			break;
		case AMOUNT ://金額 54,55
			listAttdID.add(54);
			listAttdID.add(55);
			break;
		default:break;
		}
		
		StringBuilder builderString = new StringBuilder();	
		if(listAttdID.isEmpty())
			return Collections.emptyList();
		builderString.append("SELECT b");
		builderString.append(" FROM KrcmtMonAttendanceItem b");
		builderString.append(" WHERE b.primitiveValue IN :listAttdID");
		builderString.append(" AND b.krcmtMonAttendanceItemPK.cid = :companyId");
		
		List<MonthlyAttendanceItem> resultList = new ArrayList<>();
		CollectionUtil.split(listAttdID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(builderString.toString(), KrcmtMonAttendanceItem.class)
								  .setParameter("listAttdID", subList)
								  .setParameter("companyId", companyId)
								  .getList(f -> toDomain(f)));
		});
		return resultList;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<MonthlyAttendanceItem> findByAttendanceItemId(String companyId, int attendanceItemId) {
		Optional<KrcmtMonAttendanceItem> entity = this.queryProxy()
				.find(new KrcmtMonAttendanceItemPK(companyId, attendanceItemId), KrcmtMonAttendanceItem.class);
		if (entity.isPresent()) {
			return Optional.of(toDomain(entity.get()));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void update(MonthlyAttendanceItem domain) {
		KrcmtMonAttendanceItem entity = new KrcmtMonAttendanceItem(domain);
		this.commandProxy().update(entity);
	}
}
