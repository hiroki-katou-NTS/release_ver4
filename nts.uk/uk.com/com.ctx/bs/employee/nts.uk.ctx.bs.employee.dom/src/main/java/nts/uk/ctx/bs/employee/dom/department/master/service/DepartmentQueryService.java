//package nts.uk.ctx.bs.employee.dom.department.master.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfiguration;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfigurationRepository;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformation;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformationRepository;
//import nts.uk.shr.com.history.DateHistoryItem;
//
//@Stateless
//public class DepartmentQueryService {
//
//	@Inject
//	private DepartmentConfigurationRepository depConfigRepo;
//
//	@Inject
//	private DepartmentInformationRepository depInforRepo;
//
//	/**
//	 * 対象の部門コードが過去に使用されているかチェックする
//	 * 
//	 * @param companyId
//	 * @param historyId
//	 * @param depCode
//	 * @return
//	 */
//	public DepartmentPastCodeCheckOutput checkDepCodeUsedInThePast(String companyId, String historyId, String depCode) {
//		List<DepartmentPastCodeOutput> listDuplicatePast = new ArrayList<>();
//		List<DepartmentPastCodeOutput> listDuplicateFuture = new ArrayList<>();
//		Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(companyId);
//		if (optDepConfig.isPresent()) {
//			DepartmentConfiguration depConfig = optDepConfig.get();
//			Optional<DateHistoryItem> optDepHistory = depConfig.items().stream()
//					.filter(i -> i.identifier().equals(historyId)).findFirst();
//			if (!optDepHistory.isPresent())
//				return new DepartmentPastCodeCheckOutput(false, false, listDuplicatePast, listDuplicateFuture);
//			DateHistoryItem depHistory = optDepHistory.get();
//			Optional<DepartmentInformation> optDeletedDep = depInforRepo.getDeletedDepartmentByCode(companyId,
//					historyId, depCode);
//			if (optDeletedDep.isPresent()) {
//				listDuplicatePast.add(new DepartmentPastCodeOutput(optDeletedDep.get().getDepartmentId(),
//						optDeletedDep.get().getDepartmentCode().v(), optDeletedDep.get().getDepartmentName().v(),
//						depHistory.start(), optDeletedDep.get().getDepartmentHistoryId()));
//			}
//			int size = depConfig.items().size();
//			for (int i = 0; i < size; i++) {
//				DateHistoryItem tmpHist = depConfig.items().get(i);
//				if (tmpHist.identifier().equals(depHistory.identifier()))
//					continue;
//				if (tmpHist.start().before(depHistory.start())) {
//					Optional<DepartmentInformation> optPastDep = depInforRepo.getActiveDepartmentByCode(companyId,
//							tmpHist.identifier(), depCode);
//					if (optPastDep.isPresent()) {
//						listDuplicatePast.add(new DepartmentPastCodeOutput(optPastDep.get().getDepartmentId(),
//								optPastDep.get().getDepartmentCode().v(), optPastDep.get().getDepartmentName().v(),
//								tmpHist.start(), optPastDep.get().getDepartmentHistoryId()));
//					}
//				}
//				if (tmpHist.start().after(depHistory.start())) {
//					Optional<DepartmentInformation> optPastDep = depInforRepo.getActiveDepartmentByCode(companyId,
//							tmpHist.identifier(), depCode);
//					if (optPastDep.isPresent()) {
//						listDuplicateFuture.add(new DepartmentPastCodeOutput(optPastDep.get().getDepartmentId(),
//								optPastDep.get().getDepartmentCode().v(), optPastDep.get().getDepartmentName().v(),
//								tmpHist.start(), optPastDep.get().getDepartmentHistoryId()));
//					}
//				}
//			}
//		}
//		return new DepartmentPastCodeCheckOutput(!listDuplicatePast.isEmpty(), !listDuplicateFuture.isEmpty(),
//				listDuplicatePast, listDuplicateFuture);
//	}
//
//}
