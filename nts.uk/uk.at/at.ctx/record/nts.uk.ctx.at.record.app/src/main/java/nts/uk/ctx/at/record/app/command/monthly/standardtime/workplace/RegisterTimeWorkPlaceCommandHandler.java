package nts.uk.ctx.at.record.app.command.monthly.standardtime.workplace;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfWorkPlaceDomainService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfWorkPlaceRepository;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.AgreementTimeOfWorkPlace;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.enums.LaborSystemtAtr;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.limitrule.AgreementMultiMonthAvg;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.onemonth.AgreementOneMonthTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.onemonth.OneMonthErrorAlarmTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.onemonth.OneMonthTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.oneyear.AgreementOneYearTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.oneyear.OneYearErrorAlarmTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.oneyear.OneYearTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.timesetting.AgreementOneMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.timesetting.AgreementOneYear;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.timesetting.AgreementOverMaxTimes;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.timesetting.BasicAgreementSetting;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class RegisterTimeWorkPlaceCommandHandler extends CommandHandlerWithResult<RegisterTimeWorkPlaceCommand, List<String>> {

    @Inject
    private AgreementTimeOfWorkPlaceRepository repo;

    @Inject
    private AgreementTimeOfWorkPlaceDomainService agreementTimeOfWorkPlaceDomainService;

    @Override
    protected List<String> handle(CommandHandlerContext<RegisterTimeWorkPlaceCommand> context) {
        RegisterTimeWorkPlaceCommand command = context.getCommand();

        val errorTimeInMonth = OneMonthErrorAlarmTime.of(new AgreementOneMonthTime(command.getErrorTimeMonth1())
                , new AgreementOneMonthTime(command.getAlarmTimeMonth1()));
        AgreementOneMonthTime upperLimitTime = new AgreementOneMonthTime(command.getUpperLimitTimeMonth1());

        val basicSettingMonth = OneMonthTime.of(errorTimeInMonth, upperLimitTime);

        val errorTimeInMonthUpper = OneMonthErrorAlarmTime.of(new AgreementOneMonthTime(command.getErrorTimeMonth2())
                , new AgreementOneMonthTime(command.getAlarmTimeMonth2()));
        val upperLimitTimeMonthUpper = new AgreementOneMonthTime(command.getUpperLimitTimeMonth2());
        val upperLimitDueToSpecialProvisionsMonth = OneMonthTime.of(errorTimeInMonthUpper, upperLimitTimeMonthUpper);

        val errorTimeInYear = OneYearErrorAlarmTime.of(new AgreementOneYearTime(command.getErrorTimeYear1())
                , new AgreementOneYearTime(command.getAlarmTimeYear1()));
        val upperLimitYear = new AgreementOneYearTime(command.getUpperLimitTimeYear1());
        val basicSettingYear = OneYearTime.of(errorTimeInYear, upperLimitYear);

        val errorTimeInYearUpper = OneYearErrorAlarmTime.of(new AgreementOneYearTime(command.getErrorTimeYear2())
                , new AgreementOneYearTime(command.getAlarmTimeYear2()));

        BasicAgreementSetting basicAgreementSetting = new BasicAgreementSetting(
                new AgreementOneMonth(basicSettingMonth, upperLimitDueToSpecialProvisionsMonth),
                new AgreementOneYear(errorTimeInYearUpper,basicSettingYear),
                new AgreementMultiMonthAvg(errorTimeInMonth), EnumAdaptor.valueOf(command.getOverMaxTimes(), AgreementOverMaxTimes.class));

        Optional<AgreementTimeOfWorkPlace> agreementTimeOfWorkPlace = this.repo.findAgreementTimeOfWorkPlace(AppContexts.user().companyId(),
                EnumAdaptor.valueOf(command.getLaborSystemAtr(), LaborSystemtAtr.class));

        if (agreementTimeOfWorkPlace.isPresent()) {
            AgreementTimeOfWorkPlace agreementTimeOfEmployment1 = new AgreementTimeOfWorkPlace(AppContexts.user().companyId(),
                    EnumAdaptor.valueOf(command.getLaborSystemAtr(), LaborSystemtAtr.class), basicAgreementSetting);
            return this.agreementTimeOfWorkPlaceDomainService.update(basicAgreementSetting, agreementTimeOfEmployment1);
        } else {

            AgreementTimeOfWorkPlace agreementTimeOfWorkPlace1 = new AgreementTimeOfWorkPlace(AppContexts.user().companyId(),
                    EnumAdaptor.valueOf(command.getLaborSystemAtr(), LaborSystemtAtr.class), basicAgreementSetting);

            return this.agreementTimeOfWorkPlaceDomainService.add(agreementTimeOfWorkPlace1,basicAgreementSetting);
        }

    }
}
