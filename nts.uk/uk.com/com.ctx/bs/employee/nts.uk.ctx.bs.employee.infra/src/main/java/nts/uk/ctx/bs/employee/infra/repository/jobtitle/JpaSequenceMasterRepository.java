/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.jobtitle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import nts.uk.ctx.bs.employee.dom.jobtitle.info.SequenceMaster;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.SequenceMasterRepository;
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
	 * @param domain
	 *            the domain
	 * @return the csqmt sequence master
	 */
	private BsymtJobSeqMaster toEntity(SequenceMaster domain) {
		BsymtJobSeqMaster entity = new BsymtJobSeqMaster();
		domain.saveToMemento(new JpaSequenceMasterSetMemento(entity));
		return entity;
	}

	/**
	 * To domain.
	 *
	 * @param entity
	 *            the entity
	 * @return the sequence master
	 */
	private SequenceMaster toDomain(BsymtJobSeqMaster entity) {
		return new SequenceMaster(new JpaSequenceMasterGetMemento(entity));
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
		Expression<String> exp = root
				.get(BsymtJobSeqMaster_.bsymtJobSeqMasterPK)
				.get(BsymtJobSeqMasterPK_.cid);
		predicateList.add(exp.in(companyId));
				
		cq.where(predicateList.toArray(new Predicate[] {}));
		cq.orderBy(criteriaBuilder.desc(root
				.get(BsymtJobSeqMaster_.disporder)));
		
		return em.createQuery(cq).getResultList().stream()
				.map(entity -> toDomain(entity))
				.collect(Collectors.toList());
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
		this.commandProxy().remove(BsymtJobSeqMaster.class,
				new BsymtJobSeqMasterPK(companyId, sequenceCode));
	}

}
