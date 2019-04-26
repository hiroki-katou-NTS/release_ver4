package nts.uk.ctx.at.function.dom.adapter.toppagealarmpub;
/**
 * アラームカテゴリ
 * @author yennth
 *
 */
public enum AlarmCategoryFn {
	/** スケジュール作成 */
	CREATE_SCHEDULE(1),
	/** 日別実績の作成 */
	CREATE_DAILY_REPORT(2),
	/** 日別実績の計算 */
	CALCULATION_DAILY_REPORT(3),
	/** 承認結果の反映 */
	REFLECT_APPROVAL_RESULT(4),
	/** 月別実績の集計 */
	AGGREGATE_RESULT_MONTH(5),
	/** アラームリスト（個人別） */
	ALARM_LIST_PERSONAL(6),
	/** 承認中間データ作成（日別実績） */
	APPROVAL_DAILY(7),
	/** 承認中間データ作成（月別実績） */
	APPROVAL_MONTHLY(8);
	public final int value;
	private AlarmCategoryFn(int type){
		this.value = type;
	}
}
