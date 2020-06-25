package nts.uk.ctx.at.request.dom.application.overtime;

import lombok.AllArgsConstructor;

/**
 * refactor 4
 * 残業申請区分
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
public enum OvertimeAppAtr {
	
	/**
	 * 早出残業
	 */
	EARLY_OVERTIME(0, "早出残業"),
	/**
	 * 通常残業
	 */
	NORMAL_OVERTIME(1, "通常残業"),
	/**
	 * 早出残業・通常残業
	 */
	EARLY_NORMAL_OVERTIME(2, "早出残業・通常残業");
	
	public final int value;
	
	public final String name;
}
