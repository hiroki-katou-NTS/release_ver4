package nts.uk.ctx.at.request.app.command.application.appabsence;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.request.app.find.application.ApplicationDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApprovalPhaseStateForAppDto;
import nts.uk.ctx.at.shared.app.find.remainingnumber.paymana.PayoutSubofHDManagementDto;
import nts.uk.ctx.at.shared.app.find.remainingnumber.subhdmana.dto.LeaveComDayOffManaDto;

@Data
@AllArgsConstructor
public class RegisterAppAbsenceCommand {

    // 休暇申請
    private ApplyForLeaveDto applyForLeave;
    
    // 休日の申請日<List>
    private List<String> appDates;
    
    // 休出代休紐付け管理<List>
    private List<LeaveComDayOffManaDto> leaveComDayOffMana;
    
    // 振出振休紐付け管理<List>
    private List<PayoutSubofHDManagementDto> payoutSubofHDManagements;
    
    // メールサーバ設定済区分
    private boolean mailServerSet;
    
    // 承認ルートインスタンス
    private ApprovalPhaseStateForAppDto approvalRoot;
    
    private ApplicationDto application;
}
