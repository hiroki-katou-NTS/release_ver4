//package nts.uk.ctx.bs.employee.infra.repository.operationrule;
//
//import java.util.Optional;
//
//import javax.ejb.Stateless;
//
//import nts.arc.layer.infra.data.JpaRepository;
//import nts.uk.ctx.bs.employee.dom.operationrule.OperationRule;
//import nts.uk.ctx.bs.employee.dom.operationrule.OperationRuleRepository;
//import nts.uk.ctx.bs.employee.infra.entity.operationrule.BsystOperationRule;
//
///**
// * 
// * @author HungTT
// *
// */
//
//@Stateless
//public class JpaOperationRuleRepository extends JpaRepository implements OperationRuleRepository {
//
//	@Override
//	public Optional<OperationRule> getOperationRule(String companyId) {
//		return this.queryProxy().find(companyId, BsystOperationRule.class).map(BsystOperationRule::toDomain);
//	}
//
//}
