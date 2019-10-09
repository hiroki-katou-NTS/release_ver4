/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.jobtitle.info;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleCode;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfo;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleName;
import nts.uk.ctx.bs.employee.dom.jobtitle.sequence.SequenceCode;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobHist;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobHist_;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobInfo;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobInfoPK;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobInfoPK_;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobInfo_;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobSeqMaster;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.BsymtJobSeqMaster_;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaJobTitleInfoRepository.
 */
@Stateless
public class JpaJobTitleInfoRepository extends JpaRepository implements JobTitleInfoRepository {

	/**
	 * To entity.
	 *
	 * @param jobTitleInfo
	 *            the job title info
	 * @return the bsymt job info
	 */
	private BsymtJobInfo toEntity(JobTitleInfo jobTitleInfo) {

		BsymtJobInfoPK pk = new BsymtJobInfoPK(jobTitleInfo.getCompanyId().v(),
				jobTitleInfo.getJobTitleHistoryId(), jobTitleInfo.getJobTitleId());
		BsymtJobInfo entity = this.queryProxy()
				.find(pk, BsymtJobInfo.class)
				.orElse(new BsymtJobInfo());

		jobTitleInfo.saveToMemento(new JpaJobTitleInfoSetMemento(entity));
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
		this.commandProxy().remove(BsymtJobInfo.class,
				new BsymtJobInfoPK(companyId, historyId, jobTitleId));
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
		listPredicate.add(criteriaBuilder
				.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
		listPredicate.add(criteriaBuilder.equal(
				root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.jobId), jobTitleId));
		listPredicate.add(criteriaBuilder.equal(
				root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.histId), historyId));
		cq.where(listPredicate.toArray(new Predicate[] {}));

		List<BsymtJobInfo> result = em.createQuery(cq).getResultList();

