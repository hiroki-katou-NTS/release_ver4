package nts.uk.ctx.at.shared.dom.workrule.closure.service;

import lombok.Data;

/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared.就業規則.就業締め日.アルゴリズム.Query.全ての締めの処理年月と締め期間を取得する.OUTPUT．List＜締めID, 現在の締め期間＞に追加する
 * @author tutt
 *
 */
@Data
public class ClosureIdPresentClosingPeriod {
	
	//締めID
	private Integer closureId;
	
	//現在の締め期間
	private GetYearProcessAndPeriodDto currentClosingPeriod;
	
}
