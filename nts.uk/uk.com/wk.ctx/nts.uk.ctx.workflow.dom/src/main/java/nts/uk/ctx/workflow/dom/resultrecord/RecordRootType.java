package nts.uk.ctx.workflow.dom.resultrecord;

import nts.arc.enums.EnumAdaptor;

/**
 * 実績確認ルート種類
 * @author Doan Duy Hung
 *
 */
public enum RecordRootType {
	
	/**
	 * 就業日別確認
	 */
	CONFIRM_WORK_BY_DAY(1, "就業日別確認"),
	
	/**
	 * 就業月別確認
	 */
	CONFIRM_WORK_BY_MONTH(2, "就業月別確認");
	
	public final int value;
	
	public final String name;
	
	RecordRootType(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public static RecordRootType of(int value) {
		return EnumAdaptor.valueOf(value, RecordRootType.class);
	}
	
}
