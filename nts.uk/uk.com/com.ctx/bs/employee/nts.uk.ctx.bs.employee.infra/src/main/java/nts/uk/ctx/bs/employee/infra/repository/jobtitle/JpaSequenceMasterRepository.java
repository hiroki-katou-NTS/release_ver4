/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.jobtitle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.jobtitle.sequence.SequenceMaster;
import nts.uk.ctx.bs.employee.dom.jobtitle.sequence.SequenceMasterRepository;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobInfoPK_;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobInfo_;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobSeqMaster;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobSeqMasterPK;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobSeqMasterPK_;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobSeqMaster_;

/**
 * The Class JpaSequenceMasterRepository.
 */
@Stateless
public class JpaSequenceMasterRepository extends JpaRepository implements SequenceMasterRepository {

	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the bsymt job seq master
	 */
	private BsymtJobSeqMaster toEntity(SequenceMaster domain) {
		BsymtJobSeqMasterPK pk = new BsymtJobSeqMasterPK(domain.getCompanyId().v(), domain.getSequenceCode().v());
		BsymtJobSeqMaster entity = this.queryProxy()
				.find(pk, BsymtJobSeqMaster.class)
				.orElse(new BsymtJobSeqMaster());
		domain.saveToMemento(new JpaSequenceMasterSetMemento(entity));
		return entity;
	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the sequence master
	 */
	private SequenceMaster toDomain(BsymtJobSeqMaster entity) {
		return new SequenceMaster(new JpaSequenceMasterGetMemento(entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.SequenceMasterRepository#add(nts
	 * .uk.ctx.bs.employee.dom.jobtitle.info.SequenceMaster)
	 */
	@Override
	public void add(SequenceMaster domain) {
		this.commandProxy().insert(this.toEntity(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.SequenceMasterRepository#update(
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.SequenceMaster)
	 */
	@Override
	public void update(SequenceMaster domain) {
		this.commandProxy().update(this.toEntity(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.SequenceMasterRepository#remove(
	 * java.lang.String, java.lang.String, short)
	 */
	@Override
	public void remove(String companyId, String sequenceCode) {
		this.commandProxy().remove(BsymtJobSeqMaster.class, new BsymtJobSeqMasterPK(companyId, sequenceCode));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.SequenceMasterRepository#
	 * findByCompanyId(java.lang.String)
	 */
	@Override
	public List<SequenceMaster> findByCompanyId(String companyId) {

		// Check empty
		if (StringUtils.isEmpty(companyId)) {
			return Collections.<SequenceMaster>emptyList();
		}

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobSeqMaster> cq = criteriaBuilder.createQuery(BsymtJobSeqMaster.class);
		Root<BsymtJobSeqMaster> root = cq.from(BsymtJobSeqMaster.class);

		// Build query
		cq.select(root);

		// Add where conditions
		List<Predicate> predicateList = new ArrayList<>();
		Expression<String> exp = root.get(BsymtJobSeqMaster_.bsymtJobSeqMasterPK).get(BsymtJobSeqMasterPK_.cid);
		predicateList.add(exp.in(companyId));

		cq.where(predicateList.toArray(new Predicate[] {}));
		cq.orderBy(criteriaBuilder.asc(root.get(BsymtJobSeqMaster_.disporder)));

		return em.createQuery(cq).getResultList().stream().map(entity -> toDomain(entity)).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.SequenceMasterRepository#
	 * findBySequenceCode(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<SequenceMaster> findBySequenceCode(String companyId, String sequenceCode) {

		// Check empty
		if (StringUtils.isEmpty(companyId)) {
			return Optional.empty();
		}

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobSeqMaster> cq = criteriaBuilder.createQuery(BsymtJobSeqMaster.class);
		Root<BsymtJobSeqMaster> root = cq.from(BsymtJobSeqMaster.class);

		// Build query
		cq.select(root);

		// Add where conditions
		List<Predicate> predicateList = new ArrayList<>();
		Expression<String> exp = root.get(BsymtJobSeqMaster_.bsymtJobSeqMasterPK).get(BsymtJobSeqMasterPK_.cid);
		predicateList.add(exp.in(companyId));
		predicateList.add(criteriaBuilder
				.equal(root.get(BsymtJobSeqMaster_.bsymtJobSeqMasterPK).get(BsymtJobSeqMasterPK_.seqCd), sequenceCode));

		cq.where(predicateList.toArray(new Predicate[] {}));
		cq.orderBy(criteriaBuilder.asc(root.get(BsymtJobSeqMaster_.disporder)));

		List<BsymtJobSeqMaster> result = em.createQuery(cq).getResultList();
		// Check empty
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}
		return Optional.of(toDomain(result.get(0)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.SequenceMasterRepository#
	 * findMaxOrder()
	 */
	@Override
	public int findMaxOrder() {

		// Check if sequence is empty
		if (isEmpty()) {
			return 1;
		}

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = criteriaBuilder.createQuery(Integer.class);
		Root<BsymtJobSeqMaster> root = cq.from(BsymtJobSeqMaster.class);

		// Build query
		cq.select(criteriaBuilder.max(root.get(BsymtJobSeqMaster_.disporder)));

		return em.createQuery(cq).getSingleResult();
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	private boolean isEmpty() {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		Root<BsymtJobSeqMaster> root = cq.from(BsymtJobSeqMaster.class);

		cq.select(criteriaBuilder.count(root));
		Long count = em.createQuery(cq).getSingleResult();

		return count == 0;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.sequence.SequenceMasterRepository#updateOrder(java.util.List)
	 */
	@Override
	public void updateOrder(List<SequenceMaster> listSequenceMaster) {
		this.commandProxy().updateAll(listSequenceMaster.stream()
				.map(domain -> this.toEntity(domain))
				.collect(Collectors.toList()));
	}
	
	@Override
	public List<SequenceMaster> findAll(String companyId, String sequenceCode) {

		// Check empty
		if (StringUtils.isEmpty(companyId)) {
			return Collections.<SequenceMaster>emptyList();
		}

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobSeqMaster> cq = criteriaBuilder.createQuery(BsymtJobSeqMaster.class);
		Root<BsymtJobSeqMaster> root = cq.from(BsymtJobSeqMaster.class);

		// Build query
		cq.select(root);

		// Add where conditions
		List<Predicate> predicateList = new ArrayList<>();
		Expression<String> exp = root.get(BsymtJobSeqMaster_.bsymtJobSeqMasterPK).get(BsymtJobSeqMasterPK_.cid);
		predicateList.add(exp.in(companyId));
		predicateList.add(criteriaBuilder
				.equal(root.get(BsymtJobSeqMaster_.bsymtJobSeqMasterPK).get(BsymtJobSeqMasterPK_.cid), companyId));

		cq.where(predicateList.toArray(new Predicate[] {}));
		cq.orderBy(criteriaBuilder.asc(root.get(BsymtJobSeqMaster_.disporder)));

		return em.createQuery(cq).getResultList().stream().map(entity -> toDomain(entity)).collect(Collectors.toList());
	}
}
