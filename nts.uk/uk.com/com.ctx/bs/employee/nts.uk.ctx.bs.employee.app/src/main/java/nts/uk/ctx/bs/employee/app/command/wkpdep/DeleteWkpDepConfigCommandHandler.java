//package nts.uk.ctx.bs.employee.app.command.wkpdep;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.arc.layer.app.command.CommandHandler;
//import nts.arc.layer.app.command.CommandHandlerContext;
//import nts.uk.ctx.bs.employee.dom.operationrule.service.OperationRuleCommandService;
//import nts.uk.shr.com.context.AppContexts;
//
//@Stateless
//public class DeleteWkpDepConfigCommandHandler extends CommandHandler<DeleteWkpDepConfigCommand> {
//
//	@Inject
//	private OperationRuleCommandService operationCommandService;
//
//	@Override
//	protected void handle(CommandHandlerContext<DeleteWkpDepConfigCommand> context) {
//		String companyId = AppContexts.user().companyId();
//		operationCommandService.deleteWkpDepConfig(context.getCommand().getInitMode(), companyId,
//				context.getCommand().getHistoryId());
//	}
//
//}
