//package nts.uk.ctx.bs.employee.dom.workplace.master.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfiguration;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfigurationRepository;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformation;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformationRepository;
//import nts.uk.shr.com.history.DateHistoryItem;
//
//@Stateless
//public class WorkplaceQueryService {
//
//	@Inject
//	private WorkplaceConfigurationRepository wkpConfigRepo;
//
//	@Inject
//	private WorkplaceInformationRepository wkpInforRepo;
//
//	/**
//	 * 対象の職場コードが過去に使用されているかチェックする
//	 * 
//	 * @param companyId
//	 * @param historyId
//	 * @param wkpCode
//	 * @return
//	 */
//	public WorkplacePastCodeCheckOutput checkWkpCodeUsedInThePast(String companyId, String historyId, String wkpCode) {
//		List<WorkplacePastCodeOutput> listDuplicatePast = new ArrayList<>();
//		List<WorkplacePastCodeOutput> listDuplicateFuture = new ArrayList<>();
//		Optional<WorkplaceConfiguration> optWkpConfig = wkpConfigRepo.getWkpConfig(companyId);
//		if (optWkpConfig.isPresent()) {
//			WorkplaceConfiguration wkpConfig = optWkpConfig.get();
//			Optional<DateHistoryItem> optWkpHistory = wkpConfig.items().stream()
//					.filter(i -> i.identifier().equals(historyId)).findFirst();
//			if (!optWkpHistory.isPresent())
//				return new WorkplacePastCodeCheckOutput(false, false, listDuplicatePast, listDuplicateFuture);
//			DateHistoryItem wkpHistory = optWkpHistory.get();
//			Optional<WorkplaceInformation> optDeletdWkp = wkpInforRepo.getDeletedWorkplaceByCode(companyId, historyId,
//					wkpCode);
//			if (optDeletdWkp.isPresent()) {
//				listDuplicatePast.add(new WorkplacePastCodeOutput(optDeletdWkp.get().getWorkplaceId(),
//						optDeletdWkp.get().getWorkplaceCode().v(), optDeletdWkp.get().getWorkplaceName().v(),
//						wkpHistory.start(), optDeletdWkp.get().getWorkplaceHistoryId()));
//			}
//			int size = wkpConfig.items().size();
//			for (int i = 0; i < size; i++) {
//				DateHistoryItem tmpHist = wkpConfig.items().get(i);
//				if (tmpHist.start().before(wkpHistory.start())) {
//					Optional<WorkplaceInformation> optPastWkp = wkpInforRepo.getActiveWorkplaceByCode(companyId,
//							tmpHist.identifier(), wkpCode);
//					if (optPastWkp.isPresent()) {
//						listDuplicatePast.add(new WorkplacePastCodeOutput(optPastWkp.get().getWorkplaceId(),
//								optPastWkp.get().getWorkplaceCode().v(), optPastWkp.get().getWorkplaceName().v(), tmpHist.start(),
//								optPastWkp.get().getWorkplaceHistoryId()));
//					}
//				}
//				if (tmpHist.start().after(wkpHistory.start())) {
//					Optional<WorkplaceInformation> optPastWkp = wkpInforRepo.getActiveWorkplaceByCode(companyId,
//							tmpHist.identifier(), wkpCode);
//					if (optPastWkp.isPresent()) {
//						listDuplicateFuture.add(new WorkplacePastCodeOutput(optPastWkp.get().getWorkplaceId(),
//								optPastWkp.get().getWorkplaceCode().v(), optPastWkp.get().getWorkplaceName().v(), tmpHist.start(),
//								optPastWkp.get().getWorkplaceHistoryId()));
//					}
//				}
//			}
//		}
//		return new WorkplacePastCodeCheckOutput(!listDuplicatePast.isEmpty(), !listDuplicateFuture.isEmpty(),
//				listDuplicatePast, listDuplicateFuture);
//	}
//
//}
