package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

/**
 * 再作成フラグ
 * 
 * @author tutk
 *
 */
public enum RecreateFlag {
	// しない
	DO_NOT(0, "しない"),

	// する（日別作成）
	CREATE_DAILY(1, "する（日別作成）"),
	
	//する（更新自動実行）
	UPDATE_AUTO_EXECUTION(2, "する（更新自動実行）");

	public final int value;
	public String nameId;

	private RecreateFlag(int value, String nameId) {
		this.value = value;
		this.nameId = nameId;
	}
}
