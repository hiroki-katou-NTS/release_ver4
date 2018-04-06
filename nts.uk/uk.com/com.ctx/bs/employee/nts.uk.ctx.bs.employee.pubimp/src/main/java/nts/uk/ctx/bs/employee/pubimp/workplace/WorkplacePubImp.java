/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pubimp.workplace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHist;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistByEmployee;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistItem;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistRepository;
import nts.uk.ctx.bs.employee.dom.workplace.Workplace;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItem;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceHierarchy;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.ctx.bs.employee.pub.workplace.AffAtWorkplaceExport;
import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.ctx.bs.employee.pub.workplace.WkpCdNameExport;
import nts.uk.ctx.bs.employee.pub.workplace.WkpConfigAtTimeExport;
import nts.uk.ctx.bs.employee.pub.workplace.WorkPlaceHistExport;
import nts.uk.ctx.bs.employee.pub.workplace.WorkPlaceIdAndPeriod;
import nts.uk.ctx.bs.employee.pub.workplace.WorkPlaceInfoExport;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class WorkplacePubImp.
 */
@Stateless
public class WorkplacePubImp implements SyWorkplacePub {

	/** The workplace config info repo. */
	@Inject
	private WorkplaceConfigInfoRepository workplaceConfigInfoRepo;

	/** The workplace info repo. */
	@Inject
	private WorkplaceInfoRepository workplaceInfoRepo;

	/** The aff workplace history repository. */
	@Inject
	private AffWorkplaceHistoryRepository affWorkplaceHistoryRepository;

	/** The aff workplace history item repository. */
	@Inject
	private AffWorkplaceHistoryItemRepository affWorkplaceHistoryItemRepository;

	/** The wkp config repository. */
	@Inject
	private WorkplaceConfigRepository wkpConfigRepository;

	/** The wkp config info repo. */
	@Inject
	private WorkplaceConfigInfoRepository wkpConfigInfoRepo;

	/** The aff company hist repo. */
	@Inject
	private AffCompanyHistRepository affCompanyHistRepo;

