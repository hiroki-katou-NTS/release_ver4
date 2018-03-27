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
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.childcareschedule.ChildCareSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.personalfee.WorkSchedulePersonFee;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workschedulebreak.WorkScheduleBreak;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletime.WorkScheduleTime;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletimezone.WorkScheduleTimeZone;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.ScheMasterInfo;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscdtBasicSchedule;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscdtBasicSchedulePK;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscdtScheChildCare;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscdtScheChildCarePK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscdtScheChildCare_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscdtScheFee;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscdtScheFeePK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscdtScheFee_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workschedulebreak.KscdtWorkScheduleBreak;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workschedulebreak.KscdtWorkScheduleBreakPK;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workschedulebreak.KscdtWorkScheduleBreakPK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workschedulebreak.KscdtWorkScheduleBreak_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletime.KscdtScheTime;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletime.KscdtScheTimePK;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletimezone.KscdtWorkScheduleTimeZone;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletimezone.KscdtWorkScheduleTimeZonePK;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletimezone.KscdtWorkScheduleTimeZonePK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.workscheduletimezone.KscdtWorkScheduleTimeZone_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.schedulemaster.KscdtScheMasterInfo;
import nts.uk.ctx.at.schedule.infra.entity.schedule.schedulemaster.KscdtScheMasterInfoPK;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.childcareschedule.JpaChildCareScheduleGetMemento;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.childcareschedule.JpaChildCareScheduleSetMememto;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.personalfee.JpaWorkSchedulePersonFeeGetMemento;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.workscheduletimezone.JpaWorkScheduleTimeZoneSetMemento;

/**
 * The Class JpaBasicScheduleRepository.
 */
