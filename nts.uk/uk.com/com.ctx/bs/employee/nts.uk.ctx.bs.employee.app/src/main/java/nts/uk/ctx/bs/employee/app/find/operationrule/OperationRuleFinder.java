//package nts.uk.ctx.bs.employee.app.find.operationrule;
//
//import java.util.Optional;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//
//import nts.arc.error.BusinessException;
//import nts.arc.error.RawErrorMessage;
//import nts.uk.ctx.bs.employee.dom.operationrule.DepartmentWorkplaceSynchronizationAtr;
//import nts.uk.ctx.bs.employee.dom.operationrule.OperationRule;
//import nts.uk.ctx.bs.employee.dom.operationrule.OperationRuleRepository;
//import nts.uk.shr.com.context.AppContexts;
//
//@Stateless
//public class OperationRuleFinder {
//
//	@Inject
//	private OperationRuleRepository operationRuleRepo;
//
//	public OperationRuleDto getOperationRule() {
//		String companyId = AppContexts.user().companyId();
//		Optional<OperationRule> optOperationRule = operationRuleRepo.getOperationRule(companyId);
//		if (!optOperationRule.isPresent())
//			throw new BusinessException(new RawErrorMessage("OperationRule not found!"));
//		return new OperationRuleDto(
//				optOperationRule.get().getDepWkpSynchAtr() == DepartmentWorkplaceSynchronizationAtr.SYNCHRONIZED);
//	}
//}
