package nts.uk.ctx.at.record.dom.dailyprocess.calc;

/**
 * 使用する時間区分
 * @author keisuke_hoshina
 *
 */
public enum UseTimeAtr {
	TIME,
	CALCTIME;
	
	/**
	 * 時間であるか判定する
	 * @return　時間である
	 */
	public boolean isTime() {
		return this.equals(TIME);
	}
}
