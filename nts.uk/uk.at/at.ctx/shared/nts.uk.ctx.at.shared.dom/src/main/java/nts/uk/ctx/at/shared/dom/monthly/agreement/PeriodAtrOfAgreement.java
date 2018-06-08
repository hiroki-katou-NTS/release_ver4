package nts.uk.ctx.at.shared.dom.monthly.agreement;

/**
 * 期間区分
 * @author shuichu_ishida
 */
public enum PeriodAtrOfAgreement {
	/** 2ヶ月 */
	TWO_MONTHS(0),
	/** 3ヶ月 */
	THREE_MONTHS(1);
	
	public int value;
	private PeriodAtrOfAgreement(int value){
		this.value = value;
	}
}
