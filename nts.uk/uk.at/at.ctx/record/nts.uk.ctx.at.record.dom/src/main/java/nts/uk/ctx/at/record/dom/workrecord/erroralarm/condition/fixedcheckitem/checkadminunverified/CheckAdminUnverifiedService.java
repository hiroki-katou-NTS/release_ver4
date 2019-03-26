package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkadminunverified;

import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 4.管理者未確認チェック
 * @author tutk
 *
 */
public interface CheckAdminUnverifiedService {
	public List<ValueExtractAlarmWR> checkAdminUnverified(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
	public List<ValueExtractAlarmWR> checkAdminUnverified(String workplaceID,String employeeID,DatePeriod datePeriod);
	public Map<String, List<GeneralDate>> checkAdminUnverified(List<String> employeeID,DatePeriod datePeriod);
}
