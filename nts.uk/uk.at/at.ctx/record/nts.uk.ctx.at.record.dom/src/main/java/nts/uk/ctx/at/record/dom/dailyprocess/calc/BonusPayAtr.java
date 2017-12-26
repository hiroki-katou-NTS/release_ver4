package nts.uk.ctx.at.record.dom.dailyprocess.calc;

/**
 * 加給区分
 * @author keisuke_hoshina
 *
 */
public enum BonusPayAtr {
	BonusPay,
	SpecifiedBonusPay;
	
	/**
	 * 加給であるか判定する
	 * @return　加給である
	 */
	public boolean isBonusPay() {
		return BonusPay.equals(this);
	}
}
