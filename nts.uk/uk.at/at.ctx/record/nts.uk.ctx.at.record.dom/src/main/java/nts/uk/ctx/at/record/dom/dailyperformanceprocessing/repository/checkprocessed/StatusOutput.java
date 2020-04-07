package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.checkprocessed;
/**
 * enum : ステータス
 * @author tutk
 *
 */
public enum StatusOutput {
	
	/**
	 * 次の日へ
	 */
	NEXT_DAY(0),
	/**
	 * 次の社員へ
	 */
	NEXT_EMPLOYEE(1),
	/**
	 * 処理する
	 */
	PROCESS(2);
	
	
	public int value;
	
	StatusOutput(int type){
		this.value = type;
	}
	
}
