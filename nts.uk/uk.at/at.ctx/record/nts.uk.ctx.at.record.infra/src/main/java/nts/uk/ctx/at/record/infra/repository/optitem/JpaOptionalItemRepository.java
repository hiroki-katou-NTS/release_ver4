/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.optitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemUsageAtr;
import nts.uk.ctx.at.record.dom.optitem.PerformanceAtr;
import nts.uk.ctx.at.record.infra.entity.optitem.KrcstCalcResultRange;
import nts.uk.ctx.at.record.infra.entity.optitem.KrcstCalcResultRangePK;
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
	public OptionalItem find(String companyId, Integer optionalItemNo) {
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
	@SneakyThrows
	public List<OptionalItem> findAll(String companyId) {
		
		List<KrcstOptionalItem> items;
		{
			val stmt = this.connection().prepareStatement(
					"select * from KRCST_OPTIONAL_ITEM"
					+ " where CID = ?");
			stmt.setString(1, companyId);
			
			items = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
				KrcstOptionalItem e = new KrcstOptionalItem();
				e.setKrcstOptionalItemPK(new KrcstOptionalItemPK(
						companyId, rec.getInt("OPTIONAL_ITEM_NO")));
				e.setOptionalItemName(rec.getString("OPTIONAL_ITEM_NAME"));
				e.setOptionalItemAtr(rec.getInt("OPTIONAL_ITEM_ATR"));
				e.setUsageAtr(rec.getInt("USAGE_ATR"));
				e.setPerformanceAtr(rec.getInt("PERFORMANCE_ATR"));
				e.setEmpConditionAtr(rec.getInt("EMP_CONDITION_ATR"));
				e.setUnitOfOptionalItem(rec.getString("UNIT_OF_OPTIONAL_ITEM"));
				return e;
			});
		}
		
		Map<Integer, KrcstCalcResultRange> rangesMap;
		{
			val stmt = this.connection().prepareCall(
					"select * from KRCST_CALC_RESULT_RANGE"
					+ " where CID = ?");
			stmt.setString(1, companyId);

			List<KrcstCalcResultRange> ranges = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
				KrcstCalcResultRange e = new KrcstCalcResultRange();
				e.setKrcstCalcResultRangePK(new KrcstCalcResultRangePK(
						companyId, rec.getInt("OPTIONAL_ITEM_NO")));
				e.setUpperLimitAtr(rec.getInt("UPPER_LIMIT_ATR"));
				e.setLowerLimitAtr(rec.getInt("LOWER_LIMIT_ATR"));
				e.setUpperTimeRange(rec.getInt("UPPER_TIME_RANGE"));
				e.setLowerTimeRange(rec.getInt("LOWER_TIME_RANGE"));
				e.setUpperNumberRange(rec.getDouble("UPPER_NUMBER_RANGE"));
				e.setLowerNumberRange(rec.getDouble("LOWER_NUMBER_RANGE"));
				e.setUpperAmountRange(rec.getInt("UPPER_AMOUNT_RANGE"));
				e.setLowerAmountRange(rec.getInt("LOWER_AMOUNT_RANGE"));
				return e;
			});
			
			rangesMap = ranges.stream().collect(Collectors.toMap(
					e -> e.getKrcstCalcResultRangePK().getOptionalItemNo(),
					e -> e));
		}
		
		return items.stream().map(item -> {
			KrcstCalcResultRange range = rangesMap.get(item.getKrcstOptionalItemPK().getOptionalItemNo());
			return new OptionalItem(new JpaOptionalItemGetMemento(item, range));
		}).collect(Collectors.toList());
	}

	@Override
	public List<OptionalItem> findByAtr(String companyId, int atr) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<?> cq = builder.createQuery();

		// From table
		Root<KrcstOptionalItem> root = cq.from(KrcstOptionalItem.class);
		Join<KrcstOptionalItem, KrcstCalcResultRange> joinRoot = root
				.join(KrcstOptionalItem_.krcstCalcResultRange, JoinType.LEFT);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Add where condition
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.cid),
				companyId));
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.optionalItemAtr), atr));
		cq.multiselect(root, joinRoot);
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get results
		@SuppressWarnings("unchecked")
		List<Object[]> results = (List<Object[]>) em.createQuery(cq).getResultList();

		// Check empty
		if (CollectionUtil.isEmpty(results)) {
			return Collections.emptyList();
		}

		// Return
		return results.stream()
				.map(item -> new OptionalItem(new JpaOptionalItemGetMemento(
						(KrcstOptionalItem) item[0], (KrcstCalcResultRange) item[1])))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository#findByListNos(
	 * java.lang.String, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<OptionalItem> findByListNos(String companyId, List<Integer> optionalitemNos) {

		// Check empty
		if (CollectionUtil.isEmpty(optionalitemNos)) {
			return Collections.emptyList();
		}
		
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<?> cq = builder.createQuery();

		// From table
		Root<KrcstOptionalItem> root = cq.from(KrcstOptionalItem.class);
		Join<KrcstOptionalItem, KrcstCalcResultRange> joinRoot = root
				.join(KrcstOptionalItem_.krcstCalcResultRange, JoinType.LEFT);

		List<Object[]> results = new ArrayList<>();

		// Split conditions
		CollectionUtil.split(optionalitemNos, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			List<Predicate> predicateList = new ArrayList<Predicate>();

			// Add where condition
			predicateList.add(builder.equal(
					root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.cid),
					companyId));
			predicateList.add(root.get(KrcstOptionalItem_.krcstOptionalItemPK)
					.get(KrcstOptionalItemPK_.optionalItemNo).in(splitData));
			cq.multiselect(root, joinRoot);
			cq.where(predicateList.toArray(new Predicate[] {}));

			// Get results
			results.addAll((List<Object[]>) em.createQuery(cq).getResultList());
		});

		// Return
		return results.stream()
				.map(item -> new OptionalItem(new JpaOptionalItemGetMemento(
						(KrcstOptionalItem) item[0], (KrcstCalcResultRange) item[1])))
				.collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository#findByAtr(java.lang.String, nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr)
	 */
	@Override
	public List<OptionalItem> findByAtr(String companyId, OptionalItemAtr atr) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<?> cq = builder.createQuery();

		// From table
		Root<KrcstOptionalItem> root = cq.from(KrcstOptionalItem.class);
		Join<KrcstOptionalItem, KrcstCalcResultRange> joinRoot = root
				.join(KrcstOptionalItem_.krcstCalcResultRange, JoinType.LEFT);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Add where condition
		predicateList.add(builder.equal(
				root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.cid),
				companyId));
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.optionalItemAtr), atr.value));
		cq.multiselect(root, joinRoot);
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get results
		@SuppressWarnings("unchecked")
		List<Object[]> results = (List<Object[]>) em.createQuery(cq).getResultList();

		// Check empty
		if (CollectionUtil.isEmpty(results)) {
			return Collections.emptyList();
		}

		// Return
		return results.stream()
				.map(item -> new OptionalItem(new JpaOptionalItemGetMemento(
						(KrcstOptionalItem) item[0], (KrcstCalcResultRange) item[1])))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository#
	 * findByPerformanceAtr(java.lang.String,
	 * nts.uk.ctx.at.record.dom.optitem.PerformanceAtr)
	 */
	@Override
	public List<OptionalItem> findByPerformanceAtr(String companyId, PerformanceAtr atr) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<?> cq = builder.createQuery();

		// From table
		Root<KrcstOptionalItem> root = cq.from(KrcstOptionalItem.class);
		Join<KrcstOptionalItem, KrcstCalcResultRange> joinRoot = root
				.join(KrcstOptionalItem_.krcstCalcResultRange, JoinType.LEFT);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Add where condition
		predicateList.add(builder.equal(
				root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.cid),
				companyId));
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.performanceAtr), atr.value));
		cq.multiselect(root, joinRoot);
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get results
		@SuppressWarnings("unchecked")
		List<Object[]> results = (List<Object[]>) em.createQuery(cq).getResultList();

		// Check empty
		if (CollectionUtil.isEmpty(results)) {
			return Collections.emptyList();
		}

		// Return
		return results.stream()
				.map(item -> new OptionalItem(new JpaOptionalItemGetMemento(
						(KrcstOptionalItem) item[0], (KrcstCalcResultRange) item[1])))
				.collect(Collectors.toList());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository#
	 * findByPerformanceAtr(java.lang.String,
	 * nts.uk.ctx.at.record.dom.optitem.PerformanceAtr)
	 */
	@Override
	public List<OptionalItem> findUsedByPerformanceAtr(String companyId, PerformanceAtr atr) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<Tuple> cq = builder.createQuery(Tuple.class);

		// From table
		Root<KrcstOptionalItem> root = cq.from(KrcstOptionalItem.class);
		Join<KrcstOptionalItem, KrcstCalcResultRange> join = root.join(KrcstOptionalItem_.krcstCalcResultRange, JoinType.LEFT);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Add where condition
		predicateList.add(builder.equal(
				root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.cid),
				companyId));
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.performanceAtr), atr.value));
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.usageAtr), OptionalItemUsageAtr.USE.value));
		cq.where(predicateList.toArray(new Predicate[] {}));
		
		cq = cq.multiselect(root, join);
		List<Tuple> result = em.createQuery(cq.distinct(true)).getResultList();
		
		// Get results
		return result.stream()
				.map(c -> new OptionalItem(new JpaOptionalItemGetMemento((KrcstOptionalItem) c.get(0), (KrcstCalcResultRange) c.get(1))))
				.collect(Collectors.toList());
	}
	
	@Override
	public Map<Integer, OptionalItemAtr> findOptionalTypeBy(String companyId, PerformanceAtr atr) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<Tuple> cq = builder.createTupleQuery();

		// From table
		Root<KrcstOptionalItem> root = cq.from(KrcstOptionalItem.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Add where condition
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.cid), companyId));
		predicateList.add(builder.equal(root.get(KrcstOptionalItem_.performanceAtr), atr.value));
		cq.where(predicateList.toArray(new Predicate[] {}));
		
		// Get NO and optional attr only
		cq.multiselect(root.get(KrcstOptionalItem_.krcstOptionalItemPK).get(KrcstOptionalItemPK_.optionalItemNo), 
						root.get(KrcstOptionalItem_.optionalItemAtr));

		// Get results
		List<Tuple> results = em.createQuery(cq).getResultList();
		
		// Check empty
		if (CollectionUtil.isEmpty(results)) {
			return new HashMap<>();
		}
		
		// Return
		return results.stream().collect(Collectors.toMap(r -> (Integer) r.get(0), 
				r -> EnumAdaptor.valueOf((Integer) r.get(1), OptionalItemAtr.class)));
	}

}
