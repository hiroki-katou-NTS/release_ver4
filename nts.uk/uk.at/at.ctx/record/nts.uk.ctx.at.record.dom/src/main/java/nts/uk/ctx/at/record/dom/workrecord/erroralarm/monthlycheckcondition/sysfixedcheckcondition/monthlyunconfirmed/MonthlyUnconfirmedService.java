package nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.sysfixedcheckcondition.monthlyunconfirmed;

import java.util.List;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
/**
 * 1:月次未確認
 * @author tutk
 *
 */

public interface MonthlyUnconfirmedService {
	Optional<ValueExtractAlarmWR> checkMonthlyUnconfirmed(String employeeID,int yearMonth);
	
	List<ValueExtractAlarmWR> checkMonthlyUnconfirmeds(String employeeID,int yearMonth,Optional<IdentityProcessUseSet> identityProcess);
	/**
	 * 月の本人確認を取得
	 * @param employeeID　List＜社員ID＞
	 * @param yearMonth　List＜年月＞
	 * @param identityProcess　本人確認処理の利用設定
	 * @return
	 */
	List<ValueExtractAlarmWR> checkMonthlyUnconfirmeds(List<String> employeeID, List<YearMonth> yearMonth, Optional<IdentityProcessUseSet> identityProcess);
}
