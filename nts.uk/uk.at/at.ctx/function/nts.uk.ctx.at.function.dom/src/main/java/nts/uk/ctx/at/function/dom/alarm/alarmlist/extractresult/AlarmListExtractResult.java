package nts.uk.ctx.at.function.dom.alarm.alarmlist.extractresult;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AlarmListExtractResult {

	/** 実行者ID: 手動の場合は社員ID、更新処理自動実行の場合は更新処理自動実行のタスクのコード */
	private final String executeEmpId;	
	
	/** 会社ID */
	private final String cid;	
	
	/** "実行種類
		1:手動
		2:更新処理自動実行"
	*/
	private final ExtractExecuteType executeType;
	
	/** 実行ID */
	private final String executeId;
	
	/** アラームリスト抽出従業員情報 */
	private List<ExtractEmployeeInfo> empInfos = new ArrayList<>();
	
	/** アラームリスト抽出従業員エラー */
	private List<ExtractEmployeeErAlData> empEralData = new ArrayList<>();
}
