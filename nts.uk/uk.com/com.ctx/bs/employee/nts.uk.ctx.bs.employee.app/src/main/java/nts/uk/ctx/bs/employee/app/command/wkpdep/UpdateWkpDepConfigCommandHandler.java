//package nts.uk.ctx.bs.employee.app.command.wkpdep;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.arc.layer.app.command.CommandHandler;
//import nts.arc.layer.app.command.CommandHandlerContext;
//import nts.uk.ctx.bs.employee.dom.operationrule.service.OperationRuleCommandService;
//import nts.uk.ctx.bs.employee.dom.operationrule.service.UpdateWkpDepConfigParam;
//
//@Stateless
//public class UpdateWkpDepConfigCommandHandler extends CommandHandler<UpdateWkpDepConfigCommand> {
//
//	@Inject
//	private OperationRuleCommandService operationCommandService;
//
//	@Override
//	protected void handle(CommandHandlerContext<UpdateWkpDepConfigCommand> context) {
//		UpdateWkpDepConfigCommand command = context.getCommand();
//		UpdateWkpDepConfigParam param = new UpdateWkpDepConfigParam(command.getInitMode(), command.getHistoryId(),
//				command.getStartDate(), command.getEndDate());
//		operationCommandService.updateWkpDepConfig(param);
//	}
//
//}
