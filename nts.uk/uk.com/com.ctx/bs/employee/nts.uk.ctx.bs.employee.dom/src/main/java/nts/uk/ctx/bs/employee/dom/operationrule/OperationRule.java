//package nts.uk.ctx.bs.employee.dom.operationrule;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import nts.arc.layer.dom.AggregateRoot;
//
///**
// * 
// * @author HungTT - 運用ルール
// *
// */
//
//@Getter
//@AllArgsConstructor
//public class OperationRule extends AggregateRoot {
//
//	/**
//	 * 会社ID
//	 */
//	private String companyId;
//
//	/**
//	 * 部門職場同期区分
//	 */
//	private DepartmentWorkplaceSynchronizationAtr depWkpSynchAtr;
//
//	public OperationRule(String companyId, int depWkpSynchAtr) {
//		this.companyId = companyId;
//		this.depWkpSynchAtr = DepartmentWorkplaceSynchronizationAtr.of(depWkpSynchAtr);
//	}
//	
//}
