/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.infra.repository.singlesignon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountGetMemento;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountInfo;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountRepository;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtWindowAcc;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtWindowAccPK;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtWindowAccPK_;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtWindowAcc_;

/**
 * The Class JpaWindowAccountRepository.
 */
@Stateless
@Transactional
public class JpaWindowAccountRepository extends JpaRepository implements WindowsAccountRepository {

	/** The get by list userids. */
	private static final String GET_BY_LIST_SIDS = "SELECT w FROM SgwmtWindowAcc w "
			+ " where w.sgwmtWindowAccPK.employeeId IN :lstEmployeeId";

	/** The Constant USED. */
	private static final int USED = 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountRepository#
	 * findByUserIdAndUseAtr(java.lang.String, java.lang.Integer)
	 */
	@Override
	public Optional<WindowsAccount> findListWindowAccountByEmployeeId(String companyId, String employeeId) {

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder bd = em.getCriteriaBuilder();
		CriteriaQuery<SgwmtWindowAcc> cq = bd.createQuery(SgwmtWindowAcc.class);

		// Root
		Root<SgwmtWindowAcc> root = cq.from(SgwmtWindowAcc.class);
		cq.select(root);

		// Predicate where clause
		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(bd.equal(
				root.get(SgwmtWindowAcc_.sgwmtWindowAccPK).get(SgwmtWindowAccPK_.cid), companyId));
		predicateList.add(bd.equal(
				root.get(SgwmtWindowAcc_.sgwmtWindowAccPK).get(SgwmtWindowAccPK_.employeeId), employeeId));

		// Set Where clause to SQL Query
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Create Query
		List<SgwmtWindowAcc> result = em.createQuery(cq).getResultList();

		if (result.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(this.toWindowsAccountDomain(companyId, employeeId,
					result.stream().map(item -> this.toAccInfoDomain(item)).collect(Collectors.toList())));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountRepository#remove(
	 * java.util.List)
	 */
	@Override
	public void remove(String cid, String sid, Integer no) {

		SgwmtWindowAccPK pk = new SgwmtWindowAccPK(cid,sid, no);

		if (pk != null) {
			this.commandProxy().remove(SgwmtWindowAcc.class, pk);

		}
	}

	/**
	 * Adds the.
	 *
	 * @param windowAccount
	 *            the window account
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountRepository#add(nts.
	 * uk.ctx.sys.gateway.dom.singlesignon.WindowAccount)
	 */
	@Override
	public void add(String cid, String employeeId, WindowsAccountInfo windowAccount) {
		SgwmtWindowAcc entity = new SgwmtWindowAcc();
		windowAccount.saveToMemento(new JpaWindowAccountInfoSetMemento(cid,employeeId, entity));
		this.commandProxy().insert(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountRepository#
	 * findbyUserNameAndHostName(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Optional<WindowsAccount> findbyUserNameAndHostName(String userName, String hostName) {

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder bd = em.getCriteriaBuilder();
		CriteriaQuery<SgwmtWindowAcc> cq = bd.createQuery(SgwmtWindowAcc.class);

		// Root
		Root<SgwmtWindowAcc> root = cq.from(SgwmtWindowAcc.class);
		cq.select(root);

		// Predicate where clause
		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(bd.equal(root.get(SgwmtWindowAcc_.userName), userName));
		predicateList.add(bd.equal(root.get(SgwmtWindowAcc_.hostName), hostName));

		// Set Where clause to SQL Query
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Create Query
		List<SgwmtWindowAcc> result = em.createQuery(cq).getResultList();

		if (result.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional
					.of(this.toWindowsAccountDomain(result.stream().findFirst().get().getSgwmtWindowAccPK().getCid(),
							result.stream().findFirst().get().getSgwmtWindowAccPK().getEmployeeId(),
							result.stream().map(item -> this.toAccInfoDomain(item)).collect(Collectors.toList())));
		}

	}

	/**
	 * Findby user name and host name and is used.
	 *
	 * @param userName the user name
	 * @param hostName the host name
	 * @return the optional
	 */
	@Override
	public Optional<WindowsAccount> findbyUserNameAndHostNameAndIsUsed(String userName, String hostName) {
		
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder bd = em.getCriteriaBuilder();
		CriteriaQuery<SgwmtWindowAcc> cq = bd.createQuery(SgwmtWindowAcc.class);
		
		// Root
		Root<SgwmtWindowAcc> root = cq.from(SgwmtWindowAcc.class);
		cq.select(root);
		
		// Predicate where clause
		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(bd.equal(root.get(SgwmtWindowAcc_.userName), userName));
		predicateList.add(bd.equal(root.get(SgwmtWindowAcc_.hostName), hostName));
		predicateList.add(bd.equal(root.get(SgwmtWindowAcc_.useAtr), USED));
		
		// Set Where clause to SQL Query
		cq.where(predicateList.toArray(new Predicate[] {}));
		
		// Create Query
		List<SgwmtWindowAcc> result = em.createQuery(cq).getResultList();
		
		if (result.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(this.toWindowsAccountDomain(
					result.stream().findFirst().get().getSgwmtWindowAccPK().getCid(),
					result.stream().findFirst().get().getSgwmtWindowAccPK().getEmployeeId(),
					result.stream().map(item -> this.toAccInfoDomain(item))
					.collect(Collectors.toList())));
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountRepository#
	 * findByUserId(java.lang.String)
	 */
	@Override
	public Optional<WindowsAccount> findByEmployeeId(String cid,String employeeId) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder bd = em.getCriteriaBuilder();
		CriteriaQuery<SgwmtWindowAcc> cq = bd.createQuery(SgwmtWindowAcc.class);

		// Root
		Root<SgwmtWindowAcc> root = cq.from(SgwmtWindowAcc.class);
		cq.select(root);

		// Predicate where clause
		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(bd.equal(
				root.get(SgwmtWindowAcc_.sgwmtWindowAccPK).get(SgwmtWindowAccPK_.cid), cid));
		predicateList.add(bd.equal(
				root.get(SgwmtWindowAcc_.sgwmtWindowAccPK).get(SgwmtWindowAccPK_.employeeId), employeeId));

		// Set Where clause to SQL Query
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Create Query
		List<SgwmtWindowAcc> result = em.createQuery(cq).getResultList();

		if (result.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(this.toWindowsAccountDomain(cid, employeeId,
					result.stream().map(item -> this.toAccInfoDomain(item)).collect(Collectors.toList())));
		}
	}

	/**
	 * To windows account domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the windows account
	 */
	private WindowsAccount toWindowsAccountDomain(String companyId, String employeeId,
			List<WindowsAccountInfo> windowsAccountInfos) {
		return new WindowsAccount(new JpaWindowsAccountGetMemento(companyId, employeeId, windowsAccountInfos));
	}

	/**
	 * The Class JpaWindowsAccountGetMemento.
	 */
	private class JpaWindowsAccountGetMemento implements WindowsAccountGetMemento {

		/** The company id. */
		private String companyId;
		
		/** The user id. */
		private String employeeId;

		/** The windows account infos. */
		private List<WindowsAccountInfo> windowsAccountInfos;

		/**
		 * Instantiates a new jpa windows account get memento.
		 *
		 * @param userId
		 *            the user id
		 * @param windowsAccountInfos
		 *            the windows account infos
		 */
		public JpaWindowsAccountGetMemento(String companyId, String userId,
				List<WindowsAccountInfo> windowsAccountInfos) {
			this.companyId = companyId;
			this.employeeId = userId;
			this.windowsAccountInfos = windowsAccountInfos;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountGetMemento#
		 * getUserId()
		 */
		@Override
		public String getEmployeeId() {
			return this.employeeId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountGetMemento#
		 * getAccountInfos()
		 */
		@Override
		public List<WindowsAccountInfo> getAccountInfos() {
			return this.windowsAccountInfos;
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountGetMemento#getCompanyId()
		 */
		@Override
		public String getCompanyId() {
			return this.companyId;
		}
	}

	/**
	 * To acc info domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the windows account info
	 */
	private WindowsAccountInfo toAccInfoDomain(SgwmtWindowAcc entity) {
		return new WindowsAccountInfo(new JpaWindowAccountInfoGetMemento(entity));
	}

	/**
	 * Update.
	 *
	 * @param winAccCommand
	 *            the win acc command
	 * @param winAccDb
	 *            the win acc db
	 */
	@Override
	public void update(String companyId, String employeeId, WindowsAccountInfo winAccCommand,
			WindowsAccountInfo winAccDb) {
		SgwmtWindowAcc entity = this.queryProxy()
				.find(new SgwmtWindowAccPK(companyId,employeeId, winAccDb.getNo()), SgwmtWindowAcc.class).get();

		// set data
		entity.setHostName(winAccCommand.getHostName().v());
		entity.setUserName(winAccCommand.getUserName().v());
		entity.setUseAtr(winAccCommand.getUseAtr().value);

		// update
		this.commandProxy().update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountRepository#
	 * findByListUserId(java.util.List)
	 */
	@Override
	public List<WindowsAccount> findByListEmployeeId(List<String> lstEmployeeId) {
		// Check conditions
		if (CollectionUtil.isEmpty(lstEmployeeId)) {
			return Collections.emptyList();
		}

		// Split user id list.
		List<SgwmtWindowAcc> resultList = new ArrayList<>();

		CollectionUtil.split(lstEmployeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(GET_BY_LIST_SIDS, SgwmtWindowAcc.class)
					.setParameter("lstEmployeeId", subList).getList());
		});

		Map<String, List<SgwmtWindowAcc>> mapUsrAcc = resultList.stream()
				.collect(Collectors.groupingBy(item -> item.getSgwmtWindowAccPK().getEmployeeId()));

		// Return
		return mapUsrAcc.keySet().stream().map(employeeId -> {
			List<SgwmtWindowAcc> result = mapUsrAcc.get(employeeId);

			return this.toWindowsAccountDomain(result.stream().findFirst().get().getSgwmtWindowAccPK().getCid(),
					result.stream().findFirst().get().getSgwmtWindowAccPK().getEmployeeId(),
					result.stream().map(item -> this.toAccInfoDomain(item)).collect(Collectors.toList()));

		}).collect(Collectors.toList());
	}

}
