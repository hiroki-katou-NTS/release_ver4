package nts.uk.ctx.pr.core.dom.retirement.payitem;
/**
 * カテゴリ区分(2と9は退職金明細書では利用しない)
 * @author Doan Duy Hung
 *
 */
public enum IndicatorCategory {
	/**
	 * 0 - 支給
	 */
	PAYMENT(0),
	
	/**
	 * 1 - 控除
	 */
	DEDUCTION(1),
	
	/**
	 * 2 - 勤怠
	 */
	ATTENDANCE(2),
	
	/**
	 * 3 - 記事
	 */
	ARTICLE(3),
	
	/**
	 * 9 - その他
	 */
	OTHER(9);
	
	public final int value;
	
	IndicatorCategory(int value) {
		this.value = value;
	}
}
