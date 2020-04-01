//package nts.uk.ctx.bs.employee.app.command.wkpdep;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.arc.layer.app.command.CommandHandlerContext;
//import nts.arc.layer.app.command.CommandHandlerWithResult;
//import nts.uk.ctx.bs.employee.dom.operationrule.service.AddWkpDepConfigParam;
//import nts.uk.ctx.bs.employee.dom.operationrule.service.OperationRuleCommandService;
//
//@Stateless
//public class AddWkpDepConfigCommandHandler extends CommandHandlerWithResult<AddWkpDepConfigCommand, String> {
//
//	@Inject
//	private OperationRuleCommandService operationCommandService;
//
//	@Override
//	protected String handle(CommandHandlerContext<AddWkpDepConfigCommand> context) {
//		AddWkpDepConfigCommand command = context.getCommand();
//		AddWkpDepConfigParam param = new AddWkpDepConfigParam(command.getInitMode(), command.getNewHistoryId(),
//				command.getPrevHistoryId(), command.getStartDate(), command.getEndDate(),
//				command.isCopyPreviousConfig());
//		return operationCommandService.addWkpDepConfig(param);
//	}
//
//}
