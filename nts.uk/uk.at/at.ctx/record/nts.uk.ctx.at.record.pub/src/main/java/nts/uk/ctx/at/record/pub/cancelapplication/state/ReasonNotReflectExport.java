package nts.uk.ctx.at.record.pub.cancelapplication.state;

import lombok.AllArgsConstructor;

/**
 * 予定反映不可理由
 * 
 * @author thanh_nx
 *
 */
@AllArgsConstructor
public enum ReasonNotReflectExport {

	/**
	 * 勤務スケジュール確定済
	 */
	WORK_FIXED(0, "勤務スケジュール確定済"),

	/**
	 * 作業スケジュール確定済
	 */
	WORK_CONFIRMED(1, "作業スケジュール確定済"),

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
	
	private final static ReasonNotReflectExport[] values = ReasonNotReflectExport.values();

	public static ReasonNotReflectExport valueOf(Integer value) {
		if (value == null) {
			return null;
		}

		for (ReasonNotReflectExport val : ReasonNotReflectExport.values) {
			if (val.value == value) {
				return val;
			}
		}

		return null;
	}
}
