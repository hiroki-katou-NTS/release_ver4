/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.app.find.workplace.config.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.app.find.workplace.config.dto.WkpConfigInfoFindObject;
import nts.uk.ctx.bs.employee.app.find.workplace.config.dto.WorkplaceHierarchyDto;
import nts.uk.ctx.bs.employee.dom.access.role.SyRoleAdapter;
import nts.uk.ctx.bs.employee.dom.access.role.WorkplaceIDImport;
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

	/** The Constant HIERARCHY_LENGTH. */
	private static final Integer HIERARCHY_LENGTH = 3;

	@Inject
	private SyRoleAdapter syRoleWorkplaceAdapter;
	
	/**
	 * Find all by base date.
	 *
	 * @param object the object
	 * @return the list
	 */
	public List<WorkplaceHierarchyDto> findAllByBaseDate(WkpConfigInfoFindObject object) {

		// get base date
		GeneralDate baseD = object.getBaseDate();

		// get all WorkplaceConfigInfo with StartDate
		String companyId = AppContexts.user().companyId();
		Optional<WorkplaceConfig> optionalWkpConfig = wkpConfigRepository.findByBaseDate(companyId, baseD);
		if (!optionalWkpConfig.isPresent()) {
			return null;
		}
		WorkplaceConfig wkpConfig = optionalWkpConfig.get();
		String historyId = wkpConfig.getWkpConfigHistoryLatest().identifier();
		Optional<WorkplaceConfigInfo> opWkpConfigInfo = wkpConfigInfoRepo.find(companyId, historyId);
		if (!opWkpConfigInfo.isPresent()) {
			return Collections.emptyList();
		}

		// get list hierarchy
		List<WorkplaceHierarchy> lstHierarchy = opWkpConfigInfo.get().getLstWkpHierarchy();

		WorkplaceIDImport workplaceIDImport = syRoleWorkplaceAdapter.findListWkpIdByRoleId(object.getSystemType());
		
		List<WorkplaceHierarchy> result = new ArrayList<>();
		
		// if listWorkplaceIds is empty
		if(workplaceIDImport.getListWorkplaceIds().isEmpty()){
			return this.initTree(baseD, result);
		}
		
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
		Optional<WorkplaceConfig> optionalWkpConfig = wkpConfigRepository.findByStartDate(companyId,
				strD);
		if (!optionalWkpConfig.isPresent()) {
			return null;
		}
		WorkplaceConfig wkpConfig = optionalWkpConfig.get();
		String historyId = wkpConfig.getWkpConfigHistoryLatest().identifier();

		Optional<WorkplaceConfigInfo> opWkpConfigInfo = wkpConfigInfoRepo.find(companyId,
				historyId);
		if (!opWkpConfigInfo.isPresent()) {
			throw new BusinessException("Msg_373");
		}
		return this.initTree(strD, opWkpConfigInfo.get().getLstWkpHierarchy());
	}

	/**
	 * Inits the tree.
	 *
	 * @param startDWkpConfigHist the start D wkp config hist
	 * @param lstHierarchy the lst hierarchy
	 * @return the list
	 */
	private List<WorkplaceHierarchyDto> initTree(GeneralDate startDWkpConfigHist, List<WorkplaceHierarchy> lstHierarchy) {
		String companyId = AppContexts.user().companyId();

		// filter workplace infor latest
		List<WorkplaceInfo> lstWkpInfo = this.wkpInfoRepo.findAll(companyId, startDWkpConfigHist);
		return this.createTree(lstHierarchy.iterator(), lstWkpInfo, new ArrayList<>());
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
	private List<WorkplaceHierarchyDto> createTree(
			Iterator<WorkplaceHierarchy> iteratorWkpHierarchy, List<WorkplaceInfo> lstHWkpInfo,
			List<WorkplaceHierarchyDto> lstReturn) {
		
		List<WorkplaceHierarchy> lstWkpHierarchyRemove = new ArrayList<>();
		
		// while have workplace
		while (iteratorWkpHierarchy.hasNext()) {
			// pop 1 item
			WorkplaceHierarchy wkpHierarchy = iteratorWkpHierarchy.next();
			
			// convert
			WorkplaceHierarchyDto dto = new WorkplaceHierarchyDto();
			wkpHierarchy.saveToMemento(dto);
			
			// get workplace hierarchy by wkpId
			Optional<WorkplaceInfo> opWkpInfo = lstHWkpInfo.stream()
					.filter(w -> w.getWorkplaceId().equals(wkpHierarchy.getWorkplaceId()))
					.findFirst();
			if (opWkpInfo.isPresent()) {
				WorkplaceInfo wkpInfo = opWkpInfo.get();
				dto.setCode(wkpInfo.getWorkplaceCode().v());
				dto.setName(wkpInfo.getWorkplaceName().v());
			} else {
				lstWkpHierarchyRemove.add(wkpHierarchy);
			}
			
			// ignore workplace that don't have code and name.
			if (lstWkpHierarchyRemove.contains(wkpHierarchy)) {
				continue;
			}

			dto.setHierarchyCode(new HierarchyCode(wkpHierarchy.getHierarchyCode().v()));
			// build List
			this.pushToList(lstReturn, dto, wkpHierarchy.getHierarchyCode().v(), Strings.EMPTY);
		}
		return lstReturn;
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
	private void pushToList(List<WorkplaceHierarchyDto> lstReturn, WorkplaceHierarchyDto dto,
			String hierarchyCode, String preCode) {
		String searchCode = preCode + hierarchyCode.substring(0, HIERARCHY_LENGTH);
		dto.setChilds(new ArrayList<>());
		if (hierarchyCode.length() == HIERARCHY_LENGTH) {
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
			Optional<WorkplaceHierarchyDto> optWorkplaceFindDto = lstReturn.stream()
					.filter(item -> item.getHierarchyCode().equals(searchCode)).findFirst();

			if (!optWorkplaceFindDto.isPresent()) {
				return;
			}

			List<WorkplaceHierarchyDto> currentItemChilds = optWorkplaceFindDto.get().getChilds();

			pushToList(currentItemChilds, dto,
					hierarchyCode.substring(HIERARCHY_LENGTH, hierarchyCode.length()), searchCode);
		}
	}

}
