package nts.uk.ctx.at.record.dom.daily;

/**
 * フレックス勤務の日勤務日の場合の計算方法
 * @author keisuke_hoshina
 *
 */
public enum CalcMethodOfNoWorkingDay {
	isCalculateFlexTime,
	isNotCalculateFlexTime;
	
	/**
	 * フレックス時間を計算するか判定する
	 * @return　フレックス時間をする
	 */
	public boolean isCalclateFlexTime() {
		return isCalculateFlexTime.equals(this);
	}
}
