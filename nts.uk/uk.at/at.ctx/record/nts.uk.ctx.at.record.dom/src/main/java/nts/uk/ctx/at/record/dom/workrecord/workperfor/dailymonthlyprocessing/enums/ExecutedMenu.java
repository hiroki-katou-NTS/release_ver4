/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums;

/**
 * @author danpv
 *
 */
public enum ExecutedMenu {
	
	//0: 選択して実行
	SELECT_AND_RUN(0), 
	
	// 1:ケース別実行
	EXECUTION_BY_CASE(1);

	public final int value;

	private ExecutedMenu(int value) {
		this.value = value;
	}
}
