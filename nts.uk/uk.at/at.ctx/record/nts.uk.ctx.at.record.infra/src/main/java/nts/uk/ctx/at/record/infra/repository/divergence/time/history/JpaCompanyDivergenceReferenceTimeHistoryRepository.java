/*
 * 
 */
package nts.uk.ctx.at.record.infra.repository.divergence.time.history;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistory;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistoryGetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistoryRepository;
import nts.uk.ctx.at.record.dom.divergence.time.history.CompanyDivergenceReferenceTimeHistorySetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.history.KrcstComDrtHist;
import nts.uk.ctx.at.record.infra.entity.divergence.time.history.KrcstComDrtHist_;

/**
 * The Class JpaCompanyDivergenceReferenceTimeHistoryRepository.
 */
@Stateless
public class JpaCompanyDivergenceReferenceTimeHistoryRepository extends JpaRepository
		implements CompanyDivergenceReferenceTimeHistoryRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeHistoryRepository#findbyPeriodDate(nts.arc.time
	 * .GeneralDate, nts.arc.time.GeneralDate)
	 */
	@Override
	public Integer countByPeriodDate(GeneralDate startDate, GeneralDate endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeHistoryRepository#findByHistId(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public CompanyDivergenceReferenceTimeHistory findByHistId(String histId) {
		KrcstComDrtHist krcstComDrtHist = this.queryProxy().find(histId, KrcstComDrtHist.class).orElse(null);
		ArrayList<KrcstComDrtHist> entities = new ArrayList<>();

		if (krcstComDrtHist != null) {
			entities.add(krcstComDrtHist);
		}

		return this.toDomain(entities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeHistoryRepository#findAll(java.lang.String)
	 */
	@Override
	public CompanyDivergenceReferenceTimeHistory findAll(String companyId) {

		// return
		return this.toDomain(this.findByCompanyId(companyId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeHistoryRepository#add(nts.uk.ctx.at.record.dom.
	 * divergence.time.history.CompanyDivergenceReferenceTimeHistory)
	 */
	@Override
	public void add(CompanyDivergenceReferenceTimeHistory domain) {
		this.commandProxy().insertAll(this.toEntities(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeHistoryRepository#update(nts.uk.ctx.at.record.
	 * dom.divergence.time.history.CompanyDivergenceReferenceTimeHistory)
	 */
	@Override
	public void update(CompanyDivergenceReferenceTimeHistory domain) {
		this.commandProxy().updateAll(this.toEntities(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.history.
	 * CompanyDivergenceReferenceTimeHistoryRepository#delete(nts.uk.ctx.at.record.
	 * dom.divergence.time.history.CompanyDivergenceReferenceTimeHistory)
	 */
	@Override
	public void delete(CompanyDivergenceReferenceTimeHistory domain) {
		this.commandProxy().removeAll(this.toEntities(domain));
	}

	/**
	 * To domain.
	 *
	 * @param entities
	 *            the entities
	 * @return the company divergence reference time history
	 */
	private CompanyDivergenceReferenceTimeHistory toDomain(List<KrcstComDrtHist> entities) {
		CompanyDivergenceReferenceTimeHistoryGetMemento memento = new JpaCompanyDivergenceReferenceTimeHistoryGetMemento(
				entities);
		return new CompanyDivergenceReferenceTimeHistory(memento);
	}

	/**
	 * To entities.
	 *
	 * @param domain
	 *            the domain
	 * @return the list
	 */
	private List<KrcstComDrtHist> toEntities(CompanyDivergenceReferenceTimeHistory domain) {
		List<KrcstComDrtHist> comDrtHists = this.findByCompanyId(domain.getCId());

		CompanyDivergenceReferenceTimeHistorySetMemento memento = new JpaCompanyDivergenceReferenceTimeHistorySetMemento(
				comDrtHists);

		domain.saveToMemento(memento);

		return comDrtHists;
	}

	/**
	 * Find by company id.
	 *
	 * @param companyId
	 *            the company id
	 * @return the list
	 */
	private List<KrcstComDrtHist> findByCompanyId(String companyId) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcstComDrtHist> cq = criteriaBuilder.createQuery(KrcstComDrtHist.class);
		Root<KrcstComDrtHist> root = cq.from(KrcstComDrtHist.class);

		// Build query
		cq.select(root);

		// create where conditions
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.equal(root.get(KrcstComDrtHist_.cid), companyId));

		// add where to query
		cq.where(predicates.toArray(new Predicate[] {}));

		// query data
		List<KrcstComDrtHist> comDrtHists = em.createQuery(cq).getResultList();

		return comDrtHists;
	}
}
