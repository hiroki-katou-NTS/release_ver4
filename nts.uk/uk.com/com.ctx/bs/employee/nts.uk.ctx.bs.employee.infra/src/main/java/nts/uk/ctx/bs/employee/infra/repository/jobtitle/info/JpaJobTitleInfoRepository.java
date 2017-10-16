/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.jobtitle.info;

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
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfo;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobHist_;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobInfo;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobInfoPK;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobInfoPK_;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobInfo_;

/**
 * The Class JpaJobTitleInfoRepository.
 */
@Stateless
public class JpaJobTitleInfoRepository extends JpaRepository implements JobTitleInfoRepository {

	/**
	 * To entity.
	 *
	 * @param jobTitleInfo the job title info
	 * @return the bsymt job info
	 */
	private BsymtJobInfo toEntity(JobTitleInfo jobTitleInfo) {

		Optional<BsymtJobInfo> optional = this.queryProxy().find(new BsymtJobInfoPK(jobTitleInfo.getCompanyId().v(),
				jobTitleInfo.getJobTitleHistoryId(), jobTitleInfo.getJobTitleId().v()), BsymtJobInfo.class);
		BsymtJobInfo entity = new BsymtJobInfo();
		if (optional.isPresent()) {
			entity = optional.get();
		}

		JpaJobTitleInfoSetMemento memento = new JpaJobTitleInfoSetMemento(entity);
		jobTitleInfo.saveToMemento(memento);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#add(nts.
	 * uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfo)
	 */
	@Override
	public void add(JobTitleInfo jobTitleInfo) {
		this.commandProxy().insert(this.toEntity(jobTitleInfo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#update(
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfo)
	 */
	@Override
	public void update(JobTitleInfo jobTitleInfo) {
		this.commandProxy().update(this.toEntity(jobTitleInfo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#remove(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void remove(String companyId, String jobTitleId, String historyId) {
		this.commandProxy().remove(BsymtJobInfo.class, new BsymtJobInfoPK(companyId, historyId, jobTitleId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#find(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<JobTitleInfo> find(String companyId, String jobTitleId, String historyId) {

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);

		// Build query
		cq.select(root);

		// add where
		List<Predicate> listPredicate = new ArrayList<>();
		listPredicate.add(
				criteriaBuilder.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
		listPredicate.add(
				criteriaBuilder.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.jobId), jobTitleId));
		listPredicate.add(
				criteriaBuilder.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.histId), historyId));
		cq.where(listPredicate.toArray(new Predicate[] {}));

		List<BsymtJobInfo> result = em.createQuery(cq).getResultList();

		if (result.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(new JobTitleInfo(new JpaJobTitleInfoGetMemento(result.get(0))));
		}
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#isSequenceMasterUsed(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isSequenceMasterUsed(String companyId, String sequenceCode) {
		
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);
		
		// Build query
		cq.select(root);
		
		// add where
		List<Predicate> listPredicate = new ArrayList<>();
		listPredicate.add(
				criteriaBuilder.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
		listPredicate.add(
				criteriaBuilder.equal(root.get(BsymtJobInfo_.sequenceCd), sequenceCode));
		cq.where(listPredicate.toArray(new Predicate[] {}));

		List<BsymtJobInfo> result = em.createQuery(cq).getResultList();
		
		return !result.isEmpty();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#findAll(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<JobTitleInfo> findAll(String companyId, GeneralDate baseDate) {
		
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);
		
		// Build query
		cq.select(root);
		
		// add where
        List<Predicate> listPredicate = new ArrayList<>();
        listPredicate.add(
        		criteriaBuilder.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
        listPredicate.add(
        		criteriaBuilder.lessThanOrEqualTo(root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.startDate), baseDate));
        listPredicate.add(
        		criteriaBuilder.greaterThanOrEqualTo(root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.endDate), baseDate));

        cq.where(listPredicate.toArray(new Predicate[] {}));
        
        return em.createQuery(cq).getResultList().stream()
                .map(item -> new JobTitleInfo(new JpaJobTitleInfoGetMemento(item)))
                .collect(Collectors.toList());
	}
}
