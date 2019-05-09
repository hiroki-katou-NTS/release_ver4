package nts.uk.ctx.at.record.dom.workrecord.daily.erroralarm.createerrorforemployee;

import java.util.Optional;
import javax.ejb.Stateless;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class CreateErrorForEmployeeService {
	
	public Optional<ValueExtractAlarmWR> createErrorForEmployeeService(String workplaceID,String companyID,String employeeID,GeneralDate date) {
		ValueExtractAlarmWR valueExtractAlarmWR = new ValueExtractAlarmWR(
				workplaceID,
				employeeID,
				date,
				TextResource.localize("KAL010_1"),
				null,
				null,
				null,
				null
				);
		return Optional.ofNullable(valueExtractAlarmWR);
	}
}
