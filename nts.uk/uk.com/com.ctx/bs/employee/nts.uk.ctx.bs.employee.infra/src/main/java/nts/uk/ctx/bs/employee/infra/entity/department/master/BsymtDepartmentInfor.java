//package nts.uk.ctx.bs.employee.infra.entity.department.master;
//
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentInformation;
//import nts.uk.shr.infra.data.entity.UkJpaEntity;
//
///**
// * 
// * @author HungTT - 部門情報
// *
// */
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "BSYMT_DEP_INFO")
//public class BsymtDepartmentInfor extends UkJpaEntity {
//
//	@EmbeddedId
//	public BsymtDepartmentInforPk pk;
//
//	@Column(name = "DELETE_FLAG")
//	public boolean deleteFlag;
//
//	@Column(name = "DEP_CD")
//	public String departmentCode;
//
//	@Column(name = "DEP_NAME")
//	public String departmentName;
//
//	@Column(name = "DEP_GENERIC")
//	public String departmentGeneric;
//
//	@Column(name = "DEP_DISP_NAME")
//	public String departmentDisplayName;
//
//	@Column(name = "HIERARCHY_CD")
//	public String hierarchyCode;
//
//	@Column(name = "DEP_EXTERNAL_CD")
//	@Basic(optional = true)
//	public String departmentExternalCode;
//
//	@Override
//	protected Object getKey() {
//		return this.pk;
//	}
//
//	public DepartmentInformation toDomain() {
//		return new DepartmentInformation(this.pk.companyId, this.deleteFlag, this.pk.departmentHistoryId,
//				this.pk.departmentId, this.departmentCode, this.departmentName, this.departmentGeneric,
//				this.departmentDisplayName, this.hierarchyCode, this.departmentExternalCode);
//	}
//
//	public static BsymtDepartmentInfor fromDomain(DepartmentInformation domain) {
//		return new BsymtDepartmentInfor(
//				new BsymtDepartmentInforPk(domain.getCompanyId(), domain.getDepartmentHistoryId(),
//						domain.getDepartmentId()),
//				domain.isDeleteFlag(), domain.getDepartmentCode().v(), domain.getDepartmentName().v(),
//				domain.getDepartmentGeneric().v(), domain.getDepartmentDisplayName().v(), domain.getHierarchyCode().v(),
//				domain.getDepartmentExternalCode().isPresent() ? domain.getDepartmentExternalCode().get().v() : null);
//	}
//
//}
