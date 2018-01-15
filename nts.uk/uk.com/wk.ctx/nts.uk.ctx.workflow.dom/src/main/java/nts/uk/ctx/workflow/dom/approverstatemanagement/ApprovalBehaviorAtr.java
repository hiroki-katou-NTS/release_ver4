package nts.uk.ctx.workflow.dom.approverstatemanagement;
/**
 * 承認区分
 * @author Doan Duy Hung
 *
 */
public enum ApprovalBehaviorAtr {
	
	/** 0:未承認 */
	UNAPPROVED(0,"未承認"),
	
	/** 1:承認済 */
	APPROVED(1,"承認済"),
	
	/** 2:否認 */
	DENIAL(2,"否認 "),
	
	/** 3:差し戻し */
	REMAND(3,"差し戻し");

	public final int value;
	
	public final String name;
	
	ApprovalBehaviorAtr(int type, String name) {
		this.value = type;
		this.name = name;
	}
}
