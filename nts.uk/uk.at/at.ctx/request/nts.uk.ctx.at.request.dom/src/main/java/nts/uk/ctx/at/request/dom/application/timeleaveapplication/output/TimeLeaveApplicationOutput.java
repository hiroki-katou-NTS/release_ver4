package nts.uk.ctx.at.request.dom.application.timeleaveapplication.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.request.dom.application.common.service.setting.output.AppDispInfoStartupOutput;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.timeleaveapplication.TimeLeaveApplicationReflect;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;

/**
 * output: 時間休暇申請の表示情報
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeLeaveApplicationOutput {

    //労働条件項目
    private WorkingConditionItem workingConditionItem;

    //時間休暇残数
    private TimeVacationRemainingOutput timeVacationRemaining;

    // 時間休暇申請の反映
    private TimeLeaveApplicationReflect timeLeaveApplicationReflect;

    //時間休暇管理
    private TimeVacationManagementOutput timeVacationManagement;

    // 申請表示情報
    private AppDispInfoStartupOutput appDispInfoStartup;

}