		if (result.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(new JobTitleInfo(new JpaJobTitleInfoGetMemento(result.get(0))));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#
	 * findByJobCode(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<JobTitleInfo> findByJobCode(String companyId, String jobTitleCode) {

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);

		// Build query
		cq.select(root);

		// add where
		List<Predicate> listPredicate = new ArrayList<>();
		listPredicate.add(criteriaBuilder
				.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
		listPredicate.add(criteriaBuilder.equal(root.get(BsymtJobInfo_.jobCd), jobTitleCode));
		cq.where(listPredicate.toArray(new Predicate[] {}));

		List<BsymtJobInfo> result = em.createQuery(cq).getResultList();

		if (result.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(new JobTitleInfo(new JpaJobTitleInfoGetMemento(result.get(0))));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#
	 * isSequenceMasterUsed(java.lang.String, java.lang.String)
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
		listPredicate.add(criteriaBuilder
				.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
		listPredicate.add(criteriaBuilder.equal(root.get(BsymtJobInfo_.sequenceCd), sequenceCode));
		cq.where(listPredicate.toArray(new Predicate[] {}));

		List<BsymtJobInfo> result = em.createQuery(cq).getResultList();

		return !result.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#findAll(
	 * java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<JobTitleInfo> findAll(String companyId, GeneralDate baseDate) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);
		Join<BsymtJobInfo, BsymtJobSeqMaster> joinRoot = root.join(BsymtJobInfo_.bsymtJobSeqMaster, JoinType.LEFT);
		
		// Build query
		cq.select(root);
		
		// add where
		List<Predicate> listPredicate = new ArrayList<>();
		listPredicate.add(criteriaBuilder
				.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
		listPredicate.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.startDate), baseDate));
		listPredicate.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.endDate), baseDate));

		cq.where(listPredicate.toArray(new Predicate[] {}));
		
		// Sort by disporder
		Expression<Object> queryCase = criteriaBuilder.selectCase()
				.when(criteriaBuilder.isNull(joinRoot.get(BsymtJobSeqMaster_.disporder)),
						Integer.MAX_VALUE)
				.otherwise(joinRoot.get(BsymtJobSeqMaster_.disporder));
		cq.orderBy(criteriaBuilder.asc(queryCase),
				criteriaBuilder.asc(root.get(BsymtJobInfo_.jobCd)));
		
		return em.createQuery(cq).getResultList().stream()
				.map(item -> new JobTitleInfo(new JpaJobTitleInfoGetMemento(item)))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#
	 * isJobTitleCodeExist(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isJobTitleCodeExist(String companyId, String jobTitleCode) {

		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);

		// Build query
		cq.select(root);

		// add where
		List<Predicate> listPredicate = new ArrayList<>();
		listPredicate.add(criteriaBuilder
				.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
		listPredicate.add(criteriaBuilder.equal(root.get(BsymtJobInfo_.jobCd), jobTitleCode));

		cq.where(listPredicate.toArray(new Predicate[] {}));

		return !em.createQuery(cq).getResultList().isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#find(java
	 * .lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<JobTitleInfo> find(String companyId, String jobTitleId, GeneralDate baseDate) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);

		// Build query
		cq.select(root);

		// add where
		List<Predicate> listPredicate = new ArrayList<>();
		listPredicate.add(criteriaBuilder
				.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
		listPredicate.add(criteriaBuilder.equal(
				root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.jobId), jobTitleId));
		listPredicate.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.startDate), baseDate));
		listPredicate.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.endDate), baseDate));

		cq.where(listPredicate.toArray(new Predicate[] {}));

		List<BsymtJobInfo> result = em.createQuery(cq).getResultList();
		if (result.isEmpty()) {
			return Optional.empty();
		} 
		
		return Optional.of(new JobTitleInfo(new JpaJobTitleInfoGetMemento(result.get(0))));
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#find(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<JobTitleInfo> find(String jobTitleId, GeneralDate baseDate) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);

		// Build query
		cq.select(root);

		// add where
		List<Predicate> listPredicate = new ArrayList<>();
		listPredicate.add(criteriaBuilder.equal(
				root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.jobId), jobTitleId));
		listPredicate.add(criteriaBuilder.lessThanOrEqualTo(
				root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.startDate), baseDate));
		listPredicate.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.endDate), baseDate));

		cq.where(listPredicate.toArray(new Predicate[] {}));

		List<BsymtJobInfo> result = em.createQuery(cq).getResultList();
		if (result.isEmpty()) {
			return Optional.empty();
		} 
		
		return Optional.of(new JobTitleInfo(new JpaJobTitleInfoGetMemento(result.get(0))));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#find(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<JobTitleCode> findJobTitleCode(String companyId, String jobTitleId) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);

		// Build query
		cq.select(root);

		// add where
		List<Predicate> listPredicate = new ArrayList<>();
		listPredicate.add(criteriaBuilder
				.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
		listPredicate.add(criteriaBuilder.equal(
				root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.jobId), jobTitleId));

		cq.where(listPredicate.toArray(new Predicate[] {}));

		List<BsymtJobInfo> result = em.createQuery(cq).getResultList();

		return Optional.of(new JobTitleInfo(new JpaJobTitleInfoGetMemento(result.get(0))).getJobTitleCode());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#findByIds(java.lang.String, java.util.List, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<JobTitleInfo> findByIds(String companyId, List<String> jobIds,
			GeneralDate baseDate) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);
		Join<BsymtJobInfo, BsymtJobSeqMaster> joinRoot = root.join(BsymtJobInfo_.bsymtJobSeqMaster, JoinType.LEFT);

		// Build query
		cq.select(root);
		
		List<BsymtJobInfo> resultList = new ArrayList<>();
		
		CollectionUtil.split(jobIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			// add where
			List<Predicate> listPredicate = new ArrayList<>();
			listPredicate.add(criteriaBuilder
					.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
			listPredicate.add(
					root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.jobId).in(subList));
			listPredicate.add(criteriaBuilder.lessThanOrEqualTo(
					root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.startDate), baseDate));
			listPredicate.add(criteriaBuilder.greaterThanOrEqualTo(
					root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.endDate), baseDate));

			cq.where(listPredicate.toArray(new Predicate[] {}));
			
			// Sort by disporder
			Expression<Object> queryCase = criteriaBuilder.selectCase()
					.when(criteriaBuilder.isNull(joinRoot.get(BsymtJobSeqMaster_.disporder)),
							Integer.MAX_VALUE)
					.otherwise(joinRoot.get(BsymtJobSeqMaster_.disporder));
			cq.orderBy(criteriaBuilder.asc(queryCase),
					criteriaBuilder.asc(root.get(BsymtJobInfo_.jobCd)));
			
			resultList.addAll(em.createQuery(cq).getResultList());
		});
		resultList.sort((o1, o2) -> {
			BsymtJobSeqMaster seqMst1 = o1.getBsymtJobSeqMaster();
			BsymtJobSeqMaster seqMst2 = o2.getBsymtJobSeqMaster();
			Integer order1 = seqMst1 != null ? seqMst1.getDisporder() : null;
			Integer order2 = seqMst2 != null ? seqMst2.getDisporder() : null;
			if (order1 != null && order2 != null) return order1.compareTo(order2);
			if (order1 != null && order2 == null) return 1;
			if (order1 == null && order2 != null) return -1;
			return o1.getJobCd().compareTo(o2.getJobCd());
		});
		
		// Check exist
		if (CollectionUtil.isEmpty(resultList)) {
			return Collections.emptyList();
		}

		// Return
		return resultList.stream().map(item -> new JobTitleInfo(new JpaJobTitleInfoGetMemento(item)))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<JobTitleInfo> findByIds(List<String> jobIds, GeneralDate baseDate) {
		List<JobTitleInfo> data = new ArrayList<>();
		CollectionUtil.split(jobIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			try (PreparedStatement statement = this.connection().prepareStatement(
						"SELECT j.CID, j.HIST_ID, j.JOB_ID, j.JOB_CD, j.JOB_NAME, j.SEQUENCE_CD, j.IS_MANAGER from BSYMT_JOB_INFO j"
						+ " INNER JOIN BSYMT_JOB_HIST h ON h.HIST_ID = j.HIST_ID"
						+ " WHERE h.START_DATE <= ? and h.END_DATE >= ? AND j.JOB_ID IN (" + subList.stream().map(s -> "?").collect(Collectors.joining(",")) + ")")) {
				statement.setDate(1, Date.valueOf(baseDate.localDate()));
				statement.setDate(2, Date.valueOf(baseDate.localDate()));
				for (int i = 0; i < subList.size(); i++) {
					statement.setString(i + 3, subList.get(i));
				}
				data.addAll(new NtsResultSet(statement.executeQuery()).getList(rec -> {
					return new JobTitleInfo(new CompanyId(rec.getString("CID")), rec.getString("HIST_ID"), rec.getInt("IS_MANAGER") == 1,
							rec.getString("JOB_ID"), new JobTitleCode(rec.getString("JOB_CD")), 
							new JobTitleName(rec.getString("JOB_NAME")), new SequenceCode(rec.getString("SEQUENCE_CD")));
				}));
			}catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		
		return data;
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository#findByIds(java.lang.String, java.util.List, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<GeneralDate, List<JobTitleInfo>> findByIds(String companyId, List<String> jobIds,
			List<GeneralDate> baseDates) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<?> cq = criteriaBuilder.createQuery();
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);
		Join<BsymtJobInfo, BsymtJobSeqMaster> joinRoot = root.join(BsymtJobInfo_.bsymtJobSeqMaster,
				JoinType.LEFT);
		Join<BsymtJobInfo, BsymtJobHist> joinHistRoot = root.join(BsymtJobInfo_.bsymtJobHist,
				JoinType.LEFT);

		// Build query
		cq.multiselect(root, joinHistRoot);

		List<Predicate> listPredicateBaseDate = new ArrayList<>();
		baseDates.forEach(baseDate -> {
			listPredicateBaseDate
					.add(criteriaBuilder.and(
							criteriaBuilder.lessThanOrEqualTo(root.get(BsymtJobInfo_.bsymtJobHist)
									.get(BsymtJobHist_.startDate), baseDate),
							criteriaBuilder.greaterThanOrEqualTo(
									root.get(BsymtJobInfo_.bsymtJobHist).get(BsymtJobHist_.endDate),
									baseDate)));
		});

		List<Object[]> resultList = new ArrayList<>();

		CollectionUtil.split(jobIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			// add where
			List<Predicate> listPredicate = new ArrayList<>();
			listPredicate.add(criteriaBuilder
					.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
			listPredicate
					.add(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.jobId).in(subList));


			listPredicate.add(criteriaBuilder.or(listPredicateBaseDate.toArray(new Predicate[] {})));

			cq.where(listPredicate.toArray(new Predicate[] {}));

			// Sort by disporder
			Expression<Object> queryCase = criteriaBuilder.selectCase()
					.when(criteriaBuilder.isNull(joinRoot.get(BsymtJobSeqMaster_.disporder)),
							Integer.MAX_VALUE)
					.otherwise(joinRoot.get(BsymtJobSeqMaster_.disporder));
			cq.orderBy(criteriaBuilder.asc(queryCase),
					criteriaBuilder.asc(root.get(BsymtJobInfo_.jobCd)));
			
			resultList.addAll( (List<Object[]>) em.createQuery(cq).getResultList() );
		});
		resultList.sort((o1, o2) -> {
			BsymtJobSeqMaster seqMst1 = ((BsymtJobInfo) o1[0]).getBsymtJobSeqMaster();
			BsymtJobSeqMaster seqMst2 = ((BsymtJobInfo) o2[0]).getBsymtJobSeqMaster();
			Integer order1 = seqMst1 != null ? seqMst1.getDisporder() : null;
			Integer order2 = seqMst2 != null ? seqMst2.getDisporder() : null;
			if (order1 != null && order2 != null) return order1.compareTo(order2);
			if (order1 != null && order2 == null) return 1;
			if (order1 == null && order2 != null) return -1;
			String jobCd1 = ((BsymtJobInfo) o1[0]).getJobCd();
			String jobCd2 = ((BsymtJobInfo) o2[0]).getJobCd();
			return jobCd1.compareTo(jobCd2);
		});

		Map<GeneralDate, List<JobTitleInfo>> mapItem = new HashMap<>();

		baseDates.forEach(baseDate -> {
			mapItem.putAll(resultList.stream().collect(Collectors.groupingBy(item -> {
				BsymtJobHist jobHist = (BsymtJobHist)item[1];
				return (new DatePeriod(jobHist.getStartDate(), jobHist.getEndDate())).contains(baseDate) ? baseDate : null;
			},Collectors.mapping(x ->   new JobTitleInfo(new JpaJobTitleInfoGetMemento((BsymtJobInfo)x[0])), Collectors.toList()))));
		});
		
		
		// Return
		return mapItem;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<JobTitleInfo> findByJobIds(String companyId, List<String> jobIds,
			String historyId) {
		// Get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<BsymtJobInfo> cq = criteriaBuilder.createQuery(BsymtJobInfo.class);
		Root<BsymtJobInfo> root = cq.from(BsymtJobInfo.class);
		Join<BsymtJobInfo, BsymtJobSeqMaster> joinRoot = root.join(BsymtJobInfo_.bsymtJobSeqMaster, JoinType.LEFT);

		// Build query
		cq.select(root);
		
		List<BsymtJobInfo> resultList = new ArrayList<>();
		
		CollectionUtil.split(jobIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			// add where
			List<Predicate> listPredicate = new ArrayList<>();
			listPredicate.add(criteriaBuilder
					.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.cid), companyId));
			listPredicate.add(
					root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.jobId).in(subList));
			listPredicate.add(criteriaBuilder
					.equal(root.get(BsymtJobInfo_.bsymtJobInfoPK).get(BsymtJobInfoPK_.histId), historyId));


			cq.where(listPredicate.toArray(new Predicate[] {}));
			
			// Sort by disporder
			Expression<Object> queryCase = criteriaBuilder.selectCase()
					.when(criteriaBuilder.isNull(joinRoot.get(BsymtJobSeqMaster_.disporder)),
							Integer.MAX_VALUE)
					.otherwise(joinRoot.get(BsymtJobSeqMaster_.disporder));
			cq.orderBy(criteriaBuilder.asc(queryCase),
					criteriaBuilder.asc(root.get(BsymtJobInfo_.jobCd)));
			
			resultList.addAll(em.createQuery(cq).getResultList());
		});
		resultList.sort((o1, o2) -> {
			BsymtJobSeqMaster seqMst1 = o1.getBsymtJobSeqMaster();
			BsymtJobSeqMaster seqMst2 = o2.getBsymtJobSeqMaster();
			Integer order1 = seqMst1 != null ? seqMst1.getDisporder() : null;
			Integer order2 = seqMst2 != null ? seqMst2.getDisporder() : null;
			if (order1 != null && order2 != null) return order1.compareTo(order2);
			if (order1 != null && order2 == null) return 1;
			if (order1 == null && order2 != null) return -1;
			return o1.getJobCd().compareTo(o2.getJobCd());
		});
		
		// Check exist
		if (CollectionUtil.isEmpty(resultList)) {
			return Collections.emptyList();
		}

		// Return
		return resultList.stream().map(item -> new JobTitleInfo(new JpaJobTitleInfoGetMemento(item)))
				.collect(Collectors.toList());
	}
}
