package nts.uk.ctx.pr.core.dom.rule.employment.averagepay;

/**
 * 出勤日数取得方法
 * Attend Day Getting Setting
 * @author Doan Duy Hung
 *
 */

public enum AttendDayGettingSet {
	/**
	 * 0 - 就業からの連携
	 */
	COLLABORATION_FROM_EMPLOYMENT(0),
	
	/**
	 * 1 - 明細書項目から選択
	 */
	SELECT_FROM_STATEMENT_ITEM(1);
	
	public final int value;
	
	AttendDayGettingSet(int value) {
		this.value = value;
		// TODO Auto-generated constructor stub
	}
}
