//package nts.uk.ctx.bs.employee.app.command.wkpdep;
//
//import java.util.stream.Collectors;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.arc.layer.app.command.CommandHandlerContext;
//import nts.arc.layer.app.command.CommandHandlerWithResult;
//import nts.uk.ctx.bs.employee.dom.operationrule.service.AddWkpDepInforParam;
//import nts.uk.ctx.bs.employee.dom.operationrule.service.OperationRuleCommandService;
//import nts.uk.shr.com.context.AppContexts;
//
//@Stateless
//public class RegisterWkpDepInforCommandHandler extends CommandHandlerWithResult<RegisterWkpDepInforCommand, String> {
//
//	@Inject
//	private OperationRuleCommandService operationCommandService;
//
//	@Override
//	protected String handle(CommandHandlerContext<RegisterWkpDepInforCommand> context) {
//		String companyId = AppContexts.user().companyId();
//		RegisterWkpDepInforCommand command = context.getCommand();
//		AddWkpDepInforParam param = new AddWkpDepInforParam(command.getInitMode(), companyId, command.getHistoryId(),
//				command.getId(), command.getCode(), command.getName(), command.getDispName(), command.getGenericName(),
//				command.getExternalCode(), command.getHierarchyCode(), command.getListHierarchyChange().stream()
//						.collect(Collectors.toMap(i -> i.getId(), i -> i.getHierarchyCode())), command.isUpdateMode());
//		return operationCommandService.registerWkpDepInformation(param);
//	}
//
//}
