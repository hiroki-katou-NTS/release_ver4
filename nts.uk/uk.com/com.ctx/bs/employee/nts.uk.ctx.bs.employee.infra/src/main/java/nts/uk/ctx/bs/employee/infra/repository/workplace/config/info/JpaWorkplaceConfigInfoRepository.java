/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.workplace.config.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceHierarchy;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfigInfo;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfigInfoPK;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfigInfoPK_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfigInfo_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfig_;

/**
 * The Class JpaWorkplaceConfigInfoRepository.
 */
@Stateless
public class JpaWorkplaceConfigInfoRepository extends JpaRepository
		implements WorkplaceConfigInfoRepository {

	/** The Constant FIRST_ITEM_INDEX. */
	private static final int FIRST_ITEM_INDEX = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.configinfo.
	 * WorkplaceConfigInfoRepository#addList(nts.uk.ctx.bs.employee.dom.
	 * workplace.configinfo.WorkplaceConfigInfo)
	 */
	@Override
	public void add(WorkplaceConfigInfo wkpConfigInfo) {
		this.commandProxy().insertAll(this.toListEntity(wkpConfigInfo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.
	 * WorkplaceConfigInfoRepository#update(nts.uk.ctx.bs.employee.dom.workplace
	 * .config.info.WorkplaceConfigInfo)
	 */
	@Override
	public void update(WorkplaceConfigInfo wkpConfigInfo) {
		this.commandProxy().updateAll(this.toListEntity(wkpConfigInfo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.
	 * WorkplaceConfigInfoRepository#removeWkpHierarchy(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void removeWkpHierarchy(String companyId, String historyId, String wkpId) {
		this.commandProxy().remove(BsymtWkpConfigInfo.class,
				new BsymtWkpConfigInfoPK(companyId, historyId, wkpId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.configinfo.
	 * WorkplaceConfigInfoRepository#find(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<WorkplaceConfigInfo> find(String companyId, String historyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWkpConfigInfo> cq = criteriaBuilder
				.createQuery(BsymtWkpConfigInfo.class);
		Root<BsymtWkpConfigInfo> root = cq.from(BsymtWkpConfigInfo.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.cid),
				companyId));
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK)
						.get(BsymtWkpConfigInfoPK_.historyId), historyId));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
		cq.orderBy(criteriaBuilder.asc(root.get(BsymtWkpConfigInfo_.hierarchyCd)));

		List<BsymtWkpConfigInfo> lstEntity = em.createQuery(cq).getResultList();

		if (lstEntity.isEmpty()) {
			return Optional.empty();
		}

		return Optional
				.of(new WorkplaceConfigInfo(new JpaWorkplaceConfigInfoGetMemento(lstEntity)));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.
	 * WorkplaceConfigInfoRepository#find(java.lang.String,
	 * nts.arc.time.GeneralDate)
	 */
//	@Override
//	public Optional<WorkplaceConfigInfo> find(String companyId, GeneralDate baseDate) {
//		// get entity manager
//		EntityManager em = this.getEntityManager();
//		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//
//		CriteriaQuery<BsymtWkpConfigInfo> cq = criteriaBuilder
//				.createQuery(BsymtWkpConfigInfo.class);
//		Root<BsymtWkpConfigInfo> root = cq.from(BsymtWkpConfigInfo.class);
//
//		// select root
//		cq.select(root);
//
//		// add where
//		List<Predicate> lstpredicateWhere = new ArrayList<>();
//		lstpredicateWhere.add(criteriaBuilder.equal(
//				root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.cid),
//				companyId));
//		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
//				root.get(BsymtWkpConfigInfo_.bsymtWkpConfig).get(BsymtWkpConfig_.strD), baseDate));
//		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
//				root.get(BsymtWkpConfigInfo_.bsymtWkpConfig).get(BsymtWkpConfig_.endD), baseDate));
//		
//		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
//		cq.orderBy(criteriaBuilder.asc(root.get(BsymtWkpConfigInfo_.hierarchyCd)));
//
//		List<BsymtWkpConfigInfo> lstEntity = em.createQuery(cq).getResultList();
//
//		if (lstEntity.isEmpty()) {
//			return Optional.empty();
//		}
//
//		return Optional
//				.of(new WorkplaceConfigInfo(new JpaWorkplaceConfigInfoGetMemento(lstEntity)));
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.
	 * WorkplaceConfigInfoRepository#findByWkpId(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Optional<WorkplaceConfigInfo> find(String companyId, String historyId, String wkpId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWkpConfigInfo> cq = criteriaBuilder
				.createQuery(BsymtWkpConfigInfo.class);
		Root<BsymtWkpConfigInfo> root = cq.from(BsymtWkpConfigInfo.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.cid),
				companyId));
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK)
						.get(BsymtWkpConfigInfoPK_.historyId), historyId));
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.wkpid),
				wkpId));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		List<BsymtWkpConfigInfo> lstEntity = em.createQuery(cq).getResultList();

		// check empty
		if (lstEntity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional
				.of(new WorkplaceConfigInfo(new JpaWorkplaceConfigInfoGetMemento(lstEntity)));
	}

	@Override
	public Optional<WorkplaceConfigInfo> find(String companyId, GeneralDate baseDate,
			String wkpId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWkpConfigInfo> cq = criteriaBuilder
				.createQuery(BsymtWkpConfigInfo.class);
		Root<BsymtWkpConfigInfo> root = cq.from(BsymtWkpConfigInfo.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.cid),
				companyId));
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfig).get(BsymtWkpConfig_.strD), baseDate));
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfig).get(BsymtWkpConfig_.endD), baseDate));
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.wkpid),
				wkpId));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		List<BsymtWkpConfigInfo> lstEntity = em.createQuery(cq).getResultList();

		// check empty
		if (lstEntity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional
				.of(new WorkplaceConfigInfo(new JpaWorkplaceConfigInfoGetMemento(lstEntity)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.
	 * WorkplaceConfigInfoRepository#findAllByParentWkpId(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<WorkplaceConfigInfo> findAllByParentWkpId(String companyId,
			GeneralDate baseDate, String parentWkpId) {
		Optional<WorkplaceConfigInfo> optParentWkpConfigInfo = this.find(companyId, baseDate, parentWkpId);
		
		// check empty
		if (!optParentWkpConfigInfo.isPresent()) {
			return Optional.empty();
		}

		WorkplaceConfigInfo parentWkpConfigInfo = optParentWkpConfigInfo.get();

		String prHierarchyCode = parentWkpConfigInfo.getLstWkpHierarchy().get(FIRST_ITEM_INDEX)
				.getHierarchyCode().v();

		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWkpConfigInfo> cq = criteriaBuilder
				.createQuery(BsymtWkpConfigInfo.class);
		Root<BsymtWkpConfigInfo> root = cq.from(BsymtWkpConfigInfo.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.cid),
				companyId));
		lstpredicateWhere
				.add(criteriaBuilder.equal(
						root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK)
								.get(BsymtWkpConfigInfoPK_.historyId),
						parentWkpConfigInfo.getHistoryId()));
		lstpredicateWhere.add(criteriaBuilder.like(root.get(BsymtWkpConfigInfo_.hierarchyCd),
				prHierarchyCode + "%"));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		List<BsymtWkpConfigInfo> lstEntity = em.createQuery(cq).getResultList();

		// check empty
		if (lstEntity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional
				.of(new WorkplaceConfigInfo(new JpaWorkplaceConfigInfoGetMemento(lstEntity)));
	}

	/**
	 * To list entity.
	 *
	 * @param wkpConfigInfo
	 *            the wkp config info
	 * @return the list
	 */
	private List<BsymtWkpConfigInfo> toListEntity(WorkplaceConfigInfo wkpConfigInfo) {
		String companyId = wkpConfigInfo.getCompanyId();
		String historyId = wkpConfigInfo.getHistoryId();

		List<BsymtWkpConfigInfo> lstEntity = new ArrayList<>();

		for (WorkplaceHierarchy wkpHierarchy : wkpConfigInfo.getLstWkpHierarchy()) {
			BsymtWkpConfigInfoPK pk = new BsymtWkpConfigInfoPK(companyId, historyId,
					wkpHierarchy.getWorkplaceId());
			BsymtWkpConfigInfo entity = this.queryProxy().find(pk, BsymtWkpConfigInfo.class)
					.orElse(new BsymtWkpConfigInfo(pk));

			lstEntity.add(entity);
		}
		JpaWorkplaceConfigInfoSetMemento memento = new JpaWorkplaceConfigInfoSetMemento(lstEntity);
		wkpConfigInfo.saveToMemento(memento);

		return lstEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.
	 * WorkplaceConfigInfoRepository#findAllParentByWkpId(java.lang.String,
	 * nts.arc.time.GeneralDate, java.lang.String)
	 */
	@Override
	public Optional<WorkplaceConfigInfo> findAllParentByWkpId(String companyId,
			GeneralDate baseDate, String wkpId) {
		Optional<WorkplaceConfigInfo> optWkpConfigInfo = this.find(companyId, baseDate, wkpId);
		
		if(!optWkpConfigInfo.isPresent()) {
			return Optional.empty();
		}
		
		WorkplaceConfigInfo wkpConfigInfo = optWkpConfigInfo.get();

		String prHierarchyCode = wkpConfigInfo.getLstWkpHierarchy().get(FIRST_ITEM_INDEX)
				.getHierarchyCode().v();

		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWkpConfigInfo> cq = criteriaBuilder
				.createQuery(BsymtWkpConfigInfo.class);
		Root<BsymtWkpConfigInfo> root = cq.from(BsymtWkpConfigInfo.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.cid),
				companyId));
		lstpredicateWhere
				.add(criteriaBuilder.equal(root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK)
						.get(BsymtWkpConfigInfoPK_.historyId), wkpConfigInfo.getHistoryId()));

		ParameterExpression<String> prHierarchyCodeParameter = criteriaBuilder
				.parameter(String.class, "prHierarchyCodeParameter");
		Path<String> senderEmailPath = root.get(BsymtWkpConfigInfo_.hierarchyCd);

		lstpredicateWhere.add(criteriaBuilder.like(prHierarchyCodeParameter,
				criteriaBuilder.concat(senderEmailPath, "%")));

		// Base in EAP: Don't exclude
		// Ignore the wkp
		// lstpredicateWhere.add(criteriaBuilder.notEqual(root.get(BsymtWkpConfigInfo_.hierarchyCd),
		// prHierarchyCode));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		cq.orderBy(criteriaBuilder.asc(root.get(BsymtWkpConfigInfo_.hierarchyCd)));

		List<BsymtWkpConfigInfo> lstEntity = em.createQuery(cq)
				.setParameter("prHierarchyCodeParameter", prHierarchyCode).getResultList();

		
		// check empty
		if (lstEntity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional
				.of(new WorkplaceConfigInfo(new JpaWorkplaceConfigInfoGetMemento(lstEntity)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.
	 * WorkplaceConfigInfoRepository#findByWkpIdsAtTime(java.lang.String,
	 * nts.arc.time.GeneralDate, java.util.List)
	 */
	@Override
	public List<WorkplaceConfigInfo> findByWkpIdsAtTime(String companyId, GeneralDate baseDate,
			List<String> wkpIds) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWkpConfigInfo> cq = criteriaBuilder
				.createQuery(BsymtWkpConfigInfo.class);
		Root<BsymtWkpConfigInfo> root = cq.from(BsymtWkpConfigInfo.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.cid),
				companyId));
		lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfig).get(BsymtWkpConfig_.strD), baseDate));
		lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfig).get(BsymtWkpConfig_.endD), baseDate));
		lstpredicateWhere.add(
				root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.wkpid).in(wkpIds));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		List<BsymtWkpConfigInfo> lstEntity = em.createQuery(cq).getResultList();

		// check empty
		if (lstEntity.isEmpty()) {
			return Collections.emptyList();
		}

		return lstEntity.stream()
				.map(entity -> new WorkplaceConfigInfo(
						new JpaWorkplaceConfigInfoGetMemento(Arrays.asList(entity))))
				.collect(Collectors.toList());
	}

}
