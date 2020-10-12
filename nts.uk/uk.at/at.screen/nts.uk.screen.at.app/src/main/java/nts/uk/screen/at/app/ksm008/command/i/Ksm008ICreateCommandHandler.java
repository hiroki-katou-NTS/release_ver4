package nts.uk.screen.at.app.ksm008.command.i;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.consecutivework.ConsecutiveNumberOfDays;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.consecutivework.consecutiveworktime.*;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * KSM008I:会社の就業時間帯の連続勤務できる上限日数を新規する
 *
 * @Author Md Rafiqul Islam
 */

@Stateless
public class Ksm008ICreateCommandHandler extends CommandHandler<Ksm008ICreateCommand> {

    @Inject
    private MaxDaysOfContinuousWorkTimeCompanyRepository maxDaysOfContinuousWorkTimeCompanyRepository;

    @Override
    protected void handle(CommandHandlerContext<Ksm008ICreateCommand> context) {
        Ksm008ICreateCommand appCommand = context.getCommand();
        /*存在するか==true?*/
        if (maxDaysOfContinuousWorkTimeCompanyRepository.exists(AppContexts.user().companyId(), new ConsecutiveWorkTimeCode(appCommand.getCode()))) {
            throw new BusinessException("Msg_3");
        } else {
            maxDaysOfContinuousWorkTimeCompanyRepository.insert(AppContexts.user().companyId(), new MaxDaysOfContinuousWorkTimeCompany(
                    new ConsecutiveWorkTimeCode(appCommand.getCode()),
                    new ConsecutiveWorkTimeName(appCommand.getName()),
                    new MaxDaysOfConsecutiveWorkTime(
                            appCommand.getMaxDaysContiWorktime()
                                    .getWorkTimeCodes()
                                    .stream()
                                    .map(item -> new WorkTimeCode(item))
                                    .collect(Collectors.toList()),
                            new ConsecutiveNumberOfDays(appCommand.getMaxDaysContiWorktime().getNumberOfDays())
                    )
            ));
        }
    }
}
