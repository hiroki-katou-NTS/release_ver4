/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.classification.affiliate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistItem;
import nts.uk.ctx.bs.employee.dom.classification.affiliate.AffClassHistItemRepository;
import nts.uk.ctx.bs.employee.infra.entity.classification.affiliate.BsymtAffClassHistItem;
import nts.uk.ctx.bs.employee.infra.entity.classification.affiliate.BsymtAffClassHistItem_;
import nts.uk.ctx.bs.employee.infra.entity.classification.affiliate.BsymtAffClassHistory_;

/**
 * @author danpv
 *
 */
@Stateless
public class JpaAffClassHistItemRepository extends JpaRepository implements AffClassHistItemRepository {
	
	private static final String GET_BY_HISTID_LIST = "SELECT ci FROM BsymtAffClassHistItem ci" 
			+ " WHERE ci.historyId IN :historyIds";

	@Override
	public Optional<AffClassHistItem> getByHistoryId(String historyId) {
		Optional<BsymtAffClassHistItem> optionData = this.queryProxy().find(historyId,
				BsymtAffClassHistItem.class);
		if (optionData.isPresent()) {
			return Optional.of(toDomain(optionData.get()));
		}
		return Optional.empty();
	}

	private AffClassHistItem toDomain(BsymtAffClassHistItem entity) {
		return AffClassHistItem.createFromJavaType(entity.sid, entity.historyId, entity.classificationCode);
	}

	@Override
	public void add(AffClassHistItem item) {
		BsymtAffClassHistItem entity = new BsymtAffClassHistItem(item.getHistoryId(), item.getEmployeeId(),
				item.getClassificationCode().v());
		this.commandProxy().insert(entity);

	}

	@Override
	public void update(AffClassHistItem item) {
		Optional<BsymtAffClassHistItem> entityOpt = this.queryProxy().find(item.getHistoryId(),
				BsymtAffClassHistItem.class);

		if (!entityOpt.isPresent()) {
			throw new RuntimeException("invalid TempAbsenceHisItem");
		}
		BsymtAffClassHistItem ent = entityOpt.get();
		ent.classificationCode = item.getClassificationCode().v();
		this.commandProxy().update(ent);

	}

	@Override
	public void delete(String historyId) {
		Optional<BsymtAffClassHistItem> entityOpt = this.queryProxy().find(historyId,
				BsymtAffClassHistItem.class);

		if (!entityOpt.isPresent()) {
			throw new RuntimeException("invalid TempAbsenceHisItem");
		}
		this.commandProxy().remove(BsymtAffClassHistItem.class, historyId);
	}
	
	@Override
	public List<AffClassHistItem> searchClassification(GeneralDate baseDate,
			List<String> classificationCodes) {

		if (CollectionUtil.isEmpty(classificationCodes)) {
			return new ArrayList<>();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KMNMT_CLASSIFICATION_HIST (KmnmtClassificationHist SQL)
		CriteriaQuery<BsymtAffClassHistItem> cq = criteriaBuilder
				.createQuery(BsymtAffClassHistItem.class);

		// root data
		Root<BsymtAffClassHistItem> root = cq.from(BsymtAffClassHistItem.class);

		// select root
		cq.select(root);
		
		List<BsymtAffClassHistItem> resultList = new ArrayList<>();
		
		CollectionUtil.split(classificationCodes, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();

			// classification in data classification
			lstpredicateWhere.add(criteriaBuilder
					.and(root.get(BsymtAffClassHistItem_.classificationCode)
							.in(splitData)));
			
			// start date <= base date
			lstpredicateWhere.add(criteriaBuilder
					.lessThanOrEqualTo(root.get(BsymtAffClassHistItem_.bsymtAffClassHistory)
							.get(BsymtAffClassHistory_.startDate), baseDate));

			// endDate >= base date
			lstpredicateWhere.add(criteriaBuilder
					.greaterThanOrEqualTo(root.get(BsymtAffClassHistItem_.bsymtAffClassHistory)
							.get(BsymtAffClassHistory_.endDate), baseDate));

			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
			
			resultList.addAll(em.createQuery(cq).getResultList());
		});

		return resultList.stream().map(category -> toDomain(category))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.basic.dom.company.organization.classification.history.
	 * ClassificationHistoryRepository#searchClassification(java.util.List,
	 * nts.arc.time.GeneralDate, java.util.List)
	 */
	@Override
	public List<AffClassHistItem> searchClassification(List<String> employeeIds,
			GeneralDate baseDate, List<String> classificationCodes) {

		// check not data
		if (CollectionUtil.isEmpty(classificationCodes) || CollectionUtil.isEmpty(employeeIds)) {
			return Collections.emptyList();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KMNMT_CLASSIFICATION_HIST (KmnmtClassificationHist SQL)
		CriteriaQuery<BsymtAffClassHistItem> cq = criteriaBuilder
				.createQuery(BsymtAffClassHistItem.class);

		// root data
		Root<BsymtAffClassHistItem> root = cq.from(BsymtAffClassHistItem.class);

		// select root
		cq.select(root);
		
		List<BsymtAffClassHistItem> resultList = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			CollectionUtil.split(classificationCodes, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, classSubList -> {
				// add where
				List<Predicate> lstpredicateWhere = new ArrayList<>();

				// employee id in data employee id
				lstpredicateWhere.add(criteriaBuilder
						.and(root.get(BsymtAffClassHistItem_.sid).in(subList)));

				// classification in data classification
				lstpredicateWhere.add(
						criteriaBuilder.and(root.get(BsymtAffClassHistItem_.classificationCode)
								.in(classSubList)));

				// start date <= base date
				lstpredicateWhere
						.add(criteriaBuilder
								.lessThanOrEqualTo(
										root.get(BsymtAffClassHistItem_.bsymtAffClassHistory)
												.get(BsymtAffClassHistory_.startDate),
										baseDate));

				// endDate >= base date
				lstpredicateWhere
						.add(criteriaBuilder
								.greaterThanOrEqualTo(
										root.get(BsymtAffClassHistItem_.bsymtAffClassHistory)
												.get(BsymtAffClassHistory_.endDate),
										baseDate));

				// set where to SQL
				cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

				// create query
				TypedQuery<BsymtAffClassHistItem> query = em.createQuery(cq);
				
				resultList.addAll(query.getResultList());
			});
		});
		
		// convert.
		return resultList.stream().map(category -> toDomain(category))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.classification.affiliate.
	 * AffClassHistItemRepository#getByHistoryIds(java.util.List)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AffClassHistItem> getByHistoryIds(List<String> historyIds) {
		if (historyIds.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<BsymtAffClassHistItem> results = new ArrayList<>();
		
		CollectionUtil.split(historyIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIds -> {
			results.addAll(this.queryProxy()
					.query(GET_BY_HISTID_LIST, BsymtAffClassHistItem.class)
					.setParameter("historyIds", subIds)
					.getList());
		});

		return results.stream().map(ent -> toDomain(ent)).collect(Collectors.toList());
	}

}
