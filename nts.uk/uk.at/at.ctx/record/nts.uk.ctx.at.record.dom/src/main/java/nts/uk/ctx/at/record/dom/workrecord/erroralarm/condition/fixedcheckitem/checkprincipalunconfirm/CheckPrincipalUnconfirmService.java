package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm;

import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDate;

/**
 * 
 * @author tutk 3.本人未確認チェック
 */
public interface CheckPrincipalUnconfirmService {
	public List<ValueExtractAlarmWR> checkPrincipalUnconfirm(String workplaceID, String employeeID,
			GeneralDate startDate, GeneralDate endDate);

	public Map<String, List<GeneralDate>> checkPrincipalConfirmed(List<String> employeeID, GeneralDate startDate,
			GeneralDate endDate);
}
