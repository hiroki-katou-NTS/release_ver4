package nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.holidayworkapplication.HolidayWorkApplicationReflect;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.overtimeworkapplycation.OvertimeWorkApplicationReflect;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * refactor4 refactor 4
 * UKDesign.ドメインモデル."NittsuSystem.UniversalK".就業.shared(勤務予定、勤務実績).申請反映処理.申請反映条件.残業・休日出勤.残業休日出勤申請の反映
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppReflectOvertimeHolidayWork extends AggregateRoot {
    /**
     * 会社ID
     */
    private String companyId;

    /**
     * 休日出勤申請
     */
    private HolidayWorkApplicationReflect holidayWorkAppReflect;

    /**
     * 残業申請
     */
    private OvertimeWorkApplicationReflect overtimeWorkAppReflect;

    /**
     * 時間外深夜時間を反映する
     */
    private NotUseAtr nightOvertimeReflectAtr;
}
