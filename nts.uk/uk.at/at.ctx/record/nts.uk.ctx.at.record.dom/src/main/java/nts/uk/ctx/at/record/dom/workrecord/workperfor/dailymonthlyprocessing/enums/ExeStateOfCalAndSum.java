/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums;

/**
 * @author danpv
 *
 */
public enum ExeStateOfCalAndSum {

	//0: 完了
	DONE(0,"完了"),
	
	//1: 完了（エラーあり）
	DONE_WITH_ERROR(1,"完了（エラーあり）"),

	//2: 処理中
	PROCESSING(2,"処理中"),

	//3:実行中止
	STOPPING(3,"実行中止"),

	//4: 中断開始
	START_INTERRUPTION(4,"中断開始"),

	//5: 中断終了
	END_INTERRUPTION(5,"中断終了");

	public final int value;
	
	public String nameId;

	private ExeStateOfCalAndSum(int value,String nameId) {
		this.value = value;
		this.nameId = nameId;
	}
	
	/**
	 * now value is Processing 
	 * @return now value == procesing。
	 */
	public boolean isProcessing() {
		return this.equals(PROCESSING);
	}
	
	/**
	 * now value is START_INTERRUPTION
	 * @return now value == START_INTERRUPTION。
	 */
	public boolean isStartInterruption() {
		return this.equals(START_INTERRUPTION);
	}

}
