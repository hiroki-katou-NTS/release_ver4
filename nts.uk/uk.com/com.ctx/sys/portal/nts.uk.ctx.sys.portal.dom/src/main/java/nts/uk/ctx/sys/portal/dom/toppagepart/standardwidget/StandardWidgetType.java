package nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget;

/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.システム.ポータル.トップページの部品.標準ウィジェット.標準ウィジェット種別
 * 
 * @author tutt
 *
 */
public enum StandardWidgetType {
	/**
	 * 0001 - 申請状況
	 */
	APPLICATION_STATUS(0001),

	/**
	 * 0002 - 承認すべき状況
	 */
	APPROVE_STATUS(0002),

	/**
	 * 0003 - 勤務状況
	 */
	WORK_STATUS(0003),

	/**
	 * 0004 - 上長用の時間外労働時間
	 */
	OVERTIME_FOR_SUPERIOR(0004),

	/**
	 * 0005 - 打刻入力
	 */
	STAMPT_INPUT(0005),

	/**
	 * 0006 - トップぺージアラーム
	 */
	TOP_PAGE_ALARM(0006),

	/**
	 * 0007 - 従業員用の時間外勤務時間
	 */
	OVERTIME_FOR_EMPLOYEE(0007);

	StandardWidgetType(int type) {
		this.value = type;
	}

	public final int value;
}
