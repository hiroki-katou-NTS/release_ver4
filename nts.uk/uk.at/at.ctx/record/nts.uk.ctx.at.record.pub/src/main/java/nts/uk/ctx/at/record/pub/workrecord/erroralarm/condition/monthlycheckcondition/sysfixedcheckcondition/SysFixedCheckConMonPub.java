package nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.sysfixedcheckcondition;

import java.util.Optional;

import nts.uk.ctx.at.record.pub.fixedcheckitem.ValueExtractAlarmWRPubExport;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;

public interface SysFixedCheckConMonPub {
		//5:36協定のチェック
		public Optional<ValueExtractAlarmWRPubExport>  checkAgreement(String employeeID,int yearMonth,int closureId,ClosureDate closureDate);
		//1:月次未確認
		public Optional<ValueExtractAlarmWRPubExport>  checkMonthlyUnconfirmed(String employeeID,int yearMonth);
		//6：代休の消化期限チェック
		public Optional<ValueExtractAlarmWRPubExport>  checkDeadlineCompensatoryLeaveCom(String employeeID, Closure closing, CompensatoryLeaveComSetting compensatoryLeaveComSetting );
}
