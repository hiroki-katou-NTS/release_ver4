/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums;

/**
 * @author danpv
 *
 */
public enum ExecutionType {
	
	//0: 通常実行
	NORMAL_EXECUTION(0,"通常実行"),
	
	//1: 再実行
	RERUN(1,"再実行");
	
	public final int value;
	public String nameId;
	
	private ExecutionType(int value,String nameId) {
		this.value = value;
		this.nameId = nameId;
	}

}
