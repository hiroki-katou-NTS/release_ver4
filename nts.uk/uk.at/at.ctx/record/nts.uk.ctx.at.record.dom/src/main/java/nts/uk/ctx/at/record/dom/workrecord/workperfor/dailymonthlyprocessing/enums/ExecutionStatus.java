/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums;

/**
 * @author danpv
 *
 */
public enum ExecutionStatus {
	
	/** 0: 完了*/
	DONE(0, "完了"),
	
	/** 1: 処理中 */
	PROCESSING(1, "処理中"),
	
	/** 2: 未完了 */
	INCOMPLETE(2, "未完了");
	
	public final int value;
	
	public final String nameId;
	
	private ExecutionStatus(int value, String nameId) {
		this.value = value;
		this.nameId = nameId;
	}

}
