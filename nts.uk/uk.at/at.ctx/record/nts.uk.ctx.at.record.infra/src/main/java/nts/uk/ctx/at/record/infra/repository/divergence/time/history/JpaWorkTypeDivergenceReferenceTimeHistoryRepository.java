package nts.uk.ctx.at.record.infra.repository.divergence.time.history;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistory;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryRepository;
import nts.uk.ctx.at.record.infra.entity.divergence.time.history.KrcstComDrtHist_;
import nts.uk.ctx.at.record.infra.entity.divergence.time.history.KrcstWorktypeDrtHist;
import nts.uk.ctx.at.record.infra.entity.divergence.time.history.KrcstWorktypeDrtHist_;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaWorkTypeDivergenceReferenceTimeHistoryRepository.
 */
@Stateless
public class JpaWorkTypeDivergenceReferenceTimeHistoryRepository extends JpaRepository
		implements WorkTypeDivergenceReferenceTimeHistoryRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeHistoryRepository#countByPeriodDate(nts.arc.
	 * time.GeneralDate, nts.arc.time.GeneralDate)
	 */
	@Override
	public Integer countByDatePeriod(String companyId, BusinessTypeCode workTypeCode, DatePeriod datePeriod, String histId) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		Root<KrcstWorktypeDrtHist> root = cq.from(KrcstWorktypeDrtHist.class);

		// Get start date, end Date
		GeneralDate startDate = datePeriod.start();
		GeneralDate endDate = datePeriod.end();

		// Build query
		cq.select(criteriaBuilder.count(root));

		// create where conditions
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.equal(root.get(KrcstWorktypeDrtHist_.cid), companyId));
		predicates.add(criteriaBuilder.equal(root.get(KrcstWorktypeDrtHist_.worktypeCd), workTypeCode.v()));
		if (!StringUtils.isEmpty(histId)) {
			predicates.add(criteriaBuilder.notEqual(root.get(KrcstWorktypeDrtHist_.histId), histId));
		}
		
		predicates.add(criteriaBuilder.or(
				criteriaBuilder.between(root.get(KrcstComDrtHist_.strD.getName()), startDate, endDate),
				criteriaBuilder.between(root.get(KrcstComDrtHist_.endD.getName()), startDate, endDate),
				criteriaBuilder.and(criteriaBuilder.lessThan(root.get(KrcstComDrtHist_.strD.getName()), startDate),
						criteriaBuilder.greaterThan(root.get(KrcstComDrtHist_.endD.getName()), endDate))));

		// add where to query
		cq.where(predicates.toArray(new Predicate[] {}));

		// query count
		return em.createQuery(cq).getSingleResult().intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeHistoryRepository#findByKey(java.lang.String,
	 * nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode, java.lang.String)
	 */
	@Override
	public WorkTypeDivergenceReferenceTimeHistory findByKey(String histId) {
		KrcstWorktypeDrtHist worktypeDrtHist = this.queryProxy().find(histId, KrcstWorktypeDrtHist.class).orElse(null);
		ArrayList<KrcstWorktypeDrtHist> entities = new ArrayList<>();
		if (worktypeDrtHist != null) {
			entities.add(worktypeDrtHist);
		}

		return this.toDomain(entities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeHistoryRepository#findAll(java.lang.String,
	 * nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode)
	 */
	@Override
	public WorkTypeDivergenceReferenceTimeHistory findAll(String companyId, BusinessTypeCode workTypeCode) {

		return this.toDomain(this.findByCompanyIdAndWorkType(companyId, workTypeCode.v(), new ArrayList<String>()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeHistoryRepository#add(nts.uk.ctx.at.record.dom
	 * .divergence.time.history.WorkTypeDivergenceReferenceTimeHistory)
	 */
	@Override
	public void add(WorkTypeDivergenceReferenceTimeHistory domain) {
		this.commandProxy().insertAll(this.toEntities(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeHistoryRepository#update(nts.uk.ctx.at.record.
	 * dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistory)
	 */
	@Override
	public void update(WorkTypeDivergenceReferenceTimeHistory domain) {
		this.commandProxy().updateAll(this.toEntities(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeHistoryRepository#delete(nts.uk.ctx.at.record.
	 * dom.divergence.time.history.WorkTypeDivergenceReferenceTimeHistory)
	 */
	@Override
	public void delete(WorkTypeDivergenceReferenceTimeHistory domain) {
		this.commandProxy().removeAll(this.toEntities(domain));
	}

	@Override
	public WorkTypeDivergenceReferenceTimeHistory findLatestHist(String companyId, BusinessTypeCode workTypeCode) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcstWorktypeDrtHist> cq = criteriaBuilder.createQuery(KrcstWorktypeDrtHist.class);
		Root<KrcstWorktypeDrtHist> root = cq.from(KrcstWorktypeDrtHist.class);

		// Build query
		cq.select(root);

		// create where conditions
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.equal(root.get(KrcstWorktypeDrtHist_.cid), companyId));
		predicates.add(criteriaBuilder.equal(root.get(KrcstWorktypeDrtHist_.worktypeCd), workTypeCode));

		// add where to query
		cq.where(predicates.toArray(new Predicate[] {}));
		cq.orderBy(criteriaBuilder.desc(root.get(KrcstWorktypeDrtHist_.endD)));

		// query data
		List<KrcstWorktypeDrtHist> comDrtHists = em.createQuery(cq).setMaxResults(1).getResultList();

		return this.toDomain(comDrtHists);
	}

	/**
	 * To domain.
	 *
	 * @param entities
	 *            the entities
	 * @return the work type divergence reference time history
	 */
	private WorkTypeDivergenceReferenceTimeHistory toDomain(List<KrcstWorktypeDrtHist> entities) {
		JpaWorkTypeDivergenceReferenceTimeHistoryGetMemento memento = new JpaWorkTypeDivergenceReferenceTimeHistoryGetMemento(
				entities);
		return new WorkTypeDivergenceReferenceTimeHistory(memento);
	}

	/**
	 * To entities.
	 *
	 * @param domain
	 *            the domain
	 * @return the list
	 */
	private List<KrcstWorktypeDrtHist> toEntities(WorkTypeDivergenceReferenceTimeHistory domain) {
		List<String> histIds = domain.getHistoryItems().stream().map(item -> item.identifier())
				.collect(Collectors.toList());

		List<KrcstWorktypeDrtHist> worktypeDrtHists = this.findByCompanyIdAndWorkType(domain.getCId(),
				domain.getWorkTypeCode().v(), histIds);

		JpaWorkTypeDivergenceReferenceTimeHistorySetMemento memento = new JpaWorkTypeDivergenceReferenceTimeHistorySetMemento(
				worktypeDrtHists);

		domain.saveToMemento(memento);

		return worktypeDrtHists;
	}

	/**
	 * Find by company id and work type.
	 *
	 * @param companyId
	 *            the company id
	 * @param workTypeCode
	 *            the work type code
	 * @param histIds
	 *            the hist ids
	 * @return the list
	 */
	private List<KrcstWorktypeDrtHist> findByCompanyIdAndWorkType(String companyId, String workTypeCode,
			List<String> histIds) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcstWorktypeDrtHist> cq = criteriaBuilder.createQuery(KrcstWorktypeDrtHist.class);
		Root<KrcstWorktypeDrtHist> root = cq.from(KrcstWorktypeDrtHist.class);

		// Build query
		cq.select(root);

		// create where conditions
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.equal(root.get(KrcstWorktypeDrtHist_.cid), companyId));
		predicates.add(criteriaBuilder.equal(root.get(KrcstWorktypeDrtHist_.worktypeCd), workTypeCode));
		
		// Find by history id
		if (!histIds.isEmpty()) {
			predicates.add(root.get(KrcstWorktypeDrtHist_.histId).in(histIds));
		}

		// add where to query
		cq.where(predicates.toArray(new Predicate[] {}));

		// order by insert date
		cq.orderBy(criteriaBuilder.asc(root.get("insDate")));

		// query data
		List<KrcstWorktypeDrtHist> worktypeDrtHists = em.createQuery(cq).getResultList();

		return worktypeDrtHists;
	}
}
