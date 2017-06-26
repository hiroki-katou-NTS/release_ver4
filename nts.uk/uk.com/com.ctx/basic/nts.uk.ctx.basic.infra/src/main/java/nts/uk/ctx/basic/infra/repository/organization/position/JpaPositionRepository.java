package nts.uk.ctx.basic.infra.repository.organization.position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.organization.position.AuthorizationLevel;
import nts.uk.ctx.basic.dom.organization.position.JobCode;
import nts.uk.ctx.basic.dom.organization.position.JobHistory;
import nts.uk.ctx.basic.dom.organization.position.JobRefAuth;
import nts.uk.ctx.basic.dom.organization.position.JobTitle;
import nts.uk.ctx.basic.dom.organization.position.JobTitleRef;
import nts.uk.ctx.basic.dom.organization.position.PositionRepository;
import nts.uk.ctx.basic.infra.entity.organization.position.CatmtAuth;
import nts.uk.ctx.basic.infra.entity.organization.position.CmnmtJobHist;
import nts.uk.ctx.basic.infra.entity.organization.position.CmnmtJobHistPK;
import nts.uk.ctx.basic.infra.entity.organization.position.CmnmtJobTitle;
import nts.uk.ctx.basic.infra.entity.organization.position.CmnmtJobTitlePK;
import nts.uk.ctx.basic.infra.entity.organization.position.CmnmtJobTitleRef;
import nts.uk.ctx.basic.infra.entity.organization.position.CmnmtJobTitleRefPK;

@Stateless
public class JpaPositionRepository extends JpaRepository implements PositionRepository {

	private static final String SELECT_ALL_POSITION;
	private static final String SELECT_ALL_HISTORY;
	private static final String FIND_SINGLE_HISTORY;
	private static final String FIND_SINGLE;
	private static final String SELECT_HISTORY_BY_END_DATE;
	private static final String SELECT_JOBTITLEREF;
	private static final String SELECT_JOBTITLEREF_BY_HIST;
	private static final String SELECT_AUTHNAME;
	private static final String SELECT_AUTHLEVEL;

	static {

		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtJobTitleRef e");
		builderString.append(" WHERE e.cmnmtJobTitleRefPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtJobTitleRefPK.historyId = :historyId");
		builderString.append(" AND e.cmnmtJobTitleRefPK.jobCode = :jobCode");
		SELECT_JOBTITLEREF = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtJobTitleRef e");
		builderString.append(" WHERE e.cmnmtJobTitleRefPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtJobTitleRefPK.historyId = :historyId");
		SELECT_JOBTITLEREF_BY_HIST = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CatmtAuth e");
		builderString.append(" WHERE e.catmtAuthPK.companyCode = :companyCode");
		builderString.append(" AND e.catmtAuthPK.authScopeAtr = :authScopeAtr");
		SELECT_AUTHLEVEL = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a.catmtAuthPk.authCode, a.authName, coalesce(b.referenceSettings,0) referenceSettings");
		builderString.append(" FROM CatmtAuth a LEFT JOIN CmnmtJobTitleRef b");
		builderString.append(" ON a.catmtAuthPk.authCode = b.cmnmtJobTitleRefPK.authCode");
		builderString.append(" AND a.catmtAuthPk.companyCode = b.cmnmtJobTitleRefPK.companyCode");
		builderString.append(" AND a.catmtAuthPk.authScopeAtr = :authScopeAtr");
		builderString.append(" AND a.catmtAuthPk.companyCode = :companyCode");
		builderString.append(" AND b.cmnmtJobTitleRefPK.jobCode = :jobCode");
		builderString.append(" AND b.cmnmtJobTitleRefPK.historyId = :historyId");
		SELECT_AUTHNAME = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtJobTitle e");
		builderString.append(" WHERE e.cmnmtJobTitlePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtJobTitlePK.historyId = :historyId");
		SELECT_ALL_POSITION = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtJobHist e");
		builderString.append(" WHERE e.cmnmtJobHistPK.companyCode = :companyCode");
		builderString.append(" ORDER BY e.endDate DESC");
		SELECT_ALL_HISTORY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtJobTitle e");
		builderString.append(" WHERE e.cmnmtJobTitlePK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtJobTitlePK.historyId = :historyId");
		builderString.append(" AND e.cmnmtJobTitlePK.jobCode = :jobCode");
		FIND_SINGLE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtJobHist e");
		builderString.append(" WHERE e.cmnmtJobHistPK.companyCode = :companyCode");
		builderString.append(" AND e.cmnmtJobHistPK.historyId = :historyId");
		FIND_SINGLE_HISTORY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM CmnmtJobHist e");
		builderString.append(" WHERE e.cmnmtJobHistPK.companyCode = :companyCode");
		builderString.append(" AND e.endDate = :endDate");
		builderString.append(" ORDER BY e.endDate DESC");
		SELECT_HISTORY_BY_END_DATE = builderString.toString();
	}

