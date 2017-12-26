package nts.uk.ctx.at.record.dom.dailyprocess.calc;

/**
 * 控除種別
 * @author keisuke_hoshina
 *
 */
public enum DeductionClassification {
	GO_OUT, //外出
	CHILD_CARE, //育児
	BREAK //休憩
	;
	/**
	 *外出であるか判定する
	 *@param 外出である 
	 */
	public boolean isGoOut() {
		return GO_OUT.equals(this);
	}
	
	/**
	 * 育児であるか判定する
	 * @return 育児である
	 */
	public boolean isChildCare() {
		return CHILD_CARE.equals(this);
	}
	
	/**
	 * 休憩であるか判定する
	 * @return 休憩である
	 */
	public boolean isBreak() {
		return BREAK.equals(this);
	}
	
}
