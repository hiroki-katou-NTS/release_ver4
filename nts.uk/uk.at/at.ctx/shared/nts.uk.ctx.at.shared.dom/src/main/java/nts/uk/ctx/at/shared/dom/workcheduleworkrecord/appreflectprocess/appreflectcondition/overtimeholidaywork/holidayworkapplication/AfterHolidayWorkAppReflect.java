package nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.holidayworkapplication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.BreakApplication;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.OthersReflect;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.overtimeholidaywork.overtimeworkapplycation.AfterOvertimeWorkAppReflect;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * refactor 4 refactor4
 * 事後休日出勤申請の反映
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AfterHolidayWorkAppReflect extends DomainObject {
    /**
     * その他項目を反映する
     */
    private OthersReflect othersReflect;

    /**
     * 休憩・外出を申請反映する
     */
    private BreakApplication breakLeaveApplication;

    /**
     * 出退勤を反映する
     */
    private NotUseAtr workReflect;

    public static AfterHolidayWorkAppReflect create(int workReflect, int reflectPaytime, int reflectOptional, int reflectDivergence, int reflectBreakOuting) {
        return new AfterHolidayWorkAppReflect(
                new OthersReflect(
                        EnumAdaptor.valueOf(reflectDivergence, NotUseAtr.class),
                        EnumAdaptor.valueOf(reflectOptional, NotUseAtr.class),
                        EnumAdaptor.valueOf(reflectPaytime, NotUseAtr.class)
                ),
                new BreakApplication(EnumAdaptor.valueOf(reflectBreakOuting, NotUseAtr.class)),
                EnumAdaptor.valueOf(workReflect, NotUseAtr.class)
        );
    }
}
