/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.shortworktime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository;
import nts.uk.ctx.at.shared.dom.shortworktime.ShortWorkTimeHistory;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHist;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistPK;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistPK_;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHist_;

/**
 * The Class JpaSWorkTimeHistoryRepository.
 */
@Stateless
public class JpaSWorkTimeHistoryRepository extends JpaRepository implements SWorkTimeHistoryRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository#add(nts
	 * .uk.ctx.at.shared.dom.shortworktime.ShortWorkTimeHistory)
	 */
	@Override
	public void add(ShortWorkTimeHistory domain) {
		this.commandProxy().insert(this.toEntity(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository#update(
	 * nts.uk.ctx.at.shared.dom.shortworktime.ShortWorkTimeHistory)
	 */
	@Override
	public void update(ShortWorkTimeHistory domain) {
		this.commandProxy().update(this.toEntity(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository#
	 * findByKey(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<ShortWorkTimeHistory> findByKey(String empId, String histId) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BshmtWorktimeHist> query = builder.createQuery(BshmtWorktimeHist.class);
        Root<BshmtWorktimeHist> root = query.from(BshmtWorktimeHist.class);
        
        List<Predicate> predicateList = new ArrayList<>();
        
        predicateList.add(builder.equal(root.get(BshmtWorktimeHist_.bshmtWorktimeHistPK)
                .get(BshmtWorktimeHistPK_.sid), empId));
        predicateList.add(builder.equal(root.get(BshmtWorktimeHist_.bshmtWorktimeHistPK)
                .get(BshmtWorktimeHistPK_.histId), histId));
        
        query.where(predicateList.toArray(new Predicate[]{}));
        
		return em.createQuery(query).getResultList().stream()
				.map(entity -> new ShortWorkTimeHistory(new JpaSWorkTimeHistGetMemento(entity)))
				.findFirst();
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the bshmt worktime hist
	 */
	private BshmtWorktimeHist toEntity(ShortWorkTimeHistory domain) {
		BshmtWorktimeHist entity = this.queryProxy()
				.find(new BshmtWorktimeHistPK(domain.getEmployeeId(), domain.getHistoryItem().identifier()),
						BshmtWorktimeHist.class)
				.orElse(new BshmtWorktimeHist(new BshmtWorktimeHistPK()));
		JpaSWorkTimeHistSetMemento memento = new JpaSWorkTimeHistSetMemento(entity);
		domain.saveToMemento(memento);
		return entity;
	}
}
