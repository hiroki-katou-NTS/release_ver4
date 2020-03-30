//package nts.uk.ctx.bs.employee.dom.department.master.service;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.arc.time.GeneralDate;
//import nts.uk.ctx.bs.employee.dom.department.DepartmentConfig;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfiguration;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfigurationRepository;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformation;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformationRepository;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfiguration;
//import nts.uk.shr.com.history.DateHistoryItem;
//
///**
// * 
// * @author HungTT
// *
// */
//
//@Stateless
//public class DepartmentExportSerivce {
//
//	@Inject
//	private DepartmentConfigurationRepository depConfigRepo;
//
//	@Inject
//	private DepartmentInformationRepository depInforRepo;
//
//	/**
//	 * [No.562]運用している部門の情報をすべて取得する
//	 * 
//	 * @param companyId
//	 * @param baseDate
//	 * @return
//	 */
//	public List<DepartmentInformation> getAllActiveDepartment(String companyId, GeneralDate baseDate) {
//		Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(companyId);
//		if (!optDepConfig.isPresent())
//			return Collections.emptyList();
//		DepartmentConfiguration depConfig = optDepConfig.get();
//		Optional<DateHistoryItem> optDepHist = depConfig.items().stream().filter(i -> i.contains(baseDate)).findFirst();
//		if (!optDepHist.isPresent())
//			return Collections.emptyList();
//		DateHistoryItem depHist = optDepHist.get();
//		List<DepartmentInformation> result = depInforRepo.getAllActiveDepartmentByCompany(companyId,
//				depHist.identifier());
//		result.sort((e1, e2) -> {
//			return e1.getHierarchyCode().v().compareTo(e2.getHierarchyCode().v());
//		});
//		return result;
//	}
//
//	/**
//	 * [No.563]部門IDから部門の情報をすべて取得する
//	 * 
//	 * @param companyId
//	 * @param listDepartmentId
//	 * @param baseDate
//	 * @return
//	 */
//	public List<DepartmentInforParam> getDepartmentInforFromDepIds(String companyId, List<String> listDepartmentId,
//			GeneralDate baseDate) {
//		Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(companyId);
//		if (!optDepConfig.isPresent())
//			return listDepartmentId.stream()
//					.map(d -> new DepartmentInforParam(d, "", "", "コード削除済", "コード削除済", "コード削除済", null))
//					.collect(Collectors.toList());
//		DepartmentConfiguration depConfig = optDepConfig.get();
//		Optional<DateHistoryItem> optDepHist = depConfig.items().stream().filter(i -> i.contains(baseDate)).findFirst();
//		if (!optDepHist.isPresent())
//			return listDepartmentId.stream()
//					.map(d -> new DepartmentInforParam(d, "", "", "コード削除済", "コード削除済", "コード削除済", null))
//					.collect(Collectors.toList());
//		DateHistoryItem depHist = optDepHist.get();
//		List<DepartmentInforParam> result = depInforRepo
//				.getActiveDepartmentByDepIds(companyId, depHist.identifier(), listDepartmentId).stream()
//				.map(d -> new DepartmentInforParam(d.getDepartmentId(), d.getHierarchyCode().v(),
//						d.getDepartmentCode().v(), d.getDepartmentName().v(), d.getDepartmentDisplayName().v(),
//						d.getDepartmentGeneric().v(),
//						d.getDepartmentExternalCode().isPresent() ? d.getDepartmentExternalCode().get().v() : null))
//				.collect(Collectors.toList());
//		List<String> listAcquiredId = result.stream().map(i -> i.getDepartmentId()).collect(Collectors.toList());
//		List<String> listDepIdNoResult = listDepartmentId.stream().filter(i -> !listAcquiredId.contains(i))
//				.collect(Collectors.toList());
//		if (!listDepIdNoResult.isEmpty()) {
//			result.addAll(this.getPastDepartmentInfor(companyId, depHist.identifier(), listDepIdNoResult));
//		}
//		result.sort((e1, e2) -> {
//			return e1.getHierarchyCode().compareTo(e2.getHierarchyCode());
//		});
//		return result;
//	}
//
//	/**
//	 * [No.564]過去の部門の情報を取得する
//	 * 
//	 * @param companyId
//	 * @param depHistId
//	 * @param listDepartmentId
//	 * @return
//	 */
//	public List<DepartmentInforParam> getPastDepartmentInfor(String companyId, String depHistId,
//			List<String> listDepartmentId) {
//		Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(companyId);
//		if (!optDepConfig.isPresent())
//			return listDepartmentId.stream()
//					.map(d -> new DepartmentInforParam(d, "", "", "コード削除済", "コード削除済", "コード削除済", null))
//					.collect(Collectors.toList());
//		DepartmentConfiguration depConfig = optDepConfig.get();
//		Optional<DateHistoryItem> optDepHist = depConfig.items().stream().filter(i -> i.identifier().equals(depHistId))
//				.findFirst();
//		if (!optDepHist.isPresent())
//			return listDepartmentId.stream()
//					.map(d -> new DepartmentInforParam(d, "", "", "コード削除済", "コード削除済", "コード削除済", null))
//					.collect(Collectors.toList());
//		DateHistoryItem depHist = optDepHist.get();
//		int currentIndex = depConfig.items().indexOf(depHist);
//		int size = depConfig.items().size();
//		List<DepartmentInforParam> result = new ArrayList<>();
//		for (int i = currentIndex + 1; i < size; i++) {
//			result.addAll(depInforRepo.getAllDepartmentByDepIds(companyId, depHist.identifier(), listDepartmentId)
//					.stream()
//					.map(d -> new DepartmentInforParam(d.getDepartmentId(), d.getHierarchyCode().v(),
//							d.getDepartmentCode().v(), "マスタ未登録", "マスタ未登録", "マスタ未登録",
//							d.getDepartmentExternalCode().isPresent() ? d.getDepartmentExternalCode().get().v() : null))
//					.collect(Collectors.toList()));
//			List<String> listAcquiredId = result.stream().map(d -> d.getDepartmentId()).collect(Collectors.toList());
//			listDepartmentId = listDepartmentId.stream().filter(id -> !listAcquiredId.contains(id))
//					.collect(Collectors.toList());
//			if (listDepartmentId.isEmpty())
//				break;
//		}
//		if (!listDepartmentId.isEmpty()) {
//			result.addAll(listDepartmentId.stream()
//					.map(d -> new DepartmentInforParam(d, "", "", "コード削除済", "コード削除済", "コード削除済", null))
//					.collect(Collectors.toList()));
//		}
//		result.sort((e1, e2) -> {
//			return e1.getHierarchyCode().compareTo(e2.getHierarchyCode());
//		});
//		return result;
//	}
//
//	/**
//	 * [No.568]部門の下位部門を取得する
//	 * 
//	 * @param companyId
//	 * @param baseDate
//	 * @param parentDepartmentId
//	 * @return
//	 */
//	public List<String> getAllChildDepartmentId(String companyId, GeneralDate baseDate, String parentDepartmentId) {
//		Optional<String> optHistoryId = this.getDepHistByCidAndDate(companyId, baseDate);
//		if (!optHistoryId.isPresent()) {
//			return new ArrayList<>();
//		}
//		String historyId = optHistoryId.get();
//		return this.getAllChildDepartmentId(companyId, historyId, parentDepartmentId);
//	}
//
//    public List<String> getAllChildDepartmentId(String companyId, String historyId, String parentDepartmentId) {
//        List<DepartmentInformation> listDep = depInforRepo.getAllActiveDepartmentByCompany(companyId, historyId);
//        Optional<DepartmentInformation> optParentDep = listDep.stream()
//                .filter(d -> d.getDepartmentId().equals(parentDepartmentId)).findFirst();
//        if (!optParentDep.isPresent())
//            return new ArrayList<>();
//        DepartmentInformation parentDep = optParentDep.get();
//        listDep.remove(parentDep);
//        return listDep.stream().filter(d -> d.getHierarchyCode().v().startsWith(parentDep.getHierarchyCode().v()))
//                .map(d -> d.getDepartmentId()).collect(Collectors.toList());
//    }
//
//	/**
//	 * [No.574]部門の下位部門を基準部門を含めて取得する
//	 * 
//	 * @param companyId
//	 * @param baseDate
//	 * @param departmentId
//	 * @return
//	 */
//	public List<String> getDepartmentIdAndChildren(String companyId, GeneralDate baseDate, String departmentId) {
//		List<String> result = this.getAllChildDepartmentId(companyId, baseDate, departmentId);
//		result.add(departmentId);
//		return result;
//	}
//
//    public List<String> getDepartmentIdAndChildren(String companyId, String historyId, String departmentId) {
//        List<String> result = this.getAllChildDepartmentId(companyId, historyId, departmentId);
//        result.add(departmentId);
//        return result;
//    }
//
//	private Optional<String> getDepHistByCidAndDate(String companyId, GeneralDate baseDate) {
//		Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(companyId);
//		if (optDepConfig.isPresent()) {
//			Optional<DateHistoryItem> optDateHistItem = optDepConfig.get().items().stream().filter(item -> item.span().contains(baseDate)).findFirst();
//			if (optDateHistItem.isPresent()) {
//				return Optional.of(optDateHistItem.get().identifier());
//			}
//		}
//		return Optional.empty();
//	}
//}
