/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.workplace.config.info;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistory;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceHierarchy;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfigInfo;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfigInfoPK;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfigInfoPK_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfigInfo_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfig_;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.
	 * WorkplaceConfigInfoRepository#findAllParentByWkpId(java.lang.String,
	 * nts.arc.time.GeneralDate, java.lang.String, boolean)
	 */
	@Override
	public Optional<WorkplaceConfigInfo> findAllParentByWkpId(String companyId,
			GeneralDate baseDate, String wkpId, boolean isSortAscHierarchyCd) {

		EntityManager em = this.getEntityManager();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWkpConfigInfo> cq = criteriaBuilder
				.createQuery(BsymtWkpConfigInfo.class);
		Root<BsymtWkpConfigInfo> root = cq.from(BsymtWkpConfigInfo.class);

		Order hierarchyCdOrder = isSortAscHierarchyCd
				? criteriaBuilder.asc(root.get(BsymtWkpConfigInfo_.hierarchyCd))
				: criteriaBuilder.desc(root.get(BsymtWkpConfigInfo_.hierarchyCd));

		List<BsymtWkpConfigInfo> lstEntity = this.getAllParentByWkpId(companyId, baseDate, wkpId,
				hierarchyCdOrder);

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
		
		List<String> parentCodes = getCanBeParentCodes(prHierarchyCode);
		
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

//		ParameterExpression<String> prHierarchyCodeParameter = criteriaBuilder
//				.parameter(String.class, "prHierarchyCodeParameter");
//		Path<String> senderEmailPath = root.get(BsymtWkpConfigInfo_.hierarchyCd);

		lstpredicateWhere.add(root.get(BsymtWkpConfigInfo_.hierarchyCd).in(parentCodes));

		// Base in EAP: Don't exclude
		// Ignore the wkp
		// lstpredicateWhere.add(criteriaBuilder.notEqual(root.get(BsymtWkpConfigInfo_.hierarchyCd),
		// prHierarchyCode));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		cq.orderBy(criteriaBuilder.asc(root.get(BsymtWkpConfigInfo_.hierarchyCd)));
		
		List<BsymtWkpConfigInfo> lstEntity = em.createQuery(cq).getResultList();
//		List<BsymtWkpConfigInfo> lstEntity = em.createQuery(cq)
//				.setParameter("prHierarchyCodeParameter", prHierarchyCode).getResultList();

		
		// check empty
		if (lstEntity.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional
				.of(new WorkplaceConfigInfo(new JpaWorkplaceConfigInfoGetMemento(lstEntity)));
	}
	
	@Override
	@SneakyThrows
	public Map<WorkplaceConfigHistory, List<WorkplaceConfigInfo>> findAllParentByWkpId(String companyId, DatePeriod baseDate, List<String> wkpId) {
		if(wkpId.isEmpty()) {
			return new HashMap<>();
		}
		List<WorkplaceConfigHistory> configHis = new ArrayList<>();
		try(val st = this.connection().prepareStatement("SELECT HIST_ID, START_DATE, END_DATE FROM BSYMT_WKP_CONFIG WHERE START_DATE <= ? AND END_DATE >= ? AND CID = ?")){
			st.setDate(1, Date.valueOf(baseDate.end().localDate()));
			st.setDate(2, Date.valueOf(baseDate.start().localDate()));
			st.setString(3, companyId);
			configHis.addAll(new NtsResultSet(st.executeQuery()).getList(c -> {
				return new WorkplaceConfigHistory(c.getString("HIST_ID"), new DatePeriod(c.getGeneralDate("START_DATE"), c.getGeneralDate("END_DATE")));
			}));
		}
		
		List<String> hisId = configHis.stream().map(c -> c.identifier()).collect(Collectors.toList());
		
		List<Map<String, String>> optWkpConfigInfo = new ArrayList<>();
		String hisParams = hisId.stream().map(c -> "?").collect(Collectors.joining(","));
		StringBuilder query = new StringBuilder("SELECT WKPID, HIERARCHY_CD, HIST_ID FROM BSYMT_WKP_CONFIG_INFO ");
		query.append(" WHERE CID = ? AND HIST_ID IN (").append(hisParams).append( ")");
		
		try(val st = this.connection().prepareStatement(query.toString())){
			st.setString(1, companyId);
			for(int i = 0; i < hisId.size(); i++){
				st.setString(2 + i, hisId.get(i));
			}
			optWkpConfigInfo.addAll(new NtsResultSet(st.executeQuery()).getList(c -> {
				Map<String, String> r = new HashMap<>();
				r.put("WKPID", c.getString("WKPID"));
				r.put("HIERARCHY_CD", c.getString("HIERARCHY_CD"));
				r.put("HIST_ID", c.getString("HIST_ID"));
				return r;
			}));
		}
		
		if(optWkpConfigInfo.isEmpty()) {
			return new HashMap<>();
		}
		Map<String, List<Map<String, String>>> mappedHis = optWkpConfigInfo.stream().collect(Collectors.groupingBy(c -> c.get("HIST_ID"), Collectors.toList()));
		
		return configHis.stream().collect(Collectors.toMap(c -> c, c -> {
			List<Map<String, String>> currentHisWp = mappedHis.get(c.identifier());
			if(currentHisWp != null){
				return wkpId.stream().map(wId -> {
					Map<String, String> currentWpc = currentHisWp.stream().filter(wpc -> wpc.get("WKPID").equals(wId)).findFirst().orElse(null);
					if(currentWpc != null) {
						String hierarchyCD = currentWpc.get("HIERARCHY_CD");
						List<String> parentHierarchy = getCanBeParentCodes(hierarchyCD);
						List<Map<String, String>> parent = currentHisWp.stream().filter(wpc -> parentHierarchy.contains(wpc.get("HIERARCHY_CD"))).collect(Collectors.toList());
						List<WorkplaceHierarchy> lstWkpHierarchy = parent.stream().map(wpc -> WorkplaceHierarchy.newInstance(wpc.get("WKPID"), wpc.get("HIERARCHY_CD"))).collect(Collectors.toList());
						lstWkpHierarchy.add(WorkplaceHierarchy.newInstance(wId, hierarchyCD));
						lstWkpHierarchy.sort((c1, c2) -> c2.getHierarchyCode().compareTo(c1.getHierarchyCode()));
						return new WorkplaceConfigInfo(companyId, c.identifier(), lstWkpHierarchy);
					}
					return null;
				}).filter(w -> w != null).collect(Collectors.toList());
			}
			
			return new ArrayList<>();
		}));
	}

	private List<String> getCanBeParentCodes(String prHierarchyCode) {
		String[] codeAvailables = prHierarchyCode.split("(?<=\\G.{3})");
		
		List<String> parentCodes = new ArrayList<>();
		for(String c : codeAvailables){
			if(parentCodes.isEmpty()){
				parentCodes.add(c);
			} else {
				parentCodes.add(StringUtils.join(parentCodes.get(parentCodes.size() - 1), c));
			}
		}
		
		return parentCodes;
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
		
		List<BsymtWkpConfigInfo> resultList = new ArrayList<>();
		
		CollectionUtil.split(wkpIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
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
					root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.wkpid).in(subList));

			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
			
			resultList.addAll(em.createQuery(cq).getResultList());
		});

		// check empty
		if (resultList.isEmpty()) {
			return Collections.emptyList();
		}

		return resultList.stream()
				.map(entity -> new WorkplaceConfigInfo(
						new JpaWorkplaceConfigInfoGetMemento(Arrays.asList(entity))))
				.collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository
	 * #findByHistoryIdsAndWplIds(java.lang.String, java.util.List, java.util.List)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<WorkplaceConfigInfo> findByHistoryIdsAndWplIds(String companyId, List<String> historyIds,
			List<String> workplaceIds) {
		if (CollectionUtil.isEmpty(historyIds) || CollectionUtil.isEmpty(workplaceIds)) {
			return Collections.emptyList();
		}
		
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWkpConfigInfo> cq = criteriaBuilder.createQuery(BsymtWkpConfigInfo.class);
		Root<BsymtWkpConfigInfo> root = cq.from(BsymtWkpConfigInfo.class);

		// select root
		cq.select(root);
		
		List<BsymtWkpConfigInfo> resultList = new ArrayList<>();
		
		CollectionUtil.split(historyIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subHistorieIds -> {
			CollectionUtil.split(workplaceIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subWorkplaceIds -> {
				// add where
				List<Predicate> lstpredicateWhere = new ArrayList<>();
				lstpredicateWhere.add(criteriaBuilder
						.equal(root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.cid), companyId));
				
				lstpredicateWhere.add(root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK)
						.get(BsymtWkpConfigInfoPK_.historyId).in(subHistorieIds));
				lstpredicateWhere.add(root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.wkpid)
						.in(subWorkplaceIds));

				cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
				
				resultList.addAll(em.createQuery(cq).getResultList());
			});
			
		});
		
		// check empty
		if (resultList.isEmpty()) {
			return Collections.emptyList();
		}

		return resultList.stream()
				.map(entity -> new WorkplaceConfigInfo(new JpaWorkplaceConfigInfoGetMemento(Arrays.asList(entity))))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.
	 * WorkplaceConfigInfoRepository#findByHistoryIds(java.lang.String,
	 * java.util.List)
	 */
	@Override
	public List<WorkplaceConfigInfo> findByHistoryIds(String companyId, List<String> historyIds) {
		if (CollectionUtil.isEmpty(historyIds)) {
			return Collections.emptyList();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<BsymtWkpConfigInfo> cq = criteriaBuilder.createQuery(BsymtWkpConfigInfo.class);
		Root<BsymtWkpConfigInfo> root = cq.from(BsymtWkpConfigInfo.class);

		// select root
		cq.select(root);

		List<BsymtWkpConfigInfo> resultList = new ArrayList<>();

		CollectionUtil.split(historyIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subHistorieIds -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();
			lstpredicateWhere.add(criteriaBuilder.equal(
					root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK).get(BsymtWkpConfigInfoPK_.cid), companyId));

			lstpredicateWhere.add(root.get(BsymtWkpConfigInfo_.bsymtWkpConfigInfoPK)
					.get(BsymtWkpConfigInfoPK_.historyId).in(subHistorieIds));

			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			resultList.addAll(em.createQuery(cq).getResultList());
		});

		// check empty
		if (resultList.isEmpty()) {
			return Collections.emptyList();
		}

		return resultList.stream()
				.map(entity -> new WorkplaceConfigInfo(new JpaWorkplaceConfigInfoGetMemento(Arrays.asList(entity))))
				.collect(Collectors.toList());
	}

	/**
	 * Gets the all parent by wkp id.
	 *
	 * @param companyId
	 *            the company id
	 * @param baseDate
	 *            the base date
	 * @param wkpId
	 *            the wkp id
	 * @param hierarchyCdOrder
	 *            the hierarchy cd order
	 * @return the all parent by wkp id
	 */
	private List<BsymtWkpConfigInfo> getAllParentByWkpId(String companyId, GeneralDate baseDate,
			String wkpId, Order hierarchyCdOrder) {
		Optional<WorkplaceConfigInfo> optWkpConfigInfo = this.find(companyId, baseDate, wkpId);

		if (!optWkpConfigInfo.isPresent()) {
			return Collections.emptyList();
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

		cq.orderBy(hierarchyCdOrder);

		return em.createQuery(cq).setParameter("prHierarchyCodeParameter", prHierarchyCode)
				.getResultList();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository#updateWorkplaceConfigInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateWorkplaceConfigInfo(String companyId, String historyId, String wkpId, String hierarchyCd) {
		BsymtWkpConfigInfoPK pk = new BsymtWkpConfigInfoPK(companyId, historyId, wkpId);
		BsymtWkpConfigInfo entity = new BsymtWkpConfigInfo();
		entity.setBsymtWkpConfigInfoPK(pk);
		entity.setHierarchyCd(hierarchyCd);
		this.commandProxy().update(entity);
	}
}
