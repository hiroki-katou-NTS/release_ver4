//package nts.uk.ctx.bs.employee.pubimp.workplace.master;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.arc.time.GeneralDate;
//import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory;
//import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItem;
//import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItemRepository;
//import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository;
//import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformation;
//import nts.uk.ctx.bs.employee.dom.workplace.master.service.WorkplaceExportService;
//import nts.uk.ctx.bs.employee.dom.workplace.master.service.WorkplaceInforParam;
//import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
//import nts.uk.ctx.bs.employee.pub.workplace.master.WorkplaceInforExport;
//import nts.uk.ctx.bs.employee.pub.workplace.master.WorkplacePub;
//import nts.uk.shr.com.context.AppContexts;
//
//@Stateless
//public class NewWorkplacePubImpl implements WorkplacePub {
//
//	@Inject
//	private WorkplaceExportService wkpExpService;
//
//    @Inject
//    private AffWorkplaceHistoryRepository affWkpHistRepo;
//
//    @Inject
//    private AffWorkplaceHistoryItemRepository affWkpHistItemRepo;
//
//	@Override
//	public List<WorkplaceInforExport> getWorkplaceInforByWkpIds(String companyId, List<String> listWorkplaceId,
//			GeneralDate baseDate) {
//		return wkpExpService.getWorkplaceInforFromWkpIds(companyId, listWorkplaceId, baseDate).stream()
//				.map(i -> new WorkplaceInforExport(i.getWorkplaceId(), i.getHierarchyCode(), i.getWorkplaceCode(),
//						i.getWorkplaceName(), i.getDisplayName(), i.getGenericName(), i.getExternalCode()))
//				.collect(Collectors.toList());
//	}
//
//	@Override
//	public List<WorkplaceInforExport> getAllActiveWorkplaceInfor(String companyId, GeneralDate baseDate) {
//		return wkpExpService.getAllActiveWorkplace(companyId, baseDate).stream()
//				.map(i -> new WorkplaceInforExport(i.getWorkplaceId(), i.getHierarchyCode().v(),
//						i.getWorkplaceCode().v(), i.getWorkplaceName().v(), i.getWorkplaceDisplayName().v(),
//						i.getWorkplaceGeneric().v(),
//						i.getWorkplaceExternalCode().isPresent() ? i.getWorkplaceExternalCode().get().v() : null))
//				.collect(Collectors.toList());
//	}
//
//	@Override
//	public List<WorkplaceInforExport> getPastWorkplaceInfor(String companyId, String historyId,
//			List<String> listWorkplaceId) {
//		return wkpExpService.getPastWorkplaceInfor(companyId, historyId, listWorkplaceId).stream()
//				.map(i -> new WorkplaceInforExport(i.getWorkplaceId(), i.getHierarchyCode(), i.getWorkplaceCode(),
//						i.getWorkplaceName(), i.getDisplayName(), i.getGenericName(), i.getExternalCode()))
//				.collect(Collectors.toList());
//	}
//
//    @Override
//    public List<String> getAllChildrenOfWorkplaceId(String companyId, GeneralDate baseDate, String parentWorkplaceId) {
//        return wkpExpService.getAllChildWorkplaceId(companyId, baseDate, parentWorkplaceId);
//    }
//
//    @Override
//    public List<String> getWorkplaceIdAndChildren(String companyId, GeneralDate baseDate, String workplaceId) {
//        return wkpExpService.getWorkplaceIdAndChildren(companyId, baseDate, workplaceId);
//    }
//
//    @Override
//    public Optional<SWkpHistExport> findBySid(String employeeId, GeneralDate baseDate) {
//        // get AffWorkplaceHistory
//        Optional<AffWorkplaceHistory> affWrkPlc = affWkpHistRepo.getByEmpIdAndStandDate(employeeId,
//                baseDate);
//        if (!affWrkPlc.isPresent())
//            return Optional.empty();
//
//        // get AffWorkplaceHistoryItem
//        String historyId = affWrkPlc.get().getHistoryItems().get(0).identifier();
//        Optional<AffWorkplaceHistoryItem> affWrkPlcItem = affWkpHistItemRepo.getByHistId(historyId);
//        if (!affWrkPlcItem.isPresent())
//            return Optional.empty();
//
//        // Get workplace info.
//        String companyId = AppContexts.user().companyId();
//        String workplaceId = affWrkPlcItem.get().getWorkplaceId();
//        List<WorkplaceInforParam> lstWkpInfo = wkpExpService.getWorkplaceInforFromWkpIds(companyId, Arrays.asList(workplaceId), baseDate);
//
//        // Check exist
//        if (lstWkpInfo.isEmpty()) {
//            return Optional.empty();
//        }
//
//        // Return workplace id
//        WorkplaceInforParam param = lstWkpInfo.get(0);
//
//        return Optional.of(SWkpHistExport.builder().dateRange(affWrkPlc.get().getHistoryItems().get(0).span())
//                .employeeId(affWrkPlc.get().getEmployeeId()).workplaceId(param.getWorkplaceId())
//                .workplaceCode(param.getWorkplaceCode()).workplaceName(param.getWorkplaceName())
//                .wkpDisplayName(param.getDisplayName())
//                .build());
//    }
//
//}
