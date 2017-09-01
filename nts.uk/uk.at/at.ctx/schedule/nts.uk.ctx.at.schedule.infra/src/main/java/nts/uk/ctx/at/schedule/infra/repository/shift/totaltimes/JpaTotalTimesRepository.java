/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.shift.totaltimes;

import java.util.ArrayList;
import java.util.Collections;
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
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimes;
import nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.totaltimes.KshstTotalTimes;
import nts.uk.ctx.at.schedule.infra.entity.shift.totaltimes.KshstTotalTimesPK;
import nts.uk.ctx.at.schedule.infra.entity.shift.totaltimes.KshstTotalTimesPK_;
import nts.uk.ctx.at.schedule.infra.entity.shift.totaltimes.KshstTotalTimes_;

/**
 * The Class JpaTotalTimesRepository.
 */
@Stateless
public class JpaTotalTimesRepository extends JpaRepository implements TotalTimesRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesRepository#
	 * getAllTotalTimes(java.lang.String)
	 */
	@Override
	public List<TotalTimes> getAllTotalTimes(String companyId) {
		EntityManager em = this.getEntityManager();

		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<KshstTotalTimes> query = builder.createQuery(KshstTotalTimes.class);
		Root<KshstTotalTimes> root = query.from(KshstTotalTimes.class);

		List<Predicate> predicateList = new ArrayList<>();

		predicateList.add(
				builder.equal(root.get(KshstTotalTimes_.kshstTotalTimesPK).get(KshstTotalTimesPK_.cid), companyId));

		query.where(predicateList.toArray(new Predicate[] {}));

		// order by closure id asc
		query.orderBy(builder.asc(root.get(KshstTotalTimes_.kshstTotalTimesPK).get(KshstTotalTimesPK_.totalTimesNo)));

		List<KshstTotalTimes> result = em.createQuery(query).getResultList();

		if (result.isEmpty()) {
			return Collections.emptyList();
		}

		return result.stream().map(entity -> new TotalTimes(new JpaTotalTimesGetMemento(entity)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesRepository#
	 * getTotalTimesDetail(java.lang.String, java.lang.Integer)
	 */
	@Override
	public Optional<TotalTimes> getTotalTimesDetail(String companyId, Integer totalCountNo) {
		KshstTotalTimesPK kshstTotalTimesPK = new KshstTotalTimesPK(companyId, totalCountNo);

		Optional<KshstTotalTimes> optKshstTotalTimes = this.queryProxy().find(kshstTotalTimesPK, KshstTotalTimes.class);

		if (!optKshstTotalTimes.isPresent()) {
			return Optional.empty();
		}

		return Optional.of(new TotalTimes(new JpaTotalTimesGetMemento(optKshstTotalTimes.get())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimesRepository#update(
	 * nts.uk.ctx.at.schedule.dom.shift.totaltimes.TotalTimes)
	 */
	@Override
	public void update(TotalTimes totalTimes) {
		Optional<KshstTotalTimes> optional = this.queryProxy().find(
				new KshstTotalTimesPK(totalTimes.getCompanyId().v(), totalTimes.getTotalCountNo()),
				KshstTotalTimes.class);

		if (!optional.isPresent()) {
			throw new RuntimeException("Total times not existed.");
		}

		KshstTotalTimes entity = optional.get();
		totalTimes.saveToMemento(new JpaTotalTimesSetMemento(entity));
		this.commandProxy().update(entity);
	}

}
