/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pubimp.jobtitle;
/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.jobtitle.JobTitle;
import nts.uk.ctx.bs.employee.dom.jobtitle.JobTitleRepository;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistory;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryRepository;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1.AffJobTitleHistoryItem_ver1;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1.AffJobTitleHistoryItemRepository_ver1;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1.AffJobTitleHistoryRepository_ver1;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1.AffJobTitleHistory_ver1;
import nts.uk.ctx.bs.employee.dom.jobtitle.history.JobTitleHistory;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfo;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository;
import nts.uk.ctx.bs.employee.dom.jobtitle.sequence.SequenceCode;
import nts.uk.ctx.bs.employee.dom.jobtitle.sequence.SequenceMaster;
import nts.uk.ctx.bs.employee.dom.jobtitle.sequence.SequenceMasterRepository;
import nts.uk.ctx.bs.employee.pub.jobtitle.EmployeeJobHistExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.JobTitleExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.SimpleJobTitleExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.SyJobTitlePub;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;

/**
 * The Class JobTitlePubImp.
 */
@Stateless
public class JobTitlePubImp implements SyJobTitlePub {

	/** The first item index. */
	private final int FIRST_ITEM_INDEX = 0;

	/** The job title repository. */
	@Inject
	private JobTitleInfoRepository jobTitleInfoRepository;

	/** The job title repository. */
	@Inject
	private JobTitleRepository jobTitleRepository;

	/** The job title history repository. */
	@Inject
	private AffJobTitleHistoryRepository jobTitleHistoryRepository;
	
	/** The sequence master repository. */
	@Inject
	private SequenceMasterRepository sequenceMasterRepository;
	
	@Inject
	private AffJobTitleHistoryRepository_ver1 affJobTitleHisRepo_ver1;
	
	@Inject
	private AffJobTitleHistoryItemRepository_ver1 affJobTitleHisItemRepo_ver1;

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

		String companyId = AppContexts.user().companyId();

		// Return
		return affJobTitleHistories.stream().map(item -> {
			JobTitleInfo jobTitleInfo = this.jobTitleInfoRepository.find(companyId,
					item.getJobTitleId().v(), item.getPeriod().start()).get();
			return JobTitleExport.builder().companyId(jobTitleInfo.getCompanyId().v())
					.jobTitleId(jobTitleInfo.getJobTitleId())
					.jobTitleCode(jobTitleInfo.getJobTitleCode().v())
					.jobTitleName(jobTitleInfo.getJobTitleName().v())
					.sequenceCode(jobTitleInfo.getSequenceCode().v())
					.startDate(item.getPeriod().start()).endDate(item.getPeriod().end()).build();
		}).collect(Collectors.toList());
	}

	@Override
	public Optional<EmployeeJobHistExport> findBySid(String employeeId, GeneralDate baseDate) {
		// Query
		Optional<AffJobTitleHistory_ver1> optAffJobTitleHist = this.affJobTitleHisRepo_ver1
				.getByEmpIdAndStandardDate(employeeId, baseDate);

		if (optAffJobTitleHist.isPresent()) {

			DateHistoryItem dateHistoryItem = optAffJobTitleHist.get().getHistoryItems().get(0);

			AffJobTitleHistoryItem_ver1 affJobTitleHistItem = affJobTitleHisItemRepo_ver1
					.findByHitoryId(dateHistoryItem.identifier()).get();

			// Get information of employee
			String companyId = AppContexts.user().companyId();

			List<SimpleJobTitleExport> simpleJobTitleExports = findByIds(companyId,
					Arrays.asList(affJobTitleHistItem.getJobTitleId()), baseDate);
			
			if ( !simpleJobTitleExports.isEmpty()) {
				SimpleJobTitleExport simpleJobTitleExport = simpleJobTitleExports.get(0);
				EmployeeJobHistExport jobTitleExport = EmployeeJobHistExport.builder()
						.employeeId(employeeId)
						.jobTitleID(simpleJobTitleExport.getJobTitleId())
						.jobTitleName(simpleJobTitleExport.getJobTitleName())
						.startDate(dateHistoryItem.start())
						.endDate(dateHistoryItem.end())
						.build();
				// Return
				return Optional.of(jobTitleExport);
			}
		}

		return Optional.empty();
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

		JobTitleHistory jobTitleHistory = jobTitle.getJobTitleHistories().get(FIRST_ITEM_INDEX);

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
			JobTitleHistory jobTitleHistory = jobTitle.getJobTitleHistories().get(FIRST_ITEM_INDEX);
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

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.pub.jobtitle.SyJobTitlePub#findSJobHistBySId(java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<EmployeeJobHistExport> findSJobHistBySId(String employeeId, GeneralDate baseDate) {
		// Query 
		Optional<AffJobTitleHistory> optAffJobTitleHistory = jobTitleHistoryRepository.findBySid(employeeId, baseDate);
		
		// Check exist
		if(!optAffJobTitleHistory.isPresent()) {
			return Optional.empty();
		}
		
		AffJobTitleHistory affJobTitleHist =  optAffJobTitleHistory.get();
		
		// Query 
		Optional<JobTitleInfo>  optJobTitleInfo = this.jobTitleInfoRepository.find(affJobTitleHist.getJobTitleId().v(), baseDate);

		// Check exist		
		if(!optJobTitleInfo.isPresent()) {
			return Optional.empty();
		}

		JobTitleInfo jobTitleInfo = optJobTitleInfo.get();
		
		// Return
		return Optional.of(EmployeeJobHistExport.builder().
				employeeId(affJobTitleHist.getEmployeeId())
				.jobTitleID(jobTitleInfo.getJobTitleId())
				.jobTitleName(jobTitleInfo.getJobTitleName().v())
				.startDate(affJobTitleHist.getPeriod().start())
				.endDate(affJobTitleHist.getPeriod().end()).build());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.jobtitle.SyJobTitlePub#findByIds(java.lang.
	 * String, java.util.List, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<SimpleJobTitleExport> findByIds(String companyId, List<String> jobIds,
			GeneralDate baseDate) {
		// Query infos
		List<JobTitleInfo> jobTitleInfos = this.jobTitleInfoRepository.findByIds(companyId, jobIds, baseDate);
		
		List<SequenceMaster> seqMasters = this.sequenceMasterRepository.findByCompanyId(companyId);
		
		Map<SequenceCode, Integer> seqMasterMap = seqMasters.stream().collect(Collectors.toMap(SequenceMaster::getSequenceCode, SequenceMaster::getOrder));
		
		// Return
		return jobTitleInfos.stream()
				.map(item -> SimpleJobTitleExport.builder().jobTitleId(item.getJobTitleId())
						.jobTitleCode(item.getJobTitleCode().v())
						.jobTitleName(item.getJobTitleName().v())
						.disporder(seqMasterMap.get(item.getSequenceCode())).build())
				.collect(Collectors.toList());
	}

}
