//package nts.uk.query.ac.operationrule;
//
//import nts.uk.ctx.bs.employee.pub.operationrule.OperationRulePub;
//import nts.uk.query.model.operationrule.QueryOperationRuleAdapter;
//import nts.uk.query.model.operationrule.OperationRuleImport;
//
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//import java.util.Optional;
//
//@Stateless
//public class QueryOperationRuleAdapterImpl implements QueryOperationRuleAdapter {
//
//    @Inject
//    private OperationRulePub operationRulePub;
//
//    @Override
//    public Optional<OperationRuleImport> getOperationRuleByCompanyId(String companyId) {
//        return operationRulePub.getOperationRuleByCompanyId(companyId).map(o -> new OperationRuleImport(o.getCompanyId(), o.isSynchronization()));
//    }
//
//}
