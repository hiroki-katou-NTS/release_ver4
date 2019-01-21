/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.infra.repository.singlesignon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtOtherSysAcc;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtOtherSysAccPK;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtOtherSysAccPK_;
import nts.uk.ctx.sys.gateway.infra.entity.singlesignon.SgwmtOtherSysAcc_;

/**
 * The Class JpaOtherSysAccountRepository.
 */
@Stateless
public class JpaOtherSysAccountRepository extends JpaRepository implements OtherSysAccountRepository {

	/** The get by list userids. */
	private static final String GET_BY_LIST_SID = "SELECT o FROM SgwmtOtherSysAcc o "
			+ " where o.sgwmtOtherSysAccPK.employeeId IN :lstEmployeeId";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository#remove(
	 * java.lang.String)
	 */
	@Override
	public void remove(String cid, String employeeId) {
		SgwmtOtherSysAccPK pk = new SgwmtOtherSysAccPK(cid,employeeId);

		if (pk != null) {
			this.commandProxy().remove(SgwmtOtherSysAcc.class, pk);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository#add(nts
	 * .uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccount)
	 */
	@Override
	public void add(OtherSysAccount otherSysAccount) {
		SgwmtOtherSysAcc entity = new SgwmtOtherSysAcc();
		otherSysAccount.saveToMemento(new JpaOtherSysAccountSetMemento(entity));
		this.commandProxy().insert(entity);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository#
	 * findByCompanyCodeAndUserName(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<OtherSysAccount> findByCompanyCodeAndUserName(String companyCode, String userName) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder bd = em.getCriteriaBuilder();
		CriteriaQuery<SgwmtOtherSysAcc> cq = bd.createQuery(SgwmtOtherSysAcc.class);

		// Root
		Root<SgwmtOtherSysAcc> root = cq.from(SgwmtOtherSysAcc.class);
		cq.select(root);

		// Predicate where clause
		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(bd.equal(root.get(SgwmtOtherSysAcc_.ccd), companyCode));
		predicateList.add(bd.equal(root.get(SgwmtOtherSysAcc_.userName), userName));

		// Set Where clause to SQL Query
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Create Query
		List<SgwmtOtherSysAcc> result = em.createQuery(cq).getResultList();

		if (result.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(this.toDomain(result.get(0)));
		}

	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the other sys account
	 */
	private OtherSysAccount toDomain(SgwmtOtherSysAcc entity) {
		return new OtherSysAccount(new JpaOtherSysAccountGetMemento(entity));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository#
	 * findByUserId(java.lang.String)
	 */
	@Override
	public Optional<OtherSysAccount> findByEmployeeId(String companyId, String employeeId) {

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder bd = em.getCriteriaBuilder();
		CriteriaQuery<SgwmtOtherSysAcc> cq = bd.createQuery(SgwmtOtherSysAcc.class);

		// Root
		Root<SgwmtOtherSysAcc> root = cq.from(SgwmtOtherSysAcc.class);
		cq.select(root);

		// Predicate where clause
		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(bd.equal(root.get(SgwmtOtherSysAcc_.sgwmtOtherSysAccPK).get(SgwmtOtherSysAccPK_.employeeId),
				employeeId));
		predicateList
				.add(bd.equal(root.get(SgwmtOtherSysAcc_.sgwmtOtherSysAccPK).get(SgwmtOtherSysAccPK_.cid), companyId));

		// Set Where clause to SQL Query
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Create Query
		TypedQuery<SgwmtOtherSysAcc> query = em.createQuery(cq);

		try {
			// exclude select
			return Optional.of(this.toDomain(query.getSingleResult()));
		} catch (NoResultException e) {
			return Optional.empty();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository#update(
	 * nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccount,
	 * nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccount)
	 */
	@Override
	public void update(OtherSysAccount otherSysAccCommand, OtherSysAccount otherSysAccDB) {
		SgwmtOtherSysAcc entity = this.queryProxy()
				.find(new SgwmtOtherSysAccPK(otherSysAccDB.getCompanyId(),otherSysAccDB.getEmployeeId()), SgwmtOtherSysAcc.class).get();

		// set data
		entity.setCcd(otherSysAccCommand.getAccountInfo().getCompanyCode().v());
		entity.setUserName(otherSysAccCommand.getAccountInfo().getUserName().v());
		entity.setUseAtr(otherSysAccCommand.getAccountInfo().getUseAtr().value);

		// update
		this.commandProxy().update(entity);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository#
	 * findAllOtherSysAccount(java.util.List)
	 */
	@Override
	public List<OtherSysAccount> findAllOtherSysAccount(List<String> listEmployeeId) {
		// Check conditions
		if (CollectionUtil.isEmpty(listEmployeeId)) {
			return Collections.emptyList();
		}

		// Split user id list.
		List<SgwmtOtherSysAcc> resultList = new ArrayList<>();

		CollectionUtil.split(listEmployeeId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(GET_BY_LIST_SID, SgwmtOtherSysAcc.class)
					.setParameter("lstEmployeeId", subList).getList());
		});

		// Return
		return resultList.stream().map(entity -> this.toDomain(entity)).collect(Collectors.toList());

	}

}
