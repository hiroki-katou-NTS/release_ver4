package nts.uk.screen.at.app.kmk004.h;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.screen.at.app.kmk004.g.GetFlexPredWorkTimeDto;

/**
 * 
 * @author sonnlb
 *
 *         職場別基本設定（フレックス勤務）を表示する
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DisplayFlexBasicSettingByWorkPlaceDto {
	// 職場別フレックス勤務集計方法
	WkpFlexMonthActCalSetDto flexMonthActCalSet;
	// フレックス勤務所定労働時間取得
	GetFlexPredWorkTimeDto flexPredWorkTime;
	
}
