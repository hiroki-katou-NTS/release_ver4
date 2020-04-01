//package nts.uk.ctx.bs.employee.pubimp.operationrule;
//
//import java.util.Optional;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.uk.ctx.bs.employee.dom.operationrule.OperationRuleRepository;
//import nts.uk.ctx.bs.employee.pub.operationrule.OperationRuleExport;
//import nts.uk.ctx.bs.employee.pub.operationrule.OperationRulePub;
//
//@Stateless
//public class OperationRulePubImpl implements OperationRulePub {
//
//	@Inject
//	private OperationRuleRepository operationRuleRepo;
//
//	@Override
//	public Optional<OperationRuleExport> getOperationRuleByCompanyId(String companyId) {
//		return operationRuleRepo.getOperationRule(companyId)
//				.map(i -> new OperationRuleExport(i.getCompanyId(), i.getDepWkpSynchAtr().value == 1));
//	}
//
//}
