/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pubimp.workplace;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.ctx.bs.employee.pub.workplace.WkpCdNameExport;

/**
 * The Class WorkplacePubImp.
 */
@Stateless
public class WorkplacePubImp implements SyWorkplacePub {

	/** The first item index. */
	private final int FIRST_ITEM_INDEX = 0;

	/** The workplace info repository. */
	@Inject
	private WorkplaceInfoRepository workplaceInfoRepository;

	/** The workplace history repository. */
	@Inject
	private AffWorkplaceHistoryRepository workplaceHistoryRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.workplace.WorkplacePub#findWpkIds(java.lang.
	 * String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findWpkIdsByWkpCode(String companyId, String wpkCode,
			GeneralDate baseDate) {
		return workplaceInfoRepository.findByWkpCd(companyId, wpkCode, baseDate).stream()
				.map(item -> item.getWorkplaceId()).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.workplace.WorkplacePub#findByWkpId(java.lang.
	 * String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<WkpCdNameExport> findByWkpId(String workplaceId, GeneralDate baseDate) {
		Optional<WorkplaceInfo> optWorkplaceInfo = workplaceInfoRepository.findByWkpId(workplaceId,
				baseDate);

		// Check exist
		if (!optWorkplaceInfo.isPresent()) {
			return Optional.empty();
		}

		// Return
		WorkplaceInfo wkpInfo = optWorkplaceInfo.get();
		return Optional.of(WkpCdNameExport.builder().wkpCode(wkpInfo.getWorkplaceCode().v())
				.wkpName(wkpInfo.getWorkplaceName().v()).build());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.workplace.WorkplacePub#getWorkplaceId(java.
	 * lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public String getWorkplaceId(String companyId, String employeeId, GeneralDate baseDate) {
		// Query
		List<AffWorkplaceHistory> affWorkplaceHistories = workplaceHistoryRepository
				.searchWorkplaceHistoryByEmployee(employeeId, baseDate);

		List<String> wkpIds = affWorkplaceHistories.stream().map(item -> item.getWorkplaceId().v())
				.collect(Collectors.toList());

		// Check exist
		if (CollectionUtil.isEmpty(wkpIds)) {
			return null;
		}

		// Return workplace id
		return wkpIds.get(FIRST_ITEM_INDEX);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.workplace.WorkplacePub#findWpkIdsBySid(java.
	 * lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findWpkIdsBySid(String companyId, String employeeId, GeneralDate baseDate) {
		// Query
		List<AffWorkplaceHistory> affWorkplaceHistories = workplaceHistoryRepository
				.searchWorkplaceHistoryByEmployee(employeeId, baseDate);

		// Return
		return affWorkplaceHistories.stream().map(item -> {
			return item.getWorkplaceId().v();
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#findBySid(java.lang.
	 * String, nts.arc.time.GeneralDate)
	 */
	@Override
	public Optional<SWkpHistExport> findBySid(String employeeId, GeneralDate baseDate) {
		// Query
		List<AffWorkplaceHistory> affWorkplaceHistories = workplaceHistoryRepository
				.searchWorkplaceHistoryByEmployee(employeeId, baseDate);

		// Check exist
		if (CollectionUtil.isEmpty(affWorkplaceHistories)) {
			return Optional.empty();
		}

		AffWorkplaceHistory affWorkplaceHistory = affWorkplaceHistories.get(FIRST_ITEM_INDEX);

		String wkpId = affWorkplaceHistory.getWorkplaceId().v();

		// Get workplace info.
		Optional<WorkplaceInfo> optWorkplaceInfo = workplaceInfoRepository.findByWkpId(wkpId,
				baseDate);

		// Check exist
		if (!optWorkplaceInfo.isPresent()) {
			return Optional.empty();
		}

		// Return workplace id
		WorkplaceInfo wkpInfo = optWorkplaceInfo.get();
		return Optional.of(SWkpHistExport.builder()
				.workplaceCode(wkpInfo.getWorkplaceCode().v())
				.workplaceName(wkpInfo.getWorkplaceName().v())
				.wkpDisplayName(wkpInfo.getWkpDisplayName().v())
				.build());
	}
}
