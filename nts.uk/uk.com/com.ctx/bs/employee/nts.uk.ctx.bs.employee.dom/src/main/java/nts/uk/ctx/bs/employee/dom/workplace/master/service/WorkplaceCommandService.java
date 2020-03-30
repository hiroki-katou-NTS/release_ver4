//package nts.uk.ctx.bs.employee.dom.workplace.master.service;
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
//import nts.uk.ctx.bs.employee.dom.operationrule.service.AddWkpDepInforParam;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfiguration;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfigurationRepository;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformation;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformationRepository;
//import nts.uk.shr.com.history.DateHistoryItem;
//import nts.uk.shr.com.time.calendar.period.DatePeriod;
//
//@Stateless
//public class WorkplaceCommandService {
//
//	@Inject
//	private WorkplaceConfigurationRepository wkpConfigRepo;
//
//	@Inject
//	private WorkplaceInformationRepository wkpInforRepo;
//
//	/**
//	 * 職場構成を追加する
//	 * 
//	 * @param param
//	 * @return historyId
//	 */
//	public String addWorkplaceConfig(AddWorkplaceConfigParam param) {
//		if (param.getNewHistoryId() == null)
//			param.setNewHistoryId(IdentifierUtil.randomUniqueId());
//		Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(param.getCompanyId());
//		if (optWkpConfig.isPresent()) {
//			WorkplaceConfiguration wkpConfig = optWkpConfig.get();
//			wkpConfig.add(new DateHistoryItem(param.getNewHistoryId(),
//					new DatePeriod(param.getStartDate(), param.getEndDate())));
//			wkpConfigRepo.updateWorkplaceConfig(wkpConfig);
//		} else {
//			WorkplaceConfiguration wkpConfig = new WorkplaceConfiguration(param.getCompanyId(),
//					Arrays.asList(new DateHistoryItem(param.getNewHistoryId(),
//							new DatePeriod(param.getStartDate(), param.getEndDate()))));
//			wkpConfigRepo.addWorkplaceConfig(wkpConfig);
//		}
//		if (param.isCopyPreviousConfig()) {
//			List<WorkplaceInformation> listWorkplacePrevHist = wkpInforRepo
//					.getAllActiveWorkplaceByCompany(param.getCompanyId(), param.getPrevHistoryId());
//			List<WorkplaceInformation> listWorkplaceNewHist = listWorkplacePrevHist.stream()
//					.map(d -> new WorkplaceInformation(d.getCompanyId(), d.isDeleteFlag(), param.getNewHistoryId(),
//							d.getWorkplaceId(), d.getWorkplaceCode(), d.getWorkplaceName(), d.getWorkplaceGeneric(),
//							d.getWorkplaceDisplayName(), d.getHierarchyCode(), d.getWorkplaceExternalCode()))
//					.collect(Collectors.toList());
//			wkpInforRepo.addWorkplaces(listWorkplaceNewHist);
//		}
//		return param.getNewHistoryId();
//	}
//
//	/**
//	 * 職場構成を更新する
//	 * 
//	 * @param param
//	 */
//	public void updateWorkplaceConfig(UpdateWorkplaceConfigParam param) {
//		Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(param.getCompanyId());
//		if (optWkpConfig.isPresent()) {
//			WorkplaceConfiguration wkpConfig = optWkpConfig.get();
//			wkpConfig.items().stream().filter(i -> i.identifier().equals(param.getHistoryId())).findFirst()
//					.ifPresent(itemToBeChanged -> {
//						wkpConfig.changeSpan(itemToBeChanged, new DatePeriod(param.getStartDate(), param.getEndDate()));
//						wkpConfigRepo.updateWorkplaceConfig(wkpConfig);
//					});
//		}
//	}
//
//	/**
//	 * 職場構成を削除する
//	 * 
//	 * @param companyId
//	 * @param historyId
//	 */
//	public void deleteWorkplaceConfig(String companyId, String historyId) {
//		Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(companyId);
//		if (optWkpConfig.isPresent()) {
//			WorkplaceConfiguration wkpConfig = optWkpConfig.get();
//			if (wkpConfig.items().size() == 1) {
//				throw new BusinessException("Msg_57");
//			}
//			wkpConfig.items().stream().filter(i -> i.identifier().equals(historyId)).findFirst()
//					.ifPresent(itemToBeRemoved -> {
//						if (!itemToBeRemoved.contains(GeneralDate.ymd(9999, 12, 31))) {
//							throw new BusinessException("Msg_55");
//						}
//						wkpConfig.remove(itemToBeRemoved);
//						wkpConfig.items().get(0).changeSpan(
//								new DatePeriod(wkpConfig.items().get(0).start(), GeneralDate.ymd(9999, 12, 31)));
//						wkpConfigRepo.deleteWorkplaceConfig(companyId, historyId);
//						wkpConfigRepo.updateWorkplaceConfig(wkpConfig);
//					});
//		}
//		wkpInforRepo.deleteWorkplaceInforOfHistory(companyId, historyId);
//	}
//
//	/**
//	 * 職場情報を削除する
//	 * 
//	 * @param companyId
//	 * @param historyId
//	 * @param workplaceId
//	 */
//	public void deleteWorkplaceInformation(String companyId, String historyId, String workplaceId) {
//		Optional<WorkplaceInformation> optWkp = wkpInforRepo.getWorkplaceByKey(companyId, historyId, workplaceId);
//		if (optWkp.isPresent()) {
//			WorkplaceInformation workplace = optWkp.get();
//			Optional<WorkplaceInformation> optDeletedWkp = wkpInforRepo.getDeletedWorkplaceByCode(companyId, historyId,
//					workplace.getWorkplaceCode().v());
//			if (optDeletedWkp.isPresent()) {
//				wkpInforRepo.deleteWorkplaceInfor(companyId, historyId, optDeletedWkp.get().getWorkplaceId());
//			}
//			workplace.delete();
//			wkpInforRepo.updateWorkplace(workplace);
//		}
//	}
//
//	/**
//	 * 職場情報を登録する
//	 * 
//	 * @param param
//	 * @param isUpdate
//	 */
//	public void registerWorkplaceInformation(AddWkpDepInforParam param, boolean isUpdate) {
//		WorkplaceInformation workplace = new WorkplaceInformation(param.getCompanyId(), false, param.getHistoryId(),
//				param.getId(), param.getCode(), param.getName(), param.getGenericName(), param.getDispName(),
//				param.getHierarchyCode(), param.getExternalCode());
//		if (workplace.getWorkplaceId() == null)
//			throw new BusinessException(new RawErrorMessage("Cannot register! Workplace does not exist!"));
//		Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(workplace.getCompanyId());
//		if (!optWkpConfig.isPresent())
//			throw new BusinessException(new RawErrorMessage("Workplace Configuration not found!"));
//		WorkplaceConfiguration wkpConfig = optWkpConfig.get();
//		Optional<DateHistoryItem> optWkpHistory = wkpConfig.items().stream()
//				.filter(i -> i.identifier().equals(workplace.getWorkplaceHistoryId())).findFirst();
//		if (!optWkpHistory.isPresent())
//			throw new BusinessException(new RawErrorMessage("Cannot register! History does not exist!"));
//		if (!isUpdate) { // add new
//			if (wkpInforRepo.getActiveWorkplaceByCode(workplace.getCompanyId(), workplace.getWorkplaceHistoryId(),
//					workplace.getWorkplaceCode().v()).isPresent())
//				throw new BusinessException("Msg_3");
//			wkpInforRepo.addWorkplace(workplace);
//		} else { // update
//			wkpInforRepo.updateWorkplace(workplace);
//		}
//		this.updateHierarchyCode(param.getCompanyId(), param.getHistoryId(), param.getMapHierarchyChange());
//	}
//
//	public void updateHierarchyCode(String companyId, String historyId, Map<String, String> mapHierarchyChange) {
//		List<String> listWkpId = mapHierarchyChange.keySet().stream().filter(k -> k != null)
//				.collect(Collectors.toList());
//		List<WorkplaceInformation> listWorkplace = wkpInforRepo.getActiveWorkplaceByWkpIds(companyId, historyId,
//				listWkpId);
//		listWorkplace.forEach(wkp -> {
//			wkp.changeHierarchyCode(mapHierarchyChange.get(wkp.getWorkplaceId()));
//			wkpInforRepo.updateWorkplace(wkp);
//		});
//	}
//
//}