	/** The workplace repo. */
	@Inject
	private WorkplaceRepository workplaceRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.workplace.WorkplacePub#findWpkIds(java.lang.
	 * String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findWpkIdsByWkpCode(String companyId, String wpkCode, GeneralDate baseDate) {
		return workplaceInfoRepo.findByWkpCd(companyId, wpkCode, baseDate).stream().map(item -> item.getWorkplaceId())
				.collect(Collectors.toList());
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
		Optional<WorkplaceInfo> optWorkplaceInfo = workplaceInfoRepo.findByWkpId(workplaceId, baseDate);

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
		Optional<AffWorkplaceHistory> affWrkPlc = affWorkplaceHistoryRepository.getByEmpIdAndStandDate(employeeId,
				baseDate);
		if (!affWrkPlc.isPresent())
			return null;
		String historyId = affWrkPlc.get().getHistoryItems().get(0).identifier();
		Optional<AffWorkplaceHistoryItem> affWrkPlcItem = affWorkplaceHistoryItemRepository.getByHistId(historyId);
		if (affWrkPlcItem.isPresent())
			return affWrkPlcItem.get().getWorkplaceId();

		return null;
		// // Query
		// List<AffWorkplaceHistory> affWorkplaceHistories =
		// workplaceHistoryRepo
		// .searchWorkplaceHistoryByEmployee(employeeId, baseDate);
		//
		// List<String> wkpIds = affWorkplaceHistories.stream().map(item ->
		// item.getWorkplaceId().v())
		// .collect(Collectors.toList());
		//
		// // Check exist
		// if (CollectionUtil.isEmpty(wkpIds)) {
		// return null;
		// }
		//
		// // Return workplace id
		// return wkpIds.get(FIRST_ITEM_INDEX);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#findListSIdByCidAndWkpIdAndPeriod(java.lang.String, nts.arc.time.GeneralDate, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findListSIdByCidAndWkpIdAndPeriod(String workplaceId, GeneralDate startDate,
			GeneralDate endDate) {

		List<String> listSid = affWorkplaceHistoryRepository.getByWplIdAndPeriod(workplaceId, startDate, endDate);

		List<String> result = new ArrayList<>();

		if (!CollectionUtil.isEmpty(listSid)) {

			listSid.forEach(sid -> {

				AffCompanyHist affCompanyHist = affCompanyHistRepo
						.getAffCompanyHistoryOfEmployee(AppContexts.user().companyId(), sid);

				AffCompanyHistByEmployee affCompanyHistByEmp = affCompanyHist.getAffCompanyHistByEmployee(sid);

				List<AffCompanyHistItem> listAffComHisItem = affCompanyHistByEmp.getLstAffCompanyHistoryItem();

				if (!CollectionUtil.isEmpty(listAffComHisItem)) {
					listAffComHisItem.forEach(m -> {
						if (m.start().beforeOrEquals(startDate) && m.end().afterOrEquals(endDate)) {
							result.add(sid);
						}
					});
				}
			});

			return result;

		} else {
			return Collections.emptyList();
		}
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
		List<AffWorkplaceHistoryItem> items = affWorkplaceHistoryItemRepository
				.getAffWrkplaHistItemByEmpIdAndDate(baseDate, employeeId);

		List<String> lstWpkIds = new ArrayList<>();

		// Get all parent wkp.
		items.stream().forEach(item -> {
			wkpConfigInfoRepo.findAllParentByWkpId(companyId, baseDate, item.getWorkplaceId())
					.ifPresent(wkpConfigInfo -> {
						lstWpkIds.addAll(wkpConfigInfo.getLstWkpHierarchy().stream()
								.map(WorkplaceHierarchy::getWorkplaceId).collect(Collectors.toList()));
					});

			// Include this wkp
			lstWpkIds.add(item.getWorkplaceId());
		});

		// reverse list (child -> parent)
		Collections.reverse(lstWpkIds);

		// Return
		return lstWpkIds.stream().distinct().collect(Collectors.toList());
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
		// get AffWorkplaceHistory
		Optional<AffWorkplaceHistory> affWrkPlc = affWorkplaceHistoryRepository.getByEmpIdAndStandDate(employeeId,
				baseDate);
		if (!affWrkPlc.isPresent())
			return Optional.empty();

		// get AffWorkplaceHistoryItem
		String historyId = affWrkPlc.get().getHistoryItems().get(0).identifier();
		Optional<AffWorkplaceHistoryItem> affWrkPlcItem = affWorkplaceHistoryItemRepository.getByHistId(historyId);
		if (!affWrkPlcItem.isPresent())
			return Optional.empty();

		// Get workplace info.
		Optional<WorkplaceInfo> optWorkplaceInfo = workplaceInfoRepo.findByWkpId(affWrkPlcItem.get().getWorkplaceId(),
				baseDate);

		// Check exist
		if (!optWorkplaceInfo.isPresent()) {
			return Optional.empty();
		}

		// Return workplace id
		WorkplaceInfo wkpInfo = optWorkplaceInfo.get();

		return Optional.of(SWkpHistExport.builder().dateRange(affWrkPlc.get().getHistoryItems().get(0).span())
				.employeeId(affWrkPlc.get().getEmployeeId()).workplaceId(wkpInfo.getWorkplaceId())
				.workplaceCode(wkpInfo.getWorkplaceCode().v()).workplaceName(wkpInfo.getWorkplaceName().v())
				.wkpDisplayName(wkpInfo.getWkpDisplayName().v()).build());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#
	 * findParentWpkIdsByWkpId(java.lang.String, java.lang.String,
	 * nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findParentWpkIdsByWkpId(String companyId, String workplaceId, GeneralDate date) {
		// Get config info
		Optional<WorkplaceConfigInfo> optWorkplaceConfigInfo = workplaceConfigInfoRepo.findAllParentByWkpId(companyId,
				date, workplaceId);

		// Check exist
		if (!optWorkplaceConfigInfo.isPresent()) {
			return Collections.emptyList();
		}

		// Return
		return optWorkplaceConfigInfo.get().getLstWkpHierarchy().stream().map(WorkplaceHierarchy::getWorkplaceId)
				.collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#findListWorkplaceIdByBaseDate(nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findListWorkplaceIdByBaseDate(GeneralDate baseDate) {

		// get all WorkplaceConfigInfo with StartDate
		String companyId = AppContexts.user().companyId();

		Optional<WorkplaceConfig> optionalWkpConfig = wkpConfigRepository.findByBaseDate(companyId, baseDate);
		if (!optionalWkpConfig.isPresent()) {
			return null;
		}
		WorkplaceConfig wkpConfig = optionalWkpConfig.get();
		String historyId = wkpConfig.getWkpConfigHistoryLatest().identifier();

		Optional<WorkplaceConfigInfo> opWkpConfigInfo = wkpConfigInfoRepo.find(companyId, historyId);
		if (!opWkpConfigInfo.isPresent()) {
			return Collections.emptyList();
		}

		return opWkpConfigInfo.get().getLstWkpHierarchy().stream()
				.map(workplaceConfigInfo -> workplaceConfigInfo.getWorkplaceId()).collect(Collectors.toList());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#
	 * findListWorkplaceIdByCidAndWkpIdAndBaseDate(java.lang.String,
	 * java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findListWorkplaceIdByCidAndWkpIdAndBaseDate(String companyId, String workplaceId,
			GeneralDate baseDate) {

		Optional<WorkplaceConfigInfo> wkpConfigInfo = workplaceConfigInfoRepo.findAllByParentWkpId(companyId, baseDate,
				workplaceId);

		List<WorkplaceHierarchy> listWkpHierachy = wkpConfigInfo.get().getLstWkpHierarchy();

		return listWkpHierachy.stream().map(wkpHierachy -> wkpHierachy.getWorkplaceId()).collect(Collectors.toList());

	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#GetWplByListSidAndPeriod(java.util.List, nts.uk.shr.com.time.calendar.period.DatePeriod)
	 */
	@Override
	public List<WorkPlaceHistExport> GetWplByListSidAndPeriod(List<String> sids, DatePeriod datePeriod) {

		if (sids.isEmpty() || datePeriod.start() == null || datePeriod.end() == null)
			return null;

		List<AffWorkplaceHistory> lstAffWkpHist = affWorkplaceHistoryRepository.getByListSid(sids);
		if (lstAffWkpHist.isEmpty())
			return null;

		List<WorkPlaceHistExport> result = new ArrayList<>();

		lstAffWkpHist.forEach(affWkp -> {
			WorkPlaceHistExport workPlaceHistExport = new WorkPlaceHistExport();

			workPlaceHistExport.setEmployeeId(affWkp.getEmployeeId());

			if (!affWkp.getHistoryItems().isEmpty()) {
				workPlaceHistExport.setLstWkpIdAndPeriod(getLstWkpIdAndPeriod(affWkp, datePeriod));
			}

			result.add(workPlaceHistExport);
		});

		return result;
	}

	/**
	 * Gets the lst wkp id and period.
	 *
	 * @param affWkp the aff wkp
	 * @param datePeriod the date period
	 * @return the lst wkp id and period
	 */
	private List<WorkPlaceIdAndPeriod> getLstWkpIdAndPeriod(AffWorkplaceHistory affWkp, DatePeriod datePeriod) {

		List<WorkPlaceIdAndPeriod> result = new ArrayList<>();

		affWkp.getHistoryItems().forEach(itemHist -> {

			WorkPlaceIdAndPeriod workPlaceIdAndPeriod = new WorkPlaceIdAndPeriod();

			boolean check = (itemHist.start().afterOrEquals(datePeriod.start())
					&& itemHist.start().beforeOrEquals(datePeriod.end())
					&& itemHist.end().afterOrEquals(datePeriod.start())
					&& itemHist.end().beforeOrEquals(datePeriod.end()))
					|| (itemHist.start().afterOrEquals(datePeriod.start())
							&& itemHist.start().beforeOrEquals(datePeriod.end())
							&& itemHist.end().after(datePeriod.end()))
					|| (itemHist.end().afterOrEquals(datePeriod.start())
							&& itemHist.end().beforeOrEquals(datePeriod.end())
							&& itemHist.start().before(datePeriod.start()));

			if (check) {
				DatePeriod date = new DatePeriod(itemHist.start(), itemHist.end());

				AffWorkplaceHistoryItem affWkpHisItem = affWorkplaceHistoryItemRepository
						.getByHistId(itemHist.identifier()).get();

				workPlaceIdAndPeriod.setWorkplaceId(affWkpHisItem.getWorkplaceId());

				workPlaceIdAndPeriod.setDatePeriod(date);

				result.add(workPlaceIdAndPeriod);

			}
		});
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#findByWkpIdsAtTime(java.
	 * lang.String, nts.arc.time.GeneralDate, java.util.List)
	 */
	@Override
	public List<WkpConfigAtTimeExport> findByWkpIdsAtTime(String companyId, GeneralDate baseDate, List<String> wkpIds) {

		List<WorkplaceConfigInfo> configInfos = wkpConfigInfoRepo.findByWkpIdsAtTime(companyId, baseDate, wkpIds);

		return configInfos.stream().map(configInfo -> configInfo.getLstWkpHierarchy().get(0))
				.map(wkpHierarchy -> WkpConfigAtTimeExport.builder().workplaceId(wkpHierarchy.getWorkplaceId())
						.hierarchyCd(wkpHierarchy.getHierarchyCode().v()).build())
				.collect(Collectors.toList());
	}

	@Override
	public List<AffAtWorkplaceExport> findBySIdAndBaseDate(List<String> sids, GeneralDate baseDate) {

		List<AffAtWorkplaceExport> result = new ArrayList<AffAtWorkplaceExport>();

		if (sids.isEmpty() || baseDate == null)
			return null;

		List<AffWorkplaceHistory> lstAffWkpHist = affWorkplaceHistoryRepository.getByListSid(sids);
		if (lstAffWkpHist.isEmpty())
			return null;

		List<String> historyIds = new ArrayList<>();

		lstAffWkpHist.stream().forEach(x -> {
			
			List<DateHistoryItem> historyItemList = x.items();
			List<String> hists = new ArrayList<>();
			if (!historyItemList.isEmpty()) {
				hists = historyItemList.stream().filter(m -> {
					return m.end().afterOrEquals(baseDate) && m.start().beforeOrEquals(baseDate);
				}).map( y-> y.identifier()).collect(Collectors.toList());
				
				historyIds.addAll(hists);
			}
			
		});
		
		if(historyIds.isEmpty())
			return null;
		
		List<AffWorkplaceHistoryItem> affWrkPlcItems = affWorkplaceHistoryItemRepository.findByHistIds(historyIds);
		
		return result =  affWrkPlcItems.stream().map(x -> {
			AffAtWorkplaceExport affWkp = new AffAtWorkplaceExport();
			affWkp.setEmployeeId(x.getEmployeeId());
			affWkp.setHistoryID(x.getHistoryId());
			affWkp.setWorkplaceId(x.getWorkplaceId());
			affWkp.setNormalWorkplaceID(x.getNormalWorkplaceId());
			return affWkp;
		}).collect(Collectors.toList());
	}
		
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#findWkpByWkpId(java.lang.String, nts.arc.time.GeneralDate, java.util.List)
	 */
	@Override
	public List<WorkPlaceInfoExport> findWkpByWkpId(String companyId, GeneralDate baseDate, List<String> wkpIds) {

		List<Workplace> workplace = workplaceRepo.findByWkpIds(wkpIds);

		List<Workplace> workplaceList = workplace.stream().filter(m -> {
			return m.getCompanyId().equals(companyId) && m.getWkpHistoryLatest().start().afterOrEquals(baseDate)
					&& m.getWkpHistoryLatest().end().beforeOrEquals(baseDate);
		}).collect(Collectors.toList());
		
		List<String> historyList = new ArrayList<>();
		
		workplaceList.forEach(item -> {
			historyList.add(item.getWkpHistoryLatest().identifier());
		});

		List<WorkplaceInfo> wkpInfors = workplaceInfoRepo.findByHistory(historyList, companyId);

		return wkpInfors.stream().map(item -> WorkPlaceInfoExport.builder().workplaceId(item.getWorkplaceId())
				.workPlaceName(item.getWorkplaceName().v()).build()).collect(Collectors.toList());
	}
}
