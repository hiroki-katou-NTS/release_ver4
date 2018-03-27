/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums;

/**
 * 日別再作成区分
 * @author danpv
 *
 */
public enum DailyRecreateClassification {
	
	//0 :もう一度作り直す
	REBUILD(0),

	//1 :一部修正
	PARTLY_MODIFIED(1);
	
	
	public final int value;
	
	private DailyRecreateClassification (int value ) {
		this.value = value;
	}

}
