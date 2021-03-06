package nts.uk.ctx.at.record.pub.dailymonthlyprocessing;

public enum EmployeeExecutionStatusExport {
	/**
	 * 0 : 完了
	 */
	COMPLETE(0,"完了"),

	/**
	 * 1 : 未完了
	 */
	INCOMPLETE(1,"完了");

	public final int value;
	public String nameId; 

	private EmployeeExecutionStatusExport(int value,String nameId) {
		this.value = value;
		this.nameId = nameId;
	}
}
