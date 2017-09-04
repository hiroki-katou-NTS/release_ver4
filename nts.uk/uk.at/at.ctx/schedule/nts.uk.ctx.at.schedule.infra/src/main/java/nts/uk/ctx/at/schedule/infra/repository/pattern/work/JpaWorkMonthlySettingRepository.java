/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.pattern.work;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.dom.shift.pattern.work.WorkMonthlySetting;
import nts.uk.ctx.at.schedule.dom.shift.pattern.work.WorkMonthlySettingRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.pattern.work.KscmtWorkMonthSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.pattern.work.KscmtWorkMonthSetPK;
import nts.uk.ctx.at.schedule.infra.entity.shift.pattern.work.KscmtWorkMonthSetPK_;
import nts.uk.ctx.at.schedule.infra.entity.shift.pattern.work.KscmtWorkMonthSet_;

/**
 * The Class JpaWorkMonthlySettingRepository.
 */
@Stateless
public class JpaWorkMonthlySettingRepository extends JpaRepository
		implements WorkMonthlySettingRepository {

	/** The Constant INDEX_ONE. */
	public static final int INDEX_ONE = 1;
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.pattern.work.
	 * WorkMonthlySettingRepository#addAll(java.util.List)
	 */
	@Override
	public void addAll(List<WorkMonthlySetting> workMonthlySettings) {
		this.commandProxy().insertAll( workMonthlySettings.stream()
				.map(domain -> this.toEntity(domain)).collect(Collectors.toList()));
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.pattern.work.
	 * WorkMonthlySettingRepository#updateAll(java.util.List)
	 */
	@Override
	public void updateAll(List<WorkMonthlySetting> workMonthlySettings) {
		
		// get by input work monthly setting
		List<KscmtWorkMonthSet> entitys = this.toEntityUpdateAll(workMonthlySettings);
		
		// convert to map entity
		Map<BigDecimal, KscmtWorkMonthSet> mapEntity = entitys.stream()
				.collect(Collectors.toMap((entity) -> {
					return entity.getKscmtWorkMonthSetPK().getYmdK();
				}, Function.identity()));
		
		// update all entity
		this.commandProxy().updateAll(workMonthlySettings.stream().map(domain -> {
			KscmtWorkMonthSet entity = new KscmtWorkMonthSet();
			if (mapEntity.containsKey(domain.getYmdk())) {
				entity = mapEntity.get(domain.getYmdk());
			}
			domain.saveToMemento(new JpaWorkMonthlySettingSetMemento(entity));
			return entity;
		}).collect(Collectors.toList()));
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.pattern.work.
	 * WorkMonthlySettingRepository#findById(java.lang.String, java.lang.String,
	 * nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<WorkMonthlySetting> findById(String companyId, String monthlyPatternCode,
			int baseDate) {
		return this.queryProxy()
				.find(new KscmtWorkMonthSetPK(companyId, monthlyPatternCode,
						BigDecimal.valueOf(baseDate)), KscmtWorkMonthSet.class)
				.map(c -> this.toDomain(c));
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.pattern.work.
	 * WorkMonthlySettingRepository#findByStartEndDate(java.lang.String,
	 * java.lang.String, nts.arc.time.GeneralDate, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<WorkMonthlySetting> findByStartEndDate(String companyId, String monthlyPatternCode,
			int startDate, int endDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KWMMT_WORK_MONTH_SET (KwmmtWorkMonthSet SQL)
		CriteriaQuery<KscmtWorkMonthSet> cq = criteriaBuilder.createQuery(KscmtWorkMonthSet.class);

		// root data
		Root<KscmtWorkMonthSet> root = cq.from(KscmtWorkMonthSet.class);

		// select root
		cq.select(root);
		
		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK).get(KscmtWorkMonthSetPK_.cid),
				companyId));
		
		// equal monthly pattern code
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK)
				.get(KscmtWorkMonthSetPK_.mPatternCd), monthlyPatternCode));
		
		// greater than or equal start date
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK).get(KscmtWorkMonthSetPK_.ymdK),
				BigDecimal.valueOf(startDate)));
		
		// less than or equal end date
		lstpredicateWhere.add(criteriaBuilder.lessThan(
				root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK).get(KscmtWorkMonthSetPK_.ymdK),
				BigDecimal.valueOf(endDate)));
				
		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
		
		// order by ymdk id asc
		cq.orderBy(criteriaBuilder.asc(
				root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK).get(KscmtWorkMonthSetPK_.ymdK)));

		// create query
		TypedQuery<KscmtWorkMonthSet> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(item -> this.toDomain(item))
			.collect(Collectors.toList());
	}
	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the work monthly setting
	 */
	private WorkMonthlySetting toDomain(KscmtWorkMonthSet entity) {
		return new WorkMonthlySetting(new JpaWorkMonthlySettingGetMemento(entity));
	}
	
	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the kwmmt work month set
	 */
	private KscmtWorkMonthSet toEntity(WorkMonthlySetting domain){
		KscmtWorkMonthSet entity = new KscmtWorkMonthSet();
		domain.saveToMemento(new JpaWorkMonthlySettingSetMemento(entity));
		return entity;
	}
	
	/**
	 * To entity update all.
	 *
	 * @param workMonthlySettings the work monthly settings
	 * @return the list
	 */
	private List<KscmtWorkMonthSet> toEntityUpdateAll(List<WorkMonthlySetting> workMonthlySettings){
		
		// check exist data by input
		if(CollectionUtil.isEmpty(workMonthlySettings)){
			return new ArrayList<>();
		}
		
		// get company id
		String companyId = workMonthlySettings.get(INDEX_ONE).getCompanyId().v();
		
		// get monthly pattern code
		String monthlyPatternCode = workMonthlySettings.get(INDEX_ONE).getMonthlyPatternCode().v();
		
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KWMMT_WORK_MONTH_SET (KwmmtWorkMonthSet SQL)
		CriteriaQuery<KscmtWorkMonthSet> cq = criteriaBuilder.createQuery(KscmtWorkMonthSet.class);

		// root data
		Root<KscmtWorkMonthSet> root = cq.from(KscmtWorkMonthSet.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK).get(KscmtWorkMonthSetPK_.cid),
				companyId));

		// equal monthly pattern code
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK)
				.get(KscmtWorkMonthSetPK_.mPatternCd), monthlyPatternCode));

		// in base date data list
		lstpredicateWhere.add(criteriaBuilder.and(root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK)
				.get(KscmtWorkMonthSetPK_.ymdK).in(workMonthlySettings.stream()
						.map(setting -> setting.getYmdk()).collect(Collectors.toList()))));
		
		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by ymdk id asc
		cq.orderBy(criteriaBuilder.asc(
				root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK).get(KscmtWorkMonthSetPK_.ymdK)));

		// create query
		TypedQuery<KscmtWorkMonthSet> query = em.createQuery(cq);

		// exclude select
		return query.getResultList();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.pattern.work.
	 * WorkMonthlySettingRepository#findByYMD(java.lang.String,
	 * java.lang.String, java.util.List)
	 */
	@Override
	public List<WorkMonthlySetting> findByYMD(String companyId, String monthlyPatternCode,
			List<BigDecimal> baseDates) {

		// check exist data by input
		if(CollectionUtil.isEmpty(baseDates)){
			return new ArrayList<>();
		}
		
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KWMMT_WORK_MONTH_SET (KwmmtWorkMonthSet SQL)
		CriteriaQuery<KscmtWorkMonthSet> cq = criteriaBuilder.createQuery(KscmtWorkMonthSet.class);

		// root data
		Root<KscmtWorkMonthSet> root = cq.from(KscmtWorkMonthSet.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK).get(KscmtWorkMonthSetPK_.cid),
				companyId));

		// equal monthly pattern code
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK)
				.get(KscmtWorkMonthSetPK_.mPatternCd), monthlyPatternCode));

		// in base date data list
		lstpredicateWhere.add(criteriaBuilder.and(root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK)
				.get(KscmtWorkMonthSetPK_.ymdK).in(baseDates)));
		
		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by ymdk id asc
		cq.orderBy(criteriaBuilder.asc(
				root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK).get(KscmtWorkMonthSetPK_.ymdK)));

		// create query
		TypedQuery<KscmtWorkMonthSet> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomain(entity))
				.collect(Collectors.toList());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.pattern.work.
	 * WorkMonthlySettingRepository#remove(java.lang.String, java.lang.String)
	 */
	@Override
	public void remove(String companyId, String monthlyPatternCode) {
		this.commandProxy().removeAll(this.toEntityRemove(companyId, monthlyPatternCode));
	}
	
	/**
	 * To entity remove.
	 *
	 * @param companyId the company id
	 * @param monthlyPatternCode the monthly pattern code
	 * @return the list
	 */
	private List<KscmtWorkMonthSet> toEntityRemove(String companyId, String monthlyPatternCode){
				
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KWMMT_WORK_MONTH_SET (KwmmtWorkMonthSet SQL)
		CriteriaQuery<KscmtWorkMonthSet> cq = criteriaBuilder.createQuery(KscmtWorkMonthSet.class);

		// root data
		Root<KscmtWorkMonthSet> root = cq.from(KscmtWorkMonthSet.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK).get(KscmtWorkMonthSetPK_.cid),
				companyId));

		// equal monthly pattern code
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK)
				.get(KscmtWorkMonthSetPK_.mPatternCd), monthlyPatternCode));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by ymdk id asc
		cq.orderBy(criteriaBuilder.asc(
				root.get(KscmtWorkMonthSet_.kscmtWorkMonthSetPK).get(KscmtWorkMonthSetPK_.ymdK)));

		// create query
		TypedQuery<KscmtWorkMonthSet> query = em.createQuery(cq);

		// exclude select
		return query.getResultList();
	}

	
}
