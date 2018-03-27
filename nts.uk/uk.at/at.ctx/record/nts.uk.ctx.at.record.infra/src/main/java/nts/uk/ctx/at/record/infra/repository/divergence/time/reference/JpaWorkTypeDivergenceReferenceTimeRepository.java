package nts.uk.ctx.at.record.infra.repository.divergence.time.reference;

import java.math.BigDecimal;
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
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceType;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.history.WorkTypeDivergenceReferenceTimeRepository;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDrt;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDrtPK;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDrtPK_;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDrt_;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class JpaWorkTypeDivergenceReferenceTimeRepository.
 */
@Stateless
public class JpaWorkTypeDivergenceReferenceTimeRepository extends JpaRepository
		implements WorkTypeDivergenceReferenceTimeRepository {

	/** The Constant DIVERGENCE_TIME_MAX_COUNT. */
	private final int DIVERGENCE_TIME_MAX_COUNT = 10;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeRepository#findByKey(java.lang.String,
	 * nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode,
	 * nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceType)
	 */
	@Override
	public Optional<WorkTypeDivergenceReferenceTime> findByKey(String histId, BusinessTypeCode workTypeCode,
			DivergenceType divergenceTimeNo) {
		KrcstDrtPK pk = new KrcstDrtPK();
		pk.setHistId(histId);
		pk.setDvgcTimeNo(divergenceTimeNo.value);

		KrcstDrt drt = this.queryProxy().find(pk, KrcstDrt.class).orElse(null);

		return Optional.of(this.toDomain(drt, workTypeCode));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeRepository#findAll(java.lang.String)
	 */
	@Override
	public List<WorkTypeDivergenceReferenceTime> findAll(String histId, BusinessTypeCode workTypeCode) {
		// query data
		List<KrcstDrt> krcstDrts = this.findByHistoryId(histId);

		// return
		return krcstDrts.isEmpty() ? new ArrayList<WorkTypeDivergenceReferenceTime>()
				: krcstDrts.stream().map(item -> this.toDomain(item, workTypeCode)).collect(Collectors.toList());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeRepository#update(nts.uk.ctx.at.record.dom.
	 * divergence.time.history.WorkTypeDivergenceReferenceTime)
	 */
	@Override
	public void update(List<WorkTypeDivergenceReferenceTime> listDomain) {
		this.commandProxy()
				.updateAll(listDomain.stream().map(domain -> this.toEntity(domain)).collect(Collectors.toList()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeRepository#delete(nts.uk.ctx.at.record.dom.
	 * divergence.time.history.WorkTypeDivergenceReferenceTime)
	 */
	@Override
	public void delete(WorkTypeDivergenceReferenceTime domain) {
		this.commandProxy().remove(this.toEntity(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeRepository#addDefaultDataWhenCreateHistory(
	 * java.lang.String)
	 */
	@Override
	public void addDefaultDataWhenCreateHistory(String historyId) {
		for (int i = 1; i <= DIVERGENCE_TIME_MAX_COUNT; i++) {
			// set value for entity
			KrcstDrt drt = new KrcstDrt();
			drt.setId(new KrcstDrtPK(historyId, i));
			drt.setDvgcTimeUseSet(BigDecimal.valueOf(NotUseAtr.NOT_USE.value));

			// Insert to DB
			this.commandProxy().insert(drt);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * WorkTypeDivergenceReferenceTimeRepository#copyDataFromLatestHistory(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void copyDataFromLatestHistory(String targetHistId, String destHistId) {
		List<KrcstDrt> targetHistories = this.findByHistoryId(targetHistId);

		targetHistories.forEach(history -> {
			// set new history ID
			history.getId().setHistId(destHistId);

			// Insert to DB
			this.commandProxy().insert(history);
		});
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @param workTypeCode
	 *            the work type code
	 * @return the work type divergence reference time
	 */
	private WorkTypeDivergenceReferenceTime toDomain(KrcstDrt entity, BusinessTypeCode workTypeCode) {
		if (entity == null) {
			return null;
		}

		JpaWorkTypeDivergenceReferenceTimeGetMemento memento = new JpaWorkTypeDivergenceReferenceTimeGetMemento(entity,
				workTypeCode);
		return new WorkTypeDivergenceReferenceTime(memento);
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the krcst drt
	 */
	private KrcstDrt toEntity(WorkTypeDivergenceReferenceTime domain) {
		KrcstDrtPK pk = new KrcstDrtPK();
		pk.setHistId(domain.getHistoryId());
		pk.setDvgcTimeNo(domain.getDivergenceTimeNo());

		KrcstDrt entity = this.queryProxy().find(pk, KrcstDrt.class).orElse(new KrcstDrt());
		domain.saveToMemento(new JpaWorkTypeDivergenceReferenceTimeSetMemento(entity));

		return entity;
	}

	/**
	 * Find by history id.
	 *
	 * @param historyId
	 *            the history id
	 * @return the list
	 */
	private List<KrcstDrt> findByHistoryId(String historyId) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcstDrt> cq = criteriaBuilder.createQuery(KrcstDrt.class);
		Root<KrcstDrt> root = cq.from(KrcstDrt.class);

		// Build query
		cq.select(root);

		// create where conditions
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.equal(root.get(KrcstDrt_.id).get(KrcstDrtPK_.histId), historyId));

		// add where to query
		cq.where(predicates.toArray(new Predicate[] {}));
		cq.orderBy(criteriaBuilder.asc(root.get(KrcstDrt_.id).get(KrcstDrtPK_.dvgcTimeNo)));

		// query data
		List<KrcstDrt> krcstDrts = em.createQuery(cq).getResultList();

		return krcstDrts;
	}
}
