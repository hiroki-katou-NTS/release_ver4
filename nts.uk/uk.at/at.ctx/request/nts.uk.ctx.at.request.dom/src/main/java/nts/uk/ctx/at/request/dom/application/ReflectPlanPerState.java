package nts.uk.ctx.at.request.dom.application;
/**
 * 予定反映状態/ステータス
 * @author dudt
 *
 */
public enum ReflectPlanPerState {
	/**
	 * 未反映
	 */
	NOTREFLECTED(0),
	/**
	 * 差し戻し
	 */
	REMAND(1),
	/**
	 * 取消済
	 */
	CANCELED(2),
	/**
	 * 取消待ち
	 */
	WAITCANCEL(3),
	/**
	 * 反映済
	 */
	REFLECTED(4),
	/**
	 * 反映待ち
	 */
	WAITREFLECTION(5),
	/**
	 * 否認
	 */
	
	DENIAL(6),
	/** Other */
	/**Status = 過去申請 */
	PASTAPP(99)
	;
	
	
	public int value;
	
	ReflectPlanPerState(int type){
		this.value = type;
	}
	
}
