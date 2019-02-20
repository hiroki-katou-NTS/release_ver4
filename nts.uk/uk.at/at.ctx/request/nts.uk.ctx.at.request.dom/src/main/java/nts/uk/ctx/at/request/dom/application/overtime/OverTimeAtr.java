package nts.uk.ctx.at.request.dom.application.overtime;

/**
 * @author loivt
 * 残業区分
 */
public enum OverTimeAtr {
	
	/**
	 * 早出残業
	 */
	PREOVERTIME(0, "早出残業"),
	/**
	 * 通常残業
	 */
	REGULAROVERTIME(1, "通常残業"),
	/**
	 * 早出残業・通常残業
	 */
	ALL(2, "早出残業・通常残業");
	public final int value;
	
	public final String name;
	
	OverTimeAtr(int value, String name){
		this.value = value;
		this.name = name;
	}

}
