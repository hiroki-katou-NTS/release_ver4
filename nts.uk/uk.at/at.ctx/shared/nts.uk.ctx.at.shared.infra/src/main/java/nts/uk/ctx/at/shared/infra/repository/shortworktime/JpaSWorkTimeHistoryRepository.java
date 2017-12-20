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
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository;
import nts.uk.ctx.at.shared.dom.shortworktime.ShortWorkTimeHistory;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHist;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistPK;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistPK_;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHist_;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaSWorkTimeHistoryRepository.
 */
@Stateless
public class JpaSWorkTimeHistoryRepository extends JpaRepository
		implements SWorkTimeHistoryRepository {
	
	private final String QUERY_GET__BYSID = "SELECT ad FROM BshmtWorktimeHist ad"
			+ " WHERE ad.BshmtWorktimeHistPK.sid = :sid and ad.cId = :cid ORDER BY ad.strDate";
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

		predicateList.add(builder.equal(
				root.get(BshmtWorktimeHist_.bshmtWorktimeHistPK).get(BshmtWorktimeHistPK_.sid),
				empId));
		predicateList.add(builder.equal(
				root.get(BshmtWorktimeHist_.bshmtWorktimeHistPK).get(BshmtWorktimeHistPK_.histId),
				histId));

		query.where(predicateList.toArray(new Predicate[] {}));

		List<BshmtWorktimeHist> result = em.createQuery(query).getResultList();

		// Check exist
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// Return
		return Optional.of(new ShortWorkTimeHistory(new JpaSWorkTimeHistGetMemento(result)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistoryRepository#
	 * findByBaseDate(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<ShortWorkTimeHistory> findByBaseDate(String empId, GeneralDate baseDate) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<BshmtWorktimeHist> query = builder.createQuery(BshmtWorktimeHist.class);
		Root<BshmtWorktimeHist> root = query.from(BshmtWorktimeHist.class);

		List<Predicate> predicateList = new ArrayList<>();

		predicateList.add(builder.equal(
				root.get(BshmtWorktimeHist_.bshmtWorktimeHistPK).get(BshmtWorktimeHistPK_.sid),
				empId));
		predicateList.add(builder.lessThanOrEqualTo(root.get(BshmtWorktimeHist_.strYmd), baseDate));
		predicateList
				.add(builder.greaterThanOrEqualTo(root.get(BshmtWorktimeHist_.endYmd), baseDate));

		query.where(predicateList.toArray(new Predicate[] {}));

		List<BshmtWorktimeHist> result = em.createQuery(query).getResultList();

		// Check exist
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}

		// Return
		return Optional.of(new ShortWorkTimeHistory(new JpaSWorkTimeHistGetMemento(result)));
	}

	@Override
	public Optional<ShortWorkTimeHistory> getBySid(String cid, String sid) {
		List<BshmtWorktimeHist> listHist = this.queryProxy().query(QUERY_GET__BYSID,BshmtWorktimeHist.class)
				.setParameter("sid", sid)
				.setParameter("cid", cid).getList();
		if (listHist != null && !listHist.isEmpty()){
			return Optional.of(toWorkTime(listHist));
		}
		return Optional.empty();
	}
	
	/**
	 * Convert to domain
	 * @param listHist
	 * @return
	 */
	private ShortWorkTimeHistory toWorkTime(List<BshmtWorktimeHist> listHist){
		ShortWorkTimeHistory affDepart = new ShortWorkTimeHistory(listHist.get(0).getCId(), listHist.get(0).getBshmtWorktimeHistPK().sid, new ArrayList<>());
		DateHistoryItem dateItem = null;
		for (BshmtWorktimeHist item : listHist){
			dateItem = new DateHistoryItem(item.getBshmtWorktimeHistPK().getHistId(), new DatePeriod(item.getStrYmd(), item.getEndYmd()));
			affDepart.getHistoryItems().add(dateItem);
		}
		return affDepart;
	}
	
	@Override
	public void add(String cid, String sid, DateHistoryItem histItem) {
		this.commandProxy().insert(toEntity(cid, sid, histItem));
	}

	@Override
	public void update(String sid, DateHistoryItem histItem) {
		BshmtWorktimeHistPK key = new BshmtWorktimeHistPK(sid, histItem.identifier());
		Optional<BshmtWorktimeHist> existItem = this.queryProxy().find(key, BshmtWorktimeHist.class);
		if (!existItem.isPresent()){
			throw new RuntimeException("Invalid BshmtWorktimeHist");
		}
		updateEntity(histItem, existItem.get());
		this.commandProxy().update(existItem.get());
	}

	@Override
	public void delete(String sid, String histId) {
		BshmtWorktimeHistPK key = new BshmtWorktimeHistPK(sid, histId);
		this.commandProxy().remove(BshmtWorktimeHist.class, key);
	}
	/**
	 * Convert from domain to entity
	 * @param cid
	 * @param sid
	 * @param dateItem
	 * @return
	 */
	private BshmtWorktimeHist toEntity(String cid, String sid, DateHistoryItem dateItem){
		BshmtWorktimeHistPK bshmtWorktimeHistPK = new BshmtWorktimeHistPK(sid, dateItem.identifier());
		return new BshmtWorktimeHist(bshmtWorktimeHistPK,cid,dateItem.start(),dateItem.end());
	}
	
	/**
	 * Update entity
	 * @param domain
	 * @param entity
	 */
	private void updateEntity(DateHistoryItem domain,BshmtWorktimeHist entity){
		entity.setStrYmd(domain.start());
		entity.setEndYmd(domain.end());
	}
}
