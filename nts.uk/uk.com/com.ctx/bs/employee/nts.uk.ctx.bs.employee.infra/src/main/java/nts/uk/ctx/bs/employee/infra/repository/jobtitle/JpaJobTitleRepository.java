/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.jobtitle;

import java.util.ArrayList;
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

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.jobtitle.JobTitle;
import nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobHist;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobHistPK;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobHistPK_;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobHist_;

/**
 * The Class JpaJobTitleRepository.
 */
@Stateless
public class JpaJobTitleRepository extends JpaRepository implements JobTitleRepository {

	/** The Constant ELEMENT_FIRST. */
	private static final Integer ELEMENT_FIRST = 0;

	/**
	 * To entity.
	 *
	 * @param jobTitle
	 *            the job title
	 * @return the list
	 */
	private List<BsymtJobHist> toEntity(JobTitle jobTitle) {

		List<BsymtJobHist> listEntity = jobTitle.getJobTitleHistories().stream()
				.map(jobTitleHistory -> {
					BsymtJobHistPK pk = new BsymtJobHistPK(jobTitle.getCompanyId().v(),
							jobTitleHistory.identifier(), jobTitle.getJobTitleId());
					BsymtJobHist entity = this.queryProxy().find(pk, BsymtJobHist.class)
							.orElse(new BsymtJobHist());
					entity.setBsymtJobHistPK(pk);
					entity.setStartDate(jobTitleHistory.span().start());
					entity.setEndDate(jobTitleHistory.span().end());
					return entity;
				}).collect(Collectors.toList());

		jobTitle.saveToMemento(new JpaJobTitleSetMemento(listEntity));
		return listEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository#add(nts.uk.ctx.bs.
	 * employee.dom.jobtitle.JobTitle)
	 */
	@Override
	public void add(JobTitle jobTitle) {
		this.commandProxy().insertAll(this.toEntity(jobTitle));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository#update(nts.uk.ctx.
	 * bs.employee.dom.jobtitle.JobTitle)
	 */
	@Override
	public void update(JobTitle jobTitle) {
		this.commandProxy().updateAll(this.toEntity(jobTitle));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository#remove(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public void remove(String companyId, String jobTitleId) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository#removeHistory(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void removeHistory(String companyId, String jobTitleId, String historyId) {
		this.commandProxy().remove(BsymtJobHist.class,
				new BsymtJobHistPK(companyId, historyId, jobTitleId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository#findByJobTitleId(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<JobTitle> findByJobTitleId(String companyId, String jobTitleId) {

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobHist> cq = criteriaBuilder.createQuery(BsymtJobHist.class);
		Root<BsymtJobHist> root = cq.from(BsymtJobHist.class);

		// Build query
		cq.select(root);

		// Add where conditions
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(BsymtJobHist_.bsymtJobHistPK).get(BsymtJobHistPK_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtJobHist_.bsymtJobHistPK).get(BsymtJobHistPK_.jobId), jobTitleId));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
		cq.orderBy(criteriaBuilder.desc(root.get(BsymtJobHist_.startDate)));

		List<BsymtJobHist> listBsymtJobHist = em.createQuery(cq).getResultList();

		// Check exist
		if (CollectionUtil.isEmpty(listBsymtJobHist)) {
			return Optional.empty();
		}

		// Return
		return Optional.of(
				new JobTitle(new JpaJobTitleGetMemento(companyId, jobTitleId, listBsymtJobHist)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository#findByHistoryId(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<JobTitle> findByHistoryId(String companyId, String historyId) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobHist> cq = criteriaBuilder.createQuery(BsymtJobHist.class);
		Root<BsymtJobHist> root = cq.from(BsymtJobHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(BsymtJobHist_.bsymtJobHistPK).get(BsymtJobHistPK_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtJobHist_.bsymtJobHistPK).get(BsymtJobHistPK_.histId), historyId));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
		cq.orderBy(criteriaBuilder.desc(root.get(BsymtJobHist_.startDate)));

		List<BsymtJobHist> listBsymtJobHist = em.createQuery(cq).getResultList();

		return Optional.of(new JobTitle(new JpaJobTitleGetMemento(companyId,
				listBsymtJobHist.get(ELEMENT_FIRST).getBsymtJobHistPK().getJobId(),
				listBsymtJobHist)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository#findByBaseDate(
	 * java.lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<JobTitle> findByBaseDate(String companyId, String jobTitleId,
			GeneralDate baseDate) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobHist> cq = criteriaBuilder.createQuery(BsymtJobHist.class);
		Root<BsymtJobHist> root = cq.from(BsymtJobHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(BsymtJobHist_.bsymtJobHistPK).get(BsymtJobHistPK_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(BsymtJobHist_.bsymtJobHistPK).get(BsymtJobHistPK_.jobId), jobTitleId));
		lstpredicateWhere.add(
				criteriaBuilder.lessThanOrEqualTo(root.get(BsymtJobHist_.startDate), baseDate));
		lstpredicateWhere.add(
				criteriaBuilder.greaterThanOrEqualTo(root.get(BsymtJobHist_.endDate), baseDate));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		List<BsymtJobHist> listBsymtJobHist = em.createQuery(cq).getResultList();

		return Optional.of(
				new JobTitle(new JpaJobTitleGetMemento(companyId, jobTitleId, listBsymtJobHist)));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository#findAll(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<JobTitle> findAll(String companyId, GeneralDate baseDate) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobHist> cq = criteriaBuilder.createQuery(BsymtJobHist.class);
		Root<BsymtJobHist> root = cq.from(BsymtJobHist.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(BsymtJobHist_.bsymtJobHistPK).get(BsymtJobHistPK_.cid), companyId));
		lstpredicateWhere.add(
				criteriaBuilder.lessThanOrEqualTo(root.get(BsymtJobHist_.startDate), baseDate));
		lstpredicateWhere.add(
				criteriaBuilder.greaterThanOrEqualTo(root.get(BsymtJobHist_.endDate), baseDate));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
		
		// TODO: Join BsymtJobSeqMaster, sort by 序列．並び順 ASC

		// Get result
		List<BsymtJobHist> listBsymtJobHist = em.createQuery(cq).getResultList();

		// Create map
		Map<String, List<BsymtJobHist>> mapBsymtJobHist = listBsymtJobHist.stream()
				.collect(Collectors.groupingBy(item -> item.getBsymtJobHistPK().getJobId()));

		// Return
		return mapBsymtJobHist.keySet().stream()
				.map(item -> new JobTitle(
						new JpaJobTitleGetMemento(companyId, item, mapBsymtJobHist.get(item))))
				.collect(Collectors.toList());

	}
	//ドメインモデル「職位」を取得する
	@Override
	public List<JobTitle> findAllById(String companyId,List<String> positionIds ,GeneralDate baseDate) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobHist> cq = criteriaBuilder.createQuery(BsymtJobHist.class);
		Root<BsymtJobHist> root = cq.from(BsymtJobHist.class);

		// select root
		cq.select(root);

		List<BsymtJobHist> listBsymtJobHist = new ArrayList<>();
		CollectionUtil.split(positionIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder
				.equal(root.get(BsymtJobHist_.bsymtJobHistPK).get(BsymtJobHistPK_.cid), companyId));
		lstpredicateWhere.add(root.get(BsymtJobHist_.bsymtJobHistPK).get(BsymtJobHistPK_.jobId).in(splitData));
		lstpredicateWhere.add(
				criteriaBuilder.lessThanOrEqualTo(root.get(BsymtJobHist_.startDate), baseDate));
		lstpredicateWhere.add(
				criteriaBuilder.greaterThanOrEqualTo(root.get(BsymtJobHist_.endDate), baseDate));

		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
		// Get result
		listBsymtJobHist.addAll(em.createQuery(cq).getResultList());
		});
		
		// Create map
		Map<String, List<BsymtJobHist>> mapBsymtJobHist = listBsymtJobHist.stream()
				.collect(Collectors.groupingBy(item -> item.getBsymtJobHistPK().getJobId()));

		// Return
		return mapBsymtJobHist.keySet().stream()
				.map(item -> new JobTitle(
						new JpaJobTitleGetMemento(companyId, item, mapBsymtJobHist.get(item))))
				.collect(Collectors.toList());

	}
}
