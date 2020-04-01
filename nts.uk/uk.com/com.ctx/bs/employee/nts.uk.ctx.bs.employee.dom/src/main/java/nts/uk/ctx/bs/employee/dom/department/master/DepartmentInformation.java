//package nts.uk.ctx.bs.employee.dom.department.master;
//
//import java.util.Optional;
//
//import lombok.AllArgsConstructor;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import nts.arc.error.BusinessException;
//import nts.arc.layer.dom.AggregateRoot;
//
///**
// * 
// * @author HungTT - 部門情報
// *
// */
//
//@Getter
//@AllArgsConstructor
//@EqualsAndHashCode(callSuper = false)
//public class DepartmentInformation extends AggregateRoot {
//
//	/**
//	 * 会社ID
//	 */
//	private String companyId;
//
//	/**
//	 * 削除フラグ
//	 */
//	private boolean deleteFlag;
//
//	/**
//	 * 部門履歴ID
//	 */
//	private String departmentHistoryId;
//
//	/**
//	 * 部門ID
//	 */
//	private String departmentId;
//
//	/**
//	 * 部門コード
//	 */
//	private DepartmentCode departmentCode;
//
//	/**
//	 * 部門名称
//	 */
//	private DepartmentName departmentName;
//
//	/**
//	 * 部門総称
//	 */
//	private DepartmentGeneric departmentGeneric;
//
//	/**
//	 * 部門表示名
//	 */
//	private DepartmentDisplayName departmentDisplayName;
//
//	/**
//	 * 階層コード
//	 */
//	private DepartmentHierarchyCode hierarchyCode;
//
//	/**
//	 * 部門外部コード
//	 */
//	private Optional<DepartmentExternalCode> departmentExternalCode;
//
//	public DepartmentInformation(String companyId, boolean deleteFlag, String historyId, String departmentId,
//			String departmentCode, String departmentName, String departmentGeneric, String departmentDisplayName,
//			String hierarchyCode, String externalCode) {
//		this.companyId = companyId;
//		this.deleteFlag = deleteFlag;
//		this.departmentHistoryId = historyId;
//		this.departmentId = departmentId;
//		this.departmentCode = new DepartmentCode(departmentCode);
//		this.departmentName = new DepartmentName(departmentName);
//		this.departmentGeneric = new DepartmentGeneric(departmentGeneric);
//		this.departmentDisplayName = new DepartmentDisplayName(departmentDisplayName);
//		this.hierarchyCode = new DepartmentHierarchyCode(hierarchyCode);
//		this.departmentExternalCode = externalCode == null || externalCode.isEmpty() ? Optional.empty()
//				: Optional.of(new DepartmentExternalCode(externalCode));
//	}
//
//	public void delete() {
//		this.deleteFlag = true;
//	}
//	
//	public void changeHierarchyCode(String hierarchyCode) {
//		this.hierarchyCode = new DepartmentHierarchyCode(hierarchyCode);
//	}
//
//	@Override
//	public void validate() {
//		if (this.hierarchyCode.v().length() % 3 != 0) {
//			throw new BusinessException("Msg_368");
//		} else if (this.hierarchyCode.v().length() > 30)
//			throw new BusinessException("Msg_369");
//	}
//
//}
