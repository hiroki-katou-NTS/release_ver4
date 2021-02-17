package nts.uk.ctx.at.shared.dom.application.overtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.optionalitemvalue.AnyItemValue;

/**
 * UKDesign.ドメインモデル."NittsuSystem.UniversalK".就業.contexts.申請承認.申請.残業休出申請
 * 
 * @author thanhnx
 *
 */
@AllArgsConstructor
@Data
// 申請時間
public class ApplicationTimeShare {
	// 申請時間
	private List<OvertimeApplicationSettingShare> applicationTime = Collections.emptyList();
	// フレックス超過時間
	private Optional<AttendanceTimeOfExistMinus> flexOverTime = Optional.empty();
	// 就業時間外深夜時間
	private Optional<OverTimeShiftNightShare> overTimeShiftNight = Optional.empty();
	// 任意項目
	private List<AnyItemValue> anyItem = new ArrayList<>();
	// 乖離理由
	private List<ReasonDivergenceShare> reasonDissociation = new ArrayList<>();
}
