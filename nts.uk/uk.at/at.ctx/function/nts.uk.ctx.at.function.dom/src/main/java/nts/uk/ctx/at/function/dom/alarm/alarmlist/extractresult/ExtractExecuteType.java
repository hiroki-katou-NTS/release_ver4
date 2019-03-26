package nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExtractExecuteType {
	
	/** "実行種類
		1:手動
		2:更新処理自動実行"
	*/
	MANUAL(1),
	
	AUTO(2);
	
	public final int value;
}
