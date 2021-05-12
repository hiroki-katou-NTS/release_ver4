package nts.uk.ctx.at.schedule.pub.appreflectprocess.appremove.export;

import lombok.AllArgsConstructor;

/**
 * 日別実績反映不可理由
 * 
 * @author thanh_nx
 *
 */
@AllArgsConstructor
public enum ReasonNotReflectDailyExport {

	/**
	 * 実績確定済
	 */
	ACTUAL_CONFIRMED(0, "実績確定済"),

	/**
	 * 実績がロックされている
	 */
	ACHIEVEMENTS_LOCKED(1, "実績がロックされている"),

	/**
	 * 本人確認、上司確認済
	 */
	SELF_CONFIRMED_BOSS_CONFIRMED(2, "本人確認、上司確認済"),

	/**
	 * 締め処理が完了している
	 */
	TIGHTENING_PROCESS_COMPLETED(3, "締め処理が完了している");

	public final int value;

	public final String name;

	private final static ReasonNotReflectDailyExport[] values = ReasonNotReflectDailyExport.values();

	public static ReasonNotReflectDailyExport valueOf(Integer value) {
		if (value == null) {
			return null;
		}

		for (ReasonNotReflectDailyExport val : ReasonNotReflectDailyExport.values) {
			if (val.value == value) {
				return val;
			}
		}

		return null;
	}
}
