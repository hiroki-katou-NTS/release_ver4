package nts.uk.ctx.at.request.dom.application.overtime.CommonAlgorithm;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.OverTimeWorkHoursOutput;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.appovertime.OvertimeAppSet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.deviationtime.deviationtimeframe.DivergenceTimeRoot;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.overtimeholidaywork.AppReflectOtHdWork;

/**
 * Refactor5
 * UKDesign.UniversalK.就業.KAF_申請.KAF005_残業申請.AB画面の共通アルゴリズム.基準日に関係ない情報を取得する
 * @author hoangnd
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
// 基準日に関係しない情報
public class InfoNoBaseDate {
	// 残業休日出勤申請の反映
	private AppReflectOtHdWork overTimeReflect;
	
	// 残業申請設定
	private OvertimeAppSet overTimeAppSet;
	
	// 申請用時間外労働時間パラメータ
	private Optional<OverTimeWorkHoursOutput> agreeOverTimeOutput;
	
	// 利用する乖離理由(DivergenceReasonInputMethod at record , so create new class #112406)
	private List<DivergenceReasonInputMethod> divergenceReasonInputMethod;
	
	// 乖離時間枠
	private List<DivergenceTimeRoot> divergenceTimeRoot;
}
