/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.log.infra.repository.loginrecord;

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

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.log.dom.loginrecord.LoginRecord;
import nts.uk.ctx.sys.log.dom.loginrecord.LoginRecordRepository;
import nts.uk.ctx.sys.log.infra.entity.loginrecord.SrcdtLoginRecord;
import nts.uk.ctx.sys.log.infra.entity.loginrecord.SrcdtLoginRecordPK_;
import nts.uk.ctx.sys.log.infra.entity.loginrecord.SrcdtLoginRecord_;

/**
 * The Class JpaLoginRecordRepository.
 */
@Stateless
public class JpaLoginRecordRepository extends JpaRepository implements LoginRecordRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.log.dom.loginrecord.LoginRecordRepository#add(nts.uk.ctx.
	 * sys.log.dom.loginrecord.LoginRecord)
	 */
	@Override
	public void add(LoginRecord loginRecord) {
		SrcdtLoginRecord entity = new SrcdtLoginRecord();
		loginRecord.saveToMemento(new JpaLoginRecordSetMemento(entity));
		this.commandProxy().insert(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.log.dom.loginrecord.LoginRecordRepository#loginRecordInfor
	 * (java.lang.String)
	 */
	@Override
	public Optional<LoginRecord> loginRecordInfor(String operationId) {
		EntityManager em = this.getEntityManager();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<SrcdtLoginRecord> query = builder.createQuery(SrcdtLoginRecord.class);
		Root<SrcdtLoginRecord> root = query.from(SrcdtLoginRecord.class);

		List<Predicate> predicateList = new ArrayList<>();

		predicateList.add(builder.equal(
				root.get(SrcdtLoginRecord_.srcdtLoginRecordPK).get(SrcdtLoginRecordPK_.operationId), operationId));

		query.where(predicateList.toArray(new Predicate[] {}));

		List<SrcdtLoginRecord> result = em.createQuery(query).getResultList();
		// get single Employee login setting
		if (result.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(new LoginRecord(new JpaLoginRecordGetMemento(result.get(0))));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.log.dom.loginrecord.LoginRecordRepository#logRecordInfor(
	 * java.util.List)
	 */
	@Override
	public List<LoginRecord> logRecordInfor(List<String> operationIds) {

		// check not data input
		if (CollectionUtil.isEmpty(operationIds)) {
			return new ArrayList<>();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<SrcdtLoginRecord> cq = criteriaBuilder.createQuery(SrcdtLoginRecord.class);
		Root<SrcdtLoginRecord> root = cq.from(SrcdtLoginRecord.class);

		// select root
		cq.select(root);

		// Split query.
		List<SrcdtLoginRecord> resultList = new ArrayList<>();

		CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, operationId -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();

			// employment in data employment
			lstpredicateWhere.add(criteriaBuilder.and(root.get(SrcdtLoginRecord_.srcdtLoginRecordPK)
					.get(SrcdtLoginRecordPK_.operationId).in(operationId)));

			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			// create query
			TypedQuery<SrcdtLoginRecord> query = em.createQuery(cq);
			resultList.addAll(query.getResultList());
		});

		// exclude select
		return resultList.stream().map(item -> new LoginRecord(new JpaLoginRecordGetMemento(item)))
				.collect(Collectors.toList());
	}

	@Override
	public List<LoginRecord> logRecordInforScreenF(List<String> operationIds) {
		// check not data input
		if (CollectionUtil.isEmpty(operationIds)) {
			return new ArrayList<>();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<SrcdtLoginRecord> cq = criteriaBuilder.createQuery(SrcdtLoginRecord.class);
		Root<SrcdtLoginRecord> root = cq.from(SrcdtLoginRecord.class);

		// select root
		cq.select(root);

		// Split query.
		List<SrcdtLoginRecord> resultList = new ArrayList<>();

		CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, operationId -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();

			// employment in data employment
			lstpredicateWhere.add(criteriaBuilder.and(root.get(SrcdtLoginRecord_.srcdtLoginRecordPK)
					.get(SrcdtLoginRecordPK_.operationId).in(operationId)));

			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			// create query
			TypedQuery<SrcdtLoginRecord> query = em.createQuery(cq).setMaxResults(1000);
			resultList.addAll(query.getResultList());
		});

		// exclude select
		return resultList.stream().map(item -> new LoginRecord(new JpaLoginRecordGetMemento(item)))
				.collect(Collectors.toList());
	}

	@Override
	public List<LoginRecord> logRecordInforRefactors(List<String> operationIds, int offset, int limit) {
		// check not data input
		if (CollectionUtil.isEmpty(operationIds)) {
			return new ArrayList<>();
		}

		// get entity manager
		EntityManager em = this.getEntityManager();
		em.clear();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<SrcdtLoginRecord> cq = criteriaBuilder.createQuery(SrcdtLoginRecord.class);
		Root<SrcdtLoginRecord> root = cq.from(SrcdtLoginRecord.class);

		// select root
		cq.select(root);

		// Split query.
		List<SrcdtLoginRecord> resultList = new ArrayList<>();

		CollectionUtil.split(operationIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, operationId -> {
			// add where
			List<Predicate> lstpredicateWhere = new ArrayList<>();

			// employment in data employment
			lstpredicateWhere.add(criteriaBuilder.and(root.get(SrcdtLoginRecord_.srcdtLoginRecordPK)
					.get(SrcdtLoginRecordPK_.operationId).in(operationId)));

			// set where to SQL
			cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

			// create query
			TypedQuery<SrcdtLoginRecord> query = em.createQuery(cq).setFirstResult(offset).setMaxResults(limit);
			resultList.addAll(query.getResultList());
		});

		// exclude select
		return resultList.stream().map(item -> new LoginRecord(new JpaLoginRecordGetMemento(item)))
				.collect(Collectors.toList());
	}
}
