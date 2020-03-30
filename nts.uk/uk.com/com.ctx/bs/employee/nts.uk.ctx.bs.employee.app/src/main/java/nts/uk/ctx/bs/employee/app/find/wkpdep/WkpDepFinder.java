//package nts.uk.ctx.bs.employee.app.find.wkpdep;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.uk.ctx.bs.employee.dom.access.role.SyRoleAdapter;
//import nts.uk.ctx.bs.employee.dom.department.master.service.DepartmentExportSerivce;
//import nts.uk.ctx.bs.employee.dom.department.master.service.DepartmentPastCodeCheckOutput;
//import nts.uk.ctx.bs.employee.dom.department.master.service.DepartmentQueryService;
//import nts.uk.ctx.bs.employee.dom.workplace.master.service.WorkplaceExportService;
//import nts.uk.ctx.bs.employee.dom.workplace.master.service.WorkplacePastCodeCheckOutput;
//import nts.uk.ctx.bs.employee.dom.workplace.master.service.WorkplaceQueryService;
//
//import org.apache.logging.log4j.util.Strings;
//
//import nts.arc.error.BusinessException;
//import nts.arc.time.GeneralDate;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfiguration;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfigurationRepository;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformation;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformationRepository;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfiguration;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfigurationRepository;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformation;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformationRepository;
//import nts.uk.shr.com.context.AppContexts;
//import nts.uk.shr.com.history.DateHistoryItem;
//
///**
// * 
// * @author HungTT
// *
// */
//
//@Stateless
//public class WkpDepFinder {
//
//	private static final int WORKPLACE_MODE = 0;
//	private static final int DEPARTMENT_MODE = 1;
//	private static final Integer HIERARCHY_LENGTH = 3;
//	private static final int MAX_WKP_DEP_NUMBER = 9999;
//
//	@Inject
//	private WorkplaceConfigurationRepository wkpConfigRepo;
//
//	@Inject
//	private DepartmentConfigurationRepository depConfigRepo;
//
//	@Inject
//	private WorkplaceInformationRepository wkpInforRepo;
//
//	@Inject
//	private DepartmentInformationRepository depInforRepo;
//
//	@Inject
//	private DepartmentExportSerivce depExportSerivce;
//
//	@Inject
//	private WorkplaceExportService wkpExportService;
//
//	@Inject
//	private WorkplaceQueryService wkpQueryService;
//
//	@Inject
//	private DepartmentQueryService depQueryService;
//
//	@Inject
//	private SyRoleAdapter syRoleWorkplaceAdapter;
//
//	public ConfigurationDto getWkpDepConfig(int mode, GeneralDate baseDate) {
//		String companyId = AppContexts.user().companyId();
//		switch (mode) {
//		case WORKPLACE_MODE:
//			Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(companyId);
//			if (optWkpConfig.isPresent()) {
//				WorkplaceConfiguration wkpConfig = optWkpConfig.get();
//				Optional<DateHistoryItem> optWkpHistory = wkpConfig.items().stream().filter(i -> i.contains(baseDate))
//						.findFirst();
//				if (optWkpHistory.isPresent()) {
//					DateHistoryItem wkpHistory = optWkpHistory.get();
//					return new ConfigurationDto(wkpHistory.identifier(), wkpHistory.start(), wkpHistory.end());
//				}
//			}
//			return null;
//		case DEPARTMENT_MODE:
//			Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(companyId);
//			if (optDepConfig.isPresent()) {
//				DepartmentConfiguration depConfig = optDepConfig.get();
//				Optional<DateHistoryItem> optDepHistory = depConfig.items().stream().filter(i -> i.contains(baseDate))
//						.findFirst();
//				if (optDepHistory.isPresent()) {
//					DateHistoryItem depHistory = optDepHistory.get();
//					return new ConfigurationDto(depHistory.identifier(), depHistory.start(), depHistory.end());
//				}
//			}
//			return null;
//		default:
//			return null;
//		}
//	}
//
//	public List<ConfigurationDto> getAllWkpDepConfig(int mode) {
//		String companyId = AppContexts.user().companyId();
//		switch (mode) {
//		case WORKPLACE_MODE:
//			Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(companyId);
//			if (!optWkpConfig.isPresent())
//				return null;
//			WorkplaceConfiguration wkpConfig = optWkpConfig.get();
//			return wkpConfig.items().stream().map(
//					wkpHistory -> new ConfigurationDto(wkpHistory.identifier(), wkpHistory.start(), wkpHistory.end()))
//					.collect(Collectors.toList());
//		case DEPARTMENT_MODE:
//			Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(companyId);
//			if (!optDepConfig.isPresent())
//				return null;
//			DepartmentConfiguration depConfig = optDepConfig.get();
//			return depConfig.items().stream().map(
//					depHistory -> new ConfigurationDto(depHistory.identifier(), depHistory.start(), depHistory.end()))
//					.collect(Collectors.toList());
//		default:
//			return null;
//		}
//	}
//
//	public List<InformationDto> getWkpDepInfor(int mode, String historyId) {
//		String companyId = AppContexts.user().companyId();
//		switch (mode) {
//		case WORKPLACE_MODE:
//			List<WorkplaceInformation> listWkp = wkpInforRepo.getAllActiveWorkplaceByCompany(companyId, historyId);
//			return listWkp.stream().map(i -> new InformationDto(i)).collect(Collectors.toList());
//		case DEPARTMENT_MODE:
//			List<DepartmentInformation> listDep = depInforRepo.getAllActiveDepartmentByCompany(companyId, historyId);
//			return listDep.stream().map(i -> new InformationDto(i)).collect(Collectors.toList());
//		default:
//			return null;
//		}
//	}
//
//	public InformationDto getWkpDepInfor(int mode, String historyId, String id) {
//		String companyId = AppContexts.user().companyId();
//		switch (mode) {
//		case WORKPLACE_MODE:
//			Optional<WorkplaceInformation> optWkp = wkpInforRepo.getWorkplaceByKey(companyId, historyId, id);
//			return optWkp.isPresent() ? new InformationDto(optWkp.get()) : null;
//		case DEPARTMENT_MODE:
//			Optional<DepartmentInformation> optDep = depInforRepo.getDepartmentByKey(companyId, historyId, id);
//			return optDep.isPresent() ? new InformationDto(optDep.get()) : null;
//		default:
//			return null;
//		}
//	}
//
//	public void checkTotalWkpDep(int mode, String historyId) {
//		String companyId = AppContexts.user().companyId();
//		switch (mode) {
//		case WORKPLACE_MODE:
//			if (wkpInforRepo.getAllActiveWorkplaceByCompany(companyId, historyId).size() >= MAX_WKP_DEP_NUMBER)
//				throw new BusinessException("Msg_367");
//			break;
//		case DEPARTMENT_MODE:
//			if (depInforRepo.getAllActiveDepartmentByCompany(companyId, historyId).size() >= MAX_WKP_DEP_NUMBER)
//				throw new BusinessException("Msg_367");
//			break;
//		default:
//			break;
//		}
//	}
//
//	public List<WkpDepTreeDto> getWkpDepInforTree(int mode, String historyId) {
//		List<InformationDto> listInfor = this.getWkpDepInfor(mode, historyId);
//		List<WkpDepTreeDto> result = this.createTree(listInfor);
//		return result;
//	}
//
//	private List<WkpDepTreeDto> createTree(List<InformationDto> lstHWkpInfo) {
//		List<WkpDepTreeDto> lstReturn = new ArrayList<>();
//		if (lstHWkpInfo.isEmpty())
//			return lstReturn;
//		// Higher hierarchyCode has shorter length
//		int highestHierarchy = lstHWkpInfo.stream()
//				.min((a, b) -> a.getHierarchyCode().length() - b.getHierarchyCode().length()).get().getHierarchyCode()
//				.length();
//		Iterator<InformationDto> iteratorWkpHierarchy = lstHWkpInfo.iterator();
//		// while have workplace
//		while (iteratorWkpHierarchy.hasNext()) {
//			// pop 1 item
//			InformationDto wkpHierarchy = iteratorWkpHierarchy.next();
//			// convert
//			WkpDepTreeDto dto = new WkpDepTreeDto(wkpHierarchy.getId(), wkpHierarchy.getCode(), wkpHierarchy.getName(),
//					wkpHierarchy.getHierarchyCode(), new ArrayList<>());
//			// build List
//			this.pushToList(lstReturn, dto, wkpHierarchy.getHierarchyCode(), Strings.EMPTY, highestHierarchy);
//		}
//		return lstReturn;
//	}
//
//	private void pushToList(List<WkpDepTreeDto> lstReturn, WkpDepTreeDto dto, String hierarchyCode, String preCode,
//			int highestHierarchy) {
//		if (hierarchyCode.length() == highestHierarchy) {
//			// check duplicate code
//			if (lstReturn.isEmpty()) {
//				lstReturn.add(dto);
//				return;
//			}
//			for (WkpDepTreeDto item : lstReturn) {
//				if (!item.getId().equals(dto.getId())) {
//					lstReturn.add(dto);
//					break;
//				}
//			}
//		} else {
//			String searchCode = preCode.isEmpty() ? preCode + hierarchyCode.substring(0, highestHierarchy)
//					: preCode + hierarchyCode.substring(0, HIERARCHY_LENGTH);
//			Optional<WkpDepTreeDto> optWorkplaceFindDto = lstReturn.stream()
//					.filter(item -> item.getHierarchyCode().equals(searchCode)).findFirst();
//			if (!optWorkplaceFindDto.isPresent()) {
//				return;
//			}
//			List<WkpDepTreeDto> currentItemChilds = optWorkplaceFindDto.get().getChildren();
//			pushToList(currentItemChilds, dto, hierarchyCode.substring(HIERARCHY_LENGTH, hierarchyCode.length()),
//					searchCode, highestHierarchy);
//		}
//	}
//
//	public WkpDepCheckCodeDto checkWkpDepCodeInThePast(int initMode, String historyId, String code) {
//		String companyId = AppContexts.user().companyId();
//		switch (initMode) {
//		case WORKPLACE_MODE:
//			Optional<WorkplaceInformation> optWkpInfor = wkpInforRepo.getActiveWorkplaceByCode(companyId, historyId,
//					code);
//			if (optWkpInfor.isPresent())
//				throw new BusinessException("Msg_3");
//			WorkplacePastCodeCheckOutput outputWkp = wkpQueryService.checkWkpCodeUsedInThePast(companyId, historyId,
//					code);
//			return new WkpDepCheckCodeDto(outputWkp.isUsedInThePast(), outputWkp.isUsedInTheFuture(),
//					outputWkp.getListDuplicatePast().stream()
//							.map(i -> new WkpDepDuplicateCodeDto(i.getTargetId(), i.getTargetCode(), i.getTargetName(),
//									i.getDeleteDate(), i.getHistoryId()))
//							.collect(Collectors.toList()),
//					outputWkp.getListDuplicateFuture()
//							.stream().map(i -> new WkpDepDuplicateCodeDto(i.getTargetId(), i.getTargetCode(),
//									i.getTargetName(), i.getDeleteDate(), i.getHistoryId()))
//							.collect(Collectors.toList()));
//		case DEPARTMENT_MODE:
//			Optional<DepartmentInformation> optDepInfor = depInforRepo.getActiveDepartmentByCode(companyId, historyId,
//					code);
//			if (optDepInfor.isPresent())
//				throw new BusinessException("Msg_3");
//			DepartmentPastCodeCheckOutput outputDep = depQueryService.checkDepCodeUsedInThePast(companyId, historyId,
//					code);
//			return new WkpDepCheckCodeDto(outputDep.isUsedInThePast(), outputDep.isUsedInTheFuture(),
//					outputDep.getListDuplicatePast().stream()
//							.map(i -> new WkpDepDuplicateCodeDto(i.getTargetId(), i.getTargetCode(), i.getTargetName(),
//									i.getDeleteDate(), i.getHistoryId()))
//							.collect(Collectors.toList()),
//					outputDep.getListDuplicateFuture()
//							.stream().map(i -> new WkpDepDuplicateCodeDto(i.getTargetId(), i.getTargetCode(),
//									i.getTargetName(), i.getDeleteDate(), i.getHistoryId()))
//							.collect(Collectors.toList()));
//		default:
//			return null;
//		}
//	}
//
//    public List<WkpDepTreeDto> getDepWkpInfoTree(DepWkpInfoFindObject findObject) {
//        List<InformationDto> listInfo = this.getDepWkpInfo(findObject);
//        List<InformationDto> listInfoHasHierarchyCode = new ArrayList<>();
//		List<InformationDto> listInfoHasNoHierarchyCode = new ArrayList<>();
//		listInfo.forEach(e -> {
//			if (e.getHierarchyCode().isEmpty()) {
//				listInfoHasNoHierarchyCode.add(e);
//			} else {
//				listInfoHasHierarchyCode.add(e);
//			}
//		});
//		List<WkpDepTreeDto> result = this.createTree(listInfoHasHierarchyCode);
//		// convert list no hierarchy code to tree
//		List<WkpDepTreeDto> noHierarchyCodeTree = listInfoHasNoHierarchyCode.stream().map(WkpDepTreeDto::toTreeDto).collect(Collectors.toList());
//		// add list no hierarchy code to the end of the tree list
//		result.addAll(noHierarchyCodeTree);
//        return result;
//    }
//
//    private List<InformationDto> getDepWkpInfo(DepWkpInfoFindObject findObject) {
//
//        String companyId = AppContexts.user().companyId();
//
//        // Check system type.
//        if (findObject.getSystemType() == null) {
//            return Collections.emptyList();
//        }
//        // Check start mode (department or workplace)
//        switch (findObject.getStartMode()) {
//            case WORKPLACE_MODE:
//            	if (findObject.getRestrictionOfReferenceRange()) {
//					List<String> workplaceIdsCanReference = this.syRoleWorkplaceAdapter
//							.findListWkpIdByRoleId(findObject.getSystemType(), findObject.getBaseDate(), false).getListWorkplaceIds();
//            		return wkpExportService.getWorkplaceInforFromWkpIds(companyId, workplaceIdsCanReference, findObject.getBaseDate())
//							.stream().map(InformationDto::new).collect(Collectors.toList());
//				} else {
//					return wkpExportService.getAllActiveWorkplace(companyId, findObject.getBaseDate())
//							.stream().map(InformationDto::new).collect(Collectors.toList());
//				}
//            case DEPARTMENT_MODE:
//				// Pending check filter reference range
//                return depExportSerivce.getAllActiveDepartment(companyId, findObject.getBaseDate())
//						.stream().map(InformationDto::new).collect(Collectors.toList());
//            default:
//                return Collections.emptyList();
//        }
//    }
//
//}
