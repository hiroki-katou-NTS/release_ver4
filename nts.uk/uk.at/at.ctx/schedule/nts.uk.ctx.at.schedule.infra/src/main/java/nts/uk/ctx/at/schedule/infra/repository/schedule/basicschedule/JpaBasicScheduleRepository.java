/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.childcareschedule.ChildCareSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.personalfee.WorkSchedulePersonFee;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscdpBasicSchedulePK;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscdtBasicSchedule;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscmtChildCareSch;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscmtChildCareSchPK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscmtChildCareSch_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscmtWsPersonFee;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscmtWsPersonFeePK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscmtWsPersonFee_;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.childcareschedule.JpaChildCareScheduleGetMemento;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.personalfee.JpaWorkSchedulePersonFeeGetMemento;

/**
 * 
 * @author sonnh1
 *
 */
@Stateless
// @Transactional
public class JpaBasicScheduleRepository extends JpaRepository implements BasicScheduleRepository {

	/**
	 * Convert Domain to Entity
	 * 
	 * @param domain
	 * @return
	 */
	private static KscdtBasicSchedule toEntity(BasicSchedule domain) {
		val entity = new KscdtBasicSchedule();

		entity.kscdpBSchedulePK = new KscdpBasicSchedulePK(domain.getSId(), domain.getDate());
		entity.workTimeCode = StringUtil.isNullOrEmpty(domain.getWorkTimeCode(), true)
				|| ("000").equals(domain.getWorkTimeCode()) ? "   " : domain.getWorkTimeCode();
		entity.workTypeCode = domain.getWorkTypeCode();
		entity.confirmedAtr = domain.getConfirmedAtr().value;
		entity.workingDayAtr = domain.getWorkDayAtr().value;

		return entity;
	}

	/**
	 * Convert Entity to Domain
	 * 
	 * @param entity
	 * @return
	 */
	private static BasicSchedule toDomain(KscdtBasicSchedule entity) {
		val domain = BasicSchedule.createFromJavaType(entity.kscdpBSchedulePK.sId, entity.kscdpBSchedulePK.date,
				entity.workTypeCode, entity.workTimeCode, entity.confirmedAtr, entity.workingDayAtr);
		return domain;
	}

	/**
	 * Insert data to Basic Schedule
	 * 
	 * @param bSchedule
	 */
	@Override
	public void insert(BasicSchedule bSchedule) {
		KscdtBasicSchedule x = toEntity(bSchedule);
		this.commandProxy().insert(x);
	}

	/**
	 * update data to Basic Schedule
	 * 
	 * @param bSchedule
	 */
	@Override
	public void update(BasicSchedule bSchedule) {
		KscdpBasicSchedulePK pk = new KscdpBasicSchedulePK(bSchedule.getSId(), bSchedule.getDate());
		KscdtBasicSchedule entity = this.queryProxy()
				.find(new KscdpBasicSchedulePK(bSchedule.getSId(), bSchedule.getDate()), KscdtBasicSchedule.class)
				.get();
		entity.kscdpBSchedulePK = pk;
		entity.workTimeCode = StringUtil.isNullOrEmpty(bSchedule.getWorkTimeCode(), true)
				|| ("000").equals(bSchedule.getWorkTimeCode()) ? "   " : bSchedule.getWorkTimeCode();
		entity.workTypeCode = bSchedule.getWorkTypeCode();
		entity.confirmedAtr = bSchedule.getConfirmedAtr().value;
		entity.workingDayAtr = bSchedule.getWorkDayAtr().value;

		this.commandProxy().update(entity);
	}

	/**
	 * Get BasicSchedule
	 */
	@Override
	public Optional<BasicSchedule> find(String sId, GeneralDate date) {
		return this.queryProxy().find(new KscdpBasicSchedulePK(sId, date), KscdtBasicSchedule.class)
				.map(x -> toDomain(x));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository
	 * #findChildCareById(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<ChildCareSchedule> findChildCareById(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSCMT_CHILD_CARE_SCH (KscmtChildCareSch SQL)
		CriteriaQuery<KscmtChildCareSch> cq = criteriaBuilder.createQuery(KscmtChildCareSch.class);

		// root data
		Root<KscmtChildCareSch> root = cq.from(KscmtChildCareSch.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscmtChildCareSch_.kscmtChildCareSchPK).get(KscmtChildCareSchPK_.sid), employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscmtChildCareSch_.kscmtChildCareSchPK).get(KscmtChildCareSchPK_.ymd), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by child care number asc
		cq.orderBy(criteriaBuilder
				.asc(root.get(KscmtChildCareSch_.kscmtChildCareSchPK).get(KscmtChildCareSchPK_.childCareNumber)));

		// create query
		TypedQuery<KscmtChildCareSch> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomainChildCare(entity))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository
	 * #findPersonFeeById(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<WorkSchedulePersonFee> findPersonFeeById(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSCMT_WS_PERSON_FEE (KscmtWsPersonFee SQL)
		CriteriaQuery<KscmtWsPersonFee> cq = criteriaBuilder.createQuery(KscmtWsPersonFee.class);

		// root data
		Root<KscmtWsPersonFee> root = cq.from(KscmtWsPersonFee.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscmtWsPersonFee_.kscmtWsPersonFeePK).get(KscmtWsPersonFeePK_.sid), employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscmtWsPersonFee_.kscmtWsPersonFeePK).get(KscmtWsPersonFeePK_.ymd), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by no id asc
		cq.orderBy(criteriaBuilder.asc(root.get(KscmtWsPersonFee_.kscmtWsPersonFeePK).get(KscmtWsPersonFeePK_.no)));

		// create query
		TypedQuery<KscmtWsPersonFee> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomainPersonFee(entity))
				.collect(Collectors.toList());
	}

	/**
	 * To domain child care.
	 *
	 * @param entity
	 *            the entity
	 * @return the child care schedule
	 */
	private ChildCareSchedule toDomainChildCare(KscmtChildCareSch entity) {
		return new ChildCareSchedule(new JpaChildCareScheduleGetMemento(entity));
	}

	/**
	 * To domain person fee.
	 *
	 * @param entity
	 *            the entity
	 * @return the work schedule person fee
	 */
	private WorkSchedulePersonFee toDomainPersonFee(KscmtWsPersonFee entity) {
		return new WorkSchedulePersonFee(new JpaWorkSchedulePersonFeeGetMemento(entity));
	}

}
