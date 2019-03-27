package nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.sysfixedcheckcondition;

import java.util.List;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.pub.fixedcheckitem.ValueExtractAlarmWRPubExport;
import nts.uk.ctx.at.record.pub.workrecord.identificationstatus.identityconfirmprocess.IdentityConfirmProcessExport;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

public interface SysFixedCheckConMonPub {
		//5:36協定のチェック
		public Optional<ValueExtractAlarmWRPubExport>  checkAgreement(String employeeID,int yearMonth,int closureId,ClosureDate closureDate);
		//1:月次未確認
		public Optional<ValueExtractAlarmWRPubExport>  checkMonthlyUnconfirmed(String employeeID,int yearMonth);
		//6：代休の消化期限チェック
		public Optional<ValueExtractAlarmWRPubExport>  checkDeadlineCompensatoryLeaveCom(String employeeID, Closure closing, CompensatoryLeaveComSetting compensatoryLeaveComSetting );
		//1:月次未確認 (process two record in monthy)
		public List<ValueExtractAlarmWRPubExport>  checkMonthlyUnconfirmeds(String employeeID,int yearMonth,IdentityConfirmProcessExport identityConfirmProcessExport);
		
		public List<ValueExtractAlarmWRPubExport>  checkMonthlyUnconfirmeds(List<String> employeeID, List<YearMonth> yearMonth);
}
