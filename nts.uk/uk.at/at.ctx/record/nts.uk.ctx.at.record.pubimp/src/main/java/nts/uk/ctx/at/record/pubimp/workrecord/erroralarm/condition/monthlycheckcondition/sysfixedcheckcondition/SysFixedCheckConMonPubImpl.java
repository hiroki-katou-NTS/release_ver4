package nts.uk.ctx.at.record.pubimp.workrecord.erroralarm.condition.monthlycheckcondition.sysfixedcheckcondition;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.sysfixedcheckcondition.checkforagreement.CheckAgreementService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.sysfixedcheckcondition.monthlyunconfirmed.MonthlyUnconfirmedService;
import nts.uk.ctx.at.record.pub.fixedcheckitem.ValueExtractAlarmWRPubExport;
import nts.uk.ctx.at.record.pub.workrecord.erroralarm.condition.monthlycheckcondition.sysfixedcheckcondition.SysFixedCheckConMonPub;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
@Stateless
public class SysFixedCheckConMonPubImpl implements SysFixedCheckConMonPub {

	@Inject
	private MonthlyUnconfirmedService monthlyUnconfirmedService;
	
	@Inject
	private CheckAgreementService checkAgreementService;
	
	@Override
	public Optional<ValueExtractAlarmWRPubExport> checkAgreement(String employeeID, int yearMonth,int closureId,ClosureDate closureDate) {
		Optional<ValueExtractAlarmWR> data = checkAgreementService.checkAgreement(employeeID, yearMonth,closureId,closureDate);
		if(data.isPresent()) {
			return Optional.of(convertToExport(data.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<ValueExtractAlarmWRPubExport> checkMonthlyUnconfirmed(String employeeID, int yearMonth) {
		Optional<ValueExtractAlarmWR> data = monthlyUnconfirmedService.checkMonthlyUnconfirmed(employeeID, yearMonth);
		if(data.isPresent()) {
			return Optional.of(convertToExport(data.get()));
		}
		return Optional.empty();
	}
	
	private ValueExtractAlarmWRPubExport convertToExport(ValueExtractAlarmWR valueExtractAlarmWR) {
		return new ValueExtractAlarmWRPubExport(
				valueExtractAlarmWR.getWorkplaceID().orElse(null),
				valueExtractAlarmWR.getEmployeeID(),
				valueExtractAlarmWR.getAlarmValueDate(),
				valueExtractAlarmWR.getClassification(),
				valueExtractAlarmWR.getAlarmItem(),
				valueExtractAlarmWR.getAlarmValueMessage(),
				valueExtractAlarmWR.getComment().orElse(null)
				);
	}

}
