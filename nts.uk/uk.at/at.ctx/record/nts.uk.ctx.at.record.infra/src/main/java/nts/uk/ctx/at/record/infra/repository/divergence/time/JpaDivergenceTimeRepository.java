package nts.uk.ctx.at.record.infra.repository.divergence.time;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import nts.uk.ctx.at.record.dom.divergence.time.*;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcAttendance;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcAttendancePK;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcAttendancePK_;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcAttendance_;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTime;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTimePK;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTimePK_;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTime_;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class JpaDivergenceTimeRepository.
 */
@Stateless
public class JpaDivergenceTimeRepository extends JpaRepository implements DivergenceTimeRepository {

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository#getAllDivTime(java.lang.String)
	 */
	@Override
	public List<DivergenceTime> getAllDivTime(String companyId) {
		return this.findByCompanyId(companyId);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository#update(nts.uk.ctx.at.record.dom.divergence.time.DivergenceTime)
	 */
	@Override
	public void update(DivergenceTime divTimeDomain) {

		if (divTimeDomain.getDivTimeUseSet().value == 1) {
			// Update Divergence Time Information
			this.commandProxy().update(this.toEntity(divTimeDomain));

			// Delete Divergence Attendance List
			this.deleteAllAttendance(divTimeDomain.getDivergenceTimeNo(), divTimeDomain.getCompanyId());

			// Get Divergence Attendance Entity List
			List<KrcstDvgcAttendance> attendanceList = divTimeDomain.getTargetItems().stream()
					.map(e -> toEntityAttendance(divTimeDomain.getDivergenceTimeNo(), divTimeDomain.getCompanyId(),
							e.intValue()))
					.collect(Collectors.toList());

			// Add new Divergence Attendance List
			this.addAttendance(attendanceList);
		} else {
			// get primaty Key
			KrcstDvgcTimePK PK = new KrcstDvgcTimePK(divTimeDomain.getDivergenceTimeNo(),
					AppContexts.user().companyId());

			// get optional entity
			Optional<KrcstDvgcTime> optionalEntity = this.queryProxy().find(PK, KrcstDvgcTime.class);

			// if entity is present
			if (optionalEntity.isPresent()) {
				KrcstDvgcTime entity = optionalEntity.get();
				// update UsesageSet
				entity.setDvgcTimeUseSet(new BigDecimal(divTimeDomain.getDivTimeUseSet().value));
				this.commandProxy().update(entity);
			}

		}

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository#getDivTimeInfo(java.lang.String, int)
	 */
	@Override
	public DivergenceTime getDivTimeInfo(String companyId, int divTimeNo) {

		return this.findByCode(companyId, divTimeNo);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository#findAttendanceId(java.lang.String, int)
	 */
	@Override
	public List<Integer> findAttendanceId(String companyId, int divTimeNo) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcstDvgcAttendance> cq = criteriaBuilder.createQuery(KrcstDvgcAttendance.class);
		Root<KrcstDvgcAttendance> root = cq.from(KrcstDvgcAttendance.class);

		// Build query
		cq.select(root);

		// create where conditions
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(
				criteriaBuilder.equal(root.get(KrcstDvgcAttendance_.id).get(KrcstDvgcAttendancePK_.cid), companyId));
		predicates.add(
				criteriaBuilder.equal(root.get(KrcstDvgcAttendance_.id).get(KrcstDvgcAttendancePK_.no), divTimeNo));

		// add where to query
		cq.where(predicates.toArray(new Predicate[] {}));

		// query data
		List<KrcstDvgcAttendance> KrcstDvgcAttendance = em.createQuery(cq).getResultList();

		// return
		return KrcstDvgcAttendance.isEmpty() ? new ArrayList<Integer>()
				: KrcstDvgcAttendance.stream().map(item -> item.getId().getAttendanceId()).collect(Collectors.toList());

	}

	/**
	 * To domain.
	 *
	 * @param entities
	 *            the entities
	 * @return the divergence time
	 */
	private DivergenceTime toDomain(KrcstDvgcTime entities) {

		DivergenceTimeGetMemento memento = new JpaDivergenceTimeRepositoryGetMemento(entities);

		return new DivergenceTime(memento);
	}

	/**
	 * To entity attendance.
	 *
	 * @param divTimeNo
	 *            the div time no
	 * @param companyId
	 *            the company id
	 * @param attendanceId
	 *            the attendance id
	 * @return the krcst dvgc attendance
	 */
	private KrcstDvgcAttendance toEntityAttendance(Integer divTimeNo, String companyId, Integer attendanceId) {

		KrcstDvgcAttendancePK PK = new KrcstDvgcAttendancePK(divTimeNo, companyId, attendanceId);

		KrcstDvgcAttendance entity = new KrcstDvgcAttendance();
		entity.setId(PK);

		return entity;
	}

	/**
	 * Delete all attendance.
	 *
	 * @param divTimeNo
	 *            the div time no
	 * @param companyId
	 *            the company id
	 */
	private void deleteAllAttendance(int divTimeNo, String companyId) {

		// Get List entity
		List<KrcstDvgcAttendance> attendanceList = findAttendanceEntity(companyId, divTimeNo);

		// Remove List
		// this.commandProxy().removeAll(KrcstDvgcAttendance.class,
		// attendanceList);
		this.commandProxy().removeAll(attendanceList);
		this.getEntityManager().flush();

	}

	/**
	 * Adds the attendance.
	 *
	 * @param attendanceList
	 *            the attendance list
	 */
	private void addAttendance(List<KrcstDvgcAttendance> attendanceList) {

		this.commandProxy().insertAll(attendanceList);

	}

	/**
	 * Find by company id.
	 *
	 * @param companyId
	 *            the company id
	 * @return the list
	 */
	private List<DivergenceTime> findByCompanyId(String companyId) {
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
		return KrcstDvgcTime.isEmpty() ? new ArrayList<DivergenceTime>()
				: KrcstDvgcTime.stream().map(item -> this.toDomain(item)).collect(Collectors.toList());
	}

	/**
	 * Find by code.
	 *
	 * @param companyId
	 *            the company id
	 * @param divTimeNo
	 *            the div time no
	 * @return the divergence time
	 */
	private DivergenceTime findByCode(String companyId, int divTimeNo) {
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
		return new DivergenceTime(null);

	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the krcst dvgc time
	 */
	private KrcstDvgcTime toEntity(DivergenceTime domain) {

		KrcstDvgcTimePK PK = new KrcstDvgcTimePK(domain.getDivergenceTimeNo(), domain.getCompanyId());
		KrcstDvgcTime entity = this.queryProxy().find(PK, KrcstDvgcTime.class).orElse(new KrcstDvgcTime());

		domain.saveToMemento(new JpaDivergenceTimeRepositorySetMemento(entity));

		return entity;

	}

	/**
	 * Find attendance entity.
	 *
	 * @param companyId
	 *            the company id
	 * @param divTimeNo
	 *            the div time no
	 * @return the list
	 */
	private List<KrcstDvgcAttendance> findAttendanceEntity(String companyId, int divTimeNo) {
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcstDvgcAttendance> cq = criteriaBuilder.createQuery(KrcstDvgcAttendance.class);
		Root<KrcstDvgcAttendance> root = cq.from(KrcstDvgcAttendance.class);

		// Build query
		cq.select(root);

		// create where conditions
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(
				criteriaBuilder.equal(root.get(KrcstDvgcAttendance_.id).get(KrcstDvgcAttendancePK_.cid), companyId));
		predicates.add(
				criteriaBuilder.equal(root.get(KrcstDvgcAttendance_.id).get(KrcstDvgcAttendancePK_.no), divTimeNo));

		// add where to query
		cq.where(predicates.toArray(new Predicate[] {}));

		// query data
		List<KrcstDvgcAttendance> KrcstDvgcAttendance = em.createQuery(cq).getResultList();

		// return
		return KrcstDvgcAttendance.isEmpty() ? new ArrayList<KrcstDvgcAttendance>() : KrcstDvgcAttendance;

	}

}
