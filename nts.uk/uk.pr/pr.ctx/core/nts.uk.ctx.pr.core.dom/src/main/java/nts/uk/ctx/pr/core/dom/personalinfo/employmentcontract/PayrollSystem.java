package nts.uk.ctx.pr.core.dom.personalinfo.employmentcontract;

/**
 * Enum: Payroll system
 *
 */
public enum PayrollSystem {
	/** 月給 **/
	MONTHLY(0), 
	/** 日給月給 **/
	DAILY(1), 
	/** 日給 **/
	DAY(2), 
	/** 時間給 **/
	HOURS(3);

	public final int value;

	private PayrollSystem(int value) {
		this.value = value;
	}

}
