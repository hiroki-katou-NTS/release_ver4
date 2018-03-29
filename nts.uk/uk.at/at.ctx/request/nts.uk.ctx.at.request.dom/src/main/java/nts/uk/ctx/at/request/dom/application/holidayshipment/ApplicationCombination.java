package nts.uk.ctx.at.request.dom.application.holidayshipment;

/**
 * 申請組み合わせ
 * 
 * @author sonnlb
 */
public enum ApplicationCombination {
	/**
	 * 振出＋振休
	 */
	RecAndAbs(0),
	/**
	 * 振出
	 */
	Abs(1),
	/**
	 * 振休
	 */
	Rec(2);

	public final int value;

	private ApplicationCombination(int value) {
		this.value = value;
	}
}
