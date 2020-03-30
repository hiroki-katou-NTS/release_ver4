//package nts.uk.ctx.bs.employee.ws.operationrule;
//
//import javax.inject.Inject;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//
//import nts.arc.layer.ws.WebService;
//import nts.uk.ctx.bs.employee.app.find.operationrule.OperationRuleDto;
//import nts.uk.ctx.bs.employee.app.find.operationrule.OperationRuleFinder;
//
//@Path("bs/employee/operationrule")
//@Produces("application/json")
//public class OperationRuleWebService extends WebService {
//
//	@Inject
//	private OperationRuleFinder operationRuleFinder;
//
//	@POST
//	@Path("/get-operation-rule")
//	public OperationRuleDto getOperationRule() {
//		return operationRuleFinder.getOperationRule();
//	}
//
//}
