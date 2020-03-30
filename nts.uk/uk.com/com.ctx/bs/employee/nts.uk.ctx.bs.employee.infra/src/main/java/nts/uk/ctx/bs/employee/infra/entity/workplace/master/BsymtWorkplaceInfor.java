//package nts.uk.ctx.bs.employee.infra.entity.workplace.master;
//
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceInformation;
//import nts.uk.shr.infra.data.entity.UkJpaEntity;
//
///**
// * 
// * @author HungTT - 職場情報
// *
// */
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "BSYMT_WKP_INFO")
//public class BsymtWorkplaceInfor extends UkJpaEntity {
//
//	@EmbeddedId
//	public BsymtWorkplaceInforPk pk;
//
//	@Column(name = "DELETE_FLAG")
//	public boolean deleteFlag;
//
//	@Column(name = "WKP_CD")
//	public String workplaceCode;
//
//	@Column(name = "WKP_NAME")
//	public String workplaceName;
//
//	@Column(name = "WKP_GENERIC")
//	public String workplaceGeneric;
//
//	@Column(name = "WKP_DISP_NAME")
//	public String workplaceDisplayName;
//
//	@Column(name = "HIERARCHY_CD")
//	public String hierarchyCode;
//
//	@Column(name = "WKP_EXTERNAL_CD")
//	@Basic(optional = true)
//	public String workplaceExternalCode;
//
//	@Override
//	protected Object getKey() {
//		return this.pk;
//	}
//
//	public WorkplaceInformation toDomain() {
//		return new WorkplaceInformation(this.pk.companyId, this.deleteFlag, this.pk.workplaceHistoryId,
//				this.pk.workplaceId, this.workplaceCode, this.workplaceName, this.workplaceGeneric,
//				this.workplaceDisplayName, this.hierarchyCode, this.workplaceExternalCode);
//	}
//
//	public static BsymtWorkplaceInfor fromDomain(WorkplaceInformation domain) {
//		return new BsymtWorkplaceInfor(
//				new BsymtWorkplaceInforPk(domain.getCompanyId(), domain.getWorkplaceHistoryId(),
//						domain.getWorkplaceId()),
//				domain.isDeleteFlag(), domain.getWorkplaceCode().v(), domain.getWorkplaceName().v(),
//				domain.getWorkplaceGeneric().v(), domain.getWorkplaceDisplayName().v(), domain.getHierarchyCode().v(),
//				domain.getWorkplaceExternalCode().isPresent() ? domain.getWorkplaceExternalCode().get().v() : null);
//	}
//
//}
