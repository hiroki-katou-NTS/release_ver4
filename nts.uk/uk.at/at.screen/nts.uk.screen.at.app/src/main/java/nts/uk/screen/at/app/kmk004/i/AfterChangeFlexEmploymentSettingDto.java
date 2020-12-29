package nts.uk.screen.at.app.kmk004.i;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.screen.at.app.kmk004.g.GetFlexPredWorkTimeDto;

/**
 * 
 * @author sonnlb
 *
 *         雇用別基本設定（フレックス勤務）を作成・変更・削除した時
 */
@NoArgsConstructor
@Data
public class AfterChangeFlexEmploymentSettingDto {

	// 雇用を選択する
	EmpFlexMonthActCalSetDto flexMonthActCalSet;

	// フレックス勤務所定労働時間取得
	GetFlexPredWorkTimeDto flexPredWorkTime;

	List<String> alreadySettings;
}
