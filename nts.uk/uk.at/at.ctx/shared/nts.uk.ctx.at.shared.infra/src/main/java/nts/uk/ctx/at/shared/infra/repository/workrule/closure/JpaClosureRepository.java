/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.workrule.closure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosure;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosureHist;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosureHistPK;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosureHistPK_;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosureHist_;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosurePK;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosurePK_;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosure_;

/**
 * The Class JpaClosureRepository.
 */
@Stateless
public class JpaClosureRepository extends JpaRepository implements ClosureRepository {

	/** The Constant FIRST_DATA. */
	public static final int FIRST_DATA = 0;

	/** The Constant FIRST_LENGTH. */
	public static final int FIRST_LENGTH = 1;

	/** The Constant FIND_BY_CURRENT_YEARMONTH_AND_USED. */
	public static final String FIND_BY_CURRENT_YEARMONTH_AND_USED = "SELECT his from KclmtClosureHist his "
			+ "JOIN KclmtClosure c "
			+ "ON his.kclmtClosureHistPK.cid = c.kclmtClosurePK.cid "
			+ "AND his.kclmtClosureHistPK.closureId = c.kclmtClosurePK.closureId "
			+ "WHERE his.kclmtClosureHistPK.cid = :comId "
			+ "AND c.useClass = 1 " // is used
			+ "AND his.kclmtClosureHistPK.strYM <= :baseDate "
			+ "AND his.endYM >= :baseDate";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#add(nts.uk.
	 * ctx.at.shared.dom.workrule.closure.Closure)
	 */
	@Override
	public void add(Closure closure) {
		this.commandProxy().insert(this.toEntity(closure));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#update(nts.uk
	 * .ctx.at.shared.dom.workrule.closure.Closure)
	 */
	@Override
	public void update(Closure closure) {
		this.commandProxy().update(this.toEntityUpdate(closure));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#findAll(java.
	 * lang.String)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Closure> findAll(String companyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KCLMT_CLOSURE (KclmtClosure SQL)
		CriteriaQuery<KclmtClosure> cq = criteriaBuilder.createQuery(KclmtClosure.class);

		// root data
		Root<KclmtClosure> root = cq.from(KclmtClosure.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KclmtClosure_.kclmtClosurePK).get(KclmtClosurePK_.cid), companyId));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by closure id asc
		cq.orderBy(criteriaBuilder.asc(root.get(KclmtClosure_.kclmtClosurePK).get(KclmtClosurePK_.closureId)));

		// create query
		TypedQuery<KclmtClosure> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream()
				.map(entity -> this.toDomain(entity,
						this.findHistoryByClosureId(companyId, entity.getKclmtClosurePK().getClosureId())))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#findAllUse(
	 * java.lang.String)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Closure> findAllUse(String companyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KCLMT_CLOSURE (KclmtClosure SQL)
		CriteriaQuery<KclmtClosure> cq = criteriaBuilder.createQuery(KclmtClosure.class);

		// root data
		Root<KclmtClosure> root = cq.from(KclmtClosure.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KclmtClosure_.kclmtClosurePK).get(KclmtClosurePK_.cid), companyId));

		// is use closure
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KclmtClosure_.useClass), UseClassification.UseClass_Use.value));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by closure id asc
		cq.orderBy(criteriaBuilder.asc(root.get(KclmtClosure_.kclmtClosurePK).get(KclmtClosurePK_.closureId)));

		// create query
		TypedQuery<KclmtClosure> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream()
				.map(entity -> this.toDomain(entity,
						this.findHistoryByClosureId(companyId, entity.getKclmtClosurePK().getClosureId())))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#findById(java
	 * .lang.String, int)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<Closure> findById(String companyId, int closureId) {
		return this.queryProxy().find(new KclmtClosurePK(companyId, closureId), KclmtClosure.class)
				.map(c -> this.toDomain(c, this.findHistoryByClosureId(companyId, closureId)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#findByListId(
	 * java.lang.String, java.util.List)
	 */
	@Override
	public List<Closure> findByListId(String companyId, List<Integer> closureIds) {

		// check closure id empty
		if (CollectionUtil.isEmpty(closureIds)) {
			return new ArrayList<>();
		}
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KCLMT_CLOSURE (KclmtClosure SQL)
		CriteriaQuery<KclmtClosure> cq = criteriaBuilder.createQuery(KclmtClosure.class);

		// root data
		Root<KclmtClosure> root = cq.from(KclmtClosure.class);

		// select root
		cq.select(root);
		
		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KclmtClosure_.kclmtClosurePK).get(KclmtClosurePK_.cid), companyId));

		// in closure id
		lstpredicateWhere.add(root.get(KclmtClosure_.kclmtClosurePK).get(KclmtClosurePK_.closureId)
				.in(closureIds));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by closure id asc
		cq.orderBy(criteriaBuilder
				.asc(root.get(KclmtClosure_.kclmtClosurePK).get(KclmtClosurePK_.closureId)));

		List<KclmtClosure> resultList = em.createQuery(cq).getResultList();

		// exclude select
		return resultList.stream()
				.map(entity -> this.toDomain(entity,
						this.findHistoryByClosureId(companyId, entity.getKclmtClosurePK().getClosureId())))
				.collect(Collectors.toList());
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the closure
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private Closure toDomain(KclmtClosure entity, List<KclmtClosureHist> entityHistorys) {
		return new Closure(new JpaClosureGetMemento(entity, entityHistorys));
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the kclmt closure
	 */
	private KclmtClosure toEntity(Closure domain) {
		KclmtClosure entity = new KclmtClosure();
		domain.saveToMemento(new JpaClosureSetMemento(entity));
		return entity;
	}

	/**
	 * To entity update.
	 *
	 * @param domain
	 *            the domain
	 * @return the kclmt closure
	 */
	private KclmtClosure toEntityUpdate(Closure domain) {
		KclmtClosure entity = new KclmtClosure();
		Optional<KclmtClosure> optionalEntity = this.queryProxy()
				.find(new KclmtClosurePK(domain.getCompanyId().v(), domain.getClosureId().value), KclmtClosure.class);
		if (optionalEntity.isPresent()) {
			entity = optionalEntity.get();
		}
		domain.saveToMemento(new JpaClosureSetMemento(entity));
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#findAllActive
	 * (java.lang.String,
	 * nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Closure> findAllActive(String companyId, UseClassification useAtr) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KCLMT_CLOSURE (KclmtClosure SQL)
		CriteriaQuery<KclmtClosure> cq = criteriaBuilder.createQuery(KclmtClosure.class);

		// root data
		Root<KclmtClosure> root = cq.from(KclmtClosure.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KclmtClosure_.kclmtClosurePK).get(KclmtClosurePK_.cid), companyId));
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(KclmtClosure_.useClass), UseClassification.UseClass_Use.value));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by closure id asc
		cq.orderBy(criteriaBuilder.asc(root.get(KclmtClosure_.kclmtClosurePK).get(KclmtClosurePK_.closureId)));

		// create query
		TypedQuery<KclmtClosure> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream()
				.map(entity -> this.toDomain(entity,
						this.findHistoryByClosureId(companyId, entity.getKclmtClosurePK().getClosureId())))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistoryRepository#add(
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory)
	 */
	@Override
	public void addHistory(ClosureHistory closureHistory) {
		this.commandProxy().insert(this.toEntity(closureHistory));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistoryRepository#update
	 * (nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory)
	 */
	@Override
	public void updateHistory(ClosureHistory closureHistory) {
		this.commandProxy().update(this.toEntityUpdate(closureHistory));
	}

	/**
	 * Find history by closure id.
	 *
	 * @param companyId
	 *            the company id
	 * @param closureId
	 *            the closure id
	 * @return the list
	 */
	@SneakyThrows
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private List<KclmtClosureHist> findHistoryByClosureId(String companyId, int closureId) {
		
		try (val statement = this.connection().prepareStatement(
				"select * from KCLMT_CLOSURE_HIST where CID = ? and CLOSURE_ID = ?")) {
			statement.setString(1, companyId);
			statement.setInt(2, closureId);
			return new NtsResultSet(statement.executeQuery()).getList(rec -> {
				KclmtClosureHist entity = new KclmtClosureHist();
				entity.setKclmtClosureHistPK(new KclmtClosureHistPK(
						rec.getString("CID"),
						rec.getInt("CLOSURE_ID"),
						rec.getInt("STR_YM")));
				entity.setName(rec.getString("CLOSURE_NAME"));
				entity.setEndYM(rec.getInt("END_YM"));
				entity.setCloseDay(rec.getInt("CLOSURE_DAY"));
				entity.setIsLastDay(rec.getInt("IS_LAST_DAY"));
				return entity;
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistoryRepository#
	 * findByClosureId(java.lang.String, int)
	 */
	@Override
	public List<ClosureHistory> findByClosureId(String companyId, int closureId) {
		return this.findHistoryByClosureId(companyId, closureId).stream().map(item -> this.toDomain(item))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistoryRepository#
	 * findById(java.lang.String, int, int)
	 */
	@Override
	public Optional<ClosureHistory> findById(String companyId, int closureId, int startYM) {
		return this.queryProxy().find(new KclmtClosureHistPK(companyId, closureId, startYM), KclmtClosureHist.class)
				.map(c -> this.toDomain(c));
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the closure history
	 */
	private ClosureHistory toDomain(KclmtClosureHist entity) {
		return new ClosureHistory(new JpaClosureHistoryGetMemento(entity));
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the kclmt closure hist
	 */
	private KclmtClosureHist toEntity(ClosureHistory domain) {
		KclmtClosureHist entity = new KclmtClosureHist();
		domain.saveToMemento(new JpaClosureHistorySetMemento(entity));
		return entity;
	}

	/**
	 * To entity update.
	 *
	 * @param domain
	 *            the domain
	 * @return the kclmt closure hist
	 */
	private KclmtClosureHist toEntityUpdate(ClosureHistory domain) {
		Optional<KclmtClosureHist> optionalEntity = this.queryProxy()
				.find(new KclmtClosureHistPK(domain.getCompanyId().v(), domain.getClosureId().value,
						domain.getStartYearMonth().v()), KclmtClosureHist.class);
		KclmtClosureHist entity = new KclmtClosureHist();
		if (optionalEntity.isPresent()) {
			entity = optionalEntity.get();
		}
		domain.saveToMemento(new JpaClosureHistorySetMemento(entity));
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistoryRepository#
	 * findByCompanyId(java.lang.String)
	 */
	@Override
	public List<ClosureHistory> findByCompanyId(String companyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KCLMT_CLOSURE_HIST (KclmtClosureHist SQL)
		CriteriaQuery<KclmtClosureHist> cq = criteriaBuilder.createQuery(KclmtClosureHist.class);

		// root data
		Root<KclmtClosureHist> root = cq.from(KclmtClosureHist.class);

		// select root
		cq.select(root);

		// create query
		TypedQuery<KclmtClosureHist> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(item -> this.toDomain(item)).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistoryRepository#
	 * findBySelectedYearMonth(java.lang.String, int, int)
	 */
	@Override
	public Optional<ClosureHistory> findBySelectedYearMonth(String companyId, int closureId, int yearMonth) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KCLMT_CLOSURE_HIST (KclmtClosureHist SQL)
		CriteriaQuery<KclmtClosureHist> cq = criteriaBuilder.createQuery(KclmtClosureHist.class);

		// root data
		Root<KclmtClosureHist> root = cq.from(KclmtClosureHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.cid), companyId));

		// equal closure id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.closureId), closureId));

		// less than or equal year month
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.strYM), yearMonth));

		// great than or equal year month
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(root.get(KclmtClosureHist_.endYM), yearMonth));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// create query
		TypedQuery<KclmtClosureHist> query = em.createQuery(cq).setMaxResults(FIRST_LENGTH);

		// exclude select
		List<KclmtClosureHist> resData = query.getResultList();

		if (CollectionUtil.isEmpty(resData)) {
			return Optional.empty();
		}

		return Optional.ofNullable(this.toDomain(resData.get(FIRST_DATA)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistoryRepository#
	 * findByHistoryLast(java.lang.String, int)
	 */
	@Override
	public Optional<ClosureHistory> findByHistoryLast(String companyId, int closureId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KCLMT_CLOSURE_HIST (KclmtClosureHist SQL)
		CriteriaQuery<KclmtClosureHist> cq = criteriaBuilder.createQuery(KclmtClosureHist.class);

		// root data
		Root<KclmtClosureHist> root = cq.from(KclmtClosureHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.cid), companyId));

		// equal closure id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.closureId), closureId));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by end date desc
		cq.orderBy(criteriaBuilder.desc(root.get(KclmtClosureHist_.endYM)));

		// create query
		TypedQuery<KclmtClosureHist> query = em.createQuery(cq).setMaxResults(FIRST_LENGTH);

		// exclude select
		List<KclmtClosureHist> resData = query.getResultList();

		if (CollectionUtil.isEmpty(resData)) {
			return Optional.empty();
		}

		return Optional.ofNullable(this.toDomain(resData.get(FIRST_DATA)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistoryRepository#
	 * findByHistoryBegin(java.lang.String, int)
	 */
	@Override
	public Optional<ClosureHistory> findByHistoryBegin(String companyId, int closureId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KCLMT_CLOSURE_HIST (KclmtClosureHist SQL)
		CriteriaQuery<KclmtClosureHist> cq = criteriaBuilder.createQuery(KclmtClosureHist.class);

		// root data
		Root<KclmtClosureHist> root = cq.from(KclmtClosureHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.cid), companyId));

		// equal closure id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.closureId), closureId));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by end date asc
		cq.orderBy(criteriaBuilder.asc(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.strYM)));

		// create query
		TypedQuery<KclmtClosureHist> query = em.createQuery(cq).setMaxResults(FIRST_LENGTH);

		// exclude select
		List<KclmtClosureHist> resData = query.getResultList();

		if (CollectionUtil.isEmpty(resData)) {
			return Optional.empty();
		}

		return Optional.ofNullable(this.toDomain(resData.get(FIRST_DATA)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#
	 * findByClosureIdAndCurrentMonth(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Optional<ClosureHistory> findByClosureIdAndCurrentMonth(String companyId, Integer closureId, Integer closureMonth) {

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KclmtClosureHist> cq = criteriaBuilder.createQuery(KclmtClosureHist.class);
		Root<KclmtClosureHist> root = cq.from(KclmtClosureHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal closure id
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.closureId), closureId));
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.cid), companyId));
		// current month between startMonth and endMonth
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.strYM), closureMonth));
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(root.get(KclmtClosureHist_.endYM), closureMonth));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		cq.orderBy(criteriaBuilder.desc(root.get(KclmtClosureHist_.endYM)));

		// create query
		TypedQuery<KclmtClosureHist> query = em.createQuery(cq);

		// exclude select
		List<KclmtClosureHist> resData = query.getResultList();

		if (CollectionUtil.isEmpty(resData)) {
			return Optional.empty();
		}

		return Optional.ofNullable(this.toDomain(resData.get(FIRST_DATA)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#
	 * findByCurrentMonth(nts.arc.time.YearMonth)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ClosureHistory> findByCurrentMonth(String companyId, YearMonth closureYm) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KCLMT_CLOSURE_HIST (KclmtClosureHist SQL)
		CriteriaQuery<KclmtClosureHist> cq = criteriaBuilder.createQuery(KclmtClosureHist.class);

		// root data
		Root<KclmtClosureHist> root = cq.from(KclmtClosureHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// current month between startMonth and endMonth
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.strYM), closureYm.v()));
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(root.get(KclmtClosureHist_.endYM), closureYm.v()));
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.cid), companyId));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		cq.orderBy(criteriaBuilder.desc(root.get(KclmtClosureHist_.endYM)));

		// create query
		TypedQuery<KclmtClosureHist> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(item -> this.toDomain(item)).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#
	 * findHistoryByIdAndCurrentMonth(java.util.List, java.util.List)
	 */
	@Override
	public List<ClosureHistory> findHistoryByIdAndCurrentMonth(List<Integer> closureIds, List<Integer> closureMonths) {

		// check empty list
		if (CollectionUtil.isEmpty(closureMonths) || CollectionUtil.isEmpty(closureIds)) {
			return new ArrayList<>();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KCLMT_CLOSURE_HIST (KclmtClosureHist SQL)
		CriteriaQuery<KclmtClosureHist> cq = criteriaBuilder.createQuery(KclmtClosureHist.class);

		// root data
		Root<KclmtClosureHist> root = cq.from(KclmtClosureHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// closure id in
		lstpredicateWhere
				.add(root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.closureId).in(closureIds));

		Predicate predicateOrFull = null;

		for (Integer month : closureMonths) {
			Predicate predicateStart = criteriaBuilder.greaterThanOrEqualTo(
					root.get(KclmtClosureHist_.kclmtClosureHistPK).get(KclmtClosureHistPK_.strYM), month);
			Predicate predicateEnd = criteriaBuilder.lessThanOrEqualTo(root.get(KclmtClosureHist_.endYM), month);
			Predicate predicate = criteriaBuilder.and(predicateStart, predicateEnd);
			if (predicateOrFull == null) {
				predicateOrFull = predicate;
			} else {
				predicateOrFull = criteriaBuilder.or(predicateOrFull, predicate);
			}
		}

		lstpredicateWhere.add(predicateOrFull);

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		cq.orderBy(criteriaBuilder.desc(root.get(KclmtClosureHist_.endYM)));

		// create query
		TypedQuery<KclmtClosureHist> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(item -> this.toDomain(item)).collect(Collectors.toList());
	}

	public List<Closure> getClosureList(String companyId){
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KclmtClosure> cq = criteriaBuilder.createQuery(KclmtClosure.class);
		Root<KclmtClosure> root = cq.from(KclmtClosure.class);

		// select root
		cq.select(root);

		List<KclmtClosure> resultList = new ArrayList<>();
		
		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(KclmtClosure_.kclmtClosurePK).get(KclmtClosurePK_.cid),companyId));
		
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(KclmtClosure_.useClass),UseClassification.UseClass_Use.value));
		
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		resultList.addAll(em.createQuery(cq).getResultList());

		if (CollectionUtil.isEmpty(resultList)) {
			return null;
		}
		
		return resultList.stream().map(item -> this.toDomain(item, this.findHistoryByClosureId(companyId, 
				item.getKclmtClosurePK().getClosureId()))).collect(Collectors.toList());
	}

	private static final String SELECT_CLOSURE_HISTORY = "SELECT c FROM KclmtClosure c "
			+ "WHERE c.kclmtClosurePK.cid =:companyId "
			+ "AND c.kclmtClosurePK.closureId =:closureId "
			+ "AND c.useClass =:useClass ";
	@Override
	public Optional<Closure> findClosureHistory(String companyId, int closureId, int useClass) {
		Optional<KclmtClosure> kclmtClosure = this.queryProxy().query(SELECT_CLOSURE_HISTORY, KclmtClosure.class)
			.setParameter("companyId", companyId)
			.setParameter("closureId", closureId)
			.setParameter("useClass", useClass)
			.getSingle();
		if(kclmtClosure.isPresent()) {
			return Optional.of(this.toDomain(kclmtClosure.get(), this.findHistoryByClosureId(companyId, closureId)));
		}
		return Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository#
	 * findByCurrentYearMonthAndUsed(java.lang.String)
	 */
	@Override
	public List<ClosureHistory> findByCurrentYearMonthAndUsed(String companyId) {
		GeneralDate now = GeneralDate.today();
		YearMonth currentYearMonth = YearMonth.of(now.year(), now.month());
		return this.queryProxy().query(FIND_BY_CURRENT_YEARMONTH_AND_USED, KclmtClosureHist.class)
				.setParameter("comId", companyId)
				.setParameter("baseDate", currentYearMonth).getList()
				.stream().map(item -> this.toDomain(item)).collect(Collectors.toList());
	}
}
