package nts.uk.ctx.at.shared.dom.common;

/**
 * ２つの時間帯の位置関係
 * @author keisuke_hoshina
 *
 */
public enum RangeDuplication {
	
	//同じ期間
	SAME_SPAN,
	//基準時間が完全に内包する
	BASE_CONTAINS_COMPLETE,
	//基準時間が内包し、開始が同じ
	BASE_CONTAINS_SAME_START,
	//基準時間が内包し、終了が同じ
	BASE_CONTAINS_SAME_END,
	//基準期間が完全に内包される
	BASE_CONTAINED_COMPLETE,
	//基準時間が内包され、開始が同じ
	BASE_CONTAINED_SAME_START,
	//基準時間が内包され、終了が同じ
	BASE_CONTAINED_SAME_END,
	//基準時間の後ろに連続する
	CONTINUOUS_AFTER_BASE,
	//基準時間の前に連続する
	CONTINUOUS_BEFORE_BASE,
	//基準期間が比較期間より前
	BASE_BEFORE_COMPARED,
	//基準時間が比較期間より後ろ
	BASE_AFTER_COMPARED,
	//比較期間の開始が基準期間の間にある
	COMPARED_START_BETWEEN_BASE,
	//比較期間の終了が基準期間の間にある
	COMPARED_END_BETWEEN_BASE;
	
	/**
	 * 基準時間の前に連続するか判定する
	 */
	public boolean isContinuousBeforeBase() {
		return this.equals(CONTINUOUS_BEFORE_BASE);
	}
	
	/**
	 * 基準時間の後ろに連続するか判定する
	 */
	public boolean isContinuousAfterBase() {
		return this.equals(CONTINUOUS_AFTER_BASE);
	}
}
