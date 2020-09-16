package nts.uk.ctx.at.function.dom.executionstatusmanage.optionalperiodprocess.periodtarget;

import lombok.AllArgsConstructor;
/**
 * 従業員の実行状況
 *
 */
@AllArgsConstructor
public enum State {

		// 0:未完了
		UNDONE(0, "未完了"),
		// 1:完了
		DONE(1, "完了");

		public final int value;
		public final String name;
}
