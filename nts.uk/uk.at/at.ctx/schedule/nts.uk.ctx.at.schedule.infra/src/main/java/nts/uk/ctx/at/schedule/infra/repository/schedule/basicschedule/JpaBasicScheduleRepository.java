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

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.childcareschedule.ChildCareSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.personalfee.WorkSchedulePersonFee;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscdtBasicSchedule;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.KscdtBasicSchedulePK;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscdtScheChildCare;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscdtScheChildCarePK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.childcareschedule.KscdtScheChildCare_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscdtScheFee;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscdtScheFeePK_;
import nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule.personalfee.KscdtScheFee_;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.childcareschedule.JpaChildCareScheduleGetMemento;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.childcareschedule.JpaChildCareScheduleSetMememto;
import nts.uk.ctx.at.schedule.infra.repository.schedule.basicschedule.personalfee.JpaWorkSchedulePersonFeeGetMemento;

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
		this.commandProxy().insert(x);
		this.insertAllChildCare(bSchedule.getEmployeeId(), bSchedule.getDate(),
				bSchedule.getChildCareSchedules());
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
		this.commandProxy().remove(KscdtBasicSchedule.class,
				new KscdtBasicSchedulePK(employeeId, baseDate));
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
		return this.findById(sId, date).map(x -> toDomain(x));
	}
	
	/**
	 * Find by id.
	 *
	 * @param employeeId the employee id
	 * @param date the date
	 * @return the optional
	 */
	private Optional<KscdtBasicSchedule> findById(String employeeId, GeneralDate date){
		return this.queryProxy().find(new KscdtBasicSchedulePK(employeeId,date), KscdtBasicSchedule.class);
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
		CriteriaQuery<KscdtScheChildCare> cq = criteriaBuilder
				.createQuery(KscdtScheChildCare.class);

		// root data
		Root<KscdtScheChildCare> root = cq.from(KscdtScheChildCare.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal employee id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtScheChildCare_.kscdtScheChildCarePK).get(KscdtScheChildCarePK_.sid),
				employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtScheChildCare_.kscdtScheChildCarePK).get(KscdtScheChildCarePK_.ymd),
				baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[]{}));

		// order by child care number asc
		cq.orderBy(criteriaBuilder.asc(root.get(KscdtScheChildCare_.kscdtScheChildCarePK)
				.get(KscdtScheChildCarePK_.childCareNumber)));

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
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KscdtScheFee_.kscdtScheFeePK).get(KscdtScheFeePK_.sid), employeeId));

		// equal year month date base date
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KscdtScheFee_.kscdtScheFeePK).get(KscdtScheFeePK_.ymd), baseDate));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[]{}));

		// order by no id asc
		cq.orderBy(criteriaBuilder
				.asc(root.get(KscdtScheFee_.kscdtScheFeePK).get(KscdtScheFeePK_.no)));

		// create query
		TypedQuery<KscdtScheFee> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomainPersonFee(entity))
				.collect(Collectors.toList());
	}

	/**
	 * Insert all child care.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @param childCareSchedules the child care schedules
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
	 * @param entity the entity
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
	 * @param domain the domain
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
	 * @param domain the domain
	 * @return the kscdt basic schedule
	 */
	private KscdtBasicSchedule toEntityUpdate(BasicSchedule domain) {
		KscdtBasicSchedule entity = new KscdtBasicSchedule();
		Optional<KscdtBasicSchedule> optionalEntity = this.findById(domain.getEmployeeId(), domain.getDate());
		if (optionalEntity.isPresent()) {
			entity = optionalEntity.get();
		}
		domain.saveToMemento(new JpaBasicScheduleSetMemento(entity));
		entity.workTimeCode = StringUtil.isNullOrEmpty(domain.getWorkTimeCode(), true)
				|| ("000").equals(domain.getWorkTimeCode()) ? "   " : domain.getWorkTimeCode();
		
		return entity;
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the basic schedule
	 */
	private BasicSchedule toDomain(KscdtBasicSchedule entity) {
		return new BasicSchedule(new JpaBasicScheduleGetMemento(entity));

	}


}
