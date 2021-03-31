package nts.uk.ctx.at.function.app.command.outputworkstatustable;


import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.tran.AtomTask;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingCode;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingName;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.*;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.SettingClassificationCommon;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Command: 勤務状況表の設定の詳細を更新する
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UpdateSettingDetailCommandHandler extends CommandHandler<UpdateSettingDetailCommand>{


    @Inject
    private WorkStatusOutputSettingsRepository workStatusOutputSettingsRepository;
    @Override
    protected void handle(CommandHandlerContext<UpdateSettingDetailCommand> commandHandlerContext) {
        val command = commandHandlerContext.getCommand();
        val code = new OutputItemSettingCode(command.getCode());
        val name = new OutputItemSettingName(command.getName());
        val settingCategory = EnumAdaptor.valueOf(command.getSettingCategory(), SettingClassificationCommon.class);
        val outputItemList = command.getOutputItemList();
        RequireImpl require = new RequireImpl(workStatusOutputSettingsRepository);
        AtomTask persist = UpdateWorkStatusSettingDomainService
                .updateSetting(require,command.getSettingId(),code,name,settingCategory,outputItemList);
        transaction.execute(persist::run);
    }

    @AllArgsConstructor
    public class RequireImpl implements UpdateWorkStatusSettingDomainService.Require{
        private WorkStatusOutputSettingsRepository workStatusOutputSettingsRepository;
        @Override
        public Optional<WorkStatusOutputSettings> getWorkStatusOutputSettings(String settingId) {
            val rs =  this.workStatusOutputSettingsRepository.getWorkStatusOutputSettings(AppContexts.user().companyId(),settingId);
            if (rs != null) {
                return Optional.of(rs);
            }
            return Optional.empty();
        }

        @Override
        public void update(String settingId, WorkStatusOutputSettings outputSettings) {
            this.workStatusOutputSettingsRepository
                    .update(AppContexts.user().companyId(),settingId,outputSettings);
        }

    }
}
