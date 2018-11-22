package nts.uk.ctx.pr.core.app.command.wageprovision.statementbindingsetting;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.YearMonth;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.wageprovision.statementbindingsetting.*;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Transactional
public class RegisterStateCorrelationHisPositionCommandHandler extends CommandHandler<StateCorrelationHisPositionCommand> {
    
    @Inject
    private StateCorrelationHisPositionService stateCorrelationHisPositionService;
    
    @Override
    protected void handle(CommandHandlerContext<StateCorrelationHisPositionCommand> context) {
        StateCorrelationHisPositionCommand command = context.getCommand();
        YearMonth start = new YearMonth(command.getStartYearMonth());
        YearMonth end = new YearMonth(command.getEndYearMonth());
        if(command.getMode() == RegisterMode.NEW.value) {
            String hisId = IdentifierUtil.randomUniqueId();
            List<StateLinkSettingMaster> listStateLinkSettingMaster = command.getStateLinkSettingMaster().stream().map(i -> {
                return new StateLinkSettingMaster(hisId,
                        new MasterCode(i.getMasterCode()),
                        i.getSalaryCode() != null ? new StatementCode(i.getSalaryCode()) : null,
                        i.getBonusCode() != null ? new StatementCode(i.getBonusCode()) : null);
                        }).collect(Collectors.toList());
            StateLinkSettingDate baseDate = new StateLinkSettingDate(hisId, command.getBaseDate());
            stateCorrelationHisPositionService.addHistoryPosition(hisId, start, end, listStateLinkSettingMaster, baseDate);
        } else {
            String hisId = command.getHisId();
            List<StateLinkSettingMaster> listStateLinkSettingMaster = command.getStateLinkSettingMaster().stream().map(i -> {
                return new StateLinkSettingMaster(
                        hisId,
                        new MasterCode(i.getMasterCode()),
                        i.getSalaryCode() != null ? new StatementCode(i.getSalaryCode()) : null,
                        i.getBonusCode() != null ? new StatementCode(i.getBonusCode()) : null);
                        }).collect(Collectors.toList());
            StateLinkSettingDate baseDate = new StateLinkSettingDate(hisId, command.getBaseDate());
            stateCorrelationHisPositionService.updateHistoryPosition(hisId,listStateLinkSettingMaster,start, end,baseDate );
        }
    
    }
}
