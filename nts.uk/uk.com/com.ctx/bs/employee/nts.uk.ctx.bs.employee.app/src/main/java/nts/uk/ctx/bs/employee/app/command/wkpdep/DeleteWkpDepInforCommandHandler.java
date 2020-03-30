//package nts.uk.ctx.bs.employee.app.command.wkpdep;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.arc.layer.app.command.CommandHandler;
//import nts.arc.layer.app.command.CommandHandlerContext;
//import nts.uk.ctx.bs.employee.dom.department.master.service.DepartmentExportSerivce;
//import nts.uk.ctx.bs.employee.dom.operationrule.service.DeleteWkpDepInforParam;
//import nts.uk.ctx.bs.employee.dom.operationrule.service.OperationRuleCommandService;
//import nts.uk.ctx.bs.employee.dom.workplace.master.service.WorkplaceExportService;
//import nts.uk.shr.com.context.AppContexts;
//
//@Stateless
//public class DeleteWkpDepInforCommandHandler extends CommandHandler<DeleteWkpDepInforCommand> {
//
//	private static final int WORKPLACE_MODE = 0;
//	private static final int DEPARTMENT_MODE = 1;
//
//	@Inject
//	private WorkplaceExportService wkpExpService;
//
//	@Inject
//	private DepartmentExportSerivce depExpService;
//
//	@Inject
//	private OperationRuleCommandService operationCommandService;
//
//	@Override
//	protected void handle(CommandHandlerContext<DeleteWkpDepInforCommand> context) {
//		String companyId = AppContexts.user().companyId();
//		DeleteWkpDepInforCommand command = context.getCommand();
//		List<String> targetWkpDepId = new ArrayList<>();
//		switch (command.getInitMode()) {
//		case WORKPLACE_MODE:
//			targetWkpDepId = wkpExpService.getWorkplaceIdAndChildren(companyId, command.getHistoryId(),
//					command.getSelectedWkpDepId());
//
//			break;
//		case DEPARTMENT_MODE:
//			targetWkpDepId = depExpService.getDepartmentIdAndChildren(companyId, command.getHistoryId(),
//					command.getSelectedWkpDepId());
//			break;
//		default:
//			break;
//		}
//		targetWkpDepId.forEach(wkpDepId -> {
//			DeleteWkpDepInforParam param = new DeleteWkpDepInforParam(command.getInitMode(), companyId,
//					command.getHistoryId(), wkpDepId);
//			operationCommandService.deleteWkpDepInfor(param);
//		});
//		operationCommandService.updateHierarchyCode(command.getInitMode(), companyId, command.getHistoryId(),
//				command.getListHierarchyChange().stream()
//						.collect(Collectors.toMap(i -> i.getId(), i -> i.getHierarchyCode())));
//	}
//
//}
