/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.workplace.info;

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
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceHist_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfo;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfoPK;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfoPK_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfo_;

/**
 * The Class JpaWorkplaceInfoRepository.
 */
@Stateless
public class JpaWorkplaceInfoRepository extends JpaRepository implements WorkplaceInfoRepository {

	/** The Constant MAX_ELEMENTS. */
	private static final Integer MAX_ELEMENTS = 1000;

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
		CollectionUtil.split(wkpIds, MAX_ELEMENTS, (subList) -> {
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

		CollectionUtil.split(wkpIds, MAX_ELEMENTS, (subList) -> {
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

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#findByHistory(java.util.List)
	 */
	@Override
	public List<WorkplaceInfo> findByHistory(List<String> historyList, String companyId) {
		if (CollectionUtil.isEmpty(historyList)) {
			return null;
		}
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
		Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

		// select root
		cq.select(root);

		List<BsymtWorkplaceInfo> resultList = new ArrayList<>();
		
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		
		lstpredicateWhere.add(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.cid)
				.in(companyId));
		
		CollectionUtil.split(historyList, MAX_ELEMENTS, (subList) -> {
			// add where
			
			lstpredicateWhere.add(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.historyId)
					.in(subList));
			
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			resultList.addAll(em.createQuery(cq).getResultList());
		});

		if (CollectionUtil.isEmpty(resultList)) {
			return null;
		}

		return resultList.stream().map(item -> new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item)))
				.collect(Collectors.toList());
	}
}
