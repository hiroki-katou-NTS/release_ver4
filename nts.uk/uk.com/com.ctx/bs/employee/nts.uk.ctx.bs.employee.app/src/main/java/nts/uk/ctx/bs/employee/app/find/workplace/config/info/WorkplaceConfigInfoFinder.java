/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.find.workplace.config.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.app.find.workplace.config.dto.WkpConfigInfoFindObject;
import nts.uk.ctx.bs.employee.app.find.workplace.config.dto.WorkplaceHierarchyDto;
import nts.uk.ctx.bs.employee.dom.access.role.SyRoleAdapter;
import nts.uk.ctx.bs.employee.dom.access.role.WorkplaceIDImport;
import nts.uk.ctx.bs.employee.dom.workplace.Workplace;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.HierarchyCode;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfo;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceConfigInfoRepository;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.WorkplaceHierarchy;
import nts.uk.ctx.bs.employee.dom.workplace.config.info.service.WkpConfigInfoService;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WorkplaceConfigInfoFinder.
 */
@Stateless
public class WorkplaceConfigInfoFinder {

	/** The wkp config info service. */
	@Inject
	WkpConfigInfoService wkpConfigInfoService;

	/** The wkp config info repo. */
	@Inject
	private WorkplaceConfigInfoRepository wkpConfigInfoRepo;

	/** The wkp info repo. */
	@Inject
	private WorkplaceInfoRepository wkpInfoRepo;

	/** The wkp config repository. */
	@Inject
	private WorkplaceConfigRepository wkpConfigRepository;
	
	@Inject
	private WorkplaceRepository wkpRepository;

	/** The Constant HIERARCHY_LENGTH. */
	private static final Integer HIERARCHY_LENGTH = 3;

	@Inject
	private SyRoleAdapter syRoleWorkplaceAdapter;
	

	/**
	 * Find all by base date.
	 *
	 * @param object
	 *            the object
	 * @return the list
	 */
	public List<WorkplaceHierarchyDto> findAllByBaseDate(WkpConfigInfoFindObject object) {
		
		// Check system type.
		if (object.getSystemType() == null) {
			return Collections.emptyList();
		}
		
		// Find Workplace Config.
		String companyId = AppContexts.user().companyId();
		Optional<WorkplaceConfig> optionalWkpConfig = wkpConfigRepository.findByBaseDate(companyId, object.getBaseDate());

		if (!optionalWkpConfig.isPresent()) {
			return Collections.emptyList();
		}
		
		// Find workplace config info.
		List<String> configHisIds = optionalWkpConfig.get().items().stream().map(item -> item.identifier())
				.collect(Collectors.toList());

		List<WorkplaceConfigInfo> workplaceConfigInfos;

		// Check if is restrictionOfReferenceRange.
		if (object.getRestrictionOfReferenceRange()) {
			List<String> workplaceIdsCanReference = this.syRoleWorkplaceAdapter
					.findListWkpIdByRoleId(object.getSystemType(), object.getBaseDate()).getListWorkplaceIds();
			workplaceConfigInfos = this.wkpConfigInfoRepo.findByHistoryIdsAndWplIds(companyId, configHisIds,
					workplaceIdsCanReference);
		} else {
			workplaceConfigInfos = this.wkpConfigInfoRepo.findByHistoryIds(companyId, configHisIds);
		}

		if (CollectionUtil.isEmpty(workplaceConfigInfos)) {
			return Collections.emptyList();
		}
		
		List<WorkplaceHierarchy> workplaceHierarchies = workplaceConfigInfos.stream()
				.map(info -> info.getLstWkpHierarchy()).flatMap(list -> list.stream())
				.sorted((a,b) -> a.getHierarchyCode().v().compareTo(b.getHierarchyCode().v()))
				.collect(Collectors.toList());
		return this.initTree(object.getBaseDate(), workplaceHierarchies);
	}

	/**
	 * Find all by start date.
	 *
	 * @param strD
	 *            the str D
	 * @return the list
	 */
	public List<WorkplaceHierarchyDto> findAllByStartDate(GeneralDate strD) {
		// get all WorkplaceConfigInfo with StartDate
		String companyId = AppContexts.user().companyId();
		Optional<WorkplaceConfig> optionalWkpConfig = wkpConfigRepository.findByStartDate(companyId, strD);
		if (!optionalWkpConfig.isPresent()) {
			return null;
		}
		WorkplaceConfig wkpConfig = optionalWkpConfig.get();
		String historyId = wkpConfig.getWkpConfigHistoryLatest().identifier();

		Optional<WorkplaceConfigInfo> opWkpConfigInfo = wkpConfigInfoRepo.find(companyId, historyId);
		if (!opWkpConfigInfo.isPresent()) {
			throw new BusinessException("Msg_373");
		}
		
		return this.initTree(strD, opWkpConfigInfo.get().getLstWkpHierarchy());
	}

