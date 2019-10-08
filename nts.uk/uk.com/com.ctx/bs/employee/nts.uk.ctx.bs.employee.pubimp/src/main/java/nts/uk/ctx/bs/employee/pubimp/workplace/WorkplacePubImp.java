/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pubimp.workplace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHist;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistByEmployee;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistItem;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistRepository;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.Workplace;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItem;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistory;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceHierarchy;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.workplace.AffAtWorkplaceExport;
import nts.uk.ctx.bs.employee.pub.workplace.AffWorkplaceExport;
import nts.uk.ctx.bs.employee.pub.workplace.AffWorkplaceHistoryExport;
import nts.uk.ctx.bs.employee.pub.workplace.AffWorkplaceHistoryItemExport;
import nts.uk.ctx.bs.employee.pub.workplace.ResultRequest597Export;
import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;
import nts.uk.ctx.bs.employee.pub.workplace.WkpByEmpExport;
import nts.uk.ctx.bs.employee.pub.workplace.WkpCdNameExport;
import nts.uk.ctx.bs.employee.pub.workplace.WkpConfigAtTimeExport;
import nts.uk.ctx.bs.employee.pub.workplace.WkpHistWithPeriodExport;
import nts.uk.ctx.bs.employee.pub.workplace.WkpIdNameHierarchyCdExport;
import nts.uk.ctx.bs.employee.pub.workplace.WkpInfoExport;
import nts.uk.ctx.bs.employee.pub.workplace.WkpInfoHistExport;
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

	@Inject
	private EmployeeDataMngInfoRepository empDataMngRepo;
	
	@Inject
	private SyEmployeePub subEmp;

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.bs.employee.pub.workplace.WorkplacePub#findWpkIds(java.lang.
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
	 * @see nts.uk.ctx.bs.employee.pub.workplace.WorkplacePub#findByWkpId(java.lang.
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
	 * @see nts.uk.ctx.bs.employee.pub.workplace.WorkplacePub#getWorkplaceId(java.
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

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#
	 * findListSIdByCidAndWkpIdAndPeriod(java.lang.String, nts.arc.time.GeneralDate,
	 * nts.arc.time.GeneralDate)
	 */
	@Override
	public List<AffWorkplaceExport> findListSIdByCidAndWkpIdAndPeriod(String workplaceId, GeneralDate startDate,
			GeneralDate endDate) {

		List<String> listSid = affWorkplaceHistoryRepository.getByWplIdAndPeriod(workplaceId, startDate, endDate);

		if (listSid.isEmpty())
			return new ArrayList<>();

		List<AffCompanyHist> listAffCompanyHist = affCompanyHistRepo.getAffCompanyHistoryOfEmployees(listSid);

		Map<String, AffCompanyHist> mapPidAndAffCompanyHist = listAffCompanyHist.stream()
				.collect(Collectors.toMap(x -> x.getPId(), x -> x));

		List<AffWorkplaceExport> result = new ArrayList<>();

		//EA修正履歴2638 liên quan đến bug #100243, lọc ra những employee không bị xóa
		List<EmployeeDataMngInfo> listEmpDomain = empDataMngRepo.findBySidNotDel(listSid);

		Map<String, String> mapSidPid = listEmpDomain.stream()
				.collect(Collectors.toMap(x -> x.getEmployeeId(), x -> x.getPersonId()));

		listSid.forEach(sid -> {
			AffCompanyHist affCompanyHist = mapPidAndAffCompanyHist.get(mapSidPid.get(sid));
			// check null
			if (affCompanyHist != null) {
				AffCompanyHistByEmployee affCompanyHistByEmp = affCompanyHist.getAffCompanyHistByEmployee(sid);
				// check null
				if (affCompanyHistByEmp != null) {
					List<AffCompanyHistItem> listAffComHisItem = affCompanyHistByEmp.getLstAffCompanyHistoryItem() == null? new ArrayList<>(): affCompanyHistByEmp.getLstAffCompanyHistoryItem();
					if (!CollectionUtil.isEmpty(listAffComHisItem)) {
						listAffComHisItem.forEach(m -> {
							/*
							 * EA修正履歴2059 update RequestList120 【Codition】 param．period．startDate ＜＝
							 * retirementDate AND entrialDate ＜＝ param．period．endDate
							 */
							if (startDate.beforeOrEquals(m.end()) && endDate.afterOrEquals(m.start())) {
								AffWorkplaceExport aff = new AffWorkplaceExport(sid, m.start(), m.end());
								result.add(aff);
							}
						});
					}
				}
			}
		});

		return result;
	}


	@Override
	public List<AffWorkplaceExport> getByLstWkpIdAndPeriod(List<String> lstWkpId, GeneralDate startDate,
			GeneralDate endDate) {
		if (lstWkpId.isEmpty() ||startDate == null  || endDate == null)
			return new ArrayList<>();

		List<EmployeeDataMngInfo> listEmpDomain = empDataMngRepo.findByCompanyId(AppContexts.user().companyId());

		Map<String, String> mapSidPid = listEmpDomain.stream()
				.collect(Collectors.toMap(x -> x.getEmployeeId(), x -> x.getPersonId()));

		List<String> listSid = affWorkplaceHistoryRepository.getByLstWplIdAndPeriod(lstWkpId, startDate, endDate);

		if (listSid.isEmpty())
			return new ArrayList<>();

		List<AffCompanyHist> listAffCompanyHist = new ArrayList<>();

		// vidu listSid = 25100
		if (listSid.size() > 1000) {
			int max = listSid.size() / 1000;
			for (int i = 0; i <= max; i++) {
				if (i != max) {
					ArrayList<String> subListSid = new ArrayList<String>(listSid.subList(i * 1000, i * 1000 + 999));
					List<AffCompanyHist> lstAffCompanyHist = affCompanyHistRepo.getAffCompanyHistoryOfEmployees(subListSid);
					listAffCompanyHist.addAll(lstAffCompanyHist);
				} else {
					ArrayList<String> subListSid = new ArrayList<String>(listSid.subList(max * 1000, listSid.size()));
					List<AffCompanyHist> lstAffCompanyHist = affCompanyHistRepo.getAffCompanyHistoryOfEmployees(subListSid);
					listAffCompanyHist.addAll(lstAffCompanyHist);
				}
			}

		} else {
			listAffCompanyHist = affCompanyHistRepo.getAffCompanyHistoryOfEmployees(listSid);
		}

		Map<String, AffCompanyHist> mapPidAndAffCompanyHist = listAffCompanyHist.stream()
				.collect(Collectors.toMap(x -> x.getPId(), x -> x));

		List<AffWorkplaceExport> result = new ArrayList<>();

		listSid.forEach(sid -> {

			AffCompanyHist affCompanyHist = mapPidAndAffCompanyHist.get(mapSidPid.get(sid));
			if(affCompanyHist != null){
				AffCompanyHistByEmployee affCompanyHistByEmp = affCompanyHist.getAffCompanyHistByEmployee(sid);
				Optional.ofNullable(affCompanyHistByEmp).ifPresent(f -> {
					if (f.items() != null) {
						List<AffCompanyHistItem> listAffComHisItem = affCompanyHistByEmp.getLstAffCompanyHistoryItem();

						if (!CollectionUtil.isEmpty(listAffComHisItem)) {
							listAffComHisItem.forEach(m -> {
								if (m.start().beforeOrEquals(startDate) && m.end().afterOrEquals(endDate)) {
									AffWorkplaceExport aff = new AffWorkplaceExport(sid, m.start(), m.end());
									result.add(aff);
								}
							});
						}

					}
				});
			}else{
				System.out.println("data sai: " + sid);
			}
		});

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.bs.employee.pub.workplace.WorkplacePub#findWpkIdsBySid(java.
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
	
	@Override
	public Map<GeneralDate, Map<String, List<String>>> findWpkIdsBySids(String companyId, List<String> employeeId, DatePeriod date) {
		// Query
		Map<GeneralDate, List<AffWorkplaceHistoryItem>> his = new HashMap<>();
		date.datesBetween().stream().forEach(c -> {
			his.put(c, affWorkplaceHistoryItemRepository.getAffWrkplaHistItemByListEmpIdAndDateV2(c, employeeId));
		});

		List<String> lstWpkIds = his.entrySet().stream().map(c -> c.getValue().stream().map(h -> h.getWorkplaceId()).collect(Collectors.toList()))
										.flatMap(List::stream).distinct().collect(Collectors.toList());
		Map<WorkplaceConfigHistory, List<WorkplaceConfigInfo>> wpc = wkpConfigInfoRepo.findAllParentByWkpId(companyId, date, lstWpkIds);

		return his.entrySet().stream().collect(Collectors.toMap(c -> c.getKey(), c -> {
			List<WorkplaceConfigInfo> wpConfig = wpc.entrySet().stream().filter(wpch -> wpch.getKey().contains(c.getKey())).findFirst().get().getValue();
			return c.getValue().stream().collect(Collectors.groupingBy(h -> h.getEmployeeId(), Collectors.collectingAndThen(Collectors.toList(), list -> {
				Optional<WorkplaceConfigInfo> currentWpci = wpConfig.stream().filter(w -> w.getLstWkpHierarchy().get(0).getWorkplaceId().equals(list.get(0).getEmployeeId())).findFirst();
				if(currentWpci.isPresent()){
					return currentWpci.get().getLstWkpHierarchy().stream().map(wkph -> wkph.getWorkplaceId()).collect(Collectors.toList());
				}
				return new ArrayList<>();
			})));
		}));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#findBySid(java.lang.
	 * String, nts.arc.time.GeneralDate)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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


	@Override
	public List<SWkpHistExport> findBySId(List<String> sids) {
		String companyID = AppContexts.user().companyId();
		// get AffWorkplaceHistory
				List<AffWorkplaceHistory> affWrkPlc = affWorkplaceHistoryRepository.getByListSid(sids);
				if (affWrkPlc.isEmpty())
					return Collections.emptyList();

				// get AffWorkplaceHistoryItem
				List<String> historyIds = affWrkPlc.stream().map(c->c.getHistoryItems().get(0).identifier()).collect(Collectors.toList());
				List<AffWorkplaceHistoryItem> affWrkPlcItem = affWorkplaceHistoryItemRepository.findByHistIds(historyIds);
				if (affWrkPlcItem.isEmpty())
					return Collections.emptyList();

				// Get workplace info.
				List<WorkplaceInfo> optWorkplaceInfo = workplaceInfoRepo.findByWkpIds(companyID,affWrkPlcItem.stream().map(c->c.getWorkplaceId()).collect(Collectors.toList()));

				// Check exist
				if (optWorkplaceInfo.isEmpty()) {
					return Collections.emptyList();
				}

//				// Return workplace id
//				WorkplaceInfo wkpInfo = optWorkplaceInfo.get();

				List<SWkpHistExport> listData = new ArrayList<>();
				for(WorkplaceInfo workplaceInfo : optWorkplaceInfo) {
					for(AffWorkplaceHistoryItem affWorkplaceHistoryItem : affWrkPlcItem) {
						if(affWorkplaceHistoryItem.getHistoryId().equals(workplaceInfo.getHistoryId())) {
							for(AffWorkplaceHistory affWorkplaceHistory : affWrkPlc) {
								if(affWorkplaceHistoryItem.getEmployeeId().equals(affWorkplaceHistory.getEmployeeId())) {
									listData.add(convertToWorkplaceInfo(workplaceInfo, affWorkplaceHistory));
									break;
								}
							}
							break;
						}

					}

				}


				return listData;
	}

	@Override
	public List<SWkpHistExport> findBySId(List<String> sids, GeneralDate baseDate) {
		String companyID = AppContexts.user().companyId();
		// Get AffWorkplaceHistory
		List<AffWorkplaceHistory> affWrkPlc = affWorkplaceHistoryRepository.getWorkplaceHistoryByEmpIdsAndDate(baseDate, sids);
		// Check exist
		if (affWrkPlc.isEmpty())
			return Collections.emptyList();

		// Get AffWorkplaceHistoryItem
		List<String> historyIds = affWrkPlc.stream().map(c -> c.getHistoryItems().get(0).identifier()).collect(Collectors.toList());
		List<AffWorkplaceHistoryItem> affWrkPlcItem = affWorkplaceHistoryItemRepository.findByHistIds(historyIds);
		// Check exist
		if (affWrkPlcItem.isEmpty())
			return Collections.emptyList();

		// Get workplace info.
		List<String> workplaceIds = affWrkPlcItem.stream().map(AffWorkplaceHistoryItem::getWorkplaceId).distinct().collect(Collectors.toList());
		List<WorkplaceInfo> list = workplaceInfoRepo.findByBaseDateWkpIds(companyID, baseDate, workplaceIds);
		// Check exist
		if (list.isEmpty()) {
			return Collections.emptyList();
		}

		// Convert and get data
		List<SWkpHistExport> listData = new ArrayList<>();
		Map<String, AffWorkplaceHistoryItem> affWorkplaceHistItemMap = affWrkPlcItem.stream().collect(Collectors.toMap(AffWorkplaceHistoryItem::getEmployeeId, Function.identity()));
		Map<String, WorkplaceInfo> workplaceInfoMap = list.stream().collect(Collectors.toMap(WorkplaceInfo::getWorkplaceId, Function.identity()));

		affWrkPlc.forEach(w -> {
			String workplaceId = affWorkplaceHistItemMap.get(w.getEmployeeId()).getWorkplaceId();
			listData.add(convertToWorkplaceInfo(workplaceInfoMap.get(workplaceId), w));
		});

		return listData;
	}

	private SWkpHistExport convertToWorkplaceInfo(WorkplaceInfo wkpInfo,AffWorkplaceHistory affWrkPlc) {
		return SWkpHistExport.builder()
				.dateRange(affWrkPlc.getHistoryItems().get(0).span())
		.employeeId(affWrkPlc.getEmployeeId())
		.workplaceId(wkpInfo.getWorkplaceId())
		.workplaceCode(wkpInfo.getWorkplaceCode().v())
		.workplaceName(wkpInfo.getWorkplaceName().v())
		.wkpDisplayName(wkpInfo.getWkpDisplayName().v()).build();
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

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#
	 * findListWorkplaceIdByBaseDate(nts.arc.time.GeneralDate)
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

		if (!wkpConfigInfo.isPresent()) {
			return Collections.emptyList();
		}
		List<WorkplaceHierarchy> listWkpHierachy = wkpConfigInfo.get().getLstWkpHierarchy();

		return listWkpHierachy.stream().map(wkpHierachy -> wkpHierachy.getWorkplaceId()).collect(Collectors.toList());

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#GetWplByListSidAndPeriod(
	 * java.util.List, nts.uk.shr.com.time.calendar.period.DatePeriod)
	 */
	@Override
	public List<WorkPlaceHistExport> GetWplByListSidAndPeriod(List<String> sids, DatePeriod datePeriod) {

		if (sids.isEmpty() || datePeriod.start() == null || datePeriod.end() == null)
			return Collections.emptyList();

		List<AffWorkplaceHistory> lstAffWkpHist = affWorkplaceHistoryRepository.getByListSid(sids);
		if (lstAffWkpHist.isEmpty())
			return Collections.emptyList();

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
	 * @param affWkp
	 *            the aff wkp
	 * @param datePeriod
	 *            the date period
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

		if (sids.isEmpty() || baseDate == null)
			return Collections.emptyList();

		List<AffWorkplaceHistory> lstAffWkpHist = affWorkplaceHistoryRepository.getByListSid(sids);
		if (lstAffWkpHist.isEmpty())
			return Collections.emptyList();

		List<String> historyIds = new ArrayList<>();

		lstAffWkpHist.stream().forEach(x -> {

			List<DateHistoryItem> historyItemList = x.items();
			List<String> hists = new ArrayList<>();
			if (!historyItemList.isEmpty()) {
				hists = historyItemList.stream().filter(m -> {
					return m.end().afterOrEquals(baseDate) && m.start().beforeOrEquals(baseDate);
				}).map(y -> y.identifier()).collect(Collectors.toList());

				historyIds.addAll(hists);
			}

		});

		if (historyIds.isEmpty())
			return Collections.emptyList();

		List<AffWorkplaceHistoryItem> affWrkPlcItems = affWorkplaceHistoryItemRepository.findByHistIds(historyIds);

		return affWrkPlcItems.stream().map(x -> {
			AffAtWorkplaceExport affWkp = new AffAtWorkplaceExport();
			affWkp.setEmployeeId(x.getEmployeeId());
			affWkp.setHistoryID(x.getHistoryId());
			affWkp.setWorkplaceId(x.getWorkplaceId());
			affWkp.setNormalWorkplaceID(x.getNormalWorkplaceId());
			return affWkp;
		}).collect(Collectors.toList());
	}

	@Override
	public List<AffAtWorkplaceExport> findBySIdAndBaseDateV2(List<String> sids, GeneralDate baseDate) {

		return affWorkplaceHistoryItemRepository.getAffWrkplaHistItemByListEmpIdAndDateV2(baseDate, sids).stream().map(x -> {
			return new AffAtWorkplaceExport(x.getEmployeeId(), x.getWorkplaceId(), x.getHistoryId(), x.getNormalWorkplaceId());
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#findWkpByWkpId(java.lang.
	 * String, nts.arc.time.GeneralDate, java.util.List)
	 */
	@Override
	public List<WorkPlaceInfoExport> findWkpByWkpId(String companyId, GeneralDate baseDate, List<String> wkpIds) {

		List<Workplace> workplace = workplaceRepo.findByWkpIds(wkpIds);

		List<Workplace> workplaceList = workplace.stream().filter(m -> {
			return m.getCompanyId().equals(companyId) && m.getWkpHistoryLatest().start().beforeOrEquals(baseDate)
					&& m.getWkpHistoryLatest().end().afterOrEquals(baseDate);
		}).collect(Collectors.toList());

		List<String> historyList = new ArrayList<>();

		workplaceList.forEach(item -> {
			historyList.add(item.getWkpHistoryLatest().identifier());
		});

		List<WorkplaceInfo> wkpInfors = workplaceInfoRepo.findByHistory(historyList, companyId);

		return wkpInfors.stream().map(item -> WorkPlaceInfoExport.builder().workplaceId(item.getWorkplaceId())
				.workPlaceName(item.getWorkplaceName().v()).build()).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#getWkpCdName(java.lang.
	 * String, nts.arc.time.GeneralDate, java.util.List)
	 */
	@Override
	public List<WkpIdNameHierarchyCdExport> getWkpIdNameHierarchyCd(String companyId,
			GeneralDate baseDate, List<String> wkpIds) {
		List<WorkplaceInfo> optWorkplaceInfos = workplaceInfoRepo.findByBaseDateWkpIds(companyId,
				baseDate, wkpIds);
		List<WorkplaceConfigInfo> workplaceConfigInfos = workplaceConfigInfoRepo
				.findByWkpIdsAtTime(companyId, baseDate, wkpIds);
		List<WorkplaceHierarchy> lstWkpHierarchy = workplaceConfigInfos.stream()
				.flatMap(item -> item.getLstWkpHierarchy().stream()).collect(Collectors.toList());
		Map<String, String> mapHierarchyCd = lstWkpHierarchy.stream().collect(Collectors
				.toMap(WorkplaceHierarchy::getWorkplaceId, item -> item.getHierarchyCode().v()));

		return optWorkplaceInfos.stream()
				.map(wkpInfo -> WkpIdNameHierarchyCdExport.builder().wkpId(wkpInfo.getWorkplaceId())
						.wkpName(wkpInfo.getWorkplaceName().v())
						.hierarchyCd(mapHierarchyCd.get(wkpInfo.getWorkplaceId())).build())
				.collect(Collectors.toList());
	}

	@Override
	public WkpByEmpExport getLstHistByEmpAndPeriod(String employeeID, GeneralDate startDate, GeneralDate endDate) {
		String companyID = AppContexts.user().companyId();
		List<AffWorkplaceHistory> affWorkplaceHistoryLst = affWorkplaceHistoryRepository.findByEmployeesWithPeriod(
				Arrays.asList(employeeID), new DatePeriod(startDate, endDate));
		if(CollectionUtil.isEmpty(affWorkplaceHistoryLst)){
			return new WkpByEmpExport(employeeID, Collections.emptyList());
		}
		List<DateHistoryItem> dateHistoryItemLst = new ArrayList<>();
		affWorkplaceHistoryLst.forEach(x -> {
			dateHistoryItemLst.addAll(x.getHistoryItems());
		});
		List<String> hisIDs = dateHistoryItemLst.stream().map(x -> x.identifier()).collect(Collectors.toList());
		Map<String,String> wkpIDLst = new HashMap<String,String>();
		affWorkplaceHistoryItemRepository.findByHistIds(hisIDs).forEach(x -> {
			wkpIDLst.put(x.getHistoryId(), x.getWorkplaceId());
		});

		List<WorkplaceInfo> wkpInfoLst = workplaceInfoRepo.findByWkpIds(companyID, wkpIDLst.entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList()));
		List<WkpInfoExport> wkpInfoExportLst = new ArrayList<>();
		dateHistoryItemLst.forEach(x -> {
			Optional<Entry<String,String>> wkpIDItem = wkpIDLst.entrySet().stream().filter(t -> t.getKey().equals(x.identifier())).findAny();
			if(!wkpIDItem.isPresent()){
				for(int i = 0; i< 10; i++){
					System.out.println("lỗi dữ liệu WorkplacePubImp line 703");
				}
				return;
			}
			String wkpID = wkpIDItem.get().getValue();
			WkpInfoExport wkpInfoExport = wkpInfoLst.stream().filter(y -> y.getWorkplaceId().equals(wkpID)).findAny()
					.map(k -> new WkpInfoExport(
							x.span(),
							k.getWorkplaceId(),
							k.getWorkplaceCode().toString(),
							k.getWorkplaceName().toString()))
					.orElse(new WkpInfoExport(
							x.span(),
							wkpID,
							"マスタ未登録",
							"マスタ未登録"));
			wkpInfoExportLst.add(wkpInfoExport);
		});
		return new WkpByEmpExport(employeeID, wkpInfoExportLst);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#
	 * getLstHistByWkpsAndPeriod(java.util.List,
	 * nts.uk.shr.com.time.calendar.period.DatePeriod)
	 */
	@Override
	public List<WkpHistWithPeriodExport> getLstHistByWkpsAndPeriod(List<String> wkpIds,
			DatePeriod period) {

		List<Workplace> workplaces = this.workplaceRepo.findWorkplaces(wkpIds, period);

		List<String> historyIds = workplaces.stream()
				.flatMap(item -> item.getWorkplaceHistory().stream())
				.map(WorkplaceHistory::identifier).collect(Collectors.toList());

		List<WorkplaceInfo> optWorkplaceInfos = workplaceInfoRepo.findByHistory(historyIds);

		Map<String, WorkplaceInfo> mapWorkplaceInfo = optWorkplaceInfos.stream()
				.collect(Collectors.toMap(WorkplaceInfo::getHistoryId, Function.identity()));

		return workplaces.stream().map(item -> {
			List<WkpInfoHistExport> wkpInfoHistLst = item.getWorkplaceHistory().stream()
					.map(hist -> {
						WorkplaceInfo info = mapWorkplaceInfo.get(hist.identifier());
						String wkpCode = info.getWorkplaceCode().v();
						String wkpDisplayName = info.getWkpDisplayName().v();
						return WkpInfoHistExport.builder().period(hist.span()).wkpCode(wkpCode)
								.wkpDisplayName(wkpDisplayName).build();
					}).collect(Collectors.toList());
			return WkpHistWithPeriodExport.builder().wkpId(item.getWorkplaceId())
					.wkpInfoHistLst(wkpInfoHistLst).build();
		}).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#getLstPeriod(java.lang.String, nts.uk.shr.com.time.calendar.period.DatePeriod)
	 */
	@Override
	public List<DatePeriod> getLstPeriod(String companyId, DatePeriod period){
		List<WorkplaceConfig> wkps = this.wkpConfigRepository.findByCompanyIdAndPeriod(companyId, period);

		List<DatePeriod> dateList = new ArrayList<>();

		wkps.stream().map(item -> {
			List<DatePeriod> dates = new ArrayList<>();
			dates = item.getWkpConfigHistory().stream().map(hst -> {
				return hst.span();
			}).collect(Collectors.toList());
			return dates;
		}).collect(Collectors.toList());

		return dateList;
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#
	 * findParentWpkIdsByWkpIdDesc(java.lang.String, java.lang.String,
	 * nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findParentWpkIdsByWkpIdDesc(String companyId, String workplaceId,
			GeneralDate date) {
		// Get config info
		Optional<WorkplaceConfigInfo> optWorkplaceConfigInfo = workplaceConfigInfoRepo
				.findAllParentByWkpId(companyId, date, workplaceId, false);

		// Check exist
		if (!optWorkplaceConfigInfo.isPresent()) {
			return Collections.emptyList();
		}

		// Return
		return optWorkplaceConfigInfo.get().getLstWkpHierarchy().stream()
				.map(WorkplaceHierarchy::getWorkplaceId).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub#getWorkplaceMapCodeBaseDateName(java.lang.String, java.util.List, java.util.List)
	 */
	@Override
	public Map<Pair<String, GeneralDate>, Pair<String,String>> getWorkplaceMapCodeBaseDateName(String companyId,
			List<String> wpkIds, List<GeneralDate> baseDates) {
		// Query infos
		Map<GeneralDate, List<WorkplaceInfo>> mapWorkplaceInfos = this.workplaceInfoRepo
				.findByWkpIds(companyId, wpkIds, baseDates);

		Map<Pair<String, GeneralDate>, Pair<String,String>> mapResult = new HashMap<>();
		mapWorkplaceInfos.entrySet().forEach(item -> {
			item.getValue().forEach(workplaceInfo -> {
				mapResult.put(Pair.of(workplaceInfo.getWorkplaceId(), item.getKey()),
						Pair.of(workplaceInfo.getWorkplaceCode().v(),workplaceInfo.getWorkplaceName().v()));
			});
		});

		return mapResult;
	}

	@Override
	public List< AffWorkplaceHistoryExport> getWorkplaceBySidsAndBaseDate(List<String> sids, GeneralDate baseDate) {
		List<String> histIds =  new ArrayList<>();
		List<AffWorkplaceHistoryExport>  result = new ArrayList<>();
		Map<String, List<AffWorkplaceHistory>> workplaceHistMap = affWorkplaceHistoryRepository.getWorkplaceHistoryBySidsAndDateV2( baseDate, sids).stream().collect(Collectors.groupingBy(c -> c.getEmployeeId()));
		workplaceHistMap.values().stream().forEach(c -> {
			histIds.addAll(c.get(0).getHistoryIds());
		});
		List<AffWorkplaceHistoryItem> histItemMaps = affWorkplaceHistoryItemRepository.findByHistIds(histIds);
		workplaceHistMap.entrySet().stream().forEach(c -> {
			AffWorkplaceHistory value = c.getValue().get(0);
			Map<String, AffWorkplaceHistoryItemExport> workplaceHistItems = new HashMap<>();
			value.getHistoryItems().stream().forEach(hist -> {
				Optional<AffWorkplaceHistoryItem> workplacehistItemOpt = histItemMaps.stream()
						.filter(item -> item.getHistoryId().equals(hist.identifier())).findFirst();
				if (workplacehistItemOpt.isPresent()) {
					AffWorkplaceHistoryItem histItem = workplacehistItemOpt.get();
					AffWorkplaceHistoryItemExport histItemExport = new AffWorkplaceHistoryItemExport(
							histItem.getHistoryId(), histItem.getWorkplaceId(), histItem.getNormalWorkplaceId());
					workplaceHistItems.put(hist.identifier(), histItemExport);
				}
			});
			AffWorkplaceHistoryExport export = new AffWorkplaceHistoryExport(value.getEmployeeId(), value.getHistoryItems(), workplaceHistItems);
			result.add(export);
		});
		return result;
	}

	@Override
	public List<String> getLstWorkplaceIdBySidAndPeriod(String sid, DatePeriod period) {
		List<String> workPlaceIds = affWorkplaceHistoryItemRepository.getHistIdLstBySidAndPeriod(sid, period);
		return workPlaceIds;
	}

	@Override
	public List<ResultRequest597Export> getLstEmpByWorkplaceIdsAndPeriod(List<String> workplaceIds, DatePeriod period) {
		List<String> sids = affWorkplaceHistoryItemRepository.getHistIdLstByWorkplaceIdsAndPeriod(workplaceIds, period);
		if(CollectionUtil.isEmpty(sids)) { return new ArrayList<>();}
		List<ResultRequest597Export> results = subEmp.getEmpInfoLstBySids(sids, period, true, true).stream()
				.map(c -> new ResultRequest597Export(c.getSid(), c.getEmployeeCode(), c.getEmployeeName()))
				.collect(Collectors.toList());
		return results;
	}

	@Override
	public List<WorkPlaceInfoExport> findWkpByWkpIdRQ324Ver2(String companyId, GeneralDate baseDate,
			List<String> wkpIds) {
		List<Workplace> workplace = workplaceRepo.findByWkpIds(wkpIds);
		List<String> lstHistID = new ArrayList<>();
		for (Workplace wkp : workplace) {
			if(!wkp.getCompanyId().equals(companyId)){
				continue;
			}
			List<WorkplaceHistory> workplaceHistory = wkp.getWorkplaceHistory();
			for (WorkplaceHistory wkpHist : workplaceHistory) {
				if(wkpHist.start().beforeOrEquals(baseDate) && wkpHist.end().afterOrEquals(baseDate)){
					lstHistID.add(wkpHist.identifier());
				}
			}
		}
		List<WorkplaceInfo> wkpInfors = workplaceInfoRepo.findByHistory(lstHistID, companyId);
		return wkpInfors.stream().map(item -> WorkPlaceInfoExport.builder().workplaceId(item.getWorkplaceId())
				.workPlaceName(item.getWorkplaceName().v()).build()).collect(Collectors.toList());
	}

}
