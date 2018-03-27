package nts.uk.ctx.at.record.infra.repository.divergence.time;

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
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethod;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodGetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodRepository;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTime;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTimePK;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTimePK_;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTime_;

/**
 * The Class JpaDivergenceReasonInputMethodRepository.
 */
@Stateless
public class JpaDivergenceReasonInputMethodRepository extends JpaRepository
		implements DivergenceReasonInputMethodRepository {

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodRepository#getAllDivTime(java.lang.String)
	 */
	@Override
	public List<DivergenceReasonInputMethod> getAllDivTime(String companyId) {

		return this.findByCompanyId(companyId);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodRepository#getDivTimeInfo(java.lang.String, int)
	 */
	@Override
	public DivergenceReasonInputMethod getDivTimeInfo(String companyId, int divTimeNo) {

		return this.findByCode(companyId, divTimeNo);
	}

	/**
	 * To domain.
	 *
	 * @param entities
	 *            the entities
	 * @return the company divergence reference time history
	 */
	private DivergenceReasonInputMethod toDomain(KrcstDvgcTime entities) {
		DivergenceReasonInputMethodGetMemento memento = new JpaDivergenceReasonInputMethodRepositoryGetMemento(
				entities);
		return new DivergenceReasonInputMethod(memento);
	}

	/**
	 * Find by company id.
	 *
	 * @param companyId
	 *            the company id
	 * @return the list
	 */
	private List<DivergenceReasonInputMethod> findByCompanyId(String companyId) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcstDvgcTime> cq = criteriaBuilder.createQuery(KrcstDvgcTime.class);
		Root<KrcstDvgcTime> root = cq.from(KrcstDvgcTime.class);

		// Build query
		cq.select(root);

		// create where conditions
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.equal(root.get(KrcstDvgcTime_.id).get(KrcstDvgcTimePK_.cid), companyId));

		// add where to query
		cq.where(predicates.toArray(new Predicate[] {}));

		// query data
		List<KrcstDvgcTime> KrcstDvgcTime = em.createQuery(cq).getResultList();

		// return
		return KrcstDvgcTime.isEmpty() ? new ArrayList<DivergenceReasonInputMethod>()
				: KrcstDvgcTime.stream().map(item -> this.toDomain(item)).collect(Collectors.toList());
	}

	/**
	 * Find by code.
	 *
	 * @param companyId
	 *            the company id
	 * @param divTimeNo
	 *            the div time no
	 * @return the divergence reason input method
	 */
	private DivergenceReasonInputMethod findByCode(String companyId, int divTimeNo) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcstDvgcTime> cq = criteriaBuilder.createQuery(KrcstDvgcTime.class);
		Root<KrcstDvgcTime> root = cq.from(KrcstDvgcTime.class);

		// Build query
		cq.select(root);

		// create where conditions
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.equal(root.get(KrcstDvgcTime_.id).get(KrcstDvgcTimePK_.cid), companyId));
		predicates.add(criteriaBuilder.equal(root.get(KrcstDvgcTime_.id).get(KrcstDvgcTimePK_.no), divTimeNo));

		// add where to query
		cq.where(predicates.toArray(new Predicate[] {}));

		// query data
		KrcstDvgcTime KrcstDvgcTime = em.createQuery(cq).getResultList().get(0);

		// return
		if (KrcstDvgcTime != null)
			return this.toDomain(KrcstDvgcTime);
		return new DivergenceReasonInputMethod(null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.
	 * DivergenceReasonInputMethodRepository#update(nts.uk.ctx.at.record.dom.
	 * divergence.time.DivergenceReasonInputMethod)
	 */
	@Override
	public void update(DivergenceReasonInputMethod domain) {
		this.commandProxy().update(this.toEntity(domain));

	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the krcst dvgc time
	 */
	private KrcstDvgcTime toEntity(DivergenceReasonInputMethod domain) {

		KrcstDvgcTimePK PK = new KrcstDvgcTimePK(domain.getDivergenceTimeNo(), domain.getCompanyId());
		KrcstDvgcTime entity = this.queryProxy().find(PK, KrcstDvgcTime.class).orElse(new KrcstDvgcTime());

		domain.saveToMemento(new JpaDivergenceReasonInputMethodRepositorySetMemento(entity));

		return entity;
	}

}
