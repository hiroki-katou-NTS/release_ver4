package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkadminunverified;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
/**
 * 4.管理者未確認チェック
 * @author tutk
 *
 */
public interface CheckAdminUnverifiedService {
	public List<ValueExtractAlarmWR> checkAdminUnverified(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
}
