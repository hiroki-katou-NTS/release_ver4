/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.workplace.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceHist;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceHist_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfo;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfoPK;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfoPK_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfo_;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaWorkplaceInfoRepository.
 */
@Stateless
public class JpaWorkplaceInfoRepository extends JpaRepository implements WorkplaceInfoRepository {

	/** The Constant FIND_WKP_DETAIL_HIERARCHY_ORDER. */
	private static final String FIND_WKP_DETAIL_HIERARCHY_ORDER = "SELECT C FROM BsymtWorkplaceInfo AS C "
			+ "LEFT JOIN BsymtWkpConfig AS B ON A.bsymtWkpConfigInfoPK.historyId = B.bsymtWkpConfigPK.historyId "
			+ "LEFT JOIN BsymtWkpConfigInfo AS A ON A.bsymtWkpConfigInfoPK.wkpid = C.bsymtWorkplaceInfoPK.wkpid "
			+ "LEFT JOIN BsymtWorkplaceHist AS D ON C.bsymtWorkplaceInfoPK.historyId = D.bsymtWorkplaceHistPK.historyId "
			+ "WHERE A.bsymtWkpConfigInfoPK.cid = :cid " + "AND B.strD <= :baseDate AND B.endD >= :baseDate "
			+ "AND D.strD <= :baseDate AND D.endD >= :baseDate " + "ORDER BY A.hierarchyCd ASC";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#add(nts
	 * .uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo)
	 */
	@Override
	public void add(WorkplaceInfo workplaceInfo) {
		this.commandProxy().insert(this.toEntity(workplaceInfo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#update(
	 * nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo)
	 */
	@Override
	public void update(WorkplaceInfo workplaceInfo) {
		this.commandProxy().update(this.toEntity(workplaceInfo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#remove(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void remove(String companyId, String workplaceId, String historyId) {
		this.commandProxy().remove(BsymtWorkplaceInfo.class,
				new BsymtWorkplaceInfoPK(companyId, workplaceId, historyId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#find(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<WorkplaceInfo> find(String companyId, String wkpId, String historyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.wkpid), wkpId));
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.historyId), historyId));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		return em.createQuery(cq).getResultList().stream()
				.map(entity -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(entity))).findFirst();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
	 * findByWkpCd(java.lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<WorkplaceInfo> findByWkpCd(String companyId, String wpkCode, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(BsymtWorkplaceInfo_.wkpcd), wpkCode));
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.strD), baseDate));
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.endD), baseDate));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		return em.createQuery(cq).getResultList().stream()
				.map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item))).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
	 * findByWkpId(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<WorkplaceInfo> findByWkpId(String wpkId, GeneralDate baseDate) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.wkpid), wpkId));
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.strD), baseDate));
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.endD), baseDate));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		return em.createQuery(cq).getResultList().stream()
				.map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item))).findFirst();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
	 * isExisted(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isExistedWkpCd(String companyId, String newWkpCd) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(BsymtWorkplaceInfo_.wkpcd), newWkpCd));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		return !em.createQuery(cq).getResultList().isEmpty();
	}

	/**
	 * To entity.
	 *
	 * @param workplaceInfo the workplace info
	 * @return the bsymt workplace info
	 */
	private BsymtWorkplaceInfo toEntity(WorkplaceInfo workplaceInfo) {
		Optional<BsymtWorkplaceInfo> optional = this.queryProxy()
				.find(new BsymtWorkplaceInfoPK(workplaceInfo.getCompanyId(), workplaceInfo.getWorkplaceId(),
						workplaceInfo.getHistoryId()), BsymtWorkplaceInfo.class);
		BsymtWorkplaceInfo entity = new BsymtWorkplaceInfo();
		if (optional.isPresent()) {
			entity = optional.get();
		}
		JpaWorkplaceInfoSetMemento memento = new JpaWorkplaceInfoSetMemento(entity);
		workplaceInfo.saveToMemento(memento);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#findAll
	 * (java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<WorkplaceInfo> findAll(String companyId, GeneralDate baseDate) {

		List<BsymtWorkplaceInfo> resultList = this.queryProxy()
				.query(FIND_WKP_DETAIL_HIERARCHY_ORDER, BsymtWorkplaceInfo.class).setParameter("cid", companyId)
				.setParameter("baseDate", baseDate).getList();

		return resultList.stream().map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item)))
				.collect(Collectors.toList());
	}
	
