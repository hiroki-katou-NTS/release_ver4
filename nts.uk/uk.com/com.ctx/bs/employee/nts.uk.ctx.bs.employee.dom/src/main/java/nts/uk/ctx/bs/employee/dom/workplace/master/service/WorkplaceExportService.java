//package nts.uk.ctx.bs.employee.dom.workplace.master.service;
//
//import nts.arc.time.GeneralDate;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfiguration;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfigurationRepository;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformation;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformationRepository;
//import nts.uk.shr.com.history.DateHistoryItem;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
///**
// * 
// * @author HungTT
// *
// */
//
//@Stateless
//public class WorkplaceExportService {
//
//	@Inject
//	private WorkplaceConfigurationRepository wkpConfigRepo;
//
//	@Inject
//	private WorkplaceInformationRepository wkpInforRepo;
//
//	/**
//	 * [No.559]運用している職場の情報をすべて取得する (follow EA)
//	 * 
//	 * @param companyId
//	 * @param baseDate
//	 * @return
//	 */
//	public List<WorkplaceInformation> getAllActiveWorkplace(String companyId, GeneralDate baseDate) {
//		Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(companyId);
//		if (!optWkpConfig.isPresent())
//			return Collections.emptyList();
//		WorkplaceConfiguration wkpConfig = optWkpConfig.get();
//		Optional<DateHistoryItem> optWkpHistory = wkpConfig.items().stream().filter(i -> i.contains(baseDate))
//				.findFirst();
//		if (!optWkpHistory.isPresent())
//			return Collections.emptyList();
//		DateHistoryItem wkpHistory = optWkpHistory.get();
//		List<WorkplaceInformation> result = wkpInforRepo.getAllActiveWorkplaceByCompany(companyId, wkpHistory.identifier());
//
//		result.sort((e1, e2) -> {
//			return e1.getHierarchyCode().v().compareTo(e2.getHierarchyCode().v());
//		});
//		return result;
//	}
//
//	/**
//	 * [No.560]職場IDから職場の情報をすべて取得する
//	 * 
//	 * @param companyId
//	 * @param listWorkplaceId
//	 * @param baseDate
//	 * @return
//	 */
//	public List<WorkplaceInforParam> getWorkplaceInforFromWkpIds(String companyId, List<String> listWorkplaceId,
//			GeneralDate baseDate) {
//		Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(companyId);
//		if (!optWkpConfig.isPresent())
//			return listWorkplaceId.stream()
//					.map(w -> new WorkplaceInforParam(w, "", "", "コード削除済", "コード削除済", "コード削除済", null))
//					.collect(Collectors.toList());
//		WorkplaceConfiguration wkpConfig = optWkpConfig.get();
//		Optional<DateHistoryItem> optWkpHistory = wkpConfig.items().stream().filter(i -> i.contains(baseDate))
//				.findFirst();
//		if (!optWkpHistory.isPresent())
//			return listWorkplaceId.stream()
//					.map(w -> new WorkplaceInforParam(w, "", "", "コード削除済", "コード削除済", "コード削除済", null))
//					.collect(Collectors.toList());
//		DateHistoryItem wkpHistory = optWkpHistory.get();
//		List<WorkplaceInforParam> result = wkpInforRepo
//				.getActiveWorkplaceByWkpIds(companyId, wkpHistory.identifier(), listWorkplaceId).stream()
//				.map(w -> new WorkplaceInforParam(w.getWorkplaceId(), w.getHierarchyCode().v(),
//						w.getWorkplaceCode().v(), w.getWorkplaceName().v(), w.getWorkplaceDisplayName().v(),
//						w.getWorkplaceGeneric().v(),
//						w.getWorkplaceExternalCode().isPresent() ? w.getWorkplaceExternalCode().get().v() : null))
//				.collect(Collectors.toList());
//		List<String> listAcquiredWkpId = result.stream().map(w -> w.getWorkplaceId()).collect(Collectors.toList());
//		List<String> listWkpIdNoResult = listWorkplaceId.stream().filter(i -> !listAcquiredWkpId.contains(i))
//				.collect(Collectors.toList());
//		if (!listWkpIdNoResult.isEmpty()) {
//			List<WorkplaceInforParam> listPastWkpInfor = this.getPastWorkplaceInfor(companyId, wkpHistory.identifier(),
//					listWkpIdNoResult);
//			result.addAll(listPastWkpInfor);
//		}
//		result.sort((e1, e2) -> {
//			return e1.getHierarchyCode().compareTo(e2.getHierarchyCode());
//		});
//		return result;
//	}
//
//	/**
//	 * [No.561]過去の職場の情報を取得する
//	 * 
//	 * @param companyId
//	 * @param historyId
//	 * @param listWorkplaceId
//	 * @return
//	 */
//	public List<WorkplaceInforParam> getPastWorkplaceInfor(String companyId, String historyId,
//			List<String> listWorkplaceId) {
//		Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(companyId);
//		if (!optWkpConfig.isPresent())
//			return listWorkplaceId.stream()
//					.map(w -> new WorkplaceInforParam(w, "", "", "コード削除済", "コード削除済", "コード削除済", null))
//					.collect(Collectors.toList());
//		WorkplaceConfiguration wkpConfig = optWkpConfig.get();
//		Optional<DateHistoryItem> optWkpHistory = wkpConfig.items().stream()
//				.filter(i -> i.identifier().equals(historyId)).findFirst();
//		if (!optWkpHistory.isPresent())
//			return listWorkplaceId.stream()
//					.map(w -> new WorkplaceInforParam(w, "", "", "コード削除済", "コード削除済", "コード削除済", null))
//					.collect(Collectors.toList());
//		DateHistoryItem wkpHistory = optWkpHistory.get();
//		int currentIndex = wkpConfig.items().indexOf(wkpHistory);
//		int size = wkpConfig.items().size();
//		List<WorkplaceInforParam> result = new ArrayList<>();
//		for (int i = currentIndex + 1; i < size; i++) {
//			result.addAll(wkpInforRepo
//					.getAllWorkplaceByWkpIds(companyId, wkpConfig.items().get(i).identifier(), listWorkplaceId)
//					.stream()
//					.map(w -> new WorkplaceInforParam(w.getWorkplaceId(), w.getHierarchyCode().v(),
//							w.getWorkplaceCode().v(), "マスタ未登録", "マスタ未登録", "マスタ未登録",
//							w.getWorkplaceExternalCode().isPresent() ? w.getWorkplaceExternalCode().get().v() : null))
//					.collect(Collectors.toList()));
//			List<String> listAcquiredWkpId = result.stream().map(w -> w.getWorkplaceId()).collect(Collectors.toList());
//			listWorkplaceId = listWorkplaceId.stream().filter(id -> !listAcquiredWkpId.contains(id))
//					.collect(Collectors.toList());
//			if (listWorkplaceId.isEmpty())
//				break;
//		}
//		if (!listWorkplaceId.isEmpty()) {
//			result.addAll(listWorkplaceId.stream()
//					.map(w -> new WorkplaceInforParam(w, "", "", "コード削除済", "コード削除済", "コード削除済", null))
//					.collect(Collectors.toList()));
//		}
//		result.sort((e1, e2) -> {
//			return e1.getHierarchyCode().compareTo(e2.getHierarchyCode());
//		});
//		return result;
//	}
//
//	public List<String> getAllChildWorkplaceId(String companyId, String historyId, String parentWorkplaceId) {
//		List<WorkplaceInformation> listWkp = wkpInforRepo.getAllActiveWorkplaceByCompany(companyId, historyId);
//		Optional<WorkplaceInformation> optParentWkp = listWkp.stream()
//				.filter(w -> w.getWorkplaceId().equals(parentWorkplaceId)).findFirst();
//		if (!optParentWkp.isPresent())
//			return new ArrayList<>();
//		WorkplaceInformation parentWkp = optParentWkp.get();
//		listWkp.remove(parentWkp);
//		return listWkp.stream().filter(w -> w.getHierarchyCode().v().startsWith(parentWkp.getHierarchyCode().v()))
//				.map(w -> w.getWorkplaceId()).collect(Collectors.toList());
//	}
//
//	/**
//	 * [No.567]職場の下位職場を取得する
//	 *
//	 * @param companyId
//	 * @param baseDate
//	 * @param parentWorkplaceId
//	 * @return
//	 */
//	public List<String> getAllChildWorkplaceId(String companyId, GeneralDate baseDate, String parentWorkplaceId) {
//		Optional<String> optHistoryId = this.getWkpHistByCidAndDate(companyId, baseDate);
//		if (!optHistoryId.isPresent()) {
//			return new ArrayList<>();
//		}
//		String historyId = optHistoryId.get();
//		return this.getAllChildWorkplaceId(companyId, historyId, parentWorkplaceId);
//	}
//
//	public List<String> getWorkplaceIdAndChildren(String companyId, String historyId, String workplaceId) {
//		List<String> result = this.getAllChildWorkplaceId(companyId, historyId, workplaceId);
//		result.add(workplaceId);
//		return result;
//	}
//
//	/**
//	 * [No.573]職場の下位職場を基準職場を含めて取得する
//	 *
//	 * @param companyId
//	 * @param baseDate
//	 * @param workplaceId
//	 * @return
//	 */
//	public List<String> getWorkplaceIdAndChildren(String companyId, GeneralDate baseDate, String workplaceId) {
//		List<String> result = this.getAllChildWorkplaceId(companyId, baseDate, workplaceId);
//		result.add(workplaceId);
//		return result;
//	}
//
//	private Optional<String> getWkpHistByCidAndDate(String companyId, GeneralDate baseDate) {
//		Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(companyId);
//		if (optWkpConfig.isPresent()) {
//			Optional<DateHistoryItem> optDateHistItem = optWkpConfig.get().items().stream().filter(item -> item.span().contains(baseDate)).findFirst();
//			if (optDateHistItem.isPresent()) {
//				return Optional.of(optDateHistItem.get().identifier());
//			}
//		}
//		return Optional.empty();
//	}
//}
