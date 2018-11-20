package nts.uk.ctx.pr.core.app.command.wageprovision.statementbindingsetting;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.wageprovision.statementbindingsetting.*;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class AddOrUpdateStateCorrelationHisCompanyCommandHandler extends CommandHandler<StateCorrelationHisCompanySettingCommand> {

    @Inject
    private StateLinkSettingCompanyRepository stateLinkSettingCompanyRepository;

    @Inject
    private StateCorrelationHisCompanyService stateCorrelationHisCompanyService;

    @Override
    protected void handle(CommandHandlerContext<StateCorrelationHisCompanySettingCommand> commandHandlerContext) {
        String cid = AppContexts.user().companyId();

        StateLinkSettingCompanyCommand stateLinkSettingCompanyCommand = commandHandlerContext.getCommand().getStateLinkSettingCompanyCommand();

        StateCorrelationHisCompanyCommand stateCorrelationHisCompanyCommand = commandHandlerContext.getCommand().getStateCorrelationHisCompanyCommand();

        int mode = commandHandlerContext.getCommand().getMode();
        if(mode == RegisterMode.NEW.value){
            stateLinkSettingCompanyRepository.add(new StateLinkSettingCompany(stateLinkSettingCompanyCommand.getHistoryID(),
                    stateLinkSettingCompanyCommand.getSalaryCode() == null ? null : new StatementCode(stateLinkSettingCompanyCommand.getSalaryCode()),
                    stateLinkSettingCompanyCommand.getBonusCode() == null ? null : new StatementCode(stateLinkSettingCompanyCommand.getBonusCode())));

            stateCorrelationHisCompanyService.addStateCorrelationHisCompany(cid,stateCorrelationHisCompanyCommand.getHistoryID(),new YearMonth(stateCorrelationHisCompanyCommand.getStartYearMonth()),new YearMonth(stateCorrelationHisCompanyCommand.getEndYearMonth()));
        }else if(mode == RegisterMode.UPDATE.value){
            stateLinkSettingCompanyRepository.update(new StateLinkSettingCompany(stateLinkSettingCompanyCommand.getHistoryID(),
                    stateLinkSettingCompanyCommand.getSalaryCode() == null ? null : new StatementCode(stateLinkSettingCompanyCommand.getSalaryCode()),
                    stateLinkSettingCompanyCommand.getBonusCode() == null ? null : new StatementCode(stateLinkSettingCompanyCommand.getBonusCode())));
        }


    }
}

