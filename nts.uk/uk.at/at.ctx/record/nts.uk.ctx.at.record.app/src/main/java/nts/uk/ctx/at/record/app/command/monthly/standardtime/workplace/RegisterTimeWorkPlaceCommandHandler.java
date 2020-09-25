package nts.uk.ctx.at.record.app.command.monthly.standardtime.workplace;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfWorkPlaceDomainService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfWorkPlaceRepository;
import nts.uk.ctx.at.shared.dom.monthlyattdcal.agreementresult.AgreementOneMonthTime;
import nts.uk.ctx.at.shared.dom.monthlyattdcal.agreementresult.AgreementOneYearTime;
import nts.uk.ctx.at.shared.dom.monthlyattdcal.agreementresult.hourspermonth.ErrorTimeInMonth;
import nts.uk.ctx.at.shared.dom.monthlyattdcal.agreementresult.hourspermonth.OneMonthTime;
import nts.uk.ctx.at.shared.dom.monthlyattdcal.agreementresult.hoursperyear.ErrorTimeInYear;
import nts.uk.ctx.at.shared.dom.monthlyattdcal.agreementresult.hoursperyear.OneYearTime;
import nts.uk.ctx.at.shared.dom.standardtime.*;
import nts.uk.ctx.at.shared.dom.standardtime.enums.LaborSystemtAtr;
import nts.uk.ctx.at.shared.dom.standardtime.enums.TimeOverLimitType;
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

        val errorTimeInMonth = new ErrorTimeInMonth(new AgreementOneMonthTime(command.getErrorTimeMonth1())
                , new AgreementOneMonthTime(command.getAlarmTimeMonth1()));
        AgreementOneMonthTime upperLimitTime = new AgreementOneMonthTime(command.getUpperLimitTimeMonth1());

        val basicSettingMonth = new OneMonthTime(errorTimeInMonth, upperLimitTime);

        val errorTimeInMonthUpper = new ErrorTimeInMonth(new AgreementOneMonthTime(command.getErrorTimeMonth2())
                , new AgreementOneMonthTime(command.getAlarmTimeMonth2()));
        val upperLimitTimeMonthUpper = new AgreementOneMonthTime(command.getUpperLimitTimeMonth2());
        val upperLimitDueToSpecialProvisionsMonth = new OneMonthTime(errorTimeInMonthUpper, upperLimitTimeMonthUpper);

        val errorTimeInYear = new ErrorTimeInYear(new AgreementOneYearTime(command.getErrorTimeYear1())
                , new AgreementOneYearTime(command.getAlarmTimeYear1()));
        val upperLimitYear = new AgreementOneYearTime(command.getUpperLimitTimeYear1());
        val basicSettingYear = new OneYearTime(errorTimeInYear, upperLimitYear);

        val errorTimeInYearUpper = new ErrorTimeInYear(new AgreementOneYearTime(command.getErrorTimeYear2())
                , new AgreementOneYearTime(command.getAlarmTimeYear2()));
        val upperLimitTimeYearUpper = new AgreementOneYearTime(command.getUpperLimitTimeYear2());
        val upperLimitDueToSpecialProvisionsYear = new OneYearTime(errorTimeInYearUpper, upperLimitTimeYearUpper);

        BasicAgreementSetting basicAgreementSetting = new BasicAgreementSetting(
                new AgreementsOneMonth(basicSettingMonth, upperLimitDueToSpecialProvisionsMonth),
                new AgreementsOneYear(basicSettingYear, upperLimitDueToSpecialProvisionsYear),
                new AgreementsMultipleMonthsAverage(errorTimeInMonth), EnumAdaptor.valueOf(command.getNumberTimesOverLimitType(), TimeOverLimitType.class));

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
