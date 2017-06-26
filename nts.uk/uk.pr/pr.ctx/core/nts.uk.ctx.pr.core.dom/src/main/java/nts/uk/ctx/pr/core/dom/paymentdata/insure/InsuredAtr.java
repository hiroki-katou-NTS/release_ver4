package nts.uk.ctx.pr.core.dom.paymentdata.insure;

/** 被保険者区分 */
public enum InsuredAtr {
	/**
	 * 0:一般被保険者
	 */
	GENERAL_INSURED_PERSON(0),
	/**
	 * 1:パート扱い
	 */
	PART_HANDLING(1),

	/**
	 * 2:短時間労働者
	 */
	SHORT_WORKERS(2);

	public final int value;

	private InsuredAtr(int value) {
		this.value = value;
	}

}
