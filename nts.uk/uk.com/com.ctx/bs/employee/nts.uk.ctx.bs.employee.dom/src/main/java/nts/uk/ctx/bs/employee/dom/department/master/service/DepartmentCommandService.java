//package nts.uk.ctx.bs.employee.dom.department.master.service;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.arc.error.BusinessException;
//import nts.arc.error.RawErrorMessage;
//import nts.arc.time.GeneralDate;
//import nts.gul.text.IdentifierUtil;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfiguration;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfigurationRepository;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformation;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformationRepository;
//import nts.uk.ctx.bs.employee.dom.operationrule.service.AddWkpDepInforParam;
//import nts.uk.shr.com.history.DateHistoryItem;
//import nts.uk.shr.com.time.calendar.period.DatePeriod;
//
//@Stateless
//public class DepartmentCommandService {
//
//	@Inject
//	private DepartmentConfigurationRepository depConfigRepo;
//
//	@Inject
//	private DepartmentInformationRepository depInforRepo;
//
//	/**
//	 * 部門構成を追加する
//	 * 
//	 * @param param
//	 * @return historyId
//	 */
//	public String addDepartmentConfig(AddDepartmentConfigParam param) {
//		if (param.getNewHistoryId() == null)
//			param.setNewHistoryId(IdentifierUtil.randomUniqueId());
//		Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(param.getCompanyId());
//		if (optDepConfig.isPresent()) {
//			DepartmentConfiguration depConfig = optDepConfig.get();
//			depConfig.add(new DateHistoryItem(param.getNewHistoryId(),
//					new DatePeriod(param.getStartDate(), param.getEndDate())));
//			depConfigRepo.updateDepartmentConfig(depConfig);
//		} else {
//			DepartmentConfiguration depConfig = new DepartmentConfiguration(param.getCompanyId(),
//					Arrays.asList(new DateHistoryItem(param.getNewHistoryId(),
//							new DatePeriod(param.getStartDate(), param.getEndDate()))));
//			depConfigRepo.addDepartmentConfig(depConfig);
//		}
//
//		if (param.isCopyPreviousConfig()) {
//			List<DepartmentInformation> listDepartmentPrevHist = depInforRepo
//					.getAllActiveDepartmentByCompany(param.getCompanyId(), param.getPrevHistoryId());
//			List<DepartmentInformation> listDepartmentNewHist = listDepartmentPrevHist.stream()
//					.map(d -> new DepartmentInformation(d.getCompanyId(), d.isDeleteFlag(), param.getNewHistoryId(),
//							d.getDepartmentId(), d.getDepartmentCode(), d.getDepartmentName(), d.getDepartmentGeneric(),
//							d.getDepartmentDisplayName(), d.getHierarchyCode(), d.getDepartmentExternalCode()))
//					.collect(Collectors.toList());
//			depInforRepo.addDepartments(listDepartmentNewHist);
//		}
//		return param.getNewHistoryId();
//	}
//
//	/**
//	 * 部門構成を更新する
//	 * 
//	 * @param param
//	 */
//	public void updateDepartmentConfig(UpdateDepartmentConfigParam param) {
//		Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(param.getCompanyId());
//		if (optDepConfig.isPresent()) {
//			DepartmentConfiguration depConfig = optDepConfig.get();
//			depConfig.items().stream().filter(i -> i.identifier().equals(param.getHistoryId())).findFirst()
//					.ifPresent(itemToBeChanged -> {
//						depConfig.changeSpan(itemToBeChanged, new DatePeriod(param.getStartDate(), param.getEndDate()));
//						depConfigRepo.updateDepartmentConfig(depConfig);
//					});
//		}
//	}
//
//	/**
//	 * 部門構成を削除する
//	 * 
//	 * @param companyId
//	 * @param historyId
//	 */
//	public void deleteDepartmentConfig(String companyId, String historyId) {
//		Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(companyId);
//		if (optDepConfig.isPresent()) {
//			DepartmentConfiguration depConfig = optDepConfig.get();
//			if (depConfig.items().size() == 1) {
//				throw new BusinessException("Msg_57");
//			}
//			depConfig.items().stream().filter(i -> i.identifier().equals(historyId)).findFirst()
//					.ifPresent(itemToBeRemoved -> {
//						if (!itemToBeRemoved.contains(GeneralDate.ymd(9999, 12, 31))) {
//							throw new BusinessException("Msg_55");
//						}
//						depConfig.remove(itemToBeRemoved);
//						depConfig.items().get(0).changeSpan(
//								new DatePeriod(depConfig.items().get(0).start(), GeneralDate.ymd(9999, 12, 31)));
//						depConfigRepo.deleteDepartmentConfig(companyId, historyId);
//						depConfigRepo.updateDepartmentConfig(depConfig);
//					});
//		}
//		depInforRepo.deleteDepartmentInforOfHistory(companyId, historyId);
//	}
//
//	/**
//	 * 部門情報を削除する
//	 * 
//	 * @param companyId
//	 * @param historyId
//	 * @param departmentId
//	 */
//	public void deleteDepartmentInformation(String companyId, String historyId, String departmentId) {
//		Optional<DepartmentInformation> optDep = depInforRepo.getDepartmentByKey(companyId, historyId, departmentId);
//		if (optDep.isPresent()) {
//			DepartmentInformation department = optDep.get();
//			Optional<DepartmentInformation> optDeletedDep = depInforRepo.getDeletedDepartmentByCode(companyId,
//					historyId, department.getDepartmentCode().v());
//			if (optDeletedDep.isPresent()) {
//				depInforRepo.deleteDepartmentInfor(companyId, historyId, optDeletedDep.get().getDepartmentId());
//			}
//			department.delete();
//			depInforRepo.updateDepartment(department);
//		}
//	}
//
//	/**
//	 * 部門情報を登録する
//	 * 
//	 * @param param
//	 * @param isUpdate
//	 */
//	public void registerDepartmentInformation(AddWkpDepInforParam param, boolean isUpdate) {
//		DepartmentInformation department = new DepartmentInformation(param.getCompanyId(), false, param.getHistoryId(),
//				param.getId(), param.getCode(), param.getName(), param.getGenericName(), param.getDispName(),
//				param.getHierarchyCode(), param.getExternalCode());
//		if (department.getDepartmentId() == null)
//			throw new BusinessException(new RawErrorMessage("Cannot register! Department does not exist"));
//		Optional<DepartmentConfiguration> optDepConfig = depConfigRepo.getDepConfig(department.getCompanyId());
//		if (!optDepConfig.isPresent())
//			throw new BusinessException(new RawErrorMessage("Department Configuration not found!"));
//		DepartmentConfiguration depConfig = optDepConfig.get();
//		Optional<DateHistoryItem> optDepHistory = depConfig.items().stream()
//				.filter(i -> i.identifier().equals(department.getDepartmentHistoryId())).findFirst();
//		if (!optDepHistory.isPresent())
//			throw new BusinessException(new RawErrorMessage("Cannot register! History does not exist!"));
//		if (!isUpdate) { // add new
//			if (depInforRepo.getActiveDepartmentByCode(department.getCompanyId(), department.getDepartmentHistoryId(),
//					department.getDepartmentCode().v()).isPresent())
//				throw new BusinessException("Msg_3");
//			depInforRepo.addDepartment(department);
//		} else { // update
//			depInforRepo.updateDepartment(department);
//		}
//		this.updateHierarchyCode(param.getCompanyId(), param.getHistoryId(), param.getMapHierarchyChange());
//	}
//
//	public void updateHierarchyCode(String companyId, String historyId, Map<String, String> mapHierarchyChange) {
//		List<String> listDepId = mapHierarchyChange.keySet().stream().filter(k -> k != null)
//				.collect(Collectors.toList());
//		List<DepartmentInformation> listDepartment = depInforRepo.getActiveDepartmentByDepIds(companyId, historyId,
//				listDepId);
//		listDepartment.forEach(dep -> {
//			dep.changeHierarchyCode(mapHierarchyChange.get(dep.getDepartmentId()));
//			depInforRepo.updateDepartment(dep);
//		});
//	}
//
//}