	private JobTitle convertToDomain(CmnmtJobTitle cmnmtJobTittle) {
		JobTitle jobTittle = JobTitle.createFromJavaType(
				cmnmtJobTittle.getCmnmtJobTitlePK().getCompanyCode(),
				cmnmtJobTittle.getCmnmtJobTitlePK().getHistoryId(), 
				cmnmtJobTittle.getCmnmtJobTitlePK().getJobCode(),
				cmnmtJobTittle.getJobName(), 
				cmnmtJobTittle.getPresenceCheckScopeSet(),
				cmnmtJobTittle.getJobOutCode(),
				cmnmtJobTittle.getMemo(),
				cmnmtJobTittle.getHierarchyOrderCode());
				
		return jobTittle;
	}

	private JobHistory convertToDomainHist(CmnmtJobHist cmnmtJobTitleHistory) {
		JobHistory jobHistory = JobHistory.createFromJavaType(
				cmnmtJobTitleHistory.getCmnmtJobHistPK().getCompanyCode(),
				cmnmtJobTitleHistory.getCmnmtJobHistPK().getHistoryId(), 
				cmnmtJobTitleHistory.getStartDate(),
				cmnmtJobTitleHistory.getEndDate());
		return jobHistory;
	}
	
	private JobTitleRef convertToDomainRef(CmnmtJobTitleRef cmnmtJobTitleRef) {
		JobTitleRef jobTitleRef = JobTitleRef.createFromJavaType(
				cmnmtJobTitleRef.getCmnmtJobTitleRefPK().getCompanyCode(),
				cmnmtJobTitleRef.getCmnmtJobTitleRefPK().getHistoryId(),
				cmnmtJobTitleRef.getCmnmtJobTitleRefPK().getJobCode(),
				cmnmtJobTitleRef.getCmnmtJobTitleRefPK().getAuthCode(),
				cmnmtJobTitleRef.getReferenceSettings());
		return jobTitleRef;
	}

	private CmnmtJobTitle convertToDbType(JobTitle position) {
		CmnmtJobTitle cmnmtJobTitle = new CmnmtJobTitle();
		CmnmtJobTitlePK cmnmtJobTitlePK = new CmnmtJobTitlePK(
				position.getCompanyCode(),
				position.getHistoryId(),
				position.getJobCode().v());
		cmnmtJobTitle.setJobName(position.getJobName().v());
		cmnmtJobTitle.setPresenceCheckScopeSet(position.getPresenceCheckScopeSet().value);
		cmnmtJobTitle.setJobOutCode(position.getJobOutCode().v());
		cmnmtJobTitle.setMemo(position.getMemo().v());
		cmnmtJobTitle.setHierarchyOrderCode(position.getHiterarchyOrderCode().v());
		cmnmtJobTitle.setCmnmtJobTitlePK(cmnmtJobTitlePK);
		return cmnmtJobTitle;
	}

	private CmnmtJobHist convertToDbTypeHist(JobHistory positionHistory) {
		CmnmtJobHist cmnmtJobTitleHistory = new CmnmtJobHist();
		CmnmtJobHistPK cmnmtJobTitleHistoryPK = new CmnmtJobHistPK(
				positionHistory.getCompanyCode(),
				positionHistory.getHistoryId());
		cmnmtJobTitleHistory.setStartDate(positionHistory.getStartDate());
		cmnmtJobTitleHistory.setEndDate(positionHistory.getEndDate());
		cmnmtJobTitleHistory.setCmnmtJobHistPK(cmnmtJobTitleHistoryPK);
		return cmnmtJobTitleHistory;
	}

	private CmnmtJobTitleRef convertToDbTypeRef(JobTitleRef jobTitleRef) {
		CmnmtJobTitleRef cmnmtJobTitleRef = new CmnmtJobTitleRef();
		CmnmtJobTitleRefPK cmnmtJobTitleRefPK = new CmnmtJobTitleRefPK(
				jobTitleRef.getCompanyCode(),
				jobTitleRef.getHistoryId(),
				jobTitleRef.getJobCode().v(), 
				jobTitleRef.getAuthCode().v());
		cmnmtJobTitleRef.setReferenceSettings(jobTitleRef.getReferenceSettings().value);
		cmnmtJobTitleRef.setCmnmtJobTitleRefPK(cmnmtJobTitleRefPK);
		return cmnmtJobTitleRef;
	}
	
