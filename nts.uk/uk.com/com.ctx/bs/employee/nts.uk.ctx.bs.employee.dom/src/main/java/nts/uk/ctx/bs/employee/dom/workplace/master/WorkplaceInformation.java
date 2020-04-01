//package nts.uk.ctx.bs.employee.dom.workplace.master;
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
// * @author HungTT - 職場情報
// *
// */
//
//@Getter
//@AllArgsConstructor
//@EqualsAndHashCode(callSuper = false)
//public class WorkplaceInformation extends AggregateRoot {
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
//	 * 職場履歴ID
//	 */
//	private String workplaceHistoryId;
//
//	/**
//	 * 職場ID
//	 */
//	private String workplaceId;
//
//	/**
//	 * 職場コード
//	 */
//	private WorkplaceCode workplaceCode;
//
//	/**
//	 * 職場名称
//	 */
//	private WorkplaceName workplaceName;
//
//	/**
//	 * 職場総称
//	 */
//	private WorkplaceGeneric workplaceGeneric;
//
//	/**
//	 * 職場表示名
//	 */
//	private WorkplaceDisplayName workplaceDisplayName;
//
//	/**
//	 * 階層コード
//	 */
//	private WorkplaceHierarchyCode hierarchyCode;
//
//	/**
//	 * 職場外部コード
//	 */
//	private Optional<WorkplaceExternalCode> workplaceExternalCode;
//
//	public WorkplaceInformation(String companyId, boolean deleteFlag, String historyId, String workplaceId,
//			String workplaceCode, String workplaceName, String workplaceGeneric, String workplaceDisplayName,
//			String hierarchyCode, String externalCode) {
//		this.companyId = companyId;
//		this.deleteFlag = deleteFlag;
//		this.workplaceHistoryId = historyId;
//		this.workplaceId = workplaceId;
//		this.workplaceCode = new WorkplaceCode(workplaceCode);
//		this.workplaceName = new WorkplaceName(workplaceName);
//		this.workplaceGeneric = new WorkplaceGeneric(workplaceGeneric);
//		this.workplaceDisplayName = new WorkplaceDisplayName(workplaceDisplayName);
//		this.hierarchyCode = new WorkplaceHierarchyCode(hierarchyCode);
//		this.workplaceExternalCode = externalCode == null || externalCode.isEmpty() ? Optional.empty()
//				: Optional.of(new WorkplaceExternalCode(externalCode));
//	}
//	
//	public void delete() {
//		this.deleteFlag = true;
//	}
//	
//	public void changeHierarchyCode(String hierarchyCode) {
//		this.hierarchyCode = new WorkplaceHierarchyCode(hierarchyCode);
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
