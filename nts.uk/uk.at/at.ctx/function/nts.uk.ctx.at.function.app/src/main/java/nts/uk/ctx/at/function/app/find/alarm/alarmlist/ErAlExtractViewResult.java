package nts.uk.ctx.at.function.app.find.alarm.alarmlist;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErAlExtractViewResult {

	/** アラームリスト抽出従業員情報 */
	private List<List<Object>> empInfos = new ArrayList<>();
	
	/** アラームリスト抽出従業員エラー */
	private List<List<Object>> empEralDatas = new ArrayList<>();
}