//	@Override
//	public List<WorkplaceInfo> findAll(String companyId, GeneralDate baseDate) {
//
//		List<BsymtWorkplaceInfo> resultList = this.queryProxy()
//				.query(FIND_WKP_DETAIL_HIERARCHY_ORDER, BsymtWorkplaceInfo.class).setParameter("cid", companyId)
//				.setParameter("baseDate", baseDate).getList();
//
//		return resultList.stream().map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item)))
//				.collect(Collectors.toList());
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
	 * getByWkpIds(java.util.List, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<WorkplaceInfo> getByWkpIds(List<String> wkpIds, GeneralDate baseDate) {
		if (CollectionUtil.isEmpty(wkpIds)) {
			return new ArrayList<>();
		}
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		// Split query where in.
		List<BsymtWorkplaceInfo> resultList = new ArrayList<>();
		CollectionUtil.split(wkpIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();
			lstpredicateWhere.add(
					root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.wkpid).in(subList));
			lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
					root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.strD), baseDate));
			lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
					root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.endD), baseDate));

			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			resultList.addAll(em.createQuery(cq).getResultList());
		});

		// Convert.
		return resultList.stream().map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
	 * findByWkpIds(java.lang.String, java.util.List)
	 */
	@Override
	public List<WorkplaceInfo> findByWkpIds(String companyId, List<String> wkpIds) {

		// check empty
		if (CollectionUtil.isEmpty(wkpIds)) {
			return new ArrayList<>();
		}
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		List<BsymtWorkplaceInfo> resultList = new ArrayList<>();

		CollectionUtil.split(wkpIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();
			lstpredicateWhere.add(
					root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.wkpid).in(subList));

			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			resultList.addAll(em.createQuery(cq).getResultList());
		});

		// check empty
		if (CollectionUtil.isEmpty(resultList)) {
			return new ArrayList<>();
		}

		return resultList.stream().map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item)))
				.collect(Collectors.toList());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
	 * findByBaseDateWkpIds(java.lang.String, nts.arc.time.GeneralDate,
	 * java.util.List)
	 */
	@Override
	public List<WorkplaceInfo> findByBaseDateWkpIds(String companyId, GeneralDate baseDate,
			List<String> wkpIds) {

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder
				.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		List<BsymtWorkplaceInfo> resultList = new ArrayList<>();

		CollectionUtil.split(wkpIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();
			lstpredicateWhere.add(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK)
					.get(BsymtWorkplaceInfoPK_.wkpid).in(subList));
			lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
					root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.strD),
					baseDate));
			lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
					root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.endD),
					baseDate));
			lstpredicateWhere.add(criteriaBuilder.equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.cid), companyId));
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			resultList.addAll(em.createQuery(cq).getResultList());
		});

		// check empty
		if (CollectionUtil.isEmpty(resultList)) {
			return Collections.emptyList();
		}

		return resultList.stream()
				.map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item)))
				.collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#findByHistory(java.util.List)
	 */
	@Override
	public List<WorkplaceInfo> findByHistory(List<String> historyList, String companyId) {
		if (CollectionUtil.isEmpty(historyList)) {
			return Collections.emptyList();
		}
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		List<BsymtWorkplaceInfo> resultList = new ArrayList<>();
		
		CollectionUtil.split(historyList, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();
			
			lstpredicateWhere
					.add(criteriaBuilder.equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK)
							.get(BsymtWorkplaceInfoPK_.cid), companyId));
			
			lstpredicateWhere.add(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.historyId)
					.in(subList));
			
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			resultList.addAll(em.createQuery(cq).getResultList());
		});

		if (CollectionUtil.isEmpty(resultList)) {
			return Collections.emptyList();
		}

		return resultList.stream().map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
	 * findByWkpIdsAndHistIds(java.lang.String, java.util.List, java.util.List)
	 */
	@Override
	public List<WorkplaceInfo> findByWkpIdsAndHistIds(String companyId, List<String> wkpIds,
			List<String> histIds) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder
				.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		List<BsymtWorkplaceInfo> resultList = new ArrayList<>();

		CollectionUtil.split(wkpIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subWkpList) -> {
			CollectionUtil.split(histIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subHistList) -> {
				// add where
				List<Predicate> lstpredicateWhere = new ArrayList<>();
				lstpredicateWhere.add(
						criteriaBuilder.equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK)
								.get(BsymtWorkplaceInfoPK_.cid), companyId));
				lstpredicateWhere.add(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK)
						.get(BsymtWorkplaceInfoPK_.wkpid).in(subWkpList));
				lstpredicateWhere.add(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK)
						.get(BsymtWorkplaceInfoPK_.historyId).in(subHistList));

				cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

				resultList.addAll(em.createQuery(cq).getResultList());
			});
		});

		return resultList.stream()
				.map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item)))
				.collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#findByHistory(java.util.List)
	 */
	@Override
	public List<WorkplaceInfo> findByHistory(List<String> historyList) {
		if (CollectionUtil.isEmpty(historyList)) {
			return Collections.emptyList();
		}
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		List<BsymtWorkplaceInfo> resultList = new ArrayList<>();
		
		CollectionUtil.split(historyList, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();
			
			lstpredicateWhere.add(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.historyId)
					.in(subList));
			
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			resultList.addAll(em.createQuery(cq).getResultList());
		});

		if (CollectionUtil.isEmpty(resultList)) {
			return Collections.emptyList();
		}

		return resultList.stream().map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
	 * findByWkpCds(java.lang.String, java.util.List, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<GeneralDate, List<WorkplaceInfo>> findByWkpIds(String companyId,
			List<String> wpkIds, List<GeneralDate> baseDates) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<?> cq = criteriaBuilder.createQuery();
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);
		Join<BsymtWorkplaceInfo, BsymtWorkplaceHist> joinHistRoot = root
				.join(BsymtWorkplaceInfo_.bsymtWorkplaceHist, JoinType.LEFT);

		// Build query
		cq.multiselect(root, joinHistRoot);

		List<Object[]> result = new ArrayList<>();
				
		CollectionUtil.split(wpkIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, (subList) -> {
			// add where
			List<Predicate> listPredicate = new ArrayList<>();
			listPredicate
					.add(criteriaBuilder.equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK)
							.get(BsymtWorkplaceInfoPK_.cid), companyId));
			listPredicate.add(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK)
					.get(BsymtWorkplaceInfoPK_.wkpid).in(subList));

			List<Predicate> listPredicateBaseDate = new ArrayList<>();
			baseDates.forEach(baseDate -> {
				listPredicateBaseDate.add(criteriaBuilder.and(
						criteriaBuilder
								.lessThanOrEqualTo(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist)
										.get(BsymtWorkplaceHist_.strD), baseDate),
						criteriaBuilder.greaterThanOrEqualTo(
								root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist)
										.get(BsymtWorkplaceHist_.endD),
								baseDate)));
			});

			listPredicate
					.add(criteriaBuilder.or(listPredicateBaseDate.toArray(new Predicate[] {})));

			cq.where(listPredicate.toArray(new Predicate[] {}));

			result.addAll((List<Object[]>) em.createQuery(cq).getResultList());
		});

		Map<GeneralDate, List<WorkplaceInfo>> mapItem = new HashMap<>();

		baseDates.forEach(baseDate -> {
			mapItem.putAll(result.stream().collect(Collectors.groupingBy(item -> {
				BsymtWorkplaceHist workplaceHist = (BsymtWorkplaceHist) item[1];
				return (new DatePeriod(workplaceHist.getStrD(), workplaceHist.getEndD()))
						.contains(baseDate) ? baseDate : null;
			}, Collectors.mapping(
					x -> new WorkplaceInfo(
							new JpaWorkplaceInfoGetMemento((BsymtWorkplaceInfo) x[0])),
					Collectors.toList()))));
		});

		// Return
		return mapItem;
	}
}
