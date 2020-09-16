package nts.uk.ctx.at.record.app.command.standardtime.monthsetting;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.require.RecordDomRequireService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementMonthSetDomainService;
import nts.uk.ctx.at.shared.dom.monthly.agreement.management.exceptsetting.AgreementMonthSetting;
import nts.uk.ctx.at.shared.dom.monthly.agreement.management.primitivevalue.AlarmOneMonth;
import nts.uk.ctx.at.shared.dom.monthly.agreement.management.primitivevalue.ErrorOneMonth;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.service.WorkingConditionService;

/**
 * 
 * @author nampt 特例設定 screen update month
 *
 */
@Stateless
public class UpdateAgreementMonthSettingCommandHandler extends CommandHandlerWithResult<UpdateAgreementMonthSettingCommand, List<String>> {

	@Inject
	private AgreementMonthSetDomainService agreementMonthSetDomainService;
	
	@Inject
	private RecordDomRequireService requireService;

	@Override
	protected List<String> handle(CommandHandlerContext<UpdateAgreementMonthSettingCommand> context) {
		UpdateAgreementMonthSettingCommand command = context.getCommand();

		AgreementMonthSetting agreementMonthSetting = new AgreementMonthSetting(command.getEmployeeId(),
				new YearMonth(command.getYearMonthValue()), new ErrorOneMonth(command.getErrorOneMonth()),
				new AlarmOneMonth(command.getAlarmOneMonth()));
		
//		agreementMonthSetting.validate();
		
		Optional<WorkingConditionItem> workingConditionItem = WorkingConditionService
				.findWorkConditionByEmployee(requireService.createRequire(),
						command.getEmployeeId(), GeneralDate.today());
		Integer yearMonthValueOld = command.getYearMonthValueOld();
		return this.agreementMonthSetDomainService.update(agreementMonthSetting, workingConditionItem, yearMonthValueOld);
	}

}
