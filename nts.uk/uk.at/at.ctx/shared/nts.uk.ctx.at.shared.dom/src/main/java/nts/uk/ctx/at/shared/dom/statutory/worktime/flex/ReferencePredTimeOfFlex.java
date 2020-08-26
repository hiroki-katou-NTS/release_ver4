package nts.uk.ctx.at.shared.dom.statutory.worktime.flex;

import lombok.AllArgsConstructor;

/**
 * フレックス勤務の所定時間参照
 * @author shuichu_ishida
 */
@AllArgsConstructor
public enum ReferencePredTimeOfFlex {
	/** マスタから参照 */
	FROM_MASTER(0),
	/** 実績から参照 */
	FROM_RECORD(1);
	
	public int value;
	
	public static ReferencePredTimeOfFlex valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (ReferencePredTimeOfFlex val : ReferencePredTimeOfFlex.values()) {
			if (val.value == value) {
				return val;
			}
		}
		// Not found.
		return null;
	}
}