	/**
	 * Add Job Title
	 */
	@Override
	public void add(JobTitle position) {
		this.commandProxy().insert(convertToDbType(position));
	}
	/**
	 * Update Job Title
	 */
	@Override
	public void update(JobTitle position) {
		this.commandProxy().update(convertToDbType(position));
	}
	/**
	 * Delete Job Title Select
	 */
	@Override
	public void delete(String companyCode, String historyId, JobCode jobCode) {
		CmnmtJobTitlePK cmnmtClassPK = new CmnmtJobTitlePK(companyCode, historyId, jobCode.toString());
		this.commandProxy().remove(CmnmtJobTitle.class, cmnmtClassPK);
	}
	/**
	 * Find All Job Title
	 */
	@Override
	public List<JobTitle> findAllJobTitle(String companyCode, String historyId) {
		return this.queryProxy().query(SELECT_ALL_POSITION, CmnmtJobTitle.class)
				.setParameter("companyCode", companyCode).setParameter("historyId", historyId)
				.getList(c -> convertToDomain(c));
	}
	/**
	 * Get all History
	 */
	@Override
	public List<JobHistory> getAllHistory(String companyCode) {

		return this.queryProxy().query(SELECT_ALL_HISTORY, CmnmtJobHist.class).setParameter("companyCode", companyCode)
				.getList(c -> convertToDomainHist(c));
	}
	/**
	 * Delete History
	 */
	@Override
	public void deleteHist(String companyCode, String historyId) {
		CmnmtJobHistPK cmnmtJobTitleHistoryPK = new CmnmtJobHistPK(companyCode, historyId);
		this.commandProxy().remove(CmnmtJobHist.class, cmnmtJobTitleHistoryPK);
	}
	/**
	 * Add History
	 */
	@Override
	public void addHistory(JobHistory history) {
		this.commandProxy().insert(convertToDbTypeHist(history));
	}
	/**
	 * Update History
	 */
	@Override
	public void updateHistory(JobHistory history) {
		this.commandProxy().update(convertToDbTypeHist(history));

	}
	/**
	 * Find single History
	 */
	@Override
	public Optional<JobHistory> findSingleHistory(String companyCode, String historyId) {
		return this.queryProxy().query(FIND_SINGLE_HISTORY, CmnmtJobHist.class)
				.setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId).getSingle().map(e -> {
					return Optional.of(convertToDomainHist(e));
				}).orElse(Optional.empty());
	}
	/**
	 * Get History By End Date
	 */
	@Override
	public Optional<JobHistory> getHistoryByEdate(String companyCode, GeneralDate endDate) {

		return this.queryProxy().query(SELECT_HISTORY_BY_END_DATE, CmnmtJobHist.class)
				.setParameter("companyCode", companyCode)
				.setParameter("endDate", endDate).getSingle().map(e -> {
					return Optional.of(convertToDomainHist(e));
				}).orElse(Optional.empty());
	}
	/**
	 * 
	 */
	@Override
	public List<JobTitleRef> findAllJobTitleRef(String companyCode, String historyId, String jobCode) {
		return this.queryProxy().query(SELECT_JOBTITLEREF, CmnmtJobTitleRef.class)
				.setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId)
				.setParameter("jobCode", jobCode).getList(c -> convertToDomainRef(c));
	}
	/**
	 * 
	 */
	@Override
	public List<JobTitleRef> findAllJobTitleRefByJobHist(String companyCode, String historyId) {
		return this.queryProxy().query(SELECT_JOBTITLEREF_BY_HIST, CmnmtJobTitleRef.class)
				.setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId)
				.getList(c -> convertToDomainRef(c));
	}
	/**
	 * Get JobTitle By Code
	 */
	@Override
	public Optional<JobTitle> getJobTitleByCode(String companyCode, String historyId, String jobCode) {
		return this.queryProxy().query(FIND_SINGLE, CmnmtJobTitle.class)
				.setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId)
				.setParameter("jobCode", jobCode).getSingle().map(c -> {
					return Optional.of(convertToDomain(c));
				}).orElse(Optional.empty());
	}

	private CmnmtJobTitlePK toEntityTitlePk(JobTitle domain) {
		val entityPk = new CmnmtJobTitlePK();
		entityPk.companyCode = domain.getCompanyCode();
		entityPk.historyId = domain.getHistoryId();
		entityPk.jobCode = domain.getJobCode().toString();
		return entityPk;
	}
	/**
	 * Delete JobTitle By HistoryId
	 */
	@Override
	public void deleteJobTitleByHisId(String companyCode, String historyId) {
		List<JobTitle> lstAllJobByHisId = this.findAllJobTitle(companyCode, historyId);
		List<CmnmtJobTitlePK> detailEntitiesPk = lstAllJobByHisId.stream().map(detail -> {
			return this.toEntityTitlePk(detail);
		}).collect(Collectors.toList());
		this.commandProxy().removeAll(CmnmtJobTitle.class, detailEntitiesPk);

	}
	/**
	 * Get All Auth
	 */
	@Override
	public List<JobRefAuth> getAllAuth(String companyCode, String historyId, String jobCode, String authScopeAtr) {
		return this.queryProxy().query(SELECT_AUTHNAME, Object[].class)
				.setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId).setParameter("jobCode", jobCode)
				.setParameter("authScopeAtr", authScopeAtr).getList(c -> {
					String authCode = (String) c[0];
					String authName = (String) c[1];
					int referenceSettings = Integer.valueOf(c[2].toString());
					return JobRefAuth.createSimpleFromJavaType(authCode, authName, referenceSettings);
				});
	}

	private CmnmtJobTitleRefPK toEntityTitlePkRef(JobTitleRef domain) {
		val entityPk = new CmnmtJobTitleRefPK();
		entityPk.companyCode = domain.getCompanyCode();
		entityPk.historyId = domain.getHistoryId();
		entityPk.jobCode = domain.getJobCode().toString();
		entityPk.authCode = domain.getAuthCode().toString();
		return entityPk;
	}
	/**
	 * Delete JobTitle Ref
	 */
	public void deleteJobTitleRef(String companyCode, String historyId,String jobCode) {
		List<JobTitleRef> newRefInfor = this.findAllJobTitleRef(companyCode, historyId, jobCode);
		List<CmnmtJobTitleRefPK> cmnmtJobTitleRefPK = newRefInfor.stream().map(detail -> {
			return this.toEntityTitlePkRef(detail);
		}).collect(Collectors.toList());
		this.commandProxy().removeAll(CmnmtJobTitleRef.class, cmnmtJobTitleRefPK);
	}
	/**
	 * Delete JobTitle Ref By JobHist
	 */
	public void deleteJobTitleRefByJobHist(String companyCode, String historyId) {
		List<JobTitleRef> newRefInfor = this.findAllJobTitleRefByJobHist(companyCode, historyId);
		List<CmnmtJobTitleRefPK> cmnmtJobTitleRefPK = newRefInfor.stream().map(detail -> {
			return this.toEntityTitlePkRef(detail);
		}).collect(Collectors.toList());
		this.commandProxy().removeAll(CmnmtJobTitleRef.class, cmnmtJobTitleRefPK);
	}
	/**
	 * Add List JobTitle
	 */
	@Override
	public void add(List<JobTitle> lstPositionNow) {
		this.commandProxy().insertAll(lstPositionNow.stream().map(item -> {
			return convertToDbType(item);
		}).collect(Collectors.toList()));

	}
	/**
	 * Add JobTitle Ref
	 */
	@Override
	public void addJobTitleRef(List<JobTitleRef> jobTitleRef) {
		this.commandProxy().insertAll(jobTitleRef.stream().map(item -> {
			return convertToDbTypeRef(item);
		}).collect(Collectors.toList()));
	}
	/**
	 * Update JobTitle Ref
	 */
	@Override
	public void updateRef(List<JobTitleRef> refInfor) {
		this.commandProxy().updateAll(refInfor.stream().map(item -> {
			return convertToDbTypeRef(item);
		}).collect(Collectors.toList()));
	}
	
	private static AuthorizationLevel toDomainAuth(CatmtAuth entity) {
		val domain = AuthorizationLevel.createFromJavaType(entity.catmtAuthPk.companyCode,
				entity.catmtAuthPk.authScopeAtr, entity.catmtAuthPk.authCode, entity.authName, entity.empScopeAtr,
				entity.inChargeAtr, entity.memo);
		return domain;
	}
	/**
	 * Find All Auth
	 */
	@Override
	public Optional<AuthorizationLevel> findAuth(String companyCode,String authScopeAtr) {
		return this.queryProxy().query(SELECT_AUTHLEVEL, CatmtAuth.class)
				.setParameter("authScopeAtr", authScopeAtr)
				.getSingle(c -> toDomainAuth(c));
	}

	@Override
	public Optional<JobTitleRef> findSingleJobTitleRef(String companyCode, String historyId, String jobCode) {
		return this.queryProxy().query(SELECT_JOBTITLEREF, CmnmtJobTitleRef.class)
				.setParameter("companyCode", companyCode)
				.setParameter("historyId", historyId)
				.setParameter("jobCode", jobCode).getSingle(c -> convertToDomainRef(c));
	}
}
