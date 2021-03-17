package nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export;

import lombok.AllArgsConstructor;

/**
 * 暫定残数データ管理モード
 * @author shuichi_ishida
 */
@AllArgsConstructor
public enum InterimRemainMngMode {
	/** 月次モード */
	MONTHLY(0),
	/** その他モード */
	OTHER(1);

	public final int value;

	static public InterimRemainMngMode of(boolean mode) {
		if (mode ) {
			return MONTHLY;
		} else {
			return OTHER;
		}
	}
}