@Stateless
public class JpaBasicScheduleRepository extends JpaRepository implements BasicScheduleRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository
	 * #insert(nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule)
	 */
	@Override
	public void insert(BasicSchedule bSchedule) {
		KscdtBasicSchedule x = toEntity(bSchedule);
		this.removeAllChildCare(bSchedule.getEmployeeId(), bSchedule.getDate());
		this.commandProxy().insert(x);
		this.insertAllChildCare(bSchedule.getEmployeeId(), bSchedule.getDate(), bSchedule.getChildCareSchedules());
		List<WorkScheduleTimeZone> list = new ArrayList<>();
		bSchedule.getWorkScheduleTimeZones().stream()
				.filter(map -> (map.getScheduleStartClock() != null && map.getScheduleEndClock() != null))
				.map(map -> list.add(map)).collect(Collectors.toList());
		if (list.size() > 0) {
			this.insertAllWorkScheduleTimeZone(bSchedule.getEmployeeId(), bSchedule.getDate(), list);
		}
		this.insertScheduleMaster(bSchedule.getWorkScheduleMaster());
		this.insertScheduleBreakTime(bSchedule.getEmployeeId(), bSchedule.getDate(), bSchedule.getWorkScheduleBreaks());
		this.insertScheduleTime(bSchedule.getEmployeeId(), bSchedule.getDate(), bSchedule.getWorkScheduleTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository
	 * #update(nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule)
	 */
	@Override
	public void update(BasicSchedule bSchedule) {
		this.commandProxy().update(this.toEntityUpdate(bSchedule));
		this.removeAllChildCare(bSchedule.getEmployeeId(), bSchedule.getDate());
		this.insertAllChildCare(bSchedule.getEmployeeId(), bSchedule.getDate(), bSchedule.getChildCareSchedules());
		this.removeAllTimeZone(bSchedule.getEmployeeId(), bSchedule.getDate());
		this.insertAllWorkScheduleTimeZone(bSchedule.getEmployeeId(), bSchedule.getDate(), bSchedule.getWorkScheduleTimeZones());
		this.updateScheduleMaster(bSchedule.getWorkScheduleMaster());
		this.updateScheduleBreakTime(bSchedule.getEmployeeId(), bSchedule.getDate(), bSchedule.getWorkScheduleBreaks());
		this.updateScheduleTime(bSchedule.getEmployeeId(), bSchedule.getDate(), bSchedule.getWorkScheduleTime());
	}

	/**
	 * update work schedule time zone
	 * 
	 * @param bSchedule
	 * @return
	 */
	private List<KscdtWorkScheduleTimeZone> updateWorkScheduleTimeZone(BasicSchedule bSchedule) {
		List<WorkScheduleTimeZone> scheduleTimeZones = bSchedule.getWorkScheduleTimeZones();
		List<KscdtWorkScheduleTimeZone> entities = new ArrayList<KscdtWorkScheduleTimeZone>();
		scheduleTimeZones.forEach(schedule -> {
			KscdtWorkScheduleTimeZone entity = new KscdtWorkScheduleTimeZone();
			String employeeId = bSchedule.getEmployeeId();
			GeneralDate date = bSchedule.getDate();
			Optional<KscdtWorkScheduleTimeZone> optionalEntity = this.findWorkScheduleTimeZone(employeeId, date,
					schedule.getScheduleCnt());
			// check null of startTime-endTime
			if (schedule.getScheduleStartClock() == null || schedule.getScheduleEndClock() == null) {
				if (optionalEntity.isPresent()) {
					entity = optionalEntity.get();
					this.commandProxy().remove(KscdtWorkScheduleTimeZone.class, entity.kscdtWorkScheduleTimeZonePk);
				}
				return;
			}

			if (optionalEntity.isPresent()) {
				entity = optionalEntity.get();
			}
			schedule.saveToMemento(new JpaWorkScheduleTimeZoneSetMemento(entity, employeeId, date));
			entities.add(entity);
		});
		return entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository
	 * #delete(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public void delete(String employeeId, GeneralDate baseDate) {
		this.commandProxy().remove(KscdtBasicSchedule.class, new KscdtBasicSchedulePK(employeeId, baseDate));
		this.removeAllChildCare(employeeId, baseDate);
		this.removeAllTimeZone(employeeId, baseDate);
		this.removeAllScheduleBreakTime(employeeId, baseDate);
		this.removeScheduleMaster(employeeId, baseDate);
		this.removeScheduleTime(employeeId, baseDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository
	 * #find(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<BasicSchedule> find(String sId, GeneralDate date) {
		Optional<KscdtBasicSchedule> optionalEntity = this.findById(sId, date);
		if (optionalEntity.isPresent()) {
			return Optional.of(this.toDomain(optionalEntity.get(), this.findAllWorkScheduleTimeZone(sId, date)));
		}
		return Optional.empty();
	}
	
	@Override
	public boolean isExists(String employeeId, GeneralDate date) {
		Optional<KscdtBasicSchedule>  result = findById(employeeId, date);
		return result.isPresent();
	}

	/**
	 * Find by id.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param date
	 *            the date
	 * @return the optional
	 */
	private Optional<KscdtBasicSchedule> findById(String employeeId, GeneralDate date) {
		return this.queryProxy().find(new KscdtBasicSchedulePK(employeeId, date), KscdtBasicSchedule.class);
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
		CriteriaQuery<KscdtScheChildCare> cq = criteriaBuilder.createQuery(KscdtScheChildCare.class);

		// root data
		Root<KscdtScheChildCare> root = cq.from(KscdtScheChildCare.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscdtScheChildCare_.kscdtScheChildCarePK).get(KscdtScheChildCarePK_.sid), employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscdtScheChildCare_.kscdtScheChildCarePK).get(KscdtScheChildCarePK_.ymd), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by child care number asc
		cq.orderBy(criteriaBuilder
				.asc(root.get(KscdtScheChildCare_.kscdtScheChildCarePK).get(KscdtScheChildCarePK_.childCareNumber)));

		// create query
		TypedQuery<KscdtScheChildCare> query = em.createQuery(cq);

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
		CriteriaQuery<KscdtScheFee> cq = criteriaBuilder.createQuery(KscdtScheFee.class);

		// root data
		Root<KscdtScheFee> root = cq.from(KscdtScheFee.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(
				criteriaBuilder.equal(root.get(KscdtScheFee_.kscdtScheFeePK).get(KscdtScheFeePK_.sid), employeeId));

		// equal year month date base date
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KscdtScheFee_.kscdtScheFeePK).get(KscdtScheFeePK_.ymd), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by no id asc
		cq.orderBy(criteriaBuilder.asc(root.get(KscdtScheFee_.kscdtScheFeePK).get(KscdtScheFeePK_.no)));

		// create query
		TypedQuery<KscdtScheFee> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomainPersonFee(entity))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<WorkScheduleBreak> findWorkBreakTime(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KscdtWorkScheduleBreak> cq = criteriaBuilder.createQuery(KscdtWorkScheduleBreak.class);

		// root data
		Root<KscdtWorkScheduleBreak> root = cq.from(KscdtWorkScheduleBreak.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscdtWorkScheduleBreak_.kscdtWorkScheduleBreakPk).get(KscdtWorkScheduleBreakPK_.sId), employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscdtWorkScheduleBreak_.kscdtWorkScheduleBreakPk).get(KscdtWorkScheduleBreakPK_.date), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KscdtWorkScheduleBreak> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomainWorkScheduleBreak(entity))
				.collect(Collectors.toList());
	}
	
	/**
	 * Convert entity KscdtWorkScheduleBreak to domain object WorkScheduleBreak
	 * @param entity
	 * @return
	 */
	private WorkScheduleBreak toDomainWorkScheduleBreak(KscdtWorkScheduleBreak entity) {
		return WorkScheduleBreak.createFromJavaType(entity.scheduleStartClock, entity.scheduleStartClock, entity.scheduleEndClock);
	}

	/**
	 * Insert all child care.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param baseDate
	 *            the base date
	 * @param childCareSchedules
	 *            the child care schedules
	 */
	private void insertAllChildCare(String employeeId, GeneralDate baseDate,
			List<ChildCareSchedule> childCareSchedules) {
		if (CollectionUtil.isEmpty(childCareSchedules)) {
			return;
		}
		List<KscdtScheChildCare> entityChildCares = childCareSchedules.stream().map(domain -> {
			KscdtScheChildCare entity = new KscdtScheChildCare();
			domain.saveToMemento(new JpaChildCareScheduleSetMememto(entity, employeeId, baseDate));
			return entity;
		}).collect(Collectors.toList());
		this.commandProxy().insertAll(entityChildCares);
	}

	/**
	 * To domain child care.
	 *
	 * @param entity
	 *            the entity
	 * @return the child care schedule
	 */
	private ChildCareSchedule toDomainChildCare(KscdtScheChildCare entity) {
		return new ChildCareSchedule(new JpaChildCareScheduleGetMemento(entity));
	}

	/**
	 * To domain person fee.
	 *
	 * @param entity
	 *            the entity
	 * @return the work schedule person fee
	 */
	private WorkSchedulePersonFee toDomainPersonFee(KscdtScheFee entity) {
		return new WorkSchedulePersonFee(new JpaWorkSchedulePersonFeeGetMemento(entity));
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the kscdt basic schedule
	 */
	private KscdtBasicSchedule toEntity(BasicSchedule domain) {
		KscdtBasicSchedule entity = new KscdtBasicSchedule();
		domain.saveToMemento(new JpaBasicScheduleSetMemento(entity));
		return entity;
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the kscdt basic schedule
	 */
	private KscdtBasicSchedule toEntityUpdate(BasicSchedule domain) {
		KscdtBasicSchedule entity = new KscdtBasicSchedule();
		Optional<KscdtBasicSchedule> optionalEntity = this.findById(domain.getEmployeeId(), domain.getDate());
		entity = optionalEntity.get();
		domain.saveToMemento(new JpaBasicScheduleSetMemento(entity));
		entity.workTimeCode = StringUtil.isNullOrEmpty(domain.getWorkTimeCode(), true)
				|| ("000").equals(domain.getWorkTimeCode()) ? null : domain.getWorkTimeCode();

		return entity;
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @param entityTimeZones
	 *            the entity time zones
	 * @return the basic schedule
	 */
	private BasicSchedule toDomain(KscdtBasicSchedule entity, List<KscdtWorkScheduleTimeZone> entityTimeZones) {
		return new BasicSchedule(new JpaBasicScheduleGetMemento(entity, entityTimeZones));

	}

	/**
	 * Insert all work schedule time zone.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param baseDate
	 *            the base date
	 * @param list
	 *            the list
	 */
	private void insertAllWorkScheduleTimeZone(String employeeId, GeneralDate baseDate,
			List<WorkScheduleTimeZone> list) {
		if (CollectionUtil.isEmpty(list)) {
			return;
		}
		List<KscdtWorkScheduleTimeZone> entityWorkTimeZone = list.stream().map(domain -> {
			KscdtWorkScheduleTimeZone entity = new KscdtWorkScheduleTimeZone();
			domain.saveToMemento(new JpaWorkScheduleTimeZoneSetMemento(entity, employeeId, baseDate));
			return entity;
		}).collect(Collectors.toList());
		this.commandProxy().insertAll(entityWorkTimeZone);
	}

	/**
	 * Removes the all child care.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param baseDate
	 *            the base date
	 */
	private void removeAllChildCare(String employeeId, GeneralDate baseDate) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSCMT_CHILD_CARE_SCH (KscdtScheChildCare SQL)
		CriteriaDelete<KscdtScheChildCare> cq = criteriaBuilder.createCriteriaDelete(KscdtScheChildCare.class);

		// root data
		Root<KscdtScheChildCare> root = cq.from(KscdtScheChildCare.class);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscdtScheChildCare_.kscdtScheChildCarePK).get(KscdtScheChildCarePK_.sid), employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscdtScheChildCare_.kscdtScheChildCarePK).get(KscdtScheChildCarePK_.ymd), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		em.createQuery(cq).executeUpdate();

	}

	/**
	 * Removes the all time zone.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 */
	private void removeAllTimeZone(String employeeId, GeneralDate baseDate) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSCDT_WORK_SCH_TIMEZONE (KscdtWorkScheduleTimeZone SQL)
		CriteriaDelete<KscdtWorkScheduleTimeZone> cq = criteriaBuilder
				.createCriteriaDelete(KscdtWorkScheduleTimeZone.class);

		// root data
		Root<KscdtWorkScheduleTimeZone> root = cq.from(KscdtWorkScheduleTimeZone.class);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtWorkScheduleTimeZone_.kscdtWorkScheduleTimeZonePk).get(KscdtWorkScheduleTimeZonePK_.sId),
				employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtWorkScheduleTimeZone_.kscdtWorkScheduleTimeZonePk).get(KscdtWorkScheduleTimeZonePK_.date),
				baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		em.createQuery(cq).executeUpdate();

	}

	/**
	 * find employeeId
	 * 
	 * @param employeeId
	 * @param date
	 * @param scheduleCnt
	 * @return
	 */
	private Optional<KscdtWorkScheduleTimeZone> findWorkScheduleTimeZone(String employeeId, GeneralDate date,
			int scheduleCnt) {
		return this.queryProxy().find(new KscdtWorkScheduleTimeZonePK(employeeId, date, scheduleCnt),
				KscdtWorkScheduleTimeZone.class);
	}

	/**
	 * Find all work schedule time zone.
	 *
	 * @param employeeId
	 *            the employee id
	 * @param baseDate
	 *            the base date
	 * @return the list
	 */
	private List<KscdtWorkScheduleTimeZone> findAllWorkScheduleTimeZone(String employeeId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSCDT_WORK_SCH_TIMEZONE (KscdtWorkScheduleTimeZone SQL)
		CriteriaQuery<KscdtWorkScheduleTimeZone> cq = criteriaBuilder.createQuery(KscdtWorkScheduleTimeZone.class);

		// root data
		Root<KscdtWorkScheduleTimeZone> root = cq.from(KscdtWorkScheduleTimeZone.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtWorkScheduleTimeZone_.kscdtWorkScheduleTimeZonePk).get(KscdtWorkScheduleTimeZonePK_.sId),
				employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtWorkScheduleTimeZone_.kscdtWorkScheduleTimeZonePk).get(KscdtWorkScheduleTimeZonePK_.date),
				baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KscdtWorkScheduleTimeZone> query = em.createQuery(cq);

		// exclude select
		return query.getResultList();
	}
	
	/**
	 * insert 勤務予定マスタ情報
	 * @param workScheduleMaster
	 */
	private void insertScheduleMaster(ScheMasterInfo scheMasterInfo) {
		if (scheMasterInfo == null) {
			return;
		}
		
		KscdtScheMasterInfoPK primaryKey = new KscdtScheMasterInfoPK(scheMasterInfo.getSId(), scheMasterInfo.getGeneralDate());
		
		KscdtScheMasterInfo sscdtScheMasterInfo = new KscdtScheMasterInfo(
				primaryKey,
				scheMasterInfo.getEmploymentCd(),
				scheMasterInfo.getClassificationCd(),
				scheMasterInfo.getWorkTypeCd(),
				scheMasterInfo.getJobId(),
				scheMasterInfo.getWorkplaceId());
		this.commandProxy().insert(sscdtScheMasterInfo);
	}

	/**
	 * update 勤務予定マスタ情報
	 * @param workScheduleMaster
	 */
	private void updateScheduleMaster(ScheMasterInfo scheMasterInfo) {
		if (scheMasterInfo == null) {
			return;
		}
		
		KscdtScheMasterInfoPK primaryKey = new KscdtScheMasterInfoPK(scheMasterInfo.getSId(), scheMasterInfo.getGeneralDate());		
		KscdtScheMasterInfo kscdtScheMasterInfo = this.queryProxy().find(primaryKey, KscdtScheMasterInfo.class).get();
		
		kscdtScheMasterInfo.employmentCd = scheMasterInfo.getEmploymentCd();
		kscdtScheMasterInfo.classificationCd = scheMasterInfo.getClassificationCd();
		kscdtScheMasterInfo.workTypeCd = scheMasterInfo.getWorkTypeCd();
		kscdtScheMasterInfo.jobId = scheMasterInfo.getJobId();
		kscdtScheMasterInfo.workplaceId = scheMasterInfo.getWorkplaceId();
		
		this.commandProxy().update(kscdtScheMasterInfo);
	}
	
	/**
	 * remove 勤務予定マスタ情報
	 * @param employeeId
	 * @param baseDate
	 */
	private void removeScheduleMaster(String employeeId, GeneralDate baseDate) {
		KscdtScheMasterInfoPK primaryKey = new KscdtScheMasterInfoPK(employeeId, baseDate);
		if (this.queryProxy().find(primaryKey, KscdtScheMasterInfo.class).isPresent()) {
			this.commandProxy().remove(KscdtScheMasterInfo.class, new KscdtScheMasterInfoPK(employeeId, baseDate));
		}
	}
	
	/**
	 * insert 勤務予定休憩
	 * @param workScheduleBreaks
	 */
	private void insertScheduleBreakTime(String employeeId, GeneralDate baseDate, List<WorkScheduleBreak> workScheduleBreaks) {
		if (CollectionUtil.isEmpty(workScheduleBreaks)) {
			return;
		}
		
		List<KscdtWorkScheduleBreak> entityWorkBreakList = workScheduleBreaks.stream().map(domain -> {
			KscdtWorkScheduleBreakPK key = new KscdtWorkScheduleBreakPK(employeeId, baseDate, domain.getScheduleBreakCnt().v());
			KscdtWorkScheduleBreak entity = new KscdtWorkScheduleBreak(key, domain.getScheduledStartClock().v(), domain.getScheduledEndClock().v());
			return entity;
		}).collect(Collectors.toList());
		
		this.commandProxy().insertAll(entityWorkBreakList);
	}

	/**
	 * update 勤務予定休憩
	 * @param workScheduleBreaks
	 */
	private void updateScheduleBreakTime(String employeeId, GeneralDate baseDate, List<WorkScheduleBreak> workScheduleBreaks) {
		if (CollectionUtil.isEmpty(workScheduleBreaks)) {
			return;
		}
		
		this.removeAllScheduleBreakTime(employeeId, baseDate);
		this.insertScheduleBreakTime(employeeId, baseDate, workScheduleBreaks);
	}
	
	/**
	 * Removes the all time zone.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 */
	private void removeAllScheduleBreakTime(String employeeId, GeneralDate baseDate) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSCDT_WORK_SCH_TIMEZONE (KscdtWorkScheduleBreak SQL)
		CriteriaDelete<KscdtWorkScheduleBreak> cq = criteriaBuilder
				.createCriteriaDelete(KscdtWorkScheduleBreak.class);

		// root data
		Root<KscdtWorkScheduleBreak> root = cq.from(KscdtWorkScheduleBreak.class);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtWorkScheduleBreak_.kscdtWorkScheduleBreakPk).get(KscdtWorkScheduleBreakPK_.sId),
				employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtWorkScheduleBreak_.kscdtWorkScheduleBreakPk).get(KscdtWorkScheduleBreakPK_.date),
				baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		em.createQuery(cq).executeUpdate();

	}
	
	/**
	 * insert 勤務予定時間
	 * @param workScheduleTime
	 */
	private void insertScheduleTime(String employeeId, GeneralDate baseDate, Optional<WorkScheduleTime> workScheduleTime) {
		if (!workScheduleTime.isPresent()) {
			return;
		}
		
		WorkScheduleTime scheduleTime = workScheduleTime.get();
		KscdtScheTimePK key = new KscdtScheTimePK(employeeId, baseDate);
		KscdtScheTime entity = new KscdtScheTime(key);
		entity.setBreakTime(scheduleTime.getBreakTime().valueAsMinutes());
		entity.setChildCareTime(scheduleTime.getChildCareTime().valueAsMinutes());
		entity.setPrescribedTime(scheduleTime.getPredetermineTime().valueAsMinutes());
		entity.setTotalLaborTime(scheduleTime.getTotalLaborTime().valueAsMinutes());
		entity.setWeekdayTime(scheduleTime.getWeekdayTime().valueAsMinutes());
		entity.setWorkingTime(scheduleTime.getWorkingTime().valueAsMinutes());
		this.commandProxy().insert(entity);
	}
	
	/**
	 * update 勤務予定時間
	 * @param workScheduleTime
	 */
	private void updateScheduleTime(String employeeId, GeneralDate baseDate, Optional<WorkScheduleTime> workScheduleTime) {
		if (!workScheduleTime.isPresent()) {
			return;
		}
		
		WorkScheduleTime scheduleTime = workScheduleTime.get();
		KscdtScheTimePK key = new KscdtScheTimePK(employeeId, baseDate);
		KscdtScheTime entity = this.queryProxy().find(key, KscdtScheTime.class).get();
		entity.setBreakTime(scheduleTime.getBreakTime().valueAsMinutes());
		entity.setChildCareTime(scheduleTime.getChildCareTime().valueAsMinutes());
		entity.setPrescribedTime(scheduleTime.getPredetermineTime().valueAsMinutes());
		entity.setTotalLaborTime(scheduleTime.getTotalLaborTime().valueAsMinutes());
		entity.setWeekdayTime(scheduleTime.getWeekdayTime().valueAsMinutes());
		entity.setWorkingTime(scheduleTime.getWorkingTime().valueAsMinutes());
		this.commandProxy().update(entity);
	}
	
	/**
	 * remove 勤務予定時間
	 * @param workScheduleTime
	 */
	private void removeScheduleTime(String employeeId, GeneralDate baseDate) {
		KscdtScheTimePK key = new KscdtScheTimePK(employeeId, baseDate);
		if (this.queryProxy().find(key, KscdtScheTime.class).isPresent()) {
			this.commandProxy().remove(KscdtScheTime.class, new KscdtScheTimePK(employeeId, baseDate));
		}
	}
}
