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
import nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistItemRepository;
import nts.uk.ctx.at.shared.dom.shortworktime.ShortWorkTimeHistoryItem;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistItem;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistItemPK;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistItemPK_;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistItem_;

/**
 * The Class JpaSWorkTimeHistItemRepository.
 */
@Stateless
public class JpaSWorkTimeHistItemRepository extends JpaRepository implements SWorkTimeHistItemRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistItemRepository#add(
	 * nts.uk.ctx.at.shared.dom.shortworktime.ShortWorkTimeHistoryItem)
	 */
	@Override
	public void add(ShortWorkTimeHistoryItem domain) {
		this.commandProxy().insert(this.toEntity(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistItemRepository#update
	 * (nts.uk.ctx.at.shared.dom.shortworktime.ShortWorkTimeHistoryItem)
	 */
	@Override
	public void update(ShortWorkTimeHistoryItem domain) {
		this.commandProxy().remove(this.toEntity(domain));
		this.commandProxy().insert(this.toEntity(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistItemRepository#
	 * findByKey(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<ShortWorkTimeHistoryItem> findByKey(String empId, String histId) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BshmtWorktimeHistItem> query = builder.createQuery(BshmtWorktimeHistItem.class);
        Root<BshmtWorktimeHistItem> root = query.from(BshmtWorktimeHistItem.class);
        
        List<Predicate> predicateList = new ArrayList<>();
        
        predicateList.add(builder.equal(root.get(BshmtWorktimeHistItem_.bshmtWorktimeHistItemPK)
        		.get(BshmtWorktimeHistItemPK_.sid), empId));
        predicateList.add(builder.equal(root.get(BshmtWorktimeHistItem_.bshmtWorktimeHistItemPK)
        		.get(BshmtWorktimeHistItemPK_.histId), histId));
        
        query.where(predicateList.toArray(new Predicate[]{}));
        
		return em.createQuery(query).getResultList().stream()
				.map(entity -> new ShortWorkTimeHistoryItem(new JpaSWorkTimeHistItemGetMemento(entity)))
				.findFirst();
	}
	
	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the bshmt worktime hist item
	 */
	private BshmtWorktimeHistItem toEntity(ShortWorkTimeHistoryItem domain) {
		BshmtWorktimeHistItem entity = this.queryProxy()
				.find(new BshmtWorktimeHistItemPK(domain.getEmployeeId(), domain.getHistoryId()),
						BshmtWorktimeHistItem.class)
				.orElse(new BshmtWorktimeHistItem(new BshmtWorktimeHistItemPK()));
		JpaSWorkTimeHistItemSetMemento memento = new JpaSWorkTimeHistItemSetMemento(entity);
		domain.saveToMemento(memento);
		return entity;
	}

	@Override
	public void delete(String sid, String hist) {
		BshmtWorktimeHistItemPK key = new BshmtWorktimeHistItemPK(sid, hist);
		this.commandProxy().remove(BshmtWorktimeHistItem.class,key);
	}

}
