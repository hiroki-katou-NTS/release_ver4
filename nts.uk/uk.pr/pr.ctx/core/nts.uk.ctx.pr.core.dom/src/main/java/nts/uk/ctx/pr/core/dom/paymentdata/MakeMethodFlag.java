package nts.uk.ctx.pr.core.dom.paymentdata;

/**
 * 作成方法フラグ
 * 
 * @author vunv
 *
 */
public enum MakeMethodFlag {
	/**
	 * 0:就業連動明細作成
	 */
	WORK_LINKAGE_DETAIL(0),

	/**
	 * 1:初期データ作成
	 */
	INITIAL_DATA(1);

	public int value;

	private MakeMethodFlag(int value) {
		this.value = value;
	}

}
