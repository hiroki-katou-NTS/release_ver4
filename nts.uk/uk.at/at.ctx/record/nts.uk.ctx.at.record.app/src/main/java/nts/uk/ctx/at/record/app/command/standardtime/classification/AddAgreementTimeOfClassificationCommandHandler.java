package nts.uk.ctx.at.record.app.command.standardtime.classification;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfClassificationDomainService;
import nts.uk.ctx.at.shared.dom.standardtime.AgreementTimeOfClassification;
import nts.uk.ctx.at.shared.dom.standardtime.BasicAgreementSetting;
import nts.uk.ctx.at.shared.dom.standardtime.UpperAgreementSetting;
import nts.uk.ctx.at.shared.dom.standardtime.enums.LaborSystemtAtr;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.AgreementOneMonthTime;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.AlarmFourWeeks;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.AlarmOneMonth;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.AlarmOneYear;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.AlarmThreeMonths;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.AlarmTwoMonths;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.AlarmTwoWeeks;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.AlarmWeek;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.ErrorFourWeeks;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.ErrorOneMonth;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.ErrorOneYear;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.ErrorThreeMonths;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.ErrorTwoMonths;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.ErrorTwoWeeks;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.ErrorWeek;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.LimitFourWeeks;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.LimitOneMonth;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.LimitOneYear;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.LimitThreeMonths;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.LimitTwoMonths;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.LimitTwoWeeks;
import nts.uk.ctx.at.shared.dom.standardtime.primitivevalue.LimitWeek;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * 
 * @author nampt 分類 screen add
 *
 */
@Stateless
public class AddAgreementTimeOfClassificationCommandHandler
		extends CommandHandlerWithResult<AddAgreementTimeOfClassificationCommand, List<String>> {
	
	@Inject
	private AgreementTimeOfClassificationDomainService agreementTimeOfClassificationDomainService;


	@Override
	protected List<String> handle(CommandHandlerContext<AddAgreementTimeOfClassificationCommand> context) {
		AddAgreementTimeOfClassificationCommand command = context.getCommand();
		LoginUserContext login = AppContexts.user();
		String companyId = login.companyId();
		String basicSettingId = IdentifierUtil.randomUniqueId();

		AgreementTimeOfClassification agreementTimeOfClassification = new AgreementTimeOfClassification(companyId,
				basicSettingId, EnumAdaptor.valueOf(command.getLaborSystemAtr(), LaborSystemtAtr.class),
				command.getClassificationCode(),
				new UpperAgreementSetting(new AgreementOneMonthTime(command.getUpperMonth()),
						new AgreementOneMonthTime(command.getUpperMonthAverage())));
		
		BasicAgreementSetting basicAgreementSetting = new BasicAgreementSetting(basicSettingId,
				new AlarmWeek(command.getAlarmWeek()), new ErrorWeek(command.getErrorWeek()), new LimitWeek(command.getLimitWeek()), 
				new AlarmTwoWeeks(command.getAlarmTwoWeeks()), new ErrorTwoWeeks(command.getErrorTwoWeeks()), new LimitTwoWeeks(command.getLimitTwoWeeks()),
				new AlarmFourWeeks(command.getAlarmFourWeeks()), new ErrorFourWeeks(command.getErrorFourWeeks()), new LimitFourWeeks(command.getLimitFourWeeks()),
				new AlarmOneMonth(command.getAlarmOneMonth()), new ErrorOneMonth(command.getErrorOneMonth()), new LimitOneMonth(command.getLimitOneMonth()),
				new AlarmTwoMonths(command.getAlarmTwoMonths()), new ErrorTwoMonths(command.getErrorTwoMonths()), new LimitTwoMonths(command.getLimitTwoMonths()),
				new AlarmThreeMonths(command.getAlarmThreeMonths()), new ErrorThreeMonths(command.getErrorThreeMonths()), new LimitThreeMonths(command.getLimitThreeMonths()),
				new AlarmOneYear(command.getAlarmOneYear()), new ErrorOneYear(command.getErrorOneYear()), new LimitOneYear(command.getLimitOneYear()));
		
		return this.agreementTimeOfClassificationDomainService.add(agreementTimeOfClassification, basicAgreementSetting);
	}

}
