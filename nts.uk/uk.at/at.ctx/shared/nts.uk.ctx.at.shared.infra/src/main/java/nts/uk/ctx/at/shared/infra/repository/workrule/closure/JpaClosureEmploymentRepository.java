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

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmpClosureEmploymentPK;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmpClosureEmploymentPK_;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosureEmployment;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosureEmployment_;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaClosureEmploymentRepository extends JpaRepository implements ClosureEmploymentRepository {

	private static final String DELETE_ALL = "DELETE FROM KclmtClosureEmployment c WHERE c.kclmpClosureEmploymentPK.companyId = :companyId";
	
	private final String DELETE_CID_SCD = "DELETE FROM KclmtClosureEmployment c WHERE c.kclmpClosureEmploymentPK.companyId = :companyId "
																				+ "	AND c.kclmpClosureEmploymentPK.employmentCD = :employmentCD";
	
	private static final String FIND;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KclmtClosureEmployment a ");
		builderString.append("WHERE a.kclmpClosureEmploymentPK.companyId = :companyId ");
		builderString.append("AND a.kclmpClosureEmploymentPK.employmentCD IN :employmentCDs ");
		FIND = builderString.toString();
	}
	
	/**
	 * Add list ClosureEmployment by delete all and insert.
	 */
	@Override
	public void addListClousureEmp(String companyID, List<ClosureEmployment> listClosureEmpDom) {
		//List Clousure Employment to add new.
		List<KclmtClosureEmployment> lstEntityAdd = listClosureEmpDom.stream().map(item -> {
			return new KclmtClosureEmployment(new KclmpClosureEmploymentPK(companyID, item.getEmploymentCD()), item.getClosureId());
		}).collect(Collectors.toList());
		
		//List Clousure Employment to delete all when size of listClosureEmpDom > 1
		if (listClosureEmpDom.size() == 1) {
			this.getEntityManager().createQuery(DELETE_CID_SCD).setParameter("companyId", companyID)
															   .setParameter("employmentCD", listClosureEmpDom.get(0).getEmploymentCD()).executeUpdate();
		} else {
			this.getEntityManager().createQuery(DELETE_ALL).setParameter("companyId", companyID)
			.executeUpdate();
		}
		
		//Then, add new all row in table.
		this.commandProxy().insertAll(lstEntityAdd);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository#findByEmploymentCD(java.lang.String, java.lang.String)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<ClosureEmployment> findByEmploymentCD(String companyID, String employmentCD) {
		return this.queryProxy()
				.find(new KclmpClosureEmploymentPK(companyID, employmentCD), KclmtClosureEmployment.class)
				.map(x -> convertToDomain(x));
	}

	/**
	 * get list by list employmentCD for KIF 001
	 */
	@Override
	public List<ClosureEmployment> findListEmployment(String companyId,
			List<String> employmentCDs) {
		List<KclmtClosureEmployment> result = new ArrayList<>();

		CollectionUtil.split(employmentCDs, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			result.addAll(this.queryProxy().query(FIND, KclmtClosureEmployment.class)
					.setParameter("companyId", companyId)
					.setParameter("employmentCDs", splitData).getList());
		});

		return result.stream().map(f -> convertToDomain(f)).collect(Collectors.toList());
	}

	/**
	 * Convert to domain.
	 *
	 * @param kclmtClosureEmployment the kclmt closure employment
	 * @return the closure employment
	 */
	private ClosureEmployment convertToDomain(KclmtClosureEmployment kclmtClosureEmployment) {
		return new ClosureEmployment(kclmtClosureEmployment.kclmpClosureEmploymentPK.companyId,
				kclmtClosureEmployment.kclmpClosureEmploymentPK.employmentCD, 
				kclmtClosureEmployment.closureId);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository#findByClosureId(java.lang.String, int)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ClosureEmployment> findByClosureId(String companyId, int closureId) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<KclmtClosureEmployment> cq = cb.createQuery(KclmtClosureEmployment.class);
		
		// Root
		Root<KclmtClosureEmployment> root = cq.from(KclmtClosureEmployment.class);
		cq.select(root);
		
		// Predicate where clause
		List<Predicate> predicateList = new ArrayList<>();
		// Equal companyId
		predicateList.add(cb.equal(
				root.get(KclmtClosureEmployment_.kclmpClosureEmploymentPK).get(KclmpClosureEmploymentPK_.companyId),
				companyId));
		// Equal ClosureId
		predicateList.add(cb.equal(
				root.get(KclmtClosureEmployment_.closureId),
				closureId));

		// Create Query
		cq.where(predicateList.toArray(new Predicate[] {}));
		TypedQuery<KclmtClosureEmployment> query = em.createQuery(cq);

		return query.getResultList().stream().map(item -> this.convertToDomain(item)).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository#
	 * findByClosureId(java.lang.String, java.util.List)
	 */
	@Override
	public List<ClosureEmployment> findByClosureIds(String companyId, List<Integer> closureIds) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<KclmtClosureEmployment> cq = cb.createQuery(KclmtClosureEmployment.class);

		// Root
		Root<KclmtClosureEmployment> root = cq.from(KclmtClosureEmployment.class);
		cq.select(root);
		
		// Predicate where clause
		List<Predicate> predicateList = new ArrayList<>();
		// Equal companyId
		predicateList.add(cb.equal(root.get(KclmtClosureEmployment_.kclmpClosureEmploymentPK)
				.get(KclmpClosureEmploymentPK_.companyId), companyId));
		// in ClosureIds
		predicateList.add(root.get(KclmtClosureEmployment_.closureId).in(closureIds));

		// Create Query.
		cq.where(predicateList.toArray(new Predicate[] {}));

		List<KclmtClosureEmployment> resultList = em.createQuery(cq).getResultList();
		
		return resultList.stream().map(item -> this.convertToDomain(item)).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository#removeClousureEmp(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeClousureEmp(String companyID, String employmentCD) {
//		KclmpClosureEmploymentPK key = new KclmpClosureEmploymentPK(companyID, employmentCD);
//		this.commandProxy().remove(KclmpClosureEmploymentPK.class, key);
		this.getEntityManager().createQuery(DELETE_CID_SCD).setParameter("companyId", companyID)
		   												   .setParameter("employmentCD", employmentCD).executeUpdate();
	}
}
