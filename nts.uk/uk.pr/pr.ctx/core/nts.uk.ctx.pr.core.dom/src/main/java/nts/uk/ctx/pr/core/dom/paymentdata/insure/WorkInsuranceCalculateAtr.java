package nts.uk.ctx.pr.core.dom.paymentdata.insure;

/** 労働災害保険計算区分 ※参照先未確定 */
public enum WorkInsuranceCalculateAtr {
	/**
	 * 0:常勤者
	 */
	FULL_TIME_EMPLOYEE(0),
	/**
	 * 1:役員被保険者
	 */
	OFFICERS_INSURED(1),

	/**
	 * 2:臨時者
	 */
	TEMPORARY_PERSON(2),

	/**
	 * 3:非対象
	 */
	NON_TARGET(3);

	public final int value;

	private WorkInsuranceCalculateAtr(int value) {
		this.value = value;
	}

}
