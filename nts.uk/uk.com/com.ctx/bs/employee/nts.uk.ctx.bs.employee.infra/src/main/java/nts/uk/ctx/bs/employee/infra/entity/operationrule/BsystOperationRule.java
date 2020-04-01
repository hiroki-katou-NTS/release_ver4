//package nts.uk.ctx.bs.employee.infra.entity.operationrule;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import nts.uk.ctx.bs.employee.dom.operationrule.OperationRule;
//import nts.uk.shr.infra.data.entity.UkJpaEntity;
//
///**
// * 
// * @author HungTT - 運用ルール
// *
// */
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "BSYST_OPERATION_RULE")
//public class BsystOperationRule extends UkJpaEntity {
//
//	@Id
//	@Column(name = "CID")
//	public String companyId;
//
//	@Column(name = "DEP_WKP_SYNCH_ATR")
//	public int depWkpSynchAtr;
//
//	@Override
//	protected Object getKey() {
//		return this.companyId;
//	}
//	
//	public OperationRule toDomain() {
//		return new OperationRule(companyId, depWkpSynchAtr);
//	}
//	
//	public static BsystOperationRule fromDomain(OperationRule domain) {
//		return new BsystOperationRule(domain.getCompanyId(), domain.getDepWkpSynchAtr().value);
//	}
//
//}
