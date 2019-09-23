package nts.uk.ctx.at.function.dom.processexecution.executionlog;

import lombok.AllArgsConstructor;

/**
 * 更新処理自動実行タスクの状態
 */
@AllArgsConstructor
public enum EndStatus {
	/* 未実施 */
	NOT_IMPLEMENT(0, "未実施"),
	
	/* 正常終了 -> 完了 */
	SUCCESS(1, "完了"),
	
	/* 強制終了 */
	FORCE_END(2, "強制終了"),
	
	/* 終了中 */
	CLOSING(3, "終了中");
	
	/** The value. */
	public final int value;
	
	public final String name;
}
