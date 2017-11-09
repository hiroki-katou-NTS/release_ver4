/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pubimp.jobtitle;
/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employeeinfo.Employee;
import nts.uk.ctx.bs.employee.dom.employeeinfo.EmployeeRepository;
import nts.uk.ctx.bs.employee.dom.jobtile.affiliate.AffJobTitleHistory;
import nts.uk.ctx.bs.employee.dom.jobtile.affiliate.AffJobTitleHistoryRepository;
import nts.uk.ctx.bs.employee.dom.jobtitle.JobTitle;
import nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository;
import nts.uk.ctx.bs.employee.dom.jobtitle.history.JobTitleHistory;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfo;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository;
import nts.uk.ctx.bs.employee.pub.jobtitle.JobTitleExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.SyJobTitlePub;

/**
 * The Class JobTitlePubImp.
 */
@Stateless
public class JobTitlePubImp implements SyJobTitlePub {

	/** The first item index. */
	private final int FIRST_ITEM_INDEX = 0;

	/** The employee repository. */
	@Inject
	private EmployeeRepository employeeRepository;

	/** The job title repository. */
	@Inject
	private JobTitleInfoRepository jobTitleInfoRepository;

	/** The job title repository. */
	@Inject
	private JobTitleRepository jobTitleRepository;

	/** The job title history repository. */
	@Inject
	private AffJobTitleHistoryRepository jobTitleHistoryRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.employee.EmployeePub#findJobTitleBySid(String)
	 */
	@Override
	public List<JobTitleExport> findJobTitleBySid(String employeeId) {
		// Query
		List<AffJobTitleHistory> affJobTitleHistories = this.jobTitleHistoryRepository
				.findBySid(employeeId);

		Employee employee = employeeRepository.getBySid(employeeId).get();

		// Return
		return affJobTitleHistories.stream().map(item -> {
			JobTitleInfo jobTitleInfo = this.jobTitleInfoRepository.find(employee.getCompanyId(),
					item.getJobTitleId().v(), item.getPeriod().start()).get();
			return JobTitleExport.builder().companyId(jobTitleInfo.getCompanyId().v())
					.jobTitleId(jobTitleInfo.getJobTitleId())
					.jobTitleCode(jobTitleInfo.getJobTitleCode().v())
					.jobTitleName(jobTitleInfo.getJobTitleName().v())
					.sequenceCode(jobTitleInfo.getSequenceCode().v())
					.startDate(item.getPeriod().start()).endDate(item.getPeriod().end()).build();
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.employee.EmployeePub#findJobTitleBySid(java.
	 * lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<JobTitleExport> findBySid(String employeeId, GeneralDate baseDate) {
		// Query
		Optional<AffJobTitleHistory> optAffJobTitleHist = this.jobTitleHistoryRepository
				.findBySid(employeeId, baseDate);

		if (!optAffJobTitleHist.isPresent()) {
			return Optional.empty();
		}

		AffJobTitleHistory affJobTitleHist = optAffJobTitleHist.get();

		Employee employee = this.employeeRepository.getBySid(employeeId).get();

		// Get infos
		JobTitleInfo jobInfo = this.jobTitleInfoRepository
				.find(employee.getCompanyId(), affJobTitleHist.getJobTitleId().v(), baseDate).get();

		// Return
		return Optional.of(JobTitleExport.builder().companyId(jobInfo.getCompanyId().v())
				.jobTitleId(jobInfo.getJobTitleId()).jobTitleCode(jobInfo.getJobTitleCode().v())
				.jobTitleName(jobInfo.getJobTitleName().v())
				.sequenceCode(
						jobInfo.getSequenceCode() != null ? jobInfo.getSequenceCode().v() : null)
				.startDate(affJobTitleHist.getPeriod().start())
				.endDate(affJobTitleHist.getPeriod().end()).build());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.pub.employee.jobtitle.SyJobTitlePub#
	 * findJobTitleByJobTitleId(java.lang.String, java.lang.String,
	 * nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<JobTitleExport> findByJobId(String companyId, String jobTitleId,
			GeneralDate baseDate) {

		// Query
		JobTitleInfo jobInfo = this.jobTitleInfoRepository.find(companyId, jobTitleId, baseDate)
				.get();

		JobTitle jobTitle = this.jobTitleRepository
				.findByHistoryId(companyId, jobInfo.getJobTitleHistoryId()).get();

		JobTitleHistory jobTitleHistory = jobTitle.getJobTitleHistory().get(FIRST_ITEM_INDEX);

		// Return
		return Optional.of(JobTitleExport.builder().companyId(jobInfo.getCompanyId().v())
				.jobTitleId(jobInfo.getJobTitleId()).jobTitleCode(jobInfo.getJobTitleCode().v())
				.jobTitleName(jobInfo.getJobTitleName().v())
				.sequenceCode(
						jobInfo.getSequenceCode() != null ? jobInfo.getSequenceCode().v() : null)
				.startDate(jobTitleHistory.span().start())
				.endDate(jobTitleHistory.span().end())
				.build());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.jobtitle.SyJobTitlePub#findByBaseDate(java.
	 * lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<JobTitleExport> findAll(String companyId, GeneralDate baseDate) {
		// Query
		List<JobTitleInfo> jobInfos = this.jobTitleInfoRepository.findAll(companyId, baseDate);

		// Return
		return jobInfos.stream().map(jobInfo -> {
			JobTitle jobTitle = this.jobTitleRepository
					.findByHistoryId(companyId, jobInfo.getJobTitleHistoryId()).get();
			JobTitleHistory jobTitleHistory = jobTitle.getJobTitleHistory().get(FIRST_ITEM_INDEX);
			return JobTitleExport.builder().companyId(jobInfo.getCompanyId().v())
					.jobTitleId(jobInfo.getJobTitleId()).jobTitleCode(jobInfo.getJobTitleCode().v())
					.jobTitleName(jobInfo.getJobTitleName().v())
					.sequenceCode(jobInfo.getSequenceCode() != null ? jobInfo.getSequenceCode().v()
							: null)
					.startDate(jobTitleHistory.span().start())
					.endDate(jobTitleHistory.span().end())
					.build();
		}).collect(Collectors.toList());
	}

}