	/**
	 * Inits the tree.
	 *
	 * @param startDWkpConfigHist
	 *            the start D wkp config hist
	 * @param lstHierarchy
	 *            the lst hierarchy
	 * @return the list
	 */
	private List<WorkplaceHierarchyDto> initTree(GeneralDate startDWkpConfigHist,
			List<WorkplaceHierarchy> lstHierarchy) {
		String companyId = AppContexts.user().companyId();

		//ドメインモデル「職場」をすべて取得する(get domain 「職場」)
		List<Workplace> wkps = this.wkpRepository.findWorkplacesByDate(companyId, startDWkpConfigHist);
		
		List<String> wkpIds = lstHierarchy.stream().map(item -> {
			return item.getWorkplaceId();
		}).collect(Collectors.toList());
		
		List<String> wkpHistIds = new ArrayList<String>(); 
		
		wkps.stream().map(item -> {
			return item.getWorkplaceHistory().stream().map(hist -> {
				return wkpHistIds.add(hist.identifier());
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());
		
		// filter workplace infor latest
		List<WorkplaceInfo> lstWkpInfo = this.wkpInfoRepo.findByWkpIdsAndHistIds(companyId, wkpIds, wkpHistIds);
		return this.createTree(lstHierarchy, lstWkpInfo);
	}

	@Getter
	@Setter
	class HierWorkplaceHierarchyDto extends WorkplaceHierarchyDto {
		private HierWorkplaceHierarchyDto parent;
		
		private List<HierWorkplaceHierarchyDto> childrends;
		
		public WorkplaceHierarchyDto toDto() {
			WorkplaceHierarchyDto dto = new WorkplaceHierarchyDto();
			
			dto.setCode(this.code);
			dto.setName(this.name);
			
			dto.setWorkplaceId(this.workplaceId);
			dto.setHierarchyCode(new HierarchyCode(this.hierarchyCode));
			
			dto.setChilds(this.childrends.stream().map(m -> m.toDto()).collect(Collectors.toList()));
			
			return dto;
		}		
	}
	
	/**
	 * Creates the tree.
	 *
	 * @param lstWkpInfo
	 *            the lst wkp info
	 * @param lstHierarchy
	 *            the lst hierarchy
	 * @param lstReturn
	 *            the lst return
	 * @return the list
	 */
	private List<WorkplaceHierarchyDto> createTree(List<WorkplaceHierarchy> lstHierarchy,
			List<WorkplaceInfo> lstHWkpInfo) {

		List<HierWorkplaceHierarchyDto> lstReturn = new ArrayList<>();

		Iterator<WorkplaceHierarchy> iteratorWkpHierarchy = lstHierarchy.iterator();
		
		// while have workplace
		while (iteratorWkpHierarchy.hasNext()) {
			// pop 1 item
			WorkplaceHierarchy wkpHierarchy = iteratorWkpHierarchy.next();

			// convert
			HierWorkplaceHierarchyDto dto = new HierWorkplaceHierarchyDto();
			wkpHierarchy.saveToMemento(dto);

			// get workplace hierarchy by wkpId
			Optional<WorkplaceInfo> opWkpInfo = lstHWkpInfo.stream()
					.filter(w -> w.getWorkplaceId().equals(wkpHierarchy.getWorkplaceId())).findFirst();

			if (opWkpInfo.isPresent()) {
				WorkplaceInfo wkpInfo = opWkpInfo.get();
				dto.setCode(wkpInfo.getWorkplaceCode().v());
				dto.setName(wkpInfo.getWorkplaceName().v());
			} else {
				// ignore workplace that don't have code and name.
				continue;
			}

			dto.setChilds(new ArrayList<>());

			lstReturn.add(dto);
		}
		
		lstReturn.stream()
			.sorted(Comparator.comparing(HierWorkplaceHierarchyDto::getHierarchyCode))
			.map(m -> {
				// Lọc đối tượng con và loại bỏ chính đối tượng đó
				List<HierWorkplaceHierarchyDto> childs = lstReturn.stream().filter(f -> {
					boolean notSelf = !f.getHierarchyCode().equals(m.getHierarchyCode());
					boolean isChilds = f.getHierarchyCode().startsWith(m.getHierarchyCode());
					
					return notSelf && isChilds;
				}).collect(Collectors.toList());
				
				// lập quan hệ cha - com
				childs.forEach(f -> f.setParent(m));
				
				// lập quan hệ con cha
				m.setChildrends(childs);
				
				return m;
			})
			.collect(Collectors.toList());
		
		lstReturn.forEach(m -> { 
			// Lọc bỏ những đối tượng con không hợp lệ
			List<HierWorkplaceHierarchyDto> childs = m.getChildrends()
					.stream()
					.filter(c -> c.getParent().getHierarchyCode().equals(m.getHierarchyCode()))
					.collect(Collectors.toList());
			
			m.setChildrends(childs);
		});

		return lstReturn.stream()
				.filter(f -> f.getParent() == null)
				.map(f -> f.toDto()).collect(Collectors.toList());
	}

	/**
	 * Push to list.
	 *
	 * @param lstReturn
	 *            the lst return
	 * @param dto
	 *            the dto
	 * @param hierarchyCode
	 *            the hierarchy code
	 * @param preCode
	 *            the pre code
	 */
	private void pushToList(List<WorkplaceHierarchyDto> lstReturn, WorkplaceHierarchyDto dto, String hierarchyCode,
			String preCode, int highestHierarchy) {
		dto.setChilds(new ArrayList<>());
		if (hierarchyCode.length() == highestHierarchy) {
			// check duplicate code
			if (lstReturn.isEmpty()) {
				lstReturn.add(dto);
				return;
			}
			for (WorkplaceHierarchyDto item : lstReturn) {
				if (!item.getCode().equals(dto.getCode())) {
					lstReturn.add(dto);
					break;
				}
			}
		} else {
			String searchCode = preCode.isEmpty() ? preCode + hierarchyCode.substring(0, highestHierarchy)
			: preCode + hierarchyCode.substring(0, HIERARCHY_LENGTH); 
			//: preCode + hierarchyCode.substring(hierarchyCode.length() - HIERARCHY_LENGTH, hierarchyCode.length());

			Optional<WorkplaceHierarchyDto> optWorkplaceFindDto = lstReturn.stream()
					.filter(item -> item.getHierarchyCode().equals(searchCode)).findFirst();

			if (!optWorkplaceFindDto.isPresent()) {
				return;
			}

			List<WorkplaceHierarchyDto> currentItemChilds = optWorkplaceFindDto.get().getChilds();

			if (preCode.isEmpty()) {
				pushToList(currentItemChilds, dto, hierarchyCode.substring(highestHierarchy, hierarchyCode.length()),
						searchCode, highestHierarchy);
			} else {
				pushToList(currentItemChilds, dto, hierarchyCode.substring(HIERARCHY_LENGTH, hierarchyCode.length()),
						searchCode, highestHierarchy);
			}
		}
	}
	
//	@Inject
//	private WorkplaceInfoRepository workplaceInfoRepo;

//	@Inject
//	private WorkplaceConfigInfoRepository workplaceConfigInfoRepo;
	/**
	 * Find all by base date.
	 *
	 * @param object
	 *            the object
	 * @return the list
	 */
	
	public List<WorkplaceHierarchyDto> findAllByBaseDateForKcp010(WkpConfigInfoFindObject object) {
		
		// get base date
		GeneralDate baseD = object.getBaseDate();
		object.setSystemType(2);
		// get all WorkplaceConfigInfo with StartDate
		String companyId = AppContexts.user().companyId();
		Optional<WorkplaceConfig> optionalWkpConfig = wkpConfigRepository.findByBaseDate(companyId, baseD);
		if (!optionalWkpConfig.isPresent()) {
			return Collections.emptyList();
		}
		WorkplaceConfig wkpConfig = optionalWkpConfig.get();
		String historyId = wkpConfig.getWkpConfigHistoryLatest().identifier();
		Optional<WorkplaceConfigInfo> opWkpConfigInfo = wkpConfigInfoRepo.find(companyId, historyId);
		if (!opWkpConfigInfo.isPresent()) {
			return Collections.emptyList();
		}
		// get list hierarchy
		List<WorkplaceHierarchy> lstHierarchy = opWkpConfigInfo.get().getLstWkpHierarchy();

		WorkplaceIDImport workplaceIDImport = syRoleWorkplaceAdapter.findListWkpIdByRoleId(object.getSystemType(),baseD);

		List<WorkplaceHierarchy> result = new ArrayList<>();

		// if listWorkplaceIds is empty
		if (workplaceIDImport.getListWorkplaceIds().isEmpty()) {
			return this.initTree(baseD, result);
		}
//		
//		List<String> listWpId = workplaceIDImport.getListWorkplaceIds();
//		//RQ 164
//		List<WorkplaceInfo> optWorkplaceInfos = workplaceInfoRepo.findByBaseDateWkpIds(companyId,
//				baseD, listWpId);
//		List<WorkplaceConfigInfo> workplaceConfigInfos = workplaceConfigInfoRepo
//				.findByWkpIdsAtTime(companyId, baseD, listWpId);
//		List<WorkplaceHierarchy> lstWkpHierarchy = workplaceConfigInfos.stream()
//				.flatMap(item -> item.getLstWkpHierarchy().stream()).collect(Collectors.toList());
//		Map<String, String> mapHierarchyCd = lstWkpHierarchy.stream().collect(Collectors
//				.toMap(WorkplaceHierarchy::getWorkplaceId, item -> item.getHierarchyCode().v()));

//		List<WkpIdNameHierarchyCd> listWkpIdNameHierarchyCd = optWorkplaceInfos.stream()
//				.map(wkpInfo -> WkpIdNameHierarchyCd.builder().wkpId(wkpInfo.getWorkplaceId())
//						.wkpName(wkpInfo.getWorkplaceName().v())
//						.hierarchyCd(mapHierarchyCd.get(wkpInfo.getWorkplaceId())).build())
//				.collect(Collectors.toList());
//		
//		listWkpIdNameHierarchyCd.sort(Comparator.comparing(WkpIdNameHierarchyCd::getHierarchyCd));
//		
//		List<WorkplaceHierarchy> result2 = new ArrayList<>();
//		if(!listWkpIdNameHierarchyCd.isEmpty()) {
//			result2 = listWkpIdNameHierarchyCd.stream().map(c->convertToWorkplaceHierarchy(c)).collect(Collectors.toList());
//		}else {
//			return Collections.emptyList();
//		}
		// if listWorkplaceIds is not empty
		for (WorkplaceHierarchy item : lstHierarchy) {
			if (workplaceIDImport.getListWorkplaceIds().contains(item.getWorkplaceId())) {
				// if get part of list workplace id
				if (!workplaceIDImport.getIsAllEmp()) {
					// if list workplace id just have childs workplace id
					if (item.getHierarchyCode().v().length() == 3) {
						result.add(item);
					} else if (item.getHierarchyCode().v().length() > 3) {
						Optional<WorkplaceConfigInfo> opWorkplaceConfigInfo = wkpConfigInfoRepo
								.findAllParentByWkpId(companyId, baseD, item.getWorkplaceId());
						// find parents workplace id from childs workplace id
						List<WorkplaceHierarchy> listWorkplaceHierarchy = opWorkplaceConfigInfo.get()
								.getLstWkpHierarchy();
						// add parents workplace id to list
						result.addAll(listWorkplaceHierarchy);
						// add childs workplace id to list
						result.add(item);
					}
					// if get all of list workplace id
				} else {
					result.add(item);
				}
			}

			// remove dublicate element in list
			result = result.stream().distinct().collect(Collectors.toList());

		}
		return this.initTree(baseD, result);
	}
//	
//	private WorkplaceHierarchy convertToWorkplaceHierarchy(WkpIdNameHierarchyCd wkpIdNameHierarchyCd) {
//		return new WorkplaceHierarchy(
//				wkpIdNameHierarchyCd.getWkpId(),
//				new HierarchyCode(wkpIdNameHierarchyCd.getHierarchyCd())
//				);
//		
//	}

	/**
	 * Find flat list. 会社の基準日時点の職場を取得する
	 */
	public List<WorkplaceHierarchyDto> findFlatList(GeneralDate baseDate) {
		// Find all workplace in compant
		String companyId = AppContexts.user().companyId();
		Optional<WorkplaceConfig> optionalWkpConfig = wkpConfigRepository.findByBaseDate(companyId, baseDate);
		if (!optionalWkpConfig.isPresent()) {
			return Collections.emptyList();
		}

		// Find Workplace config info
		WorkplaceConfig wkpConfig = optionalWkpConfig.get();
		String historyId = wkpConfig.getWkpConfigHistoryLatest().identifier();
		Optional<WorkplaceConfigInfo> opWkpConfigInfo = wkpConfigInfoRepo.find(companyId, historyId);
		if (!opWkpConfigInfo.isPresent()) {
			return Collections.emptyList();
		}

		// Convert list to tree. -> special process for sort workplace list.
		List<WorkplaceHierarchy> lstHierarchy = opWkpConfigInfo.get().getLstWkpHierarchy();
		List<WorkplaceHierarchyDto> treeList = this.initTree(baseDate, lstHierarchy);

		// flat workplace tree. -> special process for sort workplace list.
		List<WorkplaceHierarchyDto> flatList = new ArrayList<>();
		treeList.stream().forEach(wpl -> {
			// Convert tree to flat list.
			flatList.addAll(this.convertToFlatList(wpl));
		});

		return flatList;
	}

	/**
	 * Convert to flat list.
	 *
	 * @param dto
	 *            the dto
	 * @return the list
	 */
	private List<WorkplaceHierarchyDto> convertToFlatList(WorkplaceHierarchyDto dto) {
		List<WorkplaceHierarchyDto> resultList = new ArrayList<>();
		;
		resultList.add(dto);
		if (CollectionUtil.isEmpty(dto.getChilds())) {
			return resultList;
		}
		dto.getChilds().stream().forEach(wpl -> {
			resultList.addAll(this.convertToFlatList(wpl));
		});
		return resultList;
	}
}
